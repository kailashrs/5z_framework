package android.webkit;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import java.util.Calendar;
import java.util.Locale;
import libcore.icu.LocaleData;

public class DateSorter
{
  public static final int DAY_COUNT = 5;
  private static final String LOGTAG = "webkit";
  private static final int NUM_DAYS_AGO = 7;
  private long[] mBins = new long[4];
  private String[] mLabels = new String[5];
  
  public DateSorter(Context paramContext)
  {
    Resources localResources = paramContext.getResources();
    Object localObject = Calendar.getInstance();
    beginningOfDay((Calendar)localObject);
    mBins[0] = ((Calendar)localObject).getTimeInMillis();
    ((Calendar)localObject).add(6, -1);
    mBins[1] = ((Calendar)localObject).getTimeInMillis();
    ((Calendar)localObject).add(6, -6);
    mBins[2] = ((Calendar)localObject).getTimeInMillis();
    ((Calendar)localObject).add(6, 7);
    ((Calendar)localObject).add(2, -1);
    mBins[3] = ((Calendar)localObject).getTimeInMillis();
    Locale localLocale = getConfigurationlocale;
    localObject = localLocale;
    if (localLocale == null) {
      localObject = Locale.getDefault();
    }
    localObject = LocaleData.get((Locale)localObject);
    mLabels[0] = today;
    mLabels[1] = yesterday;
    localObject = localResources.getQuantityString(18153491, 7);
    mLabels[2] = String.format((String)localObject, new Object[] { Integer.valueOf(7) });
    mLabels[3] = paramContext.getString(17040229);
    mLabels[4] = paramContext.getString(17040527);
  }
  
  private void beginningOfDay(Calendar paramCalendar)
  {
    paramCalendar.set(11, 0);
    paramCalendar.set(12, 0);
    paramCalendar.set(13, 0);
    paramCalendar.set(14, 0);
  }
  
  public long getBoundary(int paramInt)
  {
    int i;
    if (paramInt >= 0)
    {
      i = paramInt;
      if (paramInt <= 4) {}
    }
    else
    {
      i = 0;
    }
    if (i == 4) {
      return Long.MIN_VALUE;
    }
    return mBins[i];
  }
  
  public int getIndex(long paramLong)
  {
    for (int i = 0; i < 4; i++) {
      if (paramLong > mBins[i]) {
        return i;
      }
    }
    return 4;
  }
  
  public String getLabel(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt < 5)) {
      return mLabels[paramInt];
    }
    return "";
  }
}
