package android.telephony.ims;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Arrays;

@SystemApi
public final class ImsSuppServiceNotification
  implements Parcelable
{
  public static final Parcelable.Creator<ImsSuppServiceNotification> CREATOR = new Parcelable.Creator()
  {
    public ImsSuppServiceNotification createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ImsSuppServiceNotification(paramAnonymousParcel);
    }
    
    public ImsSuppServiceNotification[] newArray(int paramAnonymousInt)
    {
      return new ImsSuppServiceNotification[paramAnonymousInt];
    }
  };
  private static final String TAG = "ImsSuppServiceNotification";
  public final int code;
  public final String[] history;
  public final int index;
  public final int notificationType;
  public final String number;
  public final int type;
  
  public ImsSuppServiceNotification(int paramInt1, int paramInt2, int paramInt3, int paramInt4, String paramString, String[] paramArrayOfString)
  {
    notificationType = paramInt1;
    code = paramInt2;
    index = paramInt3;
    type = paramInt4;
    number = paramString;
    history = paramArrayOfString;
  }
  
  public ImsSuppServiceNotification(Parcel paramParcel)
  {
    notificationType = paramParcel.readInt();
    code = paramParcel.readInt();
    index = paramParcel.readInt();
    type = paramParcel.readInt();
    number = paramParcel.readString();
    history = paramParcel.createStringArray();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{ notificationType=");
    localStringBuilder.append(notificationType);
    localStringBuilder.append(", code=");
    localStringBuilder.append(code);
    localStringBuilder.append(", index=");
    localStringBuilder.append(index);
    localStringBuilder.append(", type=");
    localStringBuilder.append(type);
    localStringBuilder.append(", number=");
    localStringBuilder.append(number);
    localStringBuilder.append(", history=");
    localStringBuilder.append(Arrays.toString(history));
    localStringBuilder.append(" }");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(notificationType);
    paramParcel.writeInt(code);
    paramParcel.writeInt(index);
    paramParcel.writeInt(type);
    paramParcel.writeString(number);
    paramParcel.writeStringArray(history);
  }
}
