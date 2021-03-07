package model;

public class Usuario {

    private String email, telefone, nome;

    public Usuario(String email, String telefone, String nome) {
        this.email = email;
        this.telefone = telefone;
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public String getNome() {
        return nome;
    }
}
