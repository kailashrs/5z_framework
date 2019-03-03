package android.nfc.tech;

import android.nfc.INfcTag;
import android.nfc.Tag;
import android.nfc.TransceiveResult;
import android.os.RemoteException;
import android.util.Log;
import java.io.IOException;

abstract class BasicTagTechnology
  implements TagTechnology
{
  private static final String TAG = "NFC";
  boolean mIsConnected;
  int mSelectedTechnology;
  final Tag mTag;
  
  BasicTagTechnology(Tag paramTag, int paramInt)
    throws RemoteException
  {
    mTag = paramTag;
    mSelectedTechnology = paramInt;
  }
  
  void checkConnected()
  {
    if ((mTag.getConnectedTechnology() == mSelectedTechnology) && (mTag.getConnectedTechnology() != -1)) {
      return;
    }
    throw new IllegalStateException("Call connect() first!");
  }
  
  /* Error */
  public void close()
    throws IOException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 25	android/nfc/tech/BasicTagTechnology:mTag	Landroid/nfc/Tag;
    //   4: invokevirtual 50	android/nfc/Tag:getTagService	()Landroid/nfc/INfcTag;
    //   7: invokeinterface 55 1 0
    //   12: aload_0
    //   13: getfield 25	android/nfc/tech/BasicTagTechnology:mTag	Landroid/nfc/Tag;
    //   16: invokevirtual 50	android/nfc/Tag:getTagService	()Landroid/nfc/INfcTag;
    //   19: aload_0
    //   20: getfield 25	android/nfc/tech/BasicTagTechnology:mTag	Landroid/nfc/Tag;
    //   23: invokevirtual 58	android/nfc/Tag:getServiceHandle	()I
    //   26: invokeinterface 62 2 0
    //   31: pop
    //   32: goto +17 -> 49
    //   35: astore_1
    //   36: goto +26 -> 62
    //   39: astore_1
    //   40: ldc 10
    //   42: ldc 64
    //   44: aload_1
    //   45: invokestatic 70	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   48: pop
    //   49: aload_0
    //   50: iconst_0
    //   51: putfield 72	android/nfc/tech/BasicTagTechnology:mIsConnected	Z
    //   54: aload_0
    //   55: getfield 25	android/nfc/tech/BasicTagTechnology:mTag	Landroid/nfc/Tag;
    //   58: invokevirtual 75	android/nfc/Tag:setTechnologyDisconnected	()V
    //   61: return
    //   62: aload_0
    //   63: iconst_0
    //   64: putfield 72	android/nfc/tech/BasicTagTechnology:mIsConnected	Z
    //   67: aload_0
    //   68: getfield 25	android/nfc/tech/BasicTagTechnology:mTag	Landroid/nfc/Tag;
    //   71: invokevirtual 75	android/nfc/Tag:setTechnologyDisconnected	()V
    //   74: aload_1
    //   75: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	76	0	this	BasicTagTechnology
    //   35	1	1	localObject	Object
    //   39	36	1	localRemoteException	RemoteException
    // Exception table:
    //   from	to	target	type
    //   0	32	35	finally
    //   40	49	35	finally
    //   0	32	39	android/os/RemoteException
  }
  
  public void connect()
    throws IOException
  {
    try
    {
      int i = mTag.getTagService().connect(mTag.getServiceHandle(), mSelectedTechnology);
      if (i == 0)
      {
        mTag.setConnectedTechnology(mSelectedTechnology);
        mIsConnected = true;
        return;
      }
      if (i == -21)
      {
        localObject = new java/lang/UnsupportedOperationException;
        ((UnsupportedOperationException)localObject).<init>("Connecting to this technology is not supported by the NFC adapter.");
        throw ((Throwable)localObject);
      }
      Object localObject = new java/io/IOException;
      ((IOException)localObject).<init>();
      throw ((Throwable)localObject);
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("NFC", "NFC service dead", localRemoteException);
      throw new IOException("NFC service died");
    }
  }
  
  int getMaxTransceiveLengthInternal()
  {
    try
    {
      int i = mTag.getTagService().getMaxTransceiveLength(mSelectedTechnology);
      return i;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("NFC", "NFC service dead", localRemoteException);
    }
    return 0;
  }
  
  public Tag getTag()
  {
    return mTag;
  }
  
  public boolean isConnected()
  {
    if (!mIsConnected) {
      return false;
    }
    try
    {
      boolean bool = mTag.getTagService().isPresent(mTag.getServiceHandle());
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("NFC", "NFC service dead", localRemoteException);
    }
    return false;
  }
  
  public void reconnect()
    throws IOException
  {
    if (mIsConnected) {
      try
      {
        if (mTag.getTagService().reconnect(mTag.getServiceHandle()) == 0) {
          return;
        }
        mIsConnected = false;
        mTag.setTechnologyDisconnected();
        IOException localIOException = new java/io/IOException;
        localIOException.<init>();
        throw localIOException;
      }
      catch (RemoteException localRemoteException)
      {
        mIsConnected = false;
        mTag.setTechnologyDisconnected();
        Log.e("NFC", "NFC service dead", localRemoteException);
        throw new IOException("NFC service died");
      }
    }
    throw new IllegalStateException("Technology not connected yet");
  }
  
  byte[] transceive(byte[] paramArrayOfByte, boolean paramBoolean)
    throws IOException
  {
    checkConnected();
    try
    {
      paramArrayOfByte = mTag.getTagService().transceive(mTag.getServiceHandle(), paramArrayOfByte, paramBoolean);
      if (paramArrayOfByte != null) {
        return paramArrayOfByte.getResponseOrThrow();
      }
      paramArrayOfByte = new java/io/IOException;
      paramArrayOfByte.<init>("transceive failed");
      throw paramArrayOfByte;
    }
    catch (RemoteException paramArrayOfByte)
    {
      Log.e("NFC", "NFC service dead", paramArrayOfByte);
      throw new IOException("NFC service died");
    }
  }
}
