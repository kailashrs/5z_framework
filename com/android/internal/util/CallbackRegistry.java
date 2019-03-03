package com.android.internal.util;

import java.util.ArrayList;
import java.util.List;

public class CallbackRegistry<C, T, A>
  implements Cloneable
{
  private static final String TAG = "CallbackRegistry";
  private List<C> mCallbacks = new ArrayList();
  private long mFirst64Removed = 0L;
  private int mNotificationLevel;
  private final NotifierCallback<C, T, A> mNotifier;
  private long[] mRemainderRemoved;
  
  public CallbackRegistry(NotifierCallback<C, T, A> paramNotifierCallback)
  {
    mNotifier = paramNotifierCallback;
  }
  
  private boolean isRemovedLocked(int paramInt)
  {
    boolean bool1 = true;
    boolean bool2 = true;
    if (paramInt < 64)
    {
      if ((mFirst64Removed & 1L << paramInt) == 0L) {
        bool2 = false;
      }
      return bool2;
    }
    if (mRemainderRemoved == null) {
      return false;
    }
    int i = paramInt / 64 - 1;
    if (i >= mRemainderRemoved.length) {
      return false;
    }
    if ((mRemainderRemoved[i] & 1L << paramInt % 64) != 0L) {
      bool2 = bool1;
    } else {
      bool2 = false;
    }
    return bool2;
  }
  
  private void notifyCallbacksLocked(T paramT, int paramInt1, A paramA, int paramInt2, int paramInt3, long paramLong)
  {
    long l = 1L;
    while (paramInt2 < paramInt3)
    {
      if ((paramLong & l) == 0L) {
        mNotifier.onNotifyCallback(mCallbacks.get(paramInt2), paramT, paramInt1, paramA);
      }
      l <<= 1;
      paramInt2++;
    }
  }
  
  private void notifyFirst64Locked(T paramT, int paramInt, A paramA)
  {
    notifyCallbacksLocked(paramT, paramInt, paramA, 0, Math.min(64, mCallbacks.size()), mFirst64Removed);
  }
  
  private void notifyRecurseLocked(T paramT, int paramInt, A paramA)
  {
    int i = mCallbacks.size();
    int j;
    if (mRemainderRemoved == null) {
      j = -1;
    } else {
      j = mRemainderRemoved.length - 1;
    }
    notifyRemainderLocked(paramT, paramInt, paramA, j);
    notifyCallbacksLocked(paramT, paramInt, paramA, (j + 2) * 64, i, 0L);
  }
  
  private void notifyRemainderLocked(T paramT, int paramInt1, A paramA, int paramInt2)
  {
    if (paramInt2 < 0)
    {
      notifyFirst64Locked(paramT, paramInt1, paramA);
    }
    else
    {
      long l = mRemainderRemoved[paramInt2];
      int i = (paramInt2 + 1) * 64;
      int j = Math.min(mCallbacks.size(), i + 64);
      notifyRemainderLocked(paramT, paramInt1, paramA, paramInt2 - 1);
      notifyCallbacksLocked(paramT, paramInt1, paramA, i, j, l);
    }
  }
  
  private void removeRemovedCallbacks(int paramInt, long paramLong)
  {
    long l = Long.MIN_VALUE;
    for (int i = paramInt + 64 - 1; i >= paramInt; i--)
    {
      if ((paramLong & l) != 0L) {
        mCallbacks.remove(i);
      }
      l >>>= 1;
    }
  }
  
  private void setRemovalBitLocked(int paramInt)
  {
    if (paramInt < 64)
    {
      mFirst64Removed |= 1L << paramInt;
    }
    else
    {
      int i = paramInt / 64 - 1;
      if (mRemainderRemoved == null)
      {
        mRemainderRemoved = new long[mCallbacks.size() / 64];
      }
      else if (mRemainderRemoved.length < i)
      {
        arrayOfLong = new long[mCallbacks.size() / 64];
        System.arraycopy(mRemainderRemoved, 0, arrayOfLong, 0, mRemainderRemoved.length);
        mRemainderRemoved = arrayOfLong;
      }
      long[] arrayOfLong = mRemainderRemoved;
      arrayOfLong[i] |= 1L << paramInt % 64;
    }
  }
  
  public void add(C paramC)
  {
    try
    {
      int i = mCallbacks.lastIndexOf(paramC);
      if ((i < 0) || (isRemovedLocked(i))) {
        mCallbacks.add(paramC);
      }
      return;
    }
    finally {}
  }
  
  public void clear()
  {
    try
    {
      if (mNotificationLevel == 0) {
        mCallbacks.clear();
      } else if (!mCallbacks.isEmpty()) {
        for (int i = mCallbacks.size() - 1; i >= 0; i--) {
          setRemovalBitLocked(i);
        }
      }
      return;
    }
    finally {}
  }
  
  /* Error */
  public CallbackRegistry<C, T, A> clone()
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aconst_null
    //   3: astore_1
    //   4: aload_0
    //   5: invokespecial 128	java/lang/Object:clone	()Ljava/lang/Object;
    //   8: checkcast 2	com/android/internal/util/CallbackRegistry
    //   11: astore_2
    //   12: aload_2
    //   13: astore_1
    //   14: aload_2
    //   15: lconst_0
    //   16: putfield 38	com/android/internal/util/CallbackRegistry:mFirst64Removed	J
    //   19: aload_2
    //   20: astore_1
    //   21: aload_2
    //   22: aconst_null
    //   23: putfield 47	com/android/internal/util/CallbackRegistry:mRemainderRemoved	[J
    //   26: iconst_0
    //   27: istore_3
    //   28: aload_2
    //   29: astore_1
    //   30: aload_2
    //   31: iconst_0
    //   32: putfield 113	com/android/internal/util/CallbackRegistry:mNotificationLevel	I
    //   35: aload_2
    //   36: astore_1
    //   37: new 33	java/util/ArrayList
    //   40: astore 4
    //   42: aload_2
    //   43: astore_1
    //   44: aload 4
    //   46: invokespecial 34	java/util/ArrayList:<init>	()V
    //   49: aload_2
    //   50: astore_1
    //   51: aload_2
    //   52: aload 4
    //   54: putfield 36	com/android/internal/util/CallbackRegistry:mCallbacks	Ljava/util/List;
    //   57: aload_2
    //   58: astore_1
    //   59: aload_0
    //   60: getfield 36	com/android/internal/util/CallbackRegistry:mCallbacks	Ljava/util/List;
    //   63: invokeinterface 66 1 0
    //   68: istore 5
    //   70: iload_3
    //   71: iload 5
    //   73: if_icmpge +41 -> 114
    //   76: aload_2
    //   77: astore_1
    //   78: aload_0
    //   79: iload_3
    //   80: invokespecial 106	com/android/internal/util/CallbackRegistry:isRemovedLocked	(I)Z
    //   83: ifne +25 -> 108
    //   86: aload_2
    //   87: astore_1
    //   88: aload_2
    //   89: getfield 36	com/android/internal/util/CallbackRegistry:mCallbacks	Ljava/util/List;
    //   92: aload_0
    //   93: getfield 36	com/android/internal/util/CallbackRegistry:mCallbacks	Ljava/util/List;
    //   96: iload_3
    //   97: invokeinterface 55 2 0
    //   102: invokeinterface 109 2 0
    //   107: pop
    //   108: iinc 3 1
    //   111: goto -41 -> 70
    //   114: aload_2
    //   115: astore_1
    //   116: goto +12 -> 128
    //   119: astore_1
    //   120: goto +12 -> 132
    //   123: astore_2
    //   124: aload_2
    //   125: invokevirtual 131	java/lang/CloneNotSupportedException:printStackTrace	()V
    //   128: aload_0
    //   129: monitorexit
    //   130: aload_1
    //   131: areturn
    //   132: aload_0
    //   133: monitorexit
    //   134: aload_1
    //   135: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	136	0	this	CallbackRegistry
    //   3	113	1	localObject	Object
    //   119	16	1	localCallbackRegistry	CallbackRegistry<C, T, A>
    //   11	104	2	localCallbackRegistry1	CallbackRegistry
    //   123	2	2	localCloneNotSupportedException	CloneNotSupportedException
    //   27	82	3	i	int
    //   40	13	4	localArrayList	ArrayList
    //   68	6	5	j	int
    // Exception table:
    //   from	to	target	type
    //   4	12	119	finally
    //   14	19	119	finally
    //   21	26	119	finally
    //   30	35	119	finally
    //   37	42	119	finally
    //   44	49	119	finally
    //   51	57	119	finally
    //   59	70	119	finally
    //   78	86	119	finally
    //   88	108	119	finally
    //   124	128	119	finally
    //   4	12	123	java/lang/CloneNotSupportedException
    //   14	19	123	java/lang/CloneNotSupportedException
    //   21	26	123	java/lang/CloneNotSupportedException
    //   30	35	123	java/lang/CloneNotSupportedException
    //   37	42	123	java/lang/CloneNotSupportedException
    //   44	49	123	java/lang/CloneNotSupportedException
    //   51	57	123	java/lang/CloneNotSupportedException
    //   59	70	123	java/lang/CloneNotSupportedException
    //   78	86	123	java/lang/CloneNotSupportedException
    //   88	108	123	java/lang/CloneNotSupportedException
  }
  
  public ArrayList<C> copyListeners()
  {
    try
    {
      ArrayList localArrayList = new java/util/ArrayList;
      localArrayList.<init>(mCallbacks.size());
      int i = mCallbacks.size();
      for (int j = 0; j < i; j++) {
        if (!isRemovedLocked(j)) {
          localArrayList.add(mCallbacks.get(j));
        }
      }
      return localArrayList;
    }
    finally {}
  }
  
  public boolean isEmpty()
  {
    try
    {
      boolean bool = mCallbacks.isEmpty();
      if (bool) {
        return true;
      }
      int i = mNotificationLevel;
      if (i == 0) {
        return false;
      }
      int j = mCallbacks.size();
      for (i = 0; i < j; i++)
      {
        bool = isRemovedLocked(i);
        if (!bool) {
          return false;
        }
      }
      return true;
    }
    finally {}
  }
  
  public void notifyCallbacks(T paramT, int paramInt, A paramA)
  {
    try
    {
      mNotificationLevel += 1;
      notifyRecurseLocked(paramT, paramInt, paramA);
      mNotificationLevel -= 1;
      if (mNotificationLevel == 0)
      {
        if (mRemainderRemoved != null) {
          for (paramInt = mRemainderRemoved.length - 1; paramInt >= 0; paramInt--)
          {
            long l = mRemainderRemoved[paramInt];
            if (l != 0L)
            {
              removeRemovedCallbacks((paramInt + 1) * 64, l);
              mRemainderRemoved[paramInt] = 0L;
            }
          }
        }
        if (mFirst64Removed != 0L)
        {
          removeRemovedCallbacks(0, mFirst64Removed);
          mFirst64Removed = 0L;
        }
      }
      return;
    }
    finally {}
  }
  
  public void remove(C paramC)
  {
    try
    {
      if (mNotificationLevel == 0)
      {
        mCallbacks.remove(paramC);
      }
      else
      {
        int i = mCallbacks.lastIndexOf(paramC);
        if (i >= 0) {
          setRemovalBitLocked(i);
        }
      }
      return;
    }
    finally {}
  }
  
  public static abstract class NotifierCallback<C, T, A>
  {
    public NotifierCallback() {}
    
    public abstract void onNotifyCallback(C paramC, T paramT, int paramInt, A paramA);
  }
}
