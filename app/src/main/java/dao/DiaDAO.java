package dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import model.Dia;
import model.Horario;
import model.Tarifa;
import sql.Conexao;

public class DiaDAO {

    private Conexao conexao;
    private SQLiteDatabase banco;
    private Dia dia;

    public DiaDAO(Context context){
        conexao = new Conexao(context);
        banco = conexao.getWritableDatabase();
        banco = conexao.getReadableDatabase();
    }

    public Dia recupera(){
        Cursor cursor = banco.rawQuery("SELECT * FROM dia", null);
        // cursor.moveToFirst();
        while (cursor.moveToNext()){
            dia = new Dia();
            dia.setId(cursor.getInt(0));
            dia.setIdTarifa(cursor.getInt(1));
            dia.setDia(cursor.getInt(2));

        }
        return dia;
    }

    public List<Dia> obterTodos(){
        List<Dia> dias = new ArrayList<>();
        Cursor cursor = banco.query("dia", new String[]{"id", "idTarifa", "dia"},
                null, null, null, null, null);

        cursor.moveToFirst();

        while (cursor.moveToNext()){
            dia = new Dia();
            dia.setId(cursor.getInt(0));
            dia.setIdTarifa(cursor.getInt(1));
            dia.setDia(cursor.getInt(2));
            dias.add(dia);
        }
        return dias;
    }

    public Dia getById(int id){
        Cursor cursor = banco.rawQuery("SELECT * FROM dia WHERE id =" + id, null);

        if(cursor.moveToFirst()){
            dia = new Dia();
            dia.setId(cursor.getInt(0));
            dia.setIdTarifa(cursor.getInt(1));
            dia.setDia(cursor.getInt(2));
        }
        return dia;
    }

    public Dia getByDia(int day){
        Cursor cursor = banco.rawQuery("SELECT * FROM dia WHERE dia =" + day, null);

        if(cursor.moveToFirst()){
            dia = new Dia();
            dia.setId(cursor.getInt(0));
            dia.setIdTarifa(cursor.getInt(1));
            dia.setDia(cursor.getInt(2));
        }
        return dia;
    }

    public List<Dia> getByTarifa(int idTarifa){
        List<Dia> dias = new ArrayList<>();
        Cursor cursor = banco.query("dia", new String[]{"id", "idTarifa", "dia"},
                null, null, null, null, null);

        while (cursor.moveToNext()){
            if(cursor.getInt(1) == idTarifa) {
                dia = new Dia();
                dia.setId(cursor.getInt(0));
                dia.setIdTarifa(cursor.getInt(1));
                dia.setDia(cursor.getInt(2));
                dias.add(dia);
            }
        }

        return dias;
    }

    public long inserir(Dia dia){
        ContentValues values = new ContentValues();
        values.put("idTarifa", dia.getIdTarifa());
        values.put("dia", dia.getDia());
        return banco.insert("dia", null, values);
    }

    public void atualizar(Dia dia){
        ContentValues values = new ContentValues();
        values.put("idTarifa", dia.getIdTarifa());
        values.put("dia", dia.getDia());

        banco.update("dia", values, "id = ?",
                new String[]{String.valueOf(dia.getId())});
    }
}
