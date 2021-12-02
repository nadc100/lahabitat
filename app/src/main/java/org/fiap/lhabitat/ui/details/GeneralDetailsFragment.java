package org.fiap.lhabitat.ui.details;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
//import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.fiap.lhabitat.R;
import org.fiap.lhabitat.ui.gallery.PropertyModel;
import org.fiap.lhabitat.ui.home.HomeFragment;
import org.fiap.lhabitat.ui.slideshow.SlideshowFragment;

import java.util.ArrayList;
import java.util.List;


public class GeneralDetailsFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public static List<PropertyModel> property = new ArrayList<>();
    String status, city,  estrato, neighborhood, price, room, parking, imageUrl;
    FloatingActionButton fab_goback;
    Button fab_purchase;

    public GeneralDetailsFragment() {
        // Required empty public constructor
    }

    public GeneralDetailsFragment(String status, String city,  String estrato, String neighborhood, String price, String room, String parking, String imageUrl) {
        this.status = status;
        this.city = city;
        this.estrato = estrato;
        this.neighborhood = neighborhood;
        this.price = price;
        this.room = room;
        this.parking = parking;
        this.imageUrl = imageUrl;
    }


    public static GeneralDetailsFragment newInstance(String param1, String param2) {
        GeneralDetailsFragment fragment = new GeneralDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_general_details, container, false);

        fab_goback = view.findViewById(R.id.general_details_fab_goback);
        fab_purchase = view.findViewById(R.id.fab_purchase);

        ImageView detailsImage = view.findViewById(R.id.imageviewimage);
        TextView detailsStatus = view.findViewById(R.id.general_details_status);
        TextView detailsCity = view.findViewById(R.id.general_details_city);
        TextView detailsEstrato = view.findViewById(R.id.general_details_estrato);
        TextView detailsNeighborhood = view.findViewById(R.id.general_details_neighborhood);
        TextView detailsPrice = view.findViewById(R.id.general_details_price);
        TextView detailsRooms = view.findViewById(R.id.general_details_rooms);
        TextView detailsParking = view.findViewById(R.id.general_details_parking);
        Glide.with(getContext()).load(imageUrl).into(detailsImage);

        detailsStatus.setText(status);
        detailsCity.setText(city);
        detailsEstrato.setText(estrato);
        detailsNeighborhood.setText(neighborhood);
        detailsPrice.setText(price);
        detailsRooms.setText(room);
        detailsParking.setText(parking);

        fab_goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goingToHomeFragment();
            }
        });

        fab_purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goingToSlideshowFragment();
            }
        });

        //SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapa);
        //mapFragment.getMapAsync(this);

        return view;

    }

    public void goingToHomeFragment() {
        Fragment homeFragment = new HomeFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fr_home, homeFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void goingToSlideshowFragment() {
        Fragment slideshowFragment = new SlideshowFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fr_home, slideshowFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


}