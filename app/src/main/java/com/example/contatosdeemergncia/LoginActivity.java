package com.example.contatosdeemergncia;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    Button btLogar;
    Button btNovo;
    EditText username;
    EditText password;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(montarObjetoUserSemLogar()){
            User user = montarObjetoUser();

            preencherListaDeContatos(user);

            //Abrir a atividade de Lista de Contatos
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("usuario",user);
            startActivity(intent);
            finish();



        }else { //Checar Usuário e Senha ou clicar em criar novo
            btLogar = findViewById(R.id.login);
            btNovo = findViewById(R.id.novo);
            username = findViewById(R.id.username);
            password = findViewById(R.id.password);

            btLogar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    SharedPreferences temUser = getSharedPreferences("usuarioPadrao", Activity.MODE_PRIVATE);
                    String loginSalvo = temUser.getString("login", "");
                    String senhaSalva = temUser.getString("senha", "");

                    if ((loginSalvo != null) && (senhaSalva != null)) {

                        String senha = password.getText().toString();
                        String login = username.getText().toString();

                        if ((loginSalvo.compareTo(login) == 0)
                                && (senhaSalva.compareTo(senha) == 0)) {

                            User user = montarObjetoUser();
                            preencherListaDeContatos(user);
                            //Abrindo a Lista de Contatos
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("usuario", user);
                            startActivity(intent);


                        } else {
                            Toast.makeText(LoginActivity.this, "Login e Senha Incorretos", Toast.LENGTH_LONG).show();

                        }

                    } else {
                        Toast.makeText(LoginActivity.this, "Login e Senha nulos", Toast.LENGTH_LONG).show();

                    }

                }
            });

            //Novo Usuário
            btNovo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(LoginActivity.this, NovoUsuario_Activity.class);
                    startActivity(intent);
                }
            });

        }
    }

    private User montarObjetoUser() {
        User user = null;
        SharedPreferences temUser= getSharedPreferences("usuarioPadrao", Activity.MODE_PRIVATE);
        String loginSalvo = temUser.getString("login","");
        String senhaSalva = temUser.getString("senha","");
        boolean manterLogado=temUser.getBoolean("manterLogado",false);


        user=new User(loginSalvo,senhaSalva,manterLogado);
        return user;
    }

    private boolean montarObjetoUserSemLogar() {
        SharedPreferences temUser= getSharedPreferences("usuarioPadrao", Activity.MODE_PRIVATE);
        boolean manterLogado = temUser.getBoolean("manterLogado",false);
        return manterLogado;
    }

    protected void preencherListaDeContatos(User user) {

        SharedPreferences recuperarContatos = getSharedPreferences("contatos", Activity.MODE_PRIVATE);

        int num = recuperarContatos.getInt("numContatos", 0);
        ArrayList<Contato> contatos = new ArrayList<Contato>();

        Contato contato;


        for (int i = 1; i <= num; i++) {
            String objSel = recuperarContatos.getString("contato" + i, "");
            if (objSel.compareTo("") != 0) {
                try {
                    ByteArrayInputStream bis =
                            new ByteArrayInputStream(objSel.getBytes(StandardCharsets.ISO_8859_1.name()));
                    ObjectInputStream oos = new ObjectInputStream(bis);
                    contato = (Contato) oos.readObject();

                    if (contato != null) {
                        contatos.add(contato);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


        }
        user.setContatos(contatos);
    }
}