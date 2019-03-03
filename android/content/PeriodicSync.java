package android.content;

import android.accounts.Account;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

public class PeriodicSync
  implements Parcelable
{
  public static final Parcelable.Creator<PeriodicSync> CREATOR = new Parcelable.Creator()
  {
    public PeriodicSync createFromParcel(Parcel paramAnonymousParcel)
    {
      return new PeriodicSync(paramAnonymousParcel, null);
    }
    
    public PeriodicSync[] newArray(int paramAnonymousInt)
    {
      return new PeriodicSync[paramAnonymousInt];
    }
  };
  public final Account account;
  public final String authority;
  public final Bundle extras;
  public final long flexTime;
  public final long period;
  
  public PeriodicSync(Account paramAccount, String paramString, Bundle paramBundle, long paramLong)
  {
    account = paramAccount;
    authority = paramString;
    if (paramBundle == null) {
      extras = new Bundle();
    } else {
      extras = new Bundle(paramBundle);
    }
    period = paramLong;
    flexTime = 0L;
  }
  
  public PeriodicSync(Account paramAccount, String paramString, Bundle paramBundle, long paramLong1, long paramLong2)
  {
    account = paramAccount;
    authority = paramString;
    extras = new Bundle(paramBundle);
    period = paramLong1;
    flexTime = paramLong2;
  }
  
  public PeriodicSync(PeriodicSync paramPeriodicSync)
  {
    account = account;
    authority = authority;
    extras = new Bundle(extras);
    period = period;
    flexTime = flexTime;
  }
  
  private PeriodicSync(Parcel paramParcel)
  {
    account = ((Account)paramParcel.readParcelable(null));
    authority = paramParcel.readString();
    extras = paramParcel.readBundle();
    period = paramParcel.readLong();
    flexTime = paramParcel.readLong();
  }
  
  public static boolean syncExtrasEquals(Bundle paramBundle1, Bundle paramBundle2)
  {
    if (paramBundle1.size() != paramBundle2.size()) {
      return false;
    }
    if (paramBundle1.isEmpty()) {
      return true;
    }
    Iterator localIterator = paramBundle1.keySet().iterator();
    while (localIterator.hasNext())
    {
      String str = (String)localIterator.next();
      if (!paramBundle2.containsKey(str)) {
        return false;
      }
      if (!Objects.equals(paramBundle1.get(str), paramBundle2.get(str))) {
        return false;
      }
    }
    return true;
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
    if (!(paramObject instanceof PeriodicSync)) {
      return false;
    }
    paramObject = (PeriodicSync)paramObject;
    if ((!account.equals(account)) || (!authority.equals(authority)) || (period != period) || (!syncExtrasEquals(extras, extras))) {
      bool = false;
    }
    return bool;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("account: ");
    localStringBuilder.append(account);
    localStringBuilder.append(", authority: ");
    localStringBuilder.append(authority);
    localStringBuilder.append(". period: ");
    localStringBuilder.append(period);
    localStringBuilder.append("s , flex: ");
    localStringBuilder.append(flexTime);
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeParcelable(account, paramInt);
    paramParcel.writeString(authority);
    paramParcel.writeBundle(extras);
    paramParcel.writeLong(period);
    paramParcel.writeLong(flexTime);
  }
}
