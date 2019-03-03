package android.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Arrays;

public final class RadioAccessSpecifier
  implements Parcelable
{
  public static final Parcelable.Creator<RadioAccessSpecifier> CREATOR = new Parcelable.Creator()
  {
    public RadioAccessSpecifier createFromParcel(Parcel paramAnonymousParcel)
    {
      return new RadioAccessSpecifier(paramAnonymousParcel, null);
    }
    
    public RadioAccessSpecifier[] newArray(int paramAnonymousInt)
    {
      return new RadioAccessSpecifier[paramAnonymousInt];
    }
  };
  private int[] mBands;
  private int[] mChannels;
  private int mRadioAccessNetwork;
  
  public RadioAccessSpecifier(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2)
  {
    mRadioAccessNetwork = paramInt;
    if (paramArrayOfInt1 != null) {
      mBands = ((int[])paramArrayOfInt1.clone());
    } else {
      mBands = null;
    }
    if (paramArrayOfInt2 != null) {
      mChannels = ((int[])paramArrayOfInt2.clone());
    } else {
      mChannels = null;
    }
  }
  
  private RadioAccessSpecifier(Parcel paramParcel)
  {
    mRadioAccessNetwork = paramParcel.readInt();
    mBands = paramParcel.createIntArray();
    mChannels = paramParcel.createIntArray();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool1 = false;
    try
    {
      RadioAccessSpecifier localRadioAccessSpecifier = (RadioAccessSpecifier)paramObject;
      if (paramObject == null) {
        return false;
      }
      boolean bool2 = bool1;
      if (mRadioAccessNetwork == mRadioAccessNetwork)
      {
        bool2 = bool1;
        if (Arrays.equals(mBands, mBands))
        {
          bool2 = bool1;
          if (Arrays.equals(mChannels, mChannels)) {
            bool2 = true;
          }
        }
      }
      return bool2;
    }
    catch (ClassCastException paramObject) {}
    return false;
  }
  
  public int[] getBands()
  {
    int[] arrayOfInt;
    if (mBands == null) {
      arrayOfInt = null;
    } else {
      arrayOfInt = (int[])mBands.clone();
    }
    return arrayOfInt;
  }
  
  public int[] getChannels()
  {
    int[] arrayOfInt;
    if (mChannels == null) {
      arrayOfInt = null;
    } else {
      arrayOfInt = (int[])mChannels.clone();
    }
    return arrayOfInt;
  }
  
  public int getRadioAccessNetwork()
  {
    return mRadioAccessNetwork;
  }
  
  public int hashCode()
  {
    return mRadioAccessNetwork * 31 + Arrays.hashCode(mBands) * 37 + Arrays.hashCode(mChannels) * 39;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mRadioAccessNetwork);
    paramParcel.writeIntArray(mBands);
    paramParcel.writeIntArray(mChannels);
  }
}
