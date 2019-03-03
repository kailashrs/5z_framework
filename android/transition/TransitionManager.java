package android.transition;

import android.util.ArrayMap;
import android.view.View;
import android.view.View.OnAttachStateChangeListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

public class TransitionManager
{
  private static final String[] EMPTY_STRINGS;
  private static String LOG_TAG = "TransitionManager";
  private static Transition sDefaultTransition = new AutoTransition();
  private static ArrayList<ViewGroup> sPendingTransitions = new ArrayList();
  private static ThreadLocal<WeakReference<ArrayMap<ViewGroup, ArrayList<Transition>>>> sRunningTransitions;
  ArrayMap<Scene, ArrayMap<Scene, Transition>> mScenePairTransitions = new ArrayMap();
  ArrayMap<Scene, Transition> mSceneTransitions = new ArrayMap();
  
  static
  {
    EMPTY_STRINGS = new String[0];
    sRunningTransitions = new ThreadLocal();
  }
  
  public TransitionManager() {}
  
  public static void beginDelayedTransition(ViewGroup paramViewGroup)
  {
    beginDelayedTransition(paramViewGroup, null);
  }
  
  public static void beginDelayedTransition(ViewGroup paramViewGroup, Transition paramTransition)
  {
    if ((!sPendingTransitions.contains(paramViewGroup)) && (paramViewGroup.isLaidOut()))
    {
      sPendingTransitions.add(paramViewGroup);
      Transition localTransition = paramTransition;
      if (paramTransition == null) {
        localTransition = sDefaultTransition;
      }
      paramTransition = localTransition.clone();
      sceneChangeSetup(paramViewGroup, paramTransition);
      Scene.setCurrentScene(paramViewGroup, null);
      sceneChangeRunTransition(paramViewGroup, paramTransition);
    }
  }
  
  private static void changeScene(Scene paramScene, Transition paramTransition)
  {
    ViewGroup localViewGroup = paramScene.getSceneRoot();
    if (!sPendingTransitions.contains(localViewGroup)) {
      if (paramTransition == null)
      {
        paramScene.enter();
      }
      else
      {
        sPendingTransitions.add(localViewGroup);
        Transition localTransition = paramTransition.clone();
        localTransition.setSceneRoot(localViewGroup);
        paramTransition = Scene.getCurrentScene(localViewGroup);
        if ((paramTransition != null) && (paramTransition.isCreatedFromLayoutResource())) {
          localTransition.setCanRemoveViews(true);
        }
        sceneChangeSetup(localViewGroup, localTransition);
        paramScene.enter();
        sceneChangeRunTransition(localViewGroup, localTransition);
      }
    }
  }
  
  public static void endTransitions(ViewGroup paramViewGroup)
  {
    sPendingTransitions.remove(paramViewGroup);
    ArrayList localArrayList = (ArrayList)getRunningTransitions().get(paramViewGroup);
    if ((localArrayList != null) && (!localArrayList.isEmpty()))
    {
      localArrayList = new ArrayList(localArrayList);
      for (int i = localArrayList.size() - 1; i >= 0; i--) {
        ((Transition)localArrayList.get(i)).forceToEnd(paramViewGroup);
      }
    }
  }
  
  public static Transition getDefaultTransition()
  {
    return sDefaultTransition;
  }
  
  private static ArrayMap<ViewGroup, ArrayList<Transition>> getRunningTransitions()
  {
    WeakReference localWeakReference1 = (WeakReference)sRunningTransitions.get();
    WeakReference localWeakReference2;
    if (localWeakReference1 != null)
    {
      localWeakReference2 = localWeakReference1;
      if (localWeakReference1.get() != null) {}
    }
    else
    {
      localWeakReference2 = new WeakReference(new ArrayMap());
      sRunningTransitions.set(localWeakReference2);
    }
    return (ArrayMap)localWeakReference2.get();
  }
  
  public static void go(Scene paramScene)
  {
    changeScene(paramScene, sDefaultTransition);
  }
  
  public static void go(Scene paramScene, Transition paramTransition)
  {
    changeScene(paramScene, paramTransition);
  }
  
  private static void sceneChangeRunTransition(ViewGroup paramViewGroup, Transition paramTransition)
  {
    if ((paramTransition != null) && (paramViewGroup != null))
    {
      paramTransition = new MultiListener(paramTransition, paramViewGroup);
      paramViewGroup.addOnAttachStateChangeListener(paramTransition);
      paramViewGroup.getViewTreeObserver().addOnPreDrawListener(paramTransition);
    }
  }
  
