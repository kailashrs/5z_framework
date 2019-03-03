package com.google.android.collect;

import java.util.ArrayList;
import java.util.Collections;

public class Lists
{
  public Lists() {}
  
  public static <E> ArrayList<E> newArrayList()
  {
    return new ArrayList();
  }
  
  public static <E> ArrayList<E> newArrayList(E... paramVarArgs)
  {
    ArrayList localArrayList = new ArrayList(paramVarArgs.length * 110 / 100 + 5);
    Collections.addAll(localArrayList, paramVarArgs);
    return localArrayList;
  }
}
