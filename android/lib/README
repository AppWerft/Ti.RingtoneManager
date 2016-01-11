package de.appwerft.ringtonemanager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

import org.appcelerator.kroll.KrollModule;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.kroll.common.TiConfig;
import org.appcelerator.titanium.TiApplication;
import org.appcelerator.titanium.TiC;
import org.appcelerator.titanium.util.TiConvert;
import org.appcelerator.titanium.io.TiBaseFile;
import org.appcelerator.titanium.io.TiFileFactory;

import android.content.Context;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.media.RingtoneManager;

@Kroll.module(name="Ringtonemanager", id="de.appwerft.ringtonemanager")
public class RingtonemanagerModule extends KrollModule {

private static final String LCAT = "RingtonemanagerModule";
	private static final boolean DBG = TiConfig.LOGD;
	
	private static Ringtonemanager rtm;
	

	public RingtonemanagerModule() {
		super();
	}
	
	@Kroll.onAppCreate
	public static void onAppCreate(TiApplication app) {
		Log.d(LCAT, "inside onAppCreate");
	}
	@Kroll.method
	public void setActualDefaultRingtoneUri(Context context, int type, Uri ringtoneUri){
	}
}