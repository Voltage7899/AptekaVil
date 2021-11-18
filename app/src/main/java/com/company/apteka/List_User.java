package com.company.apteka;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.company.apteka.databinding.ActivityListAdminBinding;
import com.company.apteka.databinding.ActivityListViewBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class List_User extends AppCompatActivity {



    private ActivityListViewBinding binding;


    private DatabaseReference database;
    private ItemViewHolder itemViewHolder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityListViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initRecyclerView();
    }
    private void initRecyclerView() {
        binding.recyclerViewUser.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        binding.recyclerViewUser.addItemDecoration(dividerItemDecoration);

    }

    @Override
    protected void onStart() {
        super.onStart();

        database= FirebaseDatabase.getInstance().getReference().child("Product");
        FirebaseRecyclerOptions<Product> options=new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(database,Product.class).build();
        FirebaseRecyclerAdapter<Product, ItemViewHolder> adapter = new FirebaseRecyclerAdapter<Product, ItemViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ItemViewHolder holder, int position, @NonNull Product model) {

                holder.price.setText("Название: "+model.getPrice());
                holder.name.setText("Описание: "+model.getName());
                holder.desc.setText("Цена: "+model.getDescription());
                holder.imageView.setImageURI(Uri.parse(model.getImage()));//тут мы конвертируем стринговую переменную в URI,это для картинки,так как мы не можем хранить в бд ничего кроме стрингов и интов,то мы конвертировали URI в стринг при добавление в бд,в активити  Add_Sweat



            }

            @NonNull
            @Override
            public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.element,parent,false);
                itemViewHolder=new ItemViewHolder(view);
                return itemViewHolder;
            }
        };
        binding.recyclerViewUser.setAdapter(adapter);//Устанавливаем адаптер
        adapter.startListening();

    }
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView price,name,desc;

        ImageView imageView;

        public ItemViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.Name_element);
            desc=view.findViewById(R.id.description_element);
            price=view.findViewById(R.id.price_element);
            imageView = view.findViewById(R.id.imageRec_element);


        }
    }
}