/**
 *
 */
package de.appwerft.ringtonmanager;

import org.appcelerator.kroll.KrollModule;
import org.appcelerator.kroll.annotations.Kroll;

import org.appcelerator.titanium.TiApplication;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.kroll.common.TiConfig;

import android.app.Activity;
import android.os.Build;
import java.io.File;
import android.content.ContentValues;
import android.content.Context;
import android.provider.MediaStore;
import android.net.Uri;
import android.media.RingtoneManager;

import android.provider.Settings;
import android.provider.Settings.System;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.app.ActivityCompat;
//import android.content.pm.PackageManager;

@Kroll.module(name="Tiringtonemanager", id="de.appwerft.ringtonmanager")
public class TiringtonemanagerModule extends KrollModule {

	// Standard Debugging variables
	private static final String LCAT = "TiringtonemanagerModule";
	private static final boolean DBG = TiConfig.LOGD;
    Activity currentActivity = TiApplication.getInstance().getCurrentActivity();

	public TiringtonemanagerModule() {
		super();
	}

	@Kroll.onAppCreate
	public static void onAppCreate(TiApplication app)	{
		Log.d(LCAT, "inside onAppCreate");
	}

	// Methods
    @Kroll.method
    private boolean hasSystemWritePermission() {
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }
        Activity currentActivity = TiApplication.getInstance().getCurrentActivity();
        if (currentActivity.checkSelfPermission(Manifest.permission.WRITE_SETTINGS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    /* solution for Titanium in relation to other permission
    https://github.com/gimdongwoo/Ti-Android-RequestStoragePermission/blob/master/android/src/com/boxoutthinkers/reqstorageperm/TiAndroidRequeststoragepermissionModule.java
     */
	
    private void setRingtone(Uri uri) {
        
        RingtoneManager.setActualDefaultRingtoneUri(
                currentActivity,
                RingtoneManager.TYPE_RINGTONE,
                uri
        );
    }
   
    if (false == hasSystemWritePermission()) {
        // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(currentActivity,
                                                                    Manifest.permission.WRITE_SETTINGS)) {
            
            // Show an expanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
            
        } else {
            
            // No explanation needed, we can request the permission.

            ActivityCompat.requestPermissions(currentActivity,
                                              new String[]{Manifest.permission.WRITE_SETTINGS},
                                              MY_PERMISSIONS_REQUEST_WRITE_SETTINGS);
            
            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }
    }
    
    @Kroll.method
	public void setActualDefaultRingtone(String filepath) {
        File k = new File(filepath);
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DATA, k.getAbsolutePath());
        values.put(MediaStore.MediaColumns.TITLE, "Ringtone");
        values.put(MediaStore.MediaColumns.SIZE, 215454); // how can I determine?
        values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3");
        values.put(MediaStore.Audio.Media.ARTIST, "NoArtist");
        values.put(MediaStore.Audio.Media.DURATION, 230); // how can I determine?
        values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
        values.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
        values.put(MediaStore.Audio.Media.IS_ALARM, false);
        values.put(MediaStore.Audio.Media.IS_MUSIC, false);
        
        //Insert it into the database
        Uri uri = MediaStore.Audio.Media.getContentUriForPath(k.getAbsolutePath());
        Uri newUri = getActivity().getContentResolver().insert(uri, values);

        boolean canDo =  android.provider.Settings.System.canWrite(TiApplication.getInstance());
        if (false == canDo) {
            Intent grantIntent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            startActivity(grantIntent);
        } else {
            setRingtone(newUri);

        }
        
    }
}

    /* Proposal for generic requester */
    @Kroll.method
    public void requestPermission(String permission) { 
        Activity currentActivity = TiApplication.getInstance().getCurrentActivity();
        if (ContextCompat.checkSelfPermission(currentActivity,permission) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(currentActivity,new String[]{permission},0); }
