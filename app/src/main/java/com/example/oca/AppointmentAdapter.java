package com.example.oca;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder> {

    private static List<Appointment> appointmentList;
    CollectionReference ref;

    public AppointmentAdapter(List<Appointment> appointments) {
        appointmentList = appointments;
        ref = FirebaseFirestore.getInstance().collection("Patients").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Appointments");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointmentview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Appointment appointment = appointmentList.get(position);
        holder.bind(appointment);
    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        Button deleteApp;
        private TextView clinicNameTextView;
        private TextView dateTextView;
        private TextView timeTextView;
        private TextView detailTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            clinicNameTextView = itemView.findViewById(R.id.myclinic);
            dateTextView = itemView.findViewById(R.id.mydate);
            timeTextView = itemView.findViewById(R.id.mytime);
            detailTextView = itemView.findViewById(R.id.mydetail);
            deleteApp = itemView.findViewById(R.id.mydelete);
        }

        public void bind(Appointment appointment) {
            clinicNameTextView.setText(appointment.getClinicName());
            dateTextView.setText(appointment.getDate());
            timeTextView.setText(appointment.getTime());
            detailTextView.setText(appointment.getDetail());

            deleteApp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Remove appointment record from Firebase
                    // Remove appointment object from the list and notify adapter
                    int position = getAdapterPosition();
                    appointmentList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, appointmentList.size());
                    ref.document(appointment.getId()).delete();
                    Toast.makeText(itemView.getContext(), "Appointment Deleted", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
