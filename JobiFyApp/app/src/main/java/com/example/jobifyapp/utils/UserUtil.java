package com.example.jobifyapp.utils;
// -----------------------------------------------------------------------------------------------------------------------------
import android.content.Context;
import android.content.SharedPreferences;
// -----------------------------------------------------------------------------------------------------------------------------
public class UserUtil {
    // -----------------------------------------------------------------------------------------------------------------------------
    private static final String PREF_NAME = "UserUtilPrefs";
    private static final String KEY_TIPO_USUARIO = "tipoUsuario";

    private static SharedPreferences preferences;
    // -----------------------------------------------------------------------------------------------------------------------------
    public static void init(Context context) {
        if (preferences == null) {
            preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        }
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    public static String getTipoUsuario() {
        return preferences.getString(KEY_TIPO_USUARIO, null);
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    public static void setTipoUsuario(String tipo) {
        preferences.edit().putString(KEY_TIPO_USUARIO, tipo).apply();
    }
    // -----------------------------------------------------------------------------------------------------------------------------
}
// -----------------------------------------------------------------------------------------------------------------------------
