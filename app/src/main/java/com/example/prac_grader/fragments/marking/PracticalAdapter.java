package com.example.prac_grader.fragments.marking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.prac_grader.R;
import com.example.prac_grader.classes.Practical;

import java.util.List;

import androidx.annotation.NonNull;


public class PracticalAdapter extends ArrayAdapter<Practical> {

    public PracticalAdapter(Context context, List<Practical> practicalList) {
        super(context, 0, practicalList);
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

        Practical currentItem = getItem(position);

        if (currentItem != null) {
            textViewName.setText(currentItem.getTitle());
        }

        return convertView;
    }
}

