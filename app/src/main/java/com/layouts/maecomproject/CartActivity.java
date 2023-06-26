package com.layouts.maecomproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.layouts.maecomproject.databinding.ActivityCartBinding;

import org.w3c.dom.Document;

import java.util.List;

public class CartActivity extends AppCompatActivity {
    ActivityCartBinding binding;
    private CartAdapter cartAdapter;
    public static List<CartModel> cartsItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        cartAdapter=new CartAdapter(this);
        binding.cardRecycler.setAdapter(cartAdapter);
        binding.cardRecycler.setLayoutManager(new LinearLayoutManager(this));
        getCartItems();

        binding.proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartsItemList= cartAdapter.getSelectedItems();
                startActivity(new Intent(CartActivity.this, OrderPlacingActivity.class));
            }
        });
    }


    private void getCartItems() {
        FirebaseFirestore.getInstance()
                .collection("cart")
                .whereEqualTo("sellerUid", FirebaseAuth.getInstance().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> dsList= queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot ds:dsList){
                            CartModel cartModel=ds.toObject(CartModel.class);
                            cartAdapter.addProduct(cartModel);
                        }
                    }
                });
    }
}