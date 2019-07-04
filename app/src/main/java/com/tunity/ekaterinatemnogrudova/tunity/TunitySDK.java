package com.tunity.ekaterinatemnogrudova.tunity;
import java.io.File;
import java.io.IOException;
import android.content.Context;
import android.content.Intent;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.LinearLayout;

public class TunitySDK {
    public static String path = "//sdcard//Download//" + "videocapture_example.mp4";
    private SurfaceHolder holder;
    private MediaRecorder mRecorder;
    boolean recording = false;
    private View mView;
    private Context mContext;
    private CameraPreview mPreview;
    private int mDuration;
    private double mFps;
    private static final TunitySDK instance = new TunitySDK();

    public static TunitySDK getInstance() {
        return instance;
    }

    public void takeVideo() {
        if (!recording) {
            recording = true;
            mRecorder.start();
        }
    }

    public void start(View view, Context context, int duration, double fps) {
        mContext = context;
        mDuration = duration;
        mFps = fps;
        if (view!= null) {
            mView = view;
            mRecorder = new MediaRecorder();

            initRecorder(duration, fps);

            mPreview = new CameraPreview(context);
            ((LinearLayout) view).addView(mPreview);
        }
        else{
        //TODO: FULL SCREEN
        }

    }

    private void initRecorder(int max_duration_ms, double fps) {
        mRecorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
            @Override
            public void onInfo(MediaRecorder mr, int what, int extra) {
                if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {

                    recording = false;
                    try{
                        mRecorder.release();
                        mRecorder = null;
                        ((LinearLayout) mView).removeView(mPreview);

                        start(mView, mContext, mDuration, mFps);

                    }catch(RuntimeException stopException){
                        Log.i("TAG",stopException.getMessage());
                    }
                }
            }
        });
        mRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        mRecorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);

        CamcorderProfile cpHigh = CamcorderProfile
                .get(CamcorderProfile.QUALITY_HIGH);
        mRecorder.setProfile(cpHigh);
        mRecorder.setOutputFile(path);
        mRecorder.setMaxDuration(max_duration_ms);
        mRecorder.setCaptureRate(fps);

    }

    public void end(){
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        Uri screenshotUri = Uri.parse(path);
        sharingIntent.setType("video/*");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
        mContext.startActivity(Intent.createChooser(sharingIntent, "Share image using"));
        File tbd = new File(path);
        tbd.deleteOnExit();
    }

    public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

        public CameraPreview(Context context) {
            super(context);
            holder =getHolder();
            holder.addCallback(this);
            holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            recording = false;
            mRecorder.setPreviewDisplay(holder.getSurface());
            try {
                mRecorder.prepare();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (recording) {
            //    mRecorder.stop();
                recording = false;
            }
           // mRecorder.release();*/
           // finish();
        }
    }
}
