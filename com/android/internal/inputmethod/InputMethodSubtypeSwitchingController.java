package com.android.internal.inputmethod;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Printer;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodSubtype;
import com.android.internal.annotations.VisibleForTesting;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

public class InputMethodSubtypeSwitchingController
{
  private static final boolean DEBUG = false;
  private static final int NOT_A_SUBTYPE_ID = -1;
  private static final String TAG = InputMethodSubtypeSwitchingController.class.getSimpleName();
  private ControllerImpl mController;
  private final InputMethodUtils.InputMethodSettings mSettings;
  private InputMethodAndSubtypeList mSubtypeList;
  
  private InputMethodSubtypeSwitchingController(InputMethodUtils.InputMethodSettings paramInputMethodSettings, Context paramContext)
  {
    mSettings = paramInputMethodSettings;
    resetCircularListLocked(paramContext);
  }
  
  private static int calculateSubtypeId(InputMethodInfo paramInputMethodInfo, InputMethodSubtype paramInputMethodSubtype)
  {
    int i;
    if (paramInputMethodSubtype != null) {
      i = InputMethodUtils.getSubtypeIdFromHashCode(paramInputMethodInfo, paramInputMethodSubtype.hashCode());
    } else {
      i = -1;
    }
    return i;
  }
  
  public static InputMethodSubtypeSwitchingController createInstanceLocked(InputMethodUtils.InputMethodSettings paramInputMethodSettings, Context paramContext)
  {
    return new InputMethodSubtypeSwitchingController(paramInputMethodSettings, paramContext);
  }
  
  public void dump(Printer paramPrinter)
  {
    if (mController != null) {
      mController.dump(paramPrinter);
    } else {
      paramPrinter.println("    mController=null");
    }
  }
  
  public ImeSubtypeListItem getNextInputMethodLocked(boolean paramBoolean1, InputMethodInfo paramInputMethodInfo, InputMethodSubtype paramInputMethodSubtype, boolean paramBoolean2)
  {
    if (mController == null) {
      return null;
    }
    return mController.getNextInputMethod(paramBoolean1, paramInputMethodInfo, paramInputMethodSubtype, paramBoolean2);
  }
  
  public List<ImeSubtypeListItem> getSortedInputMethodAndSubtypeListLocked(boolean paramBoolean1, boolean paramBoolean2)
  {
    return mSubtypeList.getSortedInputMethodAndSubtypeList(paramBoolean1, paramBoolean2);
  }
  
  public void onUserActionLocked(InputMethodInfo paramInputMethodInfo, InputMethodSubtype paramInputMethodSubtype)
  {
    if (mController == null) {
      return;
    }
    mController.onUserActionLocked(paramInputMethodInfo, paramInputMethodSubtype);
  }
  
  public void resetCircularListLocked(Context paramContext)
  {
    mSubtypeList = new InputMethodAndSubtypeList(paramContext, mSettings);
    mController = ControllerImpl.createFrom(mController, mSubtypeList.getSortedInputMethodAndSubtypeList(false, false));
  }
  
  @VisibleForTesting
  public static class ControllerImpl
  {
    private final InputMethodSubtypeSwitchingController.DynamicRotationList mSwitchingAwareRotationList;
    private final InputMethodSubtypeSwitchingController.StaticRotationList mSwitchingUnawareRotationList;
    
    private ControllerImpl(InputMethodSubtypeSwitchingController.DynamicRotationList paramDynamicRotationList, InputMethodSubtypeSwitchingController.StaticRotationList paramStaticRotationList)
    {
      mSwitchingAwareRotationList = paramDynamicRotationList;
      mSwitchingUnawareRotationList = paramStaticRotationList;
    }
    
