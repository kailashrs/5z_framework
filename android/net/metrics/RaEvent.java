package android.net.metrics;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class RaEvent
  implements Parcelable
{
  public static final Parcelable.Creator<RaEvent> CREATOR = new Parcelable.Creator()
  {
    public RaEvent createFromParcel(Parcel paramAnonymousParcel)
    {
      return new RaEvent(paramAnonymousParcel, null);
    }
    
    public RaEvent[] newArray(int paramAnonymousInt)
    {
      return new RaEvent[paramAnonymousInt];
    }
  };
  public static final long NO_LIFETIME = -1L;
  public final long dnsslLifetime;
  public final long prefixPreferredLifetime;
  public final long prefixValidLifetime;
  public final long rdnssLifetime;
  public final long routeInfoLifetime;
  public final long routerLifetime;
  
  public RaEvent(long paramLong1, long paramLong2, long paramLong3, long paramLong4, long paramLong5, long paramLong6)
  {
    routerLifetime = paramLong1;
    prefixValidLifetime = paramLong2;
    prefixPreferredLifetime = paramLong3;
    routeInfoLifetime = paramLong4;
    rdnssLifetime = paramLong5;
    dnsslLifetime = paramLong6;
  }
  
  private RaEvent(Parcel paramParcel)
  {
    routerLifetime = paramParcel.readLong();
    prefixValidLifetime = paramParcel.readLong();
    prefixPreferredLifetime = paramParcel.readLong();
    routeInfoLifetime = paramParcel.readLong();
    rdnssLifetime = paramParcel.readLong();
    dnsslLifetime = paramParcel.readLong();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder("RaEvent(lifetimes: ");
    localStringBuilder.append(String.format("router=%ds, ", new Object[] { Long.valueOf(routerLifetime) }));
    localStringBuilder.append(String.format("prefix_valid=%ds, ", new Object[] { Long.valueOf(prefixValidLifetime) }));
    localStringBuilder.append(String.format("prefix_preferred=%ds, ", new Object[] { Long.valueOf(prefixPreferredLifetime) }));
    localStringBuilder.append(String.format("route_info=%ds, ", new Object[] { Long.valueOf(routeInfoLifetime) }));
    localStringBuilder.append(String.format("rdnss=%ds, ", new Object[] { Long.valueOf(rdnssLifetime) }));
    localStringBuilder.append(String.format("dnssl=%ds)", new Object[] { Long.valueOf(dnsslLifetime) }));
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeLong(routerLifetime);
    paramParcel.writeLong(prefixValidLifetime);
    paramParcel.writeLong(prefixPreferredLifetime);
    paramParcel.writeLong(routeInfoLifetime);
    paramParcel.writeLong(rdnssLifetime);
    paramParcel.writeLong(dnsslLifetime);
  }
  
  public static class Builder
  {
    long dnsslLifetime = -1L;
    long prefixPreferredLifetime = -1L;
    long prefixValidLifetime = -1L;
    long rdnssLifetime = -1L;
    long routeInfoLifetime = -1L;
    long routerLifetime = -1L;
    
    public Builder() {}
    
    private long updateLifetime(long paramLong1, long paramLong2)
    {
      if (paramLong1 == -1L) {
        return paramLong2;
      }
      return Math.min(paramLong1, paramLong2);
    }
    
    public RaEvent build()
    {
      return new RaEvent(routerLifetime, prefixValidLifetime, prefixPreferredLifetime, routeInfoLifetime, rdnssLifetime, dnsslLifetime);
    }
    
    public Builder updateDnsslLifetime(long paramLong)
    {
      dnsslLifetime = updateLifetime(dnsslLifetime, paramLong);
      return this;
    }
    
    public Builder updatePrefixPreferredLifetime(long paramLong)
    {
      prefixPreferredLifetime = updateLifetime(prefixPreferredLifetime, paramLong);
      return this;
    }
    
    public Builder updatePrefixValidLifetime(long paramLong)
    {
      prefixValidLifetime = updateLifetime(prefixValidLifetime, paramLong);
      return this;
    }
    
    public Builder updateRdnssLifetime(long paramLong)
    {
      rdnssLifetime = updateLifetime(rdnssLifetime, paramLong);
      return this;
    }
    
    public Builder updateRouteInfoLifetime(long paramLong)
    {
      routeInfoLifetime = updateLifetime(routeInfoLifetime, paramLong);
      return this;
    }
    
    public Builder updateRouterLifetime(long paramLong)
    {
      routerLifetime = updateLifetime(routerLifetime, paramLong);
      return this;
    }
  }
}