  private static void sceneChangeSetup(ViewGroup paramViewGroup, Transition paramTransition)
  {
    Object localObject = (ArrayList)getRunningTransitions().get(paramViewGroup);
    if ((localObject != null) && (((ArrayList)localObject).size() > 0))
    {
      localObject = ((ArrayList)localObject).iterator();
      while (((Iterator)localObject).hasNext()) {
        ((Transition)((Iterator)localObject).next()).pause(paramViewGroup);
      }
    }
    if (paramTransition != null) {
      paramTransition.captureValues(paramViewGroup, true);
    }
    paramViewGroup = Scene.getCurrentScene(paramViewGroup);
    if (paramViewGroup != null) {
      paramViewGroup.exit();
    }
  }
  
  public Transition getTransition(Scene paramScene)
  {
    Object localObject = paramScene.getSceneRoot();
    if (localObject != null)
    {
      Scene localScene = Scene.getCurrentScene((View)localObject);
      if (localScene != null)
      {
        localObject = (ArrayMap)mScenePairTransitions.get(paramScene);
        if (localObject != null)
        {
          localObject = (Transition)((ArrayMap)localObject).get(localScene);
          if (localObject != null) {
            return localObject;
          }
        }
      }
    }
    paramScene = (Transition)mSceneTransitions.get(paramScene);
    if (paramScene == null) {
      paramScene = sDefaultTransition;
    }
    return paramScene;
  }
  
  public void setDefaultTransition(Transition paramTransition)
  {
    sDefaultTransition = paramTransition;
  }
  
  public void setTransition(Scene paramScene1, Scene paramScene2, Transition paramTransition)
  {
    ArrayMap localArrayMap1 = (ArrayMap)mScenePairTransitions.get(paramScene2);
    ArrayMap localArrayMap2 = localArrayMap1;
    if (localArrayMap1 == null)
    {
      localArrayMap2 = new ArrayMap();
      mScenePairTransitions.put(paramScene2, localArrayMap2);
    }
    localArrayMap2.put(paramScene1, paramTransition);
  }
  
  public void setTransition(Scene paramScene, Transition paramTransition)
  {
    mSceneTransitions.put(paramScene, paramTransition);
  }
  
  public void transitionTo(Scene paramScene)
  {
    changeScene(paramScene, getTransition(paramScene));
  }
  
  private static class MultiListener
    implements ViewTreeObserver.OnPreDrawListener, View.OnAttachStateChangeListener
  {
    ViewGroup mSceneRoot;
    Transition mTransition;
    final ViewTreeObserver mViewTreeObserver;
    
    MultiListener(Transition paramTransition, ViewGroup paramViewGroup)
    {
      mTransition = paramTransition;
      mSceneRoot = paramViewGroup;
      mViewTreeObserver = mSceneRoot.getViewTreeObserver();
    }
    
    private void removeListeners()
    {
      if (mViewTreeObserver.isAlive()) {
        mViewTreeObserver.removeOnPreDrawListener(this);
      } else {
        mSceneRoot.getViewTreeObserver().removeOnPreDrawListener(this);
      }
      mSceneRoot.removeOnAttachStateChangeListener(this);
    }
    
    public boolean onPreDraw()
    {
      removeListeners();
      if (!TransitionManager.sPendingTransitions.remove(mSceneRoot)) {
        return true;
      }
      final ArrayMap localArrayMap = TransitionManager.access$100();
      ArrayList localArrayList1 = (ArrayList)localArrayMap.get(mSceneRoot);
      ArrayList localArrayList2 = null;
      Object localObject;
      if (localArrayList1 == null)
      {
        localObject = new ArrayList();
        localArrayMap.put(mSceneRoot, localObject);
      }
      else
      {
        localObject = localArrayList1;
        if (localArrayList1.size() > 0)
        {
          localArrayList2 = new ArrayList(localArrayList1);
          localObject = localArrayList1;
        }
      }
      ((ArrayList)localObject).add(mTransition);
      mTransition.addListener(new TransitionListenerAdapter()
      {
        public void onTransitionEnd(Transition paramAnonymousTransition)
        {
          ((ArrayList)localArrayMap.get(mSceneRoot)).remove(paramAnonymousTransition);
        }
      });
      mTransition.captureValues(mSceneRoot, false);
      if (localArrayList2 != null)
      {
        localObject = localArrayList2.iterator();
        while (((Iterator)localObject).hasNext()) {
          ((Transition)((Iterator)localObject).next()).resume(mSceneRoot);
        }
      }
      mTransition.playTransition(mSceneRoot);
      return true;
    }
    
    public void onViewAttachedToWindow(View paramView) {}
    
    public void onViewDetachedFromWindow(View paramView)
    {
      removeListeners();
      TransitionManager.sPendingTransitions.remove(mSceneRoot);
      paramView = (ArrayList)TransitionManager.access$100().get(mSceneRoot);
      if ((paramView != null) && (paramView.size() > 0))
      {
        paramView = paramView.iterator();
        while (paramView.hasNext()) {
          ((Transition)paramView.next()).resume(mSceneRoot);
        }
      }
      mTransition.clearValues(true);
    }
  }
}
