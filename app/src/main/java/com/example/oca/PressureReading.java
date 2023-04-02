package com.example.oca;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PressureReading extends AppCompatActivity {

    Button result, back;
    EditText date, time, dia, sys;
    int D, S;
    TextView analysis, analysis2;
    LineChart lineChart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pressure_reading);
        result = findViewById(R.id.result);
        back = findViewById(R.id.back);
        date = findViewById(R.id.date_input);
        time = findViewById(R.id.time_input);
        dia = findViewById(R.id.diastolic_input);
        sys = findViewById(R.id.systolic_input);
        analysis = findViewById(R.id.analysis);
        analysis2 = findViewById(R.id.analysis2);
        lineChart = findViewById(R.id.lineChart);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getAxisRight().setDrawGridLines(false);
        lineChart.setDrawGridBackground(false);
        TableLayout tableLayout = findViewById(R.id.tableLayout);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(PressureReading.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        date.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);

                datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
                datePickerDialog.show();
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int hours = calendar.get(Calendar.HOUR);
                int min = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(PressureReading.this, android.R.style.Theme_Holo_Dialog_MinWidth, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        time.setText(hourOfDay + ":" + minute);
                    }
                }, hours, min, false);
                timePickerDialog.show();
            }
        });

        result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tableLayout.removeAllViews(); // clear previous table contents
                tableLayout.setShowDividers(TableLayout.SHOW_DIVIDER_MIDDLE);
                tableLayout.setDividerDrawable(getResources().getDrawable(R.drawable.divider));

