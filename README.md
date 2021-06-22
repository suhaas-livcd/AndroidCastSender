### Implementing CAST Sender

To know everything about casting [Read here at Google's Developer Website](https://developers.google.com/cast)

The Google Cast SDK includes API libraries to connect to the receiver. Below are the steps, that will help to quickly get started.

### Setting up MediaRouteButton

Add the MediaRouteButton in xml or in the Menu Options
```
<androidx.mediarouter.app.MediaRouteButton
        android:id="@+id/media_route_button"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:mediaRouteTypes="user"
        android:visibility="visible"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"/>
```
Bind the media_route_button with Casting functionality

```
mMediaRouteButton = (MediaRouteButton) findViewById(R.id.media_route_button);
CastButtonFactory.setUpMediaRouteButton(getApplicationContext(), mMediaRouteButton);

```
Shows MediaRouteButton 

![Casting Enabled](/samples/CastDevicesAvailable.jpg)

Shows Cast Dialogue

![Available Cast devices in network](/samples/CastDeviceChoosingDailogue.png)


### Setting up Meta-Data, Media-Info, RemoteMediaClient
Add the metadata which shows on the casting dialogue

AndroidCastSender_Cast_dialogue.jpg

```
MediaMetadata metadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_GENERIC);
    metadata.putString(MediaMetadata.KEY_TITLE, "Sample Music");
    metadata.putString(MediaMetadata.KEY_SUBTITLE, "Sound Helix");
    metadata.addImage(new WebImage(Uri.parse("https://homepages.cae.wisc.edu/~ece533/images/airplane.png")));
    movieMetadata.addImage(new WebImage(Uri.parse("https://github.com/mkaflowski/HybridMediaPlayer/blob/master/images/cover.jpg?raw=true")));
```
Add the MediaInfo using the MediaInfo builder
```
MediaInfo mediaInfo = new MediaInfo.Builder("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3")
    .setContentType("audio/mp3")
    .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
    .setMetadata(metadata)
    .build();
```

Create a media load request object

```
MediaLoadRequestData mediaLoadRequestData = new MediaLoadRequestData.Builder().setMediaInfo(mediaInfo).build();
```

Load it using RemoteMediaClient

```
RemoteMediaClient remoteMediaClient = mCastContext.getSessionManager().getCurrentCastSession().getRemoteMediaClient();
```

Using a RemoteMediaClient is similar to a MediaPlayerObject

```
if (remoteMediaClient.isPlaying()) {
    // Change Image button to play
    mPlayPauseButton.setImageResource(R.drawable.ic_play_arrow_black_24dp);
    // Pause if playing
    remoteMediaClient.pause();
} else {
    // Change Image button to pause
    mPlayPauseButton.setImageResource(R.drawable.ic_pause_black_24dp);
    if (remoteMediaClient.isPaused()) {
        // Play if previous pause
        remoteMediaClient.play();
    } else {
        // If first time playing
        remoteMediaClient.load(mediaLoadRequestData);
    }
}
```

Check for DEVICE_CONNECTED status
```
if (mCastContext.getCastState() == CastState.CONNECTED) {
    // Load MediaInfo
} else{
    Toast.makeText(getApplicationContext(),"CAST NOT CONNECTED",Toast.LENGTH_SHORT).show();
}
```

Shows Cast Playing

![Available Cast devices in network](/samples/AndroidCastSender_Cast_dialogue.png)

#### Pending things
- Show cast options in Notification
- Adding Queues as playlist
- More Cast controls and Mediaroutes

### Reference
- [Setup for Developing with the Cast Application Framework (CAF) for Android](https://developers.google.com/cast/docs/android_sender)
- [Google Sender App](https://developers.google.com/cast/docs/design_checklist/sender)
- [Integrate Cast Into Your Android App](https://developers.google.com/cast/docs/android_sender/integrate)
- [Sending media to Chromecast has never been easier (Exoplayer cast extension)](https://medium.com/android-news/sending-media-to-chromecast-has-never-been-easier-c331eeef1e0a)
- [Google Play Services: Google Cast v3 and Media](https://code.tutsplus.com/tutorials/google-play-services-google-cast-v3-and-media--cms-26893)