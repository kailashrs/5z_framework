package android.nfc;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class TechListParcel
  implements Parcelable
{
  public static final Parcelable.Creator<TechListParcel> CREATOR = new Parcelable.Creator()
  {
    public TechListParcel createFromParcel(Parcel paramAnonymousParcel)
    {
      int i = paramAnonymousParcel.readInt();
      String[][] arrayOfString; = new String[i][];
      for (int j = 0; j < i; j++) {
        arrayOfString;[j] = paramAnonymousParcel.readStringArray();
      }
      return new TechListParcel(arrayOfString;);
    }
    
    public TechListParcel[] newArray(int paramAnonymousInt)
    {
      return new TechListParcel[paramAnonymousInt];
    }
  };
  private String[][] mTechLists;
  
  public TechListParcel(String[]... paramVarArgs)
  {
    mTechLists = paramVarArgs;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String[][] getTechLists()
  {
    return mTechLists;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    int i = mTechLists.length;
    paramParcel.writeInt(i);
    for (paramInt = 0; paramInt < i; paramInt++) {
      paramParcel.writeStringArray(mTechLists[paramInt]);
    }
  }
}
