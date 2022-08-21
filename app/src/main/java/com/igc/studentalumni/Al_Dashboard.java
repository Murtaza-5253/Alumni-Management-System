package com.igc.studentalumni;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ss.bottomnavigation.BottomNavigation;
import com.ss.bottomnavigation.events.OnSelectedItemChangeListener;

public class Al_Dashboard extends AppCompatActivity {
    BottomNavigation bottomNavigation;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.al_dashboard);

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
                        fragmentTransaction.replace(R.id.frmNavigate,new Al_Alumni_Fragment());
                        break;
                    case R.id.tbPlacements:
                        fragmentTransaction=getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frmNavigate,new Al_Placements_Fragment());
                        break;
                    case R.id.tbNotices:
                        fragmentTransaction=getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frmNavigate,new Al_Notices_Fragment());
                        break;
                }
                fragmentTransaction.commit();
            }
        });
    }
}