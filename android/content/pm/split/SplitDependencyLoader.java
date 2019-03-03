package android.content.pm.split;

import android.content.pm.PackageParser.PackageLite;
import android.util.IntArray;
import android.util.SparseArray;
import java.util.Arrays;
import java.util.BitSet;
import libcore.util.EmptyArray;

public abstract class SplitDependencyLoader<E extends Exception>
{
  private final SparseArray<int[]> mDependencies;
  
  protected SplitDependencyLoader(SparseArray<int[]> paramSparseArray)
  {
    mDependencies = paramSparseArray;
  }
  
  private static int[] append(int[] paramArrayOfInt, int paramInt)
  {
    if (paramArrayOfInt == null) {
      return new int[] { paramInt };
    }
    int[] arrayOfInt = Arrays.copyOf(paramArrayOfInt, paramArrayOfInt.length + 1);
    arrayOfInt[paramArrayOfInt.length] = paramInt;
    return arrayOfInt;
  }
  
  private int[] collectConfigSplitIndices(int paramInt)
  {
    int[] arrayOfInt = (int[])mDependencies.get(paramInt);
    if ((arrayOfInt != null) && (arrayOfInt.length > 1)) {
      return Arrays.copyOfRange(arrayOfInt, 1, arrayOfInt.length);
    }
    return EmptyArray.INT;
  }
  
  public static SparseArray<int[]> createDependenciesFromPackage(PackageParser.PackageLite paramPackageLite)
    throws SplitDependencyLoader.IllegalDependencyException
  {
    Object localObject1 = new SparseArray();
    ((SparseArray)localObject1).put(0, new int[] { -1 });
    Object localObject2;
    for (int i = 0; i < splitNames.length; i++) {
      if (isFeatureSplits[i] != 0)
      {
        localObject2 = usesSplitNames[i];
        if (localObject2 != null)
        {
          j = Arrays.binarySearch(splitNames, localObject2);
          if (j >= 0)
          {
            j++;
          }
          else
          {
            localObject1 = new StringBuilder();
            ((StringBuilder)localObject1).append("Split '");
            ((StringBuilder)localObject1).append(splitNames[i]);
            ((StringBuilder)localObject1).append("' requires split '");
            ((StringBuilder)localObject1).append((String)localObject2);
            ((StringBuilder)localObject1).append("', which is missing.");
            throw new IllegalDependencyException(((StringBuilder)localObject1).toString(), null);
          }
        }
        else
        {
          j = 0;
        }
        ((SparseArray)localObject1).put(i + 1, new int[] { j });
      }
    }
    for (i = 0; i < splitNames.length; i++) {
      if (isFeatureSplits[i] == 0)
      {
        localObject2 = configForSplit[i];
        if (localObject2 != null)
        {
          j = Arrays.binarySearch(splitNames, localObject2);
          if (j >= 0)
          {
            if (isFeatureSplits[j] != 0)
            {
              j++;
            }
            else
            {
              localObject1 = new StringBuilder();
              ((StringBuilder)localObject1).append("Split '");
              ((StringBuilder)localObject1).append(splitNames[i]);
              ((StringBuilder)localObject1).append("' declares itself as configuration split for a non-feature split '");
              ((StringBuilder)localObject1).append(splitNames[j]);
              ((StringBuilder)localObject1).append("'");
              throw new IllegalDependencyException(((StringBuilder)localObject1).toString(), null);
            }
          }
          else
          {
            localObject1 = new StringBuilder();
            ((StringBuilder)localObject1).append("Split '");
            ((StringBuilder)localObject1).append(splitNames[i]);
            ((StringBuilder)localObject1).append("' targets split '");
            ((StringBuilder)localObject1).append((String)localObject2);
            ((StringBuilder)localObject1).append("', which is missing.");
            throw new IllegalDependencyException(((StringBuilder)localObject1).toString(), null);
          }
        }
        else
        {
          j = 0;
        }
        ((SparseArray)localObject1).put(j, append((int[])((SparseArray)localObject1).get(j), i + 1));
      }
    }
    paramPackageLite = new BitSet();
    int j = 0;
    int k = ((SparseArray)localObject1).size();
    while (j < k)
    {
      i = ((SparseArray)localObject1).keyAt(j);
      paramPackageLite.clear();
      while (i != -1) {
        if (!paramPackageLite.get(i))
        {
          paramPackageLite.set(i);
          localObject2 = (int[])((SparseArray)localObject1).get(i);
          if (localObject2 != null) {
            i = localObject2[0];
          } else {
            i = -1;
          }
        }
        else
        {
          throw new IllegalDependencyException("Cycle detected in split dependencies.", null);
        }
      }
      j++;
    }
    return localObject1;
  }
  
  protected abstract void constructSplit(int paramInt1, int[] paramArrayOfInt, int paramInt2)
    throws Exception;
  
  protected abstract boolean isSplitCached(int paramInt);
  
  protected void loadDependenciesForSplit(int paramInt)
    throws Exception
  {
    if (isSplitCached(paramInt)) {
      return;
    }
    if (paramInt == 0)
    {
      constructSplit(0, collectConfigSplitIndices(0), -1);
      return;
    }
    IntArray localIntArray = new IntArray();
    localIntArray.add(paramInt);
    for (;;)
    {
      int[] arrayOfInt = (int[])mDependencies.get(paramInt);
      if ((arrayOfInt != null) && (arrayOfInt.length > 0)) {
        paramInt = arrayOfInt[0];
      } else {
        paramInt = -1;
      }
      if ((paramInt < 0) || (isSplitCached(paramInt))) {
        break;
      }
      localIntArray.add(paramInt);
    }
    int i = paramInt;
    for (paramInt = localIntArray.size() - 1; paramInt >= 0; paramInt--)
    {
      int j = localIntArray.get(paramInt);
      constructSplit(j, collectConfigSplitIndices(j), i);
      i = j;
    }
  }
  
  public static class IllegalDependencyException
    extends Exception
  {
    private IllegalDependencyException(String paramString)
    {
      super();
    }
  }
}
