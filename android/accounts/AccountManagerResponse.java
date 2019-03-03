package android.accounts;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public class AccountManagerResponse
  implements Parcelable
{
  public static final Parcelable.Creator<AccountManagerResponse> CREATOR = new Parcelable.Creator()
  {
    public AccountManagerResponse createFromParcel(Parcel paramAnonymousParcel)
    {
      return new AccountManagerResponse(paramAnonymousParcel);
    }
    
    public AccountManagerResponse[] newArray(int paramAnonymousInt)
    {
      return new AccountManagerResponse[paramAnonymousInt];
    }
  };
  private IAccountManagerResponse mResponse;
  
  public AccountManagerResponse(IAccountManagerResponse paramIAccountManagerResponse)
  {
    mResponse = paramIAccountManagerResponse;
  }
  
  public AccountManagerResponse(Parcel paramParcel)
  {
    mResponse = IAccountManagerResponse.Stub.asInterface(paramParcel.readStrongBinder());
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void onError(int paramInt, String paramString)
  {
    try
    {
      mResponse.onError(paramInt, paramString);
    }
    catch (RemoteException paramString) {}
  }
  
  public void onResult(Bundle paramBundle)
  {
    try
    {
      mResponse.onResult(paramBundle);
    }
    catch (RemoteException paramBundle) {}
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeStrongBinder(mResponse.asBinder());
  }
}
