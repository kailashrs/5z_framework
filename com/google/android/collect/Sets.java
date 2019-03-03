package com.google.android.collect;

import android.util.ArraySet;
import java.util.Collections;
import java.util.HashSet;
import java.util.SortedSet;
import java.util.TreeSet;

public class Sets
{
  public Sets() {}
  
  public static <E> ArraySet<E> newArraySet()
  {
    return new ArraySet();
  }
  
  public static <E> ArraySet<E> newArraySet(E... paramVarArgs)
  {
    ArraySet localArraySet = new ArraySet(paramVarArgs.length * 4 / 3 + 1);
    Collections.addAll(localArraySet, paramVarArgs);
    return localArraySet;
  }
  
  public static <K> HashSet<K> newHashSet()
  {
    return new HashSet();
  }
  
  public static <E> HashSet<E> newHashSet(E... paramVarArgs)
  {
    HashSet localHashSet = new HashSet(paramVarArgs.length * 4 / 3 + 1);
    Collections.addAll(localHashSet, paramVarArgs);
    return localHashSet;
  }
  
  public static <E> SortedSet<E> newSortedSet()
  {
    return new TreeSet();
  }
  
  public static <E> SortedSet<E> newSortedSet(E... paramVarArgs)
  {
    TreeSet localTreeSet = new TreeSet();
    Collections.addAll(localTreeSet, paramVarArgs);
    return localTreeSet;
  }
}
