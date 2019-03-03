package android.telecom;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import java.util.Objects;

public final class DisconnectCause
  implements Parcelable
{
  public static final int ANSWERED_ELSEWHERE = 11;
  public static final int BUSY = 7;
  public static final int CALL_PULLED = 12;
  public static final int CANCELED = 4;
  public static final int CONNECTION_MANAGER_NOT_SUPPORTED = 10;
  public static final Parcelable.Creator<DisconnectCause> CREATOR = new Parcelable.Creator()
  {
    public DisconnectCause createFromParcel(Parcel paramAnonymousParcel)
    {
      return new DisconnectCause(paramAnonymousParcel.readInt(), (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramAnonymousParcel), (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramAnonymousParcel), paramAnonymousParcel.readString(), paramAnonymousParcel.readInt());
    }
    
    public DisconnectCause[] newArray(int paramAnonymousInt)
    {
      return new DisconnectCause[paramAnonymousInt];
    }
  };
  public static final int ERROR = 1;
  public static final int LOCAL = 2;
  public static final int MISSED = 5;
  public static final int OTHER = 9;
  public static final String REASON_IMS_ACCESS_BLOCKED = "REASON_IMS_ACCESS_BLOCKED";
  public static final String REASON_WIFI_ON_BUT_WFC_OFF = "REASON_WIFI_ON_BUT_WFC_OFF";
  public static final int REJECTED = 6;
  public static final int REMOTE = 3;
  public static final int RESTRICTED = 8;
  public static final int UNKNOWN = 0;
  private int mDisconnectCode;
  private CharSequence mDisconnectDescription;
  private CharSequence mDisconnectLabel;
  private String mDisconnectReason;
  private int mToneToPlay;
  
  public DisconnectCause(int paramInt)
  {
    this(paramInt, null, null, null, -1);
  }
  
  public DisconnectCause(int paramInt, CharSequence paramCharSequence1, CharSequence paramCharSequence2, String paramString)
  {
    this(paramInt, paramCharSequence1, paramCharSequence2, paramString, -1);
  }
  
  public DisconnectCause(int paramInt1, CharSequence paramCharSequence1, CharSequence paramCharSequence2, String paramString, int paramInt2)
  {
    mDisconnectCode = paramInt1;
    mDisconnectLabel = paramCharSequence1;
    mDisconnectDescription = paramCharSequence2;
    mDisconnectReason = paramString;
    mToneToPlay = paramInt2;
  }
  
  public DisconnectCause(int paramInt, String paramString)
  {
    this(paramInt, null, null, paramString, -1);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool1 = paramObject instanceof DisconnectCause;
    boolean bool2 = false;
    if (bool1)
    {
      paramObject = (DisconnectCause)paramObject;
      bool1 = bool2;
      if (Objects.equals(Integer.valueOf(mDisconnectCode), Integer.valueOf(paramObject.getCode())))
      {
        bool1 = bool2;
        if (Objects.equals(mDisconnectLabel, paramObject.getLabel()))
        {
          bool1 = bool2;
          if (Objects.equals(mDisconnectDescription, paramObject.getDescription()))
          {
            bool1 = bool2;
            if (Objects.equals(mDisconnectReason, paramObject.getReason()))
            {
              bool1 = bool2;
              if (Objects.equals(Integer.valueOf(mToneToPlay), Integer.valueOf(paramObject.getTone()))) {
                bool1 = true;
              }
            }
          }
        }
      }
      return bool1;
    }
    return false;
  }
  
  public int getCode()
  {
    return mDisconnectCode;
  }
  
  public CharSequence getDescription()
  {
    return mDisconnectDescription;
  }
  
  public CharSequence getLabel()
  {
    return mDisconnectLabel;
  }
  
  public String getReason()
  {
    return mDisconnectReason;
  }
  
  public int getTone()
  {
    return mToneToPlay;
  }
  
  public int hashCode()
  {
    return Objects.hashCode(Integer.valueOf(mDisconnectCode)) + Objects.hashCode(mDisconnectLabel) + Objects.hashCode(mDisconnectDescription) + Objects.hashCode(mDisconnectReason) + Objects.hashCode(Integer.valueOf(mToneToPlay));
  }
  
  public String toString()
  {
    Object localObject;
    switch (mDisconnectCode)
    {
    default: 
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("invalid code: ");
      ((StringBuilder)localObject).append(mDisconnectCode);
      localObject = ((StringBuilder)localObject).toString();
      break;
    case 12: 
      localObject = "CALL_PULLED";
      break;
    case 11: 
      localObject = "ANSWERED_ELSEWHERE";
      break;
    case 10: 
      localObject = "CONNECTION_MANAGER_NOT_SUPPORTED";
      break;
    case 9: 
      localObject = "OTHER";
      break;
    case 8: 
      localObject = "RESTRICTED";
      break;
    case 7: 
      localObject = "BUSY";
      break;
    case 6: 
      localObject = "REJECTED";
      break;
    case 5: 
      localObject = "MISSED";
      break;
    case 4: 
      localObject = "CANCELED";
      break;
    case 3: 
      localObject = "REMOTE";
      break;
    case 2: 
      localObject = "LOCAL";
      break;
    case 1: 
      localObject = "ERROR";
      break;
    case 0: 
      localObject = "UNKNOWN";
    }
    String str1;
    if (mDisconnectLabel == null) {
      str1 = "";
    } else {
      str1 = mDisconnectLabel.toString();
    }
    String str2;
    if (mDisconnectDescription == null) {
      str2 = "";
    } else {
      str2 = mDisconnectDescription.toString();
    }
    String str3;
    if (mDisconnectReason == null) {
      str3 = "";
    } else {
      str3 = mDisconnectReason;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("DisconnectCause [ Code: (");
    localStringBuilder.append((String)localObject);
    localStringBuilder.append(") Label: (");
    localStringBuilder.append(str1);
    localStringBuilder.append(") Description: (");
    localStringBuilder.append(str2);
    localStringBuilder.append(") Reason: (");
    localStringBuilder.append(str3);
    localStringBuilder.append(") Tone: (");
    localStringBuilder.append(mToneToPlay);
    localStringBuilder.append(") ]");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mDisconnectCode);
    TextUtils.writeToParcel(mDisconnectLabel, paramParcel, paramInt);
    TextUtils.writeToParcel(mDisconnectDescription, paramParcel, paramInt);
    paramParcel.writeString(mDisconnectReason);
    paramParcel.writeInt(mToneToPlay);
  }
}
