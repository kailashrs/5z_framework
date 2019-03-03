package android.view.textservice;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.SpellCheckSpan;

public final class TextInfo
  implements Parcelable
{
  public static final Parcelable.Creator<TextInfo> CREATOR = new Parcelable.Creator()
  {
    public TextInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new TextInfo(paramAnonymousParcel);
    }
    
    public TextInfo[] newArray(int paramAnonymousInt)
    {
      return new TextInfo[paramAnonymousInt];
    }
  };
  private static final int DEFAULT_COOKIE = 0;
  private static final int DEFAULT_SEQUENCE_NUMBER = 0;
  private final CharSequence mCharSequence;
  private final int mCookie;
  private final int mSequenceNumber;
  
  public TextInfo(Parcel paramParcel)
  {
    mCharSequence = ((CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel));
    mCookie = paramParcel.readInt();
    mSequenceNumber = paramParcel.readInt();
  }
  
  public TextInfo(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if (!TextUtils.isEmpty(paramCharSequence))
    {
      SpannableStringBuilder localSpannableStringBuilder = new SpannableStringBuilder(paramCharSequence, paramInt1, paramInt2);
      paramInt2 = localSpannableStringBuilder.length();
      paramInt1 = 0;
      paramCharSequence = (SpellCheckSpan[])localSpannableStringBuilder.getSpans(0, paramInt2, SpellCheckSpan.class);
      while (paramInt1 < paramCharSequence.length)
      {
        localSpannableStringBuilder.removeSpan(paramCharSequence[paramInt1]);
        paramInt1++;
      }
      mCharSequence = localSpannableStringBuilder;
      mCookie = paramInt3;
      mSequenceNumber = paramInt4;
      return;
    }
    throw new IllegalArgumentException("charSequence is empty");
  }
  
  public TextInfo(String paramString)
  {
    this(paramString, 0, getStringLengthOrZero(paramString), 0, 0);
  }
  
  public TextInfo(String paramString, int paramInt1, int paramInt2)
  {
    this(paramString, 0, getStringLengthOrZero(paramString), paramInt1, paramInt2);
  }
  
  private static int getStringLengthOrZero(String paramString)
  {
    int i;
    if (TextUtils.isEmpty(paramString)) {
      i = 0;
    } else {
      i = paramString.length();
    }
    return i;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public CharSequence getCharSequence()
  {
    return mCharSequence;
  }
  
  public int getCookie()
  {
    return mCookie;
  }
  
  public int getSequence()
  {
    return mSequenceNumber;
  }
  
  public String getText()
  {
    if (mCharSequence == null) {
      return null;
    }
    return mCharSequence.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    TextUtils.writeToParcel(mCharSequence, paramParcel, paramInt);
    paramParcel.writeInt(mCookie);
    paramParcel.writeInt(mSequenceNumber);
  }
}
