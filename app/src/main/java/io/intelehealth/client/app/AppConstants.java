package io.intelehealth.client.app;


import android.os.Environment;

import com.snatik.storage.Storage;

import java.io.File;

import io.intelehealth.client.database.InteleHealthDatabaseHelper;
import io.intelehealth.client.network.ApiClient;
import io.intelehealth.client.network.ApiInterface;
import io.intelehealth.client.utilities.DateAndTimeUtils;
import io.intelehealth.client.utilities.SqliteDbCloseHelper;
import io.intelehealth.client.utilities.UuidGenerator;

public class AppConstants {
    //Constants
    public static final String DATABASE_NAME = "localrecords.db";
    public static final int DATABASE_VERSION = 4;
    public static final String JSON_FOLDER = "Engines";
    public static final String JSON_FOLDER_Update = "Engines_Update";
    public static final String MIND_MAP_SERVER_URL = "http://165.227.97.214:1337/parse/";
    public static final String IMAGE_APP_ID = "app2";
    public static final String dbfilepath = Environment.getExternalStorageDirectory() + File.separator + "InteleHealth_DB" + File.separator + "Intelehealth.db"; // directory: Intelehealth_DB   ,  filename: Intelehealth.db

    //App vitals constants
    public static final String MAXIMUM_HEIGHT = "272";
    public static final String MAXIMUM_WEIGHT = "150";
    public static final String MAXIMUM_BP_SYS = "300";
    public static final String MINIMUM_BP_SYS = "50";
    public static final String MAXIMUM_BP_DSYS = "150";
    public static final String MINIMUM_BP_DSYS = "30";
    public static final String MAXIMUM_PULSE = "200";
    public static final String MINIMUM_PULSE = "30";
    public static final String MAXIMUM_TEMPERATURE_CELSIUS = "43";
    public static final String MINIMUM_TEMPERATURE_CELSIUS = "25";
    public static final String MINIMUM_TEMPERATURE_FARHENIT = "80";
    public static final String MAXIMUM_TEMPERATURE_FARHENIT = "120";
    public static final String MAXIMUM_SPO2 = "100";
    public static final String MINIMUM_SPO2 = "1";
    public static final String MAXIMUM_RESPIRATORY = "80";
    public static final String MINIMUM_RESPIRATORY = "10";

    //functions constants
    public static Storage storage = new Storage(IntelehealthApplication.getAppContext());
    public static InteleHealthDatabaseHelper inteleHealthDatabaseHelper = new InteleHealthDatabaseHelper(IntelehealthApplication.getAppContext());
    public static ApiInterface apiInterface = ApiClient.createService(ApiInterface.class);
    public static SqliteDbCloseHelper sqliteDbCloseHelper = new SqliteDbCloseHelper();
    public static DateAndTimeUtils dateAndTimeUtils = new DateAndTimeUtils();
    public static String NEW_UUID = new UuidGenerator().UuidGenerator();

}
