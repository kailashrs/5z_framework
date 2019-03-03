package android.speech.tts;

import android.media.AudioTrack;
import android.media.AudioTrack.OnPlaybackPositionUpdateListener;
import android.util.Log;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

final class SynthesisPlaybackQueueItem
  extends PlaybackQueueItem
  implements AudioTrack.OnPlaybackPositionUpdateListener
{
  private static final boolean DBG = false;
  private static final long MAX_UNCONSUMED_AUDIO_MS = 500L;
  private static final int NOT_RUN = 0;
  private static final int RUN_CALLED = 1;
  private static final int STOP_CALLED = 2;
  private static final String TAG = "TTS.SynthQueueItem";
  private final BlockingAudioTrack mAudioTrack;
  private final LinkedList<ListEntry> mDataBufferList = new LinkedList();
  private volatile boolean mDone = false;
  private final Lock mListLock = new ReentrantLock();
  private final AbstractEventLogger mLogger;
  private final Condition mNotFull = mListLock.newCondition();
  private final Condition mReadReady = mListLock.newCondition();
  private final AtomicInteger mRunState = new AtomicInteger(0);
  private volatile int mStatusCode = 0;
  private volatile boolean mStopped = false;
  private int mUnconsumedBytes = 0;
  private ConcurrentLinkedQueue<ProgressMarker> markerList = new ConcurrentLinkedQueue();
  
  SynthesisPlaybackQueueItem(TextToSpeechService.AudioOutputParams paramAudioOutputParams, int paramInt1, int paramInt2, int paramInt3, TextToSpeechService.UtteranceProgressDispatcher paramUtteranceProgressDispatcher, Object paramObject, AbstractEventLogger paramAbstractEventLogger)
  {
    super(paramUtteranceProgressDispatcher, paramObject);
    mAudioTrack = new BlockingAudioTrack(paramAudioOutputParams, paramInt1, paramInt2, paramInt3);
    mLogger = paramAbstractEventLogger;
  }
  
  private void dispatchEndStatus()
  {
    TextToSpeechService.UtteranceProgressDispatcher localUtteranceProgressDispatcher = getDispatcher();
    if (mStatusCode == 0) {
      localUtteranceProgressDispatcher.dispatchOnSuccess();
    } else if (mStatusCode == -2) {
      localUtteranceProgressDispatcher.dispatchOnStop();
    } else {
      localUtteranceProgressDispatcher.dispatchOnError(mStatusCode);
    }
    mLogger.onCompleted(mStatusCode);
  }
  
  private byte[] take()
    throws InterruptedException
  {
    try
    {
      mListLock.lock();
      while ((mDataBufferList.size() == 0) && (!mStopped) && (!mDone)) {
        mReadReady.await();
      }
      boolean bool = mStopped;
      if (bool) {
        return null;
      }
      Object localObject1 = (ListEntry)mDataBufferList.poll();
      if (localObject1 == null) {
        return null;
      }
      mUnconsumedBytes -= mBytes.length;
      mNotFull.signal();
      localObject1 = mBytes;
      return localObject1;
    }
    finally
    {
      mListLock.unlock();
    }
  }
  
  void done()
  {
    try
    {
      mListLock.lock();
      mDone = true;
      mReadReady.signal();
      mNotFull.signal();
      return;
    }
    finally
    {
      mListLock.unlock();
    }
  }
  
  public void onMarkerReached(AudioTrack paramAudioTrack)
  {
    paramAudioTrack = (ProgressMarker)markerList.poll();
    if (paramAudioTrack == null)
    {
      Log.e("TTS.SynthQueueItem", "onMarkerReached reached called but no marker in queue");
      return;
    }
    getDispatcher().dispatchOnRangeStart(start, end, frames);
    updateMarker();
  }
  
  public void onPeriodicNotification(AudioTrack paramAudioTrack) {}
  
  void put(byte[] paramArrayOfByte)
    throws InterruptedException
  {
    try
    {
      mListLock.lock();
      while ((mAudioTrack.getAudioLengthMs(mUnconsumedBytes) > 500L) && (!mStopped)) {
        mNotFull.await();
      }
      boolean bool = mStopped;
      if (bool) {
        return;
      }
      LinkedList localLinkedList = mDataBufferList;
      ListEntry localListEntry = new android/speech/tts/SynthesisPlaybackQueueItem$ListEntry;
      localListEntry.<init>(paramArrayOfByte);
      localLinkedList.add(localListEntry);
      mUnconsumedBytes += paramArrayOfByte.length;
      mReadReady.signal();
      return;
    }
    finally
    {
      mListLock.unlock();
    }
  }
  
  void rangeStart(int paramInt1, int paramInt2, int paramInt3)
  {
    markerList.add(new ProgressMarker(paramInt1, paramInt2, paramInt3));
    updateMarker();
  }
  
  public void run()
  {
    if (!mRunState.compareAndSet(0, 1)) {
      return;
    }
    Object localObject = getDispatcher();
    ((TextToSpeechService.UtteranceProgressDispatcher)localObject).dispatchOnStart();
    if (!mAudioTrack.init())
    {
      ((TextToSpeechService.UtteranceProgressDispatcher)localObject).dispatchOnError(-5);
      return;
    }
    mAudioTrack.setPlaybackPositionUpdateListener(this);
    updateMarker();
    try
    {
      for (;;)
      {
        localObject = take();
        if (localObject == null) {
          break;
        }
        mAudioTrack.write((byte[])localObject);
        mLogger.onAudioDataWritten();
      }
    }
    catch (InterruptedException localInterruptedException) {}
    mAudioTrack.waitAndRelease();
    dispatchEndStatus();
  }
  
  void stop(int paramInt)
  {
    try
    {
      mListLock.lock();
      mStopped = true;
      mStatusCode = paramInt;
      mNotFull.signal();
      if (mRunState.getAndSet(2) == 0)
      {
        dispatchEndStatus();
        return;
      }
      mReadReady.signal();
      mListLock.unlock();
      mAudioTrack.stop();
      return;
    }
    finally
    {
      mListLock.unlock();
    }
  }
  
  void updateMarker()
  {
    ProgressMarker localProgressMarker = (ProgressMarker)markerList.peek();
    if (localProgressMarker != null)
    {
      int i;
      if (frames == 0) {
        i = 1;
      } else {
        i = frames;
      }
      mAudioTrack.setNotificationMarkerPosition(i);
    }
  }
  
  static final class ListEntry
  {
    final byte[] mBytes;
    
    ListEntry(byte[] paramArrayOfByte)
    {
      mBytes = paramArrayOfByte;
    }
  }
  
  private class ProgressMarker
  {
    public final int end;
    public final int frames;
    public final int start;
    
    public ProgressMarker(int paramInt1, int paramInt2, int paramInt3)
    {
      frames = paramInt1;
      start = paramInt2;
      end = paramInt3;
    }
  }
}
