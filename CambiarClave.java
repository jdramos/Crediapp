package com.example.credimovil;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class CambiarClave extends AppCompatActivity {

    SharedPreferences preferencias;
    String nomUser, spNumSuc, spCodOfi,
            spToken, spWeb, spEmpresa, url,  rol;
    RequestQueue requestQueue;
    ProgressDialog progreso;
    String passwordInput;
    String validarPasswordInput;

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=!/()?¿¡*])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{6,}" +               //at least 4 characters
                    "$");


    private TextInputLayout textInputPassword;
    private TextInputLayout textInputValidarPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_clave);


        textInputPassword = findViewById(R.id.text_input_password);
        textInputValidarPassword = findViewById(R.id.text_input_validar_password);

        preferencias = getSharedPreferences("usuario", Context.MODE_PRIVATE);
        spNumSuc = preferencias.getString("numsuc", null);
        spCodOfi = preferencias.getString("codofi", null);
        spToken = preferencias.getString("token", null);
        spWeb = preferencias.getString("web", null);
        spEmpresa = preferencias.getString("empresa", null);
        rol = preferencias.getString("rol", null);
        nomUser = preferencias.getString("nomuser", null);


    }

    private boolean validatePassword() {
        passwordInput = textInputPassword.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            textInputPassword.setError("Campo no puede ser vacío");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            textInputPassword.setError("Contraseña debe contener al menos 6 caracteres, una letra mayúscula, " +
                    "un numero y un cáracter especial @#$%^&+=!/()?¿¡* ");
            return false;
        }else {
            textInputPassword.setError(null);
            textInputValidarPassword.setError(null);
            return true;
        }
    }

    private boolean validarIguales() {
        passwordInput = textInputPassword.getEditText().getText().toString().trim();
        validarPasswordInput = textInputValidarPassword.getEditText().getText().toString().trim();

        if (!passwordInput.equals(validarPasswordInput) ) {
            textInputValidarPassword.setError("Contraseñas no coninciden");
            return false;
        }else {
            textInputPassword.setError(null);
            textInputValidarPassword.setError(null);
            return true;
        }
    }


    public void confirmInput(View v) {
        if ( !validatePassword() || !validarIguales()) {
            return;
        }
        passwordInput = textInputPassword.getEditText().getText().toString().trim();
        url = "https://www.nicaraguahosting.com/crediapp/cambiarclave.php?nomUser="+nomUser+"&pasUser="+passwordInput;
        AlertDialog.Builder builder = new AlertDialog.Builder(CambiarClave.this);
        builder.setMessage("Confirme el cambio de contraseña")
                .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        espere();
                        cambiarClave(url);

                    }
                })
                .setNegativeButton("Cancelar", null);

        AlertDialog alert = builder.create();
        alert.show();

    }

    private void cambiarClave (String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        if (response.contains("EXITO")){
                            Toast.makeText(getApplicationContext(), "Su contraseña ha sido actualizada", Toast.LENGTH_SHORT).show();
                            finish();
                            Intent intent = new Intent(getApplicationContext(), Dashbord.class);
                            startActivity(intent);
                            progreso.dismiss();

                        }

                        if (response.contains("EXISTE")){
                            Toast.makeText(getApplicationContext(), "Esta constraseña ya ha sido usada", Toast.LENGTH_SHORT).show();
                            progreso.dismiss();
                            return;

                        }

                        if (response.contains("ERROR")){
                            Toast.makeText(getApplicationContext(), "Error al actualizar contraseña o usuario no existe", Toast.LENGTH_SHORT).show();
                            progreso.dismiss();
                            return;

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(),Toast.LENGTH_SHORT).show();
                progreso.dismiss();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros= new HashMap<>();
                String pass = textInputPassword.getEditText().getText().toString().trim();
                parametros.put("nomUser", nomUser);
                parametros.put("pasUser", pass);

                return parametros;
            }
        };

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    public void espere(){
        progreso = new ProgressDialog(this);
        progreso.setCancelable(false);
        progreso.setTitle("Cargando...");
        progreso.show();
        progreso.setContentView(R.layout.progress_dialog);
        progreso.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }

}
