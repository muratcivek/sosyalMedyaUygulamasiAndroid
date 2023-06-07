package com.muratcivek.kampsosyalmedya;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.muratcivek.kampsosyalmedya.models.ProfilGonderiModel;
import com.muratcivek.kampsosyalmedya.models.akisGonderiModel;

import java.util.List;

public class akisGonderiAdapter  extends RecyclerView.Adapter<akisGonderiAdapter.tutucu>{
    List<akisGonderiModel> gonderi;
    Context context;
    private onItemClickListener listener;

    public akisGonderiAdapter(List<akisGonderiModel> gonderi) {
        this.gonderi = gonderi;
    }
    public akisGonderiAdapter.tutucu onCreateViewHolder(ViewGroup parent, int viewType) {
        return new akisGonderiAdapter.tutucu(LayoutInflater
                .from(parent.getContext()).inflate(R.layout.rw_gonderiler,parent,false));
    }
    public void onBindViewHolder(akisGonderiAdapter.tutucu holder, int position) {
        String detay = gonderi.get(position).getDetay().toString();
        String kullaniciAdi = gonderi.get(position).getKullaniciAdi().toString();
        String olumluSayi = gonderi.get(position).getOlumluSayi();
        String yorumSayi = gonderi.get(position).getYorumSayi();
        holder.detay.setText(detay);
        holder.kullaniciAdi.setText(kullaniciAdi);
        holder.olumlu.setText(olumluSayi + " Olumlu   ");
        holder.yorum.setText(yorumSayi + " Yorum");
    }
    public int getItemCount() {
        return gonderi.size();
    }

    class tutucu extends RecyclerView.ViewHolder{

        TextView detay;
        TextView kullaniciAdi;
        public TextView olumlu;
        public TextView yorum;
        public tutucu(View itemView) {
            super(itemView);
            detay = itemView.findViewById(R.id.anaGonderiDetay);
            kullaniciAdi=itemView.findViewById(R.id.anaGonderikullaniciAd);
            olumlu = itemView.findViewById(R.id.akisGonderiOlumlu);
            yorum = itemView.findViewById(R.id.akisGonderiYorum);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener!=null && position!=RecyclerView.NO_POSITION){
                        listener.onItemClick(gonderi.get(position),position);
                    }
                }
            });
            olumlu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener!=null && position!=RecyclerView.NO_POSITION){
                        listener.onOlumluItemClick(gonderi.get(position),position,olumlu);
                    }
                }
            });

            yorum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener!=null && position!=RecyclerView.NO_POSITION){
                        listener.onYorumItemClick(gonderi.get(position),position,olumlu);
                    }
                }
            });

        }}
        public interface onItemClickListener{
        void onItemClick(akisGonderiModel akisGonderiModel, int position);
        void onOlumluItemClick(akisGonderiModel akisGonderiModel, int position, TextView textView);
        void onYorumItemClick (akisGonderiModel akisGonderiModel, int position, TextView textView);

        }
        public void setonItemClickListener(onItemClickListener listener){
        this.listener = listener;
        }
        public void  setonOlumluClickListener(onItemClickListener listener){
            this.listener = listener;

        }
    public void  setonYorumItemClickListener(onItemClickListener listener){
        this.listener = listener;

    }

}
