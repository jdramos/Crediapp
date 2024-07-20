package com.example.credimovil;

import static com.android.volley.Response.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.TimeInterpolator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.credimovil.modelo.frecuencias;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class solicitud_acciones extends AppCompatActivity {


    Spinner spFrecuencia;
    TextView tvTitulo,
             txtFrecuencia,
            tvSolicitadopor, tvAprobadopor, tvAplicadopor;
    EditText txtMontoSol, txtPlazo, txtTasa, txtCliente, txtSaldoActual, txtCuota,
            txtTotPagar, txtCapacidadPago, txtCoberturaGarantia, txtconsultaCrediticia,
            txtRecordInterno, txtComentarios, txtFechaActualizacion;
    Button btnAprobar, btnRechazar, btnEliminar, btnGuardar;
    String idSolicitud, codCli, nomCli, fechaSol, monto, plazo, tasa,
            frecuencia, solicitadopor, aprobadopor, aplicadopor,
            fechaAprob, fechaApli, url, nomuser, spWeb, spEmpresa, spToken, spCodOfi,
            telSupervisor, rol, telefono, accion, montoaprobado,
            tasaAprobada, plazoaprobado, aprobada, idFrecuencia, comentarios,
          consultaCrediticia, capacidadPago, coberturaGtia, recordInterno, saldoActual,
            fechaActualizacion, disponibilidad, valorGarantia;
    TextView tvErrorMonto, tvErrorFecha, tvErrorCuota;
    RequestQueue requestQueue;
    SharedPreferences preferencias;
    DecimalFormat precision = new DecimalFormat("#,##0.00");
    FloatingActionButton floatMenu, floatSave, floatDelete, floatReject;
    Float translationYaxis = 100f, anios;
    Boolean menuOpen = false;
    TimeInterpolator interporlator;
    ProgressDialog progreso;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitud_acciones);

        initializeViews();


        preferencias = getSharedPreferences("usuario", Context.MODE_PRIVATE);
        nomuser = preferencias.getString("nombre", null);
        spWeb = preferencias.getString("web", null);
        spEmpresa = preferencias.getString("empresa", null);
        telSupervisor = preferencias.getString("telSupervisor", null);
        rol = preferencias.getString("rol", null);
        spToken = preferencias.getString("token", null);
        spCodOfi = preferencias.getString("codofi", null);

        idSolicitud = getIntent().getStringExtra("idSolicitud");
        codCli = getIntent().getStringExtra("codCli");
        nomCli = getIntent().getStringExtra("nomCli");
        plazo = getIntent().getStringExtra("plazo");
        tasa = getIntent().getStringExtra("tasa");
        frecuencia = getIntent().getStringExtra("frecuencia");
        idFrecuencia = getIntent().getStringExtra("idFrecuencia");
        solicitadopor = getIntent().getStringExtra("solicitadopor");
        aprobadopor = getIntent().getStringExtra("aprobadopor");
        aplicadopor = getIntent().getStringExtra("aplicadopor");
        monto = getIntent().getStringExtra("montoSol");
        fechaSol = getIntent().getStringExtra("fechaSol");
        fechaAprob = getIntent().getStringExtra("fechaAprob");
        fechaApli = getIntent().getStringExtra("fechaApli");
        telefono = getIntent().getStringExtra("telefono");
        aprobada = getIntent().getStringExtra("aprobada");
        comentarios = getIntent().getStringExtra("comentarios");
        consultaCrediticia = getIntent().getStringExtra("consultaCrediticia");
        capacidadPago = getIntent().getStringExtra("capacidadPago");
        coberturaGtia = getIntent().getStringExtra("coberturaGtia");
        recordInterno = getIntent().getStringExtra("recordInterno");
        saldoActual = getIntent().getStringExtra("saldoActual");
        fechaActualizacion = getIntent().getStringExtra("fechaActualizacion");
        disponibilidad = getIntent().getStringExtra("disponibilidadNeta");
        valorGarantia = getIntent().getStringExtra("valorGarantia");

        //Toast.makeText(getApplicationContext(), "entrando " + valorGarantia, Toast.LENGTH_SHORT).show();


        tvTitulo.setText("Solicitud de crédito Nº: " + idSolicitud);
        txtCliente.setText(codCli + "-" + nomCli);
        txtPlazo.setText(plazo);
        txtTasa.setText(tasa);
        txtComentarios.setText(comentarios);
        txtCapacidadPago.setText(precision.format(Double.parseDouble(disponibilidad)));
        txtCoberturaGarantia.setText(precision.format(Double.parseDouble(valorGarantia)));
        txtconsultaCrediticia.setText(consultaCrediticia);
        txtRecordInterno.setText(recordInterno);
        txtSaldoActual.setText(saldoActual);
        txtFechaActualizacion.setText(fechaActualizacion);
        /*
        if (frecuencia.equals("D")){
            spFrecuencia.set;
        }else if(frecuencia.equals("S")){
            txtFrecuencia.setText("Semanal");
        }else if(frecuencia.equals("M")){
            txtFrecuencia.setText("Mensual");
        }else if(frecuencia.equals("B")){
            txtFrecuencia.setText("Bisemanal");
        }else if(frecuencia.equals("V")){
            txtFrecuencia.setText("Al Vencimiento");
        }

         */
        url = spWeb + "/" + spEmpresa + "/listar_frecuencias.php";

        listarFrecuencias(url);
        showMenu();
        calcularDias();



        //calcularCuota();
        double dblMonto = Double.parseDouble(monto);
        txtMontoSol.setText(monto);
        /* tvSolicitadopor.setText("Solicitado: "+solicitadopor +" | " +fechaSol);
        tvAprobadopor.setText("Aprobado:"+aprobadopor+ " | "+fechaAprob);
        tvAplicadopor.setText("Aplicado:"+aplicadopor+ " | "+fechaApli);


         */
        this.setTitle("Detalle de solicitud " + idSolicitud);
        this.setTitleColor(R.color.blue);

        if (rol.equals("3")){
            floatMenu.setVisibility(View.GONE);
        }

        spFrecuencia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
               calcularCuota();
                //frecuencias frec = (frecuencias) spFrecuencia.getSelectedItem();
               //Toast.makeText(getApplicationContext(), String.valueOf(frec.getDias()),Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView)
            {
                //Nada seleccionado
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

        txtMontoSol.addTextChangedListener(new TextWatcher() {
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
                if (s.length()>0){
                    calcularCuota();
                }

            }
        });

        txtTasa.addTextChangedListener(new TextWatcher() {
                   @Override
                   public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                   }

                   @Override
                   public void onTextChanged(CharSequence s, int start, int before, int count) {


                       if (s.toString().equals("")) btnGuardar.setEnabled(false);
                       else{
                           btnGuardar.setEnabled(true);
                       }
                       if (count>0) calcularCuota();

                   }

                   @Override
                   public void afterTextChanged(Editable s) {

                   }
        });

        floatMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (menuOpen){
                    closeMenu();
                }else{
                    openMenu();
                }

            }
        });


        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (anios >= 1){
                    Toast.makeText(getApplicationContext(),"FECHA DE ACTUALIZACION DE EXPEDIENTE " +
                            "MAYOR A UN AÑO, NO PUEDE PROCEDER", Toast.LENGTH_SHORT).show();
                    return;
                }


                String validarMonto = txtMontoSol.getText().toString();
                String validarPlazo = txtPlazo.getText().toString();

                if(!validarMonto.isEmpty() && !validarPlazo.isEmpty()){
                    if(!validarMonto.matches(".")){
                        double x = Double.parseDouble((validarMonto));
                        double y = Double.parseDouble((validarPlazo));
                        if(x > 0 && y > 0){
                            AlertDialog.Builder builder = new AlertDialog.Builder(solicitud_acciones.this);
                            builder.setMessage("Confirme la operación")
                                    .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            url = spWeb+"/"+spEmpresa+"/actualizar_solicitud.php";
                                            actualizarSolicitud(url);

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

        floatSave.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(solicitud_acciones.this);
            builder.setMessage("Confirme aprobar solicitud")
                    .setPositiveButton("Confirmar", (dialog, which) -> {
                        accion = "A";
                        plazoaprobado = txtPlazo.getText().toString();
                        tasaAprobada = txtTasa.getText().toString();
                        montoaprobado = txtMontoSol.getText().toString();
                       // url = spWeb+"/"+spEmpresa+"/solicitud_acciones.php";
                        url = "https://www.nicaraguahosting.com/nestor/solicitud_acciones.php?" +
                                "idSolicitud=" + idSolicitud +
                                "&accion=" + accion +
                                "&rol=" + rol +
                                "&user=" + nomuser +
                                "&montoaprobado=" + montoaprobado +
                                "&tasaaprobada=" + tasaAprobada +
                                "&plazoaprobado=" + plazoaprobado;
                        accionSolicitud(url);
                    })
                    .setNegativeButton("Cancelar", null);

            AlertDialog alert = builder.create();
            alert.show();


        });

        floatDelete.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(solicitud_acciones.this);
            builder.setMessage("Confirme eliminar solicitud")
                    .setPositiveButton("Confirmar", (dialog, which) -> {
                        accion = "E";
                        plazoaprobado = "0";
                        tasaAprobada = "0";
                        montoaprobado = "0";
                       // url = spWeb+"/"+spEmpresa+"/solicitud_acciones.php";
                        url = "https://www.nicaraguahosting.com/nestor/solicitud_acciones.php?" +
                                "idSolicitud=" + idSolicitud +
                                "&accion=" + accion +
                                "&rol=" + rol +
                                "&user=" + nomuser +
                                "&montoaprobado=" + montoaprobado +
                                "&tasaaprobada=" + tasaAprobada +
                                "&plazoaprobado=" + plazoaprobado;
                        accionSolicitud(url);
                    })
                    .setNegativeButton("Cancelar", null);

            AlertDialog alert = builder.create();
            alert.show();


        });

        floatReject.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(solicitud_acciones.this);
            builder.setMessage("Confirme rechazar solicitud")
                    .setPositiveButton("Confirmar", (dialog, which) -> {
                        accion = "R";
                        plazoaprobado = "0";
                        tasaAprobada = "0";
                        montoaprobado = "0";
                       // url = spWeb+"/"+spEmpresa+"/solicitud_acciones.php";
                        url = "https://www.nicaraguahosting.com/nestor/solicitud_acciones.php?" +
                                "idSolicitud=" + idSolicitud +
                                "&accion=" + accion +
                                "&rol=" + rol +
                                "&user=" +nomuser +
                                "&montoaprobado=" + montoaprobado +
                                "&tasaaprobada=" + tasaAprobada +
                                "&plazoaprobado=" + plazoaprobado;
                        accionSolicitud( url);
                    })
                    .setNegativeButton("Cancelar", null);
            AlertDialog alert = builder.create();
            alert.show();
        });

    }

    private  void initializeViews(){

        tvTitulo = findViewById(R.id.textViewTitulo);
        txtCliente = findViewById(R.id.txtCliente);
        txtSaldoActual = findViewById(R.id.txtSaldoActual);
        txtMontoSol = findViewById(R.id.txtMontoSol);
        txtTasa = findViewById(R.id.txtTasa);
        txtPlazo = findViewById(R.id.txtPlazo);
        spFrecuencia = findViewById(R.id.spFrecuencia);
        txtCuota = findViewById(R.id.txtCuota);
        txtTotPagar = findViewById(R.id.txtTotPagar);
        txtCapacidadPago = findViewById(R.id.txtCapacidadPago);
        txtCoberturaGarantia = findViewById(R.id.txtCoberturaGarantia);
        txtconsultaCrediticia = findViewById(R.id.txtConsultaCretidicia);
        txtRecordInterno = findViewById(R.id.txtRecordInterno);
        txtComentarios = findViewById(R.id.txtComentarios);
        txtFechaActualizacion = findViewById(R.id.txtFechaActualizacion);
        floatMenu = findViewById(R.id.floatMenu);
        floatSave = findViewById(R.id.fiSave);
        floatReject = findViewById(R.id.fiReject);
        floatDelete = findViewById(R.id.fiDelete);
        tvErrorMonto = findViewById(R.id.tvErrorMonto);
        tvErrorCuota = findViewById(R.id.tvErrorCuota);
        tvErrorFecha = findViewById(R.id.tvErrorFecha);

        btnGuardar = findViewById(R.id.btGuardar);
    }

    private void showMenu() {
        floatSave.setAlpha(0f);
        floatReject.setAlpha(0f);
        floatDelete.setAlpha(0f);

        floatSave.setTranslationY(translationYaxis);
        floatReject.setTranslationY(translationYaxis);
        floatDelete.setTranslationY(translationYaxis);
    }

    public void calcularDias(){
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String currentDate = dateFormat.format(new Date());

            Date dateStart = dateFormat.parse(fechaActualizacion);
            Date dateEnd = dateFormat.parse(String.valueOf(currentDate));

            long difference = Math.abs(dateEnd.getTime() - dateStart.getTime());

            difference = difference / (60 * 60 * 1000); // Horas
            difference = difference / 24; // Dias

            anios = Float.parseFloat(String.valueOf(difference / 365));

            //Toast.makeText(getApplicationContext(), String.valueOf(anios),Toast.LENGTH_SHORT).show();

            if(anios>= 1){
                tvErrorFecha.setVisibility(View.VISIBLE);
                txtFechaActualizacion.setTextColor(getResources().getColor(R.color.vencido));

            }else{
                tvErrorFecha.setVisibility(View.INVISIBLE);
                txtFechaActualizacion.setTextColor(getResources().getColor(R.color.blue));
            }

        }catch(Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void closeMenu() {
        menuOpen = !menuOpen;
        floatMenu.setImageResource(R.drawable.menu);

        floatSave.animate().translationY(translationYaxis).alpha(0f).setInterpolator(interporlator).setDuration(100).start();
        floatDelete.animate().translationY(translationYaxis).alpha(0f).setInterpolator(interporlator).setDuration(100).start();
        floatReject.animate().translationY(translationYaxis).alpha(0f).setInterpolator(interporlator).setDuration(100).start();
    }

    private void openMenu(){
        menuOpen = !menuOpen;
        floatMenu.setImageResource(R.drawable.ic_baseline_menu_open_24);

        floatSave.animate().translationY(0f).alpha(1f).setInterpolator(interporlator).setDuration(100).start();
        floatDelete.animate().translationY(0f).alpha(1f).setInterpolator(interporlator).setDuration(100).start();
        floatReject.animate().translationY(0f).alpha(1f).setInterpolator(interporlator).setDuration(100).start();
    }

    private void accionSolicitud(String URL) {
        Log.e("TAG_URL", URL );
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,

                response -> {

                    if (response.contains("CONECTADO")) {
                        Toast.makeText(getApplicationContext(), "SE HA ABIERTO SESION EN OTRO DISPOSITIVO, LA SESION EN ESTE DISPOSITIVO SE HA CERRADO", Toast.LENGTH_LONG).show();
                        finish();
                        Intent i = new Intent(getApplicationContext(), Login.class);
                        startActivity(i);
                    } else if (response.contains("Eliminado")) {
                        Toast.makeText(getApplicationContext(), "Solicitud ha sido eliminada", Toast.LENGTH_LONG).show();
                    } else if (response.contains("Aprobado")) {
                        Toast.makeText(getApplicationContext(), "Solicitud ha sido aprobada", Toast.LENGTH_LONG).show();
                    } else if (response.contains("Rechazado")) {
                        Toast.makeText(getApplicationContext(), "Solicitud ha sido rechazada", Toast.LENGTH_LONG).show();
                    } else if (response.contains("VACIO")) {
                        Toast.makeText(getApplicationContext(), "OCURRIO UN ERROR, NO SE PUDO COMPLETAR LA ACCION: " + response, Toast.LENGTH_LONG).show();
                        return;
                    }
                    solicitud_acciones.super.getOnBackPressedDispatcher().onBackPressed();

                },
                error -> {
                    if (error instanceof NoConnectionError) {
                        Toast.makeText(getApplicationContext(), "Sin conexión a internet", Toast.LENGTH_LONG).show();
                    } else if (error instanceof TimeoutError) {
                        Toast.makeText(getApplicationContext(), "Tiempo de respuesta agotado", Toast.LENGTH_LONG).show();
                    } else if (error instanceof AuthFailureError) {
                        Toast.makeText(getApplicationContext(), "Error de autenticación", Toast.LENGTH_LONG).show();
                    } else if (error instanceof ServerError) {
                        Toast.makeText(getApplicationContext(), "Error del servidor "+ error.getMessage(), Toast.LENGTH_LONG).show();
                    } else if (error instanceof NetworkError) {
                        Toast.makeText(getApplicationContext(), "Error de red", Toast.LENGTH_LONG).show();
                    } else if (error instanceof ParseError) {
                        Toast.makeText(getApplicationContext(), "Response parsing error", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Error desconocido: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    // Registrar el error para un mejor debugging
                    Log.e("VolleyError", "Error: " + error.getMessage(), error);
                }
        ) {
            @NonNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("idSolicitud", idSolicitud);
                parametros.put("accion", accion);
                parametros.put("user", nomuser);
                parametros.put("rol", rol);
                parametros.put("usermontoaprobado", montoaprobado);
                parametros.put("tasaaprobada", tasaAprobada);
                parametros.put("plazoaprobado", plazoaprobado);
                return parametros;
            }
        };

        requestQueue  = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        Log.e("VolleyError", "Error: " + requestQueue.toString());
    }

    private void enviarsms(String celular){
        String msj = "Estimado Sr(a): " + nomCli + "  credito aprobadopor C$"+monto+
                ". "+spEmpresa.toUpperCase();

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(celular, null, msj , null, null);
    }

    private void calcularCuota() {

        frecuencias miFrecuencia = (frecuencias) spFrecuencia.getSelectedItem();


        double montoDouble = Double.parseDouble(txtMontoSol.getText().toString()); //Monto del principal
        double plazoDouble = Double.parseDouble(txtPlazo.getText().toString()); // Plazo en meses
        double tasaDouble = Double.parseDouble(txtTasa.getText().toString()); //Tasa de interes
        double diasInt = Double.valueOf(miFrecuencia.getDias()); //Dias por mes
        double totInteres = ((montoDouble * tasaDouble / 100) * plazoDouble); //Total interes a pagar
        double totalDeuda = montoDouble + totInteres; // Total deuda Principal + total de intereses
        double totCuotasDouble = diasInt * plazoDouble; // Dias de pagos por mes por cantidad de meses
        double cuotaDouble = totalDeuda / totCuotasDouble; // Valor de la cuota
        double coberturaGtia = Double.parseDouble(valorGarantia);
        double disponibilidad = Double.parseDouble(capacidadPago);
        txtCuota.setText(precision.format(cuotaDouble));
        txtTotPagar.setText(precision.format(totalDeuda));

        if (montoDouble > 0 && montoDouble <= 50000) {
            if (((coberturaGtia/ montoDouble ) * 100) < 80) {
                tvErrorMonto.setVisibility(View.VISIBLE);
            } else {
                tvErrorMonto.setVisibility(View.INVISIBLE);
            }

            if (((cuotaDouble / coberturaGtia) * 100) > 80) {
                tvErrorCuota.setVisibility(View.VISIBLE);
            } else {
                tvErrorCuota.setVisibility(View.INVISIBLE);
            }

        }
        if (montoDouble > 50000) {
            if (((montoDouble / disponibilidad) * 100) < 100) {
                tvErrorMonto.setVisibility(View.VISIBLE);
            } else {
                tvErrorMonto.setVisibility(View.INVISIBLE);
            }

            if (((cuotaDouble / coberturaGtia) * 100) > 80) {
                tvErrorCuota.setVisibility(View.VISIBLE);
            } else {
                tvErrorCuota.setVisibility(View.INVISIBLE);
            }


        }
    }

    private void listarFrecuencias(String URL){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
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
                            new ArrayAdapter<frecuencias>(getApplicationContext(),
                                    android.R.layout.simple_spinner_dropdown_item, lista);

                    spFrecuencia.setAdapter(adaptador);
                    spFrecuencia.setSelection(Integer.parseInt(idFrecuencia)-1);


                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();

                }

            }
        }, new ErrorListener() {
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

    private void actualizarSolicitud (String URL){
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

                    progreso.dismiss();

                }

            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                SharedPreferences preferencias = getSharedPreferences("usuario", Context.MODE_PRIVATE);


                Map<String, String> parametros= new HashMap<>();
                parametros.put("idSolicitud", idSolicitud);
                parametros.put("codCli", codCli);
                parametros.put("monto", txtMontoSol.getText().toString());
                parametros.put("plazo", txtPlazo.getText().toString());
                parametros.put("tasa", txtTasa.getText().toString());
                parametros.put("frecuencia", spFrecuencia.getSelectedItem().toString());
                parametros.put("token", spToken);
                parametros.put("codOfi", spCodOfi);
                parametros.put("tipCre","Represtamo");
                parametros.put("observa", txtComentarios.getText().toString());
                parametros.put("consultaCr", txtconsultaCrediticia.getText().toString());
                parametros.put("capacidadP", txtCapacidadPago.getText().toString());
                parametros.put("coberturaG", txtCoberturaGarantia.getText().toString());
                parametros.put("recordInt", txtRecordInterno.getText().toString());
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