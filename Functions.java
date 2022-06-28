package com.example.credimovil;

import android.app.ProgressDialog;
import android.content.Context;
import android.telephony.SmsManager;
import android.widget.Toast;

public class Functions {

    public void sendSms(String tipo, String celular, String cliente, String codCred,
                        String valor, String saldoActual, String empresa, Context context){

        String msj = "";
        if (tipo.equals("pago")){
            msj = "Estimado Sr(a): " + cliente + " pago por C$ "+valor+" credito#"+codCred+
                    " saldo C$"+saldoActual+" agredecemos su preferencia. "+empresa.toUpperCase();
        }

        if (tipo.equals("renovacion")){
            msj = "Estimado Sr(a): " + cliente + ", le informamos que su credito ya " +
                    "aplica renovacion, comuniquese con su oficial. "+empresa.toUpperCase();
        }

        try{
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(celular, null, msj , null, null);
            Toast.makeText(context, "Mensaje enviado", Toast.LENGTH_SHORT).show();
        }catch (Exception e){

        }


    }



}
