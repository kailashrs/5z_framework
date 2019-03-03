package android.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;

public final class UssdResponse
  implements Parcelable
{
  public static final Parcelable.Creator<UssdResponse> CREATOR = new Parcelable.Creator()
  {
    public UssdResponse createFromParcel(Parcel paramAnonymousParcel)
    {
      return new UssdResponse(paramAnonymousParcel.readString(), (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramAnonymousParcel));
    }
    
    public UssdResponse[] newArray(int paramAnonymousInt)
    {
      return new UssdResponse[paramAnonymousInt];
    }
  };
  private CharSequence mReturnMessage;
  private String mUssdRequest;
  
  public UssdResponse(String paramString, CharSequence paramCharSequence)
  {
    mUssdRequest = paramString;
    mReturnMessage = paramCharSequence;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public CharSequence getReturnMessage()
  {
    return mReturnMessage;
  }
  
  public String getUssdRequest()
  {
    return mUssdRequest;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mUssdRequest);
    TextUtils.writeToParcel(mReturnMessage, paramParcel, 0);
  }
}
