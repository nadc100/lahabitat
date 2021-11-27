package org.fiap.lhabitat.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.fiap.lhabitat.R;
import org.fiap.lhabitat.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    RecyclerView rv;

    List<Propiedad> propiedades;

    Adapter adapter;

    View vista;
    Button btnHome;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        View vista = inflater.inflate(R.layout.fragment_home,container,false);

        propiedades = new ArrayList<>();
        rv = (RecyclerView) vista.findViewById(R.id.recyclerId);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        llenarpropiedades();

        Adapter adapter = new Adapter(propiedades);
        rv.setAdapter(adapter);


        return vista;

    }

    private void llenarpropiedades(){
        //FirebaseDatabase database = FirebaseDatabase.getInstance();
        propiedades.add(new Propiedad("1500","Bogota","5","4","Imagen 1"));
        propiedades.add(new Propiedad("2500","Cali","5","4","Imagen 2"));
        propiedades.add(new Propiedad("3500","Villavicencio","5","4","Imagen 3"));
        propiedades.add(new Propiedad("4500","Medellin","5","4","Imagen 4"));

       /* database.getReference().getRoot().child("property").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                propiedades.removeAll(propiedades);
                for (DataSnapshot ds: snapshot.getChildren()) {
                    Propiedad propiedad = ds.getValue(Propiedad.class);
                    propiedades.add(propiedad);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        */


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
