package com.dsmini.Shirtify.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dsmini.Shirtify.Fragment.HomeFragment;
import com.dsmini.Shirtify.R;

public class SearchFiltersActivity extends AppCompatActivity implements View.OnClickListener {
    TextView clearFilters;
    LinearLayout categoryAlaCarte, categoryBento, categoryHandRoll, categoryBeverage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_filters);

        initAll();

        clearFilters.setOnClickListener(this);

        categoryAlaCarte.setOnClickListener(this);
        categoryBento.setOnClickListener(this);
        categoryHandRoll.setOnClickListener(this);
        categoryBeverage.setOnClickListener(this);


    }

    private void initAll() {
        clearFilters = findViewById(R.id.id_clear_btn);
        categoryAlaCarte = findViewById(R.id.cat_alacart);
        categoryBento = findViewById(R.id.cat_bento);
        categoryHandRoll = findViewById(R.id.cat_handroll);
        categoryBeverage = findViewById(R.id.cat_beverage);
    }

    public void goBack(View view) {
        finish();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.cat_alacart:
                HomeFragment.category = "Shirts";
                HomeFragment.isCategorySeleted = true;
                HomeFragment.isFiltersApplied = true;
                finish();
                break;
            case R.id.cat_bento:
                HomeFragment.category = "T - Shirt";
                HomeFragment.isCategorySeleted = true;
                HomeFragment.isFiltersApplied = true;
                finish();
                break;
            case R.id.cat_handroll:
                HomeFragment.category = "Mens Skinny";
                HomeFragment.isCategorySeleted = true;
                HomeFragment.isFiltersApplied = true;
                finish();
                break;
            case R.id.cat_beverage:
                HomeFragment.category = "Beverage";
                HomeFragment.isCategorySeleted = true;
                HomeFragment.isFiltersApplied = true;
                finish();
                break;
            case R.id.id_clear_btn:
                HomeFragment.clearClicked();
                finish();
                break;
        }
    }
}