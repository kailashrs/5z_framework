package android.bluetooth;

import android.os.Handler;
import android.os.Message;
import android.os.ParcelUuid;
import android.util.Log;
import java.io.Closeable;
import java.io.IOException;

public final class BluetoothServerSocket
  implements Closeable
{
  private static final boolean DBG = false;
  private static final String TAG = "BluetoothServerSocket";
  private int mChannel;
  private Handler mHandler;
  private int mMessage;
  final BluetoothSocket mSocket;
  
  BluetoothServerSocket(int paramInt1, boolean paramBoolean1, boolean paramBoolean2, int paramInt2)
    throws IOException
  {
    mChannel = paramInt2;
    mSocket = new BluetoothSocket(paramInt1, -1, paramBoolean1, paramBoolean2, null, paramInt2, null);
    if (paramInt2 == -2) {
      mSocket.setExcludeSdp(true);
    }
  }
  
  BluetoothServerSocket(int paramInt1, boolean paramBoolean1, boolean paramBoolean2, int paramInt2, boolean paramBoolean3, boolean paramBoolean4)
    throws IOException
  {
    mChannel = paramInt2;
    mSocket = new BluetoothSocket(paramInt1, -1, paramBoolean1, paramBoolean2, null, paramInt2, null, paramBoolean3, paramBoolean4);
    if (paramInt2 == -2) {
      mSocket.setExcludeSdp(true);
    }
  }
  
  BluetoothServerSocket(int paramInt, boolean paramBoolean1, boolean paramBoolean2, ParcelUuid paramParcelUuid)
    throws IOException
  {
    mSocket = new BluetoothSocket(paramInt, -1, paramBoolean1, paramBoolean2, null, -1, paramParcelUuid);
    mChannel = mSocket.getPort();
  }
  
  public BluetoothSocket accept()
    throws IOException
  {
    return accept(-1);
  }
  
  public BluetoothSocket accept(int paramInt)
    throws IOException
  {
    return mSocket.accept(paramInt);
  }
  
  public void close()
    throws IOException
  {
    try
    {
      if (mHandler != null) {
        mHandler.obtainMessage(mMessage).sendToTarget();
      }
      mSocket.close();
      return;
    }
    finally {}
  }
  
  public int getChannel()
  {
    return mChannel;
  }
  
  public int getPsm()
  {
    return mChannel;
  }
  
  void setChannel(int paramInt)
  {
    if ((mSocket != null) && (mSocket.getPort() != paramInt))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("The port set is different that the underlying port. mSocket.getPort(): ");
      localStringBuilder.append(mSocket.getPort());
      localStringBuilder.append(" requested newChannel: ");
      localStringBuilder.append(paramInt);
      Log.w("BluetoothServerSocket", localStringBuilder.toString());
    }
    mChannel = paramInt;
  }
  
  void setCloseHandler(Handler paramHandler, int paramInt)
  {
    try
    {
      mHandler = paramHandler;
      mMessage = paramInt;
      return;
    }
    finally
    {
      paramHandler = finally;
      throw paramHandler;
    }
  }
  
  void setServiceName(String paramString)
  {
    mSocket.setServiceName(paramString);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("ServerSocket: Type: ");
    switch (mSocket.getConnectionType())
    {
    default: 
      break;
    case 4: 
      localStringBuilder.append("TYPE_L2CAP_LE");
      break;
    case 3: 
      localStringBuilder.append("TYPE_L2CAP");
      break;
    case 2: 
      localStringBuilder.append("TYPE_SCO");
      break;
    case 1: 
      localStringBuilder.append("TYPE_RFCOMM");
    }
    localStringBuilder.append(" Channel: ");
    localStringBuilder.append(mChannel);
    return localStringBuilder.toString();
  }
}
