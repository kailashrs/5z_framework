package com.android.internal.midi;

import android.media.midi.MidiReceiver;
import java.io.IOException;

public class MidiFramer
  extends MidiReceiver
{
  public String TAG = "MidiFramer";
  private byte[] mBuffer = new byte[3];
  private int mCount;
  private boolean mInSysEx;
  private int mNeeded;
  private MidiReceiver mReceiver;
  private byte mRunningStatus;
  
  public MidiFramer(MidiReceiver paramMidiReceiver)
  {
    mReceiver = paramMidiReceiver;
  }
  
  public static String formatMidiData(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("MIDI+");
    ((StringBuilder)localObject).append(paramInt1);
    ((StringBuilder)localObject).append(" : ");
    localObject = ((StringBuilder)localObject).toString();
    for (int i = 0; i < paramInt2; i++)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append((String)localObject);
      localStringBuilder.append(String.format("0x%02X, ", new Object[] { Byte.valueOf(paramArrayOfByte[(paramInt1 + i)]) }));
      localObject = localStringBuilder.toString();
    }
    return localObject;
  }
  
  public void onSend(byte[] paramArrayOfByte, int paramInt1, int paramInt2, long paramLong)
    throws IOException
  {
    int i;
    if (mInSysEx) {
      i = paramInt1;
    } else {
      i = -1;
    }
    int j = 0;
    while (j < paramInt2)
    {
      int k = paramArrayOfByte[paramInt1];
      int m = k & 0xFF;
      if (m >= 128)
      {
        if (m < 240)
        {
          mRunningStatus = k;
          mCount = 1;
          mNeeded = (MidiConstants.getBytesPerMessage(k) - 1);
          m = i;
        }
        else if (m < 248)
        {
          if (m == 240) {
            mInSysEx = true;
          }
          for (i = paramInt1;; i = -1)
          {
            m = i;
            break label367;
            if (m != 247) {
              break;
            }
            m = i;
            if (!mInSysEx) {
              break label367;
            }
            mReceiver.send(paramArrayOfByte, i, paramInt1 - i + 1, paramLong);
            mInSysEx = false;
          }
          mBuffer[0] = k;
          mRunningStatus = ((byte)0);
          mCount = 1;
          mNeeded = (MidiConstants.getBytesPerMessage(k) - 1);
          m = i;
        }
        else
        {
          m = i;
          if (mInSysEx)
          {
            mReceiver.send(paramArrayOfByte, i, paramInt1 - i, paramLong);
            m = paramInt1 + 1;
          }
          mReceiver.send(paramArrayOfByte, paramInt1, 1, paramLong);
        }
      }
      else
      {
        m = i;
        if (!mInSysEx)
        {
          byte[] arrayOfByte = mBuffer;
          m = mCount;
          mCount = (m + 1);
          arrayOfByte[m] = k;
          int n = mNeeded - 1;
          mNeeded = n;
          m = i;
          if (n == 0)
          {
            if (mRunningStatus != 0) {
              mBuffer[0] = ((byte)mRunningStatus);
            }
            mReceiver.send(mBuffer, 0, mCount, paramLong);
            mNeeded = (MidiConstants.getBytesPerMessage(mBuffer[0]) - 1);
            mCount = 1;
            m = i;
          }
        }
      }
      label367:
      paramInt1++;
      j++;
      i = m;
    }
    if ((i >= 0) && (i < paramInt1)) {
      mReceiver.send(paramArrayOfByte, i, paramInt1 - i, paramLong);
    }
  }
}
