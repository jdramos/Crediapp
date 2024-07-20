package com.example.credimovil;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.credimovil.modelo.SolicitudesListAdapter;
import com.example.credimovil.modelo.SolicitudesModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListaSolicitudes extends AppCompatActivity  {


    RequestQueue requestQueue;
    SharedPreferences preferencias;
    ListView lvSolicitud;
    ArrayList<SolicitudesModel> arrayList;
    SolicitudesListAdapter adapter;
    //SearchView searchView;
    TextView tvCobros, tvVacio;


    FloatingActionButton my_fab;

    ProgressDialog progreso;
    String lcCodCli, lcNomCli, lcCodCre, spNumSuc, spCodOfi, lcMonto,
            spToken, spWeb, spEmpresa, url, urlElimina, rol, idSolicitud, telefono, telSupervisor;
    DecimalFormat precision = new DecimalFormat("#,##0.00");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.solicitudes_list_view);
        this.setTitle("Listado de solicitudes.");



        preferencias = getSharedPreferences("usuario",Context.MODE_PRIVATE);
        spNumSuc = preferencias.getString("numsuc", null);
        spCodOfi = preferencias.getString("codofi", null);
        spToken = preferencias.getString("token", null);
        spWeb = preferencias.getString("web", null);
        spEmpresa = preferencias.getString("empresa", null);
        telSupervisor = preferencias.getString("telSupervisor", null);
        rol = preferencias.getString("rol", null);
        lvSolicitud = findViewById(R.id.listview_solicitudes);
       // searchView = findViewById(R.id.txtBuscar);
        arrayList = new ArrayList<>();
        adapter = new SolicitudesListAdapter(getApplicationContext(), arrayList);
        tvCobros = findViewById(R.id.btnDialog);
        tvVacio = findViewById(R.id.tvVacio);
        my_fab = findViewById(R.id.my_fab);
        lvSolicitud.setEmptyView(tvVacio);


        String strRol    = getIntent().getStringExtra("rol");
        String strCodOfi = getIntent().getStringExtra("codOfi");
        //String strOficobro = getIntent().getStringExtra("ofiCobro");
        String strNumSuc = getIntent().getStringExtra("numSuc");
        //String strNomOfi  = getIntent().getStringExtra("nomOfi");

        espere();

        url = spWeb+"/"+spEmpresa+"/listar_solicitudes.php?codOfi="+strCodOfi+"&numSuc="+strNumSuc+"&rol="+rol;
        listar(url);

            lvSolicitud.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    SolicitudesModel sm = (SolicitudesModel) adapter.getItem(position);

                    Intent i = new Intent(getApplicationContext(), solicitud_acciones.class);
                    i.putExtra("idSolicitud", sm.getIdSolicitud());
                    i.putExtra("codCli", sm.getCodCli());
                    i.putExtra("nomCli", sm.getNomCli());
                    i.putExtra("montoSol", sm.getMonto());
                    i.putExtra("plazo", String.valueOf(sm.getPlazo()));
                    i.putExtra("tasa", String.valueOf(sm.getTasa()));
                    i.putExtra("frecuencia", sm.getFrecuencia());
                    i.putExtra("idFrecuencia", sm.getIdFrecuencia());
                    i.putExtra("solicitadopor", sm.getSolicitadopor());
                    i.putExtra("aprobadopor", sm.getAprobadoSup());
                    i.putExtra("aplicadopor", sm.getAplicadopor());
                    i.putExtra("fechaSol", sm.getFecha());
                    i.putExtra("fechaAprob", sm.getFechaaprobadoSup());
                    i.putExtra("fechaApli", sm.getFechaaplicado());
                    i.putExtra("telefono", sm.getTelefono());
                    i.putExtra("aprobada", sm.getAprobado());
                    i.putExtra("comentarios", sm.getComentarios());
                    i.putExtra("consultaCrediticia", sm.getConsultaCrediticia());
                    i.putExtra("capacidadPago", sm.getCapacidadPago());
                    i.putExtra("coberturaGtia", sm.getCoberturaGtia());
                    i.putExtra("recordInterno", sm.getRecordInterno());
                    i.putExtra("saldoActual", sm.getSaldoActual());
                    i.putExtra("fechaActualizacion", sm.getFechaActualizacion());
                    i.putExtra("disponibilidadNeta", sm.getCapacidadPago());
                    i.putExtra("valorGarantia", sm.getCoberturaGtia());


                    //Toast.makeText(getApplicationContext(), "saliendo " + sm.getCoberturaGtia(), Toast.LENGTH_SHORT).show();
                    startActivity(i);

                }
            });


            lvSolicitud.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    SolicitudesModel sm = (SolicitudesModel) adapter.getItem(position);
                    confirmar(position);
                    return true;
                }
            });


        my_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), solicitudes_agregar.class);
                startActivity(i);
            }
        });


    }

    protected void onResume(){
        super.onResume();

        listar(url);


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);
        MenuItem buscador = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(buscador);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filtrar(newText);
                return false;
            }

        });

                return super.onCreateOptionsMenu(menu);
    }

    private void listar(String URL){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

              JSONObject jsonObject = null;
                //espere();
                arrayList.clear();
                try {
                    double totalSolicitud = 0;
                    for(int i =0;i < response.length(); i++){

                        jsonObject = response.getJSONObject(i);

                        String idSolicitud          = (jsonObject.getString("idSolicitud"));
                        String codCli               = (jsonObject.getString("codCli"));
                        String nomCli               = (jsonObject.getString("nomCli"));
                        String codCre               = (jsonObject.getString("codCred"));
                        String monto                = (jsonObject.getString("monto"));
                        String aprobado             = (jsonObject.getString("aprobado"));
                        String aplicado             = (jsonObject.getString("aplicado"));
                        String fecha                = (jsonObject.getString("fecha"));
                        String telefono             = (jsonObject.getString("telefono"));
                        Double plazo                = Double.parseDouble(jsonObject.getString("plazo"));
                        Double tasa                 = Double.parseDouble(jsonObject.getString("tasa"));
                        String idFrecuencia         = (jsonObject.getString("idFrecuencia"));
                        String frecuencia           = (jsonObject.getString("frecuencia"));
                        String tipCred              = (jsonObject.getString("tipCre"));
                        String nomSuc               = (jsonObject.getString("nomSuc"));
                        String solicitadopor        = (jsonObject.getString("solicitadopor"));
                        String aprobadoSup          = (jsonObject.getString("nombreSup"));
                        String aprobadoGteSuc       = (jsonObject.getString("nombreGteSuc"));
                        String aprobadoGteGral      = (jsonObject.getString("nombreGteGral"));
                        String aplicadopor          = (jsonObject.getString("aplicadopor"));
                        String fechasolcitud        = (jsonObject.getString("fechasolicitud"));
                        String fechaaprobadoSup     = (jsonObject.getString("fechaaprobadoSup"));
                        String fechaaprobadoGteSuc  = (jsonObject.getString("fechaaprobadoGteSuc"));
                        String fechaaprobadogteGral = (jsonObject.getString("fechaaprobadoGteGral"));
                        String fechaaplicado        = (jsonObject.getString("fechaaplicado"));
                        String comentarios          = (jsonObject.getString("observa"));
                        String saldoActual          = (jsonObject.getString("saldoActual"));
                        String consultaCrediticia   = (jsonObject.getString("consultaCredi"));
                        String capacidadPago        = (jsonObject.getString("DisponibilidadNeta"));
                        String coberturaGtia        = (jsonObject.getString("ValorGarantia"));
                        String recordInterno        = (jsonObject.getString("recordInterno"));
                        String fechaActualizacion   = (jsonObject.getString("fechaActualizacion"));

                        arrayList.add(new SolicitudesModel(idSolicitud, codCli, nomCli, fecha, monto, telefono, aprobado,
                                aplicado, plazo, tasa, idFrecuencia, frecuencia, tipCred, codCre, nomSuc, solicitadopor,
                                aprobadoSup, aprobadoGteSuc, aprobadoGteGral, aplicadopor, fechasolcitud, fechaaprobadoSup,
                                fechaaprobadoGteSuc, fechaaprobadogteGral, fechaaplicado, Double.parseDouble(saldoActual),
                                comentarios, consultaCrediticia, capacidadPago, coberturaGtia, recordInterno, fechaActualizacion));

                        adapter = new SolicitudesListAdapter(getApplicationContext(), arrayList);

                        lvSolicitud.setAdapter(adapter);

                        totalSolicitud = totalSolicitud+Double.parseDouble(monto);

                    }



                    tvCobros.setText(response.length() +" Solicitud(es) del día: C$"+precision.format(totalSolicitud));
                    progreso.dismiss();

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    progreso.dismiss();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                //limpiarCampos();
                progreso.dismiss();

            }
        }
        );

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);

    }

    private void confirmar(int pos){
        SolicitudesModel sm = (SolicitudesModel) adapter.getItem(pos);
        lcCodCli = sm.getCodCli();
        lcNomCli = sm.getNomCli();
        lcMonto = sm.getMonto();
        idSolicitud = sm.getIdSolicitud();
        urlElimina = spWeb+"/"+spEmpresa+"/anular_solicitud.php?idSolicitud="+ idSolicitud;
        telefono = sm.getTelefono();

        AlertDialog.Builder guardar = new AlertDialog.Builder(ListaSolicitudes.this, R.style.AlertDialogTheme);
        final View customLayout = getLayoutInflater().inflate(R.layout.custom_layout, null);
        EditText editText = customLayout.findViewById(R.id.txtValorPago);
        editText.setText(lcMonto);
        guardar.setView(customLayout);
        guardar.setTitle("Confime anular solicitud \n"+lcNomCli);
        guardar.setCancelable(true);
        guardar.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                eliminarSolicitud(urlElimina);

                };

        });
        guardar.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = guardar.create();
        dialog.show();
    }

    private void eliminarSolicitud(String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.contains("CONECTADO")){
                    Toast.makeText(getApplicationContext(), "SE HA ABIERTO SERSION EN OTRO DISPOSTIVO, LA SESION EN ESTE DISPOSITIVO SE HA CERRADO", Toast.LENGTH_LONG).show();

                    Intent i = new Intent(getApplicationContext(), Login.class);
                    startActivity(i);
                }

                if (response.contains("EXITO")){
                    enviarsms(telefono);
                    enviarsmsSup(telSupervisor);
                    lvSolicitud.setAdapter(null);
                    listar(url);
                    adapter.notifyDataSetChanged();
                    lvSolicitud.setAdapter(adapter);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getCause().toString(),Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros= new HashMap<>();
                parametros.put("idSolicitud", idSolicitud);


                return parametros;
            }
        };
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void enviarsms(String celular){
        String msj = "Estimado Sr(a): " + lcNomCli + " se ha anulado pago por C$"+lcMonto+" credito#"+lcCodCre+
                     ". "+spEmpresa.toUpperCase();

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(celular, null, msj , null, null);
    }

    private void enviarsmsSup(String celular){
        String msjSup = "Se anuló pago del Sr(a) " + lcNomCli + " por C$"+lcMonto+" credito#"+lcCodCre+
                ", Oficial: "+spCodOfi+". "+spEmpresa.toUpperCase();

        SmsManager smsSup = SmsManager.getDefault();
        smsSup.sendTextMessage(celular, null, msjSup , null, null);
    }

    public void espere(){
        progreso = new ProgressDialog(this);
        progreso.setCancelable(false);
        progreso.setTitle("Cargando...");
        progreso.show();
        progreso.setContentView(R.layout.progress_dialog);
        progreso.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
   }

    public void filtrar(String texto) {
        ArrayList<SolicitudesModel> filtrarLista = new ArrayList<>();
        for(SolicitudesModel solicitudes : arrayList) {
            if(solicitudes.getNomCli().toLowerCase().contains(texto.toLowerCase())
                    || solicitudes.getCodCli().toLowerCase().contains(texto.toLowerCase())
                    || solicitudes.getCodCli().toLowerCase().contains(texto.toLowerCase())) {
                filtrarLista.add(solicitudes);
            }
        }

        adapter.filtrar(filtrarLista);

    }

}
