package android.media.midi;

import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import dalvik.system.CloseGuard;
import java.io.Closeable;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import libcore.io.IoUtils;

public final class MidiInputPort
  extends MidiReceiver
  implements Closeable
{
  private static final String TAG = "MidiInputPort";
  private final byte[] mBuffer = new byte['Ѐ'];
  private IMidiDeviceServer mDeviceServer;
  private FileDescriptor mFileDescriptor;
  private final CloseGuard mGuard = CloseGuard.get();
  private boolean mIsClosed;
  private FileOutputStream mOutputStream;
  private final int mPortNumber;
  private final IBinder mToken;
  
  MidiInputPort(IMidiDeviceServer paramIMidiDeviceServer, IBinder paramIBinder, FileDescriptor paramFileDescriptor, int paramInt)
  {
    super(1015);
    mDeviceServer = paramIMidiDeviceServer;
    mToken = paramIBinder;
    mFileDescriptor = paramFileDescriptor;
    mPortNumber = paramInt;
    mOutputStream = new FileOutputStream(paramFileDescriptor);
    mGuard.open("close");
  }
  
  MidiInputPort(FileDescriptor paramFileDescriptor, int paramInt)
  {
    this(null, null, paramFileDescriptor, paramInt);
  }
  
  FileDescriptor claimFileDescriptor()
  {
    synchronized (mGuard)
    {
      synchronized (mBuffer)
      {
        FileDescriptor localFileDescriptor = mFileDescriptor;
        if (localFileDescriptor == null) {
          return null;
        }
        IoUtils.closeQuietly(mOutputStream);
        mFileDescriptor = null;
        mOutputStream = null;
        mIsClosed = true;
        return localFileDescriptor;
      }
    }
  }
  
  public void close()
    throws IOException
  {
    synchronized (mGuard)
    {
      if (mIsClosed) {
        return;
      }
      mGuard.close();
      synchronized (mBuffer)
      {
        if (mFileDescriptor != null)
        {
          IoUtils.closeQuietly(mFileDescriptor);
          mFileDescriptor = null;
        }
        if (mOutputStream != null)
        {
          mOutputStream.close();
          mOutputStream = null;
        }
        IMidiDeviceServer localIMidiDeviceServer = mDeviceServer;
        if (localIMidiDeviceServer != null) {
          try
          {
            mDeviceServer.closePort(mToken);
          }
          catch (RemoteException localRemoteException)
          {
            Log.e("MidiInputPort", "RemoteException in MidiInputPort.close()");
          }
        }
        mIsClosed = true;
        return;
      }
    }
  }
  
  protected void finalize()
    throws Throwable
  {
    try
    {
      if (mGuard != null) {
        mGuard.warnIfOpen();
      }
      mDeviceServer = null;
      close();
      return;
    }
    finally
    {
      super.finalize();
    }
  }
  
  IMidiDeviceServer getDeviceServer()
  {
    return mDeviceServer;
  }
  
  public final int getPortNumber()
  {
    return mPortNumber;
  }
  
  IBinder getToken()
  {
    return mToken;
  }
  
  public void onFlush()
    throws IOException
  {
    synchronized (mBuffer)
    {
      if (mOutputStream != null)
      {
        int i = MidiPortImpl.packFlush(mBuffer);
        mOutputStream.write(mBuffer, 0, i);
        return;
      }
      IOException localIOException = new java/io/IOException;
      localIOException.<init>("MidiInputPort is closed");
      throw localIOException;
    }
  }
  
  public void onSend(byte[] paramArrayOfByte, int paramInt1, int paramInt2, long paramLong)
    throws IOException
  {
    if ((paramInt1 >= 0) && (paramInt2 >= 0) && (paramInt1 + paramInt2 <= paramArrayOfByte.length))
    {
      if (paramInt2 <= 1015) {
        synchronized (mBuffer)
        {
          if (mOutputStream != null)
          {
            paramInt1 = MidiPortImpl.packData(paramArrayOfByte, paramInt1, paramInt2, paramLong, mBuffer);
            mOutputStream.write(mBuffer, 0, paramInt1);
            return;
          }
          paramArrayOfByte = new java/io/IOException;
          paramArrayOfByte.<init>("MidiInputPort is closed");
          throw paramArrayOfByte;
        }
      }
      throw new IllegalArgumentException("count exceeds max message size");
    }
    throw new IllegalArgumentException("offset or count out of range");
  }
}
