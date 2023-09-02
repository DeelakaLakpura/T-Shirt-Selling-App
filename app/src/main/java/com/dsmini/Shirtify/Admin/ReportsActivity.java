package com.dsmini.Shirtify.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dsmini.Shirtify.Model.Report;
import com.dsmini.Shirtify.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ReportsActivity extends AppCompatActivity {

    PieChart pieChart;
    PieData pieData;
    PieDataSet pieDataSet;
    ArrayList pieEntries;
    private ArrayList<Report> reportArrayList;


    private Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn10, btn11, btn12;
    DatabaseReference myRootRef;
    private ProgressBar progressBar;
    private TextView noSellText;
    private TextView dateText;
    private TextView totalSell;
    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        initAll();


        clickListeners();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void clickListeners() {
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadData("01");
                setDate("01");
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadData("02");
                setDate("02");
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadData("03");
                setDate("03");
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadData("04");
                setDate("04");
            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadData("05");
                setDate("05");
            }
        });
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadData("06");
                setDate("06");
            }
        });
        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadData("07");
                setDate("07");
            }
        });
        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadData("08");
                setDate("08");
            }
        });
        btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadData("09");
                setDate("09");
            }
        });
        btn10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadData("10");
                setDate("10");
            }
        });
        btn11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadData("11");
                setDate("11");
            }
        });
        btn12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadData("12");
                setDate("12");
            }
        });
    }

    private void setDate(String s) {
        switch (s){
            case "01":{
                dateText.setText("Report of January");
                break;
            }
            case "02":{
                dateText.setText("Report of February");
                break;
            }
            case "03":{
                dateText.setText("Report of March");
                break;
            }
            case "04":{
                dateText.setText("Report of April");
                break;
            }
            case "05":{
                dateText.setText("Report of May");
                break;
            }
            case "06":{
                dateText.setText("Report of June");
                break;
            }
            case "07":{
                dateText.setText("Report of July");
                break;
            }
            case "08":{
                dateText.setText("Report of August");
                break;
            }
            case "09":{
                dateText.setText("Report of September");
                break;
            }
            case "10":{
                dateText.setText("Report of October");
                break;
            }
            case "11":{
                dateText.setText("Report of November");
                break;
            }
            case "12":{
                dateText.setText("Report of December");
                break;
            }
        }
    }

    private void LoadData(String month) {
        progressBar.setVisibility(View.VISIBLE);
        final int[] counter = {0};
        reportArrayList.clear();
        myRootRef.child("Reports").child(month).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        Report product = new Report();
                        product = child.getValue(Report.class);
                        reportArrayList.add(product);
                        counter[0]++;
                        if (counter[0] == dataSnapshot.getChildrenCount()) {
                            getEntries();
                            progressBar.setVisibility(View.GONE);
                        }
                        Log.d("ShowEventInfo:", product.toString());
                    }
                } else {
                    noSellText.setVisibility(View.VISIBLE);
                    pieChart.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void initAll() {
        pieChart = findViewById(R.id.pieChart);
        progressBar = findViewById(R.id.progress_bar);
        noSellText = findViewById(R.id.no_sell_text);
        dateText = findViewById(R.id.date_text);
        backBtn = findViewById(R.id.checkout_back_btn);
        totalSell = findViewById(R.id.total_sell);
        reportArrayList = new ArrayList<>();
        myRootRef = FirebaseDatabase.getInstance().getReference();

        btn1 = findViewById(R.id.month1);
        btn2 = findViewById(R.id.month2);
        btn3 = findViewById(R.id.month3);
        btn4 = findViewById(R.id.month4);
        btn5 = findViewById(R.id.month5);
        btn6 = findViewById(R.id.month6);
        btn7 = findViewById(R.id.month7);
        btn8 = findViewById(R.id.month8);
        btn9 = findViewById(R.id.month9);
        btn10 = findViewById(R.id.month10);
        btn11 = findViewById(R.id.month11);
        btn12 = findViewById(R.id.month12);

    }

    private void getEntries() {
        pieEntries = new ArrayList<>();

        pieEntries.clear();

        if(reportArrayList.size()>0){
            pieChart.setVisibility(View.VISIBLE);
            noSellText.setVisibility(View.GONE);

            int Alacarte=0;
            int Bento=0;
            int Handroll=0;
            int Beverage=0;

            double totalPrice=0.0;

            for(int i=0;i<reportArrayList.size();i++){
                Alacarte=Alacarte+reportArrayList.get(i).getAlacarte();
                Bento=Bento+reportArrayList.get(i).getBento();
                Handroll=Handroll+reportArrayList.get(i).getHandroll();
                Beverage=Beverage+reportArrayList.get(i).getBeverage();
                totalPrice=totalPrice+reportArrayList.get(i).getTotalPrice();
            }


            Log.d("countText",Alacarte+"");
            Log.d("countText",Bento+"");
            Log.d("countText",Handroll+"");
            Log.d("countText",Beverage+"");

            totalSell.setText("Total Sell : RM: "+totalPrice);

            pieEntries.add(new PieEntry(Alacarte, "Alacarte", 0));
            pieEntries.add(new PieEntry(Bento, "Bento", 1));
            pieEntries.add(new PieEntry(Handroll, "Handroll", 2));
            pieEntries.add(new PieEntry(Beverage, "Beverage", 3));


            pieChart.clear();

            pieDataSet = new PieDataSet(pieEntries, "Categories");
            pieData = new PieData(pieDataSet);
            pieChart.setData(pieData);
            pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
            pieDataSet.setSliceSpace(2f);
            pieDataSet.setValueTextColor(Color.WHITE);
            pieDataSet.setValueTextSize(10f);
            pieDataSet.setSliceSpace(5f);

        }
        else{
            pieChart.setVisibility(View.GONE);
            noSellText.setVisibility(View.VISIBLE);
        }



    }
}