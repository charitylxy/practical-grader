package com.example.prac_grader.fragments.accounts;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.prac_grader.R;
import com.example.prac_grader.classes.Account;
import com.example.prac_grader.classes.CountryData;
import com.example.prac_grader.classes.Student;
import com.example.prac_grader.sql_database.AccountDBModel;
import com.example.prac_grader.sql_database.StudentDBModel;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class FragmentStudentAdd extends Fragment {
    AccountDBModel accountDBModel;
    StudentDBModel studentDBModel;
    Account userAccount;

    private Spinner countrySpinner;
    private CountryAdapter adapter;

    private TextView addTitle;
    private EditText tName, tEmail, tUsername, tPin;
    private Button addAccountButton;

    public FragmentStudentAdd (){}

    @Override
    public void onCreate(Bundle b) {

        super.onCreate(b);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate((R.layout.fragment_account_add), container, false);

        //get data
        Bundle bundle = this.getArguments();
        userAccount = (Account) bundle.getSerializable("userAccount");

        //load database
        accountDBModel = new AccountDBModel();
        accountDBModel.load(getActivity().getApplicationContext());

        studentDBModel = new StudentDBModel();
        studentDBModel.load(getActivity().getApplicationContext());

        //title
        addTitle = (TextView)view.findViewById(R.id.titleHeader);
        addTitle.setText(getString(R.string.addStudent));

        //initialised edit text boxes
        tName = (EditText)view.findViewById(R.id.txtStuName);
        tEmail = (EditText)view.findViewById(R.id.txtAccEmail);
        tUsername = (EditText)view.findViewById(R.id.txtAccUsername);
        tPin = (EditText)view.findViewById(R.id.txtAccPin);

        //set up country spinner
        countrySpinner= (Spinner) view.findViewById(R.id.countrySpinner);
        CountryData countryList = new CountryData();
        adapter = new CountryAdapter(getContext(), countryList.getCountryList() );
        countrySpinner.setAdapter(adapter);

        //add student
        addAccountButton = (Button)view.findViewById(R.id.btnAddAccount);
        addAccountButton.setOnClickListener(view1 ->  {
            int country = countrySpinner.getSelectedItemPosition();

            String name = tName.getText().toString();
            String email = tEmail.getText().toString();
            String username = tUsername.getText().toString();
            String pin = tPin.getText().toString();

            Boolean check = true;

            //check textbox  if its Empty
            if(TextUtils.isEmpty(name)) {
                tName.setError("Name cannot be empty");
                check = false;
            }

            if(TextUtils.isEmpty(email)) {
                tEmail.setError("Email cannot be empty");
                check = false;
            }

            if(TextUtils.isEmpty(username)) {
                tUsername.setError("Username cannot be empty");
                check = false;
            }
            if (TextUtils.isEmpty(pin) || pin.length() < 4){
                tPin.setError("Pin must be 4 digits.");
                check = false;
            }
            else {
                //check unique
                if (!checkUnique(username)) {
                    tUsername.setError("Username is not unique");
                    check = false;
                }
            }

            //if all input is okay - add account
            if (check == true){
                accountDBModel.add(new Account(username,
                        Integer.parseInt(pin),
                        name, email,
                        country, Account.STUDENT));

                if (userAccount.getType() == Account.INSTRUCTOR){
                   studentDBModel.add(new Student(username, userAccount.getUsername()));
                }
                returnFragment();
            }
        });

        return view;
    }

    public void returnFragment(){
        Bundle bundle = new Bundle();

        bundle.putSerializable("userAccount", userAccount);
        Fragment fragmenStudent = new FragmentStudent();
        fragmenStudent.setArguments(bundle);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setReorderingAllowed(true);
        transaction.replace(R.id.fragmentContainer,fragmenStudent , null)
                .commit();
    }

    public boolean checkUnique (String username){
        Boolean check = true;
        for (Account currentAccount : accountDBModel.getAllAccount()){
            if ((currentAccount.getUsername()).equals(username)){
                check = false;
            }
        }
        return check;
    }
}