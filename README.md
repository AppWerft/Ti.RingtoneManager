Ti.RingtoneManager
==================

This Titanium module is a wrapper to [Androids Ringtonemanager](http://developer.android.com/reference/android/media/RingtoneManager.html)

Currently this module has only one method 'setActualDefaultRingtone({url:nativePath_to_mp3,title:NAME_OF_SOUND})'



~~~
var RingTone = require('de.appwerft.ringtonmanager');

module.exports = function(record) {
    if (!Ti.Filesystem.isExternalStoragePresent()) return;
    var xhr = Ti.Network.createHTTPClient({
        onload : function() {
            var soundfile = Ti.Filesystem.getFile(Ti.Filesystem.externalStorageDirectory, record.filename + '.mp3');
            soundfile.write(this.responseData);
            RingTone.setActualDefaultRingtone({
                url : soundfile.nativePath,
                title : record.species_latin
            });
        }
    });
xhr.open('GET', record.audio);
xhr.send();
};

~~~


