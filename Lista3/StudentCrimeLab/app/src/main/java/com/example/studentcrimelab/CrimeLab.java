package com.example.studentcrimelab;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrimeLab {
    private static CrimeLab sCrimeLab;

    private List<Crime> mCrimes;

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
        for (int i = 0; i < 50; i++)
        {
            Crime crime = new Crime();
            crime.seTitle("Crime #" + i);
            crime.setSolved(i % 2 == 0);
            mCrimes.add(crime);
        }
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
