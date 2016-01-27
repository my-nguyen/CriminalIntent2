package com.bignerdranch.android.criminalintent2.database;

/**
 * Created by My on 1/26/2016.
 */
public class CrimeDbSchema {
   // this inner class only exists to define the String constants needed to describe the moving
   // pieces of your table definition.
   public static final class CrimeTable {
      // the first piece of that definition is the name of the table: CrimeTable.NAME
      public static final String NAME = "crimes";

      // describe the columns
      public static final class Cols {
         public static final String UUID = "uuid";
         public static final String TITLE = "title";
         public static final String DATE = "date";
         public static final String SOLVED = "solved";
         public static final String SUSPECT = "suspect";
      }
   }
}
