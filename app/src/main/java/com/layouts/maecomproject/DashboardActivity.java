package com.layouts.maecomproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.layouts.maecomproject.databinding.ActivityDashboardBinding;

import org.w3c.dom.Document;

import java.util.List;

public class DashboardActivity extends AppCompatActivity {
    ActivityDashboardBinding binding;
    private ProductsAdapter productsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.myToolbar);
        productsAdapter=new ProductsAdapter(this);
        binding.productRecycler.setAdapter(productsAdapter);
        binding.productRecycler.setLayoutManager(new LinearLayoutManager(this));

        getProducts();

        binding.cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, CartActivity.class));
            }
        });
        binding.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, ProfileActivity.class));
            }
        });

    }

    private void getProducts() {
        FirebaseFirestore.getInstance()
                .collection("products")
                .whereEqualTo("show",true)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> dsList=queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot ds:dsList){
                            ProductModel productModel= ds.toObject(ProductModel.class);
                            productsAdapter.addProduct(productModel);
                        }
                    }
                });
    }
}