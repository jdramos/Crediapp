/*package com.example.credimovil;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.credimovil.modelo.SaldosListAdapter;
import com.example.credimovil.modelo.SaldosModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class buscar_cliente extends AppCompatActivity {


    RequestQueue requestQueue;
    ProgressDialog progreso;
    String spNomSuc, spCodOfi, spToken, spWeb, spEmpresa, url;
    SharedPreferences preferencias;


    SearchView searchView;
    ListView listView;
    SaldosListAdapter adapter;
    private List<SaldosModel> mSaldosList;
    ArrayList<SaldosModel> listanombres;


    String [] nameList = {"uno", "dos", "tres", "cuatro,", "cinco"};

    ArrayAdapter<String> arrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_cliente);

        searchView = findViewById(R.id.searchBar);
        listView = findViewById(R.id.list_item);
        preferencias = getSharedPreferences("usuario", Context.MODE_PRIVATE);
        spNomSuc = preferencias.getString("numsuc", null);
        spCodOfi = preferencias.getString("codofi", null);
        spToken = preferencias.getString("token", null);
        spWeb = preferencias.getString("web", null);
        spEmpresa = preferencias.getString("empresa", null);
        listanombres = new ArrayList<>();

        arrayAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, nameList);
        listView.setAdapter(arrayAdapter);
        url = "https://"+spWeb+"/"+spEmpresa+"/listar_saldos.php?codOfi="+spCodOfi;
    //    saldos(url);

        searchView.setOnQueryTextListener(new OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                arrayAdapter.getFilter().filter(newText);
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(buscar_cliente.this,parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void saldos(String URL){


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                if(response.length()==0){

                    Toast.makeText(getApplicationContext(), "No existen saldos a mostrar",Toast.LENGTH_SHORT).show();
                    return;
                }

                JSONObject jsonObject = null;
                //espere();
                try {
                    for(int i =0;i < response.length(); i++){

                        jsonObject = response.getJSONObject(i);

                        String codCli  = (jsonObject.getString("codCli"));
                        String nomCli = (jsonObject.getString("nomCli"));
                        String codCre = (jsonObject.getString("codCredi"));
                        String saldoTotal = (jsonObject.getString("saldoTotal"));
                        String moraTotal = (jsonObject.getString("moraTotal"));
                        String vencidoTotal = (jsonObject.getString("vencidoTotal"));
                        String fechaFin = (jsonObject.getString("fechafin"));
                        String nomdia = (jsonObject.getString("nomdia"));
                        String concurr = (jsonObject.getString("concurr"));
                        String cuota = (jsonObject.getString("cuota"));

                        listanombres.add(new SaldosModel(codCli,nomCli,codCre,saldoTotal, moraTotal, vencidoTotal, fechaFin, nomdia, concurr, cuota));
                        adapter = new SaldosListAdapter(getApplicationContext(), listanombres);
                        listView.setAdapter(adapter);
                    }


                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

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



}

 */