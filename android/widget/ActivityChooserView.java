package android.widget;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ActionProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.AccessibilityDelegate;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.accessibility.AccessibilityNodeInfo;
import com.android.internal.R.styleable;
import com.android.internal.view.menu.ShowableListMenu;

public class ActivityChooserView
  extends ViewGroup
  implements ActivityChooserModel.ActivityChooserModelClient
{
  private static final String LOG_TAG = "ActivityChooserView";
  private final LinearLayout mActivityChooserContent;
  private final Drawable mActivityChooserContentBackground;
  private final ActivityChooserViewAdapter mAdapter;
  private final Callbacks mCallbacks;
  private int mDefaultActionButtonContentDescription;
  private final FrameLayout mDefaultActivityButton;
  private final ImageView mDefaultActivityButtonImage;
  private final FrameLayout mExpandActivityOverflowButton;
  private final ImageView mExpandActivityOverflowButtonImage;
  private int mInitialActivityCount = 4;
  private boolean mIsAttachedToWindow;
  private boolean mIsSelectingDefaultActivity;
  private final int mListPopupMaxWidth;
  private ListPopupWindow mListPopupWindow;
  private final DataSetObserver mModelDataSetOberver = new DataSetObserver()
  {
    public void onChanged()
    {
      super.onChanged();
      mAdapter.notifyDataSetChanged();
    }
    
    public void onInvalidated()
    {
      super.onInvalidated();
      mAdapter.notifyDataSetInvalidated();
    }
  };
  private PopupWindow.OnDismissListener mOnDismissListener;
  private final ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener()
  {
    public void onGlobalLayout()
    {
      if (isShowingPopup()) {
        if (!isShown())
        {
          ActivityChooserView.this.getListPopupWindow().dismiss();
        }
        else
        {
          ActivityChooserView.this.getListPopupWindow().show();
          if (mProvider != null) {
            mProvider.subUiVisibilityChanged(true);
          }
        }
      }
    }
  };
  ActionProvider mProvider;
  
  public ActivityChooserView(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public ActivityChooserView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public ActivityChooserView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public ActivityChooserView(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    Object localObject = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.ActivityChooserView, paramInt1, paramInt2);
    mInitialActivityCount = ((TypedArray)localObject).getInt(1, 4);
    paramAttributeSet = ((TypedArray)localObject).getDrawable(0);
    ((TypedArray)localObject).recycle();
    LayoutInflater.from(mContext).inflate(17367075, this, true);
    mCallbacks = new Callbacks(null);
    mActivityChooserContent = ((LinearLayout)findViewById(16908711));
    mActivityChooserContentBackground = mActivityChooserContent.getBackground();
    mDefaultActivityButton = ((FrameLayout)findViewById(16908899));
    mDefaultActivityButton.setOnClickListener(mCallbacks);
    mDefaultActivityButton.setOnLongClickListener(mCallbacks);
    mDefaultActivityButtonImage = ((ImageView)mDefaultActivityButton.findViewById(16909021));
    localObject = (FrameLayout)findViewById(16908921);
    ((FrameLayout)localObject).setOnClickListener(mCallbacks);
    ((FrameLayout)localObject).setAccessibilityDelegate(new View.AccessibilityDelegate()
    {
      public void onInitializeAccessibilityNodeInfo(View paramAnonymousView, AccessibilityNodeInfo paramAnonymousAccessibilityNodeInfo)
      {
        super.onInitializeAccessibilityNodeInfo(paramAnonymousView, paramAnonymousAccessibilityNodeInfo);
        paramAnonymousAccessibilityNodeInfo.setCanOpenPopup(true);
      }
    });
    ((FrameLayout)localObject).setOnTouchListener(new ForwardingListener((View)localObject)
    {
      public ShowableListMenu getPopup()
      {
        return ActivityChooserView.this.getListPopupWindow();
      }
      
      protected boolean onForwardingStarted()
      {
        showPopup();
        return true;
      }
      
      protected boolean onForwardingStopped()
      {
        dismissPopup();
        return true;
      }
    });
    mExpandActivityOverflowButton = ((FrameLayout)localObject);
    mExpandActivityOverflowButtonImage = ((ImageView)((FrameLayout)localObject).findViewById(16909021));
    mExpandActivityOverflowButtonImage.setImageDrawable(paramAttributeSet);
    mAdapter = new ActivityChooserViewAdapter(null);
    mAdapter.registerDataSetObserver(new DataSetObserver()
    {
      public void onChanged()
      {
        super.onChanged();
        ActivityChooserView.this.updateAppearance();
      }
    });
    paramContext = paramContext.getResources();
    mListPopupMaxWidth = Math.max(getDisplayMetricswidthPixels / 2, paramContext.getDimensionPixelSize(17105088));
  }
  
  private ListPopupWindow getListPopupWindow()
  {
    if (mListPopupWindow == null)
    {
      mListPopupWindow = new ListPopupWindow(getContext());
      mListPopupWindow.setAdapter(mAdapter);
      mListPopupWindow.setAnchorView(this);
      mListPopupWindow.setModal(true);
      mListPopupWindow.setOnItemClickListener(mCallbacks);
      mListPopupWindow.setOnDismissListener(mCallbacks);
    }
    return mListPopupWindow;
  }
  
  private void showPopupUnchecked(int paramInt)
  {
    if (mAdapter.getDataModel() != null)
    {
      getViewTreeObserver().addOnGlobalLayoutListener(mOnGlobalLayoutListener);
      boolean bool;
      if (mDefaultActivityButton.getVisibility() == 0) {
        bool = true;
      } else {
        bool = false;
      }
      int i = mAdapter.getActivityCount();
      int j;
      if (bool) {
        j = 1;
      } else {
        j = 0;
      }
      if ((paramInt != Integer.MAX_VALUE) && (i > paramInt + j))
      {
        mAdapter.setShowFooterView(true);
        mAdapter.setMaxActivityCount(paramInt - 1);
      }
      else
      {
        mAdapter.setShowFooterView(false);
        mAdapter.setMaxActivityCount(paramInt);
      }
      ListPopupWindow localListPopupWindow = getListPopupWindow();
      if (!localListPopupWindow.isShowing())
      {
        if ((!mIsSelectingDefaultActivity) && (bool)) {
          mAdapter.setShowDefaultActivity(false, false);
        } else {
          mAdapter.setShowDefaultActivity(true, bool);
        }
        localListPopupWindow.setContentWidth(Math.min(mAdapter.measureContentWidth(), mListPopupMaxWidth));
        localListPopupWindow.show();
        if (mProvider != null) {
          mProvider.subUiVisibilityChanged(true);
        }
        localListPopupWindow.getListView().setContentDescription(mContext.getString(17039441));
        localListPopupWindow.getListView().setSelector(new ColorDrawable(0));
      }
      return;
    }
    throw new IllegalStateException("No data model. Did you call #setDataModel?");
  }
  
  private void updateAppearance()
  {
    if (mAdapter.getCount() > 0) {
      mExpandActivityOverflowButton.setEnabled(true);
    } else {
      mExpandActivityOverflowButton.setEnabled(false);
    }
    int i = mAdapter.getActivityCount();
    int j = mAdapter.getHistorySize();
    if ((i != 1) && ((i <= 1) || (j <= 0)))
    {
      mDefaultActivityButton.setVisibility(8);
    }
    else
    {
      mDefaultActivityButton.setVisibility(0);
      ResolveInfo localResolveInfo = mAdapter.getDefaultActivity();
      Object localObject = mContext.getPackageManager();
      mDefaultActivityButtonImage.setImageDrawable(localResolveInfo.loadIcon((PackageManager)localObject));
      if (mDefaultActionButtonContentDescription != 0)
      {
        localObject = localResolveInfo.loadLabel((PackageManager)localObject);
        localObject = mContext.getString(mDefaultActionButtonContentDescription, new Object[] { localObject });
        mDefaultActivityButton.setContentDescription((CharSequence)localObject);
      }
    }
    if (mDefaultActivityButton.getVisibility() == 0) {
      mActivityChooserContent.setBackground(mActivityChooserContentBackground);
    } else {
      mActivityChooserContent.setBackground(null);
    }
  }
  
  public boolean dismissPopup()
  {
    if (isShowingPopup())
    {
      getListPopupWindow().dismiss();
      ViewTreeObserver localViewTreeObserver = getViewTreeObserver();
      if (localViewTreeObserver.isAlive()) {
        localViewTreeObserver.removeOnGlobalLayoutListener(mOnGlobalLayoutListener);
      }
    }
    return true;
  }
  
  public ActivityChooserModel getDataModel()
  {
    return mAdapter.getDataModel();
  }
  
  public boolean isShowingPopup()
  {
    return getListPopupWindow().isShowing();
  }
  
  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    ActivityChooserModel localActivityChooserModel = mAdapter.getDataModel();
    if (localActivityChooserModel != null) {
      localActivityChooserModel.registerObserver(mModelDataSetOberver);
    }
    mIsAttachedToWindow = true;
  }
  
  protected void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    Object localObject = mAdapter.getDataModel();
    if (localObject != null) {
      ((ActivityChooserModel)localObject).unregisterObserver(mModelDataSetOberver);
    }
    localObject = getViewTreeObserver();
    if (((ViewTreeObserver)localObject).isAlive()) {
      ((ViewTreeObserver)localObject).removeOnGlobalLayoutListener(mOnGlobalLayoutListener);
    }
    if (isShowingPopup()) {
      dismissPopup();
    }
    mIsAttachedToWindow = false;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    mActivityChooserContent.layout(0, 0, paramInt3 - paramInt1, paramInt4 - paramInt2);
    if (!isShowingPopup()) {
      dismissPopup();
    }
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    LinearLayout localLinearLayout = mActivityChooserContent;
    int i = paramInt2;
    if (mDefaultActivityButton.getVisibility() != 0) {
      i = View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(paramInt2), 1073741824);
    }
    measureChild(localLinearLayout, paramInt1, i);
    setMeasuredDimension(localLinearLayout.getMeasuredWidth(), localLinearLayout.getMeasuredHeight());
  }
  
  public void setActivityChooserModel(ActivityChooserModel paramActivityChooserModel)
  {
    mAdapter.setDataModel(paramActivityChooserModel);
    if (isShowingPopup())
    {
      dismissPopup();
      showPopup();
    }
  }
  
  public void setDefaultActionButtonContentDescription(int paramInt)
  {
    mDefaultActionButtonContentDescription = paramInt;
  }
  
  public void setExpandActivityOverflowButtonContentDescription(int paramInt)
  {
    String str = mContext.getString(paramInt);
    mExpandActivityOverflowButtonImage.setContentDescription(str);
  }
  
  public void setExpandActivityOverflowButtonDrawable(Drawable paramDrawable)
  {
    mExpandActivityOverflowButtonImage.setImageDrawable(paramDrawable);
  }
  
  public void setInitialActivityCount(int paramInt)
  {
    mInitialActivityCount = paramInt;
  }
  
  public void setOnDismissListener(PopupWindow.OnDismissListener paramOnDismissListener)
  {
    mOnDismissListener = paramOnDismissListener;
  }
  
  public void setProvider(ActionProvider paramActionProvider)
  {
    mProvider = paramActionProvider;
  }
  
  public boolean showPopup()
  {
    if ((!isShowingPopup()) && (mIsAttachedToWindow))
    {
      mIsSelectingDefaultActivity = false;
      showPopupUnchecked(mInitialActivityCount);
      return true;
    }
    return false;
  }
  
  private class ActivityChooserViewAdapter
    extends BaseAdapter
  {
    private static final int ITEM_VIEW_TYPE_ACTIVITY = 0;
    private static final int ITEM_VIEW_TYPE_COUNT = 3;
    private static final int ITEM_VIEW_TYPE_FOOTER = 1;
    public static final int MAX_ACTIVITY_COUNT_DEFAULT = 4;
    public static final int MAX_ACTIVITY_COUNT_UNLIMITED = Integer.MAX_VALUE;
    private ActivityChooserModel mDataModel;
    private boolean mHighlightDefaultActivity;
    private int mMaxActivityCount = 4;
    private boolean mShowDefaultActivity;
    private boolean mShowFooterView;
    
    private ActivityChooserViewAdapter() {}
    
    public int getActivityCount()
    {
      return mDataModel.getActivityCount();
    }
    
    public int getCount()
    {
      int i = mDataModel.getActivityCount();
      int j = i;
      if (!mShowDefaultActivity)
      {
        j = i;
        if (mDataModel.getDefaultActivity() != null) {
          j = i - 1;
        }
      }
      i = Math.min(j, mMaxActivityCount);
      j = i;
      if (mShowFooterView) {
        j = i + 1;
      }
      return j;
    }
    
    public ActivityChooserModel getDataModel()
    {
      return mDataModel;
    }
    
    public ResolveInfo getDefaultActivity()
    {
      return mDataModel.getDefaultActivity();
    }
    
    public int getHistorySize()
    {
      return mDataModel.getHistorySize();
    }
    
    public Object getItem(int paramInt)
    {
      switch (getItemViewType(paramInt))
      {
      default: 
        throw new IllegalArgumentException();
      case 1: 
        return null;
      }
      int i = paramInt;
      if (!mShowDefaultActivity)
      {
        i = paramInt;
        if (mDataModel.getDefaultActivity() != null) {
          i = paramInt + 1;
        }
      }
      return mDataModel.getActivity(i);
    }
    
    public long getItemId(int paramInt)
    {
      return paramInt;
    }
    
    public int getItemViewType(int paramInt)
    {
      if ((mShowFooterView) && (paramInt == getCount() - 1)) {
        return 1;
      }
      return 0;
    }
    
    public boolean getShowDefaultActivity()
    {
      return mShowDefaultActivity;
    }
    
    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      View localView;
      switch (getItemViewType(paramInt))
      {
      default: 
        throw new IllegalArgumentException();
      case 1: 
        if (paramView != null)
        {
          localView = paramView;
          if (paramView.getId() == 1) {}
        }
        else
        {
          localView = LayoutInflater.from(getContext()).inflate(17367076, paramViewGroup, false);
          localView.setId(1);
          ((TextView)localView.findViewById(16908310)).setText(mContext.getString(17039436));
        }
        return localView;
      }
      if (paramView != null)
      {
        localView = paramView;
        if (paramView.getId() == 16909092) {}
      }
      else
      {
        localView = LayoutInflater.from(getContext()).inflate(17367076, paramViewGroup, false);
      }
      PackageManager localPackageManager = mContext.getPackageManager();
      paramView = (ImageView)localView.findViewById(16908294);
      paramViewGroup = (ResolveInfo)getItem(paramInt);
      paramView.setImageDrawable(paramViewGroup.loadIcon(localPackageManager));
      ((TextView)localView.findViewById(16908310)).setText(paramViewGroup.loadLabel(localPackageManager));
      if ((mShowDefaultActivity) && (paramInt == 0) && (mHighlightDefaultActivity)) {
        localView.setActivated(true);
      } else {
        localView.setActivated(false);
      }
      return localView;
    }
    
    public int getViewTypeCount()
    {
      return 3;
    }
    
    public int measureContentWidth()
    {
      int i = mMaxActivityCount;
      mMaxActivityCount = Integer.MAX_VALUE;
      int j = 0;
      View localView = null;
      int k = 0;
      int m = View.MeasureSpec.makeMeasureSpec(0, 0);
      int n = View.MeasureSpec.makeMeasureSpec(0, 0);
      int i1 = getCount();
      while (k < i1)
      {
        localView = getView(k, localView, null);
        localView.measure(m, n);
        j = Math.max(j, localView.getMeasuredWidth());
        k++;
      }
      mMaxActivityCount = i;
      return j;
    }
    
    public void setDataModel(ActivityChooserModel paramActivityChooserModel)
    {
      ActivityChooserModel localActivityChooserModel = mAdapter.getDataModel();
      if ((localActivityChooserModel != null) && (isShown())) {
        localActivityChooserModel.unregisterObserver(mModelDataSetOberver);
      }
      mDataModel = paramActivityChooserModel;
      if ((paramActivityChooserModel != null) && (isShown())) {
        paramActivityChooserModel.registerObserver(mModelDataSetOberver);
      }
      notifyDataSetChanged();
    }
    
    public void setMaxActivityCount(int paramInt)
    {
      if (mMaxActivityCount != paramInt)
      {
        mMaxActivityCount = paramInt;
        notifyDataSetChanged();
      }
    }
    
    public void setShowDefaultActivity(boolean paramBoolean1, boolean paramBoolean2)
    {
      if ((mShowDefaultActivity != paramBoolean1) || (mHighlightDefaultActivity != paramBoolean2))
      {
        mShowDefaultActivity = paramBoolean1;
        mHighlightDefaultActivity = paramBoolean2;
        notifyDataSetChanged();
      }
    }
    
    public void setShowFooterView(boolean paramBoolean)
    {
      if (mShowFooterView != paramBoolean)
      {
        mShowFooterView = paramBoolean;
        notifyDataSetChanged();
      }
    }
  }
  
  private class Callbacks
    implements AdapterView.OnItemClickListener, View.OnClickListener, View.OnLongClickListener, PopupWindow.OnDismissListener
  {
    private Callbacks() {}
    
    private void notifyOnDismissListener()
    {
      if (mOnDismissListener != null) {
        mOnDismissListener.onDismiss();
      }
    }
    
    private void startActivity(Intent paramIntent, ResolveInfo paramResolveInfo)
    {
      try
      {
        mContext.startActivity(paramIntent);
      }
      catch (RuntimeException paramIntent)
      {
        paramIntent = paramResolveInfo.loadLabel(mContext.getPackageManager());
        paramIntent = mContext.getString(17039442, new Object[] { paramIntent });
        Log.e("ActivityChooserView", paramIntent);
        Toast.makeText(mContext, paramIntent, 0).show();
      }
    }
    
    public void onClick(View paramView)
    {
      if (paramView == mDefaultActivityButton)
      {
        dismissPopup();
        paramView = mAdapter.getDefaultActivity();
        int i = mAdapter.getDataModel().getActivityIndex(paramView);
        Intent localIntent = mAdapter.getDataModel().chooseActivity(i);
        if (localIntent != null)
        {
          localIntent.addFlags(524288);
          startActivity(localIntent, paramView);
        }
      }
      else
      {
        if (paramView != mExpandActivityOverflowButton) {
          break label115;
        }
        ActivityChooserView.access$602(ActivityChooserView.this, false);
        ActivityChooserView.this.showPopupUnchecked(mInitialActivityCount);
      }
      return;
      label115:
      throw new IllegalArgumentException();
    }
    
    public void onDismiss()
    {
      notifyOnDismissListener();
      if (mProvider != null) {
        mProvider.subUiVisibilityChanged(false);
      }
    }
    
    public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
    {
      switch (((ActivityChooserView.ActivityChooserViewAdapter)paramAdapterView.getAdapter()).getItemViewType(paramInt))
      {
      default: 
        throw new IllegalArgumentException();
      case 1: 
        ActivityChooserView.this.showPopupUnchecked(Integer.MAX_VALUE);
        break;
      case 0: 
        dismissPopup();
        if (mIsSelectingDefaultActivity)
        {
          if (paramInt > 0) {
            mAdapter.getDataModel().setDefaultActivity(paramInt);
          }
        }
        else
        {
          if (!mAdapter.getShowDefaultActivity()) {
            paramInt++;
          }
          paramAdapterView = mAdapter.getDataModel().chooseActivity(paramInt);
          if (paramAdapterView != null)
          {
            paramAdapterView.addFlags(524288);
            startActivity(paramAdapterView, mAdapter.getDataModel().getActivity(paramInt));
          }
        }
        break;
      }
    }
    
    public boolean onLongClick(View paramView)
    {
      if (paramView == mDefaultActivityButton)
      {
        if (mAdapter.getCount() > 0)
        {
          ActivityChooserView.access$602(ActivityChooserView.this, true);
          ActivityChooserView.this.showPopupUnchecked(mInitialActivityCount);
        }
        return true;
      }
      throw new IllegalArgumentException();
    }
  }
}
