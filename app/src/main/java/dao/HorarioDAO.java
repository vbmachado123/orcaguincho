package dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import model.Horario;
import sql.Conexao;

public class HorarioDAO {

    private Conexao conexao;
    private SQLiteDatabase banco;
    private Horario horario;

    public HorarioDAO(Context context){
        conexao = new Conexao(context);
        banco = conexao.getWritableDatabase();
        banco = conexao.getReadableDatabase();
    }

    public Horario recupera(){
        Cursor cursor = banco.rawQuery("SELECT * FROM horario", null);
        // cursor.moveToFirst();
        while (cursor.moveToNext()){
            horario = new Horario();
            horario.setId(cursor.getInt(0));
            horario.setIdTarifa(cursor.getInt(1));
            horario.setHoraInicial(cursor.getInt(2));
            horario.setHoraFinal(cursor.getInt(3));

        }
        return horario;
    }

    public Horario getById(int id){
        Cursor cursor = banco.rawQuery("SELECT * FROM horario WHERE id =" + id, null);

        if(cursor.moveToFirst()){
            horario = new Horario();
            horario.setId(cursor.getInt(0));
            horario.setIdTarifa(cursor.getInt(1));
            horario.setHoraInicial(cursor.getInt(2));
            horario.setHoraFinal(cursor.getInt(3));
        }
        return horario;
    }

    public List<Horario> getByTarifa(int idTarifa){
        List<Horario> horarios = new ArrayList<>();
        Cursor cursor = banco.query("horario", new String[]{"id", "idTarifa", "horarioInicial", "horarioFinal"},
                null, null, null, null, null);

        while (cursor.moveToNext()){
            if(cursor.getInt(1) == idTarifa) {
                horario = new Horario();
                horario.setId(cursor.getInt(0));
                horario.setIdTarifa(cursor.getInt(1));
                horario.setHoraInicial(cursor.getInt(2));
                horario.setHoraFinal(cursor.getInt(3));
                horarios.add(horario);
            }
        }

        return horarios;
    }

    public long inserir(Horario horario){
        ContentValues values = new ContentValues();
        values.put("idTarifa", horario.getIdTarifa());
        values.put("horarioInicial", horario.getHoraInicial());
        values.put("horarioFinal", horario.getHoraFinal());
        return banco.insert("horario", null, values);
    }

    public void atualizar(Horario horario){
        ContentValues values = new ContentValues();
        values.put("idTarifa", horario.getIdTarifa());
        values.put("horarioInicial", horario.getHoraInicial());
        values.put("horarioFinal", horario.getHoraFinal());

        banco.update("horario", values, "id = ?",
                new String[]{String.valueOf(horario.getId())});
    }

}
