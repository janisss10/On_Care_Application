package com.example.oca;


import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClinicFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class ClinicFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ListView listofClinic;
    private TextView nameC, locC, addC, hoursC;
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final FirebaseUser currentUser = mAuth.getCurrentUser();
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private ImageView imageView;
    private Button bookApp,myApp;




    public ClinicFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ClinicFragment.
     */

    // TODO: Rename and change types and number of parameters
    public static ClinicFragment newInstance(String param1, String param2) {
        ClinicFragment fragment = new ClinicFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_clinic_fragment, container, false);

        listofClinic = (ListView) view.findViewById(R.id.listallclinics);
        bookApp = (Button) view.findViewById(R.id.appBook);
        myApp = (Button) view.findViewById(R.id.appMy);
//
        //imageView = view.findViewById(R.id.imagec);

        /*
        fStore.collection("Clinic")
                .orderBy("Name")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            final ArrayList<HashMap<String, Object>> Clin = new ArrayList<HashMap<String, Object>>();
                            final SimpleAdapter adapter = new SimpleAdapter(getActivity(), Clin, R.layout.activity_clinic_view,
                                    new String[]{"image","name", "location", "add", "operating hours"};
                                    new int[]{R.id.imagec,R.id.cname, R.id.clocc, R.id.cadd, R.id.copp});
                            listofClinic.setAdapter(adapter);
                            for (final QueryDocumentSnapshot document : task.getResult()) {
                                String url = document.getData().get("Image").toString();

                                Picasso.get().load(url).into(new Target() {
                                    @Override
                                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                        HashMap<String, Object> clinic = new HashMap<String, Object>();
                                        clinic.put("image", bitmap);
                                        clinic.put("name", document.getData().get("Name").toString());
                                        clinic.put("add", "Address: " + document.getData().get("Address").toString());
                                        clinic.put("location", "Location: " + document.getData().get("Location").toString());
                                        clinic.put("operating hours", "Operating Hours: " + document.getData().get("Operating Hours").toString());
                                        Clin.add(clinic);

                                        adapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                                            @Override
                                            public boolean setViewValue(View view, Object data, String textRepresentation) {
                                                if((view instanceof ImageView) & (data instanceof Bitmap)){
                                                    ImageView iv = (ImageView) view;
                                                    Bitmap bm = (Bitmap) data;
                                                    iv.setImageBitmap(bm);
                                                    return true;
                                                }
                                                return false;
                                            }
                                        });
                                    }

                                    @Override
                                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                                    }

                                    @Override
                                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                                    }
                                });
                            }
                        }
                        else { Toast.makeText(getActivity(), "Error retrieving Clinics", Toast.LENGTH_LONG).show();
                        }
                    }
                });

        */

        bookApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getActivity(), BookAppointmentActivity.class);
                startActivity(myIntent);

            }
        });

        myApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getActivity(), MyAppointmentActivity.class);
                startActivity(myIntent);
            }
        });


        return view;
    }
}