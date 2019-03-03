package com.android.internal.app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.method.MovementMethod;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.view.ViewStub;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.CursorAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.android.internal.R.styleable;
import java.lang.ref.WeakReference;

public class AlertController
{
  public static final int MICRO = 1;
  private ListAdapter mAdapter;
  private int mAlertDialogLayout;
  private final View.OnClickListener mButtonHandler = new View.OnClickListener()
  {
    public void onClick(View paramAnonymousView)
    {
      if ((paramAnonymousView == mButtonPositive) && (mButtonPositiveMessage != null)) {
        paramAnonymousView = Message.obtain(mButtonPositiveMessage);
      }
      for (;;)
      {
        break;
        if ((paramAnonymousView == mButtonNegative) && (mButtonNegativeMessage != null)) {
          paramAnonymousView = Message.obtain(mButtonNegativeMessage);
        } else if ((paramAnonymousView == mButtonNeutral) && (mButtonNeutralMessage != null)) {
          paramAnonymousView = Message.obtain(mButtonNeutralMessage);
        } else {
          paramAnonymousView = null;
        }
      }
      if (paramAnonymousView != null) {
        paramAnonymousView.sendToTarget();
      }
      mHandler.obtainMessage(1, mDialogInterface).sendToTarget();
    }
  };
  private Button mButtonNegative;
  private Message mButtonNegativeMessage;
  private CharSequence mButtonNegativeText;
  private Button mButtonNeutral;
  private Message mButtonNeutralMessage;
  private CharSequence mButtonNeutralText;
  private int mButtonPanelLayoutHint = 0;
  private int mButtonPanelSideLayout;
  private Button mButtonPositive;
  private Message mButtonPositiveMessage;
  private CharSequence mButtonPositiveText;
  private int mCheckedItem = -1;
  private final Context mContext;
  private View mCustomTitleView;
  private final DialogInterface mDialogInterface;
  private boolean mDoShowMessageView = false;
  private boolean mForceInverseBackground;
  private Handler mHandler;
  private Drawable mIcon;
  private int mIconId = 0;
  private ImageView mIconView;
  private int mListItemLayout;
  private int mListLayout;
  protected ListView mListView;
  protected CharSequence mMessage;
  private Integer mMessageHyphenationFrequency;
  private MovementMethod mMessageMovementMethod;
  protected TextView mMessageView;
  private int mMultiChoiceItemLayout;
  protected ScrollView mScrollView;
  private boolean mShowTitle;
  private int mSingleChoiceItemLayout;
  private CharSequence mTitle;
  private TextView mTitleView;
  private View mView;
  private int mViewLayoutResId;
  private int mViewSpacingBottom;
  private int mViewSpacingLeft;
  private int mViewSpacingRight;
  private boolean mViewSpacingSpecified = false;
  private int mViewSpacingTop;
  protected final Window mWindow;
  
  protected AlertController(Context paramContext, DialogInterface paramDialogInterface, Window paramWindow)
  {
    mContext = paramContext;
    mDialogInterface = paramDialogInterface;
    mWindow = paramWindow;
    mHandler = new ButtonHandler(paramDialogInterface);
    paramContext = paramContext.obtainStyledAttributes(null, R.styleable.AlertDialog, 16842845, 0);
    mAlertDialogLayout = paramContext.getResourceId(10, 17367080);
    mButtonPanelSideLayout = paramContext.getResourceId(11, 0);
    mListLayout = paramContext.getResourceId(15, 17367300);
    mMultiChoiceItemLayout = paramContext.getResourceId(16, 17367059);
    mSingleChoiceItemLayout = paramContext.getResourceId(21, 17367058);
    mListItemLayout = paramContext.getResourceId(14, 17367057);
    mShowTitle = paramContext.getBoolean(20, true);
    paramContext.recycle();
    paramWindow.requestFeature(1);
  }
  
  static boolean canTextInput(View paramView)
  {
    if (paramView.onCheckIsTextEditor()) {
      return true;
    }
    if (!(paramView instanceof ViewGroup)) {
      return false;
    }
    paramView = (ViewGroup)paramView;
    int i = paramView.getChildCount();
    while (i > 0)
    {
      int j = i - 1;
      i = j;
      if (canTextInput(paramView.getChildAt(j))) {
        return true;
      }
    }
    return false;
  }
  