    public static ControllerImpl createFrom(ControllerImpl paramControllerImpl, List<InputMethodSubtypeSwitchingController.ImeSubtypeListItem> paramList)
    {
      Object localObject1 = null;
      List localList = filterImeSubtypeList(paramList, true);
      Object localObject2 = localObject1;
      if (paramControllerImpl != null)
      {
        localObject2 = localObject1;
        if (mSwitchingAwareRotationList != null)
        {
          localObject2 = localObject1;
          if (Objects.equals(InputMethodSubtypeSwitchingController.DynamicRotationList.access$200(mSwitchingAwareRotationList), localList)) {
            localObject2 = mSwitchingAwareRotationList;
          }
        }
      }
      localObject1 = localObject2;
      if (localObject2 == null) {
        localObject1 = new InputMethodSubtypeSwitchingController.DynamicRotationList(localList, null);
      }
      localObject2 = null;
      localList = filterImeSubtypeList(paramList, false);
      paramList = (List<InputMethodSubtypeSwitchingController.ImeSubtypeListItem>)localObject2;
      if (paramControllerImpl != null)
      {
        paramList = (List<InputMethodSubtypeSwitchingController.ImeSubtypeListItem>)localObject2;
        if (mSwitchingUnawareRotationList != null)
        {
          paramList = (List<InputMethodSubtypeSwitchingController.ImeSubtypeListItem>)localObject2;
          if (Objects.equals(InputMethodSubtypeSwitchingController.StaticRotationList.access$400(mSwitchingUnawareRotationList), localList)) {
            paramList = mSwitchingUnawareRotationList;
          }
        }
      }
      paramControllerImpl = paramList;
      if (paramList == null) {
        paramControllerImpl = new InputMethodSubtypeSwitchingController.StaticRotationList(localList);
      }
      return new ControllerImpl((InputMethodSubtypeSwitchingController.DynamicRotationList)localObject1, paramControllerImpl);
    }
    
    private static List<InputMethodSubtypeSwitchingController.ImeSubtypeListItem> filterImeSubtypeList(List<InputMethodSubtypeSwitchingController.ImeSubtypeListItem> paramList, boolean paramBoolean)
    {
      ArrayList localArrayList = new ArrayList();
      int i = paramList.size();
      for (int j = 0; j < i; j++)
      {
        InputMethodSubtypeSwitchingController.ImeSubtypeListItem localImeSubtypeListItem = (InputMethodSubtypeSwitchingController.ImeSubtypeListItem)paramList.get(j);
        if (mImi.supportsSwitchingToNextInputMethod() == paramBoolean) {
          localArrayList.add(localImeSubtypeListItem);
        }
      }
      return localArrayList;
    }
    
    protected void dump(Printer paramPrinter)
    {
      paramPrinter.println("    mSwitchingAwareRotationList:");
      mSwitchingAwareRotationList.dump(paramPrinter, "      ");
      paramPrinter.println("    mSwitchingUnawareRotationList:");
      mSwitchingUnawareRotationList.dump(paramPrinter, "      ");
    }
    
    public InputMethodSubtypeSwitchingController.ImeSubtypeListItem getNextInputMethod(boolean paramBoolean1, InputMethodInfo paramInputMethodInfo, InputMethodSubtype paramInputMethodSubtype, boolean paramBoolean2)
    {
      if (paramInputMethodInfo == null) {
        return null;
      }
      if (paramInputMethodInfo.supportsSwitchingToNextInputMethod()) {
        return mSwitchingAwareRotationList.getNextInputMethodLocked(paramBoolean1, paramInputMethodInfo, paramInputMethodSubtype, paramBoolean2);
      }
      return mSwitchingUnawareRotationList.getNextInputMethodLocked(paramBoolean1, paramInputMethodInfo, paramInputMethodSubtype, paramBoolean2);
    }
    
    public void onUserActionLocked(InputMethodInfo paramInputMethodInfo, InputMethodSubtype paramInputMethodSubtype)
    {
      if (paramInputMethodInfo == null) {
        return;
      }
      if (paramInputMethodInfo.supportsSwitchingToNextInputMethod()) {
        mSwitchingAwareRotationList.onUserAction(paramInputMethodInfo, paramInputMethodSubtype);
      }
    }
  }
  
