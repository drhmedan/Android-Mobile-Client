package io.intelehealth.client.views.activites;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.intelehealth.client.R;
import io.intelehealth.client.database.InteleHealthDatabaseHelper;
import io.intelehealth.client.databinding.ActivityTodayPatientBinding;
import io.intelehealth.client.objects.TodayPatientModel;
import io.intelehealth.client.utilities.Logger;
import io.intelehealth.client.views.adapters.TodayPatientAdapter;

public class TodayPatientActivity extends AppCompatActivity {
    private static final String TAG = TodayPatientActivity.class.getSimpleName();
    ActivityTodayPatientBinding binding;
    InteleHealthDatabaseHelper mDbHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_today_patient);
        setSupportActionBar(binding.toolbar);
        mDbHelper = new InteleHealthDatabaseHelper(this);
        db = mDbHelper.getWritableDatabase();
        doQuery();

    }

    /**
     * This method retrieves visit details about patient for a particular date.
     *
     * @return void
     */
    private void doQuery() {
        List<TodayPatientModel> todayPatientList = new ArrayList<>();
        Date cDate = new Date();
        String currentDate = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a").format(cDate);
        String query =
                "SELECT tbl_visit.uuid, tbl_visit.patientuuid, tbl_visit.startdate, tbl_visit.enddate," +
                        "tbl_visit.uuid, tbl_patient.first_name, tbl_patient.middle_name, tbl_patient.last_name, " +
                        "tbl_patient.date_of_birth,tbl_patient.openmrs_id,tbl_patient.phone_number FROM tbl_visit, tbl_patient WHERE tbl_visit.patientuuid = tbl_patient.uuid " +
                        "AND tbl_visit.startdate LIKE '" + currentDate + "%'" +
                        "ORDER BY tbl_patient.first_name ASC";
        //  "SELECT * FROM visit, patient WHERE visit.patient_id = patient._id AND visit.start_datetime LIKE '" + currentDate + "T%'";
        Logger.logD(TAG, query);
        final Cursor cursor = db.rawQuery(query, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    todayPatientList.add(new TodayPatientModel(
                            cursor.getString(cursor.getColumnIndexOrThrow("uuid")),
                            cursor.getString(cursor.getColumnIndexOrThrow("patientuuid")),
                            cursor.getString(cursor.getColumnIndexOrThrow("startdate")),
                            cursor.getString(cursor.getColumnIndexOrThrow("enddate")),
                            cursor.getString(cursor.getColumnIndexOrThrow("openmrs_id")),
                            cursor.getString(cursor.getColumnIndexOrThrow("first_name")),
                            cursor.getString(cursor.getColumnIndexOrThrow("middle_name")),
                            cursor.getString(cursor.getColumnIndexOrThrow("last_name")),
                            cursor.getString(cursor.getColumnIndexOrThrow("date_of_birth")),
                            cursor.getString(cursor.getColumnIndexOrThrow("phone_number"))
                    ));
                } while (cursor.moveToNext());
            }
        }
        cursor.close();

        if (!todayPatientList.isEmpty()) {
            for (TodayPatientModel todayPatientModel : todayPatientList)
                Log.i(TAG, todayPatientModel.getFirst_name() + " " + todayPatientModel.getLast_name());

            TodayPatientAdapter mTodayPatientAdapter = new TodayPatientAdapter(todayPatientList, TodayPatientActivity.this);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(TodayPatientActivity.this);
            binding.todayPatientRecyclerView.setLayoutManager(linearLayoutManager);
            binding.todayPatientRecyclerView.addItemDecoration(new
                    DividerItemDecoration(this,
                    DividerItemDecoration.VERTICAL));
            binding.todayPatientRecyclerView.setAdapter(mTodayPatientAdapter);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_today_patient, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.summary_endAllVisit:
                endAllVisit();

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void endAllVisit() {

        int failedUploads = 0;

        String query = "SELECT tbl_visit.patientuuid, tbl_visit.enddate, tbl_visit.uuid," +
                "tbl_patient.first_name, tbl_patient.middle_name, tbl_patient.last_name FROM tbl_visit, tbl_patient WHERE" +
                " tbl_visit.patientuuid = tbl_patient.uuid AND tbl_visit.enddate IS NULL OR tbl_visit.enddate = ''";

        final Cursor cursor = db.rawQuery(query, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    boolean result = endVisit(
                            cursor.getString(cursor.getColumnIndexOrThrow("patientuuid")),
                            cursor.getString(cursor.getColumnIndexOrThrow("first_name")) + " " +
                                    cursor.getString(cursor.getColumnIndexOrThrow("last_name")),
                            cursor.getString(cursor.getColumnIndexOrThrow("uuid"))
                    );
                    if (!result) failedUploads++;
                } while (cursor.moveToNext());
            }
        }
        cursor.close();

        if (failedUploads == 0) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Unable to end " + failedUploads +
                    " visits.Please upload visit before attempting to end the visit.");
            alertDialogBuilder.setNeutralButton(R.string.generic_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

    }

    private boolean endVisit(String patientID, String patientName, String visitUUID) {

        return visitUUID != null;

    }
}




