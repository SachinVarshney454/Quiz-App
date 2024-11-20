package com.example.test1.quiz;

import static android.content.ContentValues.TAG;
import static android.view.View.GONE;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.example.test1.databinder.holdername;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class quiz extends AppCompatActivity {
    FirebaseFirestore db;
    FirebaseUser user;
    FirebaseAuth auth;
    String useremail;
    String choosen;
    Button next,prev;
    String usercode;
    String userpin;
    TextView question,test,a,b,c,d,count,subname;
    RadioButton optiona;
    Map<Integer,String> map;
    RadioButton optionb;
    String strsubname;
    RadioButton optionc;
    RadioButton optiond;
    Button submit,finish;
    String author,subject,time,date;
    int i=0;
    String numdata,email;
    int numques;
    String formattedTimestamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quiz);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        //performing quiz







        prev=findViewById(R.id.prev);

        submit=findViewById(R.id.submit);
        subname= findViewById(R.id.subname);


        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        useremail=user.getEmail();
        finish=findViewById(R.id.finish);
        count= findViewById(R.id.count);
        Intent intent= getIntent();
        usercode=  intent.getStringExtra("code");
        userpin=  intent.getStringExtra("pin");
        db= FirebaseFirestore.getInstance();
        sendinfo(usercode,userpin);
        storepersonaldatatoauthor(usercode,userpin,useremail);



        optiona=findViewById(R.id.ra);
        optionb=findViewById(R.id.rb);
        optionc=findViewById(R.id.rc);
        optiond=findViewById(R.id.rd);
        a=findViewById(R.id.a);
        b=findViewById(R.id.b);
        c=findViewById(R.id.c);
        d=findViewById(R.id.d);
         map = new HashMap<>();
        optiona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setback();
                optiona.setChecked(true);
            }
        });
        optionb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setback();
                optionb.setChecked(true);
            }
        });
        optionc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setback();
                optionc.setChecked(true);
            }
        });
        optiond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setback();
                optiond.setChecked(true);
            }
        });
        question=findViewById(R.id.question);
        next=findViewById(R.id.next);
        otherdata(usercode,userpin);




        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextques(usercode,userpin,"next");
//                ques(usercode,userpin,"next",numques);

            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextques(usercode,userpin,"prev");
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(optiona.isChecked()){
                    choosen = a.getText().toString();

                }
                else if(optionb.isChecked()){
                    choosen = b.getText().toString();

                }
                else if(optionc.isChecked()){
                    choosen = c.getText().toString();
                }
                else if(optiond.isChecked()){
                    choosen = d.getText().toString();
                }
                else{
                    Toast.makeText(quiz.this, "Please check any answer", Toast.LENGTH_SHORT).show();
                    return;
                }
                submit.setText("Submitted");
                map.put(i,"yes");
                storedatatopersonal(choosen,usercode,userpin,useremail);
                storedatatoauthor(choosen,usercode,userpin,useremail);
            }
        });
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               submit.performClick();
               Intent intent1 = new Intent(quiz.this, quizperformcompleted.class);
               startActivity(intent1);
               finish();
            }
        });
    }


    public void otherdata(String code,String pin){
        db.collection("data").document("quiz").collection(code + pin).document("data")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                numdata = document.getString("number");
                                numques=Integer.parseInt(numdata);
                                strsubname= document.getString("subject");
                                subname.setText(strsubname);

                                count.setText(i+" "+"Of "+numques);
                                nextques(code,pin,"");

                            } else {
                                Log.d(TAG, "No such document.");
                            }
                        } else {
                            Log.w(TAG, "Error getting document.", task.getException());
                        }
                    }
                });
    }
   public void nextques(String usercode, String userpin,String ref) {

        if(ref.equals("prev")){
            i--;
        }
        else {
            i++;
        }
        if(map.containsKey(i)&&map.get(i).equals("yes")){
            submit.setText("submitted");
        }
        else{
            submit.setText("Submit");

        }
       count.setText(String.valueOf(i)+" "+"of"+" "+String.valueOf(numques));
        if(i==numques){
            next.setVisibility(GONE);

        }
        else if(i==1){
                prev.setVisibility(GONE);
        }
        if(i>1&&i<numques){
            next.setVisibility(View.VISIBLE);
            prev.setVisibility(View.VISIBLE);
        }



        db.collection("data").document("quiz").collection(usercode + userpin).document(String.valueOf(i))
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String questionText = document.getString("question");
                                question.setText(questionText);
                                a.setText(document.getString("a"));
                                b.setText(document.getString("b"));
                                c.setText(document.getString("c"));
                                d.setText(document.getString("d"));

                            } else {
                                i--;
                                next.setVisibility(GONE);
                            }
                        } else {
                            Log.w(TAG, "Error getting document.", task.getException());
                        }
                    }
                });

    }

//    public void storedatatopersonal(String choice,String usercode,String userpin,String email){
//        Map<String, Object> user = new HashMap<>();
//        user.put(String.valueOf(i), choice);
//
//        db.collection("personal").document(email).collection("attempted").document(formattedTimestamp)
//                .set(user, SetOptions.merge())
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Log.d(TAG, "Document successfully merged in storedatatopersonal()");
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w(TAG, "Error writing document", e);
//                    }
//                });
//    }
public void storedatatopersonal(String choice,String usercode,String userpin,String email){
    Map<String, Object> user = new HashMap<>();
    user.put(String.valueOf(i), choice);
// Specify a document within the "question" collection, for example using the question number (i) as the document ID
    db.collection("personal").document(email).collection("attempted")
            .document(formattedTimestamp).collection("question").document(String.valueOf(i))
            .set(user,SetOptions.merge())
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
}
    public void storedatatoauthor(String choice,String usercode,String userpin,String email){
        DocumentReference docRef = db.collection("data").document("quiz").collection(usercode+userpin).document("com/example/test1/participant").collection(email).document(String.valueOf(i));
        Map<String, Object> user = new HashMap<>();
        user.put("choice", choice);
        docRef.set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }


    public void storepersonaldatatoauthor(String code,String pin,String email){
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("email", email);
        user.put("name", holdername.getInstance().getData());

        db.collection("data").document("quiz").collection(code+pin).document("participant").collection("data")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    public void setback(){
        optiona.setChecked(false);
        optionb.setChecked(false);
        optionc.setChecked(false);
        optiond.setChecked(false);
    }
    public void sendinfo(String code,String pin){
        db.collection("data").document("quiz").collection(code + pin).document("data")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                author=document.getString("author");
                               subject=document.getString("subject");
                               time= document.getString("time");
                               date=document.getString("date");
                               senddata(code,pin,useremail,author,subject,time,date);


                            } else {

                            }
                        } else {
                            Log.w(TAG, "Error getting document.", task.getException());
                        }
                    }
                });

    }
    public void senddata(String code,String pin,String email,String author,String subject,String time,String date){
        Map<String, Object> user = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // Adjust the format as needed
        formattedTimestamp = sdf.format(new Date());
        user.put("code",usercode+userpin);
        user.put("time",time);
        user.put("date",date);
        user.put("author",author);
        user.put("subject",subject);
        db.collection("personal").document(email).collection("attempted").document(formattedTimestamp)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(quiz.this, "sdasad", Toast.LENGTH_SHORT).show();


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

    public void onBackPressed() {
        return;


    }


}