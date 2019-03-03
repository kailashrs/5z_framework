package android.text.format;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.icu.text.MeasureFormat;
import android.icu.text.MeasureFormat.FormatWidth;
import android.icu.util.Measure;
import android.icu.util.MeasureUnit;
import android.net.NetworkUtils;
import android.os.LocaleList;
import android.text.BidiFormatter;
import android.text.TextUtils;
import java.net.InetAddress;
import java.util.Locale;

public final class Formatter
{
  public static final int FLAG_CALCULATE_ROUNDED = 2;
  public static final int FLAG_IEC_UNITS = 8;
  public static final int FLAG_SHORTER = 1;
  public static final int FLAG_SI_UNITS = 4;
  private static final int MILLIS_PER_MINUTE = 60000;
  private static final int SECONDS_PER_DAY = 86400;
  private static final int SECONDS_PER_HOUR = 3600;
  private static final int SECONDS_PER_MINUTE = 60;
  
  public Formatter() {}
  
  private static String bidiWrap(Context paramContext, String paramString)
  {
    if (TextUtils.getLayoutDirectionFromLocale(localeFromContext(paramContext)) == 1) {
      return BidiFormatter.getInstance(true).unicodeWrap(paramString);
    }
    return paramString;
  }
  
  public static BytesResult formatBytes(Resources paramResources, long paramLong, int paramInt)
  {
    int i;
    if ((paramInt & 0x8) != 0) {
      i = 1024;
    } else {
      i = 1000;
    }
    long l1 = 0L;
    int j;
    if (paramLong < 0L) {
      j = 1;
    } else {
      j = 0;
    }
    if (j != 0) {
      f1 = (float)-paramLong;
    } else {
      f1 = (float)paramLong;
    }
    int k = 17039624;
    long l2 = 1L;
    float f2 = f1;
    if (f1 > 900.0F)
    {
      k = 17040225;
      l2 = i;
      f2 = f1 / i;
    }
    float f1 = f2;
    paramLong = l2;
    if (f2 > 900.0F)
    {
      k = 17040424;
      paramLong = l2 * i;
      f1 = f2 / i;
    }
    float f3 = f1;
    l2 = paramLong;
    if (f1 > 900.0F)
    {
      k = 17040041;
      l2 = paramLong * i;
      f3 = f1 / i;
    }
    f2 = f3;
    paramLong = l2;
    if (f3 > 900.0F)
    {
      k = 17041103;
      paramLong = l2 * i;
      f2 = f3 / i;
    }
    f1 = f2;
    int m = k;
    l2 = paramLong;
    if (f2 > 900.0F)
    {
      m = 17040800;
      l2 = paramLong * i;
      f1 = f2 / i;
    }
    if ((l2 != 1L) && (f1 < 100.0F))
    {
      if (f1 < 1.0F)
      {
        k = 100;
        str = "%.2f";
      }
      else if (f1 < 10.0F)
      {
        if ((paramInt & 0x1) != 0)
        {
          k = 10;
          str = "%.1f";
        }
        else
        {
          k = 100;
          str = "%.2f";
        }
      }
      else if ((paramInt & 0x1) != 0)
      {
        k = 1;
        str = "%.0f";
      }
      else
      {
        k = 100;
        str = "%.2f";
      }
    }
    else
    {
      k = 1;
      str = "%.0f";
    }
    f2 = f1;
    if (j != 0) {
      f2 = -f1;
    }
    String str = String.format(str, new Object[] { Float.valueOf(f2) });
    if ((paramInt & 0x2) == 0) {
      paramLong = l1;
    } else {
      paramLong = Math.round(k * f2) * l2 / k;
    }
    return new BytesResult(str, paramResources.getString(m), paramLong);
  }
  