  private void centerButton(Button paramButton)
  {
    LinearLayout.LayoutParams localLayoutParams = (LinearLayout.LayoutParams)paramButton.getLayoutParams();
    gravity = 1;
    weight = 0.5F;
    paramButton.setLayoutParams(localLayoutParams);
    paramButton = mWindow.findViewById(16909083);
    if (paramButton != null) {
      paramButton.setVisibility(0);
    }
    paramButton = mWindow.findViewById(16909297);
    if (paramButton != null) {
      paramButton.setVisibility(0);
    }
  }
  
  public static final AlertController create(Context paramContext, DialogInterface paramDialogInterface, Window paramWindow)
  {
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(null, R.styleable.AlertDialog, 16842845, 0);
    int i = localTypedArray.getInt(12, 0);
    localTypedArray.recycle();
    if (i != 1) {
      return new AlertController(paramContext, paramDialogInterface, paramWindow);
    }
    return new MicroAlertController(paramContext, paramDialogInterface, paramWindow);
  }
  
  private static void manageScrollIndicators(View paramView1, View paramView2, View paramView3)
  {
    int i = 4;
    int j;
    if (paramView2 != null)
    {
      if (paramView1.canScrollVertically(-1)) {
        j = 0;
      } else {
        j = 4;
      }
      paramView2.setVisibility(j);
    }
    if (paramView3 != null)
    {
      j = i;
      if (paramView1.canScrollVertically(1)) {
        j = 0;
      }
      paramView3.setVisibility(j);
    }
  }
  
  private ViewGroup resolvePanel(View paramView1, View paramView2)
  {
    if (paramView1 == null)
    {
      paramView1 = paramView2;
      if ((paramView2 instanceof ViewStub)) {
        paramView1 = ((ViewStub)paramView2).inflate();
      }
      return (ViewGroup)paramView1;
    }
    if (paramView2 != null)
    {
      ViewParent localViewParent = paramView2.getParent();
      if ((localViewParent instanceof ViewGroup)) {
        ((ViewGroup)localViewParent).removeView(paramView2);
      }
    }
    paramView2 = paramView1;
    if ((paramView1 instanceof ViewStub)) {
      paramView2 = ((ViewStub)paramView1).inflate();
    }
    return (ViewGroup)paramView2;
  }
  
  private int selectContentView()
  {
    if (mButtonPanelSideLayout == 0) {
      return mAlertDialogLayout;
    }
    if (mButtonPanelLayoutHint == 1) {
      return mButtonPanelSideLayout;
    }
    return mAlertDialogLayout;
  }
  
