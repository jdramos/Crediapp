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

public class CobrosSucursalesAdapter extends BaseAdapter implements Filterable {

    private Context mContext;
    private ArrayList<CobrosSucursalModel> mSaldosList;
    CustomFilter filter;
    private ArrayList<CobrosSucursalModel> filterList;
    DecimalFormat decimalFormat = new DecimalFormat("#,###.00");


    public CobrosSucursalesAdapter(Context mContext, ArrayList<CobrosSucursalModel> mSaldosList) {
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
        @SuppressLint("ViewHolder") View v = View.inflate(mContext, R.layout.item_cobros_sucursales,null);
        TextView tvSucursal = v.findViewById(R.id.tvSucursal);
        TextView tvCantCobros = v.findViewById(R.id.tvCantCobros);
        TextView tvValorTotal = v.findViewById(R.id.tvValorTotal);


        double dbCantCobros = Double.parseDouble(mSaldosList.get(position).getCantCobros());
        double dbValorTotal = Double.parseDouble(mSaldosList.get(position).getCobroTotal());

        String strValorTotal = decimalFormat.format(dbValorTotal);
        String strCantTotal = decimalFormat.format(dbCantCobros);

        tvSucursal.setText(mSaldosList.get(position).getNumSuc()+" - "+mSaldosList.get(position).getNomSuc());
        tvCantCobros.setText("Cant. Cobros: "+strCantTotal);
        tvValorTotal.setText("Total Cobrado: "+strValorTotal);

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
                ArrayList<CobrosSucursalModel> filters = new ArrayList<>();
                for (int i = 0;i<filterList.size();i++){
                    if(filterList.get(i).getNumSuc().toUpperCase().contains(constraint)
                            ||filterList.get(i).getNomSuc().contains(constraint)){
                        CobrosSucursalModel e = new CobrosSucursalModel(filterList.get(i).getNumSuc(),
                                        filterList.get(i).getNomSuc(),
                                        filterList.get(i).getCantCobros(),
                                        filterList.get(i).getCobroTotal());
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
            mSaldosList=(ArrayList<CobrosSucursalModel>)results.values;
            notifyDataSetChanged();
        }
    }

    public void filtrar(ArrayList<CobrosSucursalModel> filtroSucursales) {
        this.mSaldosList = filtroSucursales;
        notifyDataSetChanged();
    }
}
