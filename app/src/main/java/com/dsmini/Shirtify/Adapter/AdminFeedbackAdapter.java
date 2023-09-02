package com.dsmini.Shirtify.Adapter;

import android.content.Context;
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

import com.dsmini.Shirtify.Model.Feedback;
import com.dsmini.Shirtify.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdminFeedbackAdapter extends RecyclerView.Adapter<AdminFeedbackAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Feedback> orderArrayList;
    DatabaseReference myRootRef;

    public AdminFeedbackAdapter(Context context, ArrayList<Feedback> orderArrayList) {
        this.context = context;
        this.orderArrayList = orderArrayList;
        myRootRef = FirebaseDatabase.getInstance().getReference();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_single_feedback_admin, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Feedback feedback = orderArrayList.get(position);
        holder.status.setText(feedback.getStatus());
        if (feedback.getStatus().equals("Successful")) {
            holder.status.setTextColor(context.getResources().getColor(R.color.green));
        } else if (feedback.getStatus().equals("Processing")) {
            holder.status.setTextColor(context.getResources().getColor(R.color.quantum_yellow));
        }
        else if (feedback.getStatus().equals("Failed")) {
            holder.status.setTextColor(context.getResources().getColor(R.color.red));
        }
        holder.date.setText(feedback.getDate());
        holder.feedbackTv.setText(feedback.getText());
        holder.username.setText(feedback.getUsername());


        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);
                ViewGroup viewGroup = view.findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialoge_edit_feedback_status, viewGroup, false);

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

                completedBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressBar.setVisibility(View.VISIBLE);
                        holder.status.setText("Successful");
                        holder.status.setTextColor(context.getResources().getColor(R.color.green));
                        updateStatus("Successful", feedback.getUserId(), feedback.getId(), alertDialog, progressBar);
                    }
                });
                cencelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressBar.setVisibility(View.VISIBLE);
                        holder.status.setText("Failed");
                        holder.status.setTextColor(context.getResources().getColor(R.color.red));
                        updateStatus("Failed", feedback.getUserId(), feedback.getId(), alertDialog, progressBar);
                    }
                });
                alertDialog.show();
            }
        });
    }

    private void updateStatus(String stauts, String cutomerId, String orderId, AlertDialog alertDialog, ProgressBar progressBar) {
        myRootRef.child("Feedback").child(cutomerId).child(orderId).child("status").setValue(stauts).addOnSuccessListener(new OnSuccessListener<Void>() {
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
        TextView status, date, feedbackTv, username;
        LinearLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            status = itemView.findViewById(R.id.feedback_status);
            date = itemView.findViewById(R.id.date_of_order);
            feedbackTv = itemView.findViewById(R.id.feedback_tv);
            username = itemView.findViewById(R.id.username_tv);
            layout = itemView.findViewById(R.id.layout);
        }
    }


}
