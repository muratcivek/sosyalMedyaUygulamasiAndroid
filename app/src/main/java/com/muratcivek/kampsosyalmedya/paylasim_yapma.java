package com.muratcivek.kampsosyalmedya;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class paylasim_yapma extends AppCompatActivity {
EditText akisGonderiBaslik;
EditText akisGonderiDetay;
EditText akisGonderiKonum;
ImageView akisGonderiFoto;
Uri secilenFoto;
ImageView GonderiFoto;
FirebaseUser kullanici;
String email;
String izin[] = {Manifest.permission.READ_EXTERNAL_STORAGE};
    FirebaseFirestore db;
    FirebaseStorage storage ;
    StorageReference storageRef ;
    DocumentReference docRef;
    int sayi, toplamGonderiSayisi,gonderilecekGonderi;
    Map<String, Object> kullaniciGonderiBilgi;
    String kullaniciAdi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paylasim_yapma);
        GonderiFoto = findViewById(R.id.akisGonderiFoto);
        ;
        akisGonderiDetay = findViewById(R.id.akisGonderiDetay);

       kullanici = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        docRef= db.collection("sistemGenelBilgiler").document("bilgiler");
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                toplamGonderiSayisi =Integer.valueOf(documentSnapshot.getData().get("toplamGonderi").toString()) ;

            }
        });

        if (kullanici != null) {
            email = kullanici.getEmail();
        }

        docRef= db.collection("KullaniciKayitBilgi").document(email);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                kullaniciAdi=documentSnapshot.getData().get("Kullanici adi").toString() ;

            }
        });
    }








    public void paylas (View view ) {

        String detay = akisGonderiDetay.getText().toString();
        //kullanıcının gönderi sayısını alma
        docRef = db.collection("KullaniciKayitBilgi").document(email);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                int kontrol = Integer.valueOf(documentSnapshot.getData().get("GonderiSayisi").toString());
                for(int i=0; i<=kontrol; i++){
                    if(kontrol == i){
                        i = i+1;
                        sayi =i;
                        Date now = new Date();
                       Long tarih=  now.getTime();
                        kullaniciGonderiBilgi = new HashMap<>();

                        kullaniciGonderiBilgi.put("Detay", detay.toString());
                        kullaniciGonderiBilgi.put("Tarih", tarih);
                        kullaniciGonderiBilgi.put("Olumlu", 0);
                        kullaniciGonderiBilgi.put("Olumsuz", 0);
                        kullaniciGonderiBilgi.put("Yorum", 0);



                        db.collection("KullaniciGonderiBilgi").document("gonderi")
                                .collection(email.toString()).document(": " + sayi).set(kullaniciGonderiBilgi)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        kullaniciGonderiBilgi.put("kullanici adi", kullaniciAdi);
                                        kullaniciGonderiBilgi.put("mail", email);
                                        gonderilecekGonderi = toplamGonderiSayisi+1;
                                        db.collection("gonderiler").document(":" + gonderilecekGonderi ).set(kullaniciGonderiBilgi)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        //Gönderi sayısını database üzerinde güncelleme
                                                        docRef = db.collection("sistemGenelBilgiler").document("bilgiler");
                                                        docRef.update("toplamGonderi",toplamGonderiSayisi+1);
                                                        finish(); }}); }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(Exception e) {
                                Toast.makeText(paylasim_yapma.this, "Sistemsel bir sorundan kaynaklı şuanda paylaşım yapamıyorsunuz"
                                        ,Toast.LENGTH_SHORT).show();
                            }
                        });
                        //Gönderi sayısını database üzerinde güncelleme
                        docRef = db.collection("KullaniciKayitBilgi").document(email);
                        docRef.update("GonderiSayisi",i);

                    }



                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Toast.makeText(paylasim_yapma.this, "Sistemsel bir sorundan kaynaklı şuanda paylaşım yapamıyorsunuz"
                        ,Toast.LENGTH_SHORT).show();
            }
        });


    }


}