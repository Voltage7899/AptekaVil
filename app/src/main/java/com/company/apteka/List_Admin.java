package com.company.apteka;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.company.apteka.databinding.ActivityListAdminBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.content.ContentValues.TAG;

public class List_Admin extends AppCompatActivity {



    private DatabaseReference database;
    //Переменная держателя образа элемента
    private FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    private ActivityListAdminBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityListAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Кнопка для перехода на добавление товара
        binding.newAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(List_Admin.this,add_product.class);
                startActivity(intent);
            }
        });

        //Иницилизация обновляемого списка
        initRecyclerView();
    }

    private void initRecyclerView() {
        //Устанавливаем расположение списка
        binding.recyclerViewAdmin.setLayoutManager(new LinearLayoutManager(this));
        //Декорация для разделения элементов
        DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        binding.recyclerViewAdmin.addItemDecoration(dividerItemDecoration);



        //Участок кода отвечающий за свайп влево и удаление
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                int position=viewHolder.getAdapterPosition();

                //После свайпа происходит удаление,за счет того,что мы получаем ссылку на объект и затем удаляем его из таблицы

                //database.child().removeValue();
                ((FirebaseRecyclerAdapter)binding.recyclerViewAdmin.getAdapter()).getRef(position).removeValue();
            }
        }).attachToRecyclerView(binding.recyclerViewAdmin);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Получаем ссылку на таблицу с товаром
        database= FirebaseDatabase.getInstance().getReference().child("Product");
        //Прописываем настройки для обновляемого списка,заточенного для Firebase,он из отдельной библиотеки подключенной в градле,последняя имплементация
        FirebaseRecyclerOptions<Product> options=new FirebaseRecyclerOptions.Builder<Product>()//Грубо говоря,данные поступающие из базы конвертируются в тип объекта Sweat
                .setQuery(database,Product.class).build();
        //Далее мы прописываем адаптер,как и куда вставляются данные из объекта Sweat
        FirebaseRecyclerAdapter<Product, ItemViewHolder> adapter = new FirebaseRecyclerAdapter<Product, ItemViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ItemViewHolder holder, int position, @NonNull Product model) {
                //Собественно сдесь и вставляются
                holder.name.setText("Название"+model.getName());
                holder.desc.setText("Описание "+model.getDescription());
                holder.price.setText("Цена"+model.getPrice());
                holder.imageView.setImageURI(Uri.parse(model.getImage()));//тут мы конвертируем стринговую переменную в URI,это для картинки,так как мы не можем хранить в бд ничего кроме стрингов и интов,то мы конвертировали URI в стринг при добавление в бд,в активити  Add_Sweat

            }


            @NonNull
            @Override
            public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                //Прописываем обертку,как она будет выглядеть
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.element,parent,false);//в данном случае прообраз это лист элемент
                ItemViewHolder itemViewHolder=new ItemViewHolder(view);
                //Пихаем в переменную обертки созданную нами обертку,чуть выше и возвращаем ее.
                return itemViewHolder;
            }
        };
        firebaseRecyclerAdapter=adapter;
        binding.recyclerViewAdmin.setAdapter(adapter);//Устанавливаем адаптер
        adapter.startListening();//на адаптер устанавливаем слушатель,когда данные будут меняться,это сразу отобразиться,воть
        //Все тоже самое и в списке для юзера

    }
    public  class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView name,desc,price;

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