package com.example.contatosdeemergncia;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PerfilActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    EditText edUser;
    EditText edPass;
    Switch swLogado;

    Button btModificar;
    BottomNavigationView bnv;

    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        btModificar=findViewById(R.id.salvar);
        bnv=findViewById(R.id.bnv);
        bnv.setOnNavigationItemSelectedListener(this);
        bnv.setSelectedItemId(R.id.tabPerfil);

        edUser=findViewById(R.id.username);
        edPass=findViewById(R.id.password);
        swLogado=findViewById(R.id.swLogado);

        Intent quemChamou = this.getIntent();
        if (quemChamou != null) {
            Bundle params = quemChamou.getExtras();
            if (params != null) {
                //Recuperando o Usuario
                user = (User) params.getSerializable("usuario");
                setTitle("Alterar dados do usuário");

            }
        }
        if (user != null) {
            edUser.setText(user.getLogin());
            edPass.setText(user.getSenha());
            swLogado.setChecked(user.isManterLogado());
        }

        btModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                user.setLogin(edUser.getText().toString());
                user.setSenha(edPass.getText().toString());
                user.setManterLogado(swLogado.isChecked());
                salvarModificacoes(user);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Checagem de o Item selecionado é a de mudanças de contatos
        if (item.getItemId() == R.id.tabContatos) {
            //Abertura da Tela de Perfil
            Intent intent = new Intent(this, AlterarContatosActivity.class);
            intent.putExtra("usuario", user);
            startActivity(intent);

        }
        // Checagem de o Item selecionado é Ligar
        if (item.getItemId() == R.id.tabLigar) {
            //Abertura da Tela Mudar COntatos
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("usuario", user);
            startActivity(intent);

        }
        return true;
    }

    public void salvarModificacoes(User user){
        SharedPreferences salvaUser= getSharedPreferences("usuarioPadrao", Activity.MODE_PRIVATE);
        SharedPreferences.Editor escritor= salvaUser.edit();

        escritor.putString("senha",user.getSenha());
        escritor.putString("login",user.getLogin());
        escritor.putBoolean("manterLogado",user.isManterLogado());


        //Falta Salvar o E-mail

        escritor.commit(); //Salva em Disco

        Toast.makeText(PerfilActivity.this,"Modificações Salvas",Toast.LENGTH_LONG).show() ;

        finish();
    }
}
