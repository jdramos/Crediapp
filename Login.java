package com.example.credimovil;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Random;
import java.util.TimeZone;

import static android.content.pm.PackageManager.*;

public class Login extends AppCompatActivity {

    String etuser, etpass, imei;
    Button btIngresa;
    RequestQueue requestQueue;
    SharedPreferences sharedPreferences;
    ProgressDialog progreso;
    TextView tvOlvido;
    private TextInputLayout textInputUsuario;
    private TextInputLayout textInputPassword;
    int diaDeSemana;
    TelephonyManager tm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btIngresa = findViewById(R.id.button2);
        textInputUsuario  = findViewById(R.id.text_input_usuario);
        textInputPassword = findViewById(R.id.text_input_password);
        tvOlvido = findViewById(R.id.olvido);


        btIngresa.setOnClickListener(new View.OnClickListener() {
            @Override


            public void onClick(View v) {

                // Check network availability
                if (!NetworkUtils.isNetworkAvailable(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(), "Conexión no disponible, verifique.", Toast.LENGTH_SHORT).show();
                    return;
                }
                etuser = textInputUsuario.getEditText().getText().toString().trim();
                etpass = textInputPassword.getEditText().getText().toString().trim();
                buscarUser("https://www.nicaraguahosting.com/crediapp/validaruser2.php?nomUser="+ etuser + "&pasUser=" + etpass +"&token="+aleatorio());
                //buscarUser("https://www.nicaraguahosting.com/crediapp/validaruser3.php?nomUser="+ etuser + "&pasUser=" + etpass +"&token="+aleatorio());

                
              
            }
        });
        /*
        if (networkInfo == null && !networkInfo.isAvailable()) {
            Snackbar.make(btIngresa, "Revise su conexion", BaseTransientBottomBar.LENGTH_SHORT).show();
        } else {
            // No hay conexión a Internet en este momento
        }

         */
        tvOlvido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (textInputUsuario.getEditText().getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Ingrese su nombre de usuario", Toast.LENGTH_SHORT).show();
                }else{
                    SharedPreferences preferences = getSharedPreferences("usuario",
                            Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("nomuser", etuser);
                    Intent intent = new Intent(getApplicationContext(), CambiarClave.class);
                    startActivity(intent);
                }

            }
        });


    }


    private void buscarUser(String URL) {
        espere();
        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(JSONArray response) {


                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {

                        jsonObject = response.getJSONObject(i);

                        String nomUser = (jsonObject.getString("nomUser"));
                        String nombre = (jsonObject.getString("nombre"));
                        String pasUser = (jsonObject.getString("pasUser"));
                        String numSuc  = (jsonObject.getString("numSuc"));
                        String codOfi  = (jsonObject.getString("codOfi"));
                        String ofiCob  = (jsonObject.getString("ofiCobro"));
                        String tokenO  = (jsonObject.getString("token"));
                        String empresa = (jsonObject.getString("empresa"));
                        String web     = (jsonObject.getString("web"));
                        String rol     = (jsonObject.getString("rolUser"));
                        String mnAutoriza    = (jsonObject.getString("mnAutoriza"));
                        String mnCobros    = (jsonObject.getString("mnCobros"));
                        String cambioClave    = (jsonObject.getString("cambioClave"));
                        String telSuperv    = (jsonObject.getString("telSupervisor"));
                        String todasSuc    = (jsonObject.getString("todasSuc"));
                        String verTotalCobros    = (jsonObject.getString("verTotalCobros"));





                        if(response.length()>0){

                            SharedPreferences preferences = getSharedPreferences("usuario",
                                    Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("nomuser", nomUser);
                            editor.putString("nombre", nombre);
                            editor.putString("codofi", codOfi);
                            editor.putString("ofiCobro", ofiCob);
                            editor.putString("numsuc", numSuc);
                            editor.putString("token", tokenO);
                            editor.putString("empresa", empresa);
                            editor.putString("web", web);
                            editor.putString("rol", rol);
                            editor.putString("mnAutoriza", mnAutoriza);
                            editor.putString("mnCobros", mnCobros);
                            editor.putString("cambioClave", cambioClave);
                            editor.putString("telSupervisor", telSuperv);
                            editor.putString("todasSuc", todasSuc);
                            editor.putString("verTotalCobros", verTotalCobros);
                            editor.commit();

                            //Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            if (cambioClave.equals("N")){
                                Intent intent = new Intent(getApplicationContext(), CambiarClave.class);
                                startActivity(intent);
                            }else {
                                Intent intent = new Intent(getApplicationContext(), Dashbord.class);
                                startActivity(intent);
                            }

                            finish();
                            progreso.dismiss();
                        }
                    } catch (JSONException e) {

                        if(e.getMessage().toString().contains("APPINACTIVA")){

                            Toast.makeText(getApplicationContext(), "HORARIO FUERA DEL RANGO PERMITIDO",
                                    Toast.LENGTH_SHORT).show();
                        }

                        if(e.getMessage().toString().contains("PASSNO")){

                            Toast.makeText(getApplicationContext(), "USUARIO O CONSTRASEÑA NO VALIDO",
                                    Toast.LENGTH_SHORT).show();
                        }

                        if(e.getMessage().toString().contains("USERNO")){

                            Toast.makeText(getApplicationContext(), "USUARIO O CONSTRASEÑA NO VALIDO",
                                    Toast.LENGTH_SHORT).show();
                        }

                        progreso.dismiss();
                        return;
                    }
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                progreso.dismiss();
                return;

            }
        }
        );
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);

    }

    public void espere(){
        progreso = new ProgressDialog(this);
        progreso.setCancelable(false);
        progreso.setTitle("Cargando...");
        progreso.show();
        progreso.setContentView(R.layout.progress_dialog);
        progreso.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    
    private StringBuffer aleatorio(){
        char [] chars = "0123456789abcdefghijklmnopqrstuvwxyz".toCharArray();
        int charsLength = chars.length;         // Longitud del array de char.
        Random random = new Random();   // Instanciamos la clase Random
        StringBuffer buffer = new StringBuffer(); // Un StringBuffer para componer la cadena aleatoria de forma eficiente
        for (int i=0;i<10;i++){  // Bucle para elegir una cadena de 10 caracteres al azar
            buffer.append(chars[random.nextInt(charsLength)]);            // Añadimos al buffer un caracter al azar del array
        }
        return buffer;
    }




}