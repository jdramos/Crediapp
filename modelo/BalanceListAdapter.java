package com.example.credimovil.modelo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.credimovil.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class BalanceListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<BalanceModel> mSaldosList;
    DecimalFormat decimalFormat = new DecimalFormat("#,###.00");


    public BalanceListAdapter(Context mContext, ArrayList<BalanceModel> mSaldosList) {
        this.mContext = mContext;
        this.mSaldosList = mSaldosList;
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
    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        @SuppressLint("ViewHolder") View v = View.inflate(mContext, R.layout.item_balance_list,null);
        TextView tvFecha = v.findViewById(R.id.txtFecha);
        TextView tvMonto = v.findViewById(R.id.txtMonto);
        TextView tvSaldo = v.findViewById(R.id.txtSaldo);

        float floatPago = Float.parseFloat(mSaldosList.get(position).getMonto());
        float floatSaldo = Float.parseFloat(mSaldosList.get(position).getSaldo());

        tvFecha.setText(mSaldosList.get(position).getFecha());
        tvMonto.setText(decimalFormat.format(floatPago));
        tvSaldo.setText(decimalFormat.format(floatSaldo));
        return v;
    }


}
