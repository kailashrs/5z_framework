package android.widget;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.icu.text.MeasureFormat;
import android.icu.text.MeasureFormat.FormatWidth;
import android.icu.util.Measure;
import android.icu.util.MeasureUnit;
import android.net.Uri;
import android.os.SystemClock;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.RemotableViewMethod;
import android.view.View;
import com.android.internal.R.styleable;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.IllegalFormatException;
import java.util.Locale;

@RemoteViews.RemoteView
public class Chronometer
  extends TextView
{
  private static final int HOUR_IN_SEC = 3600;
  private static final int MIN_IN_SEC = 60;
  private static final String TAG = "Chronometer";
  private long mBase;
  private boolean mCountDown;
  private String mFormat;
  private StringBuilder mFormatBuilder;
  private Formatter mFormatter;
  private Object[] mFormatterArgs = new Object[1];
  private Locale mFormatterLocale;
  private boolean mLogged;
  private long mNow;
  private OnChronometerTickListener mOnChronometerTickListener;
  private StringBuilder mRecycle = new StringBuilder(8);
  private boolean mRunning;
  private boolean mStarted;
  private final Runnable mTickRunnable = new Runnable()
  {
    public void run()
    {
      if (mRunning)
      {
        Chronometer.this.updateText(SystemClock.elapsedRealtime());
        dispatchChronometerTick();
        postDelayed(mTickRunnable, 1000L);
      }
    }
  };
  private boolean mVisible;
  
  public Chronometer(Context paramContext)
  {
    this(paramContext, null, 0);
  }
  
  public Chronometer(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public Chronometer(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public Chronometer(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.Chronometer, paramInt1, paramInt2);
    setFormat(paramContext.getString(0));
    setCountDown(paramContext.getBoolean(1, false));
    paramContext.recycle();
    init();
  }
  
  private static String formatDuration(long paramLong)
  {
    int i = (int)(paramLong / 1000L);
    int j = i;
    if (i < 0) {
      j = -i;
    }
    int k = 0;
    int m = 0;
    i = j;
    if (j >= 3600)
    {
      k = j / 3600;
      i = j - k * 3600;
    }
    int n = i;
    j = m;
    if (i >= 60)
    {
      j = i / 60;
      n = i - j * 60;
    }
    ArrayList localArrayList = new ArrayList();
    if (k > 0) {
      localArrayList.add(new Measure(Integer.valueOf(k), MeasureUnit.HOUR));
    }
    if (j > 0) {
      localArrayList.add(new Measure(Integer.valueOf(j), MeasureUnit.MINUTE));
    }
    localArrayList.add(new Measure(Integer.valueOf(n), MeasureUnit.SECOND));
    return MeasureFormat.getInstance(Locale.getDefault(), MeasureFormat.FormatWidth.WIDE).formatMeasures((Measure[])localArrayList.toArray(new Measure[localArrayList.size()]));
  }
  
  private void init()
  {
    mBase = SystemClock.elapsedRealtime();
    updateText(mBase);
  }
  
  private void updateRunning()
  {
    boolean bool;
    if ((mVisible) && (mStarted) && (isShown())) {
      bool = true;
    } else {
      bool = false;
    }
    if (bool != mRunning)
    {
      if (bool)
      {
        updateText(SystemClock.elapsedRealtime());
        dispatchChronometerTick();
        postDelayed(mTickRunnable, 1000L);
      }
      else
      {
        removeCallbacks(mTickRunnable);
      }
      mRunning = bool;
    }
  }
  
  private void updateText(long paramLong)
  {
    try
    {
      mNow = paramLong;
      if (mCountDown) {
        paramLong = mBase - paramLong;
      } else {
        paramLong -= mBase;
      }
      long l = paramLong / 1000L;
      int i = 0;
      paramLong = l;
      if (l < 0L)
      {
        paramLong = -l;
        i = 1;
      }
      Object localObject1 = DateUtils.formatElapsedTime(mRecycle, paramLong);
      Object localObject3 = localObject1;
      if (i != 0) {
        localObject3 = getResources().getString(17040459, new Object[] { localObject1 });
      }
      localObject1 = localObject3;
      Object localObject2;
      if (mFormat != null)
      {
        Locale localLocale = Locale.getDefault();
        if ((mFormatter == null) || (!localLocale.equals(mFormatterLocale)))
        {
          mFormatterLocale = localLocale;
          localObject1 = new java/util/Formatter;
          ((Formatter)localObject1).<init>(mFormatBuilder, localLocale);
          mFormatter = ((Formatter)localObject1);
        }
        mFormatBuilder.setLength(0);
        mFormatterArgs[0] = localObject3;
        try
        {
          mFormatter.format(mFormat, mFormatterArgs);
          localObject1 = mFormatBuilder.toString();
        }
        catch (IllegalFormatException localIllegalFormatException)
        {
          localObject2 = localObject3;
          if (!mLogged)
          {
            localObject2 = new java/lang/StringBuilder;
            ((StringBuilder)localObject2).<init>();
            ((StringBuilder)localObject2).append("Illegal format string: ");
            ((StringBuilder)localObject2).append(mFormat);
            Log.w("Chronometer", ((StringBuilder)localObject2).toString());
            mLogged = true;
            localObject2 = localObject3;
          }
        }
      }
      setText((CharSequence)localObject2);
      return;
    }
    finally {}
  }
  
  void dispatchChronometerTick()
  {
    if (mOnChronometerTickListener != null) {
      mOnChronometerTickListener.onChronometerTick(this);
    }
  }
  
  public CharSequence getAccessibilityClassName()
  {
    return Chronometer.class.getName();
  }
  
  public long getBase()
  {
    return mBase;
  }
  
  public CharSequence getContentDescription()
  {
    return formatDuration(mNow - mBase);
  }
  
  public String getFormat()
  {
    return mFormat;
  }
  
  public OnChronometerTickListener getOnChronometerTickListener()
  {
    return mOnChronometerTickListener;
  }
  
  public boolean isCountDown()
  {
    return mCountDown;
  }
  
  public boolean isTheFinalCountDown()
  {
    try
    {
      Context localContext = getContext();
      Intent localIntent = new android/content/Intent;
      localIntent.<init>("android.intent.action.VIEW", Uri.parse("https://youtu.be/9jK-NcRmVcw"));
      localContext.startActivity(localIntent.addCategory("android.intent.category.BROWSABLE").addFlags(528384));
      return true;
    }
    catch (Exception localException) {}
    return false;
  }
  
  protected void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    mVisible = false;
    updateRunning();
  }
  
  protected void onVisibilityChanged(View paramView, int paramInt)
  {
    super.onVisibilityChanged(paramView, paramInt);
    updateRunning();
  }
  
  protected void onWindowVisibilityChanged(int paramInt)
  {
    super.onWindowVisibilityChanged(paramInt);
    boolean bool;
    if (paramInt == 0) {
      bool = true;
    } else {
      bool = false;
    }
    mVisible = bool;
    updateRunning();
  }
  
  @RemotableViewMethod
  public void setBase(long paramLong)
  {
    mBase = paramLong;
    dispatchChronometerTick();
    updateText(SystemClock.elapsedRealtime());
  }
  
  @RemotableViewMethod
  public void setCountDown(boolean paramBoolean)
  {
    mCountDown = paramBoolean;
    updateText(SystemClock.elapsedRealtime());
  }
  
  @RemotableViewMethod
  public void setFormat(String paramString)
  {
    mFormat = paramString;
    if ((paramString != null) && (mFormatBuilder == null)) {
      mFormatBuilder = new StringBuilder(paramString.length() * 2);
    }
  }
  
  public void setOnChronometerTickListener(OnChronometerTickListener paramOnChronometerTickListener)
  {
    mOnChronometerTickListener = paramOnChronometerTickListener;
  }
  
  @RemotableViewMethod
  public void setStarted(boolean paramBoolean)
  {
    mStarted = paramBoolean;
    updateRunning();
  }
  
  public void start()
  {
    mStarted = true;
    updateRunning();
  }
  
  public void stop()
  {
    mStarted = false;
    updateRunning();
  }
  
  public static abstract interface OnChronometerTickListener
  {
    public abstract void onChronometerTick(Chronometer paramChronometer);
  }
}
