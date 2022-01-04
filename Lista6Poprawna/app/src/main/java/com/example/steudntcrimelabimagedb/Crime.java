package com.example.steudntcrimelabimagedb;

import java.util.UUID;
import java.util.Date;

public class Crime {

    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;
    private String mImage;

    public Crime()
    {
        this.mId = UUID.randomUUID();
        this.mDate = new Date();
    }

    public Crime(UUID id, String title, Date date, boolean solved, String image)
    {
        this.mId = id;
        this.mTitle = title;
        this.mDate = date;
        this.mSolved = solved;
        this.mImage = image;
    }

    public void setTitle(String string) { this.mTitle = string; }
    public String getTitle() { return this.mTitle; }

    public void setSolved(boolean solved) { this.mSolved = solved; }
    public boolean isSolved() { return this.mSolved; }

    public void setDate(Date newDate) { this.mDate = newDate; }
    public Date getDate() { return this.mDate; }

    public void setImage(String image) { this.mImage = image; }
    public String getImage() { return mImage; }

    public UUID getId() {  return this.mId; }
}
