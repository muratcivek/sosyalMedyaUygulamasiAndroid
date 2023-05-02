package com.muratcivek.kampsosyalmedya;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
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
import com.google.firebase.storage.StorageReference;
import com.muratcivek.kampsosyalmedya.models.akisGonderiModel;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class karsi_profil_sayfasi extends AppCompatActivity {
String gelenMail,kullaniciAdi,detay,olumluSayi,yorumSayi,olumluMail,belgeIsim,kullanicii;
FirebaseFirestore db;
FirebaseStorage storage ;
StorageReference storageRef ;
DocumentReference docRef;
    FirebaseUser kullanici = FirebaseAuth.getInstance().getCurrentUser();

TextView profilKullaniciAdi;
Uri profilUri;
CircleImageView ProfilFoto;
int guncelOlumlu;
public List<akisGonderiModel> gonderiler;
    public akisGonderiAdapter akisGonderiAdapter;
Context context;
private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_karsi_profil_sayfasi);

        Intent intent = getIntent();
        gelenMail = intent.getStringExtra("mail");

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        recyclerView = findViewById(R.id.gonderilerRecyclerView);
        gonderiler = new ArrayList<>();


        profilKullaniciAdi = findViewById(R.id.kullaniciAd);
        ProfilFoto=findViewById(R.id.profilSayfaProfilFoto);

        docRef = db.collection("KullaniciKayitBilgi").document(gelenMail);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                kullanicii = documentSnapshot.getData().get("Kullanici adi").toString();


            }
        });

        profilBilgileriniAl();
        recyclerViewBilgileriniAl();
        
      

}
    private void profilBilgileriniAl() {
        docRef = db.collection("KullaniciKayitBilgi").document(gelenMail);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                kullaniciAdi= documentSnapshot.getData().get("Kullanici adi").toString();
                profilKullaniciAdi.setText(kullaniciAdi);
                storageRef.child("profilFoto/").child(gelenMail)
                        .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(ProfilFoto);

                    }
                });

            }});
    }
    private void recyclerViewBilgileriniAl() {
        db.collection("KullaniciGonderiBilgi").document("gonderi")
                .collection(gelenMail).orderBy("Tarih", Query.Direction.DESCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    detay = document.get("Detay").toString();
                    kullaniciAdi = "-";
                    olumluSayi =document.get("Olumlu").toString() ;
                    yorumSayi=document.get("Yorum").toString();
                    akisGonderiModel akisGonderiModel = new akisGonderiModel(detay, kullaniciAdi,olumluSayi,yorumSayi);
                    gonderiler.add(akisGonderiModel);
                }

                akisGonderiAdapter = new akisGonderiAdapter(gonderiler);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(akisGonderiAdapter);
                
                akisGonderiAdapter.setonItemClickListener(new akisGonderiAdapter.onItemClickListener() {
                    @Override
                    public void onItemClick(akisGonderiModel akisGonderiModel, int position) {
                        
                    }

                    @Override
                    public void onOlumluItemClick(akisGonderiModel akisGonderiModel, int position, TextView textView) {
                        db.collection("gonderiler")
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
                                });                    }

                    @Override
                    public void onYorumItemClick(akisGonderiModel akisGonderiModel, int position, TextView textView) {

                        Intent intent = new Intent(karsi_profil_sayfasi.this, yorumlar_liste.class);
                        intent.putExtra("kullaniciAdi",kullanicii);
                        intent.putExtra("detay",akisGonderiModel.detay);
                        startActivity(intent);
                    }
                });
                
                
            }
        });
    }




}