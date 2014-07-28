Sometimes its necessary to fake your gps position: for demonstration or human testing,  Its not so hard for emulation device - using
Android Device Monitor or telnet gps fix. But there is no solution to fake your location on device.

http://developer.android.com/training/location/location-testing.html - really obsolete.

LocationManager.addTestProvider - its good solution for automatic tests but not interactive. 

https://play.google.com/store/apps/details?id=com.tim.apps.mockgps&hl=ru - good example if you want trick foursquare, 
but i wanna see how MY application reacts on location updates.

This simple application mock LocationManager.GPS_PROVIDER, listen broadcast intent with Location
data and push this Location to provider.

So all other application using GPS_PROVIDER can be mocked



Recommended usage - install and run debug build. Enable GPS mocking in your device developers settings.

Then use adb shell (if you reading this, you should be familiar with adb) to send broadcast intent
Example (send device to north pole)
adb shell am broadcast -a me.piratas.gpsmock.Location --ef lat 90 --ef lon 0 --ef acc 0 --ef alt 0

or to south:
adb shell am broadcast -a me.piratas.gpsmock.Location --ef lat -90 --ef lon 0 --ef acc 0 --ef alt 0

or to Rome:
adb shell am broadcast -a me.piratas.gpsmock.Location --ef lat 41.9 --ef lon 12.5

float extras:

lat - latitude
lon - longitude
acc - accuracy
alt - altitude
spd - speed
brn - bearing



TODO python cli wrapper and kml reader