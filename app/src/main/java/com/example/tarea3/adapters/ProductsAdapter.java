package com.example.tarea3.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.example.tarea3.R;
import com.example.tarea3.activities.ModifyProductActivity;
import com.example.tarea3.datamodels.ProductoModel;
import com.example.tarea3.dbModels.ProductoDB;



public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductViewHolder> {

    private List<ProductoModel> productoList;
    private Context mContext;

    public ProductsAdapter(List<ProductoModel> productoList, Context mContext) {
        this.productoList = productoList;
        this.mContext = mContext;
    }

    public ProductsAdapter() {
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_producto,parent,false);
        return new ProductViewHolder(view, this);

    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        holder.bindData(productoList.get(position), mContext);
    }

    @Override
    public int getItemCount() {
        return productoList.size();
    }

    void deleteItem(int index) {
        productoList.remove(index);
        notifyItemRemoved(index);
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder{

        public ImageView cardImageView;
        public TextView nombreProducto;
        public TextView precioProducto;
        public TextView marcaProducto;
        public Button btnMod, btnDel;
        private ProductsAdapter adapter;

        public ProductViewHolder(@NonNull View itemView, ProductsAdapter adapter) {
            super(itemView);
            this.adapter = adapter;
            cardImageView = itemView.findViewById(R.id.productImage);
            nombreProducto = itemView.findViewById(R.id.textViewNombre);
            precioProducto = itemView.findViewById(R.id.textViewPrecio);
            marcaProducto = itemView.findViewById(R.id.textViewMarca);
            btnMod = itemView.findViewById(R.id.buttonModificar);
            btnDel = itemView.findViewById(R.id.buttonBorrar);
        }

        public void bindData(ProductoModel productoModel, Context context){
            byte[] valueDecoded= new byte[0];
            try {
                valueDecoded = Base64.decode(productoModel.getImage64(), Base64.DEFAULT);
            } catch (Exception e) {
                e.printStackTrace();
            }

            cardImageView.setImageBitmap(BitmapFactory.decodeByteArray(valueDecoded,0,valueDecoded.length));
            nombreProducto.setText(productoModel.getName());
            precioProducto.setText(productoModel.getPrice().toString());
            marcaProducto.setText(productoModel.getMarca());
            btnMod.setOnClickListener(v -> {
                Intent intent = new Intent(context, ModifyProductActivity.class);
                intent.putExtra("id", productoModel.getId());
                context.startActivity(intent);
            });
            btnDel.setOnClickListener(v -> {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("Borrar producto");
                alertDialogBuilder.setMessage("Está seguro de que quiere borrar el producto " + productoModel.getName() + "?");
                alertDialogBuilder.setPositiveButton("Sí", (dialogInterface, i) -> {
                    ProductoDB productoDB= new ProductoDB(context).open();
                    Boolean result = productoDB.delete(productoModel.getId());
                    productoDB.close();
                    if(result)
                        adapter.deleteItem(getAdapterPosition());
                });
                alertDialogBuilder.setCancelable(true);
                alertDialogBuilder.show();

            });
        }
    }
}
