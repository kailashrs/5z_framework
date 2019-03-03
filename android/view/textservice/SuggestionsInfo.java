package android.view.textservice;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.internal.util.ArrayUtils;

public final class SuggestionsInfo
  implements Parcelable
{
  public static final Parcelable.Creator<SuggestionsInfo> CREATOR = new Parcelable.Creator()
  {
    public SuggestionsInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new SuggestionsInfo(paramAnonymousParcel);
    }
    
    public SuggestionsInfo[] newArray(int paramAnonymousInt)
    {
      return new SuggestionsInfo[paramAnonymousInt];
    }
  };
  private static final String[] EMPTY = (String[])ArrayUtils.emptyArray(String.class);
  public static final int RESULT_ATTR_HAS_RECOMMENDED_SUGGESTIONS = 4;
  public static final int RESULT_ATTR_IN_THE_DICTIONARY = 1;
  public static final int RESULT_ATTR_LOOKS_LIKE_TYPO = 2;
  private int mCookie;
  private int mSequence;
  private final String[] mSuggestions;
  private final int mSuggestionsAttributes;
  private final boolean mSuggestionsAvailable;
  
  public SuggestionsInfo(int paramInt, String[] paramArrayOfString)
  {
    this(paramInt, paramArrayOfString, 0, 0);
  }
  
  public SuggestionsInfo(int paramInt1, String[] paramArrayOfString, int paramInt2, int paramInt3)
  {
    if (paramArrayOfString == null)
    {
      mSuggestions = EMPTY;
      mSuggestionsAvailable = false;
    }
    else
    {
      mSuggestions = paramArrayOfString;
      mSuggestionsAvailable = true;
    }
    mSuggestionsAttributes = paramInt1;
    mCookie = paramInt2;
    mSequence = paramInt3;
  }
  
  public SuggestionsInfo(Parcel paramParcel)
  {
    mSuggestionsAttributes = paramParcel.readInt();
    mSuggestions = paramParcel.readStringArray();
    mCookie = paramParcel.readInt();
    mSequence = paramParcel.readInt();
    int i = paramParcel.readInt();
    boolean bool = true;
    if (i != 1) {
      bool = false;
    }
    mSuggestionsAvailable = bool;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getCookie()
  {
    return mCookie;
  }
  
  public int getSequence()
  {
    return mSequence;
  }
  
  public String getSuggestionAt(int paramInt)
  {
    return mSuggestions[paramInt];
  }
  
  public int getSuggestionsAttributes()
  {
    return mSuggestionsAttributes;
  }
  
  public int getSuggestionsCount()
  {
    if (!mSuggestionsAvailable) {
      return -1;
    }
    return mSuggestions.length;
  }
  
  public void setCookieAndSequence(int paramInt1, int paramInt2)
  {
    mCookie = paramInt1;
    mSequence = paramInt2;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mSuggestionsAttributes);
    paramParcel.writeStringArray(mSuggestions);
    paramParcel.writeInt(mCookie);
    paramParcel.writeInt(mSequence);
    paramParcel.writeInt(mSuggestionsAvailable);
  }
}
