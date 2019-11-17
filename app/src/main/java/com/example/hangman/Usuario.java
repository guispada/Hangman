package com.example.hangman;

import android.text.Editable;

import java.io.Serializable;

public class Usuario implements Serializable {

    private Integer id;
    private String usuario;
    private String idade;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getIdade() {
        return idade;
    }

    public void setIdade(String idade) {
        this.idade = idade;
    }

    @Override
    public String toString(){
        return usuario;
    }

}
