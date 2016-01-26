package com.bignerdranch.android.criminalintent2;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by My on 1/25/2016.
 */
public class DatePickerFragment extends DialogFragment {
   private static final String   ARG_DATE = "date";
   public static final String    EXTRA_DATE = "com.bignerdranch.android.criminalintent2.date";
   private DatePicker mDatePicker;

   @Override
   public Dialog onCreateDialog(Bundle savedInstanceState) {
      // extract the Date object passed from CrimeFragment
      Date date = (Date)getArguments().getSerializable(ARG_DATE);
      // extract year, month, day from the Date object via a Calendar object
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(date);
      int year = calendar.get(Calendar.YEAR);
      int month = calendar.get(Calendar.MONTH);
      int day = calendar.get(Calendar.DAY_OF_MONTH);
      // inflate the DatePicker view
      View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date, null);
      // initialize the DatePicker view with year, month, day
      mDatePicker = (DatePicker)view.findViewById(R.id.dialog_date_date_picker);
      mDatePicker.init(year, month, day, null);

      return new AlertDialog.Builder(getActivity())
            // display the DatePicker view between the dialog's title and its buttons.
            .setView(view)
            // set the dialog's title
            .setTitle(R.string.date_picker_title)
            // pass in Android OK string resource and an implementation of DialogInterface.OnClickLister
            // .setPositiveButton(android.R.string.ok, null)
            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which) {
                  // retrieve year, month, day from the DatePicker
                  int year = mDatePicker.getYear();
                  int month = mDatePicker.getMonth();
                  int day = mDatePicker.getDayOfMonth();
                  // build a Date object out of year, month, day
                  Date date = new GregorianCalendar(year, month, day).getTime();
                  // send the Date object back to CrimeFragment
                  sendResult(Activity.RESULT_OK, date);
               }
            })
                  // finish building the dialog
            .create();
   }

   public static DatePickerFragment newInstance(Date date) {
      Bundle args = new Bundle();
      args.putSerializable(ARG_DATE, date);
      DatePickerFragment fragment = new DatePickerFragment();
      fragment.setArguments(args);
      return fragment;
   }

   // this method sends a Date object back from DatePickerFragment to the target fragment (CrimeFragment)
   private void sendResult(int resultCode, Date date) {
      if (getTargetFragment() != null) {
         // create an Intent to stuff a Date object in it
         Intent intent = new Intent();
         intent.putExtra(EXTRA_DATE, date);
         // call onActivityResult() directly, passing a request code, a result code, and the Intent
         // that has data being sent back
         getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
      }
   }
}
