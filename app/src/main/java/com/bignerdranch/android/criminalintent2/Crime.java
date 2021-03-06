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
   private String    mSuspect;

   public Crime() {
      this(UUID.randomUUID());
   }

   public Crime(UUID id) {
      mId = id;
      mDate = new Date();
   }

   public String getPhotoFilename() {
      // the filename will be unique since it's based on the Crime's ID
      return "IMG_" + getId().toString() + ".jpg";
   }

   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append(mTitle).append(", ").append(mDate).append(", ").append(mSolved ? "SOLVED" : "UNSOLVED");
      return builder.toString();
   }

   public String getSuspect() {
      return mSuspect;
   }

   public void setSuspect(String mSuspect) {
      this.mSuspect = mSuspect;
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
