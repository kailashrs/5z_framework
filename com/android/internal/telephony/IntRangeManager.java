package com.android.internal.telephony;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public abstract class IntRangeManager
{
  private static final int INITIAL_CLIENTS_ARRAY_SIZE = 4;
  private ArrayList<IntRange> mRanges = new ArrayList();
  
  protected IntRangeManager() {}
  
  private void populateAllClientRanges()
  {
    int i = mRanges.size();
    for (int j = 0; j < i; j++)
    {
      IntRange localIntRange = (IntRange)mRanges.get(j);
      int k = mClients.size();
      for (int m = 0; m < k; m++)
      {
        ClientRange localClientRange = (ClientRange)mClients.get(m);
        addRange(mStartId, mEndId, true);
      }
    }
  }
  
  private void populateAllRanges()
  {
    Iterator localIterator = mRanges.iterator();
    while (localIterator.hasNext())
    {
      IntRange localIntRange = (IntRange)localIterator.next();
      addRange(mStartId, mEndId, true);
    }
  }
  
  protected abstract void addRange(int paramInt1, int paramInt2, boolean paramBoolean);
  
  public boolean disableRange(int paramInt1, int paramInt2, String paramString)
  {
    try
    {
      int i = mRanges.size();
      int j = 0;
      for (;;)
      {
        int k = paramInt1;
        if (j >= i) {
          break;
        }
        IntRange localIntRange = (IntRange)mRanges.get(j);
        int m = mStartId;
        if (k < m) {
          return false;
        }
        m = i;
        if (paramInt2 <= mEndId)
        {
          Object localObject1 = mClients;
          int n = ((ArrayList)localObject1).size();
          int i1 = 1;
          boolean bool;
          if (n == 1)
          {
            localObject1 = (ClientRange)((ArrayList)localObject1).get(0);
            if ((mStartId == k) && (mEndId == paramInt2) && (mClient.equals(paramString)))
            {
              mRanges.remove(j);
              bool = updateRanges();
              if (bool) {
                return true;
              }
              mRanges.add(j, localIntRange);
              return false;
            }
            return false;
          }
          int i2 = 0;
          int i3 = 0;
          k = Integer.MIN_VALUE;
          int i4 = 0;
          for (;;)
          {
            m = i;
            if (i4 >= n) {
              break;
            }
            Object localObject2 = (ClientRange)((ArrayList)localObject1).get(i4);
            if ((mStartId == paramInt1) && (mEndId == paramInt2) && (mClient.equals(paramString)))
            {
              if (i4 == n - 1)
              {
                if (mEndId == k)
                {
                  ((ArrayList)localObject1).remove(i4);
                  return i1;
                }
                ((ArrayList)localObject1).remove(i4);
                mEndId = k;
                bool = updateRanges();
                if (bool) {
                  return i1;
                }
                ((ArrayList)localObject1).add(i4, localObject2);
                mEndId = mEndId;
                return false;
              }
              paramString = new com/android/internal/telephony/IntRangeManager$IntRange;
              paramString.<init>(this, localIntRange, i4);
              paramInt1 = i2;
              if (i4 == 0)
              {
                k = getmStartId;
                paramInt1 = i3;
                if (k != mStartId)
                {
                  mStartId = k;
                  paramInt1 = 1;
                }
                k = get1mEndId;
              }
              localObject2 = new java/util/ArrayList;
              ((ArrayList)localObject2).<init>();
              m = i4 + 1;
              while (m < n)
              {
                ClientRange localClientRange = (ClientRange)((ArrayList)localObject1).get(m);
                if (mStartId > k + 1)
                {
                  mEndId = k;
                  ((ArrayList)localObject2).add(paramString);
                  paramString = new com/android/internal/telephony/IntRangeManager$IntRange;
                  paramString.<init>(this, localClientRange);
                  paramInt1 = 1;
                }
                else
                {
                  if (mEndId < mEndId) {
                    mEndId = mEndId;
                  }
                  mClients.add(localClientRange);
                }
                i4 = k;
                if (mEndId > k) {
                  i4 = mEndId;
                }
                m++;
                k = i4;
              }
              if (k < paramInt2)
              {
                paramInt1 = 1;
                mEndId = k;
              }
              ((ArrayList)localObject2).add(paramString);
              mRanges.remove(j);
              mRanges.addAll(j, (Collection)localObject2);
              if ((paramInt1 != 0) && (!updateRanges()))
              {
                mRanges.removeAll((Collection)localObject2);
                mRanges.add(j, localIntRange);
                return false;
              }
              return true;
            }
            m = k;
            if (mEndId > k) {
              m = mEndId;
            }
            i4++;
            k = m;
          }
        }
        j++;
        i = m;
      }
      return false;
    }
    finally {}
  }
  
  public boolean enableRange(int paramInt1, int paramInt2, String paramString)
  {
    try
    {
      int i = mRanges.size();
      Object localObject1;
      Object localObject2;
      if (i == 0)
      {
        if (tryAddRanges(paramInt1, paramInt2, true))
        {
          localObject1 = mRanges;
          localObject2 = new com/android/internal/telephony/IntRangeManager$IntRange;
          ((IntRange)localObject2).<init>(this, paramInt1, paramInt2, paramString);
          ((ArrayList)localObject1).add(localObject2);
          return true;
        }
        return false;
      }
      for (int j = 0; j < i; j++)
      {
        IntRange localIntRange = (IntRange)mRanges.get(j);
        if ((paramInt1 >= mStartId) && (paramInt2 <= mEndId))
        {
          localObject1 = new com/android/internal/telephony/IntRangeManager$ClientRange;
          ((ClientRange)localObject1).<init>(this, paramInt1, paramInt2, paramString);
          localIntRange.insert((ClientRange)localObject1);
          return true;
        }
        int k;
        int m;
        if (paramInt1 - 1 == mEndId)
        {
          k = paramInt2;
          localObject1 = null;
          m = k;
          if (j + 1 < i)
          {
            localObject2 = (IntRange)mRanges.get(j + 1);
            if (mStartId - 1 <= paramInt2)
            {
              m = k;
              localObject1 = localObject2;
              if (paramInt2 <= mEndId)
              {
                m = mStartId - 1;
                localObject1 = localObject2;
              }
            }
            else
            {
              localObject1 = null;
              m = k;
            }
          }
          if (tryAddRanges(paramInt1, m, true))
          {
            mEndId = paramInt2;
            localObject2 = new com/android/internal/telephony/IntRangeManager$ClientRange;
            ((ClientRange)localObject2).<init>(this, paramInt1, paramInt2, paramString);
            localIntRange.insert((ClientRange)localObject2);
            if (localObject1 != null)
            {
              if (mEndId < mEndId) {
                mEndId = mEndId;
              }
              mClients.addAll(mClients);
              mRanges.remove(localObject1);
            }
            return true;
          }
          return false;
        }
        if (paramInt1 < mStartId)
        {
          if (paramInt2 + 1 < mStartId)
          {
            if (tryAddRanges(paramInt1, paramInt2, true))
            {
              localObject1 = mRanges;
              localObject2 = new com/android/internal/telephony/IntRangeManager$IntRange;
              ((IntRange)localObject2).<init>(this, paramInt1, paramInt2, paramString);
              ((ArrayList)localObject1).add(j, localObject2);
              return true;
            }
            return false;
          }
          if (paramInt2 <= mEndId)
          {
            if (tryAddRanges(paramInt1, mStartId - 1, true))
            {
              mStartId = paramInt1;
              localObject1 = mClients;
              localObject2 = new com/android/internal/telephony/IntRangeManager$ClientRange;
              ((ClientRange)localObject2).<init>(this, paramInt1, paramInt2, paramString);
              ((ArrayList)localObject1).add(0, localObject2);
              return true;
            }
            return false;
          }
          for (m = j + 1; m < i; m++)
          {
            localObject1 = (IntRange)mRanges.get(m);
            if (paramInt2 + 1 < mStartId)
            {
              if (tryAddRanges(paramInt1, paramInt2, true))
              {
                mStartId = paramInt1;
                mEndId = paramInt2;
                localObject1 = mClients;
                localObject2 = new com/android/internal/telephony/IntRangeManager$ClientRange;
                ((ClientRange)localObject2).<init>(this, paramInt1, paramInt2, paramString);
                ((ArrayList)localObject1).add(0, localObject2);
                paramInt2 = j + 1;
                for (paramInt1 = paramInt2; paramInt1 < m; paramInt1++)
                {
                  paramString = (IntRange)mRanges.get(paramInt2);
                  mClients.addAll(mClients);
                  mRanges.remove(paramString);
                }
                return true;
              }
              return false;
            }
            if (paramInt2 <= mEndId)
            {
              if (tryAddRanges(paramInt1, mStartId - 1, true))
              {
                mStartId = paramInt1;
                mEndId = mEndId;
                localObject2 = mClients;
                localObject1 = new com/android/internal/telephony/IntRangeManager$ClientRange;
                ((ClientRange)localObject1).<init>(this, paramInt1, paramInt2, paramString);
                ((ArrayList)localObject2).add(0, localObject1);
                paramInt2 = j + 1;
                for (paramInt1 = paramInt2; paramInt1 <= m; paramInt1++)
                {
                  paramString = (IntRange)mRanges.get(paramInt2);
                  mClients.addAll(mClients);
                  mRanges.remove(paramString);
                }
                return true;
              }
              return false;
            }
          }
          if (tryAddRanges(paramInt1, paramInt2, true))
          {
            mStartId = paramInt1;
            mEndId = paramInt2;
            localObject1 = mClients;
            localObject2 = new com/android/internal/telephony/IntRangeManager$ClientRange;
            ((ClientRange)localObject2).<init>(this, paramInt1, paramInt2, paramString);
            ((ArrayList)localObject1).add(0, localObject2);
            paramInt2 = j + 1;
            for (paramInt1 = paramInt2; paramInt1 < i; paramInt1++)
            {
              paramString = (IntRange)mRanges.get(paramInt2);
              mClients.addAll(mClients);
              mRanges.remove(paramString);
            }
            return true;
          }
          return false;
        }
        if (paramInt1 + 1 <= mEndId)
        {
          if (paramInt2 <= mEndId)
          {
            localObject1 = new com/android/internal/telephony/IntRangeManager$ClientRange;
            ((ClientRange)localObject1).<init>(this, paramInt1, paramInt2, paramString);
            localIntRange.insert((ClientRange)localObject1);
            return true;
          }
          k = j;
          for (m = j + 1; (m < i) && (paramInt2 + 1 >= mRanges.get(m)).mStartId); m++) {
            k = m;
          }
          if (k == j)
          {
            if (tryAddRanges(mEndId + 1, paramInt2, true))
            {
              mEndId = paramInt2;
              localObject1 = new com/android/internal/telephony/IntRangeManager$ClientRange;
              ((ClientRange)localObject1).<init>(this, paramInt1, paramInt2, paramString);
              localIntRange.insert((ClientRange)localObject1);
              return true;
            }
            return false;
          }
          localObject1 = (IntRange)mRanges.get(k);
          if (paramInt2 <= mEndId) {
            m = mStartId - 1;
          } else {
            m = paramInt2;
          }
          if (tryAddRanges(mEndId + 1, m, true))
          {
            if (paramInt2 <= mEndId) {
              m = mEndId;
            } else {
              m = paramInt2;
            }
            mEndId = m;
            localObject1 = new com/android/internal/telephony/IntRangeManager$ClientRange;
            ((ClientRange)localObject1).<init>(this, paramInt1, paramInt2, paramString);
            localIntRange.insert((ClientRange)localObject1);
            paramInt2 = j + 1;
            for (paramInt1 = paramInt2; paramInt1 <= k; paramInt1++)
            {
              paramString = (IntRange)mRanges.get(paramInt2);
              mClients.addAll(mClients);
              mRanges.remove(paramString);
            }
            return true;
          }
          return false;
        }
      }
      if (tryAddRanges(paramInt1, paramInt2, true))
      {
        localObject1 = mRanges;
        localObject2 = new com/android/internal/telephony/IntRangeManager$IntRange;
        ((IntRange)localObject2).<init>(this, paramInt1, paramInt2, paramString);
        ((ArrayList)localObject1).add(localObject2);
        return true;
      }
      return false;
    }
    finally {}
  }
  
  protected abstract boolean finishUpdate();
  
  public boolean isEmpty()
  {
    return mRanges.isEmpty();
  }
  
  protected abstract void startUpdate();
  
  protected boolean tryAddRanges(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    startUpdate();
    populateAllRanges();
    addRange(paramInt1, paramInt2, paramBoolean);
    return finishUpdate();
  }
  
  public boolean updateRanges()
  {
    startUpdate();
    populateAllRanges();
    return finishUpdate();
  }
  
  private class ClientRange
  {
    final String mClient;
    final int mEndId;
    final int mStartId;
    
    ClientRange(int paramInt1, int paramInt2, String paramString)
    {
      mStartId = paramInt1;
      mEndId = paramInt2;
      mClient = paramString;
    }
    
    public boolean equals(Object paramObject)
    {
      boolean bool1 = false;
      if ((paramObject != null) && ((paramObject instanceof ClientRange)))
      {
        paramObject = (ClientRange)paramObject;
        boolean bool2 = bool1;
        if (mStartId == mStartId)
        {
          bool2 = bool1;
          if (mEndId == mEndId)
          {
            bool2 = bool1;
            if (mClient.equals(mClient)) {
              bool2 = true;
            }
          }
        }
        return bool2;
      }
      return false;
    }
    
    public int hashCode()
    {
      return (mStartId * 31 + mEndId) * 31 + mClient.hashCode();
    }
  }
  
  private class IntRange
  {
    final ArrayList<IntRangeManager.ClientRange> mClients;
    int mEndId;
    int mStartId;
    
    IntRange(int paramInt1, int paramInt2, String paramString)
    {
      mStartId = paramInt1;
      mEndId = paramInt2;
      mClients = new ArrayList(4);
      mClients.add(new IntRangeManager.ClientRange(IntRangeManager.this, paramInt1, paramInt2, paramString));
    }
    
    IntRange(IntRangeManager.ClientRange paramClientRange)
    {
      mStartId = mStartId;
      mEndId = mEndId;
      mClients = new ArrayList(4);
      mClients.add(paramClientRange);
    }
    
    IntRange(IntRange paramIntRange, int paramInt)
    {
      mStartId = mStartId;
      mEndId = mEndId;
      mClients = new ArrayList(mClients.size());
      for (int i = 0; i < paramInt; i++) {
        mClients.add((IntRangeManager.ClientRange)mClients.get(i));
      }
    }
    
    void insert(IntRangeManager.ClientRange paramClientRange)
    {
      int i = mClients.size();
      int j = -1;
      int m;
      for (int k = 0;; k++)
      {
        m = j;
        if (k >= i) {
          break;
        }
        IntRangeManager.ClientRange localClientRange = (IntRangeManager.ClientRange)mClients.get(k);
        if (mStartId <= mStartId) {
          if (!paramClientRange.equals(localClientRange))
          {
            if ((mStartId == mStartId) && (mEndId > mEndId))
            {
              j = k + 1;
              m = j;
              if (j >= i) {
                break;
              }
            }
            else
            {
              mClients.add(k, paramClientRange);
            }
          }
          else {
            return;
          }
        }
      }
      if ((m != -1) && (m < i))
      {
        mClients.add(m, paramClientRange);
        return;
      }
      mClients.add(paramClientRange);
    }
  }
}
