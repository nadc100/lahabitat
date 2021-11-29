package org.fiap.lhabitat.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import org.fiap.lhabitat.R;
import org.fiap.lhabitat.ui.details.GeneralDetailsFragment;
import org.fiap.lhabitat.ui.gallery.PropertyModel;

public class Adapter extends FirebaseRecyclerAdapter<PropertyModel, org.fiap.lhabitat.ui.home.Adapter.myViewHolder> {

    public Adapter(@NonNull FirebaseRecyclerOptions<PropertyModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull Adapter.myViewHolder myViewHolder, int i, @NonNull PropertyModel propertyModel) {
        myViewHolder.textViewprecio.setText(propertyModel.getPrice());
        myViewHolder.textViewciudad.setText(propertyModel.getCity());
        myViewHolder.textViewest.setText(propertyModel.getEstrato());
        myViewHolder.textViewparking.setText(propertyModel.getParking());

        Glide.with(myViewHolder.imageViewimagen.getContext()).load(propertyModel.getImageURL()).into(myViewHolder.imageViewimagen);

        myViewHolder.imageViewimagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fr_home, new GeneralDetailsFragment(
                        propertyModel.getStatus(),
                        propertyModel.getCity(),
                        propertyModel.getEstrato(),
                        propertyModel.getNeighborhood(),
                        propertyModel.getPrice(),
                        propertyModel.getRoom(),
                        propertyModel.getParking(),
                        propertyModel.getImageURL()
                )).addToBackStack(null).commit();

            }
        });
    }

    @NonNull
    @Override
    public Adapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_recycler, parent,false);
        return new Adapter.myViewHolder(view);
    }

    public class myViewHolder extends RecyclerView.ViewHolder{

        ImageView imageViewimagen;
        TextView textViewprecio;
        TextView textViewciudad;
        TextView textViewest;
        TextView textViewparking;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            imageViewimagen = itemView.findViewById(R.id.imageViewimagen);
            textViewprecio = itemView.findViewById(R.id.textViewprecio);
            textViewciudad = itemView.findViewById(R.id.textViewciudad);
            textViewest = itemView.findViewById(R.id.textViewest);
            textViewparking = itemView.findViewById(R.id.textViewparking);
        }
    }

}