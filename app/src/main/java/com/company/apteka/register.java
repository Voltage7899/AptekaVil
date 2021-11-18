package com.company.apteka;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.company.apteka.databinding.ActivityRegisterBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class register extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database= FirebaseDatabase.getInstance().getReference("User");

        //Задаем слушателя
        binding.regReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //В переменные типа стринг пихаем введенные данные из полей
                String name = binding.nameReg.getText().toString();
                String phone=binding.phoneReg.getText().toString();
                String pass = binding.passReg.getText().toString();

                //Создаем разметку данных через ХэшМэп
                HashMap<String,Object> userDataMap=new HashMap<>();
                userDataMap.put("name",name);
                userDataMap.put("phone",phone);
                userDataMap.put("pass",pass);

                //Устанавливаем слушателя на изменение данных в базе данных
                database.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //Как только мы считали данные с базы данных,нам приходят данные в качестве снэпшота
                        if(!(snapshot.child(phone).exists())){//Проверяем,есть ли введенный телефон уже в базе данных,если нет,то добавляем хэшмэп в бд

                            database.child(phone).updateChildren(userDataMap);
                            Toast.makeText(register.this, "Вы зарегестрированы", Toast.LENGTH_SHORT).show();
                            finish();

                        }
                        else{
                            Toast.makeText(register.this, "Пользователь с такими данными уже есть", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }
}
