package com.example.wig2you.ui.addWig;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
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
import androidx.navigation.Navigation;

import com.example.wig2you.Model.Model;
import com.example.wig2you.Model.User;
import com.example.wig2you.Model.Wig;
import com.example.wig2you.R;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

public class AddWig_fragment extends Fragment {
    View view;
    EditText length;
    EditText style;
    EditText variety;
    EditText price;
    EditText maker;
    TextView purchaseDate;
    EditText howToUse;
    EditText kosher;
    Button saveBtn;
    ImageView image;
    Bitmap imageBitmap;

    public AddWig_fragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_wig_fragment, container, false);

        length = view.findViewById(R.id.addWig_et_length);
        style=view.findViewById(R.id.addWig_et_style);
        variety=view.findViewById(R.id.addWig_et_variety);
        price=view.findViewById(R.id.addWig_et_price);
        maker=view.findViewById(R.id.addWig_et_maker);
        purchaseDate=view.findViewById(R.id.addWig_et_tv_purchaseDate);
        howToUse=view.findViewById(R.id.addWig_et_howToUse);
        kosher=view.findViewById(R.id.addWig_et_kosher);
        saveBtn=view.findViewById(R.id.addWig_btn_saveBtn);
        image = view.findViewById(R.id.addWig_imgView_wig_image);


        purchaseDate = view.findViewById(R.id.addWig_et_tv_purchaseDate);

        LocalDateTime currentDate = null;
        int selectedDayOfMonth = 0;
        int selectedMonth = 0;
        int selectedYear = 0;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            currentDate = LocalDateTime.now();
            selectedYear = currentDate.getYear();
            selectedMonth = currentDate.getMonthValue()-1;
            selectedDayOfMonth = currentDate.getDayOfMonth();
        }

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
        image.setOnClickListener(v->takePictureFromGallery());

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });


        return view;
    }

    private void save() {
        Wig wig = new Wig();
        wig.setPrice(Double.parseDouble(price.getText().toString()));
        wig.setVariety(variety.getText().toString());
        wig.setLength(length.getText().toString());
        wig.setStyle(style.getText().toString());
        wig.setMaker(maker.getText().toString());
        wig.setPurchaseDate(purchaseDate.getText().toString());
        wig.setHowToUse(howToUse.getText().toString());
        wig.setKosher(kosher.getText().toString());
        wig.setAvailable(true);
        UUID uuid = UUID.randomUUID();
        wig.setId(uuid.toString());
        wig.setOwner(Model.instance.getUser().id);
        Model.instance.saveWig(wig, new Model.OnCompleteListener() {
            @Override
            public void onComplete() {
                if (imageBitmap!=null){
                    Model.instance.uploadImage(imageBitmap,wig.getId(),(url)->{
                        wig.setImage(url);
                        Model.instance.saveWig(wig,()->Navigation.findNavController(view).navigate(R.id.myAccountFragment));
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