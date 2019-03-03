package android.nfc.tech;

import android.nfc.INfcTag;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import java.io.IOException;

public final class NfcA
  extends BasicTagTechnology
{
  public static final String EXTRA_ATQA = "atqa";
  public static final String EXTRA_SAK = "sak";
  private static final String TAG = "NFC";
  private byte[] mAtqa;
  private short mSak = (short)0;
  
  public NfcA(Tag paramTag)
    throws RemoteException
  {
    super(paramTag, 1);
    if (paramTag.hasTech(8)) {
      mSak = paramTag.getTechExtras(8).getShort("sak");
    }
    paramTag = paramTag.getTechExtras(1);
    mSak = ((short)(short)(mSak | paramTag.getShort("sak")));
    mAtqa = paramTag.getByteArray("atqa");
  }
  
  public static NfcA get(Tag paramTag)
  {
    if (!paramTag.hasTech(1)) {
      return null;
    }
    try
    {
      paramTag = new NfcA(paramTag);
      return paramTag;
    }
    catch (RemoteException paramTag) {}
    return null;
  }
  
  public byte[] getAtqa()
  {
    return mAtqa;
  }
  
  public int getMaxTransceiveLength()
  {
    return getMaxTransceiveLengthInternal();
  }
  
  public short getSak()
  {
    return mSak;
  }
  
  public int getTimeout()
  {
    try
    {
      int i = mTag.getTagService().getTimeout(1);
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
      if (mTag.getTagService().setTimeout(1, paramInt) != 0)
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
