package android.view.textservice;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Arrays;

public final class SentenceSuggestionsInfo
  implements Parcelable
{
  public static final Parcelable.Creator<SentenceSuggestionsInfo> CREATOR = new Parcelable.Creator()
  {
    public SentenceSuggestionsInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new SentenceSuggestionsInfo(paramAnonymousParcel);
    }
    
    public SentenceSuggestionsInfo[] newArray(int paramAnonymousInt)
    {
      return new SentenceSuggestionsInfo[paramAnonymousInt];
    }
  };
  private final int[] mLengths;
  private final int[] mOffsets;
  private final SuggestionsInfo[] mSuggestionsInfos;
  
  public SentenceSuggestionsInfo(Parcel paramParcel)
  {
    mSuggestionsInfos = new SuggestionsInfo[paramParcel.readInt()];
    paramParcel.readTypedArray(mSuggestionsInfos, SuggestionsInfo.CREATOR);
    mOffsets = new int[mSuggestionsInfos.length];
    paramParcel.readIntArray(mOffsets);
    mLengths = new int[mSuggestionsInfos.length];
    paramParcel.readIntArray(mLengths);
  }
  
  public SentenceSuggestionsInfo(SuggestionsInfo[] paramArrayOfSuggestionsInfo, int[] paramArrayOfInt1, int[] paramArrayOfInt2)
  {
    if ((paramArrayOfSuggestionsInfo != null) && (paramArrayOfInt1 != null) && (paramArrayOfInt2 != null))
    {
      if ((paramArrayOfSuggestionsInfo.length == paramArrayOfInt1.length) && (paramArrayOfInt1.length == paramArrayOfInt2.length))
      {
        int i = paramArrayOfSuggestionsInfo.length;
        mSuggestionsInfos = ((SuggestionsInfo[])Arrays.copyOf(paramArrayOfSuggestionsInfo, i));
        mOffsets = Arrays.copyOf(paramArrayOfInt1, i);
        mLengths = Arrays.copyOf(paramArrayOfInt2, i);
        return;
      }
      throw new IllegalArgumentException();
    }
    throw new NullPointerException();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getLengthAt(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt < mLengths.length)) {
      return mLengths[paramInt];
    }
    return -1;
  }
  
  public int getOffsetAt(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt < mOffsets.length)) {
      return mOffsets[paramInt];
    }
    return -1;
  }
  
  public int getSuggestionsCount()
  {
    return mSuggestionsInfos.length;
  }
  
  public SuggestionsInfo getSuggestionsInfoAt(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt < mSuggestionsInfos.length)) {
      return mSuggestionsInfos[paramInt];
    }
    return null;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mSuggestionsInfos.length);
    paramParcel.writeTypedArray(mSuggestionsInfos, 0);
    paramParcel.writeIntArray(mOffsets);
    paramParcel.writeIntArray(mLengths);
  }
}
