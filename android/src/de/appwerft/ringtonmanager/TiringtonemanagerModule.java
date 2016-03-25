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
import android.content.ContentResolver;
import android.content.Context;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.media.RingtoneManager;
import android.media.Ringtone;
import android.media.AudioAttributes;
import android.content.Context;
import android.provider.Settings.NameValueTable;
import android.provider.Settings;
import android.provider.Settings.System;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.app.ActivityCompat;
import android.app.Activity;


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
		// http://stackoverflow.com/questions/18100885/set-raw-resource-as-ringtone-in-android
		ContentValues content = new ContentValues();
		content.put(MediaStore.MediaColumns.DATA, absUrl);
		content.put(MediaStore.MediaColumns.TITLE, soundName);
		content.put(MediaStore.MediaColumns.SIZE, ringtoneFile.size());
		content.put(MediaStore.MediaColumns.MIME_TYPE, "audio/*");
		content.put(MediaStore.Audio.Media.IS_RINGTONE, true);
		content.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
		content.put(MediaStore.Audio.Media.IS_ALARM, false);
		content.put(MediaStore.Audio.Media.IS_MUSIC, false);
		Log.i(LCAT, "nativePath=" + ringtoneFile.nativePath());
		Uri uri = MediaStore.Audio.Media.getContentUriForPath(ringtoneFile
				.nativePath());
		Uri mUri = context.getContentResolver().insert(uri, content);
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
}
