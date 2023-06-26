package com.layouts.maecomproject;

import static com.layouts.maecomproject.CartActivity.cartsItemList;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.layouts.maecomproject.databinding.ActivityOrderPlacingBinding;

import java.util.Calendar;
import java.util.Random;
import java.util.UUID;

public class OrderPlacingActivity extends AppCompatActivity {
    ActivityOrderPlacingBinding binding;
    int mainTotal = 0;
    private String name, address, cityName, postalCode, number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderPlacingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.name.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

        binding.placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = binding.name.getText().toString();
                address = binding.address.getText().toString();
                cityName = binding.cityName.getText().toString();
                number = binding.number.getText().toString();

                placeOrder();
            }
        });
    }

    private void placeOrder() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Placing");
        progressDialog.setMessage("your Order");
        progressDialog.show();

        String orderNumber = String.valueOf(getRandomNumber(100000, 999999));
        OrderModel orderModel = new OrderModel(orderNumber
                ,name,number,cityName,address,String.valueOf(mainTotal),"220",null,
                "Tcs",String.valueOf(Calendar.getInstance().getTimeInMillis()), "Pending", FirebaseAuth.getInstance().getUid());

        FirebaseFirestore.getInstance()
                .collection("orders")
                .document(orderNumber)
                .set(orderModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        for(int i =0;i<cartsItemList.size();i++){
                            CartModel cartModel=cartsItemList.get(i);
                            cartModel.setOrderNumber(orderNumber);

                            String id = UUID.randomUUID().toString();
                            cartModel.setCartId(id);

                            FirebaseFirestore.getInstance()
                                    .collection("orderProducts")
                                    .document(id)
                                    .set(cartModel);
                        }
                        finish();
                        progressDialog.cancel();
                    }
                });
    }

    public static int getRandomNumber(int min, int max) {
        return (new Random()).nextInt((max - min) + 1) + min;
    }

    @Override
    protected void onStart() {
        super.onStart();
        for (int i = 0; i < cartsItemList.size(); i++) {
            CartModel cartModel = cartsItemList.get(i);
            int price = Integer.parseInt(cartModel.getProductPrice());
            int qty = Integer.parseInt(cartModel.getProductQty());
            int total = price * qty;
            mainTotal += total;

        }
        binding.expense.setText(String.valueOf(mainTotal));
        binding.delivery.setText("220");
        binding.totalCod.setText(String.valueOf(mainTotal + 220));
    }

}