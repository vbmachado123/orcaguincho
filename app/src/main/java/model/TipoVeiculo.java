package model;

public class TipoVeiculo {
    private int id;
    private String tipoVeiculo;

    public TipoVeiculo(int id, String tipoVeiculo) {
        this.id = id;
        this.tipoVeiculo = tipoVeiculo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipoVeiculo() {
        return tipoVeiculo;
    }

    public void setTipoVeiculo(String tipoVeiculo) {
        this.tipoVeiculo = tipoVeiculo;
    }
}
