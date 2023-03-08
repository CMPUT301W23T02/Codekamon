package com.example.codekamon;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class leaderBoard extends AppCompatActivity {

    private TextView playerRank;
    private TextView playerScore;
    private ListView rankingList;
    private Spinner sortbyOption;
    private ArrayList<Player> playerArrayList = new ArrayList<>();
    private customPlayerRankArrayAdapter playerRankArrayAdapter;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);
        playerRank = findViewById(R.id.playerRank);
        playerScore = findViewById(R.id.playerScore);
        rankingList = findViewById(R.id.rankingList);
        sortbyOption = findViewById(R.id.sortbyOption);



        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String deviceID = bundle.getString("DEVICE_ID");



        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection("Players");

        collectionReference
                .orderBy("totalScore")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Player player = document.toObject(Player.class);
                                playerArrayList.add(player);

                                //Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        playerRankArrayAdapter = new customPlayerRankArrayAdapter(this,playerArrayList);

        rankingList.setAdapter(playerRankArrayAdapter);


        collectionReference.document(deviceID).get()
                .addOnSuccessListener(snapshot -> {
                    if (snapshot.exists()) {
                        Log.i(TAG, "Player found .");
                        Integer totalScore = Integer.parseInt(snapshot.get("totalScore").toString());
                        playerScore.setText("Score: " +totalScore.toString());

                    } else {
                        Log.i(TAG, "Failed to get Player from database.");
                    }
                })
                .addOnFailureListener(failure -> {
                    Log.i(TAG, "Failed to get Player from database.");
                });
    }


    }

