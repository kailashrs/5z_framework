package android.transition;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public final class Scene
{
  private Context mContext;
  Runnable mEnterAction;
  Runnable mExitAction;
  private View mLayout;
  private int mLayoutId = -1;
  private ViewGroup mSceneRoot;
  
  public Scene(ViewGroup paramViewGroup)
  {
    mSceneRoot = paramViewGroup;
  }
  
  private Scene(ViewGroup paramViewGroup, int paramInt, Context paramContext)
  {
    mContext = paramContext;
    mSceneRoot = paramViewGroup;
    mLayoutId = paramInt;
  }
  
  public Scene(ViewGroup paramViewGroup, View paramView)
  {
    mSceneRoot = paramViewGroup;
    mLayout = paramView;
  }
  
  @Deprecated
  public Scene(ViewGroup paramViewGroup1, ViewGroup paramViewGroup2)
  {
    mSceneRoot = paramViewGroup1;
    mLayout = paramViewGroup2;
  }
  
  static Scene getCurrentScene(View paramView)
  {
    return (Scene)paramView.getTag(16908879);
  }
  
  public static Scene getSceneForLayout(ViewGroup paramViewGroup, int paramInt, Context paramContext)
  {
    Object localObject1 = (SparseArray)paramViewGroup.getTag(16909311);
    Object localObject2 = localObject1;
    if (localObject1 == null)
    {
      localObject2 = new SparseArray();
      paramViewGroup.setTagInternal(16909311, localObject2);
    }
    localObject1 = (Scene)((SparseArray)localObject2).get(paramInt);
    if (localObject1 != null) {
      return localObject1;
    }
    paramViewGroup = new Scene(paramViewGroup, paramInt, paramContext);
    ((SparseArray)localObject2).put(paramInt, paramViewGroup);
    return paramViewGroup;
  }
  
  static void setCurrentScene(View paramView, Scene paramScene)
  {
    paramView.setTagInternal(16908879, paramScene);
  }
  
  public void enter()
  {
    if ((mLayoutId > 0) || (mLayout != null))
    {
      getSceneRoot().removeAllViews();
      if (mLayoutId > 0) {
        LayoutInflater.from(mContext).inflate(mLayoutId, mSceneRoot);
      } else {
        mSceneRoot.addView(mLayout);
      }
    }
    if (mEnterAction != null) {
      mEnterAction.run();
    }
    setCurrentScene(mSceneRoot, this);
  }
  
  public void exit()
  {
    if ((getCurrentScene(mSceneRoot) == this) && (mExitAction != null)) {
      mExitAction.run();
    }
  }
  
  public ViewGroup getSceneRoot()
  {
    return mSceneRoot;
  }
  
  boolean isCreatedFromLayoutResource()
  {
    boolean bool;
    if (mLayoutId > 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void setEnterAction(Runnable paramRunnable)
  {
    mEnterAction = paramRunnable;
  }
  
  public void setExitAction(Runnable paramRunnable)
  {
    mExitAction = paramRunnable;
  }
}
