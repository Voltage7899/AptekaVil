package com.company.apteka;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.company.apteka.databinding.ActivityAddProductBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class add_product extends AppCompatActivity {


    //ссылкa
    private DatabaseReference database;
    private ActivityAddProductBinding binding;
    private String image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivityAddProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        binding.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Как только прошли проверку на заполнение полей и картинки,то добавляем данные в бд

                if (image==null) {
                    Toast.makeText(add_product.this, "Добавьте картинку", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(binding.name.getText().toString())) {
                    Toast.makeText(add_product.this, "Добавьте название", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(binding.description.getText().toString())) {
                    Toast.makeText(add_product.this, "Добавьте описание", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(binding.price.getText().toString())) {
                    Toast.makeText(add_product.this, "Добавьте цену", Toast.LENGTH_SHORT).show();

                }else {
                    //Функция для добавления
                    AddInDataBase();
                }


            }

        });

        //слушатель на картинку
        binding.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Короче,прописываем намерение и в качестве параметра указываем что нам нужно получить
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                //Устанавливаем тип данных получаемых
                intent.setType("image/*");
                //Запускаем намерение с возвращаемым результатом и вернется нам все в онАктивитиРезалт
                startActivityForResult(Intent.createChooser(intent, "pickImage"), 1);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {//Проверка на удачное получение данных,все прошло гладко
            //Вытаскиваем ссылку на картинку из даты
           Uri imageUri = data.getData();
            binding.image.setImageURI(imageUri);//Устанавливаем картинку
            image = imageUri.toString();//Переводим Uri в стринг,чтобы запихнуть в базу данных
            getContentResolver().takePersistableUriPermission(imageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);//А тут мы даем постоянный допуск к этой картинке,иначе картинка не отобразится через несколько минут,ну или когда ты сменишь активити или перезапустишь приложение,вот попа будет,да

        }

    }
    private void AddInDataBase()
    {
        //Сдесь мы устанавливаем нужный айдишник и приводим его в инту
        String id=Integer.toString((int) (1+Math.random()*1000));

        Log.d(TAG,"Айдишник при добавлении "+id);

        database= FirebaseDatabase.getInstance().getReference("Product");

        HashMap<String,Object> ProductMap = new HashMap<>();

        ProductMap.put("id",id);
        ProductMap.put("name",binding.name.getText().toString());
        ProductMap.put("description",binding.description.getText().toString());
        ProductMap.put("price",binding.price.getText().toString());
        ProductMap.put("image",image);
        //Здесь мы пихаем данные в бд через Хэшмэп и делаем проверку на результат
        database.child(id).updateChildren(ProductMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(add_product.this, "Товар добавлен", Toast.LENGTH_SHORT).show();
                    //finish возвращает нас обратно если успешно
                    finish();
                }
                else {
                    Toast.makeText(add_product.this, "Произошла ошибка", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}