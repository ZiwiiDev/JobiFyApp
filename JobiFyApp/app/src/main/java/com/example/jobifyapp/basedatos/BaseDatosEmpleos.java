package com.example.jobifyapp.basedatos;
// -----------------------------------------------------------------------------------------------------------------------------
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
// -----------------------------------------------------------------------------------------------------------------------------
public class BaseDatosEmpleos extends SQLiteOpenHelper {
    // -----------------------------------------------------------------------------------------------------------------------------
    /* Constantes */
    private static final String NOMBRE_BD = "empleosdb";
    private static final int VERSION_BD = 1;
    // -----------------------------------------------------------------------------------------------------------------------------
    /* Constructor */
    public BaseDatosEmpleos(Context context) {
        super(context, NOMBRE_BD, null, VERSION_BD);
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Categoria (" +
                "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                " nombre VARCHAR(100) NOT NULL" +
                ")");

        db.execSQL("CREATE TABLE Vacante (" +
                "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "nombre VARCHAR(200) NOT NULL, " +
                "fecha DATE NOT NULL, " +
                "salario DOUBLE NOT NULL, " +
                "destacado INTEGER NOT NULL, " +
                "detalles TEXT, " +
                "idCategoria INTEGER NOT NULL, " +
                "idUsuario INTEGER NOT NULL, " +
                "FOREIGN KEY (idCategoria) REFERENCES Categoria(id), " +
                "FOREIGN KEY (idUsuario) REFERENCES Empresa(usuario_id)" +
                ")");

        db.execSQL("CREATE TABLE Usuario (" +
                "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "nombre VARCHAR(45) DEFAULT NULL, " +
                "email VARCHAR(100) NOT NULL, " +
                "username VARCHAR(45) DEFAULT NULL, " +
                "password VARCHAR(100) NOT NULL, " +
                "telefono VARCHAR(9) DEFAULT NULL, " +
                "direccion VARCHAR(200) DEFAULT NULL, " +
                "UNIQUE (email)" +
                ")");

        db.execSQL("CREATE TABLE Candidato (" +
                "usuario_id INTEGER NOT NULL, " +
                "experiencia VARCHAR(500) DEFAULT NULL, " +
                "educacion VARCHAR(500) DEFAULT NULL, " +
                "PRIMARY KEY (usuario_id), " +
                "FOREIGN KEY (usuario_id) REFERENCES Usuario(id)" +
                ")");

        db.execSQL("CREATE TABLE Empresa (" +
                "usuario_id INTEGER NOT NULL, " +
                "sector VARCHAR(100) DEFAULT NULL, " +
                "web VARCHAR(100) DEFAULT NULL, " +
                "PRIMARY KEY (usuario_id), " +
                "FOREIGN KEY (usuario_id) REFERENCES Usuario(id)" +
                ")");

        db.execSQL("CREATE TABLE Solicitud (" +
                "id TEXT NOT NULL PRIMARY KEY, " +
                "fecha DATE NOT NULL, " +
                "archivo BLOB NOT NULL, " +
                "idVacante INTEGER NOT NULL, " +
                "idUsuario INTEGER NOT NULL, " +
                "FOREIGN KEY (idVacante) REFERENCES Vacante(id), " +
                "FOREIGN KEY (idUsuario) REFERENCES Candidato(usuario_id)" +
                ")");
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Categoria");
        db.execSQL("DROP TABLE IF EXISTS Vacante");
        db.execSQL("DROP TABLE IF EXISTS Usuario");
        db.execSQL("DROP TABLE IF EXISTS Candidato");
        db.execSQL("DROP TABLE IF EXISTS Empresa");
        db.execSQL("DROP TABLE IF EXISTS Solicitud");

        onCreate(db);
    }
    // -----------------------------------------------------------------------------------------------------------------------------
}
// -----------------------------------------------------------------------------------------------------------------------------
