package com.example.wig2you.ui.saveLocationMap;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.wig2you.R;
import com.example.wig2you.ui.editUserAccount.editUserAccountFragment;
import com.example.wig2you.ui.myAccount.MyAccountFragmentDirections;
import com.example.wig2you.ui.signUp.SignUpFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class SaveLocationMapFragment extends Fragment implements OnMapReadyCallback {
    View view;
    FusedLocationProviderClient client;
    SupportMapFragment supportMapFragment;
    GoogleMap mGoogleMap;
    Double latitude = new Double(0);
    Double longitude = new Double(0);
    Button saveLocationBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_save_location_map, container, false);
        FloatingActionButton zoomInBtn = view.findViewById(R.id.saveLocationMap_zoomInButton);
        saveLocationBtn = view.findViewById(R.id.saveLocationMap_btn_saveLocationBtn);
        client = new FusedLocationProviderClient(getActivity());
        zoomInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentFocus();
            }
        });
        saveLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpFragment.latitude = latitude;
                SignUpFragment.longitude = longitude;
                editUserAccountFragment.latitude = latitude;
                editUserAccountFragment.longitude = longitude;
                Navigation.findNavController(view).navigateUp();
            }
        });

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        permission();
        supportMapFragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.saveLocationMap_map));
        if (supportMapFragment != null) {
            supportMapFragment.onCreate(null);
            supportMapFragment.onResume();
            supportMapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        permission();
        MapsInitializer.initialize(getContext());
        mGoogleMap = googleMap;
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LatLng latStartLocation = new LatLng(31.762465893563718, 35.20033924190005);//myLatLng.latitude,myLatLng.longitude);

        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latStartLocation, 10));
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        mGoogleMap.setOnMapClickListener(latLng -> {
            mGoogleMap.clear();
            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("I am here");
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
            mGoogleMap.addMarker(markerOptions);
            this.latitude = latLng.latitude;
            this.longitude = latLng.longitude;
        });
    }

    private void permission() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            ;
        else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                permission();
            }
        }
    }

    private void getCurrentFocus() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        client.getLastLocation().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                Location location = task.getResult();
                goToLocation(location.getLatitude(), location.getLongitude());
            }
        });
    }

    private void goToLocation(double latitude, double longitude) {
        LatLng latLng = new LatLng(latitude,longitude);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng,18);
        mGoogleMap.moveCamera(cameraUpdate);
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        CircleOptions circleOptions = new CircleOptions().center(latLng).radius(30).fillColor(0x8800CCFF).strokeColor(Color.BLUE).strokeWidth(2);
        mGoogleMap.addCircle(circleOptions);
    }

}