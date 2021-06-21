package com.idlikadai.androidcastsender;

import androidx.appcompat.app.AppCompatActivity;
import androidx.mediarouter.app.MediaRouteButton;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.cast.Cast;
import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaLoadRequestData;
import com.google.android.gms.cast.MediaMetadata;
import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.Session;
import com.google.android.gms.cast.framework.SessionManager;
import com.google.android.gms.cast.framework.SessionManagerListener;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private ImageButton mPlayPauseButton;

    private CastContext mCastContext = null;
    private MediaRouteButton mMediaRouteButton;

    private CastSession mCastSession;
    private SessionManager mSessionManager;
    private final SessionManagerListener mSessionManagerListener =
            new SessionManagerListenerImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSessionManager = CastContext.getSharedInstance(this).getSessionManager();

        setContentView(R.layout.activity_main);

        mCastContext = CastContext.getSharedInstance();

        mMediaRouteButton = (MediaRouteButton) findViewById(R.id.media_route_button);
        CastButtonFactory.setUpMediaRouteButton(getApplicationContext(), mMediaRouteButton);


        mPlayPauseButton = (ImageButton) findViewById(R.id.media_button);

        mediaPlayer = MediaPlayer.create(this,
                Settings.System.DEFAULT_RINGTONE_URI);




        mPlayPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    mPlayPauseButton.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                    mediaPlayer.pause();
                }else{
                    mPlayPauseButton.setImageResource(R.drawable.ic_pause_black_24dp);
                    MediaMetadata metadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_MUSIC_TRACK);

                    metadata.putString(MediaMetadata.KEY_TITLE, "Ringtone");
                    metadata.putString(MediaMetadata.KEY_SUBTITLE, "Retro Ring");

                    MediaInfo mediaInfo = new MediaInfo.Builder("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3")
                            .setContentType("audio/mp3")
                            .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                            .setMetadata(metadata)
                            .build();
                    RemoteMediaClient remoteMediaClient = mCastContext.getSessionManager().getCurrentCastSession().getRemoteMediaClient();
                    remoteMediaClient.load(mediaInfo, true, 0);

                    MediaLoadRequestData mediaLoadRequestData = new MediaLoadRequestData.Builder().setMediaInfo(mediaInfo).build();
                    remoteMediaClient.load(mediaLoadRequestData);

                    mediaPlayer.start();
                }
            }
        });

    }


    @Override
    protected void onResume() {
        mCastSession = mSessionManager.getCurrentCastSession();
        mSessionManager.addSessionManagerListener(mSessionManagerListener);
        super.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        mSessionManager.removeSessionManagerListener(mSessionManagerListener);
        mCastSession = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.browse, menu);
        CastButtonFactory.setUpMediaRouteButton(this, menu, R.id.media_route_menu_item);

        return true;
    }

    private class SessionManagerListenerImpl implements SessionManagerListener {
        @Override
        public void onSessionStarting(Session session) {

        }

        @Override
        public void onSessionStarted(Session session, String sessionId) {
            invalidateOptionsMenu();
        }

        @Override
        public void onSessionStartFailed(Session session, int i) {

        }

        @Override
        public void onSessionEnding(Session session) {

        }

        @Override
        public void onSessionResumed(Session session, boolean wasSuspended) {
            invalidateOptionsMenu();
        }

        @Override
        public void onSessionResumeFailed(Session session, int i) {

        }

        @Override
        public void onSessionSuspended(Session session, int i) {

        }

        @Override
        public void onSessionEnded(Session session, int error) {
            finish();
        }

        @Override
        public void onSessionResuming(Session session, String s) {

        }
    }
}