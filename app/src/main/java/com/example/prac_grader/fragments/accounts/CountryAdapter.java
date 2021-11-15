package com.example.prac_grader.fragments.accounts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.prac_grader.R;
import com.example.prac_grader.classes.Country;

import java.util.List;

import androidx.annotation.NonNull;


public class CountryAdapter extends ArrayAdapter<Country> {

    public CountryAdapter(Context context, List<Country> countryList) {
        super(context, 0, countryList);
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
                    R.layout.list_countries, parent, false
            );
        }

        ImageView imageViewFlag = convertView.findViewById(R.id.imgFlag);
        TextView textViewName = convertView.findViewById(R.id.txtItem);

        Country currentItem = getItem(position);

        if (currentItem != null) {
            imageViewFlag.setImageResource(currentItem.getDrawableId());
            textViewName.setText(currentItem.getName());
        }

        return convertView;
    }
}

