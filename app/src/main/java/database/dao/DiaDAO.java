package database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import model.Dia;

@Dao
public interface DiaDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long salva(Dia dia);

    @Update
    void atualiza(Dia dia);

    @Query("SELECT * FROM Dia")
    List<Dia> buscaTodos();

    @Query("SELECT * FROM Dia WHERE idTarifa = :id")
    List<Dia> buscaPorTarifa(long id);

    @Query("SELECT * FROM Dia WHERE dia = :dia")
    Dia buscaPorDia(int dia);

    @Delete
    void remove(Dia dia);
}
