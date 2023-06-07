package com.muratcivek.kampsosyalmedya;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

public class ana_sayfa extends AppCompatActivity {
    FrameLayout frameLayout;
    MeowBottomNavigation bottomNavigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //aktivitede klavye açılınca ekranın kaymasını engelleme
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);


        setContentView(R.layout.activity_ana_sayfa);
        frameLayout = findViewById(R.id.anaSayfa_frameLayout);
        bottomNavigation = findViewById(R.id.anaSayfa_meowBottom);
        bottomNavigation.add(new MeowBottomNavigation.Model(1,R.drawable.anasayfa));
        bottomNavigation.add(new MeowBottomNavigation.Model(2,R.drawable.arama));
        bottomNavigation.add(new MeowBottomNavigation.Model(3,R.drawable.mesajlar));
        bottomNavigation.add(new MeowBottomNavigation.Model(4,R.drawable.bildirimler));
        bottomNavigation.add(new MeowBottomNavigation.Model(5,R.drawable.kullanici_adi_girisi));

        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                Fragment fragment = null;

                switch (item.getId()){
                    case 1:
                        fragment = new fragment_akis_sayfasi();
                        break;
                    case 2:
                        fragment = new fragment_kullaniciAra_sayfasi();
                        break;
                    case 3:
                        fragment = new fragment_mesajlar() ;
                        break;
                    case 4:
                        fragment = new fragment_bildirim_sayfasi();
                        break;
                    case 5:
                        fragment = new fragment_profil_sayfasi();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.anaSayfa_frameLayout,fragment).commit();
            }
        });

        //bottomNavigation.setCount(3,"5"); //bildirim göstermek için
        //bottomNavigation.setCount(4,"4"); //bildirim göstermek için
        bottomNavigation.show(1,true);

        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                //Toast.makeText(getApplicationContext(),"secilen " + item.getId(),Toast.LENGTH_LONG).show();


            }
        });
        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
                //Toast.makeText(getApplicationContext(),"reselect " + item.getId(),Toast.LENGTH_LONG).show();
                //aynı butona yenıden tıklanırsa
            }
        });

    }
}