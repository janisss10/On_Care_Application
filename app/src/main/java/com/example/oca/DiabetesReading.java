package com.example.oca;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiabetesReading extends AppCompatActivity {
    String[] items = {"Breakfast, before", "Breakfast, 2 hours after", "Lunch, before", "Lunch, 2 hours after", "Dinner, before", "Dinner, 2 hours after", "bedtime, before"};
    String[] options = {"Yes", "No"};

    AutoCompleteTextView autoCompleteTextView, autoCompleteTextView2;
    ArrayAdapter<String> adapterItems, adapterOptions;
    EditText date, time, diabetes;
    Button result, back;
    TextView analysis, analysis2;
    TableLayout tableLayout;
String diab_result;
    String chosen="0";
    String chose="0";
    float D;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diabetes_reading);
        date = findViewById(R.id.date_input);
        time = findViewById(R.id.time_input);
        diabetes = findViewById(R.id.diabetes_input);
        result = findViewById(R.id.result);
        back = findViewById(R.id.back);
        analysis = findViewById(R.id.analysis);
        analysis2 = findViewById(R.id.analysis2);
        autoCompleteTextView = findViewById(R.id.auto_complete_text);
        autoCompleteTextView2 = findViewById(R.id.auto_complete_text2);
        tableLayout = findViewById(R.id.tableLayout2);

        adapterItems = new ArrayAdapter<String>(this, R.layout.list_item, items);
        autoCompleteTextView.setAdapter(adapterItems);

        adapterOptions = new ArrayAdapter<String>(this,R.layout.list_item, options);
        autoCompleteTextView2.setAdapter(adapterOptions);


        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(DiabetesReading.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        date.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                },year, month, day);

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
                TimePickerDialog timePickerDialog = new TimePickerDialog(DiabetesReading.this, android.R.style.Theme_Holo_Dialog_MinWidth, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        time.setText(hourOfDay + ":" + minute);
                    }
                },hours,min,false);
                timePickerDialog.show();
            }
        });

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                chosen = item;
            }
        });

        autoCompleteTextView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String option = adapterView.getItemAtPosition(i).toString();
                chose = option;
            }
        });

        result.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View view) {

                                          if (date.getText().toString().isEmpty()) {
                                              Toast.makeText(DiabetesReading.this, "Please enter the date", Toast.LENGTH_SHORT).show();
                                          } else if (time.getText().toString().isEmpty()) {
                                              Toast.makeText(DiabetesReading.this, "Please enter the time", Toast.LENGTH_SHORT).show();
                                          } else if (autoCompleteTextView2.getText().toString().isEmpty()) {
                                              Toast.makeText(DiabetesReading.this, "Do you have diabetes?", Toast.LENGTH_SHORT).show();
                                          } else if (autoCompleteTextView.getText().toString().isEmpty()) {
                                              Toast.makeText(DiabetesReading.this, "Please select the correct time period", Toast.LENGTH_SHORT).show();
                                          } else if (diabetes.getText().toString().isEmpty()) {
                                              Toast.makeText(DiabetesReading.this, "Please enter the diabetes value", Toast.LENGTH_SHORT).show();
                                          } else {
                                              D = Float.parseFloat(diabetes.getText().toString());

                                              if (chose.equals("Yes") && (chosen.equals("Breakfast, before") || chosen.equals("Lunch, before") || chosen.equals("Dinner, before") || chosen.equals("Bedtime, before"))) {
                                                  if (D >= 4.0 && D <= 7.0) {
                                                      analysis.setText("Your blood glucose level is good!");
                                                      analysis2.setText("");
                                                      diab_result="Normal";
                                                  } else if (D < 4.0) {
                                                      analysis.setText("Your blood glucose level is low!");
                                                      analysis2.setText("Eat or drink (choose 1): \n - 1/2 cup juice or soft drink \n - 2-4 tsp honey or sugar \n - 3 sweets");
                                                      diab_result="low";
                                                  } else {
                                                      analysis.setText("Your blood glucose level is high!");
                                                      analysis2.setText("");
                                                      diab_result="high";
                                                  }
                                              }

                                              if (chose.equals("Yes") && (chosen.equals("Breakfast, 2 hours after") || chosen.equals("Lunch, 2 hours after") || chosen.equals("Dinner, 2 hours after"))) {
                                                  if (D >= 4.0 && D <= 10.0) {
                                                      analysis.setText("Your blood glucose level is good!");
                                                      analysis2.setText("");
                                                      diab_result="Normal";
                                                  } else if (D < 4.0) {
                                                      analysis.setText("Your blood glucose level is low!");
                                                      analysis2.setText("Eat or drink (choose 1): \n - 1/2 cup juice or soft drink \n - 2-4 tsp honey or sugar \n - 3 sweets");
                                                      diab_result="low";
                                                  } else {
                                                      analysis.setText("Your blood glucose level is high!");
                                                      analysis2.setText("");
                                                      diab_result="high";
                                                  }
                                              }

                                              if (chose.equals("No") && (chosen.equals("Breakfast, before") || chosen.equals("Lunch, before") || chosen.equals("Dinner, before") || chosen.equals("Bedtime, before"))) {
                                                  if (D >= 4.0 && D <= 6.0) {
                                                      analysis.setText("Your blood glucose level is good!");
                                                      analysis2.setText("");
                                                      diab_result="Normal";
                                                  } else if (D < 4.0) {
                                                      analysis.setText("Your blood glucose level is low!");
                                                      analysis2.setText("Eat or drink (choose 1): \n - 1/2 cup juice or soft drink \n - 2-4 tsp honey or sugar \n - 3 sweets");
                                                      diab_result="low";
                                                  } else {
                                                      analysis.setText("Your blood glucose level is high!");
                                                      analysis2.setText("");
                                                      diab_result="high";
                                                  }
                                              }
                                              if (chose.equals("No") && (chosen.equals("Breakfast, 2 hours after") || chosen.equals("Lunch, 2 hours after") || chosen.equals("Dinner, 2 hours after"))) {
                                                  if (D >= 4.0 && D <= 7.8) {
                                                      analysis.setText("Your blood glucose level is good!");
                                                      analysis2.setText("");
                                                      diab_result="Normal";
                                                  } else if (D < 4.0) {
                                                      analysis.setText("Your blood glucose level is low!");
                                                      analysis2.setText("Eat or drink (choose 1): \n - 1/2 cup juice or soft drink \n - 2-4 tsp honey or sugar \n - 3 sweets");
                                                      diab_result="low";
                                                  } else {
                                                      analysis.setText("Your blood glucose level is high!");
                                                      analysis2.setText("");
                                                      diab_result="high";
                                                  }
                                              }
                                          }

                                          FirebaseFirestore db = FirebaseFirestore.getInstance();
                                          Map<String, Object> data = new HashMap<>();
                                          data.put("Diabetes", autoCompleteTextView2.getText().toString());
                                          data.put("Readings_for", autoCompleteTextView.getText().toString());
                                          data.put("Reading", diabetes.getText().toString());
                                          data.put("Result", diab_result);
                                          data.put("Date", date.getText().toString());
                                          data.put("Time", time.getText().toString());
                                          data.put("User_ID", FirebaseAuth.getInstance().getUid());

                                          String recordID = db.collection("Patients").document(FirebaseAuth.getInstance().getUid()).collection("Diabetes_Records").document().getId(); // generate a new unique record ID

                                          db.collection("Patients").document(FirebaseAuth.getInstance().getUid()).collection("Diabetes_Records").document(recordID)
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

                                          tableLayout.removeAllViews(); // clear previous table contents
                                          tableLayout.setShowDividers(TableLayout.SHOW_DIVIDER_MIDDLE);
                                          tableLayout.setDividerDrawable(getResources().getDrawable(R.drawable.divider));

                                          db.collection("Patients").document(FirebaseAuth.getInstance().getUid()).collection("Diabetes_Records")
                                                  .whereEqualTo("User_ID", FirebaseAuth.getInstance().getUid())
                                                  .get()
                                                  .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                      @Override
                                                      public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                          if (task.isSuccessful()) {

                                                              List<DiabetesRecord> DiabetesRecords = new ArrayList<>();
                                                              List<String> dateValues = new ArrayList<>();
                                                              List<TableRow> tableRows = new ArrayList<>();
                                                              TableRow headerRow = new TableRow(getApplicationContext());
                                                              headerRow.setLayoutParams(new TableRow.LayoutParams(
                                                                      TableRow.LayoutParams.MATCH_PARENT,
                                                                      TableRow.LayoutParams.WRAP_CONTENT));
                                                              TextView dateTitleTextView = new TextView(getApplicationContext());
                                                              TextView timeTitleTextView = new TextView(getApplicationContext());
                                                              TextView diabetesTextView = new TextView(getApplicationContext());
                                                              TextView readingsForTextView = new TextView(getApplicationContext());
                                                              TextView diabetesReadingTextView = new TextView(getApplicationContext());
                                                              dateTitleTextView.setText("Date");
                                                              timeTitleTextView.setText("Time");
                                                              diabetesTextView.setText("Diabetes");
                                                              readingsForTextView.setText("Readings for");
                                                              diabetesReadingTextView.setText("Readings");
                                                              dateTitleTextView.setTypeface(null, Typeface.BOLD);
                                                              timeTitleTextView.setTypeface(null, Typeface.BOLD);
                                                              diabetesReadingTextView.setTypeface(null, Typeface.BOLD);
                                                              diabetesTextView.setTypeface(null, Typeface.BOLD);
                                                              readingsForTextView.setTypeface(null, Typeface.BOLD);
                                                              headerRow.addView(dateTitleTextView);
                                                              headerRow.addView(timeTitleTextView);
                                                              headerRow.addView(diabetesTextView);
                                                              headerRow.addView(readingsForTextView);
                                                              headerRow.addView(diabetesReadingTextView);
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
                                                                  String diabetes = document.get("Diabetes").toString();
                                                                  String readings_for = document.get("Readings_for").toString();
                                                                  float diabetes_readings = Float.parseFloat(document.get("Reading").toString());
                                                                  String time = document.get("Time").toString();
                                                                  dateValues.add(dateValue);

                                                                  DiabetesRecord DiabetesRecord = new DiabetesRecord(dateValue, time, diabetes, readings_for, diabetes_readings);
                                                                  DiabetesRecords.add(DiabetesRecord);
                                                              }
                                                              Collections.sort(DiabetesRecords);

                                                              for (DiabetesRecord DiabetesRecord : DiabetesRecords) {
                                                                  TableRow tableRow = new TableRow(getApplicationContext());
                                                                  tableRow.setLayoutParams(new TableRow.LayoutParams(
                                                                          TableRow.LayoutParams.MATCH_PARENT,
                                                                          TableRow.LayoutParams.WRAP_CONTENT));
                                                                  TextView dateTextView = new TextView(getApplicationContext());
                                                                  TextView timeTextView = new TextView(getApplicationContext());
                                                                  TextView readings = new TextView(getApplicationContext());
                                                                  TextView diabetes = new TextView(getApplicationContext());
                                                                  TextView readings_for = new TextView(getApplicationContext());
                                                                  dateTextView.setText(DiabetesRecord.getDate());
                                                                  timeTextView.setText(DiabetesRecord.getTime());
                                                                  diabetes.setText(String.valueOf(DiabetesRecord.getDiabetes()));
                                                                  readings_for.setText(String.valueOf(DiabetesRecord.getReadings_for()));
                                                                  readings.setText(String.valueOf(DiabetesRecord.getReading()));

                                                                  tableRow.addView(dateTextView);
                                                                  tableRow.addView(timeTextView);
                                                                  tableRow.addView(diabetes);
                                                                  tableRow.addView(readings_for);
                                                                  tableRow.addView(readings);
                                                                  tableRows.add(tableRow);
                                                              }

                                                              for (TableRow row : tableRows) {
                                                                  row.setShowDividers(TableRow.SHOW_DIVIDER_MIDDLE);
                                                                  row.setDividerDrawable(getResources().getDrawable(R.drawable.divider));
                                                                  tableLayout.addView(row);
                                                              }

                                                          }
                                                      }
                                                  });
                                      }
                                  });

                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(DiabetesReading.this, Journal.class);
                        startActivity(intent);
                    }
                });
    }
}



