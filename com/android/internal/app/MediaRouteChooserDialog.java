package com.android.internal.app;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources.Theme;
import android.media.MediaRouter;
import android.media.MediaRouter.RouteInfo;
import android.media.MediaRouter.SimpleCallback;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import java.util.Comparator;

public class MediaRouteChooserDialog
  extends Dialog
{
  private RouteAdapter mAdapter;
  private boolean mAttachedToWindow;
  private final MediaRouterCallback mCallback;
  private Button mExtendedSettingsButton;
  private View.OnClickListener mExtendedSettingsClickListener;
  private ListView mListView;
  private int mRouteTypes;
  private final MediaRouter mRouter;
  
  public MediaRouteChooserDialog(Context paramContext, int paramInt)
  {
    super(paramContext, paramInt);
    mRouter = ((MediaRouter)paramContext.getSystemService("media_router"));
    mCallback = new MediaRouterCallback(null);
  }
  
  static boolean isLightTheme(Context paramContext)
  {
    TypedValue localTypedValue = new TypedValue();
    paramContext = paramContext.getTheme();
    boolean bool = true;
    if ((!paramContext.resolveAttribute(17891418, localTypedValue, true)) || (data == 0)) {
      bool = false;
    }
    return bool;
  }
  
  private void updateExtendedSettingsButton()
  {
    if (mExtendedSettingsButton != null)
    {
      mExtendedSettingsButton.setOnClickListener(mExtendedSettingsClickListener);
      Button localButton = mExtendedSettingsButton;
      int i;
      if (mExtendedSettingsClickListener != null) {
        i = 0;
      } else {
        i = 8;
      }
      localButton.setVisibility(i);
    }
  }
  
  public int getRouteTypes()
  {
    return mRouteTypes;
  }
  
  public void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    mAttachedToWindow = true;
    mRouter.addCallback(mRouteTypes, mCallback, 1);
    refreshRoutes();
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    getWindow().requestFeature(3);
    setContentView(17367205);
    int i;
    if (mRouteTypes == 4) {
      i = 17040334;
    } else {
      i = 17040333;
    }
    setTitle(i);
    paramBundle = getWindow();
    if (isLightTheme(getContext())) {
      i = 17302878;
    } else {
      i = 17302877;
    }
    paramBundle.setFeatureDrawableResource(3, i);
    mAdapter = new RouteAdapter(getContext());
    mListView = ((ListView)findViewById(16909115));
    mListView.setAdapter(mAdapter);
    mListView.setOnItemClickListener(mAdapter);
    mListView.setEmptyView(findViewById(16908292));
    mExtendedSettingsButton = ((Button)findViewById(16909114));
    updateExtendedSettingsButton();
  }
  
  public void onDetachedFromWindow()
  {
    mAttachedToWindow = false;
    mRouter.removeCallback(mCallback);
    super.onDetachedFromWindow();
  }
  
  public boolean onFilterRoute(MediaRouter.RouteInfo paramRouteInfo)
  {
    boolean bool;
    if ((!paramRouteInfo.isDefault()) && (paramRouteInfo.isEnabled()) && (paramRouteInfo.matchesTypes(mRouteTypes))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void refreshRoutes()
  {
    if (mAttachedToWindow) {
      mAdapter.update();
    }
  }
  
  public void setExtendedSettingsClickListener(View.OnClickListener paramOnClickListener)
  {
    if (paramOnClickListener != mExtendedSettingsClickListener)
    {
      mExtendedSettingsClickListener = paramOnClickListener;
      updateExtendedSettingsButton();
    }
  }
  
  public void setRouteTypes(int paramInt)
  {
    if (mRouteTypes != paramInt)
    {
      mRouteTypes = paramInt;
      if (mAttachedToWindow)
      {
        mRouter.removeCallback(mCallback);
        mRouter.addCallback(paramInt, mCallback, 1);
      }
      refreshRoutes();
    }
  }
  
  private final class MediaRouterCallback
    extends MediaRouter.SimpleCallback
  {
    private MediaRouterCallback() {}
    
    public void onRouteAdded(MediaRouter paramMediaRouter, MediaRouter.RouteInfo paramRouteInfo)
    {
      refreshRoutes();
    }
    
    public void onRouteChanged(MediaRouter paramMediaRouter, MediaRouter.RouteInfo paramRouteInfo)
    {
      refreshRoutes();
    }
    
    public void onRouteRemoved(MediaRouter paramMediaRouter, MediaRouter.RouteInfo paramRouteInfo)
    {
      refreshRoutes();
    }
    
    public void onRouteSelected(MediaRouter paramMediaRouter, int paramInt, MediaRouter.RouteInfo paramRouteInfo)
    {
      dismiss();
    }
  }
  
  private final class RouteAdapter
    extends ArrayAdapter<MediaRouter.RouteInfo>
    implements AdapterView.OnItemClickListener
  {
    private final LayoutInflater mInflater;
    
    public RouteAdapter(Context paramContext)
    {
      super(0);
      mInflater = LayoutInflater.from(paramContext);
    }
    
    public boolean areAllItemsEnabled()
    {
      return false;
    }
    
    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      Object localObject1 = paramView;
      paramView = (View)localObject1;
      if (localObject1 == null) {
        paramView = mInflater.inflate(17367207, paramViewGroup, false);
      }
      localObject1 = (MediaRouter.RouteInfo)getItem(paramInt);
      Object localObject2 = (TextView)paramView.findViewById(16908308);
      paramViewGroup = (TextView)paramView.findViewById(16908309);
      ((TextView)localObject2).setText(((MediaRouter.RouteInfo)localObject1).getName());
      localObject2 = ((MediaRouter.RouteInfo)localObject1).getDescription();
      if (TextUtils.isEmpty((CharSequence)localObject2))
      {
        paramViewGroup.setVisibility(8);
        paramViewGroup.setText("");
      }
      else
      {
        paramViewGroup.setVisibility(0);
        paramViewGroup.setText((CharSequence)localObject2);
      }
      paramView.setEnabled(((MediaRouter.RouteInfo)localObject1).isEnabled());
      return paramView;
    }
    
    public boolean isEnabled(int paramInt)
    {
      return ((MediaRouter.RouteInfo)getItem(paramInt)).isEnabled();
    }
    
    public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
    {
      paramAdapterView = (MediaRouter.RouteInfo)getItem(paramInt);
      if (paramAdapterView.isEnabled())
      {
        paramAdapterView.select();
        dismiss();
      }
    }
    
    public void update()
    {
      clear();
      int i = mRouter.getRouteCount();
      for (int j = 0; j < i; j++)
      {
        MediaRouter.RouteInfo localRouteInfo = mRouter.getRouteAt(j);
        if (onFilterRoute(localRouteInfo)) {
          add(localRouteInfo);
        }
      }
      sort(MediaRouteChooserDialog.RouteComparator.sInstance);
      notifyDataSetChanged();
    }
  }
  
  private static final class RouteComparator
    implements Comparator<MediaRouter.RouteInfo>
  {
    public static final RouteComparator sInstance = new RouteComparator();
    
    private RouteComparator() {}
    
    public int compare(MediaRouter.RouteInfo paramRouteInfo1, MediaRouter.RouteInfo paramRouteInfo2)
    {
      return paramRouteInfo1.getName().toString().compareTo(paramRouteInfo2.getName().toString());
    }
  }
}
