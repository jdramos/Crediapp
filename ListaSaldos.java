package com.example.credimovil;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

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
import com.example.credimovil.modelo.SaldosListAdapter;
import com.example.credimovil.modelo.SaldosModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListaSaldos extends AppCompatActivity  {


    RequestQueue requestQueue;

    SharedPreferences preferencias;
    ListView lvProduct;
    ArrayList<SaldosModel> arrayList;
    SaldosListAdapter adapter;
    //SearchView searchView;
    ProgressDialog progreso;
    Functions functions = new Functions();
    TextView tvNombreOficial;
    String strRol;




    String lcCodCli, lcNomCli, lcCodCre, spNumSuc, spCodOfi, nombreOficial,
            spToken, spWeb, spEmpresa, url, lnValPag, spRol, lnSalAnt, lnSalAct, telefono, lnCuota,
    pagoHoy, ncHoy, ndHoy, urlSucursales, todasSuc, spOfiCobro;
    DecimalFormat precision = new DecimalFormat("###,###,###.##");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saldos_list_view);

        preferencias = getSharedPreferences("usuario",Context.MODE_PRIVATE);
        spNumSuc = preferencias.getString("numsuc", null);
        spCodOfi = preferencias.getString("codofi", null);
        spOfiCobro = preferencias.getString("ofiCobro", null);
        spToken = preferencias.getString("token", null);
        spWeb = preferencias.getString("web", null);
        spEmpresa = preferencias.getString("empresa", null);
        todasSuc = preferencias.getString("todasSuc", null);
        spRol = preferencias.getString("rol", null);
        tvNombreOficial = findViewById(R.id.tvNomOfi);
        lvProduct = findViewById(R.id.listview_saldos);
       // searchView = findViewById(R.id.txtBuscar);
        arrayList = new ArrayList<>();
        adapter = new SaldosListAdapter(this, arrayList, spEmpresa, strRol);


        loading();


        strRol    = getIntent().getStringExtra("rol");
        String strCodOfi = getIntent().getStringExtra("codOfi");
        String strOficobro = getIntent().getStringExtra("ofiCobro");
        String strNumSuc = getIntent().getStringExtra("numSuc");
        String strOficial = getIntent().getStringExtra("nombreOficial");

        tvNombreOficial.setText(strCodOfi+" - "+strOficial);

        url = spWeb+"/"+spEmpresa+"/listar_saldos.php?rol="+strRol+
                "&codOfi="+strCodOfi+"&numSuc="+strNumSuc;
        saldos(url);



        lvProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SaldosModel sm = (SaldosModel) adapter.getItem(position);

                Intent i = new Intent(getApplicationContext(), balance_credito.class);
                i.putExtra("codcli", sm.getCodCli());
                i.putExtra("nomcli", sm.getNomCli());
                i.putExtra("codCred", sm.getCodCre());
                i.putExtra("fecha", sm.getFechaIni());

                startActivity(i);
            }
        });

        lvProduct.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                SaldosModel sm = (SaldosModel) adapter.getItem(position);
                capturarPago(position);
                return true;
            }
        });


/*
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


 */


;
    }
