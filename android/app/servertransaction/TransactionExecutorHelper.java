package android.app.servertransaction;

import android.app.ActivityThread.ActivityClientRecord;
import android.util.IntArray;
import com.android.internal.annotations.VisibleForTesting;
import java.util.List;

public class TransactionExecutorHelper
{
  private static final int DESTRUCTION_PENALTY = 10;
  private static final int[] ON_RESUME_PRE_EXCUTION_STATES = { 2, 4 };
  private IntArray mLifecycleSequence = new IntArray(6);
  
  public TransactionExecutorHelper() {}
  
  public static ActivityLifecycleItem getLifecycleRequestForCurrentState(ActivityThread.ActivityClientRecord paramActivityClientRecord)
  {
    switch (paramActivityClientRecord.getLifecycleState())
    {
    default: 
      paramActivityClientRecord = ResumeActivityItem.obtain(false);
      break;
    case 5: 
      paramActivityClientRecord = StopActivityItem.obtain(paramActivityClientRecord.isVisibleFromServer(), 0);
      break;
    case 4: 
      paramActivityClientRecord = PauseActivityItem.obtain();
    }
    return paramActivityClientRecord;
  }
  
  static int lastCallbackRequestingState(ClientTransaction paramClientTransaction)
  {
    paramClientTransaction = paramClientTransaction.getCallbacks();
    if ((paramClientTransaction != null) && (paramClientTransaction.size() != 0))
    {
      int i = -1;
      int j = -1;
      int k = paramClientTransaction.size() - 1;
      while (k >= 0)
      {
        int m = ((ClientTransactionItem)paramClientTransaction.get(k)).getPostExecutionState();
        int n = i;
        int i1 = j;
        if (m != -1)
        {
          if ((i != -1) && (i != m)) {
            break;
          }
          n = m;
          i1 = k;
        }
        k--;
        i = n;
        j = i1;
      }
      return j;
    }
    return -1;
  }
  
  private static boolean pathInvolvesDestruction(IntArray paramIntArray)
  {
    int i = paramIntArray.size();
    for (int j = 0; j < i; j++) {
      if (paramIntArray.get(j) == 6) {
        return true;
      }
    }
    return false;
  }
  
  @VisibleForTesting
  public int getClosestOfStates(ActivityThread.ActivityClientRecord paramActivityClientRecord, int[] paramArrayOfInt)
  {
    if ((paramArrayOfInt != null) && (paramArrayOfInt.length != 0))
    {
      int i = paramActivityClientRecord.getLifecycleState();
      int j = -1;
      int k = 0;
      int n;
      for (int m = Integer.MAX_VALUE; k < paramArrayOfInt.length; m = n)
      {
        getLifecyclePath(i, paramArrayOfInt[k], false);
        n = mLifecycleSequence.size();
        int i1 = n;
        if (pathInvolvesDestruction(mLifecycleSequence)) {
          i1 = n + 10;
        }
        n = m;
        if (m > i1)
        {
          j = paramArrayOfInt[k];
          n = i1;
        }
        k++;
      }
      return j;
    }
    return -1;
  }
  
  @VisibleForTesting
  public int getClosestPreExecutionState(ActivityThread.ActivityClientRecord paramActivityClientRecord, int paramInt)
  {
    if (paramInt != -1)
    {
      if (paramInt == 3) {
        return getClosestOfStates(paramActivityClientRecord, ON_RESUME_PRE_EXCUTION_STATES);
      }
      paramActivityClientRecord = new StringBuilder();
      paramActivityClientRecord.append("Pre-execution states for state: ");
      paramActivityClientRecord.append(paramInt);
      paramActivityClientRecord.append(" is not supported.");
      throw new UnsupportedOperationException(paramActivityClientRecord.toString());
    }
    return -1;
  }
  
  @VisibleForTesting
  public IntArray getLifecyclePath(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    if ((paramInt1 != -1) && (paramInt2 != -1))
    {
      if ((paramInt1 != 7) && (paramInt2 != 7))
      {
        if ((paramInt2 == 0) && (paramInt1 != paramInt2)) {
          throw new IllegalArgumentException("Can only start in pre-onCreate state");
        }
        mLifecycleSequence.clear();
        if (paramInt2 >= paramInt1)
        {
          paramInt1++;
          while (paramInt1 <= paramInt2)
          {
            mLifecycleSequence.add(paramInt1);
            paramInt1++;
          }
        }
        if ((paramInt1 == 4) && (paramInt2 == 3))
        {
          mLifecycleSequence.add(3);
        }
        else
        {
          if (paramInt1 <= 5)
          {
            int i = 2;
            if (paramInt2 >= 2)
            {
              paramInt1++;
              while (paramInt1 <= 5)
              {
                mLifecycleSequence.add(paramInt1);
                paramInt1++;
              }
              mLifecycleSequence.add(7);
              for (paramInt1 = i; paramInt1 <= paramInt2; paramInt1++) {
                mLifecycleSequence.add(paramInt1);
              }
            }
          }
          paramInt1++;
          while (paramInt1 <= 6)
          {
            mLifecycleSequence.add(paramInt1);
            paramInt1++;
          }
          for (paramInt1 = 1; paramInt1 <= paramInt2; paramInt1++) {
            mLifecycleSequence.add(paramInt1);
          }
        }
        if ((paramBoolean) && (mLifecycleSequence.size() != 0)) {
          mLifecycleSequence.remove(mLifecycleSequence.size() - 1);
        }
        return mLifecycleSequence;
      }
      throw new IllegalArgumentException("Can't start or finish in intermittent RESTART state");
    }
    throw new IllegalArgumentException("Can't resolve lifecycle path for undefined state");
  }
}
