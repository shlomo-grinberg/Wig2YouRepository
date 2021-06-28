package com.example.wig2you.ui.editUserAccount;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.wig2you.Model.Model;
import com.example.wig2you.Model.User;
import com.example.wig2you.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.time.LocalDateTime;

public class editUserAccountFragment extends Fragment {
    View view;

    EditText name;
    EditText address;
    EditText phone;
    EditText email;
    EditText password;
    Button saveBtn;
    ImageView image;
    public static double latitude =0;
    public static double longitude=0;
    Button addLocationOnMap;
    Bitmap imageBitmap;
    User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit_user_account, container, false);
        saveBtn = view.findViewById(R.id.editUserAccount_btn_save);
        name =  view.findViewById(R.id.editUserAccount_et_name);
        address =  view.findViewById(R.id.editUserAccount_et_address);
        phone =  view.findViewById(R.id.editUserAccount_et_phone);
        email =  view.findViewById(R.id.editUserAccount_et_email);
        password =  view.findViewById(R.id.editUserAccount_et_password);
        image = view.findViewById(R.id.editUserAccount_et_imgView_user_image);
        addLocationOnMap = view.findViewById(R.id.editUserAccount_btn_addLocationOnMap);
        image.setOnClickListener(v->takePictureFromGallery());

        user = Model.instance.getUser();

        name.setText(user.name);
        address.setText(user.address);
        phone.setText(user.phone);
        email.setText(user.phone);
        password.setText(user.phone);
        image.setImageResource(R.drawable.avatar_woman);
        if(user.getImage()!=null && !user.getImage().equals("")){
            Picasso.get().load(user.getImage()).placeholder(R.drawable.avatar_woman).into(image);
        }
//        image = view.findViewById(R.id.editUserAccount_et_imgView_user_image);


        saveBtn.setOnClickListener(v -> {
            save();
        });
        addLocationOnMap.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.saveLocationMapFragment));

        ProgressBar pb = view.findViewById(R.id.editUserAccount_progressBar);
        pb.setVisibility(View.GONE);

        Model.instance.loadingState.observe(getViewLifecycleOwner(),(state)->{
            switch(state){
                case loaded:
                    pb.setVisibility(View.GONE);
                    break;
                case loading:
                    pb.setVisibility(View.VISIBLE);
                    break;
                case error:
                    //TODO: display error message
            }
        });

        return view;
    }

    private void save() {
        User user = new User();
        user.setName(name.getText().toString());
        user.setAddress(address.getText().toString());
        user.setPhone(phone.getText().toString());
        user.setEmail(email.getText().toString());
        user.setPassword(password.getText().toString());
        user.setLatitude(latitude);
        user.setLongitude(longitude);
        user.setAvailable(true);
        image.setEnabled(false);

        Model.instance.saveUser(user, new Model.OnCompleteListener() {
            @Override
            public void onComplete() {
                if (imageBitmap!=null){
                    Model.instance.uploadImage(imageBitmap,user.id,(url)->{
                        user.setImage(url);
                        Model.instance.saveUser(user,()->Navigation.findNavController(view).navigate(R.id.myAccountFragment));
                    });    //TODO: change to ID
                }
                else  {
                    Navigation.findNavController(view).navigate(R.id.myAccountFragment);
                }
            }
        });
    }

    static final int GALLERY_IMAGE = 0;
    final static int RESULT_SUCCESS = -1;

    void takePictureFromGallery(){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //If we back from camera:
        if(requestCode == GALLERY_IMAGE && resultCode == RESULT_SUCCESS)
        {
            Uri selectedImage = data.getData();
            try
            {
                imageBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
            } catch (IOException e) {}
            image.setImageBitmap(imageBitmap);
        }
    }
}