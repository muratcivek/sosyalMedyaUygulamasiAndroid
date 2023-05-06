package com.muratcivek.kampsosyalmedya;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.muratcivek.kampsosyalmedya.models.akisYorumModel;
import com.muratcivek.kampsosyalmedya.models.bildirimModel;
import com.muratcivek.kampsosyalmedya.models.kullaniciAraListeleModel;

import org.w3c.dom.Text;

import java.util.List;

public class bildirimAdapter  extends RecyclerView.Adapter<bildirimAdapter.bildirimTutucu>{

    List<bildirimModel> bildirimListe;
    Context context;
    private  onItemClickListener listener;

    public bildirimAdapter(List<bildirimModel> bildirimListe) {
        this.bildirimListe = bildirimListe;
    }

    @Override
    public bildirimTutucu onCreateViewHolder(ViewGroup parent, int viewType) {
        return new bildirimAdapter.bildirimTutucu(LayoutInflater
                .from(parent.getContext()).inflate(R.layout.rw_bildirimler,parent,false));
    }

    @Override
    public void onBindViewHolder(bildirimTutucu holder, int position) {
        String mail = bildirimListe.get(position).getMail().toString();
        String detay = bildirimListe.get(position).getDetay().toString();
        String bildirim = bildirimListe.get(position).getBildirim().toString();

        holder.bir.setText(mail);
        holder.iki.setText(detay);
        holder.uc.setText(bildirim);
    }

    @Override
    public int getItemCount() {
        return bildirimListe.size();
    }

    class bildirimTutucu extends RecyclerView.ViewHolder{
        TextView bir,iki,uc;
        public bildirimTutucu(View itemView) {
            super(itemView);
            bir=itemView.findViewById(R.id.bildirimMail);
            iki=itemView.findViewById(R.id.bildirimDetay);
            uc=itemView.findViewById(R.id.bildirim);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener!=null && position!=RecyclerView.NO_POSITION){
                        listener.onItemClick(bildirimListe.get(position),position); } }});
        }
    }

    public interface onItemClickListener{
        void onItemClick(bildirimModel bildirimModel, int position);
    }

    public void setonItemClickListener(onItemClickListener listener){
        this.listener = listener;
    }
}
