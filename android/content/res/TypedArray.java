package android.content.res;

import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Pools.SynchronizedPool;
import android.util.TypedValue;
import com.android.internal.util.XmlUtils;
import dalvik.system.VMRuntime;
import java.util.Arrays;

public class TypedArray
{
  static final int STYLE_ASSET_COOKIE = 2;
  static final int STYLE_CHANGING_CONFIGURATIONS = 4;
  static final int STYLE_DATA = 1;
  static final int STYLE_DENSITY = 5;
  static final int STYLE_NUM_ENTRIES = 6;
  static final int STYLE_RESOURCE_ID = 3;
  static final int STYLE_TYPE = 0;
  private AssetManager mAssets;
  int[] mData;
  long mDataAddress;
  int[] mIndices;
  long mIndicesAddress;
  int mLength;
  private DisplayMetrics mMetrics;
  private boolean mRecycled;
  private final Resources mResources;
  Resources.Theme mTheme;
  TypedValue mValue = new TypedValue();
  XmlBlock.Parser mXml;
  
  protected TypedArray(Resources paramResources)
  {
    mResources = paramResources;
    mMetrics = mResources.getDisplayMetrics();
    mAssets = mResources.getAssets();
  }
  
  private boolean getValueAt(int paramInt, TypedValue paramTypedValue)
  {
    Object localObject = mData;
    int i = localObject[(paramInt + 0)];
    if (i == 0) {
      return false;
    }
    type = i;
    data = localObject[(paramInt + 1)];
    assetCookie = localObject[(paramInt + 2)];
    resourceId = localObject[(paramInt + 3)];
    changingConfigurations = ActivityInfo.activityInfoConfigNativeToJava(localObject[(paramInt + 4)]);
    density = localObject[(paramInt + 5)];
    if (i == 3) {
      localObject = loadStringValueAt(paramInt);
    } else {
      localObject = null;
    }
    string = ((CharSequence)localObject);
    return true;
  }
  
  private CharSequence loadStringValueAt(int paramInt)
  {
    int[] arrayOfInt = mData;
    int i = arrayOfInt[(paramInt + 2)];
    if (i < 0)
    {
      if (mXml != null) {
        return mXml.getPooledString(arrayOfInt[(paramInt + 1)]);
      }
      return null;
    }
    return mAssets.getPooledStringForCookie(i, arrayOfInt[(paramInt + 1)]);
  }
  
  static TypedArray obtain(Resources paramResources, int paramInt)
  {
    TypedArray localTypedArray1 = (TypedArray)mTypedArrayPool.acquire();
    TypedArray localTypedArray2 = localTypedArray1;
    if (localTypedArray1 == null) {
      localTypedArray2 = new TypedArray(paramResources);
    }
    mRecycled = false;
    mAssets = paramResources.getAssets();
    mMetrics = paramResources.getDisplayMetrics();
    localTypedArray2.resize(paramInt);
    return localTypedArray2;
  }
  
  private void resize(int paramInt)
  {
    mLength = paramInt;
    int i = paramInt * 6;
    VMRuntime localVMRuntime = VMRuntime.getRuntime();
    if ((mDataAddress == 0L) || (mData.length < i))
    {
      mData = ((int[])localVMRuntime.newNonMovableArray(Integer.TYPE, i));
      mDataAddress = localVMRuntime.addressOf(mData);
      mIndices = ((int[])localVMRuntime.newNonMovableArray(Integer.TYPE, paramInt + 1));
      mIndicesAddress = localVMRuntime.addressOf(mIndices);
    }
  }
  
  public int[] extractThemeAttrs()
  {
    return extractThemeAttrs(null);
  }
  
