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
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.media.RingtoneManager;
import android.media.Ringtone;
import android.media.AudioAttributes;
import android.widget.Toast;
import android.content.Context;
import android.provider.Settings.NameValueTable;

import android.provider.Settings;
import android.provider.Settings.System;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.app.ActivityCompat;
import android.content.pm.PackageManager;
import android.Manifest;
import android.app.Activity;

//import android.content.pm.PackageManager;

@Kroll.module(name = "Tiringtonemanager", id = "de.appwerft.ringtonmanager")
public class TiringtonemanagerModule extends KrollModule {

	// Standard Debugging variables
	private static final String LCAT = "Tiringtone";
	private static final boolean DBG = TiConfig.LOGD;

	public TiringtonemanagerModule() {
		super();
	}

	@Kroll.onAppCreate
	public static void onAppCreate(TiApplication app) {
		Log.d(LCAT, "inside onAppCreate");
	}

	@Kroll.method
	public void setActualDefaultRingtone(Object args) {
		HashMap<String, String> d = (HashMap<String, String>) args;
		final TiBaseFile ringtoneFile;
		final int NOTIFY_CODE = 999;
		if (!d.containsKey(TiC.PROPERTY_URL)) {
			Log.e(LCAT, "url not provided");

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

		Context context = TiApplication.getInstance().getApplicationContext();

		// see
		// http://stackoverflow.com/questions/18100885/set-raw-resource-as-ringtone-in-android
		ContentValues values = new ContentValues();
		values.put(MediaStore.MediaColumns.DATA, absUrl);
		values.put(MediaStore.MediaColumns.TITLE, soundName);
		values.put(MediaStore.MediaColumns.SIZE, ringtoneFile.size());
		values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/*");
		values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
		values.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
		values.put(MediaStore.Audio.Media.IS_ALARM, false);
		values.put(MediaStore.Audio.Media.IS_MUSIC, false);

		Log.i(LCAT, "nativePath=" + ringtoneFile.nativePath());

		Uri mUri = context.getContentResolver().insert(
				MediaStore.Audio.Media.getContentUriForPath(ringtoneFile
						.nativePath()), values);

		Log.i(LCAT, "The new ringtone is =" + soundName); // content://media/internal/audio/media/274

		Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
		intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE,
				"Bestätige jetzt „" + soundName + "“");
		intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
		intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
		intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,
				RingtoneManager.TYPE_RINGTONE);
		intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, mUri);
		TiApplication.getInstance().getCurrentActivity()
				.startActivityForResult(intent, NOTIFY_CODE);

	}

	/*
	 * @Override protected void onActivityResult(final int requestCode, final
	 * int resultCode, final Intent intent) { if (resultCode ==
	 * Activity.RESULT_OK && requestCode == NOTIFY_CODE) { Uri uri =
	 * intent.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
	 * 
	 * if (uri != null) { this.chosenRingtone = uri.toString(); } else {
	 * this.chosenRingtone = null; } } }
	 */

	@Kroll.method
	public String getDefaultUri() {
		Context context = TiApplication.getInstance().getApplicationContext();
		Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
		return uri.toString();
	}

	@Kroll.method
	public String getCurrentRingtone() {
		Context context = TiApplication.getInstance().getApplicationContext();
		Uri currentRingtoneUri = RingtoneManager.getActualDefaultRingtoneUri(
				context, RingtoneManager.TYPE_RINGTONE);
		Ringtone currentRingtone = RingtoneManager.getRingtone(context,
				currentRingtoneUri);
		String title = currentRingtone.getTitle(context);
		return title;
	}
}

/*
 * 
 * After calling comes on console:
 * 
 * W/Ringtone( 3873): Neither local nor remote playback available W/Ringtone(
 * 3873): not playing fallback for content://media/internal/audio/media/274
 */