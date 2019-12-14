/**
 * ant; unzip -uo  dist/de.appwerft.ringtonmanager-android-1.0.7.zip -d  ~/Documents/APPC_WORKSPACE/Tierstimmenarchiv/
 */
package ti.ringtonmanager;

import org.appcelerator.kroll.KrollModule;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.titanium.TiApplication;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.kroll.common.TiConfig;
import org.appcelerator.titanium.io.TiBaseFile;
import org.appcelerator.titanium.io.TiFileFactory;
import org.appcelerator.titanium.TiC;
import org.appcelerator.titanium.TiBaseActivity;
import org.appcelerator.titanium.util.TiConvert;
import org.appcelerator.kroll.KrollObject;
import org.appcelerator.titanium.util.TiActivityResultHandler;
import org.appcelerator.titanium.util.TiActivitySupport;
import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollFunction;

import android.app.Activity;
import android.os.Build;
import java.io.File;
import android.os.Environment;
import android.os.Build;
import android.view.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
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
import android.database.Cursor;
import android.support.v4.content.ContextCompat;
import android.support.v4.app.ActivityCompat;
import android.app.Activity;

@Kroll.module(name = "Tiringtonemanager", id = "ti.ringtonemanager")
public class TiringtonemanagerModule extends KrollModule {
	private static final String LCAT = "Tiringtone";
	private static final boolean DBG = TiConfig.LOGD;
	static final int REQUEST_SYSTEM_WRITE_PERMISSION = 23;
	public Uri mUri = null;
	private Context ctx= TiApplication.getInstance().getApplicationContext();
	@Kroll.constant
	public static int TYPE_ALAM = RingtoneManager.TYPE_ALARM;
	@Kroll.constant
	public static int TYPE_NOTIFICATION = RingtoneManager.TYPE_NOTIFICATION;
	@Kroll.constant
	public static int TYPE_RINGTONE = RingtoneManager.TYPE_RINGTONE;
	@Kroll.constant
	public static int TYPE_ALL = RingtoneManager.TYPE_ALL;
	

	private boolean setRingtone(Uri mUri, KrollFunction mCallback) {
		try {
			RingtoneManager.setActualDefaultRingtoneUri(ctx,
					RingtoneManager.TYPE_RINGTONE, mUri);
			Log.i(LCAT, "RingtoneManagersetActualDefaultRingtoneUri SUCCESSFUL");
			Log.i(LCAT,
					"RingtoneManagersetActualDefaultRingtoneUri "
							+ mUri.toString());
			HashMap<String, Boolean> map = new HashMap<String, Boolean>();
			map.put("success", true);
			mCallback.call(getKrollObject(), map);

		} catch (Exception e) {
			Log.e(LCAT, "exception: " + e.getMessage());
			Log.e(LCAT, "exception: " + e.toString());
			return false;
		}
		return true;
	}

	public TiringtonemanagerModule() {
		super();
	}

	@Kroll.onAppCreate
	public static void onAppCreate(TiApplication app) {
		Log.d(LCAT, "inside onAppCreate");
	}

	@Kroll.method
	public Object getAllRingtones(int type) {
		ArrayList<KrollDict> list = new ArrayList<KrollDict>(); 
		RingtoneManager manager = new RingtoneManager(ctx);
		    manager.setType(type);
		    Cursor cursor = manager.getCursor();
		    while (cursor.moveToNext()) {
		    	KrollDict res = new KrollDict();
		        String notificationTitle = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX);
		        String notificationUri = cursor.getString(RingtoneManager.URI_COLUMN_INDEX) + "/" + cursor.getString(RingtoneManager.ID_COLUMN_INDEX);
		        res.put("title",notificationTitle);
		        res.put("uri",notificationUri);
		        res.put("id",cursor.getString(RingtoneManager.ID_COLUMN_INDEX));
		        list.add(res);
		    }
		    return list.toArray();
	}
	
	@Kroll.method 
	public void playRingtone(String uri) {
		Ringtone ringtone = RingtoneManager.getRingtone(ctx, Uri.parse(uri));
		ringtone.setLooping(false);
		ringtone.play();
	}
	
	@Kroll.method
	public boolean setActualDefaultRingtone(Object args,
			@Kroll.argument(optional = true) KrollFunction mCallback) {
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
		String soundPath = absUrl.replace("file://", "");
		File file = new File(soundPath);
		if (!file.exists()) {
			return false;
		}
		ContentValues values = new ContentValues();
		values.put(MediaStore.MediaColumns.DATA, soundPath);
		values.put(MediaStore.MediaColumns.TITLE, soundName);
		values.put(MediaStore.MediaColumns.SIZE, ringtoneFile.size());
		values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3");
		values.put(MediaStore.Audio.Media.ARTIST, "Nature");
		values.put(MediaStore.Audio.Media.DURATION, 20000);
		values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
		values.put(MediaStore.Audio.Media.IS_NOTIFICATION, true);
		values.put(MediaStore.Audio.Media.IS_ALARM, true);
		values.put(MediaStore.Audio.Media.IS_MUSIC, true);
		Uri uri = MediaStore.Audio.Media.getContentUriForPath(soundPath);
		ContentResolver mCr = ctx.getContentResolver();
		mCr.delete(uri,
				MediaStore.MediaColumns.DATA + "=\"" + soundPath + "\"", null);
		Uri mUri = mCr.insert(uri, values);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			Log.i(LCAT, "Build.VERSION.SDK_INT=" + Build.VERSION.SDK_INT);
			if (Settings.System.canWrite(ctx)) {
				Log.i(LCAT, "Settings.System.canWrite=true");
				setRingtone(mUri, mCallback);
			} else {
				Log.i(LCAT, "try to get write permissin from user");
				Activity activity = TiApplication.getInstance()
						.getCurrentActivity();
				Intent intent = new Intent(
						android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
				intent.setData(Uri.parse("package:" + activity.getPackageName()));
				intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				activity.startActivityForResult(intent,
						REQUEST_SYSTEM_WRITE_PERMISSION);
				testDelaydRingtoneSetting(mUri, mCallback);
			}
		} else
			setRingtone(mUri, mCallback);
		return true;
	}

	private void testDelaydRingtoneSetting(Uri mUri, KrollFunction mCallback) {
		final Uri ringtoneUri = mUri;
		final KrollFunction callback = mCallback;
		new android.os.Handler().postDelayed(new Runnable() {
			public void run() {
				if (Settings.System.canWrite(ctx)) {
					Activity activity = TiApplication.getInstance()
							.getCurrentActivity();
					activity.finishActivity(REQUEST_SYSTEM_WRITE_PERMISSION);
					setRingtone(ringtoneUri, callback);
				} else
					testDelaydRingtoneSetting(ringtoneUri, callback);
			}
		}, 1000);
	}
}
