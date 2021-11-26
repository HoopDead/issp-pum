package com.example.studentcrimelab;

import java.util.UUID;
import java.util.Date;

public class Crime {

    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;

    public Crime()
    {
        this.mId = UUID.randomUUID();
        this.mDate = new Date();
    }

    public void seTitle(String string) { this.mTitle = string; }

    public void setSolved(boolean solved) { this.mSolved = solved; }

    public UUID getId() {  return this.mId; }
}
