package sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Conexao extends SQLiteOpenHelper {

    private static final String name = "banco.db";
    private static final int version = 1;

    private String tabelaTarifa = "CREATE TABLE IF NOT EXISTS tarifa(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "valor15 INTEGER, valor30 INTEGER, valor31 INTEGER)";
    private String tabelaDia = "CREATE TABLE IF NOT EXISTS dia(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "idTarifa INTEGER, dia INTEGER)";
    private String tabelaHorario = "CREATE TABLE IF NOT EXISTS horario(id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "idTarifa INTEGER, horarioInicial INTEGER, horarioFinal INTEGER)";

    public Conexao(Context context){
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(tabelaTarifa);
        db.execSQL(tabelaDia);
        db.execSQL(tabelaHorario);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(tabelaTarifa);
        db.execSQL(tabelaDia);
        db.execSQL(tabelaHorario);
    }
}


