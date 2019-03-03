package android.net;

import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.graphics.drawable.Drawable;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Deprecated
public class NetworkBadging
{
  public static final int BADGING_4K = 30;
  public static final int BADGING_HD = 20;
  public static final int BADGING_NONE = 0;
  public static final int BADGING_SD = 10;
  
  private NetworkBadging() {}
  
  private static int getBadgedWifiSignalResource(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Invalid signal level: ");
      localStringBuilder.append(paramInt);
      throw new IllegalArgumentException(localStringBuilder.toString());
    case 4: 
      return 17303019;
    case 3: 
      return 17303018;
    case 2: 
      return 17303017;
    case 1: 
      return 17303016;
    }
    return 17303015;
  }
  
  public static Drawable getWifiIcon(int paramInt1, int paramInt2, Resources.Theme paramTheme)
  {
    return Resources.getSystem().getDrawable(getWifiSignalResource(paramInt1), paramTheme);
  }
  
  private static int getWifiSignalResource(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Invalid signal level: ");
      localStringBuilder.append(paramInt);
      throw new IllegalArgumentException(localStringBuilder.toString());
    case 4: 
      return 17303058;
    case 3: 
      return 17303057;
    case 2: 
      return 17303056;
    case 1: 
      return 17303055;
    }
    return 17303054;
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Badging {}
}
