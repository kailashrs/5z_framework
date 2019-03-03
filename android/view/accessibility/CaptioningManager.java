package android.view.accessibility;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings.Secure;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

public class CaptioningManager
{
  private static final int DEFAULT_ENABLED = 0;
  private static final float DEFAULT_FONT_SCALE = 1.0F;
  private static final int DEFAULT_PRESET = 0;
  private final ContentObserver mContentObserver;
  private final ContentResolver mContentResolver;
  private final ArrayList<CaptioningChangeListener> mListeners = new ArrayList();
  private final Runnable mStyleChangedRunnable = new Runnable()
  {
    public void run()
    {
      CaptioningManager.this.notifyUserStyleChanged();
    }
  };
  
  public CaptioningManager(Context paramContext)
  {
    mContentResolver = paramContext.getContentResolver();
    mContentObserver = new MyContentObserver(new Handler(paramContext.getMainLooper()));
  }
  
  private void notifyEnabledChanged()
  {
    boolean bool = isEnabled();
    synchronized (mListeners)
    {
      Iterator localIterator = mListeners.iterator();
      while (localIterator.hasNext()) {
        ((CaptioningChangeListener)localIterator.next()).onEnabledChanged(bool);
      }
      return;
    }
  }
  
  private void notifyFontScaleChanged()
  {
    float f = getFontScale();
    synchronized (mListeners)
    {
      Iterator localIterator = mListeners.iterator();
      while (localIterator.hasNext()) {
        ((CaptioningChangeListener)localIterator.next()).onFontScaleChanged(f);
      }
      return;
    }
  }
  
  private void notifyLocaleChanged()
  {
    Locale localLocale = getLocale();
    synchronized (mListeners)
    {
      Iterator localIterator = mListeners.iterator();
      while (localIterator.hasNext()) {
        ((CaptioningChangeListener)localIterator.next()).onLocaleChanged(localLocale);
      }
      return;
    }
  }
  
  private void notifyUserStyleChanged()
  {
    CaptionStyle localCaptionStyle = getUserStyle();
    synchronized (mListeners)
    {
      Iterator localIterator = mListeners.iterator();
      while (localIterator.hasNext()) {
        ((CaptioningChangeListener)localIterator.next()).onUserStyleChanged(localCaptionStyle);
      }
      return;
    }
  }
  
  private void registerObserver(String paramString)
  {
    mContentResolver.registerContentObserver(Settings.Secure.getUriFor(paramString), false, mContentObserver);
  }
  
  public void addCaptioningChangeListener(CaptioningChangeListener paramCaptioningChangeListener)
  {
    synchronized (mListeners)
    {
      if (mListeners.isEmpty())
      {
        registerObserver("accessibility_captioning_enabled");
        registerObserver("accessibility_captioning_foreground_color");
        registerObserver("accessibility_captioning_background_color");
        registerObserver("accessibility_captioning_window_color");
        registerObserver("accessibility_captioning_edge_type");
        registerObserver("accessibility_captioning_edge_color");
        registerObserver("accessibility_captioning_typeface");
        registerObserver("accessibility_captioning_font_scale");
        registerObserver("accessibility_captioning_locale");
        registerObserver("accessibility_captioning_preset");
      }
      mListeners.add(paramCaptioningChangeListener);
      return;
    }
  }
  
  public final float getFontScale()
  {
    return Settings.Secure.getFloat(mContentResolver, "accessibility_captioning_font_scale", 1.0F);
  }
  
  public final Locale getLocale()
  {
    Object localObject = getRawLocale();
    if (!TextUtils.isEmpty((CharSequence)localObject))
    {
      localObject = ((String)localObject).split("_");
      switch (localObject.length)
      {
      default: 
        break;
      case 3: 
        return new Locale(localObject[0], localObject[1], localObject[2]);
      case 2: 
        return new Locale(localObject[0], localObject[1]);
      case 1: 
        return new Locale(localObject[0]);
      }
    }
    return null;
  }
  
  public final String getRawLocale()
  {
    return Settings.Secure.getString(mContentResolver, "accessibility_captioning_locale");
  }
  
  public int getRawUserStyle()
  {
    return Settings.Secure.getInt(mContentResolver, "accessibility_captioning_preset", 0);
  }
  
  public CaptionStyle getUserStyle()
  {
    int i = getRawUserStyle();
    if (i == -1) {
      return CaptionStyle.getCustomStyle(mContentResolver);
    }
    return CaptionStyle.PRESETS[i];
  }
  
