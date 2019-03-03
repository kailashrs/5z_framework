package android.preference;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.AbsSavedState;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.internal.R.styleable;
import com.android.internal.util.CharSequences;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Preference
  implements Comparable<Preference>
{
  public static final int DEFAULT_ORDER = Integer.MAX_VALUE;
  private boolean mBaseMethodCalled;
  private Context mContext;
  private Object mDefaultValue;
  private String mDependencyKey;
  private boolean mDependencyMet = true;
  private List<Preference> mDependents;
  private boolean mEnabled = true;
  private Bundle mExtras;
  private String mFragment;
  private boolean mHasSingleLineTitleAttr;
  private Drawable mIcon;
  private int mIconResId;
  private boolean mIconSpaceReserved;
  private long mId;
  private Intent mIntent;
  private String mKey;
  private int mLayoutResId = 17367246;
  private OnPreferenceChangeInternalListener mListener;
  private OnPreferenceChangeListener mOnChangeListener;
  private OnPreferenceClickListener mOnClickListener;
  private int mOrder = Integer.MAX_VALUE;
  private boolean mParentDependencyMet = true;
  private PreferenceGroup mParentGroup;
  private boolean mPersistent = true;
  private PreferenceDataStore mPreferenceDataStore;
  private PreferenceManager mPreferenceManager;
  private boolean mRecycleEnabled = true;
  private boolean mRequiresKey;
  private boolean mSelectable = true;
  private boolean mShouldDisableView = true;
  private boolean mSingleLineTitle = true;
  private CharSequence mSummary;
  private CharSequence mTitle;
  private int mTitleRes;
  private int mWidgetLayoutResId;
  
  public Preference(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public Preference(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 16842894);
  }
  
  public Preference(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public Preference(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    mContext = paramContext;
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.Preference, paramInt1, paramInt2);
    for (paramInt1 = paramContext.getIndexCount() - 1; paramInt1 >= 0; paramInt1--)
    {
      paramInt2 = paramContext.getIndex(paramInt1);
      switch (paramInt2)
      {
      default: 
        break;
      case 16: 
        mIconSpaceReserved = paramContext.getBoolean(paramInt2, mIconSpaceReserved);
        break;
      case 15: 
        mSingleLineTitle = paramContext.getBoolean(paramInt2, mSingleLineTitle);
        mHasSingleLineTitleAttr = true;
        break;
      case 14: 
        mRecycleEnabled = paramContext.getBoolean(paramInt2, mRecycleEnabled);
        break;
      case 13: 
        mFragment = paramContext.getString(paramInt2);
        break;
      case 12: 
        mShouldDisableView = paramContext.getBoolean(paramInt2, mShouldDisableView);
        break;
      case 11: 
        mDefaultValue = onGetDefaultValue(paramContext, paramInt2);
        break;
      case 10: 
        mDependencyKey = paramContext.getString(paramInt2);
        break;
      case 9: 
        mWidgetLayoutResId = paramContext.getResourceId(paramInt2, mWidgetLayoutResId);
        break;
      case 8: 
        mOrder = paramContext.getInt(paramInt2, mOrder);
        break;
      case 7: 
        mSummary = paramContext.getText(paramInt2);
        break;
      case 6: 
        mKey = paramContext.getString(paramInt2);
        break;
      case 5: 
        mSelectable = paramContext.getBoolean(paramInt2, true);
        break;
      case 4: 
        mTitleRes = paramContext.getResourceId(paramInt2, 0);
        mTitle = paramContext.getText(paramInt2);
        break;
      case 3: 
        mLayoutResId = paramContext.getResourceId(paramInt2, mLayoutResId);
        break;
      case 2: 
        mEnabled = paramContext.getBoolean(paramInt2, true);
        break;
      case 1: 
        mPersistent = paramContext.getBoolean(paramInt2, mPersistent);
        break;
      case 0: 
        mIconResId = paramContext.getResourceId(paramInt2, 0);
      }
    }
    paramContext.recycle();
  }
  
  private void dispatchSetInitialValue()
  {
    if (getPreferenceDataStore() != null)
    {
      onSetInitialValue(true, mDefaultValue);
      return;
    }
    if ((shouldPersist()) && (getSharedPreferences().contains(mKey))) {
      onSetInitialValue(true, null);
    } else if (mDefaultValue != null) {
      onSetInitialValue(false, mDefaultValue);
    }
  }
  
  private void registerDependency()
  {
    if (TextUtils.isEmpty(mDependencyKey)) {
      return;
    }
    Object localObject = findPreferenceInHierarchy(mDependencyKey);
    if (localObject != null)
    {
      ((Preference)localObject).registerDependent(this);
      return;
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Dependency \"");
    ((StringBuilder)localObject).append(mDependencyKey);
    ((StringBuilder)localObject).append("\" not found for preference \"");
    ((StringBuilder)localObject).append(mKey);
    ((StringBuilder)localObject).append("\" (title: \"");
    ((StringBuilder)localObject).append(mTitle);
    ((StringBuilder)localObject).append("\"");
    throw new IllegalStateException(((StringBuilder)localObject).toString());
  }
  
  private void registerDependent(Preference paramPreference)
  {
    if (mDependents == null) {
      mDependents = new ArrayList();
    }
    mDependents.add(paramPreference);
    paramPreference.onDependencyChanged(this, shouldDisableDependents());
  }
  
  private void setEnabledStateOnViews(View paramView, boolean paramBoolean)
  {
    paramView.setEnabled(paramBoolean);
    if ((paramView instanceof ViewGroup))
    {
      paramView = (ViewGroup)paramView;
      for (int i = paramView.getChildCount() - 1; i >= 0; i--) {
        setEnabledStateOnViews(paramView.getChildAt(i), paramBoolean);
      }
    }
  }
  
  private void tryCommit(SharedPreferences.Editor paramEditor)
  {
    if (mPreferenceManager.shouldCommit()) {
      try
      {
        paramEditor.apply();
      }
      catch (AbstractMethodError localAbstractMethodError)
      {
        paramEditor.commit();
      }
    }
  }
  
  private void unregisterDependency()
  {
    if (mDependencyKey != null)
    {
      Preference localPreference = findPreferenceInHierarchy(mDependencyKey);
      if (localPreference != null) {
        localPreference.unregisterDependent(this);
      }
    }
  }
  
  private void unregisterDependent(Preference paramPreference)
  {
    if (mDependents != null) {
      mDependents.remove(paramPreference);
    }
  }
  
  void assignParent(PreferenceGroup paramPreferenceGroup)
  {
    mParentGroup = paramPreferenceGroup;
  }
  
  protected boolean callChangeListener(Object paramObject)
  {
    boolean bool;
    if ((mOnChangeListener != null) && (!mOnChangeListener.onPreferenceChange(this, paramObject))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public int compareTo(Preference paramPreference)
  {
    if (mOrder != mOrder) {
      return mOrder - mOrder;
    }
    if (mTitle == mTitle) {
      return 0;
    }
    if (mTitle == null) {
      return 1;
    }
    if (mTitle == null) {
      return -1;
    }
    return CharSequences.compareToIgnoreCase(mTitle, mTitle);
  }
  
  void dispatchRestoreInstanceState(Bundle paramBundle)
  {
    if (hasKey())
    {
      paramBundle = paramBundle.getParcelable(mKey);
      if (paramBundle != null)
      {
        mBaseMethodCalled = false;
        onRestoreInstanceState(paramBundle);
        if (!mBaseMethodCalled) {
          throw new IllegalStateException("Derived class did not call super.onRestoreInstanceState()");
        }
      }
    }
  }
  
  void dispatchSaveInstanceState(Bundle paramBundle)
  {
    if (hasKey())
    {
      mBaseMethodCalled = false;
      Parcelable localParcelable = onSaveInstanceState();
      if (mBaseMethodCalled)
      {
        if (localParcelable != null) {
          paramBundle.putParcelable(mKey, localParcelable);
        }
      }
      else {
        throw new IllegalStateException("Derived class did not call super.onSaveInstanceState()");
      }
    }
  }
  
  protected Preference findPreferenceInHierarchy(String paramString)
  {
    if ((!TextUtils.isEmpty(paramString)) && (mPreferenceManager != null)) {
      return mPreferenceManager.findPreference(paramString);
    }
    return null;
  }
  
  public Context getContext()
  {
    return mContext;
  }
  
  public String getDependency()
  {
    return mDependencyKey;
  }
  
  public SharedPreferences.Editor getEditor()
  {
    if ((mPreferenceManager != null) && (getPreferenceDataStore() == null)) {
      return mPreferenceManager.getEditor();
    }
    return null;
  }
  
  public Bundle getExtras()
  {
    if (mExtras == null) {
      mExtras = new Bundle();
    }
    return mExtras;
  }
  
  StringBuilder getFilterableStringBuilder()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    CharSequence localCharSequence = getTitle();
    if (!TextUtils.isEmpty(localCharSequence))
    {
      localStringBuilder.append(localCharSequence);
      localStringBuilder.append(' ');
    }
    localCharSequence = getSummary();
    if (!TextUtils.isEmpty(localCharSequence))
    {
      localStringBuilder.append(localCharSequence);
      localStringBuilder.append(' ');
    }
    if (localStringBuilder.length() > 0) {
      localStringBuilder.setLength(localStringBuilder.length() - 1);
    }
    return localStringBuilder;
  }
  
  public String getFragment()
  {
    return mFragment;
  }
  
  public Drawable getIcon()
  {
    if ((mIcon == null) && (mIconResId != 0)) {
      mIcon = getContext().getDrawable(mIconResId);
    }
    return mIcon;
  }
  
  long getId()
  {
    return mId;
  }
  
  public Intent getIntent()
  {
    return mIntent;
  }
  
  public String getKey()
  {
    return mKey;
  }
  
  public int getLayoutResource()
  {
    return mLayoutResId;
  }
  
  public OnPreferenceChangeListener getOnPreferenceChangeListener()
  {
    return mOnChangeListener;
  }
  
  public OnPreferenceClickListener getOnPreferenceClickListener()
  {
    return mOnClickListener;
  }
  
  public int getOrder()
  {
    return mOrder;
  }
  
  public PreferenceGroup getParent()
  {
    return mParentGroup;
  }
  
  protected boolean getPersistedBoolean(boolean paramBoolean)
  {
    if (!shouldPersist()) {
      return paramBoolean;
    }
    PreferenceDataStore localPreferenceDataStore = getPreferenceDataStore();
    if (localPreferenceDataStore != null) {
      return localPreferenceDataStore.getBoolean(mKey, paramBoolean);
    }
    return mPreferenceManager.getSharedPreferences().getBoolean(mKey, paramBoolean);
  }
  
  protected float getPersistedFloat(float paramFloat)
  {
    if (!shouldPersist()) {
      return paramFloat;
    }
    PreferenceDataStore localPreferenceDataStore = getPreferenceDataStore();
    if (localPreferenceDataStore != null) {
      return localPreferenceDataStore.getFloat(mKey, paramFloat);
    }
    return mPreferenceManager.getSharedPreferences().getFloat(mKey, paramFloat);
  }
  
  protected int getPersistedInt(int paramInt)
  {
    if (!shouldPersist()) {
      return paramInt;
    }
    PreferenceDataStore localPreferenceDataStore = getPreferenceDataStore();
    if (localPreferenceDataStore != null) {
      return localPreferenceDataStore.getInt(mKey, paramInt);
    }
    return mPreferenceManager.getSharedPreferences().getInt(mKey, paramInt);
  }
  
  protected long getPersistedLong(long paramLong)
  {
    if (!shouldPersist()) {
      return paramLong;
    }
    PreferenceDataStore localPreferenceDataStore = getPreferenceDataStore();
    if (localPreferenceDataStore != null) {
      return localPreferenceDataStore.getLong(mKey, paramLong);
    }
    return mPreferenceManager.getSharedPreferences().getLong(mKey, paramLong);
  }
  
  protected String getPersistedString(String paramString)
  {
    if (!shouldPersist()) {
      return paramString;
    }
    PreferenceDataStore localPreferenceDataStore = getPreferenceDataStore();
    if (localPreferenceDataStore != null) {
      return localPreferenceDataStore.getString(mKey, paramString);
    }
    return mPreferenceManager.getSharedPreferences().getString(mKey, paramString);
  }
  
  public Set<String> getPersistedStringSet(Set<String> paramSet)
  {
    if (!shouldPersist()) {
      return paramSet;
    }
    PreferenceDataStore localPreferenceDataStore = getPreferenceDataStore();
    if (localPreferenceDataStore != null) {
      return localPreferenceDataStore.getStringSet(mKey, paramSet);
    }
    return mPreferenceManager.getSharedPreferences().getStringSet(mKey, paramSet);
  }
  
  public PreferenceDataStore getPreferenceDataStore()
  {
    if (mPreferenceDataStore != null) {
      return mPreferenceDataStore;
    }
    if (mPreferenceManager != null) {
      return mPreferenceManager.getPreferenceDataStore();
    }
    return null;
  }
  
  public PreferenceManager getPreferenceManager()
  {
    return mPreferenceManager;
  }
  
  public SharedPreferences getSharedPreferences()
  {
    if ((mPreferenceManager != null) && (getPreferenceDataStore() == null)) {
      return mPreferenceManager.getSharedPreferences();
    }
    return null;
  }
  
  public boolean getShouldDisableView()
  {
    return mShouldDisableView;
  }
  
  public CharSequence getSummary()
  {
    return mSummary;
  }
  
  public CharSequence getTitle()
  {
    return mTitle;
  }
  
  public int getTitleRes()
  {
    return mTitleRes;
  }
  
  public View getView(View paramView, ViewGroup paramViewGroup)
  {
    View localView = paramView;
    if (paramView == null) {
      localView = onCreateView(paramViewGroup);
    }
    onBindView(localView);
    return localView;
  }
  
  public int getWidgetLayoutResource()
  {
    return mWidgetLayoutResId;
  }
  
  public boolean hasKey()
  {
    return TextUtils.isEmpty(mKey) ^ true;
  }
  
  public boolean isEnabled()
  {
    boolean bool;
    if ((mEnabled) && (mDependencyMet) && (mParentDependencyMet)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isIconSpaceReserved()
  {
    return mIconSpaceReserved;
  }
  
  public boolean isPersistent()
  {
    return mPersistent;
  }
  
  public boolean isRecycleEnabled()
  {
    return mRecycleEnabled;
  }
  
  public boolean isSelectable()
  {
    return mSelectable;
  }
  
  public boolean isSingleLineTitle()
  {
    return mSingleLineTitle;
  }
  
  protected void notifyChanged()
  {
    if (mListener != null) {
      mListener.onPreferenceChange(this);
    }
  }
  
  public void notifyDependencyChange(boolean paramBoolean)
  {
    List localList = mDependents;
    if (localList == null) {
      return;
    }
    int i = localList.size();
    for (int j = 0; j < i; j++) {
      ((Preference)localList.get(j)).onDependencyChanged(this, paramBoolean);
    }
  }
  
  protected void notifyHierarchyChanged()
  {
    if (mListener != null) {
      mListener.onPreferenceHierarchyChange(this);
    }
  }
  
  protected void onAttachedToActivity()
  {
    registerDependency();
  }
  
  protected void onAttachedToHierarchy(PreferenceManager paramPreferenceManager)
  {
    mPreferenceManager = paramPreferenceManager;
    mId = paramPreferenceManager.getNextId();
    dispatchSetInitialValue();
  }
  
  protected void onBindView(View paramView)
  {
    Object localObject = (TextView)paramView.findViewById(16908310);
    int i = 8;
    CharSequence localCharSequence;
    if (localObject != null)
    {
      localCharSequence = getTitle();
      if (!TextUtils.isEmpty(localCharSequence))
      {
        ((TextView)localObject).setText(localCharSequence);
        ((TextView)localObject).setVisibility(0);
        if (mHasSingleLineTitleAttr) {
          ((TextView)localObject).setSingleLine(mSingleLineTitle);
        }
      }
      else
      {
        ((TextView)localObject).setVisibility(8);
      }
    }
    localObject = (TextView)paramView.findViewById(16908304);
    if (localObject != null)
    {
      localCharSequence = getSummary();
      if (!TextUtils.isEmpty(localCharSequence))
      {
        ((TextView)localObject).setText(localCharSequence);
        ((TextView)localObject).setVisibility(0);
      }
      else
      {
        ((TextView)localObject).setVisibility(8);
      }
    }
    localObject = (ImageView)paramView.findViewById(16908294);
    int j;
    if (localObject != null)
    {
      if ((mIconResId != 0) || (mIcon != null))
      {
        if (mIcon == null) {
          mIcon = getContext().getDrawable(mIconResId);
        }
        if (mIcon != null) {
          ((ImageView)localObject).setImageDrawable(mIcon);
        }
      }
      if (mIcon != null)
      {
        ((ImageView)localObject).setVisibility(0);
      }
      else
      {
        if (mIconSpaceReserved) {
          j = 4;
        } else {
          j = 8;
        }
        ((ImageView)localObject).setVisibility(j);
      }
    }
    localObject = paramView.findViewById(16908350);
    if (localObject != null) {
      if (mIcon != null)
      {
        ((View)localObject).setVisibility(0);
      }
      else
      {
        j = i;
        if (mIconSpaceReserved) {
          j = 4;
        }
        ((View)localObject).setVisibility(j);
      }
    }
    if (mShouldDisableView) {
      setEnabledStateOnViews(paramView, isEnabled());
    }
  }
  
  protected void onClick() {}
  
  protected View onCreateView(ViewGroup paramViewGroup)
  {
    LayoutInflater localLayoutInflater = (LayoutInflater)mContext.getSystemService("layout_inflater");
    View localView = localLayoutInflater.inflate(mLayoutResId, paramViewGroup, false);
    paramViewGroup = (ViewGroup)localView.findViewById(16908312);
    if (paramViewGroup != null) {
      if (mWidgetLayoutResId != 0) {
        localLayoutInflater.inflate(mWidgetLayoutResId, paramViewGroup);
      } else {
        paramViewGroup.setVisibility(8);
      }
    }
    return localView;
  }
  
  public void onDependencyChanged(Preference paramPreference, boolean paramBoolean)
  {
    if (mDependencyMet == paramBoolean)
    {
      mDependencyMet = (paramBoolean ^ true);
      notifyDependencyChange(shouldDisableDependents());
      notifyChanged();
    }
  }
  
  protected Object onGetDefaultValue(TypedArray paramTypedArray, int paramInt)
  {
    return null;
  }
  
  public boolean onKey(View paramView, int paramInt, KeyEvent paramKeyEvent)
  {
    return false;
  }
  
  public void onParentChanged(Preference paramPreference, boolean paramBoolean)
  {
    if (mParentDependencyMet == paramBoolean)
    {
      mParentDependencyMet = (paramBoolean ^ true);
      notifyDependencyChange(shouldDisableDependents());
      notifyChanged();
    }
  }
  
  protected void onPrepareForRemoval()
  {
    unregisterDependency();
  }
  
  protected void onRestoreInstanceState(Parcelable paramParcelable)
  {
    mBaseMethodCalled = true;
    if ((paramParcelable != BaseSavedState.EMPTY_STATE) && (paramParcelable != null)) {
      throw new IllegalArgumentException("Wrong state class -- expecting Preference State");
    }
  }
  
  protected Parcelable onSaveInstanceState()
  {
    mBaseMethodCalled = true;
    return BaseSavedState.EMPTY_STATE;
  }
  
  protected void onSetInitialValue(boolean paramBoolean, Object paramObject) {}
  
  public Bundle peekExtras()
  {
    return mExtras;
  }
  
  public void performClick(PreferenceScreen paramPreferenceScreen)
  {
    if (!isEnabled()) {
      return;
    }
    onClick();
    if ((mOnClickListener != null) && (mOnClickListener.onPreferenceClick(this))) {
      return;
    }
    Object localObject = getPreferenceManager();
    if (localObject != null)
    {
      localObject = ((PreferenceManager)localObject).getOnPreferenceTreeClickListener();
      if ((paramPreferenceScreen != null) && (localObject != null) && (((PreferenceManager.OnPreferenceTreeClickListener)localObject).onPreferenceTreeClick(paramPreferenceScreen, this))) {
        return;
      }
    }
    if (mIntent != null) {
      getContext().startActivity(mIntent);
    }
  }
  
  protected boolean persistBoolean(boolean paramBoolean)
  {
    if (!shouldPersist()) {
      return false;
    }
    if (paramBoolean == getPersistedBoolean(paramBoolean ^ true)) {
      return true;
    }
    Object localObject = getPreferenceDataStore();
    if (localObject != null)
    {
      ((PreferenceDataStore)localObject).putBoolean(mKey, paramBoolean);
    }
    else
    {
      localObject = mPreferenceManager.getEditor();
      ((SharedPreferences.Editor)localObject).putBoolean(mKey, paramBoolean);
      tryCommit((SharedPreferences.Editor)localObject);
    }
    return true;
  }
  
  protected boolean persistFloat(float paramFloat)
  {
    if (!shouldPersist()) {
      return false;
    }
    if (paramFloat == getPersistedFloat(NaN.0F)) {
      return true;
    }
    Object localObject = getPreferenceDataStore();
    if (localObject != null)
    {
      ((PreferenceDataStore)localObject).putFloat(mKey, paramFloat);
    }
    else
    {
      localObject = mPreferenceManager.getEditor();
      ((SharedPreferences.Editor)localObject).putFloat(mKey, paramFloat);
      tryCommit((SharedPreferences.Editor)localObject);
    }
    return true;
  }
  
  protected boolean persistInt(int paramInt)
  {
    if (!shouldPersist()) {
      return false;
    }
    if (paramInt == getPersistedInt(paramInt)) {
      return true;
    }
    Object localObject = getPreferenceDataStore();
    if (localObject != null)
    {
      ((PreferenceDataStore)localObject).putInt(mKey, paramInt);
    }
    else
    {
      localObject = mPreferenceManager.getEditor();
      ((SharedPreferences.Editor)localObject).putInt(mKey, paramInt);
      tryCommit((SharedPreferences.Editor)localObject);
    }
    return true;
  }
  
  protected boolean persistLong(long paramLong)
  {
    if (!shouldPersist()) {
      return false;
    }
    if (paramLong == getPersistedLong(paramLong)) {
      return true;
    }
    Object localObject = getPreferenceDataStore();
    if (localObject != null)
    {
      ((PreferenceDataStore)localObject).putLong(mKey, paramLong);
    }
    else
    {
      localObject = mPreferenceManager.getEditor();
      ((SharedPreferences.Editor)localObject).putLong(mKey, paramLong);
      tryCommit((SharedPreferences.Editor)localObject);
    }
    return true;
  }
  
  protected boolean persistString(String paramString)
  {
    if (!shouldPersist()) {
      return false;
    }
    if (TextUtils.equals(paramString, getPersistedString(null))) {
      return true;
    }
    Object localObject = getPreferenceDataStore();
    if (localObject != null)
    {
      ((PreferenceDataStore)localObject).putString(mKey, paramString);
    }
    else
    {
      localObject = mPreferenceManager.getEditor();
      ((SharedPreferences.Editor)localObject).putString(mKey, paramString);
      tryCommit((SharedPreferences.Editor)localObject);
    }
    return true;
  }
  
  public boolean persistStringSet(Set<String> paramSet)
  {
    if (!shouldPersist()) {
      return false;
    }
    if (paramSet.equals(getPersistedStringSet(null))) {
      return true;
    }
    Object localObject = getPreferenceDataStore();
    if (localObject != null)
    {
      ((PreferenceDataStore)localObject).putStringSet(mKey, paramSet);
    }
    else
    {
      localObject = mPreferenceManager.getEditor();
      ((SharedPreferences.Editor)localObject).putStringSet(mKey, paramSet);
      tryCommit((SharedPreferences.Editor)localObject);
    }
    return true;
  }
  
  void requireKey()
  {
    if (mKey != null)
    {
      mRequiresKey = true;
      return;
    }
    throw new IllegalStateException("Preference does not have a key assigned.");
  }
  
  public void restoreHierarchyState(Bundle paramBundle)
  {
    dispatchRestoreInstanceState(paramBundle);
  }
  
  public void saveHierarchyState(Bundle paramBundle)
  {
    dispatchSaveInstanceState(paramBundle);
  }
  
  public void setDefaultValue(Object paramObject)
  {
    mDefaultValue = paramObject;
  }
  
  public void setDependency(String paramString)
  {
    unregisterDependency();
    mDependencyKey = paramString;
    registerDependency();
  }
  
  public void setEnabled(boolean paramBoolean)
  {
    if (mEnabled != paramBoolean)
    {
      mEnabled = paramBoolean;
      notifyDependencyChange(shouldDisableDependents());
      notifyChanged();
    }
  }
  
  public void setFragment(String paramString)
  {
    mFragment = paramString;
  }
  
  public void setIcon(int paramInt)
  {
    if (mIconResId != paramInt)
    {
      mIconResId = paramInt;
      setIcon(mContext.getDrawable(paramInt));
    }
  }
  
  public void setIcon(Drawable paramDrawable)
  {
    if (((paramDrawable == null) && (mIcon != null)) || ((paramDrawable != null) && (mIcon != paramDrawable)))
    {
      mIcon = paramDrawable;
      notifyChanged();
    }
  }
  
  public void setIconSpaceReserved(boolean paramBoolean)
  {
    mIconSpaceReserved = paramBoolean;
    notifyChanged();
  }
  
  public void setIntent(Intent paramIntent)
  {
    mIntent = paramIntent;
  }
  
  public void setKey(String paramString)
  {
    mKey = paramString;
    if ((mRequiresKey) && (!hasKey())) {
      requireKey();
    }
  }
  
  public void setLayoutResource(int paramInt)
  {
    if (paramInt != mLayoutResId) {
      mRecycleEnabled = false;
    }
    mLayoutResId = paramInt;
  }
  
  final void setOnPreferenceChangeInternalListener(OnPreferenceChangeInternalListener paramOnPreferenceChangeInternalListener)
  {
    mListener = paramOnPreferenceChangeInternalListener;
  }
  
  public void setOnPreferenceChangeListener(OnPreferenceChangeListener paramOnPreferenceChangeListener)
  {
    mOnChangeListener = paramOnPreferenceChangeListener;
  }
  
  public void setOnPreferenceClickListener(OnPreferenceClickListener paramOnPreferenceClickListener)
  {
    mOnClickListener = paramOnPreferenceClickListener;
  }
  
  public void setOrder(int paramInt)
  {
    if (paramInt != mOrder)
    {
      mOrder = paramInt;
      notifyHierarchyChanged();
    }
  }
  
  public void setPersistent(boolean paramBoolean)
  {
    mPersistent = paramBoolean;
  }
  
  public void setPreferenceDataStore(PreferenceDataStore paramPreferenceDataStore)
  {
    mPreferenceDataStore = paramPreferenceDataStore;
  }
  
  public void setRecycleEnabled(boolean paramBoolean)
  {
    mRecycleEnabled = paramBoolean;
    notifyChanged();
  }
  
  public void setSelectable(boolean paramBoolean)
  {
    if (mSelectable != paramBoolean)
    {
      mSelectable = paramBoolean;
      notifyChanged();
    }
  }
  
  public void setShouldDisableView(boolean paramBoolean)
  {
    mShouldDisableView = paramBoolean;
    notifyChanged();
  }
  
  public void setSingleLineTitle(boolean paramBoolean)
  {
    mHasSingleLineTitleAttr = true;
    mSingleLineTitle = paramBoolean;
    notifyChanged();
  }
  
  public void setSummary(int paramInt)
  {
    setSummary(mContext.getString(paramInt));
  }
  
  public void setSummary(CharSequence paramCharSequence)
  {
    if (((paramCharSequence == null) && (mSummary != null)) || ((paramCharSequence != null) && (!paramCharSequence.equals(mSummary))))
    {
      mSummary = paramCharSequence;
      notifyChanged();
    }
  }
  
  public void setTitle(int paramInt)
  {
    setTitle(mContext.getString(paramInt));
    mTitleRes = paramInt;
  }
  
  public void setTitle(CharSequence paramCharSequence)
  {
    if (((paramCharSequence == null) && (mTitle != null)) || ((paramCharSequence != null) && (!paramCharSequence.equals(mTitle))))
    {
      mTitleRes = 0;
      mTitle = paramCharSequence;
      notifyChanged();
    }
  }
  
  public void setWidgetLayoutResource(int paramInt)
  {
    if (paramInt != mWidgetLayoutResId) {
      mRecycleEnabled = false;
    }
    mWidgetLayoutResId = paramInt;
  }
  
  public boolean shouldCommit()
  {
    if (mPreferenceManager == null) {
      return false;
    }
    return mPreferenceManager.shouldCommit();
  }
  
  public boolean shouldDisableDependents()
  {
    return isEnabled() ^ true;
  }
  
  protected boolean shouldPersist()
  {
    boolean bool;
    if ((mPreferenceManager != null) && (isPersistent()) && (hasKey())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public String toString()
  {
    return getFilterableStringBuilder().toString();
  }
  
  public static class BaseSavedState
    extends AbsSavedState
  {
    public static final Parcelable.Creator<BaseSavedState> CREATOR = new Parcelable.Creator()
    {
      public Preference.BaseSavedState createFromParcel(Parcel paramAnonymousParcel)
      {
        return new Preference.BaseSavedState(paramAnonymousParcel);
      }
      
      public Preference.BaseSavedState[] newArray(int paramAnonymousInt)
      {
        return new Preference.BaseSavedState[paramAnonymousInt];
      }
    };
    
    public BaseSavedState(Parcel paramParcel)
    {
      super();
    }
    
    public BaseSavedState(Parcelable paramParcelable)
    {
      super();
    }
  }
  
  static abstract interface OnPreferenceChangeInternalListener
  {
    public abstract void onPreferenceChange(Preference paramPreference);
    
    public abstract void onPreferenceHierarchyChange(Preference paramPreference);
  }
  
  public static abstract interface OnPreferenceChangeListener
  {
    public abstract boolean onPreferenceChange(Preference paramPreference, Object paramObject);
  }
  
  public static abstract interface OnPreferenceClickListener
  {
    public abstract boolean onPreferenceClick(Preference paramPreference);
  }
}
