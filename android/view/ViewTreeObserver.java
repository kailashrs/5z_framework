package android.view;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Rect;
import android.graphics.Region;
import android.util.Log;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public final class ViewTreeObserver
{
  private static boolean sIllegalOnDrawModificationIsFatal;
  private boolean mAlive;
  private boolean mInDispatchOnDraw;
  private CopyOnWriteArray<OnComputeInternalInsetsListener> mOnComputeInternalInsetsListeners;
  private ArrayList<OnDrawListener> mOnDrawListeners;
  private CopyOnWriteArrayList<OnEnterAnimationCompleteListener> mOnEnterAnimationCompleteListeners;
  private CopyOnWriteArrayList<OnGlobalFocusChangeListener> mOnGlobalFocusListeners;
  private CopyOnWriteArray<OnGlobalLayoutListener> mOnGlobalLayoutListeners;
  private CopyOnWriteArray<OnPreDrawListener> mOnPreDrawListeners;
  private CopyOnWriteArray<OnScrollChangedListener> mOnScrollChangedListeners;
  private CopyOnWriteArrayList<OnTouchModeChangeListener> mOnTouchModeChangeListeners;
  private CopyOnWriteArrayList<OnWindowAttachListener> mOnWindowAttachListeners;
  private CopyOnWriteArrayList<OnWindowFocusChangeListener> mOnWindowFocusListeners;
  private CopyOnWriteArray<OnWindowShownListener> mOnWindowShownListeners;
  private boolean mWindowShown;
  
  ViewTreeObserver(Context paramContext)
  {
    boolean bool = true;
    mAlive = true;
    if (getApplicationInfotargetSdkVersion < 26) {
      bool = false;
    }
    sIllegalOnDrawModificationIsFatal = bool;
  }
  
  private void checkIsAlive()
  {
    if (mAlive) {
      return;
    }
    throw new IllegalStateException("This ViewTreeObserver is not alive, call getViewTreeObserver() again");
  }
  
  private void kill()
  {
    mAlive = false;
  }
  
  public void addOnComputeInternalInsetsListener(OnComputeInternalInsetsListener paramOnComputeInternalInsetsListener)
  {
    checkIsAlive();
    if (mOnComputeInternalInsetsListeners == null) {
      mOnComputeInternalInsetsListeners = new CopyOnWriteArray();
    }
    mOnComputeInternalInsetsListeners.add(paramOnComputeInternalInsetsListener);
  }
  
  public void addOnDrawListener(OnDrawListener paramOnDrawListener)
  {
    checkIsAlive();
    if (mOnDrawListeners == null) {
      mOnDrawListeners = new ArrayList();
    }
    if (mInDispatchOnDraw)
    {
      IllegalStateException localIllegalStateException = new IllegalStateException("Cannot call addOnDrawListener inside of onDraw");
      if (!sIllegalOnDrawModificationIsFatal) {
        Log.e("ViewTreeObserver", localIllegalStateException.getMessage(), localIllegalStateException);
      } else {
        throw localIllegalStateException;
      }
    }
    mOnDrawListeners.add(paramOnDrawListener);
  }
  
  public void addOnEnterAnimationCompleteListener(OnEnterAnimationCompleteListener paramOnEnterAnimationCompleteListener)
  {
    checkIsAlive();
    if (mOnEnterAnimationCompleteListeners == null) {
      mOnEnterAnimationCompleteListeners = new CopyOnWriteArrayList();
    }
    mOnEnterAnimationCompleteListeners.add(paramOnEnterAnimationCompleteListener);
  }
  
  public void addOnGlobalFocusChangeListener(OnGlobalFocusChangeListener paramOnGlobalFocusChangeListener)
  {
    checkIsAlive();
    if (mOnGlobalFocusListeners == null) {
      mOnGlobalFocusListeners = new CopyOnWriteArrayList();
    }
    mOnGlobalFocusListeners.add(paramOnGlobalFocusChangeListener);
  }
  
  public void addOnGlobalLayoutListener(OnGlobalLayoutListener paramOnGlobalLayoutListener)
  {
    checkIsAlive();
    if (mOnGlobalLayoutListeners == null) {
      mOnGlobalLayoutListeners = new CopyOnWriteArray();
    }
    mOnGlobalLayoutListeners.add(paramOnGlobalLayoutListener);
  }
  
  public void addOnPreDrawListener(OnPreDrawListener paramOnPreDrawListener)
  {
    checkIsAlive();
    if (mOnPreDrawListeners == null) {
      mOnPreDrawListeners = new CopyOnWriteArray();
    }
    mOnPreDrawListeners.add(paramOnPreDrawListener);
  }
  
  public void addOnScrollChangedListener(OnScrollChangedListener paramOnScrollChangedListener)
  {
    checkIsAlive();
    if (mOnScrollChangedListeners == null) {
      mOnScrollChangedListeners = new CopyOnWriteArray();
    }
    mOnScrollChangedListeners.add(paramOnScrollChangedListener);
  }
  
  public void addOnTouchModeChangeListener(OnTouchModeChangeListener paramOnTouchModeChangeListener)
  {
    checkIsAlive();
    if (mOnTouchModeChangeListeners == null) {
      mOnTouchModeChangeListeners = new CopyOnWriteArrayList();
    }
    mOnTouchModeChangeListeners.add(paramOnTouchModeChangeListener);
  }
  
  public void addOnWindowAttachListener(OnWindowAttachListener paramOnWindowAttachListener)
  {
    checkIsAlive();
    if (mOnWindowAttachListeners == null) {
      mOnWindowAttachListeners = new CopyOnWriteArrayList();
    }
    mOnWindowAttachListeners.add(paramOnWindowAttachListener);
  }
  
  public void addOnWindowFocusChangeListener(OnWindowFocusChangeListener paramOnWindowFocusChangeListener)
  {
    checkIsAlive();
    if (mOnWindowFocusListeners == null) {
      mOnWindowFocusListeners = new CopyOnWriteArrayList();
    }
    mOnWindowFocusListeners.add(paramOnWindowFocusChangeListener);
  }
  
  public void addOnWindowShownListener(OnWindowShownListener paramOnWindowShownListener)
  {
    checkIsAlive();
    if (mOnWindowShownListeners == null) {
      mOnWindowShownListeners = new CopyOnWriteArray();
    }
    mOnWindowShownListeners.add(paramOnWindowShownListener);
    if (mWindowShown) {
      paramOnWindowShownListener.onWindowShown();
    }
  }
  
  final void dispatchOnComputeInternalInsets(InternalInsetsInfo paramInternalInsetsInfo)
  {
    CopyOnWriteArray localCopyOnWriteArray = mOnComputeInternalInsetsListeners;
    if ((localCopyOnWriteArray != null) && (localCopyOnWriteArray.size() > 0))
    {
      ViewTreeObserver.CopyOnWriteArray.Access localAccess = localCopyOnWriteArray.start();
      try
      {
        int i = localAccess.size();
        for (int j = 0; j < i; j++) {
          ((OnComputeInternalInsetsListener)localAccess.get(j)).onComputeInternalInsets(paramInternalInsetsInfo);
        }
      }
      finally
      {
        localCopyOnWriteArray.end();
      }
    }
  }
  
  public final void dispatchOnDraw()
  {
    if (mOnDrawListeners != null)
    {
      mInDispatchOnDraw = true;
      ArrayList localArrayList = mOnDrawListeners;
      int i = localArrayList.size();
      for (int j = 0; j < i; j++) {
        ((OnDrawListener)localArrayList.get(j)).onDraw();
      }
      mInDispatchOnDraw = false;
    }
  }
  
  public final void dispatchOnEnterAnimationComplete()
  {
    Object localObject = mOnEnterAnimationCompleteListeners;
    if ((localObject != null) && (!((CopyOnWriteArrayList)localObject).isEmpty()))
    {
      localObject = ((CopyOnWriteArrayList)localObject).iterator();
      while (((Iterator)localObject).hasNext()) {
        ((OnEnterAnimationCompleteListener)((Iterator)localObject).next()).onEnterAnimationComplete();
      }
    }
  }
  
  final void dispatchOnGlobalFocusChange(View paramView1, View paramView2)
  {
    Object localObject = mOnGlobalFocusListeners;
    if ((localObject != null) && (((CopyOnWriteArrayList)localObject).size() > 0))
    {
      localObject = ((CopyOnWriteArrayList)localObject).iterator();
      while (((Iterator)localObject).hasNext()) {
        ((OnGlobalFocusChangeListener)((Iterator)localObject).next()).onGlobalFocusChanged(paramView1, paramView2);
      }
    }
  }
  
  public final void dispatchOnGlobalLayout()
  {
    CopyOnWriteArray localCopyOnWriteArray = mOnGlobalLayoutListeners;
    if ((localCopyOnWriteArray != null) && (localCopyOnWriteArray.size() > 0))
    {
      ViewTreeObserver.CopyOnWriteArray.Access localAccess = localCopyOnWriteArray.start();
      try
      {
        int i = localAccess.size();
        for (int j = 0; j < i; j++) {
          ((OnGlobalLayoutListener)localAccess.get(j)).onGlobalLayout();
        }
      }
      finally
      {
        localCopyOnWriteArray.end();
      }
    }
  }
  
  public final boolean dispatchOnPreDraw()
  {
    boolean bool1 = false;
    boolean bool2 = false;
    CopyOnWriteArray localCopyOnWriteArray = mOnPreDrawListeners;
    boolean bool3 = bool1;
    if (localCopyOnWriteArray != null)
    {
      bool3 = bool1;
      if (localCopyOnWriteArray.size() > 0)
      {
        ViewTreeObserver.CopyOnWriteArray.Access localAccess = localCopyOnWriteArray.start();
        try
        {
          int i = localAccess.size();
          int j = 0;
          bool3 = bool2;
          while (j < i)
          {
            bool2 = ((OnPreDrawListener)localAccess.get(j)).onPreDraw();
            bool3 |= bool2 ^ true;
            j++;
          }
        }
        finally
        {
          localCopyOnWriteArray.end();
        }
      }
    }
    return bool3;
  }
  
  final void dispatchOnScrollChanged()
  {
    CopyOnWriteArray localCopyOnWriteArray = mOnScrollChangedListeners;
    if ((localCopyOnWriteArray != null) && (localCopyOnWriteArray.size() > 0))
    {
      ViewTreeObserver.CopyOnWriteArray.Access localAccess = localCopyOnWriteArray.start();
      try
      {
        int i = localAccess.size();
        for (int j = 0; j < i; j++) {
          ((OnScrollChangedListener)localAccess.get(j)).onScrollChanged();
        }
      }
      finally
      {
        localCopyOnWriteArray.end();
      }
    }
  }
  
  final void dispatchOnTouchModeChanged(boolean paramBoolean)
  {
    Object localObject = mOnTouchModeChangeListeners;
    if ((localObject != null) && (((CopyOnWriteArrayList)localObject).size() > 0))
    {
      localObject = ((CopyOnWriteArrayList)localObject).iterator();
      while (((Iterator)localObject).hasNext()) {
        ((OnTouchModeChangeListener)((Iterator)localObject).next()).onTouchModeChanged(paramBoolean);
      }
    }
  }
  
  final void dispatchOnWindowAttachedChange(boolean paramBoolean)
  {
    Object localObject = mOnWindowAttachListeners;
    if ((localObject != null) && (((CopyOnWriteArrayList)localObject).size() > 0))
    {
      Iterator localIterator = ((CopyOnWriteArrayList)localObject).iterator();
      while (localIterator.hasNext())
      {
        localObject = (OnWindowAttachListener)localIterator.next();
        if (paramBoolean) {
          ((OnWindowAttachListener)localObject).onWindowAttached();
        } else {
          ((OnWindowAttachListener)localObject).onWindowDetached();
        }
      }
    }
  }
  
  final void dispatchOnWindowFocusChange(boolean paramBoolean)
  {
    Object localObject = mOnWindowFocusListeners;
    if ((localObject != null) && (((CopyOnWriteArrayList)localObject).size() > 0))
    {
      localObject = ((CopyOnWriteArrayList)localObject).iterator();
      while (((Iterator)localObject).hasNext()) {
        ((OnWindowFocusChangeListener)((Iterator)localObject).next()).onWindowFocusChanged(paramBoolean);
      }
    }
  }
  
  public final void dispatchOnWindowShown()
  {
    mWindowShown = true;
    CopyOnWriteArray localCopyOnWriteArray = mOnWindowShownListeners;
    if ((localCopyOnWriteArray != null) && (localCopyOnWriteArray.size() > 0))
    {
      ViewTreeObserver.CopyOnWriteArray.Access localAccess = localCopyOnWriteArray.start();
      try
      {
        int i = localAccess.size();
        for (int j = 0; j < i; j++) {
          ((OnWindowShownListener)localAccess.get(j)).onWindowShown();
        }
      }
      finally
      {
        localCopyOnWriteArray.end();
      }
    }
  }
  
  final boolean hasComputeInternalInsetsListeners()
  {
    CopyOnWriteArray localCopyOnWriteArray = mOnComputeInternalInsetsListeners;
    boolean bool;
    if ((localCopyOnWriteArray != null) && (localCopyOnWriteArray.size() > 0)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  final boolean hasOnPreDrawListeners()
  {
    boolean bool;
    if ((mOnPreDrawListeners != null) && (mOnPreDrawListeners.size() > 0)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isAlive()
  {
    return mAlive;
  }
  
  void merge(ViewTreeObserver paramViewTreeObserver)
  {
    if (mOnWindowAttachListeners != null) {
      if (mOnWindowAttachListeners != null) {
        mOnWindowAttachListeners.addAll(mOnWindowAttachListeners);
      } else {
        mOnWindowAttachListeners = mOnWindowAttachListeners;
      }
    }
    if (mOnWindowFocusListeners != null) {
      if (mOnWindowFocusListeners != null) {
        mOnWindowFocusListeners.addAll(mOnWindowFocusListeners);
      } else {
        mOnWindowFocusListeners = mOnWindowFocusListeners;
      }
    }
    if (mOnGlobalFocusListeners != null) {
      if (mOnGlobalFocusListeners != null) {
        mOnGlobalFocusListeners.addAll(mOnGlobalFocusListeners);
      } else {
        mOnGlobalFocusListeners = mOnGlobalFocusListeners;
      }
    }
    if (mOnGlobalLayoutListeners != null) {
      if (mOnGlobalLayoutListeners != null) {
        mOnGlobalLayoutListeners.addAll(mOnGlobalLayoutListeners);
      } else {
        mOnGlobalLayoutListeners = mOnGlobalLayoutListeners;
      }
    }
    if (mOnPreDrawListeners != null) {
      if (mOnPreDrawListeners != null) {
        mOnPreDrawListeners.addAll(mOnPreDrawListeners);
      } else {
        mOnPreDrawListeners = mOnPreDrawListeners;
      }
    }
    if (mOnDrawListeners != null) {
      if (mOnDrawListeners != null) {
        mOnDrawListeners.addAll(mOnDrawListeners);
      } else {
        mOnDrawListeners = mOnDrawListeners;
      }
    }
    if (mOnTouchModeChangeListeners != null) {
      if (mOnTouchModeChangeListeners != null) {
        mOnTouchModeChangeListeners.addAll(mOnTouchModeChangeListeners);
      } else {
        mOnTouchModeChangeListeners = mOnTouchModeChangeListeners;
      }
    }
    if (mOnComputeInternalInsetsListeners != null) {
      if (mOnComputeInternalInsetsListeners != null) {
        mOnComputeInternalInsetsListeners.addAll(mOnComputeInternalInsetsListeners);
      } else {
        mOnComputeInternalInsetsListeners = mOnComputeInternalInsetsListeners;
      }
    }
    if (mOnScrollChangedListeners != null) {
      if (mOnScrollChangedListeners != null) {
        mOnScrollChangedListeners.addAll(mOnScrollChangedListeners);
      } else {
        mOnScrollChangedListeners = mOnScrollChangedListeners;
      }
    }
    if (mOnWindowShownListeners != null) {
      if (mOnWindowShownListeners != null) {
        mOnWindowShownListeners.addAll(mOnWindowShownListeners);
      } else {
        mOnWindowShownListeners = mOnWindowShownListeners;
      }
    }
    paramViewTreeObserver.kill();
  }
  
  @Deprecated
  public void removeGlobalOnLayoutListener(OnGlobalLayoutListener paramOnGlobalLayoutListener)
  {
    removeOnGlobalLayoutListener(paramOnGlobalLayoutListener);
  }
  
  public void removeOnComputeInternalInsetsListener(OnComputeInternalInsetsListener paramOnComputeInternalInsetsListener)
  {
    checkIsAlive();
    if (mOnComputeInternalInsetsListeners == null) {
      return;
    }
    mOnComputeInternalInsetsListeners.remove(paramOnComputeInternalInsetsListener);
  }
  
  public void removeOnDrawListener(OnDrawListener paramOnDrawListener)
  {
    checkIsAlive();
    if (mOnDrawListeners == null) {
      return;
    }
    if (mInDispatchOnDraw)
    {
      IllegalStateException localIllegalStateException = new IllegalStateException("Cannot call removeOnDrawListener inside of onDraw");
      if (!sIllegalOnDrawModificationIsFatal) {
        Log.e("ViewTreeObserver", localIllegalStateException.getMessage(), localIllegalStateException);
      } else {
        throw localIllegalStateException;
      }
    }
    mOnDrawListeners.remove(paramOnDrawListener);
  }
  
  public void removeOnEnterAnimationCompleteListener(OnEnterAnimationCompleteListener paramOnEnterAnimationCompleteListener)
  {
    checkIsAlive();
    if (mOnEnterAnimationCompleteListeners == null) {
      return;
    }
    mOnEnterAnimationCompleteListeners.remove(paramOnEnterAnimationCompleteListener);
  }
  
  public void removeOnGlobalFocusChangeListener(OnGlobalFocusChangeListener paramOnGlobalFocusChangeListener)
  {
    checkIsAlive();
    if (mOnGlobalFocusListeners == null) {
      return;
    }
    mOnGlobalFocusListeners.remove(paramOnGlobalFocusChangeListener);
  }
  
  public void removeOnGlobalLayoutListener(OnGlobalLayoutListener paramOnGlobalLayoutListener)
  {
    checkIsAlive();
    if (mOnGlobalLayoutListeners == null) {
      return;
    }
    mOnGlobalLayoutListeners.remove(paramOnGlobalLayoutListener);
  }
  
  public void removeOnPreDrawListener(OnPreDrawListener paramOnPreDrawListener)
  {
    checkIsAlive();
    if (mOnPreDrawListeners == null) {
      return;
    }
    mOnPreDrawListeners.remove(paramOnPreDrawListener);
  }
  
  public void removeOnScrollChangedListener(OnScrollChangedListener paramOnScrollChangedListener)
  {
    checkIsAlive();
    if (mOnScrollChangedListeners == null) {
      return;
    }
    mOnScrollChangedListeners.remove(paramOnScrollChangedListener);
  }
  
  public void removeOnTouchModeChangeListener(OnTouchModeChangeListener paramOnTouchModeChangeListener)
  {
    checkIsAlive();
    if (mOnTouchModeChangeListeners == null) {
      return;
    }
    mOnTouchModeChangeListeners.remove(paramOnTouchModeChangeListener);
  }
  
  public void removeOnWindowAttachListener(OnWindowAttachListener paramOnWindowAttachListener)
  {
    checkIsAlive();
    if (mOnWindowAttachListeners == null) {
      return;
    }
    mOnWindowAttachListeners.remove(paramOnWindowAttachListener);
  }
  
  public void removeOnWindowFocusChangeListener(OnWindowFocusChangeListener paramOnWindowFocusChangeListener)
  {
    checkIsAlive();
    if (mOnWindowFocusListeners == null) {
      return;
    }
    mOnWindowFocusListeners.remove(paramOnWindowFocusChangeListener);
  }
  
  public void removeOnWindowShownListener(OnWindowShownListener paramOnWindowShownListener)
  {
    checkIsAlive();
    if (mOnWindowShownListeners == null) {
      return;
    }
    mOnWindowShownListeners.remove(paramOnWindowShownListener);
  }
  
  static class CopyOnWriteArray<T>
  {
    private final Access<T> mAccess = new Access();
    private ArrayList<T> mData = new ArrayList();
    private ArrayList<T> mDataCopy;
    private boolean mStart;
    
    CopyOnWriteArray() {}
    
    private ArrayList<T> getArray()
    {
      if (mStart)
      {
        if (mDataCopy == null) {
          mDataCopy = new ArrayList(mData);
        }
        return mDataCopy;
      }
      return mData;
    }
    
    void add(T paramT)
    {
      getArray().add(paramT);
    }
    
    void addAll(CopyOnWriteArray<T> paramCopyOnWriteArray)
    {
      getArray().addAll(mData);
    }
    
    void clear()
    {
      getArray().clear();
    }
    
    void end()
    {
      if (mStart)
      {
        mStart = false;
        if (mDataCopy != null)
        {
          mData = mDataCopy;
          mAccess.mData.clear();
          Access.access$102(mAccess, 0);
        }
        mDataCopy = null;
        return;
      }
      throw new IllegalStateException("Iteration not started");
    }
    
    void remove(T paramT)
    {
      getArray().remove(paramT);
    }
    
    int size()
    {
      return getArray().size();
    }
    
    Access<T> start()
    {
      if (!mStart)
      {
        mStart = true;
        mDataCopy = null;
        Access.access$002(mAccess, mData);
        Access.access$102(mAccess, mData.size());
        return mAccess;
      }
      throw new IllegalStateException("Iteration already started");
    }
    
    static class Access<T>
    {
      private ArrayList<T> mData;
      private int mSize;
      
      Access() {}
      
      T get(int paramInt)
      {
        return mData.get(paramInt);
      }
      
      int size()
      {
        return mSize;
      }
    }
  }
  
  public static final class InternalInsetsInfo
  {
    public static final int TOUCHABLE_INSETS_CONTENT = 1;
    public static final int TOUCHABLE_INSETS_FRAME = 0;
    public static final int TOUCHABLE_INSETS_REGION = 3;
    public static final int TOUCHABLE_INSETS_VISIBLE = 2;
    public final Rect contentInsets = new Rect();
    int mTouchableInsets;
    public final Region touchableRegion = new Region();
    public final Rect visibleInsets = new Rect();
    
    public InternalInsetsInfo() {}
    
    public boolean equals(Object paramObject)
    {
      boolean bool = true;
      if (this == paramObject) {
        return true;
      }
      if ((paramObject != null) && (getClass() == paramObject.getClass()))
      {
        paramObject = (InternalInsetsInfo)paramObject;
        if ((mTouchableInsets != mTouchableInsets) || (!contentInsets.equals(contentInsets)) || (!visibleInsets.equals(visibleInsets)) || (!touchableRegion.equals(touchableRegion))) {
          bool = false;
        }
        return bool;
      }
      return false;
    }
    
    public int hashCode()
    {
      return 31 * (31 * (31 * contentInsets.hashCode() + visibleInsets.hashCode()) + touchableRegion.hashCode()) + mTouchableInsets;
    }
    
    boolean isEmpty()
    {
      boolean bool;
      if ((contentInsets.isEmpty()) && (visibleInsets.isEmpty()) && (touchableRegion.isEmpty()) && (mTouchableInsets == 0)) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    void reset()
    {
      contentInsets.setEmpty();
      visibleInsets.setEmpty();
      touchableRegion.setEmpty();
      mTouchableInsets = 0;
    }
    
    void set(InternalInsetsInfo paramInternalInsetsInfo)
    {
      contentInsets.set(contentInsets);
      visibleInsets.set(visibleInsets);
      touchableRegion.set(touchableRegion);
      mTouchableInsets = mTouchableInsets;
    }
    
    public void setTouchableInsets(int paramInt)
    {
      mTouchableInsets = paramInt;
    }
  }
  
  public static abstract interface OnComputeInternalInsetsListener
  {
    public abstract void onComputeInternalInsets(ViewTreeObserver.InternalInsetsInfo paramInternalInsetsInfo);
  }
  
  public static abstract interface OnDrawListener
  {
    public abstract void onDraw();
  }
  
  public static abstract interface OnEnterAnimationCompleteListener
  {
    public abstract void onEnterAnimationComplete();
  }
  
  public static abstract interface OnGlobalFocusChangeListener
  {
    public abstract void onGlobalFocusChanged(View paramView1, View paramView2);
  }
  
  public static abstract interface OnGlobalLayoutListener
  {
    public abstract void onGlobalLayout();
  }
  
  public static abstract interface OnPreDrawListener
  {
    public abstract boolean onPreDraw();
  }
  
  public static abstract interface OnScrollChangedListener
  {
    public abstract void onScrollChanged();
  }
  
  public static abstract interface OnTouchModeChangeListener
  {
    public abstract void onTouchModeChanged(boolean paramBoolean);
  }
  
  public static abstract interface OnWindowAttachListener
  {
    public abstract void onWindowAttached();
    
    public abstract void onWindowDetached();
  }
  
  public static abstract interface OnWindowFocusChangeListener
  {
    public abstract void onWindowFocusChanged(boolean paramBoolean);
  }
  
  public static abstract interface OnWindowShownListener
  {
    public abstract void onWindowShown();
  }
}