  private static class DynamicRotationList
  {
    private static final String TAG = DynamicRotationList.class.getSimpleName();
    private final List<InputMethodSubtypeSwitchingController.ImeSubtypeListItem> mImeSubtypeList;
    private final int[] mUsageHistoryOfSubtypeListItemIndex;
    
    private DynamicRotationList(List<InputMethodSubtypeSwitchingController.ImeSubtypeListItem> paramList)
    {
      mImeSubtypeList = paramList;
      mUsageHistoryOfSubtypeListItemIndex = new int[mImeSubtypeList.size()];
      int i = mImeSubtypeList.size();
      for (int j = 0; j < i; j++) {
        mUsageHistoryOfSubtypeListItemIndex[j] = j;
      }
    }
    
    private int getUsageRank(InputMethodInfo paramInputMethodInfo, InputMethodSubtype paramInputMethodSubtype)
    {
      int i = InputMethodSubtypeSwitchingController.calculateSubtypeId(paramInputMethodInfo, paramInputMethodSubtype);
      int j = mUsageHistoryOfSubtypeListItemIndex.length;
      for (int k = 0; k < j; k++)
      {
        int m = mUsageHistoryOfSubtypeListItemIndex[k];
        paramInputMethodSubtype = (InputMethodSubtypeSwitchingController.ImeSubtypeListItem)mImeSubtypeList.get(m);
        if ((mImi.equals(paramInputMethodInfo)) && (mSubtypeId == i)) {
          return k;
        }
      }
      return -1;
    }
    
    protected void dump(Printer paramPrinter, String paramString)
    {
      for (int i = 0; i < mUsageHistoryOfSubtypeListItemIndex.length; i++)
      {
        int j = mUsageHistoryOfSubtypeListItemIndex[i];
        InputMethodSubtypeSwitchingController.ImeSubtypeListItem localImeSubtypeListItem = (InputMethodSubtypeSwitchingController.ImeSubtypeListItem)mImeSubtypeList.get(i);
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append(paramString);
        localStringBuilder.append("rank=");
        localStringBuilder.append(j);
        localStringBuilder.append(" item=");
        localStringBuilder.append(localImeSubtypeListItem);
        paramPrinter.println(localStringBuilder.toString());
      }
    }
    
    public InputMethodSubtypeSwitchingController.ImeSubtypeListItem getNextInputMethodLocked(boolean paramBoolean1, InputMethodInfo paramInputMethodInfo, InputMethodSubtype paramInputMethodSubtype, boolean paramBoolean2)
    {
      int i = getUsageRank(paramInputMethodInfo, paramInputMethodSubtype);
      if (i < 0) {
        return null;
      }
      int j = mUsageHistoryOfSubtypeListItemIndex.length;
      int k = 1;
      while (k < j)
      {
        if (paramBoolean2) {
          m = k;
        } else {
          m = j - k;
        }
        int m = mUsageHistoryOfSubtypeListItemIndex[((i + m) % j)];
        paramInputMethodSubtype = (InputMethodSubtypeSwitchingController.ImeSubtypeListItem)mImeSubtypeList.get(m);
        if ((paramBoolean1) && (!paramInputMethodInfo.equals(mImi))) {
          k++;
        } else {
          return paramInputMethodSubtype;
        }
      }
      return null;
    }
    
    public void onUserAction(InputMethodInfo paramInputMethodInfo, InputMethodSubtype paramInputMethodSubtype)
    {
      int i = getUsageRank(paramInputMethodInfo, paramInputMethodSubtype);
      if (i <= 0) {
        return;
      }
      int j = mUsageHistoryOfSubtypeListItemIndex[i];
      System.arraycopy(mUsageHistoryOfSubtypeListItemIndex, 0, mUsageHistoryOfSubtypeListItemIndex, 1, i);
      mUsageHistoryOfSubtypeListItemIndex[0] = j;
    }
  }
  
