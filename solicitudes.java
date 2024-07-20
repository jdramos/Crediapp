package com.example.credimovil;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.example.credimovil.modelo.clientesModel;
import com.example.credimovil.modelo.frecuencias;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class solicitudes extends AppCompatActivity {

    Spinner spFrecuencia;
    RequestQueue requestQueue;
    AutoCompleteTextView autoCliente;
    EditText etMontoSolicitado, etPlazo, etCuotaxMes, etTotalCuotas, etTasa, etCuota, etTotalPagar;
    Button btGuardar;
    TextView etCodCli;
    ProgressDialog progreso;
    String spNomSuc, spCodOfi, spToken, spWeb, spEmpresa, url, nomUser,
            urlClientes, rolUser, nombre;
    SharedPreferences preferencias;
    ArrayList<clientesModel> listaClientes = new ArrayList<clientesModel>();
    DecimalFormat precision = new DecimalFormat("#,##0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitudes);

        spFrecuencia = findViewById(R.id.spFrecuencia);
        etCodCli = findViewById(R.id.txtCodCli);
        autoCliente = findViewById(R.id.txtBuscarCliente);
        btGuardar = findViewById(R.id.btGuardar);
        etMontoSolicitado = findViewById(R.id.txtValorPago);
        etPlazo = findViewById(R.id.txtSaldoTot);
        etCuotaxMes = findViewById(R.id.txtCuota);
        etTotalCuotas = findViewById(R.id.txtTotalCuotas);
        etTasa = findViewById(R.id.txtTasa);
        etCuota = findViewById(R.id.txtCuota);
        etTotalPagar = findViewById(R.id.txtTotPagar);

        preferencias = getSharedPreferences("usuario",Context.MODE_PRIVATE);
        spNomSuc = preferencias.getString("numsuc", null);
        spCodOfi = preferencias.getString("codofi", null);
        spToken = preferencias.getString("token", null);
        spWeb = preferencias.getString("web", null);
        spEmpresa = preferencias.getString("empresa", null);
        nomUser = preferencias.getString("nomuser", null);
        rolUser = preferencias.getString("rol", null);
        nombre = preferencias.getString("nombre", null);
        //ArrayAdapter<String> adap = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, countries);
        //autoCliente.setAdapter(adap);


        limpiar();
        url = spWeb+"/"+spEmpresa+"/listar_frecuencias.php";
        urlClientes = spWeb+"/"+spEmpresa+"/listar_clientes.php?codOfi="+spCodOfi+
        "&rol="+rolUser+"&numSuc="+spNomSuc;
        listarFrecuencias(url);
        listarClientes(urlClientes);
        ArrayAdapter<clientesModel> adapterCliente =
                new ArrayAdapter<clientesModel>(getApplicationContext(),
                        android.R.layout.simple_spinner_dropdown_item, listaClientes);

        etMontoSolicitado.addTextChangedListener(new TextWatcher() {
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

        etTasa.addTextChangedListener(new TextWatcher() {
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

        etPlazo.addTextChangedListener(new TextWatcher() {
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

        btGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etCodCli.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"DEBE AGREGAR UN CLIENTE", Toast.LENGTH_SHORT).show();
                    return;
                }

                String validarMonto = etMontoSolicitado.getText().toString().trim();
                String validarPlazo = etPlazo.getText().toString().trim();

                if(!validarMonto.isEmpty() && !validarPlazo.isEmpty()){
                    if(!validarMonto.matches(".")){
                        double x = Double.parseDouble((etMontoSolicitado.getText().toString().trim()));
                        double y = Double.parseDouble((etPlazo.getText().toString().trim()));
                        if(x > 0 && y > 0){
                            AlertDialog.Builder builder = new AlertDialog.Builder(solicitudes.this);
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
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clientesModel miCliente =  (clientesModel) parent.getItemAtPosition(position);
                etCodCli.setText(miCliente.getCodCli());

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

    }

    private void calcularCuota(){
        frecuencias miFrecuencia = (frecuencias) spFrecuencia.getSelectedItem();

        double MontoDouble = Double.parseDouble(etMontoSolicitado.getText().toString()); //Monto del principal
        double plazoDouble = Double.parseDouble(etPlazo.getText().toString()); // Plazo en meses
        double tasaDouble  = Double.parseDouble(etTasa.getText().toString()); //Tasa de interes
        double diasInt     = Double.valueOf(miFrecuencia.getDias()); //Dias por mes
        double totInteres  =  ((MontoDouble*tasaDouble/100)*plazoDouble); //Total interes a pagar
        double totalDeuda  =  MontoDouble+totInteres; // Total deuda Principal + total de intereses
        double totCuotasDouble = diasInt*plazoDouble; // Dias de pagos por mes por cantidad de meses
        double cuotaDouble = totalDeuda/totCuotasDouble; // Valor de la cuota
        etCuotaxMes.setText(String.valueOf(diasInt));
        etTotalCuotas.setText(String.valueOf(totCuotasDouble));
        etCuota.setText(String.format("%.2f", cuotaDouble)) ;
        etTotalPagar.setText(String.format("%.2f", totalDeuda));

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

                        String codcli = (jsonObject.getString("codCli"));
                        String nomcli = (jsonObject.getString("cliente"));
                        String saldoA = (jsonObject.getString("saldoActual"));
                        String fechaActualizacion  = (jsonObject.getString("fechaActualizacion"));
                        String disponibilidad = (jsonObject.getString("DisponibilidadNeta"));
                        String valorGarantia = (jsonObject.getString("ValorGarantia"));


                        //frecuencias[i] = desFre+" || "+diasFre;
                        //listaClientes.add(new clientesModel(nomcli+"-"+codcli));
                        listaClientes.add(new clientesModel(codcli, nomcli, saldoA, fechaActualizacion,  disponibilidad, valorGarantia));

                    }

                    ArrayAdapter<clientesModel> adaptador =
                            new ArrayAdapter<clientesModel>(solicitudes.this,
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
                            new ArrayAdapter<frecuencias>(solicitudes.this,
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
                parametros.put("codCli",etCodCli.getText().toString());
                parametros.put("codOfi",spCodOfi);
                parametros.put("monto",etMontoSolicitado.getText().toString());
                parametros.put("plazo",etPlazo.getText().toString());
                parametros.put("tasa",etTasa.getText().toString());
                parametros.put("frecuencia",spFrecuencia.getSelectedItem().toString());
                parametros.put("tipCre","Represtamo");
                parametros.put("numSuc",spNomSuc);
                parametros.put("token",spToken);
                parametros.put("cuotasxmes", etCuotaxMes.getText().toString());
                parametros.put("nomuser", nomUser);
                parametros.put("nombre", nombre);
                return parametros;
            }
        };
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void limpiar(){
        etCuotaxMes.setText("0");
        autoCliente.setText("");
        etCuota.setText("0");
        etMontoSolicitado.setText("0");
        etTasa.setText("0");
        etPlazo.setText("1");
        etCodCli.setText("");
        //tvNomCli.setText("");
        etTotalCuotas.setText("0");
        etTotalPagar.setText("0");


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
