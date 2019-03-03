package android.telephony.euicc;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.telephony.UiccAccessRule;
import com.android.internal.util.Preconditions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class DownloadableSubscription
  implements Parcelable
{
  public static final Parcelable.Creator<DownloadableSubscription> CREATOR = new Parcelable.Creator()
  {
    public DownloadableSubscription createFromParcel(Parcel paramAnonymousParcel)
    {
      return new DownloadableSubscription(paramAnonymousParcel, null);
    }
    
    public DownloadableSubscription[] newArray(int paramAnonymousInt)
    {
      return new DownloadableSubscription[paramAnonymousInt];
    }
  };
  private List<UiccAccessRule> accessRules;
  private String carrierName;
  private String confirmationCode;
  @Deprecated
  public final String encodedActivationCode;
  
  private DownloadableSubscription(Parcel paramParcel)
  {
    encodedActivationCode = paramParcel.readString();
    confirmationCode = paramParcel.readString();
    carrierName = paramParcel.readString();
    accessRules = new ArrayList();
    paramParcel.readTypedList(accessRules, UiccAccessRule.CREATOR);
  }
  
  private DownloadableSubscription(String paramString)
  {
    encodedActivationCode = paramString;
  }
  
  private DownloadableSubscription(String paramString1, String paramString2, String paramString3, List<UiccAccessRule> paramList)
  {
    encodedActivationCode = paramString1;
    confirmationCode = paramString2;
    carrierName = paramString3;
    accessRules = paramList;
  }
  
  public static DownloadableSubscription forActivationCode(String paramString)
  {
    Preconditions.checkNotNull(paramString, "Activation code may not be null");
    return new DownloadableSubscription(paramString);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  @SystemApi
  public List<UiccAccessRule> getAccessRules()
  {
    return accessRules;
  }
  
  @SystemApi
  public String getCarrierName()
  {
    return carrierName;
  }
  
  public String getConfirmationCode()
  {
    return confirmationCode;
  }
  
  public String getEncodedActivationCode()
  {
    return encodedActivationCode;
  }
  
  @Deprecated
  public void setAccessRules(List<UiccAccessRule> paramList)
  {
    accessRules = paramList;
  }
  
  @Deprecated
  public void setAccessRules(UiccAccessRule[] paramArrayOfUiccAccessRule)
  {
    accessRules = Arrays.asList(paramArrayOfUiccAccessRule);
  }
  
  @Deprecated
  public void setCarrierName(String paramString)
  {
    carrierName = paramString;
  }
  
  @Deprecated
  public void setConfirmationCode(String paramString)
  {
    confirmationCode = paramString;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(encodedActivationCode);
    paramParcel.writeString(confirmationCode);
    paramParcel.writeString(carrierName);
    paramParcel.writeTypedList(accessRules);
  }
  
  @SystemApi
  public static final class Builder
  {
    List<UiccAccessRule> accessRules;
    private String carrierName;
    private String confirmationCode;
    private String encodedActivationCode;
    
    public Builder() {}
    
    public Builder(DownloadableSubscription paramDownloadableSubscription)
    {
      encodedActivationCode = paramDownloadableSubscription.getEncodedActivationCode();
      confirmationCode = paramDownloadableSubscription.getConfirmationCode();
      carrierName = paramDownloadableSubscription.getCarrierName();
      accessRules = paramDownloadableSubscription.getAccessRules();
    }
    
    public DownloadableSubscription build()
    {
      return new DownloadableSubscription(encodedActivationCode, confirmationCode, carrierName, accessRules, null);
    }
    
    public Builder setAccessRules(List<UiccAccessRule> paramList)
    {
      accessRules = paramList;
      return this;
    }
    
    public Builder setCarrierName(String paramString)
    {
      carrierName = paramString;
      return this;
    }
    
    public Builder setConfirmationCode(String paramString)
    {
      confirmationCode = paramString;
      return this;
    }
    
    public Builder setEncodedActivationCode(String paramString)
    {
      encodedActivationCode = paramString;
      return this;
    }
  }
}
