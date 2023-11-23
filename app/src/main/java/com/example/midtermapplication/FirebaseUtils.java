package com.example.midtermapplication;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseUtils {

    public interface OnObjectExistenceListener {
        void onObjectExistence(boolean exists);
    }

    public static void checkIfObjectExists(String node, String objectId, OnObjectExistenceListener listener) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(node);

        databaseReference.child(objectId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (listener != null) {
                    listener.onObjectExistence(dataSnapshot.exists());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
