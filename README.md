Ti.RingtoneManager
==================

This Titanium module is a wrapper to [Androids Ringtonemanager](http://developer.android.com/reference/android/media/RingtoneManager.html)

Currently this module has only one method 'setActualDefaultRingtone({url:nativePath_to_mp3,title:NAME_OF_SOUND})'

On Android6+ the module opens an intent to ask the user for system write permission (if not granted before)



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
            },function(_e){
                if (_success==true) {
                    Ti.UI.createNotification({message:'Ringtone added and changed'}).show()
                }
            });
        }
    });
xhr.open('GET', record.audio);
xhr.send();
};

~~~


