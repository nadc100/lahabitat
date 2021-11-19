package org.fiap.lhabitat.ui.gallery;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.fiap.lhabitat.R;
import org.fiap.lhabitat.databinding.FragmentGalleryBinding;

import java.util.HashMap;
import java.util.Map;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    private FragmentGalleryBinding binding;

    public static final String EXTRA_INFO = "default";
    private ImageView captureBtn;
    private ImageView picture;
    private static final int REQUEST_PERMISSION_CAMERA = 100;
    private static final int REQUEST_IMAGE_CAMERA = 101;

    private AutoCompleteTextView status, city, estrato, parking;
    EditText neighborhood, price;
    Button btnRegister;

    DatabaseReference Property;


//    FirebaseDatabase database = FirebaseDatabase.getInstance();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

//        DatabaseReference Property = database.getReference("Property");
//        Property.setValue("Segunda Prueba");

        Property = FirebaseDatabase.getInstance().getReference();


        galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        captureBtn = root.findViewById(R.id.capture_button);
        picture = root.findViewById(R.id.picture);


        captureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED){
                        goToCamera();
                        Toast.makeText(getActivity(), "going to Camera", Toast.LENGTH_LONG).show();
                    }else{
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA},REQUEST_PERMISSION_CAMERA);
                    }
                }else{
                    goToCamera();
                }
            }
        });

        status = root.findViewById(R.id.status);
        String[] statusOption = {"Nuevo", "Usado"};
        ArrayAdapter statusAdapter = new ArrayAdapter(getActivity(), R.layout.dropdown_item, statusOption);
        status.setText(statusAdapter.getItem(0).toString(), false);
        status.setAdapter(statusAdapter);

        city = root.findViewById(R.id.city);
        String[] cityOption = {"Barranquilla", "Bogotá", "Cali", "Cartagena", "Medellín", "San Andrés"};
        ArrayAdapter cityAdapter = new ArrayAdapter(getActivity(), R.layout.dropdown_item, cityOption);
        city.setText(cityAdapter.getItem(0).toString(), false);
        city.setAdapter(cityAdapter);

        estrato = root.findViewById(R.id.estrato);
        String[] estratoOption = {"1", "2", "3", "4", "5", "6"};
        ArrayAdapter estratoAdapter = new ArrayAdapter(getActivity(), R.layout.dropdown_item, estratoOption);
        estrato.setText(estratoAdapter.getItem(0).toString(), false);
        estrato.setAdapter(estratoAdapter);

        parking = root.findViewById(R.id.parking);
        String[] parkingOption = {"1", "2", "3", "4", "5"};
        ArrayAdapter parkingAdapter = new ArrayAdapter(getActivity(), R.layout.dropdown_item, parkingOption);
        parking.setText(parkingAdapter.getItem(0).toString(), false);
        parking.setAdapter(parkingAdapter);





        neighborhood = root.findViewById(R.id.editTextNeighborhood);
        price = root.findViewById(R.id.editTextprice);
        btnRegister = root.findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String statusItem = status.getText().toString();
                String cityItem = city.getText().toString();
                String neighborhoodItem = neighborhood.getText().toString();
                String priceItem = price.getText().toString();
                String estratoItem = estrato.getText().toString();
                String parkingItem = parking.getText().toString();

                Map<String, Object> propertyData = new HashMap<>();
                propertyData.put("estado", statusItem);
                propertyData.put("ciudad", cityItem);
                propertyData.put("barrio", neighborhoodItem);
                propertyData.put("precio", priceItem);
                propertyData.put("estrato", estratoItem);
                propertyData.put("parqueaderos", parkingItem);

                Property.child("Property").push().setValue(propertyData);

                Toast.makeText(getActivity(), "Information Sent to Database", Toast.LENGTH_SHORT).show();
            }
        });




        final TextView textView = binding.title;
        galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

//    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//
//
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_CAMERA){
            if (permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                goToCamera();
            }else{
                Toast.makeText(getActivity(), "Necesitas Activar los Permisos", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == REQUEST_IMAGE_CAMERA){
            if (resultCode == RESULT_OK){
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                picture.setImageBitmap(bitmap);
                Log.i("TAG", "Result =>" + bitmap);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_LONG).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void goToCamera(){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAMERA);
        if (cameraIntent.resolveActivity(getActivity().getPackageManager() )!=null){
            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAMERA);
        }
    }

}