package com.bignerdranch.android.criminalintent2.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bignerdranch.android.criminalintent2.database.CrimeDbSchema.CrimeTable;

/**
 * Created by My on 1/26/2016.
 */
// SQLiteOpenHelper is designed to get rid of the grunt work of opening a SQLiteDatabase
public class CrimeBaseHelper extends SQLiteOpenHelper {
   private static final int      VERSION = 1;
   private static final String   DATABASE_NAME = "crimeBase.db";

   public CrimeBaseHelper(Context context) {
      super(context, DATABASE_NAME, null, VERSION);
   }

   @Override
   public void onCreate(SQLiteDatabase db) {
      // create the Crime table
      db.execSQL("create table " + CrimeTable.NAME + "(" +  " _id integer primary key autoincrement, " +
      CrimeTable.Cols.UUID + ", " + CrimeTable.Cols.TITLE + ", " + CrimeTable.Cols.DATE + ", " +
      CrimeTable.Cols.SOLVED + ", " + CrimeTable.Cols.SUSPECT + ")");
   }

   @Override
   // this method is ignored, since CriminalIntent will only have one version
   public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
   }
}
