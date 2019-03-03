package android.content.res;

import android.util.ArrayMap;
import android.util.LongSparseArray;
import java.lang.ref.WeakReference;

abstract class ThemedResourceCache<T>
{
  private LongSparseArray<WeakReference<T>> mNullThemedEntries;
  private ArrayMap<Resources.ThemeKey, LongSparseArray<WeakReference<T>>> mThemedEntries;
  private LongSparseArray<WeakReference<T>> mUnthemedEntries;
  
  ThemedResourceCache() {}
  
  private LongSparseArray<WeakReference<T>> getThemedLocked(Resources.Theme paramTheme, boolean paramBoolean)
  {
    if (paramTheme == null)
    {
      if ((mNullThemedEntries == null) && (paramBoolean)) {
        mNullThemedEntries = new LongSparseArray(1);
      }
      return mNullThemedEntries;
    }
    if (mThemedEntries == null) {
      if (paramBoolean) {
        mThemedEntries = new ArrayMap(1);
      } else {
        return null;
      }
    }
    Resources.ThemeKey localThemeKey = paramTheme.getKey();
    Object localObject = (LongSparseArray)mThemedEntries.get(localThemeKey);
    paramTheme = (Resources.Theme)localObject;
    if (localObject == null)
    {
      paramTheme = (Resources.Theme)localObject;
      if (paramBoolean)
      {
        paramTheme = new LongSparseArray(1);
        localObject = localThemeKey.clone();
        mThemedEntries.put(localObject, paramTheme);
      }
    }
    return paramTheme;
  }
  
  private LongSparseArray<WeakReference<T>> getUnthemedLocked(boolean paramBoolean)
  {
    if ((mUnthemedEntries == null) && (paramBoolean)) {
      mUnthemedEntries = new LongSparseArray(1);
    }
    return mUnthemedEntries;
  }
  
  private boolean prune(int paramInt)
  {
    try
    {
      ArrayMap localArrayMap = mThemedEntries;
      boolean bool = true;
      if (localArrayMap != null) {
        for (int i = mThemedEntries.size() - 1; i >= 0; i--) {
          if (pruneEntriesLocked((LongSparseArray)mThemedEntries.valueAt(i), paramInt)) {
            mThemedEntries.removeAt(i);
          }
        }
      }
      pruneEntriesLocked(mNullThemedEntries, paramInt);
      pruneEntriesLocked(mUnthemedEntries, paramInt);
      if ((mThemedEntries != null) || (mNullThemedEntries != null) || (mUnthemedEntries != null)) {
        bool = false;
      }
      return bool;
    }
    finally {}
  }
  
  private boolean pruneEntriesLocked(LongSparseArray<WeakReference<T>> paramLongSparseArray, int paramInt)
  {
    boolean bool = true;
    if (paramLongSparseArray == null) {
      return true;
    }
    for (int i = paramLongSparseArray.size() - 1; i >= 0; i--)
    {
      WeakReference localWeakReference = (WeakReference)paramLongSparseArray.valueAt(i);
      if ((localWeakReference == null) || (pruneEntryLocked(localWeakReference.get(), paramInt))) {
        paramLongSparseArray.removeAt(i);
      }
    }
    if (paramLongSparseArray.size() != 0) {
      bool = false;
    }
    return bool;
  }
  
  private boolean pruneEntryLocked(T paramT, int paramInt)
  {
    boolean bool;
    if ((paramT != null) && ((paramInt == 0) || (!shouldInvalidateEntry(paramT, paramInt)))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public T get(long paramLong, Resources.Theme paramTheme)
  {
    try
    {
      paramTheme = getThemedLocked(paramTheme, false);
      if (paramTheme != null)
      {
        paramTheme = (WeakReference)paramTheme.get(paramLong);
        if (paramTheme != null)
        {
          paramTheme = paramTheme.get();
          return paramTheme;
        }
      }
      paramTheme = getUnthemedLocked(false);
      if (paramTheme != null)
      {
        paramTheme = (WeakReference)paramTheme.get(paramLong);
        if (paramTheme != null)
        {
          paramTheme = paramTheme.get();
          return paramTheme;
        }
      }
      return null;
    }
    finally {}
  }
  
  public void onConfigurationChange(int paramInt)
  {
    prune(paramInt);
  }
  
  public void put(long paramLong, Resources.Theme paramTheme, T paramT)
  {
    put(paramLong, paramTheme, paramT, true);
  }
  
  public void put(long paramLong, Resources.Theme paramTheme, T paramT, boolean paramBoolean)
  {
    if (paramT == null) {
      return;
    }
    if (!paramBoolean) {
      try
      {
        paramTheme = getUnthemedLocked(true);
      }
      finally
      {
        break label59;
      }
    } else {
      paramTheme = getThemedLocked(paramTheme, true);
    }
    if (paramTheme != null)
    {
      WeakReference localWeakReference = new java/lang/ref/WeakReference;
      localWeakReference.<init>(paramT);
      paramTheme.put(paramLong, localWeakReference);
    }
    return;
    label59:
    throw paramTheme;
  }
  
  protected abstract boolean shouldInvalidateEntry(T paramT, int paramInt);
}
