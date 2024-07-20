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

public class SolicitudesSucursalesAdapter extends BaseAdapter implements Filterable {

    private Context mContext;
    private ArrayList<SolicitudesSucursalModel> mSolicitudesList;
    CustomFilter filter;
    private ArrayList<SolicitudesSucursalModel> filterList;
    DecimalFormat decimalFormat = new DecimalFormat("#,###.00");


    public SolicitudesSucursalesAdapter(Context mContext, ArrayList<SolicitudesSucursalModel> mSolicitudesList) {
        this.mContext = mContext;
        this.mSolicitudesList = mSolicitudesList;
    }


        @Override
        public int getCount() {
            return mSolicitudesList.size();
        }


    @Override
    public Object getItem(int position) {
        return mSolicitudesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        @SuppressLint("ViewHolder") View v = View.inflate(mContext, R.layout.item_solicitudes_sucursales,null);
        TextView tvSucursal = v.findViewById(R.id.tvSucursal);
        TextView tvCantSolicitudes = v.findViewById(R.id.tvCantSolicitudes);
        TextView tvValorTotal = v.findViewById(R.id.tvValorTotal);


        double dbCantCobros = Double.parseDouble(mSolicitudesList.get(position).getCantSolicitudes());
        double dbValorTotal = Double.parseDouble(mSolicitudesList.get(position).getSolicitudTotal());

        String strValorTotal = decimalFormat.format(dbValorTotal);
        String strCantTotal = decimalFormat.format(dbCantCobros);

        tvSucursal.setText(mSolicitudesList.get(position).getNumSuc()+" - "+mSolicitudesList.get(position).getNomSuc());
        tvCantSolicitudes.setText("Cant. Solicitudes: "+strCantTotal);
        tvValorTotal.setText("Total solicitado: "+strValorTotal);

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
                ArrayList<SolicitudesSucursalModel> filters = new ArrayList<>();
                for (int i = 0;i<filterList.size();i++){
                    if(filterList.get(i).getNumSuc().toUpperCase().contains(constraint)
                            ||filterList.get(i).getNomSuc().contains(constraint)){
                        SolicitudesSucursalModel e = new SolicitudesSucursalModel(filterList.get(i).getNumSuc(),
                                        filterList.get(i).getNomSuc(),
                                        filterList.get(i).getCantSolicitudes(),
                                        filterList.get(i).getSolicitudTotal());
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
            mSolicitudesList = (ArrayList<SolicitudesSucursalModel>) results.values;
            notifyDataSetChanged();
        }
    }

    public void filtrar(ArrayList<SolicitudesSucursalModel> filtroSucursales) {
        this.mSolicitudesList = filtroSucursales;
        notifyDataSetChanged();
    }
}
