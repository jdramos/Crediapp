package com.example.credimovil;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.credimovil.modelo.BalanceListAdapter;
import com.example.credimovil.modelo.BalanceModel;
import com.example.credimovil.modelo.SaldosListAdapter;
import com.example.credimovil.modelo.SaldosModel;
import com.example.credimovil.modelo.clientesModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class balance_credito extends AppCompatActivity {

    TextView tvCliente, tvFecha, tvCredito, tvPlazo, tvMonto;
    ListView lvBalance;
    String url, spWeb, spEmpresa, strCodCli, strCodCred, strNomCli;
    SharedPreferences preferencias;
    ArrayList<BalanceModel> listaSaldos = new ArrayList<>();
    ProgressDialog progreso;
    RequestQueue requestQueue;
    BalanceListAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_credito);

        tvCliente   = findViewById(R.id.txtCliente);
        tvFecha     = findViewById(R.id.txtFechaDesembolso);
        tvCredito   = findViewById(R.id.txtCredito);
        tvPlazo     = findViewById(R.id.txtPlazo);
        tvMonto     = findViewById(R.id.txtPlazo);
        lvBalance   = findViewById(R.id.lvbalance);

        preferencias    = getSharedPreferences("usuario",Context.MODE_PRIVATE);
        spWeb           = preferencias.getString("web", null);
        spEmpresa       = preferencias.getString("empresa", null);

        strCodCli   = getIntent().getStringExtra("codcli");
        strCodCred  = getIntent().getStringExtra("codCred");
        strNomCli  = getIntent().getStringExtra("nomcli");

        tvCliente.setText(strCodCli+"-"+strNomCli);
        tvCredito.setText(strCodCred);


        url = spWeb+"/"+spEmpresa+"/balance_credito.php?codCli="+strCodCli+"&codCredi="+strCodCred;
        espere();
        balance(url);

    }


    private void balance(String URL){
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

                    double dbsaldo = 0;
                    for(int i =0;i < response.length(); i++){

                        jsonObject = response.getJSONObject(i);

                        String docto     = (jsonObject.getString("docto"));
                        String fecha     = (jsonObject.getString("fecha"));
                        String monto     = (jsonObject.getString("monto"));
                        double dbmonto   = Double.parseDouble(monto);

                        dbsaldo = dbsaldo+dbmonto;

                        String strSaldo = String.valueOf(dbsaldo);


                        listaSaldos.add(new BalanceModel(docto, fecha, monto, strSaldo));
                        adapter = new BalanceListAdapter(getApplicationContext(), listaSaldos);
                        lvBalance.setAdapter(adapter);
                        progreso.dismiss();

                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "catch "+ e.getMessage(), Toast.LENGTH_SHORT).show();

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
}