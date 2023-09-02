package com.dsmini.Shirtify.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.dsmini.Shirtify.Adapter.CartCustomAdapter;
import com.dsmini.Shirtify.Model.Order;
import com.dsmini.Shirtify.Model.Product;
import com.dsmini.Shirtify.Model.Utils;
import com.dsmini.Shirtify.R;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;

import io.paperdb.Paper;

public class CartActivity extends AppCompatActivity {

    public static TextView totalPriceView;
    private ImageView deletCart,cartBackArrow;
    CardView checkOut;
    private Order order;
    private RecyclerView cartRecyclerView;
    private CartCustomAdapter cartCustomAdapter;
    private ArrayList<Product> productArrayList;
    public static double totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        initAll();
        clickListener();
    }



    private void clickListener() {
        deletCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                order.getCartProductList().clear();
                order.setTotalPrice(0);
                productArrayList.clear();
                Paper.book().delete("order");
                Paper.book().write("order",order);
                totalPriceView.setText("0.0");
                cartCustomAdapter.notifyDataSetChanged();
            }
        });

        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(order.getTotalPrice()>0){
                    Intent intent=new Intent(CartActivity.this,EventLocationActivity.class);
                    intent.putExtra("order", (Serializable) order);
                    intent.putExtra(Utils.TAG_medicine_list, productArrayList);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(CartActivity.this, "Cart is Empty", Toast.LENGTH_SHORT).show();
                }

            }
        });
        cartBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initAll() {
        order=new Order();
        productArrayList =new ArrayList<>();
        totalPriceView=findViewById(R.id.cart_total_price_view);
        deletCart=findViewById(R.id.delete_cart_imageView);
        cartBackArrow=findViewById(R.id.cart_back_arrow);

        if(Paper.book().read("order")!=null){
            order=Paper.book().read("order");
            for(int i = 0; i<order.getCartProductList().size(); i++){
                Log.d("productNames",order.getCartProductList().get(i).getName() + "  "+i);
            }
            productArrayList =(ArrayList<Product>) order.getCartProductList().clone();
           // Double price=Double.toString());
            totalPriceView.setText(new DecimalFormat("##.##").format(order.getTotalPrice()));
        }
        cartRecyclerView=findViewById(R.id.cart_recyclerview);
        cartCustomAdapter=new CartCustomAdapter(CartActivity.this, productArrayList,order);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartRecyclerView.setNestedScrollingEnabled(false);
        cartRecyclerView.setAdapter(cartCustomAdapter);
        checkOut=findViewById(R.id.check_out_btn);

    }

}