  public int[] extractThemeAttrs(int[] paramArrayOfInt)
  {
    if (!mRecycled)
    {
      int[] arrayOfInt = mData;
      int i = length();
      Object localObject1 = null;
      int j = 0;
      while (j < i)
      {
        int k = j * 6;
        Object localObject2;
        if (arrayOfInt[(k + 0)] != 2)
        {
          localObject2 = localObject1;
        }
        else
        {
          arrayOfInt[(k + 0)] = 0;
          k = arrayOfInt[(k + 1)];
          if (k == 0)
          {
            localObject2 = localObject1;
          }
          else
          {
            localObject2 = localObject1;
            if (localObject1 == null) {
              if ((paramArrayOfInt != null) && (paramArrayOfInt.length == i))
              {
                localObject2 = paramArrayOfInt;
                Arrays.fill((int[])localObject2, 0);
              }
              else
              {
                localObject2 = new int[i];
              }
            }
            localObject2[j] = k;
          }
        }
        j++;
        localObject1 = localObject2;
      }
      return localObject1;
    }
    throw new RuntimeException("Cannot make calls to a recycled instance!");
  }
  
  public boolean getBoolean(int paramInt, boolean paramBoolean)
  {
    if (!mRecycled)
    {
      int i = paramInt * 6;
      Object localObject = mData;
      paramInt = localObject[(i + 0)];
      if (paramInt == 0) {
        return paramBoolean;
      }
      if ((paramInt >= 16) && (paramInt <= 31))
      {
        if (localObject[(i + 1)] != 0) {
          paramBoolean = true;
        } else {
          paramBoolean = false;
        }
        return paramBoolean;
      }
      localObject = mValue;
      if (getValueAt(i, (TypedValue)localObject))
      {
        StrictMode.noteResourceMismatch(localObject);
        return XmlUtils.convertValueToBoolean(((TypedValue)localObject).coerceToString(), paramBoolean);
      }
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("getBoolean of bad type: 0x");
      ((StringBuilder)localObject).append(Integer.toHexString(paramInt));
      throw new RuntimeException(((StringBuilder)localObject).toString());
    }
    throw new RuntimeException("Cannot make calls to a recycled instance!");
  }
  
  public int getChangingConfigurations()
  {
    if (!mRecycled)
    {
      int i = 0;
      int[] arrayOfInt = mData;
      int j = length();
      for (int k = 0; k < j; k++)
      {
        int m = k * 6;
        if (arrayOfInt[(m + 0)] != 0) {
          i |= ActivityInfo.activityInfoConfigNativeToJava(arrayOfInt[(m + 4)]);
        }
      }
      return i;
    }
    throw new RuntimeException("Cannot make calls to a recycled instance!");
  }
  
  public int getColor(int paramInt1, int paramInt2)
  {
    if (!mRecycled)
    {
      int i = paramInt1 * 6;
      Object localObject = mData;
      int j = localObject[(i + 0)];
      if (j == 0) {
        return paramInt2;
      }
      if ((j >= 16) && (j <= 31)) {
        return localObject[(i + 1)];
      }
      if (j == 3)
      {
        localObject = mValue;
        if (getValueAt(i, (TypedValue)localObject)) {
          return mResources.loadColorStateList((TypedValue)localObject, resourceId, mTheme).getDefaultColor();
        }
        return paramInt2;
      }
      if (j == 2)
      {
        TypedValue localTypedValue = mValue;
        getValueAt(i, localTypedValue);
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Failed to resolve attribute at index ");
        ((StringBuilder)localObject).append(paramInt1);
        ((StringBuilder)localObject).append(": ");
        ((StringBuilder)localObject).append(localTypedValue);
        throw new UnsupportedOperationException(((StringBuilder)localObject).toString());
      }
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Can't convert value at index ");
      ((StringBuilder)localObject).append(paramInt1);
      ((StringBuilder)localObject).append(" to color: type=0x");
      ((StringBuilder)localObject).append(Integer.toHexString(j));
      throw new UnsupportedOperationException(((StringBuilder)localObject).toString());
    }
    throw new RuntimeException("Cannot make calls to a recycled instance!");
  }
  
