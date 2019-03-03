package android.app;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.ComponentInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ResolveInfo.DisplayNameComparator;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filter.FilterResults;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class LauncherActivity
  extends ListActivity
{
  IconResizer mIconResizer;
  Intent mIntent;
  PackageManager mPackageManager;
  
  public LauncherActivity() {}
  
  private void updateAlertTitle()
  {
    TextView localTextView = (TextView)findViewById(16908726);
    if (localTextView != null) {
      localTextView.setText(getTitle());
    }
  }
  
  private void updateButtonText()
  {
    Button localButton = (Button)findViewById(16908313);
    if (localButton != null) {
      localButton.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          finish();
        }
      });
    }
  }
  
  protected Intent getTargetIntent()
  {
    return new Intent();
  }
  
  protected Intent intentForPosition(int paramInt)
  {
    return ((ActivityAdapter)mAdapter).intentForPosition(paramInt);
  }
  
  protected ListItem itemForPosition(int paramInt)
  {
    return ((ActivityAdapter)mAdapter).itemForPosition(paramInt);
  }
  
  public List<ListItem> makeListItems()
  {
    List localList = onQueryPackageManager(mIntent);
    onSortResultList(localList);
    ArrayList localArrayList = new ArrayList(localList.size());
    int i = localList.size();
    for (int j = 0; j < i; j++)
    {
      ResolveInfo localResolveInfo = (ResolveInfo)localList.get(j);
      localArrayList.add(new ListItem(mPackageManager, localResolveInfo, null));
    }
    return localArrayList;
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    mPackageManager = getPackageManager();
    if (!mPackageManager.hasSystemFeature("android.hardware.type.watch"))
    {
      requestWindowFeature(5);
      setProgressBarIndeterminateVisibility(true);
    }
    onSetContentView();
    mIconResizer = new IconResizer();
    mIntent = new Intent(getTargetIntent());
    mIntent.setComponent(null);
    mAdapter = new ActivityAdapter(mIconResizer);
    setListAdapter(mAdapter);
    getListView().setTextFilterEnabled(true);
    updateAlertTitle();
    updateButtonText();
    if (!mPackageManager.hasSystemFeature("android.hardware.type.watch")) {
      setProgressBarIndeterminateVisibility(false);
    }
  }
  
  protected boolean onEvaluateShowIcons()
  {
    return true;
  }
  
  protected void onListItemClick(ListView paramListView, View paramView, int paramInt, long paramLong)
  {
    startActivity(intentForPosition(paramInt));
  }
  
  protected List<ResolveInfo> onQueryPackageManager(Intent paramIntent)
  {
    return mPackageManager.queryIntentActivities(paramIntent, 0);
  }
  
  protected void onSetContentView()
  {
    setContentView(17367077);
  }
  
  protected void onSortResultList(List<ResolveInfo> paramList)
  {
    Collections.sort(paramList, new ResolveInfo.DisplayNameComparator(mPackageManager));
  }
  
  public void setTitle(int paramInt)
  {
    super.setTitle(paramInt);
    updateAlertTitle();
  }
  
  public void setTitle(CharSequence paramCharSequence)
  {
    super.setTitle(paramCharSequence);
    updateAlertTitle();
  }
  
  private class ActivityAdapter
    extends BaseAdapter
    implements Filterable
  {
    private final Object lock = new Object();
    protected List<LauncherActivity.ListItem> mActivitiesList;
    private Filter mFilter;
    protected final LauncherActivity.IconResizer mIconResizer;
    protected final LayoutInflater mInflater;
    private ArrayList<LauncherActivity.ListItem> mOriginalValues;
    private final boolean mShowIcons;
    
    public ActivityAdapter(LauncherActivity.IconResizer paramIconResizer)
    {
      mIconResizer = paramIconResizer;
      mInflater = ((LayoutInflater)getSystemService("layout_inflater"));
      mShowIcons = onEvaluateShowIcons();
      mActivitiesList = makeListItems();
    }
    
    private void bindView(View paramView, LauncherActivity.ListItem paramListItem)
    {
      paramView = (TextView)paramView;
      paramView.setText(label);
      if (mShowIcons)
      {
        if (icon == null) {
          icon = mIconResizer.createIconThumbnail(resolveInfo.loadIcon(getPackageManager()));
        }
        paramView.setCompoundDrawablesRelativeWithIntrinsicBounds(icon, null, null, null);
      }
    }
    
    public int getCount()
    {
      int i;
      if (mActivitiesList != null) {
        i = mActivitiesList.size();
      } else {
        i = 0;
      }
      return i;
    }
    
    public Filter getFilter()
    {
      if (mFilter == null) {
        mFilter = new ArrayFilter(null);
      }
      return mFilter;
    }
    
    public Object getItem(int paramInt)
    {
      return Integer.valueOf(paramInt);
    }
    
    public long getItemId(int paramInt)
    {
      return paramInt;
    }
    
    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if (paramView == null) {
        paramView = mInflater.inflate(17367078, paramViewGroup, false);
      }
      bindView(paramView, (LauncherActivity.ListItem)mActivitiesList.get(paramInt));
      return paramView;
    }
    
    public Intent intentForPosition(int paramInt)
    {
      if (mActivitiesList == null) {
        return null;
      }
      Intent localIntent = new Intent(mIntent);
      LauncherActivity.ListItem localListItem = (LauncherActivity.ListItem)mActivitiesList.get(paramInt);
      localIntent.setClassName(packageName, className);
      if (extras != null) {
        localIntent.putExtras(extras);
      }
      return localIntent;
    }
    
    public LauncherActivity.ListItem itemForPosition(int paramInt)
    {
      if (mActivitiesList == null) {
        return null;
      }
      return (LauncherActivity.ListItem)mActivitiesList.get(paramInt);
    }
    
    private class ArrayFilter
      extends Filter
    {
      private ArrayFilter() {}
      
      protected Filter.FilterResults performFiltering(CharSequence arg1)
      {
        Filter.FilterResults localFilterResults = new Filter.FilterResults();
        Object localObject3;
        Object localObject4;
        if (mOriginalValues == null) {
          synchronized (lock)
          {
            localObject3 = LauncherActivity.ActivityAdapter.this;
            localObject4 = new java/util/ArrayList;
            ((ArrayList)localObject4).<init>(mActivitiesList);
            LauncherActivity.ActivityAdapter.access$102((LauncherActivity.ActivityAdapter)localObject3, (ArrayList)localObject4);
          }
        }
        if ((??? != null) && (???.length() != 0))
        {
          localObject4 = ???.toString().toLowerCase();
          ??? = mOriginalValues;
          int i = ((ArrayList)???).size();
          ??? = new ArrayList(i);
          for (int j = 0; j < i; j++)
          {
            LauncherActivity.ListItem localListItem = (LauncherActivity.ListItem)((ArrayList)???).get(j);
            localObject3 = label.toString().toLowerCase().split(" ");
            int k = localObject3.length;
            for (int m = 0; m < k; m++) {
              if (localObject3[m].startsWith((String)localObject4))
              {
                ???.add(localListItem);
                break;
              }
            }
          }
          values = ???;
          count = ???.size();
        }
        synchronized (lock)
        {
          ??? = new java/util/ArrayList;
          ((ArrayList)???).<init>(mOriginalValues);
          values = ???;
          count = ((ArrayList)???).size();
          return localFilterResults;
        }
      }
      
      protected void publishResults(CharSequence paramCharSequence, Filter.FilterResults paramFilterResults)
      {
        mActivitiesList = ((List)values);
        if (count > 0) {
          notifyDataSetChanged();
        } else {
          notifyDataSetInvalidated();
        }
      }
    }
  }
  
  public class IconResizer
  {
    private Canvas mCanvas = new Canvas();
    private int mIconHeight = -1;
    private int mIconWidth = -1;
    private final Rect mOldBounds = new Rect();
    
    public IconResizer()
    {
      mCanvas.setDrawFilter(new PaintFlagsDrawFilter(4, 2));
      int i = (int)getResources().getDimension(17104896);
      mIconHeight = i;
      mIconWidth = i;
    }
    
    public Drawable createIconThumbnail(Drawable paramDrawable)
    {
      int i = mIconWidth;
      int j = mIconHeight;
      int k = paramDrawable.getIntrinsicWidth();
      int m = paramDrawable.getIntrinsicHeight();
      if ((paramDrawable instanceof PaintDrawable))
      {
        localObject = (PaintDrawable)paramDrawable;
        ((PaintDrawable)localObject).setIntrinsicWidth(i);
        ((PaintDrawable)localObject).setIntrinsicHeight(j);
      }
      Object localObject = paramDrawable;
      if (i > 0)
      {
        localObject = paramDrawable;
        if (j > 0)
        {
          Canvas localCanvas;
          int n;
          if ((i >= k) && (j >= m))
          {
            localObject = paramDrawable;
            if (k < i)
            {
              localObject = paramDrawable;
              if (m < j)
              {
                localObject = Bitmap.Config.ARGB_8888;
                localObject = Bitmap.createBitmap(mIconWidth, mIconHeight, (Bitmap.Config)localObject);
                localCanvas = mCanvas;
                localCanvas.setBitmap((Bitmap)localObject);
                mOldBounds.set(paramDrawable.getBounds());
                n = (i - k) / 2;
                i = (j - m) / 2;
                paramDrawable.setBounds(n, i, n + k, i + m);
                paramDrawable.draw(localCanvas);
                paramDrawable.setBounds(mOldBounds);
                localObject = new BitmapDrawable(getResources(), (Bitmap)localObject);
                localCanvas.setBitmap(null);
              }
            }
          }
          else
          {
            float f = k / m;
            if (k > m)
            {
              n = (int)(i / f);
            }
            else
            {
              n = j;
              if (m > k)
              {
                i = (int)(j * f);
                n = j;
              }
            }
            if (paramDrawable.getOpacity() != -1) {
              localObject = Bitmap.Config.ARGB_8888;
            } else {
              localObject = Bitmap.Config.RGB_565;
            }
            localObject = Bitmap.createBitmap(mIconWidth, mIconHeight, (Bitmap.Config)localObject);
            localCanvas = mCanvas;
            localCanvas.setBitmap((Bitmap)localObject);
            mOldBounds.set(paramDrawable.getBounds());
            m = (mIconWidth - i) / 2;
            j = (mIconHeight - n) / 2;
            paramDrawable.setBounds(m, j, m + i, j + n);
            paramDrawable.draw(localCanvas);
            paramDrawable.setBounds(mOldBounds);
            localObject = new BitmapDrawable(getResources(), (Bitmap)localObject);
            localCanvas.setBitmap(null);
          }
        }
      }
      return localObject;
    }
  }
  
  public static class ListItem
  {
    public String className;
    public Bundle extras;
    public Drawable icon;
    public CharSequence label;
    public String packageName;
    public ResolveInfo resolveInfo;
    
    public ListItem() {}
    
    ListItem(PackageManager paramPackageManager, ResolveInfo paramResolveInfo, LauncherActivity.IconResizer paramIconResizer)
    {
      resolveInfo = paramResolveInfo;
      label = paramResolveInfo.loadLabel(paramPackageManager);
      ActivityInfo localActivityInfo = activityInfo;
      Object localObject = localActivityInfo;
      if (localActivityInfo == null) {
        localObject = serviceInfo;
      }
      if ((label == null) && (localObject != null)) {
        label = activityInfo.name;
      }
      if (paramIconResizer != null) {
        icon = paramIconResizer.createIconThumbnail(paramResolveInfo.loadIcon(paramPackageManager));
      }
      packageName = applicationInfo.packageName;
      className = name;
    }
  }
}
