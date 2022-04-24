package com.example.pm1e3brendagarcia.vista;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pm1e3brendagarcia.BD.SQLiteConexion;
import com.example.pm1e3brendagarcia.BD.Transacciones;
import com.example.pm1e3brendagarcia.R;
import com.example.pm1e3brendagarcia.controlador.AdaptadorSpinner;
import com.example.pm1e3brendagarcia.controlador.MedicamentoAdapter;
import com.example.pm1e3brendagarcia.modelo.Medicamento;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class ActivityModificar extends AppCompatActivity {

    private EditText txtEditDescripcion, txtEditCantidad, txtEditPeriodo,txtEditcodigo;
    private Spinner spEditTiempo;
    private Button btnActualizar, btnEditFoto;
    private ImageView Editfoto;

    private MedicamentoAdapter medicamentoAdapter;
    private Medicamento medicamento;
    private AdaptadorSpinner adapter;
    private String[] arraycontenido;
    private ArrayList<Medicamento> medicamentoArrayList;
    byte[] byteArray;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int PETICION_ACCESO_CAM = 100;

    SQLiteConexion db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar);

        Editfoto = (ImageView) findViewById(R.id.foto);
        txtEditcodigo = (EditText) findViewById(R.id.txtEditcodigo);
        txtEditDescripcion = (EditText) findViewById(R.id.txtEditDescripcion);
        txtEditCantidad = (EditText) findViewById(R.id.txtEditCantidad);
        txtEditPeriodo = (EditText) findViewById(R.id.txtEditPeriodo);
        spEditTiempo = (Spinner) findViewById(R.id.spEditTiempo);
        btnActualizar = (Button) findViewById(R.id.btnActualizar);
        btnEditFoto = (Button) findViewById(R.id.btnEditFoto);

        arraycontenido = new String[]{"Horas", "Diaria"};
        adapter = new AdaptadorSpinner(this, arraycontenido);
        spEditTiempo.setAdapter(adapter);

        db = new SQLiteConexion(getApplicationContext(), Transacciones.NameDataBase, null, 1);

        btnEditFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Permisos();
            }
        });

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editarDatos(view);
            }
        });

        spEditTiempo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (spEditTiempo.getSelectedItem() == "Horas"){
                    txtEditPeriodo.setEnabled(true);
                }else{
                    txtEditPeriodo.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        medicamento=(Medicamento)getIntent().getSerializableExtra("medicamento");
        llenarDatos();
    }

    private void llenarDatos(){

        byte[]image=medicamento.getImagen();
        Bitmap bitmap= BitmapFactory.decodeByteArray(image,0,image.length);

        //Editfoto.setImageBitmap(bitmap);
        txtEditcodigo.setText(String.valueOf(medicamento.getId()));
        txtEditDescripcion.setText(String.valueOf(medicamento.getDescripcion()));
        txtEditCantidad.setText(String.valueOf(medicamento.getCantidad()));
        txtEditPeriodo.setText(String.valueOf(medicamento.getPeriocidad()));

    }

    private void editarDatos(View v){
        SQLiteConexion sqLiteConexion = new SQLiteConexion(this,"medicamento",null,1);

        SQLiteDatabase sqLiteDatabase = sqLiteConexion.getWritableDatabase();

        int id = Integer.parseInt(txtEditcodigo.getText().toString());
        String descripcion = txtEditDescripcion.getText().toString();
        int cantidad = Integer.parseInt(txtEditCantidad.getText().toString());
        String hora = spEditTiempo.getSelectedItem().toString();
        int periodo = Integer.parseInt(txtEditPeriodo.getText().toString());

        ContentValues values = new ContentValues();
        values.put("descripcion",descripcion);
        values.put("cantidad",cantidad);
        values.put("tiempo",hora);
        values.put("periocidad",periodo);
        values.put("imagen",byteArray);

        sqLiteDatabase.update("medicamentos",values,"idMedicamento="+id,null);
        sqLiteDatabase.close();
        finish();

        Toast.makeText(getApplicationContext(),"Registro actualizado con exito",Toast.LENGTH_LONG).show();

        Intent intent = new Intent(ActivityModificar.this,ActivityMostrar.class);
        startActivity(intent);
    }

    private void Permisos() {
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ActivityModificar.this, new String[]{ Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, PETICION_ACCESO_CAM);
        }else{
            tomarFoto();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PETICION_ACCESO_CAM){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                tomarFoto();
            }
        }else{
            Toast.makeText(getApplicationContext(), "Se necesitan permisos de acceso", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            getBytes(data);
        }
    }

    private void getBytes(Intent data){
        Bitmap photo = (Bitmap) data.getExtras().get("data");
        Editfoto.setImageBitmap(photo);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byteArray = stream.toByteArray();
    }

    private void tomarFoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
    }
}