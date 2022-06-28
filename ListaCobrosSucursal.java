package com.example.credimovil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
import com.example.credimovil.modelo.CobrosSucursalModel;
import com.example.credimovil.modelo.CobrosSucursalesAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ListaCobrosSucursal extends AppCompatActivity  {


    RequestQueue requestQueue;

    SharedPreferences preferencias;
    ListView lvSaldosSucursal;
    ArrayList<CobrosSucursalModel> arrayList;
    CobrosSucursalesAdapter adapter;
    ProgressDialog progreso;
    TextView textViewTotales;
    String spNumSuc, spCodOfi,
            spToken, spWeb, spEmpresa, url;
    DecimalFormat precision = new DecimalFormat("#,##0.00");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cobros_sucursales_list_view);

        preferencias = getSharedPreferences("usuario",Context.MODE_PRIVATE);
        spNumSuc = preferencias.getString("numsuc", null);
        spCodOfi = preferencias.getString("codofi", null);
        spToken = preferencias.getString("token", null);
        spWeb = preferencias.getString("web", null);
        spEmpresa = preferencias.getString("empresa", null);
        lvSaldosSucursal = findViewById(R.id.listview_cobros_sucursales);
        textViewTotales = findViewById(R.id.btnDialog);
        arrayList = new ArrayList<>();
        adapter = new CobrosSucursalesAdapter(this, arrayList);

        this.setTitle("Cobros por Sucursal");


        espere();

        url = spWeb+"/"+spEmpresa+"/listar_cobros_sucursales.php";

        saldos(url);



        lvSaldosSucursal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CobrosSucursalModel sm = (CobrosSucursalModel) adapter.getItem(position);

                Intent i = new Intent(getApplicationContext(), ListaCobrosOficial.class);
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

    private void saldos(String URL){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                if(response.equals("VACIO")){
                    Toast.makeText(getApplicationContext(), "No existen cobros a mostrar",Toast.LENGTH_SHORT).show();
                    return;
                }

                JSONObject jsonObject = null;
                //espere();
                //arrayList.clear();
                try {
                    double dblCantCobros  = 0;
                    double dblTotalCobros = 0;

                    for(int i =0;i < response.length(); i++){

                        jsonObject = response.getJSONObject(i);

                        String numSuc       = (jsonObject.getString("numSuc"));
                        String nomSuc       = (jsonObject.getString("sucursal"));
                        String cantCobros    = (jsonObject.getString("cantCobros"));
                        String cuotaTotal   = (jsonObject.getString("cuota"));


                        arrayList.add(new CobrosSucursalModel(numSuc,  nomSuc, cantCobros, cuotaTotal));
                        adapter = new CobrosSucursalesAdapter(getApplicationContext(), arrayList);
                        lvSaldosSucursal.setAdapter(adapter);

                        dblCantCobros = dblCantCobros + Double.parseDouble(cantCobros);
                        dblTotalCobros = dblTotalCobros + Double.parseDouble(cuotaTotal);


                    }
                    progreso.dismiss();


                   textViewTotales.setText("Total ("+ precision.format(dblCantCobros)+") "+precision.format(dblTotalCobros));
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "catch"+ e.getMessage(), Toast.LENGTH_SHORT).show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "error "+ error.getMessage(), Toast.LENGTH_SHORT).show();
                progreso.dismiss();

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

    public void filtrar(String texto) {
        ArrayList<CobrosSucursalModel> filtrarLista = new ArrayList<>();

        for(CobrosSucursalModel cobros : arrayList) {
            if(cobros.getNumSuc().toLowerCase().contains(texto.toLowerCase())
                    || cobros.getNomSuc().toLowerCase().contains(texto.toLowerCase())) {
                filtrarLista.add(cobros);
            }
        }

        adapter.filtrar(filtrarLista);
    }



}