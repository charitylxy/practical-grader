package com.example.prac_grader.fragments.practicals;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prac_grader.R;
import com.example.prac_grader.classes.Practical;
import com.example.prac_grader.sql_database.PracticalDBModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class FragmentPracticalAdd extends Fragment {
    private static final String TAG = "MarkingActivity";

    PracticalDBModel practicalDBModel;

    private EditText title, desc, marks;
    private TextView header;
    private Button addPracButton;
    private FloatingActionButton editPracButton, deletePracButton;

    public FragmentPracticalAdd(){}

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate((R.layout.fragment_practical_add_view), container, false);

        //load database
        practicalDBModel = new PracticalDBModel();
        practicalDBModel.load(getActivity().getApplicationContext());

        //initialised edit text boxes
        header = (TextView)view.findViewById(R.id.titleHeader) ;
        title = (EditText)view.findViewById(R.id.txtPracTitle);
        desc = (EditText)view.findViewById(R.id.txtPracDesc);
        marks = (EditText)view.findViewById(R.id.txtPracMarks);

        //initialised buttons
        editPracButton = (FloatingActionButton)view.findViewById(R.id.btnEditPrac);
        deletePracButton = (FloatingActionButton)view.findViewById(R.id.btnDeletePrac);
        addPracButton = (Button)view.findViewById(R.id.btnAddPractical);

        //set visibility
        editPracButton.setVisibility(View.GONE);
        deletePracButton.setVisibility(View.GONE);
        addPracButton.setVisibility(View.VISIBLE);
        addPracButton.setText(R.string.addButton);

        header.setText(getString(R.string.addPractical));
        title.setEnabled(true);
        desc.setEnabled(true);
        marks.setEnabled(true);

        //add practical
        addPracButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtTitle =title.getText().toString();
                String txtDesc = desc.getText().toString();
                String txtMarks = marks.getText().toString();

                //if all field is filled
                if (!TextUtils.isEmpty(txtTitle) && !TextUtils.isEmpty(txtDesc) && !TextUtils.isEmpty(txtMarks)){
                    if (Double.parseDouble(txtMarks) > 0.0){
                        Practical newPractical = new Practical(
                                txtTitle, txtDesc,
                                Double.parseDouble(txtMarks));
                        practicalDBModel.add(newPractical);

                        Toast.makeText(getActivity(),
                                "Practical Added",Toast.LENGTH_SHORT).show();
                        returnFragment();
                    }
                    else {
                        marks.setError("Marks must be more than zero");
                    }

                }

                // empty field found
                else {
                    if(TextUtils.isEmpty(txtTitle)) {
                        title.setError("Title cannot be empty");
                    }

                    if(TextUtils.isEmpty(txtDesc)) {
                        desc.setError("Description cannot be empty");
                    }

                    if(TextUtils.isEmpty(txtMarks)) {
                        marks.setError("Marks cannot be empty");
                    }
                }
            }
        });

        return view;
    }

    public void returnFragment(){
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setReorderingAllowed(true);
        transaction.replace(R.id.fragmentContainer, FragmentPractical.class, null)
                .commit();
    }


}