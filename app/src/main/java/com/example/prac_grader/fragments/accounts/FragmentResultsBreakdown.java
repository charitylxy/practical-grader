package com.example.prac_grader.fragments.accounts;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.prac_grader.MainAdminActivity;
import com.example.prac_grader.R;
import com.example.prac_grader.classes.Account;
import com.example.prac_grader.classes.Practical;
import com.example.prac_grader.classes.Result;
import com.example.prac_grader.sql_database.PracticalDBModel;
import com.example.prac_grader.sql_database.ResultDBModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FragmentResultsBreakdown extends Fragment {
    public MainAdminActivity mainActivity;

    Account selectedAccount, userAccount;
    List<Result> results;
    Double totalMarks;
    Double totalMaxMarks;
    TextView txtTotalMarks;

    ResultDBModel resultDBModel;
    PracticalDBModel practicalDBModel;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate((R.layout.fragment_results_breakdown), container, false);

        //get data
        Bundle bundle = this.getArguments();
        selectedAccount = (Account) bundle.getSerializable("selectedAccount");
        userAccount = (Account) bundle.getSerializable("userAccount");

        //load database
        resultDBModel = new ResultDBModel();
        resultDBModel.load(getActivity().getApplicationContext());
        results = new ArrayList<>();
        results = resultDBModel.getStudentResults(selectedAccount.getUsername());

        practicalDBModel = new PracticalDBModel();
        practicalDBModel.load(getActivity().getApplicationContext());

        //recycler view
        recyclerView = (RecyclerView) view.findViewById(R.id.results_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(
                getActivity(),
                LinearLayoutManager.VERTICAL,
                false));
        adapter = new ResultAdapter();
        recyclerView.setAdapter(adapter);

        txtTotalMarks= (TextView)view.findViewById(R.id.txtTotalMarks);
        getTotalMarks();

        return view;
    }

    public void getTotalMarks(){
        totalMarks = 0.0;
        totalMaxMarks = 0.0;
        if (!results.isEmpty()){
            for (Result result : results){
                totalMarks += result.getMarks();
                for (Practical practical : practicalDBModel.getAllPracticals()){
                    if (practical.getId().equals(result.getPractical_id())){
                        totalMaxMarks += practical.getMarks();
                    }
                }
            }
            totalMarks = totalMarks/totalMaxMarks*100;
        }

        txtTotalMarks.setText(String. format("%.2f", totalMarks) + "%");
    }

    private class ResultViewHolder extends RecyclerView.ViewHolder {

        private Result result = null;
        private TextView txtName;
        private TextView txtMarks;


        public ResultViewHolder(LayoutInflater inflater, ViewGroup view) {
            super(inflater.inflate(R.layout.list_results, view, false));

            txtName = (TextView) itemView.findViewById(R.id.txtResultName);
            txtMarks = (TextView) itemView.findViewById(R.id.txtResultMarks);

            //delete result on long click
            if (userAccount.getType() != Account.STUDENT){
                itemView.setOnLongClickListener(view1 -> {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                    dialog.setTitle("Delete Confirmation");
                    dialog.setMessage("Are you sure you want to delete this result? ");
                    dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            results.remove(result);
                            resultDBModel.remove(result);
                            adapter.notifyItemRemoved(getAdapterPosition());
                            getTotalMarks();
                        }
                    });

                    dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                    return true;
                });
            }

        }

        public void bind(Result result) {
            this.result = result;
            String practicalTitle = "";
            double practicalMaxMarks = 0.0;
            for (Practical practical : practicalDBModel.getAllPracticals()){
                if (practical.getId().equals(result.getPractical_id())){
                    practicalTitle= practical.getTitle();
                    practicalMaxMarks += practical.getMarks();
                }
            }
            txtName.setText(practicalTitle);
            txtMarks.setText(String. format("%.2f", result.getMarks()) + "/"
                    + String. format("%.2f", practicalMaxMarks));
        }
    }

    private class ResultAdapter extends RecyclerView.Adapter<ResultViewHolder> {
        @NonNull
        @Override
        public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new ResultViewHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(ResultViewHolder holder, int position) {
            holder.bind(results.get(position));
        }

        @Override
        public int getItemCount() {
            return results.size();
        }
    }
}
