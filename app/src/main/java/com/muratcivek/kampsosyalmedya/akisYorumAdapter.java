package com.muratcivek.kampsosyalmedya;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.muratcivek.kampsosyalmedya.models.akisYorumModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class akisYorumAdapter extends RecyclerView.Adapter<akisYorumAdapter.akisYorumTutucu>{
    List<akisYorumModel> yorumListe;
    Context context;
    public akisYorumAdapter(List<akisYorumModel> yorumListe) {
        this.yorumListe = yorumListe;
    }
    @Override
    public akisYorumTutucu onCreateViewHolder(ViewGroup parent, int viewType) {
        return new akisYorumAdapter.akisYorumTutucu(LayoutInflater
                .from(parent.getContext()).inflate(R.layout.rw_yorumlar_liste,parent,false)); }
    @Override
    public void onBindViewHolder(akisYorumTutucu holder, int position) {
        String detay = yorumListe.get(position).getDetay().toString();
        String kullaniciAdi = yorumListe.get(position).getKullaniciAdi().toString();
        holder.detay.setText(detay);
        holder.kullaniciAdi.setText(kullaniciAdi); }
    @Override
    public int getItemCount() {
        return yorumListe.size();
    }
    class akisYorumTutucu extends RecyclerView.ViewHolder{
        TextView detay;
        TextView kullaniciAdi;
        public akisYorumTutucu(View itemView) {
            super(itemView);
            detay = itemView.findViewById(R.id.akisYorumDetay);
            kullaniciAdi=itemView.findViewById(R.id.akisYorumKullaniciAd);

        }}}
