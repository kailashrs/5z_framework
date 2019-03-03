package com.android.internal.telephony;

import android.icu.util.TimeZone;
import android.telephony.Rlog;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import libcore.util.TimeZoneFinder;

public class DefaultTimeZoneAsus
{
  private static final String[] COUNTRYLIST = { "cn", "cy", "kz", "pt", "cl", "gl", "ec", "cd", "nz" };
  private static final String TAG = "DefaultTimeZone";
  
  public DefaultTimeZoneAsus() {}
  
  public static String getDefaultTimeZone(String paramString1, String paramString2, String paramString3)
  {
    int i = 0;
    int k;
    for (int j = 0;; j++)
    {
      k = i;
      if (j >= COUNTRYLIST.length) {
        break;
      }
      if (paramString1.equals(COUNTRYLIST[j]))
      {
        k = 1;
        break;
      }
    }
    if (k != 0)
    {
      if (paramString2 != null)
      {
        Iterator localIterator = getTimeZonesAsus(paramString1).iterator();
        while (localIterator.hasNext()) {
          if (((TimeZone)localIterator.next()).getID().equals(paramString2))
          {
            paramString3 = new StringBuilder();
            paramString3.append("current ZoneID ");
            paramString3.append(paramString2);
            paramString3.append(" is in country ");
            paramString3.append(paramString1);
            paramString3.append(", skip");
            Rlog.i("DefaultTimeZone", paramString3.toString());
            return paramString2;
          }
        }
      }
      paramString1 = getTimeZonesAsus(paramString1).iterator();
      while (paramString1.hasNext()) {
        if (((TimeZone)paramString1.next()).getID().equals(paramString3)) {
          return paramString3;
        }
      }
    }
    return "";
  }
  
  public static List<TimeZone> getTimeZonesAsus(String paramString)
  {
    if (paramString == null)
    {
      Rlog.d("DefaultTimeZone", "getTimeZonesAsus(null): return empty list");
      return Collections.emptyList();
    }
    Object localObject = TimeZoneFinder.getInstance().lookupTimeZonesByCountry(paramString);
    if (localObject == null)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("getIcuTimeZonesAsus(");
      ((StringBuilder)localObject).append(paramString);
      ((StringBuilder)localObject).append("): returned null, converting to empty list");
      Rlog.i("DefaultTimeZone", ((StringBuilder)localObject).toString());
      return Collections.emptyList();
    }
    return localObject;
  }
}
