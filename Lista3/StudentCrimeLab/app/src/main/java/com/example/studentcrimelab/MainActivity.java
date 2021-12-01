package com.example.studentcrimelab;

import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CrimeLabAdapter.ItemClickListener {

    // https://stackoverflow.com/questions/40584424/simple-android-recyclerview-example

    private List<Crime> mCrimes = CrimeLab.get(this).getCrimes();
    private CrimeLabAdapter mCrimeLabAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.rvCrimeLab);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mCrimeLabAdapter = new CrimeLabAdapter(this, mCrimes);
        mCrimeLabAdapter.setClickListener(this);
        recyclerView.setAdapter(mCrimeLabAdapter);
    }


    @Override
    protected void onResume()
    {
        super.onResume();

        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.rvCrimeLab);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mCrimeLabAdapter = new CrimeLabAdapter(this, mCrimes);
        mCrimeLabAdapter.setClickListener(this);
        recyclerView.setAdapter(mCrimeLabAdapter);
    }

    @Override
    public void onItemClick(View view, int position)
    {
        Intent newActivity = new Intent(view.getContext(), CrimeActivity.class);
        newActivity.putExtra("CrimeID", mCrimeLabAdapter.getItem(position).getId().toString());
        view.getContext().startActivity(newActivity);
    }
}