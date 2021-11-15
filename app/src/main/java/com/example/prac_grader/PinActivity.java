package com.example.prac_grader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prac_grader.classes.Account;
import com.example.prac_grader.sql_database.AccountDBModel;

public class PinActivity extends AppCompatActivity {
    private EditText pin1, pin2, pin3, pin4;
    private TextView instruction;
    private Button next;
    private int pin, checkPin;
    private boolean registered, retype;
    private String username;

    AccountDBModel accountDBModel;
    Account userAccount = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);

        //initialised edit text boxes
        pin1 = (EditText) findViewById(R.id.txtPin1);
        pin2 = (EditText) findViewById(R.id.txtPin2);
        pin3 = (EditText) findViewById(R.id.txtPin3);
        pin4 = (EditText) findViewById(R.id.txtPin4);
        next = (Button) findViewById(R.id.btnLoginPin);
        instruction = (TextView) findViewById(R.id.txtInstruction);

        //load database
        accountDBModel = new AccountDBModel();
        accountDBModel.load(getApplicationContext());

        //get data
        Intent intent = getIntent();
        registered = intent. getBooleanExtra("registered", true);
        if (registered) {
            userAccount = (Account) intent.getSerializableExtra("accountLogIn");
            next.setText(R.string.login);
        }
        else {
            username = intent.getStringExtra("username");
            next.setText(R.string.next);
        }
        retype = true;



        //next / login/ register button
        next.setOnClickListener(view -> {
            try{
                //convert to int
                pin = Integer.parseInt(pin1.getText().toString() +
                        pin2.getText().toString() +
                        pin3.getText().toString() +
                        pin4.getText().toString());

                //log in
                if (registered){
                    //get password
                    if (pin == userAccount.getPin()){ //check password
                        //go to new activity
                        goToAccount();
                        Toast.makeText(PinActivity.this,
                                "Signed in",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(PinActivity.this,
                                "Invalid Pin",Toast.LENGTH_SHORT).show();
                    }
                }

                //register
                else {
                    //first set up pin
                    if (retype == true){
                        next.setText(getString(R.string.login));
                        pin1.setText("");
                        pin2.setText("");
                        pin3.setText("");
                        pin4.setText("");
                        instruction.setText(getString(R.string.reenter_pin));
                        next.setText(R.string.register);
                        checkPin = pin;
                        retype = false;
                    }
                    // retype pin
                    else{
                        if (pin == checkPin){
                            //add account (admin account only have username and pin)
                            userAccount = new Account (username, pin, "", "", 0, 0);
                            accountDBModel.add(userAccount);
                            //go next activity
                            goToAccount();
                            Toast.makeText(PinActivity.this,
                                    "Registered",Toast.LENGTH_SHORT).show();
                        }
                        //pin not the same as the first one
                        else {
                            pin1.setText("");
                            pin2.setText("");
                            pin3.setText("");
                            pin4.setText("");
                            Toast.makeText(PinActivity.this,
                                    "Pin is not the same, please re-enter",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
            catch (NumberFormatException e){
                Toast.makeText(PinActivity.this,
                        "Invalid pin format! please enter integer only",Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void goToAccount (){
        Intent intent;
        if (userAccount.getType() == Account.INSTRUCTOR){
            intent = new Intent(PinActivity.this, MainInstructorActivity.class);
        }
        else if (userAccount.getType() == Account.STUDENT){
            intent = new Intent(PinActivity.this, MainStudentActivity.class);
        }
        else {
            intent = new Intent(PinActivity.this, MainAdminActivity.class);
        }
        intent.putExtra("accountLogIn", userAccount);
        startActivity (intent);
    }
}