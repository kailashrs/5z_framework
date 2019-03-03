package android.media;

import android.os.Handler;

class NativeRoutingEventHandlerDelegate
{
  private AudioRouting mAudioRouting;
  private Handler mHandler;
  private AudioRouting.OnRoutingChangedListener mOnRoutingChangedListener;
  
  NativeRoutingEventHandlerDelegate(AudioRouting paramAudioRouting, AudioRouting.OnRoutingChangedListener paramOnRoutingChangedListener, Handler paramHandler)
  {
    mAudioRouting = paramAudioRouting;
    mOnRoutingChangedListener = paramOnRoutingChangedListener;
    mHandler = paramHandler;
  }
  
  void notifyClient()
  {
    if (mHandler != null) {
      mHandler.post(new Runnable()
      {
        public void run()
        {
          if (mOnRoutingChangedListener != null) {
            mOnRoutingChangedListener.onRoutingChanged(mAudioRouting);
          }
        }
      });
    }
  }
}