  public static class ImeSubtypeListItem
    implements Comparable<ImeSubtypeListItem>
  {
    public final CharSequence mImeName;
    public final InputMethodInfo mImi;
    public final boolean mIsSystemLanguage;
    public final boolean mIsSystemLocale;
    public final int mSubtypeId;
    public final CharSequence mSubtypeName;
    
    public ImeSubtypeListItem(CharSequence paramCharSequence1, CharSequence paramCharSequence2, InputMethodInfo paramInputMethodInfo, int paramInt, String paramString1, String paramString2)
    {
      mImeName = paramCharSequence1;
      mSubtypeName = paramCharSequence2;
      mImi = paramInputMethodInfo;
      mSubtypeId = paramInt;
      boolean bool1 = TextUtils.isEmpty(paramString1);
      boolean bool2 = false;
      if (bool1)
      {
        mIsSystemLocale = false;
        mIsSystemLanguage = false;
      }
      else
      {
        mIsSystemLocale = paramString1.equals(paramString2);
        if (mIsSystemLocale)
        {
          mIsSystemLanguage = true;
        }
        else
        {
          paramCharSequence1 = parseLanguageFromLocaleString(paramString2);
          paramCharSequence2 = parseLanguageFromLocaleString(paramString1);
          bool1 = bool2;
          if (paramCharSequence1.length() >= 2)
          {
            bool1 = bool2;
            if (paramCharSequence1.equals(paramCharSequence2)) {
              bool1 = true;
            }
          }
          mIsSystemLanguage = bool1;
        }
      }
    }
    
    private static int compareNullableCharSequences(CharSequence paramCharSequence1, CharSequence paramCharSequence2)
    {
      boolean bool1 = TextUtils.isEmpty(paramCharSequence1);
      boolean bool2 = TextUtils.isEmpty(paramCharSequence2);
      if ((!bool1) && (!bool2)) {
        return paramCharSequence1.toString().compareTo(paramCharSequence2.toString());
      }
      return bool1 - bool2;
    }
    
    private static String parseLanguageFromLocaleString(String paramString)
    {
      int i = paramString.indexOf('_');
      if (i < 0) {
        return paramString;
      }
      return paramString.substring(0, i);
    }
    
    public int compareTo(ImeSubtypeListItem paramImeSubtypeListItem)
    {
      int i = compareNullableCharSequences(mImeName, mImeName);
      if (i != 0) {
        return i;
      }
      boolean bool = mIsSystemLocale;
      int j = 0;
      if (bool) {
        i = -1;
      } else {
        i = 0;
      }
      if (mIsSystemLocale) {
        k = -1;
      } else {
        k = 0;
      }
      i -= k;
      if (i != 0) {
        return i;
      }
      if (mIsSystemLanguage) {
        i = -1;
      } else {
        i = 0;
      }
      int k = j;
      if (mIsSystemLanguage) {
        k = -1;
      }
      i -= k;
      if (i != 0) {
        return i;
      }
      return compareNullableCharSequences(mSubtypeName, mSubtypeName);
    }
    
    public boolean equals(Object paramObject)
    {
      boolean bool = true;
      if (paramObject == this) {
        return true;
      }
      if ((paramObject instanceof ImeSubtypeListItem))
      {
        paramObject = (ImeSubtypeListItem)paramObject;
        if ((!Objects.equals(mImi, mImi)) || (mSubtypeId != mSubtypeId)) {
          bool = false;
        }
        return bool;
      }
      return false;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("ImeSubtypeListItem{mImeName=");
      localStringBuilder.append(mImeName);
      localStringBuilder.append(" mSubtypeName=");
      localStringBuilder.append(mSubtypeName);
      localStringBuilder.append(" mSubtypeId=");
      localStringBuilder.append(mSubtypeId);
      localStringBuilder.append(" mIsSystemLocale=");
      localStringBuilder.append(mIsSystemLocale);
      localStringBuilder.append(" mIsSystemLanguage=");
      localStringBuilder.append(mIsSystemLanguage);
      localStringBuilder.append("}");
      return localStringBuilder.toString();
    }
  }
  
