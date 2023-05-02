package com.muratcivek.kampsosyalmedya;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import org.checkerframework.checker.nullness.qual.NonNull;

public class giris_sayfasi extends AppCompatActivity {

    EditText etSifre;
    EditText etMail;
    ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris_sayfasi);
        etSifre = findViewById(R.id.etSifre);
        etMail = findViewById(R.id.etMail);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Giriş yapılıyor, lütfen bekleyiniz...");



    }
    public void btnGirisYap (View view){
        progressDialog.show();
        String mail = etMail.getText().toString();
        String sifre = etSifre.getText().toString();

        mAuth.signInWithEmailAndPassword(mail, sifre)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent ikincisayfagecis=new Intent(giris_sayfasi.this, ana_sayfa.class);
                            startActivity(ikincisayfagecis);
                            progressDialog.dismiss();


                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            Toast.makeText(giris_sayfasi.this, "Bilgilerinizi yanlış girdiniz",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



    public void KayitOlGit (View view){

        Intent ikincisayfagecis=new Intent(giris_sayfasi.this, kayit_ol_sayfa.class);
        startActivity(ikincisayfagecis);
}

}
