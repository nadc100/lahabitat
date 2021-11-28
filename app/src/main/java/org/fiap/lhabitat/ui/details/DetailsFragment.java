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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.fiap.lhabitat.R;
import org.fiap.lhabitat.databinding.FragmentDetailsBinding;
import org.fiap.lhabitat.databinding.FragmentHomeBinding;
import org.fiap.lhabitat.ui.gallery.PropertyFragment;
import org.fiap.lhabitat.ui.home.HomeFragment;
import org.fiap.lhabitat.ui.home.Propiedad;

import java.util.List;

public class DetailsFragment extends Fragment {
    private FragmentDetailsBinding binding;
    List<Propiedad> propiedad;
    String status, city,  estrato, neighborhood, price, room, parking, imageUrl;
    FloatingActionButton fab_goback;

    public DetailsFragment() {
        // Required empty public constructor
    }

    public DetailsFragment(String status, String city,  String estrato, String neighborhood, String price, String room, String parking, String imageUrl) {
        this.status = status;
        this.city = city;
        this.estrato = estrato;
        this.neighborhood = neighborhood;
        this.price = price;
        this.room = room;
        this.parking = parking;
        this.imageUrl = imageUrl;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDetailsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        Bundle bundle = this.getArguments();
        if(bundle!= null);{
            propiedad = (List<Propiedad>) bundle.getSerializable("PROPIEDAD");
        }
        fab_goback = view.findViewById(R.id.fab_goback);

        ImageView detailsImage = view.findViewById(R.id.details_image);
        TextView detailsStatus = view.findViewById(R.id.details_status);
        TextView detailsCity = view.findViewById(R.id.details_city);
        TextView detailsEstrato = view.findViewById(R.id.details_estrato);
        TextView detailsNeighborhood = view.findViewById(R.id.details_neighborhood);
        TextView detailsPrice = view.findViewById(R.id.details_price);
        TextView detailsRooms = view.findViewById(R.id.details_rooms);
        TextView detailsParking = view.findViewById(R.id.details_parking);
        Glide.with(getContext()).load(imageUrl).into(detailsImage);

        detailsStatus.setText(status);
        detailsCity.setText(propiedad.get(0).getCity());
        detailsEstrato.setText(propiedad.get(0).getEstrato());
        detailsNeighborhood.setText(neighborhood);
        detailsPrice.setText(propiedad.get(0).getPrice());
        detailsRooms.setText(room);
        detailsParking.setText(parking);

        fab_goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeFragment homeFragment = new HomeFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.details_drawer, homeFragment).commit();;
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
