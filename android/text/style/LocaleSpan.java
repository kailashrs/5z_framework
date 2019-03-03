package android.text.style;

import android.graphics.Paint;
import android.os.LocaleList;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.text.ParcelableSpan;
import android.text.TextPaint;
import com.android.internal.util.Preconditions;
import java.util.Locale;

public class LocaleSpan
  extends MetricAffectingSpan
  implements ParcelableSpan
{
  private final LocaleList mLocales;
  
  public LocaleSpan(LocaleList paramLocaleList)
  {
    Preconditions.checkNotNull(paramLocaleList, "locales cannot be null");
    mLocales = paramLocaleList;
  }
  
  public LocaleSpan(Parcel paramParcel)
  {
    mLocales = ((LocaleList)LocaleList.CREATOR.createFromParcel(paramParcel));
  }
  
  public LocaleSpan(Locale paramLocale)
  {
    if (paramLocale == null) {
      paramLocale = LocaleList.getEmptyLocaleList();
    } else {
      paramLocale = new LocaleList(new Locale[] { paramLocale });
    }
    mLocales = paramLocale;
  }
  
  private static void apply(Paint paramPaint, LocaleList paramLocaleList)
  {
    paramPaint.setTextLocales(paramLocaleList);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public Locale getLocale()
  {
    return mLocales.get(0);
  }
  
  public LocaleList getLocales()
  {
    return mLocales;
  }
  
  public int getSpanTypeId()
  {
    return getSpanTypeIdInternal();
  }
  
  public int getSpanTypeIdInternal()
  {
    return 23;
  }
  
  public void updateDrawState(TextPaint paramTextPaint)
  {
    apply(paramTextPaint, mLocales);
  }
  
  public void updateMeasureState(TextPaint paramTextPaint)
  {
    apply(paramTextPaint, mLocales);
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    writeToParcelInternal(paramParcel, paramInt);
  }
  
  public void writeToParcelInternal(Parcel paramParcel, int paramInt)
  {
    mLocales.writeToParcel(paramParcel, paramInt);
  }
}