  public final boolean isEnabled()
  {
    int i = Settings.Secure.getInt(mContentResolver, "accessibility_captioning_enabled", 0);
    boolean bool = true;
    if (i != 1) {
      bool = false;
    }
    return bool;
  }
  
  public void removeCaptioningChangeListener(CaptioningChangeListener paramCaptioningChangeListener)
  {
    synchronized (mListeners)
    {
      mListeners.remove(paramCaptioningChangeListener);
      if (mListeners.isEmpty()) {
        mContentResolver.unregisterContentObserver(mContentObserver);
      }
      return;
    }
  }
  
  public static final class CaptionStyle
  {
    private static final CaptionStyle BLACK_ON_WHITE;
    private static final int COLOR_NONE_OPAQUE = 255;
    public static final int COLOR_UNSPECIFIED = 16777215;
    public static final CaptionStyle DEFAULT = WHITE_ON_BLACK;
    private static final CaptionStyle DEFAULT_CUSTOM;
    public static final int EDGE_TYPE_DEPRESSED = 4;
    public static final int EDGE_TYPE_DROP_SHADOW = 2;
    public static final int EDGE_TYPE_NONE = 0;
    public static final int EDGE_TYPE_OUTLINE = 1;
    public static final int EDGE_TYPE_RAISED = 3;
    public static final int EDGE_TYPE_UNSPECIFIED = -1;
    public static final CaptionStyle[] PRESETS;
    public static final int PRESET_CUSTOM = -1;
    private static final CaptionStyle UNSPECIFIED;
    private static final CaptionStyle WHITE_ON_BLACK = new CaptionStyle(-1, -16777216, 0, -16777216, 255, null);
    private static final CaptionStyle YELLOW_ON_BLACK;
    private static final CaptionStyle YELLOW_ON_BLUE;
    public final int backgroundColor;
    public final int edgeColor;
    public final int edgeType;
    public final int foregroundColor;
    private final boolean mHasBackgroundColor;
    private final boolean mHasEdgeColor;
    private final boolean mHasEdgeType;
    private final boolean mHasForegroundColor;
    private final boolean mHasWindowColor;
    private Typeface mParsedTypeface;
    public final String mRawTypeface;
    public final int windowColor;
    
    static
    {
      BLACK_ON_WHITE = new CaptionStyle(-16777216, -1, 0, -16777216, 255, null);
      YELLOW_ON_BLACK = new CaptionStyle(65280, -16777216, 0, -16777216, 255, null);
      YELLOW_ON_BLUE = new CaptionStyle(65280, -16776961, 0, -16777216, 255, null);
      UNSPECIFIED = new CaptionStyle(16777215, 16777215, -1, 16777215, 16777215, null);
      PRESETS = new CaptionStyle[] { WHITE_ON_BLACK, BLACK_ON_WHITE, YELLOW_ON_BLACK, YELLOW_ON_BLUE, UNSPECIFIED };
      DEFAULT_CUSTOM = WHITE_ON_BLACK;
    }
    
    private CaptionStyle(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, String paramString)
    {
      mHasForegroundColor = hasColor(paramInt1);
      mHasBackgroundColor = hasColor(paramInt2);
      int i = 0;
      int j = -1;
      if (paramInt3 != -1) {
        bool = true;
      } else {
        bool = false;
      }
      mHasEdgeType = bool;
      mHasEdgeColor = hasColor(paramInt4);
      mHasWindowColor = hasColor(paramInt5);
      if (mHasForegroundColor) {
        j = paramInt1;
      }
      foregroundColor = j;
      boolean bool = mHasBackgroundColor;
      j = -16777216;
      if (!bool) {
        paramInt2 = -16777216;
      }
      backgroundColor = paramInt2;
      paramInt1 = i;
      if (mHasEdgeType) {
        paramInt1 = paramInt3;
      }
      edgeType = paramInt1;
      paramInt1 = j;
      if (mHasEdgeColor) {
        paramInt1 = paramInt4;
      }
      edgeColor = paramInt1;
      if (mHasWindowColor) {
        paramInt1 = paramInt5;
      } else {
        paramInt1 = 255;
      }
      windowColor = paramInt1;
      mRawTypeface = paramString;
    }
    
