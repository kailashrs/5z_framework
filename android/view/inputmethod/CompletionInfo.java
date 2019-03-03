package android.view.inputmethod;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;

public final class CompletionInfo
  implements Parcelable
{
  public static final Parcelable.Creator<CompletionInfo> CREATOR = new Parcelable.Creator()
  {
    public CompletionInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new CompletionInfo(paramAnonymousParcel, null);
    }
    
    public CompletionInfo[] newArray(int paramAnonymousInt)
    {
      return new CompletionInfo[paramAnonymousInt];
    }
  };
  private final long mId;
  private final CharSequence mLabel;
  private final int mPosition;
  private final CharSequence mText;
  
  public CompletionInfo(long paramLong, int paramInt, CharSequence paramCharSequence)
  {
    mId = paramLong;
    mPosition = paramInt;
    mText = paramCharSequence;
    mLabel = null;
  }
  
  public CompletionInfo(long paramLong, int paramInt, CharSequence paramCharSequence1, CharSequence paramCharSequence2)
  {
    mId = paramLong;
    mPosition = paramInt;
    mText = paramCharSequence1;
    mLabel = paramCharSequence2;
  }
  
  private CompletionInfo(Parcel paramParcel)
  {
    mId = paramParcel.readLong();
    mPosition = paramParcel.readInt();
    mText = ((CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel));
    mLabel = ((CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel));
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public long getId()
  {
    return mId;
  }
  
  public CharSequence getLabel()
  {
    return mLabel;
  }
  
  public int getPosition()
  {
    return mPosition;
  }
  
  public CharSequence getText()
  {
    return mText;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("CompletionInfo{#");
    localStringBuilder.append(mPosition);
    localStringBuilder.append(" \"");
    localStringBuilder.append(mText);
    localStringBuilder.append("\" id=");
    localStringBuilder.append(mId);
    localStringBuilder.append(" label=");
    localStringBuilder.append(mLabel);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeLong(mId);
    paramParcel.writeInt(mPosition);
    TextUtils.writeToParcel(mText, paramParcel, paramInt);
    TextUtils.writeToParcel(mLabel, paramParcel, paramInt);
  }
}
