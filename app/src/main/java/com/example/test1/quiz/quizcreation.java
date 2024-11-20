package com.example.test1.quiz;

import static android.content.ContentValues.TAG;
import static android.view.View.GONE;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.test1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class quizcreation extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;
    RadioButton ra,rb,rc,rd;
    Button next,prev,finish;
    int num;    FirebaseFirestore db;
    int i=1;
    EditText a,b,c,d;TextView question;
    String choosen;
    int rdid;
    LinearLayout l1,l2,l3,l4;
    boolean cond;
    TextView count;
    Map<Integer,Integer> map;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quizcreation);

        // Set up window insets listener for padding adjustments
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //creating quiz




        //Intialize UI
        choosen = "";
        l1=findViewById(R.id.l1);
       l2=findViewById(R.id.l2);
      l3=findViewById(R.id.l3);
     l4=findViewById(R.id.l4);
//        setcolor();
        map = new HashMap<>();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        question = findViewById(R.id.question);
        next = findViewById(R.id.next);
        prev=findViewById(R.id.prev);
        finish=findViewById(R.id.finish);



        a=findViewById(R.id.a);
        b=findViewById(R.id.b);
        c=findViewById(R.id.c);
        d=findViewById(R.id.d);



        ra=findViewById(R.id.ra);
        rb=findViewById(R.id.rb);
        rc=findViewById(R.id.rc);
        rd=findViewById(R.id.rd);

        count=findViewById(R.id.count);




        //initialize radiobutton
        ra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setback();

                if(a.getText().toString().equals("")){
                    Toast.makeText(quizcreation.this, "Enter Answer", Toast.LENGTH_SHORT).show();
                    return;
                }
                ra.setChecked(true);
                rdid=ra.getId();
                if(a.getText().toString().equals("")){
                    choosen="a";
                }
                else
                choosen= a.getText().toString();

            }
        });
        rb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setback();

                if(b.getText().toString().equals("")){
                    Toast.makeText(quizcreation.this, "Enter Answer", Toast.LENGTH_SHORT).show();
                    return;
                }
                rb.setChecked(true);
                if(b.getText().toString().equals("")){
                    choosen="b";
                }
                else
                    choosen= b.getText().toString();
            }
        });
        rc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setback();

                if(c.getText().toString().equals("")){
                    Toast.makeText(quizcreation.this, "Enter Answer", Toast.LENGTH_SHORT).show();
                    return;
                }
                rc.setChecked(true);
                if(c.getText().toString().equals("")){
                    choosen="c";
                }
                else
                    choosen= c.getText().toString();
            }
        });
        rd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setback();

                if(d.getText().toString().equals("")){
                    Toast.makeText(quizcreation.this, "Enter Answer", Toast.LENGTH_SHORT).show();
                    return;
                }
                rd.setChecked(true);
                if(d.getText().toString().equals("")){
                    choosen="d";
                }
                else
                    choosen= d.getText().toString();
            }
        });




        //get data from activity
        Intent intent = getIntent();
        String code = intent.getStringExtra("code");
        String pincode = intent.getStringExtra("pin");
         num = intent.getIntExtra("number", 0);
        cond = false;
        
        
