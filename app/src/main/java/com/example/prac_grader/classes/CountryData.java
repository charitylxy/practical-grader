package com.example.prac_grader.classes;

import com.example.prac_grader.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CountryData {
    public static final int[] DRAWABLES = {
            R.drawable.flag__unknown, R.drawable.flag_ad, R.drawable.flag_ae,
            R.drawable.flag_af, R.drawable.flag_ag, R.drawable.flag_ai,
            R.drawable.flag_al, R.drawable.flag_am, R.drawable.flag_ar,
            R.drawable.flag_at, R.drawable.flag_au, R.drawable.flag_az,
            R.drawable.flag_ba, R.drawable.flag_bd, R.drawable.flag_be,
            R.drawable.flag_bf, R.drawable.flag_bg, R.drawable.flag_br,
            R.drawable.flag_ca, R.drawable.flag_ch, R.drawable.flag_cn,
            R.drawable.flag_cz, R.drawable.flag_de, R.drawable.flag_dk,
            R.drawable.flag_es, R.drawable.flag_fr, R.drawable.flag_gb,
            R.drawable.flag_ge, R.drawable.flag_gr, R.drawable.flag_hk,
            R.drawable.flag_it, R.drawable.flag_jp, R.drawable.flag_kr,
            R.drawable.flag_lt, R.drawable.flag_mx, R.drawable.flag_my,
            R.drawable.flag_nl, R.drawable.flag_pl, R.drawable.flag_qa,
            R.drawable.flag_ru, R.drawable.flag_uk, R.drawable.flag_us,
            R.drawable.flag_vn
    }        ;

    public List<Country> getCountryList() {
        return countryList;
    }

    private List<Country> countryList = Arrays.asList(new Country[]{
            new Country(R.drawable.flag__unknown, "Unknown"),
            new Country(R.drawable.flag_ad, "Andorra"),
            new Country(R.drawable.flag_ae, "United Arab Emirates"),
            new Country(R.drawable.flag_af, "Afghanistan"),
            new Country(R.drawable.flag_ag, "Antigua and Barbuda"),
            new Country(R.drawable.flag_ai, "Anguilla"),
            new Country(R.drawable.flag_al, "Albania"),
            new Country(R.drawable.flag_am, "Armenia"),
            new Country(R.drawable.flag_ar, "Argentina"),
            new Country(R.drawable.flag_at, "Austria"),
            new Country(R.drawable.flag_au, "Australia"),
            new Country(R.drawable.flag_az, "Azerbaijan"),
            new Country(R.drawable.flag_ba, "Bosnia and Herzegovina"),
            new Country(R.drawable.flag_bd, "Belgium"),
            new Country(R.drawable.flag_bf, "Burkina Faso"),
            new Country(R.drawable.flag_bg, "Bulgaria"),
            new Country(R.drawable.flag_br, "Brazil"),
            new Country(R.drawable.flag_ca, "Canada"),
            new Country(R.drawable.flag_ch, "Switzerland"),
            new Country(R.drawable.flag_cn, "China"),
            new Country(R.drawable.flag_cz, "Czeck Republic"),
            new Country(R.drawable.flag_de, "Germany"),
            new Country(R.drawable.flag_dk, "Denmark"),
            new Country(R.drawable.flag_es, "Spain"),
            new Country(R.drawable.flag_fr, "France"),
            new Country(R.drawable.flag_gb, "Great Britain"),
            new Country(R.drawable.flag_ge, "Georgia"),
            new Country(R.drawable.flag_gr, "Greece"),
            new Country(R.drawable.flag_hk, "Hong Kong"),
            new Country(R.drawable.flag_it, "Italy"),
            new Country(R.drawable.flag_jp, "Japan"),
            new Country(R.drawable.flag_kr, "South Korea"),
            new Country(R.drawable.flag_lt, "Lithuania"),
            new Country(R.drawable.flag_mx, "Mexico"),
            new Country(R.drawable.flag_my, "Malaysia"),
            new Country(R.drawable.flag_nl, "Netherlands"),
            new Country(R.drawable.flag_pl, "Polland"),
            new Country(R.drawable.flag_qa, "Qatar"),
            new Country(R.drawable.flag_ru, "Russia"),
            new Country(R.drawable.flag_uk, "United Kingdom"),
            new Country(R.drawable.flag_us, "United States of America"),
            new Country(R.drawable.flag_vn, "Vietnam")
    });



    private static CountryData instance = null;

    public static CountryData get()
    {
        if(instance == null)
        {
            instance = new CountryData();
        }
        return instance;
    }

    public CountryData() {
    }

    public Country get(int i)
    {
        return countryList.get(i);
    }

    public int size()
    {
        return countryList.size();
    }

}
