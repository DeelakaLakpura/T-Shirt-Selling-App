package com.dsmini.Shirtify.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dsmini.Shirtify.Model.Order;
import com.dsmini.Shirtify.Model.Product;
import com.dsmini.Shirtify.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.paperdb.Paper;

public class CartCustomAdapter extends RecyclerView.Adapter<CartCustomAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Product> productArrayList;
    private Order order;

    public CartCustomAdapter(Context context, ArrayList<Product> productArrayList, Order order) {
        this.context = context;
        this.productArrayList = productArrayList;
        this.order = order;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_cart_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Product product = productArrayList.get(position);
        holder.name.setText(product.getName());
        holder.price.setText("RM"+product.getPrice());
        holder.quantity.setText(product.getQuantityInCart()+"");
        if (product.getPhotoUrl() != null) {
            if (!product.getPhotoUrl().equals("")) {
                Picasso.get().load(product.getPhotoUrl()).placeholder(R.drawable.icon).into(holder.image);
            }
        }

        holder.cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productArrayList.remove(product);
                notifyDataSetChanged();
                order.removeProduct(product);
                Paper.book().delete("order");
                Paper.book().write("order",order);

            }
        });


        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int q=Integer.parseInt(holder.quantity.getText().toString());
                order.addTotal(product);
                holder.quantity.setText(String.valueOf(++q));
                Paper.book().delete("order");
                Paper.book().write("order",order);
            }
        });
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int q=Integer.parseInt(holder.quantity.getText().toString());
                if(q!=1){
                    order.minusTotal(product);
                    holder.quantity.setText(String.valueOf(--q));
                    Paper.book().delete("order");
                    Paper.book().write("order",order);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView plus, minus;
        ImageView image,cross;
        TextView name, price, quantity;
        RelativeLayout parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            parent = itemView.findViewById(R.id.parent);
            image = itemView.findViewById(R.id.p_img);
            name = itemView.findViewById(R.id.product_name);
            price = itemView.findViewById(R.id.cart_price_view);
            quantity = itemView.findViewById(R.id.quantity_tv);
            plus = itemView.findViewById(R.id.plus_btn);
            minus = itemView.findViewById(R.id.minus_btn);
            cross = itemView.findViewById(R.id.img_delete);


        }
    }


}
