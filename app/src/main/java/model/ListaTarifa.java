package model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

public class ListaTarifa implements Serializable {

    public String tipoVeiculo;
    private int id;
    private String diasSemana;
    private String horario;
    private String valor15;
    private String valor30;
    private String kmAdicional;
    private Long idTarifa;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDiasSemana() {
        return diasSemana;
    }

    public void setDiasSemana(String diasSemana) {
        this.diasSemana = diasSemana;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getValor15() {
        return valor15;
    }

    public void setValor15(String valor15) {
        this.valor15 = valor15;
    }

    public String getValor30() {
        return valor30;
    }

    public void setValor30(String valor30) {
        this.valor30 = valor30;
    }

    public String getKmAdicional() {
        return kmAdicional;
    }

    public void setKmAdicional(String kmAdicional) {
        this.kmAdicional = kmAdicional;
    }

    public Long getIdTarifa() {
        return idTarifa;
    }

    public void setIdTarifa(Long idTarifa) {
        this.idTarifa = idTarifa;
    }

    public String getTipoVeiculo() {
        return tipoVeiculo;
    }

    public void setTipoVeiculo(String tipoVeiculo) {
        this.tipoVeiculo = tipoVeiculo;
    }
}
