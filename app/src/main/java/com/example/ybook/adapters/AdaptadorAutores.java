package com.example.ybook.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ybook.R;
import com.example.ybook.models.ListElementAutores;
import com.example.ybook.models.ListElementLibros;

import java.util.List;

public class AdaptadorAutores extends RecyclerView.Adapter<AdaptadorAutores.ViewHolder> {

    List<ListElementAutores> mData;
    AdaptadorAutores.OnAutorListener onAutorListener;
    LayoutInflater mInflater;
    Context context;

    public AdaptadorAutores(List<ListElementAutores> mData, OnAutorListener onAutorListener, Context context) {
        this.mInflater= LayoutInflater.from(context);
        this.mData = mData;
        this.onAutorListener = onAutorListener;
        this.context = context;
    }

    @NonNull
    @Override
    public AdaptadorAutores.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.card_autor,null);
        return new AdaptadorAutores.ViewHolder(view, onAutorListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorAutores.ViewHolder holder, int position) {
        holder.binData(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {
        CardView cardView;
        TextView nombreAutor;
        AdaptadorAutores.OnAutorListener onAutorListener;

        ViewHolder(View itemView, AdaptadorAutores.OnAutorListener onAutorListener) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_autor_general);
            nombreAutor = itemView.findViewById(R.id.nombreAutor);
            this.onAutorListener = AdaptadorAutores.this.onAutorListener;
            itemView.setOnClickListener(this);
        }

        void binData(final ListElementAutores item) {
            nombreAutor.setText(item.getNombreAutor());

        }

        @Override
        public void onClick(View view) {
            onAutorListener.onAutorClick(getAbsoluteAdapterPosition());
        }
    }

    public interface OnAutorListener{
        void onAutorClick(int position);
    }
}
