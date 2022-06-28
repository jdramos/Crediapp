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

public class CobrosOficialesAdapter extends BaseAdapter implements Filterable {

    private Context mContext;
    private ArrayList<CobrosOficialesModel> mSaldosList;
    CustomFilter filter;
    private ArrayList<CobrosOficialesModel> filterList;
    DecimalFormat decimalFormat = new DecimalFormat("#,###.00");


    public CobrosOficialesAdapter(Context mContext, ArrayList<CobrosOficialesModel> mSaldosList) {
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


    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        @SuppressLint("ViewHolder") View v = View.inflate(mContext, R.layout.item_cobros_oficiales,null);
        TextView tvOficial = v.findViewById(R.id.tvOficial);
        TextView tvCantCobros = v.findViewById(R.id.tvCantCobros);
        TextView tvValorTotal = v.findViewById(R.id.tvValorTotal);

        double dblCantCobros = Double.parseDouble(mSaldosList.get(position).getCantPagos());
        double dblValorTotal = Double.parseDouble(mSaldosList.get(position).getValorTot());


        String strCantCobros = decimalFormat.format(dblCantCobros);
        String strValorTotal = decimalFormat.format(dblValorTotal);


        tvOficial.setText(mSaldosList.get(position).getCodOfi()+" - "+mSaldosList.get(position).getNomOfi());
        tvCantCobros.setText("Cantidad cobros: "+mSaldosList.get(position).getCantPagos());
        tvValorTotal.setText("Valor cobrado: "+mSaldosList.get(position).getValorTot());

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
                ArrayList<CobrosOficialesModel> filters = new ArrayList<>();
                for (int i = 0;i<filterList.size();i++){
                    if(filterList.get(i).getCodOfi().toUpperCase().contains(constraint)
                            ||filterList.get(i).getNomOfi().contains(constraint)){
                        CobrosOficialesModel e = new CobrosOficialesModel(filterList.get(i).getCodOfi(),
                                        filterList.get(i).getNomOfi(),
                                        filterList.get(i).getCantPagos(),
                                        filterList.get(i).getValorTot());
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
            mSaldosList=(ArrayList<CobrosOficialesModel>)results.values;
            notifyDataSetChanged();
        }
    }

    public void filtrar(ArrayList<CobrosOficialesModel> filtroSucursales) {
        this.mSaldosList = filtroSucursales;
        notifyDataSetChanged();
    }
}
