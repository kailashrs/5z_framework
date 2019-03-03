package android.widget;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Process;
import android.os.SystemClock;
import android.provider.Settings.System;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.RemotableViewMethod;
import android.view.ViewDebug.ExportedProperty;
import android.view.ViewHierarchyEncoder;
import com.android.internal.R.styleable;
import java.util.Calendar;
import java.util.TimeZone;
import libcore.icu.LocaleData;

@RemoteViews.RemoteView
public class TextClock
  extends TextView
{
  @Deprecated
  public static final CharSequence DEFAULT_FORMAT_12_HOUR = "h:mm a";
  @Deprecated
  public static final CharSequence DEFAULT_FORMAT_24_HOUR = "H:mm";
  private CharSequence mDescFormat;
  private CharSequence mDescFormat12;
  private CharSequence mDescFormat24;
  @ViewDebug.ExportedProperty
  private CharSequence mFormat;
  private CharSequence mFormat12;
  private CharSequence mFormat24;
  private ContentObserver mFormatChangeObserver;
  @ViewDebug.ExportedProperty
  private boolean mHasSeconds;
  private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      if (mStopTicking) {
        return;
      }
      if ((mTimeZone == null) && ("android.intent.action.TIMEZONE_CHANGED".equals(paramAnonymousIntent.getAction())))
      {
        paramAnonymousContext = paramAnonymousIntent.getStringExtra("time-zone");
        TextClock.this.createTime(paramAnonymousContext);
      }
      TextClock.this.onTimeChanged();
    }
  };
  private boolean mRegistered;
  private boolean mShouldRunTicker;
  private boolean mShowCurrentUserTime;
  private boolean mStopTicking;
  private final Runnable mTicker = new Runnable()
  {
    public void run()
    {
      if (mStopTicking) {
        return;
      }
      TextClock.this.onTimeChanged();
      long l = SystemClock.uptimeMillis();
      getHandler().postAtTime(mTicker, 1000L - l % 1000L + l);
    }
  };
  private Calendar mTime;
  private String mTimeZone;
  
  public TextClock(Context paramContext)
  {
    super(paramContext);
    init();
  }
  
  public TextClock(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public TextClock(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public TextClock(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    paramAttributeSet = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.TextClock, paramInt1, paramInt2);
    try
    {
      mFormat12 = paramAttributeSet.getText(0);
      mFormat24 = paramAttributeSet.getText(1);
      mTimeZone = paramAttributeSet.getString(2);
      paramAttributeSet.recycle();
      init();
      return;
    }
    finally
    {
      paramAttributeSet.recycle();
    }
  }
  
  private static CharSequence abc(CharSequence paramCharSequence1, CharSequence paramCharSequence2, CharSequence paramCharSequence3)
  {
    if (paramCharSequence1 == null) {
      if (paramCharSequence2 == null) {
        paramCharSequence1 = paramCharSequence3;
      } else {
        paramCharSequence1 = paramCharSequence2;
      }
    }
    return paramCharSequence1;
  }
  
  private void chooseFormat()
  {
    boolean bool = is24HourModeEnabled();
    LocaleData localLocaleData = LocaleData.get(getContextgetResourcesgetConfigurationlocale);
    if (bool)
    {
      mFormat = abc(mFormat24, mFormat12, timeFormat_Hm);
      mDescFormat = abc(mDescFormat24, mDescFormat12, mFormat);
    }
    else
    {
      mFormat = abc(mFormat12, mFormat24, timeFormat_hm);
      mDescFormat = abc(mDescFormat12, mDescFormat24, mFormat);
    }
    bool = mHasSeconds;
    mHasSeconds = DateFormat.hasSeconds(mFormat);
    if ((mShouldRunTicker) && (bool != mHasSeconds)) {
      if (bool) {
        getHandler().removeCallbacks(mTicker);
      } else {
        mTicker.run();
      }
    }
  }
  
  private void createTime(String paramString)
  {
    if (paramString != null) {
      mTime = Calendar.getInstance(TimeZone.getTimeZone(paramString));
    } else {
      mTime = Calendar.getInstance();
    }
  }
  
  private void init()
  {
    if ((mFormat12 == null) || (mFormat24 == null))
    {
      LocaleData localLocaleData = LocaleData.get(getContextgetResourcesgetConfigurationlocale);
      if (mFormat12 == null) {
        mFormat12 = timeFormat_hm;
      }
      if (mFormat24 == null) {
        mFormat24 = timeFormat_Hm;
      }
    }
    createTime(mTimeZone);
    chooseFormat();
  }
  
  private void onTimeChanged()
  {
    if (mShouldRunTicker)
    {
      mTime.setTimeInMillis(System.currentTimeMillis());
      setText(DateFormat.format(mFormat, mTime));
      setContentDescription(DateFormat.format(mDescFormat, mTime));
    }
  }
  
  private void registerObserver()
  {
    if (mRegistered)
    {
      if (mFormatChangeObserver == null) {
        mFormatChangeObserver = new FormatChangeObserver(getHandler());
      }
      ContentResolver localContentResolver = getContext().getContentResolver();
      Uri localUri = Settings.System.getUriFor("time_12_24");
      if (mShowCurrentUserTime) {
        localContentResolver.registerContentObserver(localUri, true, mFormatChangeObserver, -1);
      } else {
        localContentResolver.registerContentObserver(localUri, true, mFormatChangeObserver);
      }
    }
  }
  
  private void registerReceiver()
  {
    IntentFilter localIntentFilter = new IntentFilter();
    localIntentFilter.addAction("android.intent.action.TIME_TICK");
    localIntentFilter.addAction("android.intent.action.TIME_SET");
    localIntentFilter.addAction("android.intent.action.TIMEZONE_CHANGED");
    getContext().registerReceiverAsUser(mIntentReceiver, Process.myUserHandle(), localIntentFilter, null, getHandler());
  }
  
  private void unregisterObserver()
  {
    if (mFormatChangeObserver != null) {
      getContext().getContentResolver().unregisterContentObserver(mFormatChangeObserver);
    }
  }
  
  private void unregisterReceiver()
  {
    getContext().unregisterReceiver(mIntentReceiver);
  }
  
  public void disableClockTick()
  {
    mStopTicking = true;
  }
  
  protected void encodeProperties(ViewHierarchyEncoder paramViewHierarchyEncoder)
  {
    super.encodeProperties(paramViewHierarchyEncoder);
    Object localObject1 = getFormat12Hour();
    Object localObject2 = null;
    if (localObject1 == null) {
      localObject1 = null;
    } else {
      localObject1 = ((CharSequence)localObject1).toString();
    }
    paramViewHierarchyEncoder.addProperty("format12Hour", (String)localObject1);
    localObject1 = getFormat24Hour();
    if (localObject1 == null) {
      localObject1 = null;
    } else {
      localObject1 = ((CharSequence)localObject1).toString();
    }
    paramViewHierarchyEncoder.addProperty("format24Hour", (String)localObject1);
    if (mFormat == null) {
      localObject1 = localObject2;
    } else {
      localObject1 = mFormat.toString();
    }
    paramViewHierarchyEncoder.addProperty("format", (String)localObject1);
    paramViewHierarchyEncoder.addProperty("hasSeconds", mHasSeconds);
  }
  
  public CharSequence getFormat()
  {
    return mFormat;
  }
  
  @ViewDebug.ExportedProperty
  public CharSequence getFormat12Hour()
  {
    return mFormat12;
  }
  
  @ViewDebug.ExportedProperty
  public CharSequence getFormat24Hour()
  {
    return mFormat24;
  }
  
  public String getTimeZone()
  {
    return mTimeZone;
  }
  
  public boolean is24HourModeEnabled()
  {
    if (mShowCurrentUserTime) {
      return DateFormat.is24HourFormat(getContext(), ActivityManager.getCurrentUser());
    }
    return DateFormat.is24HourFormat(getContext());
  }
  
  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    if (!mRegistered)
    {
      mRegistered = true;
      registerReceiver();
      registerObserver();
      createTime(mTimeZone);
    }
  }
  
  protected void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    if (mRegistered)
    {
      unregisterReceiver();
      unregisterObserver();
      mRegistered = false;
    }
  }
  
  public void onVisibilityAggregated(boolean paramBoolean)
  {
    super.onVisibilityAggregated(paramBoolean);
    if ((!mShouldRunTicker) && (paramBoolean))
    {
      mShouldRunTicker = true;
      if (mHasSeconds) {
        mTicker.run();
      } else {
        onTimeChanged();
      }
    }
    else if ((mShouldRunTicker) && (!paramBoolean))
    {
      mShouldRunTicker = false;
      getHandler().removeCallbacks(mTicker);
    }
  }
  
  public void refresh()
  {
    onTimeChanged();
    invalidate();
  }
  
  public void setContentDescriptionFormat12Hour(CharSequence paramCharSequence)
  {
    mDescFormat12 = paramCharSequence;
    chooseFormat();
    onTimeChanged();
  }
  
  public void setContentDescriptionFormat24Hour(CharSequence paramCharSequence)
  {
    mDescFormat24 = paramCharSequence;
    chooseFormat();
    onTimeChanged();
  }
  
  @RemotableViewMethod
  public void setFormat12Hour(CharSequence paramCharSequence)
  {
    mFormat12 = paramCharSequence;
    chooseFormat();
    onTimeChanged();
  }
  
  @RemotableViewMethod
  public void setFormat24Hour(CharSequence paramCharSequence)
  {
    mFormat24 = paramCharSequence;
    chooseFormat();
    onTimeChanged();
  }
  
  public void setShowCurrentUserTime(boolean paramBoolean)
  {
    mShowCurrentUserTime = paramBoolean;
    chooseFormat();
    onTimeChanged();
    unregisterObserver();
    registerObserver();
  }
  
  @RemotableViewMethod
  public void setTimeZone(String paramString)
  {
    mTimeZone = paramString;
    createTime(paramString);
    onTimeChanged();
  }
  
  private class FormatChangeObserver
    extends ContentObserver
  {
    public FormatChangeObserver(Handler paramHandler)
    {
      super();
    }
    
    public void onChange(boolean paramBoolean)
    {
      TextClock.this.chooseFormat();
      TextClock.this.onTimeChanged();
    }
    
    public void onChange(boolean paramBoolean, Uri paramUri)
    {
      TextClock.this.chooseFormat();
      TextClock.this.onTimeChanged();
    }
  }
}
