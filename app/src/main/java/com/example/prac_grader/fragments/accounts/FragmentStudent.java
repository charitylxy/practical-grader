package com.example.prac_grader.fragments.accounts;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.prac_grader.R;
import com.example.prac_grader.classes.Account;
import com.example.prac_grader.classes.Country;
import com.example.prac_grader.classes.CountryData;
import com.example.prac_grader.classes.Practical;
import com.example.prac_grader.classes.Result;
import com.example.prac_grader.sql_database.AccountDBModel;
import com.example.prac_grader.sql_database.PracticalDBModel;
import com.example.prac_grader.sql_database.ResultDBModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class FragmentStudent extends Fragment {
    private static final String TAG = "StudentActivity";

    AccountDBModel accountDBModel;
    ResultDBModel resultDBModel;
    PracticalDBModel practicalDBModel;

    private Account userAccount;
    private Account selectedStudent = null;
    private List<Account> students, studentSearch;
    private Boolean searchCountry = false;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    FloatingActionButton addButton;
    SearchView searchView;
    ToggleButton searchOption;

    public FragmentStudent() {
    }

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate((R.layout.fragment_student), container, false);

        //get data
        Bundle bundle = this.getArguments();
        userAccount = (Account) bundle.getSerializable("userAccount");

        // load database
        accountDBModel = new AccountDBModel();
        accountDBModel.load(getActivity().getApplicationContext());

        resultDBModel = new ResultDBModel();
        resultDBModel.load(getActivity().getApplicationContext());

        practicalDBModel = new PracticalDBModel();
        practicalDBModel.load(getActivity().getApplicationContext());

        //get students
        students = new ArrayList<>();
        //students = accountDBModel.getAllStudents();
        if (userAccount.getType() == Account.ADMIN) {
            students = accountDBModel.getAllStudents();
        }
        else {
            students = accountDBModel.getInstructorStudents(userAccount.getUsername());
        }
        studentSearch = students;

        //recycler view
        recyclerView = (RecyclerView) view.findViewById(R.id.studentList);
        recyclerView.setLayoutManager(new LinearLayoutManager(
                getActivity(),
                LinearLayoutManager.VERTICAL,
                false));
        adapter = new StudentAdapter();
        recyclerView.setAdapter(adapter);

        //toggle button (search option)
        searchOption = (ToggleButton) view.findViewById(R.id.searchOptionStu);
        searchOption.setTextOff("Name");
        searchOption.setTextOn("Country");
        searchOption.setText("Name");
        searchOption.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    searchView.setQueryHint("Search Students' Country");
                    searchCountry = true;
                } else {
                    searchView.setQueryHint("Search Students' Name");
                    searchCountry = false;
                }
            }
        });

        //search filter
        searchView = (SearchView) view.findViewById(R.id.searchStudent);
        searchView();

        //add button
        addButton = (FloatingActionButton) view.findViewById(R.id.btnEditStudent);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addStudent();
            }
        });
        return view;
    }

    public void addStudent() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("userAccount", userAccount);
        Fragment fragmentAddStudent = new FragmentStudentAdd();
        fragmentAddStudent.setArguments(bundle);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setReorderingAllowed(true);
        transaction.replace(R.id.fragmentContainer,fragmentAddStudent , null)
                .commit();
    }

    public void viewStudent(){
        Bundle bundle = new Bundle();
        bundle.putSerializable("selectedAccount",selectedStudent);
        bundle.putSerializable("userAccount", userAccount);
        Fragment fragmentViewAccount = new FragmentAccountView();
        fragmentViewAccount.setArguments(bundle);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setReorderingAllowed(true);
        transaction.replace(R.id.fragmentContainer,fragmentViewAccount , null)
                .commit();
    }

    void searchView (){

        searchView.setQueryHint("Search Students");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.toLowerCase();
                List<Account> newList = new ArrayList<>();

                //search by name
                if (searchCountry == false) {
                    for (Account student : students) {
                        String stuName = student.getName().toLowerCase();

                        if (stuName.contains(newText)) {
                            newList.add(student);
                        }
                    }
                }

                //search by country
                else{
                    CountryData countryList = new CountryData();

                    for (Account student : students) {
                        String stuCountry = countryList.get(student.getCountry())
                                .getName().toLowerCase();

                        if (stuCountry.contains(newText)) {
                            newList.add(student);
                        }
                    }
                }
                studentSearch = new ArrayList<Account>();

                studentSearch.addAll(newList);
                adapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    private class StudentViewHolder extends RecyclerView.ViewHolder {

        private Account student = null;
        private ImageView imgAccount;
        private TextView txtName;
        private TextView txtMarks;


        public StudentViewHolder(LayoutInflater inflater, ViewGroup view) {
            super(inflater.inflate(R.layout.list_students, view, false));

            imgAccount = (ImageView) itemView.findViewById(R.id.imgStu);
            txtName = (TextView) itemView.findViewById(R.id.txtStuName);
            txtMarks = (TextView) itemView.findViewById(R.id.txtStuMarks);

            itemView.setOnClickListener(view1 -> {
                selectedStudent = student;
                viewStudent();
            });
        }

        public void bind(Account student) {
            this.student = student;

            CountryData countryList = new CountryData();
            Country selectedCountry = countryList.get(student.getCountry());
            imgAccount.setImageResource(selectedCountry.getDrawableId());
            txtName.setText(student.getName());

            Double totalMarks = 0.0;
            Double totalMaxMarks = 0.0;

            //set total marks and colour
            List <Result> resultList = resultDBModel.getStudentResults(student.getUsername());
            if (!resultList.isEmpty()){
                for (Result result : resultList){
                    totalMarks += result.getMarks();
                    for (Practical practical : practicalDBModel.getAllPracticals()){
                        if (practical.getId().equals(result.getPractical_id())){
                            totalMaxMarks += practical.getMarks();
                        }
                    }
                }
                totalMarks = totalMarks/totalMaxMarks*100;
            }
            txtMarks.setText(String. format("%.2f", totalMarks) + "%");

            //set marks colour
            if (totalMarks < 50){
                txtMarks.setTextColor(getResources().getColor(R.color.red));
            }
            else if (totalMarks >=50 && totalMarks <70){
                txtMarks.setTextColor(getResources().getColor(R.color.orange));
            }
            else {
                txtMarks.setTextColor(getResources().getColor(R.color.green));
            }

        }
    }

    private class StudentAdapter extends RecyclerView.Adapter<StudentViewHolder> {
        @NonNull
        @Override
        public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new FragmentStudent.StudentViewHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull FragmentStudent.StudentViewHolder holder, int position) {
           holder.bind(studentSearch.get(position));
        }

        @Override
        public int getItemCount() {
            return studentSearch.size();
        }

    }

}
