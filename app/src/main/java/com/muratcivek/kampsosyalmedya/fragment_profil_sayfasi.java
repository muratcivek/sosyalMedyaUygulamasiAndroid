package com.muratcivek.kampsosyalmedya;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.muratcivek.kampsosyalmedya.models.ProfilGonderiModel;
import com.muratcivek.kampsosyalmedya.models.akisGonderiModel;
import com.squareup.picasso.Picasso;


import org.checkerframework.checker.nullness.qual.NonNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class fragment_profil_sayfasi extends Fragment {
    List<ProfilGonderiModel> gonderi;
    private  profilGonderiAdapter profilGonderiAdapter;
    private  RecyclerView recyclerView;
    FirebaseFirestore db;
    FirebaseStorage storage ;
    StorageReference storageRef ;
    DocumentReference docRef;
    String email;
    Uri gonderilecekUriProfil;
    FirebaseUser kullanici;
    String baslik,detay,konum,kullaniciAdi;
    TextView profilKullaniciAdi;
    CircleImageView ProfilBuyukFoto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_profil_sayfasi, container, false);
        recyclerView = view.findViewById(R.id.recyclerVieww);
        kullanici = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        gonderi = new ArrayList<>();
        profilKullaniciAdi = view.findViewById(R.id.ProfilKullaniciAdi);


        if (kullanici != null) {
            email = kullanici.getEmail();
        }
        ProfilBuyukFoto = view.findViewById(R.id.profilSayfaProfilFoto);

        docRef = db.collection("KullaniciKayitBilgi").document(email);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                int gonderiSayisi= Integer.valueOf(documentSnapshot.getData().get("GonderiSayisi").toString());
                kullaniciAdi= documentSnapshot.getData().get("Kullanici adi").toString();
                profilKullaniciAdi.setText(kullaniciAdi);
                storageRef.child("profilFoto/").child(email)
                        .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        gonderilecekUriProfil = uri;
                        Picasso.get().load(uri).into(ProfilBuyukFoto);


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });




                   db.collection("KullaniciGonderiBilgi").document("gonderi")
                            .collection(email).orderBy("Tarih", Query.Direction.DESCENDING).get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(Task<QuerySnapshot> task) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        detay = document.get("Detay").toString();


                                        ProfilGonderiModel profilGonderiModel= new ProfilGonderiModel(detay, kullaniciAdi);
                                        gonderi.add(profilGonderiModel);
                                    }

                                    profilGonderiAdapter= new profilGonderiAdapter(gonderi);
                                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                                    recyclerView.setLayoutManager(linearLayoutManager);
                                    recyclerView.setAdapter(profilGonderiAdapter);
                                }
                            });

                }



        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getContext(), "Teknik bir sorun oldu, gonderileriniz yüklenemiyor."
                        ,Toast.LENGTH_SHORT).show();
            }
        });




        return view;

    }




}