  private static class InputMethodAndSubtypeList
  {
    private final Context mContext;
    private final PackageManager mPm;
    private final InputMethodUtils.InputMethodSettings mSettings;
    private final TreeMap<InputMethodInfo, List<InputMethodSubtype>> mSortedImmis = new TreeMap(new Comparator()
    {
      public int compare(InputMethodInfo paramAnonymousInputMethodInfo1, InputMethodInfo paramAnonymousInputMethodInfo2)
      {
        if (paramAnonymousInputMethodInfo2 == null) {
          return 0;
        }
        if (paramAnonymousInputMethodInfo1 == null) {
          return 1;
        }
        if (mPm == null) {
          return paramAnonymousInputMethodInfo1.getId().compareTo(paramAnonymousInputMethodInfo2.getId());
        }
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append(paramAnonymousInputMethodInfo1.loadLabel(mPm));
        localStringBuilder.append("/");
        localStringBuilder.append(paramAnonymousInputMethodInfo1.getId());
        paramAnonymousInputMethodInfo1 = localStringBuilder.toString();
        localStringBuilder = new StringBuilder();
        localStringBuilder.append(paramAnonymousInputMethodInfo2.loadLabel(mPm));
        localStringBuilder.append("/");
        localStringBuilder.append(paramAnonymousInputMethodInfo2.getId());
        paramAnonymousInputMethodInfo2 = localStringBuilder.toString();
        return paramAnonymousInputMethodInfo1.toString().compareTo(paramAnonymousInputMethodInfo2.toString());
      }
    });
    private final String mSystemLocaleStr;
    
    public InputMethodAndSubtypeList(Context paramContext, InputMethodUtils.InputMethodSettings paramInputMethodSettings)
    {
      mContext = paramContext;
      mSettings = paramInputMethodSettings;
      mPm = paramContext.getPackageManager();
      paramContext = getResourcesgetConfigurationlocale;
      if (paramContext != null) {
        paramContext = paramContext.toString();
      } else {
        paramContext = "";
      }
      mSystemLocaleStr = paramContext;
    }
    
    public List<InputMethodSubtypeSwitchingController.ImeSubtypeListItem> getSortedInputMethodAndSubtypeList(boolean paramBoolean1, boolean paramBoolean2)
    {
      ArrayList localArrayList = new ArrayList();
      HashMap localHashMap = mSettings.getExplicitlyOrImplicitlyEnabledInputMethodsAndSubtypeListLocked(mContext);
      if ((localHashMap != null) && (localHashMap.size() != 0))
      {
        if ((paramBoolean2) && (paramBoolean1)) {
          paramBoolean1 = false;
        }
        mSortedImmis.clear();
        mSortedImmis.putAll(localHashMap);
        Iterator localIterator = mSortedImmis.keySet().iterator();
        while (localIterator.hasNext())
        {
          InputMethodInfo localInputMethodInfo = (InputMethodInfo)localIterator.next();
          if (localInputMethodInfo != null)
          {
            Object localObject = (List)localHashMap.get(localInputMethodInfo);
            HashSet localHashSet = new HashSet();
            localObject = ((List)localObject).iterator();
            while (((Iterator)localObject).hasNext()) {
              localHashSet.add(String.valueOf(((InputMethodSubtype)((Iterator)localObject).next()).hashCode()));
            }
            CharSequence localCharSequence = localInputMethodInfo.loadLabel(mPm);
            if (localHashSet.size() > 0)
            {
              int i = localInputMethodInfo.getSubtypeCount();
              for (int j = 0; j < i; j++)
              {
                InputMethodSubtype localInputMethodSubtype = localInputMethodInfo.getSubtypeAt(j);
                String str = String.valueOf(localInputMethodSubtype.hashCode());
                if (localHashSet.contains(str))
                {
                  if ((!paramBoolean1) && (localInputMethodSubtype.isAuxiliary())) {}
                  if (localInputMethodSubtype.overridesImplicitlyEnabledSubtype()) {}
                  for (localObject = null;; localObject = localInputMethodSubtype.getDisplayName(mContext, localInputMethodInfo.getPackageName(), getServiceInfoapplicationInfo)) {
                    break;
                  }
                  localArrayList.add(new InputMethodSubtypeSwitchingController.ImeSubtypeListItem(localCharSequence, (CharSequence)localObject, localInputMethodInfo, j, localInputMethodSubtype.getLocale(), mSystemLocaleStr));
                  localHashSet.remove(str);
                }
              }
            }
            else
            {
              localArrayList.add(new InputMethodSubtypeSwitchingController.ImeSubtypeListItem(localCharSequence, null, localInputMethodInfo, -1, null, mSystemLocaleStr));
            }
          }
        }
        Collections.sort(localArrayList);
        return localArrayList;
      }
      return Collections.emptyList();
    }
  }
  
