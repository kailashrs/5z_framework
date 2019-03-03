package android.os;

import android.annotation.SystemApi;
import android.content.Context;
import android.provider.Settings.Global;
import android.util.proto.ProtoOutputStream;
import com.android.internal.annotations.VisibleForTesting;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class WorkSource
  implements Parcelable
{
  public static final Parcelable.Creator<WorkSource> CREATOR = new Parcelable.Creator()
  {
    public WorkSource createFromParcel(Parcel paramAnonymousParcel)
    {
      return new WorkSource(paramAnonymousParcel);
    }
    
    public WorkSource[] newArray(int paramAnonymousInt)
    {
      return new WorkSource[paramAnonymousInt];
    }
  };
  static final boolean DEBUG = false;
  static final String TAG = "WorkSource";
  static WorkSource sGoneWork;
  static WorkSource sNewbWork;
  static final WorkSource sTmpWorkSource = new WorkSource(0);
  private ArrayList<WorkChain> mChains;
  String[] mNames;
  int mNum;
  int[] mUids;
  
  public WorkSource()
  {
    mNum = 0;
    mChains = null;
  }
  
  public WorkSource(int paramInt)
  {
    mNum = 1;
    mUids = new int[] { paramInt, 0 };
    mNames = null;
    mChains = null;
  }
  
  public WorkSource(int paramInt, String paramString)
  {
    if (paramString != null)
    {
      mNum = 1;
      mUids = new int[] { paramInt, 0 };
      mNames = new String[] { paramString, null };
      mChains = null;
      return;
    }
    throw new NullPointerException("Name can't be null");
  }
  
  WorkSource(Parcel paramParcel)
  {
    mNum = paramParcel.readInt();
    mUids = paramParcel.createIntArray();
    mNames = paramParcel.createStringArray();
    int i = paramParcel.readInt();
    if (i > 0)
    {
      mChains = new ArrayList(i);
      paramParcel.readParcelableList(mChains, WorkChain.class.getClassLoader());
    }
    else
    {
      mChains = null;
    }
  }
  
  public WorkSource(WorkSource paramWorkSource)
  {
    if (paramWorkSource == null)
    {
      mNum = 0;
      mChains = null;
      return;
    }
    mNum = mNum;
    Object localObject;
    if (mUids != null)
    {
      mUids = ((int[])mUids.clone());
      if (mNames != null) {
        localObject = (String[])mNames.clone();
      } else {
        localObject = null;
      }
      mNames = ((String[])localObject);
    }
    else
    {
      mUids = null;
      mNames = null;
    }
    if (mChains != null)
    {
      mChains = new ArrayList(mChains.size());
      localObject = mChains.iterator();
      while (((Iterator)localObject).hasNext())
      {
        paramWorkSource = (WorkChain)((Iterator)localObject).next();
        mChains.add(new WorkChain(paramWorkSource));
      }
    }
    mChains = null;
  }
  
  private static WorkSource addWork(WorkSource paramWorkSource, int paramInt)
  {
    if (paramWorkSource == null) {
      return new WorkSource(paramInt);
    }
    paramWorkSource.insert(mNum, paramInt);
    return paramWorkSource;
  }
  
  private static WorkSource addWork(WorkSource paramWorkSource, int paramInt, String paramString)
  {
    if (paramWorkSource == null) {
      return new WorkSource(paramInt, paramString);
    }
    paramWorkSource.insert(mNum, paramInt, paramString);
    return paramWorkSource;
  }
  
  private int compare(WorkSource paramWorkSource, int paramInt1, int paramInt2)
  {
    int i = mUids[paramInt1] - mUids[paramInt2];
    if (i != 0) {
      return i;
    }
    return mNames[paramInt1].compareTo(mNames[paramInt2]);
  }
  
  public static ArrayList<WorkChain>[] diffChains(WorkSource paramWorkSource1, WorkSource paramWorkSource2)
  {
    WorkChain localWorkChain = null;
    Object localObject1 = null;
    int i;
    Object localObject3;
    if (mChains != null)
    {
      localObject2 = null;
      i = 0;
      while (i < mChains.size())
      {
        localObject1 = (WorkChain)mChains.get(i);
        if (mChains != null)
        {
          localObject3 = localObject2;
          if (mChains.contains(localObject1)) {}
        }
        else
        {
          localObject3 = localObject2;
          if (localObject2 == null) {
            localObject3 = new ArrayList(mChains.size());
          }
          ((ArrayList)localObject3).add(localObject1);
        }
        i++;
        localObject2 = localObject3;
      }
      localObject1 = localObject2;
    }
    Object localObject2 = localWorkChain;
    if (mChains != null)
    {
      localObject2 = null;
      i = 0;
      while (i < mChains.size())
      {
        localWorkChain = (WorkChain)mChains.get(i);
        if (mChains != null)
        {
          localObject3 = localObject2;
          if (mChains.contains(localWorkChain)) {}
        }
        else
        {
          localObject3 = localObject2;
          if (localObject2 == null) {
            localObject3 = new ArrayList(mChains.size());
          }
          ((ArrayList)localObject3).add(localWorkChain);
        }
        i++;
        localObject2 = localObject3;
      }
    }
    if ((localObject2 == null) && (localObject1 == null)) {
      return null;
    }
    return new ArrayList[] { localObject2, localObject1 };
  }
  
  private void insert(int paramInt1, int paramInt2)
  {
    if (mUids == null)
    {
      mUids = new int[4];
      mUids[0] = paramInt2;
      mNum = 1;
    }
    else if (mNum >= mUids.length)
    {
      int[] arrayOfInt = new int[mNum * 3 / 2];
      if (paramInt1 > 0) {
        System.arraycopy(mUids, 0, arrayOfInt, 0, paramInt1);
      }
      if (paramInt1 < mNum) {
        System.arraycopy(mUids, paramInt1, arrayOfInt, paramInt1 + 1, mNum - paramInt1);
      }
      mUids = arrayOfInt;
      mUids[paramInt1] = paramInt2;
      mNum += 1;
    }
    else
    {
      if (paramInt1 < mNum) {
        System.arraycopy(mUids, paramInt1, mUids, paramInt1 + 1, mNum - paramInt1);
      }
      mUids[paramInt1] = paramInt2;
      mNum += 1;
    }
  }
  
  private void insert(int paramInt1, int paramInt2, String paramString)
  {
    if (mUids == null)
    {
      mUids = new int[4];
      mUids[0] = paramInt2;
      mNames = new String[4];
      mNames[0] = paramString;
      mNum = 1;
    }
    else if (mNum >= mUids.length)
    {
      int[] arrayOfInt = new int[mNum * 3 / 2];
      String[] arrayOfString = new String[mNum * 3 / 2];
      if (paramInt1 > 0)
      {
        System.arraycopy(mUids, 0, arrayOfInt, 0, paramInt1);
        System.arraycopy(mNames, 0, arrayOfString, 0, paramInt1);
      }
      if (paramInt1 < mNum)
      {
        System.arraycopy(mUids, paramInt1, arrayOfInt, paramInt1 + 1, mNum - paramInt1);
        System.arraycopy(mNames, paramInt1, arrayOfString, paramInt1 + 1, mNum - paramInt1);
      }
      mUids = arrayOfInt;
      mNames = arrayOfString;
      mUids[paramInt1] = paramInt2;
      mNames[paramInt1] = paramString;
      mNum += 1;
    }
    else
    {
      if (paramInt1 < mNum)
      {
        System.arraycopy(mUids, paramInt1, mUids, paramInt1 + 1, mNum - paramInt1);
        System.arraycopy(mNames, paramInt1, mNames, paramInt1 + 1, mNum - paramInt1);
      }
      mUids[paramInt1] = paramInt2;
      mNames[paramInt1] = paramString;
      mNum += 1;
    }
  }
  
  public static boolean isChainedBatteryAttributionEnabled(Context paramContext)
  {
    int i = Settings.Global.getInt(paramContext.getContentResolver(), "chained_battery_attribution_enabled", 0);
    boolean bool = true;
    if (i != 1) {
      bool = false;
    }
    return bool;
  }
  
  private boolean removeUids(WorkSource paramWorkSource)
  {
    int i = mNum;
    int[] arrayOfInt = mUids;
    int j = mNum;
    paramWorkSource = mUids;
    boolean bool = false;
    int k = 0;
    int m = 0;
    while ((k < i) && (m < j)) {
      if (paramWorkSource[m] == arrayOfInt[k])
      {
        i--;
        bool = true;
        if (k < i) {
          System.arraycopy(arrayOfInt, k + 1, arrayOfInt, k, i - k);
        }
        m++;
      }
      else if (paramWorkSource[m] > arrayOfInt[k])
      {
        k++;
      }
      else
      {
        m++;
      }
    }
    mNum = i;
    return bool;
  }
  
  private boolean removeUidsAndNames(WorkSource paramWorkSource)
  {
    int i = mNum;
    int[] arrayOfInt1 = mUids;
    String[] arrayOfString = mNames;
    int j = mNum;
    int[] arrayOfInt2 = mUids;
    paramWorkSource = mNames;
    boolean bool = false;
    int k = 0;
    int m = 0;
    while ((k < i) && (m < j)) {
      if ((arrayOfInt2[m] == arrayOfInt1[k]) && (paramWorkSource[m].equals(arrayOfString[k])))
      {
        i--;
        bool = true;
        if (k < i)
        {
          System.arraycopy(arrayOfInt1, k + 1, arrayOfInt1, k, i - k);
          System.arraycopy(arrayOfString, k + 1, arrayOfString, k, i - k);
        }
        m++;
      }
      else if ((arrayOfInt2[m] <= arrayOfInt1[k]) && ((arrayOfInt2[m] != arrayOfInt1[k]) || (paramWorkSource[m].compareTo(arrayOfString[k]) <= 0)))
      {
        m++;
      }
      else
      {
        k++;
      }
    }
    mNum = i;
    return bool;
  }
  
  private boolean updateLocked(WorkSource paramWorkSource, boolean paramBoolean1, boolean paramBoolean2)
  {
    if ((mNames == null) && (mNames == null)) {
      return updateUidsLocked(paramWorkSource, paramBoolean1, paramBoolean2);
    }
    StringBuilder localStringBuilder;
    if ((mNum > 0) && (mNames == null))
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("Other ");
      localStringBuilder.append(paramWorkSource);
      localStringBuilder.append(" has names, but target ");
      localStringBuilder.append(this);
      localStringBuilder.append(" does not");
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    if ((mNum > 0) && (mNames == null))
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("Target ");
      localStringBuilder.append(this);
      localStringBuilder.append(" has names, but other ");
      localStringBuilder.append(paramWorkSource);
      localStringBuilder.append(" does not");
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    return updateUidsAndNamesLocked(paramWorkSource, paramBoolean1, paramBoolean2);
  }
  
  private boolean updateUidsAndNamesLocked(WorkSource paramWorkSource, boolean paramBoolean1, boolean paramBoolean2)
  {
    int i = mNum;
    int[] arrayOfInt = mUids;
    String[] arrayOfString = mNames;
    boolean bool = false;
    int j = 0;
    int k = 0;
    label391:
    for (;;)
    {
      if ((j >= mNum) && (k >= i)) {
        return bool;
      }
      int m = -1;
      if (j < mNum)
      {
        int n;
        if (k < i)
        {
          n = compare(paramWorkSource, j, k);
          m = n;
          if (n > 0) {}
        }
        else
        {
          if (!paramBoolean1)
          {
            n = k;
            if (k < i)
            {
              n = k;
              if (m == 0) {
                n = k + 1;
              }
            }
            j++;
            k = n;
            break label391;
          }
          n = j;
          for (int i1 = m;; i1 = m)
          {
            m = n;
            if (i1 >= 0) {
              break;
            }
            sGoneWork = addWork(sGoneWork, mUids[n], mNames[n]);
            n++;
            if (n >= mNum)
            {
              m = n;
              break;
            }
            if (k < i) {
              m = compare(paramWorkSource, n, k);
            } else {
              m = -1;
            }
          }
          n = m;
          if (j < m)
          {
            System.arraycopy(mUids, m, mUids, j, mNum - m);
            System.arraycopy(mNames, m, mNames, j, mNum - m);
            mNum -= m - j;
            n = j;
          }
          m = k;
          j = n;
          if (n < mNum)
          {
            m = k;
            j = n;
            if (i1 == 0)
            {
              j = n + 1;
              m = k + 1;
            }
          }
          k = m;
          break label391;
        }
      }
      bool = true;
      insert(j, arrayOfInt[k], arrayOfString[k]);
      if (paramBoolean2) {
        sNewbWork = addWork(sNewbWork, arrayOfInt[k], arrayOfString[k]);
      }
      j++;
      k++;
    }
  }
  
  private boolean updateUidsLocked(WorkSource paramWorkSource, boolean paramBoolean1, boolean paramBoolean2)
  {
    int i = mNum;
    int[] arrayOfInt1 = mUids;
    int j = mNum;
    int[] arrayOfInt2 = mUids;
    int k = 0;
    boolean bool = false;
    paramWorkSource = arrayOfInt1;
    int m = 0;
    for (;;)
    {
      if ((k >= i) && (m >= j))
      {
        mNum = i;
        mUids = paramWorkSource;
        return bool;
      }
      if ((k < i) && ((m >= j) || (arrayOfInt2[m] >= paramWorkSource[k])))
      {
        int n;
        if (!paramBoolean1)
        {
          n = m;
          if (m < j)
          {
            n = m;
            if (arrayOfInt2[m] == paramWorkSource[k]) {
              n = m + 1;
            }
          }
          k++;
          m = n;
        }
        else
        {
          for (int i1 = k; (i1 < i) && ((m >= j) || (arrayOfInt2[m] > paramWorkSource[i1])); i1++) {
            sGoneWork = addWork(sGoneWork, paramWorkSource[i1]);
          }
          int i2 = i;
          n = i1;
          if (k < i1)
          {
            System.arraycopy(paramWorkSource, i1, paramWorkSource, k, i - i1);
            i2 = i - (i1 - k);
            n = k;
          }
          i = m;
          k = n;
          if (n < i2)
          {
            i = m;
            k = n;
            if (m < j)
            {
              i = m;
              k = n;
              if (arrayOfInt2[m] == paramWorkSource[n])
              {
                k = n + 1;
                i = m + 1;
              }
            }
          }
          m = i;
          i = i2;
        }
      }
      else
      {
        bool = true;
        if (paramWorkSource == null)
        {
          paramWorkSource = new int[4];
          paramWorkSource[0] = arrayOfInt2[m];
        }
        else if (i >= paramWorkSource.length)
        {
          arrayOfInt1 = new int[paramWorkSource.length * 3 / 2];
          if (k > 0) {
            System.arraycopy(paramWorkSource, 0, arrayOfInt1, 0, k);
          }
          if (k < i) {
            System.arraycopy(paramWorkSource, k, arrayOfInt1, k + 1, i - k);
          }
          paramWorkSource = arrayOfInt1;
          paramWorkSource[k] = arrayOfInt2[m];
        }
        else
        {
          if (k < i) {
            System.arraycopy(paramWorkSource, k, paramWorkSource, k + 1, i - k);
          }
          paramWorkSource[k] = arrayOfInt2[m];
        }
        if (paramBoolean2) {
          sNewbWork = addWork(sNewbWork, arrayOfInt2[m]);
        }
        i++;
        k++;
        m++;
      }
    }
  }
  
  public boolean add(int paramInt)
  {
    if (mNum <= 0)
    {
      mNames = null;
      insert(0, paramInt);
      return true;
    }
    if (mNames == null)
    {
      int i = Arrays.binarySearch(mUids, 0, mNum, paramInt);
      if (i >= 0) {
        return false;
      }
      insert(-i - 1, paramInt);
      return true;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Adding without name to named ");
    localStringBuilder.append(this);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public boolean add(int paramInt, String paramString)
  {
    if (mNum <= 0)
    {
      insert(0, paramInt, paramString);
      return true;
    }
    if (mNames != null)
    {
      for (int i = 0; (i < mNum) && (mUids[i] <= paramInt); i++) {
        if (mUids[i] == paramInt)
        {
          int j = mNames[i].compareTo(paramString);
          if (j > 0) {
            break;
          }
          if (j == 0) {
            return false;
          }
        }
      }
      insert(i, paramInt, paramString);
      return true;
    }
    paramString = new StringBuilder();
    paramString.append("Adding name to unnamed ");
    paramString.append(this);
    throw new IllegalArgumentException(paramString.toString());
  }
  
  public boolean add(WorkSource paramWorkSource)
  {
    WorkSource localWorkSource = sTmpWorkSource;
    boolean bool1 = false;
    try
    {
      boolean bool2 = updateLocked(paramWorkSource, false, false);
      if (mChains != null)
      {
        Object localObject;
        if (mChains == null)
        {
          localObject = new java/util/ArrayList;
          ((ArrayList)localObject).<init>(mChains.size());
          mChains = ((ArrayList)localObject);
        }
        Iterator localIterator = mChains.iterator();
        while (localIterator.hasNext())
        {
          localObject = (WorkChain)localIterator.next();
          if (!mChains.contains(localObject))
          {
            ArrayList localArrayList = mChains;
            paramWorkSource = new android/os/WorkSource$WorkChain;
            paramWorkSource.<init>((WorkChain)localObject);
            localArrayList.add(paramWorkSource);
          }
        }
      }
      if ((!bool2) && (0 == 0)) {
        break label140;
      }
      bool1 = true;
      label140:
      return bool1;
    }
    finally {}
  }
  
  @Deprecated
  public WorkSource addReturningNewbs(WorkSource paramWorkSource)
  {
    synchronized (sTmpWorkSource)
    {
      sNewbWork = null;
      updateLocked(paramWorkSource, false, true);
      paramWorkSource = sNewbWork;
      return paramWorkSource;
    }
  }
  
  public void clear()
  {
    mNum = 0;
    if (mChains != null) {
      mChains.clear();
    }
  }
  
  public void clearNames()
  {
    if (mNames != null)
    {
      mNames = null;
      int i = 1;
      int j = mNum;
      for (int k = 1; k < mNum; k++) {
        if (mUids[k] == mUids[(k - 1)])
        {
          j--;
        }
        else
        {
          mUids[i] = mUids[k];
          i++;
        }
      }
      mNum = j;
    }
  }
  
  @SystemApi
  public WorkChain createWorkChain()
  {
    if (mChains == null) {
      mChains = new ArrayList(4);
    }
    WorkChain localWorkChain = new WorkChain();
    mChains.add(localWorkChain);
    return localWorkChain;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean diff(WorkSource paramWorkSource)
  {
    int i = mNum;
    if (i != mNum) {
      return true;
    }
    int[] arrayOfInt1 = mUids;
    int[] arrayOfInt2 = mUids;
    String[] arrayOfString = mNames;
    paramWorkSource = mNames;
    for (int j = 0; j < i; j++)
    {
      if (arrayOfInt1[j] != arrayOfInt2[j]) {
        return true;
      }
      if ((arrayOfString != null) && (paramWorkSource != null) && (!arrayOfString[j].equals(paramWorkSource[j]))) {
        return true;
      }
    }
    return false;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool1 = paramObject instanceof WorkSource;
    boolean bool2 = false;
    if (bool1)
    {
      paramObject = (WorkSource)paramObject;
      if (diff(paramObject)) {
        return false;
      }
      if ((mChains != null) && (!mChains.isEmpty())) {
        return mChains.equals(mChains);
      }
      if ((mChains != null) && (!mChains.isEmpty())) {
        break label80;
      }
      bool2 = true;
      label80:
      return bool2;
    }
    return false;
  }
  
  public int get(int paramInt)
  {
    return mUids[paramInt];
  }
  
  public String getName(int paramInt)
  {
    String str;
    if (mNames != null) {
      str = mNames[paramInt];
    } else {
      str = null;
    }
    return str;
  }
  
  public ArrayList<WorkChain> getWorkChains()
  {
    return mChains;
  }
  
  public int hashCode()
  {
    int i = 0;
    int j = 0;
    for (int k = 0; k < mNum; k++) {
      j = (j << 4 | j >>> 28) ^ mUids[k];
    }
    k = j;
    if (mNames != null) {
      for (;;)
      {
        k = j;
        if (i >= mNum) {
          break;
        }
        j = (j << 4 | j >>> 28) ^ mNames[i].hashCode();
        i++;
      }
    }
    j = k;
    if (mChains != null) {
      j = (k << 4 | k >>> 28) ^ mChains.hashCode();
    }
    return j;
  }
  
  public boolean isEmpty()
  {
    boolean bool;
    if ((mNum == 0) && ((mChains == null) || (mChains.isEmpty()))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean remove(WorkSource paramWorkSource)
  {
    boolean bool1 = isEmpty();
    boolean bool2 = false;
    if ((!bool1) && (!paramWorkSource.isEmpty()))
    {
      if ((mNames == null) && (mNames == null))
      {
        bool1 = removeUids(paramWorkSource);
      }
      else
      {
        if (mNames == null) {
          break label183;
        }
        if (mNames == null) {
          break label123;
        }
        bool1 = removeUidsAndNames(paramWorkSource);
      }
      boolean bool3 = false;
      boolean bool4 = bool3;
      if (mChains != null)
      {
        bool4 = bool3;
        if (mChains != null) {
          bool4 = mChains.removeAll(mChains);
        }
      }
      if ((!bool1) && (!bool4)) {
        bool1 = bool2;
      } else {
        bool1 = true;
      }
      return bool1;
      label123:
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Target ");
      localStringBuilder.append(this);
      localStringBuilder.append(" has names, but other ");
      localStringBuilder.append(paramWorkSource);
      localStringBuilder.append(" does not");
      throw new IllegalArgumentException(localStringBuilder.toString());
      label183:
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("Other ");
      localStringBuilder.append(paramWorkSource);
      localStringBuilder.append(" has names, but target ");
      localStringBuilder.append(this);
      localStringBuilder.append(" does not");
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    return false;
  }
  
  public void set(int paramInt)
  {
    mNum = 1;
    if (mUids == null) {
      mUids = new int[2];
    }
    mUids[0] = paramInt;
    mNames = null;
    if (mChains != null) {
      mChains.clear();
    }
  }
  
  public void set(int paramInt, String paramString)
  {
    if (paramString != null)
    {
      mNum = 1;
      if (mUids == null)
      {
        mUids = new int[2];
        mNames = new String[2];
      }
      mUids[0] = paramInt;
      mNames[0] = paramString;
      if (mChains != null) {
        mChains.clear();
      }
      return;
    }
    throw new NullPointerException("Name can't be null");
  }
  
  public void set(WorkSource paramWorkSource)
  {
    if (paramWorkSource == null)
    {
      mNum = 0;
      if (mChains != null) {
        mChains.clear();
      }
      return;
    }
    mNum = mNum;
    if (mUids != null)
    {
      if ((mUids != null) && (mUids.length >= mNum)) {
        System.arraycopy(mUids, 0, mUids, 0, mNum);
      } else {
        mUids = ((int[])mUids.clone());
      }
      if (mNames != null)
      {
        if ((mNames != null) && (mNames.length >= mNum)) {
          System.arraycopy(mNames, 0, mNames, 0, mNum);
        } else {
          mNames = ((String[])mNames.clone());
        }
      }
      else {
        mNames = null;
      }
    }
    else
    {
      mUids = null;
      mNames = null;
    }
    if (mChains != null)
    {
      if (mChains != null) {
        mChains.clear();
      } else {
        mChains = new ArrayList(mChains.size());
      }
      paramWorkSource = mChains.iterator();
      while (paramWorkSource.hasNext())
      {
        WorkChain localWorkChain = (WorkChain)paramWorkSource.next();
        mChains.add(new WorkChain(localWorkChain));
      }
    }
  }
  
  @Deprecated
  public WorkSource[] setReturningDiffs(WorkSource paramWorkSource)
  {
    synchronized (sTmpWorkSource)
    {
      sNewbWork = null;
      sGoneWork = null;
      updateLocked(paramWorkSource, true, true);
      if ((sNewbWork == null) && (sGoneWork == null)) {
        return null;
      }
      WorkSource localWorkSource2 = sNewbWork;
      paramWorkSource = sGoneWork;
      return new WorkSource[] { localWorkSource2, paramWorkSource };
    }
  }
  
  public int size()
  {
    return mNum;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("WorkSource{");
    int i = 0;
    for (int j = 0; j < mNum; j++)
    {
      if (j != 0) {
        localStringBuilder.append(", ");
      }
      localStringBuilder.append(mUids[j]);
      if (mNames != null)
      {
        localStringBuilder.append(" ");
        localStringBuilder.append(mNames[j]);
      }
    }
    if (mChains != null)
    {
      localStringBuilder.append(" chains=");
      for (j = i; j < mChains.size(); j++)
      {
        if (j != 0) {
          localStringBuilder.append(", ");
        }
        localStringBuilder.append(mChains.get(j));
      }
    }
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void transferWorkChains(WorkSource paramWorkSource)
  {
    if (mChains != null) {
      mChains.clear();
    }
    if ((mChains != null) && (!mChains.isEmpty()))
    {
      if (mChains == null) {
        mChains = new ArrayList(4);
      }
      mChains.addAll(mChains);
      mChains.clear();
      return;
    }
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mNum);
    paramParcel.writeIntArray(mUids);
    paramParcel.writeStringArray(mNames);
    if (mChains == null)
    {
      paramParcel.writeInt(-1);
    }
    else
    {
      paramParcel.writeInt(mChains.size());
      paramParcel.writeParcelableList(mChains, paramInt);
    }
  }
  
  public void writeToProto(ProtoOutputStream paramProtoOutputStream, long paramLong)
  {
    paramLong = paramProtoOutputStream.start(paramLong);
    long l1;
    for (int i = 0; i < mNum; i++)
    {
      l1 = paramProtoOutputStream.start(2246267895809L);
      paramProtoOutputStream.write(1120986464257L, mUids[i]);
      if (mNames != null) {
        paramProtoOutputStream.write(1138166333442L, mNames[i]);
      }
      paramProtoOutputStream.end(l1);
    }
    if (mChains != null) {
      for (i = 0; i < mChains.size(); i++)
      {
        Object localObject = (WorkChain)mChains.get(i);
        long l2 = paramProtoOutputStream.start(2246267895810L);
        String[] arrayOfString = ((WorkChain)localObject).getTags();
        localObject = ((WorkChain)localObject).getUids();
        for (int j = 0; j < arrayOfString.length; j++)
        {
          l1 = paramProtoOutputStream.start(2246267895809L);
          paramProtoOutputStream.write(1120986464257L, localObject[j]);
          paramProtoOutputStream.write(1138166333442L, arrayOfString[j]);
          paramProtoOutputStream.end(l1);
        }
        paramProtoOutputStream.end(l2);
      }
    }
    paramProtoOutputStream.end(paramLong);
  }
  
  @SystemApi
  public static final class WorkChain
    implements Parcelable
  {
    public static final Parcelable.Creator<WorkChain> CREATOR = new Parcelable.Creator()
    {
      public WorkSource.WorkChain createFromParcel(Parcel paramAnonymousParcel)
      {
        return new WorkSource.WorkChain(paramAnonymousParcel, null);
      }
      
      public WorkSource.WorkChain[] newArray(int paramAnonymousInt)
      {
        return new WorkSource.WorkChain[paramAnonymousInt];
      }
    };
    private int mSize;
    private String[] mTags;
    private int[] mUids;
    
    public WorkChain()
    {
      mSize = 0;
      mUids = new int[4];
      mTags = new String[4];
    }
    
    private WorkChain(Parcel paramParcel)
    {
      mSize = paramParcel.readInt();
      mUids = paramParcel.createIntArray();
      mTags = paramParcel.createStringArray();
    }
    
    @VisibleForTesting
    public WorkChain(WorkChain paramWorkChain)
    {
      mSize = mSize;
      mUids = ((int[])mUids.clone());
      mTags = ((String[])mTags.clone());
    }
    
    private void resizeArrays()
    {
      int i = mSize * 2;
      int[] arrayOfInt = new int[i];
      String[] arrayOfString = new String[i];
      System.arraycopy(mUids, 0, arrayOfInt, 0, mSize);
      System.arraycopy(mTags, 0, arrayOfString, 0, mSize);
      mUids = arrayOfInt;
      mTags = arrayOfString;
    }
    
    public WorkChain addNode(int paramInt, String paramString)
    {
      if (mSize == mUids.length) {
        resizeArrays();
      }
      mUids[mSize] = paramInt;
      mTags[mSize] = paramString;
      mSize += 1;
      return this;
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public boolean equals(Object paramObject)
    {
      boolean bool1 = paramObject instanceof WorkChain;
      boolean bool2 = false;
      if (bool1)
      {
        paramObject = (WorkChain)paramObject;
        bool1 = bool2;
        if (mSize == mSize)
        {
          bool1 = bool2;
          if (Arrays.equals(mUids, mUids))
          {
            bool1 = bool2;
            if (Arrays.equals(mTags, mTags)) {
              bool1 = true;
            }
          }
        }
        return bool1;
      }
      return false;
    }
    
    public String getAttributionTag()
    {
      return mTags[0];
    }
    
    public int getAttributionUid()
    {
      return mUids[0];
    }
    
    @VisibleForTesting
    public int getSize()
    {
      return mSize;
    }
    
    @VisibleForTesting
    public String[] getTags()
    {
      String[] arrayOfString = new String[mSize];
      System.arraycopy(mTags, 0, arrayOfString, 0, mSize);
      return arrayOfString;
    }
    
    @VisibleForTesting
    public int[] getUids()
    {
      int[] arrayOfInt = new int[mSize];
      System.arraycopy(mUids, 0, arrayOfInt, 0, mSize);
      return arrayOfInt;
    }
    
    public int hashCode()
    {
      return (mSize + Arrays.hashCode(mUids) * 31) * 31 + Arrays.hashCode(mTags);
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder("WorkChain{");
      for (int i = 0; i < mSize; i++)
      {
        if (i != 0) {
          localStringBuilder.append(", ");
        }
        localStringBuilder.append("(");
        localStringBuilder.append(mUids[i]);
        if (mTags[i] != null)
        {
          localStringBuilder.append(", ");
          localStringBuilder.append(mTags[i]);
        }
        localStringBuilder.append(")");
      }
      localStringBuilder.append("}");
      return localStringBuilder.toString();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeInt(mSize);
      paramParcel.writeIntArray(mUids);
      paramParcel.writeStringArray(mTags);
    }
  }
}
