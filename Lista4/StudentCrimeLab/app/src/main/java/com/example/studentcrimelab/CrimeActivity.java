package com.example.studentcrimelab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class CrimeActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    CrimeLab mCrimeLab;
    Crime mCurrentCrime;

    TextView tvUUID;
    EditText etCrimeTitle;
    Button bCrimeDate;
    CheckBox cbCrimeSolved;

    CrimePagerAdapter crimePagerAdapter;
    ViewPager2 viewPager2;

    int mYear;
    int mMonth;
    int mDay;
    int mHour;
    int mMinutes;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        System.out.println("on create called");
        setContentView(R.layout.crime_pager_activity);
        mCrimeLab = CrimeLab.get(this);

        viewPager2 = findViewById(R.id.pager);

        UUID CrimeID = UUID.fromString(getIntent().getStringExtra("CrimeID"));
        int crimePos = mCrimeLab.getCrimes().indexOf(mCrimeLab.getCrime(CrimeID));

        // CrimePagerAdapter adapter = new CrimePagerAdapter();
        crimePagerAdapter = new CrimePagerAdapter(this, mCrimeLab);
        viewPager2.setAdapter(crimePagerAdapter);
        viewPager2.setCurrentItem(crimePos, true);

        // Intent intent = getIntent();
    }

    @Override
    public void onPause()
    {
        super.onPause();

        System.out.println("On pause called.");
        // Update title.
        // etCrimeTitle = findViewById(R.id.etCrimeTitle);
        // if (etCrimeTitle.getText().toString() != mCurrentCrime.getTitle())
        // {
            // mCurrentCrime.seTitle(etCrimeTitle.getText().toString());
        // }
    }


    public void buttonCrimeDateClicked(View view)
    {
        System.out.println("Date button clicked.");

        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(CrimeActivity.this, CrimeActivity.this, year, month, day);
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        mYear = year;
        mMonth = month;
        mDay = dayOfMonth;
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR);
        int minute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(CrimeActivity.this, CrimeActivity.this, hour, minute, DateFormat.is24HourFormat(this));
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        mHour = hourOfDay;
        mMinutes = minute;

        Date newDate = new Date(mYear - 1900, mMonth, mDay, mHour, mMinutes);
        mCurrentCrime.setDate(newDate);

        bCrimeDate.setText(newDate.toString());
    }

    public void checkboxCrimeSolvedClicked(View view)
    {
        System.out.println("Checkbox solved clicked.");

        System.out.println("Current crime index in lab: " + mCrimeLab.getCrimes().indexOf(mCurrentCrime));

        boolean solved = cbCrimeSolved.isChecked();
        mCurrentCrime.setSolved(solved);
    }

    public void buttonCrimeAddClicked(View view)
    {
        System.out.println("Crime Add button clicked.");
        Crime newCrime = new Crime();
        newCrime.seTitle("Crime #" + mCrimeLab.getCrimes().size());
        newCrime.setSolved(false);
        mCrimeLab.addCrime(newCrime);
        finish();
    }

    public void buttonCrimeDeleteClicked(View view) {
        System.out.println("Crime Delete button clicked.");
        mCrimeLab.deleteCrime(mCurrentCrime.getId());
        finish();
    }

    public void buttonSwitchToFirstClicked(View view)
    {
        viewPager2.setCurrentItem(0);
    }

    public void buttonSwitchToLastClicked(View view)
    {
        viewPager2.setCurrentItem(mCrimeLab.getSize());
    }

}
