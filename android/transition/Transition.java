package android.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.util.LongSparseArray;
import android.util.SparseArray;
import android.util.SparseLongArray;
import android.view.InflateException;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowId;
import android.view.animation.AnimationUtils;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.android.internal.R.styleable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

public abstract class Transition
  implements Cloneable
{
  static final boolean DBG = false;
  private static final int[] DEFAULT_MATCH_ORDER = { 2, 1, 3, 4 };
  private static final String LOG_TAG = "Transition";
  private static final int MATCH_FIRST = 1;
  public static final int MATCH_ID = 3;
  private static final String MATCH_ID_STR = "id";
  public static final int MATCH_INSTANCE = 1;
  private static final String MATCH_INSTANCE_STR = "instance";
  public static final int MATCH_ITEM_ID = 4;
  private static final String MATCH_ITEM_ID_STR = "itemId";
  private static final int MATCH_LAST = 4;
  public static final int MATCH_NAME = 2;
  private static final String MATCH_NAME_STR = "name";
  private static final String MATCH_VIEW_NAME_STR = "viewName";
  private static final PathMotion STRAIGHT_PATH_MOTION = new PathMotion()
  {
    public Path getPath(float paramAnonymousFloat1, float paramAnonymousFloat2, float paramAnonymousFloat3, float paramAnonymousFloat4)
    {
      Path localPath = new Path();
      localPath.moveTo(paramAnonymousFloat1, paramAnonymousFloat2);
      localPath.lineTo(paramAnonymousFloat3, paramAnonymousFloat4);
      return localPath;
    }
  };
  private static ThreadLocal<ArrayMap<Animator, AnimationInfo>> sRunningAnimators = new ThreadLocal();
  ArrayList<Animator> mAnimators = new ArrayList();
  boolean mCanRemoveViews = false;
  private ArrayList<Animator> mCurrentAnimators = new ArrayList();
  long mDuration = -1L;
  private TransitionValuesMaps mEndValues = new TransitionValuesMaps();
  ArrayList<TransitionValues> mEndValuesList;
  private boolean mEnded = false;
  EpicenterCallback mEpicenterCallback;
  TimeInterpolator mInterpolator = null;
  ArrayList<TransitionListener> mListeners = null;
  int[] mMatchOrder = DEFAULT_MATCH_ORDER;
  private String mName = getClass().getName();
  ArrayMap<String, String> mNameOverrides;
  int mNumInstances = 0;
  TransitionSet mParent = null;
  PathMotion mPathMotion = STRAIGHT_PATH_MOTION;
  boolean mPaused = false;
  TransitionPropagation mPropagation;
  ViewGroup mSceneRoot = null;
  long mStartDelay = -1L;
  private TransitionValuesMaps mStartValues = new TransitionValuesMaps();
  ArrayList<TransitionValues> mStartValuesList;
  ArrayList<View> mTargetChildExcludes = null;
  ArrayList<View> mTargetExcludes = null;
  ArrayList<Integer> mTargetIdChildExcludes = null;
  ArrayList<Integer> mTargetIdExcludes = null;
  ArrayList<Integer> mTargetIds = new ArrayList();
  ArrayList<String> mTargetNameExcludes = null;
  ArrayList<String> mTargetNames = null;
  ArrayList<Class> mTargetTypeChildExcludes = null;
  ArrayList<Class> mTargetTypeExcludes = null;
  ArrayList<Class> mTargetTypes = null;
  ArrayList<View> mTargets = new ArrayList();
  
  public Transition() {}
  
  public Transition(Context paramContext, AttributeSet paramAttributeSet)
  {
    paramAttributeSet = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.Transition);
    long l = paramAttributeSet.getInt(1, -1);
    if (l >= 0L) {
      setDuration(l);
    }
    l = paramAttributeSet.getInt(2, -1);
    if (l > 0L) {
      setStartDelay(l);
    }
    int i = paramAttributeSet.getResourceId(0, 0);
    if (i > 0) {
      setInterpolator(AnimationUtils.loadInterpolator(paramContext, i));
    }
    paramContext = paramAttributeSet.getString(3);
    if (paramContext != null) {
      setMatchOrder(parseMatchOrder(paramContext));
    }
    paramAttributeSet.recycle();
  }
  
  private void addUnmatched(ArrayMap<View, TransitionValues> paramArrayMap1, ArrayMap<View, TransitionValues> paramArrayMap2)
  {
    int i = 0;
    for (int j = 0; j < paramArrayMap1.size(); j++)
    {
      TransitionValues localTransitionValues = (TransitionValues)paramArrayMap1.valueAt(j);
      if (isValidTarget(view))
      {
        mStartValuesList.add(localTransitionValues);
        mEndValuesList.add(null);
      }
    }
    for (j = i; j < paramArrayMap2.size(); j++)
    {
      paramArrayMap1 = (TransitionValues)paramArrayMap2.valueAt(j);
      if (isValidTarget(view))
      {
        mEndValuesList.add(paramArrayMap1);
        mStartValuesList.add(null);
      }
    }
  }
  
  static void addViewValues(TransitionValuesMaps paramTransitionValuesMaps, View paramView, TransitionValues paramTransitionValues)
  {
    viewValues.put(paramView, paramTransitionValues);
    int i = paramView.getId();
    if (i >= 0) {
      if (idValues.indexOfKey(i) >= 0) {
        idValues.put(i, null);
      } else {
        idValues.put(i, paramView);
      }
    }
    paramTransitionValues = paramView.getTransitionName();
    if (paramTransitionValues != null) {
      if (nameValues.containsKey(paramTransitionValues)) {
        nameValues.put(paramTransitionValues, null);
      } else {
        nameValues.put(paramTransitionValues, paramView);
      }
    }
    if ((paramView.getParent() instanceof ListView))
    {
      paramTransitionValues = (ListView)paramView.getParent();
      if (paramTransitionValues.getAdapter().hasStableIds())
      {
        long l = paramTransitionValues.getItemIdAtPosition(paramTransitionValues.getPositionForView(paramView));
        if (itemIdValues.indexOfKey(l) >= 0)
        {
          paramView = (View)itemIdValues.get(l);
          if (paramView != null)
          {
            paramView.setHasTransientState(false);
            itemIdValues.put(l, null);
          }
        }
        else
        {
          paramView.setHasTransientState(true);
          itemIdValues.put(l, paramView);
        }
      }
    }
  }
  
  private static boolean alreadyContains(int[] paramArrayOfInt, int paramInt)
  {
    int i = paramArrayOfInt[paramInt];
    for (int j = 0; j < paramInt; j++) {
      if (paramArrayOfInt[j] == i) {
        return true;
      }
    }
    return false;
  }
  
  private void captureHierarchy(View paramView, boolean paramBoolean)
  {
    if (paramView == null) {
      return;
    }
    int i = paramView.getId();
    if ((mTargetIdExcludes != null) && (mTargetIdExcludes.contains(Integer.valueOf(i)))) {
      return;
    }
    if ((mTargetExcludes != null) && (mTargetExcludes.contains(paramView))) {
      return;
    }
    Object localObject = mTargetTypeExcludes;
    int j = 0;
    int k;
    int m;
    if ((localObject != null) && (paramView != null))
    {
      k = mTargetTypeExcludes.size();
      for (m = 0; m < k; m++) {
        if (((Class)mTargetTypeExcludes.get(m)).isInstance(paramView)) {
          return;
        }
      }
    }
    if ((paramView.getParent() instanceof ViewGroup))
    {
      localObject = new TransitionValues();
      view = paramView;
      if (paramBoolean) {
        captureStartValues((TransitionValues)localObject);
      } else {
        captureEndValues((TransitionValues)localObject);
      }
      targetedTransitions.add(this);
      capturePropagationValues((TransitionValues)localObject);
      if (paramBoolean) {
        addViewValues(mStartValues, paramView, (TransitionValues)localObject);
      } else {
        addViewValues(mEndValues, paramView, (TransitionValues)localObject);
      }
    }
    if ((paramView instanceof ViewGroup))
    {
      if ((mTargetIdChildExcludes != null) && (mTargetIdChildExcludes.contains(Integer.valueOf(i)))) {
        return;
      }
      if ((mTargetChildExcludes != null) && (mTargetChildExcludes.contains(paramView))) {
        return;
      }
      if (mTargetTypeChildExcludes != null)
      {
        k = mTargetTypeChildExcludes.size();
        for (m = 0; m < k; m++) {
          if (((Class)mTargetTypeChildExcludes.get(m)).isInstance(paramView)) {
            return;
          }
        }
      }
      paramView = (ViewGroup)paramView;
      for (m = j; m < paramView.getChildCount(); m++) {
        captureHierarchy(paramView.getChildAt(m), paramBoolean);
      }
    }
  }
  
  private static <T> ArrayList<T> excludeObject(ArrayList<T> paramArrayList, T paramT, boolean paramBoolean)
  {
    Object localObject = paramArrayList;
    if (paramT != null) {
      if (paramBoolean) {
        localObject = ArrayListManager.add(paramArrayList, paramT);
      } else {
        localObject = ArrayListManager.remove(paramArrayList, paramT);
      }
    }
    return localObject;
  }
  
  private static ArrayMap<Animator, AnimationInfo> getRunningAnimators()
  {
    ArrayMap localArrayMap1 = (ArrayMap)sRunningAnimators.get();
    ArrayMap localArrayMap2 = localArrayMap1;
    if (localArrayMap1 == null)
    {
      localArrayMap2 = new ArrayMap();
      sRunningAnimators.set(localArrayMap2);
    }
    return localArrayMap2;
  }
  
  private static boolean isValidMatch(int paramInt)
  {
    boolean bool = true;
    if ((paramInt < 1) || (paramInt > 4)) {
      bool = false;
    }
    return bool;
  }
  
  private static boolean isValueChanged(TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2, String paramString)
  {
    if (values.containsKey(paramString) != values.containsKey(paramString)) {
      return false;
    }
    paramTransitionValues1 = values.get(paramString);
    paramTransitionValues2 = values.get(paramString);
    if ((paramTransitionValues1 == null) && (paramTransitionValues2 == null)) {}
    for (boolean bool = false;; bool = true)
    {
      break;
      if ((paramTransitionValues1 != null) && (paramTransitionValues2 != null))
      {
        bool = paramTransitionValues1.equals(paramTransitionValues2) ^ true;
        break;
      }
    }
    return bool;
  }
  
  private void matchIds(ArrayMap<View, TransitionValues> paramArrayMap1, ArrayMap<View, TransitionValues> paramArrayMap2, SparseArray<View> paramSparseArray1, SparseArray<View> paramSparseArray2)
  {
    int i = paramSparseArray1.size();
    for (int j = 0; j < i; j++)
    {
      View localView1 = (View)paramSparseArray1.valueAt(j);
      if ((localView1 != null) && (isValidTarget(localView1)))
      {
        View localView2 = (View)paramSparseArray2.get(paramSparseArray1.keyAt(j));
        if ((localView2 != null) && (isValidTarget(localView2)))
        {
          TransitionValues localTransitionValues1 = (TransitionValues)paramArrayMap1.get(localView1);
          TransitionValues localTransitionValues2 = (TransitionValues)paramArrayMap2.get(localView2);
          if ((localTransitionValues1 != null) && (localTransitionValues2 != null))
          {
            mStartValuesList.add(localTransitionValues1);
            mEndValuesList.add(localTransitionValues2);
            paramArrayMap1.remove(localView1);
            paramArrayMap2.remove(localView2);
          }
        }
      }
    }
  }
  
  private void matchInstances(ArrayMap<View, TransitionValues> paramArrayMap1, ArrayMap<View, TransitionValues> paramArrayMap2)
  {
    for (int i = paramArrayMap1.size() - 1; i >= 0; i--)
    {
      Object localObject = (View)paramArrayMap1.keyAt(i);
      if ((localObject != null) && (isValidTarget((View)localObject)))
      {
        TransitionValues localTransitionValues = (TransitionValues)paramArrayMap2.remove(localObject);
        if ((localTransitionValues != null) && (view != null) && (isValidTarget(view)))
        {
          localObject = (TransitionValues)paramArrayMap1.removeAt(i);
          mStartValuesList.add(localObject);
          mEndValuesList.add(localTransitionValues);
        }
      }
    }
  }
  
  private void matchItemIds(ArrayMap<View, TransitionValues> paramArrayMap1, ArrayMap<View, TransitionValues> paramArrayMap2, LongSparseArray<View> paramLongSparseArray1, LongSparseArray<View> paramLongSparseArray2)
  {
    int i = paramLongSparseArray1.size();
    for (int j = 0; j < i; j++)
    {
      View localView1 = (View)paramLongSparseArray1.valueAt(j);
      if ((localView1 != null) && (isValidTarget(localView1)))
      {
        View localView2 = (View)paramLongSparseArray2.get(paramLongSparseArray1.keyAt(j));
        if ((localView2 != null) && (isValidTarget(localView2)))
        {
          TransitionValues localTransitionValues1 = (TransitionValues)paramArrayMap1.get(localView1);
          TransitionValues localTransitionValues2 = (TransitionValues)paramArrayMap2.get(localView2);
          if ((localTransitionValues1 != null) && (localTransitionValues2 != null))
          {
            mStartValuesList.add(localTransitionValues1);
            mEndValuesList.add(localTransitionValues2);
            paramArrayMap1.remove(localView1);
            paramArrayMap2.remove(localView2);
          }
        }
      }
    }
  }
  
  private void matchNames(ArrayMap<View, TransitionValues> paramArrayMap1, ArrayMap<View, TransitionValues> paramArrayMap2, ArrayMap<String, View> paramArrayMap3, ArrayMap<String, View> paramArrayMap4)
  {
    int i = paramArrayMap3.size();
    for (int j = 0; j < i; j++)
    {
      View localView1 = (View)paramArrayMap3.valueAt(j);
      if ((localView1 != null) && (isValidTarget(localView1)))
      {
        View localView2 = (View)paramArrayMap4.get(paramArrayMap3.keyAt(j));
        if ((localView2 != null) && (isValidTarget(localView2)))
        {
          TransitionValues localTransitionValues1 = (TransitionValues)paramArrayMap1.get(localView1);
          TransitionValues localTransitionValues2 = (TransitionValues)paramArrayMap2.get(localView2);
          if ((localTransitionValues1 != null) && (localTransitionValues2 != null))
          {
            mStartValuesList.add(localTransitionValues1);
            mEndValuesList.add(localTransitionValues2);
            paramArrayMap1.remove(localView1);
            paramArrayMap2.remove(localView2);
          }
        }
      }
    }
  }
  
  private void matchStartAndEnd(TransitionValuesMaps paramTransitionValuesMaps1, TransitionValuesMaps paramTransitionValuesMaps2)
  {
    ArrayMap localArrayMap1 = new ArrayMap(viewValues);
    ArrayMap localArrayMap2 = new ArrayMap(viewValues);
    for (int i = 0; i < mMatchOrder.length; i++) {
      switch (mMatchOrder[i])
      {
      default: 
        break;
      case 4: 
        matchItemIds(localArrayMap1, localArrayMap2, itemIdValues, itemIdValues);
        break;
      case 3: 
        matchIds(localArrayMap1, localArrayMap2, idValues, idValues);
        break;
      case 2: 
        matchNames(localArrayMap1, localArrayMap2, nameValues, nameValues);
        break;
      case 1: 
        matchInstances(localArrayMap1, localArrayMap2);
      }
    }
    addUnmatched(localArrayMap1, localArrayMap2);
  }
  
  private static int[] parseMatchOrder(String paramString)
  {
    StringTokenizer localStringTokenizer = new StringTokenizer(paramString, ",");
    paramString = new int[localStringTokenizer.countTokens()];
    int i = 0;
    while (localStringTokenizer.hasMoreTokens())
    {
      Object localObject = localStringTokenizer.nextToken().trim();
      if ("id".equalsIgnoreCase((String)localObject))
      {
        paramString[i] = 3;
      }
      else if ("instance".equalsIgnoreCase((String)localObject))
      {
        paramString[i] = 1;
      }
      else if ("name".equalsIgnoreCase((String)localObject))
      {
        paramString[i] = 2;
      }
      else if ("viewName".equalsIgnoreCase((String)localObject))
      {
        paramString[i] = 2;
      }
      else if ("itemId".equalsIgnoreCase((String)localObject))
      {
        paramString[i] = 4;
      }
      else
      {
        if (!((String)localObject).isEmpty()) {
          break label149;
        }
        localObject = new int[paramString.length - 1];
        System.arraycopy(paramString, 0, localObject, 0, i);
        paramString = (String)localObject;
        i--;
      }
      i++;
      continue;
      label149:
      paramString = new StringBuilder();
      paramString.append("Unknown match type in matchOrder: '");
      paramString.append((String)localObject);
      paramString.append("'");
      throw new InflateException(paramString.toString());
    }
    return paramString;
  }
  
  private void runAnimator(Animator paramAnimator, final ArrayMap<Animator, AnimationInfo> paramArrayMap)
  {
    if (paramAnimator != null)
    {
      paramAnimator.addListener(new AnimatorListenerAdapter()
      {
        public void onAnimationEnd(Animator paramAnonymousAnimator)
        {
          paramArrayMap.remove(paramAnonymousAnimator);
          mCurrentAnimators.remove(paramAnonymousAnimator);
        }
        
        public void onAnimationStart(Animator paramAnonymousAnimator)
        {
          mCurrentAnimators.add(paramAnonymousAnimator);
        }
      });
      animate(paramAnimator);
    }
  }
  
  public Transition addListener(TransitionListener paramTransitionListener)
  {
    if (mListeners == null) {
      mListeners = new ArrayList();
    }
    mListeners.add(paramTransitionListener);
    return this;
  }
  
  public Transition addTarget(int paramInt)
  {
    if (paramInt > 0) {
      mTargetIds.add(Integer.valueOf(paramInt));
    }
    return this;
  }
  
  public Transition addTarget(View paramView)
  {
    mTargets.add(paramView);
    return this;
  }
  
  public Transition addTarget(Class paramClass)
  {
    if (paramClass != null)
    {
      if (mTargetTypes == null) {
        mTargetTypes = new ArrayList();
      }
      mTargetTypes.add(paramClass);
    }
    return this;
  }
  
  public Transition addTarget(String paramString)
  {
    if (paramString != null)
    {
      if (mTargetNames == null) {
        mTargetNames = new ArrayList();
      }
      mTargetNames.add(paramString);
    }
    return this;
  }
  
  protected void animate(Animator paramAnimator)
  {
    if (paramAnimator == null)
    {
      end();
    }
    else
    {
      if (getDuration() >= 0L) {
        paramAnimator.setDuration(getDuration());
      }
      if (getStartDelay() >= 0L) {
        paramAnimator.setStartDelay(getStartDelay() + paramAnimator.getStartDelay());
      }
      if (getInterpolator() != null) {
        paramAnimator.setInterpolator(getInterpolator());
      }
      paramAnimator.addListener(new AnimatorListenerAdapter()
      {
        public void onAnimationEnd(Animator paramAnonymousAnimator)
        {
          end();
          paramAnonymousAnimator.removeListener(this);
        }
      });
      paramAnimator.start();
    }
  }
  
  public boolean canRemoveViews()
  {
    return mCanRemoveViews;
  }
  
  protected void cancel()
  {
    for (int i = mCurrentAnimators.size() - 1; i >= 0; i--) {
      ((Animator)mCurrentAnimators.get(i)).cancel();
    }
    if ((mListeners != null) && (mListeners.size() > 0))
    {
      ArrayList localArrayList = (ArrayList)mListeners.clone();
      int j = localArrayList.size();
      for (i = 0; i < j; i++) {
        ((TransitionListener)localArrayList.get(i)).onTransitionCancel(this);
      }
    }
  }
  
  public abstract void captureEndValues(TransitionValues paramTransitionValues);
  
  void capturePropagationValues(TransitionValues paramTransitionValues)
  {
    if ((mPropagation != null) && (!values.isEmpty()))
    {
      String[] arrayOfString = mPropagation.getPropagationProperties();
      if (arrayOfString == null) {
        return;
      }
      int i = 1;
      int k;
      for (int j = 0;; j++)
      {
        k = i;
        if (j >= arrayOfString.length) {
          break;
        }
        if (!values.containsKey(arrayOfString[j]))
        {
          k = 0;
          break;
        }
      }
      if (k == 0) {
        mPropagation.captureValues(paramTransitionValues);
      }
    }
  }
  
  public abstract void captureStartValues(TransitionValues paramTransitionValues);
  
  void captureValues(ViewGroup paramViewGroup, boolean paramBoolean)
  {
    clearValues(paramBoolean);
    int i = mTargetIds.size();
    int j = 0;
    Object localObject1;
    Object localObject2;
    if (((i <= 0) && (mTargets.size() <= 0)) || ((mTargetNames != null) && (!mTargetNames.isEmpty())) || ((mTargetTypes != null) && (!mTargetTypes.isEmpty())))
    {
      captureHierarchy(paramViewGroup, paramBoolean);
    }
    else
    {
      for (i = 0; i < mTargetIds.size(); i++)
      {
        localObject1 = paramViewGroup.findViewById(((Integer)mTargetIds.get(i)).intValue());
        if (localObject1 != null)
        {
          localObject2 = new TransitionValues();
          view = ((View)localObject1);
          if (paramBoolean) {
            captureStartValues((TransitionValues)localObject2);
          } else {
            captureEndValues((TransitionValues)localObject2);
          }
          targetedTransitions.add(this);
          capturePropagationValues((TransitionValues)localObject2);
          if (paramBoolean) {
            addViewValues(mStartValues, (View)localObject1, (TransitionValues)localObject2);
          } else {
            addViewValues(mEndValues, (View)localObject1, (TransitionValues)localObject2);
          }
        }
      }
      for (i = 0; i < mTargets.size(); i++)
      {
        localObject2 = (View)mTargets.get(i);
        paramViewGroup = new TransitionValues();
        view = ((View)localObject2);
        if (paramBoolean) {
          captureStartValues(paramViewGroup);
        } else {
          captureEndValues(paramViewGroup);
        }
        targetedTransitions.add(this);
        capturePropagationValues(paramViewGroup);
        if (paramBoolean) {
          addViewValues(mStartValues, (View)localObject2, paramViewGroup);
        } else {
          addViewValues(mEndValues, (View)localObject2, paramViewGroup);
        }
      }
    }
    if ((!paramBoolean) && (mNameOverrides != null))
    {
      int k = mNameOverrides.size();
      paramViewGroup = new ArrayList(k);
      for (i = 0; i < k; i++)
      {
        localObject2 = (String)mNameOverrides.keyAt(i);
        paramViewGroup.add((View)mStartValues.nameValues.remove(localObject2));
      }
      for (i = j; i < k; i++)
      {
        localObject2 = (View)paramViewGroup.get(i);
        if (localObject2 != null)
        {
          localObject1 = (String)mNameOverrides.valueAt(i);
          mStartValues.nameValues.put(localObject1, localObject2);
        }
      }
    }
  }
  
  void clearValues(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      mStartValues.viewValues.clear();
      mStartValues.idValues.clear();
      mStartValues.itemIdValues.clear();
      mStartValues.nameValues.clear();
      mStartValuesList = null;
    }
    else
    {
      mEndValues.viewValues.clear();
      mEndValues.idValues.clear();
      mEndValues.itemIdValues.clear();
      mEndValues.nameValues.clear();
      mEndValuesList = null;
    }
  }
  
  public Transition clone()
  {
    Object localObject1 = null;
    try
    {
      Transition localTransition = (Transition)super.clone();
      localObject1 = localTransition;
      Object localObject2 = new java/util/ArrayList;
      localObject1 = localTransition;
      ((ArrayList)localObject2).<init>();
      localObject1 = localTransition;
      mAnimators = ((ArrayList)localObject2);
      localObject1 = localTransition;
      localObject2 = new android/transition/TransitionValuesMaps;
      localObject1 = localTransition;
      ((TransitionValuesMaps)localObject2).<init>();
      localObject1 = localTransition;
      mStartValues = ((TransitionValuesMaps)localObject2);
      localObject1 = localTransition;
      localObject2 = new android/transition/TransitionValuesMaps;
      localObject1 = localTransition;
      ((TransitionValuesMaps)localObject2).<init>();
      localObject1 = localTransition;
      mEndValues = ((TransitionValuesMaps)localObject2);
      localObject1 = localTransition;
      mStartValuesList = null;
      localObject1 = localTransition;
      mEndValuesList = null;
      localObject1 = localTransition;
    }
    catch (CloneNotSupportedException localCloneNotSupportedException) {}
    return localObject1;
  }
  
  public Animator createAnimator(ViewGroup paramViewGroup, TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2)
  {
    return null;
  }
  
  protected void createAnimators(ViewGroup paramViewGroup, TransitionValuesMaps paramTransitionValuesMaps1, TransitionValuesMaps paramTransitionValuesMaps2, ArrayList<TransitionValues> paramArrayList1, ArrayList<TransitionValues> paramArrayList2)
  {
    ArrayMap localArrayMap = getRunningAnimators();
    int i = mAnimators.size();
    SparseLongArray localSparseLongArray = new SparseLongArray();
    int j = paramArrayList1.size();
    long l1 = Long.MAX_VALUE;
    int k = 0;
    int m;
    for (;;)
    {
      m = k;
      if (m >= j) {
        break;
      }
      Object localObject1 = (TransitionValues)paramArrayList1.get(m);
      paramTransitionValuesMaps1 = (TransitionValues)paramArrayList2.get(m);
      Object localObject2 = localObject1;
      if (localObject1 != null)
      {
        localObject2 = localObject1;
        if (!targetedTransitions.contains(this)) {
          localObject2 = null;
        }
      }
      TransitionValuesMaps localTransitionValuesMaps = paramTransitionValuesMaps1;
      if (paramTransitionValuesMaps1 != null)
      {
        localTransitionValuesMaps = paramTransitionValuesMaps1;
        if (!targetedTransitions.contains(this)) {
          localTransitionValuesMaps = null;
        }
      }
      long l2;
      if ((localObject2 == null) && (localTransitionValuesMaps == null))
      {
        l2 = l1;
        k = m;
      }
      else
      {
        int n;
        if ((localObject2 != null) && (localTransitionValuesMaps != null) && (!isTransitionRequired(localObject2, localTransitionValuesMaps))) {
          n = 0;
        } else {
          n = 1;
        }
        if (n != 0)
        {
          localObject1 = createAnimator(paramViewGroup, localObject2, localTransitionValuesMaps);
          if (localObject1 != null)
          {
            Object localObject3 = null;
            paramTransitionValuesMaps1 = null;
            Object localObject4;
            if (localTransitionValuesMaps != null)
            {
              localObject3 = view;
              String[] arrayOfString = getTransitionProperties();
              if ((localObject3 != null) && (arrayOfString != null)) {
                if (arrayOfString.length > 0)
                {
                  localObject4 = new TransitionValues();
                  view = ((View)localObject3);
                  paramTransitionValuesMaps1 = (TransitionValues)viewValues.get(localObject3);
                  Object localObject5 = paramTransitionValuesMaps1;
                  k = m;
                  if (paramTransitionValuesMaps1 != null) {
                    for (n = 0;; n++)
                    {
                      localObject5 = paramTransitionValuesMaps1;
                      k = m;
                      if (n >= arrayOfString.length) {
                        break;
                      }
                      values.put(arrayOfString[n], values.get(arrayOfString[n]));
                    }
                  }
                  m = k;
                  n = localArrayMap.size();
                  k = 0;
                  paramTransitionValuesMaps1 = (TransitionValuesMaps)localObject3;
                  while (k < n)
                  {
                    localObject5 = (AnimationInfo)localArrayMap.get((Animator)localArrayMap.keyAt(k));
                    if ((values != null) && (view == paramTransitionValuesMaps1))
                    {
                      if ((name == null) && (getName() == null)) {
                        break label429;
                      }
                      if (name.equals(getName()))
                      {
                        label429:
                        localObject3 = paramTransitionValuesMaps1;
                        if (values.equals(localObject4))
                        {
                          paramTransitionValuesMaps1 = (TransitionValuesMaps)localObject4;
                          localObject4 = null;
                          localObject1 = localObject3;
                          break label496;
                        }
                      }
                    }
                    k++;
                  }
                  localObject3 = paramTransitionValuesMaps1;
                  paramTransitionValuesMaps1 = (TransitionValuesMaps)localObject4;
                  localObject4 = localObject1;
                  localObject1 = localObject3;
                  break label496;
                }
              }
              m = k;
              localObject4 = localObject1;
              localObject1 = localObject3;
            }
            else
            {
              label496:
              if (localObject2 != null) {
                paramTransitionValuesMaps1 = view;
              } else {
                paramTransitionValuesMaps1 = null;
              }
              localObject4 = localObject1;
              localObject1 = paramTransitionValuesMaps1;
              paramTransitionValuesMaps1 = (TransitionValuesMaps)localObject3;
            }
            l2 = l1;
            k = m;
            if (localObject4 == null) {
              break label639;
            }
            l2 = l1;
            if (mPropagation != null)
            {
              l2 = mPropagation.getStartDelay(paramViewGroup, this, localObject2, localTransitionValuesMaps);
              localSparseLongArray.put(mAnimators.size(), l2);
              l2 = Math.min(l2, l1);
            }
            localArrayMap.put(localObject4, new AnimationInfo((View)localObject1, getName(), this, paramViewGroup.getWindowId(), paramTransitionValuesMaps1));
            mAnimators.add(localObject4);
            k = m;
            break label639;
          }
        }
        k = m;
        l2 = l1;
      }
      label639:
      k++;
      l1 = l2;
    }
    if (localSparseLongArray.size() != 0) {
      for (m = 0; m < localSparseLongArray.size(); m++)
      {
        k = localSparseLongArray.keyAt(m);
        paramViewGroup = (Animator)mAnimators.get(k);
        paramViewGroup.setStartDelay(localSparseLongArray.valueAt(m) - l1 + paramViewGroup.getStartDelay());
      }
    }
  }
  
  protected void end()
  {
    mNumInstances -= 1;
    if (mNumInstances == 0)
    {
      Object localObject;
      if ((mListeners != null) && (mListeners.size() > 0))
      {
        localObject = (ArrayList)mListeners.clone();
        int i = ((ArrayList)localObject).size();
        for (j = 0; j < i; j++) {
          ((TransitionListener)((ArrayList)localObject).get(j)).onTransitionEnd(this);
        }
      }
      for (int j = 0; j < mStartValues.itemIdValues.size(); j++)
      {
        localObject = (View)mStartValues.itemIdValues.valueAt(j);
        if (localObject != null) {
          ((View)localObject).setHasTransientState(false);
        }
      }
      for (j = 0; j < mEndValues.itemIdValues.size(); j++)
      {
        localObject = (View)mEndValues.itemIdValues.valueAt(j);
        if (localObject != null) {
          ((View)localObject).setHasTransientState(false);
        }
      }
      mEnded = true;
    }
  }
  
  public Transition excludeChildren(int paramInt, boolean paramBoolean)
  {
    if (paramInt >= 0) {
      mTargetIdChildExcludes = excludeObject(mTargetIdChildExcludes, Integer.valueOf(paramInt), paramBoolean);
    }
    return this;
  }
  
  public Transition excludeChildren(View paramView, boolean paramBoolean)
  {
    mTargetChildExcludes = excludeObject(mTargetChildExcludes, paramView, paramBoolean);
    return this;
  }
  
  public Transition excludeChildren(Class paramClass, boolean paramBoolean)
  {
    mTargetTypeChildExcludes = excludeObject(mTargetTypeChildExcludes, paramClass, paramBoolean);
    return this;
  }
  
  public Transition excludeTarget(int paramInt, boolean paramBoolean)
  {
    if (paramInt >= 0) {
      mTargetIdExcludes = excludeObject(mTargetIdExcludes, Integer.valueOf(paramInt), paramBoolean);
    }
    return this;
  }
  
  public Transition excludeTarget(View paramView, boolean paramBoolean)
  {
    mTargetExcludes = excludeObject(mTargetExcludes, paramView, paramBoolean);
    return this;
  }
  
  public Transition excludeTarget(Class paramClass, boolean paramBoolean)
  {
    mTargetTypeExcludes = excludeObject(mTargetTypeExcludes, paramClass, paramBoolean);
    return this;
  }
  
  public Transition excludeTarget(String paramString, boolean paramBoolean)
  {
    mTargetNameExcludes = excludeObject(mTargetNameExcludes, paramString, paramBoolean);
    return this;
  }
  
  void forceToEnd(ViewGroup paramViewGroup)
  {
    ArrayMap localArrayMap = getRunningAnimators();
    int i = localArrayMap.size();
    if (paramViewGroup != null)
    {
      paramViewGroup = paramViewGroup.getWindowId();
      i--;
      while (i >= 0)
      {
        AnimationInfo localAnimationInfo = (AnimationInfo)localArrayMap.valueAt(i);
        if ((view != null) && (paramViewGroup != null) && (paramViewGroup.equals(windowId))) {
          ((Animator)localArrayMap.keyAt(i)).end();
        }
        i--;
      }
    }
  }
  
  public long getDuration()
  {
    return mDuration;
  }
  
  public Rect getEpicenter()
  {
    if (mEpicenterCallback == null) {
      return null;
    }
    return mEpicenterCallback.onGetEpicenter(this);
  }
  
  public EpicenterCallback getEpicenterCallback()
  {
    return mEpicenterCallback;
  }
  
  public TimeInterpolator getInterpolator()
  {
    return mInterpolator;
  }
  
  TransitionValues getMatchedTransitionValues(View paramView, boolean paramBoolean)
  {
    if (mParent != null) {
      return mParent.getMatchedTransitionValues(paramView, paramBoolean);
    }
    ArrayList localArrayList;
    if (paramBoolean) {
      localArrayList = mStartValuesList;
    } else {
      localArrayList = mEndValuesList;
    }
    if (localArrayList == null) {
      return null;
    }
    int i = localArrayList.size();
    int j = -1;
    int m;
    for (int k = 0;; k++)
    {
      m = j;
      if (k >= i) {
        break;
      }
      TransitionValues localTransitionValues = (TransitionValues)localArrayList.get(k);
      if (localTransitionValues == null) {
        return null;
      }
      if (view == paramView)
      {
        m = k;
        break;
      }
    }
    paramView = null;
    if (m >= 0)
    {
      if (paramBoolean) {
        paramView = mEndValuesList;
      } else {
        paramView = mStartValuesList;
      }
      paramView = (TransitionValues)paramView.get(m);
    }
    return paramView;
  }
  
  public String getName()
  {
    return mName;
  }
  
  public ArrayMap<String, String> getNameOverrides()
  {
    return mNameOverrides;
  }
  
  public PathMotion getPathMotion()
  {
    return mPathMotion;
  }
  
  public TransitionPropagation getPropagation()
  {
    return mPropagation;
  }
  
  public long getStartDelay()
  {
    return mStartDelay;
  }
  
  public List<Integer> getTargetIds()
  {
    return mTargetIds;
  }
  
  public List<String> getTargetNames()
  {
    return mTargetNames;
  }
  
  public List<Class> getTargetTypes()
  {
    return mTargetTypes;
  }
  
  public List<String> getTargetViewNames()
  {
    return mTargetNames;
  }
  
  public List<View> getTargets()
  {
    return mTargets;
  }
  
  public String[] getTransitionProperties()
  {
    return null;
  }
  
  public TransitionValues getTransitionValues(View paramView, boolean paramBoolean)
  {
    if (mParent != null) {
      return mParent.getTransitionValues(paramView, paramBoolean);
    }
    TransitionValuesMaps localTransitionValuesMaps;
    if (paramBoolean) {
      localTransitionValuesMaps = mStartValues;
    } else {
      localTransitionValuesMaps = mEndValues;
    }
    return (TransitionValues)viewValues.get(paramView);
  }
  
  public boolean isTransitionRequired(TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2)
  {
    boolean bool1 = false;
    boolean bool2 = false;
    boolean bool3 = bool1;
    if (paramTransitionValues1 != null)
    {
      bool3 = bool1;
      if (paramTransitionValues2 != null)
      {
        Object localObject = getTransitionProperties();
        if (localObject != null)
        {
          int i = localObject.length;
          for (int j = 0;; j++)
          {
            bool3 = bool2;
            if (j >= i) {
              break;
            }
            if (isValueChanged(paramTransitionValues1, paramTransitionValues2, localObject[j]))
            {
              bool3 = true;
              break;
            }
          }
        }
        else
        {
          localObject = values.keySet().iterator();
          for (;;)
          {
            bool3 = bool1;
            if (!((Iterator)localObject).hasNext()) {
              break;
            }
            if (isValueChanged(paramTransitionValues1, paramTransitionValues2, (String)((Iterator)localObject).next()))
            {
              bool3 = true;
              break;
            }
          }
        }
      }
    }
    return bool3;
  }
  
  public boolean isValidTarget(View paramView)
  {
    if (paramView == null) {
      return false;
    }
    int i = paramView.getId();
    if ((mTargetIdExcludes != null) && (mTargetIdExcludes.contains(Integer.valueOf(i)))) {
      return false;
    }
    if ((mTargetExcludes != null) && (mTargetExcludes.contains(paramView))) {
      return false;
    }
    int k;
    if ((mTargetTypeExcludes != null) && (paramView != null))
    {
      int j = mTargetTypeExcludes.size();
      for (k = 0; k < j; k++) {
        if (((Class)mTargetTypeExcludes.get(k)).isInstance(paramView)) {
          return false;
        }
      }
    }
    if ((mTargetNameExcludes != null) && (paramView != null) && (paramView.getTransitionName() != null) && (mTargetNameExcludes.contains(paramView.getTransitionName()))) {
      return false;
    }
    if ((mTargetIds.size() == 0) && (mTargets.size() == 0) && ((mTargetTypes == null) || (mTargetTypes.isEmpty())) && ((mTargetNames == null) || (mTargetNames.isEmpty()))) {
      return true;
    }
    if ((!mTargetIds.contains(Integer.valueOf(i))) && (!mTargets.contains(paramView)))
    {
      if ((mTargetNames != null) && (mTargetNames.contains(paramView.getTransitionName()))) {
        return true;
      }
      if (mTargetTypes != null) {
        for (k = 0; k < mTargetTypes.size(); k++) {
          if (((Class)mTargetTypes.get(k)).isInstance(paramView)) {
            return true;
          }
        }
      }
      return false;
    }
    return true;
  }
  
  public void pause(View paramView)
  {
    if (!mEnded)
    {
      ArrayMap localArrayMap = getRunningAnimators();
      int i = localArrayMap.size();
      if (paramView != null)
      {
        WindowId localWindowId = paramView.getWindowId();
        i--;
        while (i >= 0)
        {
          paramView = (AnimationInfo)localArrayMap.valueAt(i);
          if ((view != null) && (localWindowId != null) && (localWindowId.equals(windowId))) {
            ((Animator)localArrayMap.keyAt(i)).pause();
          }
          i--;
        }
      }
      if ((mListeners != null) && (mListeners.size() > 0))
      {
        paramView = (ArrayList)mListeners.clone();
        int j = paramView.size();
        for (i = 0; i < j; i++) {
          ((TransitionListener)paramView.get(i)).onTransitionPause(this);
        }
      }
      mPaused = true;
    }
  }
  
  void playTransition(ViewGroup paramViewGroup)
  {
    mStartValuesList = new ArrayList();
    mEndValuesList = new ArrayList();
    matchStartAndEnd(mStartValues, mEndValues);
    ArrayMap localArrayMap = getRunningAnimators();
    int i = localArrayMap.size();
    WindowId localWindowId = paramViewGroup.getWindowId();
    i--;
    while (i >= 0)
    {
      Animator localAnimator = (Animator)localArrayMap.keyAt(i);
      if (localAnimator != null)
      {
        AnimationInfo localAnimationInfo = (AnimationInfo)localArrayMap.get(localAnimator);
        if ((localAnimationInfo != null) && (view != null) && (windowId == localWindowId))
        {
          TransitionValues localTransitionValues1 = values;
          View localView = view;
          int j = 1;
          TransitionValues localTransitionValues2 = getTransitionValues(localView, true);
          TransitionValues localTransitionValues3 = getMatchedTransitionValues(localView, true);
          TransitionValues localTransitionValues4 = localTransitionValues3;
          if (localTransitionValues2 == null)
          {
            localTransitionValues4 = localTransitionValues3;
            if (localTransitionValues3 == null) {
              localTransitionValues4 = (TransitionValues)mEndValues.viewValues.get(localView);
            }
          }
          if (((localTransitionValues2 == null) && (localTransitionValues4 == null)) || (!transition.isTransitionRequired(localTransitionValues1, localTransitionValues4))) {
            j = 0;
          }
          if (j != 0) {
            if ((!localAnimator.isRunning()) && (!localAnimator.isStarted())) {
              localArrayMap.remove(localAnimator);
            } else {
              localAnimator.cancel();
            }
          }
        }
      }
      i--;
    }
    createAnimators(paramViewGroup, mStartValues, mEndValues, mStartValuesList, mEndValuesList);
    runAnimators();
  }
  
  public Transition removeListener(TransitionListener paramTransitionListener)
  {
    if (mListeners == null) {
      return this;
    }
    mListeners.remove(paramTransitionListener);
    if (mListeners.size() == 0) {
      mListeners = null;
    }
    return this;
  }
  
  public Transition removeTarget(int paramInt)
  {
    if (paramInt > 0) {
      mTargetIds.remove(Integer.valueOf(paramInt));
    }
    return this;
  }
  
  public Transition removeTarget(View paramView)
  {
    if (paramView != null) {
      mTargets.remove(paramView);
    }
    return this;
  }
  
  public Transition removeTarget(Class paramClass)
  {
    if (paramClass != null) {
      mTargetTypes.remove(paramClass);
    }
    return this;
  }
  
  public Transition removeTarget(String paramString)
  {
    if ((paramString != null) && (mTargetNames != null)) {
      mTargetNames.remove(paramString);
    }
    return this;
  }
  
  public void resume(View paramView)
  {
    if (mPaused)
    {
      if (!mEnded)
      {
        ArrayMap localArrayMap = getRunningAnimators();
        int i = localArrayMap.size();
        WindowId localWindowId = paramView.getWindowId();
        i--;
        while (i >= 0)
        {
          paramView = (AnimationInfo)localArrayMap.valueAt(i);
          if ((view != null) && (localWindowId != null) && (localWindowId.equals(windowId))) {
            ((Animator)localArrayMap.keyAt(i)).resume();
          }
          i--;
        }
        if ((mListeners != null) && (mListeners.size() > 0))
        {
          paramView = (ArrayList)mListeners.clone();
          int j = paramView.size();
          for (i = 0; i < j; i++) {
            ((TransitionListener)paramView.get(i)).onTransitionResume(this);
          }
        }
      }
      mPaused = false;
    }
  }
  
  protected void runAnimators()
  {
    start();
    ArrayMap localArrayMap = getRunningAnimators();
    Iterator localIterator = mAnimators.iterator();
    while (localIterator.hasNext())
    {
      Animator localAnimator = (Animator)localIterator.next();
      if (localArrayMap.containsKey(localAnimator))
      {
        start();
        runAnimator(localAnimator, localArrayMap);
      }
    }
    mAnimators.clear();
    end();
  }
  
  void setCanRemoveViews(boolean paramBoolean)
  {
    mCanRemoveViews = paramBoolean;
  }
  
  public Transition setDuration(long paramLong)
  {
    mDuration = paramLong;
    return this;
  }
  
  public void setEpicenterCallback(EpicenterCallback paramEpicenterCallback)
  {
    mEpicenterCallback = paramEpicenterCallback;
  }
  
  public Transition setInterpolator(TimeInterpolator paramTimeInterpolator)
  {
    mInterpolator = paramTimeInterpolator;
    return this;
  }
  
  public void setMatchOrder(int... paramVarArgs)
  {
    if ((paramVarArgs != null) && (paramVarArgs.length != 0))
    {
      int i = 0;
      while (i < paramVarArgs.length) {
        if (isValidMatch(paramVarArgs[i]))
        {
          if (!alreadyContains(paramVarArgs, i)) {
            i++;
          } else {
            throw new IllegalArgumentException("matches contains a duplicate value");
          }
        }
        else {
          throw new IllegalArgumentException("matches contains invalid value");
        }
      }
      mMatchOrder = ((int[])paramVarArgs.clone());
    }
    else
    {
      mMatchOrder = DEFAULT_MATCH_ORDER;
    }
  }
  
  public void setNameOverrides(ArrayMap<String, String> paramArrayMap)
  {
    mNameOverrides = paramArrayMap;
  }
  
  public void setPathMotion(PathMotion paramPathMotion)
  {
    if (paramPathMotion == null) {
      mPathMotion = STRAIGHT_PATH_MOTION;
    } else {
      mPathMotion = paramPathMotion;
    }
  }
  
  public void setPropagation(TransitionPropagation paramTransitionPropagation)
  {
    mPropagation = paramTransitionPropagation;
  }
  
  Transition setSceneRoot(ViewGroup paramViewGroup)
  {
    mSceneRoot = paramViewGroup;
    return this;
  }
  
  public Transition setStartDelay(long paramLong)
  {
    mStartDelay = paramLong;
    return this;
  }
  
  protected void start()
  {
    if (mNumInstances == 0)
    {
      if ((mListeners != null) && (mListeners.size() > 0))
      {
        ArrayList localArrayList = (ArrayList)mListeners.clone();
        int i = localArrayList.size();
        for (int j = 0; j < i; j++) {
          ((TransitionListener)localArrayList.get(j)).onTransitionStart(this);
        }
      }
      mEnded = false;
    }
    mNumInstances += 1;
  }
  
  public String toString()
  {
    return toString("");
  }
  
  String toString(String paramString)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append(paramString);
    ((StringBuilder)localObject).append(getClass().getSimpleName());
    ((StringBuilder)localObject).append("@");
    ((StringBuilder)localObject).append(Integer.toHexString(hashCode()));
    ((StringBuilder)localObject).append(": ");
    localObject = ((StringBuilder)localObject).toString();
    paramString = (String)localObject;
    if (mDuration != -1L)
    {
      paramString = new StringBuilder();
      paramString.append((String)localObject);
      paramString.append("dur(");
      paramString.append(mDuration);
      paramString.append(") ");
      paramString = paramString.toString();
    }
    localObject = paramString;
    if (mStartDelay != -1L)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(paramString);
      ((StringBuilder)localObject).append("dly(");
      ((StringBuilder)localObject).append(mStartDelay);
      ((StringBuilder)localObject).append(") ");
      localObject = ((StringBuilder)localObject).toString();
    }
    paramString = (String)localObject;
    if (mInterpolator != null)
    {
      paramString = new StringBuilder();
      paramString.append((String)localObject);
      paramString.append("interp(");
      paramString.append(mInterpolator);
      paramString.append(") ");
      paramString = paramString.toString();
    }
    if (mTargetIds.size() <= 0)
    {
      localObject = paramString;
      if (mTargets.size() <= 0) {}
    }
    else
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(paramString);
      ((StringBuilder)localObject).append("tgts(");
      localObject = ((StringBuilder)localObject).toString();
      int i = mTargetIds.size();
      int j = 0;
      paramString = (String)localObject;
      if (i > 0)
      {
        paramString = (String)localObject;
        for (i = 0; i < mTargetIds.size(); i++)
        {
          localObject = paramString;
          if (i > 0)
          {
            localObject = new StringBuilder();
            ((StringBuilder)localObject).append(paramString);
            ((StringBuilder)localObject).append(", ");
            localObject = ((StringBuilder)localObject).toString();
          }
          paramString = new StringBuilder();
          paramString.append((String)localObject);
          paramString.append(mTargetIds.get(i));
          paramString = paramString.toString();
        }
      }
      localObject = paramString;
      if (mTargets.size() > 0) {
        for (i = j;; i++)
        {
          localObject = paramString;
          if (i >= mTargets.size()) {
            break;
          }
          localObject = paramString;
          if (i > 0)
          {
            localObject = new StringBuilder();
            ((StringBuilder)localObject).append(paramString);
            ((StringBuilder)localObject).append(", ");
            localObject = ((StringBuilder)localObject).toString();
          }
          paramString = new StringBuilder();
          paramString.append((String)localObject);
          paramString.append(mTargets.get(i));
          paramString = paramString.toString();
        }
      }
      paramString = new StringBuilder();
      paramString.append((String)localObject);
      paramString.append(")");
      localObject = paramString.toString();
    }
    return localObject;
  }
  
  public static class AnimationInfo
  {
    String name;
    Transition transition;
    TransitionValues values;
    public View view;
    WindowId windowId;
    
    AnimationInfo(View paramView, String paramString, Transition paramTransition, WindowId paramWindowId, TransitionValues paramTransitionValues)
    {
      view = paramView;
      name = paramString;
      values = paramTransitionValues;
      windowId = paramWindowId;
      transition = paramTransition;
    }
  }
  
  private static class ArrayListManager
  {
    private ArrayListManager() {}
    
    static <T> ArrayList<T> add(ArrayList<T> paramArrayList, T paramT)
    {
      Object localObject = paramArrayList;
      if (paramArrayList == null) {
        localObject = new ArrayList();
      }
      if (!((ArrayList)localObject).contains(paramT)) {
        ((ArrayList)localObject).add(paramT);
      }
      return localObject;
    }
    
    static <T> ArrayList<T> remove(ArrayList<T> paramArrayList, T paramT)
    {
      ArrayList<T> localArrayList = paramArrayList;
      if (paramArrayList != null)
      {
        paramArrayList.remove(paramT);
        localArrayList = paramArrayList;
        if (paramArrayList.isEmpty()) {
          localArrayList = null;
        }
      }
      return localArrayList;
    }
  }
  
  public static abstract class EpicenterCallback
  {
    public EpicenterCallback() {}
    
    public abstract Rect onGetEpicenter(Transition paramTransition);
  }
  
  public static abstract interface TransitionListener
  {
    public abstract void onTransitionCancel(Transition paramTransition);
    
    public abstract void onTransitionEnd(Transition paramTransition);
    
    public abstract void onTransitionPause(Transition paramTransition);
    
    public abstract void onTransitionResume(Transition paramTransition);
    
    public abstract void onTransitionStart(Transition paramTransition);
  }
}
