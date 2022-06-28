package com.example.credimovil;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.camera2.TotalCaptureResult;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.credimovil.modelo.SaldosListAdapter;
import com.example.credimovil.modelo.SaldosModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;



public class miproductividad extends AppCompatActivity {


    PieChart pieChart;
    RequestQueue requestQueue;
    ArrayList<PieEntry> visitors = new ArrayList<>();
    String  spWeb, spEmpresa, url, spNumSuc, spCodOfi, rol, nombre;
    SharedPreferences preferencias;
    TextView tvTitulo, tvNombre;
    DecimalFormat precision = new DecimalFormat("#,##0.00");
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_miproductividad);

        preferencias = getSharedPreferences("usuario", Context.MODE_PRIVATE);
        spNumSuc = preferencias.getString("numsuc", null);
        spCodOfi = preferencias.getString("codofi", null);
        spWeb = preferencias.getString("web", null);
        spEmpresa = preferencias.getString("empresa", null);
        rol = preferencias.getString("rol", null);
        nombre = preferencias.getString("nombre", null);
        pieChart = findViewById(R.id.pieChart);
        tvTitulo = findViewById(R.id.tvTitulo);
        tvNombre = findViewById(R.id.tvNombre);
        tvNombre.setText(nombre);

        url = spWeb+"/"+spEmpresa+"/productividad.php?rol="+rol+
                "&codOfi="+spCodOfi+"&numSuc="+spNumSuc;

        saldos(url);
        pieChart.animate();
        pieChart.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);


        pieChart.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartLongPressed(MotionEvent me) {

            }

            @Override
            public void onChartDoubleTapped(MotionEvent me) {

            }

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @SuppressLint("ResourceType")
            @Override
            public void onChartSingleTapped(MotionEvent me) {


            }

            @Override
            public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

            }

            @Override
            public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

            }

            @Override
            public void onChartTranslate(MotionEvent me, float dX, float dY) {

            }
        });

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

                try {
                    String strSaldo = null;
                    float dbSaldoT;
                    String strVenc;
                    String strMora;
                    String strSano = null;

                    for(int i =0;i < response.length(); i++){

                        jsonObject = response.getJSONObject(i);


                        String saldoTotal   = jsonObject.getString("saldoTotal");
                        String moraTotal    = (jsonObject.getString("moraTotal"));
                        String vencidoTotal = (jsonObject.getString("vencidoTotal"));


                        double saldo = Double.parseDouble(saldoTotal);
                        double dbmora= Double.parseDouble(moraTotal);
                        double dbVenc=Double.parseDouble(vencidoTotal);
                        double saldoSano  = (saldo-dbmora-dbVenc);

                        strSaldo  = saldoTotal;
                        strVenc  = vencidoTotal;
                        strMora  = moraTotal;

                        strSano = String.valueOf(saldoSano);

                        visitors.add(new PieEntry(Float.parseFloat(strSaldo), "Corriente"));
                        visitors.add(new PieEntry(Float.parseFloat(strVenc), "Vencido"));
                        visitors.add(new PieEntry(Float.parseFloat(strMora), "Mora"));

                    }
                    PieDataSet pieDataSet = new PieDataSet(visitors,"");
                    pieDataSet.setColors(new int[] { R.color.blue, R.color.vencido,
                            R.color.mora,}, getApplicationContext());
                    pieDataSet.setValueTextColor(Color.WHITE);
                    pieDataSet.setValueTextSize(16f);



                    PieData pieData = new PieData(pieDataSet);
                    dbSaldoT = Float.parseFloat(strSaldo);
                    pieChart.setData(pieData);
                    pieChart.getDescription().setEnabled(false);
                    pieChart.setCenterText("Saldo Total: "+Float.parseFloat(strSaldo));






                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "catch"+ e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "error "+ error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        );

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);

    }
}