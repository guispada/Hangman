package com.example.hangman;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText usuario;
    private EditText idade;
    private UsuarioDAO dao;
    private Usuario u = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usuario = findViewById(R.id.editText);
        idade = findViewById(R.id.editText2);
        dao = new UsuarioDAO(this);

        Intent it = getIntent();
        if (it.hasExtra("usuario")){
            u = (Usuario) it.getSerializableExtra("usuario");
            usuario.setText(u.getUsuario());
            idade.setText(u.getIdade());
        }

    }

    public void salvar(View view){

        if (u == null) {
            u = new Usuario();
            u.setUsuario(usuario.getText().toString());
            u.setIdade(idade.getText().toString());
            long id = dao.inserir(u);
            Toast.makeText(this, "Usuario: " + id, Toast.LENGTH_LONG).show();
        }else{
            u.setUsuario(usuario.getText().toString());
            u.setIdade(idade.getText().toString());
            dao.atualizar(u);
            Toast.makeText(this, "Usuario atualizado", Toast.LENGTH_LONG).show();
        }
    }

}
