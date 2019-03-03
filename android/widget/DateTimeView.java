package android.widget;

import android.app.ActivityThread;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.ContentObserver;
import android.icu.util.Calendar;
import android.os.Handler;
import android.text.format.Time;
import android.util.AttributeSet;
import android.view.RemotableViewMethod;
import android.view.accessibility.AccessibilityNodeInfo;
import com.android.internal.R.styleable;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import libcore.icu.DateUtilsBridge;

@RemoteViews.RemoteView
public class DateTimeView
  extends TextView
{
  private static final int SHOW_MONTH_DAY_YEAR = 1;
  private static final int SHOW_TIME = 0;
  private static final ThreadLocal<ReceiverInfo> sReceiverInfo = new ThreadLocal();
  int mLastDisplay = -1;
  java.text.DateFormat mLastFormat;
  private String mNowText;
  private boolean mShowRelativeTime;
  Date mTime;
  long mTimeMillis;
  private long mUpdateTimeMillis;
  
  public DateTimeView(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public DateTimeView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.DateTimeView, 0, 0);
    int i = paramContext.getIndexCount();
    for (int j = 0; j < i; j++) {
      if (paramContext.getIndex(j) == 0) {
        setShowRelativeTime(paramContext.getBoolean(j, false));
      }
    }
    paramContext.recycle();
  }
  
  private long computeNextMidnight(TimeZone paramTimeZone)
  {
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.setTimeZone(DateUtilsBridge.icuTimeZone(paramTimeZone));
    localCalendar.add(5, 1);
    localCalendar.set(11, 0);
    localCalendar.set(12, 0);
    localCalendar.set(13, 0);
    localCalendar.set(14, 0);
    return localCalendar.getTimeInMillis();
  }
  
  private static int dayDistance(TimeZone paramTimeZone, long paramLong1, long paramLong2)
  {
    return Time.getJulianDay(paramLong2, paramTimeZone.getOffset(paramLong2) / 1000) - Time.getJulianDay(paramLong1, paramTimeZone.getOffset(paramLong1) / 1000);
  }
  
  private java.text.DateFormat getTimeFormat()
  {
    return android.text.format.DateFormat.getTimeFormat(getContext());
  }
  
  public static void setReceiverHandler(Handler paramHandler)
  {
    ReceiverInfo localReceiverInfo1 = (ReceiverInfo)sReceiverInfo.get();
    ReceiverInfo localReceiverInfo2 = localReceiverInfo1;
    if (localReceiverInfo1 == null)
    {
      localReceiverInfo2 = new ReceiverInfo(null);
      sReceiverInfo.set(localReceiverInfo2);
    }
    localReceiverInfo2.setHandler(paramHandler);
  }
  
  private void updateNowText()
  {
    if (!mShowRelativeTime) {
      return;
    }
    mNowText = getContext().getResources().getString(17040521);
  }
  
  private void updateRelativeTime()
  {
    long l1 = System.currentTimeMillis();
    long l2 = Math.abs(l1 - mTimeMillis);
    int i;
    if (l1 >= mTimeMillis) {
      i = 1;
    } else {
      i = 0;
    }
    if (l2 < 60000L)
    {
      setText(mNowText);
      mUpdateTimeMillis = (mTimeMillis + 60000L + 1L);
      return;
    }
    long l3 = 31449600000L;
    int j;
    Object localObject;
    int k;
    long l4;
    if (l2 < 3600000L)
    {
      j = (int)(l2 / 60000L);
      localObject = getContext().getResources();
      if (i != 0) {
        k = 18153484;
      } else {
        k = 18153485;
      }
      localObject = String.format(((Resources)localObject).getQuantityString(k, j), new Object[] { Integer.valueOf(j) });
      l4 = 60000L;
      k = j;
    }
    for (;;)
    {
      break;
      l4 = 86400000L;
      if (l2 < 86400000L)
      {
        j = (int)(l2 / 3600000L);
        localObject = getContext().getResources();
        if (i != 0) {
          k = 18153480;
        } else {
          k = 18153481;
        }
        localObject = String.format(((Resources)localObject).getQuantityString(k, j), new Object[] { Integer.valueOf(j) });
        l4 = 3600000L;
        k = j;
      }
      else if (l2 < 31449600000L)
      {
        TimeZone localTimeZone = TimeZone.getDefault();
        j = Math.max(Math.abs(dayDistance(localTimeZone, mTimeMillis, l1)), 1);
        localObject = getContext().getResources();
        if (i != 0) {
          k = 18153476;
        } else {
          k = 18153477;
        }
        localObject = String.format(((Resources)localObject).getQuantityString(k, j), new Object[] { Integer.valueOf(j) });
        if ((i == 0) && (j == 1)) {
          break label346;
        }
        mUpdateTimeMillis = computeNextMidnight(localTimeZone);
        l4 = -1L;
        label346:
        k = j;
      }
      else
      {
        j = (int)(l2 / 31449600000L);
        localObject = getContext().getResources();
        if (i != 0) {
          k = 18153488;
        } else {
          k = 18153489;
        }
        localObject = String.format(((Resources)localObject).getQuantityString(k, j), new Object[] { Integer.valueOf(j) });
        l4 = l3;
        k = j;
      }
    }
    if (l4 != -1L) {
      if (i != 0) {
        mUpdateTimeMillis = (mTimeMillis + (k + 1) * l4 + 1L);
      } else {
        mUpdateTimeMillis = (mTimeMillis - k * l4 + 1L);
      }
    }
    setText((CharSequence)localObject);
  }
  
  void clearFormatAndUpdate()
  {
    mLastFormat = null;
    update();
  }
  
  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    ReceiverInfo localReceiverInfo1 = (ReceiverInfo)sReceiverInfo.get();
    ReceiverInfo localReceiverInfo2 = localReceiverInfo1;
    if (localReceiverInfo1 == null)
    {
      localReceiverInfo2 = new ReceiverInfo(null);
      sReceiverInfo.set(localReceiverInfo2);
    }
    localReceiverInfo2.addView(this);
    if (mShowRelativeTime) {
      update();
    }
  }
  
  protected void onConfigurationChanged(Configuration paramConfiguration)
  {
    super.onConfigurationChanged(paramConfiguration);
    updateNowText();
    update();
  }
  
  protected void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    ReceiverInfo localReceiverInfo = (ReceiverInfo)sReceiverInfo.get();
    if (localReceiverInfo != null) {
      localReceiverInfo.removeView(this);
    }
  }
  
  public void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo paramAccessibilityNodeInfo)
  {
    super.onInitializeAccessibilityNodeInfoInternal(paramAccessibilityNodeInfo);
    if (mShowRelativeTime)
    {
      long l1 = System.currentTimeMillis();
      long l2 = Math.abs(l1 - mTimeMillis);
      int i;
      if (l1 >= mTimeMillis) {
        i = 1;
      } else {
        i = 0;
      }
      Object localObject;
      if (l2 < 60000L) {
        localObject = mNowText;
      }
      for (;;)
      {
        break;
        int j;
        if (l2 < 3600000L)
        {
          j = (int)(l2 / 60000L);
          localObject = getContext().getResources();
          if (i != 0) {
            i = 18153482;
          } else {
            i = 18153483;
          }
          localObject = String.format(((Resources)localObject).getQuantityString(i, j), new Object[] { Integer.valueOf(j) });
        }
        else if (l2 < 86400000L)
        {
          j = (int)(l2 / 3600000L);
          localObject = getContext().getResources();
          if (i != 0) {
            i = 18153478;
          } else {
            i = 18153479;
          }
          localObject = String.format(((Resources)localObject).getQuantityString(i, j), new Object[] { Integer.valueOf(j) });
        }
        else if (l2 < 31449600000L)
        {
          j = Math.max(Math.abs(dayDistance(TimeZone.getDefault(), mTimeMillis, l1)), 1);
          localObject = getContext().getResources();
          if (i != 0) {
            i = 18153474;
          } else {
            i = 18153475;
          }
          localObject = String.format(((Resources)localObject).getQuantityString(i, j), new Object[] { Integer.valueOf(j) });
        }
        else
        {
          j = (int)(l2 / 31449600000L);
          localObject = getContext().getResources();
          if (i != 0) {
            i = 18153486;
          } else {
            i = 18153487;
          }
          localObject = String.format(((Resources)localObject).getQuantityString(i, j), new Object[] { Integer.valueOf(j) });
        }
      }
      paramAccessibilityNodeInfo.setText((CharSequence)localObject);
    }
  }
  
  @RemotableViewMethod
  public void setShowRelativeTime(boolean paramBoolean)
  {
    mShowRelativeTime = paramBoolean;
    updateNowText();
    update();
  }
  
  @RemotableViewMethod
  public void setTime(long paramLong)
  {
    Time localTime = new Time();
    localTime.set(paramLong);
    mTimeMillis = localTime.toMillis(false);
    mTime = new Date(year - 1900, month, monthDay, hour, minute, 0);
    update();
  }
  
  @RemotableViewMethod
  public void setVisibility(int paramInt)
  {
    int i;
    if ((paramInt != 8) && (getVisibility() == 8)) {
      i = 1;
    } else {
      i = 0;
    }
    super.setVisibility(paramInt);
    if (i != 0) {
      update();
    }
  }
  
  void update()
  {
    if ((mTime != null) && (getVisibility() != 8))
    {
      if (mShowRelativeTime)
      {
        updateRelativeTime();
        return;
      }
      Object localObject = mTime;
      localObject = new Time();
      ((Time)localObject).set(mTimeMillis);
      second = 0;
      hour -= 12;
      long l1 = ((Time)localObject).toMillis(false);
      hour += 12;
      long l2 = ((Time)localObject).toMillis(false);
      hour = 0;
      minute = 0;
      long l3 = ((Time)localObject).toMillis(false);
      int i = monthDay;
      int j = 1;
      monthDay = (i + 1);
      long l4 = ((Time)localObject).toMillis(false);
      ((Time)localObject).set(System.currentTimeMillis());
      second = 0;
      long l5 = ((Time)localObject).normalize(false);
      if (((l5 >= l3) && (l5 < l4)) || ((l5 >= l1) && (l5 < l2))) {
        j = 0;
      }
      if ((j == mLastDisplay) && (mLastFormat != null))
      {
        localObject = mLastFormat;
      }
      else
      {
        switch (j)
        {
        default: 
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("unknown display value: ");
          ((StringBuilder)localObject).append(j);
          throw new RuntimeException(((StringBuilder)localObject).toString());
        case 1: 
          localObject = java.text.DateFormat.getDateInstance(3);
          break;
        case 0: 
          localObject = getTimeFormat();
        }
        mLastFormat = ((java.text.DateFormat)localObject);
      }
      setText(((java.text.DateFormat)localObject).format(mTime));
      if (j == 0)
      {
        if (l2 > l4) {
          l1 = l2;
        } else {
          l1 = l4;
        }
        mUpdateTimeMillis = l1;
      }
      else if (mTimeMillis < l5)
      {
        mUpdateTimeMillis = 0L;
      }
      else
      {
        if (l1 >= l3) {
          l1 = l3;
        }
        mUpdateTimeMillis = l1;
      }
      return;
    }
  }
  
  private static class ReceiverInfo
  {
    private final ArrayList<DateTimeView> mAttachedViews = new ArrayList();
    private Handler mHandler = new Handler();
    private final ContentObserver mObserver = new ContentObserver(new Handler())
    {
      public void onChange(boolean paramAnonymousBoolean)
      {
        updateAll();
      }
    };
    private final BroadcastReceiver mReceiver = new BroadcastReceiver()
    {
      public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
      {
        if (("android.intent.action.TIME_TICK".equals(paramAnonymousIntent.getAction())) && (System.currentTimeMillis() < getSoonestUpdateTime())) {
          return;
        }
        updateAll();
      }
    };
    
    private ReceiverInfo() {}
    
    static final Context getApplicationContextIfAvailable(Context paramContext)
    {
      paramContext = paramContext.getApplicationContext();
      if (paramContext == null) {
        paramContext = ActivityThread.currentApplication().getApplicationContext();
      }
      return paramContext;
    }
    
    public void addView(DateTimeView paramDateTimeView)
    {
      synchronized (mAttachedViews)
      {
        boolean bool = mAttachedViews.isEmpty();
        mAttachedViews.add(paramDateTimeView);
        if (bool) {
          register(getApplicationContextIfAvailable(paramDateTimeView.getContext()));
        }
        return;
      }
    }
    
    long getSoonestUpdateTime()
    {
      long l1 = Long.MAX_VALUE;
      synchronized (mAttachedViews)
      {
        int i = mAttachedViews.size();
        int j = 0;
        while (j < i)
        {
          long l2 = mAttachedViews.get(j)).mUpdateTimeMillis;
          long l3 = l1;
          if (l2 < l1) {
            l3 = l2;
          }
          j++;
          l1 = l3;
        }
        return l1;
      }
    }
    
    void register(Context paramContext)
    {
      IntentFilter localIntentFilter = new IntentFilter();
      localIntentFilter.addAction("android.intent.action.TIME_TICK");
      localIntentFilter.addAction("android.intent.action.TIME_SET");
      localIntentFilter.addAction("android.intent.action.CONFIGURATION_CHANGED");
      localIntentFilter.addAction("android.intent.action.TIMEZONE_CHANGED");
      paramContext.registerReceiver(mReceiver, localIntentFilter, null, mHandler);
    }
    
    public void removeView(DateTimeView paramDateTimeView)
    {
      synchronized (mAttachedViews)
      {
        if ((mAttachedViews.remove(paramDateTimeView)) && (mAttachedViews.isEmpty())) {
          unregister(getApplicationContextIfAvailable(paramDateTimeView.getContext()));
        }
        return;
      }
    }
    
    public void setHandler(Handler arg1)
    {
      mHandler = ???;
      synchronized (mAttachedViews)
      {
        if (!mAttachedViews.isEmpty())
        {
          unregister(((DateTimeView)mAttachedViews.get(0)).getContext());
          register(((DateTimeView)mAttachedViews.get(0)).getContext());
        }
        return;
      }
    }
    
    void unregister(Context paramContext)
    {
      paramContext.unregisterReceiver(mReceiver);
    }
    
    void updateAll()
    {
      synchronized (mAttachedViews)
      {
        int i = mAttachedViews.size();
        for (int j = 0; j < i; j++)
        {
          DateTimeView localDateTimeView = (DateTimeView)mAttachedViews.get(j);
          _..Lambda.DateTimeView.ReceiverInfo.AVLnX7U5lTcE9jLnlKKNAT1GUeI localAVLnX7U5lTcE9jLnlKKNAT1GUeI = new android/widget/_$$Lambda$DateTimeView$ReceiverInfo$AVLnX7U5lTcE9jLnlKKNAT1GUeI;
          localAVLnX7U5lTcE9jLnlKKNAT1GUeI.<init>(localDateTimeView);
          localDateTimeView.post(localAVLnX7U5lTcE9jLnlKKNAT1GUeI);
        }
        return;
      }
    }
  }
}
