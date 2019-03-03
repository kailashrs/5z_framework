package com.android.internal.telephony;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public class SmsAuthorizationRequest
  implements Parcelable
{
  public static Parcelable.Creator<SmsAuthorizationRequest> CREATOR = new Parcelable.Creator()
  {
    public SmsAuthorizationRequest createFromParcel(Parcel paramAnonymousParcel)
    {
      return new SmsAuthorizationRequest(paramAnonymousParcel);
    }
    
    public SmsAuthorizationRequest[] newArray(int paramAnonymousInt)
    {
      return new SmsAuthorizationRequest[paramAnonymousInt];
    }
  };
  public final String destinationAddress;
  public final String message;
  public final String packageName;
  private final ISmsSecurityService service;
  private final IBinder token;
  
  public SmsAuthorizationRequest(Parcel paramParcel)
  {
    service = ISmsSecurityService.Stub.asInterface(paramParcel.readStrongBinder());
    token = paramParcel.readStrongBinder();
    packageName = paramParcel.readString();
    destinationAddress = paramParcel.readString();
    message = paramParcel.readString();
  }
  
  public SmsAuthorizationRequest(ISmsSecurityService paramISmsSecurityService, IBinder paramIBinder, String paramString1, String paramString2, String paramString3)
  {
    service = paramISmsSecurityService;
    token = paramIBinder;
    packageName = paramString1;
    destinationAddress = paramString2;
    message = paramString3;
  }
  
  public void accept()
    throws RemoteException
  {
    service.sendResponse(this, true);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public IBinder getToken()
  {
    return token;
  }
  
  public void reject()
    throws RemoteException
  {
    service.sendResponse(this, false);
  }
  
  public String toString()
  {
    return String.format("[%s] (%s) # %s", new Object[] { packageName, destinationAddress, message });
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeStrongBinder(service.asBinder());
    paramParcel.writeStrongBinder(token);
    paramParcel.writeString(packageName);
    paramParcel.writeString(destinationAddress);
    paramParcel.writeString(message);
  }
}
