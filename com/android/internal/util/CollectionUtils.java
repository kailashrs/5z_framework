package com.android.internal.util;

import android.util.ArraySet;
import android.util.ExceptionUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public class CollectionUtils
{
  private CollectionUtils() {}
  
  public static <T> List<T> add(List<T> paramList, T paramT)
  {
    Object localObject;
    if (paramList != null)
    {
      localObject = paramList;
      if (paramList != Collections.emptyList()) {}
    }
    else
    {
      localObject = new ArrayList();
    }
    ((List)localObject).add(paramT);
    return localObject;
  }
  
  public static <T> Set<T> add(Set<T> paramSet, T paramT)
  {
    Object localObject;
    if (paramSet != null)
    {
      localObject = paramSet;
      if (paramSet != Collections.emptySet()) {}
    }
    else
    {
      localObject = new ArraySet();
    }
    ((Set)localObject).add(paramT);
    return localObject;
  }
  
  public static <T> void addIf(List<T> paramList, Collection<? super T> paramCollection, Predicate<? super T> paramPredicate)
  {
    for (int i = 0; i < size(paramList); i++)
    {
      Object localObject = paramList.get(i);
      if (paramPredicate.test(localObject)) {
        paramCollection.add(localObject);
      }
    }
  }
  
  public static <T> boolean any(List<T> paramList, Predicate<T> paramPredicate)
  {
    boolean bool;
    if (find(paramList, paramPredicate) != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static <T> List<T> copyOf(List<T> paramList)
  {
    if (isEmpty(paramList)) {
      paramList = Collections.emptyList();
    } else {
      paramList = new ArrayList(paramList);
    }
    return paramList;
  }
  
  public static <T> Set<T> copyOf(Set<T> paramSet)
  {
    if (isEmpty(paramSet)) {
      paramSet = Collections.emptySet();
    } else {
      paramSet = new ArraySet(paramSet);
    }
    return paramSet;
  }
  
  public static <T> List<T> emptyIfNull(List<T> paramList)
  {
    if (paramList == null) {
      paramList = Collections.emptyList();
    }
    return paramList;
  }
  
  public static <T> Set<T> emptyIfNull(Set<T> paramSet)
  {
    if (paramSet == null) {
      paramSet = Collections.emptySet();
    }
    return paramSet;
  }
  
  public static <T> List<T> filter(List<?> paramList, Class<T> paramClass)
  {
    if (isEmpty(paramList)) {
      return Collections.emptyList();
    }
    Object localObject1 = null;
    int i = 0;
    while (i < paramList.size())
    {
      Object localObject2 = paramList.get(i);
      Object localObject3 = localObject1;
      if (paramClass.isInstance(localObject2)) {
        localObject3 = ArrayUtils.add((ArrayList)localObject1, localObject2);
      }
      i++;
      localObject1 = localObject3;
    }
    return emptyIfNull((List)localObject1);
  }
  
  public static <T> List<T> filter(List<T> paramList, Predicate<? super T> paramPredicate)
  {
    Object localObject1 = null;
    int i = 0;
    while (i < size(paramList))
    {
      Object localObject2 = paramList.get(i);
      Object localObject3 = localObject1;
      if (paramPredicate.test(localObject2)) {
        localObject3 = ArrayUtils.add((ArrayList)localObject1, localObject2);
      }
      i++;
      localObject1 = localObject3;
    }
    return emptyIfNull((List)localObject1);
  }
  
  public static <T> Set<T> filter(Set<T> paramSet, Predicate<? super T> paramPredicate)
  {
    if ((paramSet != null) && (paramSet.size() != 0))
    {
      Object localObject1 = null;
      Object localObject2 = null;
      Object localObject3;
      if ((paramSet instanceof ArraySet))
      {
        localObject1 = (ArraySet)paramSet;
        int i = ((ArraySet)localObject1).size();
        int j = 0;
        for (paramSet = (Set<T>)localObject2; j < i; paramSet = (Set<T>)localObject2)
        {
          localObject3 = ((ArraySet)localObject1).valueAt(j);
          localObject2 = paramSet;
          if (paramPredicate.test(localObject3)) {
            localObject2 = ArrayUtils.add(paramSet, localObject3);
          }
          j++;
        }
        localObject2 = paramSet;
      }
      else
      {
        localObject3 = paramSet.iterator();
        for (paramSet = (Set<T>)localObject1;; paramSet = (Set<T>)localObject2)
        {
          localObject2 = paramSet;
          if (!((Iterator)localObject3).hasNext()) {
            break;
          }
          localObject1 = ((Iterator)localObject3).next();
          localObject2 = paramSet;
          if (paramPredicate.test(localObject1)) {
            localObject2 = ArrayUtils.add(paramSet, localObject1);
          }
        }
      }
      return emptyIfNull((Set)localObject2);
    }
    return Collections.emptySet();
  }
  
  public static <T> T find(List<T> paramList, Predicate<T> paramPredicate)
  {
    if (isEmpty(paramList)) {
      return null;
    }
    for (int i = 0; i < paramList.size(); i++)
    {
      Object localObject = paramList.get(i);
      if (paramPredicate.test(localObject)) {
        return localObject;
      }
    }
    return null;
  }
  
  public static <T> void forEach(Set<T> paramSet, FunctionalUtils.ThrowingConsumer<T> paramThrowingConsumer)
  {
    if ((paramSet != null) && (paramThrowingConsumer != null))
    {
      int i = paramSet.size();
      if (i == 0) {
        return;
      }
      try
      {
        if ((paramSet instanceof ArraySet))
        {
          paramSet = (ArraySet)paramSet;
          for (int j = 0; j < i; j++) {
            paramThrowingConsumer.acceptOrThrow(paramSet.valueAt(j));
          }
        }
        else
        {
          paramSet = paramSet.iterator();
          while (paramSet.hasNext()) {
            paramThrowingConsumer.acceptOrThrow(paramSet.next());
          }
        }
        return;
      }
      catch (Exception paramSet)
      {
        throw ExceptionUtils.propagate(paramSet);
      }
    }
  }
  
  public static boolean isEmpty(Collection<?> paramCollection)
  {
    boolean bool;
    if (size(paramCollection) == 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static <I, O> List<O> map(List<I> paramList, Function<? super I, ? extends O> paramFunction)
  {
    if (isEmpty(paramList)) {
      return Collections.emptyList();
    }
    ArrayList localArrayList = new ArrayList();
    for (int i = 0; i < paramList.size(); i++) {
      localArrayList.add(paramFunction.apply(paramList.get(i)));
    }
    return localArrayList;
  }
  
  public static <I, O> Set<O> map(Set<I> paramSet, Function<? super I, ? extends O> paramFunction)
  {
    if (isEmpty(paramSet)) {
      return Collections.emptySet();
    }
    ArraySet localArraySet = new ArraySet();
    if ((paramSet instanceof ArraySet))
    {
      paramSet = (ArraySet)paramSet;
      int i = paramSet.size();
      for (int j = 0; j < i; j++) {
        localArraySet.add(paramFunction.apply(paramSet.valueAt(j)));
      }
    }
    else
    {
      paramSet = paramSet.iterator();
      while (paramSet.hasNext()) {
        localArraySet.add(paramFunction.apply(paramSet.next()));
      }
    }
    return localArraySet;
  }
  
  public static <I, O> List<O> mapNotNull(List<I> paramList, Function<? super I, ? extends O> paramFunction)
  {
    if (isEmpty(paramList)) {
      return Collections.emptyList();
    }
    Object localObject1 = null;
    int i = 0;
    while (i < paramList.size())
    {
      Object localObject2 = paramFunction.apply(paramList.get(i));
      Object localObject3 = localObject1;
      if (localObject2 != null) {
        localObject3 = add((List)localObject1, localObject2);
      }
      i++;
      localObject1 = localObject3;
    }
    return emptyIfNull((List)localObject1);
  }
  
  public static <T> List<T> remove(List<T> paramList, T paramT)
  {
    if (isEmpty(paramList)) {
      return emptyIfNull(paramList);
    }
    paramList.remove(paramT);
    return paramList;
  }
  
  public static <T> Set<T> remove(Set<T> paramSet, T paramT)
  {
    if (isEmpty(paramSet)) {
      return emptyIfNull(paramSet);
    }
    paramSet.remove(paramT);
    return paramSet;
  }
  
  public static int size(Collection<?> paramCollection)
  {
    int i;
    if (paramCollection != null) {
      i = paramCollection.size();
    } else {
      i = 0;
    }
    return i;
  }
}
