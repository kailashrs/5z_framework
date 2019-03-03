package com.android.internal.app;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.os.LocaleList;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Filter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

public class LocalePickerWithRegion
  extends ListFragment
  implements SearchView.OnQueryTextListener
{
  private static final String PARENT_FRAGMENT_NAME = "localeListEditor";
  private SuggestedLocaleAdapter mAdapter;
  private int mFirstVisiblePosition = 0;
  private LocaleSelectedListener mListener;
  private Set<LocaleStore.LocaleInfo> mLocaleList;
  private LocaleStore.LocaleInfo mParentLocale;
  private CharSequence mPreviousSearch = null;
  private boolean mPreviousSearchHadFocus = false;
  private SearchView mSearchView = null;
  private int mTopDistance = 0;
  private boolean mTranslatedOnly = false;
  
  public LocalePickerWithRegion() {}
  
  private static LocalePickerWithRegion createCountryPicker(Context paramContext, LocaleSelectedListener paramLocaleSelectedListener, LocaleStore.LocaleInfo paramLocaleInfo, boolean paramBoolean)
  {
    LocalePickerWithRegion localLocalePickerWithRegion = new LocalePickerWithRegion();
    if (localLocalePickerWithRegion.setListener(paramContext, paramLocaleSelectedListener, paramLocaleInfo, paramBoolean)) {
      paramContext = localLocalePickerWithRegion;
    } else {
      paramContext = null;
    }
    return paramContext;
  }
  
  public static LocalePickerWithRegion createLanguagePicker(Context paramContext, LocaleSelectedListener paramLocaleSelectedListener, boolean paramBoolean)
  {
    LocalePickerWithRegion localLocalePickerWithRegion = new LocalePickerWithRegion();
    localLocalePickerWithRegion.setListener(paramContext, paramLocaleSelectedListener, null, paramBoolean);
    return localLocalePickerWithRegion;
  }
  
  private void returnToParentFrame()
  {
    getFragmentManager().popBackStack("localeListEditor", 1);
  }
  
  private boolean setListener(Context paramContext, LocaleSelectedListener paramLocaleSelectedListener, LocaleStore.LocaleInfo paramLocaleInfo, boolean paramBoolean)
  {
    mParentLocale = paramLocaleInfo;
    mListener = paramLocaleSelectedListener;
    mTranslatedOnly = paramBoolean;
    setRetainInstance(true);
    HashSet localHashSet = new HashSet();
    if (!paramBoolean) {
      Collections.addAll(localHashSet, LocalePicker.getLocales().toLanguageTags().split(","));
    }
    if (paramLocaleInfo != null)
    {
      mLocaleList = LocaleStore.getLevelLocales(paramContext, localHashSet, paramLocaleInfo, paramBoolean);
      if (mLocaleList.size() <= 1)
      {
        if ((paramLocaleSelectedListener != null) && (mLocaleList.size() == 1)) {
          paramLocaleSelectedListener.onLocaleSelected((LocaleStore.LocaleInfo)mLocaleList.iterator().next());
        }
        return false;
      }
    }
    else
    {
      mLocaleList = LocaleStore.getLevelLocales(paramContext, localHashSet, null, paramBoolean);
    }
    return true;
  }
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    boolean bool = true;
    setHasOptionsMenu(true);
    if (mLocaleList == null)
    {
      returnToParentFrame();
      return;
    }
    if (mParentLocale == null) {
      bool = false;
    }
    if (bool) {
      paramBundle = mParentLocale.getLocale();
    } else {
      paramBundle = Locale.getDefault();
    }
    mAdapter = new SuggestedLocaleAdapter(mLocaleList, bool);
    paramBundle = new LocaleHelper.LocaleInfoComparator(paramBundle, bool);
    mAdapter.sort(paramBundle);
    setListAdapter(mAdapter);
  }
  
  public void onCreateOptionsMenu(Menu paramMenu, MenuInflater paramMenuInflater)
  {
    if (mParentLocale == null)
    {
      paramMenuInflater.inflate(18087936, paramMenu);
      paramMenu = paramMenu.findItem(16909096);
      mSearchView = ((SearchView)paramMenu.getActionView());
      mSearchView.setQueryHint(getText(17040951));
      mSearchView.setOnQueryTextListener(this);
      if (!TextUtils.isEmpty(mPreviousSearch))
      {
        paramMenu.expandActionView();
        mSearchView.setIconified(false);
        mSearchView.setActivated(true);
        if (mPreviousSearchHadFocus) {
          mSearchView.requestFocus();
        }
        mSearchView.setQuery(mPreviousSearch, true);
      }
      else
      {
        mSearchView.setQuery(null, false);
      }
      getListView().setSelectionFromTop(mFirstVisiblePosition, mTopDistance);
    }
  }
  
  public void onListItemClick(ListView paramListView, View paramView, int paramInt, long paramLong)
  {
    paramListView = (LocaleStore.LocaleInfo)getListAdapter().getItem(paramInt);
    if (paramListView.getParent() != null)
    {
      if (mListener != null) {
        mListener.onLocaleSelected(paramListView);
      }
      returnToParentFrame();
    }
    else
    {
      paramListView = createCountryPicker(getContext(), mListener, paramListView, mTranslatedOnly);
      if (paramListView != null) {
        getFragmentManager().beginTransaction().setTransition(4097).replace(getId(), paramListView).addToBackStack(null).commit();
      } else {
        returnToParentFrame();
      }
    }
  }
  
  public boolean onOptionsItemSelected(MenuItem paramMenuItem)
  {
    if (paramMenuItem.getItemId() != 16908332) {
      return super.onOptionsItemSelected(paramMenuItem);
    }
    getFragmentManager().popBackStack();
    return true;
  }
  
  public void onPause()
  {
    super.onPause();
    Object localObject = mSearchView;
    int i = 0;
    if (localObject != null)
    {
      mPreviousSearchHadFocus = mSearchView.hasFocus();
      mPreviousSearch = mSearchView.getQuery();
    }
    else
    {
      mPreviousSearchHadFocus = false;
      mPreviousSearch = null;
    }
    ListView localListView = getListView();
    localObject = localListView.getChildAt(0);
    mFirstVisiblePosition = localListView.getFirstVisiblePosition();
    if (localObject != null) {
      i = ((View)localObject).getTop() - localListView.getPaddingTop();
    }
    mTopDistance = i;
  }
  
  public boolean onQueryTextChange(String paramString)
  {
    if (mAdapter != null) {
      mAdapter.getFilter().filter(paramString);
    }
    return false;
  }
  
  public boolean onQueryTextSubmit(String paramString)
  {
    return false;
  }
  
  public void onResume()
  {
    super.onResume();
    if (mParentLocale != null) {
      getActivity().setTitle(mParentLocale.getFullNameNative());
    } else {
      getActivity().setTitle(17040228);
    }
    getListView().requestFocus();
  }
  
  public static abstract interface LocaleSelectedListener
  {
    public abstract void onLocaleSelected(LocaleStore.LocaleInfo paramLocaleInfo);
  }
}
