package com.android.internal.app;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.internal.widget.ResolverDrawerLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class AccessibilityButtonChooserActivity
  extends Activity
{
  private static final String MAGNIFICATION_COMPONENT_ID = "com.android.server.accessibility.MagnificationController";
  private AccessibilityButtonTarget mMagnificationTarget = null;
  private List<AccessibilityButtonTarget> mTargets = null;
  
  public AccessibilityButtonChooserActivity() {}
  
  private static List<AccessibilityButtonTarget> getServiceAccessibilityButtonTargets(Context paramContext)
  {
    Object localObject = ((AccessibilityManager)paramContext.getSystemService("accessibility")).getEnabledAccessibilityServiceList(-1);
    if (localObject == null) {
      return Collections.emptyList();
    }
    ArrayList localArrayList = new ArrayList(((List)localObject).size());
    Iterator localIterator = ((List)localObject).iterator();
    while (localIterator.hasNext())
    {
      localObject = (AccessibilityServiceInfo)localIterator.next();
      if ((flags & 0x100) != 0) {
        localArrayList.add(new AccessibilityButtonTarget(paramContext, (AccessibilityServiceInfo)localObject));
      }
    }
    return localArrayList;
  }
  
  private void onTargetSelected(AccessibilityButtonTarget paramAccessibilityButtonTarget)
  {
    Settings.Secure.putString(getContentResolver(), "accessibility_button_target_component", paramAccessibilityButtonTarget.getId());
    finish();
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(17367064);
    paramBundle = (ResolverDrawerLayout)findViewById(16908873);
    if (paramBundle != null) {
      paramBundle.setOnDismissedListener(new _..Lambda.EK3sgUmlvAVQupMeTV9feOrWuPE(this));
    }
    if (TextUtils.isEmpty(Settings.Secure.getString(getContentResolver(), "accessibility_button_target_component"))) {
      ((TextView)findViewById(16908678)).setVisibility(0);
    }
    mMagnificationTarget = new AccessibilityButtonTarget(this, "com.android.server.accessibility.MagnificationController", 17039424, 17302527);
    mTargets = getServiceAccessibilityButtonTargets(this);
    if (Settings.Secure.getInt(getContentResolver(), "accessibility_display_magnification_navbar_enabled", 0) == 1) {
      mTargets.add(mMagnificationTarget);
    }
    if (mTargets.size() < 2) {
      finish();
    }
    paramBundle = (GridView)findViewById(16908677);
    paramBundle.setAdapter(new TargetAdapter(null));
    paramBundle.setOnItemClickListener(new _..Lambda.AccessibilityButtonChooserActivity.VBT2N_0vKxB2VkOg6zxi5sAX6xc(this));
  }
  
  private static class AccessibilityButtonTarget
  {
    public Drawable mDrawable;
    public String mId;
    public CharSequence mLabel;
    
    public AccessibilityButtonTarget(Context paramContext, AccessibilityServiceInfo paramAccessibilityServiceInfo)
    {
      mId = paramAccessibilityServiceInfo.getComponentName().flattenToString();
      mLabel = paramAccessibilityServiceInfo.getResolveInfo().loadLabel(paramContext.getPackageManager());
      mDrawable = paramAccessibilityServiceInfo.getResolveInfo().loadIcon(paramContext.getPackageManager());
    }
    
    public AccessibilityButtonTarget(Context paramContext, String paramString, int paramInt1, int paramInt2)
    {
      mId = paramString;
      mLabel = paramContext.getText(paramInt1);
      mDrawable = paramContext.getDrawable(paramInt2);
    }
    
    public Drawable getDrawable()
    {
      return mDrawable;
    }
    
    public String getId()
    {
      return mId;
    }
    
    public CharSequence getLabel()
    {
      return mLabel;
    }
  }
  
  private class TargetAdapter
    extends BaseAdapter
  {
    private TargetAdapter() {}
    
    public int getCount()
    {
      return mTargets.size();
    }
    
    public Object getItem(int paramInt)
    {
      return null;
    }
    
    public long getItemId(int paramInt)
    {
      return paramInt;
    }
    
    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      View localView = getLayoutInflater().inflate(17367065, paramViewGroup, false);
      paramViewGroup = (AccessibilityButtonChooserActivity.AccessibilityButtonTarget)mTargets.get(paramInt);
      ImageView localImageView = (ImageView)localView.findViewById(16908679);
      paramView = (TextView)localView.findViewById(16908680);
      localImageView.setImageDrawable(paramViewGroup.getDrawable());
      paramView.setText(paramViewGroup.getLabel());
      return localView;
    }
  }
}
