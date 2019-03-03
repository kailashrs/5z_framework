package com.android.internal.midi;

import android.media.midi.MidiReceiver;
import android.media.midi.MidiSender;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public final class MidiDispatcher
  extends MidiReceiver
{
  private final MidiReceiverFailureHandler mFailureHandler;
  private final CopyOnWriteArrayList<MidiReceiver> mReceivers = new CopyOnWriteArrayList();
  private final MidiSender mSender = new MidiSender()
  {
    public void onConnect(MidiReceiver paramAnonymousMidiReceiver)
    {
      mReceivers.add(paramAnonymousMidiReceiver);
    }
    
    public void onDisconnect(MidiReceiver paramAnonymousMidiReceiver)
    {
      mReceivers.remove(paramAnonymousMidiReceiver);
    }
  };
  
  public MidiDispatcher()
  {
    this(null);
  }
  
  public MidiDispatcher(MidiReceiverFailureHandler paramMidiReceiverFailureHandler)
  {
    mFailureHandler = paramMidiReceiverFailureHandler;
  }
  
  public int getReceiverCount()
  {
    return mReceivers.size();
  }
  
  public MidiSender getSender()
  {
    return mSender;
  }
  
  public void onFlush()
    throws IOException
  {
    Iterator localIterator = mReceivers.iterator();
    while (localIterator.hasNext())
    {
      MidiReceiver localMidiReceiver = (MidiReceiver)localIterator.next();
      try
      {
        localMidiReceiver.flush();
      }
      catch (IOException localIOException)
      {
        mReceivers.remove(localMidiReceiver);
        if (mFailureHandler != null) {
          mFailureHandler.onReceiverFailure(localMidiReceiver, localIOException);
        }
      }
    }
  }
  
  public void onSend(byte[] paramArrayOfByte, int paramInt1, int paramInt2, long paramLong)
    throws IOException
  {
    Iterator localIterator = mReceivers.iterator();
    while (localIterator.hasNext())
    {
      MidiReceiver localMidiReceiver = (MidiReceiver)localIterator.next();
      try
      {
        localMidiReceiver.send(paramArrayOfByte, paramInt1, paramInt2, paramLong);
      }
      catch (IOException localIOException)
      {
        mReceivers.remove(localMidiReceiver);
        if (mFailureHandler != null) {
          mFailureHandler.onReceiverFailure(localMidiReceiver, localIOException);
        }
      }
    }
  }
  
  public static abstract interface MidiReceiverFailureHandler
  {
    public abstract void onReceiverFailure(MidiReceiver paramMidiReceiver, IOException paramIOException);
  }
}
