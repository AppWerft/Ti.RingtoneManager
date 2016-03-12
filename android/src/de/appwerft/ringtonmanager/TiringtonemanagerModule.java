/**
 *
 */
package de.appwerft.ringtonmanager;

import org.appcelerator.kroll.KrollModule;
import org.appcelerator.kroll.annotations.Kroll;

import org.appcelerator.titanium.TiApplication;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.kroll.common.TiConfig;
import org.appcelerator.titanium.io.TiBaseFile;
import org.appcelerator.titanium.io.TiFileFactory;
import org.appcelerator.titanium.TiC;
import org.appcelerator.titanium.util.TiConvert;
import org.appcelerator.titanium.util.TiRHelper;
import org.appcelerator.titanium.util.TiRHelper.ResourceNotFoundException;

import android.app.Activity;
import android.os.Build;
import java.io.File;
import java.util.HashMap;
import android.content.ContentValues;
import android.content.ContentResolver;
import android.content.Context;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.net.Uri;
import android.media.RingtoneManager;
import android.widget.Toast;
import android.content.Context;

import android.provider.Settings;
import android.provider.Settings.System;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.app.ActivityCompat;

//import android.content.pm.PackageManager;

@Kroll.module(name = "Tiringtonemanager", id = "de.appwerft.ringtonmanager")
public class TiringtonemanagerModule extends KrollModule {

	// Standard Debugging variables
	private static final String LCAT = "Tiringtone";
	private static final boolean DBG = TiConfig.LOGD;

	public TiringtonemanagerModule() {
		super();
	}

	public static int getResString(String str) {
		try {
			return TiRHelper.getApplicationResource("string." + str);
		} catch (ResourceNotFoundException e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Kroll.onAppCreate
	public static void onAppCreate(TiApplication app) {
		Log.d(LCAT, "inside onAppCreate");
	}

	@Kroll.method
	public Boolean setActualDefaultRingtone(Object args) {
		HashMap<String, String> d = (HashMap<String, String>) args;
		final TiBaseFile file;
		if (!d.containsKey(TiC.PROPERTY_URL)){
			Log.e(LCAT,"url not provided");
			return false;
		}
		String url = TiConvert.toString(d.get(TiC.PROPERTY_URL));
		String absUrl = resolveUrl(null, url);
		file = TiFileFactory.createTitaniumFile(new String[] { absUrl }, false);
	
		String soundname = TiApplication.getInstance().getPackageName() + " ringtone";
		if (d.containsKey(TiC.PROPERTY_TITLE)){
			soundname = (String) d.get(TiC.PROPERTY_TITLE);
		}
		
		ContentValues values = new ContentValues();
		values.put(MediaStore.MediaColumns.DATA, file.nativePath());
		values.put(MediaStore.MediaColumns.TITLE, getResString("app_name"));
		values.put(MediaStore.MediaColumns.SIZE, file.size());
		values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3");
		values.put(MediaStore.Audio.Media.ARTIST, "NoArtist");
		values.put(MediaStore.Audio.Media.DURATION, 230);
		values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
		values.put(MediaStore.Audio.Media.IS_NOTIFICATION, true);
		values.put(MediaStore.Audio.Media.IS_ALARM, true);
		values.put(MediaStore.Audio.Media.IS_MUSIC, true);

		Uri uri = MediaStore.Audio.Media
				.getContentUriForPath(file.nativePath());
		Log.d(LCAT, uri.toString());
		
		Context context = TiApplication.getInstance().getApplicationContext();
		Uri mUri = context.getContentResolver().insert(uri, values);
		Log.d(LCAT, mUri.toString());

		try {
			RingtoneManager.setActualDefaultRingtoneUri(context,
					RingtoneManager.TYPE_RINGTONE, mUri);
			Log.e(LCAT, "RingtoneManagersetActualDefaultRingtoneUri SUCCESSFUL");
		} catch (Exception e) {
			Log.e(LCAT, "RingtoneManagersetActualDefaultRingtoneUri", e);
		}

		// Alternatives:
		// http://stackoverflow.com/questions/17570636/how-to-set-mp3-as-ringtone

		return true; // TODO return of success

	}

	@Kroll.method
	public String getActualDefaultRingtone() {
		Context context = TiApplication.getInstance().getApplicationContext();
		Uri uri = RingtoneManager.getActualDefaultRingtoneUri(context,
				RingtoneManager.TYPE_RINGTONE);
		return uri.toString();
	}
}
