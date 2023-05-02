package com.muratcivek.kampsosyalmedya;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.muratcivek.kampsosyalmedya.models.kullaniciAraListeleModel;

import java.util.List;

public class kullaniciAraListeleAdapter  extends RecyclerView.Adapter<kullaniciAraListeleAdapter.tutucu>{
     List<kullaniciAraListeleModel> kullaniciListe;
   private  onItemClickListener listener;
    Context context;

    public kullaniciAraListeleAdapter(List<kullaniciAraListeleModel> kullaniciListe) {
        this.kullaniciListe = kullaniciListe;
    }

    @Override
    public tutucu onCreateViewHolder(ViewGroup parent, int viewType) {
        return new kullaniciAraListeleAdapter.tutucu(LayoutInflater.from(parent.getContext()).inflate(R.layout.rw_kullaniciyi_ara,parent,false));

    }

    @Override
    public void onBindViewHolder(tutucu holder, int position) {
        String gelenMail= kullaniciListe.get(position).getMail().toString();
        holder.mail.setText(gelenMail);

    }

    @Override
    public int getItemCount() {
        return kullaniciListe.size();
    }

      class tutucu extends  RecyclerView.ViewHolder{
        TextView mail;
        public tutucu(View itemView) {
            super(itemView);
            mail  = itemView.findViewById(R.id.kullaniciaraMail);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener!=null && position!=RecyclerView.NO_POSITION){
                        listener.onItemClick(kullaniciListe.get(position),position); } }});
  }
}
    public interface onItemClickListener{
        void onItemClick(kullaniciAraListeleModel kullaniciAraListeleModel, int position);
    }

    public void setonItemClickListener(onItemClickListener listener){
        this.listener = listener;
    }
}
