package com.bignerdranch.android.criminalintent2.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.bignerdranch.android.criminalintent2.Crime;
import com.bignerdranch.android.criminalintent2.database.CrimeDbSchema.CrimeTable;

import java.util.Date;
import java.util.UUID;

/**
 * Created by My on 1/26/2016.
 */
// this class wraps a Cursor received from another place and adds new methods on top of it
public class CrimeCursorWrapper extends CursorWrapper {
   public CrimeCursorWrapper(Cursor cursor) {
      super(cursor);
   }

   // this method creates and returns a Crime object based on the id, title, date and solved columns
   // in the current row in the query result (cursor)
   public Crime getCrime() {
      // collect id, title, date and solved from the current record in the cursor
      String uuidString = getString(getColumnIndex(CrimeTable.Cols.UUID));
      String title = getString(getColumnIndex(CrimeTable.Cols.TITLE));
      long date = getLong(getColumnIndex(CrimeTable.Cols.DATE));
      int solved = getInt(getColumnIndex(CrimeTable.Cols.SOLVED));
      String suspect = getString(getColumnIndex(CrimeTable.Cols.SUSPECT));

      // build a Crime object based on the id, title, date and solved just retrieved
      Crime crime = new Crime(UUID.fromString(uuidString));
      crime.setTitle(title);
      crime.setDate(new Date(date));
      crime.setSolved(solved != 0);
      crime.setSuspect(suspect);

      return crime;
   }
}
