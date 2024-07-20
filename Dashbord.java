package com.example.credimovil;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

public class Dashbord extends AppCompatActivity implements View.OnClickListener {

    final private int REQUEST_SMS=111;
    CardView pagosCard, solicitudesCard, autorizacionesCard, salirCard, acercaCard, permisosCard;
    SharedPreferences preferencias;
    String mnAutoriza, mnCobros, cambiarClave, actualizaClave, spNumSuc, spCodOfi, rol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashbord);

        preferencias = getSharedPreferences("usuario", Context.MODE_PRIVATE);
        mnAutoriza = preferencias.getString("mnAutoriza", null);
        mnCobros = preferencias.getString("mnCobros", null);
        cambiarClave = preferencias.getString("cambioClave", null);
        actualizaClave = preferencias.getString("actualizaClave", null);
        spNumSuc = preferencias.getString("numsuc", null);
        spCodOfi = preferencias.getString("codofi", null);
        rol = preferencias.getString("rol", null);


        pagosCard = findViewById(R.id.pagos_card);
        solicitudesCard = findViewById(R.id.solicitud_card);
        autorizacionesCard = findViewById(R.id.autorizacion_card);
        salirCard = findViewById(R.id.clientes_card);
        acercaCard = findViewById(R.id.info_card);
        permisosCard = findViewById(R.id.cobros_card);

        this.setTitle("");
/*
        if (mnAutoriza.equals("0")){
            autorizacionesCard.setVisibility(View.GONE);
        }
        if (mnCobros.equals("0")){
            pagosCard.setVisibility(View.GONE);
        }


 */


        pagosCard.setOnClickListener(this);
        solicitudesCard.setOnClickListener(this);
        autorizacionesCard.setOnClickListener(this);
        salirCard.setOnClickListener(this);
        acercaCard.setOnClickListener(this);
        permisosCard.setOnClickListener(this);
        
        solicitarPermisos();
    }

    private void solicitarPermisos() {
        int permisoSms = ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SEND_SMS);
        if (permisoSms != PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
                requestPermissions(new String[] {Manifest.permission.SEND_SMS}, REQUEST_SMS);
            }
        }
    }

    @Override
    public void onClick(View v) {
        Intent i;

        switch (v.getId()){
            case R.id.pagos_card :
                if(rol.equals("3")){
                    i = new Intent(this, ListaSaldosOficial.class);
                    i.putExtra("numSuc", spNumSuc);
                    i.putExtra("codOfi", spCodOfi);
                    i.putExtra("rol", rol);
                    startActivity(i);
                }else if(rol.equals("2") || rol.equals("4")) {
                    i = new Intent(this, ListaSaldosOficial.class);
                    i.putExtra("numSuc", spNumSuc);
                    i.putExtra("codOfi", spCodOfi);
                    i.putExtra("rol", rol);
                    startActivity(i);
                }else if(rol.equals("1")){
                    i = new Intent(this, ListaSaldosSucursal.class);
                    i.putExtra("numSuc", spNumSuc);
                    i.putExtra("codOfi", spCodOfi);
                    i.putExtra("rol", rol);
                    startActivity(i);
                }

                break;
            case R.id.solicitud_card :
                if(rol.equals("3")){
                    i = new Intent(this, ListaSolicitudes.class);
                    startActivity(i);
                }else{
                    i = new Intent(this, ListaSolicitudSucursal.class);
                    startActivity(i);
                }
                break;
            case R.id.autorizacion_card :
                i = new Intent(this, miproductividad.class);
                startActivity(i);
                break;
            case R.id.cobros_card:
                switch (rol) {
                    case "3":
                        i = new Intent(this, ListaCobros.class);
                        i.putExtra("numSuc", spNumSuc);
                        i.putExtra("codOfi", spCodOfi);
                        i.putExtra("rol", rol);
                        startActivity(i);
                        break;
                    case "2":
                        i = new Intent(this, ListaCobrosOficial.class);
                        i.putExtra("numSuc", spNumSuc);
                        startActivity(i);
                        break;
                    case "1":
                        i = new Intent(this, ListaCobrosSucursal.class);
                        i.putExtra("rol", rol);
                        startActivity(i);
                        break;
                    case "4":
                        i = new Intent(this, ListaCobrosSucursal.class);
                        i.putExtra("rol", rol);
                        startActivity(i);
                        break;
                }
                break;


            case R.id.info_card :
                i = new Intent(this, construccion.class);
                //i = new Intent(this, solicitudes_agregar.class);
                startActivity(i);
                break;
            case R.id.clientes_card :
                i = new Intent(this, construccion.class);
                //i = new Intent(this, solicitudes_agregar.class);
                startActivity(i);
                break;

            default:break;
        }

    }

    public void salir(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Dashbord.this);
        builder.setMessage("Presiones confirmar para cerrar la aplicación")
                .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("Cancelar", null);

        AlertDialog alert = builder.create();
        alert.show();

    }

    public void onBackPressed() {
        super.onBackPressed();
        salir();

    }


}