  public ColorStateList getColorStateList(int paramInt)
  {
    if (!mRecycled)
    {
      TypedValue localTypedValue = mValue;
      if (getValueAt(paramInt * 6, localTypedValue))
      {
        if (type != 2) {
          return mResources.loadColorStateList(localTypedValue, resourceId, mTheme);
        }
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Failed to resolve attribute at index ");
        localStringBuilder.append(paramInt);
        localStringBuilder.append(": ");
        localStringBuilder.append(localTypedValue);
        throw new UnsupportedOperationException(localStringBuilder.toString());
      }
      return null;
    }
    throw new RuntimeException("Cannot make calls to a recycled instance!");
  }
  
  public ComplexColor getComplexColor(int paramInt)
  {
    if (!mRecycled)
    {
      TypedValue localTypedValue = mValue;
      if (getValueAt(paramInt * 6, localTypedValue))
      {
        if (type != 2) {
          return mResources.loadComplexColor(localTypedValue, resourceId, mTheme);
        }
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Failed to resolve attribute at index ");
        localStringBuilder.append(paramInt);
        localStringBuilder.append(": ");
        localStringBuilder.append(localTypedValue);
        throw new UnsupportedOperationException(localStringBuilder.toString());
      }
      return null;
    }
    throw new RuntimeException("Cannot make calls to a recycled instance!");
  }
  
  public float getDimension(int paramInt, float paramFloat)
  {
    if (!mRecycled)
    {
      int i = paramInt * 6;
      Object localObject = mData;
      int j = localObject[(i + 0)];
      if (j == 0) {
        return paramFloat;
      }
      if (j == 5) {
        return TypedValue.complexToDimension(localObject[(i + 1)], mMetrics);
      }
      if (j == 2)
      {
        TypedValue localTypedValue = mValue;
        getValueAt(i, localTypedValue);
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Failed to resolve attribute at index ");
        ((StringBuilder)localObject).append(paramInt);
        ((StringBuilder)localObject).append(": ");
        ((StringBuilder)localObject).append(localTypedValue);
        throw new UnsupportedOperationException(((StringBuilder)localObject).toString());
      }
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Can't convert value at index ");
      ((StringBuilder)localObject).append(paramInt);
      ((StringBuilder)localObject).append(" to dimension: type=0x");
      ((StringBuilder)localObject).append(Integer.toHexString(j));
      throw new UnsupportedOperationException(((StringBuilder)localObject).toString());
    }
    throw new RuntimeException("Cannot make calls to a recycled instance!");
  }
  
  public int getDimensionPixelOffset(int paramInt1, int paramInt2)
  {
    if (!mRecycled)
    {
      int i = paramInt1 * 6;
      Object localObject = mData;
      int j = localObject[(i + 0)];
      if (j == 0) {
        return paramInt2;
      }
      if (j == 5) {
        return TypedValue.complexToDimensionPixelOffset(localObject[(i + 1)], mMetrics);
      }
      if (j == 2)
      {
        TypedValue localTypedValue = mValue;
        getValueAt(i, localTypedValue);
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Failed to resolve attribute at index ");
        ((StringBuilder)localObject).append(paramInt1);
        ((StringBuilder)localObject).append(": ");
        ((StringBuilder)localObject).append(localTypedValue);
        throw new UnsupportedOperationException(((StringBuilder)localObject).toString());
      }
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Can't convert value at index ");
      ((StringBuilder)localObject).append(paramInt1);
      ((StringBuilder)localObject).append(" to dimension: type=0x");
      ((StringBuilder)localObject).append(Integer.toHexString(j));
      throw new UnsupportedOperationException(((StringBuilder)localObject).toString());
    }
    throw new RuntimeException("Cannot make calls to a recycled instance!");
  }
  
