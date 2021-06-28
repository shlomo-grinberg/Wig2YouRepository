package com.example.wig2you.ui.wigDetailsOwn;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.wig2you.Model.Model;
import com.example.wig2you.Model.Wig;
import com.example.wig2you.R;
import com.squareup.picasso.Picasso;

public class wigDetailsOwn_Fragment extends Fragment {
    WigDetailsOwnViewModel wigDetailsOwnViewModel;

    TextView length;
    TextView style;
    TextView variety;
    TextView price;
    TextView maker;
    TextView purchaseDate;
    TextView howToUse;
    TextView kosher;
    ImageView image;

    Button deleteBtn;
    Button editBtn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        wigDetailsOwnViewModel  = new ViewModelProvider(this).
                get(WigDetailsOwnViewModel.class);

        int position = wigDetailsOwn_FragmentArgs.fromBundle(getArguments()).getWigPosition();
        Wig wig = wigDetailsOwnViewModel.getData().getValue().get(position);

        View view = inflater.inflate(R.layout.fragment_wig_details_own, container, false);

        length = view.findViewById(R.id.wigDetailsOwn_et_length);
        style=view.findViewById(R.id.wigDetailsOwn_et_style);
        variety=view.findViewById(R.id.wigDetailsOwn_et_variety);
        price=view.findViewById(R.id.wigDetailsOwn_et_price);
        maker=view.findViewById(R.id.wigDetailsOwn_et_maker);
        purchaseDate=view.findViewById(R.id.wigDetailsOwn_et_tv_purchaseDate);
        howToUse=view.findViewById(R.id.wigDetailsOwn_et_howToUse);
        kosher=view.findViewById(R.id.wigDetailsOwn_et_kosher);
        image=view.findViewById(R.id.wigDetailsOwn_imgView_wig_image);


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

        deleteBtn = view.findViewById(R.id.wigDetailsOwn_btn_deleteBtn);
        editBtn = view.findViewById(R.id.wigDetailsOwn_btn_editBtn);

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wig.setAvailable(false);
                Model.instance.saveWig(wig,()->Navigation.findNavController(v).navigate(R.id.myAccountFragment));
            }
        });
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wigDetailsOwn_FragmentDirections.ActionWigDetailsOwnFragmentToEditWigFragment data = wigDetailsOwn_FragmentDirections.actionWigDetailsOwnFragmentToEditWigFragment(position);
                Navigation.findNavController(view).navigate(data);
            }
        });


        return view;
    }
}