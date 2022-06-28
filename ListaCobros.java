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
import com.example.credimovil.modelo.CobrosListAdapter;
import com.example.credimovil.modelo.CobrosModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListaCobros extends AppCompatActivity  {


    RequestQueue requestQueue;
    SharedPreferences preferencias;
    ListView lvProduct;
    ArrayList<CobrosModel> arrayList;
    CobrosListAdapter adapter;
    //SearchView searchView;
    TextView tvCobros, tvNomOfi;

    ProgressDialog progreso;
    String lcCodCli, lcNomCli, lcCodCre, spNumSuc, spCodOfi, lcMonto,
            spToken, spWeb, spEmpresa, url, urlElimina, rol, idPago,
            telefono, telSupervisor, spOfiCobro, verTotalCobros;
    DecimalFormat precision = new DecimalFormat("#,###.00");




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cobros_list_view);


        preferencias = getSharedPreferences("usuario",Context.MODE_PRIVATE);
        spNumSuc = preferencias.getString("numsuc", null);
        spCodOfi = preferencias.getString("codofi", null);
        spOfiCobro = preferencias.getString("ofiCobro", null);
        spToken = preferencias.getString("token", null);
        spWeb = preferencias.getString("web", null);
        spEmpresa = preferencias.getString("empresa", null);
        telSupervisor = preferencias.getString("telSupervisor", null);
        verTotalCobros = preferencias.getString("verTotalCobros", null);

        rol = preferencias.getString("rol", null);
        lvProduct = findViewById(R.id.listview_solicitudes);
       // searchView = findViewById(R.id.txtBuscar);
        arrayList = new ArrayList<>();
        adapter = new CobrosListAdapter(getApplicationContext(), arrayList);
        tvCobros = findViewById(R.id.btnDialog);
        tvNomOfi = findViewById(R.id.tvNomOfi);



        espere();
        String strRol    = getIntent().getStringExtra("rol");
        String strCodOfi = getIntent().getStringExtra("codOfi");
        String strOficobro = getIntent().getStringExtra("ofiCobro");
        String strNumSuc = getIntent().getStringExtra("numSuc");
        String strNomOfi  = getIntent().getStringExtra("nomOfi");

        tvNomOfi.setText("Cobros "+strCodOfi+"-"+strNomOfi);

       // url = spWeb+"/"+spEmpresa+"/listar_cobros.php?rol="+rol+"&codOfi="+spCodOfi+"&numSuc="+spNumSuc;
        url = spWeb+"/"+spEmpresa+"/listar_cobros.php?codOfi="+strCodOfi+"&numSuc="+strNumSuc+"&rol="+strRol;
        saldos(url);
        progreso.dismiss();


            lvProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    CobrosModel sm = (CobrosModel) adapter.getItem(position);
                    Toast.makeText(getApplicationContext(),
                            "PRESIONE PARA ANULAR PAGO " + sm.getNomCli(),
                            Toast.LENGTH_SHORT).show();

                }
            });

            lvProduct.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                    if(rol.equals("1") || rol.equals("4")){
                        CobrosModel sm = (CobrosModel) adapter.getItem(position);
                        confirmar(position);
                    }
                    return true;
                }
            });
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



    private void saldos(String URL){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
              //  Toast.makeText(getApplicationContext(), response.toString(),Toast.LENGTH_SHORT).show();
             /*   if(response.equals("VACIO")){
                    Toast.makeText(getApplicationContext(), "No existen pagos a mostrar",Toast.LENGTH_SHORT).show();
                    return;
                }


              */
                JSONObject jsonObject = null;
                //espere();
                if(response.length()>0){
                try {
                    double totalCobro = 0;

                        for(int i =0;i < response.length(); i++){

                            jsonObject = response.getJSONObject(i);

                            String idPago  = (jsonObject.getString("idPago"));
                            String codCli  = (jsonObject.getString("codCli"));
                            String nomCli = (jsonObject.getString("nomCli"));
                            String codCre = (jsonObject.getString("codCredi"));
                            String cuota = (jsonObject.getString("cuota"));
                            String telefono = (jsonObject.getString("telefono"));

                            arrayList.add(new CobrosModel(idPago, codCli, nomCli, codCre, cuota, telefono));
                            adapter = new CobrosListAdapter(getApplicationContext(), arrayList);
                            lvProduct.setAdapter(adapter);
                            totalCobro = totalCobro + Double.parseDouble(cuota);
                        }

                            tvCobros.setText("");

                            if(verTotalCobros.equals("1")){
                                tvCobros.setText("Cobros del día: ("+response.length()+") C$"+precision.format(totalCobro));
                            }



                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "catch"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "onError "+error.getMessage(), Toast.LENGTH_LONG).show();
                //limpiarCampos();
            }
        }
        );

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);

    }

    private void confirmar(int pos){
        CobrosModel sm = (CobrosModel) adapter.getItem(pos);
        lcCodCli = sm.getCodCli();
        lcNomCli = sm.getNomCli();
        lcCodCre = sm.getCodCre();
        lcMonto = sm.getMonto();
        idPago = sm.getIdPago();
        urlElimina = spWeb+"/"+spEmpresa+"/anular_cobro.php?idPago="+idPago;
        telefono = sm.getTelefono();

        AlertDialog.Builder guardar = new AlertDialog.Builder(ListaCobros.this, R.style.AlertDialogTheme);
        final View customLayout = getLayoutInflater().inflate(R.layout.custom_layout, null);
        EditText editText = customLayout.findViewById(R.id.txtValorPago);
        editText.setText(lcMonto);
        guardar.setView(customLayout);
        guardar.setTitle("Confirme anular recibo \n"+lcNomCli);
        guardar.setCancelable(true);
        guardar.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                eliminarPago(urlElimina);
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



    private void  eliminarPago (String URL){
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
                    lvProduct.setAdapter(null);
                    saldos(url);
                    adapter.notifyDataSetChanged();
                    lvProduct.setAdapter(adapter);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(),Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros= new HashMap<>();
                parametros.put("idPago", idPago);


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
        ArrayList<CobrosModel> filtrarLista = new ArrayList<>();

        for(CobrosModel cobros : arrayList) {
            if(cobros.getNomCli().toLowerCase().contains(texto.toLowerCase())
                    || cobros.getCodCli().toLowerCase().contains(texto.toLowerCase())
                    || cobros.getCodCre().toLowerCase().contains(texto.toLowerCase())) {
                filtrarLista.add(cobros);
            }
        }

        adapter.filtrar(filtrarLista);
    }


}