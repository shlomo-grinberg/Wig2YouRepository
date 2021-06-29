package com.example.wig2you.Model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.List;

public class ModelFirebase {
    final static String usersCollection = "users";
    final static String wigsCollection = "wigs";

    private ModelFirebase() {
    }

    public interface GetAllUsersListener {
        public void onComplete(List<User> users);
    }

    public interface GetAllWigsListener {
        public void onComplete(List<Wig> wigs);
    }

    //users
    public static void getAllUsers(GetAllUsersListener listener) {

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection(usersCollection)
                    .get()
                    .addOnCompleteListener(task -> {
                        List<User> list = new LinkedList<User>();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                User user = User.create(document.getData());
                                if(user.isAvailable()){
                                    list.add(user);
                                }
                            }
                        } else {}
                        listener.onComplete(list);
                    });


    }
    public static void saveUser(User user ,Model.OnCompleteListener listener) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(user.email, user.password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                user.setId(firebaseUser.getUid());
                save(user,()->listener.onComplete());
            }
        });
    }

    private static void save(User user, Model.OnCompleteListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(usersCollection).document(user.getId())
                .set(user.toJson())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        listener.onComplete();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onComplete();
                    }
                });
    }


    //wigs
    public static void getAllWigs(GetAllWigsListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(wigsCollection)
                .get()
                .addOnCompleteListener(task -> {
                    List<Wig> list = new LinkedList<Wig>();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Wig wig = Wig.create(document.getData());
                            if(wig.isAvailable()){
                                list.add(wig);
                            }
                        }
                    } else {}
                    listener.onComplete(list);
                });
    }

    public static void saveWig(Wig wig ,Model.OnCompleteListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(wigsCollection).document(wig.getStyle()).set(wig.toJson())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        listener.onComplete();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onComplete();
            }
        });
    }

    public static void signOut() {
        FirebaseAuth.getInstance().signOut();
    }

    public static void login(String email, String password, Model.OnCompleteLogInListener listener) {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email , password)
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            db.collection(usersCollection).document(firebaseUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    Model.instance.setUser(User.create(documentSnapshot.getData()),(val)->listener.onComplete(val));
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    listener.onComplete("Failure");
                                }
                            });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onComplete("Failure");
            }
        });
    }

    public static void isLoggedIn(Model.OnCompleteLogInListener listener) {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            FirebaseUser firebaseUser = mAuth.getCurrentUser();
            db.collection(usersCollection).document(firebaseUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Model.instance.setUser(User.create(documentSnapshot.getData()),(val)->listener.onComplete(val));

                }
            });

        }
    }





    public static void uploadImage(Bitmap imageBmp, String userID, final Model.UpLoadImageListener listener){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference imagesRef;
        imagesRef = storage.getReference().child("pictures").child(userID);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        // Now we upload the data to firebase storage:
        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                listener.onComplete("");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //Getting from fireBase the image url:
                        Uri downloadUrl = uri;
                        listener.onComplete(downloadUrl.toString());
                    }
                });
            }
        });
    }






}
