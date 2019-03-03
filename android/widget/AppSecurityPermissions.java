package android.widget;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.UserHandle;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AppSecurityPermissions
{
  private static final String TAG = "AppSecurityPermissions";
  public static final int WHICH_ALL = 65535;
  public static final int WHICH_NEW = 4;
  private static final boolean localLOGV = false;
  private final Context mContext;
  private final LayoutInflater mInflater;
  private final CharSequence mNewPermPrefix;
  private String mPackageName;
  private final PermissionInfoComparator mPermComparator = new PermissionInfoComparator();
  private final PermissionGroupInfoComparator mPermGroupComparator = new PermissionGroupInfoComparator(null);
  private final Map<String, MyPermissionGroupInfo> mPermGroups = new HashMap();
  private final List<MyPermissionGroupInfo> mPermGroupsList = new ArrayList();
  private final List<MyPermissionInfo> mPermsList = new ArrayList();
  private final PackageManager mPm;
  
  private AppSecurityPermissions(Context paramContext)
  {
    mContext = paramContext;
    mInflater = ((LayoutInflater)mContext.getSystemService("layout_inflater"));
    mPm = mContext.getPackageManager();
    mNewPermPrefix = mContext.getText(17040799);
  }
  
  public AppSecurityPermissions(Context paramContext, PackageInfo paramPackageInfo)
  {
    this(paramContext);
    HashSet localHashSet = new HashSet();
    if (paramPackageInfo == null) {
      return;
    }
    mPackageName = packageName;
    paramContext = null;
    if (requestedPermissions != null)
    {
      try
      {
        PackageInfo localPackageInfo = mPm.getPackageInfo(packageName, 4096);
        paramContext = localPackageInfo;
      }
      catch (PackageManager.NameNotFoundException localNameNotFoundException) {}
      extractPerms(paramPackageInfo, localHashSet, paramContext);
    }
    if (sharedUserId != null) {
      try
      {
        getAllUsedPermissions(mPm.getUidForSharedUser(sharedUserId), localHashSet);
      }
      catch (PackageManager.NameNotFoundException paramContext)
      {
        paramContext = new StringBuilder();
        paramContext.append("Couldn't retrieve shared user id for: ");
        paramContext.append(packageName);
        Log.w("AppSecurityPermissions", paramContext.toString());
      }
    }
    mPermsList.addAll(localHashSet);
    setPermissions(mPermsList);
  }
  
  public AppSecurityPermissions(Context paramContext, String paramString)
  {
    this(paramContext);
    mPackageName = paramString;
    HashSet localHashSet = new HashSet();
    try
    {
      paramContext = mPm.getPackageInfo(paramString, 4096);
      if ((applicationInfo != null) && (applicationInfo.uid != -1)) {
        getAllUsedPermissions(applicationInfo.uid, localHashSet);
      }
      mPermsList.addAll(localHashSet);
      setPermissions(mPermsList);
      return;
    }
    catch (PackageManager.NameNotFoundException paramContext)
    {
      paramContext = new StringBuilder();
      paramContext.append("Couldn't retrieve permissions for package:");
      paramContext.append(paramString);
      Log.w("AppSecurityPermissions", paramContext.toString());
    }
  }
  
  private void addPermToList(List<MyPermissionInfo> paramList, MyPermissionInfo paramMyPermissionInfo)
  {
    if (mLabel == null) {
      mLabel = paramMyPermissionInfo.loadSafeLabel(mPm, 20000.0F, 5);
    }
    int i = Collections.binarySearch(paramList, paramMyPermissionInfo, mPermComparator);
    if (i < 0) {
      paramList.add(-i - 1, paramMyPermissionInfo);
    }
  }
  
  private void displayPermissions(List<MyPermissionGroupInfo> paramList, LinearLayout paramLinearLayout, int paramInt, boolean paramBoolean)
  {
    paramLinearLayout.removeAllViews();
    int i = (int)(8.0F * mContext.getResources().getDisplayMetrics().density);
    for (int j = 0; j < paramList.size(); j++)
    {
      MyPermissionGroupInfo localMyPermissionGroupInfo = (MyPermissionGroupInfo)paramList.get(j);
      List localList = getPermissionList(localMyPermissionGroupInfo, paramInt);
      for (int k = 0; k < localList.size(); k++)
      {
        Object localObject1 = (MyPermissionInfo)localList.get(k);
        boolean bool;
        if (k == 0) {
          bool = true;
        } else {
          bool = false;
        }
        if (paramInt != 4) {}
        for (Object localObject2 = mNewPermPrefix;; localObject2 = null) {
          break;
        }
        localObject1 = getPermissionItemView(localMyPermissionGroupInfo, (MyPermissionInfo)localObject1, bool, (CharSequence)localObject2, paramBoolean);
        localObject2 = new LinearLayout.LayoutParams(-1, -2);
        if (k == 0) {
          topMargin = i;
        }
        if (k == mAllPermissions.size() - 1) {
          bottomMargin = i;
        }
        if (paramLinearLayout.getChildCount() == 0) {
          topMargin *= 2;
        }
        paramLinearLayout.addView((View)localObject1, (ViewGroup.LayoutParams)localObject2);
      }
    }
  }
  
  private void extractPerms(PackageInfo paramPackageInfo1, Set<MyPermissionInfo> paramSet, PackageInfo paramPackageInfo2)
  {
    String[] arrayOfString = requestedPermissions;
    int[] arrayOfInt = requestedPermissionsFlags;
    if ((arrayOfString != null) && (arrayOfString.length != 0))
    {
      for (int i = 0; i < arrayOfString.length; i++)
      {
        String str1 = arrayOfString[i];
        try
        {
          PermissionInfo localPermissionInfo = mPm.getPermissionInfo(str1, 0);
          if (localPermissionInfo == null) {
            continue;
          }
          int m;
          do
          {
            int j = -1;
            int k = j;
            if (paramPackageInfo2 != null)
            {
              k = j;
              if (requestedPermissions != null) {
                for (m = 0;; m++)
                {
                  k = j;
                  if (m >= requestedPermissions.length) {
                    break;
                  }
                  if (str1.equals(requestedPermissions[m]))
                  {
                    k = m;
                    break;
                  }
                }
              }
            }
            if (k >= 0) {
              m = requestedPermissionsFlags[k];
            } else {
              m = 0;
            }
          } while (!isDisplayablePermission(localPermissionInfo, arrayOfInt[i], m));
          String str2 = group;
          paramPackageInfo1 = str2;
          Object localObject = paramPackageInfo1;
          if (paramPackageInfo1 == null)
          {
            localObject = packageName;
            group = ((String)localObject);
          }
          if ((MyPermissionGroupInfo)mPermGroups.get(localObject) == null)
          {
            paramPackageInfo1 = null;
            if (str2 != null) {
              paramPackageInfo1 = mPm.getPermissionGroupInfo(str2, 0);
            }
            if (paramPackageInfo1 != null)
            {
              localObject = new android/widget/AppSecurityPermissions$MyPermissionGroupInfo;
              ((MyPermissionGroupInfo)localObject).<init>(paramPackageInfo1);
              paramPackageInfo1 = (PackageInfo)localObject;
            }
            else
            {
              group = packageName;
              if ((MyPermissionGroupInfo)mPermGroups.get(group) == null) {
                new MyPermissionGroupInfo(localPermissionInfo);
              }
              paramPackageInfo1 = new MyPermissionGroupInfo(localPermissionInfo);
            }
            mPermGroups.put(group, paramPackageInfo1);
          }
          boolean bool;
          if ((paramPackageInfo2 != null) && ((m & 0x2) == 0)) {
            bool = true;
          } else {
            bool = false;
          }
          paramPackageInfo1 = new android/widget/AppSecurityPermissions$MyPermissionInfo;
          paramPackageInfo1.<init>(localPermissionInfo);
          mNewReqFlags = arrayOfInt[i];
          mExistingReqFlags = m;
          mNew = bool;
          try
          {
            paramSet.add(paramPackageInfo1);
          }
          catch (PackageManager.NameNotFoundException paramPackageInfo1) {}
          paramPackageInfo1 = new StringBuilder();
        }
        catch (PackageManager.NameNotFoundException paramPackageInfo1) {}
        paramPackageInfo1.append("Ignoring unknown permission:");
        paramPackageInfo1.append(str1);
        Log.i("AppSecurityPermissions", paramPackageInfo1.toString());
      }
      return;
    }
  }
  
  private void getAllUsedPermissions(int paramInt, Set<MyPermissionInfo> paramSet)
  {
    String[] arrayOfString = mPm.getPackagesForUid(paramInt);
    if ((arrayOfString != null) && (arrayOfString.length != 0))
    {
      int i = arrayOfString.length;
      for (paramInt = 0; paramInt < i; paramInt++) {
        getPermissionsForPackage(arrayOfString[paramInt], paramSet);
      }
      return;
    }
  }
  
  public static View getPermissionItemView(Context paramContext, CharSequence paramCharSequence1, CharSequence paramCharSequence2, boolean paramBoolean)
  {
    LayoutInflater localLayoutInflater = (LayoutInflater)paramContext.getSystemService("layout_inflater");
    int i;
    if (paramBoolean) {
      i = 17302557;
    } else {
      i = 17303038;
    }
    return getPermissionItemViewOld(paramContext, localLayoutInflater, paramCharSequence1, paramCharSequence2, paramBoolean, paramContext.getDrawable(i));
  }
  
  private static PermissionItemView getPermissionItemView(Context paramContext, LayoutInflater paramLayoutInflater, MyPermissionGroupInfo paramMyPermissionGroupInfo, MyPermissionInfo paramMyPermissionInfo, boolean paramBoolean1, CharSequence paramCharSequence, String paramString, boolean paramBoolean2)
  {
    int i;
    if ((flags & 0x1) != 0) {
      i = 17367096;
    } else {
      i = 17367095;
    }
    paramContext = (PermissionItemView)paramLayoutInflater.inflate(i, null);
    paramContext.setPermission(paramMyPermissionGroupInfo, paramMyPermissionInfo, paramBoolean1, paramCharSequence, paramString, paramBoolean2);
    return paramContext;
  }
  
  private PermissionItemView getPermissionItemView(MyPermissionGroupInfo paramMyPermissionGroupInfo, MyPermissionInfo paramMyPermissionInfo, boolean paramBoolean1, CharSequence paramCharSequence, boolean paramBoolean2)
  {
    return getPermissionItemView(mContext, mInflater, paramMyPermissionGroupInfo, paramMyPermissionInfo, paramBoolean1, paramCharSequence, mPackageName, paramBoolean2);
  }
  
  private static View getPermissionItemViewOld(Context paramContext, LayoutInflater paramLayoutInflater, CharSequence paramCharSequence1, CharSequence paramCharSequence2, boolean paramBoolean, Drawable paramDrawable)
  {
    View localView = paramLayoutInflater.inflate(17367097, null);
    paramContext = (TextView)localView.findViewById(16909230);
    paramLayoutInflater = (TextView)localView.findViewById(16909232);
    ((ImageView)localView.findViewById(16909226)).setImageDrawable(paramDrawable);
    if (paramCharSequence1 != null)
    {
      paramContext.setText(paramCharSequence1);
      paramLayoutInflater.setText(paramCharSequence2);
    }
    else
    {
      paramContext.setText(paramCharSequence2);
      paramLayoutInflater.setVisibility(8);
    }
    return localView;
  }
  
  private List<MyPermissionInfo> getPermissionList(MyPermissionGroupInfo paramMyPermissionGroupInfo, int paramInt)
  {
    if (paramInt == 4) {
      return mNewPermissions;
    }
    return mAllPermissions;
  }
  
  private void getPermissionsForPackage(String paramString, Set<MyPermissionInfo> paramSet)
  {
    try
    {
      PackageInfo localPackageInfo = mPm.getPackageInfo(paramString, 4096);
      extractPerms(localPackageInfo, paramSet, localPackageInfo);
    }
    catch (PackageManager.NameNotFoundException paramSet)
    {
      paramSet = new StringBuilder();
      paramSet.append("Couldn't retrieve permissions for package: ");
      paramSet.append(paramString);
      Log.w("AppSecurityPermissions", paramSet.toString());
    }
  }
  
  private View getPermissionsView(int paramInt, boolean paramBoolean)
  {
    LinearLayout localLinearLayout1 = (LinearLayout)mInflater.inflate(17367098, null);
    LinearLayout localLinearLayout2 = (LinearLayout)localLinearLayout1.findViewById(16909233);
    View localView = localLinearLayout1.findViewById(16909167);
    displayPermissions(mPermGroupsList, localLinearLayout2, paramInt, paramBoolean);
    if (localLinearLayout2.getChildCount() <= 0) {
      localView.setVisibility(0);
    }
    return localLinearLayout1;
  }
  
  private boolean isDisplayablePermission(PermissionInfo paramPermissionInfo, int paramInt1, int paramInt2)
  {
    int i = protectionLevel & 0xF;
    int j;
    if (i == 0) {
      j = 1;
    } else {
      j = 0;
    }
    if (j != 0) {
      return false;
    }
    if ((i != 1) && ((protectionLevel & 0x80) == 0)) {
      j = 0;
    } else {
      j = 1;
    }
    if ((paramInt1 & 0x1) != 0) {
      i = 1;
    } else {
      i = 0;
    }
    int k;
    if ((protectionLevel & 0x20) != 0) {
      k = 1;
    } else {
      k = 0;
    }
    if ((paramInt2 & 0x2) != 0) {
      paramInt2 = 1;
    } else {
      paramInt2 = 0;
    }
    if ((paramInt1 & 0x2) != 0) {
      paramInt1 = 1;
    } else {
      paramInt1 = 0;
    }
    if ((j != 0) && ((i != 0) || (paramInt2 != 0) || (paramInt1 != 0))) {
      return true;
    }
    return (k != 0) && (paramInt2 != 0);
  }
  
  private void setPermissions(List<MyPermissionInfo> paramList)
  {
    MyPermissionGroupInfo localMyPermissionGroupInfo;
    if (paramList != null)
    {
      Iterator localIterator = paramList.iterator();
      while (localIterator.hasNext())
      {
        paramList = (MyPermissionInfo)localIterator.next();
        if (isDisplayablePermission(paramList, mNewReqFlags, mExistingReqFlags))
        {
          localMyPermissionGroupInfo = (MyPermissionGroupInfo)mPermGroups.get(group);
          if (localMyPermissionGroupInfo != null)
          {
            mLabel = paramList.loadSafeLabel(mPm, 20000.0F, 5);
            addPermToList(mAllPermissions, paramList);
            if (mNew) {
              addPermToList(mNewPermissions, paramList);
            }
          }
        }
      }
    }
    paramList = mPermGroups.values().iterator();
    while (paramList.hasNext())
    {
      localMyPermissionGroupInfo = (MyPermissionGroupInfo)paramList.next();
      if ((labelRes == 0) && (nonLocalizedLabel == null)) {
        try
        {
          mLabel = mPm.getApplicationInfo(packageName, 0).loadSafeLabel(mPm, 20000.0F, 5);
        }
        catch (PackageManager.NameNotFoundException localNameNotFoundException)
        {
          mLabel = localMyPermissionGroupInfo.loadSafeLabel(mPm, 20000.0F, 5);
        }
      } else {
        mLabel = localMyPermissionGroupInfo.loadSafeLabel(mPm, 20000.0F, 5);
      }
      mPermGroupsList.add(localMyPermissionGroupInfo);
    }
    Collections.sort(mPermGroupsList, mPermGroupComparator);
  }
  
  public int getPermissionCount()
  {
    return getPermissionCount(65535);
  }
  
  public int getPermissionCount(int paramInt)
  {
    int i = 0;
    for (int j = 0; j < mPermGroupsList.size(); j++) {
      i += getPermissionList((MyPermissionGroupInfo)mPermGroupsList.get(j), paramInt).size();
    }
    return i;
  }
  
  public View getPermissionsView()
  {
    return getPermissionsView(65535, false);
  }
  
  public View getPermissionsView(int paramInt)
  {
    return getPermissionsView(paramInt, false);
  }
  
  public View getPermissionsViewWithRevokeButtons()
  {
    return getPermissionsView(65535, true);
  }
  
  static class MyPermissionGroupInfo
    extends PermissionGroupInfo
  {
    final ArrayList<AppSecurityPermissions.MyPermissionInfo> mAllPermissions = new ArrayList();
    CharSequence mLabel;
    final ArrayList<AppSecurityPermissions.MyPermissionInfo> mNewPermissions = new ArrayList();
    
    MyPermissionGroupInfo(PermissionGroupInfo paramPermissionGroupInfo)
    {
      super();
    }
    
    MyPermissionGroupInfo(PermissionInfo paramPermissionInfo)
    {
      name = packageName;
      packageName = packageName;
    }
    
    public Drawable loadGroupIcon(Context paramContext, PackageManager paramPackageManager)
    {
      if (icon != 0) {
        return loadUnbadgedIcon(paramPackageManager);
      }
      return paramContext.getDrawable(17302966);
    }
  }
  
  private static class MyPermissionInfo
    extends PermissionInfo
  {
    int mExistingReqFlags;
    CharSequence mLabel;
    boolean mNew;
    int mNewReqFlags;
    
    MyPermissionInfo(PermissionInfo paramPermissionInfo)
    {
      super();
    }
  }
  
  private static class PermissionGroupInfoComparator
    implements Comparator<AppSecurityPermissions.MyPermissionGroupInfo>
  {
    private final Collator sCollator = Collator.getInstance();
    
    private PermissionGroupInfoComparator() {}
    
    public final int compare(AppSecurityPermissions.MyPermissionGroupInfo paramMyPermissionGroupInfo1, AppSecurityPermissions.MyPermissionGroupInfo paramMyPermissionGroupInfo2)
    {
      return sCollator.compare(mLabel, mLabel);
    }
  }
  
  private static class PermissionInfoComparator
    implements Comparator<AppSecurityPermissions.MyPermissionInfo>
  {
    private final Collator sCollator = Collator.getInstance();
    
    PermissionInfoComparator() {}
    
    public final int compare(AppSecurityPermissions.MyPermissionInfo paramMyPermissionInfo1, AppSecurityPermissions.MyPermissionInfo paramMyPermissionInfo2)
    {
      return sCollator.compare(mLabel, mLabel);
    }
  }
  
  public static class PermissionItemView
    extends LinearLayout
    implements View.OnClickListener
  {
    AlertDialog mDialog;
    AppSecurityPermissions.MyPermissionGroupInfo mGroup;
    private String mPackageName;
    AppSecurityPermissions.MyPermissionInfo mPerm;
    private boolean mShowRevokeUI = false;
    
    public PermissionItemView(Context paramContext, AttributeSet paramAttributeSet)
    {
      super(paramAttributeSet);
      setClickable(true);
    }
    
    private void addRevokeUIIfNecessary(AlertDialog.Builder paramBuilder)
    {
      if (!mShowRevokeUI) {
        return;
      }
      int i = mPerm.mExistingReqFlags;
      int j = 1;
      if ((i & 0x1) == 0) {
        j = 0;
      }
      if (j != 0) {
        return;
      }
      paramBuilder.setNegativeButton(17040915, new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
        {
          getContext().getPackageManager().revokeRuntimePermission(mPackageName, mPerm.name, new UserHandle(mContext.getUserId()));
          setVisibility(8);
        }
      });
      paramBuilder.setPositiveButton(17039370, null);
    }
    
    public void onClick(View paramView)
    {
      if ((mGroup != null) && (mPerm != null))
      {
        if (mDialog != null) {
          mDialog.dismiss();
        }
        PackageManager localPackageManager = getContext().getPackageManager();
        AlertDialog.Builder localBuilder = new AlertDialog.Builder(getContext());
        localBuilder.setTitle(mGroup.mLabel);
        if (mPerm.descriptionRes != 0)
        {
          localBuilder.setMessage(mPerm.loadDescription(localPackageManager));
        }
        else
        {
          try
          {
            paramView = localPackageManager.getApplicationInfo(mPerm.packageName, 0).loadLabel(localPackageManager);
          }
          catch (PackageManager.NameNotFoundException paramView)
          {
            paramView = mPerm.packageName;
          }
          StringBuilder localStringBuilder = new StringBuilder(128);
          localStringBuilder.append(getContext().getString(17040798, new Object[] { paramView }));
          localStringBuilder.append("\n\n");
          localStringBuilder.append(mPerm.name);
          localBuilder.setMessage(localStringBuilder.toString());
        }
        localBuilder.setCancelable(true);
        localBuilder.setIcon(mGroup.loadGroupIcon(getContext(), localPackageManager));
        addRevokeUIIfNecessary(localBuilder);
        mDialog = localBuilder.show();
        mDialog.setCanceledOnTouchOutside(true);
      }
    }
    
    protected void onDetachedFromWindow()
    {
      super.onDetachedFromWindow();
      if (mDialog != null) {
        mDialog.dismiss();
      }
    }
    
    public void setPermission(AppSecurityPermissions.MyPermissionGroupInfo paramMyPermissionGroupInfo, AppSecurityPermissions.MyPermissionInfo paramMyPermissionInfo, boolean paramBoolean1, CharSequence paramCharSequence, String paramString, boolean paramBoolean2)
    {
      mGroup = paramMyPermissionGroupInfo;
      mPerm = paramMyPermissionInfo;
      mShowRevokeUI = paramBoolean2;
      mPackageName = paramString;
      ImageView localImageView = (ImageView)findViewById(16909226);
      TextView localTextView = (TextView)findViewById(16909229);
      Object localObject = getContext().getPackageManager();
      paramString = null;
      if (paramBoolean1) {
        paramString = paramMyPermissionGroupInfo.loadGroupIcon(getContext(), (PackageManager)localObject);
      }
      localObject = mLabel;
      paramMyPermissionGroupInfo = (AppSecurityPermissions.MyPermissionGroupInfo)localObject;
      if (mNew)
      {
        paramMyPermissionGroupInfo = (AppSecurityPermissions.MyPermissionGroupInfo)localObject;
        if (paramCharSequence != null)
        {
          paramMyPermissionGroupInfo = new SpannableStringBuilder();
          paramMyPermissionInfo = Parcel.obtain();
          TextUtils.writeToParcel(paramCharSequence, paramMyPermissionInfo, 0);
          paramMyPermissionInfo.setDataPosition(0);
          paramCharSequence = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramMyPermissionInfo);
          paramMyPermissionInfo.recycle();
          paramMyPermissionGroupInfo.append(paramCharSequence);
          paramMyPermissionGroupInfo.append((CharSequence)localObject);
        }
      }
      localImageView.setImageDrawable(paramString);
      localTextView.setText(paramMyPermissionGroupInfo);
      setOnClickListener(this);
    }
  }
}
