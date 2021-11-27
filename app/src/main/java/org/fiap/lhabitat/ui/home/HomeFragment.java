package org.fiap.lhabitat.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

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


        Adapter adapter = new Adapter(propiedades);
        rv.setAdapter(adapter);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseStorage mStorage = FirebaseStorage.getInstance();
        database.getReference().getRoot().child("property").addValueEventListener(new ValueEventListener() {
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

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Selección: "+propiedades.get(rv.getChildAdapterPosition(v)).getPrice(), Toast.LENGTH_SHORT).show();
            }
        });

        return vista;

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
