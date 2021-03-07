package database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import database.dao.DiaDAO;
import database.dao.HorarioDAO;
import database.dao.TarifaDAO;
import model.Dia;
import model.Horario;
import model.Tarifa;

@Database(entities = {Dia.class, Horario.class, Tarifa.class}, version = 1, exportSchema = false)
public abstract class ValoraDatabase extends RoomDatabase {

    private static final String NOME_BANCO = "Valora.db";

    public abstract DiaDAO getDiaDAO();
    public abstract HorarioDAO getHorarioDAO();
    public abstract TarifaDAO gerTarifaDAO();

    public static ValoraDatabase getInstance(Context context) {
        return Room.databaseBuilder(context, ValoraDatabase.class, NOME_BANCO).build();
    }
}
