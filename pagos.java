package com.example.credimovil;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import java.util.HashMap;
import java.util.Map;

public class pagos extends AppCompatActivity {


    EditText etCodCli, etMoraTot, etVencidoTot, etSaldoTot, etValPag;
    TextView tvNomCli, tvVence, tvPeriodi, tvCuota;
    RequestQueue requestQueue;
    Spinner spCodCre;
    Button btGuardar;
    ProgressDialog progreso;
    String celular;
    String spNomSuc, spCodOfi, spToken, spWeb, spEmpresa;
    SharedPreferences preferencias;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagos);

        etCodCli = findViewById(R.id.txtCodCli);
        tvNomCli = findViewById(R.id.txtNombreCliente);
        etValPag = findViewById(R.id.txtValorPago);
        spCodCre = findViewById(R.id.spFrecuencia);
        etMoraTot = findViewById(R.id.txtMoraTot);
        etVencidoTot = findViewById(R.id.txtVencidoTot);
        etSaldoTot = findViewById(R.id.txtSaldoTot);
        btGuardar = findViewById(R.id.btGuardar);
        tvVence = findViewById(R.id.txtVence);
        tvPeriodi = findViewById(R.id.txtPeriodi);
        tvCuota = findViewById(R.id.txtCuota);

        preferencias = getSharedPreferences("usuario",Context.MODE_PRIVATE);
        spNomSuc = preferencias.getString("numsuc", null);
        spCodOfi = preferencias.getString("codofi", null);
        spToken = preferencias.getString("token", null);
        spWeb = preferencias.getString("web", null);
        spEmpresa = preferencias.getString("empresa", null);
        limpiarCampos();

        /*
        String codCliString = getIntent().getStringExtra("codCli");
        String codCreString = getIntent().getStringExtra("codCre");
        etCodCli.setText(codCliString);

        String  [] strcreditos = {codCreString};


                    ArrayAdapter<String> adaptador =
                            new ArrayAdapter<String>(pagos.this,
                                    android.R.layout.simple_spinner_dropdown_item, strcreditos);

                spCodCre.setAdapter(adaptador);
*/


        etCodCli.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (!hasFocus){
                    String codCli = etCodCli.getText().toString();
                    if (!codCli.isEmpty()){
                        String url = spWeb+"/"+spEmpresa+"/buscar_cliente.php?codCli="
                                +etCodCli.getText().toString();
                        buscarCliente(url);
                    }
                }
            }
        });

        btGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etCodCli.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"DEBE AGREGAR UN CLIENTE", Toast.LENGTH_SHORT).show();
                    return;
                }

                String validar = etValPag.getText().toString().trim();

                if(!validar.isEmpty()){
                    if(!validar.matches(".")){
                        double x = Double.parseDouble((etValPag.getText().toString().trim()));
                        if(x > 0){

                            AlertDialog.Builder builder = new AlertDialog.Builder(pagos.this);
                            builder.setMessage("Confirme la operación")
                                    .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //espere();
                                           // guardarPago("https://"+spWeb+"/"+spEmpresa+"/insertar_pago.php");


                                        }
                                    })
                                    .setNegativeButton("Cancelar", null);
                            AlertDialog alert = builder.create();
                            alert.show();
                        }else{
                            Toast.makeText(getApplicationContext(), "Monto a pagar debe ser mayor a cero", Toast.LENGTH_SHORT).show();}
                    }else{
                        Toast.makeText(getApplicationContext(), "Monto a pagar debe ser mayor a cero", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Monto a pagar debe ser mayor a cero", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void guardarPago (String URL){
        espere();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                progreso.dismiss();

                if (response.contains("CONECTADO")){
                    Toast.makeText(getApplicationContext(), "SE HA ABIERTO SERSION EN OTRO DISPOSTIVO, LA SESION EN ESTE DISPOSITIVO SE HA CERRADO", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getApplicationContext(), Login.class);
                    startActivity(i);

                }

                if (response.contains("EXITO")){
                    enviarsms();
                    limpiarCampos();
                    progreso.dismiss();


                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getCause().toString(),Toast.LENGTH_SHORT).show();
                progreso.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> parametros= new HashMap<>();
                parametros.put("codCli",etCodCli.getText().toString());
                parametros.put("codCred",spCodCre.getSelectedItem().toString());
                parametros.put("codOfi",spCodOfi);
                parametros.put("valor",etValPag.getText().toString());
                parametros.put("aplicar","N");
                parametros.put("numSuc",spNomSuc);
                parametros.put("token",spToken);

                return parametros;
            }
        };
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    private void buscarCliente(String URL){

        espere();
        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for(int i =0;i < response.length(); i++){
                    try {
                        SharedPreferences preferencias = getSharedPreferences("usuario",Context.MODE_PRIVATE);
                        String spNomSuc = preferencias.getString("numsuc", null);
                        String codCli = etCodCli.getText().toString();


                        jsonObject = response.getJSONObject(i);

                        String nomCli = (jsonObject.getString("nombres"));
                        String apeCli = (jsonObject.getString("apellidos"));
                        celular = (jsonObject.getString("telefono"));
                        tvNomCli.setText(String.format("%s %s", nomCli, apeCli));

                        buscarCredito(spWeb+"/"+spEmpresa+"/buscar_credito.php?codCli="+codCli+"&numSuc="+spNomSuc);


                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage()  , Toast.LENGTH_SHORT).show();
                        progreso.dismiss();

                    }
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                limpiarCampos();
                etCodCli.requestFocus();
                progreso.dismiss();
                return;

            }
        }
        );
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);

    }

    private void buscarCredito(String URL){

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                String [] creditos = new String[response.length()];

                try {
                for(int i =0;i < response.length(); i++){
                    jsonObject = response.getJSONObject(i);

                    String codCred = (jsonObject.getString("codCredi"));
                    String saldoCap = (jsonObject.getString("saldoCap"));
                    String saldoInt = (jsonObject.getString("saldoInt"));
                    String moraCap = (jsonObject.getString("moraCap"));
                    String moraInt = (jsonObject.getString("moraInt"));
                    String vencidoCap = (jsonObject.getString("vencidoCap"));
                    String vencidoInt = (jsonObject.getString("vencidoInt"));
                    String fechaVence = (jsonObject.getString("fechafin"));
                    String nombreDia = (jsonObject.getString("nomdia"));
                    String periodic = (jsonObject.getString("concurr"));
                    String cuota = (jsonObject.getString("cuota"));

                    Double saldoTot = ((Double.parseDouble(saldoCap)+Double.parseDouble(saldoInt)));
                    Double moraTot = ((Double.parseDouble(moraCap)+Double.parseDouble(moraInt)));
                    Double vencidoTot = ((Double.parseDouble(vencidoCap)+Double.parseDouble(vencidoInt)));
                    Double Cuota = (Double.parseDouble(cuota));

                    DecimalFormat formato = new DecimalFormat();
                    formato.setMaximumFractionDigits(2); //Numero maximo de decimales a mostrar

                    etSaldoTot.setText(String.valueOf(formato.format(saldoTot)));
                    etMoraTot.setText(String.valueOf(formato.format(moraTot)));
                    etVencidoTot.setText(String.valueOf(formato.format(vencidoTot)));
                    tvVence.setText(fechaVence);
                    tvPeriodi.setText(periodic+" - "+nombreDia);
                    tvCuota.setText(String.valueOf(formato.format(Cuota)));


                    creditos[i] = ""+codCred;

                }
                    ArrayAdapter<String> adaptador =
                            new ArrayAdapter<String>(pagos.this,
                                    android.R.layout.simple_spinner_dropdown_item, creditos);

                spCodCre.setAdapter(adaptador);
                    progreso.dismiss();


                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    progreso.dismiss();

                    }





            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                limpiarCampos();
                progreso.dismiss();

            }
        }
        );

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);

    }



    private void limpiarCampos(){
        etCodCli.setText("");
        tvNomCli.setText("");
        etMoraTot.setText("0.00");
        etVencidoTot.setText("0.00");
        etSaldoTot.setText("0.00");
        etValPag.setText("");
        spCodCre.setAdapter(null);
        etCodCli.requestFocus();
        tvPeriodi.setText("");
        tvVence.setText("");
        tvCuota.setText("");


    }

    private void limpiarValores(){
        etMoraTot.setText("");
        etVencidoTot.setText("");
        etSaldoTot.setText("");
     //   spCodCre.setAdapter(null);

    }

    private void enviarsms(){
        String msj = "Su pago de C$"+etValPag.getText().toString()+ " ha sido aplicado";
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(celular, null, msj , null, null);
    }



    public void espere(){
        progreso = new ProgressDialog(pagos.this);
        progreso.setCancelable(false);
        progreso.setTitle("Cargando...");
        progreso.show();
        progreso.setContentView(R.layout.progress_dialog);
        progreso.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


    }

}
