package android.accounts;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.Log;
import com.android.internal.annotations.GuardedBy;
import java.util.Set;

public class Account
  implements Parcelable
{
  public static final Parcelable.Creator<Account> CREATOR = new Parcelable.Creator()
  {
    public Account createFromParcel(Parcel paramAnonymousParcel)
    {
      return new Account(paramAnonymousParcel);
    }
    
    public Account[] newArray(int paramAnonymousInt)
    {
      return new Account[paramAnonymousInt];
    }
  };
  private static final String TAG = "Account";
  @GuardedBy("sAccessedAccounts")
  private static final Set<Account> sAccessedAccounts = new ArraySet();
  private final String accessId;
  public final String name;
  public final String type;
  
  public Account(Account paramAccount, String paramString)
  {
    this(name, type, paramString);
  }
  
  public Account(Parcel arg1)
  {
    name = ???.readString();
    type = ???.readString();
    accessId = ???.readString();
    if (accessId != null) {
      synchronized (sAccessedAccounts)
      {
        boolean bool = sAccessedAccounts.add(this);
        if (bool) {
          try
          {
            IAccountManager.Stub.asInterface(ServiceManager.getService("account")).onAccountAccessed(accessId);
          }
          catch (RemoteException localRemoteException)
          {
            Log.e("Account", "Error noting account access", localRemoteException);
          }
        }
      }
    }
  }
  
  public Account(String paramString1, String paramString2)
  {
    this(paramString1, paramString2, null);
  }
  
  public Account(String paramString1, String paramString2, String paramString3)
  {
    if (!TextUtils.isEmpty(paramString1))
    {
      if (!TextUtils.isEmpty(paramString2))
      {
        name = paramString1;
        type = paramString2;
        accessId = paramString3;
        return;
      }
      paramString1 = new StringBuilder();
      paramString1.append("the type must not be empty: ");
      paramString1.append(paramString2);
      throw new IllegalArgumentException(paramString1.toString());
    }
    paramString2 = new StringBuilder();
    paramString2.append("the name must not be empty: ");
    paramString2.append(paramString1);
    throw new IllegalArgumentException(paramString2.toString());
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (paramObject == this) {
      return true;
    }
    if (!(paramObject instanceof Account)) {
      return false;
    }
    paramObject = (Account)paramObject;
    if ((!name.equals(name)) || (!type.equals(type))) {
      bool = false;
    }
    return bool;
  }
  
  public String getAccessId()
  {
    return accessId;
  }
  
  public int hashCode()
  {
    return 31 * (31 * 17 + name.hashCode()) + type.hashCode();
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Account {name=");
    localStringBuilder.append(name);
    localStringBuilder.append(", type=");
    localStringBuilder.append(type);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(name);
    paramParcel.writeString(type);
    paramParcel.writeString(accessId);
  }
}
