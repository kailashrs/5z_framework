package android.nfc.tech;

import android.nfc.Tag;
import android.os.Bundle;
import android.os.RemoteException;
import java.io.IOException;

public final class NfcV
  extends BasicTagTechnology
{
  public static final String EXTRA_DSFID = "dsfid";
  public static final String EXTRA_RESP_FLAGS = "respflags";
  private byte mDsfId;
  private byte mRespFlags;
  
  public NfcV(Tag paramTag)
    throws RemoteException
  {
    super(paramTag, 5);
    paramTag = paramTag.getTechExtras(5);
    mRespFlags = paramTag.getByte("respflags");
    mDsfId = paramTag.getByte("dsfid");
  }
  
  public static NfcV get(Tag paramTag)
  {
    if (!paramTag.hasTech(5)) {
      return null;
    }
    try
    {
      paramTag = new NfcV(paramTag);
      return paramTag;
    }
    catch (RemoteException paramTag) {}
    return null;
  }
  
  public byte getDsfId()
  {
    return mDsfId;
  }
  
  public int getMaxTransceiveLength()
  {
    return getMaxTransceiveLengthInternal();
  }
  
  public byte getResponseFlags()
  {
    return mRespFlags;
  }
  
  public byte[] transceive(byte[] paramArrayOfByte)
    throws IOException
  {
    return transceive(paramArrayOfByte, true);
  }
}
