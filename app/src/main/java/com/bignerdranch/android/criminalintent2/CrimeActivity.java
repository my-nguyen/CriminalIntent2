package com.bignerdranch.android.criminalintent2;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.UUID;

public class CrimeActivity extends SingleFragmentActivity {
   public static final String EXTRA_CRIME_ID = "com.bignerdranch.android.criminalintent2.crime_id";

   @Override
   protected Fragment createFragment() {
      Log.d("NGUYEN", "in CrimeActivity.createFragment()");
      return new CrimeFragment();
   }

   public static Intent newIntent(Context context, UUID crimeId) {
      Intent intent = new Intent(context, CrimeActivity.class);
      intent.putExtra(EXTRA_CRIME_ID, crimeId);
      return intent;
   }
}
