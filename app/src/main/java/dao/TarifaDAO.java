package dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import model.Tarifa;
import sql.Conexao;

public class TarifaDAO {

    private Conexao conexao;
    private SQLiteDatabase banco;
    private Tarifa tarifa;

    public TarifaDAO(Context context){
        conexao = new Conexao(context);
        banco = conexao.getWritableDatabase();
        banco = conexao.getReadableDatabase();
    }

    public Tarifa recupera(){
        Cursor cursor = banco.rawQuery("SELECT * FROM tarifa", null);
        // cursor.moveToFirst();
        while (cursor.moveToNext()){
            tarifa = new Tarifa();
            tarifa.setId(cursor.getInt(0));
            tarifa.setValor15(cursor.getInt(1));
            tarifa.setValor30(cursor.getInt(2));
            tarifa.setValor31(cursor.getInt(3));

        }
        return tarifa;
    }

    public Tarifa getById(int id){
        Cursor cursor = banco.rawQuery("SELECT * FROM tarifa WHERE id =" + id, null);

        if(cursor.moveToFirst()){
            tarifa = new Tarifa();
            tarifa.setId(cursor.getInt(0));
            tarifa.setValor15(cursor.getInt(1));
            tarifa.setValor30(cursor.getInt(2));
            tarifa.setValor31(cursor.getInt(3));
        }
        return tarifa;
    }

    public long inserir(Tarifa tarifa){
        ContentValues values = new ContentValues();
        //values.put("tarifa", tarifa.get());
        values.put("valor15", tarifa.getValor15());
        values.put("valor30", tarifa.getValor30());
        values.put("valor31", tarifa.getValor31());
        return banco.insert("tarifa", null, values);
    }

    public void atualizar(Tarifa tarifa){

        ContentValues values = new ContentValues();
        //values.put("tarifa", tarifa.get());
        values.put("valor15", tarifa.getValor15());
        values.put("valor30", tarifa.getValor30());
        values.put("valor31", tarifa.getValor31());

        banco.update("tarifa", values, "id = ?",
                new String[]{String.valueOf(tarifa.getId())});
    }
}
