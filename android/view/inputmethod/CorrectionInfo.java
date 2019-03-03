package android.view.inputmethod;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;

public final class CorrectionInfo
  implements Parcelable
{
  public static final Parcelable.Creator<CorrectionInfo> CREATOR = new Parcelable.Creator()
  {
    public CorrectionInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new CorrectionInfo(paramAnonymousParcel, null);
    }
    
    public CorrectionInfo[] newArray(int paramAnonymousInt)
    {
      return new CorrectionInfo[paramAnonymousInt];
    }
  };
  private final CharSequence mNewText;
  private final int mOffset;
  private final CharSequence mOldText;
  
  public CorrectionInfo(int paramInt, CharSequence paramCharSequence1, CharSequence paramCharSequence2)
  {
    mOffset = paramInt;
    mOldText = paramCharSequence1;
    mNewText = paramCharSequence2;
  }
  
  private CorrectionInfo(Parcel paramParcel)
  {
    mOffset = paramParcel.readInt();
    mOldText = ((CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel));
    mNewText = ((CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel));
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public CharSequence getNewText()
  {
    return mNewText;
  }
  
  public int getOffset()
  {
    return mOffset;
  }
  
  public CharSequence getOldText()
  {
    return mOldText;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("CorrectionInfo{#");
    localStringBuilder.append(mOffset);
    localStringBuilder.append(" \"");
    localStringBuilder.append(mOldText);
    localStringBuilder.append("\" -> \"");
    localStringBuilder.append(mNewText);
    localStringBuilder.append("\"}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mOffset);
    TextUtils.writeToParcel(mOldText, paramParcel, paramInt);
    TextUtils.writeToParcel(mNewText, paramParcel, paramInt);
  }
}
