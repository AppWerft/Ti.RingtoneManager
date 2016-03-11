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

import android.app.Activity;
import android.os.Build;
import java.io.File;
import java.util.HashMap;
import android.content.ContentValues;
import android.content.Context;
import android.provider.MediaStore;
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
	private static final String LCAT = "TiringtonemanagerModule";
	private static final boolean DBG = TiConfig.LOGD;
	Activity currentActivity = TiApplication.getInstance().getCurrentActivity();

	public TiringtonemanagerModule() {
		super();
	}

	@Kroll.onAppCreate
	public static void onAppCreate(TiApplication app) {
		Log.d(LCAT, "inside onAppCreate");
	}

	@Kroll.method
	public void setActualDefaultRingtone(Object args) {
		Context context = TiApplication.getInstance().getApplicationContext();
		
		HashMap<String, String> d = (HashMap<String, String>) args;
		final TiBaseFile file;

		if (!d.containsKey(TiC.PROPERTY_URL)) {
			Log.e(LCAT, "url not provided");
			return;
		}
		String url = TiConvert.toString(d.get(TiC.PROPERTY_URL));
		String absUrl = resolveUrl(null, url);
		Log.e(LCAT, absUrl);

		File newSoundFile = TiFileFactory.createTitaniumFile(
				new String[] { absUrl }, false);
		ContentValues values = new ContentValues();
		values.put(MediaStore.MediaColumns.DATA, newSoundFile.getAbsolutePath());
		values.put(MediaStore.MediaColumns.TITLE, "Ringtone");
		values.put(MediaStore.MediaColumns.SIZE, 215454); // how can I
															// determine?
		values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3");
		values.put(MediaStore.Audio.Media.ARTIST, "NoArtist");
		values.put(MediaStore.Audio.Media.DURATION, 230); // how can I
															// determine?
		values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
		values.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
		values.put(MediaStore.Audio.Media.IS_ALARM, false);
		values.put(MediaStore.Audio.Media.IS_MUSIC, false);
		// Insert it into the database
		Uri uri = MediaStore.Audio.Media.getContentUriForPath(newSoundFile
				.getAbsolutePath());
		context.getContentResolver().delete(
				uri,
				MediaStore.MediaColumns.DATA + "=\""
						+ newSoundFile.getAbsolutePath() + "\"", null);
		Uri newUri = context.getContentResolver().insert(uri, values);
		
		// TODO return of success
		
		
		
	}
}