    public static CaptionStyle getCustomStyle(ContentResolver paramContentResolver)
    {
      CaptionStyle localCaptionStyle = DEFAULT_CUSTOM;
      int i = Settings.Secure.getInt(paramContentResolver, "accessibility_captioning_foreground_color", foregroundColor);
      int j = Settings.Secure.getInt(paramContentResolver, "accessibility_captioning_background_color", backgroundColor);
      int k = Settings.Secure.getInt(paramContentResolver, "accessibility_captioning_edge_type", edgeType);
      int m = Settings.Secure.getInt(paramContentResolver, "accessibility_captioning_edge_color", edgeColor);
      int n = Settings.Secure.getInt(paramContentResolver, "accessibility_captioning_window_color", windowColor);
      String str = Settings.Secure.getString(paramContentResolver, "accessibility_captioning_typeface");
      paramContentResolver = str;
      if (str == null) {
        paramContentResolver = mRawTypeface;
      }
      return new CaptionStyle(i, j, k, m, n, paramContentResolver);
    }
    
    public static boolean hasColor(int paramInt)
    {
      boolean bool;
      if ((paramInt >>> 24 == 0) && ((0xFFFF00 & paramInt) != 0)) {
        bool = false;
      } else {
        bool = true;
      }
      return bool;
    }
    
    public CaptionStyle applyStyle(CaptionStyle paramCaptionStyle)
    {
      if (paramCaptionStyle.hasForegroundColor()) {}
      for (int i = foregroundColor;; i = foregroundColor) {
        break;
      }
      if (paramCaptionStyle.hasBackgroundColor()) {}
      for (int j = backgroundColor;; j = backgroundColor) {
        break;
      }
      if (paramCaptionStyle.hasEdgeType()) {}
      for (int k = edgeType;; k = edgeType) {
        break;
      }
      if (paramCaptionStyle.hasEdgeColor()) {}
      for (int m = edgeColor;; m = edgeColor) {
        break;
      }
      if (paramCaptionStyle.hasWindowColor()) {}
      for (int n = windowColor;; n = windowColor) {
        break;
      }
      if (mRawTypeface != null) {}
      for (paramCaptionStyle = mRawTypeface;; paramCaptionStyle = mRawTypeface) {
        break;
      }
      return new CaptionStyle(i, j, k, m, n, paramCaptionStyle);
    }
    
    public Typeface getTypeface()
    {
      if ((mParsedTypeface == null) && (!TextUtils.isEmpty(mRawTypeface))) {
        mParsedTypeface = Typeface.create(mRawTypeface, 0);
      }
      return mParsedTypeface;
    }
    
    public boolean hasBackgroundColor()
    {
      return mHasBackgroundColor;
    }
    
    public boolean hasEdgeColor()
    {
      return mHasEdgeColor;
    }
    
    public boolean hasEdgeType()
    {
      return mHasEdgeType;
    }
    
    public boolean hasForegroundColor()
    {
      return mHasForegroundColor;
    }
    
    public boolean hasWindowColor()
    {
      return mHasWindowColor;
    }
  }
  
  public static abstract class CaptioningChangeListener
  {
    public CaptioningChangeListener() {}
    
    public void onEnabledChanged(boolean paramBoolean) {}
    
    public void onFontScaleChanged(float paramFloat) {}
    
    public void onLocaleChanged(Locale paramLocale) {}
    
    public void onUserStyleChanged(CaptioningManager.CaptionStyle paramCaptionStyle) {}
  }
  
  private class MyContentObserver
    extends ContentObserver
  {
    private final Handler mHandler;
    
    public MyContentObserver(Handler paramHandler)
    {
      super();
      mHandler = paramHandler;
    }
    
    public void onChange(boolean paramBoolean, Uri paramUri)
    {
      paramUri = paramUri.getPath();
      paramUri = paramUri.substring(paramUri.lastIndexOf('/') + 1);
      if ("accessibility_captioning_enabled".equals(paramUri))
      {
        CaptioningManager.this.notifyEnabledChanged();
      }
      else if ("accessibility_captioning_locale".equals(paramUri))
      {
        CaptioningManager.this.notifyLocaleChanged();
      }
      else if ("accessibility_captioning_font_scale".equals(paramUri))
      {
        CaptioningManager.this.notifyFontScaleChanged();
      }
      else
      {
        mHandler.removeCallbacks(mStyleChangedRunnable);
        mHandler.post(mStyleChangedRunnable);
      }
    }
  }
}
