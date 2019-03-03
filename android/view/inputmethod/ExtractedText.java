package android.view.inputmethod;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;

public class ExtractedText
  implements Parcelable
{
  public static final Parcelable.Creator<ExtractedText> CREATOR = new Parcelable.Creator()
  {
    public ExtractedText createFromParcel(Parcel paramAnonymousParcel)
    {
      ExtractedText localExtractedText = new ExtractedText();
      text = ((CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramAnonymousParcel));
      startOffset = paramAnonymousParcel.readInt();
      partialStartOffset = paramAnonymousParcel.readInt();
      partialEndOffset = paramAnonymousParcel.readInt();
      selectionStart = paramAnonymousParcel.readInt();
      selectionEnd = paramAnonymousParcel.readInt();
      flags = paramAnonymousParcel.readInt();
      hint = ((CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramAnonymousParcel));
      return localExtractedText;
    }
    
    public ExtractedText[] newArray(int paramAnonymousInt)
    {
      return new ExtractedText[paramAnonymousInt];
    }
  };
  public static final int FLAG_SELECTING = 2;
  public static final int FLAG_SINGLE_LINE = 1;
  public int flags;
  public CharSequence hint;
  public int partialEndOffset;
  public int partialStartOffset;
  public int selectionEnd;
  public int selectionStart;
  public int startOffset;
  public CharSequence text;
  
  public ExtractedText() {}
  
  public int describeContents()
  {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    TextUtils.writeToParcel(text, paramParcel, paramInt);
    paramParcel.writeInt(startOffset);
    paramParcel.writeInt(partialStartOffset);
    paramParcel.writeInt(partialEndOffset);
    paramParcel.writeInt(selectionStart);
    paramParcel.writeInt(selectionEnd);
    paramParcel.writeInt(flags);
    TextUtils.writeToParcel(hint, paramParcel, paramInt);
  }
}
