package com.example.prac_grader.fragments.practicals;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.example.prac_grader.classes.Result;
import com.example.prac_grader.sql_database.PracticalDBModel;
import com.example.prac_grader.sql_database.ResultDBModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class FragmentPracticalView extends Fragment {

    PracticalDBModel practicalDBModel;
    ResultDBModel resultDBModel;
    Practical selectedPractical;

    private EditText title, desc, marks;
    private TextView header;
    private Button savePracButton;
    private FloatingActionButton editPracButton, deletePracButton;


    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate((R.layout.fragment_practical_add_view), container, false);

        //get data
        Bundle bundle = this.getArguments();
        selectedPractical = (Practical) bundle.getSerializable("selectedPractical");

        //load database
        practicalDBModel = new PracticalDBModel();
        practicalDBModel.load(getActivity().getApplicationContext());

        resultDBModel = new ResultDBModel();
        resultDBModel.load(getActivity().getApplicationContext());

        //initialised edit text and buttons
        header = (TextView)view.findViewById(R.id.titleHeader) ;
        title = (EditText)view.findViewById(R.id.txtPracTitle);
        desc = (EditText)view.findViewById(R.id.txtPracDesc);
        marks = (EditText)view.findViewById(R.id.txtPracMarks);
        editPracButton = (FloatingActionButton)view.findViewById(R.id.btnEditPrac);
        deletePracButton = (FloatingActionButton)view.findViewById(R.id.btnDeletePrac);
        savePracButton = (Button)view.findViewById(R.id.btnAddPractical);

        savePracButton.setVisibility(View.GONE);
        editPracButton.setVisibility(View.VISIBLE);
        deletePracButton.setVisibility(View.VISIBLE);

        header.setText(getString(R.string.practical));
        title.setEnabled(false);
        desc.setEnabled(false);
        marks.setEnabled(false);

        savePracButton.setText(R.string.saveButton);
        title.setText(selectedPractical.getTitle());
        desc.setText(selectedPractical.getDescription());
        marks.setText(Double.toString(selectedPractical.getMarks()));

        //edit practica;
        editPracButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                header.setText(getString(R.string.editPractical));
                title.setEnabled(true);
                desc.setEnabled(true);
                marks.setEnabled(true);

                editPracButton.setVisibility(View.GONE);
                deletePracButton.setVisibility(View.GONE);
                savePracButton.setVisibility(View.VISIBLE);
            }
        });

        //save practical edit
        savePracButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtTitle =title.getText().toString();
                String txtDesc = desc.getText().toString();
                String txtMarks = marks.getText().toString();

                if (!TextUtils.isEmpty(txtTitle) && !TextUtils.isEmpty(txtDesc) && !TextUtils.isEmpty(txtMarks)){
                    if (Double.parseDouble(txtMarks)>0.0){
                        header.setText(getString(R.string.practical));
                        title.setEnabled(false);
                        desc.setEnabled(false);
                        marks.setEnabled(false);

                        savePracButton.setVisibility(View.GONE);
                        editPracButton.setVisibility(View.VISIBLE);
                        deletePracButton.setVisibility(View.VISIBLE);

                        selectedPractical.setTitle(title.getText().toString());
                        selectedPractical.setDescription(desc.getText().toString());
                        selectedPractical.setMarks(Double.parseDouble(marks.getText().toString()));
                        practicalDBModel.edit(selectedPractical);

                        Toast.makeText(getActivity(),
                                "Practical Saved",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        marks.setError("Marks must be more than zero");
                    }
                }
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

        //delete practical
        deletePracButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteConfirmation();
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

    public void deletePractical (){
        //delete account
        practicalDBModel.remove(selectedPractical);

        //delete student's results
        List<Result> resultList = resultDBModel.getPracticalResults(selectedPractical.getId());

        if (!resultList.isEmpty()){
            for (Result result : resultList){
                resultDBModel.remove(result);
            }
        }

        Toast.makeText(getActivity(),
                "Practical Deleted",Toast.LENGTH_SHORT).show();

        returnFragment();
    }

    public void deleteConfirmation (){
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("Delete Confirmation");
        dialog.setMessage("Are you sure you want to delete this practical?");
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               deletePractical();
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
