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

public class SolicitudesOficialesAdapter extends BaseAdapter implements Filterable {

    private Context mContext;
    private ArrayList<SolicitudesModel> mSolicitudesList;
    CustomFilter filter;
    private ArrayList<SolicitudesModel> filterList;
    DecimalFormat precision = new DecimalFormat("#,##0.00");

    public SolicitudesOficialesAdapter(Context mContext, ArrayList<SolicitudesModel> mSolicitudesList) {
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
        @SuppressLint("ViewHolder")
        View v = View.inflate(mContext, R.layout.item_solicitudes_oficiales,null);
        TextView tvOficial = v.findViewById(R.id.tvOficial);
        TextView tvCantSolicitudes = v.findViewById(R.id.tvCantSolicitudes);
        TextView tvValorTotal  = v.findViewById(R.id.tvValorTotal);

        tvOficial.setText(mSolicitudesList.get(position).getCodOfi()+ " - " + mSolicitudesList.get(position).getNomOfi());
        tvCantSolicitudes.setText("Cantidad Solicitudes: "+ String.valueOf(mSolicitudesList.get(position).getCantSolicitud()));
        tvValorTotal.setText("Monto Solicitado: "+ precision.format(Double.parseDouble(mSolicitudesList.get(position).getMonto())));

        v.setTag(mSolicitudesList.get(position).getCodCli());
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
                ArrayList<SolicitudesModel> filters = new ArrayList<>();
                for (int i = 0;i<filterList.size();i++){
                    if(filterList.get(i).getNomOfi().toUpperCase().contains(constraint)
                            ||filterList.get(i).getCodOfi().contains(constraint)){
                            SolicitudesModel e = new SolicitudesModel(
                                        filterList.get(i).getCodOfi(),
                                        filterList.get(i).getNomOfi(),
                                        filterList.get(i).getCantSolicitud(),
                                        filterList.get(i).getMonto(),
                                    filterList.get(i).getEstado()
                            );
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
            mSolicitudesList =(ArrayList<SolicitudesModel>)results.values;
            notifyDataSetChanged();
        }
    }

    public void filtrar(ArrayList<SolicitudesModel> filtroUsuarios) {
        this.mSolicitudesList = filtroUsuarios;
        notifyDataSetChanged();
    }
}
