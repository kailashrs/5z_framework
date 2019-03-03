package android.security.keymaster;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class KeymasterCertificateChain
  implements Parcelable
{
  public static final Parcelable.Creator<KeymasterCertificateChain> CREATOR = new Parcelable.Creator()
  {
    public KeymasterCertificateChain createFromParcel(Parcel paramAnonymousParcel)
    {
      return new KeymasterCertificateChain(paramAnonymousParcel, null);
    }
    
    public KeymasterCertificateChain[] newArray(int paramAnonymousInt)
    {
      return new KeymasterCertificateChain[paramAnonymousInt];
    }
  };
  private List<byte[]> mCertificates;
  
  public KeymasterCertificateChain()
  {
    mCertificates = null;
  }
  
  private KeymasterCertificateChain(Parcel paramParcel)
  {
    readFromParcel(paramParcel);
  }
  
  public KeymasterCertificateChain(List<byte[]> paramList)
  {
    mCertificates = paramList;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public List<byte[]> getCertificates()
  {
    return mCertificates;
  }
  
  public void readFromParcel(Parcel paramParcel)
  {
    int i = paramParcel.readInt();
    mCertificates = new ArrayList(i);
    for (int j = 0; j < i; j++) {
      mCertificates.add(paramParcel.createByteArray());
    }
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    if (mCertificates == null)
    {
      paramParcel.writeInt(0);
    }
    else
    {
      paramParcel.writeInt(mCertificates.size());
      Iterator localIterator = mCertificates.iterator();
      while (localIterator.hasNext()) {
        paramParcel.writeByteArray((byte[])localIterator.next());
      }
    }
  }
}
