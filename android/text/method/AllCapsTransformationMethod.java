package android.text.method;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.LocaleList;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import java.util.Locale;

public class AllCapsTransformationMethod
  implements TransformationMethod2
{
  private static final String TAG = "AllCapsTransformationMethod";
  private boolean mEnabled;
  private Locale mLocale;
  
  public AllCapsTransformationMethod(Context paramContext)
  {
    mLocale = paramContext.getResources().getConfiguration().getLocales().get(0);
  }
  
  public CharSequence getTransformation(CharSequence paramCharSequence, View paramView)
  {
    if (!mEnabled)
    {
      Log.w("AllCapsTransformationMethod", "Caller did not enable length changes; not transforming text");
      return paramCharSequence;
    }
    if (paramCharSequence == null) {
      return null;
    }
    Locale localLocale = null;
    if ((paramView instanceof TextView)) {
      localLocale = ((TextView)paramView).getTextLocale();
    }
    paramView = localLocale;
    if (localLocale == null) {
      paramView = mLocale;
    }
    return TextUtils.toUpperCase(paramView, paramCharSequence, paramCharSequence instanceof Spanned);
  }
  
  public void onFocusChanged(View paramView, CharSequence paramCharSequence, boolean paramBoolean, int paramInt, Rect paramRect) {}
  
  public void setLengthChangesAllowed(boolean paramBoolean)
  {
    mEnabled = paramBoolean;
  }
}