  private void setBackground(TypedArray paramTypedArray, View paramView1, View paramView2, View paramView3, View paramView4, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
  {
    int i = 0;
    int j = 0;
    int k = 0;
    int m = 0;
    int n = 0;
    int i1 = 0;
    int i2 = 0;
    int i3 = 0;
    int i4 = 0;
    if (paramTypedArray.getBoolean(17, true))
    {
      i = 17303461;
      j = 17303475;
      k = 17303458;
      m = 17303455;
      n = 17303460;
      i1 = 17303474;
      i2 = 17303457;
      i3 = 17303454;
      i4 = 17303456;
    }
    i1 = paramTypedArray.getResourceId(5, i1);
    int i5 = paramTypedArray.getResourceId(1, j);
    i2 = paramTypedArray.getResourceId(6, i2);
    int i6 = paramTypedArray.getResourceId(2, k);
    View[] arrayOfView = new View[4];
    boolean[] arrayOfBoolean = new boolean[4];
    boolean bool = false;
    k = 0;
    if (paramBoolean1)
    {
      arrayOfView[0] = paramView1;
      arrayOfBoolean[0] = false;
      k = 0 + 1;
    }
    if (paramView2.getVisibility() == 8) {
      paramView2 = null;
    }
    arrayOfView[k] = paramView2;
    if (mListView != null) {
      paramBoolean1 = true;
    } else {
      paramBoolean1 = false;
    }
    arrayOfBoolean[k] = paramBoolean1;
    j = k + 1;
    k = j;
    if (paramBoolean2)
    {
      arrayOfView[j] = paramView3;
      arrayOfBoolean[j] = mForceInverseBackground;
      k = j + 1;
    }
    if (paramBoolean3)
    {
      arrayOfView[k] = paramView4;
      arrayOfBoolean[k] = true;
    }
    j = 0;
    paramView1 = null;
    int i7 = 0;
    paramBoolean1 = bool;
    k = i2;
    i2 = i1;
    for (i1 = i7; i1 < arrayOfView.length; i1++)
    {
      paramView2 = arrayOfView[i1];
      if (paramView2 != null)
      {
        if (paramView1 != null)
        {
          if (j == 0)
          {
            if (paramBoolean1) {
              j = i2;
            } else {
              j = i5;
            }
            paramView1.setBackgroundResource(j);
          }
          else
          {
            if (paramBoolean1) {
              j = k;
            } else {
              j = i6;
            }
            paramView1.setBackgroundResource(j);
          }
          j = 1;
        }
        paramView1 = paramView2;
        paramBoolean1 = arrayOfBoolean[i1];
      }
    }
    if (paramView1 != null) {
      if (j != 0)
      {
        k = paramTypedArray.getResourceId(7, i3);
        i2 = paramTypedArray.getResourceId(8, i4);
        j = paramTypedArray.getResourceId(3, m);
        if (paramBoolean1)
        {
          if (paramBoolean3) {
            k = i2;
          }
        }
        else {
          k = j;
        }
        paramView1.setBackgroundResource(k);
      }
      else
      {
        k = paramTypedArray.getResourceId(4, n);
        i2 = paramTypedArray.getResourceId(0, i);
        if (!paramBoolean1) {
          k = i2;
        }
        paramView1.setBackgroundResource(k);
      }
    }
    paramView1 = mListView;
    if ((paramView1 != null) && (mAdapter != null))
    {
      paramView1.setAdapter(mAdapter);
      k = mCheckedItem;
      if (k > -1)
      {
        paramView1.setItemChecked(k, true);
        paramView1.setSelectionFromTop(k, paramTypedArray.getDimensionPixelSize(19, 0));
      }
    }
  }
  
  private void setupCustomContent(ViewGroup paramViewGroup)
  {
    View localView = mView;
    int i = 0;
    if (localView != null) {
      localView = mView;
    } else if (mViewLayoutResId != 0) {
      localView = LayoutInflater.from(mContext).inflate(mViewLayoutResId, paramViewGroup, false);
    } else {
      localView = null;
    }
    if (localView != null) {
      i = 1;
    }
    if ((i == 0) || (!canTextInput(localView))) {
      mWindow.setFlags(131072, 131072);
    }
    if (i != 0)
    {
      FrameLayout localFrameLayout = (FrameLayout)mWindow.findViewById(16908331);
      localFrameLayout.addView(localView, new ViewGroup.LayoutParams(-1, -1));
      if (mViewSpacingSpecified) {
        localFrameLayout.setPadding(mViewSpacingLeft, mViewSpacingTop, mViewSpacingRight, mViewSpacingBottom);
      }
      if (mListView != null) {
        getLayoutParamsweight = 0.0F;
      }
    }
    else
    {
      paramViewGroup.setVisibility(8);
    }
  }
  
  private void setupView()
  {
    Object localObject1 = mWindow.findViewById(16909222);
    Object localObject2 = ((View)localObject1).findViewById(16909491);
    View localView1 = ((View)localObject1).findViewById(16908873);
    Object localObject3 = ((View)localObject1).findViewById(16908826);
    localObject1 = (ViewGroup)((View)localObject1).findViewById(16908880);
    setupCustomContent((ViewGroup)localObject1);
    View localView2 = ((ViewGroup)localObject1).findViewById(16909491);
    Object localObject4 = ((ViewGroup)localObject1).findViewById(16908873);
    Object localObject5 = ((ViewGroup)localObject1).findViewById(16908826);
    localObject2 = resolvePanel(localView2, (View)localObject2);
    localObject4 = resolvePanel((View)localObject4, localView1);
    localObject5 = resolvePanel((View)localObject5, (View)localObject3);
    setupContent((ViewGroup)localObject4);
    setupButtons((ViewGroup)localObject5);
    setupTitle((ViewGroup)localObject2);
    boolean bool1;
    if ((localObject1 != null) && (((ViewGroup)localObject1).getVisibility() != 8)) {
      bool1 = true;
    } else {
      bool1 = false;
    }
    boolean bool2;
    if ((localObject2 != null) && (((ViewGroup)localObject2).getVisibility() != 8)) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    boolean bool3;
    if ((localObject5 != null) && (((ViewGroup)localObject5).getVisibility() != 8)) {
      bool3 = true;
    } else {
      bool3 = false;
    }
    if (!bool3)
    {
      if (localObject4 != null)
      {
        localObject3 = ((ViewGroup)localObject4).findViewById(16909457);
        if (localObject3 != null) {
          ((View)localObject3).setVisibility(0);
        }
      }
      mWindow.setCloseOnTouchOutsideIfNotSet(true);
    }
    if (bool2)
    {
      if (mScrollView != null) {
        mScrollView.setClipToPadding(true);
      }
      localView1 = null;
      if ((mMessage == null) && (mListView == null) && (!bool1))
      {
        localObject3 = ((ViewGroup)localObject2).findViewById(16909477);
      }
      else
      {
        if (!bool1) {
          localView1 = ((ViewGroup)localObject2).findViewById(16909476);
        }
        localObject3 = localView1;
        if (localView1 == null) {
          localObject3 = ((ViewGroup)localObject2).findViewById(16909475);
        }
      }
      if (localObject3 != null) {
        ((View)localObject3).setVisibility(0);
      }
    }
    else if (localObject4 != null)
    {
      localObject3 = ((ViewGroup)localObject4).findViewById(16909458);
      if (localObject3 != null) {
        ((View)localObject3).setVisibility(0);
      }
    }
    if ((mListView instanceof RecycleListView)) {
      ((RecycleListView)mListView).setHasDecor(bool2, bool3);
    }
    if (!bool1)
    {
      if (mListView != null) {
        localObject3 = mListView;
      } else {
        localObject3 = mScrollView;
      }
      if (localObject3 != null)
      {
        int i;
        if (bool2) {
          i = 1;
        } else {
          i = 0;
        }
        int j;
        if (bool3) {
          j = 2;
        } else {
          j = 0;
        }
        ((View)localObject3).setScrollIndicators(i | j, 3);
      }
    }
    localObject3 = mContext.obtainStyledAttributes(null, R.styleable.AlertDialog, 16842845, 0);
    setBackground((TypedArray)localObject3, (View)localObject2, (View)localObject4, (View)localObject1, (View)localObject5, bool2, bool1, bool3);
    ((TypedArray)localObject3).recycle();
    mDoShowMessageView = false;
  }
  
  private static boolean shouldCenterSingleButton(Context paramContext)
  {
    TypedValue localTypedValue = new TypedValue();
    paramContext = paramContext.getTheme();
    boolean bool = true;
    paramContext.resolveAttribute(17891339, localTypedValue, true);
    if (data == 0) {
      bool = false;
    }
    return bool;
  }
  
  public Button getButton(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return null;
    case -1: 
      return mButtonPositive;
    case -2: 
      return mButtonNegative;
    }
    return mButtonNeutral;
  }
  
