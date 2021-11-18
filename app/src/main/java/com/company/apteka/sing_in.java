package com.company.apteka;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.company.apteka.databinding.ActivitySingInBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class sing_in extends AppCompatActivity {
private ActivitySingInBinding binding;
    private DatabaseReference database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivitySingInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database= FirebaseDatabase.getInstance().getReference();


        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone_field=binding.Phone.getText().toString();
                String pass_field = binding.Pass.getText().toString();


                if(TextUtils.isEmpty(phone_field)&&TextUtils.isEmpty(pass_field)){
                    Toast.makeText(sing_in.this, "Введите все данные", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (binding.adminCode.getText().toString().equals("123")){
                        database.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.child("User").child(phone_field).exists()){

                                    User userCurrentData=snapshot.child("User").child(phone_field).getValue(User.class);


                                    if(userCurrentData.phone.equals(phone_field) && userCurrentData.pass.equals(pass_field)){
                                        Toast.makeText(sing_in.this, "Вы вошли как админ", Toast.LENGTH_SHORT).show();
                                        Intent intent=new Intent(sing_in.this,List_Admin.class);
                                        startActivity(intent);

                                    }
                                    else {
                                        Toast.makeText(sing_in.this, "Неверные данные", Toast.LENGTH_SHORT).show();
                                    }
                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                    else{
                        database.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.child("User").child(phone_field).exists()){//Проверяем,есть ли пользователь с таким номером,введенным из поля телефона

                                    User userCurrentData=snapshot.child("User").child(phone_field).getValue(User.class);
                                    //Проверка на то,правильно ли введены поля
                                    if(userCurrentData.phone.equals(phone_field) && userCurrentData.pass.equals(pass_field)){
                                        //если все окей,то переносит на страницу для юзеров со списком товаров
                                        Toast.makeText(sing_in.this, "Вы вошли как Юзер", Toast.LENGTH_SHORT).show();
                                        Intent intent=new Intent(sing_in.this, List_User.class);
                                        startActivity(intent);

                                    }
                                    else {
                                        Toast.makeText(sing_in.this, "Неверные данные", Toast.LENGTH_SHORT).show();
                                    }
                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }


                }
            }
        });
    }
}