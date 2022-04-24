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
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pm1e3brendagarcia.BD.SQLiteConexion;
import com.example.pm1e3brendagarcia.BD.Transacciones;
import com.example.pm1e3brendagarcia.R;
import com.example.pm1e3brendagarcia.controlador.AdaptadorSpinner;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int PETICION_ACCESO_CAM = 100;

    EditText txtDescripcion, txtCantidad,txtPeriodo;
    Button btnGuardar, btnMedicamentos, btnFoto;
    ImageView foto;
    SQLiteConexion db;
    byte[] byteArray;
    Boolean retorno;

    private Spinner spTiempo;
    private String[] arraycontenido;
    private AdaptadorSpinner adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        foto = (ImageView) findViewById(R.id.foto);
        txtDescripcion = (EditText) findViewById(R.id.txtDescripcion);
        txtCantidad = (EditText) findViewById(R.id.txtCantidad);
        txtPeriodo = (EditText) findViewById(R.id.txtPeriodo);
        spTiempo = (Spinner) findViewById(R.id.spTiempo);
        btnGuardar = (Button) findViewById(R.id.btnGuardar);
        btnMedicamentos = (Button) findViewById(R.id.btnMedicamentos);
        btnFoto = (Button) findViewById(R.id.btnFoto);

        arraycontenido = new String[]{"Horas", "Diaria"};
        adapter = new AdaptadorSpinner(this, arraycontenido);
        spTiempo.setAdapter(adapter);

        db = new SQLiteConexion(getApplicationContext(), Transacciones.NameDataBase, null, 1);
        byteArray = new byte[0];

        spTiempo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (spTiempo.getSelectedItem() == "Horas"){
                    txtPeriodo.setEnabled(true);
                }else{
                    txtPeriodo.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Permisos();
            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Validar();

                if(byteArray.length != 0) {
                    Guardar();
                }else{
                    Toast.makeText(getApplicationContext(), "No se ha tomado ninguna fotografia", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnMedicamentos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ActivityMostrar.class);
                startActivity(intent);
            }
        });

    }

    public boolean Validar(){
        retorno= true;

        String desc = txtDescripcion.getText().toString();
        String cant= txtCantidad.getText().toString();

        if(desc.isEmpty()){
            txtDescripcion.setError("DEBE INGRESAR LA DESCRIPCION");
            retorno = false;
        }
        if(cant.isEmpty()){
            txtCantidad.setError("DEBE INGRESAR LA CANTIDAD");
            retorno = false;
        }

        return retorno;
    }

    private void Clean(){
        byteArray = new byte[0];
        foto.setImageResource(R.drawable.medicamento);
        txtDescripcion.setText("");
        txtCantidad.setText("");
        txtPeriodo.setText("");
    }

    private void Guardar() {
        SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.NameDataBase, null, 1);
        SQLiteDatabase db = conexion.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(Transacciones.descripcion, txtDescripcion.getText().toString());
        valores.put(Transacciones.cantidad, txtCantidad.getText().toString());
        valores.put(Transacciones.tiempo, spTiempo.getSelectedItem().toString());
        valores.put(Transacciones.periocidad, txtPeriodo.getText().toString());
        valores.put(Transacciones.imagen,byteArray);

        Long resultado = db.insert(Transacciones.tablaMedicamentos,Transacciones.idMedicamento, valores);

        Toast.makeText(getApplicationContext(),"Registro ingresado con exito",Toast.LENGTH_LONG).show();

        db.close();

        Clean();
    }

    private void Permisos() {
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{ Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, PETICION_ACCESO_CAM);
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
        foto.setImageBitmap(photo);
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