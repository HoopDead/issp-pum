package com.example.studentcrimelab;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class CrimeActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    CrimeLab mCrimeLab;
    Crime mCurrentCrime;
    private DBHandler mDbHandler;

    TextView tvUUID;
    EditText etCrimeTitle;
    Button bCrimeDate;
    CheckBox cbCrimeSolved;
    ImageView ivCrimeImage;
    ImageButton ibMakeCrimePhoto;

    int mYear;
    int mMonth;
    int mDay;
    int mHour;
    int mMinutes;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crime_activity);

        mDbHandler = new DBHandler(this);

        mCrimeLab = CrimeLab.get(this);
        Intent intent = getIntent();

        UUID CrimeID = UUID.fromString(getIntent().getStringExtra("CrimeID"));
        mCurrentCrime = mCrimeLab.getCrime(CrimeID);

        tvUUID = findViewById(R.id.tvUUID);
        tvUUID.setText(CrimeID.toString());

        etCrimeTitle = findViewById(R.id.etCrimeTitle);
        etCrimeTitle.setText(mCurrentCrime.getTitle());

        bCrimeDate = findViewById(R.id.bCrimeDate);
        bCrimeDate.setText(mCurrentCrime.getDate().toString());

        cbCrimeSolved = findViewById(R.id.cbCrimeSolved);
        cbCrimeSolved.setChecked(mCurrentCrime.isSolved());

        ibMakeCrimePhoto = findViewById(R.id.ibMakeCrimePhoto);

        ivCrimeImage = findViewById(R.id.ivCrimeImage);
        if (mCurrentCrime.getImage() == null)
        {

        }
        else
        {
            ivCrimeImage.setImageBitmap(BitmapFactory.decodeFile(mCurrentCrime.getImage()));
        }
        // if (mCurrentCrime.getImage().isEmpty())
        // {
        // }
        // else
        // {
            // ivCrimeImage.setImageBitmap(BitmapFactory.decodeFile(mCurrentCrime.getImage()));
        // }
    }

    @Override
    public void onPause()
    {
        super.onPause();

        // Update title.
        etCrimeTitle = findViewById(R.id.etCrimeTitle);
        if (etCrimeTitle.getText().toString() != mCurrentCrime.getTitle())
        {
            mCurrentCrime.setTitle(etCrimeTitle.getText().toString());
            mDbHandler.updateCrime(mCurrentCrime.getId(), mCurrentCrime.getTitle(), mCurrentCrime.getDate(), mCurrentCrime.isSolved(), mCurrentCrime.getImage());
        }
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
        mDbHandler.updateCrime(mCurrentCrime.getId(), mCurrentCrime.getTitle(), mCurrentCrime.getDate(), mCurrentCrime.isSolved(), mCurrentCrime.getImage());
    }

    public void checkboxCrimeSolvedClicked(View view)
    {
        System.out.println("Checkbox solved clicked.");

        boolean solved = cbCrimeSolved.isChecked();
        mCurrentCrime.setSolved(solved);
        mDbHandler.updateCrime(mCurrentCrime.getId(), mCurrentCrime.getTitle(), mCurrentCrime.getDate(), mCurrentCrime.isSolved(), mCurrentCrime.getImage());
    }

    public void buttonCrimeDeleteThisClicked(View view)
    {
        mDbHandler.deleteCrime(mCurrentCrime);
        finish();
    }

    // public void buttonCrimeDeleteClicked(View view)
    // {
        // System.out.println("Crime Delete button clicked.");
        // mCrimeLab.deleteCrime(mCurrentCrime.getId());
        // mDbHandler.deleteCrime(mCurrentCrime.getId());
        // finish();
    // }
}
