package com.example.credimovil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.credimovil.modelo.clientesModel;
import com.example.credimovil.modelo.frecuencias;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class solicitudes_agregar extends AppCompatActivity {

    Spinner spFrecuencia;
    TextView tvErrorCuota, tvCuota;
    RequestQueue requestQueue;
    AutoCompleteTextView autoCliente;
    EditText    etTotalPagar, txtCuota,
                txtPlazo, txtTasa,
                txtCapacidadPago, txtCoberturaGtia, txtConsultaCrediticia,
                txtRecordInterno, observa, saldoActual, txtFechaActualizacion;
    Button btGuardar;
    ProgressDialog progreso;
    String spNomSuc, spCodOfi, spToken, spWeb, spEmpresa, url, nomUser,
            urlClientes, rolUser, nombre, codcli, strSaldoActual, fechaActualizacion;
    SharedPreferences preferencias;
    ArrayList<clientesModel> listaClientes = new ArrayList<clientesModel>();
    DecimalFormat precision = new DecimalFormat("#,##0.00");
    double montoDouble;
    float anios;

    TextInputLayout textInputMontoSol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.solicitud_agregar);

        spFrecuencia = findViewById(R.id.spFrecuencia);
        autoCliente = findViewById(R.id.txtBuscarCliente);
        btGuardar = findViewById(R.id.btGuardar);
        //txtMontoSolicitado = findViewById(R.id.txtMontoSol);
        txtPlazo = findViewById(R.id.txtPlazo);
        //etCuotaxMes = findViewById(R.id.txtCuota);
        txtTasa = findViewById(R.id.txtTasa);
        etTotalPagar = findViewById(R.id.txtTotPagar);
        txtCapacidadPago = findViewById(R.id.txtCapacidadPago);
        txtCoberturaGtia = findViewById(R.id.txtCoberturaGarantia);
        txtConsultaCrediticia = findViewById(R.id.txtConsultaCretidicia);
        txtRecordInterno = findViewById(R.id.txtRecordInterno);
        observa = findViewById(R.id.txtComentarios);
        saldoActual = findViewById(R.id.txtSaldoActual);
        txtFechaActualizacion = findViewById(R.id.txtFechaActualizacion);
        textInputMontoSol = findViewById(R.id.txtMontoSol);
        txtCuota = findViewById(R.id.txtCuota);
        tvErrorCuota = findViewById(R.id.tvErrorCuota);
        tvCuota = findViewById(R.id.tvCuota);

        preferencias = getSharedPreferences("usuario",Context.MODE_PRIVATE);
        spNomSuc = preferencias.getString("numsuc", null);
        spCodOfi = preferencias.getString("codofi", null);
        spToken = preferencias.getString("token", null);
        spWeb = preferencias.getString("web", null);
        spEmpresa = preferencias.getString("empresa", null);
        nomUser = preferencias.getString("nomuser", null);
        rolUser = preferencias.getString("rol", null);
        nombre = preferencias.getString("nombre", null);


        limpiar();
        url = spWeb+"/"+spEmpresa+"/listar_frecuencias.php";
        urlClientes = spWeb+"/"+spEmpresa+"/listar_clientes.php?codOfi="+spCodOfi+
        "&rol="+rolUser+"&numSuc="+spNomSuc;
        listarFrecuencias(url);
        listarClientes(urlClientes);
        final ArrayAdapter<clientesModel> adapterCliente =
                new ArrayAdapter<clientesModel>(getApplicationContext(),
                        android.R.layout.simple_spinner_dropdown_item, listaClientes);

        btGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String validarMonto = textInputMontoSol.getEditText().getText().toString().trim();
                String validarPlazo = txtPlazo.getText().toString();

                if(!validarMonto.isEmpty() && !validarPlazo.isEmpty()){
                    if(!validarMonto.matches(".")){
                        double x = Double.parseDouble((validarMonto));
                        double y = Double.parseDouble((validarPlazo));
                        if(x > 0 && y > 0){
                            AlertDialog.Builder builder = new AlertDialog.Builder(solicitudes_agregar.this);
                            builder.setMessage("Confirme la operación")
                                    .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            url = spWeb+"/"+spEmpresa+"/insertar_solicitud.php";
                                            guardarSolicitud(url);

                                        }
                                    })
                                    .setNegativeButton("Cancelar", null);

                            AlertDialog alert = builder.create();
                            alert.show();
                        }else{
                            Toast.makeText(getApplicationContext(), "Monto y/o plazo debe ser mayor a cero", Toast.LENGTH_SHORT).show();}
                    }else{
                        Toast.makeText(getApplicationContext(), "Monto y/o plazo debe ser mayor a cero", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Monto y/o plazo debe ser mayor a cero", Toast.LENGTH_SHORT).show();
                }

            }


        });



        autoCliente.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                clientesModel miCliente =  (clientesModel) parent.getItemAtPosition(position);

                    codcli = miCliente.getCodCli();
                    fechaActualizacion = miCliente.getFechaActualizacion();
                    strSaldoActual = miCliente.getSaldoActual();
                    saldoActual.setText(strSaldoActual);
                    txtCoberturaGtia.setText(miCliente.getValorGarantia());
                    txtCapacidadPago.setText(miCliente.getDisponibilidad());
                    txtFechaActualizacion.setText(fechaActualizacion);
            }
        });



        spFrecuencia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {

              calcularCuota();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView)
            {
                //Nada seleccionado
            }
        });

        txtTasa.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                if (s.toString().equals("")){
                    btGuardar.setEnabled(false);
                }else{
                    btGuardar.setEnabled(true);
                }
                if (count>0){
                    calcularCuota();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        txtPlazo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                if (count>0){
                    calcularCuota();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        textInputMontoSol.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count>0){
                    calcularCuota();
                }


            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0){
                    calcularCuota();
                }

            }
        });




        /*
        textInputMontoSol.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (count>0){
                    calcularCuota();
                }

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (count>0){
                    calcularCuota();
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
 */
    }



    private void calcularCuota(){
        frecuencias miFrecuencia = (frecuencias) spFrecuencia.getSelectedItem();

        montoDouble = Double.parseDouble(textInputMontoSol.getEditText().getText().toString().trim()); //Monto del principal
        double plazoDouble = Double.parseDouble(txtPlazo.getText().toString()); // Plazo en meses
        double tasaDouble  = Double.parseDouble(txtTasa.getText().toString()); //Tasa de interes
        double diasInt     = Double.valueOf(miFrecuencia.getDias()); //Dias por mes
        double totInteres  =  ((montoDouble*tasaDouble/100)*plazoDouble); //Total interes a pagar
        double totalDeuda  =  montoDouble+totInteres; // Total deuda Principal + total de intereses
        double totCuotasDouble = diasInt*plazoDouble; // Dias de pagos por mes por cantidad de meses
        double cuotaDouble = totalDeuda/totCuotasDouble; // Valor de la cuota
        double coberturaGtia = Double.parseDouble(txtCoberturaGtia.getText().toString());
        double disponibilidad = Double.parseDouble(txtCapacidadPago.getText().toString());

        txtCuota.setText(String.valueOf(cuotaDouble));
        etTotalPagar.setText(String.format("%.2f", totalDeuda));

        if(montoDouble > 0 && montoDouble <=50000){
            if(((coberturaGtia/montoDouble)*100)<80){
                textInputMontoSol.setError("Garantia no cubre el 80% del monto solicitado");
            }else{
                textInputMontoSol.setError(null);
            }

            if(((cuotaDouble/disponibilidad)*100)>80){
                tvErrorCuota.setVisibility(View.VISIBLE);
                tvErrorCuota.setText("La capacidad de pago excede el 80%");
                tvCuota.setTextColor(Color.RED);
            }else{
                tvErrorCuota.setVisibility(View.GONE);
                tvCuota.setTextColor(getResources().getColor(R.color.blue));
            }

        }
        if(montoDouble > 50000){
            if(((coberturaGtia/montoDouble)*100)<100){
                textInputMontoSol.setError("Garantia no cubre el 100% del monto");
            }else{
                textInputMontoSol.setError(null);
            }

            if(((cuotaDouble/coberturaGtia)*100)>80){
                tvErrorCuota.setVisibility(View.VISIBLE);
                tvErrorCuota.setText("La capacidad de pago excede el 80%");
                tvCuota.setTextColor(Color.RED);
            }else{
                tvErrorCuota.setVisibility(View.GONE);
                tvCuota.setTextColor(getResources().getColor(R.color.blue));
            }

        }



    }

    private void listarClientes(String URL){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                // String [] frecuencias = new String[response.length()];
                ArrayList<clientesModel> listaClientes = new ArrayList<clientesModel>();


                try {
                    for(int i =0;i < response.length(); i++){
                        jsonObject = response.getJSONObject(i);

                        String codcli       = (jsonObject.getString("codCli"));
                        String nomcli       = (jsonObject.getString("cliente"));
                        String saldoActual  = (jsonObject.getString("saldoActual"));
                        String fechaActualiz = (jsonObject.getString("fechaActualizacion"));
                        String disponibilidad = (jsonObject.getString("DisponibilidadNeta"));
                        String valorGarantia = (jsonObject.getString("ValorGarantia"));



                        //frecuencias[i] = desFre+" || "+diasFre;
                        //listaClientes.add(new clientesModel(nomcli+"-"+codcli));
                        listaClientes.add(new clientesModel(codcli, nomcli, saldoActual, fechaActualiz, disponibilidad, valorGarantia));

                    }

                    ArrayAdapter<clientesModel> adaptador =
                            new ArrayAdapter<clientesModel>(solicitudes_agregar.this,
                                    android.R.layout.simple_spinner_dropdown_item, listaClientes);

                    autoCliente.setAdapter(adaptador);


                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                //limpiarCampos();

            }
        }
        );

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);

    }

    private void listarFrecuencias(String URL){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
            // String [] frecuencias = new String[response.length()];
                ArrayList<frecuencias> lista = new ArrayList<frecuencias>();
                try {
                    for(int i =0;i < response.length(); i++){
                        jsonObject = response.getJSONObject(i);

                        String desFre = (jsonObject.getString("desFre"));
                        String diasFre = (jsonObject.getString("dias"));
                        Integer diasInt = Integer.parseInt(diasFre);
                       //frecuencias[i] = desFre+" || "+diasFre;
                        lista.add(new frecuencias(diasInt, desFre));

                    }
                    ArrayAdapter<frecuencias> adaptador =
                            new ArrayAdapter<frecuencias>(solicitudes_agregar.this,
                                    android.R.layout.simple_spinner_dropdown_item, lista);

                    spFrecuencia.setAdapter(adaptador);

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                //limpiarCampos();

            }
        }
        );

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);

    }



    private void guardarSolicitud (String URL){
        espere();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();

                progreso.dismiss();

                if (response.contains("CONECTADO")){
                    Toast.makeText(getApplicationContext(), "SE HA ABIERTO SERSION EN OTRO DISPOSTIVO, LA SESION EN ESTE DISPOSITIVO SE HA CERRADO", Toast.LENGTH_LONG).show();
                    finishAffinity();
                    Intent i = new Intent(getApplicationContext(), Login.class);
                    startActivity(i);

                }


                if (response.contains("EXITO")){
                    limpiar();
                    progreso.dismiss();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                SharedPreferences preferencias = getSharedPreferences("usuario", Context.MODE_PRIVATE);
                String spNomSuc = preferencias.getString("numsuc", null);
                String spCodOfi = preferencias.getString("codofi", null);
                String spToken = preferencias.getString("token", null);

                Map<String, String> parametros= new HashMap<>();
                parametros.put("codCli", codcli);
                parametros.put("codOfi", spCodOfi);
                parametros.put("monto", textInputMontoSol.getEditText().getText().toString().trim());
                parametros.put("plazo", txtPlazo.getText().toString());
                parametros.put("tasa", txtTasa.getText().toString());
                parametros.put("frecuencia",spFrecuencia.getSelectedItem().toString());
                parametros.put("tipCre","Represtamo");
                parametros.put("numSuc",spNomSuc);
                parametros.put("token",spToken);
                parametros.put("cuotasxmes", txtCuota.getText().toString());
                parametros.put("nomuser", nomUser);
                parametros.put("nombre", nombre);
                parametros.put("observa", observa.getText().toString());
                parametros.put("consultaCr", txtConsultaCrediticia.getText().toString());
                parametros.put("capacidadP", txtCapacidadPago.getText().toString());
                parametros.put("coberturaG", txtCoberturaGtia.getText().toString());
                parametros.put("recordInt", txtRecordInterno.getText().toString());
                return parametros;
            }
        };
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private void limpiar(){
        txtCuota.setText("0");
        autoCliente.setText("");
        textInputMontoSol.getEditText().setText("0");
        txtTasa.setText("0");
        txtPlazo.setText("1");
        //tvNomCli.setText("");
        etTotalPagar.setText("0");
        txtCapacidadPago.setText("0");
        txtCoberturaGtia.setText("0");
        txtConsultaCrediticia.setText("0");
        txtRecordInterno.setText("0");
        observa.setText("");
    }

    private void calcularDias(){

        solicitud_acciones sa = new solicitud_acciones();
        sa.calcularDias();
        float anios= sa.anios;

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
