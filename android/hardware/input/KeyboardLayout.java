package android.hardware.input;

import android.os.LocaleList;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class KeyboardLayout
  implements Parcelable, Comparable<KeyboardLayout>
{
  public static final Parcelable.Creator<KeyboardLayout> CREATOR = new Parcelable.Creator()
  {
    public KeyboardLayout createFromParcel(Parcel paramAnonymousParcel)
    {
      return new KeyboardLayout(paramAnonymousParcel, null);
    }
    
    public KeyboardLayout[] newArray(int paramAnonymousInt)
    {
      return new KeyboardLayout[paramAnonymousInt];
    }
  };
  private final String mCollection;
  private final String mDescriptor;
  private final String mLabel;
  private final LocaleList mLocales;
  private final int mPriority;
  private final int mProductId;
  private final int mVendorId;
  
  private KeyboardLayout(Parcel paramParcel)
  {
    mDescriptor = paramParcel.readString();
    mLabel = paramParcel.readString();
    mCollection = paramParcel.readString();
    mPriority = paramParcel.readInt();
    mLocales = ((LocaleList)LocaleList.CREATOR.createFromParcel(paramParcel));
    mVendorId = paramParcel.readInt();
    mProductId = paramParcel.readInt();
  }
  
  public KeyboardLayout(String paramString1, String paramString2, String paramString3, int paramInt1, LocaleList paramLocaleList, int paramInt2, int paramInt3)
  {
    mDescriptor = paramString1;
    mLabel = paramString2;
    mCollection = paramString3;
    mPriority = paramInt1;
    mLocales = paramLocaleList;
    mVendorId = paramInt2;
    mProductId = paramInt3;
  }
  
  public int compareTo(KeyboardLayout paramKeyboardLayout)
  {
    int i = Integer.compare(mPriority, mPriority);
    int j = i;
    if (i == 0) {
      j = mLabel.compareToIgnoreCase(mLabel);
    }
    i = j;
    if (j == 0) {
      i = mCollection.compareToIgnoreCase(mCollection);
    }
    return i;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String getCollection()
  {
    return mCollection;
  }
  
  public String getDescriptor()
  {
    return mDescriptor;
  }
  
  public String getLabel()
  {
    return mLabel;
  }
  
  public LocaleList getLocales()
  {
    return mLocales;
  }
  
  public int getProductId()
  {
    return mProductId;
  }
  
  public int getVendorId()
  {
    return mVendorId;
  }
  
  public String toString()
  {
    if (mCollection.isEmpty()) {
      return mLabel;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(mLabel);
    localStringBuilder.append(" - ");
    localStringBuilder.append(mCollection);
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mDescriptor);
    paramParcel.writeString(mLabel);
    paramParcel.writeString(mCollection);
    paramParcel.writeInt(mPriority);
    mLocales.writeToParcel(paramParcel, 0);
    paramParcel.writeInt(mVendorId);
    paramParcel.writeInt(mProductId);
  }
}
