package com.bignerdranch.android.criminalintent2;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

public class CrimeActivity extends FragmentActivity {

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_crime);

      // FragmentManager maintains a back stack of fragment transactions that you can navigate
      FragmentManager manager = getSupportFragmentManager();
      // ask the FragmentManager for the fragment with a container view ID of R.id.fragment_container
      Fragment fragment = manager.findFragmentById(R.id.fragment_container);
      // there is no fragment with the given container view ID
      if (fragment == null) {
         fragment = new CrimeFragment();
         // create a new fragment transaction, include one add operation in it, and then commit it
         manager.beginTransaction().add(R.id.fragment_container, fragment).commit();
      }
   }
}
