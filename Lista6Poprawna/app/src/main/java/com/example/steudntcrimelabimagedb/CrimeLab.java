package com.example.steudntcrimelabimagedb;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class CrimeLab {
    private static CrimeLab sCrimeLab;

    private List<Crime> mCrimes;
    private DBHandler mDbHandler;

    public static CrimeLab get(Context context)  {
        if (sCrimeLab == null)
        {
            sCrimeLab = new CrimeLab(context);
        }

        return sCrimeLab;
    }

    private CrimeLab(Context context)
    {
        mCrimes = new ArrayList<>();
    }

    public List<Crime> getCrimes() {
        return mCrimes;
    }

    public Crime getCrime(UUID id) {
        for (Crime crime : mCrimes) {
            if (crime.getId().equals(id)) {
                return crime;
            }
        }

        return null;
    }

    public void addCrime(Crime newCrime)
    {
        mCrimes.add(newCrime);
    }

    public void deleteCrime(UUID id)
    {
        for (Crime c : mCrimes)
        {
            if (c.getId() == id)
            {
                mCrimes.remove(c);
                break;
            }
        }
    }
}
