package model;

import java.io.Serializable;

public class Horario implements Serializable {

    private int id;
    private int idTarifa;
    private double horaInicial;
    private double horaFinal;

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

    public double getHoraInicial() {
        return horaInicial;
    }

    public void setHoraInicial(double horaInicial) {
        this.horaInicial = horaInicial;
    }

    public double getHoraFinal() {
        return horaFinal;
    }

    public void setHoraFinal(double horaFinal) {
        this.horaFinal = horaFinal;
    }
}
