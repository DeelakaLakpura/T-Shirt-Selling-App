package com.dsmini.Shirtify.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dsmini.Shirtify.Activity.ProductDetailsActivity;
import com.dsmini.Shirtify.Admin.NewProductActivity;
import com.dsmini.Shirtify.Model.Product;
import com.dsmini.Shirtify.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.paperdb.Paper;

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
                Picasso.get().load(product.getPhotoUrl()).placeholder(R.drawable.loading).into(holder.productImg);
            }
        }
        holder.name.setText(product.getName());
        holder.price.setText("Rs." + product.getPrice());

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isAdmin) {
                    Intent intent = new Intent(context, ProductDetailsActivity.class);
                    intent.putExtra("product", product);
                    context.startActivity(intent);
                }
            }
        });


        String user = Paper.book().read("active", "user");

        if (!user.equals("user")) {
            holder.editBTn.setVisibility(View.VISIBLE);
            holder.delteBtn.setVisibility(View.VISIBLE);

            holder.editBTn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, NewProductActivity.class);
                    intent.putExtra("product", product);
                    context.startActivity(intent);
                }
            });

            holder.delteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myRootRef.child("Products").child(product.getProductId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(context, "Product removed Successfully", Toast.LENGTH_SHORT).show();
                            try {
                                myJokesList.remove(position);
                                notifyDataSetChanged();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("test", e.toString());
                        }
                    });
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return myJokesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout layout;
        ImageView productImg, delteBtn,editBTn;
        TextView name, price,loc;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.layout);
            productImg = itemView.findViewById(R.id.category_image);
            name = itemView.findViewById(R.id.product_brand_name);
            price = itemView.findViewById(R.id.price_tv);
            loc = itemView.findViewById(R.id.location);


        }
    }
}
