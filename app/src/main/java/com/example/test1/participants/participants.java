package com.example.test1.participants;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class participants extends AppCompatActivity {
    adapterparticipants adapter;
    RecyclerView rview;
    String code;
    ArrayList<modelparticipants>data;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_participants);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        data=new ArrayList<>();
        db=FirebaseFirestore.getInstance();
        rview= findViewById(R.id.rview);
        Intent intent = getIntent();
        rview.setLayoutManager(new LinearLayoutManager(this));
         code = intent.getStringExtra("code");
        getdata(code);
//        data.add(new modelparticipants("sa","sa","sa"));
//        data.add(new modelparticipants("ssad","saas","sasd"));
//
        adapter = new adapterparticipants(this,data);
        rview.setAdapter(adapter);

    }
    public void getdata(String code){
        db.collection("data").document("quiz").collection("I5H2").document("participant").collection("data")
                .orderBy("name")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                               data.add(new modelparticipants(document.getString("name"),document.getString("email"),""));
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }

}