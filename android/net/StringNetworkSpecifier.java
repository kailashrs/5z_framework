package android.net;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import com.android.internal.util.Preconditions;
import java.util.Objects;

public final class StringNetworkSpecifier
  extends NetworkSpecifier
  implements Parcelable
{
  public static final Parcelable.Creator<StringNetworkSpecifier> CREATOR = new Parcelable.Creator()
  {
    public StringNetworkSpecifier createFromParcel(Parcel paramAnonymousParcel)
    {
      return new StringNetworkSpecifier(paramAnonymousParcel.readString());
    }
    
    public StringNetworkSpecifier[] newArray(int paramAnonymousInt)
    {
      return new StringNetworkSpecifier[paramAnonymousInt];
    }
  };
  public final String specifier;
  
  public StringNetworkSpecifier(String paramString)
  {
    Preconditions.checkStringNotEmpty(paramString);
    specifier = paramString;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof StringNetworkSpecifier)) {
      return false;
    }
    return TextUtils.equals(specifier, specifier);
  }
  
  public int hashCode()
  {
    return Objects.hashCode(specifier);
  }
  
  public boolean satisfiedBy(NetworkSpecifier paramNetworkSpecifier)
  {
    return equals(paramNetworkSpecifier);
  }
  
  public String toString()
  {
    return specifier;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(specifier);
  }
}
