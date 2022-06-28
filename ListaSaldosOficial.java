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
import com.example.credimovil.modelo.SaldosOficialesAdapter;
import com.example.credimovil.modelo.SaldosOficialesModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ListaSaldosOficial extends AppCompatActivity  {


    RequestQueue requestQueue;

    SharedPreferences preferencias;
    ListView lvSaldosOficial;
    ArrayList<SaldosOficialesModel> arrayList;
    SaldosOficialesAdapter adapter;
    //SearchView searchView;
    ProgressDialog progreso;
    TextView textViewTotales, tvNomSuc;

    String  spNumSuc, spCodOfi, spToken, spWeb, spEmpresa, url, rol, nomSuc;
    ImageButton btnDialog;
    DecimalFormat precision = new DecimalFormat("#,###.00");
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
        lvSaldosOficial = findViewById(R.id.listview_saldos_sucursales);
        tvNomSuc = findViewById(R.id.tvNomSuc);
       // searchView = findViewById(R.id.txtBuscar);
        btnDialog = findViewById(R.id.btnDialog);
        arrayList = new ArrayList<>();
        adapter = new SaldosOficialesAdapter(this, arrayList);
        nomSuc = getIntent().getStringExtra("nomSuc");
        tvNomSuc.setText(nomSuc);

        this.setTitle("Saldos por oficiales");


        espere();
        String strNumSuc = getIntent().getStringExtra("numSuc");
        url = spWeb+"/"+spEmpresa+"/listar_saldos_oficiales.php?numSuc="+strNumSuc+"&rol="+rol+"&codOfi="+spCodOfi;

        saldos(url);

        btnDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        lvSaldosOficial.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SaldosOficialesModel sm = (SaldosOficialesModel) adapter.getItem(position);
                String strRol = "3";
                Intent i = new Intent(getApplicationContext(), ListaSaldos.class);
                i.putExtra("numSuc", spNumSuc);
                i.putExtra("codOfi", sm.getCodOfi());
                i.putExtra("rol", strRol);
                i.putExtra("nombreOficial", sm.getNomOfi());

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
                        String cantClies    = (jsonObject.getString("cantClies"));
                        String nomOfi       = (jsonObject.getString("nomOfi"));
                        String saldoTotal   = (jsonObject.getString("saldoTotal"));
                        String moraTotal    = (jsonObject.getString("moraTotal"));
                        String vencidoTotal = (jsonObject.getString("vencidoTotal"));
                        String pagoHoy      = (jsonObject.getString("pagosHoy"));
                        String ncHoy        = (jsonObject.getString("ncHoy"));
                        String ndHoy        = (jsonObject.getString("ndHoy"));
                        String ingresos     = (jsonObject.getString("ingresos"));


                        double saldo = Double.parseDouble(saldoTotal);
                        double pago  = Double.parseDouble(pagoHoy);
                        double nc    = Double.parseDouble(ncHoy);
                        double nd    = Double.parseDouble(ndHoy);
                        double dbmora= Double.parseDouble(moraTotal);
                        double dbVenc=Double.parseDouble(vencidoTotal);
                        double saldoNuevo = (saldo+nd)-(pago+nc);
                        double dbIngreso =Double.parseDouble(ingresos);


                        String strSaldo = String.valueOf(saldoNuevo);
                        String strVenc  = precision.format(dbVenc);
                        String strMora  = precision.format(dbmora);

                        arrayList.add(new SaldosOficialesModel(codOfi,nomOfi, cantClies, strSaldo,
                                moraTotal,  vencidoTotal, pagoHoy, ncHoy, ndHoy, ingresos));
                        adapter = new SaldosOficialesAdapter(getApplicationContext(), arrayList);
                        lvSaldosOficial.setAdapter(adapter);


                        dblTotalClies   =   Double.parseDouble(cantClies) + dblTotalClies;
                        dblTotalSaldo   =   saldo + dblTotalSaldo;
                        dblTotalMora    =   dbmora + dblTotalMora;
                        dblTotalVencido =   dbVenc + dblTotalVencido;
                        dblTotalIngresos =  dbIngreso + dblTotalIngresos;

                        progreso.dismiss();

                    }
                    //textViewTotales.setText("Total Clientes:"+String.valueOf(dblTotalClies));
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
        ArrayList<SaldosOficialesModel> filtrarLista = new ArrayList<>();

        for(SaldosOficialesModel saldos : arrayList) {
            if(saldos.getCodOfi().toLowerCase().contains(texto.toLowerCase())
                    || saldos.getNomOfi().toLowerCase().contains(texto.toLowerCase())) {
                filtrarLista.add(saldos);
            }
        }

        adapter.filtrar(filtrarLista);
    }



}