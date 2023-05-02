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





        holder.detay.setText(detay);
        holder.kullaniciAdi.setText(kullaniciAdi);


    }

    @Override
    public int getItemCount() {
        return gonderi.size();
    }

    class tutucu extends RecyclerView.ViewHolder{

        TextView detay;
        TextView kullaniciAdi;

        public tutucu(View itemView) {
            super(itemView);

            detay = itemView.findViewById(R.id.akisGonderiDetay);
            kullaniciAdi=itemView.findViewById(R.id.akisGonderikullaniciAd);





        }
    }

}
