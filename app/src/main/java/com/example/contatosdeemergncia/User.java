package com.example.contatosdeemergncia;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    String nome;
    String login;
    String senha;
    String email;
    boolean manterLogado = false;
    boolean tema_escuro = false;
    ArrayList<Contato> contatos;


    public boolean isTema_escuro() {
        return tema_escuro;
    }

    public void setTema_escuro(boolean tema_escuro) {
        this.tema_escuro = tema_escuro;
    }


    public User(String login, String password, boolean manterLogado) {
        this.login = login;
        this.senha = password;
        this.manterLogado = manterLogado;
        this.contatos = new ArrayList<Contato>();
    }

    public User() {
        this.contatos = new ArrayList<Contato>();
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public boolean isManterLogado() {
        return manterLogado;
    }

    public void setManterLogado(boolean manterLogado) {
        this.manterLogado = manterLogado;
    }

    public ArrayList<Contato> getContatos() {
        return contatos;
    }

    public void setContatos(ArrayList<Contato> contatos) {
        this.contatos = contatos;
    }
}