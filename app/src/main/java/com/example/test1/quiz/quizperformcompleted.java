package com.example.test1.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.test1.MainActivity;
import com.example.test1.R;
import com.example.test1.databinder.holdername;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class quizperformcompleted extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quizperformcompleted);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String emailstr =user.getEmail();
        TextView name,email;
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        name.setText(holdername.getInstance().getData());
        email.setText(emailstr);
    }
    public void onBackPressed() {
        Intent intent = new Intent(quizperformcompleted.this, MainActivity.class);
        startActivity(intent);


    }
}