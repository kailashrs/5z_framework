package android.telephony.ims.feature;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.ArraySet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@SystemApi
public final class CapabilityChangeRequest
  implements Parcelable
{
  public static final Parcelable.Creator<CapabilityChangeRequest> CREATOR = new Parcelable.Creator()
  {
    public CapabilityChangeRequest createFromParcel(Parcel paramAnonymousParcel)
    {
      return new CapabilityChangeRequest(paramAnonymousParcel);
    }
    
    public CapabilityChangeRequest[] newArray(int paramAnonymousInt)
    {
      return new CapabilityChangeRequest[paramAnonymousInt];
    }
  };
  private final Set<CapabilityPair> mCapabilitiesToDisable;
  private final Set<CapabilityPair> mCapabilitiesToEnable;
  
  public CapabilityChangeRequest()
  {
    mCapabilitiesToEnable = new ArraySet();
    mCapabilitiesToDisable = new ArraySet();
  }
  
  protected CapabilityChangeRequest(Parcel paramParcel)
  {
    int i = paramParcel.readInt();
    mCapabilitiesToEnable = new ArraySet(i);
    int j = 0;
    for (int k = 0; k < i; k++) {
      mCapabilitiesToEnable.add(new CapabilityPair(paramParcel.readInt(), paramParcel.readInt()));
    }
    i = paramParcel.readInt();
    mCapabilitiesToDisable = new ArraySet(i);
    for (k = j; k < i; k++) {
      mCapabilitiesToDisable.add(new CapabilityPair(paramParcel.readInt(), paramParcel.readInt()));
    }
  }
  
  private void addAllCapabilities(Set<CapabilityPair> paramSet, int paramInt1, int paramInt2)
  {
    long l = Long.highestOneBit(paramInt1);
    int i = 1;
    while (i <= l)
    {
      if ((i & paramInt1) > 0) {
        paramSet.add(new CapabilityPair(i, paramInt2));
      }
      i *= 2;
    }
  }
  
  public void addCapabilitiesToDisableForTech(int paramInt1, int paramInt2)
  {
    addAllCapabilities(mCapabilitiesToDisable, paramInt1, paramInt2);
  }
  
  public void addCapabilitiesToEnableForTech(int paramInt1, int paramInt2)
  {
    addAllCapabilities(mCapabilitiesToEnable, paramInt1, paramInt2);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {
      return true;
    }
    if (!(paramObject instanceof CapabilityChangeRequest)) {
      return false;
    }
    paramObject = (CapabilityChangeRequest)paramObject;
    if (!mCapabilitiesToEnable.equals(mCapabilitiesToEnable)) {
      return false;
    }
    return mCapabilitiesToDisable.equals(mCapabilitiesToDisable);
  }
  
  public List<CapabilityPair> getCapabilitiesToDisable()
  {
    return new ArrayList(mCapabilitiesToDisable);
  }
  
  public List<CapabilityPair> getCapabilitiesToEnable()
  {
    return new ArrayList(mCapabilitiesToEnable);
  }
  
  public int hashCode()
  {
    return 31 * mCapabilitiesToEnable.hashCode() + mCapabilitiesToDisable.hashCode();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mCapabilitiesToEnable.size());
    Object localObject1 = mCapabilitiesToEnable.iterator();
    while (((Iterator)localObject1).hasNext())
    {
      localObject2 = (CapabilityPair)((Iterator)localObject1).next();
      paramParcel.writeInt(((CapabilityPair)localObject2).getCapability());
      paramParcel.writeInt(((CapabilityPair)localObject2).getRadioTech());
    }
    paramParcel.writeInt(mCapabilitiesToDisable.size());
    Object localObject2 = mCapabilitiesToDisable.iterator();
    while (((Iterator)localObject2).hasNext())
    {
      localObject1 = (CapabilityPair)((Iterator)localObject2).next();
      paramParcel.writeInt(((CapabilityPair)localObject1).getCapability());
      paramParcel.writeInt(((CapabilityPair)localObject1).getRadioTech());
    }
  }
  
  public static class CapabilityPair
  {
    private final int mCapability;
    private final int radioTech;
    
    public CapabilityPair(int paramInt1, int paramInt2)
    {
      mCapability = paramInt1;
      radioTech = paramInt2;
    }
    
    public boolean equals(Object paramObject)
    {
      boolean bool = true;
      if (this == paramObject) {
        return true;
      }
      if (!(paramObject instanceof CapabilityPair)) {
        return false;
      }
      paramObject = (CapabilityPair)paramObject;
      if (getCapability() != paramObject.getCapability()) {
        return false;
      }
      if (getRadioTech() != paramObject.getRadioTech()) {
        bool = false;
      }
      return bool;
    }
    
    public int getCapability()
    {
      return mCapability;
    }
    
    public int getRadioTech()
    {
      return radioTech;
    }
    
    public int hashCode()
    {
      return 31 * getCapability() + getRadioTech();
    }
  }
}
