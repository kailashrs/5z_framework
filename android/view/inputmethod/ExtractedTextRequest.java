package android.view.inputmethod;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class ExtractedTextRequest
  implements Parcelable
{
  public static final Parcelable.Creator<ExtractedTextRequest> CREATOR = new Parcelable.Creator()
  {
    public ExtractedTextRequest createFromParcel(Parcel paramAnonymousParcel)
    {
      ExtractedTextRequest localExtractedTextRequest = new ExtractedTextRequest();
      token = paramAnonymousParcel.readInt();
      flags = paramAnonymousParcel.readInt();
      hintMaxLines = paramAnonymousParcel.readInt();
      hintMaxChars = paramAnonymousParcel.readInt();
      return localExtractedTextRequest;
    }
    
    public ExtractedTextRequest[] newArray(int paramAnonymousInt)
    {
      return new ExtractedTextRequest[paramAnonymousInt];
    }
  };
  public int flags;
  public int hintMaxChars;
  public int hintMaxLines;
  public int token;
  
  public ExtractedTextRequest() {}
  
  public int describeContents()
  {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(token);
    paramParcel.writeInt(flags);
    paramParcel.writeInt(hintMaxLines);
    paramParcel.writeInt(hintMaxChars);
  }
}
