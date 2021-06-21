package com.idlikadai.androidcastsender;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.mediarouter.app.MediaRouteButton;

import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaLoadRequestData;
import com.google.android.gms.cast.MediaMetadata;
import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.CastState;
import com.google.android.gms.cast.framework.Session;
import com.google.android.gms.cast.framework.SessionManager;
import com.google.android.gms.cast.framework.SessionManagerListener;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import com.google.android.gms.common.images.WebImage;

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


        mPlayPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCastContext.getCastState() == CastState.CONNECTED) {
                    final RemoteMediaClient remoteMediaClient = mCastContext.getSessionManager().getCurrentCastSession().getRemoteMediaClient();
                    if (remoteMediaClient.isPlaying()) {
                        mPlayPauseButton.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                        remoteMediaClient.pause();
                    } else {
                        mPlayPauseButton.setImageResource(R.drawable.ic_pause_black_24dp);
                        if (remoteMediaClient.isPaused()) {
                            remoteMediaClient.play();
                        } else {
                            MediaMetadata metadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_GENERIC);
                            metadata.putString(MediaMetadata.KEY_TITLE, "Sample Music");
                            metadata.putString(MediaMetadata.KEY_SUBTITLE, "Sound Helix");
                            metadata.addImage(new WebImage(Uri.parse("https://homepages.cae.wisc.edu/~ece533/images/airplane.png")));

                            MediaInfo mediaInfo = new MediaInfo.Builder("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3")
                                    .setContentType("audio/mp3")
                                    .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                                    .setMetadata(metadata)
                                    .build();
                            remoteMediaClient.load(mediaInfo, true, 0);
                            MediaLoadRequestData mediaLoadRequestData = new MediaLoadRequestData.Builder().setMediaInfo(mediaInfo).build();
                            remoteMediaClient.load(mediaLoadRequestData);
                        }
                    }

                } else {
                    Toast.makeText(getApplicationContext(),"CAST NOT CONNECTED",Toast.LENGTH_SHORT).show();
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
//            finish();
        }

        @Override
        public void onSessionResuming(Session session, String s) {

        }
    }
}