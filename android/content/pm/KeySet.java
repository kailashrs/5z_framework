package android.content.pm;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class KeySet
  implements Parcelable
{
  public static final Parcelable.Creator<KeySet> CREATOR = new Parcelable.Creator()
  {
    public KeySet createFromParcel(Parcel paramAnonymousParcel)
    {
      return KeySet.readFromParcel(paramAnonymousParcel);
    }
    
    public KeySet[] newArray(int paramAnonymousInt)
    {
      return new KeySet[paramAnonymousInt];
    }
  };
  private IBinder token;
  
  public KeySet(IBinder paramIBinder)
  {
    if (paramIBinder != null)
    {
      token = paramIBinder;
      return;
    }
    throw new NullPointerException("null value for KeySet IBinder token");
  }
  
  private static KeySet readFromParcel(Parcel paramParcel)
  {
    return new KeySet(paramParcel.readStrongBinder());
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool1 = paramObject instanceof KeySet;
    boolean bool2 = false;
    if (bool1)
    {
      paramObject = (KeySet)paramObject;
      if (token == token) {
        bool2 = true;
      }
      return bool2;
    }
    return false;
  }
  
  public IBinder getToken()
  {
    return token;
  }
  
  public int hashCode()
  {
    return token.hashCode();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeStrongBinder(token);
  }
}
