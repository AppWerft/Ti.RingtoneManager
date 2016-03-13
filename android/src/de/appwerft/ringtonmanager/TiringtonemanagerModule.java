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
		final TiBaseFile ringtoneFile;
		if (!d.containsKey(TiC.PROPERTY_URL)) {
			Log.e(LCAT, "url not provided");
			return false;
		}

		String absUrl = resolveUrl(null,
				TiConvert.toString(d.get(TiC.PROPERTY_URL)));
		ringtoneFile = TiFileFactory.createTitaniumFile(
				new String[] { absUrl }, false);
			String soundName = TiApplication.getInstance().getPackageName()
					+ " ringtone";
		if (d.containsKey(TiC.PROPERTY_TITLE)) {
			soundName = (String) d.get(TiC.PROPERTY_TITLE);
		}

		ContentValues values = new ContentValues();
		values.put(MediaStore.MediaColumns.DATA, ringtoneFile.nativePath());
		values.put(MediaStore.MediaColumns.TITLE, soundName);
		values.put(MediaStore.MediaColumns.SIZE, ringtoneFile.size());
		values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3");
		values.put(MediaStore.Audio.Media.ARTIST, "NoArtist");
		values.put(MediaStore.Audio.Media.DURATION, 20230);
		values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
		values.put(MediaStore.Audio.Media.IS_NOTIFICATION, true);
		values.put(MediaStore.Audio.Media.IS_ALARM, true);
		values.put(MediaStore.Audio.Media.IS_MUSIC, true);

		Log.i(LCAT,
				"the absolute path of the file is :"
						+ ringtoneFile.nativePath());
		/* file:///storage/emulated/0/de.appwerft.tierstimmenarchiv/Ochotona_curzoniae_S1439_08.mp3 */
		Log.i(LCAT, "the soundName :" + soundName);
		Uri uri = MediaStore.Audio.Media.getContentUriForPath(ringtoneFile
				.nativePath());
		Context context = TiApplication.getInstance().getApplicationContext();
		Uri mUri = context.getContentResolver().insert(uri, values);
		String ringtoneUri = mUri.toString();
		Log.i(LCAT, "the ringtone uri is :" + ringtoneUri);
		/* content://media/internal/audio/media/274 */
		try {
			RingtoneManager.setActualDefaultRingtoneUri(context,
					RingtoneManager.TYPE_RINGTONE, mUri);
			Log.i(LCAT, "RingtoneManagersetActualDefaultRingtoneUri SUCCESSFUL");
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


/*
 * 
 After callin come son console:
 
W/Ringtone( 3873): Neither local nor remote playback available
W/Ringtone( 3873): not playing fallback for content://media/internal/audio/media/274
 
 */