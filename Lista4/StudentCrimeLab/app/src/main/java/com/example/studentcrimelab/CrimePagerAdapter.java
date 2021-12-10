package com.example.studentcrimelab;

import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CrimePagerAdapter extends RecyclerView.Adapter<CrimePagerAdapter.CrimeViewHolder> {

    private Context context;
    private CrimeLab mCrimeLab;

    public CrimePagerAdapter(Context context, CrimeLab crimeLab) {
        this.context = context;
        this.mCrimeLab = crimeLab;
    }

    @NonNull
    @Override
    public CrimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.crime_activity, parent, false);
        return new CrimeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CrimeViewHolder holder, int position) {
        Crime currentCrime = mCrimeLab.getCrimes().get(position);
        holder.tvUUID.setText(currentCrime.getId().toString());
        holder.etCrimeTitle.setText(currentCrime.getTitle());
        holder.bCrimeDate.setText(currentCrime.getDate().toString());
        holder.cbCrimeSolved.setChecked(currentCrime.isSolved());
    }

    @Override
    public int getItemCount() {
        return mCrimeLab.getCrimes().size();
    }

    public class CrimeViewHolder extends RecyclerView.ViewHolder {

        TextView tvUUID;
        EditText etCrimeTitle;
        Button bCrimeDate;
        CheckBox cbCrimeSolved;

        public CrimeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUUID = itemView.findViewById(R.id.tvUUID);
            etCrimeTitle = itemView.findViewById(R.id.etCrimeTitle);
            bCrimeDate = itemView.findViewById(R.id.bCrimeDate);
            cbCrimeSolved = itemView.findViewById(R.id.cbCrimeSolved);
        }
    }
}