/*
    protected void onResume(){
        super.onResume();
        lvProduct.setAdapter(null);
        saldos(url);
        lvProduct.setAdapter(adapter);

    }


 */

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
                    arrayList.clear();
                    for(int i =0;i < response.length(); i++){

                        jsonObject = response.getJSONObject(i);

                        String codCli       = (jsonObject.getString("codCli"));
                        String nomCli       = (jsonObject.getString("nomCli"));
                        String telefono     = (jsonObject.getString("telefono"));
                        String codCre       = (jsonObject.getString("codCredi"));
                        String saldoTotal   = (jsonObject.getString("saldoTotal"));
                        String moraTotal    = (jsonObject.getString("moraTotal"));
                        String vencidoTotal = (jsonObject.getString("vencidoTotal"));
                        String fechaIni     = (jsonObject.getString("fecha"));
                        String fechaFin     = (jsonObject.getString("fechafin"));
                        String nomdia       = (jsonObject.getString("nomdia"));
                        String concurr      = (jsonObject.getString("concurr"));
                        String cuota        = (jsonObject.getString("cuota"));
                        String pagoHoy      = (jsonObject.getString("pagosHoy"));
                        String ncHoy        = (jsonObject.getString("ncHoy"));
                        String ndHoy        = (jsonObject.getString("ndHoy"));
                        String porcentajeSaldo        = (jsonObject.getString("porcentajeSaldo"));


                        double saldo = Double.parseDouble(saldoTotal);
                        double pago  = Double.parseDouble(pagoHoy);
                        double nc    = Double.parseDouble(ncHoy);
                        double nd    = Double.parseDouble(ndHoy);
                        double dbmora= Double.parseDouble(moraTotal);
                        double dbVenc=Double.parseDouble(vencidoTotal);
                        double saldoNuevo = (saldo+nd)-(pago+nc);

                        if((pago+nc)<= dbVenc){
                            dbVenc = dbVenc-pago-nc;
                        }else if((pago+nc) > dbVenc){
                            dbVenc = 0.00;
                        }

                        if((pago+nc)<= dbmora){
                            dbmora = dbmora-pago-nc;
                        }else if((pago+nc) > dbmora){
                            dbmora = 0.00;

                        }


                        String strSaldo = String.valueOf(saldoNuevo);
                        String strVenc  = String.valueOf(dbVenc);
                        String strMora  = String.valueOf(dbmora);

                        arrayList.add(new SaldosModel(codCli,nomCli,telefono, codCre, strSaldo,
                                strMora,  strVenc, fechaIni, fechaFin, nomdia, concurr, cuota,
                                pagoHoy, ncHoy, ndHoy, porcentajeSaldo));
                        adapter = new SaldosListAdapter(ListaSaldos.this, arrayList, spEmpresa, spRol);
                        lvProduct.setAdapter(adapter);
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




    private void capturarPago (int pos){
        SaldosModel sm = (SaldosModel) adapter.getItem(pos);
        lcCodCli = sm.getCodCli();
        lcNomCli = sm.getNomCli();
        lcCodCre = sm.getCodCre();
        lnSalAnt = sm.getSaldoTot();
        telefono = sm.getTelefono();
        lnCuota  = sm.getCuota();
        pagoHoy  = sm.getPagoHoy();
        ncHoy    = sm.getNcHoy();
        ndHoy    = sm.getNdHoy();

        AlertDialog.Builder guardar = new AlertDialog.Builder(ListaSaldos.this, R.style.AlertDialogTheme);
        final View customLayout = getLayoutInflater().inflate(R.layout.custom_layout, null);
        EditText editText = customLayout.findViewById(R.id.txtValorPago);
        editText.setText(lnCuota);
        guardar.setView(customLayout);
        guardar.setTitle("Valor a pagar \n"+lcNomCli +"\n"+ "Crédito Nº:"+lcCodCre);
        guardar.setCancelable(true);
        guardar.setPositiveButton("Grabar", new DialogInterface.OnClickListener() {
            EditText editText = customLayout.findViewById(R.id.txtValorPago);
            @Override
            public void onClick(DialogInterface dialog, int which) {
                lnValPag = editText.getText().toString();
                AlertDialog.Builder confirmar = new AlertDialog.Builder(ListaSaldos.this, R.style.AlertDialogTheme);

                final View customLayout = getLayoutInflater().inflate(R.layout.custom_layout, null);
                EditText editText = customLayout.findViewById(R.id.txtValorPago);
                editText.setText(lnValPag);
                confirmar.setView(customLayout);
                confirmar.setTitle("Confirme el valor a pagar");
                confirmar.setCancelable(true);
                confirmar.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    EditText editText = customLayout.findViewById(R.id.txtValorPago);
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        guardarPago(spWeb+"/"+spEmpresa+"/insertar_pago.php");

                    }
                });
                confirmar.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog dial = confirmar.create();
                dial.show();

            }
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



    private void guardarPago (String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                if (response.contains("CONECTADO")){
                    Toast.makeText(getApplicationContext(), "SE HA ABIERTO SERSION EN OTRO DISPOSTIVO, LA SESION EN ESTE DISPOSITIVO SE HA CERRADO", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getApplicationContext(), Login.class);
                    startActivity(i);
                }

                if (response.contains("EXITO")){
                    Toast.makeText(getApplicationContext(), "GRABADO CORRECTAMAMENTE", Toast.LENGTH_LONG).show();
                    double dbSaldoAnt   = Double.parseDouble(lnSalAnt); //Monto del principal  lnSalAnt-lnValPag;
                    double dbCuota      = Double.parseDouble(lnValPag); //Monto del principallnSalAnt-lnValPag;
                    double dbSaldoAct   = dbSaldoAnt-dbCuota;
                    lnSalAct = (String.format("%.2f", dbSaldoAct));

                    functions.sendSms("pago",telefono,lcNomCli, lcCodCre, lnValPag,lnSalAct, spEmpresa, ListaSaldos.this);
                    //enviarsms(telefono);
                    progreso.dismiss();

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
                parametros.put("codCli", lcCodCli);
                parametros.put("codCred",lcCodCre);
                parametros.put("codOfi",spCodOfi);
                parametros.put("ofiCobro", spOfiCobro);
                parametros.put("valor", lnValPag);
                parametros.put("aplicar","N");
                parametros.put("numSuc",spNumSuc);
                parametros.put("token",spToken);

                return parametros;
            }
        };
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void loading(){
        progreso = new ProgressDialog(ListaSaldos.this);
        progreso.setCancelable(false);
        progreso.setTitle("Cargando...");
        progreso.show();
        progreso.setContentView(R.layout.progress_dialog);
        progreso.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    public void filtrar(String texto) {
        ArrayList<SaldosModel> filtrarLista = new ArrayList<>();

        for(SaldosModel saldos : arrayList) {
            if(saldos.getNomCli().toLowerCase().contains(texto.toLowerCase())
                    || saldos.getCodCli().toLowerCase().contains(texto.toLowerCase())
                    || saldos.getCodCre().toLowerCase().contains(texto.toLowerCase())) {
                filtrarLista.add(saldos);
            }
        }

        adapter.filtrar(filtrarLista);
    }



/*
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
             /*   SwipeMenuItem openItem = new SwipeMenuItem(getApplicationContext());
                openItem.setBackground(new ColorDrawable(Color.blue(2)));
                openItem.setWidth((170));
                openItem.setTitle("Open");
                openItem.setTitleSize(18);
                openItem.setTitleColor(Color.blue(10));
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                deleteItem.setWidth(170);
                deleteItem.setIcon(R.drawable.payment);
                menu.addMenuItem(deleteItem);
            }
        };

  */

// set creator
        /*
lvProduct.setMenuCreator(creator);
        lvProduct.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        Intent i = new Intent(getApplicationContext(), construccion.class);
                        startActivity(i);
                        break;
                    case 1:
                        lcCodCli = arrayList.get(position).getCodCli();
                        lcCodCre = arrayList.get(position).getCodCre();
                        lcNomCli = arrayList.get(position).getNomCli();

                        AlertDialog.Builder guardar = new AlertDialog.Builder(ListaSaldos.this, R.style.AlertDialogTheme);
                            final View customLayout = getLayoutInflater().inflate(R.layout.custom_layout, null);
                            EditText editText = customLayout.findViewById(R.id.txtValorPago);
                            editText.setText(arrayList.get(position).getCuota());
                            guardar.setView(customLayout);
                            guardar.setTitle("Valor a pagar \n"+lcNomCli +"\n"+ "Crédito Nº:"+lcCodCre);
                            guardar.setCancelable(true);
                            guardar.setPositiveButton("Grabar", new DialogInterface.OnClickListener() {
                                EditText editText = customLayout.findViewById(R.id.txtValorPago);
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    lnValPag = editText.getText().toString();
                                    AlertDialog.Builder confirmar = new AlertDialog.Builder(ListaSaldos.this, R.style.AlertDialogTheme);

                                    final View customLayout = getLayoutInflater().inflate(R.layout.custom_layout, null);
                                    EditText editText = customLayout.findViewById(R.id.txtValorPago);
                                    editText.setText(lnValPag);
                                    confirmar.setView(customLayout);
                                    confirmar.setTitle("Confirme el valor a pagar");
                                    confirmar.setCancelable(true);
                                    confirmar.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                                        EditText editText = customLayout.findViewById(R.id.txtValorPago);
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                             guardarPago("https://"+spWeb+"/"+spEmpresa+"/insertar_pago.php");
                                        }
                                    });
                                    confirmar.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                                    AlertDialog dial = confirmar.create();
                                    dial.show();
                                    Toast.makeText(getApplicationContext(),editText.getText().toString(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        guardar.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        AlertDialog dialog = guardar.create();
                        dialog.show();

                        break;
                }
                // false : c lose the menu; true : not close the menu
                return false;
            }
        });
*/


}