  public int getDimensionPixelSize(int paramInt1, int paramInt2)
  {
    if (!mRecycled)
    {
      int i = paramInt1 * 6;
      Object localObject = mData;
      int j = localObject[(i + 0)];
      if (j == 0) {
        return paramInt2;
      }
      if (j == 5) {
        return TypedValue.complexToDimensionPixelSize(localObject[(i + 1)], mMetrics);
      }
      if (j == 2)
      {
        TypedValue localTypedValue = mValue;
        getValueAt(i, localTypedValue);
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Failed to resolve attribute at index ");
        ((StringBuilder)localObject).append(paramInt1);
        ((StringBuilder)localObject).append(": ");
        ((StringBuilder)localObject).append(localTypedValue);
        throw new UnsupportedOperationException(((StringBuilder)localObject).toString());
      }
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Can't convert value at index ");
      ((StringBuilder)localObject).append(paramInt1);
      ((StringBuilder)localObject).append(" to dimension: type=0x");
      ((StringBuilder)localObject).append(Integer.toHexString(j));
      throw new UnsupportedOperationException(((StringBuilder)localObject).toString());
    }
    throw new RuntimeException("Cannot make calls to a recycled instance!");
  }
  
  public Drawable getDrawable(int paramInt)
  {
    return getDrawableForDensity(paramInt, 0);
  }
  
  public Drawable getDrawableForDensity(int paramInt1, int paramInt2)
  {
    if (!mRecycled)
    {
      TypedValue localTypedValue = mValue;
      if (getValueAt(paramInt1 * 6, localTypedValue))
      {
        if (type != 2)
        {
          if (paramInt2 > 0) {
            mResources.getValueForDensity(resourceId, paramInt2, localTypedValue, true);
          }
          return mResources.loadDrawable(localTypedValue, resourceId, paramInt2, mTheme);
        }
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Failed to resolve attribute at index ");
        localStringBuilder.append(paramInt1);
        localStringBuilder.append(": ");
        localStringBuilder.append(localTypedValue);
        throw new UnsupportedOperationException(localStringBuilder.toString());
      }
      return null;
    }
    throw new RuntimeException("Cannot make calls to a recycled instance!");
  }
  
  public float getFloat(int paramInt, float paramFloat)
  {
    if (!mRecycled)
    {
      paramInt *= 6;
      Object localObject = mData;
      int i = localObject[(paramInt + 0)];
      if (i == 0) {
        return paramFloat;
      }
      if (i == 4) {
        return Float.intBitsToFloat(localObject[(paramInt + 1)]);
      }
      if ((i >= 16) && (i <= 31)) {
        return localObject[(paramInt + 1)];
      }
      TypedValue localTypedValue = mValue;
      if (getValueAt(paramInt, localTypedValue))
      {
        localObject = localTypedValue.coerceToString();
        if (localObject != null)
        {
          StrictMode.noteResourceMismatch(localTypedValue);
          return Float.parseFloat(((CharSequence)localObject).toString());
        }
      }
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("getFloat of bad type: 0x");
      ((StringBuilder)localObject).append(Integer.toHexString(i));
      throw new RuntimeException(((StringBuilder)localObject).toString());
    }
    throw new RuntimeException("Cannot make calls to a recycled instance!");
  }
  
  public Typeface getFont(int paramInt)
  {
    if (!mRecycled)
    {
      TypedValue localTypedValue = mValue;
      if (getValueAt(paramInt * 6, localTypedValue))
      {
        if (type != 2) {
          return mResources.getFont(localTypedValue, resourceId);
        }
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Failed to resolve attribute at index ");
        localStringBuilder.append(paramInt);
        localStringBuilder.append(": ");
        localStringBuilder.append(localTypedValue);
        throw new UnsupportedOperationException(localStringBuilder.toString());
      }
      return null;
    }
    throw new RuntimeException("Cannot make calls to a recycled instance!");
  }
  
  public float getFraction(int paramInt1, int paramInt2, int paramInt3, float paramFloat)
  {
    if (!mRecycled)
    {
      int i = paramInt1 * 6;
      Object localObject = mData;
      int j = localObject[(i + 0)];
      if (j == 0) {
        return paramFloat;
      }
      if (j == 6) {
        return TypedValue.complexToFraction(localObject[(i + 1)], paramInt2, paramInt3);
      }
      if (j == 2)
      {
        localObject = mValue;
        getValueAt(i, (TypedValue)localObject);
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Failed to resolve attribute at index ");
        localStringBuilder.append(paramInt1);
        localStringBuilder.append(": ");
        localStringBuilder.append(localObject);
        throw new UnsupportedOperationException(localStringBuilder.toString());
      }
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Can't convert value at index ");
      ((StringBuilder)localObject).append(paramInt1);
      ((StringBuilder)localObject).append(" to fraction: type=0x");
      ((StringBuilder)localObject).append(Integer.toHexString(j));
      throw new UnsupportedOperationException(((StringBuilder)localObject).toString());
    }
    throw new RuntimeException("Cannot make calls to a recycled instance!");
  }
  
