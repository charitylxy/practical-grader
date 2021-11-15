package com.example.prac_grader.fragments.marking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.prac_grader.R;
import com.example.prac_grader.classes.Account;

import java.util.List;

import androidx.annotation.NonNull;


public class StudentAdapter extends ArrayAdapter<Account> {

    public StudentAdapter(Context context, List<Account> studentList) {
        super(context, 0, studentList);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_spinner, parent, false
            );
        }

        TextView textViewName = convertView.findViewById(R.id.txtItem);

        Account currentItem = getItem(position);

        if (currentItem != null) {
            textViewName.setText(currentItem.getName());
        }

        return convertView;
    }
}

