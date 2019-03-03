package android.nfc.tech;

import android.nfc.INfcTag;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import java.io.IOException;

public final class IsoDep
  extends BasicTagTechnology
{
  public static final String EXTRA_HIST_BYTES = "histbytes";
  public static final String EXTRA_HI_LAYER_RESP = "hiresp";
  private static final String TAG = "NFC";
  private byte[] mHiLayerResponse = null;
  private byte[] mHistBytes = null;
  
  public IsoDep(Tag paramTag)
    throws RemoteException
  {
    super(paramTag, 3);
    paramTag = paramTag.getTechExtras(3);
    if (paramTag != null)
    {
      mHiLayerResponse = paramTag.getByteArray("hiresp");
      mHistBytes = paramTag.getByteArray("histbytes");
    }
  }
  
  public static IsoDep get(Tag paramTag)
  {
    if (!paramTag.hasTech(3)) {
      return null;
    }
    try
    {
      paramTag = new IsoDep(paramTag);
      return paramTag;
    }
    catch (RemoteException paramTag) {}
    return null;
  }
  
  public byte[] getHiLayerResponse()
  {
    return mHiLayerResponse;
  }
  
  public byte[] getHistoricalBytes()
  {
    return mHistBytes;
  }
  
  public int getMaxTransceiveLength()
  {
    return getMaxTransceiveLengthInternal();
  }
  
  public int getTimeout()
  {
    try
    {
      int i = mTag.getTagService().getTimeout(3);
      return i;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("NFC", "NFC service dead", localRemoteException);
    }
    return 0;
  }
  
  public boolean isExtendedLengthApduSupported()
  {
    try
    {
      boolean bool = mTag.getTagService().getExtendedLengthApdusSupported();
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("NFC", "NFC service dead", localRemoteException);
    }
    return false;
  }
  
  public void setTimeout(int paramInt)
  {
    try
    {
      if (mTag.getTagService().setTimeout(3, paramInt) != 0)
      {
        IllegalArgumentException localIllegalArgumentException = new java/lang/IllegalArgumentException;
        localIllegalArgumentException.<init>("The supplied timeout is not valid");
        throw localIllegalArgumentException;
      }
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("NFC", "NFC service dead", localRemoteException);
    }
  }
  
  public byte[] transceive(byte[] paramArrayOfByte)
    throws IOException
  {
    return transceive(paramArrayOfByte, true);
  }
}
