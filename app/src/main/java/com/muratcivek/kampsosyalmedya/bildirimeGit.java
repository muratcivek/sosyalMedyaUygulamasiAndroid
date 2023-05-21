package com.muratcivek.kampsosyalmedya;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class bildirimeGit extends AppCompatActivity {
String gelenMail, gelenKullaniciAd, gelenDetay,gelenOlumlu,gelenYorum;
TextView kullaniciAdi,detay,olumlu,yorum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bildirime_git);

        Intent intent = getIntent();
        gelenMail = intent.getStringExtra("mail");
        gelenKullaniciAd = intent.getStringExtra("kullaniciAd");
        gelenDetay = intent.getStringExtra("detay");
        gelenOlumlu = intent.getStringExtra("olumlu");
        gelenYorum = intent.getStringExtra("yorum");

        kullaniciAdi = findViewById(R.id.bildirimeGitKullaniciAd);
        detay = findViewById(R.id.bildirimeGitDetay);
        olumlu = findViewById(R.id.bildirimeGitOlumlu);
        yorum = findViewById(R.id.bildirimeGitYorum);

        kullaniciAdi.setText(gelenKullaniciAd);
        detay.setText(gelenDetay);
        olumlu.setText(gelenOlumlu + " Olumlu   ");
        yorum.setText("   "+gelenYorum + " Yorum");


        yorum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(bildirimeGit.this, yorumlar_liste.class);
                intent.putExtra("kullaniciAdi",gelenKullaniciAd);
                intent.putExtra("detay",gelenDetay);
                startActivity(intent);
            }
        });

    }
}