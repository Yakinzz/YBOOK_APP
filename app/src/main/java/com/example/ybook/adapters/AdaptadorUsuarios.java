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
import com.example.ybook.models.ListElementUsuarios;

import java.util.List;

public class AdaptadorUsuarios extends RecyclerView.Adapter<AdaptadorUsuarios.ViewHolder> {

    List<ListElementUsuarios> mData;
    AdaptadorUsuarios.OnUsuariosListener onUsuariosListener;
    LayoutInflater mInflater;
    Context context;

    public AdaptadorUsuarios(List<ListElementUsuarios> mData, AdaptadorUsuarios.OnUsuariosListener onUsuariosListener, Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = mData;
        this.onUsuariosListener = onUsuariosListener;
        this.context = context;
    }

    @NonNull
    @Override
    public AdaptadorUsuarios.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.card_usuarios,null);
        return new AdaptadorUsuarios.ViewHolder(view, onUsuariosListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorUsuarios.ViewHolder holder, int position) {
            holder.binData(mData.get(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {
        CardView cardView;
        TextView nombreUsuario,email;
        AdaptadorUsuarios.OnUsuariosListener onUsuariosListener;

        ViewHolder(View itemView, AdaptadorUsuarios.OnUsuariosListener onUsuariosListener) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_usuario_general);
            nombreUsuario = itemView.findViewById(R.id.nombreUsuario);
            email = itemView.findViewById(R.id.emailUsuario);
            this.onUsuariosListener = onUsuariosListener;
            itemView.setOnClickListener(this);
        }

        void binData(final ListElementUsuarios item) {
            nombreUsuario.setText(item.getUsername());
            email.setText((item.getEmail()));

        }

        @Override
        public void onClick(View view) {
            onUsuariosListener.onUsuarioClick(getAbsoluteAdapterPosition());
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public interface OnUsuariosListener{
        void onUsuarioClick(int position);
    }
}
