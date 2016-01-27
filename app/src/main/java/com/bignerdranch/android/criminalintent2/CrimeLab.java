package com.bignerdranch.android.criminalintent2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.bignerdranch.android.criminalintent2.database.CrimeBaseHelper;
import com.bignerdranch.android.criminalintent2.database.CrimeCursorWrapper;
import com.bignerdranch.android.criminalintent2.database.CrimeDbSchema;
import com.bignerdranch.android.criminalintent2.database.CrimeDbSchema.CrimeTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by My on 1/25/2016.
 */
public class CrimeLab {
   private static CrimeLab sCrimeLab;
   private Context         mContext;
   private SQLiteDatabase  mDatabase;

   public static CrimeLab get(Context context) {
      if (sCrimeLab == null)
         sCrimeLab = new CrimeLab(context);
      return sCrimeLab;
   }

   private CrimeLab(Context context) {
      // save the Application Context, which exists during the lifetime of the application because
      // CrimeLab is a singleton. on the other hand, because an Activity comes and goes, so
      // maintaining a handle to an Activity as a Context would cause problems because the Activity
      // would never be cleaned up by the garbage collector
      mContext = context.getApplicationContext();
      // persist Crime data in a local database as opposed to in memory previously
      // getWritableDatabase() opens/creates database file
      mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();
   }

   public List<Crime> getCrimes() {
      List<Crime> crimes = new ArrayList<>();
      // retrieve all records
      CrimeCursorWrapper cursor = queryCrimes(null, null);
      try {
         // position cursor at the first element
         cursor.moveToFirst();
         // cursor is not at the end of data set
         while (!cursor.isAfterLast()) {
            // crimes.add(cursor.getCrime());
            Crime crime = cursor.getCrime();
            crimes.add(crime);
            // advance cursor to new row
            cursor.moveToNext();
         }
      } finally {
         // must close cursor; otherwise Android would spit out nasty error messages
         cursor.close();
      }

      return crimes;
   }

   public Crime getCrime(UUID id) {
      // search the database for a row with matching id
      CrimeCursorWrapper cursor = queryCrimes(CrimeTable.Cols.UUID + " = ?", new String[] { id.toString() });
      try {
         // no result from the database
         if (cursor.getCount() == 0)
            return null;
         else {
            // position the cursor to first record found
            cursor.moveToFirst();
            // return a Crime object based on the record returned from the database
            return cursor.getCrime();
         }
      } finally {
         cursor.close();
      }
   }

   public void addCrime(Crime crime) {
      ContentValues values = getContentValues(crime);
      // insert ContentValues into CrimeTable
      mDatabase.insert(CrimeTable.NAME, null, values);
   }

   public void updateCrime(Crime crime) {
      String uuidString = crime.getId().toString();
      ContentValues values = getContentValues(crime);
      // update Crime table with ContentValues where UUID is the Crime ID
      mDatabase.update(CrimeTable.NAME, values, CrimeTable.Cols.UUID + " = ?", new String[]{uuidString});
   }

   // ContentValues is a helper class for writing key-value pairs, specifically designed to store
   // data into SQLite, and where keys are column names
   private static ContentValues getContentValues(Crime crime) {
      ContentValues values = new ContentValues();
      values.put(CrimeTable.Cols.UUID, crime.getId().toString());
      values.put(CrimeTable.Cols.TITLE, crime.getTitle());
      values.put(CrimeTable.Cols.DATE, crime.getDate().getTime());
      values.put(CrimeTable.Cols.SOLVED, crime.isSolved() ? 1 : 0);
      return values;
   }

   private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
      Cursor cursor = mDatabase.query(
            CrimeTable.NAME,
            null,    // columns - null selects all columns
            whereClause,
            whereArgs,
            null,    // groupBy
            null,    // having
            null     // orderBy
      );

      return new CrimeCursorWrapper(cursor);
   }
}
