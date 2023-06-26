package com.layouts.maecomproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.layouts.maecomproject.databinding.ActivityProfileBinding;

import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    ActivityProfileBinding binding;
    private OrdersAdapter ordersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.name.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        binding.email.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        ordersAdapter=new OrdersAdapter(this);
        binding.ordersRecycler.setAdapter(ordersAdapter);
        binding.ordersRecycler.setLayoutManager(new LinearLayoutManager(this));

        getOrders();

    }

    @Override
    protected void onStart() {
        super.onStart();
        getOrders();
    }

    private void getOrders(){
        FirebaseFirestore.getInstance()
                .collection("Orders")
                .whereEqualTo("uid",FirebaseAuth.getInstance().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> dsList=queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot ds:dsList){
                            OrderModel orderModel=ds.toObject(OrderModel.class);
                            ordersAdapter.addProduct(orderModel);
                        }
                    }
                });

    }
}