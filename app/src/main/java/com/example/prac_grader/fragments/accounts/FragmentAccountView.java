package com.example.prac_grader.fragments.accounts;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prac_grader.R;
import com.example.prac_grader.classes.Account;
import com.example.prac_grader.classes.CountryData;
import com.example.prac_grader.classes.Result;
import com.example.prac_grader.classes.Student;
import com.example.prac_grader.sql_database.AccountDBModel;
import com.example.prac_grader.sql_database.ResultDBModel;
import com.example.prac_grader.sql_database.StudentDBModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class FragmentAccountView extends Fragment {

    AccountDBModel accountDBModel;
    StudentDBModel studentDBModel;
    ResultDBModel resultDBModel;
    Account selectedAccount, userAccount;

    private EditText tName, tEmail, tUsername, tPin;
    private TextView type;
    private FloatingActionButton editButton, deleteButton;
    private Button saveButton, viewResButton;
    private Spinner countrySpinner;
    private CountryAdapter adapter;
    Boolean isTablet;


    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate((R.layout.fragment_account_view), container, false);

        //get data
        Bundle bundle = this.getArguments();
        selectedAccount = (Account) bundle.getSerializable("selectedAccount");
        userAccount = (Account) bundle.getSerializable("userAccount");

        //load database
        accountDBModel = new AccountDBModel();
        accountDBModel.load(getActivity().getApplicationContext());

        studentDBModel = new StudentDBModel();
        studentDBModel.load(getActivity().getApplicationContext());

        resultDBModel = new ResultDBModel();
        resultDBModel.load(getActivity().getApplicationContext());

        //get device type (tablet or phone)
        isTablet = getResources().getBoolean(R.bool.isTablet);

        //start result breakdown fragment if tablet
        if(isTablet && selectedAccount.getType() == Account.STUDENT){
            Bundle bundle2 = new Bundle();
            bundle2.putSerializable("selectedAccount",selectedAccount);
            bundle2.putSerializable("userAccount",userAccount);
            Fragment fragmentResultsBreakdown = new FragmentResultsBreakdown();
            fragmentResultsBreakdown.setArguments(bundle2);

            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.setReorderingAllowed(true);
            transaction.replace(R.id.fragmentResContainer,fragmentResultsBreakdown , null)
                    .commit();
        }
        else {
            FrameLayout fragmentRes = (FrameLayout) view.findViewById(R.id.fragmentResContainer);
            fragmentRes.setVisibility(View.GONE);
        }

        //initialised edit text boxes
        type = (TextView)view.findViewById(R.id.titleHeader);
        tName = (EditText)view.findViewById(R.id.txtStuName);
        tEmail = (EditText)view.findViewById(R.id.txtAccEmail);
        tUsername = (EditText)view.findViewById(R.id.txtAccUsername);
        tPin = (EditText)view.findViewById(R.id.txtAccPin);
        saveButton = (Button)view.findViewById(R.id.btnSaveAccount);
        if(!isTablet) {
            viewResButton = (Button) view.findViewById(R.id.btnViewResult);
        }
        editButton = (FloatingActionButton)view.findViewById(R.id.btnEditAccount);
        deleteButton = (FloatingActionButton)view.findViewById(R.id.btnDeleteAccount);

        //set up country spinner
        countrySpinner= (Spinner) view.findViewById(R.id.countrySpinner);
        CountryData countryList = new CountryData();
        adapter = new CountryAdapter(getContext(), countryList.getCountryList() );
        countrySpinner.setAdapter(adapter);
        if (userAccount.getType()==Account.STUDENT){
            editButton.setVisibility(View.GONE);
            deleteButton.setVisibility(View.GONE);
        }

        //type title
        if (selectedAccount.getType()== Account.INSTRUCTOR){
            type.setText(R.string.instructor_menu);
            if(!isTablet) {
                viewResButton.setVisibility(View.GONE);
            }
        }
        else{
            type.setText(R.string.student_menu);
            if(!isTablet) {
                viewResButton.setVisibility(View.VISIBLE);
            }
        }

        //set edit text content
        tName.setText(selectedAccount.getName().toString());
        tEmail.setText(selectedAccount.getEmail().toString());
        tUsername.setText(selectedAccount.getUsername().toString());
        countrySpinner.setSelection(selectedAccount.getCountry());
        tPin.setText(String.valueOf(selectedAccount.getPin()));

        countrySpinner.setEnabled(false);
        countrySpinner.setClickable(false);
        saveButton.setVisibility(View.GONE);

        //delete account
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteConfirmation();
            }
        });

        //edit account
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tName.setEnabled(true);
                tEmail.setEnabled(true);
                tPin.setEnabled(true);
                countrySpinner.setEnabled(true);
                countrySpinner.setClickable(true);

                saveButton.setVisibility(View.VISIBLE);
                if(!isTablet) {
                    viewResButton.setVisibility(View.GONE);
                }
                deleteButton.setVisibility(View.GONE);
                editButton.setVisibility(View.GONE);
            }
        });

        //save account edit
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = tName.getText().toString();
                String email = tEmail.getText().toString();
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

                if (TextUtils.isEmpty(pin) || pin.length() < 4){
                    tPin.setError("Pin must be 4 digits.");
                    check = false;
                }

                //if all input is okay - edit account
                if (check == true){
                    tName.setEnabled(false);
                    tEmail.setEnabled(false);
                    tPin.setEnabled(false);
                    countrySpinner.setEnabled(false);
                    countrySpinner.setClickable(false);

                    saveButton.setVisibility(View.GONE);
                    if(!isTablet) {
                        if (selectedAccount.getType() == Account.STUDENT) {
                            viewResButton.setVisibility(View.VISIBLE);
                        }
                    }
                    deleteButton.setVisibility(View.VISIBLE);
                    editButton.setVisibility(View.VISIBLE);

                    selectedAccount.setName(tName.getText().toString());
                    selectedAccount.setEmail(tEmail.getText().toString());
                    selectedAccount.setPin(Integer.parseInt(tPin.getText().toString()));
                    selectedAccount.setCountry(countrySpinner.getSelectedItemPosition());
                    accountDBModel.edit(selectedAccount);

                    Toast.makeText(getActivity(),
                            "Account Saved",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //view result breakdown button(if device is phone)
        if(!isTablet) {
            viewResButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewResBreakdown();
                }
            });
        }
        return view;
    }

    public void returnFragment(){
        Bundle bundle = new Bundle();
        Fragment fragmentReturn;
        bundle.putSerializable("userAccount", userAccount);
        if (selectedAccount.getType() == Account.INSTRUCTOR){
            fragmentReturn = new FragmentInstructor();
        }
        else {
            fragmentReturn = new FragmentStudent();
        }
        fragmentReturn.setArguments(bundle);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setReorderingAllowed(true);
        transaction.replace(R.id.fragmentContainer,fragmentReturn , null)
                .commit();
    }

    public void viewResBreakdown(){
        Bundle bundle = new Bundle();

        bundle.putSerializable("selectedAccount",selectedAccount);
        bundle.putSerializable("userAccount", userAccount);
        Fragment fragmentResultsBreakdown = new FragmentResultsBreakdown();
        fragmentResultsBreakdown.setArguments(bundle);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setReorderingAllowed(true);
        transaction.replace(R.id.fragmentContainer,fragmentResultsBreakdown , null)
                .commit();
    }

    public void deleteAccount(){
        //delete account
        accountDBModel.remove(selectedAccount);

        //delete student's results
        if (selectedAccount.getType() == Account.STUDENT){

            for (Student student : studentDBModel.getInstrutorStudent(userAccount.getUsername())){
                if (student.getUsername().equals(selectedAccount.getUsername())){
                    studentDBModel.remove(student);
                }
            }
            List<Result> resultList = resultDBModel.
                    getStudentResults(selectedAccount.getUsername());

            if (!resultList.isEmpty()){
                for (Result result : resultList){
                    resultDBModel.remove(result);
                }
            }
        }

        //delete instructor from students record
        if (selectedAccount.getType() == Account.INSTRUCTOR){
            for (Student student : studentDBModel.getInstrutorStudent(selectedAccount.getUsername())){
                studentDBModel.remove(student);
            }
        }

        Toast.makeText(getActivity(),
                "Account Deleted",Toast.LENGTH_SHORT).show();
        returnFragment();
    }

    public void deleteConfirmation (){
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("Delete Confirmation");
        dialog.setMessage("Are you sure you want to delete this account? ");
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteAccount();
            }
        });

        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}
