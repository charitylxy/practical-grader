package com.example.prac_grader.fragments.marking;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.prac_grader.R;
import com.example.prac_grader.classes.Account;
import com.example.prac_grader.classes.Practical;
import com.example.prac_grader.classes.Result;
import com.example.prac_grader.sql_database.AccountDBModel;
import com.example.prac_grader.sql_database.PracticalDBModel;
import com.example.prac_grader.sql_database.ResultDBModel;

import java.util.ArrayList;
import java.util.List;

public class FragmentMarking extends Fragment {
    AccountDBModel accountDBModel;
    ResultDBModel resultDBModel;
    PracticalDBModel practicalDBModel;
    Account userAccount;

    Spinner studentSpinner, practicalSpinner;
    StudentAdapter studentAdapter;
    PracticalAdapter practicalAdapter;

    List<Practical> practicalList;
    List<Account> studentList;

    EditText marks;
    Button addMarksButton;

    public FragmentMarking (){}

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate((R.layout.fragment_marking), container, false);

        //get data
        Bundle bundle = this.getArguments();
        userAccount = (Account) bundle.getSerializable("userAccount");

        //load database
        accountDBModel = new AccountDBModel();
        accountDBModel.load(getActivity().getApplicationContext());

        resultDBModel = new ResultDBModel();
        resultDBModel.load(getActivity().getApplicationContext());

        practicalDBModel = new PracticalDBModel();
        practicalDBModel.load(getActivity().getApplicationContext());

        //set up student spinner
        studentSpinner= (Spinner) view.findViewById(R.id.studentSpinner);
        studentList = new ArrayList<>();
        if (userAccount.getType() == Account.INSTRUCTOR){
            studentList = accountDBModel.getInstructorStudents(userAccount.getUsername());
        }
        else {
            studentList = accountDBModel.getAllStudents();
        }
        studentAdapter = new StudentAdapter(getContext(), studentList );
        studentSpinner.setAdapter(studentAdapter);

        //set up practical spinner
        practicalSpinner= (Spinner) view.findViewById(R.id.practical_spinner);
        practicalList = new ArrayList<>();
        practicalList =  practicalDBModel.getAllPracticals();
        practicalAdapter = new PracticalAdapter(getContext(), practicalList );
        practicalSpinner.setAdapter(practicalAdapter);

        marks = (EditText)view.findViewById(R.id.txtStudentMarks);
        addMarksButton = (Button)view.findViewById(R.id.btnAddMarks);

        //add marks
        addMarksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Account student;
                Practical practical;
                if (studentSpinner.getSelectedItemPosition() < 0 ){
                    Toast.makeText(getActivity(),
                            "Please add students first!",Toast.LENGTH_SHORT).show();
                }
                else if (practicalSpinner.getSelectedItemPosition() < 0 ) {
                    Toast.makeText(getActivity(),
                            "Please add practicals first!",Toast.LENGTH_SHORT).show();
                }
                else {
                    student= studentList.get (studentSpinner.getSelectedItemPosition());
                    practical =  practicalList.get(practicalSpinner.getSelectedItemPosition());
                    String txtMarks = marks.getText().toString();
                    if(TextUtils.isEmpty(txtMarks)) {
                        marks.setError("Marks cannot be empty");
                    }
                    else {
                        Double resultMarks = Double.parseDouble(marks.getText().toString());
                        if (resultMarks < 0 ){
                            marks.setError("Marks cannot be less than 0");
                        }
                        else if (resultMarks > practical.getMarks()){
                            marks.setError("Marks cannot be more than the maximum marks");
                        }
                        else {
                            resultDBModel.add(new Result(practical.getId(),
                                    student.getUsername(), resultMarks));

                            Toast.makeText(getActivity(),
                                    "Results Added",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                marks.setText("");
            }
        });

        return view;
    }

}