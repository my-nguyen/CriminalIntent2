package com.bignerdranch.android.criminalintent2;

import java.util.Date;
import java.util.UUID;

/**
 * Created by My on 1/25/2016.
 */
public class Crime {
   private UUID      mId;
   private String    mTitle;
   private Date      mDate;
   private boolean   mSolved;

   public Crime() {
      // generate unique identifier
      mId = UUID.randomUUID();
      mDate = new Date();
   }

   public Date getDate() {
      return mDate;
   }

   public void setDate(Date mDate) {
      this.mDate = mDate;
   }

   public boolean isSolved() {
      return mSolved;
   }

   public void setSolved(boolean mSolved) {
      this.mSolved = mSolved;
   }

   public UUID getId() {
      return mId;
   }

   public String getTitle() {
      return mTitle;
   }

   public void setTitle(String mTitle) {
      this.mTitle = mTitle;
   }
}
