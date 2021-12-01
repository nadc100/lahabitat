package org.fiap.lhabitat.ui.gallery;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
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
import android.webkit.MimeTypeMap;
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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.fiap.lhabitat.R;
import org.fiap.lhabitat.databinding.FragmentGalleryBinding;

import java.io.FileNotFoundException;
import java.io.IOException;

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

    private AutoCompleteTextView status, city, estrato, room, parking;
    EditText neighborhood, price, description;
    Button btnRegister;

    StorageReference mStorage, ref;
    DatabaseReference Property;
    Uri filePath;
    String downloadUrl;
    ProgressDialog progressDialog;
    CardView galleryFragmentCardView;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Property = FirebaseDatabase.getInstance().getReference();
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

        room = root.findViewById(R.id.rooms);
        String[] roomsOption = {"","1", "2", "3", "4", "5", "6", "7", "8", "9"};
        ArrayAdapter roomAdapter = new ArrayAdapter(getActivity(), R.layout.dropdown_item, roomsOption);
        room.setText(roomAdapter.getItem(0).toString(), false);
        room.setAdapter(roomAdapter);

        parking = root.findViewById(R.id.parking);
        String[] parkingOption = {"","0", "1", "2", "3", "4", "5"};
        ArrayAdapter parkingAdapter = new ArrayAdapter(getActivity(), R.layout.dropdown_item, parkingOption);
        parking.setText(parkingAdapter.getItem(0).toString(), false);
        parking.setAdapter(parkingAdapter);

        neighborhood = root.findViewById(R.id.editTextNeighborhood);
        price = root.findViewById(R.id.editTextprice);
        description = root.findViewById(R.id.et_description);
        btnRegister = root.findViewById(R.id.btnRegister);
        galleryFragmentCardView = root.findViewById(R.id.card_information);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
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
//
                picture.setImageURI(filePath);

                Bitmap filepath = (Bitmap) data.getExtras().get("data");
                picture.setImageBitmap(filepath);
                Log.i("TAG", "Result =>" + filepath);
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

        }
    }

//    private void onCaptureImageResult(Intent data) {
//        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
//        byte bb[] = bytes.toByteArray();
//        picture.setImageBitmap(thumbnail);
////        uploadToFirebase(bb);
//    }
//
//    private void uploadToFirebase(byte[] bb) {
//        StorageReference sr = mStorage.child("images");
//        sr.putBytes(bb).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                Toast.makeText(getActivity(), "Successfully Upload", Toast.LENGTH_SHORT).show();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(getActivity(), "" + "Failed To Upload", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }

    public void goToCamera(){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAMERA);
        if (cameraIntent.resolveActivity(getActivity().getPackageManager() )!=null){
            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAMERA);
        }
    }

    public void uploadImage() {

        if (filePath != null) {

            progressDialog.setTitle("Image is Uploading...");
            progressDialog.show();

            StorageReference storageReference2 = mStorage.child(System.currentTimeMillis() + "." + GetFileExtension(filePath));
            storageReference2.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            storageReference2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String tempStatus = status.getText().toString().trim();
                                    String tempCity = city.getText().toString().trim();
                                    String tempEstrato = estrato.getText().toString().trim();
                                    String tempNeighborhood = neighborhood.getText().toString().trim();
                                    String tempPrice = price.getText().toString().trim();
                                    String tempRoom = room.getText().toString().trim();
                                    String tempParking = parking.getText().toString().trim();
                                    String tempDescription = parking.getText().toString().trim();
                                    progressDialog.dismiss();
                                    Toast.makeText(getActivity().getApplicationContext(), "Image Uploaded Successfully ", Toast.LENGTH_LONG).show();
                                    @SuppressWarnings("VisibleForTests")
                                    PropertyModel imageUploadInfo = new PropertyModel(tempStatus, tempCity, tempEstrato, tempNeighborhood, tempPrice, tempRoom, tempParking, tempDescription, uri.toString());
                                    String ImageUploadId = Property.push().getKey();
                                    Property.child(ImageUploadId).setValue(imageUploadInfo);


                                    Toast.makeText(getActivity(), "Uploaded Succesfully", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });
        }
        else {
            Toast.makeText(getActivity(), "Please Select Image or Add Image Name", Toast.LENGTH_LONG).show();
        }
    }

    public void goingToPropertyFragment() {
        Fragment propertyFragment = new PropertyFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.gallery_frame, propertyFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}