  private static class StaticRotationList
  {
    private final List<InputMethodSubtypeSwitchingController.ImeSubtypeListItem> mImeSubtypeList;
    
    public StaticRotationList(List<InputMethodSubtypeSwitchingController.ImeSubtypeListItem> paramList)
    {
      mImeSubtypeList = paramList;
    }
    
    private int getIndex(InputMethodInfo paramInputMethodInfo, InputMethodSubtype paramInputMethodSubtype)
    {
      int i = InputMethodSubtypeSwitchingController.calculateSubtypeId(paramInputMethodInfo, paramInputMethodSubtype);
      int j = mImeSubtypeList.size();
      for (int k = 0; k < j; k++)
      {
        paramInputMethodSubtype = (InputMethodSubtypeSwitchingController.ImeSubtypeListItem)mImeSubtypeList.get(k);
        if ((paramInputMethodInfo.equals(mImi)) && (mSubtypeId == i)) {
          return k;
        }
      }
      return -1;
    }
    
    protected void dump(Printer paramPrinter, String paramString)
    {
      int i = mImeSubtypeList.size();
      for (int j = 0; j < i; j++)
      {
        InputMethodSubtypeSwitchingController.ImeSubtypeListItem localImeSubtypeListItem = (InputMethodSubtypeSwitchingController.ImeSubtypeListItem)mImeSubtypeList.get(j);
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append(paramString);
        localStringBuilder.append("rank=");
        localStringBuilder.append(j);
        localStringBuilder.append(" item=");
        localStringBuilder.append(localImeSubtypeListItem);
        paramPrinter.println(localStringBuilder.toString());
      }
    }
    
    public InputMethodSubtypeSwitchingController.ImeSubtypeListItem getNextInputMethodLocked(boolean paramBoolean1, InputMethodInfo paramInputMethodInfo, InputMethodSubtype paramInputMethodSubtype, boolean paramBoolean2)
    {
      if (paramInputMethodInfo == null) {
        return null;
      }
      int i = mImeSubtypeList.size();
      int j = 1;
      if (i <= 1) {
        return null;
      }
      int k = getIndex(paramInputMethodInfo, paramInputMethodSubtype);
      if (k < 0) {
        return null;
      }
      int m = mImeSubtypeList.size();
      while (j < m)
      {
        if (paramBoolean2) {
          i = j;
        } else {
          i = m - j;
        }
        paramInputMethodSubtype = (InputMethodSubtypeSwitchingController.ImeSubtypeListItem)mImeSubtypeList.get((k + i) % m);
        if ((paramBoolean1) && (!paramInputMethodInfo.equals(mImi))) {
          j++;
        } else {
          return paramInputMethodSubtype;
        }
      }
      return null;
    }
  }
}
