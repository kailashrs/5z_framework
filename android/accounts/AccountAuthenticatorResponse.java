package android.accounts;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.util.Log;

public class AccountAuthenticatorResponse
  implements Parcelable
{
  public static final Parcelable.Creator<AccountAuthenticatorResponse> CREATOR = new Parcelable.Creator()
  {
    public AccountAuthenticatorResponse createFromParcel(Parcel paramAnonymousParcel)
    {
      return new AccountAuthenticatorResponse(paramAnonymousParcel);
    }
    
    public AccountAuthenticatorResponse[] newArray(int paramAnonymousInt)
    {
      return new AccountAuthenticatorResponse[paramAnonymousInt];
    }
  };
  private static final String TAG = "AccountAuthenticator";
  private IAccountAuthenticatorResponse mAccountAuthenticatorResponse;
  
  public AccountAuthenticatorResponse(IAccountAuthenticatorResponse paramIAccountAuthenticatorResponse)
  {
    mAccountAuthenticatorResponse = paramIAccountAuthenticatorResponse;
  }
  
  public AccountAuthenticatorResponse(Parcel paramParcel)
  {
    mAccountAuthenticatorResponse = IAccountAuthenticatorResponse.Stub.asInterface(paramParcel.readStrongBinder());
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void onError(int paramInt, String paramString)
  {
    if (Log.isLoggable("AccountAuthenticator", 2))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("AccountAuthenticatorResponse.onError: ");
      localStringBuilder.append(paramInt);
      localStringBuilder.append(", ");
      localStringBuilder.append(paramString);
      Log.v("AccountAuthenticator", localStringBuilder.toString());
    }
    try
    {
      mAccountAuthenticatorResponse.onError(paramInt, paramString);
    }
    catch (RemoteException paramString) {}
  }
  
  public void onRequestContinued()
  {
    if (Log.isLoggable("AccountAuthenticator", 2)) {
      Log.v("AccountAuthenticator", "AccountAuthenticatorResponse.onRequestContinued");
    }
    try
    {
      mAccountAuthenticatorResponse.onRequestContinued();
    }
    catch (RemoteException localRemoteException) {}
  }
  
  public void onResult(Bundle paramBundle)
  {
    if (Log.isLoggable("AccountAuthenticator", 2))
    {
      paramBundle.keySet();
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("AccountAuthenticatorResponse.onResult: ");
      localStringBuilder.append(AccountManager.sanitizeResult(paramBundle));
      Log.v("AccountAuthenticator", localStringBuilder.toString());
    }
    try
    {
      mAccountAuthenticatorResponse.onResult(paramBundle);
    }
    catch (RemoteException paramBundle) {}
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeStrongBinder(mAccountAuthenticatorResponse.asBinder());
  }
}
