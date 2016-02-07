Ti.RingtoneManager
==================

This Titanium module is a wrapper to [Androids Ringtonemanager](http://developer.android.com/reference/android/media/RingtoneManager.html)

Currently this module has only one method 'setActualDefaultRingtone(nativePath_to_mp3)'

<uses-permission android:name="android.permission.WRITE_SETTINGS" ></uses-permission>
<uses-permission android:name="android.permission.CHANGE_CONFIGURATION" ></uses-permission>
<uses-permission android:name="android.permission.MODIFY_AUDIO_SETTINGS" ></uses-permission>

~~~
module.exports = function(record) {
var xhr = Ti.Network.createHTTPClient({
onload : function() {
var DEPOT = Ti.Filesystem.applicationCacheDirectory;
var fn = Ti.Filesystem.getFile(DEPOT, record.filename);
fn.write(this.responseData);
require('de.appwerft.ringtonemanager').setActualDefaultRingtone(fn.nativePath);
}
});
xhr.open('GET',url.audio);
xhr.send();

};

~~~


