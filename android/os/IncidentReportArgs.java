package android.os;

import android.annotation.SystemApi;
import android.util.IntArray;
import java.util.ArrayList;

@SystemApi
public final class IncidentReportArgs
  implements Parcelable
{
  public static final Parcelable.Creator<IncidentReportArgs> CREATOR = new Parcelable.Creator()
  {
    public IncidentReportArgs createFromParcel(Parcel paramAnonymousParcel)
    {
      return new IncidentReportArgs(paramAnonymousParcel);
    }
    
    public IncidentReportArgs[] newArray(int paramAnonymousInt)
    {
      return new IncidentReportArgs[paramAnonymousInt];
    }
  };
  private static final int DEST_AUTO = 200;
  private static final int DEST_EXPLICIT = 100;
  private boolean mAll;
  private int mDest;
  private final ArrayList<byte[]> mHeaders = new ArrayList();
  private final IntArray mSections = new IntArray();
  
  public IncidentReportArgs()
  {
    mDest = 200;
  }
  
  public IncidentReportArgs(Parcel paramParcel)
  {
    readFromParcel(paramParcel);
  }
  
  public void addHeader(byte[] paramArrayOfByte)
  {
    mHeaders.add(paramArrayOfByte);
  }
  
  public void addSection(int paramInt)
  {
    if ((!mAll) && (paramInt > 1)) {
      mSections.add(paramInt);
    }
  }
  
  public boolean containsSection(int paramInt)
  {
    boolean bool;
    if ((!mAll) && (mSections.indexOf(paramInt) < 0)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean isAll()
  {
    return mAll;
  }
  
  public void readFromParcel(Parcel paramParcel)
  {
    int i = paramParcel.readInt();
    int j = 0;
    boolean bool;
    if (i != 0) {
      bool = true;
    } else {
      bool = false;
    }
    mAll = bool;
    mSections.clear();
    int k = paramParcel.readInt();
    for (i = 0; i < k; i++) {
      mSections.add(paramParcel.readInt());
    }
    mHeaders.clear();
    k = paramParcel.readInt();
    for (i = j; i < k; i++) {
      mHeaders.add(paramParcel.createByteArray());
    }
    mDest = paramParcel.readInt();
  }
  
  public int sectionCount()
  {
    return mSections.size();
  }
  
  public void setAll(boolean paramBoolean)
  {
    mAll = paramBoolean;
    if (paramBoolean) {
      mSections.clear();
    }
  }
  
  public void setPrivacyPolicy(int paramInt)
  {
    if ((paramInt != 100) && (paramInt != 200)) {
      mDest = 200;
    } else {
      mDest = paramInt;
    }
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder("Incident(");
    if (mAll)
    {
      localStringBuilder.append("all");
    }
    else
    {
      int i = mSections.size();
      if (i > 0) {
        localStringBuilder.append(mSections.get(0));
      }
      for (int j = 1; j < i; j++)
      {
        localStringBuilder.append(" ");
        localStringBuilder.append(mSections.get(j));
      }
    }
    localStringBuilder.append(", ");
    localStringBuilder.append(mHeaders.size());
    localStringBuilder.append(" headers), ");
    localStringBuilder.append("Dest enum value: ");
    localStringBuilder.append(mDest);
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mAll);
    int i = mSections.size();
    paramParcel.writeInt(i);
    int j = 0;
    for (paramInt = 0; paramInt < i; paramInt++) {
      paramParcel.writeInt(mSections.get(paramInt));
    }
    i = mHeaders.size();
    paramParcel.writeInt(i);
    for (paramInt = j; paramInt < i; paramInt++) {
      paramParcel.writeByteArray((byte[])mHeaders.get(paramInt));
    }
    paramParcel.writeInt(mDest);
  }
}
