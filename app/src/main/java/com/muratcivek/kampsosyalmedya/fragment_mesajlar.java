package com.muratcivek.kampsosyalmedya;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.muratcivek.kampsosyalmedya.models.akisYorumModel;
import com.muratcivek.kampsosyalmedya.models.bildirimModel;
import com.muratcivek.kampsosyalmedya.models.mesajlarModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class fragment_mesajlar extends Fragment {
//genel bir mesajlaşma sohbet alanı oluşturma

Button btnMesajGonder;
EditText edtMesaj;
    FirebaseUser kullanici;
    Map<String, Object> mesajlarList;
    FirebaseFirestore db;
    public List<mesajlarModel> mesajlarListe;
    public mesajlarAdapter adapter;
    private RecyclerView recyclerView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mesajlar, container, false);
        kullanici = FirebaseAuth.getInstance().getCurrentUser();
        btnMesajGonder= view.findViewById(R.id.mesajGonder);
        edtMesaj = view.findViewById(R.id.mesaj);
        mesajlarList = new HashMap<>();
        db = FirebaseFirestore.getInstance();
        mesajlarListe = new ArrayList<>();
        recyclerView = view.findViewById(R.id.mesajlarRecyclerView);




        db.collection("genelSohbet").orderBy("tarih", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot value, FirebaseFirestoreException error) {
                        mesajlarListe.clear();
                        for (QueryDocumentSnapshot doc : value) {
                            String mesaj=doc.get("mesaj").toString();
                            String tarih = doc.get("tarih").toString();
                            String email = doc.get("mail").toString() ;
                            mesajlarModel mesajlarModel = new mesajlarModel(email,mesaj);
                            mesajlarListe.add(mesajlarModel);


                        }
                        adapter = new mesajlarAdapter(mesajlarListe);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(adapter);
                    }
                });


        btnMesajGonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mesajDetay = edtMesaj.getText().toString();
                Date now = new Date();
              long tarih=  now.getTime();
              String email;
                    email = kullanici.getEmail();
                    mesajlarList.put("mail",email);
                    mesajlarList.put("mesaj",mesajDetay);
                    mesajlarList.put("tarih",tarih);
                    db.collection("genelSohbet").document().set(mesajlarList).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                           Toast.makeText(getContext(),"Mesajınız gönderildi.",Toast.LENGTH_SHORT).show();
                        }
                    });
                }


        });


        return view;

    }




}