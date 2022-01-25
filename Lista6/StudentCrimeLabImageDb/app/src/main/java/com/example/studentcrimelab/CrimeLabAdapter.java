package com.example.studentcrimelab;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class CrimeLabAdapter extends RecyclerView.Adapter<CrimeLabAdapter.CrimeViewHolder> implements Filterable
{

    private List<Crime> mCrimes;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    public CrimeLabAdapter(Context context, List<Crime> crimes)
    {
        this.mInflater = LayoutInflater.from(context);
        this.mCrimes = crimes;
    }


    public class CrimeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mCrimeText;
        public CrimeLabAdapter mAdapter;

        public CrimeViewHolder(@NonNull View itemView, CrimeLabAdapter adapter)
        {
            super(itemView);
            this.mCrimeText = itemView.findViewById(R.id.tvCrime);
            itemView.setOnClickListener(this);
            this.mAdapter = adapter;
        }

        @Override
        public void onClick(View view)
        {
            if (mClickListener != null)
            {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        }

    }

    @NonNull
    @Override
    public CrimeLabAdapter.CrimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = mInflater.inflate(R.layout.recyleview_row, parent, false);
        return new CrimeViewHolder(view, this);
    }

    @NonNull
    @Override
    public void onBindViewHolder(@NonNull CrimeLabAdapter.CrimeViewHolder viewHolder, int position)
    {
        Crime current = mCrimes.get(position);
        viewHolder.mCrimeText.setText(current.getTitle());
    }

    @Override
    public int getItemCount()
    {
        return mCrimes.size();
    }

    Crime getItem(int id)
    {
        return mCrimes.get(id);
    }

    void setClickListener(ItemClickListener itemClickListener)
    {
        this.mClickListener = itemClickListener;
    }

    @Override
    public Filter getFilter()
    {
        return filter;
    }

    Filter filter = new Filter()
    {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Crime> f = new ArrayList<>();

            if (constraint.toString().isEmpty())
            {
                f.addAll(mCrimes);
            }
            else
            {
                for (Crime c : mCrimes)
                {
                    if (c.getTitle().toLowerCase().contains(constraint.toString().toLowerCase()))
                    {
                        f.add(c);
                    }
                }
            }

            FilterResults fr = new FilterResults();
            fr.values = f;
            return fr;
        }

        @Override
        protected void publishResults(final CharSequence constraint, FilterResults fr)
        {
            mCrimes.clear();
            mCrimes.addAll((Collection<? extends Crime>) fr.values);
            notifyDataSetChanged();
        }
    };


    public interface ItemClickListener
    {
        void onItemClick(View view, int position);
    }
}
