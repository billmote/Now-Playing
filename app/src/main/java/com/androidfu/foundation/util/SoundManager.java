package com.androidfu.foundation.util;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.SparseIntArray;

import hugo.weaving.DebugLog;

/**
 * Created by Martin Andersson on 9/23/14.
 */
@DebugLog
public class SoundManager {

    private static SoundPool mSoundPool;
    private static SparseIntArray mSoundPoolMap;
    private static AudioManager mAudioManager;
    private static Context mContext;
    private static Boolean soundEnabled = true;

    private SoundManager() {
        // Don't let this get instantiated directly.
    }

    public static void initSounds(Context context) {
        mContext = context;
        mSoundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        mSoundPoolMap = new SparseIntArray();
        mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
    }

    public static void addSound(int soundID) {
        mSoundPoolMap.put(soundID, mSoundPool.load(mContext, soundID, 1));
    }

    public static int playSound(int soundID) {
        int streamID = 0;
        if (soundEnabled) {
            if (mAudioManager != null) {
                int streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                streamID = mSoundPool.play(mSoundPoolMap.get(soundID), streamVolume, streamVolume, 1, 0, 1f);
            }
        }
        return streamID;
    }

    public static void stopSound(int streamID) {
        if (mAudioManager != null) {
            mSoundPool.stop(streamID);
        }
    }

    public static int playLoopedSound(int soundID) {
        int streamID = 0;
        if (soundEnabled) {
            int streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            streamID = mSoundPool.play(mSoundPoolMap.get(soundID), streamVolume, streamVolume, 1, -1, 1f);
        }
        return streamID;
    }

    public static void toggleSound() {
        if (soundEnabled) {
            soundEnabled = false;
        } else {
            soundEnabled = true;
        }
    }

    public boolean isSoundEnabled() {
        return soundEnabled;
    }

}