package com.example.credimovil;
import android.annotation.SuppressLint;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.credimovil.modelo.SaldosModel;

import java.util.ArrayList;
import java.util.List;

 class AdaptadorSaldos
         extends RecyclerView.Adapter<AdaptadorSaldos.SaldosViewHolder>
         implements View.OnClickListener{


    Context context;
    List<SaldosModel> listaSaldos;
    View.OnClickListener listener;

    public AdaptadorSaldos(Context context, List<SaldosModel> listaSaldos) {
        this.context = context;
        this.listaSaldos = listaSaldos;
    }

    @NonNull
    @Override
    public SaldosViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_saldos_list,
                null, false);

        v.setOnClickListener(this);

        return new SaldosViewHolder(v);


    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SaldosViewHolder saldosViewHolder, int i) {
        saldosViewHolder.tvCliente.setText(listaSaldos.get(i).getCodCli()+"-"+listaSaldos.get(i).getNomCli());
        saldosViewHolder.tvCredito.setText(
                "Crédito: "+listaSaldos.get(i).getCodCre()+
                         " | "+listaSaldos.get(i).getConcurrencia()+
                         "-"+listaSaldos.get(i).getNomDia()+
                " | Cuota:"+listaSaldos.get(i).getCuota());
        saldosViewHolder.tvFechas.setText(
                "Desemb. "+listaSaldos.get(i).getFechaIni()+ " | Vence: "+listaSaldos.get(i).getFechaVence());
        saldosViewHolder.tvSaldos.setText(
                "Saldo: "+listaSaldos.get(i).getSaldoTot()+ " | Mora: "+listaSaldos.get(i).getMoraTot()+
                        " | Vencido: "+listaSaldos.get(i).getVencidoTot());
    }

    @Override
    public int getItemCount() {
        return listaSaldos.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }
     @Override
     public void onClick(View v) {
        if(listener!=null){
            listener.onClick(v);
        }

     }

     public static class SaldosViewHolder extends RecyclerView.ViewHolder {

        TextView tvCliente, tvCredito, tvFechas, tvSaldos;

        public SaldosViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCliente = itemView.findViewById(R.id.tvCliente);
            tvCredito = itemView.findViewById(R.id.tvCredito);
            tvFechas  = itemView.findViewById(R.id.tvFechas);
            tvSaldos  = itemView.findViewById(R.id.tvSaldos);
        }
    }

    public void filtrar(ArrayList<SaldosModel> filtroUsuarios) {
        this.listaSaldos = filtroUsuarios;
        notifyDataSetChanged();
    }
}