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

public class SolicitudesListAdapter extends BaseAdapter implements Filterable {

    private Context mContext;
    private ArrayList<SolicitudesModel> mSolicitudesList;
    CustomFilter filter;
    private ArrayList<SolicitudesModel> filterList;
    DecimalFormat precision = new DecimalFormat("#,##0.00");

    public SolicitudesListAdapter(Context mContext, ArrayList<SolicitudesModel> mSolicitudesList) {
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


    //codCli, nomCli, codCre, saldoTot, moraTot,  vencidoTot, fechaVence,  concurrencia, valCuota;
    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        @SuppressLint("ViewHolder") View v = View.inflate(mContext, R.layout.solicitudes_layout,null);
        TextView tvCodCli = v.findViewById(R.id.tvCodCli);
        TextView tvNomCli = v.findViewById(R.id.tvNomCli);
        TextView tvFecha  = v.findViewById(R.id.tvFecha);
        TextView tvMonto = v.findViewById(R.id.tvMonto);
        TextView tvEstado = v.findViewById(R.id.tvEstado);
        String aprobado, aplicado;
        if(mSolicitudesList.get(position).getAprobado().equals("N")){
            aprobado = "Sin aprobar";

        }else{
            aprobado = "Aprobado";
        }
        if(mSolicitudesList.get(position).getAplicado().equals("N")){
            aplicado = "Sin aplicar";

        }else{
            aplicado = "Aplicado";
        }


        tvCodCli.setText("Solicitud Nº:"+mSolicitudesList.get(position).getIdSolicitud());
        tvNomCli.setText(mSolicitudesList.get(position).getCodCli()+"-"+mSolicitudesList.get(position).getNomCli());
        tvFecha.setText("Fecha solicitud: "+mSolicitudesList.get(position).getFecha());
        tvMonto.setText("Monto Solicitado: "+ precision.format(Double.parseDouble(mSolicitudesList.get(position).getMonto())));
        tvEstado.setText("Estado: "+ aprobado+
                " | "+ aplicado);

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
                    if(filterList.get(i).getNomCli().toUpperCase().contains(constraint)
                            ||filterList.get(i).getCodCli().contains(constraint)
                            ||filterList.get(i).getNomCli().contains(constraint)){
                            SolicitudesModel e = new SolicitudesModel(
                                        filterList.get(i).getCodCli(),
                                        filterList.get(i).getNomCli(),
                                        filterList.get(i).getMonto(),
                                        filterList.get(i).getAprobado(),
                                        filterList.get(i).getAplicado());
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
