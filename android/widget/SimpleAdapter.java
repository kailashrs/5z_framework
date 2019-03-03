package android.widget;

import android.content.Context;
import android.content.res.Resources.Theme;
import android.net.Uri;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SimpleAdapter
  extends BaseAdapter
  implements Filterable, ThemedSpinnerAdapter
{
  private List<? extends Map<String, ?>> mData;
  private LayoutInflater mDropDownInflater;
  private int mDropDownResource;
  private SimpleFilter mFilter;
  private String[] mFrom;
  private final LayoutInflater mInflater;
  private int mResource;
  private int[] mTo;
  private ArrayList<Map<String, ?>> mUnfilteredData;
  private ViewBinder mViewBinder;
  
  public SimpleAdapter(Context paramContext, List<? extends Map<String, ?>> paramList, int paramInt, String[] paramArrayOfString, int[] paramArrayOfInt)
  {
    mData = paramList;
    mDropDownResource = paramInt;
    mResource = paramInt;
    mFrom = paramArrayOfString;
    mTo = paramArrayOfInt;
    mInflater = ((LayoutInflater)paramContext.getSystemService("layout_inflater"));
  }
  
  private void bindView(int paramInt, View paramView)
  {
    Map localMap = (Map)mData.get(paramInt);
    if (localMap == null) {
      return;
    }
    ViewBinder localViewBinder = mViewBinder;
    String[] arrayOfString = mFrom;
    int[] arrayOfInt = mTo;
    int i = arrayOfInt.length;
    for (paramInt = 0; paramInt < i; paramInt++)
    {
      View localView = paramView.findViewById(arrayOfInt[paramInt]);
      if (localView != null)
      {
        Object localObject1 = localMap.get(arrayOfString[paramInt]);
        Object localObject2;
        if (localObject1 == null) {
          localObject2 = "";
        } else {
          localObject2 = localObject1.toString();
        }
        Object localObject3 = localObject2;
        if (localObject2 == null) {
          localObject3 = "";
        }
        boolean bool = false;
        if (localViewBinder != null) {
          bool = localViewBinder.setViewValue(localView, localObject1, (String)localObject3);
        }
        if (!bool) {
          if ((localView instanceof Checkable))
          {
            if ((localObject1 instanceof Boolean))
            {
              ((Checkable)localView).setChecked(((Boolean)localObject1).booleanValue());
            }
            else if ((localView instanceof TextView))
            {
              setViewText((TextView)localView, (String)localObject3);
            }
            else
            {
              localObject2 = new StringBuilder();
              ((StringBuilder)localObject2).append(localView.getClass().getName());
              ((StringBuilder)localObject2).append(" should be bound to a Boolean, not a ");
              if (localObject1 == null) {
                paramView = "<unknown type>";
              } else {
                paramView = localObject1.getClass();
              }
              ((StringBuilder)localObject2).append(paramView);
              throw new IllegalStateException(((StringBuilder)localObject2).toString());
            }
          }
          else if ((localView instanceof TextView))
          {
            setViewText((TextView)localView, (String)localObject3);
          }
          else if ((localView instanceof ImageView))
          {
            if ((localObject1 instanceof Integer)) {
              setViewImage((ImageView)localView, ((Integer)localObject1).intValue());
            } else {
              setViewImage((ImageView)localView, (String)localObject3);
            }
          }
          else
          {
            paramView = new StringBuilder();
            paramView.append(localView.getClass().getName());
            paramView.append(" is not a  view that can be bounds by this SimpleAdapter");
            throw new IllegalStateException(paramView.toString());
          }
        }
      }
    }
  }
  
  private View createViewFromResource(LayoutInflater paramLayoutInflater, int paramInt1, View paramView, ViewGroup paramViewGroup, int paramInt2)
  {
    if (paramView == null) {
      paramLayoutInflater = paramLayoutInflater.inflate(paramInt2, paramViewGroup, false);
    } else {
      paramLayoutInflater = paramView;
    }
    bindView(paramInt1, paramLayoutInflater);
    return paramLayoutInflater;
  }
  
  public int getCount()
  {
    return mData.size();
  }
  
  public View getDropDownView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    if (mDropDownInflater == null) {}
    for (LayoutInflater localLayoutInflater = mInflater;; localLayoutInflater = mDropDownInflater) {
      break;
    }
    return createViewFromResource(localLayoutInflater, paramInt, paramView, paramViewGroup, mDropDownResource);
  }
  
  public Resources.Theme getDropDownViewTheme()
  {
    Resources.Theme localTheme;
    if (mDropDownInflater == null) {
      localTheme = null;
    } else {
      localTheme = mDropDownInflater.getContext().getTheme();
    }
    return localTheme;
  }
  
  public Filter getFilter()
  {
    if (mFilter == null) {
      mFilter = new SimpleFilter(null);
    }
    return mFilter;
  }
  
  public Object getItem(int paramInt)
  {
    return mData.get(paramInt);
  }
  
  public long getItemId(int paramInt)
  {
    return paramInt;
  }
  
  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    return createViewFromResource(mInflater, paramInt, paramView, paramViewGroup, mResource);
  }
  
  public ViewBinder getViewBinder()
  {
    return mViewBinder;
  }
  
  public void setDropDownViewResource(int paramInt)
  {
    mDropDownResource = paramInt;
  }
  
  public void setDropDownViewTheme(Resources.Theme paramTheme)
  {
    if (paramTheme == null) {
      mDropDownInflater = null;
    } else if (paramTheme == mInflater.getContext().getTheme()) {
      mDropDownInflater = mInflater;
    } else {
      mDropDownInflater = LayoutInflater.from(new ContextThemeWrapper(mInflater.getContext(), paramTheme));
    }
  }
  
  public void setViewBinder(ViewBinder paramViewBinder)
  {
    mViewBinder = paramViewBinder;
  }
  
  public void setViewImage(ImageView paramImageView, int paramInt)
  {
    paramImageView.setImageResource(paramInt);
  }
  
  public void setViewImage(ImageView paramImageView, String paramString)
  {
    try
    {
      paramImageView.setImageResource(Integer.parseInt(paramString));
    }
    catch (NumberFormatException localNumberFormatException)
    {
      paramImageView.setImageURI(Uri.parse(paramString));
    }
  }
  
  public void setViewText(TextView paramTextView, String paramString)
  {
    paramTextView.setText(paramString);
  }
  
  private class SimpleFilter
    extends Filter
  {
    private SimpleFilter() {}
    
    protected Filter.FilterResults performFiltering(CharSequence paramCharSequence)
    {
      Filter.FilterResults localFilterResults = new Filter.FilterResults();
      if (mUnfilteredData == null) {
        SimpleAdapter.access$102(SimpleAdapter.this, new ArrayList(mData));
      }
      if ((paramCharSequence != null) && (paramCharSequence.length() != 0))
      {
        String str = paramCharSequence.toString().toLowerCase();
        ArrayList localArrayList = mUnfilteredData;
        int i = localArrayList.size();
        paramCharSequence = new ArrayList(i);
        for (int j = 0; j < i; j++)
        {
          Map localMap = (Map)localArrayList.get(j);
          if (localMap != null)
          {
            int k = mTo.length;
            for (int m = 0; m < k; m++)
            {
              String[] arrayOfString = ((String)localMap.get(mFrom[m])).split(" ");
              int n = arrayOfString.length;
              for (int i1 = 0; i1 < n; i1++) {
                if (arrayOfString[i1].toLowerCase().startsWith(str))
                {
                  paramCharSequence.add(localMap);
                  break;
                }
              }
            }
          }
        }
        values = paramCharSequence;
        count = paramCharSequence.size();
      }
      else
      {
        paramCharSequence = mUnfilteredData;
        values = paramCharSequence;
        count = paramCharSequence.size();
      }
      return localFilterResults;
    }
    
    protected void publishResults(CharSequence paramCharSequence, Filter.FilterResults paramFilterResults)
    {
      SimpleAdapter.access$202(SimpleAdapter.this, (List)values);
      if (count > 0) {
        notifyDataSetChanged();
      } else {
        notifyDataSetInvalidated();
      }
    }
  }
  
  public static abstract interface ViewBinder
  {
    public abstract boolean setViewValue(View paramView, Object paramObject, String paramString);
  }
}
