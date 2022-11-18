package com.example.tarea3.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.example.tarea3.R;
import com.example.tarea3.datamodels.MarcaModel;
import com.example.tarea3.dbModels.MarcaDB;



public class MarcaAdapter extends RecyclerView.Adapter<MarcaAdapter.MarcaViewHolder>{
    private List<MarcaModel> marcaList;
    private Context mContext;

    @NonNull
    @Override
    public MarcaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_marca,parent,false);
        return new MarcaViewHolder(view, this);

    }

    public MarcaAdapter(List<MarcaModel> marcaList, Context mContext) {
        this.marcaList = marcaList;
        this.mContext = mContext;
    }

    public MarcaAdapter() {
    }

    @Override
    public void onBindViewHolder(@NonNull MarcaViewHolder holder, int position) {
        holder.bindData(marcaList.get(position), mContext);
    }

    @Override
    public int getItemCount() {
        return marcaList.size();
    }

    void deleteItem(int index) {
        marcaList.remove(index);
        notifyItemRemoved(index);
    }

    public static class MarcaViewHolder extends RecyclerView.ViewHolder{

        public TextView nombreMarca;
        public Button btnDel;
        private MarcaAdapter adapter;

        public MarcaViewHolder(@NonNull View itemView, MarcaAdapter adapter) {
            super(itemView);
            this.adapter = adapter;
            nombreMarca = itemView.findViewById(R.id.textViewMarca);
            btnDel = itemView.findViewById(R.id.buttonBorrar);
        }

        public void bindData(MarcaModel marcaModel, Context context){

            nombreMarca.setText(marcaModel.getNombre());

            btnDel.setOnClickListener(v -> {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("Borrar producto");
                alertDialogBuilder.setMessage("Está seguro de que quiere borrar la marca ".concat(marcaModel.getNombre().concat("?"))  );
                alertDialogBuilder.setPositiveButton("Sí", (dialogInterface, i) -> {
                    MarcaDB marcaDB = new MarcaDB(context).open();
                    Boolean result = marcaDB.delete(marcaModel.getId());
                    marcaDB.close();
                    if(result)
                        adapter.deleteItem(getAdapterPosition());
                    else{
                        AlertDialog.Builder alertDialogBuilder1 = new AlertDialog.Builder(context);
                        alertDialogBuilder1.setTitle("Error al borrar marca");
                        alertDialogBuilder1.setMessage("Hay productos asociados a esta marca");
                        alertDialogBuilder1.show();
                    }
                });
                alertDialogBuilder.setCancelable(true);
                alertDialogBuilder.show();


            });
        }
    }
}
