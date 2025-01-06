package edu.pmdm.gonzalez_victorimdbapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import edu.pmdm.gonzalez_victorimdbapp.database.FavoritesManager;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import edu.pmdm.gonzalez_victorimdbapp.MovieDetailsActivity;
import edu.pmdm.gonzalez_victorimdbapp.R;
import edu.pmdm.gonzalez_victorimdbapp.database.FavoritesManager;
import edu.pmdm.gonzalez_victorimdbapp.models.Movie;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> movieList;
    private Context context;
    private boolean isFavoritesMode; // Indica si el adaptador se usa en la lista de favoritos

    public MovieAdapter(List<Movie> movieList, boolean isFavoritesMode) {
        this.movieList = movieList;
        this.isFavoritesMode = isFavoritesMode;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movieList.get(position);

        // Cargar la imagen de la película
        Glide.with(holder.itemView.getContext())
                .load(movie.getImageUrl())
                .into(holder.imageView);

        // Manejar el evento de click corto para abrir detalles
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), MovieDetailsActivity.class);
            intent.putExtra("MOVIE_DATA", movie); // Pasar el objeto Movie
            holder.itemView.getContext().startActivity(intent);
        });

        // Configurar clic largo dependiendo del modo
        holder.itemView.setOnLongClickListener(v -> {
            FavoritesManager favoritesManager = new FavoritesManager(context);
            String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

            if (isFavoritesMode) {
                // Modo favoritos: Eliminar de la lista
                favoritesManager.removeFavorite(movie.getId(), userEmail);
                movieList.remove(position); // Eliminar de la lista local
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, movieList.size());
                Toast.makeText(context, "Eliminada de favoritos: " + movie.getTitle(), Toast.LENGTH_SHORT).show();
            } else {
                // Modo principal: Agregar a favoritos
                favoritesManager.addFavorite(movie, userEmail);
                Toast.makeText(context, "Agregada a favoritos: " + movie.getTitle(), Toast.LENGTH_SHORT).show();
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.movie_image);
        }
    }
}
