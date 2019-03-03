package android.app;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.transition.Transition;
import android.transition.Transition.EpicenterCallback;
import android.transition.TransitionListenerAdapter;
import android.transition.TransitionSet;
import android.transition.Visibility;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.view.GhostView;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnAttachStateChangeListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewRootImpl;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.android.internal.view.OneShotPreDrawListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

abstract class ActivityTransitionCoordinator
  extends ResultReceiver
{
  protected static final String KEY_ELEVATION = "shared_element:elevation";
  protected static final String KEY_IMAGE_MATRIX = "shared_element:imageMatrix";
  static final String KEY_REMOTE_RECEIVER = "android:remoteReceiver";
  protected static final String KEY_SCALE_TYPE = "shared_element:scaleType";
  protected static final String KEY_SCREEN_BOTTOM = "shared_element:screenBottom";
  protected static final String KEY_SCREEN_LEFT = "shared_element:screenLeft";
  protected static final String KEY_SCREEN_RIGHT = "shared_element:screenRight";
  protected static final String KEY_SCREEN_TOP = "shared_element:screenTop";
  protected static final String KEY_SNAPSHOT = "shared_element:bitmap";
  protected static final String KEY_TRANSLATION_Z = "shared_element:translationZ";
  public static final int MSG_CANCEL = 106;
  public static final int MSG_EXIT_TRANSITION_COMPLETE = 104;
  public static final int MSG_HIDE_SHARED_ELEMENTS = 101;
  public static final int MSG_SET_REMOTE_RECEIVER = 100;
  public static final int MSG_SHARED_ELEMENT_DESTINATION = 107;
  public static final int MSG_START_EXIT_TRANSITION = 105;
  public static final int MSG_TAKE_SHARED_ELEMENTS = 103;
  protected static final ImageView.ScaleType[] SCALE_TYPE_VALUES = ;
  private static final String TAG = "ActivityTransitionCoordinator";
  protected final ArrayList<String> mAllSharedElementNames;
  private boolean mBackgroundAnimatorComplete;
  private final FixedEpicenterCallback mEpicenterCallback = new FixedEpicenterCallback(null);
  private ArrayList<GhostViewListeners> mGhostViewListeners = new ArrayList();
  protected final boolean mIsReturning;
  private boolean mIsStartingTransition;
  protected SharedElementCallback mListener;
  private ArrayMap<View, Float> mOriginalAlphas = new ArrayMap();
  private Runnable mPendingTransition;
  protected ResultReceiver mResultReceiver;
  protected final ArrayList<String> mSharedElementNames = new ArrayList();
  private ArrayList<Matrix> mSharedElementParentMatrices;
  private boolean mSharedElementTransitionComplete;
  protected final ArrayList<View> mSharedElements = new ArrayList();
  private ArrayList<View> mStrippedTransitioningViews = new ArrayList();
  protected ArrayList<View> mTransitioningViews = new ArrayList();
  private boolean mViewsTransitionComplete;
  private Window mWindow;
  
  public ActivityTransitionCoordinator(Window paramWindow, ArrayList<String> paramArrayList, SharedElementCallback paramSharedElementCallback, boolean paramBoolean)
  {
    super(new Handler());
    mWindow = paramWindow;
    mListener = paramSharedElementCallback;
    mAllSharedElementNames = paramArrayList;
    mIsReturning = paramBoolean;
  }
  
  private static void findIncludedViews(Transition paramTransition, ArrayList<View> paramArrayList, ArraySet<View> paramArraySet)
  {
    boolean bool = paramTransition instanceof TransitionSet;
    int i = 0;
    int j = 0;
    Object localObject;
    if (bool)
    {
      localObject = (TransitionSet)paramTransition;
      ArrayList localArrayList = new ArrayList();
      int k = paramArrayList.size();
      for (i = 0; i < k; i++)
      {
        View localView = (View)paramArrayList.get(i);
        if (paramTransition.isValidTarget(localView)) {
          localArrayList.add(localView);
        }
      }
      k = ((TransitionSet)localObject).getTransitionCount();
      for (i = j; i < k; i++) {
        findIncludedViews(((TransitionSet)localObject).getTransitionAt(i), localArrayList, paramArraySet);
      }
    }
    else
    {
      j = paramArrayList.size();
      while (i < j)
      {
        localObject = (View)paramArrayList.get(i);
        if (paramTransition.isValidTarget((View)localObject)) {
          paramArraySet.add(localObject);
        }
        i++;
      }
    }
  }
  
  private static SharedElementOriginalState getOldSharedElementState(View paramView, String paramString, Bundle paramBundle)
  {
    SharedElementOriginalState localSharedElementOriginalState = new SharedElementOriginalState();
    mLeft = paramView.getLeft();
    mTop = paramView.getTop();
    mRight = paramView.getRight();
    mBottom = paramView.getBottom();
    mMeasuredWidth = paramView.getMeasuredWidth();
    mMeasuredHeight = paramView.getMeasuredHeight();
    mTranslationZ = paramView.getTranslationZ();
    mElevation = paramView.getElevation();
    if (!(paramView instanceof ImageView)) {
      return localSharedElementOriginalState;
    }
    paramString = paramBundle.getBundle(paramString);
    if (paramString == null) {
      return localSharedElementOriginalState;
    }
    if (paramString.getInt("shared_element:scaleType", -1) < 0) {
      return localSharedElementOriginalState;
    }
    paramView = (ImageView)paramView;
    mScaleType = paramView.getScaleType();
    if (mScaleType == ImageView.ScaleType.MATRIX) {
      mMatrix = new Matrix(paramView.getImageMatrix());
    }
    return localSharedElementOriginalState;
  }
  
  private void getSharedElementParentMatrix(View paramView, Matrix paramMatrix)
  {
    int i;
    if (mSharedElementParentMatrices == null) {
      i = -1;
    } else {
      i = mSharedElements.indexOf(paramView);
    }
    if (i < 0)
    {
      paramMatrix.reset();
      paramView = paramView.getParent();
      if ((paramView instanceof ViewGroup))
      {
        paramView = (ViewGroup)paramView;
        paramView.transformMatrixToLocal(paramMatrix);
        paramMatrix.postTranslate(paramView.getScrollX(), paramView.getScrollY());
      }
    }
    else
    {
      paramMatrix.set((Matrix)mSharedElementParentMatrices.get(i));
    }
  }
  
  public static boolean isInTransitionGroup(ViewParent paramViewParent, ViewGroup paramViewGroup)
  {
    if ((paramViewParent != paramViewGroup) && ((paramViewParent instanceof ViewGroup)))
    {
      paramViewParent = (ViewGroup)paramViewParent;
      if (paramViewParent.isTransitionGroup()) {
        return true;
      }
      return isInTransitionGroup(paramViewParent.getParent(), paramViewGroup);
    }
    return false;
  }
  
  private static boolean isNested(View paramView, ArrayMap<String, View> paramArrayMap)
  {
    paramView = paramView.getParent();
    boolean bool1 = false;
    boolean bool2;
    for (;;)
    {
      bool2 = bool1;
      if (!(paramView instanceof View)) {
        break;
      }
      paramView = (View)paramView;
      if (paramArrayMap.containsValue(paramView))
      {
        bool2 = true;
        break;
      }
      paramView = paramView.getParent();
    }
    return bool2;
  }
  
  protected static Transition mergeTransitions(Transition paramTransition1, Transition paramTransition2)
  {
    if (paramTransition1 == null) {
      return paramTransition2;
    }
    if (paramTransition2 == null) {
      return paramTransition1;
    }
    TransitionSet localTransitionSet = new TransitionSet();
    localTransitionSet.addTransition(paramTransition1);
    localTransitionSet.addTransition(paramTransition2);
    return localTransitionSet;
  }
  
  private static void noLayoutSuppressionForVisibilityTransitions(Transition paramTransition)
  {
    boolean bool = paramTransition instanceof Visibility;
    int i = 0;
    if (bool)
    {
      ((Visibility)paramTransition).setSuppressLayout(false);
    }
    else if ((paramTransition instanceof TransitionSet))
    {
      paramTransition = (TransitionSet)paramTransition;
      int j = paramTransition.getTransitionCount();
      while (i < j)
      {
        noLayoutSuppressionForVisibilityTransitions(paramTransition.getTransitionAt(i));
        i++;
      }
    }
  }
  
  protected static void removeExcludedViews(Transition paramTransition, ArrayList<View> paramArrayList)
  {
    ArraySet localArraySet = new ArraySet();
    findIncludedViews(paramTransition, paramArrayList, localArraySet);
    paramArrayList.clear();
    paramArrayList.addAll(localArraySet);
  }
  
  private static int scaleTypeToInt(ImageView.ScaleType paramScaleType)
  {
    for (int i = 0; i < SCALE_TYPE_VALUES.length; i++) {
      if (paramScaleType == SCALE_TYPE_VALUES[i]) {
        return i;
      }
    }
    return -1;
  }
  
  private void setEpicenter(View paramView)
  {
    if (paramView == null)
    {
      mEpicenterCallback.setEpicenter(null);
    }
    else
    {
      Rect localRect = new Rect();
      paramView.getBoundsOnScreen(localRect);
      mEpicenterCallback.setEpicenter(localRect);
    }
  }
  
  protected static void setOriginalSharedElementState(ArrayList<View> paramArrayList, ArrayList<SharedElementOriginalState> paramArrayList1)
  {
    for (int i = 0; i < paramArrayList1.size(); i++)
    {
      View localView = (View)paramArrayList.get(i);
      SharedElementOriginalState localSharedElementOriginalState = (SharedElementOriginalState)paramArrayList1.get(i);
      if (((localView instanceof ImageView)) && (mScaleType != null))
      {
        ImageView localImageView = (ImageView)localView;
        localImageView.setScaleType(mScaleType);
        if (mScaleType == ImageView.ScaleType.MATRIX) {
          localImageView.setImageMatrix(mMatrix);
        }
      }
      localView.setElevation(mElevation);
      localView.setTranslationZ(mTranslationZ);
      localView.measure(View.MeasureSpec.makeMeasureSpec(mMeasuredWidth, 1073741824), View.MeasureSpec.makeMeasureSpec(mMeasuredHeight, 1073741824));
      localView.layout(mLeft, mTop, mRight, mBottom);
    }
  }
  
  private void setSharedElementMatrices()
  {
    int i = mSharedElements.size();
    if (i > 0) {
      mSharedElementParentMatrices = new ArrayList(i);
    }
    for (int j = 0; j < i; j++)
    {
      ViewGroup localViewGroup = (ViewGroup)((View)mSharedElements.get(j)).getParent();
      Matrix localMatrix = new Matrix();
      if (localViewGroup != null)
      {
        localViewGroup.transformMatrixToLocal(localMatrix);
        localMatrix.postTranslate(localViewGroup.getScrollX(), localViewGroup.getScrollY());
      }
      mSharedElementParentMatrices.add(localMatrix);
    }
  }
  
  private void setSharedElementState(View paramView, String paramString, Bundle paramBundle, Matrix paramMatrix, RectF paramRectF, int[] paramArrayOfInt)
  {
    paramBundle = paramBundle.getBundle(paramString);
    if (paramBundle == null) {
      return;
    }
    if ((paramView instanceof ImageView))
    {
      i = paramBundle.getInt("shared_element:scaleType", -1);
      if (i >= 0)
      {
        ImageView localImageView = (ImageView)paramView;
        paramString = SCALE_TYPE_VALUES[i];
        localImageView.setScaleType(paramString);
        if (paramString == ImageView.ScaleType.MATRIX)
        {
          paramMatrix.setValues(paramBundle.getFloatArray("shared_element:imageMatrix"));
          localImageView.setImageMatrix(paramMatrix);
        }
      }
    }
    paramView.setTranslationZ(paramBundle.getFloat("shared_element:translationZ"));
    paramView.setElevation(paramBundle.getFloat("shared_element:elevation"));
    float f1 = paramBundle.getFloat("shared_element:screenLeft");
    float f2 = paramBundle.getFloat("shared_element:screenTop");
    float f3 = paramBundle.getFloat("shared_element:screenRight");
    float f4 = paramBundle.getFloat("shared_element:screenBottom");
    if (paramArrayOfInt != null)
    {
      f1 -= paramArrayOfInt[0];
      f2 -= paramArrayOfInt[1];
      f3 -= paramArrayOfInt[0];
      f4 -= paramArrayOfInt[1];
    }
    else
    {
      getSharedElementParentMatrix(paramView, paramMatrix);
      paramRectF.set(f1, f2, f3, f4);
      paramMatrix.mapRect(paramRectF);
      f1 = left;
      f2 = top;
      paramView.getInverseMatrix().mapRect(paramRectF);
      f3 = paramRectF.width();
      f4 = paramRectF.height();
      paramView.setLeft(0);
      paramView.setTop(0);
      paramView.setRight(Math.round(f3));
      paramView.setBottom(Math.round(f4));
      paramRectF.set(0.0F, 0.0F, f3, f4);
      paramView.getMatrix().mapRect(paramRectF);
      f1 -= left;
      f2 -= top;
      f3 = f1 + f3;
      f4 = f2 + f4;
    }
    int j = Math.round(f1);
    int i = Math.round(f2);
    int k = Math.round(f3) - j;
    int m = Math.round(f4) - i;
    paramView.measure(View.MeasureSpec.makeMeasureSpec(k, 1073741824), View.MeasureSpec.makeMeasureSpec(m, 1073741824));
    paramView.layout(j, i, j + k, i + m);
  }
  
  private void setSharedElements(ArrayMap<String, View> paramArrayMap)
  {
    for (int i = 1; !paramArrayMap.isEmpty(); i = 0) {
      for (int j = paramArrayMap.size() - 1; j >= 0; j--)
      {
        View localView = (View)paramArrayMap.valueAt(j);
        String str = (String)paramArrayMap.keyAt(j);
        if ((i != 0) && ((localView == null) || (!localView.isAttachedToWindow()) || (str == null)))
        {
          paramArrayMap.removeAt(j);
        }
        else if (!isNested(localView, paramArrayMap))
        {
          mSharedElementNames.add(str);
          mSharedElements.add(localView);
          paramArrayMap.removeAt(j);
        }
      }
    }
  }
  
  private void showView(View paramView, boolean paramBoolean)
  {
    Float localFloat = (Float)mOriginalAlphas.remove(paramView);
    if (localFloat != null) {
      paramView.setAlpha(localFloat.floatValue());
    }
    if (paramBoolean) {
      paramView.setTransitionAlpha(1.0F);
    }
  }
  
  private void startInputWhenTransitionsComplete()
  {
    if ((mViewsTransitionComplete) && (mSharedElementTransitionComplete))
    {
      Object localObject = getDecor();
      if (localObject != null)
      {
        localObject = ((View)localObject).getViewRootImpl();
        if (localObject != null) {
          ((ViewRootImpl)localObject).setPausedForTransition(false);
        }
      }
      onTransitionsComplete();
    }
  }
  
  protected void backgroundAnimatorComplete()
  {
    mBackgroundAnimatorComplete = true;
  }
  
  protected boolean cancelPendingTransitions()
  {
    mPendingTransition = null;
    return mIsStartingTransition;
  }
  
  protected Bundle captureSharedElementState()
  {
    Bundle localBundle = new Bundle();
    RectF localRectF = new RectF();
    Matrix localMatrix = new Matrix();
    for (int i = 0; i < mSharedElements.size(); i++) {
      captureSharedElementState((View)mSharedElements.get(i), (String)mSharedElementNames.get(i), localBundle, localMatrix, localRectF);
    }
    return localBundle;
  }
  
  protected void captureSharedElementState(View paramView, String paramString, Bundle paramBundle, Matrix paramMatrix, RectF paramRectF)
  {
    Bundle localBundle = new Bundle();
    paramMatrix.reset();
    paramView.transformMatrixToGlobal(paramMatrix);
    paramRectF.set(0.0F, 0.0F, paramView.getWidth(), paramView.getHeight());
    paramMatrix.mapRect(paramRectF);
    localBundle.putFloat("shared_element:screenLeft", left);
    localBundle.putFloat("shared_element:screenRight", right);
    localBundle.putFloat("shared_element:screenTop", top);
    localBundle.putFloat("shared_element:screenBottom", bottom);
    localBundle.putFloat("shared_element:translationZ", paramView.getTranslationZ());
    localBundle.putFloat("shared_element:elevation", paramView.getElevation());
    Parcelable localParcelable = null;
    if (mListener != null) {
      localParcelable = mListener.onCaptureSharedElementSnapshot(paramView, paramMatrix, paramRectF);
    }
    if (localParcelable != null) {
      localBundle.putParcelable("shared_element:bitmap", localParcelable);
    }
    if ((paramView instanceof ImageView))
    {
      paramView = (ImageView)paramView;
      localBundle.putInt("shared_element:scaleType", scaleTypeToInt(paramView.getScaleType()));
      if (paramView.getScaleType() == ImageView.ScaleType.MATRIX)
      {
        paramMatrix = new float[9];
        paramView.getImageMatrix().getValues(paramMatrix);
        localBundle.putFloatArray("shared_element:imageMatrix", paramMatrix);
      }
    }
    paramBundle.putBundle(paramString, localBundle);
  }
  
  protected void clearState()
  {
    mWindow = null;
    mSharedElements.clear();
    mTransitioningViews = null;
    mStrippedTransitioningViews = null;
    mOriginalAlphas.clear();
    mResultReceiver = null;
    mPendingTransition = null;
    mListener = null;
    mSharedElementParentMatrices = null;
  }
  
  protected Transition configureTransition(Transition paramTransition, boolean paramBoolean)
  {
    Transition localTransition = paramTransition;
    if (paramTransition != null)
    {
      paramTransition = paramTransition.clone();
      paramTransition.setEpicenterCallback(mEpicenterCallback);
      localTransition = setTargets(paramTransition, paramBoolean);
    }
    noLayoutSuppressionForVisibilityTransitions(localTransition);
    return localTransition;
  }
  
  public ArrayList<View> copyMappedViews()
  {
    return new ArrayList(mSharedElements);
  }
  
  protected ArrayList<View> createSnapshots(Bundle paramBundle, Collection<String> paramCollection)
  {
    int i = paramCollection.size();
    ArrayList localArrayList = new ArrayList(i);
    if (i == 0) {
      return localArrayList;
    }
    Context localContext = getWindow().getContext();
    int[] arrayOfInt = new int[2];
    ViewGroup localViewGroup = getDecor();
    if (localViewGroup != null) {
      localViewGroup.getLocationOnScreen(arrayOfInt);
    }
    Matrix localMatrix = new Matrix();
    Iterator localIterator = paramCollection.iterator();
    while (localIterator.hasNext())
    {
      String str = (String)localIterator.next();
      Object localObject = paramBundle.getBundle(str);
      paramCollection = null;
      localViewGroup = null;
      if (localObject != null)
      {
        localObject = ((Bundle)localObject).getParcelable("shared_element:bitmap");
        paramCollection = localViewGroup;
        if (localObject != null)
        {
          paramCollection = localViewGroup;
          if (mListener != null) {
            paramCollection = mListener.onCreateSnapshotView(localContext, (Parcelable)localObject);
          }
        }
        if (paramCollection != null) {
          setSharedElementState(paramCollection, str, paramBundle, localMatrix, null, arrayOfInt);
        }
      }
      localArrayList.add(paramCollection);
    }
    return localArrayList;
  }
  
  public ArrayList<String> getAcceptedNames()
  {
    return mSharedElementNames;
  }
  
  public ArrayList<String> getAllSharedElementNames()
  {
    return mAllSharedElementNames;
  }
  
  public ViewGroup getDecor()
  {
    ViewGroup localViewGroup;
    if (mWindow == null) {
      localViewGroup = null;
    } else {
      localViewGroup = (ViewGroup)mWindow.getDecorView();
    }
    return localViewGroup;
  }
  
  protected long getFadeDuration()
  {
    return getWindow().getTransitionBackgroundFadeDuration();
  }
  
  public ArrayList<String> getMappedNames()
  {
    ArrayList localArrayList = new ArrayList(mSharedElements.size());
    for (int i = 0; i < mSharedElements.size(); i++) {
      localArrayList.add(((View)mSharedElements.get(i)).getTransitionName());
    }
    return localArrayList;
  }
  
  protected abstract Transition getViewsTransition();
  
  protected Window getWindow()
  {
    return mWindow;
  }
  
  protected void hideViews(ArrayList<View> paramArrayList)
  {
    int i = paramArrayList.size();
    for (int j = 0; j < i; j++)
    {
      View localView = (View)paramArrayList.get(j);
      if (!mOriginalAlphas.containsKey(localView)) {
        mOriginalAlphas.put(localView, Float.valueOf(localView.getAlpha()));
      }
      localView.setAlpha(0.0F);
    }
  }
  
  public boolean isTransitionRunning()
  {
    boolean bool;
    if ((mViewsTransitionComplete) && (mSharedElementTransitionComplete) && (mBackgroundAnimatorComplete)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  protected boolean isViewsTransitionComplete()
  {
    return mViewsTransitionComplete;
  }
  
  protected ArrayMap<String, View> mapSharedElements(ArrayList<String> paramArrayList, ArrayList<View> paramArrayList1)
  {
    ArrayMap localArrayMap = new ArrayMap();
    if (paramArrayList != null) {
      for (int i = 0; i < paramArrayList.size(); i++) {
        localArrayMap.put((String)paramArrayList.get(i), (View)paramArrayList1.get(i));
      }
    }
    paramArrayList = getDecor();
    if (paramArrayList != null) {
      paramArrayList.findNamedViews(localArrayMap);
    }
    return localArrayMap;
  }
  
  protected boolean moveSharedElementWithParent()
  {
    return true;
  }
  
  protected void moveSharedElementsFromOverlay()
  {
    int i = mGhostViewListeners.size();
    int j = 0;
    for (int k = 0; k < i; k++) {
      ((GhostViewListeners)mGhostViewListeners.get(k)).removeListener();
    }
    mGhostViewListeners.clear();
    if ((mWindow != null) && (mWindow.getSharedElementsUseOverlay()))
    {
      ViewGroup localViewGroup = getDecor();
      if (localViewGroup != null)
      {
        localViewGroup.getOverlay();
        i = mSharedElements.size();
        for (k = j; k < i; k++) {
          GhostView.removeGhost((View)mSharedElements.get(k));
        }
      }
      return;
    }
  }
  
  protected void moveSharedElementsToOverlay()
  {
    if ((mWindow != null) && (mWindow.getSharedElementsUseOverlay()))
    {
      setSharedElementMatrices();
      int i = mSharedElements.size();
      ViewGroup localViewGroup1 = getDecor();
      if (localViewGroup1 != null)
      {
        boolean bool = moveSharedElementWithParent();
        Matrix localMatrix = new Matrix();
        for (int j = 0; j < i; j++)
        {
          Object localObject = (View)mSharedElements.get(j);
          if (((View)localObject).isAttachedToWindow())
          {
            localMatrix.reset();
            ((Matrix)mSharedElementParentMatrices.get(j)).invert(localMatrix);
            GhostView.addGhost((View)localObject, localViewGroup1, localMatrix);
            ViewGroup localViewGroup2 = (ViewGroup)((View)localObject).getParent();
            if ((bool) && (!isInTransitionGroup(localViewGroup2, localViewGroup1)))
            {
              localObject = new GhostViewListeners((View)localObject, localViewGroup2, localViewGroup1);
              localViewGroup2.getViewTreeObserver().addOnPreDrawListener((ViewTreeObserver.OnPreDrawListener)localObject);
              localViewGroup2.addOnAttachStateChangeListener((View.OnAttachStateChangeListener)localObject);
              mGhostViewListeners.add(localObject);
            }
          }
        }
      }
      return;
    }
  }
  
  protected void notifySharedElementEnd(ArrayList<View> paramArrayList)
  {
    if (mListener != null) {
      mListener.onSharedElementEnd(mSharedElementNames, mSharedElements, paramArrayList);
    }
  }
  
  protected void onTransitionsComplete() {}
  
  protected void pauseInput()
  {
    Object localObject = getDecor();
    if (localObject == null) {
      localObject = null;
    } else {
      localObject = ((View)localObject).getViewRootImpl();
    }
    if (localObject != null) {
      ((ViewRootImpl)localObject).setPausedForTransition(true);
    }
  }
  
  protected void scheduleGhostVisibilityChange(int paramInt)
  {
    ViewGroup localViewGroup = getDecor();
    if (localViewGroup != null) {
      OneShotPreDrawListener.add(localViewGroup, new _..Lambda.ActivityTransitionCoordinator._HMo0E_15AzCK9fwQ8WHzdz8ZIw(this, paramInt));
    }
  }
  
  protected void scheduleSetSharedElementEnd(ArrayList<View> paramArrayList)
  {
    ViewGroup localViewGroup = getDecor();
    if (localViewGroup != null) {
      OneShotPreDrawListener.add(localViewGroup, new _..Lambda.ActivityTransitionCoordinator.fkaPvc8GCghP2GMwEgS_J5m_T_4(this, paramArrayList));
    }
  }
  
  protected void setEpicenter()
  {
    Object localObject1 = null;
    Object localObject2 = localObject1;
    if (!mAllSharedElementNames.isEmpty())
    {
      localObject2 = localObject1;
      if (!mSharedElementNames.isEmpty())
      {
        int i = mSharedElementNames.indexOf(mAllSharedElementNames.get(0));
        localObject2 = localObject1;
        if (i >= 0) {
          localObject2 = (View)mSharedElements.get(i);
        }
      }
    }
    setEpicenter((View)localObject2);
  }
  
  protected void setGhostVisibility(int paramInt)
  {
    int i = mSharedElements.size();
    for (int j = 0; j < i; j++)
    {
      GhostView localGhostView = GhostView.getGhost((View)mSharedElements.get(j));
      if (localGhostView != null) {
        localGhostView.setVisibility(paramInt);
      }
    }
  }
  
  protected void setResultReceiver(ResultReceiver paramResultReceiver)
  {
    mResultReceiver = paramResultReceiver;
  }
  
  protected ArrayList<SharedElementOriginalState> setSharedElementState(Bundle paramBundle, ArrayList<View> paramArrayList)
  {
    ArrayList localArrayList = new ArrayList();
    if (paramBundle != null)
    {
      Matrix localMatrix = new Matrix();
      RectF localRectF = new RectF();
      int i = mSharedElements.size();
      for (int j = 0; j < i; j++)
      {
        View localView = (View)mSharedElements.get(j);
        String str = (String)mSharedElementNames.get(j);
        localArrayList.add(getOldSharedElementState(localView, str, paramBundle));
        setSharedElementState(localView, str, paramBundle, localMatrix, localRectF, null);
      }
    }
    if (mListener != null) {
      mListener.onSharedElementStart(mSharedElementNames, mSharedElements, paramArrayList);
    }
    return localArrayList;
  }
  
  protected Transition setTargets(Transition paramTransition, boolean paramBoolean)
  {
    if ((paramTransition != null) && ((!paramBoolean) || ((mTransitioningViews != null) && (!mTransitioningViews.isEmpty()))))
    {
      TransitionSet localTransitionSet = new TransitionSet();
      int i;
      if (mTransitioningViews != null) {
        for (i = mTransitioningViews.size() - 1; i >= 0; i--)
        {
          View localView = (View)mTransitioningViews.get(i);
          if (paramBoolean) {
            localTransitionSet.addTarget(localView);
          } else {
            localTransitionSet.excludeTarget(localView, true);
          }
        }
      }
      if (mStrippedTransitioningViews != null) {
        for (i = mStrippedTransitioningViews.size() - 1; i >= 0; i--) {
          localTransitionSet.excludeTarget((View)mStrippedTransitioningViews.get(i), true);
        }
      }
      localTransitionSet.addTransition(paramTransition);
      paramTransition = localTransitionSet;
      if (!paramBoolean)
      {
        paramTransition = localTransitionSet;
        if (mTransitioningViews != null)
        {
          paramTransition = localTransitionSet;
          if (!mTransitioningViews.isEmpty()) {
            paramTransition = new TransitionSet().addTransition(localTransitionSet);
          }
        }
      }
      return paramTransition;
    }
    return null;
  }
  
  protected void setTransitioningViewsVisiblity(int paramInt, boolean paramBoolean)
  {
    Object localObject = mTransitioningViews;
    int i = 0;
    int j;
    if (localObject == null) {
      j = 0;
    } else {
      j = mTransitioningViews.size();
    }
    while (i < j)
    {
      localObject = (View)mTransitioningViews.get(i);
      if (paramBoolean) {
        ((View)localObject).setVisibility(paramInt);
      } else {
        ((View)localObject).setTransitionVisibility(paramInt);
      }
      i++;
    }
  }
  
  protected void sharedElementTransitionComplete()
  {
    mSharedElementTransitionComplete = true;
    startInputWhenTransitionsComplete();
  }
  
  protected void showViews(ArrayList<View> paramArrayList, boolean paramBoolean)
  {
    int i = paramArrayList.size();
    for (int j = 0; j < i; j++) {
      showView((View)paramArrayList.get(j), paramBoolean);
    }
  }
  
  protected void startTransition(Runnable paramRunnable)
  {
    if (mIsStartingTransition)
    {
      mPendingTransition = paramRunnable;
    }
    else
    {
      mIsStartingTransition = true;
      paramRunnable.run();
    }
  }
  
  protected void stripOffscreenViews()
  {
    if (mTransitioningViews == null) {
      return;
    }
    Rect localRect = new Rect();
    for (int i = mTransitioningViews.size() - 1; i >= 0; i--)
    {
      View localView = (View)mTransitioningViews.get(i);
      if (!localView.getGlobalVisibleRect(localRect))
      {
        mTransitioningViews.remove(i);
        mStrippedTransitioningViews.add(localView);
      }
    }
  }
  
  protected void transitionStarted()
  {
    mIsStartingTransition = false;
  }
  
  protected void viewsReady(ArrayMap<String, View> paramArrayMap)
  {
    paramArrayMap.retainAll(mAllSharedElementNames);
    if (mListener != null) {
      mListener.onMapSharedElements(mAllSharedElementNames, paramArrayMap);
    }
    setSharedElements(paramArrayMap);
    if ((getViewsTransition() != null) && (mTransitioningViews != null))
    {
      paramArrayMap = getDecor();
      if (paramArrayMap != null) {
        paramArrayMap.captureTransitioningViews(mTransitioningViews);
      }
      mTransitioningViews.removeAll(mSharedElements);
    }
    setEpicenter();
  }
  
  protected void viewsTransitionComplete()
  {
    mViewsTransitionComplete = true;
    startInputWhenTransitionsComplete();
  }
  
  protected class ContinueTransitionListener
    extends TransitionListenerAdapter
  {
    protected ContinueTransitionListener() {}
    
    public void onTransitionEnd(Transition paramTransition)
    {
      paramTransition.removeListener(this);
    }
    
    public void onTransitionStart(Transition paramTransition)
    {
      ActivityTransitionCoordinator.access$102(ActivityTransitionCoordinator.this, false);
      paramTransition = mPendingTransition;
      ActivityTransitionCoordinator.access$202(ActivityTransitionCoordinator.this, null);
      if (paramTransition != null) {
        startTransition(paramTransition);
      }
    }
  }
  
  private static class FixedEpicenterCallback
    extends Transition.EpicenterCallback
  {
    private Rect mEpicenter;
    
    private FixedEpicenterCallback() {}
    
    public Rect onGetEpicenter(Transition paramTransition)
    {
      return mEpicenter;
    }
    
    public void setEpicenter(Rect paramRect)
    {
      mEpicenter = paramRect;
    }
  }
  
  private static class GhostViewListeners
    implements ViewTreeObserver.OnPreDrawListener, View.OnAttachStateChangeListener
  {
    private ViewGroup mDecor;
    private Matrix mMatrix = new Matrix();
    private View mParent;
    private View mView;
    private ViewTreeObserver mViewTreeObserver;
    
    public GhostViewListeners(View paramView1, View paramView2, ViewGroup paramViewGroup)
    {
      mView = paramView1;
      mParent = paramView2;
      mDecor = paramViewGroup;
      mViewTreeObserver = paramView2.getViewTreeObserver();
    }
    
    public View getView()
    {
      return mView;
    }
    
    public boolean onPreDraw()
    {
      GhostView localGhostView = GhostView.getGhost(mView);
      if ((localGhostView != null) && (mView.isAttachedToWindow()))
      {
        GhostView.calculateMatrix(mView, mDecor, mMatrix);
        localGhostView.setMatrix(mMatrix);
      }
      else
      {
        removeListener();
      }
      return true;
    }
    
    public void onViewAttachedToWindow(View paramView)
    {
      mViewTreeObserver = paramView.getViewTreeObserver();
    }
    
    public void onViewDetachedFromWindow(View paramView)
    {
      removeListener();
    }
    
    public void removeListener()
    {
      if (mViewTreeObserver.isAlive()) {
        mViewTreeObserver.removeOnPreDrawListener(this);
      } else {
        mParent.getViewTreeObserver().removeOnPreDrawListener(this);
      }
      mParent.removeOnAttachStateChangeListener(this);
    }
  }
  
  static class SharedElementOriginalState
  {
    int mBottom;
    float mElevation;
    int mLeft;
    Matrix mMatrix;
    int mMeasuredHeight;
    int mMeasuredWidth;
    int mRight;
    ImageView.ScaleType mScaleType;
    int mTop;
    float mTranslationZ;
    
    SharedElementOriginalState() {}
  }
}
