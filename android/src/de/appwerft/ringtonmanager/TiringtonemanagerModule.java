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
		/*
		 * args are: url STRING title STRING
		 */
		Log.e(LCAT, args.toString());

		HashMap<String, String> hashmap = (HashMap<String, String>) args;
		if (!hashmap.containsKey(TiC.PROPERTY_URL)) {
			Log.e(LCAT, "url not provided");
			return;
		}
		String url = TiConvert.toString(hashmap.get(TiC.PROPERTY_URL));
		String title = "eigener Klingelton";
		if (hashmap.containsKey("title")) {
			title = hashmap.get("title");
		}
		String absUrl = resolveUrl(null, url);
		

		TiBaseFile file = TiFileFactory.createTitaniumFile(
				new String[] { absUrl }, false);

		// http://www.programcreek.com/java-api-examples/index.php?source_dir=MicDroid-master/src/com/intervigil/micdroid/helper/MediaStoreHelper.java

		ContentValues values = new ContentValues();

		values.put(MediaStore.MediaColumns.DATA, file.nativePath());
		Log.e(LCAT, file.nativePath());

		values.put(MediaStore.MediaColumns.TITLE, title);
		values.put(MediaStore.MediaColumns.SIZE, file.size());
		values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3");
		values.put(MediaStore.Audio.Media.ARTIST, "NoArtist");
		values.put(MediaStore.Audio.Media.DURATION, 230);
		values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
		values.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
		values.put(MediaStore.Audio.Media.IS_ALARM, false);
		values.put(MediaStore.Audio.Media.IS_MUSIC, false);
		// Insert it into the table files in sqlite
		Context context = TiApplication.getInstance().getApplicationContext();
		Uri uri = MediaStore.Audio.Media.getContentUriForPath(file.nativePath());
		Uri ringUri = context.getContentResolver().insert(uri, values);
		RingtoneManager.setActualDefaultRingtoneUri(context,RingtoneManager.TYPE_RINGTONE, ringUri);
		// TODO return of success

	}
}
