package model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Tarifa implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private double valor15;
    private double valor30;
    private double valorKmAdicional;
    private String tipoVeiculo;

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

    public double getValorKmAdicional() {
        return valorKmAdicional;
    }

    public void setValorKmAdicional(double valorKmAdicional) {
        this.valorKmAdicional = valorKmAdicional;
    }

    public String getTipoVeiculo() {
        return tipoVeiculo;
    }

    public void setTipoVeiculo(String tipoVeiculo) {
        this.tipoVeiculo = tipoVeiculo;
    }
}
