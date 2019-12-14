Ti.RingtoneManager
==================

This Titanium module is a wrapper to [Androids Ringtonemanager](http://developer.android.com/reference/android/media/RingtoneManager.html)




### Constants 

* TYPE_ALARM
* TYPE_NOTIFICATION
* TYPE_RINGTONE
* TYPE_ALL
* 
## Methods

### getAllRingtones()

#### Parameter:
type (see above) 

#### Returns

Returns a list with Objects.

* id
* uri
* title

```javascript
const RTM = require("ti.ringtonemanager");
var Picker = Ti.UI.createPicker({});
   
Picker.add(RTM.getAllRingtones(RTM.TYPE_ALL).map(function(tone) {
    return Ti.UI.createPickerRow({
            title : tone.title,
            uri : tone.uri
    });
}));
   
Picker.addEventListener("change", e => {
        RTM.playRingtone(e.row.uri);
        Settings.set("CALENDAR_RINGTONE",e.row.uri); 
        //Settings.set("CALENDAR_SOUND", e.value);
});
```

### playRingtone()
#### Parameter
* Uri, selected from list



### 

On Android6+ the module opens an intent to ask the user for system write permission (if not granted before)

![](https://raw.githubusercontent.com/AppWerft/Ti.RingtoneManager/master/perm.png)

### setActualDefaultRington()
#### Parameter
Object with 

* property `file` as nativePath 
* property `title`

Usage
-----
~~~
var RingTone = require('ti.ringtonmanager');

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


