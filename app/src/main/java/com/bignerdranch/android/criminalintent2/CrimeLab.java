package com.bignerdranch.android.criminalintent2;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by My on 1/25/2016.
 */
public class CrimeLab {
   private static CrimeLab sCrimeLab;
   private List<Crime>  mCrimes;

   public static CrimeLab get(Context context) {
      if (sCrimeLab == null)
         sCrimeLab = new CrimeLab(context);
      return sCrimeLab;
   }

   private CrimeLab(Context context) {
      mCrimes = new ArrayList<>();
      // good-bye, random crimes
   }

   public List<Crime> getCrimes() {
      return mCrimes;
   }

   public Crime getCrime(UUID id) {
      for (Crime crime : mCrimes)
         if (crime.getId().equals(id))
            return crime;
      return null;
   }

   public void addCrime(Crime crime) {
      mCrimes.add(crime);
   }
}
