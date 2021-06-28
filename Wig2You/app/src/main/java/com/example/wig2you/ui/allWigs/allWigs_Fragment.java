package com.example.wig2you.ui.allWigs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.wig2you.ui.myAccount.MyAccountFragment;
import com.squareup.picasso.Picasso;

public class allWigs_Fragment extends Fragment {
    AllWigsViewModel allWigsViewModel;
    MyAdapter adapter;
    ImageButton sortBtn;
    ImageButton mapBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        allWigsViewModel  = new ViewModelProvider(this).
                get(AllWigsViewModel.class);

        allWigsViewModel.getData().observe(getViewLifecycleOwner(),
                (data)->{
                    adapter.notifyDataSetChanged();
                });

        View view = inflater.inflate(R.layout.fragment_all_wigs_, container, false);
        mapBtn = view.findViewById(R.id.allWigs_ImageBtn_mapBtn);

        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.allWigsOnMapFragment);
            }
        });

        RecyclerView wigList = view.findViewById(R.id.allWigs_recyclerView);
        wigList.setHasFixedSize(true);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());

        wigList.setLayoutManager(manager);
        adapter = new allWigs_Fragment.MyAdapter();
        wigList.setAdapter(adapter);

        adapter.setOnClickListener(new OnItemClickListener() {
            @Override
            public void onClick(int position) {
                allWigs_FragmentDirections.ActionAllWigsFragmentToWigDetailsOtherFragment data = allWigs_FragmentDirections.actionAllWigsFragmentToWigDetailsOtherFragment(position);
                Navigation.findNavController(view).navigate(data);
            }
        });

        ProgressBar pb = view.findViewById(R.id.allWigs_progressBar);
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
            Wig wig = allWigsViewModel.getData().getValue().get(position);
            holder.bind(wig);
        }
        @Override
        public int getItemCount() {
            return allWigsViewModel.getData().getValue().size();
        }

    }

}