  public static String formatFileSize(Context paramContext, long paramLong)
  {
    if (paramContext == null) {
      return "";
    }
    BytesResult localBytesResult = formatBytes(paramContext.getResources(), paramLong, 4);
    return bidiWrap(paramContext, paramContext.getString(17039992, new Object[] { value, units }));
  }
  
  @Deprecated
  public static String formatIpAddress(int paramInt)
  {
    return NetworkUtils.intToInetAddress(paramInt).getHostAddress();
  }
  
  public static String formatShortElapsedTime(Context paramContext, long paramLong)
  {
    long l = paramLong / 1000L;
    int i = 0;
    int j = 0;
    int k = 0;
    paramLong = l;
    if (l >= 86400L)
    {
      i = (int)(l / 86400L);
      paramLong = l - 86400 * i;
    }
    l = paramLong;
    if (paramLong >= 3600L)
    {
      j = (int)(paramLong / 3600L);
      l = paramLong - j * 3600;
    }
    paramLong = l;
    if (l >= 60L)
    {
      k = (int)(l / 60L);
      paramLong = l - k * 60;
    }
    int m = (int)paramLong;
    paramContext = MeasureFormat.getInstance(localeFromContext(paramContext), MeasureFormat.FormatWidth.SHORT);
    if ((i < 2) && ((i <= 0) || (j != 0)))
    {
      if (i > 0) {
        return paramContext.formatMeasures(new Measure[] { new Measure(Integer.valueOf(i), MeasureUnit.DAY), new Measure(Integer.valueOf(j), MeasureUnit.HOUR) });
      }
      if ((j < 2) && ((j <= 0) || (k != 0)))
      {
        if (j > 0) {
          return paramContext.formatMeasures(new Measure[] { new Measure(Integer.valueOf(j), MeasureUnit.HOUR), new Measure(Integer.valueOf(k), MeasureUnit.MINUTE) });
        }
        if ((k < 2) && ((k <= 0) || (m != 0)))
        {
          if (k > 0) {
            return paramContext.formatMeasures(new Measure[] { new Measure(Integer.valueOf(k), MeasureUnit.MINUTE), new Measure(Integer.valueOf(m), MeasureUnit.SECOND) });
          }
          return paramContext.format(new Measure(Integer.valueOf(m), MeasureUnit.SECOND));
        }
        return paramContext.format(new Measure(Integer.valueOf(k + (m + 30) / 60), MeasureUnit.MINUTE));
      }
      return paramContext.format(new Measure(Integer.valueOf(j + (k + 30) / 60), MeasureUnit.HOUR));
    }
    return paramContext.format(new Measure(Integer.valueOf(i + (j + 12) / 24), MeasureUnit.DAY));
  }
  
  public static String formatShortElapsedTimeRoundingUpToMinutes(Context paramContext, long paramLong)
  {
    paramLong = (paramLong + 60000L - 1L) / 60000L;
    if ((paramLong != 0L) && (paramLong != 1L)) {
      return formatShortElapsedTime(paramContext, 60000L * paramLong);
    }
    return MeasureFormat.getInstance(localeFromContext(paramContext), MeasureFormat.FormatWidth.SHORT).format(new Measure(Long.valueOf(paramLong), MeasureUnit.MINUTE));
  }
  
  public static String formatShortFileSize(Context paramContext, long paramLong)
  {
    if (paramContext == null) {
      return "";
    }
    BytesResult localBytesResult = formatBytes(paramContext.getResources(), paramLong, 5);
    return bidiWrap(paramContext, paramContext.getString(17039992, new Object[] { value, units }));
  }
  
  private static Locale localeFromContext(Context paramContext)
  {
    return paramContext.getResources().getConfiguration().getLocales().get(0);
  }
  
  public static class BytesResult
  {
    public final long roundedBytes;
    public final String units;
    public final String value;
    
    public BytesResult(String paramString1, String paramString2, long paramLong)
    {
      value = paramString1;
      units = paramString2;
      roundedBytes = paramLong;
    }
  }
}
