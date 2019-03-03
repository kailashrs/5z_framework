package android.nfc.tech;

import android.nfc.INfcTag;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import java.io.IOException;

public final class NfcF
  extends BasicTagTechnology
{
  public static final String EXTRA_PMM = "pmm";
  public static final String EXTRA_SC = "systemcode";
  private static final String TAG = "NFC";
  private byte[] mManufacturer = null;
  private byte[] mSystemCode = null;
  
  public NfcF(Tag paramTag)
    throws RemoteException
  {
    super(paramTag, 4);
    paramTag = paramTag.getTechExtras(4);
    if (paramTag != null)
    {
      mSystemCode = paramTag.getByteArray("systemcode");
      mManufacturer = paramTag.getByteArray("pmm");
    }
  }
  
  public static NfcF get(Tag paramTag)
  {
    if (!paramTag.hasTech(4)) {
      return null;
    }
    try
    {
      paramTag = new NfcF(paramTag);
      return paramTag;
    }
    catch (RemoteException paramTag) {}
    return null;
  }
  
  public byte[] getManufacturer()
  {
    return mManufacturer;
  }
  
  public int getMaxTransceiveLength()
  {
    return getMaxTransceiveLengthInternal();
  }
  
  public byte[] getSystemCode()
  {
    return mSystemCode;
  }
  
  public int getTimeout()
  {
    try
    {
      int i = mTag.getTagService().getTimeout(4);
      return i;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("NFC", "NFC service dead", localRemoteException);
    }
    return 0;
  }
  
  public void setTimeout(int paramInt)
  {
    try
    {
      if (mTag.getTagService().setTimeout(4, paramInt) != 0)
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
