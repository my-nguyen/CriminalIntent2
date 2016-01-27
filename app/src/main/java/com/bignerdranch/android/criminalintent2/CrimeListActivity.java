package com.bignerdranch.android.criminalintent2;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;

/**
 * Created by My on 1/25/2016.
 */
public class CrimeListActivity extends SingleFragmentActivity
      implements CrimeListFragment.Callbacks, CrimeFragment.Callbacks {
   @Override
   // this method implements the CrimeListFragment.Callbacks interface. it's invoked when the user
   // selects a Crime from CrimeListActivity. if it's a phone interface, this method starts a new
   // CrimePagerActivity. else if it's a tablet interface, this method puts a CrimeFragment in the
   // detail_fragment_container.
   public void onCrimeSelected(Crime crime) {
      // check the interface for a resource with ID detail_fragment_container
      if (findViewById(R.id.detail_fragment_container) == null) {
         // if it's a phone interface, start a new CrimePagerActivity
         Intent intent = CrimePagerActivity.newIntent(this, crime.getId());
         startActivity(intent);
      }
      else {
         // if it's a tablet interface, replace any CrimeFragment existing on
         // detail_fragment_container with the newly selected CrimeFragment
         Fragment newDetail = CrimeFragment.newInstance(crime.getId());
         getSupportFragmentManager().beginTransaction().replace(R.id.detail_fragment_container, newDetail).commit();
      }
   }

   @Override
   public void onCrimeUpdated(Crime crime) {
      CrimeListFragment listFragment = (CrimeListFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_container);
      listFragment.updateUI();
   }

   @Override
   protected Fragment createFragment() {
      return new CrimeListFragment();
   }

   @Override
   protected int getLayoutResId() {
      // return R.layout.activity_twopane;
      // use the resource ID activity_masterdetail defined in refs.xml, which points to the single-
      // pane layout activity_fragment
      return R.layout.activity_masterdetail;
   }
}
