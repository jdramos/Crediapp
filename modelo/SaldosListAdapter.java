package com.example.credimovil.modelo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;

import com.example.credimovil.Functions;
import com.example.credimovil.R;

import java.util.ArrayList;

public class SaldosListAdapter extends BaseAdapter implements Filterable {

    private Context mContext;
    private ArrayList<SaldosModel> mSaldosList;
    CustomFilter filter;
    private ArrayList<SaldosModel> filterList;
    Functions functions = new Functions();
    String company, rol;


    public SaldosListAdapter(Context mContext, ArrayList<SaldosModel> mSaldosList, String company,
                             String rol) {
        this.mContext = mContext;
        this.mSaldosList = mSaldosList;
        this.company = company;
        this.rol = rol;
    }


        @Override
        public int getCount() {
            return mSaldosList.size();
        }


    @Override
    public Object getItem(int position) {
        return mSaldosList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    //codCli, nomCli, codCre, saldoTot, moraTot,  vencidoTot, fechaVence,  concurrencia, valCuota;
    @SuppressLint({"SetTextI18n"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        @SuppressLint("ViewHolder") View v = View.inflate(mContext, R.layout.item_saldos_list,null);
        TextView tvCliente = v.findViewById(R.id.tvCliente);
        TextView tvCredito = v.findViewById(R.id.tvCredito);
        TextView tvFechas = v.findViewById(R.id.tvFechas);
        TextView tvSaldos = v.findViewById(R.id.tvSaldos);
        CardView cardSaldos = v.findViewById(R.id.cardSaldos);
        Button sendMsj = v.findViewById(R.id.btnSend);


        if(rol.equals("3")){
            sendMsj.setVisibility(View.GONE);
        }


        float floatSaldo =Float.parseFloat(mSaldosList.get(position).getSaldoTot());
        float floatMonto =Float.parseFloat(mSaldosList.get(position).getPorcentajeSaldo());
        float floatMora =Float.parseFloat(mSaldosList.get(position).getMoraTot());
        float floatVencido =Float.parseFloat(mSaldosList.get(position).getVencidoTot());
        float porcentajeSaldo =Float.parseFloat(mSaldosList.get(position).getPorcentajeSaldo());
        final String celphone = mSaldosList.get(position).getTelefono();
        final String client = mSaldosList.get(position).getNomCli();



        sendMsj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setMessage("Confirme enviar mensaje")
                            .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    functions.sendSms("renovacion", celphone, client, "","","", company, mContext);

                                }
                            })
                            .setNegativeButton("Cancelar", null);

                    AlertDialog alert = builder.create();
                    alert.show();

                }
            }
        });


        if (porcentajeSaldo>30.00 || (floatMora+floatVencido)>0){
            sendMsj.setVisibility(View.GONE);

        }

        if (floatMora>0 || floatVencido>0){
            cardSaldos.setCardBackgroundColor(Color.RED);
        }


        tvCliente.setText(mSaldosList.get(position).getCodCli()+" - "+mSaldosList.get(position).getNomCli());
        tvCredito.setText(
                        "Crédito nº: "+mSaldosList.get(position).getCodCre()+
                        " | "+mSaldosList.get(position).getConcurrencia()+
                                "-"+mSaldosList.get(position).getNomDia()+
                        " | Cuota: "+mSaldosList.get(position).getCuota());
        tvFechas.setText("Desembolso: "+mSaldosList.get(position).getFechaIni()+
                        " | Vence: "+mSaldosList.get(position).getFechaVence());
        tvSaldos.setText("Saldo: "+mSaldosList.get(position).getSaldoTot()+
                        " | Mora: "+mSaldosList.get(position).getMoraTot()+
                        " | Vencido: "+mSaldosList.get(position).getVencidoTot()
        );
        v.setTag(mSaldosList.get(position).getCodCli());
        return v;
    }


    @Override
    public Filter getFilter() {

        if (filter == null){
            filter = new CustomFilter();
        }
        return filter;
    }

    class CustomFilter extends Filter{
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();
            if(constraint!=null&&constraint.length()>0){
                constraint=constraint.toString().toUpperCase();
                ArrayList<SaldosModel> filters = new ArrayList<>();
                for (int i = 0;i<filterList.size();i++){
                    if(filterList.get(i).getNomCli().toUpperCase().contains(constraint)
                            ||filterList.get(i).getCodCre().contains(constraint)
                            ||filterList.get(i).getCodCli().contains(constraint)){
                                SaldosModel e = new SaldosModel(filterList.get(i).getCodCli(),
                                        filterList.get(i).getNomCli(),
                                        filterList.get(i).getTelefono(),
                                        filterList.get(i).getCodCre(),
                                        filterList.get(i).getSaldoTot(),
                                        filterList.get(i).getMoraTot(),
                                        filterList.get(i).getFechaIni(),
                                        filterList.get(i).getVencidoTot(),
                                        filterList.get(i).getFechaVence(),
                                        filterList.get(i).getNomDia(),
                                        filterList.get(i).getConcurrencia(),
                                        filterList.get(i).getCuota(),
                                        filterList.get(i).getPagoHoy(),
                                        filterList.get(i).getNcHoy(),
                                        filterList.get(i).getNdHoy(),
                                        filterList.get(i).getPorcentajeSaldo());
                        filters.add((e));
                    }
                }
                results.count= filters.size();
                results.values  = filters;
            }else{
                results.count = filterList.size();
                results.values = filterList;

            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mSaldosList=(ArrayList<SaldosModel>)results.values;
            notifyDataSetChanged();
        }
    }

    public void filtrar(ArrayList<SaldosModel> filtroUsuarios) {
        this.mSaldosList = filtroUsuarios;
        notifyDataSetChanged();
    }
}
