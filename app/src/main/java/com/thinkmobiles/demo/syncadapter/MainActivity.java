package com.thinkmobiles.demo.syncadapter;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity {

    public static final String ACCOUNT = "dummyaccount";
    // Instance fields
    private Account mAccount;
    private static final String LOG_TAG = "denis";
    public static final long PERIOD_IN_SECONDS = 90;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAccount = createSyncAccount(getApplicationContext());
        // Enable automatic syncing
        ContentResolver.setSyncAutomatically(mAccount, getString(R.string.authority), true);
        // Period has to be at least 60 seconds
        ContentResolver.addPeriodicSync(mAccount, getString(R.string.authority), new Bundle(), PERIOD_IN_SECONDS);
        syncNow();
    }


    /**
     * Create a new dummy account for the sync adapter
     *
     * @param context The application context
     */
    public static Account createSyncAccount(Context context) {
        Account newAccount = new Account(ACCOUNT, context.getString(R.string.account_type));
        AccountManager accountManager = AccountManager.get(context);
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            Log.d(LOG_TAG, "accountManager: account added successfully");
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call context.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
        } else {
            Log.d(LOG_TAG, "accountManager: account exists or error");
             /*
             * The account exists or some other error occurred. Log this, report it,
             * or handle it internally.
             */
        }
        return newAccount;
    }

    // Manual sync
    public void syncNow() {
        // Pass the settings flags by inserting them in a bundle
        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        /*
         * Request the sync for the default account, authority, and
         * manual sync settings
         */
        ContentResolver.requestSync(mAccount, getString(R.string.authority), settingsBundle);
    }
}
