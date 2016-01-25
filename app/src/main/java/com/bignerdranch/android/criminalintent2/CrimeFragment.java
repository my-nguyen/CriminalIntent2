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

/**
 * Created by My on 1/25/2016.
 */
public class CrimeFragment extends Fragment {
   private Crime     mCrime;
   private EditText  mTitleField;
   private Button    mDateButton;
   private CheckBox  mSolvedCheckBox;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      mCrime = new Crime();
   }

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.fragment_crime, container, false);

      mTitleField = (EditText)view.findViewById(R.id.crime_title);
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
      mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
         @Override
         public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            // set the Crime's solved property
            mCrime.setSolved(isChecked);
         }
      });

      return view;
   }
}
