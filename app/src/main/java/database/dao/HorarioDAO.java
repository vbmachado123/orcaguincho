package database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import model.Horario;

@Dao
public interface HorarioDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long salva(Horario horario);

    @Update
    void atualiza(Horario horario);

    @Query("SELECT * FROM Horario")
    List<Horario> buscaTodos();

    @Query("SELECT * FROM Horario WHERE idTarifa = :id")
    Horario buscaPorTarifa(long id);

    @Delete
    void remove(Horario horario);
}
