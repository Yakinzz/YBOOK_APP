package com.example.ybook.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ybook.models.ListElementLibros;
import com.example.ybook.R;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    List<ListElementLibros> mData;
    OnLibroListener onLibroListener;
    LayoutInflater mInflater;
    Context context;

    public MyAdapter(List<ListElementLibros> itemList, Context context, OnLibroListener onLibroListener){
        this.mInflater = LayoutInflater.from(context);
        this.mData = itemList;
        this.context=context;
        this.onLibroListener=onLibroListener;
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.listelement,null);
        return new MyAdapter.ViewHolder(view,onLibroListener);
    }

    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {
        holder.binData(mData.get(position));

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
        CardView cardView;
        TextView titulo, editorial,autor;
        OnLibroListener onLibroListener;
        ViewHolder(View itemView, OnLibroListener onLibroListener){
            super(itemView);
            cardView = itemView.findViewById(R.id.cv);
            titulo = itemView.findViewById(R.id.titulo);
            editorial = itemView.findViewById(R.id.editorial);
            autor = itemView.findViewById(R.id.autor);
            this.onLibroListener=onLibroListener;

            itemView.setOnClickListener(this);
        }

        void binData(final ListElementLibros item){
            titulo.setText(item.getTitulo());
            editorial.setText(item.getEditorial());
            autor.setText(item.getAutor());

        }


        @Override
        public void onClick(View view) {
            onLibroListener.onLibroClick(getAbsoluteAdapterPosition());
        }
    }

    public interface OnLibroListener{
        void onLibroClick(int position);
    }
}
