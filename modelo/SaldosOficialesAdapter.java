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

public class SaldosOficialesAdapter extends BaseAdapter implements Filterable {

    private Context mContext;
    private ArrayList<SaldosOficialesModel> mSaldosList;
    CustomFilter filter;
    private ArrayList<SaldosOficialesModel> filterList;
    DecimalFormat decimalFormat = new DecimalFormat("#,###.00");


    public SaldosOficialesAdapter(Context mContext, ArrayList<SaldosOficialesModel> mSaldosList) {
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
        @SuppressLint("ViewHolder") View v = View.inflate(mContext, R.layout.item_saldos_sucursales,null);
        TextView tvSucursal = v.findViewById(R.id.tvSucursal);
        TextView tvCantClies = v.findViewById(R.id.tvCantClies);
        TextView tvCredito = v.findViewById(R.id.tvCredito);
        TextView tvFechas = v.findViewById(R.id.tvFechas);
        TextView tvSaldos = v.findViewById(R.id.tvSaldos);
        TextView tvIngresos     = v.findViewById(R.id.tvIngresos);


        double dblSaldoTotal = Double.parseDouble(mSaldosList.get(position).getSaldoTot());
        double dblMoraTotal = Double.parseDouble(mSaldosList.get(position).getMoraTot());
        double dblVencidoTotal = Double.parseDouble(mSaldosList.get(position).getVencidoTot());
        double dblIngresos = Double.parseDouble(mSaldosList.get(position).getIngresos());

        String strSaldoTotal = decimalFormat.format(dblSaldoTotal);
        String strMoraTotal = decimalFormat.format(dblMoraTotal);
        String strVencidoTotal = decimalFormat.format(dblVencidoTotal);
        String strIngresos = decimalFormat.format(dblIngresos);

        tvSucursal.setText(mSaldosList.get(position).getCodOfi()+" - "+mSaldosList.get(position).getNomOfi());
        tvCantClies.setText("Total clientes: "+mSaldosList.get(position).getCantClientes());
        tvCredito.setText("Total Cartera: "+strSaldoTotal);
        tvFechas.setText("Total Mora: "+strMoraTotal);
        tvSaldos.setText("Total Vencido: "+strVencidoTotal);
        tvIngresos.setText("Total ingresos: "+strIngresos);
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
                ArrayList<SaldosOficialesModel> filters = new ArrayList<>();
                for (int i = 0;i<filterList.size();i++){
                    if(filterList.get(i).getNomOfi().toUpperCase().contains(constraint)
                            ||filterList.get(i).getNomOfi().contains(constraint)){
                        SaldosOficialesModel e = new SaldosOficialesModel(filterList.get(i).getCodOfi(),
                                        filterList.get(i).getNomOfi(),
                                        filterList.get(i).getCantClientes(),
                                        filterList.get(i).getSaldoTot(),
                                        filterList.get(i).getMoraTot(),
                                        filterList.get(i).getVencidoTot(),
                                        filterList.get(i).getPagoHoy(),
                                        filterList.get(i).getNcHoy(),
                                        filterList.get(i).getNdHoy(),
                                        filterList.get(i).getIngresos());
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
            mSaldosList=(ArrayList<SaldosOficialesModel>)results.values;
            notifyDataSetChanged();
        }
    }

    public void filtrar(ArrayList<SaldosOficialesModel> filtroSucursales) {
        this.mSaldosList = filtroSucursales;
        notifyDataSetChanged();
    }
}
