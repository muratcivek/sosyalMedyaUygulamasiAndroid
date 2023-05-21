package com.muratcivek.kampsosyalmedya;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.muratcivek.kampsosyalmedya.models.ProfilGonderiModel;
import com.muratcivek.kampsosyalmedya.models.akisGonderiModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class profilGonderiAdapter extends RecyclerView.Adapter<profilGonderiAdapter.tutucu> {

    List<ProfilGonderiModel> gonderi;
    Context context;
    private onItemClickListener listener;

    public profilGonderiAdapter(List<ProfilGonderiModel> gonderi) {
        this.gonderi = gonderi;
    }

    @Override
    public tutucu onCreateViewHolder(ViewGroup parent, int viewType) {
        return new tutucu(LayoutInflater.from(parent.getContext()).inflate(R.layout.rw_profilgonderiler,parent,false));
    }

    @Override
    public void onBindViewHolder(tutucu holder, int position) {



        String detay = gonderi.get(position).getDetay().toString();
        String kullaniciAdi = gonderi.get(position).getKullaniciAdi().toString();
        String olumlu = gonderi.get(position).getOlumlu().toString();
        String yorum = gonderi.get(position).getYorum().toString();



        holder.detay.setText(detay);
        holder.kullaniciAdi.setText(kullaniciAdi);
        holder.olumlu.setText(olumlu + " Olumlu   ");
        holder.yorum.setText(yorum+ " Yorum");


    }

    @Override
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

            detay = itemView.findViewById(R.id.akisGonderiDetay);
            kullaniciAdi=itemView.findViewById(R.id.akisGonderikullaniciAd);
            olumlu = itemView.findViewById(R.id.profilGonderiOlumlu);
            yorum = itemView.findViewById(R.id.profilGonderiYorum);

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





        }
    }

    public interface onItemClickListener{
        void onOlumluItemClick(ProfilGonderiModel profilGonderiModel, int position, TextView textView);
        void onYorumItemClick (ProfilGonderiModel profilGonderiModel, int position, TextView textView);

    }
    public void  setonOlumluClickListener(profilGonderiAdapter.onItemClickListener listener){
      this.listener=listener;

    }
    public void  setonYorumItemClickListener(profilGonderiAdapter.onItemClickListener listener){
        this.listener = listener;

    }
}
