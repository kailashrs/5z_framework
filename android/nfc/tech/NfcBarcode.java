package android.nfc.tech;

import android.nfc.Tag;
import android.os.Bundle;
import android.os.RemoteException;

public final class NfcBarcode
  extends BasicTagTechnology
{
  public static final String EXTRA_BARCODE_TYPE = "barcodetype";
  public static final int TYPE_KOVIO = 1;
  public static final int TYPE_UNKNOWN = -1;
  private int mType;
  
  public NfcBarcode(Tag paramTag)
    throws RemoteException
  {
    super(paramTag, 10);
    paramTag = paramTag.getTechExtras(10);
    if (paramTag != null)
    {
      mType = paramTag.getInt("barcodetype");
      return;
    }
    throw new NullPointerException("NfcBarcode tech extras are null.");
  }
  
  public static NfcBarcode get(Tag paramTag)
  {
    if (!paramTag.hasTech(10)) {
      return null;
    }
    try
    {
      paramTag = new NfcBarcode(paramTag);
      return paramTag;
    }
    catch (RemoteException paramTag) {}
    return null;
  }
  
  public byte[] getBarcode()
  {
    if (mType != 1) {
      return null;
    }
    return mTag.getId();
  }
  
  public int getType()
  {
    return mType;
  }
}
