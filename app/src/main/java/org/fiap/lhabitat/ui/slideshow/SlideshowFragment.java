package org.fiap.lhabitat.ui.slideshow;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.fiap.lhabitat.R;
import org.fiap.lhabitat.databinding.FragmentSlideshowBinding;

public class SlideshowFragment extends Fragment {

    private ImageButton _btn_link;
    private SlideshowViewModel slideshowViewModel;
    private FragmentSlideshowBinding binding;

    //    private Button _btn_link;
    String _url = "https://api.whatsapp.com/send/?phone=573225365832&text=Hola+quiero+obtener+mas+informaci%C3%B3n+de+una+propiedad!";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        _btn_link = getView().findViewById(R.id.btn_link);
//
//        _btn_link.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Uri _link = Uri.parse(_url);
//                Intent i = new Intent(Intent.ACTION_VIEW,_link);
//                startActivity(i);
//                //Log.d("click", "ok");
//
//            }
//        });

        _btn_link = root.findViewById(R.id.btn_link);

        _btn_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri _link = Uri.parse(_url);
                Intent i = new Intent(Intent.ACTION_VIEW,_link);
                startActivity(i);
            }
//                Toast.makeText(root.getContext(), "Tu información ha sido enviada, un asesor se contactara contigo en breve", Toast.LENGTH_SHORT).show();
//            }
        });
        return root;
    }
}