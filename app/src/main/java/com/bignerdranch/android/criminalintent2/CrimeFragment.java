package com.bignerdranch.android.criminalintent2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.UUID;

/**
 * Created by My on 1/25/2016.
 */
public class CrimeFragment extends Fragment {
   private Crime     mCrime;
   private EditText  mTitleField;
   private Button    mDateButton;
   private CheckBox  mSolvedCheckBox;
   private static final String   ARG_CRIME_ID = "crime_id";

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      // passing data from Activity to Fragment, first and shortcut approach: access to
      // the activity's intent from this Fragment via getIntent(). having the fragment access the
      // intent that belongs to the hosting activity makes for simple code. however, it costs you
      // the encapsulation of your fragment. CrimeFragment is no longer a reusable building block
      // because it expects that it will always be hosted by an activity whose Intent defines an
      // extra named com.bignerdranch.android.criminalintent.crime_id.
      // UUID crimeId = (UUID)getActivity().getIntent().getSerializableExtra(CrimeActivity.EXTRA_CRIME_ID);
      UUID crimeId = (UUID)getArguments().getSerializable(ARG_CRIME_ID);
      mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
   }

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.fragment_crime, container, false);

      mTitleField = (EditText)view.findViewById(R.id.crime_title);
      // now that CrimeFragment fetches a Crime based on a crime ID passed from CrimeActivity, its
      // view can display the Crime's title (and solved status below)
      mTitleField.setText(mCrime.getTitle());
      mTitleField.addTextChangedListener(new TextWatcher() {
         @Override
         public void beforeTextChanged(CharSequence s, int start, int count, int after) {
         }
         @Override
         // this method is the only one to care about; the other 2 methods are ignored. this method
         // sets the Crime title to the text entered by the user.
         public void onTextChanged(CharSequence s, int start, int before, int count) {
            mCrime.setTitle(s.toString());
         }
         @Override
         public void afterTextChanged(Editable s) {
         }
      });

      mDateButton = (Button)view.findViewById(R.id.crime_date);
      mDateButton.setText(mCrime.getDate().toString());
      // ensure the button will not respond in any way to the user pressing it
      mDateButton.setEnabled(false);

      mSolvedCheckBox = (CheckBox)view.findViewById(R.id.crime_solved);
      mSolvedCheckBox.setChecked(mCrime.isSolved());
      mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
         @Override
         public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            // set the Crime's solved property
            mCrime.setSolved(isChecked);
         }
      });

      return view;
   }

   // passing data from Activity to Fragment, second and better approach: to stash the crime ID
   // someplace that belongs to CrimeFragment rather than keeping it in CrimeActivity’s personal
   // space. the CrimeFragment could then retrieve this data without relying on the presence of a
   // particular extra in the activity’s intent. the “someplace” that belongs to a fragment is known
   // as its arguments bundle.
   public static CrimeFragment newInstance(UUID crimeId) {
      Bundle args = new Bundle();
      args.putSerializable(ARG_CRIME_ID, crimeId);
      CrimeFragment fragment = new CrimeFragment();
      fragment.setArguments(args);
      return fragment;
   }
}
