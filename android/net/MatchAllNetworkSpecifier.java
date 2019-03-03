package android.net;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class MatchAllNetworkSpecifier
  extends NetworkSpecifier
  implements Parcelable
{
  public static final Parcelable.Creator<MatchAllNetworkSpecifier> CREATOR = new Parcelable.Creator()
  {
    public MatchAllNetworkSpecifier createFromParcel(Parcel paramAnonymousParcel)
    {
      return new MatchAllNetworkSpecifier();
    }
    
    public MatchAllNetworkSpecifier[] newArray(int paramAnonymousInt)
    {
      return new MatchAllNetworkSpecifier[paramAnonymousInt];
    }
  };
  
  public MatchAllNetworkSpecifier() {}
  
  public static void checkNotMatchAllNetworkSpecifier(NetworkSpecifier paramNetworkSpecifier)
  {
    if (!(paramNetworkSpecifier instanceof MatchAllNetworkSpecifier)) {
      return;
    }
    throw new IllegalArgumentException("A MatchAllNetworkSpecifier is not permitted");
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    return paramObject instanceof MatchAllNetworkSpecifier;
  }
  
  public int hashCode()
  {
    return 0;
  }
  
  public boolean satisfiedBy(NetworkSpecifier paramNetworkSpecifier)
  {
    throw new IllegalStateException("MatchAllNetworkSpecifier must not be used in NetworkRequests");
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt) {}
}
