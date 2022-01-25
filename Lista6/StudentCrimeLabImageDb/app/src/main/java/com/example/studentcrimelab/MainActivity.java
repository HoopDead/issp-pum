package com.example.studentcrimelab;

import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements CrimeLabAdapter.ItemClickListener {

    // https://stackoverflow.com/questions/40584424/simple-android-recyclerview-example

    private RecyclerView recyclerView;
    // private List<Crime> mCrimes = CrimeLab.get(this).getCrimes();
    private List<Crime> mCrimes = CrimeLab.get(this).getCrimes();
    private CrimeLab mCrimeLab;
    private CrimeLabAdapter mCrimeLabAdapter;
    private DBHandler mDbHandler;
    private SearchView svCrimeSearcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDbHandler = new DBHandler(this);
        mCrimeLab.get(this);
        initialize();

        recyclerView = findViewById(R.id.rvCrimeLab);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mCrimeLabAdapter = new CrimeLabAdapter(this, mCrimes);
        mCrimeLabAdapter.setClickListener(this);
        recyclerView.setAdapter(mCrimeLabAdapter);

        svCrimeSearcher = findViewById(R.id.svCrimeSearcher);
        svCrimeSearcher.setQueryHint("Insert title to search.");

        svCrimeSearcher.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mCrimeLabAdapter.getFilter().filter(newText);
                return false;
            }
        });

        svCrimeSearcher.setOnCloseListener(() -> {
            initialize();
            mCrimeLabAdapter.notifyDataSetChanged();
            return false;
        });
    }


    @Override
    protected void onResume()
    {
        super.onResume();
        initialize();
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onItemClick(View view, int position)
    {
        Intent newActivity = new Intent(view.getContext(), CrimeActivity.class);
        newActivity.putExtra("CrimeID", mCrimeLabAdapter.getItem(position).getId().toString());
        view.getContext().startActivity(newActivity);
    }

    public void buttonCrimeAddClicked(View view)
    {
        Crime newCrime = new Crime();
        newCrime.setTitle("Empty Crime.");
        newCrime.setSolved(false);
        mDbHandler.addCrime(newCrime);
        mCrimeLabAdapter.notifyDataSetChanged();

        Cursor c = mDbHandler.getCrimes();
        System.out.println(c.getCount());
    }

    public void buttonCrimeDeleteClicked(View view)
    {
        try
        {
            mDbHandler.deleteCrime(mCrimes.get(mCrimes.size() - 1));
        }
        catch (Exception e)
        {

        }
        mCrimeLabAdapter.notifyDataSetChanged();
    }

    private void initialize()
    {
        Cursor c = mDbHandler.getCrimes();
        mCrimes.clear();

        if (c.getCount() != 0)
        {
            System.out.println("[INFO] Loading crimes.");
            System.out.println(c.getCount());
            while (c.moveToNext())
            {
                UUID id = UUID.fromString(c.getString(1));
                String title = c.getString(2);
                Date d = new Date(c.getString(3)); // Probably depracted, but who cares? It's not log4j.
                int solved = c.getInt(4);
                boolean s = solved > 0 ? true : false; // It's a bit hackish, but works.
                mCrimes.add(new Crime(id, title, d, s));
            }
        }
    }
}