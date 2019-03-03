package android.view;

import android.content.res.Resources;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Region.Op;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.util.PathParser;
import android.util.proto.ProtoOutputStream;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.annotations.VisibleForTesting.Visibility;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class DisplayCutout
{
  private static final String BOTTOM_MARKER = "@bottom";
  private static final Object CACHE_LOCK = new Object();
  private static final String DP_MARKER = "@dp";
  private static final Region EMPTY_REGION;
  public static final String EMULATION_OVERLAY_CATEGORY = "com.android.internal.display_cutout_emulation";
  public static final DisplayCutout NO_CUTOUT;
  private static final Pair<Path, DisplayCutout> NULL_PAIR;
  private static final String RIGHT_MARKER = "@right";
  private static final String TAG = "DisplayCutout";
  private static final Rect ZERO_RECT = new Rect();
  @GuardedBy("CACHE_LOCK")
  private static Pair<Path, DisplayCutout> sCachedCutout = NULL_PAIR;
  @GuardedBy("CACHE_LOCK")
  private static float sCachedDensity;
  @GuardedBy("CACHE_LOCK")
  private static int sCachedDisplayHeight;
  @GuardedBy("CACHE_LOCK")
  private static int sCachedDisplayWidth;
  @GuardedBy("CACHE_LOCK")
  private static String sCachedSpec;
  private final Region mBounds;
  private final Rect mSafeInsets;
  
  static
  {
    EMPTY_REGION = new Region();
    NO_CUTOUT = new DisplayCutout(ZERO_RECT, EMPTY_REGION, false);
    NULL_PAIR = new Pair(null, null);
  }
  
  private DisplayCutout(Rect paramRect, Region paramRegion, boolean paramBoolean)
  {
    if (paramRect == null) {
      paramRect = ZERO_RECT;
    } else if (paramBoolean) {
      paramRect = new Rect(paramRect);
    }
    mSafeInsets = paramRect;
    if (paramRegion == null) {
      paramRect = Region.obtain();
    } else if (paramBoolean) {
      paramRect = Region.obtain(paramRegion);
    } else {
      paramRect = paramRegion;
    }
    mBounds = paramRect;
  }
  
  public DisplayCutout(Rect paramRect, List<Rect> paramList)
  {
    this(paramRect, boundingRectsToRegion(paramList), true);
  }
  
  private static int atLeastZero(int paramInt)
  {
    if (paramInt < 0) {
      paramInt = 0;
    }
    return paramInt;
  }
  
  private static Region boundingRectsToRegion(List<Rect> paramList)
  {
    Region localRegion = Region.obtain();
    if (paramList != null)
    {
      paramList = paramList.iterator();
      while (paramList.hasNext()) {
        localRegion.op((Rect)paramList.next(), Region.Op.UNION);
      }
    }
    return localRegion;
  }
  
  public static DisplayCutout fromBoundingRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    Path localPath = new Path();
    localPath.reset();
    localPath.moveTo(paramInt1, paramInt2);
    localPath.lineTo(paramInt1, paramInt4);
    localPath.lineTo(paramInt3, paramInt4);
    localPath.lineTo(paramInt3, paramInt2);
    localPath.close();
    return fromBounds(localPath);
  }
  
  public static DisplayCutout fromBounds(Path paramPath)
  {
    Object localObject = new RectF();
    paramPath.computeBounds((RectF)localObject, false);
    Region localRegion = Region.obtain();
    localRegion.set((int)left, (int)top, (int)right, (int)bottom);
    localObject = new Region();
    ((Region)localObject).setPath(paramPath, localRegion);
    localRegion.recycle();
    return new DisplayCutout(ZERO_RECT, (Region)localObject, false);
  }
  
  public static DisplayCutout fromResources(Resources paramResources, int paramInt1, int paramInt2)
  {
    return fromSpec(paramResources.getString(17039729), paramInt1, paramInt2, DisplayMetrics.DENSITY_DEVICE_STABLE / 160.0F);
  }
  
  @VisibleForTesting(visibility=VisibleForTesting.Visibility.PRIVATE)
  public static DisplayCutout fromSpec(String paramString, int paramInt1, int paramInt2, float paramFloat)
  {
    return (DisplayCutout)pathAndDisplayCutoutFromSpecsecond;
  }
  
  private static Pair<Path, DisplayCutout> pathAndDisplayCutoutFromSpec(String arg0, int paramInt1, int paramInt2, float paramFloat)
  {
    if (TextUtils.isEmpty(???)) {
      return NULL_PAIR;
    }
    synchronized (CACHE_LOCK)
    {
      if ((???.equals(sCachedSpec)) && (sCachedDisplayWidth == paramInt1) && (sCachedDisplayHeight == paramInt2) && (sCachedDensity == paramFloat))
      {
        ??? = sCachedCutout;
        return ???;
      }
      ??? = ???.trim();
      float f;
      if (((String)???).endsWith("@right"))
      {
        f = paramInt1;
        ??? = ((String)???).substring(0, ((String)???).length() - "@right".length()).trim();
      }
      else
      {
        f = paramInt1 / 2.0F;
      }
      boolean bool = ((String)???).endsWith("@dp");
      ??? = (String)???;
      if (bool) {
        ??? = ((String)???).substring(0, ((String)???).length() - "@dp".length());
      }
      Object localObject3 = null;
      ??? = ???;
      if (???.contains("@bottom"))
      {
        ??? = ???.split("@bottom", 2);
        ??? = ???[0].trim();
        localObject3 = ???[1].trim();
      }
      try
      {
        ??? = PathParser.createPathFromPathData((String)???);
        Matrix localMatrix = new Matrix();
        if (bool) {
          localMatrix.postScale(paramFloat, paramFloat);
        }
        localMatrix.postTranslate(f, 0.0F);
        ???.transform(localMatrix);
        if (localObject3 != null) {
          try
          {
            localObject3 = PathParser.createPathFromPathData((String)localObject3);
            localMatrix.postTranslate(0.0F, paramInt2);
            ((Path)localObject3).transform(localMatrix);
            ???.addPath((Path)localObject3);
          }
          catch (Throwable ???)
          {
            Log.wtf("DisplayCutout", "Could not inflate bottom cutout: ", ???);
            return NULL_PAIR;
          }
        }
        localObject3 = new Pair(???, fromBounds(???));
        synchronized (CACHE_LOCK)
        {
          sCachedSpec = (String)???;
          sCachedDisplayWidth = paramInt1;
          sCachedDisplayHeight = paramInt2;
          sCachedDensity = paramFloat;
          sCachedCutout = (Pair)localObject3;
          return localObject3;
        }
        ??? = finally;
      }
      catch (Throwable ???)
      {
        Log.wtf("DisplayCutout", "Could not inflate cutout: ", ???);
        return NULL_PAIR;
      }
    }
  }
  
  public static Path pathFromResources(Resources paramResources, int paramInt1, int paramInt2)
  {
    return (Path)pathAndDisplayCutoutFromSpecgetString17039729DENSITY_DEVICE_STABLE160.0Ffirst;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (paramObject == this) {
      return true;
    }
    if ((paramObject instanceof DisplayCutout))
    {
      paramObject = (DisplayCutout)paramObject;
      if ((!mSafeInsets.equals(mSafeInsets)) || (!mBounds.equals(mBounds))) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  public List<Rect> getBoundingRects()
  {
    ArrayList localArrayList = new ArrayList();
    Region localRegion = Region.obtain();
    localRegion.set(mBounds);
    localRegion.op(0, 0, Integer.MAX_VALUE, getSafeInsetTop(), Region.Op.INTERSECT);
    if (!localRegion.isEmpty()) {
      localArrayList.add(localRegion.getBounds());
    }
    localRegion.set(mBounds);
    localRegion.op(0, 0, getSafeInsetLeft(), Integer.MAX_VALUE, Region.Op.INTERSECT);
    if (!localRegion.isEmpty()) {
      localArrayList.add(localRegion.getBounds());
    }
    localRegion.set(mBounds);
    localRegion.op(getSafeInsetLeft() + 1, getSafeInsetTop() + 1, Integer.MAX_VALUE, Integer.MAX_VALUE, Region.Op.INTERSECT);
    if (!localRegion.isEmpty()) {
      localArrayList.add(localRegion.getBounds());
    }
    localRegion.recycle();
    return localArrayList;
  }
  
  public Region getBounds()
  {
    return Region.obtain(mBounds);
  }
  
  public int getSafeInsetBottom()
  {
    return mSafeInsets.bottom;
  }
  
  public int getSafeInsetLeft()
  {
    return mSafeInsets.left;
  }
  
  public int getSafeInsetRight()
  {
    return mSafeInsets.right;
  }
  
  public int getSafeInsetTop()
  {
    return mSafeInsets.top;
  }
  
  public Rect getSafeInsets()
  {
    return new Rect(mSafeInsets);
  }
  
  public int hashCode()
  {
    return mSafeInsets.hashCode() * 31 + mBounds.getBounds().hashCode();
  }
  
  public DisplayCutout inset(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if ((!mBounds.isEmpty()) && ((paramInt1 != 0) || (paramInt2 != 0) || (paramInt3 != 0) || (paramInt4 != 0)))
    {
      Rect localRect = new Rect(mSafeInsets);
      Region localRegion = Region.obtain(mBounds);
      if ((paramInt2 > 0) || (top > 0)) {
        top = atLeastZero(top - paramInt2);
      }
      if ((paramInt4 > 0) || (bottom > 0)) {
        bottom = atLeastZero(bottom - paramInt4);
      }
      if ((paramInt1 > 0) || (left > 0)) {
        left = atLeastZero(left - paramInt1);
      }
      if ((paramInt3 > 0) || (right > 0)) {
        right = atLeastZero(right - paramInt3);
      }
      localRegion.translate(-paramInt1, -paramInt2);
      return new DisplayCutout(localRect, localRegion, false);
    }
    return this;
  }
  
  public boolean isBoundsEmpty()
  {
    return mBounds.isEmpty();
  }
  
  public boolean isEmpty()
  {
    return mSafeInsets.equals(ZERO_RECT);
  }
  
  public DisplayCutout replaceSafeInsets(Rect paramRect)
  {
    return new DisplayCutout(new Rect(paramRect), mBounds, false);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("DisplayCutout{insets=");
    localStringBuilder.append(mSafeInsets);
    localStringBuilder.append(" boundingRect=");
    localStringBuilder.append(mBounds.getBounds());
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToProto(ProtoOutputStream paramProtoOutputStream, long paramLong)
  {
    paramLong = paramProtoOutputStream.start(paramLong);
    mSafeInsets.writeToProto(paramProtoOutputStream, 1146756268033L);
    mBounds.getBounds().writeToProto(paramProtoOutputStream, 1146756268034L);
    paramProtoOutputStream.end(paramLong);
  }
  
  public static final class ParcelableWrapper
    implements Parcelable
  {
    public static final Parcelable.Creator<ParcelableWrapper> CREATOR = new Parcelable.Creator()
    {
      public DisplayCutout.ParcelableWrapper createFromParcel(Parcel paramAnonymousParcel)
      {
        return new DisplayCutout.ParcelableWrapper(DisplayCutout.ParcelableWrapper.readCutoutFromParcel(paramAnonymousParcel));
      }
      
      public DisplayCutout.ParcelableWrapper[] newArray(int paramAnonymousInt)
      {
        return new DisplayCutout.ParcelableWrapper[paramAnonymousInt];
      }
    };
    private DisplayCutout mInner;
    
    public ParcelableWrapper()
    {
      this(DisplayCutout.NO_CUTOUT);
    }
    
    public ParcelableWrapper(DisplayCutout paramDisplayCutout)
    {
      mInner = paramDisplayCutout;
    }
    
    public static DisplayCutout readCutoutFromParcel(Parcel paramParcel)
    {
      int i = paramParcel.readInt();
      if (i == -1) {
        return null;
      }
      if (i == 0) {
        return DisplayCutout.NO_CUTOUT;
      }
      return new DisplayCutout((Rect)paramParcel.readTypedObject(Rect.CREATOR), (Region)paramParcel.readTypedObject(Region.CREATOR), false, null);
    }
    
    public static void writeCutoutToParcel(DisplayCutout paramDisplayCutout, Parcel paramParcel, int paramInt)
    {
      if (paramDisplayCutout == null)
      {
        paramParcel.writeInt(-1);
      }
      else if (paramDisplayCutout == DisplayCutout.NO_CUTOUT)
      {
        paramParcel.writeInt(0);
      }
      else
      {
        paramParcel.writeInt(1);
        paramParcel.writeTypedObject(mSafeInsets, paramInt);
        paramParcel.writeTypedObject(mBounds, paramInt);
      }
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public boolean equals(Object paramObject)
    {
      boolean bool;
      if (((paramObject instanceof ParcelableWrapper)) && (mInner.equals(mInner))) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public DisplayCutout get()
    {
      return mInner;
    }
    
    public int hashCode()
    {
      return mInner.hashCode();
    }
    
    public void readFromParcel(Parcel paramParcel)
    {
      mInner = readCutoutFromParcel(paramParcel);
    }
    
    public void set(ParcelableWrapper paramParcelableWrapper)
    {
      mInner = paramParcelableWrapper.get();
    }
    
    public void set(DisplayCutout paramDisplayCutout)
    {
      mInner = paramDisplayCutout;
    }
    
    public String toString()
    {
      return String.valueOf(mInner);
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      writeCutoutToParcel(mInner, paramParcel, paramInt);
    }
  }
}
