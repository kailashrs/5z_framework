package android.accounts;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class AuthenticatorDescription
  implements Parcelable
{
  public static final Parcelable.Creator<AuthenticatorDescription> CREATOR = new Parcelable.Creator()
  {
    public AuthenticatorDescription createFromParcel(Parcel paramAnonymousParcel)
    {
      return new AuthenticatorDescription(paramAnonymousParcel, null);
    }
    
    public AuthenticatorDescription[] newArray(int paramAnonymousInt)
    {
      return new AuthenticatorDescription[paramAnonymousInt];
    }
  };
  public final int accountPreferencesId;
  public final boolean customTokens;
  public final int iconId;
  public final int labelId;
  public final String packageName;
  public final int smallIconId;
  public final String type;
  
  private AuthenticatorDescription(Parcel paramParcel)
  {
    type = paramParcel.readString();
    packageName = paramParcel.readString();
    labelId = paramParcel.readInt();
    iconId = paramParcel.readInt();
    smallIconId = paramParcel.readInt();
    accountPreferencesId = paramParcel.readInt();
    int i = paramParcel.readByte();
    boolean bool = true;
    if (i != 1) {
      bool = false;
    }
    customTokens = bool;
  }
  
  private AuthenticatorDescription(String paramString)
  {
    type = paramString;
    packageName = null;
    labelId = 0;
    iconId = 0;
    smallIconId = 0;
    accountPreferencesId = 0;
    customTokens = false;
  }
  
  public AuthenticatorDescription(String paramString1, String paramString2, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    this(paramString1, paramString2, paramInt1, paramInt2, paramInt3, paramInt4, false);
  }
  
  public AuthenticatorDescription(String paramString1, String paramString2, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean)
  {
    if (paramString1 != null)
    {
      if (paramString2 != null)
      {
        type = paramString1;
        packageName = paramString2;
        labelId = paramInt1;
        iconId = paramInt2;
        smallIconId = paramInt3;
        accountPreferencesId = paramInt4;
        customTokens = paramBoolean;
        return;
      }
      throw new IllegalArgumentException("packageName cannot be null");
    }
    throw new IllegalArgumentException("type cannot be null");
  }
  
  public static AuthenticatorDescription newKey(String paramString)
  {
    if (paramString != null) {
      return new AuthenticatorDescription(paramString);
    }
    throw new IllegalArgumentException("type cannot be null");
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    if (paramObject == this) {
      return true;
    }
    if (!(paramObject instanceof AuthenticatorDescription)) {
      return false;
    }
    paramObject = (AuthenticatorDescription)paramObject;
    return type.equals(type);
  }
  
  public int hashCode()
  {
    return type.hashCode();
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("AuthenticatorDescription {type=");
    localStringBuilder.append(type);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(type);
    paramParcel.writeString(packageName);
    paramParcel.writeInt(labelId);
    paramParcel.writeInt(iconId);
    paramParcel.writeInt(smallIconId);
    paramParcel.writeInt(accountPreferencesId);
    paramParcel.writeByte((byte)customTokens);
  }
}
