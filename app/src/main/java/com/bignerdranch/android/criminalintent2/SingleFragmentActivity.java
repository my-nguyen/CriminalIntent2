package com.bignerdranch.android.criminalintent2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

/**
 * Created by My on 1/25/2016.
 */
public abstract class SingleFragmentActivity extends FragmentActivity {
   protected abstract Fragment createFragment();

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_fragment);

      // FragmentManager maintains a back stack of fragment transactions that you can navigate
      FragmentManager manager = getSupportFragmentManager();
      // ask the FragmentManager for the fragment with a container view ID of R.id.fragment_container
      Fragment fragment = manager.findFragmentById(R.id.fragment_container);
      Log.d("NGUYEN", "in SingleFragmentActivity.onCreate(), fragment=" + fragment);
      // if there is no fragment with the given container view ID
      if (fragment == null) {
         fragment = createFragment();
         // create a new fragment transaction, include one add operation in it, and then commit it
         manager.beginTransaction().add(R.id.fragment_container, fragment).commit();
      }
   }
}
