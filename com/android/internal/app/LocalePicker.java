package com.android.internal.app;

import android.app.ActivityManager;
import android.app.IActivityManager;
import android.app.ListFragment;
import android.app.backup.BackupManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.LocaleList;
import android.os.RemoteException;
import android.provider.Settings.Global;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class LocalePicker
  extends ListFragment
{
  private static final boolean DEBUG = false;
  private static final String TAG = "LocalePicker";
  private static final String[] pseudoLocales = { "en-XA", "ar-XB" };
  LocaleSelectionListener mListener;
  
  public LocalePicker() {}
  
  public static ArrayAdapter<LocaleInfo> constructAdapter(Context paramContext)
  {
    return constructAdapter(paramContext, 17367203, 16909095);
  }
  
  public static ArrayAdapter<LocaleInfo> constructAdapter(Context paramContext, final int paramInt1, final int paramInt2)
  {
    ContentResolver localContentResolver = paramContext.getContentResolver();
    boolean bool = false;
    if (Settings.Global.getInt(localContentResolver, "development_settings_enabled", 0) != 0) {
      bool = true;
    }
    new ArrayAdapter(paramContext, paramInt1, paramInt2, getAllAssetLocales(paramContext, bool))
    {
      public View getView(int paramAnonymousInt, View paramAnonymousView, ViewGroup paramAnonymousViewGroup)
      {
        if (paramAnonymousView == null)
        {
          paramAnonymousView = val$inflater.inflate(paramInt1, paramAnonymousViewGroup, false);
          paramAnonymousViewGroup = (TextView)paramAnonymousView.findViewById(paramInt2);
          paramAnonymousView.setTag(paramAnonymousViewGroup);
        }
        else
        {
          paramAnonymousViewGroup = (TextView)paramAnonymousView.getTag();
        }
        LocalePicker.LocaleInfo localLocaleInfo = (LocalePicker.LocaleInfo)getItem(paramAnonymousInt);
        paramAnonymousViewGroup.setText(localLocaleInfo.toString());
        paramAnonymousViewGroup.setTextLocale(localLocaleInfo.getLocale());
        return paramAnonymousView;
      }
    };
  }
  
  public static List<LocaleInfo> getAllAssetLocales(Context paramContext, boolean paramBoolean)
  {
    Object localObject1 = paramContext.getResources();
    paramContext = getSystemAssetLocales();
    Object localObject2 = new ArrayList(paramContext.length);
    Collections.addAll((Collection)localObject2, paramContext);
    Collections.sort((List)localObject2);
    paramContext = ((Resources)localObject1).getStringArray(17236086);
    String[] arrayOfString = ((Resources)localObject1).getStringArray(17236087);
    localObject1 = new ArrayList(((List)localObject2).size());
    Iterator localIterator = ((List)localObject2).iterator();
    while (localIterator.hasNext())
    {
      localObject2 = Locale.forLanguageTag(((String)localIterator.next()).replace('_', '-'));
      if ((localObject2 != null) && (!"und".equals(((Locale)localObject2).getLanguage())) && (!((Locale)localObject2).getLanguage().isEmpty()) && (!((Locale)localObject2).getCountry().isEmpty()) && ((paramBoolean) || (!LocaleList.isPseudoLocale((Locale)localObject2)))) {
        if (((ArrayList)localObject1).isEmpty())
        {
          ((ArrayList)localObject1).add(new LocaleInfo(toTitleCase(((Locale)localObject2).getDisplayLanguage((Locale)localObject2)), (Locale)localObject2));
        }
        else
        {
          LocaleInfo localLocaleInfo = (LocaleInfo)((ArrayList)localObject1).get(((ArrayList)localObject1).size() - 1);
          if ((locale.getLanguage().equals(((Locale)localObject2).getLanguage())) && (!locale.getLanguage().equals("zz")))
          {
            label = toTitleCase(getDisplayName(locale, paramContext, arrayOfString));
            ((ArrayList)localObject1).add(new LocaleInfo(toTitleCase(getDisplayName((Locale)localObject2, paramContext, arrayOfString)), (Locale)localObject2));
          }
          else
          {
            ((ArrayList)localObject1).add(new LocaleInfo(toTitleCase(((Locale)localObject2).getDisplayLanguage((Locale)localObject2)), (Locale)localObject2));
          }
        }
      }
    }
    Collections.sort((List)localObject1);
    return localObject1;
  }
  
  private static String getDisplayName(Locale paramLocale, String[] paramArrayOfString1, String[] paramArrayOfString2)
  {
    String str = paramLocale.toString();
    for (int i = 0; i < paramArrayOfString1.length; i++) {
      if (paramArrayOfString1[i].equals(str)) {
        return paramArrayOfString2[i];
      }
    }
    return paramLocale.getDisplayName(paramLocale);
  }
  
  public static LocaleList getLocales()
  {
    try
    {
      LocaleList localLocaleList = ActivityManager.getService().getConfiguration().getLocales();
      return localLocaleList;
    }
    catch (RemoteException localRemoteException) {}
    return LocaleList.getDefault();
  }
  
  public static String[] getSupportedLocales(Context paramContext)
  {
    return paramContext.getResources().getStringArray(17236088);
  }
  
  public static String[] getSystemAssetLocales()
  {
    return Resources.getSystem().getAssets().getLocales();
  }
  
  private static String toTitleCase(String paramString)
  {
    if (paramString.length() == 0) {
      return paramString;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(Character.toUpperCase(paramString.charAt(0)));
    localStringBuilder.append(paramString.substring(1));
    return localStringBuilder.toString();
  }
  
  public static void updateLocale(Locale paramLocale)
  {
    updateLocales(new LocaleList(new Locale[] { paramLocale }));
  }
  
  public static void updateLocales(LocaleList paramLocaleList)
  {
    try
    {
      IActivityManager localIActivityManager = ActivityManager.getService();
      Configuration localConfiguration = localIActivityManager.getConfiguration();
      localConfiguration.setLocales(paramLocaleList);
      userSetLocale = true;
      localIActivityManager.updatePersistentConfiguration(localConfiguration);
      BackupManager.dataChanged("com.android.providers.settings");
    }
    catch (RemoteException paramLocaleList) {}
  }
  
  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    setListAdapter(constructAdapter(getActivity()));
  }
  
  public void onListItemClick(ListView paramListView, View paramView, int paramInt, long paramLong)
  {
    if (mListener != null)
    {
      paramListView = getListAdaptergetItemlocale;
      mListener.onLocaleSelected(paramListView);
    }
  }
  
  public void onResume()
  {
    super.onResume();
    getListView().requestFocus();
  }
  
  public void setLocaleSelectionListener(LocaleSelectionListener paramLocaleSelectionListener)
  {
    mListener = paramLocaleSelectionListener;
  }
  
  public static class LocaleInfo
    implements Comparable<LocaleInfo>
  {
    static final Collator sCollator = ;
    String label;
    final Locale locale;
    
    public LocaleInfo(String paramString, Locale paramLocale)
    {
      label = paramString;
      locale = paramLocale;
    }
    
    public int compareTo(LocaleInfo paramLocaleInfo)
    {
      return sCollator.compare(label, label);
    }
    
    public String getLabel()
    {
      return label;
    }
    
    public Locale getLocale()
    {
      return locale;
    }
    
    public String toString()
    {
      return label;
    }
  }
  
  public static abstract interface LocaleSelectionListener
  {
    public abstract void onLocaleSelected(Locale paramLocale);
  }
}