// Iterate over the Firebase data and create a TableRow for each record

                if (date.getText().toString().isEmpty()) {
                    Toast.makeText(PressureReading.this, "Please enter the date", Toast.LENGTH_SHORT).show();
                } else if (time.getText().toString().isEmpty()) {
                    Toast.makeText(PressureReading.this, "Please enter the time", Toast.LENGTH_SHORT).show();
                } else if (sys.getText().toString().isEmpty()) {
                    Toast.makeText(PressureReading.this, "Please enter your systolic value", Toast.LENGTH_SHORT).show();
                } else if (dia.getText().toString().isEmpty()) {
                    Toast.makeText(PressureReading.this, "Please enter your diastolic value", Toast.LENGTH_SHORT).show();
                } else {
                    S = Integer.parseInt(sys.getText().toString());
                    D = Integer.parseInt(dia.getText().toString());

                    if (S < 90 || D < 60) {
                        analysis.setText("Hypotension");
                        analysis2.setText("Are you feeling alright? Your blood pressure is low. If you are unwell, see your doctor now." +
                                " Retake your blood pressure in 5 minutes. See your doctor if it remains low.");
                    } else if (S >= 90 && S < 120 || D >= 60 && D < 80) {
                        analysis.setText("Normal");
                        analysis2.setText("Keep up the good work! Your blood pressure is good.");
                    } else if (S >= 120 && S < 140 || D >= 80 && D < 90) {
                        analysis.setText("At risk, pre-hypertension");
                        analysis2.setText("Are you feeling alright? Your blood pressure is slightly high. If you are unwell, see your doctor now." +
                                " Retake your blood pressure in 5 minutes. See your doctor if it remains high.");
                    } else {
                        analysis.setText("high blood pressure, hypertension");
                        analysis2.setText("Are you feeling alright? Your blood pressure is high. If you are unwell, see your doctor now." +
                                " Retake your blood pressure in 5 minutes. See your doctor if it remains high.");
                    }
                }

                // Access a Cloud Firestore instance from your Activity
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Map<String, Object> data = new HashMap<>();
                data.put("Systolic_BP", S);
                data.put("Diastolic_BP", D);
                data.put("Date", date.getText().toString());
                data.put("Time", time.getText().toString());
                data.put("User_ID", FirebaseAuth.getInstance().getUid());

                String recordID = db.collection("Patients").document(FirebaseAuth.getInstance().getUid()).collection("BP_Records").document().getId(); // generate a new unique record ID

                db.collection("Patients").document(FirebaseAuth.getInstance().getUid()).collection("BP_Records").document(recordID)
                        .set(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_LONG).show();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
                            }
                        });

                // display chart

                XAxis xAxis = lineChart.getXAxis();
                xAxis.setLabelRotationAngle(-45f); // Rotate the labels by 45 degrees
                lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

                db.collection("Patients").document(FirebaseAuth.getInstance().getUid()).collection("BP_Records")
                        .whereEqualTo("User_ID", FirebaseAuth.getInstance().getUid())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    List<Entry> entries = new ArrayList<>();
                                    List<Entry> entries2 = new ArrayList<>();
                                    List<String> xAxisValues = new ArrayList<>();
                                    List<Float> yAxisValues = new ArrayList<>();
                                    List<Float> yAxisValues2 = new ArrayList<>();
                                    List<BPRecord> BPRecords = new ArrayList<>();
                                    List<String> dateValues = new ArrayList<>();
                                    List<TableRow> tableRows = new ArrayList<>();
                                    TableRow headerRow = new TableRow(getApplicationContext());
                                    headerRow.setLayoutParams(new TableRow.LayoutParams(
                                            TableRow.LayoutParams.MATCH_PARENT,
                                            TableRow.LayoutParams.WRAP_CONTENT));
                                    TextView dateTitleTextView = new TextView(getApplicationContext());
                                    TextView timeTitleTextView = new TextView(getApplicationContext());
                                    TextView systolicBPTitleTextView = new TextView(getApplicationContext());
                                    TextView diastolicBPTitleTextView = new TextView(getApplicationContext());
                                    dateTitleTextView.setText("Date");
                                    timeTitleTextView.setText("Time");
                                    systolicBPTitleTextView.setText("Systolic BP");
                                    diastolicBPTitleTextView.setText("Diastolic BP");
                                    dateTitleTextView.setTypeface(null, Typeface.BOLD);
                                    timeTitleTextView.setTypeface(null, Typeface.BOLD);
                                    systolicBPTitleTextView.setTypeface(null, Typeface.BOLD);
                                    diastolicBPTitleTextView.setTypeface(null, Typeface.BOLD);
                                    headerRow.addView(dateTitleTextView);
                                    headerRow.addView(timeTitleTextView);
                                    headerRow.addView(systolicBPTitleTextView);
                                    headerRow.addView(diastolicBPTitleTextView);
                                    TableRow borderRow = new TableRow(getApplicationContext());
                                    borderRow.setBackgroundColor(Color.BLACK);
                                    borderRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 2));
                                    tableLayout.addView(borderRow);
                                    tableLayout.addView(headerRow);
                                    GradientDrawable border = new GradientDrawable();
                                    border.setStroke(5, Color.BLACK);

