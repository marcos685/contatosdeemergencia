package com.example.contatosdeemergncia;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bnv;
    User user;
    ListView lista;
    String numeroCall;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bnv = findViewById(R.id.bnv);
        bnv.setOnNavigationItemSelectedListener(this);
        bnv.setSelectedItemId(R.id.tabLigar);

        lista= findViewById(R.id.listView1);

        //Dados da Intent Anterior
        Intent quemChamou = this.getIntent();
        if (quemChamou != null) {
            Bundle params = quemChamou.getExtras();
            if (params != null) {
                user = (User) params.getSerializable("usuario");
            }
        }

    }

    protected void preencherListView(User user) {

        final ArrayList<Contato> contatos = user.getContatos();

        if (contatos != null) {
            final String[] nomesSP;
            nomesSP = new String[contatos.size()];
            Contato c;
            for (int j = 0; j < contatos.size(); j++) {
                nomesSP[j] = contatos.get(j).getNome();
            }

            ArrayAdapter<String> adaptador;

            adaptador = new ArrayAdapter<String>(this,  R.layout.list_view_layout, nomesSP);

            lista.setAdapter(adaptador);


            lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    if (checarPermissaoPhone(contatos.get(i).getNumero())) {

                        Uri uri = Uri.parse(contatos.get(i).getNumero());
                        //   Intent itLigar = new Intent(Intent.ACTION_DIAL, uri);
                        Intent itLigar = new Intent(Intent.ACTION_CALL, uri);
                        startActivity(itLigar);
                    }


                }
            });
        }
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.v("PDM", "item: "+item.toString());
        if (item.getItemId() == R.id.tabPerfil) {
            Intent intent = new Intent(this, PerfilActivity.class);
            intent.putExtra("usuario", user);
            Log.v("PDM", "morremo");
            startActivityForResult(intent, 1111);
        }

        if (item.getItemId() == R.id.tabContatos) {
            Intent intent = new Intent(this, AlterarContatosActivity.class);
            intent.putExtra("usuario", user);
            Log.v("PDM", "morremo");
            startActivityForResult(intent, 1112);
        }
        return true;
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case 2222:
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "VALEU", Toast.LENGTH_LONG).show();
                    Uri uri = Uri.parse(numeroCall);
                    //   Intent itLigar = new Intent(Intent.ACTION_DIAL, uri);
                    Intent itLigar = new Intent(Intent.ACTION_CALL, uri);
                    startActivity(itLigar);

                }else{
                    Toast.makeText(this, "SEU FELA!", Toast.LENGTH_LONG).show();
                    Uri uri = Uri.parse(numeroCall);
                    Intent itLigar = new Intent(Intent.ACTION_DIAL, uri);
                    startActivity(itLigar);
                }
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected boolean checarPermissaoPhone(String numero){
        numeroCall=numero;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED){

            Log.v ("SMD","Tenho permissão");

            return true;

        } else {

            if ( shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)){
                String[] permissions ={Manifest.permission.CALL_PHONE};
                requestPermissions(permissions, 2222);
            }else{
                Uri uri = Uri.parse(numeroCall);
                Intent itLigar = new Intent(Intent.ACTION_DIAL, uri);
                startActivity(itLigar);
            }
        }
        return false;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Caso seja um Voltar ou Sucesso selecionar o item Ligar

        if (requestCode == 1111) {//Retorno de Mudar Perfil
            bnv.setSelectedItemId(R.id.tabLigar);
            user=atualizarUser();
            atualizarListaDeContatos(user);
            preencherListView(user); //Montagem do ListView
        }

        if (requestCode == 1112) {//Retorno de Mudar Contatos
            bnv.setSelectedItemId(R.id.tabLigar);
            atualizarListaDeContatos(user);
            preencherListView(user); //Montagem do ListView
        }



    }

    private User atualizarUser() {
        User user = null;
        SharedPreferences temUser= getSharedPreferences("usuarioPadrao", Activity.MODE_PRIVATE);
        String loginSalvo = temUser.getString("login","");
        String senhaSalva = temUser.getString("senha","");
        boolean manterLogado=temUser.getBoolean("manterLogado",false);

        user=new User(loginSalvo,senhaSalva,manterLogado);
        return user;
    }

    protected void atualizarListaDeContatos(User user){
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
        Log.v("PDM3","contatos:"+contatos.size());
        user.setContatos(contatos);
    }

}
