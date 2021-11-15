package com.example.prac_grader.fragments.practicals;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.prac_grader.R;
import com.example.prac_grader.classes.Practical;
import com.example.prac_grader.sql_database.PracticalDBModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class FragmentPractical extends Fragment {
    private static final String TAG = "PracticalActivity";
    PracticalDBModel practicalDBModel;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private Practical selectedPractical = null;
    FloatingActionButton addButton;

    private List<Practical> practicals;

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate((R.layout.fragment_practical), container, false);

        //load database
        practicalDBModel = new PracticalDBModel();
        practicalDBModel.load(getActivity().getApplicationContext());

        //get practicals
        practicals = new ArrayList<>();
        practicals = practicalDBModel.getAllPracticals();

        //recycler view
        recyclerView = (RecyclerView) view.findViewById(R.id.practicalList);
        recyclerView.setLayoutManager(new LinearLayoutManager(
                getActivity(),
                LinearLayoutManager.VERTICAL,
                false));
        adapter = new PracticalAdapter();
        recyclerView.setAdapter(adapter);

        //add practical
        addButton = (FloatingActionButton)view.findViewById(R.id.btnAddPractical);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPractical();
            }
        });
        return view;
    }

    public void viewPractical(){
        Bundle bundle = new Bundle();

        bundle.putSerializable("selectedPractical",selectedPractical);
        Fragment fragmentViewPractical = new FragmentPracticalView();
        fragmentViewPractical.setArguments(bundle);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setReorderingAllowed(true);
        transaction.replace(R.id.fragmentContainer,fragmentViewPractical , null)
                .commit();
    }

    public void addPractical(){
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setReorderingAllowed(true);
        transaction.replace(R.id.fragmentContainer, FragmentPracticalAdd.class, null)
                .commit();
    }

    private class PracticalViewHolder extends RecyclerView.ViewHolder{

        private Practical practical = null;
        private TextView txtName;



        public PracticalViewHolder (LayoutInflater inflater, ViewGroup view){
            super (inflater.inflate(R.layout.list_practicals, view, false));

            txtName = (TextView) itemView.findViewById(R.id.txtPracTitle);

            itemView.setOnClickListener(view1 -> {
                selectedPractical = practical;
                viewPractical();
            });
        }

        public void bind (Practical practical){
            this.practical = practical;
            txtName.setText(practical.getTitle());
        }
    }

    private class PracticalAdapter extends RecyclerView.Adapter<FragmentPractical.PracticalViewHolder>{
        @NonNull
        @Override
        public FragmentPractical.PracticalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new FragmentPractical.PracticalViewHolder(layoutInflater,parent);
        }

        @Override
        public void onBindViewHolder(@NonNull PracticalViewHolder holder, int position) {
             holder.bind(practicals.get(position));
        }

        @Override
        public int getItemCount() {
            return practicals.size();

        }
    }

}