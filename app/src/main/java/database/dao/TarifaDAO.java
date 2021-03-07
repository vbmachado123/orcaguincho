package database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import model.Tarifa;

@Dao
public interface TarifaDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long salva(Tarifa tarifa);

    @Update
    void atualiza(Tarifa tarifa);

    @Query("SELECT * FROM Tarifa")
    List<Tarifa> buscaTodos();

    @Delete
    void remove(Tarifa tarifa);

    @Query("SELECT * FROM Tarifa WHERE id = :id")
    Tarifa buscaPorId(long id);

}