  public int getIndex(int paramInt)
  {
    if (!mRecycled) {
      return mIndices[(1 + paramInt)];
    }
    throw new RuntimeException("Cannot make calls to a recycled instance!");
  }
  
  public int getIndexCount()
  {
    if (!mRecycled) {
      return mIndices[0];
    }
    throw new RuntimeException("Cannot make calls to a recycled instance!");
  }
  
  public int getInt(int paramInt1, int paramInt2)
  {
    if (!mRecycled)
    {
      paramInt1 *= 6;
      Object localObject = mData;
      int i = localObject[(paramInt1 + 0)];
      if (i == 0) {
        return paramInt2;
      }
      if ((i >= 16) && (i <= 31)) {
        return localObject[(paramInt1 + 1)];
      }
      localObject = mValue;
      if (getValueAt(paramInt1, (TypedValue)localObject))
      {
        StrictMode.noteResourceMismatch(localObject);
        return XmlUtils.convertValueToInt(((TypedValue)localObject).coerceToString(), paramInt2);
      }
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("getInt of bad type: 0x");
      ((StringBuilder)localObject).append(Integer.toHexString(i));
      throw new RuntimeException(((StringBuilder)localObject).toString());
    }
    throw new RuntimeException("Cannot make calls to a recycled instance!");
  }
  
  public int getInteger(int paramInt1, int paramInt2)
  {
    if (!mRecycled)
    {
      int i = paramInt1 * 6;
      Object localObject = mData;
      int j = localObject[(i + 0)];
      if (j == 0) {
        return paramInt2;
      }
      if ((j >= 16) && (j <= 31)) {
        return localObject[(i + 1)];
      }
      if (j == 2)
      {
        localObject = mValue;
        getValueAt(i, (TypedValue)localObject);
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Failed to resolve attribute at index ");
        localStringBuilder.append(paramInt1);
        localStringBuilder.append(": ");
        localStringBuilder.append(localObject);
        throw new UnsupportedOperationException(localStringBuilder.toString());
      }
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Can't convert value at index ");
      ((StringBuilder)localObject).append(paramInt1);
      ((StringBuilder)localObject).append(" to integer: type=0x");
      ((StringBuilder)localObject).append(Integer.toHexString(j));
      throw new UnsupportedOperationException(((StringBuilder)localObject).toString());
    }
    throw new RuntimeException("Cannot make calls to a recycled instance!");
  }
  
  public int getLayoutDimension(int paramInt1, int paramInt2)
  {
    if (!mRecycled)
    {
      int i = paramInt1 * 6;
      int[] arrayOfInt = mData;
      paramInt1 = arrayOfInt[(i + 0)];
      if ((paramInt1 >= 16) && (paramInt1 <= 31)) {
        return arrayOfInt[(i + 1)];
      }
      if (paramInt1 == 5) {
        return TypedValue.complexToDimensionPixelSize(arrayOfInt[(i + 1)], mMetrics);
      }
      return paramInt2;
    }
    throw new RuntimeException("Cannot make calls to a recycled instance!");
  }
  
