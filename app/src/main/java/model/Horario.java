package model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import static androidx.room.ForeignKey.CASCADE;

@Entity
public class Horario implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ForeignKey(entity = Tarifa.class,
            parentColumns = "id",
            childColumns = "idTarifa",
            onUpdate = CASCADE,
            onDelete = CASCADE)
    private int idTarifa;
    private int horaInicial;
    private int horaFinal;

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

    public int getHoraInicial() {
        return horaInicial;
    }

    public void setHoraInicial(int horaInicial) {
        this.horaInicial = horaInicial;
    }

    public int getHoraFinal() {
        return horaFinal;
    }

    public void setHoraFinal(int horaFinal) {
        this.horaFinal = horaFinal;
    }
}
