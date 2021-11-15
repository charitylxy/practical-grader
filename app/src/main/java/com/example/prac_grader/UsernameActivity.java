package com.example.prac_grader;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.prac_grader.classes.Account;
import com.example.prac_grader.sql_database.AccountDBModel;

import java.util.List;

public class UsernameActivity extends AppCompatActivity {

    private EditText txtUsername;
    private Button  btnNext;
    private boolean registered ;
    private String username;

    SharedPreferences prefs = null;

    AccountDBModel accountDBModel;
    Account userAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_username);

        //load database
        accountDBModel = new AccountDBModel();
        accountDBModel.load(getApplicationContext());

        registered = true;
        //check first time install
        prefs = getSharedPreferences("com.example.prac_grader", MODE_PRIVATE);

        firstTimeInstall();

        //intialised edittext and button
        txtUsername = (EditText) findViewById(R.id.username);
        btnNext = (Button) findViewById(R.id.btnLoginUsername);

        //next button
        btnNext.setOnClickListener(view ->  {
            username = txtUsername.getText().toString();

            if (TextUtils.isEmpty(username)){
                txtUsername.setError("Username cannot be empty.");
            }
            else {
                // check if username is unique if it is registering
                // check username is in database if is signing in
                if ((checkUnique(username) && registered == false)
                        || (!checkUnique(username) && registered == true)) {
                    Intent intent = new Intent(UsernameActivity.this, PinActivity.class);
                    intent.putExtra("registered", registered);
                    if (registered) {
                        intent.putExtra("accountLogIn", userAccount);
                    }
                    else {
                        intent.putExtra ("username", username);
                    }
                    startActivity(intent);
                }
                else if (checkUnique(username) &&registered == true){
                    txtUsername.setError("Username not found");
                }

                else {
                    txtUsername.setError("Username is not unique.");
                }
            }

        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        if (prefs.getBoolean("firstrun", true)) {
            firstTimeInstall();
            prefs.edit().putBoolean("firstrun", false).commit();
        }
    }

    public void firstTimeInstall (){
        AlertDialog.Builder dialog = new AlertDialog.Builder(UsernameActivity.this);
        dialog.setTitle(R.string.dialog_title);
        dialog.setMessage(R.string.dialog_message);
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                registered= true;
                dialog.dismiss();
            }
        });

        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                registered= false;
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public boolean checkUnique (String username){
        Boolean checkUnique = true;
        for (Account currentAccount : accountDBModel.getAllAccount()){
            if ((currentAccount.getUsername()).equals(username)){
                checkUnique = false;
                userAccount = currentAccount;
            }
        }
        return checkUnique;
    }
}