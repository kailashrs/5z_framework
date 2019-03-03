package android.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Objects;

public class VoiceSpecificRegistrationStates
  implements Parcelable
{
  public static final Parcelable.Creator<VoiceSpecificRegistrationStates> CREATOR = new Parcelable.Creator()
  {
    public VoiceSpecificRegistrationStates createFromParcel(Parcel paramAnonymousParcel)
    {
      return new VoiceSpecificRegistrationStates(paramAnonymousParcel, null);
    }
    
    public VoiceSpecificRegistrationStates[] newArray(int paramAnonymousInt)
    {
      return new VoiceSpecificRegistrationStates[paramAnonymousInt];
    }
  };
  public final boolean cssSupported;
  public final int defaultRoamingIndicator;
  public final int roamingIndicator;
  public final int systemIsInPrl;
  
  private VoiceSpecificRegistrationStates(Parcel paramParcel)
  {
    cssSupported = paramParcel.readBoolean();
    roamingIndicator = paramParcel.readInt();
    systemIsInPrl = paramParcel.readInt();
    defaultRoamingIndicator = paramParcel.readInt();
  }
  
  VoiceSpecificRegistrationStates(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3)
  {
    cssSupported = paramBoolean;
    roamingIndicator = paramInt1;
    systemIsInPrl = paramInt2;
    defaultRoamingIndicator = paramInt3;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if ((paramObject != null) && ((paramObject instanceof VoiceSpecificRegistrationStates)))
    {
      paramObject = (VoiceSpecificRegistrationStates)paramObject;
      if ((cssSupported != cssSupported) || (roamingIndicator != roamingIndicator) || (systemIsInPrl != systemIsInPrl) || (defaultRoamingIndicator != defaultRoamingIndicator)) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { Boolean.valueOf(cssSupported), Integer.valueOf(roamingIndicator), Integer.valueOf(systemIsInPrl), Integer.valueOf(defaultRoamingIndicator) });
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("VoiceSpecificRegistrationStates { mCssSupported=");
    localStringBuilder.append(cssSupported);
    localStringBuilder.append(" mRoamingIndicator=");
    localStringBuilder.append(roamingIndicator);
    localStringBuilder.append(" mSystemIsInPrl=");
    localStringBuilder.append(systemIsInPrl);
    localStringBuilder.append(" mDefaultRoamingIndicator=");
    localStringBuilder.append(defaultRoamingIndicator);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeBoolean(cssSupported);
    paramParcel.writeInt(roamingIndicator);
    paramParcel.writeInt(systemIsInPrl);
    paramParcel.writeInt(defaultRoamingIndicator);
  }
}
