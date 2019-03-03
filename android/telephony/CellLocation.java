package android.telephony;

import android.os.Bundle;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import com.android.internal.telephony.ITelephony;
import com.android.internal.telephony.ITelephony.Stub;

public abstract class CellLocation
{
  public CellLocation() {}
  
  public static CellLocation getEmpty()
  {
    switch (TelephonyManager.getDefault().getCurrentPhoneType())
    {
    default: 
      return null;
    case 2: 
      return new CdmaCellLocation();
    }
    return new GsmCellLocation();
  }
  
  public static CellLocation newFromBundle(Bundle paramBundle)
  {
    switch (TelephonyManager.getDefault().getCurrentPhoneType())
    {
    default: 
      return null;
    case 2: 
      return new CdmaCellLocation(paramBundle);
    }
    return new GsmCellLocation(paramBundle);
  }
  
  public static void requestLocationUpdate()
  {
    try
    {
      ITelephony localITelephony = ITelephony.Stub.asInterface(ServiceManager.getService("phone"));
      if (localITelephony != null) {
        localITelephony.updateServiceLocation();
      }
    }
    catch (RemoteException localRemoteException) {}
  }
  
  public abstract void fillInNotifierBundle(Bundle paramBundle);
  
  public abstract boolean isEmpty();
  
  public abstract void setStateInvalid();
}
