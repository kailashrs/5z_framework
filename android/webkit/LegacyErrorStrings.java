package android.webkit;

import android.content.Context;
import android.util.Log;

class LegacyErrorStrings
{
  private static final String LOGTAG = "Http";
  
  private LegacyErrorStrings() {}
  
  private static int getResource(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Using generic message for unknown error code: ");
      localStringBuilder.append(paramInt);
      Log.w("Http", localStringBuilder.toString());
      return 17040092;
    case 0: 
      return 17040100;
    case -1: 
      return 17040092;
    case -2: 
      return 17040099;
    case -3: 
      return 17040105;
    case -4: 
      return 17040093;
    case -5: 
      return 17040101;
    case -6: 
      return 17040094;
    case -7: 
      return 17040098;
    case -8: 
      return 17040103;
    case -9: 
      return 17040102;
    case -10: 
      return 17039368;
    case -11: 
      return 17040095;
    case -12: 
      return 17039367;
    case -13: 
      return 17040096;
    case -14: 
      return 17040097;
    }
    return 17040104;
  }
  
  static String getString(int paramInt, Context paramContext)
  {
    return paramContext.getText(getResource(paramInt)).toString();
  }
}