  public int getLayoutDimension(int paramInt, String paramString)
  {
    if (!mRecycled)
    {
      int i = paramInt * 6;
      Object localObject = mData;
      int j = localObject[(i + 0)];
      if ((j >= 16) && (j <= 31)) {
        return localObject[(i + 1)];
      }
      if (j == 5) {
        return TypedValue.complexToDimensionPixelSize(localObject[(i + 1)], mMetrics);
      }
      if (j == 2)
      {
        paramString = mValue;
        getValueAt(i, paramString);
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Failed to resolve attribute at index ");
        ((StringBuilder)localObject).append(paramInt);
        ((StringBuilder)localObject).append(": ");
        ((StringBuilder)localObject).append(paramString);
        throw new UnsupportedOperationException(((StringBuilder)localObject).toString());
      }
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(getPositionDescription());
      ((StringBuilder)localObject).append(": You must supply a ");
      ((StringBuilder)localObject).append(paramString);
      ((StringBuilder)localObject).append(" attribute.");
      throw new UnsupportedOperationException(((StringBuilder)localObject).toString());
    }
    throw new RuntimeException("Cannot make calls to a recycled instance!");
  }
  
  public String getNonConfigurationString(int paramInt1, int paramInt2)
  {
    if (!mRecycled)
    {
      int i = paramInt1 * 6;
      Object localObject1 = mData;
      int j = localObject1[(i + 0)];
      paramInt1 = ActivityInfo.activityInfoConfigNativeToJava(localObject1[(i + 4)]);
      localObject1 = null;
      if ((paramInt2 & paramInt1) != 0) {
        return null;
      }
      if (j == 0) {
        return null;
      }
      if (j == 3) {
        return loadStringValueAt(i).toString();
      }
      Object localObject2 = mValue;
      if (getValueAt(i, (TypedValue)localObject2))
      {
        localObject2 = ((TypedValue)localObject2).coerceToString();
        if (localObject2 != null) {
          localObject1 = ((CharSequence)localObject2).toString();
        }
        return localObject1;
      }
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("getNonConfigurationString of bad type: 0x");
      ((StringBuilder)localObject1).append(Integer.toHexString(j));
      throw new RuntimeException(((StringBuilder)localObject1).toString());
    }
    throw new RuntimeException("Cannot make calls to a recycled instance!");
  }
  
  public String getNonResourceString(int paramInt)
  {
    if (!mRecycled)
    {
      paramInt *= 6;
      int[] arrayOfInt = mData;
      if ((arrayOfInt[(paramInt + 0)] == 3) && (arrayOfInt[(paramInt + 2)] < 0)) {
        return mXml.getPooledString(arrayOfInt[(paramInt + 1)]).toString();
      }
      return null;
    }
    throw new RuntimeException("Cannot make calls to a recycled instance!");
  }
  
  public String getPositionDescription()
  {
    if (!mRecycled)
    {
      String str;
      if (mXml != null) {
        str = mXml.getPositionDescription();
      } else {
        str = "<internal>";
      }
      return str;
    }
    throw new RuntimeException("Cannot make calls to a recycled instance!");
  }
  
  public int getResourceId(int paramInt1, int paramInt2)
  {
    if (!mRecycled)
    {
      paramInt1 *= 6;
      int[] arrayOfInt = mData;
      if (arrayOfInt[(paramInt1 + 0)] != 0)
      {
        paramInt1 = arrayOfInt[(paramInt1 + 3)];
        if (paramInt1 != 0) {
          return paramInt1;
        }
      }
      return paramInt2;
    }
    throw new RuntimeException("Cannot make calls to a recycled instance!");
  }
  
  public Resources getResources()
  {
    if (!mRecycled) {
      return mResources;
    }
    throw new RuntimeException("Cannot make calls to a recycled instance!");
  }
  
  public String getString(int paramInt)
  {
    if (!mRecycled)
    {
      paramInt *= 6;
      int i = mData[(paramInt + 0)];
      Object localObject1 = null;
      if (i == 0) {
        return null;
      }
      if (i == 3) {
        return loadStringValueAt(paramInt).toString();
      }
      Object localObject2 = mValue;
      if (getValueAt(paramInt, (TypedValue)localObject2))
      {
        localObject2 = ((TypedValue)localObject2).coerceToString();
        if (localObject2 != null) {
          localObject1 = ((CharSequence)localObject2).toString();
        }
        return localObject1;
      }
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("getString of bad type: 0x");
      ((StringBuilder)localObject1).append(Integer.toHexString(i));
      throw new RuntimeException(((StringBuilder)localObject1).toString());
    }
    throw new RuntimeException("Cannot make calls to a recycled instance!");
  }
  
