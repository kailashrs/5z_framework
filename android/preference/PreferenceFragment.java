package android.preference;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.android.internal.R.styleable;

@Deprecated
public abstract class PreferenceFragment
  extends Fragment
  implements PreferenceManager.OnPreferenceTreeClickListener
{
  private static final int FIRST_REQUEST_CODE = 100;
  private static final int MSG_BIND_PREFERENCES = 1;
  private static final String PREFERENCES_TAG = "android:preferences";
  private Handler mHandler = new Handler()
  {
    public void handleMessage(Message paramAnonymousMessage)
    {
      if (what == 1) {
        PreferenceFragment.this.bindPreferences();
      }
    }
  };
  private boolean mHavePrefs;
  private boolean mInitDone;
  private int mLayoutResId = 17367265;
  private ListView mList;
  private View.OnKeyListener mListOnKeyListener = new View.OnKeyListener()
  {
    public boolean onKey(View paramAnonymousView, int paramAnonymousInt, KeyEvent paramAnonymousKeyEvent)
    {
      Object localObject = mList.getSelectedItem();
      if ((localObject instanceof Preference))
      {
        paramAnonymousView = mList.getSelectedView();
        return ((Preference)localObject).onKey(paramAnonymousView, paramAnonymousInt, paramAnonymousKeyEvent);
      }
      return false;
    }
  };
  private PreferenceManager mPreferenceManager;
  private final Runnable mRequestFocus = new Runnable()
  {
    public void run()
    {
      mList.focusableViewAvailable(mList);
    }
  };
  
  public PreferenceFragment() {}
  
  private void bindPreferences()
  {
    PreferenceScreen localPreferenceScreen = getPreferenceScreen();
    if (localPreferenceScreen != null)
    {
      Object localObject = getView();
      if (localObject != null)
      {
        View localView = ((View)localObject).findViewById(16908310);
        if ((localView instanceof TextView))
        {
          localObject = localPreferenceScreen.getTitle();
          if (TextUtils.isEmpty((CharSequence)localObject))
          {
            localView.setVisibility(8);
          }
          else
          {
            ((TextView)localView).setText((CharSequence)localObject);
            localView.setVisibility(0);
          }
        }
      }
      localPreferenceScreen.bind(getListView());
    }
    onBindPreferences();
  }
  
  private void ensureList()
  {
    if (mList != null) {
      return;
    }
    View localView = getView();
    if (localView != null)
    {
      localView = localView.findViewById(16908298);
      if ((localView instanceof ListView))
      {
        mList = ((ListView)localView);
        if (mList != null)
        {
          mList.setOnKeyListener(mListOnKeyListener);
          mHandler.post(mRequestFocus);
          return;
        }
        throw new RuntimeException("Your content must have a ListView whose id attribute is 'android.R.id.list'");
      }
      throw new RuntimeException("Content has view with id attribute 'android.R.id.list' that is not a ListView class");
    }
    throw new IllegalStateException("Content view not yet created");
  }
  
  private void postBindPreferences()
  {
    if (mHandler.hasMessages(1)) {
      return;
    }
    mHandler.obtainMessage(1).sendToTarget();
  }
  
  private void requirePreferenceManager()
  {
    if (mPreferenceManager != null) {
      return;
    }
    throw new RuntimeException("This should be called after super.onCreate.");
  }
  
  public void addPreferencesFromIntent(Intent paramIntent)
  {
    requirePreferenceManager();
    setPreferenceScreen(mPreferenceManager.inflateFromIntent(paramIntent, getPreferenceScreen()));
  }
  
  public void addPreferencesFromResource(int paramInt)
  {
    requirePreferenceManager();
    setPreferenceScreen(mPreferenceManager.inflateFromResource(getActivity(), paramInt, getPreferenceScreen()));
  }
  
  public Preference findPreference(CharSequence paramCharSequence)
  {
    if (mPreferenceManager == null) {
      return null;
    }
    return mPreferenceManager.findPreference(paramCharSequence);
  }
  
  public ListView getListView()
  {
    ensureList();
    return mList;
  }
  
  public PreferenceManager getPreferenceManager()
  {
    return mPreferenceManager;
  }
  
  public PreferenceScreen getPreferenceScreen()
  {
    return mPreferenceManager.getPreferenceScreen();
  }
  
  public boolean hasListView()
  {
    if (mList != null) {
      return true;
    }
    View localView = getView();
    if (localView == null) {
      return false;
    }
    localView = localView.findViewById(16908298);
    if (!(localView instanceof ListView)) {
      return false;
    }
    mList = ((ListView)localView);
    return mList != null;
  }
  
  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    if (mHavePrefs) {
      bindPreferences();
    }
    mInitDone = true;
    if (paramBundle != null)
    {
      Bundle localBundle = paramBundle.getBundle("android:preferences");
      if (localBundle != null)
      {
        paramBundle = getPreferenceScreen();
        if (paramBundle != null) {
          paramBundle.restoreHierarchyState(localBundle);
        }
      }
    }
  }
  
  public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    mPreferenceManager.dispatchActivityResult(paramInt1, paramInt2, paramIntent);
  }
  
  protected void onBindPreferences() {}
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    mPreferenceManager = new PreferenceManager(getActivity(), 100);
    mPreferenceManager.setFragment(this);
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramBundle = getActivity().obtainStyledAttributes(null, R.styleable.PreferenceFragment, 16844038, 0);
    mLayoutResId = paramBundle.getResourceId(0, mLayoutResId);
    paramBundle.recycle();
    return paramLayoutInflater.inflate(mLayoutResId, paramViewGroup, false);
  }
  
  public void onDestroy()
  {
    super.onDestroy();
    mPreferenceManager.dispatchActivityDestroy();
  }
  
  public void onDestroyView()
  {
    if (mList != null) {
      mList.setOnKeyListener(null);
    }
    mList = null;
    mHandler.removeCallbacks(mRequestFocus);
    mHandler.removeMessages(1);
    super.onDestroyView();
  }
  
  public boolean onPreferenceTreeClick(PreferenceScreen paramPreferenceScreen, Preference paramPreference)
  {
    if ((paramPreference.getFragment() != null) && ((getActivity() instanceof OnPreferenceStartFragmentCallback))) {
      return ((OnPreferenceStartFragmentCallback)getActivity()).onPreferenceStartFragment(this, paramPreference);
    }
    return false;
  }
  
  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    PreferenceScreen localPreferenceScreen = getPreferenceScreen();
    if (localPreferenceScreen != null)
    {
      Bundle localBundle = new Bundle();
      localPreferenceScreen.saveHierarchyState(localBundle);
      paramBundle.putBundle("android:preferences", localBundle);
    }
  }
  
  public void onStart()
  {
    super.onStart();
    mPreferenceManager.setOnPreferenceTreeClickListener(this);
  }
  
  public void onStop()
  {
    super.onStop();
    mPreferenceManager.dispatchActivityStop();
    mPreferenceManager.setOnPreferenceTreeClickListener(null);
  }
  
  protected void onUnbindPreferences() {}
  
  public void onViewCreated(View paramView, Bundle paramBundle)
  {
    super.onViewCreated(paramView, paramBundle);
    paramBundle = getActivity().obtainStyledAttributes(null, R.styleable.PreferenceFragment, 16844038, 0);
    paramView = (ListView)paramView.findViewById(16908298);
    if ((paramView != null) && (paramBundle.hasValueOrEmpty(1))) {
      paramView.setDivider(paramBundle.getDrawable(1));
    }
    paramBundle.recycle();
  }
  
  public void setPreferenceScreen(PreferenceScreen paramPreferenceScreen)
  {
    if ((mPreferenceManager.setPreferences(paramPreferenceScreen)) && (paramPreferenceScreen != null))
    {
      onUnbindPreferences();
      mHavePrefs = true;
      if (mInitDone) {
        postBindPreferences();
      }
    }
  }
  
  @Deprecated
  public static abstract interface OnPreferenceStartFragmentCallback
  {
    public abstract boolean onPreferenceStartFragment(PreferenceFragment paramPreferenceFragment, Preference paramPreference);
  }
}
