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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.muratcivek.kampsosyalmedya.models.kullaniciAraListeleModel;

import java.util.ArrayList;
import java.util.List;


public class fragment_kullaniciAra_sayfasi extends Fragment {

    String mail;
    FirebaseFirestore db;
    FirebaseUser kullanici;
    DocumentReference docRef;
    public List<kullaniciAraListeleModel> kullaniciListe;
    private RecyclerView recyclerView;
    public kullaniciAraListeleAdapter kullaniciAraListeleAdapter;
    FirebaseStorage storage ;
    StorageReference storageRef ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_kullanici_ara_sayfasi, container, false);
        recyclerView = view.findViewById(R.id.KullaniciaraRecyclerView);
        kullaniciListe = new ArrayList<>();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        db = FirebaseFirestore.getInstance();
        db.collection("KullaniciKayitBilgi").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot document : task.getResult()) {

                    mail = document.getId();
                    kullaniciAraListeleModel kullaniciAraListeleModel = new kullaniciAraListeleModel(mail);
                    kullaniciListe.add(kullaniciAraListeleModel);

                    kullaniciAraListeleAdapter = new kullaniciAraListeleAdapter(kullaniciListe);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setAdapter(kullaniciAraListeleAdapter);
                    kullaniciAraListeleAdapter.setonItemClickListener(new kullaniciAraListeleAdapter.onItemClickListener() {
                        @Override
                        public void onItemClick(kullaniciAraListeleModel kullaniciAraListeleModel, int position) {

                            Intent intent = new Intent(getContext(), karsi_profil_sayfasi.class);
                            intent.putExtra("mail",kullaniciAraListeleModel.getMail());
                            startActivity(intent);
                        }
                    });
                }
            }
        });



        return view;

    }


}