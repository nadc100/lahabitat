package org.fiap.lhabitat.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.fiap.lhabitat.R;

import java.util.List;


public class Adapter extends RecyclerView.Adapter<Adapter.PropiedadesviewHolder> implements View.OnClickListener {

    List<Propiedad> propiedades;

    private View.OnClickListener listener;



    public Adapter(List<Propiedad> propiedades) {
        this.propiedades = propiedades;
    }

    @NonNull
    @Override
    public PropiedadesviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_recycler,parent, false);
        PropiedadesviewHolder holder = new PropiedadesviewHolder(v);
        v.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PropiedadesviewHolder holder, int position) {
        Propiedad propiedad = propiedades.get(position);
        holder.textViewPrecio.setText(propiedad.getPrice());
        holder.textViewCiudad.setText(propiedad.getCity());
        holder.textViewHabitacion.setText(propiedad.getEstrato());
        holder.textViewBano.setText(propiedad.getParking());
        //holder.textViewImagen.setText(propiedad.getImagen());
    }



    @Override
    public int getItemCount() {
        return propiedades.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener != null){
            listener.onClick(v);
        }
    }


    public static class PropiedadesviewHolder extends RecyclerView.ViewHolder{
        TextView textViewPrecio, textViewCiudad, textViewHabitacion, textViewBano;
        ImageView imageViewImagen;

        public PropiedadesviewHolder(View itemView){
            super(itemView);
            textViewPrecio = (TextView) itemView.findViewById(R.id.textViewprecio);
            textViewCiudad = (TextView) itemView.findViewById(R.id.textViewciudad);
            textViewHabitacion = (TextView) itemView.findViewById(R.id.textViewest);
            textViewBano = (TextView) itemView.findViewById(R.id.textViewparking);
            imageViewImagen = (ImageView) itemView.findViewById(R.id.imageViewimagen);

        }

    }


}
