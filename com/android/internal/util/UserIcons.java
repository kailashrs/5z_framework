package com.android.internal.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;

public class UserIcons
{
  private static final int[] USER_ICON_COLORS = { 17170895, 17170896, 17170897, 17170898, 17170899, 17170900, 17170901, 17170902 };
  
  public UserIcons() {}
  
  public static Bitmap convertToBitmap(Drawable paramDrawable)
  {
    if (paramDrawable == null) {
      return null;
    }
    int i = paramDrawable.getIntrinsicWidth();
    int j = paramDrawable.getIntrinsicHeight();
    Bitmap localBitmap = Bitmap.createBitmap(i, j, Bitmap.Config.ARGB_8888);
    Canvas localCanvas = new Canvas(localBitmap);
    paramDrawable.setBounds(0, 0, i, j);
    paramDrawable.draw(localCanvas);
    return localBitmap;
  }
  
  public static Drawable getDefaultUserIcon(Resources paramResources, int paramInt, boolean paramBoolean)
  {
    int i;
    if (paramBoolean) {
      i = 17170904;
    } else {
      i = 17170903;
    }
    if (paramInt != 55536) {
      i = USER_ICON_COLORS[(paramInt % USER_ICON_COLORS.length)];
    }
    Drawable localDrawable = paramResources.getDrawable(17302528, null).mutate();
    localDrawable.setColorFilter(paramResources.getColor(i, null), PorterDuff.Mode.SRC_IN);
    localDrawable.setBounds(0, 0, localDrawable.getIntrinsicWidth(), localDrawable.getIntrinsicHeight());
    return localDrawable;
  }
}
