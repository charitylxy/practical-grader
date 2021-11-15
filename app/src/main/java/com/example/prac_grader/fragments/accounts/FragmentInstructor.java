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
import com.example.prac_grader.sql_database.AccountDBModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class FragmentInstructor extends Fragment {

    private AccountDBModel accountDBModel;
    private Account selectedInstructor = null;
    private Account userAccount;
    private List<Account> instructors, instructorSearach;
    private Boolean searchCountry = false;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    FloatingActionButton addButton;
    ToggleButton searchOption;
    SearchView searchView;

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate((R.layout.fragment_instructor), container, false);

        //get data
        Bundle bundle = this.getArguments();
        userAccount = (Account) bundle.getSerializable("userAccount");

        // load database
        accountDBModel = new AccountDBModel();
        accountDBModel.load(getActivity().getApplicationContext());

        //get instructors
        instructors = new ArrayList<>();
        instructors = accountDBModel.getAllInstructor();
        instructorSearach = instructors;

        //recycler view
        recyclerView = (RecyclerView) view.findViewById(R.id.instructorList);
        recyclerView.setLayoutManager(new LinearLayoutManager(
                getActivity(),
                LinearLayoutManager.VERTICAL,
                false));
        adapter = new InstructorAdapter();
        recyclerView.setAdapter(adapter);


        //toggle button (search option)
        searchOption = (ToggleButton) view.findViewById(R.id.searchOptionIns);
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
        searchView = (SearchView) view.findViewById(R.id.searchInstructor);
        searchView();

        //add button
        addButton = (FloatingActionButton)view.findViewById(R.id.btnAddInstructor);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addInstructor();
            }
        });
        return view;
    }

    public void addInstructor(){
        Bundle bundle = new Bundle();

        bundle.putSerializable("userAccount", userAccount);
        Fragment fragmentAddLecturer = new FragmentInstructorAdd();
        fragmentAddLecturer.setArguments(bundle);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setReorderingAllowed(true);
        transaction.replace(R.id.fragmentContainer,fragmentAddLecturer , null)
                .commit();
    }

    public void viewInstructor(){
        Bundle bundle = new Bundle();

        bundle.putSerializable("selectedAccount",selectedInstructor);
        bundle.putSerializable("userAccount", userAccount);
        Fragment fragmentViewAccount = new FragmentAccountView();
        fragmentViewAccount.setArguments(bundle);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setReorderingAllowed(true);
        transaction.replace(R.id.fragmentContainer,fragmentViewAccount , null)
                .commit();
    }

    void searchView (){

        searchView.setQueryHint("Search Instructor");
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newText = searchView.getQuery().toString();

                newText = newText.toLowerCase();
                List<Account> newList = new ArrayList<>();

                // search by name
                if (searchCountry == false) {
                    for (Account instructor : instructors) {
                        String insName = instructor.getName().toLowerCase();

                        if (insName.contains(newText)) {
                            newList.add(instructor);
                        }
                    }
                }

                //search by country
                else{
                    CountryData countryList = new CountryData();

                    for (Account instructor : instructors) {
                        String insCountry = countryList.get(instructor.getCountry())
                                .getName().toLowerCase();

                        if (insCountry.contains(newText)) {
                            newList.add(instructor);
                        }
                    }
                }
                instructorSearach = new ArrayList<Account>();
                instructorSearach.addAll(newList);
                adapter.notifyDataSetChanged();
            }
        });


//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                newText = newText.toLowerCase();
//                List<Account> newList = new ArrayList<>();
//
//                // search by name
//                if (searchCountry == false) {
//                    for (Account instructor : instructors) {
//                        String insName = instructor.getName().toLowerCase();
//
//                        if (insName.contains(newText)) {
//                            newList.add(instructor);
//                        }
//                    }
//                }
//
//                //search by country
//                else{
//                    CountryData countryList = new CountryData();
//
//                    for (Account instructor : instructors) {
//                        String insCountry = countryList.get(instructor.getCountry())
//                                .getName().toLowerCase();
//
//                        if (insCountry.contains(newText)) {
//                            newList.add(instructor);
//                        }
//                    }
//                }
//                instructorSearach = new ArrayList<Account>();
//                instructorSearach.addAll(newList);
//                adapter.notifyDataSetChanged();
//                return true;
//            }
//        });
    }

    private class InstructorViewHolder extends RecyclerView.ViewHolder{

        private Account instructor = null;
        private ImageView imgAccount;
        private TextView txtName;



        public InstructorViewHolder (LayoutInflater inflater, ViewGroup view){
            super (inflater.inflate(R.layout.list_instructors, view, false));

            imgAccount= (ImageView)itemView.findViewById(R.id.imgStu);
            txtName = (TextView) itemView.findViewById(R.id.txtStuName);

            itemView.setOnClickListener(view1 -> {
                selectedInstructor = instructor;
                viewInstructor();
            });
        }

        public void bind (Account instructor){
            this.instructor = instructor;
            CountryData countryList = new CountryData();
            Country selectedCountry = countryList.get(instructor.getCountry());
            imgAccount.setImageResource(selectedCountry.getDrawableId());
            txtName.setText(instructor.getName());
        }
    }

    private class InstructorAdapter extends RecyclerView.Adapter<InstructorViewHolder>{
        @NonNull
        @Override
        public InstructorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new InstructorViewHolder (layoutInflater,parent);
        }

        @Override
        public void onBindViewHolder(@NonNull InstructorViewHolder holder, int position) {
            holder.bind(instructorSearach.get(position));
        }

        @Override
        public int getItemCount() {
            return instructorSearach.size();
        }
    }






}