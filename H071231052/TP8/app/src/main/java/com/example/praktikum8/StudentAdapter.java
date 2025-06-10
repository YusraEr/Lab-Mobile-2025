package com.example.praktikum8;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class StudentAdapter  extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder>{
    private ArrayList<Student> students = new ArrayList<>();
    private final Activity activity;



    public StudentAdapter(Activity activity) {
        this.activity = activity;
    }
    private List<Student> studentsFull = new ArrayList<>();

    public void setStudents(ArrayList<Student> students) {
        this.students.clear();
        if (!students.isEmpty()) {
            this.students.addAll(students);
        }
        this.studentsFull = new ArrayList<>(students);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final TextView tvStatus;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false);
        return new StudentViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        holder.bind(students.get(position));
    }


    @Override
    public int getItemCount() {
        return students.size();
    }


    class StudentViewHolder extends RecyclerView.ViewHolder {
        final TextView tvName, tvNim, tvStatus;
        final CardView cardView;

        StudentViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_item_name);
            tvNim = itemView.findViewById(R.id.tv_item_nim);
            cardView = itemView.findViewById(R.id.card_view);
            tvStatus = itemView.findViewById(R.id.tv_item_status);
        }


        void bind(Student student) {
            tvName.setText(student.getName());
            tvNim.setText(student.getNim());
            if (student.getUpdatedAt() != null && !student.getUpdatedAt().isEmpty()) {
                tvStatus.setText("Update at " + student.getUpdatedAt());
            } else {
                tvStatus.setText("Created at " + student.getCreatedAt());
            }

            cardView.setOnClickListener(v -> {
                Intent intent = new Intent(activity, FormActivity.class);
                intent.putExtra(FormActivity.EXTRA_STUDENT, student);
                activity.startActivityForResult(intent, FormActivity.REQUEST_UPDATE);
            });
        }
    }

}