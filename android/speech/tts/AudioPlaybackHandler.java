package android.speech.tts;

import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;

class AudioPlaybackHandler
{
  private static final boolean DBG = false;
  private static final String TAG = "TTS.AudioPlaybackHandler";
  private volatile PlaybackQueueItem mCurrentWorkItem = null;
  private final Thread mHandlerThread = new Thread(new MessageLoop(null), "TTS.AudioPlaybackThread");
  private final LinkedBlockingQueue<PlaybackQueueItem> mQueue = new LinkedBlockingQueue();
  
  AudioPlaybackHandler() {}
  
  private void removeAllMessages()
  {
    mQueue.clear();
  }
  
  private void removeWorkItemsFor(Object paramObject)
  {
    Iterator localIterator = mQueue.iterator();
    while (localIterator.hasNext())
    {
      PlaybackQueueItem localPlaybackQueueItem = (PlaybackQueueItem)localIterator.next();
      if (localPlaybackQueueItem.getCallerIdentity() == paramObject)
      {
        localIterator.remove();
        stop(localPlaybackQueueItem);
      }
    }
  }
  
  private void stop(PlaybackQueueItem paramPlaybackQueueItem)
  {
    if (paramPlaybackQueueItem == null) {
      return;
    }
    paramPlaybackQueueItem.stop(-2);
  }
  
  public void enqueue(PlaybackQueueItem paramPlaybackQueueItem)
  {
    try
    {
      mQueue.put(paramPlaybackQueueItem);
    }
    catch (InterruptedException paramPlaybackQueueItem) {}
  }
  
  public boolean isSpeaking()
  {
    boolean bool;
    if ((mQueue.peek() == null) && (mCurrentWorkItem == null)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public void quit()
  {
    removeAllMessages();
    stop(mCurrentWorkItem);
    mHandlerThread.interrupt();
  }
  
  public void start()
  {
    mHandlerThread.start();
  }
  
  public void stop()
  {
    removeAllMessages();
    stop(mCurrentWorkItem);
  }
  
  public void stopForApp(Object paramObject)
  {
    removeWorkItemsFor(paramObject);
    PlaybackQueueItem localPlaybackQueueItem = mCurrentWorkItem;
    if ((localPlaybackQueueItem != null) && (localPlaybackQueueItem.getCallerIdentity() == paramObject)) {
      stop(localPlaybackQueueItem);
    }
  }
  
  private final class MessageLoop
    implements Runnable
  {
    private MessageLoop() {}
    
    public void run()
    {
      try
      {
        for (;;)
        {
          PlaybackQueueItem localPlaybackQueueItem = (PlaybackQueueItem)mQueue.take();
          AudioPlaybackHandler.access$202(AudioPlaybackHandler.this, localPlaybackQueueItem);
          localPlaybackQueueItem.run();
          AudioPlaybackHandler.access$202(AudioPlaybackHandler.this, null);
        }
        return;
      }
      catch (InterruptedException localInterruptedException) {}
    }
  }
}
