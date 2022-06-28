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
import com.example.credimovil.modelo.CobrosOficialesAdapter;
import com.example.credimovil.modelo.CobrosOficialesModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ListaCobrosOficial extends AppCompatActivity  {


    RequestQueue requestQueue;

    SharedPreferences preferencias;
    ListView lvCobrossOficial;
    ArrayList<CobrosOficialesModel> arrayList;
    CobrosOficialesAdapter adapter;
    ProgressDialog progreso;
    TextView tvSucursal;

    String  spCodOfi,
            spToken, spWeb, spEmpresa, url, rol;
    DecimalFormat precision = new DecimalFormat("###,###,###.##");
    ImageButton btnDialog;
    double dblCantCobros  = 0;
    double dblTotalCobros = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saldos_sucursales_list_view);

        preferencias = getSharedPreferences("usuario",Context.MODE_PRIVATE);
        spCodOfi = preferencias.getString("codofi", null);
        spToken = preferencias.getString("token", null);
        rol = preferencias.getString("rol", null);
        spWeb = preferencias.getString("web", null);
        spEmpresa = preferencias.getString("empresa", null);
        lvCobrossOficial = findViewById(R.id.listview_saldos_sucursales);
        //textViewTotales = findViewById(R.id.btnDialog);
        arrayList = new ArrayList<>();
        adapter = new CobrosOficialesAdapter(this, arrayList);
        tvSucursal = findViewById(R.id.tvNomSuc);
        DecimalFormat precision = new DecimalFormat("#,##0.00");

        this.setTitle("Cobros por oficiales");


        espere();
        final String strNumSuc = getIntent().getStringExtra("numSuc");
        final String strNomSuc = getIntent().getStringExtra("nomSuc");
        tvSucursal.setText(strNumSuc+"-"+strNomSuc);
        url = spWeb+"/"+spEmpresa+"/listar_cobros_oficiales.php?numSuc="+strNumSuc;

        saldos(url);

        lvCobrossOficial.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CobrosOficialesModel sm = (CobrosOficialesModel) adapter.getItem(position);
                Intent i = new Intent(getApplicationContext(), ListaCobros.class);
                i.putExtra("numSuc", strNumSuc);
                i.putExtra("nomOfi", sm.getNomOfi());
                i.putExtra("codOfi", sm.getCodOfi());
                i.putExtra("rol", rol);

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
                    Toast.makeText(getApplicationContext(), "No existen saldos a mostrar",Toast.LENGTH_SHORT).show();
                    return;
                }

                JSONObject jsonObject = null;
                //espere();
                //arrayList.clear();
                try {


                    for(int i =0;i < response.length(); i++){

                        jsonObject = response.getJSONObject(i);

                        String codOfi       = (jsonObject.getString("codOfi"));
                        String cantPagos    = (jsonObject.getString("cantidad"));
                        String nomOfi       = (jsonObject.getString("nomOfi"));
                        String valorTotal   = (jsonObject.getString("cuota"));

                        arrayList.add(new CobrosOficialesModel(codOfi, nomOfi, cantPagos, valorTotal));
                        adapter = new CobrosOficialesAdapter(getApplicationContext(), arrayList);
                        lvCobrossOficial.setAdapter(adapter);
                        progreso.dismiss();

                        dblCantCobros = dblCantCobros + Double.parseDouble(cantPagos);
                        dblTotalCobros = dblTotalCobros + Double.parseDouble(valorTotal);


                    }

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


    private void showDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheetlayout);


        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        TextView txtTotalClientes = dialog.findViewById(R.id.txtTotalClientes);
        TextView tvTotalClientes = dialog.findViewById(R.id.tvCantClies);
        TextView txtTotalSaldo = dialog.findViewById(R.id.txtTotalCartera);
        TextView tvTotalSaldo = dialog.findViewById(R.id.tvTotalCartera);

        TextView tvTotalMora = dialog.findViewById(R.id.txtTotalMora);
        TextView tvTotalVencido = dialog.findViewById(R.id.txtTotalVencido);
        TextView tvTotalIngresos = dialog.findViewById(R.id.txtTotalIngresos);


        txtTotalClientes.setText(precision.format(dblCantCobros));
        tvTotalClientes.setText("Cantidad cobros");
        txtTotalSaldo.setText(precision.format(dblTotalCobros));
        tvTotalSaldo.setText("Total cobrado");

        tvTotalMora.setVisibility(View.GONE);
        tvTotalVencido.setVisibility(View.GONE);
        tvTotalIngresos.setVisibility(View.GONE);


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
        ArrayList<CobrosOficialesModel> filtrarLista = new ArrayList<>();

        for(CobrosOficialesModel cobros : arrayList) {
            if(cobros.getCodOfi().toLowerCase().contains(texto.toLowerCase())
                    || cobros.getNomOfi().toLowerCase().contains(texto.toLowerCase())) {
                filtrarLista.add(cobros);
            }
        }

        adapter.filtrar(filtrarLista);
    }



}