  public int getIconAttributeResId(int paramInt)
  {
    TypedValue localTypedValue = new TypedValue();
    mContext.getTheme().resolveAttribute(paramInt, localTypedValue, true);
    return resourceId;
  }
  
  public ListView getListView()
  {
    return mListView;
  }
  
  public ScrollView getScrollView()
  {
    return mScrollView;
  }
  
  public void installContent()
  {
    int i = selectContentView();
    mWindow.setContentView(i);
    setupView();
  }
  
  public void installContent(AlertParams paramAlertParams)
  {
    paramAlertParams.apply(this);
    installContent();
  }
  
  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    boolean bool;
    if ((mScrollView != null) && (mScrollView.executeKeyEvent(paramKeyEvent))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean onKeyUp(int paramInt, KeyEvent paramKeyEvent)
  {
    boolean bool;
    if ((mScrollView != null) && (mScrollView.executeKeyEvent(paramKeyEvent))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void setButton(int paramInt, CharSequence paramCharSequence, DialogInterface.OnClickListener paramOnClickListener, Message paramMessage)
  {
    Message localMessage = paramMessage;
    if (paramMessage == null)
    {
      localMessage = paramMessage;
      if (paramOnClickListener != null) {
        localMessage = mHandler.obtainMessage(paramInt, paramOnClickListener);
      }
    }
    switch (paramInt)
    {
    default: 
      throw new IllegalArgumentException("Button does not exist");
    case -1: 
      mButtonPositiveText = paramCharSequence;
      mButtonPositiveMessage = localMessage;
      break;
    case -2: 
      mButtonNegativeText = paramCharSequence;
      mButtonNegativeMessage = localMessage;
      break;
    case -3: 
      mButtonNeutralText = paramCharSequence;
      mButtonNeutralMessage = localMessage;
    }
  }
  
  public void setButtonPanelLayoutHint(int paramInt)
  {
    mButtonPanelLayoutHint = paramInt;
  }
  
  public void setCustomTitle(View paramView)
  {
    mCustomTitleView = paramView;
  }
  
  public void setDoShowMessage(boolean paramBoolean)
  {
    mDoShowMessageView = paramBoolean;
  }
  
  public void setIcon(int paramInt)
  {
    mIcon = null;
    mIconId = paramInt;
    if (mIconView != null) {
      if (paramInt != 0)
      {
        mIconView.setVisibility(0);
        mIconView.setImageResource(mIconId);
      }
      else
      {
        mIconView.setVisibility(8);
      }
    }
  }
  
  public void setIcon(Drawable paramDrawable)
  {
    mIcon = paramDrawable;
    mIconId = 0;
    if (mIconView != null) {
      if (paramDrawable != null)
      {
        mIconView.setVisibility(0);
        mIconView.setImageDrawable(paramDrawable);
      }
      else
      {
        mIconView.setVisibility(8);
      }
    }
  }
  
  public void setInverseBackgroundForced(boolean paramBoolean)
  {
    mForceInverseBackground = paramBoolean;
  }
  
  public void setMessage(CharSequence paramCharSequence)
  {
    mMessage = paramCharSequence;
    if (mMessageView != null) {
      mMessageView.setText(paramCharSequence);
    }
  }
  
  public void setMessageHyphenationFrequency(int paramInt)
  {
    mMessageHyphenationFrequency = Integer.valueOf(paramInt);
    if (mMessageView != null) {
      mMessageView.setHyphenationFrequency(paramInt);
    }
  }
  
  public void setMessageMovementMethod(MovementMethod paramMovementMethod)
  {
    mMessageMovementMethod = paramMovementMethod;
    if (mMessageView != null) {
      mMessageView.setMovementMethod(paramMovementMethod);
    }
  }
  
  public void setTitle(CharSequence paramCharSequence)
  {
    mTitle = paramCharSequence;
    if (mTitleView != null) {
      mTitleView.setText(paramCharSequence);
    }
  }
  
  public void setView(int paramInt)
  {
    mView = null;
    mViewLayoutResId = paramInt;
    mViewSpacingSpecified = false;
  }
  
  public void setView(View paramView)
  {
    mView = paramView;
    mViewLayoutResId = 0;
    mViewSpacingSpecified = false;
  }
  
  public void setView(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    mView = paramView;
    mViewLayoutResId = 0;
    mViewSpacingSpecified = true;
    mViewSpacingLeft = paramInt1;
    mViewSpacingTop = paramInt2;
    mViewSpacingRight = paramInt3;
    mViewSpacingBottom = paramInt4;
  }
  
  protected void setupButtons(ViewGroup paramViewGroup)
  {
    int i = 0;
    mButtonPositive = ((Button)paramViewGroup.findViewById(16908313));
    mButtonPositive.setOnClickListener(mButtonHandler);
    boolean bool = TextUtils.isEmpty(mButtonPositiveText);
    int j = 0;
    if (bool)
    {
      mButtonPositive.setVisibility(8);
    }
    else
    {
      mButtonPositive.setText(mButtonPositiveText);
      mButtonPositive.setVisibility(0);
      i = 0x0 | 0x1;
    }
    mButtonNegative = ((Button)paramViewGroup.findViewById(16908314));
    mButtonNegative.setOnClickListener(mButtonHandler);
    if (TextUtils.isEmpty(mButtonNegativeText))
    {
      mButtonNegative.setVisibility(8);
    }
    else
    {
      mButtonNegative.setText(mButtonNegativeText);
      mButtonNegative.setVisibility(0);
      i |= 0x2;
    }
    mButtonNeutral = ((Button)paramViewGroup.findViewById(16908315));
    mButtonNeutral.setOnClickListener(mButtonHandler);
    if (TextUtils.isEmpty(mButtonNeutralText))
    {
      mButtonNeutral.setVisibility(8);
    }
    else
    {
      mButtonNeutral.setText(mButtonNeutralText);
      mButtonNeutral.setVisibility(0);
      i |= 0x4;
    }
    if (shouldCenterSingleButton(mContext)) {
      if (i == 1) {
        centerButton(mButtonPositive);
      } else if (i == 2) {
        centerButton(mButtonNegative);
      } else if (i == 4) {
        centerButton(mButtonNeutral);
      }
    }
    if (i != 0) {
      j = 1;
    }
    if (j == 0) {
      paramViewGroup.setVisibility(8);
    }
  }
  
  protected void setupContent(ViewGroup paramViewGroup)
  {
    mScrollView = ((ScrollView)paramViewGroup.findViewById(16909316));
    mScrollView.setFocusable(false);
    mMessageView = ((TextView)paramViewGroup.findViewById(16908299));
    if (mMessageView == null) {
      return;
    }
    if ((mDoShowMessageView) && (mMessage != null))
    {
      mMessageView.setText(mMessage);
      if (mListView != null)
      {
        paramViewGroup.addView(mListView, new LinearLayout.LayoutParams(-1, -1));
        paramViewGroup.setLayoutParams(new LinearLayout.LayoutParams(-1, 0, 1.0F));
        mScrollView.setVisibility(8);
      }
      else
      {
        mScrollView.setVisibility(0);
      }
    }
    else if (mMessage != null)
    {
      mMessageView.setText(mMessage);
      if (mMessageMovementMethod != null) {
        mMessageView.setMovementMethod(mMessageMovementMethod);
      }
      if (mMessageHyphenationFrequency != null) {
        mMessageView.setHyphenationFrequency(mMessageHyphenationFrequency.intValue());
      }
    }
    else
    {
      mMessageView.setVisibility(8);
      mScrollView.removeView(mMessageView);
      if (mListView != null)
      {
        paramViewGroup = (ViewGroup)mScrollView.getParent();
        int i = paramViewGroup.indexOfChild(mScrollView);
        paramViewGroup.removeViewAt(i);
        paramViewGroup.addView(mListView, i, new ViewGroup.LayoutParams(-1, -1));
        paramViewGroup.setLayoutParams(new LinearLayout.LayoutParams(-1, 0, 1.0F));
      }
      else
      {
        paramViewGroup.setVisibility(8);
      }
    }
  }
  
  protected void setupTitle(ViewGroup paramViewGroup)
  {
    if ((mCustomTitleView != null) && (mShowTitle))
    {
      ViewGroup.LayoutParams localLayoutParams = new ViewGroup.LayoutParams(-1, -2);
      paramViewGroup.addView(mCustomTitleView, 0, localLayoutParams);
      mWindow.findViewById(16909481).setVisibility(8);
    }
    else
    {
      mIconView = ((ImageView)mWindow.findViewById(16908294));
      if (((TextUtils.isEmpty(mTitle) ^ true)) && (mShowTitle))
      {
        mTitleView = ((TextView)mWindow.findViewById(16908726));
        mTitleView.setText(mTitle);
        if (mIconId != 0)
        {
          mIconView.setImageResource(mIconId);
        }
        else if (mIcon != null)
        {
          mIconView.setImageDrawable(mIcon);
        }
        else
        {
          mTitleView.setPadding(mIconView.getPaddingLeft(), mIconView.getPaddingTop(), mIconView.getPaddingRight(), mIconView.getPaddingBottom());
          mIconView.setVisibility(8);
        }
      }
      else
      {
        mWindow.findViewById(16909481).setVisibility(8);
        mIconView.setVisibility(8);
        paramViewGroup.setVisibility(8);
      }
    }
  }
  
  public static class AlertParams
  {
    public ListAdapter mAdapter;
    public boolean mCancelable;
    public int mCheckedItem = -1;
    public boolean[] mCheckedItems;
    public final Context mContext;
    public Cursor mCursor;
    public View mCustomTitleView;
    public boolean mForceInverseBackground;
    public Drawable mIcon;
    public int mIconAttrId = 0;
    public int mIconId = 0;
    public final LayoutInflater mInflater;
    public String mIsCheckedColumn;
    public boolean mIsMultiChoice;
    public boolean mIsSingleChoice;
    public CharSequence[] mItems;
    public String mLabelColumn;
    public CharSequence mMessage;
    public DialogInterface.OnClickListener mNegativeButtonListener;
    public CharSequence mNegativeButtonText;
    public DialogInterface.OnClickListener mNeutralButtonListener;
    public CharSequence mNeutralButtonText;
    public DialogInterface.OnCancelListener mOnCancelListener;
    public DialogInterface.OnMultiChoiceClickListener mOnCheckboxClickListener;
    public DialogInterface.OnClickListener mOnClickListener;
    public DialogInterface.OnDismissListener mOnDismissListener;
    public AdapterView.OnItemSelectedListener mOnItemSelectedListener;
    public DialogInterface.OnKeyListener mOnKeyListener;
    public OnPrepareListViewListener mOnPrepareListViewListener;
    public DialogInterface.OnClickListener mPositiveButtonListener;
    public CharSequence mPositiveButtonText;
    public boolean mRecycleOnMeasure = true;
    public CharSequence mTitle;
    public View mView;
    public int mViewLayoutResId;
    public int mViewSpacingBottom;
    public int mViewSpacingLeft;
    public int mViewSpacingRight;
    public boolean mViewSpacingSpecified = false;
    public int mViewSpacingTop;
    
    public AlertParams(Context paramContext)
    {
      mContext = paramContext;
      mCancelable = true;
      mInflater = ((LayoutInflater)paramContext.getSystemService("layout_inflater"));
    }
    
    private void createListView(final AlertController paramAlertController)
    {
      final AlertController.RecycleListView localRecycleListView = (AlertController.RecycleListView)mInflater.inflate(mListLayout, null);
      Object localObject;
      if (mIsMultiChoice)
      {
        if (mCursor == null) {}
        for (localObject = new ArrayAdapter(mContext, mMultiChoiceItemLayout, 16908308, mItems)
            {
              public View getView(int paramAnonymousInt, View paramAnonymousView, ViewGroup paramAnonymousViewGroup)
              {
                paramAnonymousView = super.getView(paramAnonymousInt, paramAnonymousView, paramAnonymousViewGroup);
                if ((mCheckedItems != null) && (mCheckedItems[paramAnonymousInt] != 0)) {
                  localRecycleListView.setItemChecked(paramAnonymousInt, true);
                }
                return paramAnonymousView;
              }
            };; localObject = new CursorAdapter(mContext, mCursor, false)
            {
              private final int mIsCheckedIndex;
              private final int mLabelIndex;
              
              public void bindView(View paramAnonymousView, Context paramAnonymousContext, Cursor paramAnonymousCursor)
              {
                ((CheckedTextView)paramAnonymousView.findViewById(16908308)).setText(paramAnonymousCursor.getString(mLabelIndex));
                paramAnonymousView = localRecycleListView;
                int i = paramAnonymousCursor.getPosition();
                int j = paramAnonymousCursor.getInt(mIsCheckedIndex);
                boolean bool = true;
                if (j != 1) {
                  bool = false;
                }
                paramAnonymousView.setItemChecked(i, bool);
              }
              
              public View newView(Context paramAnonymousContext, Cursor paramAnonymousCursor, ViewGroup paramAnonymousViewGroup)
              {
                return mInflater.inflate(paramAlertControllermMultiChoiceItemLayout, paramAnonymousViewGroup, false);
              }
            }) {
          break;
        }
      }
      int i;
      if (mIsSingleChoice) {
        i = mSingleChoiceItemLayout;
      } else {
        i = mListItemLayout;
      }
      if (mCursor != null) {
        localObject = new SimpleCursorAdapter(mContext, i, mCursor, new String[] { mLabelColumn }, new int[] { 16908308 });
      }
      for (;;)
      {
        break;
        if (mAdapter != null) {
          localObject = mAdapter;
        } else {
          localObject = new AlertController.CheckedItemAdapter(mContext, i, 16908308, mItems);
        }
      }
      if (mOnPrepareListViewListener != null) {
        mOnPrepareListViewListener.onPrepareListView(localRecycleListView);
      }
      AlertController.access$1202(paramAlertController, (ListAdapter)localObject);
      AlertController.access$1302(paramAlertController, mCheckedItem);
      if (mOnClickListener != null) {
        localRecycleListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
          public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
          {
            mOnClickListener.onClick(paramAlertControllermDialogInterface, paramAnonymousInt);
            if (!mIsSingleChoice) {
              paramAlertControllermDialogInterface.dismiss();
            }
          }
        });
      } else if (mOnCheckboxClickListener != null) {
        localRecycleListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
          public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
          {
            if (mCheckedItems != null) {
              mCheckedItems[paramAnonymousInt] = localRecycleListView.isItemChecked(paramAnonymousInt);
            }
            mOnCheckboxClickListener.onClick(paramAlertControllermDialogInterface, paramAnonymousInt, localRecycleListView.isItemChecked(paramAnonymousInt));
          }
        });
      }
      if (mOnItemSelectedListener != null) {
        localRecycleListView.setOnItemSelectedListener(mOnItemSelectedListener);
      }
      if (mIsSingleChoice) {
        localRecycleListView.setChoiceMode(1);
      } else if (mIsMultiChoice) {
        localRecycleListView.setChoiceMode(2);
      }
      mRecycleOnMeasure = mRecycleOnMeasure;
      mListView = localRecycleListView;
    }
    
