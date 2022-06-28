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

import java.util.ArrayList;

public class CobrosListAdapter extends BaseAdapter implements Filterable {

    private Context mContext;
    private ArrayList<CobrosModel> mCobrosList;
    CustomFilter filter;
    private ArrayList<CobrosModel> filterList;

    public CobrosListAdapter(Context mContext, ArrayList<CobrosModel> mCobrosList) {
        this.mContext = mContext;
        this.mCobrosList = mCobrosList;
    }


        @Override
        public int getCount() {
            return mCobrosList.size();
        }


    @Override
    public Object getItem(int position) {
        return mCobrosList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    //codCli, nomCli, codCre, saldoTot, moraTot,  vencidoTot, fechaVence,  concurrencia, valCuota;
    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        @SuppressLint("ViewHolder") View v = View.inflate(mContext, R.layout.cobros_layout,null);
        TextView tvCodCli = v.findViewById(R.id.tvCodCli);
        TextView tvNomCli = v.findViewById(R.id.tvNomCli);
        TextView tvCodCred = v.findViewById(R.id.tvMonto);
        TextView tvMonto = v.findViewById(R.id.tvEstado);


        tvCodCli.setText(mCobrosList.get(position).getCodCli());
        tvNomCli.setText(mCobrosList.get(position).getNomCli());
        tvCodCred.setText("Crédito: "+ mCobrosList.get(position).getCodCre());
        tvMonto.setText("Valor "+ mCobrosList.get(position).getMonto());

        v.setTag(mCobrosList.get(position).getCodCli());
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
                ArrayList<CobrosModel> filters = new ArrayList<>();
                for (int i = 0;i<filterList.size();i++){
                    if(filterList.get(i).getNomCli().toUpperCase().contains(constraint)
                            ||filterList.get(i).getCodCre().contains(constraint)
                            ||filterList.get(i).getCodCli().contains(constraint)){
                                CobrosModel e = new CobrosModel(
                                        filterList.get(i).getIdPago(),
                                        filterList.get(i).getCodCli(),
                                        filterList.get(i).getNomCli(),
                                        filterList.get(i).getCodCre(),
                                        filterList.get(i).getMonto(),
                                        filterList.get(i).getTelefono());
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
            mCobrosList =(ArrayList<CobrosModel>)results.values;
            notifyDataSetChanged();
        }
    }

    public void filtrar(ArrayList<CobrosModel> filtroUsuarios) {
        this.mCobrosList = filtroUsuarios;
        notifyDataSetChanged();
    }
}
