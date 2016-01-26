package com.bignerdranch.android.criminalintent2;

import android.support.v4.app.Fragment;
import android.util.Log;

/**
 * Created by My on 1/25/2016.
 */
public class CrimeListActivity extends SingleFragmentActivity {
   @Override
   protected Fragment createFragment() {
      Log.d("NGUYEN", "in CrimeListActivity.createFragment()");
      return new CrimeListFragment();
   }
}
