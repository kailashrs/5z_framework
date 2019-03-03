package android.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Collections;
import java.util.List;

public final class VisualVoicemailSmsFilterSettings
  implements Parcelable
{
  public static final Parcelable.Creator<VisualVoicemailSmsFilterSettings> CREATOR = new Parcelable.Creator()
  {
    public VisualVoicemailSmsFilterSettings createFromParcel(Parcel paramAnonymousParcel)
    {
      VisualVoicemailSmsFilterSettings.Builder localBuilder = new VisualVoicemailSmsFilterSettings.Builder();
      localBuilder.setClientPrefix(paramAnonymousParcel.readString());
      localBuilder.setOriginatingNumbers(paramAnonymousParcel.createStringArrayList());
      localBuilder.setDestinationPort(paramAnonymousParcel.readInt());
      localBuilder.setPackageName(paramAnonymousParcel.readString());
      return localBuilder.build();
    }
    
    public VisualVoicemailSmsFilterSettings[] newArray(int paramAnonymousInt)
    {
      return new VisualVoicemailSmsFilterSettings[paramAnonymousInt];
    }
  };
  public static final String DEFAULT_CLIENT_PREFIX = "//VVM";
  public static final int DEFAULT_DESTINATION_PORT = -1;
  public static final List<String> DEFAULT_ORIGINATING_NUMBERS = ;
  public static final int DESTINATION_PORT_ANY = -1;
  public static final int DESTINATION_PORT_DATA_SMS = -2;
  public final String clientPrefix;
  public final int destinationPort;
  public final List<String> originatingNumbers;
  public final String packageName;
  
  private VisualVoicemailSmsFilterSettings(Builder paramBuilder)
  {
    clientPrefix = mClientPrefix;
    originatingNumbers = mOriginatingNumbers;
    destinationPort = mDestinationPort;
    packageName = mPackageName;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[VisualVoicemailSmsFilterSettings clientPrefix=");
    localStringBuilder.append(clientPrefix);
    localStringBuilder.append(", originatingNumbers=");
    localStringBuilder.append(originatingNumbers);
    localStringBuilder.append(", destinationPort=");
    localStringBuilder.append(destinationPort);
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(clientPrefix);
    paramParcel.writeStringList(originatingNumbers);
    paramParcel.writeInt(destinationPort);
    paramParcel.writeString(packageName);
  }
  
  public static class Builder
  {
    private String mClientPrefix = "//VVM";
    private int mDestinationPort = -1;
    private List<String> mOriginatingNumbers = VisualVoicemailSmsFilterSettings.DEFAULT_ORIGINATING_NUMBERS;
    private String mPackageName;
    
    public Builder() {}
    
    public VisualVoicemailSmsFilterSettings build()
    {
      return new VisualVoicemailSmsFilterSettings(this, null);
    }
    
    public Builder setClientPrefix(String paramString)
    {
      if (paramString != null)
      {
        mClientPrefix = paramString;
        return this;
      }
      throw new IllegalArgumentException("Client prefix cannot be null");
    }
    
    public Builder setDestinationPort(int paramInt)
    {
      mDestinationPort = paramInt;
      return this;
    }
    
    public Builder setOriginatingNumbers(List<String> paramList)
    {
      if (paramList != null)
      {
        mOriginatingNumbers = paramList;
        return this;
      }
      throw new IllegalArgumentException("Originating numbers cannot be null");
    }
    
    public Builder setPackageName(String paramString)
    {
      mPackageName = paramString;
      return this;
    }
  }
}
