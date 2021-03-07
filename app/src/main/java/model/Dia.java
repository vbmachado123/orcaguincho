package model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import static androidx.room.ForeignKey.CASCADE;

@Entity
public class Dia implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ForeignKey(entity = Tarifa.class,
            parentColumns = "id",
            childColumns = "idTarifa",
            onUpdate = CASCADE,
            onDelete = CASCADE)
    private int idTarifa;
    private int dia;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdTarifa() {
        return idTarifa;
    }

    public void setIdTarifa(int idTarifa) {
        this.idTarifa = idTarifa;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }
}
