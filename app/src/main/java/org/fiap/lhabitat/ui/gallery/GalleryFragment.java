package org.fiap.lhabitat.ui.gallery;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
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
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.fiap.lhabitat.R;
import org.fiap.lhabitat.databinding.FragmentGalleryBinding;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    private FragmentGalleryBinding binding;

    public static final String EXTRA_INFO = "default";
    private ImageView captureBtn;
    private ImageView uploadBtn;
    private ImageView picture;

    private static final int REQUEST_PERMISSION_CAMERA = 100;
    private static final int REQUEST_IMAGE_CAMERA = 101;
    private static final int GALLERY_INTENT = 111;

    private AutoCompleteTextView status, city, estrato, parking;
    EditText neighborhood, price;
    Button btnRegister;

    StorageReference mStorage, ref;
    DatabaseReference Property;
    Uri filePath;
    String downloadUrl;
    ProgressDialog progressDialog;
    CardView galleryFragmentCardView;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

//        DatabaseReference Property = database.getReference("Property");
//        Property.setValue("Segunda Prueba");

//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        myRef = database.getReference("property");

        Property = FirebaseDatabase.getInstance().getReference("Image");
        mStorage = FirebaseStorage.getInstance().getReference();
        progressDialog = new ProgressDialog(getActivity());


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


//        uploadBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
//                galleryIntent.setType("*/*");
//                startActivityForResult(galleryIntent, GALLERY_INTENT);
//            }
//        });

        uploadBtn = root.findViewById(R.id.image_button);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/");
                galleryIntent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(galleryIntent, "Select image"), GALLERY_INTENT);
            }
        });


        status = root.findViewById(R.id.status);
        String[] statusOption = {"","Nuevo", "Usado"};
        ArrayAdapter statusAdapter = new ArrayAdapter(getActivity(), R.layout.dropdown_item, statusOption);
        status.setText(statusAdapter.getItem(0).toString(), false);
        status.setAdapter(statusAdapter);

        city = root.findViewById(R.id.city);
        String[] cityOption = {"", "Barranquilla", "Bogotá", "Cali", "Cartagena", "Medellín", "San Andrés"};
        ArrayAdapter cityAdapter = new ArrayAdapter(getActivity(), R.layout.dropdown_item, cityOption);
        city.setText(cityAdapter.getItem(0).toString(), false);
        city.setAdapter(cityAdapter);

        estrato = root.findViewById(R.id.estrato);
        String[] estratoOption = {"","1", "2", "3", "4", "5", "6"};
        ArrayAdapter estratoAdapter = new ArrayAdapter(getActivity(), R.layout.dropdown_item, estratoOption);
        estrato.setText(estratoAdapter.getItem(0).toString(), false);
        estrato.setAdapter(estratoAdapter);

        parking = root.findViewById(R.id.parking);
        String[] parkingOption = {"","0", "1", "2", "3", "4", "5"};
        ArrayAdapter parkingAdapter = new ArrayAdapter(getActivity(), R.layout.dropdown_item, parkingOption);
        parking.setText(parkingAdapter.getItem(0).toString(), false);
        parking.setAdapter(parkingAdapter);

        neighborhood = root.findViewById(R.id.editTextNeighborhood);
        price = root.findViewById(R.id.editTextprice);
        btnRegister = root.findViewById(R.id.btnRegister);
        galleryFragmentCardView = root.findViewById(R.id.card_information);

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

                uploadImage();
                Property.child("Property").push().setValue(propertyData);

                Toast.makeText(getActivity(), "Information Sent to Database", Toast.LENGTH_SHORT).show();


                goingToPropertyFragment();


                galleryFragmentCardView.setVisibility(View.GONE);

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

//    private void fileUpload() {
//        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
//        galleryIntent.setType("*/*");
//        startActivityForResult(galleryIntent, GALLERY_INTENT);
//    }

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
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAMERA){
            if (resultCode == RESULT_OK){
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                picture.setImageBitmap(bitmap);
                Log.i("TAG", "Result =>" + bitmap);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                picture.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


//            Uri FileUri = data.getData();
//
//            StorageReference filePath = FirebaseStorage.getInstance().getReference().child("property");
//            final StorageReference fileName = filePath.child("Pictures" + FileUri.getLastPathSegment());

//            fileName.putFile(FileUri).addOnSuccessListener(taskSnapshot -> fileName.getDownloadUrl().addOnSuccessListener(uri -> {
//               HashMap <String, String> hashMap = new HashMap<>();
//               hashMap.put("link", String.valueOf(uri));
//               myRef.setValue(hashMap);
//            }));

//                    new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Toast.makeText(getActivity(), "image uploaded", Toast.LENGTH_SHORT).show();
//                }
//            };
        }
    }


    public void goToCamera(){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAMERA);
        if (cameraIntent.resolveActivity(getActivity().getPackageManager() )!=null){
            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAMERA);
        }
    }

    public void uploadImage(){
        if(filePath != null){
            progressDialog.show();
            ref = FirebaseStorage.getInstance().getReference().child("Images");
            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mStorage.child("Images").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            downloadUrl = task.getResult().toString();
                            Property.push().child("imageUrl").setValue(downloadUrl);
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Image uploaded", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Upload Failed", Toast.LENGTH_LONG).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double pr = (100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploading " + (int)pr + "%");
                }
            });
        } else
            Toast.makeText(getActivity(), "Image not found", Toast.LENGTH_LONG).show();
    }

    public void goingToPropertyFragment() {


        Fragment propertyFragment = new PropertyFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.gallery_frame, propertyFragment);
        transaction.addToBackStack(null);
        transaction.commit();


//        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.setReorderingAllowed(true);
//
//        transaction.replace(R.id.propertyFrame, new PropertyFragment());
//        transaction.commit();
//        Toast.makeText(getActivity(), "Going to Property Fragment", Toast.LENGTH_SHORT).show();
    }

}