package android.net.lowpan;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Objects;

public class LowpanProvision
  implements Parcelable
{
  public static final Parcelable.Creator<LowpanProvision> CREATOR = new Parcelable.Creator()
  {
    public LowpanProvision createFromParcel(Parcel paramAnonymousParcel)
    {
      LowpanProvision.Builder localBuilder = new LowpanProvision.Builder();
      localBuilder.setLowpanIdentity((LowpanIdentity)LowpanIdentity.CREATOR.createFromParcel(paramAnonymousParcel));
      if (paramAnonymousParcel.readBoolean()) {
        localBuilder.setLowpanCredential((LowpanCredential)LowpanCredential.CREATOR.createFromParcel(paramAnonymousParcel));
      }
      return localBuilder.build();
    }
    
    public LowpanProvision[] newArray(int paramAnonymousInt)
    {
      return new LowpanProvision[paramAnonymousInt];
    }
  };
  private LowpanCredential mCredential = null;
  private LowpanIdentity mIdentity = new LowpanIdentity();
  
  private LowpanProvision() {}
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof LowpanProvision)) {
      return false;
    }
    paramObject = (LowpanProvision)paramObject;
    if (!mIdentity.equals(mIdentity)) {
      return false;
    }
    return Objects.equals(mCredential, mCredential);
  }
  
  public LowpanCredential getLowpanCredential()
  {
    return mCredential;
  }
  
  public LowpanIdentity getLowpanIdentity()
  {
    return mIdentity;
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { mIdentity, mCredential });
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append("LowpanProvision { identity => ");
    localStringBuffer.append(mIdentity.toString());
    if (mCredential != null)
    {
      localStringBuffer.append(", credential => ");
      localStringBuffer.append(mCredential.toString());
    }
    localStringBuffer.append("}");
    return localStringBuffer.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    mIdentity.writeToParcel(paramParcel, paramInt);
    if (mCredential == null)
    {
      paramParcel.writeBoolean(false);
    }
    else
    {
      paramParcel.writeBoolean(true);
      mCredential.writeToParcel(paramParcel, paramInt);
    }
  }
  
  public static class Builder
  {
    private final LowpanProvision provision = new LowpanProvision(null);
    
    public Builder() {}
    
    public LowpanProvision build()
    {
      return provision;
    }
    
    public Builder setLowpanCredential(LowpanCredential paramLowpanCredential)
    {
      LowpanProvision.access$202(provision, paramLowpanCredential);
      return this;
    }
    
    public Builder setLowpanIdentity(LowpanIdentity paramLowpanIdentity)
    {
      LowpanProvision.access$102(provision, paramLowpanIdentity);
      return this;
    }
  }
}
