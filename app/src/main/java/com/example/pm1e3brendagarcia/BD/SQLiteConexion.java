package com.example.pm1e3brendagarcia.BD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class SQLiteConexion extends SQLiteOpenHelper {


    public SQLiteConexion(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Transacciones.CreateTableMedicamentos);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(Transacciones.DropeTableMedicamentos);
        onCreate(db);
    }

    public Cursor getAll() {
        return(getReadableDatabase().rawQuery("SELECT * FROM medicamentos",null));
    }
    public void insert(String descripcion, int cantidad, String tiempo, int periocidad,byte[] imagen)
    {
        ContentValues cv = new ContentValues();

        cv.put(Transacciones.descripcion,descripcion);
        cv.put(Transacciones.cantidad,cantidad);
        cv.put(Transacciones.tiempo,tiempo);
        cv.put(Transacciones.periocidad,periocidad);
        cv.put(Transacciones.imagen,imagen);

        Log.e("inserted", "inserted");
        getWritableDatabase().insert(Transacciones.tablaMedicamentos,Transacciones.idMedicamento,cv);

    }
    public byte[] getImage(Cursor c)
    {
        return(c.getBlob(1));
    }
}
