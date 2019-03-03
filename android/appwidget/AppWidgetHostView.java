package android.appwidget;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.LayoutInflater.Filter;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.RemoteViews;
import android.widget.RemoteViews.OnClickHandler;
import android.widget.RemoteViews.OnViewAppliedListener;
import android.widget.RemoteViewsAdapter.RemoteAdapterConnectionCallback;
import android.widget.TextView;
import java.util.concurrent.Executor;

public class AppWidgetHostView
  extends FrameLayout
{
  private static final LayoutInflater.Filter INFLATER_FILTER = _..Lambda.AppWidgetHostView.AzPWN1sIsRb7M_0Ss1rK2mksT_o.INSTANCE;
  private static final String KEY_JAILED_ARRAY = "jail";
  static final boolean LOGD = false;
  static final String TAG = "AppWidgetHostView";
  static final int VIEW_MODE_CONTENT = 1;
  static final int VIEW_MODE_DEFAULT = 3;
  static final int VIEW_MODE_ERROR = 2;
  static final int VIEW_MODE_NOINIT = 0;
  int mAppWidgetId;
  private Executor mAsyncExecutor;
  Context mContext;
  AppWidgetProviderInfo mInfo;
  private CancellationSignal mLastExecutionSignal;
  int mLayoutId = -1;
  private RemoteViews.OnClickHandler mOnClickHandler;
  Context mRemoteContext;
  View mView;
  int mViewMode = 0;
  
  public AppWidgetHostView(Context paramContext)
  {
    this(paramContext, 17432576, 17432577);
  }
  
  public AppWidgetHostView(Context paramContext, int paramInt1, int paramInt2)
  {
    super(paramContext);
    mContext = paramContext;
    setIsRootNamespace(true);
  }
  
  public AppWidgetHostView(Context paramContext, RemoteViews.OnClickHandler paramOnClickHandler)
  {
    this(paramContext, 17432576, 17432577);
    mOnClickHandler = paramOnClickHandler;
  }
  
  private void applyContent(View paramView, boolean paramBoolean, Exception paramException)
  {
    View localView = paramView;
    if (paramView == null)
    {
      if (mViewMode == 2) {
        return;
      }
      if (paramException != null)
      {
        paramView = new StringBuilder();
        paramView.append("Error inflating RemoteViews : ");
        paramView.append(paramException.toString());
        Log.w("AppWidgetHostView", paramView.toString());
      }
      localView = getErrorView();
      mViewMode = 2;
    }
    if (!paramBoolean)
    {
      prepareView(localView);
      addView(localView);
    }
    if (mView != localView)
    {
      removeView(mView);
      mView = localView;
    }
  }
  
  private int generateId()
  {
    int i = getId();
    if (i == -1) {
      i = mAppWidgetId;
    }
    return i;
  }
  
  private Rect getDefaultPadding()
  {
    Context localContext = mContext;
    ApplicationInfo localApplicationInfo;
    if (mInfo == null) {
      localApplicationInfo = null;
    } else {
      localApplicationInfo = mInfo.providerInfo.applicationInfo;
    }
    return getDefaultPaddingForWidget(localContext, localApplicationInfo, null);
  }
  
  public static Rect getDefaultPaddingForWidget(Context paramContext, ComponentName paramComponentName, Rect paramRect)
  {
    Object localObject = null;
    try
    {
      paramComponentName = paramContext.getPackageManager().getApplicationInfo(paramComponentName.getPackageName(), 0);
    }
    catch (PackageManager.NameNotFoundException paramComponentName)
    {
      paramComponentName = localObject;
    }
    return getDefaultPaddingForWidget(paramContext, paramComponentName, paramRect);
  }
  
  private static Rect getDefaultPaddingForWidget(Context paramContext, ApplicationInfo paramApplicationInfo, Rect paramRect)
  {
    if (paramRect == null) {
      paramRect = new Rect(0, 0, 0, 0);
    } else {
      paramRect.set(0, 0, 0, 0);
    }
    if ((paramApplicationInfo != null) && (targetSdkVersion >= 14))
    {
      paramContext = paramContext.getResources();
      left = paramContext.getDimensionPixelSize(17105128);
      right = paramContext.getDimensionPixelSize(17105129);
      top = paramContext.getDimensionPixelSize(17105130);
      bottom = paramContext.getDimensionPixelSize(17105127);
    }
    return paramRect;
  }
  
  private void inflateAsync(RemoteViews paramRemoteViews)
  {
    mRemoteContext = getRemoteContext();
    int i = paramRemoteViews.getLayoutId();
    if ((i == mLayoutId) && (mView != null)) {
      try
      {
        Context localContext = mContext;
        View localView = mView;
        Executor localExecutor = mAsyncExecutor;
        ViewApplyListener localViewApplyListener = new android/appwidget/AppWidgetHostView$ViewApplyListener;
        localViewApplyListener.<init>(this, paramRemoteViews, i, true);
        mLastExecutionSignal = paramRemoteViews.reapplyAsync(localContext, localView, localExecutor, localViewApplyListener, mOnClickHandler);
      }
      catch (Exception localException) {}
    }
    if (mLastExecutionSignal == null) {
      mLastExecutionSignal = paramRemoteViews.applyAsync(mContext, this, mAsyncExecutor, new ViewApplyListener(paramRemoteViews, i, false), mOnClickHandler);
    }
  }
  
  protected void applyRemoteViews(RemoteViews paramRemoteViews, boolean paramBoolean)
  {
    boolean bool1 = false;
    boolean bool2 = false;
    Object localObject1 = null;
    Object localObject2 = null;
    Object localObject3 = null;
    if (mLastExecutionSignal != null)
    {
      mLastExecutionSignal.cancel();
      mLastExecutionSignal = null;
    }
    if (paramRemoteViews == null)
    {
      if (mViewMode == 3) {
        return;
      }
      localObject1 = getDefaultView();
      mLayoutId = -1;
      mViewMode = 3;
      paramBoolean = bool1;
      localObject3 = localObject2;
    }
    else
    {
      if ((mAsyncExecutor != null) && (paramBoolean))
      {
        inflateAsync(paramRemoteViews);
        return;
      }
      mRemoteContext = getRemoteContext();
      int i = paramRemoteViews.getLayoutId();
      paramBoolean = bool2;
      localObject2 = localObject1;
      Object localObject4 = localObject3;
      if (0 == 0)
      {
        paramBoolean = bool2;
        localObject2 = localObject1;
        localObject4 = localObject3;
        if (i == mLayoutId) {
          try
          {
            paramRemoteViews.reapply(mContext, mView, mOnClickHandler);
            localObject2 = mView;
            paramBoolean = true;
            localObject4 = localObject3;
          }
          catch (RuntimeException localRuntimeException2)
          {
            localObject2 = localObject1;
            paramBoolean = bool2;
          }
        }
      }
      localObject1 = localObject2;
      localObject3 = localRuntimeException2;
      if (localObject2 == null) {
        try
        {
          localObject1 = paramRemoteViews.apply(mContext, this, mOnClickHandler);
          localObject3 = localRuntimeException2;
        }
        catch (RuntimeException localRuntimeException1)
        {
          localObject1 = localObject2;
        }
      }
      mLayoutId = i;
      mViewMode = 1;
    }
    applyContent((View)localObject1, paramBoolean, localRuntimeException1);
  }
  
  protected void dispatchRestoreInstanceState(SparseArray<Parcelable> paramSparseArray)
  {
    Object localObject = (Parcelable)paramSparseArray.get(generateId());
    paramSparseArray = null;
    if ((localObject instanceof Bundle)) {
      paramSparseArray = ((Bundle)localObject).getSparseParcelableArray("jail");
    }
    localObject = paramSparseArray;
    if (paramSparseArray == null) {
      localObject = new SparseArray();
    }
    try
    {
      super.dispatchRestoreInstanceState((SparseArray)localObject);
    }
    catch (Exception localException)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("failed to restoreInstanceState for widget id: ");
      ((StringBuilder)localObject).append(mAppWidgetId);
      ((StringBuilder)localObject).append(", ");
      if (mInfo == null) {
        paramSparseArray = "null";
      } else {
        paramSparseArray = mInfo.provider;
      }
      ((StringBuilder)localObject).append(paramSparseArray);
      Log.e("AppWidgetHostView", ((StringBuilder)localObject).toString(), localException);
    }
  }
  
  protected void dispatchSaveInstanceState(SparseArray<Parcelable> paramSparseArray)
  {
    SparseArray localSparseArray = new SparseArray();
    super.dispatchSaveInstanceState(localSparseArray);
    Bundle localBundle = new Bundle();
    localBundle.putSparseParcelableArray("jail", localSparseArray);
    paramSparseArray.put(generateId(), localBundle);
  }
  
  public FrameLayout.LayoutParams generateLayoutParams(AttributeSet paramAttributeSet)
  {
    Context localContext;
    if (mRemoteContext != null) {
      localContext = mRemoteContext;
    } else {
      localContext = mContext;
    }
    return new FrameLayout.LayoutParams(localContext, paramAttributeSet);
  }
  
  public int getAppWidgetId()
  {
    return mAppWidgetId;
  }
  
  public AppWidgetProviderInfo getAppWidgetInfo()
  {
    return mInfo;
  }
  
  protected View getDefaultView()
  {
    Object localObject1 = null;
    Object localObject2 = null;
    Object localObject3 = null;
    try
    {
      if (mInfo != null)
      {
        localObject2 = getRemoteContext();
        mRemoteContext = ((Context)localObject2);
        localObject2 = ((LayoutInflater)((Context)localObject2).getSystemService("layout_inflater")).cloneInContext((Context)localObject2);
        ((LayoutInflater)localObject2).setFilter(INFLATER_FILTER);
        Bundle localBundle = AppWidgetManager.getInstance(mContext).getAppWidgetOptions(mAppWidgetId);
        int i = mInfo.initialLayout;
        int j = i;
        if (localBundle.containsKey("appWidgetCategory"))
        {
          j = i;
          if (localBundle.getInt("appWidgetCategory") == 2)
          {
            j = mInfo.initialKeyguardLayout;
            if (j != 0) {
              i = j;
            }
            j = i;
          }
        }
        localObject2 = ((LayoutInflater)localObject2).inflate(j, this, false);
        localObject1 = localObject2;
      }
      else
      {
        Log.w("AppWidgetHostView", "can't inflate defaultView because mInfo is missing");
        localObject1 = localObject2;
      }
    }
    catch (RuntimeException localRuntimeException) {}
    if (localRuntimeException != null)
    {
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("Error inflating AppWidget ");
      ((StringBuilder)localObject2).append(mInfo);
      ((StringBuilder)localObject2).append(": ");
      ((StringBuilder)localObject2).append(localRuntimeException.toString());
      Log.w("AppWidgetHostView", ((StringBuilder)localObject2).toString());
    }
    Object localObject4 = localObject1;
    if (localObject1 == null) {
      localObject4 = getErrorView();
    }
    return localObject4;
  }
  
  protected View getErrorView()
  {
    TextView localTextView = new TextView(mContext);
    localTextView.setText(17040037);
    localTextView.setBackgroundColor(Color.argb(127, 0, 0, 0));
    return localTextView;
  }
  
  protected Context getRemoteContext()
  {
    try
    {
      Context localContext = mContext.createApplicationContext(mInfo.providerInfo.applicationInfo, 4);
      return localContext;
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Package name ");
      localStringBuilder.append(mInfo.providerInfo.packageName);
      localStringBuilder.append(" not found");
      Log.e("AppWidgetHostView", localStringBuilder.toString());
    }
    return mContext;
  }
  
  public void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo paramAccessibilityNodeInfo)
  {
    super.onInitializeAccessibilityNodeInfoInternal(paramAccessibilityNodeInfo);
    paramAccessibilityNodeInfo.setClassName(AppWidgetHostView.class.getName());
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    try
    {
      super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    }
    catch (RuntimeException localRuntimeException)
    {
      Log.e("AppWidgetHostView", "Remote provider threw runtime exception, using error view instead.", localRuntimeException);
      removeViewInLayout(mView);
      View localView = getErrorView();
      prepareView(localView);
      addViewInLayout(localView, 0, localView.getLayoutParams());
      measureChild(localView, View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(getMeasuredHeight(), 1073741824));
      localView.layout(0, 0, localView.getMeasuredWidth() + mPaddingLeft + mPaddingRight, localView.getMeasuredHeight() + mPaddingTop + mPaddingBottom);
      mView = localView;
      mViewMode = 2;
    }
  }
  
  protected void prepareView(View paramView)
  {
    FrameLayout.LayoutParams localLayoutParams1 = (FrameLayout.LayoutParams)paramView.getLayoutParams();
    FrameLayout.LayoutParams localLayoutParams2 = localLayoutParams1;
    if (localLayoutParams1 == null) {
      localLayoutParams2 = new FrameLayout.LayoutParams(-1, -1);
    }
    gravity = 17;
    paramView.setLayoutParams(localLayoutParams2);
  }
  
  void resetAppWidget(AppWidgetProviderInfo paramAppWidgetProviderInfo)
  {
    setAppWidget(mAppWidgetId, paramAppWidgetProviderInfo);
    mViewMode = 0;
    updateAppWidget(null);
  }
  
  public void setAppWidget(int paramInt, AppWidgetProviderInfo paramAppWidgetProviderInfo)
  {
    mAppWidgetId = paramInt;
    mInfo = paramAppWidgetProviderInfo;
    Object localObject = getDefaultPadding();
    setPadding(left, top, right, bottom);
    if (paramAppWidgetProviderInfo != null)
    {
      String str = paramAppWidgetProviderInfo.loadLabel(getContext().getPackageManager());
      localObject = str;
      if ((providerInfo.applicationInfo.flags & 0x40000000) != 0) {
        localObject = Resources.getSystem().getString(17041092, new Object[] { str });
      }
      setContentDescription((CharSequence)localObject);
    }
  }
  
  public void setExecutor(Executor paramExecutor)
  {
    if (mLastExecutionSignal != null)
    {
      mLastExecutionSignal.cancel();
      mLastExecutionSignal = null;
    }
    mAsyncExecutor = paramExecutor;
  }
  
  public void setOnClickHandler(RemoteViews.OnClickHandler paramOnClickHandler)
  {
    mOnClickHandler = paramOnClickHandler;
  }
  
  public void updateAppWidget(RemoteViews paramRemoteViews)
  {
    applyRemoteViews(paramRemoteViews, true);
  }
  
  public void updateAppWidgetOptions(Bundle paramBundle)
  {
    AppWidgetManager.getInstance(mContext).updateAppWidgetOptions(mAppWidgetId, paramBundle);
  }
  
  public void updateAppWidgetSize(Bundle paramBundle, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    updateAppWidgetSize(paramBundle, paramInt1, paramInt2, paramInt3, paramInt4, false);
  }
  
  public void updateAppWidgetSize(Bundle paramBundle, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean)
  {
    if (paramBundle == null) {
      paramBundle = new Bundle();
    }
    Object localObject = getDefaultPadding();
    float f = getResourcesgetDisplayMetricsdensity;
    int i = (int)((left + right) / f);
    int j = (int)((top + bottom) / f);
    int k = 0;
    if (paramBoolean) {
      m = 0;
    } else {
      m = i;
    }
    int m = paramInt1 - m;
    if (paramBoolean) {
      paramInt1 = 0;
    } else {
      paramInt1 = j;
    }
    paramInt2 -= paramInt1;
    if (paramBoolean) {
      paramInt1 = 0;
    } else {
      paramInt1 = i;
    }
    paramInt3 -= paramInt1;
    if (paramBoolean) {
      j = k;
    }
    paramInt4 -= j;
    localObject = AppWidgetManager.getInstance(mContext).getAppWidgetOptions(mAppWidgetId);
    paramInt1 = 0;
    if ((m != ((Bundle)localObject).getInt("appWidgetMinWidth")) || (paramInt2 != ((Bundle)localObject).getInt("appWidgetMinHeight")) || (paramInt3 != ((Bundle)localObject).getInt("appWidgetMaxWidth")) || (paramInt4 != ((Bundle)localObject).getInt("appWidgetMaxHeight"))) {
      paramInt1 = 1;
    }
    if (paramInt1 != 0)
    {
      paramBundle.putInt("appWidgetMinWidth", m);
      paramBundle.putInt("appWidgetMinHeight", paramInt2);
      paramBundle.putInt("appWidgetMaxWidth", paramInt3);
      paramBundle.putInt("appWidgetMaxHeight", paramInt4);
      updateAppWidgetOptions(paramBundle);
    }
  }
  
  void viewDataChanged(int paramInt)
  {
    Object localObject = findViewById(paramInt);
    if ((localObject != null) && ((localObject instanceof AdapterView)))
    {
      AdapterView localAdapterView = (AdapterView)localObject;
      localObject = localAdapterView.getAdapter();
      if ((localObject instanceof BaseAdapter)) {
        ((BaseAdapter)localObject).notifyDataSetChanged();
      } else if ((localObject == null) && ((localAdapterView instanceof RemoteViewsAdapter.RemoteAdapterConnectionCallback))) {
        ((RemoteViewsAdapter.RemoteAdapterConnectionCallback)localAdapterView).deferNotifyDataSetChanged();
      }
    }
  }
  
  private class ViewApplyListener
    implements RemoteViews.OnViewAppliedListener
  {
    private final boolean mIsReapply;
    private final int mLayoutId;
    private final RemoteViews mViews;
    
    public ViewApplyListener(RemoteViews paramRemoteViews, int paramInt, boolean paramBoolean)
    {
      mViews = paramRemoteViews;
      mLayoutId = paramInt;
      mIsReapply = paramBoolean;
    }
    
    public void onError(Exception paramException)
    {
      if (mIsReapply) {
        AppWidgetHostView.access$102(AppWidgetHostView.this, mViews.applyAsync(mContext, AppWidgetHostView.this, mAsyncExecutor, new ViewApplyListener(AppWidgetHostView.this, mViews, mLayoutId, false), mOnClickHandler));
      } else {
        AppWidgetHostView.this.applyContent(null, false, paramException);
      }
    }
    
    public void onViewApplied(View paramView)
    {
      mLayoutId = mLayoutId;
      mViewMode = 1;
      AppWidgetHostView.this.applyContent(paramView, mIsReapply, null);
    }
  }
}
