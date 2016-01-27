package com.bignerdranch.android.criminalintent2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;
import java.util.UUID;

/**
 * Created by My on 1/25/2016.
 */
// this class is a replacement for CrimeActivity; it is also invoked by CrimeListFragment via
// startActivity(Intent). it lets users navigate between list items by swiping across the screen to
// "page" forward or backward through the crimes.
// extends from AppCompatActivity (as opposed to FragmentActivity previously) to have a toolbar
public class CrimePagerActivity extends AppCompatActivity {
   private ViewPager   mViewPager;
   private List<Crime> mCrimes;
   private static final String EXTRA_CRIME_ID = "com.bignerdranch.android.criminalintent2.crime_id";

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_crime_pager);

      UUID crimeId = (UUID)getIntent().getSerializableExtra(EXTRA_CRIME_ID);

      // find ViewPager in the Activity's view
      mViewPager = (ViewPager)findViewById(R.id.activity_crime_pager_view_pager);
      // get data set (list of Crimes) from CrimeLab
      mCrimes = CrimeLab.get(this).getCrimes();
      // get FragmentManager
      FragmentManager manager = getSupportFragmentManager();
      // set as adapter FragmentStatePagerAdapter, the agent managing the conversation with ViewPager
      mViewPager.setAdapter(new FragmentStatePagerAdapter(manager) {
         @Override
         public Fragment getItem(int position) {
            // fetch the Crime instance for the given position in the data set
            Crime crime = mCrimes.get(position);
            // use the Crime's ID to create and return a properly configured CrimeFragment
            return CrimeFragment.newInstance(crime.getId());
         }

         @Override
         // this method returns the number of items in the array list
         public int getCount() {
            return mCrimes.size();
         }
      });

      // by default, the ViewPager shows the first item in its PagerAdapter. the followings show the
      // crime that was selected by setting the ViewPager’s current item to the index of the
      // selected crime.
      // find the index of the crime to display by looping through
      for (int i = 0; i < mCrimes.size(); i++)
         // and checking each crime’s ID
         if (mCrimes.get(i).getId().equals(crimeId)) {
            // when you find the Crime instance whose mId matches the crimeId in the intent extra,
            // set the current item to the index of that Crime
            mViewPager.setCurrentItem(i);
            break;
         }
   }

   public static Intent newIntent(Context context, UUID crimeId) {
      Intent intent = new Intent(context, CrimePagerActivity.class);
      intent.putExtra(EXTRA_CRIME_ID, crimeId);
      return intent;
   }
}
