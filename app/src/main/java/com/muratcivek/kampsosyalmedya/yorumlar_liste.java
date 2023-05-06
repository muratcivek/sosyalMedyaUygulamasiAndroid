package com.muratcivek.kampsosyalmedya;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
import com.google.firebase.storage.StorageReference;
import com.muratcivek.kampsosyalmedya.models.akisGonderiModel;
import com.muratcivek.kampsosyalmedya.models.akisYorumModel;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicMarkableReference;

public class yorumlar_liste extends AppCompatActivity {
String gelenKullaniciAdi, gelenDetay, yorumYazisi,email, gonderiSahibiMail,eskiYorumSayi,yeniYorumSayi,tekliBelgeisim,cokluBelgeisim;
FirebaseFirestore db;
EditText yorum;
Button yorumYap;
String gonderiyiPaylasanMail,gonderiyiPaylasanKullaniciAd;
    Long tarih;
    Map<String, Object> yorumBilgileri, bildirimler;
    FirebaseUser kullanici;
    DocumentReference docRef;
    String emaill,detay,gelenYorumSayiGuncel;
    Uri urii;
    public List<akisYorumModel> gonderiler;
    private RecyclerView recyclerView;
    public akisYorumAdapter akisYorumAdapter;
    FirebaseStorage storage ;
    StorageReference storageRef ;
    Uri gonderilecekUriProfil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yorumlar_liste);

        Intent intent = getIntent();
        gelenKullaniciAdi = intent.getStringExtra("kullaniciAdi");
        gelenDetay = intent.getStringExtra("detay");


        recyclerView = findViewById(R.id.yorumlarListeRecyclerView);
        gonderiler = new ArrayList<>();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        db = FirebaseFirestore.getInstance();
         yorum = findViewById(R.id.yorum);
         yorumYap = findViewById(R.id.yorumYap);
        kullanici = FirebaseAuth.getInstance().getCurrentUser();
        if (kullanici != null) {
            email = kullanici.getEmail();
        }
        db.collection("gonderiler").whereEqualTo("Detay", gelenDetay)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String yorumsayisi = document.get("Yorum").toString();
                            gonderiyiPaylasanMail = document.get("mail").toString();
                            gonderiyiPaylasanKullaniciAd = document.get("kullanici adi").toString();
                            int yeniyorum = Integer.parseInt(yorumsayisi)+1;
                            gelenYorumSayiGuncel =Integer.toString(yeniyorum);
                        }
                    } else {
                        Log.d(TAG, "Veri çekilemedi: ", task.getException());
                    }
                });
        storageRef.child("profilFoto/").child(email)
                .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                gonderilecekUriProfil = uri;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

         yorumYap.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 yorumYazisi= yorum.getText().toString();
                 Date now = new Date();
                  tarih=  now.getTime();
                 yorumBilgileri = new HashMap<>();
                 bildirimler = new HashMap<>();

                 yorumBilgileri.put("yorumDetay", yorumYazisi);
                 yorumBilgileri.put("gonderiDetay", gelenDetay);
                 yorumBilgileri.put("Tarih", tarih);
                 yorumBilgileri.put("email", email);
                 yorumBilgileri.put("fotoUri", gonderilecekUriProfil);


                 db.collection("yorumlar").document( gelenDetay)
                         .collection("butunYorumlar").document(gelenYorumSayiGuncel)
                         .set(yorumBilgileri).addOnSuccessListener(new OnSuccessListener<Void>() {
                     @Override
                     public void onSuccess(Void unused) {
                        //Bildirim kodları buraya sıralanacak.
                        bildirimler.put("gonderiDetay", gelenDetay);
                        bildirimler.put("Tarih",tarih);
                        bildirimler.put("email",email);
                         bildirimler.put("Yorum",yorumYazisi);
                         bildirimler.put("Bildirim","içerikli gönderine yorum bıraktı.");
                        bildirimler.put("Paylasan Mail", gonderiyiPaylasanMail);
                        bildirimler.put("Paylasan Kullanici Ad", gonderiyiPaylasanKullaniciAd);

                        db.collection("bildirimler").document("yorum bildirimleri")
                                .collection(gonderiyiPaylasanMail).document(yorumYazisi).set(bildirimler)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d(TAG, "onSuccess: " + " tamamlandı.");
                                    }
                                });



                         db.collection("KullaniciKayitBilgi")
                                 .whereEqualTo("Kullanici adi", gelenKullaniciAdi)
                                 .get()
                                 .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                     @Override
                                     public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                         if (task.isSuccessful()) {
                                             for (QueryDocumentSnapshot document : task.getResult()) {
                                               gonderiSahibiMail=  document.getId() ;

                                                 db.collection("KullaniciGonderiBilgi").document("gonderi")
                                                         .collection(gonderiSahibiMail)
                                                         .whereEqualTo("Detay", gelenDetay)
                                                         .get()
                                                         .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {@Override
                                                             public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                 if (task.isSuccessful()) {
                                                                     for (QueryDocumentSnapshot document : task.getResult()) {
                                                                         eskiYorumSayi=  document.getData().get("Yorum").toString() ;
                                                                         String documentIsim = document.getId();
                                                                         int guncelYorumSayi = Integer.parseInt(eskiYorumSayi) + 1;
                                                                         yeniYorumSayi = Integer.toString(guncelYorumSayi);
                                                                         docRef = db.collection("KullaniciGonderiBilgi").document("gonderi")
                                                                                 .collection(gonderiSahibiMail).document( documentIsim);
                                                                         docRef.update("Yorum",yeniYorumSayi);
                                                                         db.collection("gonderiler")
                                                                                 .whereEqualTo("Detay", gelenDetay).get()
                                                                                 .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {@Override
                                                                                     public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                         if (task.isSuccessful()) {
                                                                                             for (QueryDocumentSnapshot document : task.getResult()) {
                                                                                                 String documentIsim = document.getId();
                                                                                                 docRef = db.collection("gonderiler").document(documentIsim);
                                                                                                 docRef.update("Yorum",yeniYorumSayi);
                                                                                                 finish();
                                                                                             }
                                                                                         } else {
                                                                                             Log.d(TAG, "Sunucu kaynaklı bir sorun yaşanıyor! ", task.getException());
                                                                                         }
                                                                                     }
                                                                                 });






                                                                     }
                                                                 } else {
                                                                     Log.d(TAG, "Sunucu kaynaklı bir sorun yaşanıyor! ", task.getException());
                                                                 }
                                                             }
                                                         });
                                             }
                                         } else {
                                             Log.d(TAG, "Sunucu kaynaklı bir sorun yaşanıyor! ", task.getException());
                                         }
                                     }
                                 });



                     }
                 }).addOnFailureListener(new OnFailureListener() {
                     @Override
                     public void onFailure(Exception e) {

                     }
                 });

             }
         });

        db.collection("yorumlar").document(gelenDetay).collection("butunYorumlar")
                .orderBy("Tarih", Query.Direction.DESCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {

                for (QueryDocumentSnapshot document : task.getResult()) {

                    detay = document.get("yorumDetay").toString();
                    emaill = document.get("email").toString();
                    Log.d(TAG, "onComplete: " + detay);

                    akisYorumModel akisYorumModel = new akisYorumModel(detay,emaill);
                    gonderiler.add(akisYorumModel);


                }
                akisYorumAdapter = new akisYorumAdapter(gonderiler);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(yorumlar_liste.this);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(akisYorumAdapter);
            }
        });




    }
}