package com.example.credimovil;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.credimovil.modelo.SaldosSucursalModel;
import com.example.credimovil.modelo.SaldosSucursalesAdapter;
import com.example.credimovil.modelo.SolicitudesSucursalModel;
import com.example.credimovil.modelo.SolicitudesSucursalesAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ListaSolicitudSucursal extends AppCompatActivity  {
    RequestQueue requestQueue;
    SharedPreferences preferencias;
    ListView lvSolicitudesSucursal;
    ArrayList<SolicitudesSucursalModel> arrayList;
    SolicitudesSucursalesAdapter adapter;
    //SearchView searchView;
    ProgressDialog progreso;
    TextView  tvVacio;
    String spNumSuc, spNomSuc, spCodOfi, spToken, spWeb, spEmpresa, url,rol, strTotalClientes;
    DecimalFormat precision = new DecimalFormat("#,###.00");
    ImageButton btnDialog;
    double dblTotalClies = 0;
    double dblTotalSaldo = 0;
    double dblTotalMora  = 0;
    double dblTotalVencido = 0;
    double dblTotalIngresos = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saldos_sucursales_list_view);

        preferencias = getSharedPreferences("usuario",Context.MODE_PRIVATE);
        spNumSuc = preferencias.getString("numsuc", null);
        spCodOfi = preferencias.getString("codofi", null);
        spToken = preferencias.getString("token", null);
        spWeb = preferencias.getString("web", null);
        spEmpresa = preferencias.getString("empresa", null);
        rol = preferencias.getString("rol", null);
        spNomSuc = preferencias.getString("nomSuc", null);
        lvSolicitudesSucursal = findViewById(R.id.listview_saldos_sucursales);
        tvVacio = findViewById(R.id.tvVacio);
        lvSolicitudesSucursal.setEmptyView(tvVacio);
        btnDialog = findViewById(R.id.btnDialog);
        arrayList = new ArrayList<>();
        adapter = new SolicitudesSucursalesAdapter(this, arrayList);
        this.setTitle("Solicitudes por Sucursal");

        espere();

        url = spWeb+"/"+spEmpresa+"/listar_solicitudes_sucursal.php?rol="+rol+"&numSuc="+spNumSuc;

        listarSolicitudes(url);


        btnDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        lvSolicitudesSucursal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SolicitudesSucursalModel sm = (SolicitudesSucursalModel) adapter.getItem(position);

                Intent i = new Intent(getApplicationContext(), ListaSolicitudesOficial.class);
                i.putExtra("numSuc", sm.getNumSuc());
                i.putExtra("nomSuc", sm.getNomSuc());

                startActivity(i);

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

    private void listarSolicitudes(String URL){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                if(response.equals("VACIO")){
                    Toast.makeText(getApplicationContext(), "No existen datos a mostrar",Toast.LENGTH_SHORT).show();
                    return;
                }

                JSONObject jsonObject = null;
                //espere();
                //arrayList.clear();
                try {

                    for(int i =0;i < response.length(); i++){

                        jsonObject = response.getJSONObject(i);

                        String numSuc       = (jsonObject.getString("numSuc"));
                        String cantSolicitudes    = (jsonObject.getString("cantSolicitudes"));
                        String nomSuc       = (jsonObject.getString("nomSuc"));
                        String montoTotal   = (jsonObject.getString("monto"));

                        double dblMonto = Double.parseDouble(montoTotal);

                        String strMonto = String.valueOf(dblMonto);

                        arrayList.add(new SolicitudesSucursalModel(numSuc,nomSuc, cantSolicitudes, strMonto));
                        adapter = new SolicitudesSucursalesAdapter(getApplicationContext(), arrayList);
                        lvSolicitudesSucursal.setAdapter(adapter);

                        progreso.dismiss();
                    }


                } catch (JSONException e) {
                    progreso.dismiss();
                    Toast.makeText(getApplicationContext(), "catch: "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progreso.dismiss();
                Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        );

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);

    }

    private void showDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheetlayout);


        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        TextView tvTotalClientes = dialog.findViewById(R.id.txtTotalClientes);
        TextView tvTotalSaldo = dialog.findViewById(R.id.txtTotalCartera);
        TextView tvTotalMora = dialog.findViewById(R.id.txtTotalMora);
        TextView tvTotalVencido = dialog.findViewById(R.id.txtTotalVencido);
        TextView tvTotalIngresos = dialog.findViewById(R.id.txtTotalIngresos);

        tvTotalClientes.setText(precision.format(dblTotalClies));
        tvTotalSaldo.setText(precision.format(dblTotalSaldo));
        tvTotalMora.setText(precision.format(dblTotalMora));
        tvTotalVencido.setText(precision.format(dblTotalVencido));
        tvTotalIngresos.setText(precision.format(dblTotalIngresos));



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
        ArrayList<SolicitudesSucursalModel> filtrarLista = new ArrayList<>();

        for(SolicitudesSucursalModel saldos : arrayList) {
            if(saldos.getNumSuc().toLowerCase().contains(texto.toLowerCase())
                    || saldos.getNomSuc().toLowerCase().contains(texto.toLowerCase())) {
                filtrarLista.add(saldos);
            }
        }

        adapter.filtrar(filtrarLista);
    }



}