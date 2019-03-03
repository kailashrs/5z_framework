package com.android.internal.app;

import android.content.Context;
import android.content.res.Configuration;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filter.FilterResults;
import android.widget.Filterable;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

public class SuggestedLocaleAdapter
  extends BaseAdapter
  implements Filterable
{
  private static final int MIN_REGIONS_FOR_SUGGESTIONS = 6;
  private static final int TYPE_HEADER_ALL_OTHERS = 1;
  private static final int TYPE_HEADER_SUGGESTED = 0;
  private static final int TYPE_LOCALE = 2;
  private Context mContextOverride = null;
  private final boolean mCountryMode;
  private Locale mDisplayLocale = null;
  private LayoutInflater mInflater;
  private ArrayList<LocaleStore.LocaleInfo> mLocaleOptions;
  private ArrayList<LocaleStore.LocaleInfo> mOriginalLocaleOptions;
  private int mSuggestionCount;
  
  public SuggestedLocaleAdapter(Set<LocaleStore.LocaleInfo> paramSet, boolean paramBoolean)
  {
    mCountryMode = paramBoolean;
    mLocaleOptions = new ArrayList(paramSet.size());
    paramSet = paramSet.iterator();
    while (paramSet.hasNext())
    {
      LocaleStore.LocaleInfo localLocaleInfo = (LocaleStore.LocaleInfo)paramSet.next();
      if (localLocaleInfo.isSuggested()) {
        mSuggestionCount += 1;
      }
      mLocaleOptions.add(localLocaleInfo);
    }
  }
  
  private void setTextTo(TextView paramTextView, int paramInt)
  {
    if (mContextOverride == null) {
      paramTextView.setText(paramInt);
    } else {
      paramTextView.setText(mContextOverride.getText(paramInt));
    }
  }
  
  private boolean showHeaders()
  {
    boolean bool1 = mCountryMode;
    boolean bool2 = false;
    if ((bool1) && (mLocaleOptions.size() < 6)) {
      return false;
    }
    bool1 = bool2;
    if (mSuggestionCount != 0)
    {
      bool1 = bool2;
      if (mSuggestionCount != mLocaleOptions.size()) {
        bool1 = true;
      }
    }
    return bool1;
  }
  
  public boolean areAllItemsEnabled()
  {
    return false;
  }
  
  public int getCount()
  {
    if (showHeaders()) {
      return mLocaleOptions.size() + 2;
    }
    return mLocaleOptions.size();
  }
  
  public Filter getFilter()
  {
    return new FilterByNativeAndUiNames();
  }
  
  public Object getItem(int paramInt)
  {
    int i = 0;
    if (showHeaders()) {
      if (paramInt > mSuggestionCount) {
        i = -2;
      } else {
        i = -1;
      }
    }
    return mLocaleOptions.get(paramInt + i);
  }
  
  public long getItemId(int paramInt)
  {
    return paramInt;
  }
  
  public int getItemViewType(int paramInt)
  {
    if (!showHeaders()) {
      return 2;
    }
    if (paramInt == 0) {
      return 0;
    }
    if (paramInt == mSuggestionCount + 1) {
      return 1;
    }
    return 2;
  }
  
  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    if ((paramView == null) && (mInflater == null)) {
      mInflater = LayoutInflater.from(paramViewGroup.getContext());
    }
    int i = getItemViewType(paramInt);
    View localView;
    switch (i)
    {
    default: 
      localView = paramView;
      if (!(paramView instanceof ViewGroup)) {
        localView = mInflater.inflate(17367194, paramViewGroup, false);
      }
      break;
    case 0: 
    case 1: 
      localView = paramView;
      if (!(paramView instanceof TextView)) {
        localView = mInflater.inflate(17367195, paramViewGroup, false);
      }
      paramViewGroup = (TextView)localView;
      if (i == 0) {
        setTextTo(paramViewGroup, 17040227);
      } else if (mCountryMode) {
        setTextTo(paramViewGroup, 17040885);
      } else {
        setTextTo(paramViewGroup, 17040226);
      }
      if (mDisplayLocale != null) {
        paramView = mDisplayLocale;
      } else {
        paramView = Locale.getDefault();
      }
      paramViewGroup.setTextLocale(paramView);
      paramView = localView;
      break;
    }
    paramViewGroup = (TextView)localView.findViewById(16909095);
    LocaleStore.LocaleInfo localLocaleInfo = (LocaleStore.LocaleInfo)getItem(paramInt);
    paramViewGroup.setText(localLocaleInfo.getLabel(mCountryMode));
    paramViewGroup.setTextLocale(localLocaleInfo.getLocale());
    paramViewGroup.setContentDescription(localLocaleInfo.getContentDescription(mCountryMode));
    paramView = localView;
    if (mCountryMode)
    {
      paramInt = TextUtils.getLayoutDirectionFromLocale(localLocaleInfo.getParent());
      localView.setLayoutDirection(paramInt);
      if (paramInt == 1) {
        paramInt = 4;
      } else {
        paramInt = 3;
      }
      paramViewGroup.setTextDirection(paramInt);
      paramView = localView;
    }
    return paramView;
  }
  
  public int getViewTypeCount()
  {
    if (showHeaders()) {
      return 3;
    }
    return 1;
  }
  
  public boolean isEnabled(int paramInt)
  {
    boolean bool;
    if (getItemViewType(paramInt) == 2) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void setDisplayLocale(Context paramContext, Locale paramLocale)
  {
    if (paramLocale == null)
    {
      mDisplayLocale = null;
      mContextOverride = null;
    }
    else if (!paramLocale.equals(mDisplayLocale))
    {
      mDisplayLocale = paramLocale;
      Configuration localConfiguration = new Configuration();
      localConfiguration.setLocale(paramLocale);
      mContextOverride = paramContext.createConfigurationContext(localConfiguration);
    }
  }
  
  public void sort(LocaleHelper.LocaleInfoComparator paramLocaleInfoComparator)
  {
    Collections.sort(mLocaleOptions, paramLocaleInfoComparator);
  }
  
  class FilterByNativeAndUiNames
    extends Filter
  {
    FilterByNativeAndUiNames() {}
    
    protected Filter.FilterResults performFiltering(CharSequence paramCharSequence)
    {
      Filter.FilterResults localFilterResults = new Filter.FilterResults();
      if (mOriginalLocaleOptions == null) {
        SuggestedLocaleAdapter.access$002(SuggestedLocaleAdapter.this, new ArrayList(mLocaleOptions));
      }
      ArrayList localArrayList1 = new ArrayList(mOriginalLocaleOptions);
      if ((paramCharSequence != null) && (paramCharSequence.length() != 0))
      {
        Locale localLocale = Locale.getDefault();
        paramCharSequence = LocaleHelper.normalizeForSearch(paramCharSequence.toString(), localLocale);
        int i = localArrayList1.size();
        ArrayList localArrayList2 = new ArrayList();
        for (int j = 0; j < i; j++)
        {
          LocaleStore.LocaleInfo localLocaleInfo = (LocaleStore.LocaleInfo)localArrayList1.get(j);
          String str = LocaleHelper.normalizeForSearch(localLocaleInfo.getFullNameInUiLanguage(), localLocale);
          if ((wordMatches(LocaleHelper.normalizeForSearch(localLocaleInfo.getFullNameNative(), localLocale), paramCharSequence)) || (wordMatches(str, paramCharSequence))) {
            localArrayList2.add(localLocaleInfo);
          }
        }
        values = localArrayList2;
        count = localArrayList2.size();
      }
      else
      {
        values = localArrayList1;
        count = localArrayList1.size();
      }
      return localFilterResults;
    }
    
    protected void publishResults(CharSequence paramCharSequence, Filter.FilterResults paramFilterResults)
    {
      SuggestedLocaleAdapter.access$102(SuggestedLocaleAdapter.this, (ArrayList)values);
      SuggestedLocaleAdapter.access$202(SuggestedLocaleAdapter.this, 0);
      paramCharSequence = mLocaleOptions.iterator();
      while (paramCharSequence.hasNext()) {
        if (((LocaleStore.LocaleInfo)paramCharSequence.next()).isSuggested()) {
          SuggestedLocaleAdapter.access$208(SuggestedLocaleAdapter.this);
        }
      }
      if (count > 0) {
        notifyDataSetChanged();
      } else {
        notifyDataSetInvalidated();
      }
    }
    
    boolean wordMatches(String paramString1, String paramString2)
    {
      if (paramString1.startsWith(paramString2)) {
        return true;
      }
      paramString1 = paramString1.split(" ");
      int i = paramString1.length;
      for (int j = 0; j < i; j++) {
        if (paramString1[j].startsWith(paramString2)) {
          return true;
        }
      }
      return false;
    }
  }
}
