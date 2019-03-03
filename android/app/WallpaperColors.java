package android.app;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Size;
import com.android.internal.graphics.ColorUtils;
import com.android.internal.graphics.palette.Palette;
import com.android.internal.graphics.palette.Palette.Builder;
import com.android.internal.graphics.palette.Palette.Swatch;
import com.android.internal.graphics.palette.VariationalKMeansQuantizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class WallpaperColors
  implements Parcelable
{
  private static final float BRIGHT_IMAGE_MEAN_LUMINANCE = 0.75F;
  public static final Parcelable.Creator<WallpaperColors> CREATOR = new Parcelable.Creator()
  {
    public WallpaperColors createFromParcel(Parcel paramAnonymousParcel)
    {
      return new WallpaperColors(paramAnonymousParcel);
    }
    
    public WallpaperColors[] newArray(int paramAnonymousInt)
    {
      return new WallpaperColors[paramAnonymousInt];
    }
  };
  private static final float DARK_PIXEL_LUMINANCE = 0.45F;
  private static final float DARK_THEME_MEAN_LUMINANCE = 0.25F;
  public static final int HINT_FROM_BITMAP = 4;
  public static final int HINT_SUPPORTS_DARK_TEXT = 1;
  public static final int HINT_SUPPORTS_DARK_THEME = 2;
  private static final int MAX_BITMAP_SIZE = 112;
  private static final float MAX_DARK_AREA = 0.05F;
  private static final int MAX_WALLPAPER_EXTRACTION_AREA = 12544;
  private static final float MIN_COLOR_OCCURRENCE = 0.05F;
  private int mColorHints;
  private final ArrayList<Color> mMainColors;
  
  public WallpaperColors(Color paramColor1, Color paramColor2, Color paramColor3)
  {
    this(paramColor1, paramColor2, paramColor3, 0);
  }
  
  public WallpaperColors(Color paramColor1, Color paramColor2, Color paramColor3, int paramInt)
  {
    if (paramColor1 != null)
    {
      mMainColors = new ArrayList(3);
      mMainColors.add(paramColor1);
      if (paramColor2 != null) {
        mMainColors.add(paramColor2);
      }
      if (paramColor3 != null) {
        if (paramColor2 != null) {
          mMainColors.add(paramColor3);
        } else {
          throw new IllegalArgumentException("tertiaryColor can't be specified when secondaryColor is null");
        }
      }
      mColorHints = paramInt;
      return;
    }
    throw new IllegalArgumentException("Primary color should never be null.");
  }
  
  public WallpaperColors(Parcel paramParcel)
  {
    mMainColors = new ArrayList();
    int i = paramParcel.readInt();
    for (int j = 0; j < i; j++)
    {
      Color localColor = Color.valueOf(paramParcel.readInt());
      mMainColors.add(localColor);
    }
    mColorHints = paramParcel.readInt();
  }
  
  private static int calculateDarkHints(Bitmap paramBitmap)
  {
    int i = 0;
    if (paramBitmap == null) {
      return 0;
    }
    int[] arrayOfInt = new int[paramBitmap.getWidth() * paramBitmap.getHeight()];
    double d = 0.0D;
    int j = (int)(arrayOfInt.length * 0.05F);
    int k = 0;
    paramBitmap.getPixels(arrayOfInt, 0, paramBitmap.getWidth(), 0, 0, paramBitmap.getWidth(), paramBitmap.getHeight());
    paramBitmap = new float[3];
    while (i < arrayOfInt.length)
    {
      ColorUtils.colorToHSL(arrayOfInt[i], paramBitmap);
      float f = paramBitmap[2];
      int m = Color.alpha(arrayOfInt[i]);
      n = k;
      if (f < 0.45F)
      {
        n = k;
        if (m != 0) {
          n = k + 1;
        }
      }
      d += f;
      i++;
      k = n;
    }
    int n = 0;
    d /= arrayOfInt.length;
    i = n;
    if (d > 0.75D)
    {
      i = n;
      if (k < j) {
        i = 0x0 | 0x1;
      }
    }
    k = i;
    if (d < 0.25D) {
      k = i | 0x2;
    }
    return k;
  }
  
  private static Size calculateOptimalSize(int paramInt1, int paramInt2)
  {
    int i = paramInt1 * paramInt2;
    double d = 1.0D;
    if (i > 12544) {
      d = Math.sqrt(12544.0D / i);
    }
    int j = (int)(paramInt1 * d);
    i = (int)(paramInt2 * d);
    paramInt1 = j;
    if (j == 0) {
      paramInt1 = 1;
    }
    paramInt2 = i;
    if (i == 0) {
      paramInt2 = 1;
    }
    return new Size(paramInt1, paramInt2);
  }
  
  public static WallpaperColors fromBitmap(Bitmap paramBitmap)
  {
    if (paramBitmap != null)
    {
      int i = paramBitmap.getWidth();
      int j = paramBitmap.getHeight();
      int k = 0;
      Object localObject = paramBitmap;
      if (i * j > 12544)
      {
        k = 1;
        localObject = calculateOptimalSize(paramBitmap.getWidth(), paramBitmap.getHeight());
        localObject = Bitmap.createScaledBitmap(paramBitmap, ((Size)localObject).getWidth(), ((Size)localObject).getHeight(), true);
      }
      ArrayList localArrayList = new ArrayList(Palette.from((Bitmap)localObject).setQuantizer(new VariationalKMeansQuantizer()).maximumColorCount(5).clearFilters().resizeBitmapArea(12544).generate().getSwatches());
      localArrayList.removeIf(new _..Lambda.WallpaperColors.8R5kfKKLfHjpw_QXmD1mWOKwJxc(((Bitmap)localObject).getWidth() * ((Bitmap)localObject).getHeight() * 0.05F));
      localArrayList.sort(_..Lambda.WallpaperColors.MQFGJ9EZ9CDeGbIhMufJKqru3IE.INSTANCE);
      j = localArrayList.size();
      Bitmap localBitmap1 = null;
      Bitmap localBitmap2 = null;
      Bitmap localBitmap3 = null;
      for (i = 0; i < j; i++)
      {
        paramBitmap = Color.valueOf(((Palette.Swatch)localArrayList.get(i)).getRgb());
        switch (i)
        {
        default: 
          break;
        case 2: 
          localBitmap3 = paramBitmap;
          break;
        case 1: 
          localBitmap2 = paramBitmap;
          break;
        case 0: 
          localBitmap1 = paramBitmap;
        }
      }
      i = calculateDarkHints((Bitmap)localObject);
      if (k != 0) {
        ((Bitmap)localObject).recycle();
      }
      return new WallpaperColors(localBitmap1, localBitmap2, localBitmap3, 0x4 | i);
    }
    throw new IllegalArgumentException("Bitmap can't be null");
  }
  
  public static WallpaperColors fromDrawable(Drawable paramDrawable)
  {
    if (paramDrawable != null)
    {
      Rect localRect = paramDrawable.copyBounds();
      int i = paramDrawable.getIntrinsicWidth();
      int j = paramDrawable.getIntrinsicHeight();
      int k;
      if (i > 0)
      {
        k = j;
        if (j > 0) {}
      }
      else
      {
        i = 112;
        k = 112;
      }
      Object localObject1 = calculateOptimalSize(i, k);
      localObject1 = Bitmap.createBitmap(((Size)localObject1).getWidth(), ((Size)localObject1).getHeight(), Bitmap.Config.ARGB_8888);
      Object localObject2 = new Canvas((Bitmap)localObject1);
      paramDrawable.setBounds(0, 0, ((Bitmap)localObject1).getWidth(), ((Bitmap)localObject1).getHeight());
      paramDrawable.draw((Canvas)localObject2);
      localObject2 = fromBitmap((Bitmap)localObject1);
      ((Bitmap)localObject1).recycle();
      paramDrawable.setBounds(localRect);
      return localObject2;
    }
    throw new IllegalArgumentException("Drawable cannot be null");
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool1 = false;
    if ((paramObject != null) && (getClass() == paramObject.getClass()))
    {
      paramObject = (WallpaperColors)paramObject;
      boolean bool2 = bool1;
      if (mMainColors.equals(mMainColors))
      {
        bool2 = bool1;
        if (mColorHints == mColorHints) {
          bool2 = true;
        }
      }
      return bool2;
    }
    return false;
  }
  
  public int getColorHints()
  {
    return mColorHints;
  }
  
  public List<Color> getMainColors()
  {
    return Collections.unmodifiableList(mMainColors);
  }
  
  public Color getPrimaryColor()
  {
    return (Color)mMainColors.get(0);
  }
  
  public Color getSecondaryColor()
  {
    Color localColor;
    if (mMainColors.size() < 2) {
      localColor = null;
    } else {
      localColor = (Color)mMainColors.get(1);
    }
    return localColor;
  }
  
  public Color getTertiaryColor()
  {
    Color localColor;
    if (mMainColors.size() < 3) {
      localColor = null;
    } else {
      localColor = (Color)mMainColors.get(2);
    }
    return localColor;
  }
  
  public int hashCode()
  {
    return 31 * mMainColors.hashCode() + mColorHints;
  }
  
  public void setColorHints(int paramInt)
  {
    mColorHints = paramInt;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    for (int i = 0; i < mMainColors.size(); i++)
    {
      localStringBuilder1.append(Integer.toHexString(((Color)mMainColors.get(i)).toArgb()));
      localStringBuilder1.append(" ");
    }
    StringBuilder localStringBuilder2 = new StringBuilder();
    localStringBuilder2.append("[WallpaperColors: ");
    localStringBuilder2.append(localStringBuilder1.toString());
    localStringBuilder2.append("h: ");
    localStringBuilder2.append(mColorHints);
    localStringBuilder2.append("]");
    return localStringBuilder2.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    List localList = getMainColors();
    int i = localList.size();
    paramParcel.writeInt(i);
    for (paramInt = 0; paramInt < i; paramInt++) {
      paramParcel.writeInt(((Color)localList.get(paramInt)).toArgb());
    }
    paramParcel.writeInt(mColorHints);
  }
}
