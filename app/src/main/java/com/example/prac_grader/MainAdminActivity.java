package com.example.prac_grader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.prac_grader.fragments.accounts.FragmentInstructor;
import com.example.prac_grader.fragments.accounts.FragmentStudent;
import com.example.prac_grader.classes.Account;
import com.example.prac_grader.fragments.marking.FragmentMarking;
import com.example.prac_grader.fragments.practicals.FragmentPractical;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainAdminActivity extends AppCompatActivity {
    TextView menuTitle;
    ImageButton btnSignOut;

    Account userAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);

        Intent intent = getIntent();
        userAccount = (Account) intent.getSerializableExtra("accountLogIn");

        menuTitle = (TextView) findViewById(R.id.txtTitle);
        btnSignOut = (ImageButton) findViewById(R.id.btnSignOut);
        btnSignOut.setOnClickListener(view -> {
            Intent returnIntent;
            returnIntent = new Intent(MainAdminActivity.this, UsernameActivity.class);
            startActivity (returnIntent);
        });

        goToFragment(new FragmentStudent());
        menuTitle.setText(getString(R.string.student_menu));

        BottomNavigationView bottomNav = findViewById(R.id.adminBottomNavViewBar);

        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.ic_student:
                        selectedFragment= new FragmentStudent();
                        menuTitle.setText(getString(R.string.student_menu));
                        break;

                    case R.id.ic_instructor:
                        selectedFragment = new FragmentInstructor();
                        menuTitle.setText(getString(R.string.instructor_menu));
                        break;

                    case R.id.ic_practical:
                        selectedFragment = new FragmentPractical();
                        menuTitle.setText(getString(R.string.practical_menu));
                        break;

                    case R.id.ic_marking:
                        selectedFragment = new FragmentMarking();
                        menuTitle.setText(getString(R.string.mark_menu));
                        break;
                }
                goToFragment(selectedFragment);
                return true;
            }
        });
    }


    public void goToFragment(Fragment fragment){
        Bundle bundle = new Bundle();
        bundle.putSerializable("userAccount", userAccount);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }

    @Override
    public void onBackPressed() {


    }

}