package com.example.hangman;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    private Conexao conexao;
    private SQLiteDatabase banco;

    public UsuarioDAO(Context context){
        conexao = new Conexao(context);
        banco = conexao.getWritableDatabase();
    }

    public long inserir(Usuario usuario){
        ContentValues values = new ContentValues();
        values.put("usuario", usuario.getUsuario());
        values.put("idade", usuario.getIdade());
        return banco.insert("usuario", null, values);

    }

    public List<Usuario> obterTodos(){
        List<Usuario> usuarios = new ArrayList<>();
        Cursor cursor = banco.query("usuario", new String[]{"id", "usuario", "idade"},
                null, null, null, null, null);
        while(cursor.moveToNext()){
            Usuario u = new Usuario();
            u.setId(cursor.getInt(0));
            u.setUsuario(cursor.getString(1));
            u.setIdade(cursor.getString(2));
            usuarios.add(u);
        }
        return usuarios;
    }
}
