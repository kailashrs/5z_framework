package android.media.midi;

import java.io.IOException;

public abstract class MidiReceiver
{
  private final int mMaxMessageSize;
  
  public MidiReceiver()
  {
    mMaxMessageSize = Integer.MAX_VALUE;
  }
  
  public MidiReceiver(int paramInt)
  {
    mMaxMessageSize = paramInt;
  }
  
  public void flush()
    throws IOException
  {
    onFlush();
  }
  
  public final int getMaxMessageSize()
  {
    return mMaxMessageSize;
  }
  
  public void onFlush()
    throws IOException
  {}
  
  public abstract void onSend(byte[] paramArrayOfByte, int paramInt1, int paramInt2, long paramLong)
    throws IOException;
  
  public void send(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    send(paramArrayOfByte, paramInt1, paramInt2, 0L);
  }
  
  public void send(byte[] paramArrayOfByte, int paramInt1, int paramInt2, long paramLong)
    throws IOException
  {
    int i = getMaxMessageSize();
    while (paramInt2 > 0)
    {
      int j;
      if (paramInt2 > i) {
        j = i;
      } else {
        j = paramInt2;
      }
      onSend(paramArrayOfByte, paramInt1, j, paramLong);
      paramInt1 += j;
      paramInt2 -= j;
    }
  }
}
