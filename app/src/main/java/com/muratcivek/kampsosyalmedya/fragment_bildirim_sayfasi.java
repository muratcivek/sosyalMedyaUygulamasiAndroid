package com.muratcivek.kampsosyalmedya;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
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
import com.muratcivek.kampsosyalmedya.models.akisYorumModel;
import com.muratcivek.kampsosyalmedya.models.bildirimModel;

import java.util.ArrayList;
import java.util.List;

public class fragment_bildirim_sayfasi extends Fragment {
String email,gonderiMail,gonderiDetay,gonderiBildirim,kullaniciAdi,yorumSayi,olumluSayi;
FirebaseUser kullanici;
FirebaseFirestore db,dbb;
FirebaseStorage storage ;
StorageReference storageRef ;
DocumentReference docRef;
public List<bildirimModel> bildirimler;
public bildirimAdapter adapter;
private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_bildirim_sayfasi, container, false);
        bildirimler = new ArrayList<>();
        recyclerView = view.findViewById(R.id.bildirimRecyclerView);
        db = FirebaseFirestore.getInstance();
        dbb = FirebaseFirestore.getInstance();

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        kullanici =FirebaseAuth.getInstance().getCurrentUser();

        if (kullanici != null) {
            email = kullanici.getEmail();
        }



        bildirimleriSirala();


        return view;
    }

    private void bildirimleriSirala() {
        db.collection("KullaniciKayitBilgi").document(email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(Task<DocumentSnapshot> task) {
                kullaniciAdi= task.getResult().get("Kullanici adi").toString();
            }
        });
        db.collection("bildirimler").document("yorum bildirimleri").collection(email)
                .orderBy("Tarih", Query.Direction.DESCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {

                for (QueryDocumentSnapshot document : task.getResult()) {
                    gonderiBildirim=document.get("Bildirim").toString();
                    gonderiDetay = document.get("gonderiDetay").toString();
                    gonderiMail = document.get("email").toString() ;
                if(!gonderiMail.matches(email)){
                    //Kullanıcı kendi gönderisine yorum attıysa o yorum bildirim olarak gelmeyecek.
                    bildirimModel bildirimModel = new bildirimModel(gonderiMail,gonderiDetay,gonderiBildirim);
                    bildirimler.add(bildirimModel);
                }

                }
                adapter = new bildirimAdapter(bildirimler);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(adapter);
                
                adapter.setonItemClickListener(new bildirimAdapter.onItemClickListener() {
                    @Override
                    public void onItemClick(bildirimModel bildirimModel, int position) {
                        dbb.collection("KullaniciGonderiBilgi").document("gonderi")
                                .collection(email).whereEqualTo("Detay",bildirimModel.getDetay()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(Task<QuerySnapshot> task) {
                                for (QueryDocumentSnapshot document : task.getResult()){
                                    olumluSayi=document.get("Olumlu").toString();
                                    yorumSayi=document.get("Yorum").toString();

                                    Intent intent = new Intent(getContext(), bildirimeGit.class);
                                    intent.putExtra("mail",email);
                                    intent.putExtra("kullaniciAd",kullaniciAdi);
                                    intent.putExtra("detay",bildirimModel.getDetay());
                                    intent.putExtra("olumlu",olumluSayi);
                                    intent.putExtra("yorum",yorumSayi);
                                    startActivity(intent);

                                }
                            }
                        });
                    }
                });
            }
        });
    }
}