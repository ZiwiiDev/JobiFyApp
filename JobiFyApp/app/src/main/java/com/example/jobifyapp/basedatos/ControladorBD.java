package com.example.jobifyapp.basedatos;
// -----------------------------------------------------------------------------------------------------------------------------
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.example.jobifyapp.modelo.Candidato;
import com.example.jobifyapp.modelo.Categoria;
import com.example.jobifyapp.modelo.Empresa;
import com.example.jobifyapp.modelo.Solicitud;
import com.example.jobifyapp.modelo.Usuario;
import com.example.jobifyapp.modelo.Vacante;
import com.example.jobifyapp.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
// -----------------------------------------------------------------------------------------------------------------------------
public class ControladorBD {
    // -----------------------------------------------------------------------------------------------------------------------------
    /* VARIABLES */
    private BaseDatosEmpleos baseDeDatos;
    // -----------------------------------------------------------------------------------------------------------------------------
    public ControladorBD(Context context) {
        baseDeDatos = new BaseDatosEmpleos(context);
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    public long agregarCategoria(Categoria categoria) {
        SQLiteDatabase db = baseDeDatos.getWritableDatabase();           // Modo escritura y lectura

        ContentValues values = new ContentValues();                      // Declaro valores
        values.put("nombre", categoria.getNombre());

        long idCategoriaInsertada = -1; // Valor predeterminado en caso de error

        if (db.isOpen()) {
            idCategoriaInsertada = db.insert("Categoria", null, values);
            db.close();
        }

        return idCategoriaInsertada;
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    // Verificar si una categoría ya existe en la base de datos por su nombre
    public boolean verificarCategoriaExistente(String nombreCategoria) {
        SQLiteDatabase db = baseDeDatos.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM Categoria WHERE nombre = ?";
        String[] selectionArgs = {nombreCategoria};
        Cursor cursor = db.rawQuery(query, selectionArgs);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count > 0;
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    public int obtenerUltimoIdCategoria() {
        SQLiteDatabase db = baseDeDatos.getReadableDatabase();
        int ultimoId = -1;

        Cursor cursor = db.rawQuery("SELECT MAX(id) FROM Categoria", null);

        if (cursor.moveToFirst()) {
            ultimoId = cursor.getInt(0);
        }
        cursor.close();

        return ultimoId;
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    @SuppressLint("SimpleDateFormat")
    public void agregarVacante (Vacante vacante) {
        SQLiteDatabase db = baseDeDatos.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nombre", vacante.getNombre());

        // Obtener la fecha actual
        Calendar calendar = Calendar.getInstance();
        Date fechaActual = calendar.getTime();

        // Convertir la fecha actual a un formato legible
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String fecha = dateFormat.format(fechaActual);

        values.put("fecha", fecha);

        values.put("salario", vacante.getSalario());
        values.put("destacado", vacante.getDestacado());
        values.put("detalles", vacante.getDetalles());
        values.put("idCategoria", vacante.getIdCategoria().getId());
        values.put("idUsuario", vacante.getIdUsuario().getId());

        if (db.isOpen()) {
            int  id = (int) db.insert("Vacante", null, values);
            vacante.setId(id);
            db.close();
        }
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    // Verificar si una vacante ya existe en la base de datos por su nombre
    public boolean verificarVacanteExistente(String nombreVacante) {
        SQLiteDatabase db = baseDeDatos.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM Vacante WHERE nombre = ?";
        String[] selectionArgs = {nombreVacante};
        Cursor cursor = db.rawQuery(query, selectionArgs);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count > 0;
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    @SuppressLint("Range")
    public ArrayList<Categoria> obtenerTodasLasCategorias() {
        ArrayList<Categoria> listaCategorias = new ArrayList<>();

        SQLiteDatabase db = baseDeDatos.getReadableDatabase();
        String query = "SELECT * FROM Categoria";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String nombre = cursor.getString(cursor.getColumnIndex("nombre"));

                Categoria categoria = new Categoria(nombre);
                listaCategorias.add(categoria);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return listaCategorias;
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    public int agregarUsuario(Usuario usuario) {
        SQLiteDatabase db = baseDeDatos.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nombre", usuario.getNombre());
        values.put("email", usuario.getEmail());
        values.put("username", usuario.getUsername());
        values.put("password", usuario.getPassword());
        values.put("telefono", usuario.getTelefono());
        values.put("direccion", usuario.getDireccion());

        int usuarioId = (int) db.insert("Usuario", null, values);

        db.close();

        return usuarioId;
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    public void agregarCandidato(Candidato candidato) {
        SQLiteDatabase db = baseDeDatos.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("usuario_id", candidato.getId());

        values.put("experiencia", candidato.getExperiencia());
        values.put("educacion", candidato.getEducacion());

        db.insert("Candidato", null, values);
        db.close();
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    public void agregarEmpresa(Empresa empresa) {
        SQLiteDatabase db = baseDeDatos.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("usuario_id", empresa.getId());

        values.put("sector", empresa.getSector());
        values.put("web", empresa.getWeb());

        db.insert("Empresa", null, values);
        db.close();
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    public boolean guardarSolicitud(Solicitud solicitud) {
        SQLiteDatabase db = baseDeDatos.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("id", solicitud.getId());

        // Obtener la fecha actual
        Calendar calendar = Calendar.getInstance();
        Date fechaActual = calendar.getTime();

        // Convertir la fecha actual a un formato legible
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String fecha = dateFormat.format(fechaActual);

        values.put("fecha", fecha);

        values.put("archivo", solicitud.getArchivo());
        values.put("idVacante", solicitud.getIdVacante().getId());
        values.put("idUsuario", solicitud.getIdUsuario().getId());

        // Verificar si ya existe una solicitud con el mismo par de valores
        String selection = "idVacante=? AND idUsuario=?";
        String[] selectionArgs = {String.valueOf(solicitud.getIdVacante().getId()), String.valueOf(solicitud.getIdUsuario().getId())};
        Cursor cursor = db.query("Solicitud", null, selection, selectionArgs, null, null, null);
        boolean existeSolicitud = cursor.getCount() > 0;
        cursor.close();

        if (existeSolicitud) {
            // Ya existe una solicitud con el mismo par de valores
            db.close();
            return false;
        } else {
            long resultado = db.insert("Solicitud", null, values);
            db.close();
            return resultado != -1;
        }
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    /* OBTENER LOS DATOS DE LAS TABLAS */

    @SuppressLint("Range")
    public ArrayList<Categoria> obtenerCategorias() {
        SQLiteDatabase db = baseDeDatos.getReadableDatabase();              // Modo escritura y lectura
        ArrayList<Categoria> listaCategorias = new ArrayList<>();

        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("SELECT * FROM Categoria", null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String nombre = cursor.getString(cursor.getColumnIndex("nombre"));

                    Categoria categoria = new Categoria(nombre);
                    listaCategorias.add(categoria);
                } while (cursor.moveToNext());

                cursor.close();
            }
        }

        return listaCategorias;
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    @SuppressLint("Range")
    public ArrayList<Usuario> obtenerUsuarios() {
        SQLiteDatabase db = baseDeDatos.getReadableDatabase();              // Modo escritura y lectura
        ArrayList<Usuario> listaUsuarios = new ArrayList<>();

        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("SELECT * FROM Usuario", null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String nombre = cursor.getString(cursor.getColumnIndex("nombre"));
                    String email = cursor.getString(cursor.getColumnIndex("email"));
                    String username = cursor.getString(cursor.getColumnIndex("username"));

                    Usuario usuario = new Usuario(nombre, email, username);
                    listaUsuarios.add(usuario);
                } while (cursor.moveToNext());

                cursor.close();
            }
        }

        return listaUsuarios;
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    @SuppressLint("Range")
    public ArrayList<Solicitud> obtenerSolicitudes() {
        SQLiteDatabase db = baseDeDatos.getReadableDatabase();
        ArrayList<Solicitud> listaSolicitudes = new ArrayList<>();

        if (db != null) {
            Cursor cursor = db.rawQuery("SELECT * FROM Solicitud", null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int idSolicitud = cursor.getInt(cursor.getColumnIndex("id"));
                    String fechaString = cursor.getString(cursor.getColumnIndex("fecha"));
                    Date fecha = Utils.parseDateFromString(fechaString);

                    byte[] archivo = cursor.getBlob(cursor.getColumnIndex("archivo"));
                    int idVacante = cursor.getInt(cursor.getColumnIndex("idVacante"));
                    int idUsuario = cursor.getInt(cursor.getColumnIndex("idUsuario"));

                    Vacante vacante = obtenerVacantePorId(idVacante);
                    Candidato candidato = obtenerCandidatoPorId(idUsuario);

                    Solicitud solicitud = new Solicitud(idSolicitud, fecha, archivo, vacante, candidato);
                    listaSolicitudes.add(solicitud);
                } while (cursor.moveToNext());

                cursor.close();
            }
        }

        return listaSolicitudes;
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    @SuppressLint("Range")
    public ArrayList<Vacante> obtenerVacantes() {
        SQLiteDatabase db = baseDeDatos.getReadableDatabase();
        ArrayList<Vacante> listaVacantes = new ArrayList<>();

        if (db != null) {
            Cursor cursor = db.rawQuery("SELECT * FROM Vacante", null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String nombre = cursor.getString(cursor.getColumnIndex("nombre"));

                    String fechaString = cursor.getString(cursor.getColumnIndex("fecha"));
                    Date fecha = Utils.parseDateFromString(fechaString); // Método para convertir String a Date

                    double salario = cursor.getDouble(cursor.getColumnIndex("salario"));
                    int destacado = cursor.getInt(cursor.getColumnIndex("destacado"));
                    String detalles = cursor.getString(cursor.getColumnIndex("detalles"));
                    int idCategoria = cursor.getInt(cursor.getColumnIndex("idCategoria"));
                    int idUsuario = cursor.getInt(cursor.getColumnIndex("idUsuario"));

                    Categoria categoria = obtenerCategoriaPorId(idCategoria);
                    Empresa empresa = obtenerEmpresaPorId(idUsuario);

                    Vacante vacante = new Vacante(nombre, fecha, salario, destacado, detalles, categoria, empresa);
                    listaVacantes.add(vacante);
                } while (cursor.moveToNext());

                cursor.close();
            }
        }

        return listaVacantes;
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    @SuppressLint("Range")
    public Usuario obtenerDatosUsuario(int usuarioId) {
        SQLiteDatabase db = baseDeDatos.getReadableDatabase();

        String query = "SELECT u.*, c.experiencia, c.educacion, e.sector, e.web " +
                "FROM Usuario u " +
                "LEFT JOIN Candidato c ON u.id = c.usuario_id " +
                "LEFT JOIN Empresa e ON u.id = e.usuario_id " +
                "WHERE u.id = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(usuarioId)});

        Usuario usuario = null;
        Candidato candidato = null;
        Empresa empresa = null;

        if (cursor.moveToFirst()) {
            usuario = new Usuario();
            usuario.setId(usuarioId);
            usuario.setNombre(cursor.getString(cursor.getColumnIndex("nombre")));
            usuario.setUsername(cursor.getString(cursor.getColumnIndex("username")));
            usuario.setEmail(cursor.getString(cursor.getColumnIndex("email")));
            usuario.setPassword(cursor.getString(cursor.getColumnIndex("password")));
            usuario.setTelefono(cursor.getString(cursor.getColumnIndex("telefono")));
            usuario.setDireccion(cursor.getString(cursor.getColumnIndex("direccion")));

            // Verificar si es un usuario candidato o empresa
            int candidatoIdIndex = cursor.getColumnIndex("candidato_id");
            int empresaIdIndex = cursor.getColumnIndex("empresa_id");

            if (!cursor.isNull(candidatoIdIndex)) {
                int candidatoId = cursor.getInt(candidatoIdIndex);
                if (candidatoId > 0) {
                    candidato = new Candidato();
                    candidato.setId(candidatoId);
                    candidato.setExperiencia(cursor.getString(cursor.getColumnIndex("experiencia")));
                    candidato.setEducacion(cursor.getString(cursor.getColumnIndex("educacion")));
                }
            }

            if (!cursor.isNull(empresaIdIndex)) {
                int empresaId = cursor.getInt(empresaIdIndex);
                if (empresaId > 0) {
                    empresa = new Empresa();
                    empresa.setId(empresaId);
                    empresa.setSector(cursor.getString(cursor.getColumnIndex("sector")));
                    empresa.setWeb(cursor.getString(cursor.getColumnIndex("web")));
                }
            }

            // Establecer el tipo de usuario en el objeto usuario
            if (candidato != null) {
                usuario = candidato;
            } else if (empresa != null) {
                usuario = empresa;
            }
        }

        cursor.close();
        db.close();

        return usuario;
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    @SuppressLint("Range")
    public int obtenerIdUsuario(String email) {
        SQLiteDatabase db = baseDeDatos.getReadableDatabase();
        int usuarioId = -1;

        String query = "SELECT id FROM Usuario WHERE email = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});

        if (cursor.moveToFirst()) {
            usuarioId = cursor.getInt(cursor.getColumnIndex("id"));
        }

        cursor.close();
        db.close();

        return usuarioId;
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    /* ELIMINAR REGISTROS DE LAS TABLAS */

    public void eliminarVacanteBD(String nombre) {
        SQLiteDatabase db = baseDeDatos.getWritableDatabase();

        String[] argumento = new String[]{nombre};

        if (db.isOpen()) {
            // Eliminar la vacante
            db.delete("Vacante", "nombre = ?", argumento);

            // Eliminar la solicitud asociada a la vacante
            db.delete("Solicitud", "idVacante IN (SELECT id FROM Vacante WHERE nombre = ?)", argumento);

            db.close();
        }
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    /* EDITAR REGISTROS DE LAS TABLAS */

    public void editarCategoria (Categoria categoria) {
        SQLiteDatabase db = baseDeDatos.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nombre", categoria.getNombre());

        String[] args = new String[]{String.valueOf(categoria.getId())};

        if (db.isOpen()) {
            db.update("Categoria", values, "id = ?", args);
            db.close();
        }
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    @SuppressLint("Range")
    public Categoria obtenerCategoriaPorId(int id) {
        SQLiteDatabase db = baseDeDatos.getReadableDatabase();

        String[] columnas = {"id", "nombre"};
        String seleccion = "id = ?";
        String[] seleccionArgs = {String.valueOf(id)};

        Categoria categoria = null;
        try (Cursor cursor = db.query("Categoria", columnas, seleccion, seleccionArgs, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                String nombre = cursor.getString(cursor.getColumnIndex("nombre"));
                categoria = new Categoria(id, nombre);
            }
        }

        return categoria;
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    @SuppressLint("Range")
    public Categoria obtenerCategoriaPorNombre(String nombreCategoria) {
        SQLiteDatabase db = baseDeDatos.getReadableDatabase();

        String query = "SELECT * FROM Categoria WHERE nombre = ?";
        String[] selectionArgs = {nombreCategoria};
        Cursor cursor = db.rawQuery(query, selectionArgs);

        Categoria categoria = null;

        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String nombre = cursor.getString(cursor.getColumnIndex("nombre"));
            categoria = new Categoria();
            categoria.setId(id);
            categoria.setNombre(nombre);
        }

        cursor.close();
        return categoria;
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    @SuppressLint("Range")
    public Vacante obtenerVacantePorNombre(String nombreVacante) {
        SQLiteDatabase db = baseDeDatos.getReadableDatabase();

        String query = "SELECT * FROM Vacante WHERE nombre = ?";

        String[] selectionArgs = {nombreVacante};
        Cursor cursor = db.rawQuery(query, selectionArgs);

        Vacante vacante = null;
        if (cursor.moveToFirst()) {
            vacante = new Vacante();
            vacante.setId(cursor.getInt(cursor.getColumnIndex("id")));
            vacante.setNombre(cursor.getString(cursor.getColumnIndex("nombre")));
            vacante.setSalario(cursor.getDouble(cursor.getColumnIndex("salario")));
            vacante.setDetalles(cursor.getString(cursor.getColumnIndex("detalles")));
            vacante.setDestacado(cursor.getInt(cursor.getColumnIndex("destacado")));

            int idCategoria = cursor.getInt(cursor.getColumnIndex("idCategoria"));
            Categoria categoria = obtenerCategoriaPorId(idCategoria);
            vacante.setIdCategoria(categoria);

            int idUsuario = cursor.getInt(cursor.getColumnIndex("idUsuario"));
            Empresa empresa = obtenerEmpresaPorId(idUsuario);
            vacante.setIdUsuario(empresa);
        }

        cursor.close();
        return vacante;
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    @SuppressLint("Range")
    public Vacante obtenerVacantePorId(int id) {
        SQLiteDatabase db = baseDeDatos.getReadableDatabase();

        String[] columnas = {"id", "nombre", "fecha", "salario", "destacado", "detalles", "idCategoria", "idUsuario"};

        String seleccion = "id = ?";
        String[] seleccionArgs = {String.valueOf(id)};

        Cursor cursor = db.query("Vacante", columnas, seleccion, seleccionArgs, null, null, null);

        Vacante vacante = null;
        if (cursor != null && cursor.moveToFirst()) {
            String nombre = cursor.getString(cursor.getColumnIndex("nombre"));
            int salario = cursor.getInt(cursor.getColumnIndex("salario"));
            int destacado = cursor.getInt(cursor.getColumnIndex("destacado"));
            String detalles = cursor.getString(cursor.getColumnIndex("detalles"));
            int idCategoria = cursor.getInt(cursor.getColumnIndex("idCategoria"));
            int idUsuario = cursor.getInt(cursor.getColumnIndex("idUsuario"));

            Categoria categoria = obtenerCategoriaPorId(idCategoria);
            Empresa empresa = obtenerEmpresaPorId(idUsuario);

            vacante = new Vacante(id, nombre, salario, destacado, detalles, categoria, empresa);
            cursor.close();
        }

        return vacante;
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    @SuppressLint("Range")
    public Empresa obtenerEmpresaPorId(int id) {
        SQLiteDatabase db = baseDeDatos.getReadableDatabase();

        String[] columnas = {"usuario_id", "web", "sector"};

        String seleccion = "usuario_id = ?";
        String[] seleccionArgs = {String.valueOf(id)};

        Empresa empresa = null;
        try (Cursor cursor = db.query("Empresa", columnas, seleccion, seleccionArgs, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                String web = cursor.getString(cursor.getColumnIndex("web"));
                String sector = cursor.getString(cursor.getColumnIndex("sector"));

                empresa = new Empresa(id, "", "", "", "", "", "", web, sector);
                cursor.close();
            }
        }

        return empresa;
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    @SuppressLint("Range")
    public Candidato obtenerCandidatoPorId(int id) {
        SQLiteDatabase db = baseDeDatos.getReadableDatabase();

        String[] columnas = {"usuario_id", "experiencia", "educacion"};

        String seleccion = "usuario_id = ?";
        String[] seleccionArgs = {String.valueOf(id)};

        Candidato candidato = null;
        try (Cursor cursor = db.query("Candidato", columnas, seleccion, seleccionArgs, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                String experiencia = cursor.getString(cursor.getColumnIndex("experiencia"));
                String educacion = cursor.getString(cursor.getColumnIndex("educacion"));

                candidato = new Candidato(id, "", "", "", "", "", "", experiencia, educacion);
                cursor.close();
            }
        }

        return candidato;
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    /* MÉTODO PARA OBTENER LAS VACANTES DESTACADAS */

    @SuppressLint("Range")
    public ArrayList<Vacante> obtenerVacantesDestacadas() {
        SQLiteDatabase db = baseDeDatos.getReadableDatabase();
        ArrayList<Vacante> listaVacantesDestacadas = new ArrayList<>();

        if (db != null) {
            String[] columnas = {"id", "nombre", "fecha", "salario", "destacado", "detalles", "idCategoria", "idUsuario"};

            String seleccion = "destacado = ?";     // Filtrar por vacantes destacadas
            String[] seleccionArgs = {"1"};         // 1 significa que está destacada

            Cursor cursor = db.query("Vacante", columnas, seleccion, seleccionArgs, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String nombre = cursor.getString(cursor.getColumnIndex("nombre"));

                    String fechaString = cursor.getString(cursor.getColumnIndex("fecha"));
                    Date fecha = Utils.parseDateFromString(fechaString); // Método para convertir String a Date

                    double salario = cursor.getDouble(cursor.getColumnIndex("salario"));
                    int destacado = cursor.getInt(cursor.getColumnIndex("destacado"));
                    String detalles = cursor.getString(cursor.getColumnIndex("detalles"));
                    int idCategoria = cursor.getInt(cursor.getColumnIndex("idCategoria"));
                    int idUsuario = cursor.getInt(cursor.getColumnIndex("idUsuario"));

                    Categoria categoria = obtenerCategoriaPorId(idCategoria);
                    Empresa empresa = obtenerEmpresaPorId(idUsuario);

                    Vacante vacante = new Vacante(nombre, fecha, salario, destacado, detalles, categoria, empresa);
                    listaVacantesDestacadas.add(vacante);
                } while (cursor.moveToNext());
                cursor.close();
            }
        }

        return listaVacantesDestacadas;
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    @SuppressLint("Range")
    public int obtenerIdEmpresa() {
        SQLiteDatabase db = baseDeDatos.getReadableDatabase();
        int usuarioId = -1;

        Cursor cursor = db.query("Empresa", new String[]{"usuario_id"}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            usuarioId = cursor.getInt(cursor.getColumnIndex("usuario_id"));
        }
        cursor.close();
        db.close();

        return usuarioId;
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    @SuppressLint("Range")
    public String obtenerEmailPorTipoUsuario(String email) {
        SQLiteDatabase db = baseDeDatos.getReadableDatabase();
        String tipoUsuario = "";

        // Realiza la consulta JOIN para obtener el tipo de usuario
        String query = "SELECT u.*, c.usuario_id AS candidato_id, e.usuario_id AS empresa_id " +
                "FROM Usuario u " +
                "LEFT JOIN Candidato c ON u.id = c.usuario_id " +
                "LEFT JOIN Empresa e ON u.id = e.usuario_id " +
                "WHERE u.email = ?";

        Cursor cursor = db.rawQuery(query, new String[]{email});

        if (cursor.moveToFirst()) {
            // Obtener el tipo de usuario
            int candidatoId = cursor.getInt(cursor.getColumnIndex("candidato_id"));
            int empresaId = cursor.getInt(cursor.getColumnIndex("empresa_id"));

            if (!cursor.isNull(cursor.getColumnIndex("candidato_id")) && candidatoId > 0) {
                tipoUsuario = "candidato"; // El usuario es un candidato
            } else if (!cursor.isNull(cursor.getColumnIndex("empresa_id")) && empresaId > 0) {
                tipoUsuario = "empresa"; // El usuario es una empresa
            }
        }

        cursor.close();
        db.close();

        return tipoUsuario;
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    public void actualizarVacante(Vacante vacante) {
        SQLiteDatabase db = baseDeDatos.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nombre", vacante.getNombre());

        // Obtener la fecha actual
        Calendar calendar = Calendar.getInstance();
        Date fechaActual = calendar.getTime();

        // Convertir la fecha actual a un formato legible
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String fecha = dateFormat.format(fechaActual);

        values.put("fecha", fecha);

        values.put("salario", vacante.getSalario());
        values.put("destacado", vacante.getDestacado());
        values.put("detalles", vacante.getDetalles());
        values.put("idCategoria", vacante.getIdCategoria().getId());

        String whereClause = "nombre = ?";
        String[] whereArgs = {vacante.getNombre()};

        if (db.isOpen()) {
            db.update("Vacante", values, whereClause, whereArgs);
            db.close();
        }
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    public void actualizarNombre(int idUsuario, String nuevoNombre) {
        SQLiteDatabase db = baseDeDatos.getWritableDatabase();

        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put("nombre", nuevoNombre);

            String whereClause = "id = ?";
            String[] whereArgs = {String.valueOf(idUsuario)};

            db.update("Usuario", values, whereClause, whereArgs);

            db.close();
        }
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    public void actualizarUsername(int idUsuario, String nuevoUsername) {
        SQLiteDatabase db = baseDeDatos.getWritableDatabase();

        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put("username", nuevoUsername);

            String whereClause = "id = ?";
            String[] whereArgs = {String.valueOf(idUsuario)};

            db.update("Usuario", values, whereClause, whereArgs);

            db.close();
        }
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    public void actualizarTelefono(int idUsuario, String nuevoTelefono) {
        SQLiteDatabase db = baseDeDatos.getWritableDatabase();

        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put("telefono", nuevoTelefono);

            String whereClause = "id = ?";
            String[] whereArgs = {String.valueOf(idUsuario)};

            db.update("Usuario", values, whereClause, whereArgs);

            db.close();
        }
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    public void actualizarDireccion(int idUsuario, String nuevaDireccion) {
        SQLiteDatabase db = baseDeDatos.getWritableDatabase();

        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put("direccion", nuevaDireccion);

            String whereClause = "id = ?";
            String[] whereArgs = {String.valueOf(idUsuario)};

            db.update("Usuario", values, whereClause, whereArgs);

            db.close();
        }
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    public void actualizarExperiencia(int idUsuario, String nuevaExperiencia) {
        SQLiteDatabase db = baseDeDatos.getWritableDatabase();

        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put("experiencia", nuevaExperiencia);

            String whereClause = "usuario_id = ?";
            String[] whereArgs = {String.valueOf(idUsuario)};

            db.update("Candidato", values, whereClause, whereArgs);

            db.close();
        }
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    public void actualizarEducacion(int idUsuario, String nuevaEducacion) {
        SQLiteDatabase db = baseDeDatos.getWritableDatabase();

        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put("educacion", nuevaEducacion);

            String whereClause = "usuario_id = ?";
            String[] whereArgs = {String.valueOf(idUsuario)};

            db.update("Candidato", values, whereClause, whereArgs);

            db.close();
        }
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    public void actualizarSector(int idUsuario, String nuevoSector) {
        SQLiteDatabase db = baseDeDatos.getWritableDatabase();

        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put("sector", nuevoSector);

            String whereClause = "usuario_id = ?";
            String[] whereArgs = {String.valueOf(idUsuario)};

            db.update("Empresa", values, whereClause, whereArgs);

            db.close();
        }
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    public void actualizarWeb(int idUsuario, String nuevaWeb) {
        SQLiteDatabase db = baseDeDatos.getWritableDatabase();

        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put("web", nuevaWeb);

            String whereClause = "usuario_id = ?";
            String[] whereArgs = {String.valueOf(idUsuario)};

            db.update("Empresa", values, whereClause, whereArgs);

            db.close();
        }
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    /* OBTENER NÚMERO DE REGISTROS DE UNA TABLA */
    public int numCategorias() {
        SQLiteDatabase db = baseDeDatos.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, "Categoria");
    }

    public int numVacantes() {
        SQLiteDatabase db = baseDeDatos.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, "Vacante");
    }

    public int numSolicitudes() {
        SQLiteDatabase db = baseDeDatos.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, "Solicitud");
    }

    public int numUsuarios() {
        SQLiteDatabase db = baseDeDatos.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, "Usuario");
    }

    public int numEmpresa() {
        SQLiteDatabase db = baseDeDatos.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, "Empresa");
    }

    public int numCandidato() {
        SQLiteDatabase db = baseDeDatos.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, "Candidato");
    }
    // -----------------------------------------------------------------------------------------------------------------------------
}
// -----------------------------------------------------------------------------------------------------------------------------
