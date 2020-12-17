package util;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferencias {

    private Context contexto;
    private SharedPreferences preferences;
    private final String NOME_ARQUIVO = "orcaguincho.preferencias";
    private final int MODE = 0;
    private SharedPreferences.Editor editor;

    private int CHAVE_15 = 100;
    private int CHAVE_30 = 130;
    private int CHAVE_31 = 130;

    public Preferencias(Context contextoParametro) {
        contexto = contextoParametro;
        preferences = contexto.getSharedPreferences(NOME_ARQUIVO, MODE);
        editor = preferences.edit();
    }

    public void salvarDados(int quinze, int trinta, int trintaUm) {
        editor.putInt(String.valueOf(CHAVE_15), quinze);
        editor.putInt(String.valueOf(CHAVE_30), trinta);
        editor.putInt(String.valueOf(CHAVE_31), trintaUm);

        editor.commit();
    }
}
