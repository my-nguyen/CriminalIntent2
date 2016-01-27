package com.bignerdranch.android.criminalintent2;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.util.Date;
import java.util.UUID;

/**
 * Created by My on 1/25/2016.
 */
public class CrimeFragment extends Fragment {
   private Crime        mCrime;
   private EditText     mTitleField;
   private Button       mDateButton;
   private CheckBox     mSolvedCheckBox;
   private Button       mReportButton;
   private Button       mSuspectButton;
   private ImageButton  mPhotoButton;
   private ImageView    mPhotoView;
   private File         mPhotoFile;
   private static final String   ARG_CRIME_ID = "crime_id";
   private static final String   DIALOG_DATE = "DialogDate";
   private static final int      REQUEST_DATE = 0;
   private static final int      REQUEST_CONTACT = 1;
   private static final int      REQUEST_PHOTO = 2;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      // extract the Crime ID from Intent's extra
      UUID crimeId = (UUID)getArguments().getSerializable(ARG_CRIME_ID);
      // fetch Crime from CrimeLab based on ID
      mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
      // get the location of the photo file
      mPhotoFile = CrimeLab.get(getActivity()).getPhotoFile(mCrime);
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
      mDateButton.setOnClickListener(new View.OnClickListener() {
         @Override
         // this method shows a DatePickerFragment when the date button is pressed
         public void onClick(View v) {
            FragmentManager manager = getFragmentManager();
            // DatePickerFragment dialog = new DatePickerFragment();
            // pass a Date object from CrimeFragment to DatePickerFragment
            DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
            // set CrimeFragment up to receive a Date object back from DatePickerFragment
            dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
            // show the Dialog on screen
            dialog.show(manager, DIALOG_DATE);
         }
      });

      mSolvedCheckBox = (CheckBox)view.findViewById(R.id.crime_solved);
      mSolvedCheckBox.setChecked(mCrime.isSolved());
      mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
         @Override
         public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            // set the Crime's solved property
            mCrime.setSolved(isChecked);
         }
      });

      // compose a crime report based on data from the current Crime object and invoke an activity
      // on the device that can send the report by way of an implicit Intent.
      mReportButton = (Button)view.findViewById(R.id.crime_report);
      mReportButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            // specify the implicit Intent's action as ACTION_SEND
            Intent intent = new Intent(Intent.ACTION_SEND);
            // the data sent is plain text
            intent.setType("text/plain");
            // stuff the text report as extra
            intent.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
            // stuff the subject as extra
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
            // specify a Chooser, which presents a list of choices of activity that can service the report
            intent = Intent.createChooser(intent, getString(R.string.send_report));
            startActivity(intent);
         }
      });

      // enable the user to pick an item from the contacts database by way of an implicit intent
      final Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
      // dummy code to verify the PackageManager filter below
      // pickContact.addCategory(Intent.CATEGORY_HOME);
      mSuspectButton = (Button)view.findViewById(R.id.crime_suspect);
      mSuspectButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            // expect to receive data (suspect name) back from the Contacts activity
            startActivityForResult(pickContact, REQUEST_CONTACT);
         }
      });
      if (mCrime.getSuspect() != null)
         // once a suspect is assigned, show the name on the suspect button
         mSuspectButton.setText(mCrime.getSuspect());
      // check to see if the device has a Contacts app; otherwise this application will crash
      PackageManager packageManager = getActivity().getPackageManager();
      // PackageManager knows about all the components installed on your device. resolveActivity()
      // asks PackageManager to find a Contracts app on the device
      if (packageManager.resolveActivity(pickContact, PackageManager.MATCH_DEFAULT_ONLY) == null)
         mSuspectButton.setEnabled(false);

      mPhotoButton = (ImageButton)view.findViewById(R.id.crime_camera);
      // MediaStore class defines the public interfaces used for interacting with common media --
      // images, videos, and music. ACTION_IMAGE_CAPTURE action will fire up a camera activity and
      // take a picture
      final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
      // check if there's a location at which to save the photo file, and if there's a camera app
      // (by querying PackageManager)
      boolean canTakePhoto = mPhotoFile != null && captureImage.resolveActivity(packageManager) != null;
      // enable/disable the Photo Button accordingly
      mPhotoButton.setEnabled(canTakePhoto);
      if (canTakePhoto) {
         // tell camera to take a full-resolution (not a small-resolution thumbnail) picture and
         // save it at the photo file location mPhotoFile
         Uri uri = Uri.fromFile(mPhotoFile);
         captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
      }
      mPhotoButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            // start an implicit intent for a camera app, expecting the image to be passed back
            startActivityForResult(captureImage, REQUEST_PHOTO);
         }
      });

      mPhotoView = (ImageView)view.findViewById(R.id.crime_photo);
      updatePhotoView();

      return view;
   }

   @Override
   public void onActivityResult(int requestCode, int resultCode, Intent data) {
      if (resultCode == Activity.RESULT_OK)
         switch(requestCode) {
            case REQUEST_DATE:
               // receive a Date object sent back from DatePickerFragment and update the Date Button
               // view accordingly.
               Date date = (Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
               mCrime.setDate(date);
               mDateButton.setText(mCrime.getDate().toString());
               break;
            case REQUEST_CONTACT:
               // receive a contacts database from the contacts application, query that database
               // and extract the contact name
               if (data != null) {
                  // receive a contacts database, which is a data URI, which is a locator that
                  // points at the single contact the user picked
                  Uri contactUri = data.getData();
                  // specify display name as column for the query
                  String[] queryFields = new String[] { ContactsContract.Contacts.DISPLAY_NAME };
                  // query the contacts database
                  Cursor cursor = getActivity().getContentResolver()
                        .query(contactUri, queryFields, null, null, null);
                  try {
                     // double-check that you actually got results
                     if (cursor.getCount() != 0) {
                        // the query result (cursor) contains only one item. so pull out the first
                        // column of the first row of data - that's the suspect's name
                        cursor.moveToFirst();
                        String suspect = cursor.getString(0);
                        mCrime.setSuspect(suspect);
                        mSuspectButton.setText(suspect);
                     }
                  } finally {
                     cursor.close();
                  }
               }
            case REQUEST_PHOTO:
               updatePhotoView();
         }
   }

   @Override
   // Crime instances get modified in CrimeFragment, and will need to be written out when
   // CrimeFragment is done.
   public void onPause() {
      super.onPause();
      CrimeLab.get(getActivity()).updateCrime(mCrime);
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

   private String getCrimeReport() {
      String dateFormat = "EEE, MMM dd";
      String dateString = DateFormat.format(dateFormat, mCrime.getDate()).toString();
      // String solvedString = mCrime.isSolved() ? getString(R.string.crime_report_solved) : getString(R.string.crime_report_unsolved);
      String solvedString = getString(mCrime.isSolved() ? R.string.crime_report_solved : R.string.crime_report_unsolved);
      String suspect = mCrime.getSuspect() == null ? getString(R.string.crime_report_no_suspect) : getString(R.string.crime_report_suspect, mCrime.getSuspect());

      String report = getString(R.string.crime_report, mCrime.getTitle(), dateString, solvedString, suspect);
      return report;
   }

   // this method loads a Bitmap into the ImageView member
   private void updatePhotoView() {
      if (mPhotoFile == null || !mPhotoFile.exists())
         mPhotoView.setImageDrawable(null);
      else {
         Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), getActivity());
         mPhotoView.setImageBitmap(bitmap);
      }
   }
}
