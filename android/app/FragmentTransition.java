package android.app;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Rect;
import android.transition.Transition;
import android.transition.Transition.EpicenterCallback;
import android.transition.TransitionListenerAdapter;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.ArrayMap;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import com.android.internal.view.OneShotPreDrawListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class FragmentTransition
{
  private static final int[] INVERSE_OPS = { 0, 3, 0, 1, 5, 4, 7, 6, 9, 8 };
  
  FragmentTransition() {}
  
  private static void addSharedElementsWithMatchingNames(ArrayList<View> paramArrayList, ArrayMap<String, View> paramArrayMap, Collection<String> paramCollection)
  {
    for (int i = paramArrayMap.size() - 1; i >= 0; i--)
    {
      View localView = (View)paramArrayMap.valueAt(i);
      if ((localView != null) && (paramCollection.contains(localView.getTransitionName()))) {
        paramArrayList.add(localView);
      }
    }
  }
  
  public static void addTargets(Transition paramTransition, ArrayList<View> paramArrayList)
  {
    if (paramTransition == null) {
      return;
    }
    boolean bool = paramTransition instanceof TransitionSet;
    int i = 0;
    int j = 0;
    if (bool)
    {
      paramTransition = (TransitionSet)paramTransition;
      i = paramTransition.getTransitionCount();
      while (j < i)
      {
        addTargets(paramTransition.getTransitionAt(j), paramArrayList);
        j++;
      }
    }
    else if ((!hasSimpleTarget(paramTransition)) && (isNullOrEmpty(paramTransition.getTargets())))
    {
      int k = paramArrayList.size();
      for (j = i; j < k; j++) {
        paramTransition.addTarget((View)paramArrayList.get(j));
      }
    }
  }
  
  private static void addToFirstInLastOut(BackStackRecord paramBackStackRecord, BackStackRecord.Op paramOp, SparseArray<FragmentContainerTransition> paramSparseArray, boolean paramBoolean1, boolean paramBoolean2)
  {
    Fragment localFragment = fragment;
    if (localFragment == null) {
      return;
    }
    int i = mContainerId;
    if (i == 0) {
      return;
    }
    int j;
    if (paramBoolean1) {
      j = INVERSE_OPS[cmd];
    } else {
      j = cmd;
    }
    boolean bool1 = false;
    int k = 0;
    int m = 0;
    int n = 0;
    int i1 = 0;
    int i2 = 0;
    int i3 = 0;
    int i4 = 0;
    boolean bool2 = false;
    boolean bool3 = false;
    if (j != 1) {
      switch (j)
      {
      default: 
        j = k;
      }
    }
    for (;;)
    {
      break;
      if (paramBoolean2)
      {
        bool1 = bool3;
        if (mHiddenChanged)
        {
          bool1 = bool3;
          if (!mHidden)
          {
            bool1 = bool3;
            if (mAdded) {
              bool1 = true;
            }
          }
        }
      }
      else
      {
        bool1 = mHidden;
      }
      n = 1;
      j = k;
      continue;
      if (paramBoolean2)
      {
        j = i1;
        if (mHiddenChanged)
        {
          j = i1;
          if (mAdded)
          {
            j = i1;
            if (mHidden) {
              j = 1;
            }
          }
        }
        m = j;
      }
      else
      {
        j = i2;
        if (mAdded)
        {
          j = i2;
          if (!mHidden) {
            j = 1;
          }
        }
        m = j;
      }
      j = 1;
      continue;
      if (paramBoolean2)
      {
        j = i3;
        if (!mAdded)
        {
          j = i3;
          if (mView != null)
          {
            j = i3;
            if (mView.getVisibility() == 0)
            {
              j = i3;
              if (mView.getTransitionAlpha() > 0.0F) {
                j = 1;
              }
            }
          }
        }
        m = j;
      }
      else
      {
        j = i4;
        if (mAdded)
        {
          j = i4;
          if (!mHidden) {
            j = 1;
          }
        }
        m = j;
      }
      j = 1;
      continue;
      if (paramBoolean2)
      {
        bool1 = mIsNewlyAdded;
      }
      else
      {
        bool1 = bool2;
        if (!mAdded)
        {
          bool1 = bool2;
          if (!mHidden) {
            bool1 = true;
          }
        }
      }
      n = 1;
      j = k;
    }
    Object localObject = (FragmentContainerTransition)paramSparseArray.get(i);
    paramOp = (BackStackRecord.Op)localObject;
    if (bool1)
    {
      paramOp = ensureContainer((FragmentContainerTransition)localObject, paramSparseArray, i);
      lastIn = localFragment;
      lastInIsPop = paramBoolean1;
      lastInTransaction = paramBackStackRecord;
    }
    if ((!paramBoolean2) && (n != 0))
    {
      if ((paramOp != null) && (firstOut == localFragment)) {
        firstOut = null;
      }
      localObject = mManager;
      if ((mState < 1) && (mCurState >= 1) && (mHost.getContext().getApplicationInfo().targetSdkVersion >= 24) && (!mReorderingAllowed))
      {
        ((FragmentManagerImpl)localObject).makeActive(localFragment);
        ((FragmentManagerImpl)localObject).moveToState(localFragment, 1, 0, 0, false);
      }
    }
    if (m != 0)
    {
      localObject = paramOp;
      if ((localObject == null) || (firstOut == null))
      {
        paramOp = ensureContainer((FragmentContainerTransition)localObject, paramSparseArray, i);
        firstOut = localFragment;
        firstOutIsPop = paramBoolean1;
        firstOutTransaction = paramBackStackRecord;
      }
    }
    if ((!paramBoolean2) && (j != 0) && (paramOp != null) && (lastIn == localFragment)) {
      lastIn = null;
    }
  }
  
  private static void bfsAddViewChildren(List<View> paramList, View paramView)
  {
    int i = paramList.size();
    if (containedBeforeIndex(paramList, paramView, i)) {
      return;
    }
    paramList.add(paramView);
    for (int j = i; j < paramList.size(); j++)
    {
      paramView = (View)paramList.get(j);
      if ((paramView instanceof ViewGroup))
      {
        paramView = (ViewGroup)paramView;
        int k = paramView.getChildCount();
        for (int m = 0; m < k; m++)
        {
          View localView = paramView.getChildAt(m);
          if (!containedBeforeIndex(paramList, localView, i)) {
            paramList.add(localView);
          }
        }
      }
    }
  }
  
  public static void calculateFragments(BackStackRecord paramBackStackRecord, SparseArray<FragmentContainerTransition> paramSparseArray, boolean paramBoolean)
  {
    int i = mOps.size();
    for (int j = 0; j < i; j++) {
      addToFirstInLastOut(paramBackStackRecord, (BackStackRecord.Op)mOps.get(j), paramSparseArray, false, paramBoolean);
    }
  }
  
  private static ArrayMap<String, String> calculateNameOverrides(int paramInt1, ArrayList<BackStackRecord> paramArrayList, ArrayList<Boolean> paramArrayList1, int paramInt2, int paramInt3)
  {
    ArrayMap localArrayMap = new ArrayMap();
    paramInt3--;
    while (paramInt3 >= paramInt2)
    {
      Object localObject = (BackStackRecord)paramArrayList.get(paramInt3);
      if (((BackStackRecord)localObject).interactsWith(paramInt1))
      {
        boolean bool = ((Boolean)paramArrayList1.get(paramInt3)).booleanValue();
        if (mSharedElementSourceNames != null)
        {
          int i = mSharedElementSourceNames.size();
          ArrayList localArrayList1;
          ArrayList localArrayList2;
          if (bool)
          {
            localArrayList1 = mSharedElementSourceNames;
            localArrayList2 = mSharedElementTargetNames;
          }
          else
          {
            localArrayList2 = mSharedElementSourceNames;
            localArrayList1 = mSharedElementTargetNames;
          }
          for (int j = 0; j < i; j++)
          {
            String str1 = (String)localArrayList2.get(j);
            String str2 = (String)localArrayList1.get(j);
            localObject = (String)localArrayMap.remove(str2);
            if (localObject != null) {
              localArrayMap.put(str1, localObject);
            } else {
              localArrayMap.put(str1, str2);
            }
          }
        }
      }
      paramInt3--;
    }
    return localArrayMap;
  }
  
  public static void calculatePopFragments(BackStackRecord paramBackStackRecord, SparseArray<FragmentContainerTransition> paramSparseArray, boolean paramBoolean)
  {
    if (!mManager.mContainer.onHasView()) {
      return;
    }
    for (int i = mOps.size() - 1; i >= 0; i--) {
      addToFirstInLastOut(paramBackStackRecord, (BackStackRecord.Op)mOps.get(i), paramSparseArray, true, paramBoolean);
    }
  }
  
  private static void callSharedElementStartEnd(Fragment paramFragment1, Fragment paramFragment2, boolean paramBoolean1, ArrayMap<String, View> paramArrayMap, boolean paramBoolean2)
  {
    if (paramBoolean1) {
      paramFragment1 = paramFragment2.getEnterTransitionCallback();
    } else {
      paramFragment1 = paramFragment1.getEnterTransitionCallback();
    }
    if (paramFragment1 != null)
    {
      paramFragment2 = new ArrayList();
      ArrayList localArrayList = new ArrayList();
      int i = 0;
      int j;
      if (paramArrayMap == null) {
        j = 0;
      } else {
        j = paramArrayMap.size();
      }
      while (i < j)
      {
        localArrayList.add((String)paramArrayMap.keyAt(i));
        paramFragment2.add((View)paramArrayMap.valueAt(i));
        i++;
      }
      if (paramBoolean2) {
        paramFragment1.onSharedElementStart(localArrayList, paramFragment2, null);
      } else {
        paramFragment1.onSharedElementEnd(localArrayList, paramFragment2, null);
      }
    }
  }
  
  private static ArrayMap<String, View> captureInSharedElements(ArrayMap<String, String> paramArrayMap, TransitionSet paramTransitionSet, FragmentContainerTransition paramFragmentContainerTransition)
  {
    Object localObject = lastIn;
    View localView = ((Fragment)localObject).getView();
    if ((!paramArrayMap.isEmpty()) && (paramTransitionSet != null) && (localView != null))
    {
      ArrayMap localArrayMap = new ArrayMap();
      localView.findNamedViews(localArrayMap);
      paramTransitionSet = lastInTransaction;
      if (lastInIsPop)
      {
        paramFragmentContainerTransition = ((Fragment)localObject).getExitTransitionCallback();
        paramTransitionSet = mSharedElementSourceNames;
      }
      else
      {
        paramFragmentContainerTransition = ((Fragment)localObject).getEnterTransitionCallback();
        paramTransitionSet = mSharedElementTargetNames;
      }
      if (paramTransitionSet != null) {
        localArrayMap.retainAll(paramTransitionSet);
      }
      int i;
      if ((paramTransitionSet != null) && (paramFragmentContainerTransition != null))
      {
        paramFragmentContainerTransition.onMapSharedElements(paramTransitionSet, localArrayMap);
        i = paramTransitionSet.size() - 1;
      }
      while (i >= 0)
      {
        localObject = (String)paramTransitionSet.get(i);
        paramFragmentContainerTransition = (View)localArrayMap.get(localObject);
        if (paramFragmentContainerTransition == null)
        {
          paramFragmentContainerTransition = findKeyForValue(paramArrayMap, (String)localObject);
          if (paramFragmentContainerTransition != null) {
            paramArrayMap.remove(paramFragmentContainerTransition);
          }
        }
        else if (!((String)localObject).equals(paramFragmentContainerTransition.getTransitionName()))
        {
          localObject = findKeyForValue(paramArrayMap, (String)localObject);
          if (localObject != null) {
            paramArrayMap.put(localObject, paramFragmentContainerTransition.getTransitionName());
          }
        }
        i--;
        continue;
        retainValues(paramArrayMap, localArrayMap);
      }
      return localArrayMap;
    }
    paramArrayMap.clear();
    return null;
  }
  
  private static ArrayMap<String, View> captureOutSharedElements(ArrayMap<String, String> paramArrayMap, TransitionSet paramTransitionSet, FragmentContainerTransition paramFragmentContainerTransition)
  {
    if ((!paramArrayMap.isEmpty()) && (paramTransitionSet != null))
    {
      Object localObject = firstOut;
      ArrayMap localArrayMap = new ArrayMap();
      ((Fragment)localObject).getView().findNamedViews(localArrayMap);
      paramTransitionSet = firstOutTransaction;
      if (firstOutIsPop)
      {
        paramFragmentContainerTransition = ((Fragment)localObject).getEnterTransitionCallback();
        paramTransitionSet = mSharedElementTargetNames;
      }
      else
      {
        paramFragmentContainerTransition = ((Fragment)localObject).getExitTransitionCallback();
        paramTransitionSet = mSharedElementSourceNames;
      }
      localArrayMap.retainAll(paramTransitionSet);
      if (paramFragmentContainerTransition != null)
      {
        paramFragmentContainerTransition.onMapSharedElements(paramTransitionSet, localArrayMap);
        for (int i = paramTransitionSet.size() - 1; i >= 0; i--)
        {
          localObject = (String)paramTransitionSet.get(i);
          paramFragmentContainerTransition = (View)localArrayMap.get(localObject);
          if (paramFragmentContainerTransition == null)
          {
            paramArrayMap.remove(localObject);
          }
          else if (!((String)localObject).equals(paramFragmentContainerTransition.getTransitionName()))
          {
            localObject = (String)paramArrayMap.remove(localObject);
            paramArrayMap.put(paramFragmentContainerTransition.getTransitionName(), localObject);
          }
        }
      }
      paramArrayMap.retainAll(localArrayMap.keySet());
      return localArrayMap;
    }
    paramArrayMap.clear();
    return null;
  }
  
  private static Transition cloneTransition(Transition paramTransition)
  {
    Transition localTransition = paramTransition;
    if (paramTransition != null) {
      localTransition = paramTransition.clone();
    }
    return localTransition;
  }
  
  private static ArrayList<View> configureEnteringExitingViews(Transition paramTransition, Fragment paramFragment, ArrayList<View> paramArrayList, View paramView)
  {
    Object localObject = null;
    if (paramTransition != null)
    {
      ArrayList localArrayList = new ArrayList();
      paramFragment = paramFragment.getView();
      if (paramFragment != null) {
        paramFragment.captureTransitioningViews(localArrayList);
      }
      if (paramArrayList != null) {
        localArrayList.removeAll(paramArrayList);
      }
      localObject = localArrayList;
      if (!localArrayList.isEmpty())
      {
        localArrayList.add(paramView);
        addTargets(paramTransition, localArrayList);
        localObject = localArrayList;
      }
    }
    return localObject;
  }
  
  private static TransitionSet configureSharedElementsOrdered(ViewGroup paramViewGroup, View paramView, ArrayMap<String, String> paramArrayMap, FragmentContainerTransition paramFragmentContainerTransition, ArrayList<View> paramArrayList1, ArrayList<View> paramArrayList2, Transition paramTransition1, Transition paramTransition2)
  {
    Fragment localFragment1 = lastIn;
    Fragment localFragment2 = firstOut;
    Rect localRect = null;
    if ((localFragment1 != null) && (localFragment2 != null))
    {
      boolean bool = lastInIsPop;
      TransitionSet localTransitionSet;
      if (paramArrayMap.isEmpty()) {
        localTransitionSet = null;
      } else {
        localTransitionSet = getSharedElementTransition(localFragment1, localFragment2, bool);
      }
      ArrayMap localArrayMap = captureOutSharedElements(paramArrayMap, localTransitionSet, paramFragmentContainerTransition);
      if (paramArrayMap.isEmpty()) {
        localTransitionSet = null;
      }
      for (;;)
      {
        break;
        paramArrayList1.addAll(localArrayMap.values());
      }
      if ((paramTransition1 == null) && (paramTransition2 == null) && (localTransitionSet == null)) {
        return null;
      }
      callSharedElementStartEnd(localFragment1, localFragment2, bool, localArrayMap, true);
      if (localTransitionSet != null)
      {
        localRect = new Rect();
        setSharedElementTargets(localTransitionSet, paramView, paramArrayList1);
        setOutEpicenter(localTransitionSet, paramTransition2, localArrayMap, firstOutIsPop, firstOutTransaction);
        if (paramTransition1 != null) {
          paramTransition1.setEpicenterCallback(new Transition.EpicenterCallback()
          {
            public Rect onGetEpicenter(Transition paramAnonymousTransition)
            {
              if (isEmpty()) {
                return null;
              }
              return FragmentTransition.this;
            }
          });
        }
        paramTransition2 = localRect;
      }
      else
      {
        paramTransition2 = localRect;
      }
      OneShotPreDrawListener.add(paramViewGroup, new _..Lambda.FragmentTransition.Ip0LktADPhG_3ouNBXgzufWpFfY(paramArrayMap, localTransitionSet, paramFragmentContainerTransition, paramArrayList2, paramView, localFragment1, localFragment2, bool, paramArrayList1, paramTransition1, paramTransition2));
      return localTransitionSet;
    }
    return null;
  }
  
  private static TransitionSet configureSharedElementsReordered(ViewGroup paramViewGroup, View paramView, ArrayMap<String, String> paramArrayMap, FragmentContainerTransition paramFragmentContainerTransition, ArrayList<View> paramArrayList1, ArrayList<View> paramArrayList2, Transition paramTransition1, Transition paramTransition2)
  {
    Fragment localFragment1 = lastIn;
    Fragment localFragment2 = firstOut;
    if ((localFragment1 != null) && (localFragment1.getView() != null)) {
      localFragment1.getView().setVisibility(0);
    }
    if ((localFragment1 != null) && (localFragment2 != null))
    {
      boolean bool = lastInIsPop;
      Object localObject;
      if (paramArrayMap.isEmpty()) {
        localObject = null;
      } else {
        localObject = getSharedElementTransition(localFragment1, localFragment2, bool);
      }
      ArrayMap localArrayMap1 = captureOutSharedElements(paramArrayMap, (TransitionSet)localObject, paramFragmentContainerTransition);
      ArrayMap localArrayMap2 = captureInSharedElements(paramArrayMap, (TransitionSet)localObject, paramFragmentContainerTransition);
      if (paramArrayMap.isEmpty())
      {
        paramArrayMap = null;
        if (localArrayMap1 != null) {
          localArrayMap1.clear();
        }
        localObject = paramArrayMap;
        if (localArrayMap2 != null)
        {
          localArrayMap2.clear();
          localObject = paramArrayMap;
        }
      }
      else
      {
        addSharedElementsWithMatchingNames(paramArrayList1, localArrayMap1, paramArrayMap.keySet());
        addSharedElementsWithMatchingNames(paramArrayList2, localArrayMap2, paramArrayMap.values());
      }
      if ((paramTransition1 == null) && (paramTransition2 == null) && (localObject == null)) {
        return null;
      }
      callSharedElementStartEnd(localFragment1, localFragment2, bool, localArrayMap1, true);
      if (localObject != null)
      {
        paramArrayList2.add(paramView);
        setSharedElementTargets((TransitionSet)localObject, paramView, paramArrayList1);
        setOutEpicenter((TransitionSet)localObject, paramTransition2, localArrayMap1, firstOutIsPop, firstOutTransaction);
        paramView = new Rect();
        paramFragmentContainerTransition = getInEpicenterView(localArrayMap2, paramFragmentContainerTransition, paramTransition1, bool);
        if (paramFragmentContainerTransition != null) {
          paramTransition1.setEpicenterCallback(new Transition.EpicenterCallback()
          {
            public Rect onGetEpicenter(Transition paramAnonymousTransition)
            {
              return FragmentTransition.this;
            }
          });
        }
        paramArrayMap = paramView;
        paramView = paramFragmentContainerTransition;
      }
      else
      {
        paramArrayMap = null;
        paramView = null;
      }
      OneShotPreDrawListener.add(paramViewGroup, new _..Lambda.FragmentTransition.jurn0WXuKw3bRQ_2d5zCWdeZWuI(localFragment1, localFragment2, bool, localArrayMap2, paramView, paramArrayMap));
      return localObject;
    }
    return null;
  }
  
  private static void configureTransitionsOrdered(FragmentManagerImpl paramFragmentManagerImpl, int paramInt, FragmentContainerTransition paramFragmentContainerTransition, View paramView, ArrayMap<String, String> paramArrayMap)
  {
    Transition localTransition1 = null;
    if (mContainer.onHasView()) {
      paramFragmentManagerImpl = (ViewGroup)mContainer.onFindViewById(paramInt);
    } else {
      paramFragmentManagerImpl = localTransition1;
    }
    if (paramFragmentManagerImpl == null) {
      return;
    }
    Fragment localFragment = lastIn;
    Object localObject = firstOut;
    boolean bool1 = lastInIsPop;
    boolean bool2 = firstOutIsPop;
    Transition localTransition2 = getEnterTransition(localFragment, bool1);
    localTransition1 = getExitTransition((Fragment)localObject, bool2);
    ArrayList localArrayList1 = new ArrayList();
    ArrayList localArrayList2 = new ArrayList();
    TransitionSet localTransitionSet = configureSharedElementsOrdered(paramFragmentManagerImpl, paramView, paramArrayMap, paramFragmentContainerTransition, localArrayList1, localArrayList2, localTransition2, localTransition1);
    if ((localTransition2 == null) && (localTransitionSet == null) && (localTransition1 == null)) {
      return;
    }
    localObject = configureEnteringExitingViews(localTransition1, (Fragment)localObject, localArrayList1, paramView);
    if ((localObject == null) || (((ArrayList)localObject).isEmpty())) {
      localTransition1 = null;
    }
    if (localTransition2 != null) {
      localTransition2.addTarget(paramView);
    }
    paramFragmentContainerTransition = mergeTransitions(localTransition2, localTransition1, localTransitionSet, localFragment, lastInIsPop);
    if (paramFragmentContainerTransition != null)
    {
      paramFragmentContainerTransition.setNameOverrides(paramArrayMap);
      paramArrayMap = new ArrayList();
      scheduleRemoveTargets(paramFragmentContainerTransition, localTransition2, paramArrayMap, localTransition1, (ArrayList)localObject, localTransitionSet, localArrayList2);
      scheduleTargetChange(paramFragmentManagerImpl, localFragment, paramView, localArrayList2, localTransition2, paramArrayMap, localTransition1, (ArrayList)localObject);
      TransitionManager.beginDelayedTransition(paramFragmentManagerImpl, paramFragmentContainerTransition);
    }
  }
  
  private static void configureTransitionsReordered(FragmentManagerImpl paramFragmentManagerImpl, int paramInt, FragmentContainerTransition paramFragmentContainerTransition, View paramView, ArrayMap<String, String> paramArrayMap)
  {
    Object localObject1 = null;
    if (mContainer.onHasView()) {
      paramFragmentManagerImpl = (ViewGroup)mContainer.onFindViewById(paramInt);
    } else {
      paramFragmentManagerImpl = (FragmentManagerImpl)localObject1;
    }
    if (paramFragmentManagerImpl == null) {
      return;
    }
    Object localObject2 = lastIn;
    Fragment localFragment = firstOut;
    boolean bool1 = lastInIsPop;
    boolean bool2 = firstOutIsPop;
    ArrayList localArrayList1 = new ArrayList();
    ArrayList localArrayList2 = new ArrayList();
    Transition localTransition = getEnterTransition((Fragment)localObject2, bool1);
    localObject1 = getExitTransition(localFragment, bool2);
    TransitionSet localTransitionSet = configureSharedElementsReordered(paramFragmentManagerImpl, paramView, paramArrayMap, paramFragmentContainerTransition, localArrayList2, localArrayList1, localTransition, (Transition)localObject1);
    if ((localTransition == null) && (localTransitionSet == null) && (localObject1 == null)) {
      return;
    }
    paramFragmentContainerTransition = (FragmentContainerTransition)localObject1;
    localObject1 = configureEnteringExitingViews(paramFragmentContainerTransition, localFragment, localArrayList2, paramView);
    paramView = configureEnteringExitingViews(localTransition, (Fragment)localObject2, localArrayList1, paramView);
    setViewVisibility(paramView, 4);
    localObject2 = mergeTransitions(localTransition, paramFragmentContainerTransition, localTransitionSet, (Fragment)localObject2, bool1);
    if (localObject2 != null)
    {
      replaceHide(paramFragmentContainerTransition, localFragment, (ArrayList)localObject1);
      ((Transition)localObject2).setNameOverrides(paramArrayMap);
      scheduleRemoveTargets((Transition)localObject2, localTransition, paramView, paramFragmentContainerTransition, (ArrayList)localObject1, localTransitionSet, localArrayList1);
      TransitionManager.beginDelayedTransition(paramFragmentManagerImpl, (Transition)localObject2);
      setViewVisibility(paramView, 0);
      if (localTransitionSet != null)
      {
        localTransitionSet.getTargets().clear();
        localTransitionSet.getTargets().addAll(localArrayList1);
        replaceTargets(localTransitionSet, localArrayList2, localArrayList1);
      }
    }
  }
  
  private static boolean containedBeforeIndex(List<View> paramList, View paramView, int paramInt)
  {
    for (int i = 0; i < paramInt; i++) {
      if (paramList.get(i) == paramView) {
        return true;
      }
    }
    return false;
  }
  
  private static FragmentContainerTransition ensureContainer(FragmentContainerTransition paramFragmentContainerTransition, SparseArray<FragmentContainerTransition> paramSparseArray, int paramInt)
  {
    FragmentContainerTransition localFragmentContainerTransition = paramFragmentContainerTransition;
    if (paramFragmentContainerTransition == null)
    {
      localFragmentContainerTransition = new FragmentContainerTransition();
      paramSparseArray.put(paramInt, localFragmentContainerTransition);
    }
    return localFragmentContainerTransition;
  }
  
  private static String findKeyForValue(ArrayMap<String, String> paramArrayMap, String paramString)
  {
    int i = paramArrayMap.size();
    for (int j = 0; j < i; j++) {
      if (paramString.equals(paramArrayMap.valueAt(j))) {
        return (String)paramArrayMap.keyAt(j);
      }
    }
    return null;
  }
  
  private static Transition getEnterTransition(Fragment paramFragment, boolean paramBoolean)
  {
    if (paramFragment == null) {
      return null;
    }
    if (paramBoolean) {
      paramFragment = paramFragment.getReenterTransition();
    } else {
      paramFragment = paramFragment.getEnterTransition();
    }
    return cloneTransition(paramFragment);
  }
  
  private static Transition getExitTransition(Fragment paramFragment, boolean paramBoolean)
  {
    if (paramFragment == null) {
      return null;
    }
    if (paramBoolean) {
      paramFragment = paramFragment.getReturnTransition();
    } else {
      paramFragment = paramFragment.getExitTransition();
    }
    return cloneTransition(paramFragment);
  }
  
  private static View getInEpicenterView(ArrayMap<String, View> paramArrayMap, FragmentContainerTransition paramFragmentContainerTransition, Transition paramTransition, boolean paramBoolean)
  {
    paramFragmentContainerTransition = lastInTransaction;
    if ((paramTransition != null) && (paramArrayMap != null) && (mSharedElementSourceNames != null) && (!mSharedElementSourceNames.isEmpty()))
    {
      if (paramBoolean) {
        paramFragmentContainerTransition = (String)mSharedElementSourceNames.get(0);
      } else {
        paramFragmentContainerTransition = (String)mSharedElementTargetNames.get(0);
      }
      return (View)paramArrayMap.get(paramFragmentContainerTransition);
    }
    return null;
  }
  
  private static TransitionSet getSharedElementTransition(Fragment paramFragment1, Fragment paramFragment2, boolean paramBoolean)
  {
    if ((paramFragment1 != null) && (paramFragment2 != null))
    {
      if (paramBoolean) {
        paramFragment1 = paramFragment2.getSharedElementReturnTransition();
      } else {
        paramFragment1 = paramFragment1.getSharedElementEnterTransition();
      }
      paramFragment2 = cloneTransition(paramFragment1);
      if (paramFragment2 == null) {
        return null;
      }
      paramFragment1 = new TransitionSet();
      paramFragment1.addTransition(paramFragment2);
      return paramFragment1;
    }
    return null;
  }
  
  private static boolean hasSimpleTarget(Transition paramTransition)
  {
    boolean bool;
    if ((isNullOrEmpty(paramTransition.getTargetIds())) && (isNullOrEmpty(paramTransition.getTargetNames())) && (isNullOrEmpty(paramTransition.getTargetTypes()))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  private static boolean isNullOrEmpty(List paramList)
  {
    boolean bool;
    if ((paramList != null) && (!paramList.isEmpty())) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  private static Transition mergeTransitions(Transition paramTransition1, Transition paramTransition2, Transition paramTransition3, Fragment paramFragment, boolean paramBoolean)
  {
    boolean bool1 = true;
    boolean bool2 = bool1;
    if (paramTransition1 != null)
    {
      bool2 = bool1;
      if (paramTransition2 != null)
      {
        bool2 = bool1;
        if (paramFragment != null)
        {
          if (paramBoolean) {
            paramBoolean = paramFragment.getAllowReturnTransitionOverlap();
          } else {
            paramBoolean = paramFragment.getAllowEnterTransitionOverlap();
          }
          bool2 = paramBoolean;
        }
      }
    }
    if (bool2)
    {
      paramFragment = new TransitionSet();
      if (paramTransition1 != null) {
        paramFragment.addTransition(paramTransition1);
      }
      if (paramTransition2 != null) {
        paramFragment.addTransition(paramTransition2);
      }
      if (paramTransition3 != null) {
        paramFragment.addTransition(paramTransition3);
      }
      paramTransition1 = paramFragment;
    }
    else
    {
      paramFragment = null;
      if ((paramTransition2 != null) && (paramTransition1 != null))
      {
        paramTransition2 = new TransitionSet().addTransition(paramTransition2).addTransition(paramTransition1).setOrdering(1);
      }
      else if (paramTransition2 == null)
      {
        paramTransition2 = paramFragment;
        if (paramTransition1 != null) {
          paramTransition2 = paramTransition1;
        }
      }
      paramTransition1 = paramTransition2;
      if (paramTransition3 != null)
      {
        paramTransition1 = new TransitionSet();
        if (paramTransition2 != null) {
          paramTransition1.addTransition(paramTransition2);
        }
        paramTransition1.addTransition(paramTransition3);
      }
    }
    return paramTransition1;
  }
  
  private static void replaceHide(Transition paramTransition, Fragment paramFragment, final ArrayList<View> paramArrayList)
  {
    if ((paramFragment != null) && (paramTransition != null) && (mAdded) && (mHidden) && (mHiddenChanged))
    {
      paramFragment.setHideReplaced(true);
      View localView = paramFragment.getView();
      OneShotPreDrawListener.add(mContainer, new _..Lambda.FragmentTransition.PZ32bJ_FSMpbzYzBl8x73NJPidQ(paramArrayList));
      paramTransition.addListener(new TransitionListenerAdapter()
      {
        public void onTransitionEnd(Transition paramAnonymousTransition)
        {
          paramAnonymousTransition.removeListener(this);
          setVisibility(8);
          FragmentTransition.setViewVisibility(paramArrayList, 0);
        }
      });
    }
  }
  
  public static void replaceTargets(Transition paramTransition, ArrayList<View> paramArrayList1, ArrayList<View> paramArrayList2)
  {
    boolean bool = paramTransition instanceof TransitionSet;
    int i = 0;
    int j = 0;
    if (bool)
    {
      paramTransition = (TransitionSet)paramTransition;
      i = paramTransition.getTransitionCount();
      while (j < i)
      {
        replaceTargets(paramTransition.getTransitionAt(j), paramArrayList1, paramArrayList2);
        j++;
      }
    }
    else if (!hasSimpleTarget(paramTransition))
    {
      List localList = paramTransition.getTargets();
      if ((localList != null) && (localList.size() == paramArrayList1.size()) && (localList.containsAll(paramArrayList1)))
      {
        if (paramArrayList2 == null) {
          j = 0;
        } else {
          j = paramArrayList2.size();
        }
        while (i < j)
        {
          paramTransition.addTarget((View)paramArrayList2.get(i));
          i++;
        }
        for (j = paramArrayList1.size() - 1; j >= 0; j--) {
          paramTransition.removeTarget((View)paramArrayList1.get(j));
        }
      }
    }
  }
  
  private static void retainValues(ArrayMap<String, String> paramArrayMap, ArrayMap<String, View> paramArrayMap1)
  {
    for (int i = paramArrayMap.size() - 1; i >= 0; i--) {
      if (!paramArrayMap1.containsKey((String)paramArrayMap.valueAt(i))) {
        paramArrayMap.removeAt(i);
      }
    }
  }
  
  private static void scheduleRemoveTargets(Transition paramTransition1, Transition paramTransition2, final ArrayList<View> paramArrayList1, final Transition paramTransition3, final ArrayList<View> paramArrayList2, final TransitionSet paramTransitionSet, final ArrayList<View> paramArrayList3)
  {
    paramTransition1.addListener(new TransitionListenerAdapter()
    {
      public void onTransitionStart(Transition paramAnonymousTransition)
      {
        if (FragmentTransition.this != null) {
          FragmentTransition.replaceTargets(FragmentTransition.this, paramArrayList1, null);
        }
        if (paramTransition3 != null) {
          FragmentTransition.replaceTargets(paramTransition3, paramArrayList2, null);
        }
        if (paramTransitionSet != null) {
          FragmentTransition.replaceTargets(paramTransitionSet, paramArrayList3, null);
        }
      }
    });
  }
  
  private static void scheduleTargetChange(ViewGroup paramViewGroup, Fragment paramFragment, View paramView, ArrayList<View> paramArrayList1, Transition paramTransition1, ArrayList<View> paramArrayList2, Transition paramTransition2, ArrayList<View> paramArrayList3)
  {
    OneShotPreDrawListener.add(paramViewGroup, new _..Lambda.FragmentTransition.8Ei4ls5jlZcfRvuLcweFAxtFBFs(paramTransition1, paramView, paramFragment, paramArrayList1, paramArrayList2, paramArrayList3, paramTransition2));
  }
  
  private static void setEpicenter(Transition paramTransition, View paramView)
  {
    if (paramView != null)
    {
      Rect localRect = new Rect();
      paramView.getBoundsOnScreen(localRect);
      paramTransition.setEpicenterCallback(new Transition.EpicenterCallback()
      {
        public Rect onGetEpicenter(Transition paramAnonymousTransition)
        {
          return FragmentTransition.this;
        }
      });
    }
  }
  
  private static void setOutEpicenter(TransitionSet paramTransitionSet, Transition paramTransition, ArrayMap<String, View> paramArrayMap, boolean paramBoolean, BackStackRecord paramBackStackRecord)
  {
    if ((mSharedElementSourceNames != null) && (!mSharedElementSourceNames.isEmpty()))
    {
      if (paramBoolean) {
        paramBackStackRecord = (String)mSharedElementTargetNames.get(0);
      } else {
        paramBackStackRecord = (String)mSharedElementSourceNames.get(0);
      }
      paramArrayMap = (View)paramArrayMap.get(paramBackStackRecord);
      setEpicenter(paramTransitionSet, paramArrayMap);
      if (paramTransition != null) {
        setEpicenter(paramTransition, paramArrayMap);
      }
    }
  }
  
  private static void setSharedElementTargets(TransitionSet paramTransitionSet, View paramView, ArrayList<View> paramArrayList)
  {
    List localList = paramTransitionSet.getTargets();
    localList.clear();
    int i = paramArrayList.size();
    for (int j = 0; j < i; j++) {
      bfsAddViewChildren(localList, (View)paramArrayList.get(j));
    }
    localList.add(paramView);
    paramArrayList.add(paramView);
    addTargets(paramTransitionSet, paramArrayList);
  }
  
  private static void setViewVisibility(ArrayList<View> paramArrayList, int paramInt)
  {
    if (paramArrayList == null) {
      return;
    }
    for (int i = paramArrayList.size() - 1; i >= 0; i--) {
      ((View)paramArrayList.get(i)).setVisibility(paramInt);
    }
  }
  
  static void startTransitions(FragmentManagerImpl paramFragmentManagerImpl, ArrayList<BackStackRecord> paramArrayList, ArrayList<Boolean> paramArrayList1, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    if (mCurState < 1) {
      return;
    }
    SparseArray localSparseArray = new SparseArray();
    Object localObject;
    for (int i = paramInt1; i < paramInt2; i++)
    {
      localObject = (BackStackRecord)paramArrayList.get(i);
      if (((Boolean)paramArrayList1.get(i)).booleanValue()) {
        calculatePopFragments((BackStackRecord)localObject, localSparseArray, paramBoolean);
      } else {
        calculateFragments((BackStackRecord)localObject, localSparseArray, paramBoolean);
      }
    }
    if (localSparseArray.size() != 0)
    {
      localObject = new View(mHost.getContext());
      int j = localSparseArray.size();
      for (i = 0; i < j; i++)
      {
        int k = localSparseArray.keyAt(i);
        ArrayMap localArrayMap = calculateNameOverrides(k, paramArrayList, paramArrayList1, paramInt1, paramInt2);
        FragmentContainerTransition localFragmentContainerTransition = (FragmentContainerTransition)localSparseArray.valueAt(i);
        if (paramBoolean) {
          configureTransitionsReordered(paramFragmentManagerImpl, k, localFragmentContainerTransition, (View)localObject, localArrayMap);
        } else {
          configureTransitionsOrdered(paramFragmentManagerImpl, k, localFragmentContainerTransition, (View)localObject, localArrayMap);
        }
      }
    }
  }
  
  public static class FragmentContainerTransition
  {
    public Fragment firstOut;
    public boolean firstOutIsPop;
    public BackStackRecord firstOutTransaction;
    public Fragment lastIn;
    public boolean lastInIsPop;
    public BackStackRecord lastInTransaction;
    
    public FragmentContainerTransition() {}
  }
}
