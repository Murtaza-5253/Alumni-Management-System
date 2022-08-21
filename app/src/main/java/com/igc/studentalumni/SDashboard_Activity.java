package com.igc.studentalumni;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.Toast;

import com.ss.bottomnavigation.BottomNavigation;
import com.ss.bottomnavigation.events.OnSelectedItemChangeListener;

public class SDashboard_Activity extends AppCompatActivity {

    private boolean doublebackpressedonce = false;
    BottomNavigation bottomNavigation;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sactivity_main);
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
                        fragmentTransaction.replace(R.id.frmNavigate,new SAlumni_Fragment());
                        break;
                    case R.id.tbPlacements:
                        fragmentTransaction=getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frmNavigate,new SPlacements_Fragment());
                        break;
                    case R.id.tbNotices:
                        fragmentTransaction=getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frmNavigate,new SNotices_Fragment());
                        break;
                    case R.id.tbEvent:
                        fragmentTransaction=getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frmNavigate,new S_Events_Activity());
                        break;
                    case R.id.tbProfile:
                        fragmentTransaction=getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frmNavigate,new SProfile_Fragment());
                        break;
                }
                fragmentTransaction.commit();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        this.doublebackpressedonce = false;
    }

    @Override
    public void onBackPressed() {
        if (doublebackpressedonce)
        {
            super.onBackPressed();
            return;
        }
        this.doublebackpressedonce = true;
        Toast.makeText(this, "tap back twice to exit", Toast.LENGTH_SHORT).show();
    }
}