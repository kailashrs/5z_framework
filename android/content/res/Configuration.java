package android.content.res;

import android.app.WindowConfiguration;
import android.os.Build.VERSION;
import android.os.LocaleList;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.proto.ProtoOutputStream;
import com.android.internal.util.XmlUtils;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Locale;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

public final class Configuration
  implements Parcelable, Comparable<Configuration>
{
  public static final int ASSETS_SEQ_UNDEFINED = 0;
  public static final int COLOR_MODE_HDR_MASK = 12;
  public static final int COLOR_MODE_HDR_NO = 4;
  public static final int COLOR_MODE_HDR_SHIFT = 2;
  public static final int COLOR_MODE_HDR_UNDEFINED = 0;
  public static final int COLOR_MODE_HDR_YES = 8;
  public static final int COLOR_MODE_UNDEFINED = 0;
  public static final int COLOR_MODE_WIDE_COLOR_GAMUT_MASK = 3;
  public static final int COLOR_MODE_WIDE_COLOR_GAMUT_NO = 1;
  public static final int COLOR_MODE_WIDE_COLOR_GAMUT_UNDEFINED = 0;
  public static final int COLOR_MODE_WIDE_COLOR_GAMUT_YES = 2;
  public static final Parcelable.Creator<Configuration> CREATOR = new Parcelable.Creator()
  {
    public Configuration createFromParcel(Parcel paramAnonymousParcel)
    {
      return new Configuration(paramAnonymousParcel, null);
    }
    
    public Configuration[] newArray(int paramAnonymousInt)
    {
      return new Configuration[paramAnonymousInt];
    }
  };
  private static final boolean DEBUG = false;
  public static final int DENSITY_DPI_ANY = 65534;
  public static final int DENSITY_DPI_NONE = 65535;
  public static final int DENSITY_DPI_UNDEFINED = 0;
  public static final Configuration EMPTY = new Configuration();
  public static final int HARDKEYBOARDHIDDEN_NO = 1;
  public static final int HARDKEYBOARDHIDDEN_UNDEFINED = 0;
  public static final int HARDKEYBOARDHIDDEN_YES = 2;
  public static final int KEYBOARDHIDDEN_NO = 1;
  public static final int KEYBOARDHIDDEN_SOFT = 3;
  public static final int KEYBOARDHIDDEN_UNDEFINED = 0;
  public static final int KEYBOARDHIDDEN_YES = 2;
  public static final int KEYBOARD_12KEY = 3;
  public static final int KEYBOARD_NOKEYS = 1;
  public static final int KEYBOARD_QWERTY = 2;
  public static final int KEYBOARD_UNDEFINED = 0;
  public static final int MNC_ZERO = 65535;
  private static final String MonoTAG = "Monotype";
  public static final int NATIVE_CONFIG_COLOR_MODE = 65536;
  public static final int NATIVE_CONFIG_DENSITY = 256;
  public static final int NATIVE_CONFIG_FLIPFONT = 32768;
  public static final int NATIVE_CONFIG_KEYBOARD = 16;
  public static final int NATIVE_CONFIG_KEYBOARD_HIDDEN = 32;
  public static final int NATIVE_CONFIG_LAYOUTDIR = 16384;
  public static final int NATIVE_CONFIG_LOCALE = 4;
  public static final int NATIVE_CONFIG_MCC = 1;
  public static final int NATIVE_CONFIG_MNC = 2;
  public static final int NATIVE_CONFIG_NAVIGATION = 64;
  public static final int NATIVE_CONFIG_ORIENTATION = 128;
  public static final int NATIVE_CONFIG_SCREEN_LAYOUT = 2048;
  public static final int NATIVE_CONFIG_SCREEN_SIZE = 512;
  public static final int NATIVE_CONFIG_SMALLEST_SCREEN_SIZE = 8192;
  public static final int NATIVE_CONFIG_TOUCHSCREEN = 8;
  public static final int NATIVE_CONFIG_UI_MODE = 4096;
  public static final int NATIVE_CONFIG_VERSION = 1024;
  public static final int NAVIGATIONHIDDEN_NO = 1;
  public static final int NAVIGATIONHIDDEN_UNDEFINED = 0;
  public static final int NAVIGATIONHIDDEN_YES = 2;
  public static final int NAVIGATION_DPAD = 2;
  public static final int NAVIGATION_NONAV = 1;
  public static final int NAVIGATION_TRACKBALL = 3;
  public static final int NAVIGATION_UNDEFINED = 0;
  public static final int NAVIGATION_WHEEL = 4;
  public static final int ORIENTATION_LANDSCAPE = 2;
  public static final int ORIENTATION_PORTRAIT = 1;
  @Deprecated
  public static final int ORIENTATION_SQUARE = 3;
  public static final int ORIENTATION_UNDEFINED = 0;
  public static final int SCREENLAYOUT_COMPAT_NEEDED = 268435456;
  public static final int SCREENLAYOUT_LAYOUTDIR_LTR = 64;
  public static final int SCREENLAYOUT_LAYOUTDIR_MASK = 192;
  public static final int SCREENLAYOUT_LAYOUTDIR_RTL = 128;
  public static final int SCREENLAYOUT_LAYOUTDIR_SHIFT = 6;
  public static final int SCREENLAYOUT_LAYOUTDIR_UNDEFINED = 0;
  public static final int SCREENLAYOUT_LONG_MASK = 48;
  public static final int SCREENLAYOUT_LONG_NO = 16;
  public static final int SCREENLAYOUT_LONG_UNDEFINED = 0;
  public static final int SCREENLAYOUT_LONG_YES = 32;
  public static final int SCREENLAYOUT_ROUND_MASK = 768;
  public static final int SCREENLAYOUT_ROUND_NO = 256;
  public static final int SCREENLAYOUT_ROUND_SHIFT = 8;
  public static final int SCREENLAYOUT_ROUND_UNDEFINED = 0;
  public static final int SCREENLAYOUT_ROUND_YES = 512;
  public static final int SCREENLAYOUT_SIZE_LARGE = 3;
  public static final int SCREENLAYOUT_SIZE_MASK = 15;
  public static final int SCREENLAYOUT_SIZE_NORMAL = 2;
  public static final int SCREENLAYOUT_SIZE_SMALL = 1;
  public static final int SCREENLAYOUT_SIZE_UNDEFINED = 0;
  public static final int SCREENLAYOUT_SIZE_XLARGE = 4;
  public static final int SCREENLAYOUT_UNDEFINED = 0;
  public static final int SCREEN_HEIGHT_DP_UNDEFINED = 0;
  public static final int SCREEN_WIDTH_DP_UNDEFINED = 0;
  public static final int SMALLEST_SCREEN_WIDTH_DP_UNDEFINED = 0;
  public static final int TOUCHSCREEN_FINGER = 3;
  public static final int TOUCHSCREEN_NOTOUCH = 1;
  @Deprecated
  public static final int TOUCHSCREEN_STYLUS = 2;
  public static final int TOUCHSCREEN_UNDEFINED = 0;
  public static final int UI_MODE_NIGHT_MASK = 48;
  public static final int UI_MODE_NIGHT_NO = 16;
  public static final int UI_MODE_NIGHT_UNDEFINED = 0;
  public static final int UI_MODE_NIGHT_YES = 32;
  public static final int UI_MODE_TYPE_APPLIANCE = 5;
  public static final int UI_MODE_TYPE_CAR = 3;
  public static final int UI_MODE_TYPE_DESK = 2;
  public static final int UI_MODE_TYPE_MASK = 15;
  public static final int UI_MODE_TYPE_NORMAL = 1;
  public static final int UI_MODE_TYPE_TELEVISION = 4;
  public static final int UI_MODE_TYPE_UNDEFINED = 0;
  public static final int UI_MODE_TYPE_VR_HEADSET = 7;
  public static final int UI_MODE_TYPE_WATCH = 6;
  private static final String XML_ATTR_APP_BOUNDS = "app_bounds";
  private static final String XML_ATTR_COLOR_MODE = "clrMod";
  private static final String XML_ATTR_DENSITY = "density";
  private static final String XML_ATTR_FONT_SCALE = "fs";
  private static final String XML_ATTR_HARD_KEYBOARD_HIDDEN = "hardKeyHid";
  private static final String XML_ATTR_KEYBOARD = "key";
  private static final String XML_ATTR_KEYBOARD_HIDDEN = "keyHid";
  private static final String XML_ATTR_LOCALES = "locales";
  private static final String XML_ATTR_MCC = "mcc";
  private static final String XML_ATTR_MNC = "mnc";
  private static final String XML_ATTR_NAVIGATION = "nav";
  private static final String XML_ATTR_NAVIGATION_HIDDEN = "navHid";
  private static final String XML_ATTR_ORIENTATION = "ori";
  private static final String XML_ATTR_ROTATION = "rot";
  private static final String XML_ATTR_SCREEN_HEIGHT = "height";
  private static final String XML_ATTR_SCREEN_LAYOUT = "scrLay";
  private static final String XML_ATTR_SCREEN_WIDTH = "width";
  private static final String XML_ATTR_SMALLEST_WIDTH = "sw";
  private static final String XML_ATTR_TOUCHSCREEN = "touch";
  private static final String XML_ATTR_UI_MODE = "ui";
  public int FlipFont;
  public int assetsSeq;
  public int colorMode;
  public int compatScreenHeightDp;
  public int compatScreenWidthDp;
  public int compatSmallestScreenWidthDp;
  public int densityDpi;
  public float fontScale;
  public int hardKeyboardHidden;
  public int keyboard;
  public int keyboardHidden;
  @Deprecated
  public Locale locale;
  private LocaleList mLocaleList;
  public int mcc;
  public int mnc;
  public int navigation;
  public int navigationHidden;
  public int orientation;
  public int screenHeightDp;
  public int screenLayout;
  public int screenWidthDp;
  public int seq;
  public int smallestScreenWidthDp;
  public int touchscreen;
  public int uiMode;
  public boolean userSetLocale;
  public final WindowConfiguration windowConfiguration = new WindowConfiguration();
  
  public Configuration()
  {
    unset();
  }
  
  public Configuration(Configuration paramConfiguration)
  {
    setTo(paramConfiguration);
  }
  
  private Configuration(Parcel paramParcel)
  {
    readFromParcel(paramParcel);
  }
  
  public static String configurationDiffToString(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    if ((paramInt & 0x1) != 0) {
      localArrayList.add("CONFIG_MCC");
    }
    if ((paramInt & 0x2) != 0) {
      localArrayList.add("CONFIG_MNC");
    }
    if ((paramInt & 0x4) != 0) {
      localArrayList.add("CONFIG_LOCALE");
    }
    if ((paramInt & 0x8) != 0) {
      localArrayList.add("CONFIG_TOUCHSCREEN");
    }
    if ((paramInt & 0x10) != 0) {
      localArrayList.add("CONFIG_KEYBOARD");
    }
    if ((paramInt & 0x20) != 0) {
      localArrayList.add("CONFIG_KEYBOARD_HIDDEN");
    }
    if ((paramInt & 0x40) != 0) {
      localArrayList.add("CONFIG_NAVIGATION");
    }
    if ((paramInt & 0x80) != 0) {
      localArrayList.add("CONFIG_ORIENTATION");
    }
    if ((paramInt & 0x100) != 0) {
      localArrayList.add("CONFIG_SCREEN_LAYOUT");
    }
    if ((paramInt & 0x4000) != 0) {
      localArrayList.add("CONFIG_COLOR_MODE");
    }
    if ((paramInt & 0x200) != 0) {
      localArrayList.add("CONFIG_UI_MODE");
    }
    if ((paramInt & 0x400) != 0) {
      localArrayList.add("CONFIG_SCREEN_SIZE");
    }
    if ((paramInt & 0x800) != 0) {
      localArrayList.add("CONFIG_SMALLEST_SCREEN_SIZE");
    }
    if ((paramInt & 0x2000) != 0) {
      localArrayList.add("CONFIG_LAYOUT_DIRECTION");
    }
    if ((0x40000000 & paramInt) != 0) {
      localArrayList.add("CONFIG_FONT_SCALE");
    }
    if ((0x80000000 & paramInt) != 0) {
      localArrayList.add("CONFIG_ASSETS_PATHS");
    }
    StringBuilder localStringBuilder = new StringBuilder("{");
    paramInt = 0;
    int i = localArrayList.size();
    while (paramInt < i)
    {
      localStringBuilder.append((String)localArrayList.get(paramInt));
      if (paramInt != i - 1) {
        localStringBuilder.append(", ");
      }
      paramInt++;
    }
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  private void fixUpLocaleList()
  {
    if (((locale == null) && (!mLocaleList.isEmpty())) || ((locale != null) && (!locale.equals(mLocaleList.get(0)))))
    {
      LocaleList localLocaleList;
      if (locale == null) {
        localLocaleList = LocaleList.getEmptyLocaleList();
      } else {
        localLocaleList = new LocaleList(new Locale[] { locale });
      }
      mLocaleList = localLocaleList;
    }
  }
  
  public static Configuration generateDelta(Configuration paramConfiguration1, Configuration paramConfiguration2)
  {
    Configuration localConfiguration = new Configuration();
    if (fontScale != fontScale) {
      fontScale = fontScale;
    }
    if (mcc != mcc) {
      mcc = mcc;
    }
    if (mnc != mnc) {
      mnc = mnc;
    }
    paramConfiguration1.fixUpLocaleList();
    paramConfiguration2.fixUpLocaleList();
    if (!mLocaleList.equals(mLocaleList))
    {
      mLocaleList = mLocaleList;
      locale = locale;
    }
    if (touchscreen != touchscreen) {
      touchscreen = touchscreen;
    }
    if (keyboard != keyboard) {
      keyboard = keyboard;
    }
    if (keyboardHidden != keyboardHidden) {
      keyboardHidden = keyboardHidden;
    }
    if (navigation != navigation) {
      navigation = navigation;
    }
    if (navigationHidden != navigationHidden) {
      navigationHidden = navigationHidden;
    }
    if (orientation != orientation) {
      orientation = orientation;
    }
    if ((screenLayout & 0xF) != (screenLayout & 0xF)) {
      screenLayout |= screenLayout & 0xF;
    }
    if ((screenLayout & 0xC0) != (screenLayout & 0xC0)) {
      screenLayout |= screenLayout & 0xC0;
    }
    if ((screenLayout & 0x30) != (screenLayout & 0x30)) {
      screenLayout |= screenLayout & 0x30;
    }
    if ((screenLayout & 0x300) != (screenLayout & 0x300)) {
      screenLayout |= screenLayout & 0x300;
    }
    if ((colorMode & 0x3) != (colorMode & 0x3)) {
      colorMode |= colorMode & 0x3;
    }
    if ((colorMode & 0xC) != (colorMode & 0xC)) {
      colorMode |= colorMode & 0xC;
    }
    if ((uiMode & 0xF) != (uiMode & 0xF)) {
      uiMode |= uiMode & 0xF;
    }
    if ((uiMode & 0x30) != (uiMode & 0x30)) {
      uiMode |= uiMode & 0x30;
    }
    if (screenWidthDp != screenWidthDp) {
      screenWidthDp = screenWidthDp;
    }
    if (screenHeightDp != screenHeightDp) {
      screenHeightDp = screenHeightDp;
    }
    if (smallestScreenWidthDp != smallestScreenWidthDp) {
      smallestScreenWidthDp = smallestScreenWidthDp;
    }
    if (densityDpi != densityDpi) {
      densityDpi = densityDpi;
    }
    if (assetsSeq != assetsSeq) {
      assetsSeq = assetsSeq;
    }
    if (!windowConfiguration.equals(windowConfiguration)) {
      windowConfiguration.setTo(windowConfiguration);
    }
    return localConfiguration;
  }
  
  private static int getScreenLayoutNoDirection(int paramInt)
  {
    return paramInt & 0xFF3F;
  }
  
  public static String localesToResourceQualifier(LocaleList paramLocaleList)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    for (int i = 0; i < paramLocaleList.size(); i++)
    {
      Locale localLocale = paramLocaleList.get(i);
      int j = localLocale.getLanguage().length();
      if (j != 0)
      {
        int k = localLocale.getScript().length();
        int m = localLocale.getCountry().length();
        int n = localLocale.getVariant().length();
        if (localStringBuilder.length() != 0) {
          localStringBuilder.append(",");
        }
        if ((j == 2) && (k == 0) && ((m == 0) || (m == 2)) && (n == 0))
        {
          localStringBuilder.append(localLocale.getLanguage());
          if (m == 2)
          {
            localStringBuilder.append("-r");
            localStringBuilder.append(localLocale.getCountry());
          }
        }
        else
        {
          localStringBuilder.append("b+");
          localStringBuilder.append(localLocale.getLanguage());
          if (k != 0)
          {
            localStringBuilder.append("+");
            localStringBuilder.append(localLocale.getScript());
          }
          if (m != 0)
          {
            localStringBuilder.append("+");
            localStringBuilder.append(localLocale.getCountry());
          }
          if (n != 0)
          {
            localStringBuilder.append("+");
            localStringBuilder.append(localLocale.getVariant());
          }
        }
      }
    }
    return localStringBuilder.toString();
  }
  
  public static boolean needNewResources(int paramInt1, int paramInt2)
  {
    boolean bool;
    if ((paramInt1 & (0x80000000 | paramInt2 | 0x40000000 | 0x10000000)) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static void readXmlAttrs(XmlPullParser paramXmlPullParser, Configuration paramConfiguration)
    throws XmlPullParserException, IOException
  {
    fontScale = Float.intBitsToFloat(XmlUtils.readIntAttribute(paramXmlPullParser, "fs", 0));
    mcc = XmlUtils.readIntAttribute(paramXmlPullParser, "mcc", 0);
    mnc = XmlUtils.readIntAttribute(paramXmlPullParser, "mnc", 0);
    mLocaleList = LocaleList.forLanguageTags(XmlUtils.readStringAttribute(paramXmlPullParser, "locales"));
    locale = mLocaleList.get(0);
    touchscreen = XmlUtils.readIntAttribute(paramXmlPullParser, "touch", 0);
    keyboard = XmlUtils.readIntAttribute(paramXmlPullParser, "key", 0);
    keyboardHidden = XmlUtils.readIntAttribute(paramXmlPullParser, "keyHid", 0);
    hardKeyboardHidden = XmlUtils.readIntAttribute(paramXmlPullParser, "hardKeyHid", 0);
    navigation = XmlUtils.readIntAttribute(paramXmlPullParser, "nav", 0);
    navigationHidden = XmlUtils.readIntAttribute(paramXmlPullParser, "navHid", 0);
    orientation = XmlUtils.readIntAttribute(paramXmlPullParser, "ori", 0);
    screenLayout = XmlUtils.readIntAttribute(paramXmlPullParser, "scrLay", 0);
    colorMode = XmlUtils.readIntAttribute(paramXmlPullParser, "clrMod", 0);
    uiMode = XmlUtils.readIntAttribute(paramXmlPullParser, "ui", 0);
    screenWidthDp = XmlUtils.readIntAttribute(paramXmlPullParser, "width", 0);
    screenHeightDp = XmlUtils.readIntAttribute(paramXmlPullParser, "height", 0);
    smallestScreenWidthDp = XmlUtils.readIntAttribute(paramXmlPullParser, "sw", 0);
    densityDpi = XmlUtils.readIntAttribute(paramXmlPullParser, "density", 0);
  }
  
  public static int reduceScreenLayout(int paramInt1, int paramInt2, int paramInt3)
  {
    int i = 0;
    int j;
    int k;
    if (paramInt2 < 470)
    {
      j = 0;
      k = 1;
      paramInt2 = 0;
    }
    else
    {
      if ((paramInt2 >= 960) && (paramInt3 >= 720)) {
        k = 4;
      }
      for (;;)
      {
        break;
        if ((paramInt2 >= 640) && (paramInt3 >= 480)) {
          k = 3;
        } else {
          k = 2;
        }
      }
      if ((paramInt3 <= 321) && (paramInt2 <= 570)) {
        j = 0;
      } else {
        j = 1;
      }
      if (paramInt2 * 3 / 5 >= paramInt3 - 1) {
        paramInt2 = 1;
      } else {
        paramInt2 = i;
      }
    }
    paramInt3 = paramInt1;
    if (paramInt2 == 0) {
      paramInt3 = paramInt1 & 0xFFFFFFCF | 0x10;
    }
    paramInt1 = paramInt3;
    if (j != 0) {
      paramInt1 = paramInt3 | 0x10000000;
    }
    paramInt2 = paramInt1;
    if (k < (paramInt1 & 0xF)) {
      paramInt2 = paramInt1 & 0xFFFFFFF0 | k;
    }
    return paramInt2;
  }
  
  public static int resetScreenLayout(int paramInt)
  {
    return 0xEFFFFFC0 & paramInt | 0x24;
  }
  
  public static String resourceQualifierString(Configuration paramConfiguration)
  {
    return resourceQualifierString(paramConfiguration, null);
  }
  
  public static String resourceQualifierString(Configuration paramConfiguration, DisplayMetrics paramDisplayMetrics)
  {
    ArrayList localArrayList = new ArrayList();
    Object localObject;
    if (mcc != 0)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("mcc");
      ((StringBuilder)localObject).append(mcc);
      localArrayList.add(((StringBuilder)localObject).toString());
      if (mnc != 0)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("mnc");
        ((StringBuilder)localObject).append(mnc);
        localArrayList.add(((StringBuilder)localObject).toString());
      }
    }
    if (!mLocaleList.isEmpty())
    {
      localObject = localesToResourceQualifier(mLocaleList);
      if (!((String)localObject).isEmpty()) {
        localArrayList.add(localObject);
      }
    }
    int i = screenLayout & 0xC0;
    if (i != 64)
    {
      if (i == 128) {
        localArrayList.add("ldrtl");
      }
    }
    else {
      localArrayList.add("ldltr");
    }
    if (smallestScreenWidthDp != 0)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("sw");
      ((StringBuilder)localObject).append(smallestScreenWidthDp);
      ((StringBuilder)localObject).append("dp");
      localArrayList.add(((StringBuilder)localObject).toString());
    }
    if (screenWidthDp != 0)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("w");
      ((StringBuilder)localObject).append(screenWidthDp);
      ((StringBuilder)localObject).append("dp");
      localArrayList.add(((StringBuilder)localObject).toString());
    }
    if (screenHeightDp != 0)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("h");
      ((StringBuilder)localObject).append(screenHeightDp);
      ((StringBuilder)localObject).append("dp");
      localArrayList.add(((StringBuilder)localObject).toString());
    }
    switch (screenLayout & 0xF)
    {
    default: 
      break;
    case 4: 
      localArrayList.add("xlarge");
      break;
    case 3: 
      localArrayList.add("large");
      break;
    case 2: 
      localArrayList.add("normal");
      break;
    case 1: 
      localArrayList.add("small");
    }
    i = screenLayout & 0x30;
    if (i != 16)
    {
      if (i == 32) {
        localArrayList.add("long");
      }
    }
    else {
      localArrayList.add("notlong");
    }
    i = screenLayout & 0x300;
    if (i != 256)
    {
      if (i == 512) {
        localArrayList.add("round");
      }
    }
    else {
      localArrayList.add("notround");
    }
    i = colorMode & 0xC;
    if (i != 4)
    {
      if (i == 8) {
        localArrayList.add("highdr");
      }
    }
    else {
      localArrayList.add("lowdr");
    }
    switch (colorMode & 0x3)
    {
    default: 
      break;
    case 2: 
      localArrayList.add("widecg");
      break;
    case 1: 
      localArrayList.add("nowidecg");
    }
    switch (orientation)
    {
    default: 
      break;
    case 2: 
      localArrayList.add("land");
      break;
    case 1: 
      localArrayList.add("port");
    }
    switch (uiMode & 0xF)
    {
    default: 
      break;
    case 7: 
      localArrayList.add("vrheadset");
      break;
    case 6: 
      localArrayList.add("watch");
      break;
    case 5: 
      localArrayList.add("appliance");
      break;
    case 4: 
      localArrayList.add("television");
      break;
    case 3: 
      localArrayList.add("car");
      break;
    case 2: 
      localArrayList.add("desk");
    }
    i = uiMode & 0x30;
    if (i != 16)
    {
      if (i == 32) {
        localArrayList.add("night");
      }
    }
    else {
      localArrayList.add("notnight");
    }
    i = densityDpi;
    if (i != 0) {
      if (i != 120)
      {
        if (i != 160)
        {
          if (i != 213)
          {
            if (i != 240)
            {
              if (i != 320)
              {
                if (i != 480)
                {
                  if (i != 640) {
                    switch (i)
                    {
                    default: 
                      localObject = new StringBuilder();
                      ((StringBuilder)localObject).append(densityDpi);
                      ((StringBuilder)localObject).append("dpi");
                      localArrayList.add(((StringBuilder)localObject).toString());
                      break;
                    case 65535: 
                      localArrayList.add("nodpi");
                      break;
                    case 65534: 
                      localArrayList.add("anydpi");
                      break;
                    }
                  } else {
                    localArrayList.add("xxxhdpi");
                  }
                }
                else {
                  localArrayList.add("xxhdpi");
                }
              }
              else {
                localArrayList.add("xhdpi");
              }
            }
            else {
              localArrayList.add("hdpi");
            }
          }
          else {
            localArrayList.add("tvdpi");
          }
        }
        else {
          localArrayList.add("mdpi");
        }
      }
      else {
        localArrayList.add("ldpi");
      }
    }
    i = touchscreen;
    if (i != 1)
    {
      if (i == 3) {
        localArrayList.add("finger");
      }
    }
    else {
      localArrayList.add("notouch");
    }
    switch (keyboardHidden)
    {
    default: 
      break;
    case 3: 
      localArrayList.add("keyssoft");
      break;
    case 2: 
      localArrayList.add("keyshidden");
      break;
    case 1: 
      localArrayList.add("keysexposed");
    }
    switch (keyboard)
    {
    default: 
      break;
    case 3: 
      localArrayList.add("12key");
      break;
    case 2: 
      localArrayList.add("qwerty");
      break;
    case 1: 
      localArrayList.add("nokeys");
    }
    switch (navigationHidden)
    {
    default: 
      break;
    case 2: 
      localArrayList.add("navhidden");
      break;
    case 1: 
      localArrayList.add("navexposed");
    }
    switch (navigation)
    {
    default: 
      break;
    case 4: 
      localArrayList.add("wheel");
      break;
    case 3: 
      localArrayList.add("trackball");
      break;
    case 2: 
      localArrayList.add("dpad");
      break;
    case 1: 
      localArrayList.add("nonav");
    }
    if (paramDisplayMetrics != null)
    {
      int j;
      if (widthPixels >= heightPixels)
      {
        i = widthPixels;
        j = heightPixels;
      }
      else
      {
        i = heightPixels;
        j = widthPixels;
      }
      paramConfiguration = new StringBuilder();
      paramConfiguration.append(i);
      paramConfiguration.append("x");
      paramConfiguration.append(j);
      localArrayList.add(paramConfiguration.toString());
    }
    paramConfiguration = new StringBuilder();
    paramConfiguration.append("v");
    paramConfiguration.append(Build.VERSION.RESOURCES_SDK_INT);
    localArrayList.add(paramConfiguration.toString());
    return TextUtils.join("-", localArrayList);
  }
  
  public static String uiModeToString(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return Integer.toString(paramInt);
    case 7: 
      return "UI_MODE_TYPE_VR_HEADSET";
    case 6: 
      return "UI_MODE_TYPE_WATCH";
    case 5: 
      return "UI_MODE_TYPE_APPLIANCE";
    case 4: 
      return "UI_MODE_TYPE_TELEVISION";
    case 3: 
      return "UI_MODE_TYPE_CAR";
    case 2: 
      return "UI_MODE_TYPE_DESK";
    case 1: 
      return "UI_MODE_TYPE_NORMAL";
    }
    return "UI_MODE_TYPE_UNDEFINED";
  }
  
  public static void writeXmlAttrs(XmlSerializer paramXmlSerializer, Configuration paramConfiguration)
    throws IOException
  {
    XmlUtils.writeIntAttribute(paramXmlSerializer, "fs", Float.floatToIntBits(fontScale));
    if (mcc != 0) {
      XmlUtils.writeIntAttribute(paramXmlSerializer, "mcc", mcc);
    }
    if (mnc != 0) {
      XmlUtils.writeIntAttribute(paramXmlSerializer, "mnc", mnc);
    }
    paramConfiguration.fixUpLocaleList();
    if (!mLocaleList.isEmpty()) {
      XmlUtils.writeStringAttribute(paramXmlSerializer, "locales", mLocaleList.toLanguageTags());
    }
    if (touchscreen != 0) {
      XmlUtils.writeIntAttribute(paramXmlSerializer, "touch", touchscreen);
    }
    if (keyboard != 0) {
      XmlUtils.writeIntAttribute(paramXmlSerializer, "key", keyboard);
    }
    if (keyboardHidden != 0) {
      XmlUtils.writeIntAttribute(paramXmlSerializer, "keyHid", keyboardHidden);
    }
    if (hardKeyboardHidden != 0) {
      XmlUtils.writeIntAttribute(paramXmlSerializer, "hardKeyHid", hardKeyboardHidden);
    }
    if (navigation != 0) {
      XmlUtils.writeIntAttribute(paramXmlSerializer, "nav", navigation);
    }
    if (navigationHidden != 0) {
      XmlUtils.writeIntAttribute(paramXmlSerializer, "navHid", navigationHidden);
    }
    if (orientation != 0) {
      XmlUtils.writeIntAttribute(paramXmlSerializer, "ori", orientation);
    }
    if (screenLayout != 0) {
      XmlUtils.writeIntAttribute(paramXmlSerializer, "scrLay", screenLayout);
    }
    if (colorMode != 0) {
      XmlUtils.writeIntAttribute(paramXmlSerializer, "clrMod", colorMode);
    }
    if (uiMode != 0) {
      XmlUtils.writeIntAttribute(paramXmlSerializer, "ui", uiMode);
    }
    if (screenWidthDp != 0) {
      XmlUtils.writeIntAttribute(paramXmlSerializer, "width", screenWidthDp);
    }
    if (screenHeightDp != 0) {
      XmlUtils.writeIntAttribute(paramXmlSerializer, "height", screenHeightDp);
    }
    if (smallestScreenWidthDp != 0) {
      XmlUtils.writeIntAttribute(paramXmlSerializer, "sw", smallestScreenWidthDp);
    }
    if (densityDpi != 0) {
      XmlUtils.writeIntAttribute(paramXmlSerializer, "density", densityDpi);
    }
  }
  
  public void clearLocales()
  {
    mLocaleList = LocaleList.getEmptyLocaleList();
    locale = null;
  }
  
  public int compareTo(Configuration paramConfiguration)
  {
    float f1 = fontScale;
    float f2 = fontScale;
    if (f1 < f2) {
      return -1;
    }
    if (f1 > f2) {
      return 1;
    }
    int i = mcc - mcc;
    if (i != 0) {
      return i;
    }
    i = mnc - mnc;
    if (i != 0) {
      return i;
    }
    fixUpLocaleList();
    paramConfiguration.fixUpLocaleList();
    if (mLocaleList.isEmpty())
    {
      if (!mLocaleList.isEmpty()) {
        return 1;
      }
    }
    else
    {
      if (mLocaleList.isEmpty()) {
        return -1;
      }
      int j = Math.min(mLocaleList.size(), mLocaleList.size());
      for (i = 0; i < j; i++)
      {
        Locale localLocale1 = mLocaleList.get(i);
        Locale localLocale2 = mLocaleList.get(i);
        int k = localLocale1.getLanguage().compareTo(localLocale2.getLanguage());
        if (k != 0) {
          return k;
        }
        k = localLocale1.getCountry().compareTo(localLocale2.getCountry());
        if (k != 0) {
          return k;
        }
        k = localLocale1.getVariant().compareTo(localLocale2.getVariant());
        if (k != 0) {
          return k;
        }
        k = localLocale1.toLanguageTag().compareTo(localLocale2.toLanguageTag());
        if (k != 0) {
          return k;
        }
      }
      i = mLocaleList.size() - mLocaleList.size();
      if (i != 0) {
        return i;
      }
    }
    i = touchscreen - touchscreen;
    if (i != 0) {
      return i;
    }
    i = keyboard - keyboard;
    if (i != 0) {
      return i;
    }
    i = keyboardHidden - keyboardHidden;
    if (i != 0) {
      return i;
    }
    i = hardKeyboardHidden - hardKeyboardHidden;
    if (i != 0) {
      return i;
    }
    i = navigation - navigation;
    if (i != 0) {
      return i;
    }
    i = navigationHidden - navigationHidden;
    if (i != 0) {
      return i;
    }
    i = orientation - orientation;
    if (i != 0) {
      return i;
    }
    i = colorMode - colorMode;
    if (i != 0) {
      return i;
    }
    i = screenLayout - screenLayout;
    if (i != 0) {
      return i;
    }
    i = uiMode - uiMode;
    if (i != 0) {
      return i;
    }
    i = screenWidthDp - screenWidthDp;
    if (i != 0) {
      return i;
    }
    i = screenHeightDp - screenHeightDp;
    if (i != 0) {
      return i;
    }
    i = smallestScreenWidthDp - smallestScreenWidthDp;
    if (i != 0) {
      return i;
    }
    i = densityDpi - densityDpi;
    if (i != 0) {
      return i;
    }
    f2 = FlipFont;
    f1 = FlipFont;
    if (f2 < f1) {
      return -1;
    }
    if (f2 > f1) {
      return 1;
    }
    i = assetsSeq - assetsSeq;
    if (i != 0) {
      return i;
    }
    i = windowConfiguration.compareTo(windowConfiguration);
    if (i != 0) {
      return i;
    }
    return i;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int diff(Configuration paramConfiguration)
  {
    return diff(paramConfiguration, false, false);
  }
  
  public int diff(Configuration paramConfiguration, boolean paramBoolean1, boolean paramBoolean2)
  {
    int i = 0;
    if (!paramBoolean1)
    {
      j = i;
      if (fontScale <= 0.0F) {}
    }
    else
    {
      j = i;
      if (fontScale != fontScale) {
        j = 0x0 | 0x40000000;
      }
    }
    if (!paramBoolean1)
    {
      i = j;
      if (mcc == 0) {}
    }
    else
    {
      i = j;
      if (mcc != mcc) {
        i = j | 0x1;
      }
    }
    if (!paramBoolean1)
    {
      j = i;
      if (mnc == 0) {}
    }
    else
    {
      j = i;
      if (mnc != mnc) {
        j = i | 0x2;
      }
    }
    fixUpLocaleList();
    paramConfiguration.fixUpLocaleList();
    if (!paramBoolean1)
    {
      i = j;
      if (mLocaleList.isEmpty()) {}
    }
    else
    {
      i = j;
      if (!mLocaleList.equals(mLocaleList)) {
        i = j | 0x4 | 0x2000;
      }
    }
    int k = screenLayout & 0xC0;
    if (!paramBoolean1)
    {
      j = i;
      if (k == 0) {}
    }
    else
    {
      j = i;
      if (k != (screenLayout & 0xC0)) {
        j = i | 0x2000;
      }
    }
    if (!paramBoolean1)
    {
      i = j;
      if (touchscreen == 0) {}
    }
    else
    {
      i = j;
      if (touchscreen != touchscreen) {
        i = j | 0x8;
      }
    }
    if (!paramBoolean1)
    {
      j = i;
      if (keyboard == 0) {}
    }
    else
    {
      j = i;
      if (keyboard != keyboard) {
        j = i | 0x10;
      }
    }
    if (!paramBoolean1)
    {
      i = j;
      if (keyboardHidden == 0) {}
    }
    else
    {
      i = j;
      if (keyboardHidden != keyboardHidden) {
        i = j | 0x20;
      }
    }
    if (!paramBoolean1)
    {
      k = i;
      if (hardKeyboardHidden == 0) {}
    }
    else
    {
      k = i;
      if (hardKeyboardHidden != hardKeyboardHidden) {
        k = i | 0x20;
      }
    }
    if (!paramBoolean1)
    {
      j = k;
      if (navigation == 0) {}
    }
    else
    {
      j = k;
      if (navigation != navigation) {
        j = k | 0x40;
      }
    }
    if (!paramBoolean1)
    {
      i = j;
      if (navigationHidden == 0) {}
    }
    else
    {
      i = j;
      if (navigationHidden != navigationHidden) {
        i = j | 0x20;
      }
    }
    if (!paramBoolean1)
    {
      j = i;
      if (orientation == 0) {}
    }
    else
    {
      j = i;
      if (orientation != orientation) {
        j = i | 0x80;
      }
    }
    if (!paramBoolean1)
    {
      i = j;
      if (getScreenLayoutNoDirection(screenLayout) == 0) {}
    }
    else
    {
      i = j;
      if (getScreenLayoutNoDirection(screenLayout) != getScreenLayoutNoDirection(screenLayout)) {
        i = j | 0x100;
      }
    }
    if (!paramBoolean1)
    {
      j = i;
      if ((colorMode & 0xC) == 0) {}
    }
    else
    {
      j = i;
      if ((colorMode & 0xC) != (colorMode & 0xC)) {
        j = i | 0x4000;
      }
    }
    if (!paramBoolean1)
    {
      i = j;
      if ((colorMode & 0x3) == 0) {}
    }
    else
    {
      i = j;
      if ((colorMode & 0x3) != (colorMode & 0x3)) {
        i = j | 0x4000;
      }
    }
    if (!paramBoolean1)
    {
      j = i;
      if (uiMode == 0) {}
    }
    else
    {
      j = i;
      if (uiMode != uiMode) {
        j = i | 0x200;
      }
    }
    if (!paramBoolean1)
    {
      i = j;
      if (screenWidthDp == 0) {}
    }
    else
    {
      i = j;
      if (screenWidthDp != screenWidthDp) {
        i = j | 0x400;
      }
    }
    if (!paramBoolean1)
    {
      k = i;
      if (screenHeightDp == 0) {}
    }
    else
    {
      k = i;
      if (screenHeightDp != screenHeightDp) {
        k = i | 0x400;
      }
    }
    if (!paramBoolean1)
    {
      j = k;
      if (smallestScreenWidthDp == 0) {}
    }
    else
    {
      j = k;
      if (smallestScreenWidthDp != smallestScreenWidthDp) {
        j = k | 0x800;
      }
    }
    if (!paramBoolean1)
    {
      i = j;
      if (densityDpi == 0) {}
    }
    else
    {
      i = j;
      if (densityDpi != densityDpi) {
        i = j | 0x1000;
      }
    }
    if (!paramBoolean1)
    {
      k = i;
      if (assetsSeq == 0) {}
    }
    else
    {
      k = i;
      if (assetsSeq != assetsSeq) {
        k = i | 0x80000000;
      }
    }
    int j = k;
    if (!paramBoolean2)
    {
      j = k;
      if (windowConfiguration.diff(windowConfiguration, paramBoolean1) != 0L) {
        j = k | 0x20000000;
      }
    }
    i = j;
    if (FlipFont > 0)
    {
      i = j;
      if (FlipFont != FlipFont) {
        i = j | 0x10000000;
      }
    }
    return i;
  }
  
  public int diffPublicOnly(Configuration paramConfiguration)
  {
    return diff(paramConfiguration, false, true);
  }
  
  public boolean equals(Configuration paramConfiguration)
  {
    boolean bool = false;
    if (paramConfiguration == null) {
      return false;
    }
    if (paramConfiguration == this) {
      return true;
    }
    if (compareTo(paramConfiguration) == 0) {
      bool = true;
    }
    return bool;
  }
  
  public boolean equals(Object paramObject)
  {
    try
    {
      boolean bool = equals((Configuration)paramObject);
      return bool;
    }
    catch (ClassCastException paramObject) {}
    return false;
  }
  
  public int getLayoutDirection()
  {
    int i;
    if ((screenLayout & 0xC0) == 128) {
      i = 1;
    } else {
      i = 0;
    }
    return i;
  }
  
  public LocaleList getLocales()
  {
    fixUpLocaleList();
    return mLocaleList;
  }
  
  public int hashCode()
  {
    return 31 * (31 * (31 * (31 * (31 * (31 * (31 * (31 * (31 * (31 * (31 * (31 * (31 * (31 * (31 * (31 * (31 * (31 * (31 * (31 * 17 + Float.floatToIntBits(fontScale)) + mcc) + mnc) + mLocaleList.hashCode()) + touchscreen) + keyboard) + keyboardHidden) + hardKeyboardHidden) + navigation) + navigationHidden) + orientation) + screenLayout) + colorMode) + uiMode) + screenWidthDp) + screenHeightDp) + smallestScreenWidthDp) + densityDpi) + assetsSeq) + FlipFont;
  }
  
  public boolean isLayoutSizeAtLeast(int paramInt)
  {
    int i = screenLayout & 0xF;
    boolean bool = false;
    if (i == 0) {
      return false;
    }
    if (i >= paramInt) {
      bool = true;
    }
    return bool;
  }
  
  public boolean isOtherSeqNewer(Configuration paramConfiguration)
  {
    boolean bool = false;
    if (paramConfiguration == null) {
      return false;
    }
    if (seq == 0) {
      return true;
    }
    if (seq == 0) {
      return true;
    }
    int i = seq - seq;
    if (i > 65536) {
      return false;
    }
    if (i > 0) {
      bool = true;
    }
    return bool;
  }
  
  public boolean isScreenHdr()
  {
    boolean bool;
    if ((colorMode & 0xC) == 8) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isScreenRound()
  {
    boolean bool;
    if ((screenLayout & 0x300) == 512) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isScreenWideColorGamut()
  {
    boolean bool;
    if ((colorMode & 0x3) == 2) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  @Deprecated
  public void makeDefault()
  {
    setToDefaults();
  }
  
  public void readFromParcel(Parcel paramParcel)
  {
    fontScale = paramParcel.readFloat();
    mcc = paramParcel.readInt();
    mnc = paramParcel.readInt();
    mLocaleList = ((LocaleList)paramParcel.readParcelable(LocaleList.class.getClassLoader()));
    LocaleList localLocaleList = mLocaleList;
    boolean bool = false;
    locale = localLocaleList.get(0);
    if (paramParcel.readInt() == 1) {
      bool = true;
    }
    userSetLocale = bool;
    touchscreen = paramParcel.readInt();
    keyboard = paramParcel.readInt();
    keyboardHidden = paramParcel.readInt();
    hardKeyboardHidden = paramParcel.readInt();
    navigation = paramParcel.readInt();
    navigationHidden = paramParcel.readInt();
    orientation = paramParcel.readInt();
    screenLayout = paramParcel.readInt();
    colorMode = paramParcel.readInt();
    uiMode = paramParcel.readInt();
    screenWidthDp = paramParcel.readInt();
    screenHeightDp = paramParcel.readInt();
    smallestScreenWidthDp = paramParcel.readInt();
    densityDpi = paramParcel.readInt();
    compatScreenWidthDp = paramParcel.readInt();
    compatScreenHeightDp = paramParcel.readInt();
    compatSmallestScreenWidthDp = paramParcel.readInt();
    windowConfiguration.setTo((WindowConfiguration)paramParcel.readValue(null));
    assetsSeq = paramParcel.readInt();
    seq = paramParcel.readInt();
    FlipFont = paramParcel.readInt();
  }
  
  public void setLayoutDirection(Locale paramLocale)
  {
    int i = TextUtils.getLayoutDirectionFromLocale(paramLocale);
    screenLayout = (screenLayout & 0xFF3F | 1 + i << 6);
  }
  
  public void setLocale(Locale paramLocale)
  {
    if (paramLocale == null) {
      paramLocale = LocaleList.getEmptyLocaleList();
    } else {
      paramLocale = new LocaleList(new Locale[] { paramLocale });
    }
    setLocales(paramLocale);
  }
  
  public void setLocales(LocaleList paramLocaleList)
  {
    if (paramLocaleList == null) {
      paramLocaleList = LocaleList.getEmptyLocaleList();
    }
    mLocaleList = paramLocaleList;
    locale = mLocaleList.get(0);
    setLayoutDirection(locale);
  }
  
  public void setTo(Configuration paramConfiguration)
  {
    fontScale = fontScale;
    mcc = mcc;
    mnc = mnc;
    Locale localLocale;
    if (locale == null) {
      localLocale = null;
    } else {
      localLocale = (Locale)locale.clone();
    }
    locale = localLocale;
    paramConfiguration.fixUpLocaleList();
    mLocaleList = mLocaleList;
    userSetLocale = userSetLocale;
    touchscreen = touchscreen;
    keyboard = keyboard;
    keyboardHidden = keyboardHidden;
    hardKeyboardHidden = hardKeyboardHidden;
    navigation = navigation;
    navigationHidden = navigationHidden;
    orientation = orientation;
    screenLayout = screenLayout;
    colorMode = colorMode;
    uiMode = uiMode;
    screenWidthDp = screenWidthDp;
    screenHeightDp = screenHeightDp;
    smallestScreenWidthDp = smallestScreenWidthDp;
    densityDpi = densityDpi;
    compatScreenWidthDp = compatScreenWidthDp;
    compatScreenHeightDp = compatScreenHeightDp;
    compatSmallestScreenWidthDp = compatSmallestScreenWidthDp;
    assetsSeq = assetsSeq;
    seq = seq;
    FlipFont = FlipFont;
    windowConfiguration.setTo(windowConfiguration);
  }
  
  public void setToDefaults()
  {
    fontScale = 1.0F;
    mnc = 0;
    mcc = 0;
    mLocaleList = LocaleList.getEmptyLocaleList();
    locale = null;
    userSetLocale = false;
    touchscreen = 0;
    keyboard = 0;
    keyboardHidden = 0;
    hardKeyboardHidden = 0;
    navigation = 0;
    navigationHidden = 0;
    orientation = 0;
    screenLayout = 0;
    colorMode = 0;
    uiMode = 0;
    compatScreenWidthDp = 0;
    screenWidthDp = 0;
    compatScreenHeightDp = 0;
    screenHeightDp = 0;
    compatSmallestScreenWidthDp = 0;
    smallestScreenWidthDp = 0;
    densityDpi = 0;
    assetsSeq = 0;
    seq = 0;
    FlipFont = 0;
    windowConfiguration.setToDefaults();
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder(128);
    localStringBuilder.append("{");
    localStringBuilder.append(FlipFont);
    localStringBuilder.append(" ");
    localStringBuilder.append(fontScale);
    localStringBuilder.append(" ");
    if (mcc != 0)
    {
      localStringBuilder.append(mcc);
      localStringBuilder.append("mcc");
    }
    else
    {
      localStringBuilder.append("?mcc");
    }
    if (mnc != 0)
    {
      localStringBuilder.append(mnc);
      localStringBuilder.append("mnc");
    }
    else
    {
      localStringBuilder.append("?mnc");
    }
    fixUpLocaleList();
    if (!mLocaleList.isEmpty())
    {
      localStringBuilder.append(" ");
      localStringBuilder.append(mLocaleList);
    }
    else
    {
      localStringBuilder.append(" ?localeList");
    }
    int i = screenLayout & 0xC0;
    if (i != 0)
    {
      if (i != 64)
      {
        if (i != 128)
        {
          localStringBuilder.append(" layoutDir=");
          localStringBuilder.append(i >> 6);
        }
        else
        {
          localStringBuilder.append(" ldrtl");
        }
      }
      else {
        localStringBuilder.append(" ldltr");
      }
    }
    else {
      localStringBuilder.append(" ?layoutDir");
    }
    if (smallestScreenWidthDp != 0)
    {
      localStringBuilder.append(" sw");
      localStringBuilder.append(smallestScreenWidthDp);
      localStringBuilder.append("dp");
    }
    else
    {
      localStringBuilder.append(" ?swdp");
    }
    if (screenWidthDp != 0)
    {
      localStringBuilder.append(" w");
      localStringBuilder.append(screenWidthDp);
      localStringBuilder.append("dp");
    }
    else
    {
      localStringBuilder.append(" ?wdp");
    }
    if (screenHeightDp != 0)
    {
      localStringBuilder.append(" h");
      localStringBuilder.append(screenHeightDp);
      localStringBuilder.append("dp");
    }
    else
    {
      localStringBuilder.append(" ?hdp");
    }
    if (densityDpi != 0)
    {
      localStringBuilder.append(" ");
      localStringBuilder.append(densityDpi);
      localStringBuilder.append("dpi");
    }
    else
    {
      localStringBuilder.append(" ?density");
    }
    switch (screenLayout & 0xF)
    {
    default: 
      localStringBuilder.append(" layoutSize=");
      localStringBuilder.append(screenLayout & 0xF);
      break;
    case 4: 
      localStringBuilder.append(" xlrg");
      break;
    case 3: 
      localStringBuilder.append(" lrg");
      break;
    case 2: 
      localStringBuilder.append(" nrml");
      break;
    case 1: 
      localStringBuilder.append(" smll");
      break;
    case 0: 
      localStringBuilder.append(" ?lsize");
    }
    i = screenLayout & 0x30;
    if (i != 0)
    {
      if (i != 16) {
        if (i != 32)
        {
          localStringBuilder.append(" layoutLong=");
          localStringBuilder.append(screenLayout & 0x30);
        }
        else
        {
          localStringBuilder.append(" long");
        }
      }
    }
    else {
      localStringBuilder.append(" ?long");
    }
    i = colorMode & 0xC;
    if (i != 0)
    {
      if (i != 4) {
        if (i != 8)
        {
          localStringBuilder.append(" dynamicRange=");
          localStringBuilder.append(colorMode & 0xC);
        }
        else
        {
          localStringBuilder.append(" hdr");
        }
      }
    }
    else {
      localStringBuilder.append(" ?ldr");
    }
    switch (colorMode & 0x3)
    {
    default: 
      localStringBuilder.append(" wideColorGamut=");
      localStringBuilder.append(colorMode & 0x3);
      break;
    case 2: 
      localStringBuilder.append(" widecg");
      break;
    case 1: 
      break;
    case 0: 
      localStringBuilder.append(" ?wideColorGamut");
    }
    switch (orientation)
    {
    default: 
      localStringBuilder.append(" orien=");
      localStringBuilder.append(orientation);
      break;
    case 2: 
      localStringBuilder.append(" land");
      break;
    case 1: 
      localStringBuilder.append(" port");
      break;
    case 0: 
      localStringBuilder.append(" ?orien");
    }
    switch (uiMode & 0xF)
    {
    default: 
      localStringBuilder.append(" uimode=");
      localStringBuilder.append(uiMode & 0xF);
      break;
    case 7: 
      localStringBuilder.append(" vrheadset");
      break;
    case 6: 
      localStringBuilder.append(" watch");
      break;
    case 5: 
      localStringBuilder.append(" appliance");
      break;
    case 4: 
      localStringBuilder.append(" television");
      break;
    case 3: 
      localStringBuilder.append(" car");
      break;
    case 2: 
      localStringBuilder.append(" desk");
      break;
    case 1: 
      break;
    case 0: 
      localStringBuilder.append(" ?uimode");
    }
    i = uiMode & 0x30;
    if (i != 0)
    {
      if (i != 16) {
        if (i != 32)
        {
          localStringBuilder.append(" night=");
          localStringBuilder.append(uiMode & 0x30);
        }
        else
        {
          localStringBuilder.append(" night");
        }
      }
    }
    else {
      localStringBuilder.append(" ?night");
    }
    switch (touchscreen)
    {
    default: 
      localStringBuilder.append(" touch=");
      localStringBuilder.append(touchscreen);
      break;
    case 3: 
      localStringBuilder.append(" finger");
      break;
    case 2: 
      localStringBuilder.append(" stylus");
      break;
    case 1: 
      localStringBuilder.append(" -touch");
      break;
    case 0: 
      localStringBuilder.append(" ?touch");
    }
    switch (keyboard)
    {
    default: 
      localStringBuilder.append(" keys=");
      localStringBuilder.append(keyboard);
      break;
    case 3: 
      localStringBuilder.append(" 12key");
      break;
    case 2: 
      localStringBuilder.append(" qwerty");
      break;
    case 1: 
      localStringBuilder.append(" -keyb");
      break;
    case 0: 
      localStringBuilder.append(" ?keyb");
    }
    switch (keyboardHidden)
    {
    default: 
      localStringBuilder.append("/");
      localStringBuilder.append(keyboardHidden);
      break;
    case 3: 
      localStringBuilder.append("/s");
      break;
    case 2: 
      localStringBuilder.append("/h");
      break;
    case 1: 
      localStringBuilder.append("/v");
      break;
    case 0: 
      localStringBuilder.append("/?");
    }
    switch (hardKeyboardHidden)
    {
    default: 
      localStringBuilder.append("/");
      localStringBuilder.append(hardKeyboardHidden);
      break;
    case 2: 
      localStringBuilder.append("/h");
      break;
    case 1: 
      localStringBuilder.append("/v");
      break;
    case 0: 
      localStringBuilder.append("/?");
    }
    switch (navigation)
    {
    default: 
      localStringBuilder.append(" nav=");
      localStringBuilder.append(navigation);
      break;
    case 4: 
      localStringBuilder.append(" wheel");
      break;
    case 3: 
      localStringBuilder.append(" tball");
      break;
    case 2: 
      localStringBuilder.append(" dpad");
      break;
    case 1: 
      localStringBuilder.append(" -nav");
      break;
    case 0: 
      localStringBuilder.append(" ?nav");
    }
    switch (navigationHidden)
    {
    default: 
      localStringBuilder.append("/");
      localStringBuilder.append(navigationHidden);
      break;
    case 2: 
      localStringBuilder.append("/h");
      break;
    case 1: 
      localStringBuilder.append("/v");
      break;
    case 0: 
      localStringBuilder.append("/?");
    }
    localStringBuilder.append(" winConfig=");
    localStringBuilder.append(windowConfiguration);
    if (assetsSeq != 0)
    {
      localStringBuilder.append(" as.");
      localStringBuilder.append(assetsSeq);
    }
    if (seq != 0)
    {
      localStringBuilder.append(" s.");
      localStringBuilder.append(seq);
    }
    localStringBuilder.append('}');
    return localStringBuilder.toString();
  }
  
  public void unset()
  {
    setToDefaults();
    fontScale = 0.0F;
  }
  
  public int updateFrom(Configuration paramConfiguration)
  {
    int i = 0;
    int j = i;
    if (fontScale > 0.0F)
    {
      j = i;
      if (fontScale != fontScale)
      {
        j = 0x0 | 0x40000000;
        fontScale = fontScale;
      }
    }
    i = j;
    if (mcc != 0)
    {
      i = j;
      if (mcc != mcc)
      {
        i = j | 0x1;
        mcc = mcc;
      }
    }
    j = i;
    if (mnc != 0)
    {
      j = i;
      if (mnc != mnc)
      {
        j = i | 0x2;
        mnc = mnc;
      }
    }
    fixUpLocaleList();
    paramConfiguration.fixUpLocaleList();
    i = j;
    if (!mLocaleList.isEmpty())
    {
      i = j;
      if (!mLocaleList.equals(mLocaleList))
      {
        j |= 0x4;
        mLocaleList = mLocaleList;
        i = j;
        if (!locale.equals(locale))
        {
          locale = ((Locale)locale.clone());
          i = j | 0x2000;
          setLayoutDirection(locale);
        }
      }
    }
    int k = screenLayout & 0xC0;
    j = i;
    if (k != 0)
    {
      j = i;
      if (k != (screenLayout & 0xC0))
      {
        screenLayout = (screenLayout & 0xFF3F | k);
        j = i | 0x2000;
      }
    }
    k = j;
    if (userSetLocale) {
      if (userSetLocale)
      {
        k = j;
        if ((j & 0x4) == 0) {}
      }
      else
      {
        k = j | 0x4;
        userSetLocale = true;
      }
    }
    i = k;
    if (touchscreen != 0)
    {
      i = k;
      if (touchscreen != touchscreen)
      {
        i = k | 0x8;
        touchscreen = touchscreen;
      }
    }
    j = i;
    if (keyboard != 0)
    {
      j = i;
      if (keyboard != keyboard)
      {
        j = i | 0x10;
        keyboard = keyboard;
      }
    }
    i = j;
    if (keyboardHidden != 0)
    {
      i = j;
      if (keyboardHidden != keyboardHidden)
      {
        i = j | 0x20;
        keyboardHidden = keyboardHidden;
      }
    }
    j = i;
    if (hardKeyboardHidden != 0)
    {
      j = i;
      if (hardKeyboardHidden != hardKeyboardHidden)
      {
        j = i | 0x20;
        hardKeyboardHidden = hardKeyboardHidden;
      }
    }
    i = j;
    if (navigation != 0)
    {
      i = j;
      if (navigation != navigation)
      {
        i = j | 0x40;
        navigation = navigation;
      }
    }
    j = i;
    if (navigationHidden != 0)
    {
      j = i;
      if (navigationHidden != navigationHidden)
      {
        j = i | 0x20;
        navigationHidden = navigationHidden;
      }
    }
    k = j;
    if (orientation != 0)
    {
      k = j;
      if (orientation != orientation)
      {
        k = j | 0x80;
        orientation = orientation;
      }
    }
    i = k;
    if ((screenLayout & 0xF) != 0)
    {
      i = k;
      if ((screenLayout & 0xF) != (screenLayout & 0xF))
      {
        i = k | 0x100;
        screenLayout = (screenLayout & 0xFFFFFFF0 | screenLayout & 0xF);
      }
    }
    j = i;
    if ((screenLayout & 0x30) != 0)
    {
      j = i;
      if ((screenLayout & 0x30) != (screenLayout & 0x30))
      {
        j = i | 0x100;
        screenLayout = (screenLayout & 0xFFFFFFCF | screenLayout & 0x30);
      }
    }
    i = j;
    if ((screenLayout & 0x300) != 0)
    {
      i = j;
      if ((screenLayout & 0x300) != (screenLayout & 0x300))
      {
        i = j | 0x100;
        screenLayout = (screenLayout & 0xFCFF | screenLayout & 0x300);
      }
    }
    j = i;
    if ((screenLayout & 0x10000000) != (screenLayout & 0x10000000))
    {
      j = i;
      if (screenLayout != 0)
      {
        j = i | 0x100;
        screenLayout = (screenLayout & 0xEFFFFFFF | screenLayout & 0x10000000);
      }
    }
    i = j;
    if ((colorMode & 0x3) != 0)
    {
      i = j;
      if ((colorMode & 0x3) != (colorMode & 0x3))
      {
        i = j | 0x4000;
        colorMode = (colorMode & 0xFFFFFFFC | colorMode & 0x3);
      }
    }
    j = i;
    if ((colorMode & 0xC) != 0)
    {
      j = i;
      if ((colorMode & 0xC) != (colorMode & 0xC))
      {
        j = i | 0x4000;
        colorMode = (colorMode & 0xFFFFFFF3 | colorMode & 0xC);
      }
    }
    i = j;
    if (uiMode != 0)
    {
      i = j;
      if (uiMode != uiMode)
      {
        j |= 0x200;
        if ((uiMode & 0xF) != 0) {
          uiMode = (uiMode & 0xFFFFFFF0 | uiMode & 0xF);
        }
        i = j;
        if ((uiMode & 0x30) != 0)
        {
          uiMode = (uiMode & 0xFFFFFFCF | uiMode & 0x30);
          i = j;
        }
      }
    }
    j = i;
    if (screenWidthDp != 0)
    {
      j = i;
      if (screenWidthDp != screenWidthDp)
      {
        j = i | 0x400;
        screenWidthDp = screenWidthDp;
      }
    }
    i = j;
    if (screenHeightDp != 0)
    {
      i = j;
      if (screenHeightDp != screenHeightDp)
      {
        i = j | 0x400;
        screenHeightDp = screenHeightDp;
      }
    }
    j = i;
    if (smallestScreenWidthDp != 0)
    {
      j = i;
      if (smallestScreenWidthDp != smallestScreenWidthDp)
      {
        j = i | 0x800;
        smallestScreenWidthDp = smallestScreenWidthDp;
      }
    }
    i = j;
    if (densityDpi != 0)
    {
      i = j;
      if (densityDpi != densityDpi)
      {
        i = j | 0x1000;
        densityDpi = densityDpi;
      }
    }
    if (compatScreenWidthDp != 0) {
      compatScreenWidthDp = compatScreenWidthDp;
    }
    if (compatScreenHeightDp != 0) {
      compatScreenHeightDp = compatScreenHeightDp;
    }
    if (compatSmallestScreenWidthDp != 0) {
      compatSmallestScreenWidthDp = compatSmallestScreenWidthDp;
    }
    j = i;
    if (assetsSeq != 0)
    {
      j = i;
      if (assetsSeq != assetsSeq)
      {
        j = i | 0x80000000;
        assetsSeq = assetsSeq;
      }
    }
    if (seq != 0) {
      seq = seq;
    }
    i = j;
    if (windowConfiguration.updateFrom(windowConfiguration) != 0) {
      i = j | 0x20000000;
    }
    j = i;
    if (FlipFont > 0)
    {
      j = i;
      if (FlipFont != FlipFont)
      {
        j = i | 0x10000000;
        FlipFont = FlipFont;
      }
    }
    return j;
  }
  
  public void writeResConfigToProto(ProtoOutputStream paramProtoOutputStream, long paramLong, DisplayMetrics paramDisplayMetrics)
  {
    int i;
    int j;
    if (widthPixels >= heightPixels)
    {
      i = widthPixels;
      j = heightPixels;
    }
    else
    {
      i = heightPixels;
      j = widthPixels;
    }
    paramLong = paramProtoOutputStream.start(paramLong);
    writeToProto(paramProtoOutputStream, 1146756268033L);
    paramProtoOutputStream.write(1155346202626L, Build.VERSION.RESOURCES_SDK_INT);
    paramProtoOutputStream.write(1155346202627L, i);
    paramProtoOutputStream.write(1155346202628L, j);
    paramProtoOutputStream.end(paramLong);
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeFloat(fontScale);
    paramParcel.writeInt(mcc);
    paramParcel.writeInt(mnc);
    fixUpLocaleList();
    paramParcel.writeParcelable(mLocaleList, paramInt);
    if (userSetLocale) {
      paramParcel.writeInt(1);
    } else {
      paramParcel.writeInt(0);
    }
    paramParcel.writeInt(touchscreen);
    paramParcel.writeInt(keyboard);
    paramParcel.writeInt(keyboardHidden);
    paramParcel.writeInt(hardKeyboardHidden);
    paramParcel.writeInt(navigation);
    paramParcel.writeInt(navigationHidden);
    paramParcel.writeInt(orientation);
    paramParcel.writeInt(screenLayout);
    paramParcel.writeInt(colorMode);
    paramParcel.writeInt(uiMode);
    paramParcel.writeInt(screenWidthDp);
    paramParcel.writeInt(screenHeightDp);
    paramParcel.writeInt(smallestScreenWidthDp);
    paramParcel.writeInt(densityDpi);
    paramParcel.writeInt(compatScreenWidthDp);
    paramParcel.writeInt(compatScreenHeightDp);
    paramParcel.writeInt(compatSmallestScreenWidthDp);
    paramParcel.writeValue(windowConfiguration);
    paramParcel.writeInt(assetsSeq);
    paramParcel.writeInt(seq);
    paramParcel.writeInt(FlipFont);
  }
  
  public void writeToProto(ProtoOutputStream paramProtoOutputStream, long paramLong)
  {
    paramLong = paramProtoOutputStream.start(paramLong);
    paramProtoOutputStream.write(1108101562369L, fontScale);
    paramProtoOutputStream.write(1155346202626L, mcc);
    paramProtoOutputStream.write(1155346202627L, mnc);
    mLocaleList.writeToProto(paramProtoOutputStream, 2246267895812L);
    paramProtoOutputStream.write(1155346202629L, screenLayout);
    paramProtoOutputStream.write(1155346202630L, colorMode);
    paramProtoOutputStream.write(1155346202631L, touchscreen);
    paramProtoOutputStream.write(1155346202632L, keyboard);
    paramProtoOutputStream.write(1155346202633L, keyboardHidden);
    paramProtoOutputStream.write(1155346202634L, hardKeyboardHidden);
    paramProtoOutputStream.write(1155346202635L, navigation);
    paramProtoOutputStream.write(1155346202636L, navigationHidden);
    paramProtoOutputStream.write(1155346202637L, orientation);
    paramProtoOutputStream.write(1155346202638L, uiMode);
    paramProtoOutputStream.write(1155346202639L, screenWidthDp);
    paramProtoOutputStream.write(1155346202640L, screenHeightDp);
    paramProtoOutputStream.write(1155346202641L, smallestScreenWidthDp);
    paramProtoOutputStream.write(1155346202642L, densityDpi);
    windowConfiguration.writeToProto(paramProtoOutputStream, 1146756268051L);
    paramProtoOutputStream.end(paramLong);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface NativeConfig {}
}
