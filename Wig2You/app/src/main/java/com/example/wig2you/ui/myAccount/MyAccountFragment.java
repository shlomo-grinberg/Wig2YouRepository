package com.example.wig2you.ui.myAccount;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wig2you.Model.Model;
import com.example.wig2you.Model.Wig;
import com.example.wig2you.R;
import com.squareup.picasso.Picasso;

public class MyAccountFragment extends Fragment {
    View view;
    MyAccountViewModel myAccountViewModel;
    MyAdapter adapter;
    ImageButton addWigImgBtn;
    ImageButton editUserAccount;
    ImageView logOutIV;
    Button allWigsBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        myAccountViewModel  = new ViewModelProvider(this).
                get(MyAccountViewModel.class);

        myAccountViewModel.getData().observe(getViewLifecycleOwner(),
                (data)->{
                    adapter.notifyDataSetChanged();
                });

        view = inflater.inflate(R.layout.fragment_my_account, container, false);

        addWigImgBtn = view.findViewById(R.id.myAccount_imgBtn_addBtn);
        allWigsBtn = view.findViewById(R.id.myAccount_btn_allWigsBtn);
        logOutIV = view.findViewById(R.id.myAccount_imageView_logOut);
        editUserAccount = view.findViewById(R.id.myAccount_imageButtonEditUser);
        addWigImgBtn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.addWig_fragment));
        allWigsBtn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.allWigs_Fragment));
        editUserAccount.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.editUserAccountFragment));
        logOutIV.setOnClickListener(v->logOut());

        RecyclerView wigList = view.findViewById(R.id.myAccount_recyclerView);
        wigList.setHasFixedSize(true);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());

        wigList.setLayoutManager(manager);
        adapter = new MyAdapter();
        wigList.setAdapter(adapter);
        adapter.setOnClickListener(new OnItemClickListener() {
            @Override
            public void onClick(int position) {
                MyAccountFragmentDirections.ActionMyAccountFragmentToWigDetailsOwnFragment data = MyAccountFragmentDirections.actionMyAccountFragmentToWigDetailsOwnFragment(position);
                Navigation.findNavController(view).navigate(data);
            }
        });

        ProgressBar pb = view.findViewById(R.id.myAccount_progressBar);
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

    public void logOut(){
        Model.instance.logOut();
        Navigation.findNavController(view).navigate(R.id.logInFragment);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        OnItemClickListener listener;
        TextView varietyTv;
        TextView lengthTv;
        TextView styleTv;
        ImageView wigImageImgView;


        public MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            varietyTv = itemView.findViewById(R.id.wigRow_tv_variety);
            lengthTv = itemView.findViewById(R.id.wigRow_tv_length);
            styleTv = itemView.findViewById(R.id.wigRow_tv_style);
            wigImageImgView = itemView.findViewById(R.id.wigRow_img_view_image);

            this.listener=listener;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        int position=getAdapterPosition();
                        if(position!= RecyclerView.NO_POSITION){
                            listener.onClick(position);
                        }
                    }
                }
            });
        }
        public void bind(Wig wig){
            varietyTv.setText(wig.getVariety());
            lengthTv.setText(wig.getLength());
            styleTv.setText(wig.getStyle());
            wigImageImgView.setImageResource(R.drawable.avatar_woman);
            if(wig.getImage()!=null && !wig.getImage().equals("")){
                Picasso.get().load(wig.getImage()).placeholder(R.drawable.avatar_woman).into(wigImageImgView);
            }
        }
    }

    public interface OnItemClickListener{
        void onClick(int position);
    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        OnItemClickListener listener;

        public void setOnClickListener(OnItemClickListener listener){
            this.listener=listener;
        }
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= getLayoutInflater().inflate(R.layout.wig_row,parent,false);
            MyViewHolder holder= new MyViewHolder(view,listener);
            return holder;
        }
        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Wig wig = myAccountViewModel.getFilterData().get(position);
            holder.bind(wig);
        }
        @Override
        public int getItemCount() {
            return myAccountViewModel.getFilterData().size();
        }
    }
}