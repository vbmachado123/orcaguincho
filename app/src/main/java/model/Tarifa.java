package model;

import java.io.Serializable;

public class Tarifa implements Serializable {

    private int id;
    private double valor15;
    private double valor30;
    private double valor31;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getValor15() {
        return valor15;
    }

    public void setValor15(double valor15) {
        this.valor15 = valor15;
    }

    public double getValor30() {
        return valor30;
    }

    public void setValor30(double valor30) {
        this.valor30 = valor30;
    }

    public double getValor31() {
        return valor31;
    }

    public void setValor31(double valor31) {
        this.valor31 = valor31;
    }
}
