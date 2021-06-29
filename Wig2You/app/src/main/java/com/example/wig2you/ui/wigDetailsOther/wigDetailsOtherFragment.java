package com.example.wig2you.ui.wigDetailsOther;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.wig2you.Model.Wig;
import com.example.wig2you.R;
import com.squareup.picasso.Picasso;


public class wigDetailsOtherFragment extends Fragment {
    WigDetailsOtherViewModel wigDetailsOtherViewModel;

    TextView length;
    TextView style;
    TextView variety;
    TextView price;
    TextView maker;
    TextView purchaseDate;
    TextView howToUse;
    TextView kosher;
    ImageView image;
    Button contactPhoneBtn;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        wigDetailsOtherViewModel  = new ViewModelProvider(this).
                get(WigDetailsOtherViewModel.class);
        wigDetailsOtherViewModel.getWigsData().observe(getViewLifecycleOwner(),(wigs)->{
        });
        wigDetailsOtherViewModel.getUsersData().observe(getViewLifecycleOwner(),(users)->{
        });

        int position = wigDetailsOtherFragmentArgs.fromBundle(getArguments()).getWigPosition();
        Wig wig = wigDetailsOtherViewModel.getWigsData().getValue().get(position);

        View view = inflater.inflate(R.layout.fragment_wig_details_other, container, false);

        length = view.findViewById(R.id.wigDetailsOther_et_length);
        style=view.findViewById(R.id.wigDetailsOther_et_style);
        variety=view.findViewById(R.id.wigDetailsOther_et_variety);
        price=view.findViewById(R.id.wigDetailsOther_et_price);
        maker=view.findViewById(R.id.wigDetailsOther_et_maker);
        purchaseDate=view.findViewById(R.id.wigDetailsOther_et_tv_purchaseDate);
        howToUse=view.findViewById(R.id.wigDetailsOther_et_howToUse);
        kosher=view.findViewById(R.id.wigDetailsOther_et_kosher);
        image=view.findViewById(R.id.wigDetailsOther_imgView_wig_image);
        contactPhoneBtn = view.findViewById(R.id.wigDetailsOther_Btn_contactPhone);

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

        contactPhoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(wig.getOwner()!=null){
                    contactPhoneBtn.setText(wigDetailsOtherViewModel.getPhone(wig.getOwner()));
                }
            }
        });

        return view;
    }
}