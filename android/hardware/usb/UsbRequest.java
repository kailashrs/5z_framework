package android.hardware.usb;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Log;
import com.android.internal.util.Preconditions;
import dalvik.system.CloseGuard;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;

public class UsbRequest
{
  static final int MAX_USBFS_BUFFER_SIZE = 16384;
  private static final String TAG = "UsbRequest";
  private ByteBuffer mBuffer;
  private Object mClientData;
  private final CloseGuard mCloseGuard = CloseGuard.get();
  private UsbDeviceConnection mConnection;
  private UsbEndpoint mEndpoint;
  private boolean mIsUsingNewQueue;
  private int mLength;
  private final Object mLock = new Object();
  private long mNativeContext;
  private ByteBuffer mTempBuffer;
  
  public UsbRequest() {}
  
  private native boolean native_cancel();
  
  private native void native_close();
  
  private native int native_dequeue_array(byte[] paramArrayOfByte, int paramInt, boolean paramBoolean);
  
  private native int native_dequeue_direct();
  
  private native boolean native_init(UsbDeviceConnection paramUsbDeviceConnection, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  private native boolean native_queue(ByteBuffer paramByteBuffer, int paramInt1, int paramInt2);
  
  private native boolean native_queue_array(byte[] paramArrayOfByte, int paramInt, boolean paramBoolean);
  
  private native boolean native_queue_direct(ByteBuffer paramByteBuffer, int paramInt, boolean paramBoolean);
  
  public boolean cancel()
  {
    return native_cancel();
  }
  
  public void close()
  {
    if (mNativeContext != 0L)
    {
      mEndpoint = null;
      mConnection = null;
      native_close();
      mCloseGuard.close();
    }
  }
  
  void dequeue(boolean paramBoolean)
  {
    boolean bool;
    if (mEndpoint.getDirection() == 0) {
      bool = true;
    } else {
      bool = false;
    }
    synchronized (mLock)
    {
      int i;
      if (mIsUsingNewQueue)
      {
        i = native_dequeue_direct();
        mIsUsingNewQueue = false;
        if (mBuffer != null) {
          if (mTempBuffer == null)
          {
            mBuffer.position(mBuffer.position() + i);
          }
          else
          {
            mTempBuffer.limit(i);
            if (bool) {
              try
              {
                mBuffer.position(mBuffer.position() + i);
              }
              finally
              {
                break label140;
              }
            } else {
              mBuffer.put(mTempBuffer);
            }
            mTempBuffer = null;
            break label309;
            label140:
            mTempBuffer = null;
            throw localObject2;
          }
        }
      }
      else
      {
        int j;
        if (mBuffer.isDirect()) {
          j = native_dequeue_direct();
        } else {
          j = native_dequeue_array(mBuffer.array(), mLength, bool);
        }
        i = j;
        if (j >= 0)
        {
          i = Math.min(j, mLength);
          try
          {
            mBuffer.position(i);
            i = j;
          }
          catch (IllegalArgumentException localIllegalArgumentException)
          {
            if (paramBoolean)
            {
              Object localObject3 = new java/lang/StringBuilder;
              ((StringBuilder)localObject3).<init>();
              ((StringBuilder)localObject3).append("Buffer ");
              ((StringBuilder)localObject3).append(mBuffer);
              ((StringBuilder)localObject3).append(" does not have enough space to read ");
              ((StringBuilder)localObject3).append(i);
              ((StringBuilder)localObject3).append(" bytes");
              Log.e("UsbRequest", ((StringBuilder)localObject3).toString(), localIllegalArgumentException);
              localObject3 = new java/nio/BufferOverflowException;
              ((BufferOverflowException)localObject3).<init>();
              throw ((Throwable)localObject3);
            }
            throw localIllegalArgumentException;
          }
        }
      }
      label309:
      mBuffer = null;
      mLength = 0;
      return;
    }
  }
  
  protected void finalize()
    throws Throwable
  {
    try
    {
      if (mCloseGuard != null) {
        mCloseGuard.warnIfOpen();
      }
      close();
      return;
    }
    finally
    {
      super.finalize();
    }
  }
  
  public Object getClientData()
  {
    return mClientData;
  }
  
  public UsbEndpoint getEndpoint()
  {
    return mEndpoint;
  }
  
  public boolean initialize(UsbDeviceConnection paramUsbDeviceConnection, UsbEndpoint paramUsbEndpoint)
  {
    mEndpoint = paramUsbEndpoint;
    mConnection = ((UsbDeviceConnection)Preconditions.checkNotNull(paramUsbDeviceConnection, "connection"));
    boolean bool = native_init(paramUsbDeviceConnection, paramUsbEndpoint.getAddress(), paramUsbEndpoint.getAttributes(), paramUsbEndpoint.getMaxPacketSize(), paramUsbEndpoint.getInterval());
    if (bool) {
      mCloseGuard.open("close");
    }
    return bool;
  }
  
  public boolean queue(ByteBuffer paramByteBuffer)
  {
    boolean bool;
    if (mNativeContext != 0L) {
      bool = true;
    } else {
      bool = false;
    }
    Preconditions.checkState(bool, "request is not initialized");
    Preconditions.checkState(mIsUsingNewQueue ^ true, "this request is currently queued");
    int i;
    if (mEndpoint.getDirection() == 0) {
      i = 1;
    } else {
      i = 0;
    }
    synchronized (mLock)
    {
      mBuffer = paramByteBuffer;
      if (paramByteBuffer == null)
      {
        mIsUsingNewQueue = true;
        bool = native_queue(null, 0, 0);
      }
      else
      {
        if (mConnection.getContext().getApplicationInfo().targetSdkVersion < 28) {
          Preconditions.checkArgumentInRange(paramByteBuffer.remaining(), 0, 16384, "number of remaining bytes");
        }
        if ((paramByteBuffer.isReadOnly()) && (i == 0)) {
          bool = false;
        } else {
          bool = true;
        }
        Preconditions.checkArgument(bool, "buffer can not be read-only when receiving data");
        ByteBuffer localByteBuffer = paramByteBuffer;
        if (!paramByteBuffer.isDirect())
        {
          mTempBuffer = ByteBuffer.allocateDirect(mBuffer.remaining());
          if (i != 0)
          {
            mBuffer.mark();
            mTempBuffer.put(mBuffer);
            mTempBuffer.flip();
            mBuffer.reset();
          }
          localByteBuffer = mTempBuffer;
        }
        mIsUsingNewQueue = true;
        bool = native_queue(localByteBuffer, localByteBuffer.position(), localByteBuffer.remaining());
      }
      if (!bool)
      {
        mIsUsingNewQueue = false;
        mTempBuffer = null;
        mBuffer = null;
      }
      return bool;
    }
  }
  
  @Deprecated
  public boolean queue(ByteBuffer paramByteBuffer, int paramInt)
  {
    boolean bool;
    if (mEndpoint.getDirection() == 0) {
      bool = true;
    } else {
      bool = false;
    }
    int i = paramInt;
    if (mConnection.getContext().getApplicationInfo().targetSdkVersion < 28)
    {
      i = paramInt;
      if (paramInt > 16384) {
        i = 16384;
      }
    }
    synchronized (mLock)
    {
      mBuffer = paramByteBuffer;
      mLength = i;
      if (paramByteBuffer.isDirect()) {}
      for (bool = native_queue_direct(paramByteBuffer, i, bool);; bool = native_queue_array(paramByteBuffer.array(), i, bool))
      {
        break;
        if (!paramByteBuffer.hasArray()) {
          break label133;
        }
      }
      if (!bool)
      {
        mBuffer = null;
        mLength = 0;
      }
      return bool;
      label133:
      paramByteBuffer = new java/lang/IllegalArgumentException;
      paramByteBuffer.<init>("buffer is not direct and has no array");
      throw paramByteBuffer;
    }
  }
  
  public void setClientData(Object paramObject)
  {
    mClientData = paramObject;
  }
}
