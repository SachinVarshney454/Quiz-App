package com.example.test1.authentication;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.test1.MainActivity;
import com.example.test1.R;
import com.example.test1.databinder.holderemail;
import com.example.test1.databinder.holdername;
import com.example.test1.databinder.singeltonquizzes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class logicscreen extends AppCompatActivity {
    EditText email;
    EditText password;
    FirebaseAuth mauth;
    FirebaseFirestore db;
    Button login; Button signup;
    String em; String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.loginscreen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        mauth =FirebaseAuth.getInstance();
        email = findViewById(R.id.email);
        password=findViewById(R.id.password);
        login=findViewById(R.id.login);
        signup=findViewById(R.id.signup);

        db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if(user!=null){
            Intent intent = new Intent(logicscreen.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 em =email.getText().toString();
                 pass =password.getText().toString();
                 if(em.equals(null)&&pass.equals(null)){
                     Toast.makeText(logicscreen.this, "Please Enter Email And Password", Toast.LENGTH_SHORT).show();
                 }
                else  if(em==null){
                     Toast.makeText(logicscreen.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
                 }
               else  if(em==null){
                    Toast.makeText(logicscreen.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                }
                if(em!=null&&pass!=null){
                    signin(em,pass);

                }



            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(logicscreen.this, signup.class);
                startActivity(intent);



            }
        });



    }
    public void signin(String email,String password){
        mauth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            getdata(email);


                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mauth.getCurrentUser();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(logicscreen.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }
    public void getdata(String email){
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(email.equals(document.getString("email"))){
                                    holderemail.getInstance().setData(email);
                                    holdername.getInstance().setData(document.getString("name"));
                                    singeltonquizzes.getInstance().clearItems();
                                    Intent intent = new Intent(logicscreen.this,MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }


}