//button function
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                prevvisi();
                if(map.containsKey(++i)){
                    loadeddata(code,pincode);
                }
                else{
                    i--;
                    nextdata(code,pincode,"next",choosen);

                }
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousdata(code,pincode);
//                prevvisi();
            }
        });
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(quizcreation.this, "Quiz creation complete!", Toast.LENGTH_SHORT).show();
                privatedata(code,pincode);
                Intent intent = new Intent(quizcreation.this, quizcompleted.class);
                intent.putExtra("code",code);
                intent.putExtra("pin",pincode);
                String time =  intent.getStringExtra("time");
                String date = intent.getStringExtra("date");
                intent.putExtra("time",time);
                intent.putExtra("date",date);

                startActivity(intent);


            }
        });
        prev.setVisibility(GONE);
        count.setText(String.valueOf(i)+" of "+String.valueOf(num));


    }

    public void setback(){
       ra.setChecked(false);
       rb.setChecked(false);
       rc.setChecked(false);
       rd.setChecked(false);
    }
    String select;
    public void nextdata(String code, String pincode, String go, String choosen) {
        if (choosen.equals("")) {
            Toast.makeText(this, "Select the correct answer", Toast.LENGTH_SHORT).show();
            return;
        }
        if(i>1){
            prev.setVisibility(View.VISIBLE);
        }

        select = choosen;

        // Update UI for the last question
        if (i == num - 1) {
            next.setText("Submit");
        }

        // Prevent moving past the last question
        if (i > num) {
            next.setEnabled(false);
            i--;
            return;
        }



        map.put(i, i);
        count.setText(String.valueOf(i) + " of " + String.valueOf(num));

        // Prepare question data for Firestore
        Map<String, Object> questionData = new HashMap<>();
        questionData.put("question", question.getText().toString());
        questionData.put("a", a.getText().toString());
        questionData.put("b", b.getText().toString());
        questionData.put("c", c.getText().toString());
        questionData.put("d", d.getText().toString());
        questionData.put("selected", select);

        // Save question data to Firestore
        db.collection("data").document("quiz").collection(code + pincode).document(String.valueOf(i))
                .set(questionData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        // Clear fields after successful submission
                        if(i>=num){


                        }
                        else {
                            a.setText("");
                            b.setText("");
                            c.setText("");
                            d.setText("");
                            question.setText("");
                            setback();
                            prev.setVisibility(View.VISIBLE);
                        }

                        // Increment index and update display
                        i++;
                        if (i <= num) {
                            count.setText(String.valueOf(i) + " of " + String.valueOf(num));

                        }

                        // Show a message and disable "Next" when quiz is complete
                        if (i > num) {
                            Toast.makeText(quizcreation.this, "Quiz creation complete!", Toast.LENGTH_SHORT).show();
                            next.setEnabled(false);
                            i--;
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(quizcreation.this, "Error saving question. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }





        public void previousdata(String code,String pincode){
            i--;
        if(i==1){
            prevvisi();

        }
        else prev.setVisibility(View.VISIBLE);
        next.setEnabled(true);
        map.put(i,i);
            count.setText(i+" of "+num);
            next.setText("Next");
            next.setBackgroundColor(Color.parseColor("#9D00FF"));

            db.collection("data").document("quiz")
                    .collection(code + pincode)
                    .document(String.valueOf(i))
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    selected(document.getString("selected"));
                                    a.setText(document.getString("a"));
                                    b.setText(document.getString("b"));
                                    c.setText(document.getString("c"));
                                    d.setText(document.getString("d"));
                                    question.setText(document.getString("question"));
                                    String select = document.getString("selected");
                                    selected(select);
                                } else {
                                    Log.w(TAG, "No such document exists.");
                                    Toast.makeText(quizcreation.this, code+pincode+"   "+i, Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Log.w(TAG, "Error getting documents.", task.getException());
                            }
                        }
                    });

        }
        public void privatedata(String code,String pin){
        Map<String, Object> questionData = new HashMap<>();
        Intent intent=getIntent();
        String date = intent.getStringExtra("date");
        String time = intent.getStringExtra("time");
        String author= intent.getStringExtra("author");
        String subject= intent.getStringExtra("subject");
        questionData.put("date",date);
        questionData.put("time",time);
        questionData.put("author",author);
        questionData.put("subject",subject);
        questionData.put("code",code+pin);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
           String formattedTimestamp = sdf.format(new Date());
//        questionData.put(String.valueOf(i),answer);
            db.collection("personal")
                    .document(user.getEmail())
                    .collection("created")
                    .document(formattedTimestamp)
                    .set(questionData)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(quizcreation.this, "Error saving question. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    });

        }













    public void loadeddata(String code,String pin){
        count.setText(i+" of "+num);
        if(i>1){
            prev.setVisibility(View.VISIBLE);
        }

        db.collection("data").document("quiz").collection(code+pin).document(String.valueOf(i))
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                a.setText(document.getString("a"));
                                b.setText(document.getString("b"));
                                c.setText(document.getString("c"));
                                d.setText(document.getString("d"));
                                question.setText(document.getString("question"));
                                String select = document.getString("selected");
                                selected(select);
                            } else {
                               nextdata(code,pin,"","rr");
                            }
                        } else {
                            Log.w(TAG, "Error getting document.", task.getException());
                        }
                    }
                });
    }









    public void selected(String sel) {
        if (sel.equals("a") || sel.equals(a.getText().toString())) {
            setback();
            ra.setChecked(true);
        } else if (sel.equals("b") || sel.equals(b.getText().toString())) {
            setback();


            rb.setChecked(true);
        } else if (sel.equals("c") || sel.equals(c.getText().toString())) {
            setback();
            rc.setChecked(true);
        } else if (sel.equals("d") || sel.equals(d.getText().toString())) {
            setback();
            rd.setChecked(true);
        }
    }



public void prevvisi(){

    prev.setVisibility(GONE);


}

}