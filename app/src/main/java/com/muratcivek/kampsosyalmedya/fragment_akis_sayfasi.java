package com.muratcivek.kampsosyalmedya;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.muratcivek.kampsosyalmedya.models.ProfilGonderiModel;
import com.muratcivek.kampsosyalmedya.models.akisGonderiModel;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;


public class fragment_akis_sayfasi extends Fragment {


    Button buton;
    public List<akisGonderiModel> gonderiler;
    private RecyclerView recyclerView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    DocumentReference docRef;
    FirebaseUser kullanici = FirebaseAuth.getInstance().getCurrentUser();
    int toplamGonderiSayisi;
    String detay, kullaniciadi;
    public akisGonderiAdapter akisGonderiAdapter;
    String olumluMail;
    String olumluSayi,yorumSayi;
    String belgeIsim;
    int guncelOlumlu,kontrolOlumlu=1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_ana_sayfa, container, false);
        buton = view.findViewById(R.id.paylasdeneme);
        recyclerView = view.findViewById(R.id.gonderilerRecyclerView);
        gonderiler = new ArrayList<>();


       gonderilerisirala();


        buton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), paylasim_yapma.class);
                startActivity(intent);
            }
        });


        return view;
    }

    private void gonderilerisirala() {


        db.collection("gonderiler").orderBy("Tarih",Query.Direction.DESCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            detay = document.get("Detay").toString();
                            kullaniciadi = document.get("kullanici adi").toString();
                            olumluSayi =document.get("Olumlu").toString() ;
                            yorumSayi=document.get("Yorum").toString();
                            akisGonderiModel akisGonderiModel = new akisGonderiModel(detay, kullaniciadi,olumluSayi,yorumSayi);
                            gonderiler.add(akisGonderiModel);
                        }

                        akisGonderiAdapter = new akisGonderiAdapter(gonderiler);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(akisGonderiAdapter);



                        akisGonderiAdapter.setonItemClickListener(new akisGonderiAdapter.onItemClickListener() {
                            @Override
                            public void onItemClick(akisGonderiModel akisGonderiModel, int position) {

                            }

                            @Override
                            public void onOlumluItemClick(akisGonderiModel akisGonderiModel, int position, TextView textView) {
                                db.collection("gonderiler")
                                        .whereEqualTo("kullanici adi", akisGonderiModel.kullaniciAdi)
                                        .whereEqualTo("Detay", akisGonderiModel.detay)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        olumluMail = document.get("mail").toString();
                                                        belgeIsim = document.getId();
                                                        olumluSayi = document.get("Olumlu").toString();

                                                        DocumentReference washingtonRef = db.collection("gonderiler").document(belgeIsim);

                                                        guncelOlumlu = Integer.parseInt(olumluSayi) + 1;
                                                        washingtonRef
                                                                .update("Olumlu", guncelOlumlu)
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                                                                        db.collection("KullaniciGonderiBilgi").document("gonderi")
                                                                                .collection(olumluMail)
                                                                                .whereEqualTo("Detay", akisGonderiModel.detay)
                                                                                .get()
                                                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                        if (task.isSuccessful()) {
                                                                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                                                                belgeIsim = document.getId().toString();
                                                                                                DocumentReference washingtonRef = db.collection("KullaniciGonderiBilgi")
                                                                                                        .document("gonderi").collection(olumluMail)
                                                                                                        .document(belgeIsim);


                                                                                                washingtonRef
                                                                                                        .update("Olumlu", guncelOlumlu).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                    @Override
                                                                                                    public void onSuccess(Void unused) {
                                                                                                        textView.setTextColor(Color.BLUE);
                                                                                                        textView.setClickable(false);




                                                                                                        Log.d(TAG, "DocumentSnapshot successfully updated!");

                                                                                                    }
                                                                                                });
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                });

                                                                    }
                                                                });

                                                    }
                                                } else {
                                                }
                                            }
                               });
                            }

                            @Override
                            public void onYorumItemClick(akisGonderiModel akisGonderiModel, int position, TextView textView) {
                                Intent intent = new Intent(getContext(), yorumlar_liste.class);
                                intent.putExtra("kullaniciAdi",akisGonderiModel.kullaniciAdi);
                                intent.putExtra("detay",akisGonderiModel.detay);


                                startActivity(intent);

                            }
                        });


                    }
                });

    }

}






