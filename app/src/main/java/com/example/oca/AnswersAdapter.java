package com.example.oca;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AnswersAdapter extends RecyclerView.Adapter<AnswersAdapter.AnswerVH> {

    List<Answers> answersList;

    public AnswersAdapter(List<Answers> answersList) {
        this.answersList = answersList;
    }

    @NonNull
    @Override
    public AnswersAdapter.AnswerVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        return new AnswerVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnswersAdapter.AnswerVH holder, int position) {
        Answers answers = answersList.get(position);
        holder.codeNameTxt.setText(answers.getQuestion());
        holder.answerTxt.setText(answers.getAnswer());

        boolean isExpandable = answersList.get(position).isExpandable();
        holder.expandableLayout.setVisibility(isExpandable ? View.VISIBLE : View.GONE);

    }

    @Override
    public int getItemCount() {
        return answersList.size();
    }

    public class AnswerVH extends RecyclerView.ViewHolder {

        TextView codeNameTxt, answerTxt, apiLevelTxt, descriptionTxt;
        LinearLayout linearLayout;
        RelativeLayout expandableLayout;

        public AnswerVH(@NonNull View itemView) {
            super(itemView);

            codeNameTxt = itemView.findViewById(R.id.Question);
            answerTxt = itemView.findViewById(R.id.Answer);
            linearLayout = itemView.findViewById(R.id.linear_layout);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Answers answers = answersList.get(getAdapterPosition());
                    answers.setExpandable(!answers.isExpandable());
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }
    }
}