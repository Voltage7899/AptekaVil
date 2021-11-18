package com.company.apteka;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.company.apteka.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding= ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.singIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Создаем намерение,в параметрах задаем куда нужно нас переправить
                Intent intent=new Intent(MainActivity.this,sing_in.class);
                //Запускаем намерение
                startActivity(intent);
            }
        });
        //Устанавливаем слушателя на кнопку
        binding.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Создаем намерение,в параметрах задаем куда нужно нас переправить
                Intent intent=new Intent(MainActivity.this,register.class);
                //Запускаем намерение
                startActivity(intent);
            }
        });




    }
}