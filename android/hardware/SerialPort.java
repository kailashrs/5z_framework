package android.hardware;

import android.os.ParcelFileDescriptor;
import java.io.FileDescriptor;
import java.io.IOException;
import java.nio.ByteBuffer;

public class SerialPort
{
  private static final String TAG = "SerialPort";
  private ParcelFileDescriptor mFileDescriptor;
  private final String mName;
  private int mNativeContext;
  
  public SerialPort(String paramString)
  {
    mName = paramString;
  }
  
  private native void native_close();
  
  private native void native_open(FileDescriptor paramFileDescriptor, int paramInt)
    throws IOException;
  
  private native int native_read_array(byte[] paramArrayOfByte, int paramInt)
    throws IOException;
  
  private native int native_read_direct(ByteBuffer paramByteBuffer, int paramInt)
    throws IOException;
  
  private native void native_send_break();
  
  private native void native_write_array(byte[] paramArrayOfByte, int paramInt)
    throws IOException;
  
  private native void native_write_direct(ByteBuffer paramByteBuffer, int paramInt)
    throws IOException;
  
  public void close()
    throws IOException
  {
    if (mFileDescriptor != null)
    {
      mFileDescriptor.close();
      mFileDescriptor = null;
    }
    native_close();
  }
  
  public String getName()
  {
    return mName;
  }
  
  public void open(ParcelFileDescriptor paramParcelFileDescriptor, int paramInt)
    throws IOException
  {
    native_open(paramParcelFileDescriptor.getFileDescriptor(), paramInt);
    mFileDescriptor = paramParcelFileDescriptor;
  }
  
  public int read(ByteBuffer paramByteBuffer)
    throws IOException
  {
    if (paramByteBuffer.isDirect()) {
      return native_read_direct(paramByteBuffer, paramByteBuffer.remaining());
    }
    if (paramByteBuffer.hasArray()) {
      return native_read_array(paramByteBuffer.array(), paramByteBuffer.remaining());
    }
    throw new IllegalArgumentException("buffer is not direct and has no array");
  }
  
  public void sendBreak()
  {
    native_send_break();
  }
  
  public void write(ByteBuffer paramByteBuffer, int paramInt)
    throws IOException
  {
    if (paramByteBuffer.isDirect())
    {
      native_write_direct(paramByteBuffer, paramInt);
    }
    else
    {
      if (!paramByteBuffer.hasArray()) {
        break label33;
      }
      native_write_array(paramByteBuffer.array(), paramInt);
    }
    return;
    label33:
    throw new IllegalArgumentException("buffer is not direct and has no array");
  }
}
