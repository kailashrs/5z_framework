package com.android.internal.midi;

import android.media.midi.MidiReceiver;
import java.io.IOException;

public class MidiEventScheduler
  extends EventScheduler
{
  private static final int POOL_EVENT_SIZE = 16;
  private static final String TAG = "MidiEventScheduler";
  private MidiReceiver mReceiver = new SchedulingReceiver(null);
  
  public MidiEventScheduler() {}
  
  private MidiEvent createScheduledEvent(byte[] paramArrayOfByte, int paramInt1, int paramInt2, long paramLong)
  {
    MidiEvent localMidiEvent;
    if (paramInt2 > 16)
    {
      localMidiEvent = new MidiEvent(paramArrayOfByte, paramInt1, paramInt2, paramLong, null);
    }
    else
    {
      localMidiEvent = (MidiEvent)removeEventfromPool();
      if (localMidiEvent == null) {
        localMidiEvent = new MidiEvent(16, null);
      }
      System.arraycopy(paramArrayOfByte, paramInt1, data, 0, paramInt2);
      count = paramInt2;
      localMidiEvent.setTimestamp(paramLong);
    }
    return localMidiEvent;
  }
  
  public void addEventToPool(EventScheduler.SchedulableEvent paramSchedulableEvent)
  {
    if (((paramSchedulableEvent instanceof MidiEvent)) && (data.length == 16)) {
      super.addEventToPool(paramSchedulableEvent);
    }
  }
  
  public MidiReceiver getReceiver()
  {
    return mReceiver;
  }
  
  public static class MidiEvent
    extends EventScheduler.SchedulableEvent
  {
    public int count = 0;
    public byte[] data;
    
    private MidiEvent(int paramInt)
    {
      super();
      data = new byte[paramInt];
    }
    
    private MidiEvent(byte[] paramArrayOfByte, int paramInt1, int paramInt2, long paramLong)
    {
      super();
      data = new byte[paramInt2];
      System.arraycopy(paramArrayOfByte, paramInt1, data, 0, paramInt2);
      count = paramInt2;
    }
    
    public String toString()
    {
      String str = "Event: ";
      for (int i = 0; i < count; i++)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append(str);
        localStringBuilder.append(data[i]);
        localStringBuilder.append(", ");
        str = localStringBuilder.toString();
      }
      return str;
    }
  }
  
  private class SchedulingReceiver
    extends MidiReceiver
  {
    private SchedulingReceiver() {}
    
    public void onFlush()
    {
      flush();
    }
    
    public void onSend(byte[] paramArrayOfByte, int paramInt1, int paramInt2, long paramLong)
      throws IOException
    {
      paramArrayOfByte = MidiEventScheduler.this.createScheduledEvent(paramArrayOfByte, paramInt1, paramInt2, paramLong);
      if (paramArrayOfByte != null) {
        add(paramArrayOfByte);
      }
    }
  }
}
