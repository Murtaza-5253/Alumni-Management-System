package com.igc.studentalumni;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ss.bottomnavigation.BottomNavigation;
import com.ss.bottomnavigation.events.OnSelectedItemChangeListener;

public class Ad_Dashboard_Activity extends AppCompatActivity {
    BottomNavigation bottomNavigation;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_activity_dashboard_);

        bottomNavigation = findViewById(R.id.btmDash);
        frag_nav();

    }
    private void frag_nav(){
        bottomNavigation.setOnSelectedItemChangeListener(new OnSelectedItemChangeListener() {
            @Override
            public void onSelectedItemChanged(int i) {

                switch (i)
                {
                    case R.id.tbAlumni:
                        fragmentTransaction=getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frmNavigate,new Ad_Alumni_Fragment());
                        break;
                    case R.id.tbStudent:
                        fragmentTransaction=getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frmNavigate,new Ad_Stundent_Fragment());
                        break;
                    case R.id.tbPlacements:
                        fragmentTransaction=getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frmNavigate,new Ad_Placements_Fragment());
                        break;
                    case R.id.tbEvent:
                        fragmentTransaction=getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frmNavigate,new Ad_Events_Activity());
                        break;
                    case R.id.tbNotices:
                        fragmentTransaction=getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frmNavigate,new Ad_Notices_Fragment());
                        break;
                }
                fragmentTransaction.commit();
            }
        });
    }
}