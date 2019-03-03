package com.android.internal.telephony;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.telephony.Rlog;

public final class HbpcdUtils
{
  private static final boolean DBG = false;
  private static final String LOG_TAG = "HbpcdUtils";
  private ContentResolver resolver = null;
  
  public HbpcdUtils(Context paramContext)
  {
    resolver = paramContext.getContentResolver();
  }
  
  public String getIddByMcc(int paramInt)
  {
    String str = "";
    Object localObject1 = resolver;
    Object localObject2 = HbpcdLookup.MccIdd.CONTENT_URI;
    Object localObject3 = new StringBuilder();
    ((StringBuilder)localObject3).append("MCC=");
    ((StringBuilder)localObject3).append(paramInt);
    localObject3 = ((StringBuilder)localObject3).toString();
    localObject1 = ((ContentResolver)localObject1).query((Uri)localObject2, new String[] { "IDD" }, (String)localObject3, null, null);
    localObject2 = str;
    if (localObject1 != null)
    {
      if (((Cursor)localObject1).getCount() > 0)
      {
        ((Cursor)localObject1).moveToFirst();
        str = ((Cursor)localObject1).getString(0);
      }
      ((Cursor)localObject1).close();
      localObject2 = str;
    }
    if (0 != 0) {
      throw new NullPointerException();
    }
    return localObject2;
  }
  
  public int getMcc(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean)
  {
    Object localObject1 = resolver;
    Object localObject2 = HbpcdLookup.ArbitraryMccSidMatch.CONTENT_URI;
    Object localObject3 = new StringBuilder();
    ((StringBuilder)localObject3).append("SID=");
    ((StringBuilder)localObject3).append(paramInt1);
    localObject3 = ((StringBuilder)localObject3).toString();
    localObject2 = ((ContentResolver)localObject1).query((Uri)localObject2, new String[] { "MCC" }, (String)localObject3, null, null);
    if (localObject2 != null)
    {
      if (((Cursor)localObject2).getCount() == 1)
      {
        ((Cursor)localObject2).moveToFirst();
        paramInt1 = ((Cursor)localObject2).getInt(0);
        ((Cursor)localObject2).close();
        return paramInt1;
      }
      ((Cursor)localObject2).close();
    }
    localObject2 = resolver;
    localObject1 = HbpcdLookup.MccSidConflicts.CONTENT_URI;
    localObject3 = new StringBuilder();
    ((StringBuilder)localObject3).append("SID_Conflict=");
    ((StringBuilder)localObject3).append(paramInt1);
    ((StringBuilder)localObject3).append(" and (((");
    ((StringBuilder)localObject3).append("GMT_Offset_Low");
    ((StringBuilder)localObject3).append("<=");
    ((StringBuilder)localObject3).append(paramInt2);
    ((StringBuilder)localObject3).append(") and (");
    ((StringBuilder)localObject3).append(paramInt2);
    ((StringBuilder)localObject3).append("<=");
    ((StringBuilder)localObject3).append("GMT_Offset_High");
    ((StringBuilder)localObject3).append(") and (0=");
    ((StringBuilder)localObject3).append(paramInt3);
    ((StringBuilder)localObject3).append(")) or ((");
    ((StringBuilder)localObject3).append("GMT_DST_Low");
    ((StringBuilder)localObject3).append("<=");
    ((StringBuilder)localObject3).append(paramInt2);
    ((StringBuilder)localObject3).append(") and (");
    ((StringBuilder)localObject3).append(paramInt2);
    ((StringBuilder)localObject3).append("<=");
    ((StringBuilder)localObject3).append("GMT_DST_High");
    ((StringBuilder)localObject3).append(") and (1=");
    ((StringBuilder)localObject3).append(paramInt3);
    ((StringBuilder)localObject3).append(")))");
    localObject3 = ((StringBuilder)localObject3).toString();
    localObject2 = ((ContentResolver)localObject2).query((Uri)localObject1, new String[] { "MCC" }, (String)localObject3, null, null);
    if (localObject2 != null)
    {
      paramInt2 = ((Cursor)localObject2).getCount();
      if (paramInt2 > 0)
      {
        if (paramInt2 > 1)
        {
          localObject1 = new StringBuilder();
          ((StringBuilder)localObject1).append("something wrong, get more results for 1 conflict SID: ");
          ((StringBuilder)localObject1).append(localObject2);
          Rlog.w("HbpcdUtils", ((StringBuilder)localObject1).toString());
        }
        ((Cursor)localObject2).moveToFirst();
        paramInt1 = ((Cursor)localObject2).getInt(0);
        if (!paramBoolean) {
          paramInt1 = 0;
        }
        ((Cursor)localObject2).close();
        return paramInt1;
      }
      ((Cursor)localObject2).close();
    }
    localObject2 = resolver;
    localObject1 = HbpcdLookup.MccSidRange.CONTENT_URI;
    localObject3 = new StringBuilder();
    ((StringBuilder)localObject3).append("SID_Range_Low<=");
    ((StringBuilder)localObject3).append(paramInt1);
    ((StringBuilder)localObject3).append(" and ");
    ((StringBuilder)localObject3).append("SID_Range_High");
    ((StringBuilder)localObject3).append(">=");
    ((StringBuilder)localObject3).append(paramInt1);
    localObject3 = ((StringBuilder)localObject3).toString();
    localObject2 = ((ContentResolver)localObject2).query((Uri)localObject1, new String[] { "MCC" }, (String)localObject3, null, null);
    if (localObject2 != null)
    {
      if (((Cursor)localObject2).getCount() > 0)
      {
        ((Cursor)localObject2).moveToFirst();
        paramInt1 = ((Cursor)localObject2).getInt(0);
        ((Cursor)localObject2).close();
        return paramInt1;
      }
      ((Cursor)localObject2).close();
    }
    return 0;
  }
}
