package android.content;

import android.accounts.Account;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class SyncInfo
  implements Parcelable
{
  public static final Parcelable.Creator<SyncInfo> CREATOR = new Parcelable.Creator()
  {
    public SyncInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new SyncInfo(paramAnonymousParcel);
    }
    
    public SyncInfo[] newArray(int paramAnonymousInt)
    {
      return new SyncInfo[paramAnonymousInt];
    }
  };
  private static final Account REDACTED_ACCOUNT = new Account("*****", "*****");
  public final Account account;
  public final String authority;
  public final int authorityId;
  public final long startTime;
  
  public SyncInfo(int paramInt, Account paramAccount, String paramString, long paramLong)
  {
    authorityId = paramInt;
    account = paramAccount;
    authority = paramString;
    startTime = paramLong;
  }
  
  public SyncInfo(SyncInfo paramSyncInfo)
  {
    authorityId = authorityId;
    account = new Account(account.name, account.type);
    authority = authority;
    startTime = startTime;
  }
  
  SyncInfo(Parcel paramParcel)
  {
    authorityId = paramParcel.readInt();
    account = ((Account)paramParcel.readParcelable(Account.class.getClassLoader()));
    authority = paramParcel.readString();
    startTime = paramParcel.readLong();
  }
  
  public static SyncInfo createAccountRedacted(int paramInt, String paramString, long paramLong)
  {
    return new SyncInfo(paramInt, REDACTED_ACCOUNT, paramString, paramLong);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(authorityId);
    paramParcel.writeParcelable(account, paramInt);
    paramParcel.writeString(authority);
    paramParcel.writeLong(startTime);
  }
}
