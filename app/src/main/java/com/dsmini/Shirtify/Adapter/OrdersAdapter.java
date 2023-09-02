package com.dsmini.Shirtify.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.dsmini.Shirtify.Activity.OrderDetails;
import com.dsmini.Shirtify.Admin.OrderDetailsAdmin;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.dsmini.Shirtify.Model.Order;
import com.dsmini.Shirtify.R;

import java.util.ArrayList;

import io.paperdb.Paper;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Order> orderArrayList;
    DatabaseReference myRootRef;

    public OrdersAdapter(Context context, ArrayList<Order> orderArrayList) {
        this.context = context;
        this.orderArrayList = orderArrayList;
        myRootRef = FirebaseDatabase.getInstance().getReference();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_single_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Order order = orderArrayList.get(position);
        holder.status.setText(order.getStatus());
        if (order.getStatus().equals("Completed")) {
            holder.status.setTextColor(context.getResources().getColor(R.color.green));
        }
        else if (order.getStatus().equals("Processing")) {
            holder.status.setTextColor(context.getResources().getColor(R.color.quantum_yellow));
        }
        else if (order.getStatus().equals("Canceled")) {
            holder.status.setTextColor(context.getResources().getColor(R.color.red));
        }
        holder.date.setText(order.getDateOfOrder());
        holder.totalPrice.setText("RM" + order.getTotalPrice());


        String user = Paper.book().read("active", "user");

        holder.detailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //admin case
                if (!user.equals("user")){
                    Intent intent=new Intent(context, OrderDetailsAdmin.class);
                    intent.putExtra("order",order);
                    context.startActivity(intent);
                }
                //user case
                else{
                    Intent intent=new Intent(context, OrderDetails.class);
                    intent.putExtra("order",order);
                    context.startActivity(intent);
                }
            }
        });



        if (!user.equals("user")) {
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);
                    ViewGroup viewGroup = view.findViewById(android.R.id.content);
                    View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialoge_edit_order_status, viewGroup, false);

                    final Button processBtn = dialogView.findViewById(R.id.processing_btn);
                    final Button completedBtn = dialogView.findViewById(R.id.completed_btn);
                    final Button cencelBtn = dialogView.findViewById(R.id.cancel_btn);
                    final ProgressBar progressBar = dialogView.findViewById(R.id.dialoge_bar);
                    final ImageView closeBtn = dialogView.findViewById(R.id.close_btn);

                    builder.setView(dialogView);
                    final AlertDialog alertDialog = builder.create();

                    closeBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });
                    processBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            progressBar.setVisibility(View.VISIBLE);
                            holder.status.setTextColor(context.getResources().getColor(R.color.quantum_yellow));
                            holder.status.setText("Processing");
                            Log.d("testOrder",order.toString());
                            updateStatus("Processing", order.getCustomerId(), order.getId(), alertDialog, progressBar);
                        }
                    });
                    completedBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            progressBar.setVisibility(View.VISIBLE);
                            holder.status.setTextColor(context.getResources().getColor(R.color.green));
                            holder.status.setText("Completed");
                            updateStatus("Completed", order.getCustomerId(), order.getId(), alertDialog, progressBar);
                        }
                    });
                    cencelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            progressBar.setVisibility(View.VISIBLE);
                            holder.status.setTextColor(context.getResources().getColor(R.color.red));
                            holder.status.setText("Canceled");
                            updateStatus("Canceled", order.getCustomerId(), order.getId(), alertDialog, progressBar);
                        }
                    });
                    alertDialog.show();
                }
            });
        }


    }

    private void updateStatus(String stauts, String cutomerId, String orderId, AlertDialog alertDialog, ProgressBar progressBar) {
        myRootRef.child("Order").child(cutomerId).child(orderId).child("status").setValue(stauts).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(context, "Order status Changed..!", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                Log.d("test", e.toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView status, date, totalPrice;
        LinearLayout layout;
        Button detailsBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            status = itemView.findViewById(R.id.order_status);
            date = itemView.findViewById(R.id.date_of_order);
            totalPrice = itemView.findViewById(R.id.order_total_price);
            detailsBtn = itemView.findViewById(R.id.order_details);
            layout = itemView.findViewById(R.id.layout);
        }
    }


}
