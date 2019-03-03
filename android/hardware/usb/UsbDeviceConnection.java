package android.hardware.usb;

import android.annotation.SuppressLint;
import android.annotation.SystemApi;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.ParcelFileDescriptor;
import com.android.internal.util.Preconditions;
import dalvik.system.CloseGuard;
import java.io.FileDescriptor;
import java.util.concurrent.TimeoutException;

public class UsbDeviceConnection
{
  private static final String TAG = "UsbDeviceConnection";
  private final CloseGuard mCloseGuard = CloseGuard.get();
  private Context mContext;
  private final UsbDevice mDevice;
  private long mNativeContext;
  
  public UsbDeviceConnection(UsbDevice paramUsbDevice)
  {
    mDevice = paramUsbDevice;
  }
  
  private static void checkBounds(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    int i;
    if (paramArrayOfByte != null) {
      i = paramArrayOfByte.length;
    } else {
      i = 0;
    }
    if ((paramInt2 >= 0) && (paramInt1 >= 0) && (paramInt1 + paramInt2 <= i)) {
      return;
    }
    throw new IllegalArgumentException("Buffer start or length out of bounds.");
  }
  
  private native int native_bulk_request(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3, int paramInt4);
  
  private native boolean native_claim_interface(int paramInt, boolean paramBoolean);
  
  private native void native_close();
  
  private native int native_control_request(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte, int paramInt5, int paramInt6, int paramInt7);
  
  private native byte[] native_get_desc();
  
  private native int native_get_fd();
  
  private native String native_get_serial();
  
  private native boolean native_open(String paramString, FileDescriptor paramFileDescriptor);
  
  private native boolean native_release_interface(int paramInt);
  
  private native UsbRequest native_request_wait(long paramLong)
    throws TimeoutException;
  
  private native boolean native_reset_device();
  
  private native boolean native_set_configuration(int paramInt);
  
  private native boolean native_set_interface(int paramInt1, int paramInt2);
  
  public int bulkTransfer(UsbEndpoint paramUsbEndpoint, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    return bulkTransfer(paramUsbEndpoint, paramArrayOfByte, 0, paramInt1, paramInt2);
  }
  
  public int bulkTransfer(UsbEndpoint paramUsbEndpoint, byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3)
  {
    checkBounds(paramArrayOfByte, paramInt1, paramInt2);
    int i = paramInt2;
    if (mContext.getApplicationInfo().targetSdkVersion < 28)
    {
      i = paramInt2;
      if (paramInt2 > 16384) {
        i = 16384;
      }
    }
    return native_bulk_request(paramUsbEndpoint.getAddress(), paramArrayOfByte, paramInt1, i, paramInt3);
  }
  
  public boolean claimInterface(UsbInterface paramUsbInterface, boolean paramBoolean)
  {
    return native_claim_interface(paramUsbInterface.getId(), paramBoolean);
  }
  
  public void close()
  {
    if (mNativeContext != 0L)
    {
      native_close();
      mCloseGuard.close();
    }
  }
  
  public int controlTransfer(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte, int paramInt5, int paramInt6)
  {
    return controlTransfer(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfByte, 0, paramInt5, paramInt6);
  }
  
  public int controlTransfer(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte, int paramInt5, int paramInt6, int paramInt7)
  {
    checkBounds(paramArrayOfByte, paramInt5, paramInt6);
    return native_control_request(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfByte, paramInt5, paramInt6, paramInt7);
  }
  
  protected void finalize()
    throws Throwable
  {
    try
    {
      if (mCloseGuard != null) {
        mCloseGuard.warnIfOpen();
      }
      return;
    }
    finally
    {
      super.finalize();
    }
  }
  
  public Context getContext()
  {
    return mContext;
  }
  
  public int getFileDescriptor()
  {
    return native_get_fd();
  }
  
  public byte[] getRawDescriptors()
  {
    return native_get_desc();
  }
  
  public String getSerial()
  {
    return native_get_serial();
  }
  
  boolean open(String paramString, ParcelFileDescriptor paramParcelFileDescriptor, Context paramContext)
  {
    mContext = paramContext.getApplicationContext();
    boolean bool = native_open(paramString, paramParcelFileDescriptor.getFileDescriptor());
    if (bool) {
      mCloseGuard.open("close");
    }
    return bool;
  }
  
  public boolean releaseInterface(UsbInterface paramUsbInterface)
  {
    return native_release_interface(paramUsbInterface.getId());
  }
  
  public UsbRequest requestWait()
  {
    Object localObject = null;
    try
    {
      UsbRequest localUsbRequest = native_request_wait(-1L);
      localObject = localUsbRequest;
    }
    catch (TimeoutException localTimeoutException) {}
    if (localObject != null)
    {
      boolean bool;
      if (mContext.getApplicationInfo().targetSdkVersion >= 26) {
        bool = true;
      } else {
        bool = false;
      }
      localObject.dequeue(bool);
    }
    return localObject;
  }
  
  public UsbRequest requestWait(long paramLong)
    throws TimeoutException
  {
    UsbRequest localUsbRequest = native_request_wait(Preconditions.checkArgumentNonnegative(paramLong, "timeout"));
    if (localUsbRequest != null) {
      localUsbRequest.dequeue(true);
    }
    return localUsbRequest;
  }
  
  @SystemApi
  @SuppressLint({"Doclava125"})
  public boolean resetDevice()
  {
    return native_reset_device();
  }
  
  public boolean setConfiguration(UsbConfiguration paramUsbConfiguration)
  {
    return native_set_configuration(paramUsbConfiguration.getId());
  }
  
  public boolean setInterface(UsbInterface paramUsbInterface)
  {
    return native_set_interface(paramUsbInterface.getId(), paramUsbInterface.getAlternateSetting());
  }
}
