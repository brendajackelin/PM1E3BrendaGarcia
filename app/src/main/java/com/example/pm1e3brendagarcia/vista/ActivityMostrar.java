package com.example.pm1e3brendagarcia.vista;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.pm1e3brendagarcia.BD.SQLiteConexion;
import com.example.pm1e3brendagarcia.BD.Transacciones;
import com.example.pm1e3brendagarcia.R;
import com.example.pm1e3brendagarcia.controlador.AuxiliarMedicamento;
import com.example.pm1e3brendagarcia.controlador.MedicamentoAdapter;
import com.example.pm1e3brendagarcia.modelo.Medicamento;

import java.util.ArrayList;

public class ActivityMostrar extends AppCompatActivity implements AuxiliarMedicamento {

    RecyclerView idrecyclerview;
    ArrayList<Medicamento> medicamentoArrayList;
    SQLiteConexion db;

    private MedicamentoAdapter medicamentoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar);
        idrecyclerview=findViewById(R.id.idrecyclerview);

        medicamentoArrayList = new ArrayList<>();

        db = new SQLiteConexion(getApplicationContext(), Transacciones.NameDataBase, null, 1);

        medicamentoAdapter = new MedicamentoAdapter(this,medicamentoArrayList);

        RecyclerView recyclerView = findViewById(R.id.idrecyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));
        recyclerView.setAdapter(medicamentoAdapter);
        MostrarDatos();

    }

    public void MostrarDatos(){
        SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();
        Medicamento medicamento = null;

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM medicamentos",null);
        while (cursor.moveToNext()){
            medicamento=new Medicamento();
            medicamento.setId(cursor.getInt(0));
            medicamento.setDescripcion(cursor.getString(1));
            medicamento.setCantidad(cursor.getInt(2));
            medicamento.setHoras(cursor.getString(3));
            medicamento.setPeriocidad(cursor.getInt(4));
            medicamento.setImagen(cursor.getBlob(5));

            medicamentoAdapter.agregarMedicamento(medicamento);
        }

    }

    @Override
    public void OpcionEditar(Medicamento medicamento) {
        Intent intent = new Intent(ActivityMostrar.this,ActivityModificar.class);
        intent.putExtra("medicamento",medicamento);
        startActivity(intent);
    }
}