package com.muratcivek.kampsosyalmedya;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.muratcivek.kampsosyalmedya.models.bildirimModel;
import com.muratcivek.kampsosyalmedya.models.mesajlarModel;

import java.util.List;

public class mesajlarAdapter extends RecyclerView.Adapter<mesajlarAdapter.mesajTutucu> {
    List<mesajlarModel> mesajlarListe;
    Context context;

    public mesajlarAdapter(List<mesajlarModel> mesajlarListe) {
        this.mesajlarListe = mesajlarListe;
    }



    @Override
    public mesajlarAdapter.mesajTutucu onCreateViewHolder(ViewGroup parent, int viewType) {
            return new mesajlarAdapter.mesajTutucu(LayoutInflater
                    .from(parent.getContext()).inflate(R.layout.rw_mesajlar_icerik_sol, parent, false));

    }

    @SuppressLint("WrongConstant")
    @Override
    public void onBindViewHolder(mesajlarAdapter.mesajTutucu holder, int position) {
        String mail = mesajlarListe.get(position).getEmail().toString();
        String detay = mesajlarListe.get(position).getMesajDetay().toString();

        if(mail.equals(FirebaseAuth.getInstance().getCurrentUser().getEmail().toString()) ){
            holder.email.setText(mail);

            holder.mesajlar.setText(detay);
            holder.mesajlar.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);



        }
        holder.email.setText(mail);
        holder.mesajlar.setText(detay);




    }

    @Override
    public int getItemCount() {
        return mesajlarListe.size();
    }



    public class mesajTutucu extends RecyclerView.ViewHolder {
        TextView email,mesajlar;
        public mesajTutucu(View itemView) {
            super(itemView);
            email = itemView.findViewById(R.id.mesajlarMail);
            mesajlar=itemView.findViewById(R.id.mesajlarDetay);
        }
    }
}