    public void apply(AlertController paramAlertController)
    {
      if (mCustomTitleView != null)
      {
        paramAlertController.setCustomTitle(mCustomTitleView);
      }
      else
      {
        if (mTitle != null) {
          paramAlertController.setTitle(mTitle);
        }
        if (mIcon != null) {
          paramAlertController.setIcon(mIcon);
        }
        if (mIconId != 0) {
          paramAlertController.setIcon(mIconId);
        }
        if (mIconAttrId != 0) {
          paramAlertController.setIcon(paramAlertController.getIconAttributeResId(mIconAttrId));
        }
      }
      if (mMessage != null) {
        paramAlertController.setMessage(mMessage);
      }
      if (mPositiveButtonText != null) {
        paramAlertController.setButton(-1, mPositiveButtonText, mPositiveButtonListener, null);
      }
      if (mNegativeButtonText != null) {
        paramAlertController.setButton(-2, mNegativeButtonText, mNegativeButtonListener, null);
      }
      if (mNeutralButtonText != null) {
        paramAlertController.setButton(-3, mNeutralButtonText, mNeutralButtonListener, null);
      }
      if (mForceInverseBackground) {
        paramAlertController.setInverseBackgroundForced(true);
      }
      if ((mItems != null) || (mCursor != null) || (mAdapter != null)) {
        createListView(paramAlertController);
      }
      if (mView != null)
      {
        if (mViewSpacingSpecified) {
          paramAlertController.setView(mView, mViewSpacingLeft, mViewSpacingTop, mViewSpacingRight, mViewSpacingBottom);
        } else {
          paramAlertController.setView(mView);
        }
      }
      else if (mViewLayoutResId != 0) {
        paramAlertController.setView(mViewLayoutResId);
      }
    }
    
