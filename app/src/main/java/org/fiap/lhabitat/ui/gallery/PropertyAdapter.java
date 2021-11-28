package org.fiap.lhabitat.ui.gallery;

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
import org.fiap.lhabitat.ui.details.DetailsFragment;

public class PropertyAdapter extends FirebaseRecyclerAdapter<PropertyModel, PropertyAdapter.myViewHolder> {

    public PropertyAdapter(@NonNull FirebaseRecyclerOptions<PropertyModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder myViewHolder, int i, @NonNull PropertyModel propertyModel) {
        myViewHolder.tv_property_city_list_item.setText(propertyModel.getCity());
        myViewHolder.tv_property_status_list_item.setText(propertyModel.getStatus());
        myViewHolder.tv_property_neighborhood_list_item.setText(propertyModel.getNeighborhood());
        myViewHolder.tv_property_price_list_item.setText(propertyModel.getPrice());
        Glide.with(myViewHolder.iv_property_image_list_item.getContext()).load(propertyModel.getImageURL()).into(myViewHolder.iv_property_image_list_item);

        myViewHolder.iv_property_image_list_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.gallery_frame, new DetailsFragment(
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
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.property_list_item, parent,false);
        return new myViewHolder(view);
    }

    public class myViewHolder extends RecyclerView.ViewHolder{

        ImageView iv_property_image_list_item;
        TextView tv_property_city_list_item;
        TextView tv_property_status_list_item;
        TextView tv_property_neighborhood_list_item;
        TextView tv_property_price_list_item;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_property_image_list_item = itemView.findViewById(R.id.iv_property_image_list_item);
            tv_property_city_list_item = itemView.findViewById(R.id.tv_property_city_list_item);
            tv_property_status_list_item = itemView.findViewById(R.id.tv_property_status_list_item);
            tv_property_neighborhood_list_item = itemView.findViewById(R.id.tv_property_neighborhood_list_item);
            tv_property_price_list_item = itemView.findViewById(R.id.tv_property_price_list_item);
        }
    }

}
