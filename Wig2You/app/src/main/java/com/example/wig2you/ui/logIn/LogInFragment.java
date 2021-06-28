package com.example.wig2you.ui.logIn;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.wig2you.Model.Model;
import com.example.wig2you.R;


public class LogInFragment extends Fragment {

    LogInViewModel logInViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        logInViewModel = new ViewModelProvider(this).get(LogInViewModel.class);
        View root = inflater.inflate(R.layout.fragment_log_in, container, false);
        EditText email = root.findViewById(R.id.logIn_et_email);
        EditText password = root.findViewById(R.id.logIn_et_password);

        TextView signUpTv = root.findViewById(R.id.logIn_tv_signup);
        Button signInBtn = root.findViewById(R.id.logIn_btn_login);

        signUpTv.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.signUpFragment));
        signInBtn.setOnClickListener(v->{
            Model.instance.login(email.getText().toString(),password.getText().toString(),(val)->{
                if (!val.equals("Failure")){
                    Navigation.findNavController(root).navigate(R.id.myAccountFragment);
                }});
            });

        Model.instance.isLoggedIn((val)->{
            if (!val.equals("Failure")) {
                Navigation.findNavController(root).navigate(R.id.myAccountFragment);
            }});


        return root;
    }
}