package android.text.style;

import android.os.Parcel;
import android.text.Layout.Alignment;
import android.text.ParcelableSpan;

public abstract interface AlignmentSpan
  extends ParagraphStyle
{
  public abstract Layout.Alignment getAlignment();
  
  public static class Standard
    implements AlignmentSpan, ParcelableSpan
  {
    private final Layout.Alignment mAlignment;
    
    public Standard(Parcel paramParcel)
    {
      mAlignment = Layout.Alignment.valueOf(paramParcel.readString());
    }
    
    public Standard(Layout.Alignment paramAlignment)
    {
      mAlignment = paramAlignment;
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public Layout.Alignment getAlignment()
    {
      return mAlignment;
    }
    
    public int getSpanTypeId()
    {
      return getSpanTypeIdInternal();
    }
    
    public int getSpanTypeIdInternal()
    {
      return 1;
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      writeToParcelInternal(paramParcel, paramInt);
    }
    
    public void writeToParcelInternal(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeString(mAlignment.name());
    }
  }
}
