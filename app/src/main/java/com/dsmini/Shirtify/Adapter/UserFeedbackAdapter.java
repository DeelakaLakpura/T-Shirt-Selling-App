package com.dsmini.Shirtify.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dsmini.Shirtify.Model.Feedback;
import com.dsmini.Shirtify.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UserFeedbackAdapter extends RecyclerView.Adapter<UserFeedbackAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Feedback> orderArrayList;
    DatabaseReference myRootRef;

    public UserFeedbackAdapter(Context context, ArrayList<Feedback> orderArrayList) {
        this.context = context;
        this.orderArrayList = orderArrayList;
        myRootRef = FirebaseDatabase.getInstance().getReference();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_single_feedback, parent, false);
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


    }


    @Override
    public int getItemCount() {
        return orderArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView status, date, feedbackTv;
        LinearLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            status = itemView.findViewById(R.id.feedback_status);
            date = itemView.findViewById(R.id.date_of_order);
            feedbackTv = itemView.findViewById(R.id.feedback_tv);
            layout = itemView.findViewById(R.id.layout);
        }
    }


}
