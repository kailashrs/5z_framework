package android.graphics.drawable;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import com.android.internal.R.styleable;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class LayerDrawable
  extends Drawable
  implements Drawable.Callback
{
  public static final int INSET_UNDEFINED = Integer.MIN_VALUE;
  private static final String LOG_TAG = "LayerDrawable";
  public static final int PADDING_MODE_NEST = 0;
  public static final int PADDING_MODE_STACK = 1;
  private boolean mChildRequestedInvalidation;
  private Rect mHotspotBounds;
  LayerState mLayerState = createConstantState(paramLayerState, paramResources);
  private boolean mMutated;
  private int[] mPaddingB;
  private int[] mPaddingL;
  private int[] mPaddingR;
  private int[] mPaddingT;
  private boolean mSuspendChildInvalidation;
  private final Rect mTmpContainer = new Rect();
  private final Rect mTmpOutRect = new Rect();
  private final Rect mTmpRect = new Rect();
  
  LayerDrawable()
  {
    this((LayerState)null, null);
  }
  
  LayerDrawable(LayerState paramLayerState, Resources paramResources)
  {
    if (mLayerState.mNumChildren > 0)
    {
      ensurePadding();
      refreshPadding();
    }
  }
  
  public LayerDrawable(Drawable[] paramArrayOfDrawable)
  {
    this(paramArrayOfDrawable, null);
  }
  
  LayerDrawable(Drawable[] paramArrayOfDrawable, LayerState paramLayerState)
  {
    this(paramLayerState, null);
    if (paramArrayOfDrawable != null)
    {
      int i = paramArrayOfDrawable.length;
      ChildDrawable[] arrayOfChildDrawable = new ChildDrawable[i];
      for (int j = 0; j < i; j++)
      {
        arrayOfChildDrawable[j] = new ChildDrawable(mLayerState.mDensity);
        mDrawable = paramArrayOfDrawable[j];
        paramArrayOfDrawable[j].setCallback(this);
        paramLayerState = mLayerState;
        mChildrenChangingConfigurations |= paramArrayOfDrawable[j].getChangingConfigurations();
      }
      mLayerState.mNumChildren = i;
      mLayerState.mChildren = arrayOfChildDrawable;
      ensurePadding();
      refreshPadding();
      return;
    }
    throw new IllegalArgumentException("layers must be non-null");
  }
  
  private void computeNestedPadding(Rect paramRect)
  {
    int i = 0;
    left = 0;
    top = 0;
    right = 0;
    bottom = 0;
    ChildDrawable[] arrayOfChildDrawable = mLayerState.mChildren;
    int j = mLayerState.mNumChildren;
    while (i < j)
    {
      refreshChildPadding(i, arrayOfChildDrawable[i]);
      left += mPaddingL[i];
      top += mPaddingT[i];
      right += mPaddingR[i];
      bottom += mPaddingB[i];
      i++;
    }
  }
  
  private void computeStackedPadding(Rect paramRect)
  {
    int i = 0;
    left = 0;
    top = 0;
    right = 0;
    bottom = 0;
    ChildDrawable[] arrayOfChildDrawable = mLayerState.mChildren;
    int j = mLayerState.mNumChildren;
    while (i < j)
    {
      refreshChildPadding(i, arrayOfChildDrawable[i]);
      left = Math.max(left, mPaddingL[i]);
      top = Math.max(top, mPaddingT[i]);
      right = Math.max(right, mPaddingR[i]);
      bottom = Math.max(bottom, mPaddingB[i]);
      i++;
    }
  }
  
  private ChildDrawable createLayer(Drawable paramDrawable)
  {
    ChildDrawable localChildDrawable = new ChildDrawable(mLayerState.mDensity);
    mDrawable = paramDrawable;
    return localChildDrawable;
  }
  
  private Drawable getFirstNonNullDrawable()
  {
    ChildDrawable[] arrayOfChildDrawable = mLayerState.mChildren;
    int i = mLayerState.mNumChildren;
    for (int j = 0; j < i; j++)
    {
      Drawable localDrawable = mDrawable;
      if (localDrawable != null) {
        return localDrawable;
      }
    }
    return null;
  }
  
  private void inflateLayers(Resources paramResources, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, Resources.Theme paramTheme)
    throws XmlPullParserException, IOException
  {
    LayerState localLayerState = mLayerState;
    int i = paramXmlPullParser.getDepth() + 1;
    for (;;)
    {
      int j = paramXmlPullParser.next();
      if (j == 1) {
        break;
      }
      int k = paramXmlPullParser.getDepth();
      if ((k < i) && (j == 3)) {
        break;
      }
      if ((j == 2) && (k <= i) && (paramXmlPullParser.getName().equals("item")))
      {
        ChildDrawable localChildDrawable = new ChildDrawable(mDensity);
        TypedArray localTypedArray = obtainAttributes(paramResources, paramTheme, paramAttributeSet, R.styleable.LayerDrawableItem);
        updateLayerFromTypedArray(localChildDrawable, localTypedArray);
        localTypedArray.recycle();
        if ((mDrawable == null) && ((mThemeAttrs == null) || (mThemeAttrs[4] == 0)))
        {
          do
          {
            k = paramXmlPullParser.next();
          } while (k == 4);
          if (k == 2)
          {
            mDrawable = Drawable.createFromXmlInner(paramResources, paramXmlPullParser, paramAttributeSet, paramTheme);
            mDrawable.setCallback(this);
            mChildrenChangingConfigurations |= mDrawable.getChangingConfigurations();
          }
          else
          {
            paramResources = new StringBuilder();
            paramResources.append(paramXmlPullParser.getPositionDescription());
            paramResources.append(": <item> tag requires a 'drawable' attribute or child tag defining a drawable");
            throw new XmlPullParserException(paramResources.toString());
          }
        }
        addLayer(localChildDrawable);
      }
    }
  }
  
  private boolean refreshChildPadding(int paramInt, ChildDrawable paramChildDrawable)
  {
    if (mDrawable != null)
    {
      Rect localRect = mTmpRect;
      mDrawable.getPadding(localRect);
      if ((left != mPaddingL[paramInt]) || (top != mPaddingT[paramInt]) || (right != mPaddingR[paramInt]) || (bottom != mPaddingB[paramInt]))
      {
        mPaddingL[paramInt] = left;
        mPaddingT[paramInt] = top;
        mPaddingR[paramInt] = right;
        mPaddingB[paramInt] = bottom;
        return true;
      }
    }
    return false;
  }
  
  private static int resolveGravity(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    int i = paramInt1;
    if (!Gravity.isHorizontal(paramInt1)) {
      if (paramInt2 < 0) {
        i = paramInt1 | 0x7;
      } else {
        i = paramInt1 | 0x800003;
      }
    }
    paramInt1 = i;
    if (!Gravity.isVertical(i)) {
      if (paramInt3 < 0) {
        paramInt1 = i | 0x70;
      } else {
        paramInt1 = i | 0x30;
      }
    }
    i = paramInt1;
    if (paramInt2 < 0)
    {
      i = paramInt1;
      if (paramInt4 < 0) {
        i = paramInt1 | 0x7;
      }
    }
    paramInt1 = i;
    if (paramInt3 < 0)
    {
      paramInt1 = i;
      if (paramInt5 < 0) {
        paramInt1 = i | 0x70;
      }
    }
    return paramInt1;
  }
  
  private void resumeChildInvalidation()
  {
    mSuspendChildInvalidation = false;
    if (mChildRequestedInvalidation)
    {
      mChildRequestedInvalidation = false;
      invalidateSelf();
    }
  }
  
  private void setLayerInsetInternal(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7)
  {
    ChildDrawable localChildDrawable = mLayerState.mChildren[paramInt1];
    mInsetL = paramInt2;
    mInsetT = paramInt3;
    mInsetR = paramInt4;
    mInsetB = paramInt5;
    mInsetS = paramInt6;
    mInsetE = paramInt7;
  }
  
  private void suspendChildInvalidation()
  {
    mSuspendChildInvalidation = true;
  }
  
  private void updateLayerBounds(Rect paramRect)
  {
    try
    {
      suspendChildInvalidation();
      updateLayerBoundsInternal(paramRect);
      return;
    }
    finally
    {
      resumeChildInvalidation();
    }
  }
  
  private void updateLayerBoundsInternal(Rect paramRect)
  {
    Rect localRect1 = mTmpOutRect;
    int i = getLayoutDirection();
    int j = 0;
    if (i == 1) {
      k = 1;
    } else {
      k = 0;
    }
    if (mLayerState.mPaddingMode == 0) {
      j = 1;
    }
    ChildDrawable[] arrayOfChildDrawable = mLayerState.mChildren;
    int m = mLayerState.mNumChildren;
    int n = 0;
    int i1 = 0;
    int i2 = 0;
    int i3 = 0;
    int i4 = 0;
    int i5 = k;
    int k = i2;
    for (;;)
    {
      Rect localRect2 = paramRect;
      if (i4 >= m) {
        break;
      }
      ChildDrawable localChildDrawable = arrayOfChildDrawable[i4];
      Drawable localDrawable = mDrawable;
      if (localDrawable != null)
      {
        int i6 = mInsetT;
        int i7 = mInsetB;
        if (i5 != 0) {
          i8 = mInsetE;
        } else {
          i8 = mInsetS;
        }
        if (i5 != 0) {
          i2 = mInsetS;
        } else {
          i2 = mInsetE;
        }
        if (i8 == Integer.MIN_VALUE) {
          i8 = mInsetL;
        }
        if (i2 == Integer.MIN_VALUE) {
          i2 = mInsetR;
        }
        Rect localRect3 = mTmpContainer;
        localRect3.set(left + i8 + i3, top + i6 + k, right - i2 - i1, bottom - i7 - n);
        i7 = localDrawable.getIntrinsicWidth();
        int i8 = localDrawable.getIntrinsicHeight();
        i2 = mWidth;
        i6 = mHeight;
        int i9 = resolveGravity(mGravity, i2, i6, i7, i8);
        if (i2 < 0) {
          i2 = i7;
        }
        if (i6 >= 0) {
          i8 = i6;
        }
        Gravity.apply(i9, i2, i8, localRect3, localRect1, i);
        localDrawable.setBounds(localRect1);
        if (j != 0)
        {
          i3 += mPaddingL[i4];
          i1 += mPaddingR[i4];
          i2 = mPaddingT[i4];
          n += mPaddingB[i4];
          k += i2;
        }
      }
      i4++;
    }
  }
  
  private void updateLayerFromTypedArray(ChildDrawable paramChildDrawable, TypedArray paramTypedArray)
  {
    LayerState localLayerState = mLayerState;
    mChildrenChangingConfigurations |= paramTypedArray.getChangingConfigurations();
    mThemeAttrs = paramTypedArray.extractThemeAttrs();
    int i = paramTypedArray.getIndexCount();
    for (int j = 0; j < i; j++)
    {
      int k = paramTypedArray.getIndex(j);
      switch (k)
      {
      case 4: 
      default: 
        break;
      case 10: 
        mInsetE = paramTypedArray.getDimensionPixelOffset(k, mInsetE);
        break;
      case 9: 
        mInsetS = paramTypedArray.getDimensionPixelOffset(k, mInsetS);
        break;
      case 8: 
        mInsetB = paramTypedArray.getDimensionPixelOffset(k, mInsetB);
        break;
      case 7: 
        mInsetR = paramTypedArray.getDimensionPixelOffset(k, mInsetR);
        break;
      case 6: 
        mInsetT = paramTypedArray.getDimensionPixelOffset(k, mInsetT);
        break;
      case 5: 
        mInsetL = paramTypedArray.getDimensionPixelOffset(k, mInsetL);
        break;
      case 3: 
        mWidth = paramTypedArray.getDimensionPixelSize(k, mWidth);
        break;
      case 2: 
        mHeight = paramTypedArray.getDimensionPixelSize(k, mHeight);
        break;
      case 1: 
        mId = paramTypedArray.getResourceId(k, mId);
        break;
      case 0: 
        mGravity = paramTypedArray.getInteger(k, mGravity);
      }
    }
    paramTypedArray = paramTypedArray.getDrawable(4);
    if (paramTypedArray != null)
    {
      if (mDrawable != null) {
        mDrawable.setCallback(null);
      }
      mDrawable = paramTypedArray;
      mDrawable.setCallback(this);
      mChildrenChangingConfigurations |= mDrawable.getChangingConfigurations();
    }
  }
  
  private void updateStateFromTypedArray(TypedArray paramTypedArray)
  {
    LayerState localLayerState = mLayerState;
    mChangingConfigurations |= paramTypedArray.getChangingConfigurations();
    LayerState.access$002(localLayerState, paramTypedArray.extractThemeAttrs());
    int i = paramTypedArray.getIndexCount();
    for (int j = 0; j < i; j++)
    {
      int k = paramTypedArray.getIndex(j);
      switch (k)
      {
      default: 
        break;
      case 8: 
        LayerState.access$202(localLayerState, paramTypedArray.getInteger(k, mPaddingMode));
        break;
      case 7: 
        LayerState.access$102(localLayerState, paramTypedArray.getBoolean(k, mAutoMirrored));
        break;
      case 6: 
        mPaddingEnd = paramTypedArray.getDimensionPixelOffset(k, mPaddingEnd);
        break;
      case 5: 
        mPaddingStart = paramTypedArray.getDimensionPixelOffset(k, mPaddingStart);
        break;
      case 4: 
        mOpacityOverride = paramTypedArray.getInt(k, mOpacityOverride);
        break;
      case 3: 
        mPaddingBottom = paramTypedArray.getDimensionPixelOffset(k, mPaddingBottom);
        break;
      case 2: 
        mPaddingRight = paramTypedArray.getDimensionPixelOffset(k, mPaddingRight);
        break;
      case 1: 
        mPaddingTop = paramTypedArray.getDimensionPixelOffset(k, mPaddingTop);
        break;
      case 0: 
        mPaddingLeft = paramTypedArray.getDimensionPixelOffset(k, mPaddingLeft);
      }
    }
  }
  
  public int addLayer(Drawable paramDrawable)
  {
    paramDrawable = createLayer(paramDrawable);
    int i = addLayer(paramDrawable);
    ensurePadding();
    refreshChildPadding(i, paramDrawable);
    return i;
  }
  
  int addLayer(ChildDrawable paramChildDrawable)
  {
    LayerState localLayerState = mLayerState;
    int i;
    if (mChildren != null) {
      i = mChildren.length;
    } else {
      i = 0;
    }
    int j = mNumChildren;
    if (j >= i)
    {
      ChildDrawable[] arrayOfChildDrawable = new ChildDrawable[i + 10];
      if (j > 0) {
        System.arraycopy(mChildren, 0, arrayOfChildDrawable, 0, j);
      }
      mChildren = arrayOfChildDrawable;
    }
    mChildren[j] = paramChildDrawable;
    mNumChildren += 1;
    localLayerState.invalidateCache();
    return j;
  }
  
  ChildDrawable addLayer(Drawable paramDrawable, int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    ChildDrawable localChildDrawable = createLayer(paramDrawable);
    mId = paramInt1;
    mThemeAttrs = paramArrayOfInt;
    mDrawable.setAutoMirrored(isAutoMirrored());
    mInsetL = paramInt2;
    mInsetT = paramInt3;
    mInsetR = paramInt4;
    mInsetB = paramInt5;
    addLayer(localChildDrawable);
    paramArrayOfInt = mLayerState;
    mChildrenChangingConfigurations |= paramDrawable.getChangingConfigurations();
    paramDrawable.setCallback(this);
    return localChildDrawable;
  }
  
  public void applyTheme(Resources.Theme paramTheme)
  {
    super.applyTheme(paramTheme);
    LayerState localLayerState = mLayerState;
    Object localObject1 = paramTheme.getResources();
    int i = 0;
    int j = Drawable.resolveDensity((Resources)localObject1, 0);
    localLayerState.setDensity(j);
    if (mThemeAttrs != null)
    {
      localObject1 = paramTheme.resolveAttributes(mThemeAttrs, R.styleable.LayerDrawable);
      updateStateFromTypedArray((TypedArray)localObject1);
      ((TypedArray)localObject1).recycle();
    }
    localObject1 = mChildren;
    int k = mNumChildren;
    while (i < k)
    {
      ChildDrawable localChildDrawable = localObject1[i];
      localChildDrawable.setDensity(j);
      if (mThemeAttrs != null)
      {
        localObject2 = paramTheme.resolveAttributes(mThemeAttrs, R.styleable.LayerDrawableItem);
        updateLayerFromTypedArray(localChildDrawable, (TypedArray)localObject2);
        ((TypedArray)localObject2).recycle();
      }
      Object localObject2 = mDrawable;
      if ((localObject2 != null) && (((Drawable)localObject2).canApplyTheme()))
      {
        ((Drawable)localObject2).applyTheme(paramTheme);
        mChildrenChangingConfigurations |= ((Drawable)localObject2).getChangingConfigurations();
      }
      i++;
    }
  }
  
  public boolean canApplyTheme()
  {
    boolean bool;
    if ((!mLayerState.canApplyTheme()) && (!super.canApplyTheme())) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public void clearMutated()
  {
    super.clearMutated();
    ChildDrawable[] arrayOfChildDrawable = mLayerState.mChildren;
    int i = mLayerState.mNumChildren;
    for (int j = 0; j < i; j++)
    {
      Drawable localDrawable = mDrawable;
      if (localDrawable != null) {
        localDrawable.clearMutated();
      }
    }
    mMutated = false;
  }
  
  LayerState createConstantState(LayerState paramLayerState, Resources paramResources)
  {
    return new LayerState(paramLayerState, this, paramResources);
  }
  
  public void draw(Canvas paramCanvas)
  {
    ChildDrawable[] arrayOfChildDrawable = mLayerState.mChildren;
    int i = mLayerState.mNumChildren;
    for (int j = 0; j < i; j++)
    {
      Drawable localDrawable = mDrawable;
      if (localDrawable != null) {
        localDrawable.draw(paramCanvas);
      }
    }
  }
  
  void ensurePadding()
  {
    int i = mLayerState.mNumChildren;
    if ((mPaddingL != null) && (mPaddingL.length >= i)) {
      return;
    }
    mPaddingL = new int[i];
    mPaddingT = new int[i];
    mPaddingR = new int[i];
    mPaddingB = new int[i];
  }
  
  public Drawable findDrawableByLayerId(int paramInt)
  {
    ChildDrawable[] arrayOfChildDrawable = mLayerState.mChildren;
    for (int i = mLayerState.mNumChildren - 1; i >= 0; i--) {
      if (mId == paramInt) {
        return mDrawable;
      }
    }
    return null;
  }
  
  public int findIndexByLayerId(int paramInt)
  {
    ChildDrawable[] arrayOfChildDrawable = mLayerState.mChildren;
    int i = mLayerState.mNumChildren;
    for (int j = 0; j < i; j++) {
      if (mId == paramInt) {
        return j;
      }
    }
    return -1;
  }
  
  public int getAlpha()
  {
    Drawable localDrawable = getFirstNonNullDrawable();
    if (localDrawable != null) {
      return localDrawable.getAlpha();
    }
    return super.getAlpha();
  }
  
  public int getBottomPadding()
  {
    return mLayerState.mPaddingBottom;
  }
  
  public int getChangingConfigurations()
  {
    return super.getChangingConfigurations() | mLayerState.getChangingConfigurations();
  }
  
  public Drawable.ConstantState getConstantState()
  {
    if (mLayerState.canConstantState())
    {
      mLayerState.mChangingConfigurations = getChangingConfigurations();
      return mLayerState;
    }
    return null;
  }
  
  public Drawable getDrawable(int paramInt)
  {
    if (paramInt < mLayerState.mNumChildren) {
      return mLayerState.mChildren[paramInt].mDrawable;
    }
    throw new IndexOutOfBoundsException();
  }
  
  public int getEndPadding()
  {
    return mLayerState.mPaddingEnd;
  }
  
  public void getHotspotBounds(Rect paramRect)
  {
    if (mHotspotBounds != null) {
      paramRect.set(mHotspotBounds);
    } else {
      super.getHotspotBounds(paramRect);
    }
  }
  
  public int getId(int paramInt)
  {
    if (paramInt < mLayerState.mNumChildren) {
      return mLayerState.mChildren[paramInt].mId;
    }
    throw new IndexOutOfBoundsException();
  }
  
  public int getIntrinsicHeight()
  {
    int i = -1;
    int j = 0;
    int k = 0;
    int m = mLayerState.mPaddingMode;
    int n = 0;
    int i1;
    if (m == 0) {
      i1 = 1;
    } else {
      i1 = 0;
    }
    ChildDrawable[] arrayOfChildDrawable = mLayerState.mChildren;
    int i2 = mLayerState.mNumChildren;
    while (n < i2)
    {
      ChildDrawable localChildDrawable = arrayOfChildDrawable[n];
      int i3;
      int i4;
      if (mDrawable == null)
      {
        i3 = j;
        i4 = k;
      }
      else
      {
        if (mHeight < 0) {
          m = mDrawable.getIntrinsicHeight();
        } else {
          m = mHeight;
        }
        if (m < 0) {
          i4 = -1;
        } else {
          i4 = mInsetT + m + mInsetB + j + k;
        }
        m = i;
        if (i4 > i) {
          m = i4;
        }
        i = m;
        i3 = j;
        i4 = k;
        if (i1 != 0)
        {
          i3 = j + mPaddingT[n];
          i4 = k + mPaddingB[n];
          i = m;
        }
      }
      n++;
      j = i3;
      k = i4;
    }
    return i;
  }
  
  public int getIntrinsicWidth()
  {
    int i = -1;
    int j = 0;
    int k = 0;
    int m = mLayerState.mPaddingMode;
    int n = 0;
    int i1 = 1;
    int i2;
    if (m == 0) {
      i2 = 1;
    } else {
      i2 = 0;
    }
    if (getLayoutDirection() != 1) {
      i1 = 0;
    }
    ChildDrawable[] arrayOfChildDrawable = mLayerState.mChildren;
    int i3 = mLayerState.mNumChildren;
    while (n < i3)
    {
      ChildDrawable localChildDrawable = arrayOfChildDrawable[n];
      if (mDrawable == null)
      {
        m = i;
      }
      else
      {
        int i4;
        if (i1 != 0) {
          i4 = mInsetE;
        } else {
          i4 = mInsetS;
        }
        if (i1 != 0) {
          m = mInsetS;
        } else {
          m = mInsetE;
        }
        if (i4 == Integer.MIN_VALUE) {
          i4 = mInsetL;
        }
        if (m == Integer.MIN_VALUE) {
          m = mInsetR;
        }
        int i5;
        if (mWidth < 0) {
          i5 = mDrawable.getIntrinsicWidth();
        } else {
          i5 = mWidth;
        }
        if (i5 < 0) {
          i4 = -1;
        } else {
          i4 = i5 + i4 + m + j + k;
        }
        m = i;
        if (i4 > i) {
          m = i4;
        }
        if (i2 != 0)
        {
          j += mPaddingL[n];
          k += mPaddingR[n];
        }
      }
      n++;
      i = m;
    }
    return i;
  }
  
  public int getLayerGravity(int paramInt)
  {
    return mLayerState.mChildren[paramInt].mGravity;
  }
  
  public int getLayerHeight(int paramInt)
  {
    return mLayerState.mChildren[paramInt].mHeight;
  }
  
  public int getLayerInsetBottom(int paramInt)
  {
    return mLayerState.mChildren[paramInt].mInsetB;
  }
  
  public int getLayerInsetEnd(int paramInt)
  {
    return mLayerState.mChildren[paramInt].mInsetE;
  }
  
  public int getLayerInsetLeft(int paramInt)
  {
    return mLayerState.mChildren[paramInt].mInsetL;
  }
  
  public int getLayerInsetRight(int paramInt)
  {
    return mLayerState.mChildren[paramInt].mInsetR;
  }
  
  public int getLayerInsetStart(int paramInt)
  {
    return mLayerState.mChildren[paramInt].mInsetS;
  }
  
  public int getLayerInsetTop(int paramInt)
  {
    return mLayerState.mChildren[paramInt].mInsetT;
  }
  
  public int getLayerWidth(int paramInt)
  {
    return mLayerState.mChildren[paramInt].mWidth;
  }
  
  public int getLeftPadding()
  {
    return mLayerState.mPaddingLeft;
  }
  
  public int getNumberOfLayers()
  {
    return mLayerState.mNumChildren;
  }
  
  public int getOpacity()
  {
    if (mLayerState.mOpacityOverride != 0) {
      return mLayerState.mOpacityOverride;
    }
    return mLayerState.getOpacity();
  }
  
  public void getOutline(Outline paramOutline)
  {
    ChildDrawable[] arrayOfChildDrawable = mLayerState.mChildren;
    int i = mLayerState.mNumChildren;
    for (int j = 0; j < i; j++)
    {
      Drawable localDrawable = mDrawable;
      if (localDrawable != null)
      {
        localDrawable.getOutline(paramOutline);
        if (!paramOutline.isEmpty()) {
          return;
        }
      }
    }
  }
  
  public boolean getPadding(Rect paramRect)
  {
    LayerState localLayerState = mLayerState;
    if (mPaddingMode == 0) {
      computeNestedPadding(paramRect);
    } else {
      computeStackedPadding(paramRect);
    }
    int i = mPaddingTop;
    int j = mPaddingBottom;
    int k = getLayoutDirection();
    boolean bool = false;
    if (k == 1) {
      k = 1;
    } else {
      k = 0;
    }
    int m;
    if (k != 0) {
      m = mPaddingEnd;
    } else {
      m = mPaddingStart;
    }
    if (k != 0) {
      k = mPaddingStart;
    } else {
      k = mPaddingEnd;
    }
    if (m < 0) {
      m = mPaddingLeft;
    }
    if (k < 0) {
      k = mPaddingRight;
    }
    if (m >= 0) {
      left = m;
    }
    if (i >= 0) {
      top = i;
    }
    if (k >= 0) {
      right = k;
    }
    if (j >= 0) {
      bottom = j;
    }
    if ((left == 0) && (top == 0) && (right == 0) && (bottom == 0)) {
      break label207;
    }
    bool = true;
    label207:
    return bool;
  }
  
  public int getPaddingMode()
  {
    return mLayerState.mPaddingMode;
  }
  
  public int getRightPadding()
  {
    return mLayerState.mPaddingRight;
  }
  
  public int getStartPadding()
  {
    return mLayerState.mPaddingStart;
  }
  
  public int getTopPadding()
  {
    return mLayerState.mPaddingTop;
  }
  
  public boolean hasFocusStateSpecified()
  {
    return mLayerState.hasFocusStateSpecified();
  }
  
  public void inflate(Resources paramResources, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, Resources.Theme paramTheme)
    throws XmlPullParserException, IOException
  {
    super.inflate(paramResources, paramXmlPullParser, paramAttributeSet, paramTheme);
    LayerState localLayerState = mLayerState;
    int i = 0;
    int j = Drawable.resolveDensity(paramResources, 0);
    localLayerState.setDensity(j);
    Object localObject = obtainAttributes(paramResources, paramTheme, paramAttributeSet, R.styleable.LayerDrawable);
    updateStateFromTypedArray((TypedArray)localObject);
    ((TypedArray)localObject).recycle();
    localObject = mChildren;
    int k = mNumChildren;
    while (i < k)
    {
      localObject[i].setDensity(j);
      i++;
    }
    inflateLayers(paramResources, paramXmlPullParser, paramAttributeSet, paramTheme);
    ensurePadding();
    refreshPadding();
  }
  
  public void invalidateDrawable(Drawable paramDrawable)
  {
    if (mSuspendChildInvalidation)
    {
      mChildRequestedInvalidation = true;
    }
    else
    {
      mLayerState.invalidateCache();
      invalidateSelf();
    }
  }
  
  public boolean isAutoMirrored()
  {
    return mLayerState.mAutoMirrored;
  }
  
  public boolean isProjected()
  {
    if (super.isProjected()) {
      return true;
    }
    ChildDrawable[] arrayOfChildDrawable = mLayerState.mChildren;
    int i = mLayerState.mNumChildren;
    for (int j = 0; j < i; j++) {
      if (mDrawable.isProjected()) {
        return true;
      }
    }
    return false;
  }
  
  public boolean isStateful()
  {
    return mLayerState.isStateful();
  }
  
  public void jumpToCurrentState()
  {
    ChildDrawable[] arrayOfChildDrawable = mLayerState.mChildren;
    int i = mLayerState.mNumChildren;
    for (int j = 0; j < i; j++)
    {
      Drawable localDrawable = mDrawable;
      if (localDrawable != null) {
        localDrawable.jumpToCurrentState();
      }
    }
  }
  
  public Drawable mutate()
  {
    if ((!mMutated) && (super.mutate() == this))
    {
      mLayerState = createConstantState(mLayerState, null);
      ChildDrawable[] arrayOfChildDrawable = mLayerState.mChildren;
      int i = mLayerState.mNumChildren;
      for (int j = 0; j < i; j++)
      {
        Drawable localDrawable = mDrawable;
        if (localDrawable != null) {
          localDrawable.mutate();
        }
      }
      mMutated = true;
    }
    return this;
  }
  
  protected void onBoundsChange(Rect paramRect)
  {
    updateLayerBounds(paramRect);
  }
  
  public boolean onLayoutDirectionChanged(int paramInt)
  {
    boolean bool1 = false;
    ChildDrawable[] arrayOfChildDrawable = mLayerState.mChildren;
    int i = mLayerState.mNumChildren;
    int j = 0;
    while (j < i)
    {
      Drawable localDrawable = mDrawable;
      boolean bool2 = bool1;
      if (localDrawable != null) {
        bool2 = bool1 | localDrawable.setLayoutDirection(paramInt);
      }
      j++;
      bool1 = bool2;
    }
    updateLayerBounds(getBounds());
    return bool1;
  }
  
  protected boolean onLevelChange(int paramInt)
  {
    boolean bool1 = false;
    ChildDrawable[] arrayOfChildDrawable = mLayerState.mChildren;
    int i = mLayerState.mNumChildren;
    int j = 0;
    while (j < i)
    {
      Drawable localDrawable = mDrawable;
      boolean bool2 = bool1;
      if (localDrawable != null)
      {
        bool2 = bool1;
        if (localDrawable.setLevel(paramInt))
        {
          refreshChildPadding(j, arrayOfChildDrawable[j]);
          bool2 = true;
        }
      }
      j++;
      bool1 = bool2;
    }
    if (bool1) {
      updateLayerBounds(getBounds());
    }
    return bool1;
  }
  
  protected boolean onStateChange(int[] paramArrayOfInt)
  {
    boolean bool1 = false;
    ChildDrawable[] arrayOfChildDrawable = mLayerState.mChildren;
    int i = mLayerState.mNumChildren;
    int j = 0;
    while (j < i)
    {
      Drawable localDrawable = mDrawable;
      boolean bool2 = bool1;
      if (localDrawable != null)
      {
        bool2 = bool1;
        if (localDrawable.isStateful())
        {
          bool2 = bool1;
          if (localDrawable.setState(paramArrayOfInt))
          {
            refreshChildPadding(j, arrayOfChildDrawable[j]);
            bool2 = true;
          }
        }
      }
      j++;
      bool1 = bool2;
    }
    if (bool1) {
      updateLayerBounds(getBounds());
    }
    return bool1;
  }
  
  void refreshPadding()
  {
    int i = mLayerState.mNumChildren;
    ChildDrawable[] arrayOfChildDrawable = mLayerState.mChildren;
    for (int j = 0; j < i; j++) {
      refreshChildPadding(j, arrayOfChildDrawable[j]);
    }
  }
  
  public void scheduleDrawable(Drawable paramDrawable, Runnable paramRunnable, long paramLong)
  {
    scheduleSelf(paramRunnable, paramLong);
  }
  
  public void setAlpha(int paramInt)
  {
    ChildDrawable[] arrayOfChildDrawable = mLayerState.mChildren;
    int i = mLayerState.mNumChildren;
    for (int j = 0; j < i; j++)
    {
      Drawable localDrawable = mDrawable;
      if (localDrawable != null) {
        localDrawable.setAlpha(paramInt);
      }
    }
  }
  
  public void setAutoMirrored(boolean paramBoolean)
  {
    LayerState.access$102(mLayerState, paramBoolean);
    ChildDrawable[] arrayOfChildDrawable = mLayerState.mChildren;
    int i = mLayerState.mNumChildren;
    for (int j = 0; j < i; j++)
    {
      Drawable localDrawable = mDrawable;
      if (localDrawable != null) {
        localDrawable.setAutoMirrored(paramBoolean);
      }
    }
  }
  
  public void setColorFilter(ColorFilter paramColorFilter)
  {
    ChildDrawable[] arrayOfChildDrawable = mLayerState.mChildren;
    int i = mLayerState.mNumChildren;
    for (int j = 0; j < i; j++)
    {
      Drawable localDrawable = mDrawable;
      if (localDrawable != null) {
        localDrawable.setColorFilter(paramColorFilter);
      }
    }
  }
  
  public void setDither(boolean paramBoolean)
  {
    ChildDrawable[] arrayOfChildDrawable = mLayerState.mChildren;
    int i = mLayerState.mNumChildren;
    for (int j = 0; j < i; j++)
    {
      Drawable localDrawable = mDrawable;
      if (localDrawable != null) {
        localDrawable.setDither(paramBoolean);
      }
    }
  }
  
  public void setDrawable(int paramInt, Drawable paramDrawable)
  {
    if (paramInt < mLayerState.mNumChildren)
    {
      ChildDrawable localChildDrawable = mLayerState.mChildren[paramInt];
      if (mDrawable != null)
      {
        if (paramDrawable != null) {
          paramDrawable.setBounds(mDrawable.getBounds());
        }
        mDrawable.setCallback(null);
      }
      if (paramDrawable != null) {
        paramDrawable.setCallback(this);
      }
      mDrawable = paramDrawable;
      mLayerState.invalidateCache();
      refreshChildPadding(paramInt, localChildDrawable);
      return;
    }
    throw new IndexOutOfBoundsException();
  }
  
  public boolean setDrawableByLayerId(int paramInt, Drawable paramDrawable)
  {
    paramInt = findIndexByLayerId(paramInt);
    if (paramInt < 0) {
      return false;
    }
    setDrawable(paramInt, paramDrawable);
    return true;
  }
  
  public void setHotspot(float paramFloat1, float paramFloat2)
  {
    ChildDrawable[] arrayOfChildDrawable = mLayerState.mChildren;
    int i = mLayerState.mNumChildren;
    for (int j = 0; j < i; j++)
    {
      Drawable localDrawable = mDrawable;
      if (localDrawable != null) {
        localDrawable.setHotspot(paramFloat1, paramFloat2);
      }
    }
  }
  
  public void setHotspotBounds(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    ChildDrawable[] arrayOfChildDrawable = mLayerState.mChildren;
    int i = mLayerState.mNumChildren;
    for (int j = 0; j < i; j++)
    {
      Drawable localDrawable = mDrawable;
      if (localDrawable != null) {
        localDrawable.setHotspotBounds(paramInt1, paramInt2, paramInt3, paramInt4);
      }
    }
    if (mHotspotBounds == null) {
      mHotspotBounds = new Rect(paramInt1, paramInt2, paramInt3, paramInt4);
    } else {
      mHotspotBounds.set(paramInt1, paramInt2, paramInt3, paramInt4);
    }
  }
  
  public void setId(int paramInt1, int paramInt2)
  {
    mLayerState.mChildren[paramInt1].mId = paramInt2;
  }
  
  public void setLayerGravity(int paramInt1, int paramInt2)
  {
    mLayerState.mChildren[paramInt1].mGravity = paramInt2;
  }
  
  public void setLayerHeight(int paramInt1, int paramInt2)
  {
    mLayerState.mChildren[paramInt1].mHeight = paramInt2;
  }
  
  public void setLayerInset(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    setLayerInsetInternal(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, Integer.MIN_VALUE, Integer.MIN_VALUE);
  }
  
  public void setLayerInsetBottom(int paramInt1, int paramInt2)
  {
    mLayerState.mChildren[paramInt1].mInsetB = paramInt2;
  }
  
  public void setLayerInsetEnd(int paramInt1, int paramInt2)
  {
    mLayerState.mChildren[paramInt1].mInsetE = paramInt2;
  }
  
  public void setLayerInsetLeft(int paramInt1, int paramInt2)
  {
    mLayerState.mChildren[paramInt1].mInsetL = paramInt2;
  }
  
  public void setLayerInsetRelative(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    setLayerInsetInternal(paramInt1, 0, paramInt3, 0, paramInt5, paramInt2, paramInt4);
  }
  
  public void setLayerInsetRight(int paramInt1, int paramInt2)
  {
    mLayerState.mChildren[paramInt1].mInsetR = paramInt2;
  }
  
  public void setLayerInsetStart(int paramInt1, int paramInt2)
  {
    mLayerState.mChildren[paramInt1].mInsetS = paramInt2;
  }
  
  public void setLayerInsetTop(int paramInt1, int paramInt2)
  {
    mLayerState.mChildren[paramInt1].mInsetT = paramInt2;
  }
  
  public void setLayerSize(int paramInt1, int paramInt2, int paramInt3)
  {
    ChildDrawable localChildDrawable = mLayerState.mChildren[paramInt1];
    mWidth = paramInt2;
    mHeight = paramInt3;
  }
  
  public void setLayerWidth(int paramInt1, int paramInt2)
  {
    mLayerState.mChildren[paramInt1].mWidth = paramInt2;
  }
  
  public void setOpacity(int paramInt)
  {
    mLayerState.mOpacityOverride = paramInt;
  }
  
  public void setPadding(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    LayerState localLayerState = mLayerState;
    mPaddingLeft = paramInt1;
    mPaddingTop = paramInt2;
    mPaddingRight = paramInt3;
    mPaddingBottom = paramInt4;
    mPaddingStart = -1;
    mPaddingEnd = -1;
  }
  
  public void setPaddingMode(int paramInt)
  {
    if (mLayerState.mPaddingMode != paramInt) {
      LayerState.access$202(mLayerState, paramInt);
    }
  }
  
  public void setPaddingRelative(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    LayerState localLayerState = mLayerState;
    mPaddingStart = paramInt1;
    mPaddingTop = paramInt2;
    mPaddingEnd = paramInt3;
    mPaddingBottom = paramInt4;
    mPaddingLeft = -1;
    mPaddingRight = -1;
  }
  
  public void setTintList(ColorStateList paramColorStateList)
  {
    ChildDrawable[] arrayOfChildDrawable = mLayerState.mChildren;
    int i = mLayerState.mNumChildren;
    for (int j = 0; j < i; j++)
    {
      Drawable localDrawable = mDrawable;
      if (localDrawable != null) {
        localDrawable.setTintList(paramColorStateList);
      }
    }
  }
  
  public void setTintMode(PorterDuff.Mode paramMode)
  {
    ChildDrawable[] arrayOfChildDrawable = mLayerState.mChildren;
    int i = mLayerState.mNumChildren;
    for (int j = 0; j < i; j++)
    {
      Drawable localDrawable = mDrawable;
      if (localDrawable != null) {
        localDrawable.setTintMode(paramMode);
      }
    }
  }
  
  public boolean setVisible(boolean paramBoolean1, boolean paramBoolean2)
  {
    boolean bool = super.setVisible(paramBoolean1, paramBoolean2);
    ChildDrawable[] arrayOfChildDrawable = mLayerState.mChildren;
    int i = mLayerState.mNumChildren;
    for (int j = 0; j < i; j++)
    {
      Drawable localDrawable = mDrawable;
      if (localDrawable != null) {
        localDrawable.setVisible(paramBoolean1, paramBoolean2);
      }
    }
    return bool;
  }
  
  public void unscheduleDrawable(Drawable paramDrawable, Runnable paramRunnable)
  {
    unscheduleSelf(paramRunnable);
  }
  
  static class ChildDrawable
  {
    public int mDensity = 160;
    public Drawable mDrawable;
    public int mGravity = 0;
    public int mHeight = -1;
    public int mId = -1;
    public int mInsetB;
    public int mInsetE = Integer.MIN_VALUE;
    public int mInsetL;
    public int mInsetR;
    public int mInsetS = Integer.MIN_VALUE;
    public int mInsetT;
    public int[] mThemeAttrs;
    public int mWidth = -1;
    
    ChildDrawable(int paramInt)
    {
      mDensity = paramInt;
    }
    
    ChildDrawable(ChildDrawable paramChildDrawable, LayerDrawable paramLayerDrawable, Resources paramResources)
    {
      Drawable localDrawable1 = mDrawable;
      Object localObject;
      if (localDrawable1 != null)
      {
        localObject = localDrawable1.getConstantState();
        if (localObject == null)
        {
          Drawable localDrawable2 = localDrawable1;
          localObject = localDrawable2;
          if (localDrawable1.getCallback() != null)
          {
            Log.w("LayerDrawable", "Invalid drawable added to LayerDrawable! Drawable already belongs to another owner but does not expose a constant state.", new RuntimeException());
            localObject = localDrawable2;
          }
        }
        else if (paramResources != null)
        {
          localObject = ((Drawable.ConstantState)localObject).newDrawable(paramResources);
        }
        else
        {
          localObject = ((Drawable.ConstantState)localObject).newDrawable();
        }
        ((Drawable)localObject).setLayoutDirection(localDrawable1.getLayoutDirection());
        ((Drawable)localObject).setBounds(localDrawable1.getBounds());
        ((Drawable)localObject).setLevel(localDrawable1.getLevel());
        ((Drawable)localObject).setCallback(paramLayerDrawable);
      }
      else
      {
        localObject = null;
      }
      mDrawable = ((Drawable)localObject);
      mThemeAttrs = mThemeAttrs;
      mInsetL = mInsetL;
      mInsetT = mInsetT;
      mInsetR = mInsetR;
      mInsetB = mInsetB;
      mInsetS = mInsetS;
      mInsetE = mInsetE;
      mWidth = mWidth;
      mHeight = mHeight;
      mGravity = mGravity;
      mId = mId;
      mDensity = Drawable.resolveDensity(paramResources, mDensity);
      if (mDensity != mDensity) {
        applyDensityScaling(mDensity, mDensity);
      }
    }
    
    private void applyDensityScaling(int paramInt1, int paramInt2)
    {
      mInsetL = Drawable.scaleFromDensity(mInsetL, paramInt1, paramInt2, false);
      mInsetT = Drawable.scaleFromDensity(mInsetT, paramInt1, paramInt2, false);
      mInsetR = Drawable.scaleFromDensity(mInsetR, paramInt1, paramInt2, false);
      mInsetB = Drawable.scaleFromDensity(mInsetB, paramInt1, paramInt2, false);
      if (mInsetS != Integer.MIN_VALUE) {
        mInsetS = Drawable.scaleFromDensity(mInsetS, paramInt1, paramInt2, false);
      }
      if (mInsetE != Integer.MIN_VALUE) {
        mInsetE = Drawable.scaleFromDensity(mInsetE, paramInt1, paramInt2, false);
      }
      if (mWidth > 0) {
        mWidth = Drawable.scaleFromDensity(mWidth, paramInt1, paramInt2, true);
      }
      if (mHeight > 0) {
        mHeight = Drawable.scaleFromDensity(mHeight, paramInt1, paramInt2, true);
      }
    }
    
    public boolean canApplyTheme()
    {
      boolean bool;
      if ((mThemeAttrs == null) && ((mDrawable == null) || (!mDrawable.canApplyTheme()))) {
        bool = false;
      } else {
        bool = true;
      }
      return bool;
    }
    
    public final void setDensity(int paramInt)
    {
      if (mDensity != paramInt)
      {
        int i = mDensity;
        mDensity = paramInt;
        applyDensityScaling(i, paramInt);
      }
    }
  }
  
  static class LayerState
    extends Drawable.ConstantState
  {
    private boolean mAutoMirrored;
    int mChangingConfigurations;
    private boolean mCheckedOpacity;
    private boolean mCheckedStateful;
    LayerDrawable.ChildDrawable[] mChildren;
    int mChildrenChangingConfigurations;
    int mDensity;
    private boolean mIsStateful;
    int mNumChildren;
    private int mOpacity;
    int mOpacityOverride;
    int mPaddingBottom = -1;
    int mPaddingEnd = -1;
    int mPaddingLeft = -1;
    private int mPaddingMode;
    int mPaddingRight = -1;
    int mPaddingStart = -1;
    int mPaddingTop = -1;
    private int[] mThemeAttrs;
    
    LayerState(LayerState paramLayerState, LayerDrawable paramLayerDrawable, Resources paramResources)
    {
      int i = 0;
      mOpacityOverride = 0;
      mAutoMirrored = false;
      mPaddingMode = 0;
      int j;
      if (paramLayerState != null) {
        j = mDensity;
      } else {
        j = 0;
      }
      mDensity = Drawable.resolveDensity(paramResources, j);
      if (paramLayerState != null)
      {
        LayerDrawable.ChildDrawable[] arrayOfChildDrawable = mChildren;
        int k = mNumChildren;
        mNumChildren = k;
        mChildren = new LayerDrawable.ChildDrawable[k];
        mChangingConfigurations = mChangingConfigurations;
        mChildrenChangingConfigurations = mChildrenChangingConfigurations;
        for (j = i; j < k; j++)
        {
          LayerDrawable.ChildDrawable localChildDrawable = arrayOfChildDrawable[j];
          mChildren[j] = new LayerDrawable.ChildDrawable(localChildDrawable, paramLayerDrawable, paramResources);
        }
        mCheckedOpacity = mCheckedOpacity;
        mOpacity = mOpacity;
        mCheckedStateful = mCheckedStateful;
        mIsStateful = mIsStateful;
        mAutoMirrored = mAutoMirrored;
        mPaddingMode = mPaddingMode;
        mThemeAttrs = mThemeAttrs;
        mPaddingTop = mPaddingTop;
        mPaddingBottom = mPaddingBottom;
        mPaddingLeft = mPaddingLeft;
        mPaddingRight = mPaddingRight;
        mPaddingStart = mPaddingStart;
        mPaddingEnd = mPaddingEnd;
        mOpacityOverride = mOpacityOverride;
        if (mDensity != mDensity) {
          applyDensityScaling(mDensity, mDensity);
        }
      }
      else
      {
        mNumChildren = 0;
        mChildren = null;
      }
    }
    
    private void applyDensityScaling(int paramInt1, int paramInt2)
    {
      if (mPaddingLeft > 0) {
        mPaddingLeft = Drawable.scaleFromDensity(mPaddingLeft, paramInt1, paramInt2, false);
      }
      if (mPaddingTop > 0) {
        mPaddingTop = Drawable.scaleFromDensity(mPaddingTop, paramInt1, paramInt2, false);
      }
      if (mPaddingRight > 0) {
        mPaddingRight = Drawable.scaleFromDensity(mPaddingRight, paramInt1, paramInt2, false);
      }
      if (mPaddingBottom > 0) {
        mPaddingBottom = Drawable.scaleFromDensity(mPaddingBottom, paramInt1, paramInt2, false);
      }
      if (mPaddingStart > 0) {
        mPaddingStart = Drawable.scaleFromDensity(mPaddingStart, paramInt1, paramInt2, false);
      }
      if (mPaddingEnd > 0) {
        mPaddingEnd = Drawable.scaleFromDensity(mPaddingEnd, paramInt1, paramInt2, false);
      }
    }
    
    public boolean canApplyTheme()
    {
      if ((mThemeAttrs == null) && (!super.canApplyTheme()))
      {
        LayerDrawable.ChildDrawable[] arrayOfChildDrawable = mChildren;
        int i = mNumChildren;
        for (int j = 0; j < i; j++) {
          if (arrayOfChildDrawable[j].canApplyTheme()) {
            return true;
          }
        }
        return false;
      }
      return true;
    }
    
    public final boolean canConstantState()
    {
      LayerDrawable.ChildDrawable[] arrayOfChildDrawable = mChildren;
      int i = mNumChildren;
      for (int j = 0; j < i; j++)
      {
        Drawable localDrawable = mDrawable;
        if ((localDrawable != null) && (localDrawable.getConstantState() == null)) {
          return false;
        }
      }
      return true;
    }
    
    public int getChangingConfigurations()
    {
      return mChangingConfigurations | mChildrenChangingConfigurations;
    }
    
    public final int getOpacity()
    {
      if (mCheckedOpacity) {
        return mOpacity;
      }
      int i = mNumChildren;
      LayerDrawable.ChildDrawable[] arrayOfChildDrawable = mChildren;
      int j = -1;
      int m;
      for (int k = 0;; k++)
      {
        m = j;
        if (k >= i) {
          break;
        }
        if (mDrawable != null)
        {
          m = k;
          break;
        }
      }
      if (m >= 0) {
        k = mDrawable.getOpacity();
      } else {
        k = -2;
      }
      m++;
      while (m < i)
      {
        Drawable localDrawable = mDrawable;
        j = k;
        if (localDrawable != null) {
          j = Drawable.resolveOpacity(k, localDrawable.getOpacity());
        }
        m++;
        k = j;
      }
      mOpacity = k;
      mCheckedOpacity = true;
      return k;
    }
    
    public final boolean hasFocusStateSpecified()
    {
      int i = mNumChildren;
      LayerDrawable.ChildDrawable[] arrayOfChildDrawable = mChildren;
      for (int j = 0; j < i; j++)
      {
        Drawable localDrawable = mDrawable;
        if ((localDrawable != null) && (localDrawable.hasFocusStateSpecified())) {
          return true;
        }
      }
      return false;
    }
    
    void invalidateCache()
    {
      mCheckedOpacity = false;
      mCheckedStateful = false;
    }
    
    public final boolean isStateful()
    {
      if (mCheckedStateful) {
        return mIsStateful;
      }
      int i = mNumChildren;
      LayerDrawable.ChildDrawable[] arrayOfChildDrawable = mChildren;
      boolean bool1 = false;
      boolean bool2;
      for (int j = 0;; j++)
      {
        bool2 = bool1;
        if (j >= i) {
          break;
        }
        Drawable localDrawable = mDrawable;
        if ((localDrawable != null) && (localDrawable.isStateful()))
        {
          bool2 = true;
          break;
        }
      }
      mIsStateful = bool2;
      mCheckedStateful = true;
      return bool2;
    }
    
    public Drawable newDrawable()
    {
      return new LayerDrawable(this, null);
    }
    
    public Drawable newDrawable(Resources paramResources)
    {
      return new LayerDrawable(this, paramResources);
    }
    
    protected void onDensityChanged(int paramInt1, int paramInt2)
    {
      applyDensityScaling(paramInt1, paramInt2);
    }
    
    public final void setDensity(int paramInt)
    {
      if (mDensity != paramInt)
      {
        int i = mDensity;
        mDensity = paramInt;
        onDensityChanged(i, paramInt);
      }
    }
  }
}
