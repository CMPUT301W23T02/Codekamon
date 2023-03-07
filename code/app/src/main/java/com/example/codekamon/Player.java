package com.example.codekamon;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Player implements Serializable {
    String userName;
    String email;
    HashMap<String,String> playerCodes;
    Integer highestScore;
    Integer lowestScore;
    Integer totalScore;
    Integer numScanned;
    String androidId;
    FirebaseFirestore db;


    public Player() {

    }

    public Player(String userName, String email, String androidId) {
        this.userName = userName;
        this.email = email;
        this.androidId = androidId;
        this.highestScore = 0;
        this.lowestScore = -1;
        this.totalScore = 0;
        this.numScanned = 0;
        this.playerCodes = new HashMap<>();
    }

    public void saveToDatabase() {
        // create Firestore collection
        final String TAG = "Error";
        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
        CollectionReference collectionReference =
                db.collection("Players");

        collectionReference.document(androidId)
                .set(this)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // These are a method which gets executed when the task is succeeded
                        Log.d(TAG,"Data has been added successfully!");
                    } // end onSuccess
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // These are a method which gets executed if there’s any problem
                        Log.d(TAG,"Data could not be added!" + e.toString());
                    } // end onSuccess
                });
    } // end saveToDatabase



    public Boolean addQR(QRCode code){
        String name = code.getName();
        String id = code.getContent();
        int score = code.getScore();
        if(playerCodes.containsValue(id)){
            return false;
        }

        if(score > highestScore){
            highestScore = score;
        }
        if(score < lowestScore || lowestScore == -1){
            lowestScore = score;
        }
        totalScore += score;
        playerCodes.put(name, id);
        numScanned++;
        updateDatabase();
        return true;
    }


    /**
     * This updates the database with the data contained in the current player object
     */
    public void updateDatabase() {
        // Create Firestore collection
        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference =
                db.collection("Players");
        DocumentReference myAccountRef = collectionReference.document(androidId);

        myAccountRef
                .update("playerCodes", playerCodes);
        myAccountRef
                .update("numScanned", numScanned);
        myAccountRef
                .update("totalScore", totalScore);
        myAccountRef
                .update("highestScore", highestScore);
        myAccountRef
                .update("lowestScore", lowestScore);

    } //
    public Boolean deleteQR(QRCode code){
        String name = code.getName();
        if(!playerCodes.containsKey(name)){
            return false;
        }
        // TODO: change highest/lowest score if this is that code
        totalScore -= code.getScore();
        playerCodes.remove(name);
        numScanned--;
        return true;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getHighestScore() {
        return highestScore;
    }

    public void setHighestScore(Integer highestScore) {
        this.highestScore = highestScore;
    }

    public Integer getLowestScore() {
        return lowestScore;
    }

    public void setLowestScore(Integer lowestScore) {
        this.lowestScore = lowestScore;
    }

    public Integer getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }

    public Integer getNumScanned() {
        return numScanned;
    }

    public void setNumScanned(Integer numScanned) {
        this.numScanned = numScanned;
    }

    public String getAndroidId() {
        return androidId;
    }

    public void setAndroidId(String androidId){
        this.androidId = androidId;
    }

    public HashMap<String, String> getPlayerCodes() {
        return playerCodes;
    }
}
