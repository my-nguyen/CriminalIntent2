package com.bignerdranch.android.criminalintent2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by My on 1/25/2016.
 */
public class CrimeListFragment extends Fragment {
   private RecyclerView mCrimeRecyclerView;
   private CrimeAdapter mAdapter;

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.fragment_crime_list, container, false);

      mCrimeRecyclerView = (RecyclerView)view.findViewById(R.id.crime_recycler_view);
      // RecyclerView does not do the job of positioning items on the screen itself. it delegates
      // that out to the LayoutManager. the LayoutManager handles the positioning of items and also
      // defines the scrolling behavior. so if the LayoutManager is not there, RecyclerView will
      // just fall over and die.
      mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
      updateUI();

      return view;
   }

   @Override
   public void onResume() {
      super.onResume();
      updateUI();
   }

   private void updateUI() {
      CrimeLab crimeLab = CrimeLab.get(getActivity());
      List<Crime> crimes = crimeLab.getCrimes();

      if (mAdapter == null) {
         // create a CrimeAdapter
         mAdapter = new CrimeAdapter(crimes);
         // connect the CrimeAdapter to the RecyclerView
         mCrimeRecyclerView.setAdapter(mAdapter);
      }
      else
         // inform the adapter that the data has changed; an instance of this occurrence is when
         // user selects a Crime from the list, updates its contents, then returns to the list. the
         // list must display updates of the selected Crime.
         mAdapter.notifyDataSetChanged();
   }

   // this class handles touch events
   private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
      private TextView  mTitleTextView;
      private TextView  mDateTextView;
      private CheckBox  mSolvedCheckBox;
      private Crime     mCrime;

      // itemView is the View for the entire row
      public CrimeHolder(View itemView) {
         super(itemView);
         // set CrimeHolder as the receiver of click events
         itemView.setOnClickListener(this);

         mTitleTextView = (TextView)itemView.findViewById(R.id.list_item_crime_title_text_view);
         mDateTextView = (TextView)itemView.findViewById(R.id.list_item_crime_date_text_view);
         mSolvedCheckBox = (CheckBox)itemView.findViewById(R.id.list_item_crime_solved_check_box);
      }

      public void bindCrime(Crime crime) {
         mCrime = crime;
         mTitleTextView.setText(crime.getTitle());
         mDateTextView.setText(crime.getDate().toString());
         mSolvedCheckBox.setChecked(crime.isSolved());
      }

      @Override
      public void onClick(View view) {
         // for now, show a Toast when the user selects a Crime
         // Toast.makeText(getActivity(), mCrime.getTitle() + " clicked!", Toast.LENGTH_SHORT).show();
         // create an explicit Intent that names the CrimeActivity class; use getActivity() to pass
         // the hosting activity as the Context object that the Intent constructor requires.
         Intent intent = CrimeActivity.newIntent(getActivity(), mCrime.getId());
         startActivity(intent);
      }
   }

   private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {
      private List<Crime> mCrimes;

      public CrimeAdapter(List<Crime> crimes) {
         mCrimes = crimes;
      }

      @Override
      // this method is called by the RecyclerView when it needs a new View to display an item
      public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         LayoutInflater inflater = LayoutInflater.from(getActivity());
         // create a View: inflate a layout from the Android standard library called
         // simple_list_item_1. this layout contains a single TextView, styled to look nice in a list
         // View view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
         // use the new list_item_crime layout instead of the Android standard layout simple_list_item_1
         View view = inflater.inflate(R.layout.list_item_crime, parent, false);
         // wrap the View in a ViewHolder
         return new CrimeHolder(view);
      }

      @Override
      // this method will bind a ViewHolder’s View to your model object. it receives the ViewHolder
      // and a position in your data set.
      public void onBindViewHolder(CrimeHolder holder, int position) {
         // use that position to find the right model data
         Crime crime = mCrimes.get(position);
         // update the View to reflect that model data
         // holder.mTitleTextView.setText(crime.getTitle());
         holder.bindCrime(crime);
      }

      @Override
      public int getItemCount() {
         return mCrimes.size();
      }
   }
}