    public static abstract interface OnPrepareListViewListener
    {
      public abstract void onPrepareListView(ListView paramListView);
    }
  }
  
  private static final class ButtonHandler
    extends Handler
  {
    private static final int MSG_DISMISS_DIALOG = 1;
    private WeakReference<DialogInterface> mDialog;
    
    public ButtonHandler(DialogInterface paramDialogInterface)
    {
      mDialog = new WeakReference(paramDialogInterface);
    }
    
    public void handleMessage(Message paramMessage)
    {
      int i = what;
      if (i != 1) {
        switch (i)
        {
        default: 
          break;
        case -3: 
        case -2: 
        case -1: 
          ((DialogInterface.OnClickListener)obj).onClick((DialogInterface)mDialog.get(), what);
          break;
        }
      } else {
        ((DialogInterface)obj).dismiss();
      }
    }
  }
  
  private static class CheckedItemAdapter
    extends ArrayAdapter<CharSequence>
  {
    public CheckedItemAdapter(Context paramContext, int paramInt1, int paramInt2, CharSequence[] paramArrayOfCharSequence)
    {
      super(paramInt1, paramInt2, paramArrayOfCharSequence);
    }
    
    public long getItemId(int paramInt)
    {
      return paramInt;
    }
    
    public boolean hasStableIds()
    {
      return true;
    }
  }
  
  public static class RecycleListView
    extends ListView
  {
    private final int mPaddingBottomNoButtons;
    private final int mPaddingTopNoTitle;
    boolean mRecycleOnMeasure = true;
    
    public RecycleListView(Context paramContext)
    {
      this(paramContext, null);
    }
    
    public RecycleListView(Context paramContext, AttributeSet paramAttributeSet)
    {
      super(paramAttributeSet);
      paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.RecycleListView);
      mPaddingBottomNoButtons = paramContext.getDimensionPixelOffset(0, -1);
      mPaddingTopNoTitle = paramContext.getDimensionPixelOffset(1, -1);
    }
    
    protected boolean recycleOnMeasure()
    {
      return mRecycleOnMeasure;
    }
    
    public void setHasDecor(boolean paramBoolean1, boolean paramBoolean2)
    {
      if ((!paramBoolean2) || (!paramBoolean1))
      {
        int i = getPaddingLeft();
        int j;
        if (paramBoolean1) {
          j = getPaddingTop();
        } else {
          j = mPaddingTopNoTitle;
        }
        int k = getPaddingRight();
        int m;
        if (paramBoolean2) {
          m = getPaddingBottom();
        } else {
          m = mPaddingBottomNoButtons;
        }
        setPadding(i, j, k, m);
      }
    }
  }
}
