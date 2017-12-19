package net.teamredhawk.simplefeedreader;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by luffynando on 19/12/2017.
 */

public class AdaptadorPosts extends RecyclerView.Adapter<AdaptadorPosts.ViewHolder> {
    /**
     * Interfaz de comunicación
     */
    public interface OnItemClickListener {
        void onItemClick(ViewHolder item, int position);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public OnItemClickListener getOnItemClickListener() {
        return listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        // Campos respectivos de un item
        public TextView title;
        public TextView categorias;
        //public ImageView imagen;

        private AdaptadorPosts padre = null;

        public ViewHolder(View v, AdaptadorPosts padre) {
            super(v);

            v.setOnClickListener(this);
            this.padre = padre;

            title = (TextView) v.findViewById(R.id.titulo_post);
            categorias= (TextView) v.findViewById(R.id.categorias_post);
            //imagen = (ImageView) v.findViewById(R.id.miniatura_post);

        }

        @Override
        public void onClick(View v) {
            final OnItemClickListener listener = padre.getOnItemClickListener();
            if (listener != null) {
                listener.onItemClick(this, getAdapterPosition());
            }
        }
    }


    @Override
    public int getItemCount() {
        return Post.Posts.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_post, viewGroup, false);
        return new ViewHolder(v, this);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Post item = Post.Posts.get(i);

        viewHolder.title.setText(item.getTitulo());
        String categoria= "";
        ArrayList<String> auxiliar = item.getCategorias();
        for (int j=0;  i<auxiliar.size(); i++){
            categoria= categoria+" *"+auxiliar.get(j);
        }
        viewHolder.categorias.setText(categoria);
        //carga de imagen si quieres :V
        /*Glide.with(viewHolder.itemView.getContext())
                .load(item.getUrlImagen())
                .centerCrop()
                .into(viewHolder.imagen);*/
    }
}
