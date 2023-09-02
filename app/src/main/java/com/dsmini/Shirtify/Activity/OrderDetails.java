package com.dsmini.Shirtify.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dsmini.Shirtify.Model.Order;
import com.dsmini.Shirtify.Model.Product;
import com.dsmini.Shirtify.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OrderDetails extends AppCompatActivity {

    private TextView totalPrice,status,date,address;

    private  ProductsAdapter mAdapter;
    private  RecyclerView recyclerView;

    Order order;

    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        totalPrice=findViewById(R.id.total_price);
        status=findViewById(R.id.status);
        date=findViewById(R.id.date);
        address=findViewById(R.id.address);
        recyclerView=findViewById(R.id.product_list);
        backBtn = findViewById(R.id.detaisl_backBtn);


        order= (Order) getIntent().getSerializableExtra("order");

        if(order!=null){
            totalPrice.setText("Total Price :Rm "+order.getTotalPrice());
            status.setText("Status :"+order.getStatus());
            date.setText("Date: "+order.getDateOfOrder());
            address.setText("Address: "+order.getAddress());

            mAdapter = new ProductsAdapter(order.getCartProductList(), this, false);
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            recyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.MyViewHolder> {

        List<Product> myJokesList;
        Activity context;
        boolean isAdmin;
        DatabaseReference myRootRef;


        public ProductsAdapter(List<Product> usersList, Activity context, boolean isAdmin) {
            this.myJokesList = usersList;
            this.context = context;
            this.isAdmin = isAdmin;
            myRootRef = FirebaseDatabase.getInstance().getReference();
        }

        @NonNull
        @Override
        public ProductsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.single_product_layout, parent, false);

            return new ProductsAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ProductsAdapter.MyViewHolder holder, int position) {

            Product product = myJokesList.get(position);

            if (product.getPhotoUrl() != null) {
                if (!product.getPhotoUrl().equals("")) {
                    holder.productImg.setVisibility(View.VISIBLE);
                    Picasso.get().load(product.getPhotoUrl()).placeholder(R.drawable.logo).into(holder.productImg);
                }
            }
            holder.name.setText(product.getName());
            holder.price.setText("RM" + product.getPrice());
        }

        @Override
        public int getItemCount() {
            return myJokesList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            LinearLayout layout;
            ImageView productImg;
            TextView name, price;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                layout = itemView.findViewById(R.id.layout);
                productImg = itemView.findViewById(R.id.category_image);
                name = itemView.findViewById(R.id.product_brand_name);
                price = itemView.findViewById(R.id.price_tv);
            }
        }
    }
}