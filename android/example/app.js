// This is a test harness for your module
// You should do something interesting in this harness
// to test out the module and to provide instructions
// to users on how to use it by example.


// open a single window
var win = Ti.UI.createWindow({
	backgroundColor:'white'
});
var label = Ti.UI.createLabel();
win.add(label);
win.open();

var RingTone = require('de.appwerft.ringtonmanager');
var xhr = Ti.Network.createHTTPClient({
		onload : function() {
			var soundfile = Ti.Filesystem.getFile(Ti.Filesystem.applicationDataDirectory, 'ringtones', 'sound.mp3');
			soundfile.write(this.responseData);
			RingTone.setActualDefaultRingtone({
				url : soundfile.nativePath,
				title : 'Aegyptisches_Dendrawahuhn'
			});

		}
	});
	xhr.open('GET', 'http://www.tierstimmenarchiv.de/recordings/0008_Aegyptisches_Dendrawahuhn_short.mp3');  // from http://www.tierstimmenarchiv.de/recordings/
	xhr.send();

