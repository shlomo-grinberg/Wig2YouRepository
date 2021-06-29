package com.example.wig2you.ui.editWig;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.wig2you.Model.Model;
import com.example.wig2you.Model.Wig;
import com.example.wig2you.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;


public class editWig_Fragment extends Fragment {
    EditWigViewModel editWigViewModel;
    View view;
    Wig wig;
    EditText length;
    EditText style;
    EditText variety;
    EditText price;
    EditText maker;
    TextView purchaseDate;
    EditText howToUse;
    EditText kosher;
    ImageView image;
    Bitmap imageBitmap;

    Button saveBtn;
    Button deleteBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        editWigViewModel  = new ViewModelProvider(this).
                get(EditWigViewModel.class);

        int position = editWig_FragmentArgs.fromBundle(getArguments()).getWigPosition2();
        wig = editWigViewModel.getData().getValue().get(position);

        view = inflater.inflate(R.layout.fragment_edit_wig_, container, false);

        length = view.findViewById(R.id.editWig_et_length);
        style=view.findViewById(R.id.editWig_et_style);
        variety=view.findViewById(R.id.editWig_et_variety);
        price=view.findViewById(R.id.editWig_et_price);
        maker=view.findViewById(R.id.editWig_et_maker);
        purchaseDate=view.findViewById(R.id.editWig_et_tv_purchaseDate);
        howToUse=view.findViewById(R.id.editWig_et_howToUse);
        kosher=view.findViewById(R.id.editWig_et_kosher);
        image = view.findViewById(R.id.editWig_imgView_wig_image);

        length.setText(wig.getLength());
        style.setText(wig.getStyle());
        variety.setText(wig.getVariety());
        price.setText(wig.getPrice()+"");
        maker.setText(wig.getMaker());
        purchaseDate.setText(wig.getPurchaseDate());
        howToUse.setText(wig.getHowToUse());
        kosher.setText(wig.getKosher());
        image.setImageResource(R.drawable.avatar_woman);
        if(wig.getImage()!=null && !wig.getImage().equals("")){
            Picasso.get().load(wig.getImage()).placeholder(R.drawable.avatar_woman).into(image);
        }

        image.setOnClickListener(v->takePictureFromGallery());

        deleteBtn = view.findViewById(R.id.editWig_btn_deleteBtn);
        saveBtn = view.findViewById(R.id.editWig_btn_saveBtn);

        String[] date = wig.getPurchaseDate().split("-");
        int selectedYear = Integer.parseInt(date[2]);
        int selectedMonth = Integer.parseInt(date[1])-1;
        int selectedDayOfMonth =Integer.parseInt(date[0]);

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                purchaseDate.setText(dayOfMonth + "-" + (month+1) + "-" + year);
            }
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                dateSetListener, selectedYear, selectedMonth, selectedDayOfMonth);

        purchaseDate.setOnClickListener(v -> datePickerDialog.show());

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wig.setAvailable(false);
                Model.instance.saveWig(wig, new Model.OnCompleteListener() {
                    @Override
                    public void onComplete() {
                        Navigation.findNavController(v).navigate(R.id.myAccountFragment);
                    }
                });
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });


        return view;

    }

    private void save() {
        Wig newWig = new Wig();
        newWig.setPrice(Double.parseDouble(price.getText().toString()));
        newWig.setVariety(variety.getText().toString());
        newWig.setLength(length.getText().toString());
        newWig.setStyle(style.getText().toString());
        newWig.setMaker(maker.getText().toString());
        newWig.setPurchaseDate(purchaseDate.getText().toString());
        newWig.setHowToUse(howToUse.getText().toString());
        newWig.setKosher(kosher.getText().toString());
        newWig.setAvailable(true);
        newWig.setId(wig.getId());
        newWig.setOwner(wig.getOwner());
        if(wig.getImage()!=null){
            newWig.setImage(wig.getImage());
        }
        Model.instance.saveWig(newWig, new Model.OnCompleteListener() {
            @Override
            public void onComplete() {
                if (imageBitmap!=null){
                    Model.instance.uploadImage(imageBitmap,newWig.getId(),(url)->{
                        newWig.setImage(url);
                        Model.instance.saveWig(newWig,()->Navigation.findNavController(view).navigate(R.id.myAccountFragment));
                    });
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