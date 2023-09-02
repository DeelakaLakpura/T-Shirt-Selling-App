package com.dsmini.Shirtify.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.dsmini.Shirtify.Adapter.CartCustomAdapter;
import com.dsmini.Shirtify.Model.Order;
import com.dsmini.Shirtify.Model.Product;
import com.dsmini.Shirtify.Model.Report;
import com.dsmini.Shirtify.Model.Utils;
import com.dsmini.Shirtify.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.paperdb.Paper;

public class CheckOutActivity extends AppCompatActivity {

    private ImageView checkOutBackBtn;
    private TextView orderPrice, shipmentPrice, totalPayablePrice, checkOutBtn, streetAddress;
    private EditText usercomments;

    private ProgressDialog pd;
    private AlertDialog.Builder builder;

    private Order order;
    private ArrayList<Product> productArrayList;
    private CartCustomAdapter cartCustomAdapter;
    private RelativeLayout deliveryChargesLayout;

    private String street;
    private String comments;

    private RadioGroup radioGroup;
    String movement = "Delivery";
    private CardView addressCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        initAll();

        //alert dailog
        builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm");
        builder.setMessage("Are you sure?");


        //setting Up listeners
        OnClickListeners();

    }

    private void OnClickListeners() {
        checkOutBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //checkout logic goes here
        checkOutBtn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {

                street = streetAddress.getText().toString();
                comments = usercomments.getText().toString();

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog

                        if (order.getTotalPrice() > 0) {
                            order.setStatus("Pending");
                            try {
                                settingDataOnServer();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(CheckOutActivity.this, "No Item in Cart", Toast.LENGTH_SHORT).show();
                        }

                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();

            }
        });
    }

    private void settingDataOnServer() throws ParseException {
        pd.show(this, "Please Wait..", "Submitting order..");


        DatabaseReference root = FirebaseDatabase.getInstance().getReference().child("Order");
        String key = root.push().getKey();
        //id set
        order.setId(key);
        //date set
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        order.setDateOfOrder(currentDateTimeString);
        //total payabel set

        if (movement.equals("Delivery")) {
            order.setTotalPrice(order.getTotalPrice() + 10);
            order.setStreet(street);
        } else {
            order.setTotalPrice(order.getTotalPrice() + 10);
        }
        //setting address fields
        order.setComments(comments);
        order.setCustomerId(FirebaseAuth.getInstance().getCurrentUser().getUid());

        root.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(key).setValue(order).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                updateSellReport(order.getTotalPrice());


                order.getCartProductList().clear();
                order.setTotalPrice(0.0);
                Paper.book().delete("order");
                Paper.book().write("order", order);



            }
        });


    }

    private void updateSellReport(double totalPrice) {
        int alacarte = 0;
        int bento = 0;
        int handroll = 0;
        int beverage = 0;

        for (int i = 0; i < productArrayList.size(); i++) {
            String category = productArrayList.get(i).getCategory();
            if (category.equals("Alacarte")) {
                alacarte = alacarte + 1;
            } else if (category.equals("Bento")) {
                bento = bento + 1;
            } else if (category.equals("Handroll")) {
                handroll = handroll + 1;
            } else if (category.equals("Beverage")) {
                beverage = beverage + 1;
            }
        }

        Report report = new Report(alacarte, bento, handroll, beverage,totalPrice);
        DatabaseReference root = FirebaseDatabase.getInstance().getReference().child("Reports");
        String key = root.push().getKey();
        report.setId(key);


        DateFormat dateFormat = new SimpleDateFormat("MM");
        Date date = new Date();
        Log.d("Month", dateFormat.format(date));

        root.child(dateFormat.format(date)).child(key).setValue(report).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                productArrayList.clear();

                pd.dismiss();
                Toast.makeText(CheckOutActivity.this, "Order Submitted", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(CheckOutActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initAll() {

        //view
        checkOutBackBtn = findViewById(R.id.checkout_back_btn);
        addressCardView = findViewById(R.id.address_card_view);
        orderPrice = findViewById(R.id.checkout_order_price_view);
        shipmentPrice = findViewById(R.id.checkout_shipping_price_view);
        totalPayablePrice = findViewById(R.id.checkout_total_price_view);
        streetAddress = findViewById(R.id.checkout_address_view);
        usercomments = findViewById(R.id.checkout_comment_view);
        checkOutBtn = findViewById(R.id.checkout_btn);
        radioGroup = findViewById(R.id.radioGroup);
        deliveryChargesLayout = findViewById(R.id.delivery_charges_layout);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                movement = ((RadioButton) findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString();

                if (movement.equals("Delivery")) {
                    deliveryChargesLayout.setVisibility(View.VISIBLE);
                    totalPayablePrice.setText("RM " + new DecimalFormat("##.##").format(order.getTotalPrice() + 10));
                    addressCardView.setVisibility(View.VISIBLE);
                } else {
                    deliveryChargesLayout.setVisibility(View.GONE);
                    totalPayablePrice.setText("RM " + new DecimalFormat("##.##").format(order.getTotalPrice()));
                    addressCardView.setVisibility(View.GONE);
                }
            }
        });

        pd = new ProgressDialog(this);

        order = new Order();
        Intent intent = getIntent();
        order = (Order) intent.getSerializableExtra("order");

        productArrayList = new ArrayList<>();
        Intent i = getIntent();
        productArrayList = (ArrayList<Product>) i.getSerializableExtra(Utils.TAG_medicine_list);
        streetAddress.setText(order.getAddress());
        //setting values of prices
        //orderPrice.setText("Rs. "+ order.getTotalPrice());
        orderPrice.setText("RM " + new DecimalFormat("##.##").format(order.getTotalPrice()));
        // totalPayablePrice.setText("Rs."+ (order.getTotalPrice()+200));
        totalPayablePrice.setText("RM " + new DecimalFormat("##.##").format(order.getTotalPrice() + 10));

    }
}
