package com.example.hangman;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

public class ListarUsuarios extends AppCompatActivity {

    private ListView listView;
    private UsuarioDAO dao;
    private List<Usuario> usuarios;
    private List<Usuario> usuariosFiltrados = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_usuarios);

        listView = findViewById(R.id.lista_usuarios);
        dao = new UsuarioDAO(this);
        usuarios = dao.obterTodos();
        usuariosFiltrados.addAll(usuarios);
        ArrayAdapter<Usuario> adaptador = new ArrayAdapter<Usuario>(this, android.R.layout.simple_list_item_1, usuariosFiltrados);
        listView.setAdapter(adaptador);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater i = getMenuInflater();
        i.inflate(R.menu.menu_principal, menu);

        SearchView sv = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                procuraUsuario(s);
                return false;
            }
        });

        return true;
    }

    public void procuraUsuario(String usuario){
        usuariosFiltrados.clear();
        for(Usuario u : usuarios){
            if (u.getUsuario().toLowerCase().contains(usuario.toLowerCase())){
                usuariosFiltrados.add(u);
            }
        }
        listView.invalidateViews();
    }

    public void cadastrar(MenuItem item){
        Intent it = new Intent(this, MainActivity.class);
        startActivity(it);
    }

    @Override
    public void onResume(){
        super.onResume();
        usuarios = dao.obterTodos();
        usuariosFiltrados.clear();
        usuariosFiltrados.addAll(usuarios);
        listView.invalidateViews();
    }

}
