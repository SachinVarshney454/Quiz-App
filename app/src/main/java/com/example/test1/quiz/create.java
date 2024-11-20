package com.example.test1.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.test1.R;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class create extends AppCompatActivity {
    FirebaseFirestore db;
    FirebaseAuth auth;
    EditText pin, weightage, author, subject;
    String a;
    String selectedTime, selectedDate;
    MaterialDatePicker<Long> datePicker;

    private Spinner numberOfQuestionsSpinner;
    private Button selectDateButton, selectTimeButton;
    private TextView selectedDateText, selectedTimeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        pin = findViewById(R.id.pin);
        weightage = findViewById(R.id.weightage);
        author = findViewById(R.id.author);
        subject = findViewById(R.id.subject);

        numberOfQuestionsSpinner = findViewById(R.id.number_of_questions_spinner);
        selectDateButton = findViewById(R.id.select_date_button);
        selectTimeButton = findViewById(R.id.select_time_button);
        selectedDateText = findViewById(R.id.selected_date_text);
        selectedTimeText = findViewById(R.id.selected_time_text);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.number_of_questions_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        numberOfQuestionsSpinner.setAdapter(adapter);

        // Set up the date picker
        selectDateButton.setOnClickListener(v -> {
            datePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select a date")
                    .build();
            datePicker.show(getSupportFragmentManager(), "DATE_PICKER");
            datePicker.addOnPositiveButtonClickListener(selection -> {
                selectedDate = datePicker.getHeaderText(); // Store selected date
                selectedDateText.setText("Selected Date: " + selectedDate);
            });
        });

        // Set up the time picker
        selectTimeButton.setOnClickListener(v -> {
            MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                    .setTitleText("Select Time")
                    .setHour(12)
                    .setMinute(0)
                    .build();

            timePicker.show(getSupportFragmentManager(), "TIME_PICKER");
            timePicker.addOnPositiveButtonClickListener(v1 -> {
                selectedTime = String.format("%02d:%02d", timePicker.getHour(), timePicker.getMinute()); // Store selected time
                selectedTimeText.setText("Selected Time: " + selectedTime);
            });
        });

        Button button = findViewById(R.id.button);
        button.setOnClickListener(v -> {
            String code = generateRandomCode().toUpperCase();
            int number;

            String pincode = pin.getText().toString();
            if (weightage.getText().toString().isEmpty()) {
                a = "1";
            } else {
                a = weightage.getText().toString();
            }
            if (Integer.parseInt(a) > 10 || Integer.parseInt(a) < 1) {
                Toast.makeText(create.this, "Weightage should be between 1 and 10", Toast.LENGTH_SHORT).show();
                return;
            }
            if (numberOfQuestionsSpinner.getSelectedItem() != null) {
                number = Integer.parseInt(numberOfQuestionsSpinner.getSelectedItem().toString());
            } else {
                Toast.makeText(create.this, "Please select a number of questions", Toast.LENGTH_SHORT).show();
                return;
            }

            Map<String, Object> user = new HashMap<>();
            user.put("number", String.valueOf(number));
            user.put("author", author.getText().toString());
            user.put("subject", subject.getText().toString());
            user.put("time",selectedTime);
            user.put("date",selectedDate);
            user.put("timestamp",convert(selectedDate,selectedTime));
            user.put("schedule",selectedDate+" "+selectedTime);

            db.collection("data").document("quiz").collection(code + pincode)
                    .document("data")
                    .set(user)
                    .addOnSuccessListener(aVoid -> {
                        Intent intent = new Intent(create.this, quizcreation.class);
                        intent.putExtra("number", number);
                        intent.putExtra("code", code);
                        intent.putExtra("pin", pincode);
                        intent.putExtra("time",selectedTime);
                        intent.putExtra("date",selectedDate);
                        intent.putExtra("author", author.getText().toString());
                        intent.putExtra("subject", subject.getText().toString());
                        startActivity(intent);
                    })
                    .addOnFailureListener(e -> Toast.makeText(create.this, "Error saving data", Toast.LENGTH_SHORT).show());
        });
    }

    private String generateRandomCode() {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            int randomIndex = (int) (Math.random() * alphabet.length());
            code.append(alphabet.charAt(randomIndex));
        }
        return code.toString();
    }

    public String convert(String selectedDate, String selectedTime) {
        try {
            // Combine the date and time into a single string
            String dateTime = selectedDate + " " + selectedTime;

            // Define the format of the input date and time
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

            // Parse the combined string into a Date object
            Date date = inputFormat.parse(dateTime);

            // Define the desired output format
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

            // Convert the Date object into the desired format string
            return outputFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Return null or handle the exception as needed
        }
    }
}