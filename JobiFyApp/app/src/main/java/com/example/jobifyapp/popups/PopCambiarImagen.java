package com.example.jobifyapp.popups;
// -----------------------------------------------------------------------------------------------------------------------------
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Button;

import com.example.jobifyapp.R;

import java.io.IOException;
import java.util.Objects;
// -----------------------------------------------------------------------------------------------------------------------------
public class PopCambiarImagen extends AppCompatActivity {
    // -----------------------------------------------------------------------------------------------------------------------------
    /* VARIABLES */
    private Button seleccionarCamara, seleccionarGaleria;

    private static Bitmap selectedImage;

    // Pedimos permiso de la galería
    static final int REQUEST_CODE_GALLERY = 0;

    // Pedimos permiso de la cámara
    static final int REQUEST_CODE_CAMERA = 1;
    // -----------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_cambiar_imagen);

        seleccionarCamara = findViewById(R.id.seleccionarCamara);
        seleccionarGaleria = findViewById(R.id.seleccionarGaleria);

        // Diseño de la ventana emergente
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.9),(int)(height*.5));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -50;

        getWindow().setAttributes(params);

        // Para no tener problemas con la toolbar la ocultamos
        Objects.requireNonNull(getSupportActionBar()).hide();

        // Eventos
        seleccionarGaleria.setOnClickListener(v -> abrirGaleria());
        seleccionarCamara.setOnClickListener(v -> abrirCamara());
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    // Método para abrir la galería
    private void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_GALLERY);
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    // Método para abrir la cámara
    private void abrirCamara() {
        if (ContextCompat.checkSelfPermission(this,  android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            //Si el permiso no está concedido, pedir permisos
            ActivityCompat.requestPermissions(this, new String[]{ android.Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, REQUEST_CODE_CAMERA);
        }
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    // Sobrescribo el método onRequestPermissionsResult para realizar acciones según el resultado de la solicitud.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido, puedes acceder a la cámara
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
            }
        }
    }
// -----------------------------------------------------------------------------------------------------------------------------
    // Este método lanza un activity y nos devuelva información que será usada en otra activity.
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_GALLERY) {
                Uri selectedImageUri = data.getData();
                try {
                    selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    // Enviar el resultado a EditPerfilActivity
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("image", selectedImage);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == REQUEST_CODE_CAMERA) {
                Bundle extras = data.getExtras();
                selectedImage = (Bitmap) extras.get("data");
                // Enviar el resultado a EditPerfilActivity
                Intent resultIntent = new Intent();
                resultIntent.putExtra("image", selectedImage);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        }
    }
    // -----------------------------------------------------------------------------------------------------------------------------
}
// -----------------------------------------------------------------------------------------------------------------------------
