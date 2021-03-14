package com.example.contatosdeemergncia;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

public class NovoUsuarioActivity extends AppCompatActivity {

    Button btNovo;
    EditText username;
    EditText password;
    Switch swLogado;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novousuario);

        btNovo = findViewById(R.id.salvar);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        swLogado=findViewById(R.id.swLogado);

        btNovo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String senha = password.getText().toString();
                String login = username.getText().toString();
                boolean manterLogado;
                manterLogado= swLogado.isChecked();


                SharedPreferences salvaUser= getSharedPreferences("usuarioPadrao", Activity.MODE_PRIVATE);
                SharedPreferences.Editor escritor= salvaUser.edit();

                escritor.putString("senha",senha);
                escritor.putString("email",login);
                escritor.putBoolean("manterLogado",manterLogado);

                escritor.commit();


                User user =new User(login,senha,manterLogado);


                Intent intent=new Intent(NovoUsuarioActivity.this, MainActivity.class);
                intent.putExtra("usuario",user);
                startActivity(intent);

                finish();
            }
        });


    }
}
