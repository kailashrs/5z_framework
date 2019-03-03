package android.telephony;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.telecom.PhoneAccountHandle;

public final class VisualVoicemailSms
  implements Parcelable
{
  public static final Parcelable.Creator<VisualVoicemailSms> CREATOR = new Parcelable.Creator()
  {
    public VisualVoicemailSms createFromParcel(Parcel paramAnonymousParcel)
    {
      return new VisualVoicemailSms.Builder().setPhoneAccountHandle((PhoneAccountHandle)paramAnonymousParcel.readParcelable(null)).setPrefix(paramAnonymousParcel.readString()).setFields(paramAnonymousParcel.readBundle()).setMessageBody(paramAnonymousParcel.readString()).build();
    }
    
    public VisualVoicemailSms[] newArray(int paramAnonymousInt)
    {
      return new VisualVoicemailSms[paramAnonymousInt];
    }
  };
  private final Bundle mFields;
  private final String mMessageBody;
  private final PhoneAccountHandle mPhoneAccountHandle;
  private final String mPrefix;
  
  VisualVoicemailSms(Builder paramBuilder)
  {
    mPhoneAccountHandle = mPhoneAccountHandle;
    mPrefix = mPrefix;
    mFields = mFields;
    mMessageBody = mMessageBody;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public Bundle getFields()
  {
    return mFields;
  }
  
  public String getMessageBody()
  {
    return mMessageBody;
  }
  
  public PhoneAccountHandle getPhoneAccountHandle()
  {
    return mPhoneAccountHandle;
  }
  
  public String getPrefix()
  {
    return mPrefix;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeParcelable(getPhoneAccountHandle(), paramInt);
    paramParcel.writeString(getPrefix());
    paramParcel.writeBundle(getFields());
    paramParcel.writeString(getMessageBody());
  }
  
  public static class Builder
  {
    private Bundle mFields;
    private String mMessageBody;
    private PhoneAccountHandle mPhoneAccountHandle;
    private String mPrefix;
    
    public Builder() {}
    
    public VisualVoicemailSms build()
    {
      return new VisualVoicemailSms(this);
    }
    
    public Builder setFields(Bundle paramBundle)
    {
      mFields = paramBundle;
      return this;
    }
    
    public Builder setMessageBody(String paramString)
    {
      mMessageBody = paramString;
      return this;
    }
    
    public Builder setPhoneAccountHandle(PhoneAccountHandle paramPhoneAccountHandle)
    {
      mPhoneAccountHandle = paramPhoneAccountHandle;
      return this;
    }
    
    public Builder setPrefix(String paramString)
    {
      mPrefix = paramString;
      return this;
    }
  }
}