  public CharSequence getText(int paramInt)
  {
    if (!mRecycled)
    {
      paramInt *= 6;
      int i = mData[(paramInt + 0)];
      if (i == 0) {
        return null;
      }
      if (i == 3) {
        return loadStringValueAt(paramInt);
      }
      Object localObject = mValue;
      if (getValueAt(paramInt, (TypedValue)localObject)) {
        return ((TypedValue)localObject).coerceToString();
      }
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("getText of bad type: 0x");
      ((StringBuilder)localObject).append(Integer.toHexString(i));
      throw new RuntimeException(((StringBuilder)localObject).toString());
    }
    throw new RuntimeException("Cannot make calls to a recycled instance!");
  }
  
  public CharSequence[] getTextArray(int paramInt)
  {
    if (!mRecycled)
    {
      TypedValue localTypedValue = mValue;
      if (getValueAt(paramInt * 6, localTypedValue)) {
        return mResources.getTextArray(resourceId);
      }
      return null;
    }
    throw new RuntimeException("Cannot make calls to a recycled instance!");
  }
  
  public int getThemeAttributeId(int paramInt1, int paramInt2)
  {
    if (!mRecycled)
    {
      paramInt1 *= 6;
      int[] arrayOfInt = mData;
      if (arrayOfInt[(paramInt1 + 0)] == 2) {
        return arrayOfInt[(paramInt1 + 1)];
      }
      return paramInt2;
    }
    throw new RuntimeException("Cannot make calls to a recycled instance!");
  }
  
  public int getType(int paramInt)
  {
    if (!mRecycled) {
      return mData[(paramInt * 6 + 0)];
    }
    throw new RuntimeException("Cannot make calls to a recycled instance!");
  }
  
  public boolean getValue(int paramInt, TypedValue paramTypedValue)
  {
    if (!mRecycled) {
      return getValueAt(paramInt * 6, paramTypedValue);
    }
    throw new RuntimeException("Cannot make calls to a recycled instance!");
  }
  
  public boolean hasValue(int paramInt)
  {
    if (!mRecycled)
    {
      boolean bool;
      if (mData[(paramInt * 6 + 0)] != 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    throw new RuntimeException("Cannot make calls to a recycled instance!");
  }
  
  public boolean hasValueOrEmpty(int paramInt)
  {
    if (!mRecycled)
    {
      paramInt *= 6;
      int[] arrayOfInt = mData;
      int i = arrayOfInt[(paramInt + 0)];
      boolean bool1 = true;
      boolean bool2 = bool1;
      if (i == 0) {
        if (arrayOfInt[(paramInt + 1)] == 1) {
          bool2 = bool1;
        } else {
          bool2 = false;
        }
      }
      return bool2;
    }
    throw new RuntimeException("Cannot make calls to a recycled instance!");
  }
  
  public int length()
  {
    if (!mRecycled) {
      return mLength;
    }
    throw new RuntimeException("Cannot make calls to a recycled instance!");
  }
  
  public TypedValue peekValue(int paramInt)
  {
    if (!mRecycled)
    {
      TypedValue localTypedValue = mValue;
      if (getValueAt(paramInt * 6, localTypedValue)) {
        return localTypedValue;
      }
      return null;
    }
    throw new RuntimeException("Cannot make calls to a recycled instance!");
  }
  
  public void recycle()
  {
    if (!mRecycled)
    {
      mRecycled = true;
      mXml = null;
      mTheme = null;
      mAssets = null;
      mResources.mTypedArrayPool.release(this);
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(toString());
    localStringBuilder.append(" recycled twice!");
    throw new RuntimeException(localStringBuilder.toString());
  }
  
  public String toString()
  {
    return Arrays.toString(mData);
  }
}
