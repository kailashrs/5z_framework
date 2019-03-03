package android.text.style;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.LeakyTypefaceStorage;
import android.graphics.Typeface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.text.ParcelableSpan;
import android.text.TextPaint;
import com.android.internal.R.styleable;

public class TextAppearanceSpan
  extends MetricAffectingSpan
  implements ParcelableSpan
{
  private final String mFamilyName;
  private final int mStyle;
  private final ColorStateList mTextColor;
  private final ColorStateList mTextColorLink;
  private final int mTextSize;
  private final Typeface mTypeface;
  
  public TextAppearanceSpan(Context paramContext, int paramInt)
  {
    this(paramContext, paramInt, -1);
  }
  
  public TextAppearanceSpan(Context paramContext, int paramInt1, int paramInt2)
  {
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramInt1, R.styleable.TextAppearance);
    ColorStateList localColorStateList = localTypedArray.getColorStateList(3);
    mTextColorLink = localTypedArray.getColorStateList(6);
    mTextSize = localTypedArray.getDimensionPixelSize(0, -1);
    mStyle = localTypedArray.getInt(2, 0);
    if ((!paramContext.isRestricted()) && (paramContext.canLoadUnsafeResources())) {
      mTypeface = localTypedArray.getFont(12);
    } else {
      mTypeface = null;
    }
    if (mTypeface != null)
    {
      mFamilyName = null;
    }
    else
    {
      String str = localTypedArray.getString(12);
      if (str != null) {
        mFamilyName = str;
      } else {
        switch (localTypedArray.getInt(1, 0))
        {
        default: 
          mFamilyName = null;
          break;
        case 6: 
          mFamilyName = "weatherfontZenUI5";
          break;
        case 5: 
          mFamilyName = "weatherfontReg2";
          break;
        case 4: 
          mFamilyName = "weatherfontReg";
          break;
        case 3: 
          mFamilyName = "monospace";
          break;
        case 2: 
          mFamilyName = "serif";
          break;
        case 1: 
          mFamilyName = "sans";
        }
      }
    }
    localTypedArray.recycle();
    if (paramInt2 >= 0)
    {
      paramContext = paramContext.obtainStyledAttributes(16973829, R.styleable.Theme);
      localColorStateList = paramContext.getColorStateList(paramInt2);
      paramContext.recycle();
    }
    mTextColor = localColorStateList;
  }
  
  public TextAppearanceSpan(Parcel paramParcel)
  {
    mFamilyName = paramParcel.readString();
    mStyle = paramParcel.readInt();
    mTextSize = paramParcel.readInt();
    if (paramParcel.readInt() != 0) {
      mTextColor = ((ColorStateList)ColorStateList.CREATOR.createFromParcel(paramParcel));
    } else {
      mTextColor = null;
    }
    if (paramParcel.readInt() != 0) {
      mTextColorLink = ((ColorStateList)ColorStateList.CREATOR.createFromParcel(paramParcel));
    } else {
      mTextColorLink = null;
    }
    mTypeface = LeakyTypefaceStorage.readTypefaceFromParcel(paramParcel);
  }
  
  public TextAppearanceSpan(String paramString, int paramInt1, int paramInt2, ColorStateList paramColorStateList1, ColorStateList paramColorStateList2)
  {
    mFamilyName = paramString;
    mStyle = paramInt1;
    mTextSize = paramInt2;
    mTextColor = paramColorStateList1;
    mTextColorLink = paramColorStateList2;
    mTypeface = null;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String getFamily()
  {
    return mFamilyName;
  }
  
  public ColorStateList getLinkTextColor()
  {
    return mTextColorLink;
  }
  
  public int getSpanTypeId()
  {
    return getSpanTypeIdInternal();
  }
  
  public int getSpanTypeIdInternal()
  {
    return 17;
  }
  
  public ColorStateList getTextColor()
  {
    return mTextColor;
  }
  
  public int getTextSize()
  {
    return mTextSize;
  }
  
  public int getTextStyle()
  {
    return mStyle;
  }
  
  public void updateDrawState(TextPaint paramTextPaint)
  {
    updateMeasureState(paramTextPaint);
    if (mTextColor != null) {
      paramTextPaint.setColor(mTextColor.getColorForState(drawableState, 0));
    }
    if (mTextColorLink != null) {
      linkColor = mTextColorLink.getColorForState(drawableState, 0);
    }
  }
  
  public void updateMeasureState(TextPaint paramTextPaint)
  {
    int i = 0;
    int j = 0;
    Typeface localTypeface;
    if (mTypeface != null)
    {
      j = mStyle;
      localTypeface = Typeface.create(mTypeface, j);
    }
    else if ((mFamilyName == null) && (mStyle == 0))
    {
      localTypeface = null;
      j = i;
    }
    else
    {
      localTypeface = paramTextPaint.getTypeface();
      if (localTypeface != null) {
        j = localTypeface.getStyle();
      }
      j |= mStyle;
      if (mFamilyName != null) {
        localTypeface = Typeface.create(mFamilyName, j);
      }
      for (;;)
      {
        break;
        if (localTypeface == null) {
          localTypeface = Typeface.defaultFromStyle(j);
        } else {
          localTypeface = Typeface.create(localTypeface, j);
        }
      }
    }
    if (localTypeface != null)
    {
      j = localTypeface.getStyle() & j;
      if ((j & 0x1) != 0) {
        paramTextPaint.setFakeBoldText(true);
      }
      if ((j & 0x2) != 0) {
        paramTextPaint.setTextSkewX(-0.25F);
      }
      paramTextPaint.setTypeface(localTypeface);
    }
    if (mTextSize > 0) {
      paramTextPaint.setTextSize(mTextSize);
    }
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    writeToParcelInternal(paramParcel, paramInt);
  }
  
  public void writeToParcelInternal(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mFamilyName);
    paramParcel.writeInt(mStyle);
    paramParcel.writeInt(mTextSize);
    if (mTextColor != null)
    {
      paramParcel.writeInt(1);
      mTextColor.writeToParcel(paramParcel, paramInt);
    }
    else
    {
      paramParcel.writeInt(0);
    }
    if (mTextColorLink != null)
    {
      paramParcel.writeInt(1);
      mTextColorLink.writeToParcel(paramParcel, paramInt);
    }
    else
    {
      paramParcel.writeInt(0);
    }
    LeakyTypefaceStorage.writeTypefaceToParcel(mTypeface, paramParcel);
  }
}
