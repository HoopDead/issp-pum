package com.example.steudntcrimelabimagedb;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class CrimeActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private static final int CAMERA_PERMISSION = 1;
    private static final String TAG = "Camera";
    private static final int CAMERA_INTENT = 100;

    CrimeLab mCrimeLab;
    Crime mCurrentCrime;
    private DBHandler mDbHandler;
    private Uri mImagePath;

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
            ivCrimeImage.setImageResource(R.drawable.ic_blank_file_svgrepo_com);
        }
        else
        {
            ivCrimeImage.setImageBitmap(BitmapFactory.decodeFile(mCurrentCrime.getImage()));
        }
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

    public void imageButtonListener(View view)
    {
        if(!checkPermissions())
        {
            showRationaleDialog();
        }
        else
        {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAMERA_INTENT);
        }
    }

    private void showRationaleDialog(){
        new AlertDialog.Builder(this)
                .setMessage("Camera permissions needed")
                .setPositiveButton("Change permissions", (dialog, which) -> {
                    try{
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    } catch (ActivityNotFoundException e){
                        e.printStackTrace();
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
        {
            if (requestCode == CAMERA_INTENT)
            {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ivCrimeImage.setImageBitmap(thumbnail);
                mImagePath = savePicture(thumbnail);
                mCurrentCrime.setImage(mImagePath.toString());
                mDbHandler.updateCrime(mCurrentCrime.getId(), mCurrentCrime.getTitle(), mCurrentCrime.getDate(), mCurrentCrime.isSolved(), mCurrentCrime.getImage());
            }
        }
    }

    private Uri savePicture(Bitmap bitmap)
    {
        ContextWrapper wrapper = new ContextWrapper(getApplicationContext());
        File file = wrapper.getDir("myGallery", Context.MODE_PRIVATE);
        file = new File(file, UUID.randomUUID().toString() + ".jpg");

        try {
            OutputStream stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            stream.flush();
            stream.close();
        } catch (IOException e){
            e.printStackTrace();
        }

        return Uri.parse(file.getAbsolutePath());
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

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * this method request to permission asked.
     */
    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.CAMERA);

        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
        } else {
            Log.i(TAG, "Requesting permission");
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(CrimeActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    CAMERA_PERMISSION);
        }
    }
}
