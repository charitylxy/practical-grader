package com.example.prac_grader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.prac_grader.fragments.accounts.FragmentAccountView;
import com.example.prac_grader.classes.Account;

public class MainStudentActivity extends AppCompatActivity {

    Account userAccount;
    TextView menuTitle;
    ImageButton btnSignOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_student);

        Intent intent = getIntent();
        userAccount = (Account) intent.getSerializableExtra("accountLogIn");

        menuTitle = (TextView) findViewById(R.id.txtTitle);
        btnSignOut = (ImageButton) findViewById(R.id.btnSignOut);
        btnSignOut.setOnClickListener(view -> {
            Intent returnIntent;
            returnIntent = new Intent(MainStudentActivity.this, UsernameActivity.class);
            startActivity (returnIntent);
        });

        viewStudent();

    }

    public void viewStudent(){
        Bundle bundle = new Bundle();

        bundle.putSerializable("selectedAccount",userAccount);
        bundle.putSerializable("userAccount", userAccount);
        Fragment fragmentViewAccount = new FragmentAccountView();
        fragmentViewAccount.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragmentViewAccount)
                .commit();
        menuTitle.setText (R.string.student_menu);
    }


    @Override
    public void onBackPressed() {
        viewStudent();
    }
}