// Set the background drawable to the TableRow
                                    tableLayout.setBackground(border);
                                    tableLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
                                    tableLayout.setDividerDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.vertical_divider));

                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String dateValue = document.get("Date").toString();
                                        String timeValue = document.get("Time").toString();
                                        float systolicBP = Float.parseFloat(document.get("Systolic_BP").toString());
                                        float diastolicBP = Float.parseFloat(document.get("Diastolic_BP").toString());
                                        String time = document.get("Time").toString();
                                        dateValues.add(dateValue);

                                        BPRecord BPRecord = new BPRecord(dateValue, time, systolicBP, diastolicBP);
                                        BPRecords.add(BPRecord);
                                    }
                                    Collections.sort(BPRecords);

                                    for (BPRecord BPRecord : BPRecords) {
                                        TableRow tableRow = new TableRow(getApplicationContext());
                                        tableRow.setLayoutParams(new TableRow.LayoutParams(
                                                TableRow.LayoutParams.MATCH_PARENT,
                                                TableRow.LayoutParams.WRAP_CONTENT));
                                        TextView dateTextView = new TextView(getApplicationContext());
                                        TextView timeTextView = new TextView(getApplicationContext());
                                        TextView systolicBPTextView = new TextView(getApplicationContext());
                                        TextView diastolicBPTextView = new TextView(getApplicationContext());
                                        dateTextView.setText(BPRecord.getDate());
                                        timeTextView.setText(BPRecord.getTime());
                                        systolicBPTextView.setText(String.valueOf(BPRecord.getSystolicBP()));
                                        diastolicBPTextView.setText(String.valueOf(BPRecord.getDiastolicBP()));

                                        tableRow.addView(dateTextView);
                                        tableRow.addView(timeTextView);
                                        tableRow.addView(systolicBPTextView);
                                        tableRow.addView(diastolicBPTextView);
                                        tableRows.add(tableRow);
                                    }

                                    for (TableRow row : tableRows) {
                                        row.setShowDividers(TableRow.SHOW_DIVIDER_MIDDLE);
                                        row.setDividerDrawable(getResources().getDrawable(R.drawable.divider));
                                        tableLayout.addView(row);
                                    }

                                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                                    for (BPRecord r : BPRecords) {
                                        Date date = null;
                                        try {
                                            date = dateFormat.parse(r.getDate());
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        xAxisValues.add(dateFormat.format(date));
                                        yAxisValues.add(r.getSystolicBP());
                                        yAxisValues2.add(r.getDiastolicBP());
                                    }

                                    // Create Entry objects and add them to the corresponding lists
                                    for (int i = 0; i < dateValues.size(); i++) {
                                        float yValue = yAxisValues.get(i);
                                        float yValue2 = yAxisValues2.get(i);
                                        String xValue = dateValues.get(i);
                                        entries.add(new Entry(i, yValue));
                                        entries2.add(new Entry(i, yValue2));
                                    }

                                    // Set up the appearance of the chart

                                    xAxis.setValueFormatter(new ValueFormatter() {
                                        private final SimpleDateFormat mFormat = new SimpleDateFormat("dd MM yyyy", Locale.ENGLISH);

                                        @Override
                                        public String getFormattedValue(float value) {
                                            int index = (int) value;
                                            if (index < 0 || index >= xAxisValues.size()) {
                                                return "";
                                            } else {
                                                String dateStr = xAxisValues.get(index);
                                                try {
                                                    Date date = dateFormat.parse(dateStr);
                                                    return mFormat.format(date);
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                    return "";
                                                }
                                            }
                                        }
                                    });

                                    LineDataSet dataSet = new LineDataSet(entries, "Systolic BP");
                                    LineDataSet dataSet2 = new LineDataSet(entries2, "Diastolic BP");
                                    dataSet.setColor(Color.MAGENTA);
                                    dataSet.setLineWidth(1f);
                                    dataSet.setValueTextSize(10f);
                                    dataSet.setValueTextColor(Color.BLUE);
                                    dataSet.setDrawCircleHole(false);
                                    dataSet.setCircleColor(Color.BLUE);
                                    dataSet2.setColor(Color.GREEN);
                                    dataSet2.setLineWidth(1f);
                                    dataSet2.setValueTextSize(10f);
                                    dataSet2.setValueTextColor(Color.BLUE);
                                    dataSet2.setDrawCircleHole(false);
                                    dataSet2.setCircleColor(Color.BLUE);
                                    LineData lineData = new LineData(dataSet, dataSet2);
                                    XAxis xAxis = lineChart.getXAxis();
                                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                                    xAxis.setGranularity(1f);

                                    // Set the chart data and refresh the chart
                                    lineChart.setData(lineData);
                                    lineChart.setDrawGridBackground(false);

                                    lineChart.invalidate();
                                }
                            }


                        });


            }


        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PressureReading.this, Journal.class);
                startActivity(intent);
            }
        });
    }
}