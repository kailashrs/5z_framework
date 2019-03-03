package android.nfc.tech;

import android.nfc.Tag;
import android.os.Bundle;
import android.os.RemoteException;
import java.io.IOException;

public final class NfcB
  extends BasicTagTechnology
{
  public static final String EXTRA_APPDATA = "appdata";
  public static final String EXTRA_PROTINFO = "protinfo";
  private byte[] mAppData;
  private byte[] mProtInfo;
  
  public NfcB(Tag paramTag)
    throws RemoteException
  {
    super(paramTag, 2);
    paramTag = paramTag.getTechExtras(2);
    mAppData = paramTag.getByteArray("appdata");
    mProtInfo = paramTag.getByteArray("protinfo");
  }
  
  public static NfcB get(Tag paramTag)
  {
    if (!paramTag.hasTech(2)) {
      return null;
    }
    try
    {
      paramTag = new NfcB(paramTag);
      return paramTag;
    }
    catch (RemoteException paramTag) {}
    return null;
  }
  
  public byte[] getApplicationData()
  {
    return mAppData;
  }
  
  public int getMaxTransceiveLength()
  {
    return getMaxTransceiveLengthInternal();
  }
  
  public byte[] getProtocolInfo()
  {
    return mProtInfo;
  }
  
  public byte[] transceive(byte[] paramArrayOfByte)
    throws IOException
  {
    return transceive(paramArrayOfByte, true);
  }
}
