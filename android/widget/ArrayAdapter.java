package android.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ArrayAdapter<T>
  extends BaseAdapter
  implements Filterable, ThemedSpinnerAdapter
{
  private final Context mContext;
  private LayoutInflater mDropDownInflater;
  private int mDropDownResource;
  private int mFieldId = 0;
  private ArrayAdapter<T>.ArrayFilter mFilter;
  private final LayoutInflater mInflater;
  private final Object mLock = new Object();
  private boolean mNotifyOnChange = true;
  private List<T> mObjects;
  private boolean mObjectsFromResources;
  private ArrayList<T> mOriginalValues;
  private final int mResource;
  
  public ArrayAdapter(Context paramContext, int paramInt)
  {
    this(paramContext, paramInt, 0, new ArrayList());
  }
  
  public ArrayAdapter(Context paramContext, int paramInt1, int paramInt2)
  {
    this(paramContext, paramInt1, paramInt2, new ArrayList());
  }
  
  public ArrayAdapter(Context paramContext, int paramInt1, int paramInt2, List<T> paramList)
  {
    this(paramContext, paramInt1, paramInt2, paramList, false);
  }
  
  private ArrayAdapter(Context paramContext, int paramInt1, int paramInt2, List<T> paramList, boolean paramBoolean)
  {
    mContext = paramContext;
    mInflater = LayoutInflater.from(paramContext);
    mDropDownResource = paramInt1;
    mResource = paramInt1;
    mObjects = paramList;
    mObjectsFromResources = paramBoolean;
    mFieldId = paramInt2;
  }
  
  public ArrayAdapter(Context paramContext, int paramInt1, int paramInt2, T[] paramArrayOfT)
  {
    this(paramContext, paramInt1, paramInt2, Arrays.asList(paramArrayOfT));
  }
  
  public ArrayAdapter(Context paramContext, int paramInt, List<T> paramList)
  {
    this(paramContext, paramInt, 0, paramList);
  }
  
  public ArrayAdapter(Context paramContext, int paramInt, T[] paramArrayOfT)
  {
    this(paramContext, paramInt, 0, Arrays.asList(paramArrayOfT));
  }
  
  public static ArrayAdapter<CharSequence> createFromResource(Context paramContext, int paramInt1, int paramInt2)
  {
    return new ArrayAdapter(paramContext, paramInt2, 0, Arrays.asList(paramContext.getResources().getTextArray(paramInt1)), true);
  }
  
  private View createViewFromResource(LayoutInflater paramLayoutInflater, int paramInt1, View paramView, ViewGroup paramViewGroup, int paramInt2)
  {
    if (paramView == null) {
      paramLayoutInflater = paramLayoutInflater.inflate(paramInt2, paramViewGroup, false);
    } else {
      paramLayoutInflater = paramView;
    }
    try
    {
      if (mFieldId == 0)
      {
        paramView = (TextView)paramLayoutInflater;
      }
      else
      {
        paramView = (TextView)paramLayoutInflater.findViewById(mFieldId);
        if (paramView == null) {
          break label88;
        }
      }
      paramViewGroup = getItem(paramInt1);
      if ((paramViewGroup instanceof CharSequence)) {
        paramView.setText((CharSequence)paramViewGroup);
      } else {
        paramView.setText(paramViewGroup.toString());
      }
      return paramLayoutInflater;
      label88:
      paramView = new java/lang/RuntimeException;
      paramLayoutInflater = new java/lang/StringBuilder;
      paramLayoutInflater.<init>();
      paramLayoutInflater.append("Failed to find view with ID ");
      paramLayoutInflater.append(mContext.getResources().getResourceName(mFieldId));
      paramLayoutInflater.append(" in item layout");
      paramView.<init>(paramLayoutInflater.toString());
      throw paramView;
    }
    catch (ClassCastException paramLayoutInflater)
    {
      Log.e("ArrayAdapter", "You must supply a resource ID for a TextView");
      throw new IllegalStateException("ArrayAdapter requires the resource ID to be a TextView", paramLayoutInflater);
    }
  }
  
  public void add(T paramT)
  {
    synchronized (mLock)
    {
      if (mOriginalValues != null) {
        mOriginalValues.add(paramT);
      } else {
        mObjects.add(paramT);
      }
      mObjectsFromResources = false;
      if (mNotifyOnChange) {
        notifyDataSetChanged();
      }
      return;
    }
  }
  
  public void addAll(Collection<? extends T> paramCollection)
  {
    synchronized (mLock)
    {
      if (mOriginalValues != null) {
        mOriginalValues.addAll(paramCollection);
      } else {
        mObjects.addAll(paramCollection);
      }
      mObjectsFromResources = false;
      if (mNotifyOnChange) {
        notifyDataSetChanged();
      }
      return;
    }
  }
  
  public void addAll(T... paramVarArgs)
  {
    synchronized (mLock)
    {
      if (mOriginalValues != null) {
        Collections.addAll(mOriginalValues, paramVarArgs);
      } else {
        Collections.addAll(mObjects, paramVarArgs);
      }
      mObjectsFromResources = false;
      if (mNotifyOnChange) {
        notifyDataSetChanged();
      }
      return;
    }
  }
  
  public void clear()
  {
    synchronized (mLock)
    {
      if (mOriginalValues != null) {
        mOriginalValues.clear();
      } else {
        mObjects.clear();
      }
      mObjectsFromResources = false;
      if (mNotifyOnChange) {
        notifyDataSetChanged();
      }
      return;
    }
  }
  
  public CharSequence[] getAutofillOptions()
  {
    CharSequence[] arrayOfCharSequence = super.getAutofillOptions();
    if (arrayOfCharSequence != null) {
      return arrayOfCharSequence;
    }
    if ((mObjectsFromResources) && (mObjects != null) && (!mObjects.isEmpty()))
    {
      arrayOfCharSequence = new CharSequence[mObjects.size()];
      mObjects.toArray(arrayOfCharSequence);
      return arrayOfCharSequence;
    }
    return null;
  }
  
  public Context getContext()
  {
    return mContext;
  }
  
  public int getCount()
  {
    return mObjects.size();
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
      mFilter = new ArrayFilter(null);
    }
    return mFilter;
  }
  
  public T getItem(int paramInt)
  {
    return mObjects.get(paramInt);
  }
  
  public long getItemId(int paramInt)
  {
    return paramInt;
  }
  
  public int getPosition(T paramT)
  {
    return mObjects.indexOf(paramT);
  }
  
  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    return createViewFromResource(mInflater, paramInt, paramView, paramViewGroup, mResource);
  }
  
  public void insert(T paramT, int paramInt)
  {
    synchronized (mLock)
    {
      if (mOriginalValues != null) {
        mOriginalValues.add(paramInt, paramT);
      } else {
        mObjects.add(paramInt, paramT);
      }
      mObjectsFromResources = false;
      if (mNotifyOnChange) {
        notifyDataSetChanged();
      }
      return;
    }
  }
  
  public void notifyDataSetChanged()
  {
    super.notifyDataSetChanged();
    mNotifyOnChange = true;
  }
  
  public void remove(T paramT)
  {
    synchronized (mLock)
    {
      if (mOriginalValues != null) {
        mOriginalValues.remove(paramT);
      } else {
        mObjects.remove(paramT);
      }
      mObjectsFromResources = false;
      if (mNotifyOnChange) {
        notifyDataSetChanged();
      }
      return;
    }
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
      mDropDownInflater = LayoutInflater.from(new ContextThemeWrapper(mContext, paramTheme));
    }
  }
  
  public void setNotifyOnChange(boolean paramBoolean)
  {
    mNotifyOnChange = paramBoolean;
  }
  
  public void sort(Comparator<? super T> paramComparator)
  {
    synchronized (mLock)
    {
      if (mOriginalValues != null) {
        Collections.sort(mOriginalValues, paramComparator);
      } else {
        Collections.sort(mObjects, paramComparator);
      }
      if (mNotifyOnChange) {
        notifyDataSetChanged();
      }
      return;
    }
  }
  
  private class ArrayFilter
    extends Filter
  {
    private ArrayFilter() {}
    
    protected Filter.FilterResults performFiltering(CharSequence arg1)
    {
      Filter.FilterResults localFilterResults = new Filter.FilterResults();
      Object localObject4;
      if (mOriginalValues == null) {
        synchronized (mLock)
        {
          ??? = ArrayAdapter.this;
          localObject4 = new java/util/ArrayList;
          ((ArrayList)localObject4).<init>(mObjects);
          ArrayAdapter.access$102((ArrayAdapter)???, (ArrayList)localObject4);
        }
      }
      if ((??? != null) && (???.length() != 0))
      {
        ??? = ???.toString().toLowerCase();
        synchronized (mLock)
        {
          ??? = new java/util/ArrayList;
          ???.<init>(mOriginalValues);
          int i = ???.size();
          ??? = new ArrayList();
          for (int j = 0; j < i; j++)
          {
            localObject4 = ???.get(j);
            Object localObject5 = localObject4.toString().toLowerCase();
            if (((String)localObject5).startsWith((String)???))
            {
              ((ArrayList)???).add(localObject4);
            }
            else
            {
              localObject5 = ((String)localObject5).split(" ");
              int k = localObject5.length;
              for (int m = 0; m < k; m++) {
                if (localObject5[m].startsWith((String)???))
                {
                  ((ArrayList)???).add(localObject4);
                  break;
                }
              }
            }
          }
          values = ???;
          count = ((ArrayList)???).size();
        }
      }
      synchronized (mLock)
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
      ArrayAdapter.access$302(ArrayAdapter.this, (List)values);
      if (count > 0) {
        notifyDataSetChanged();
      } else {
        notifyDataSetInvalidated();
      }
    }
  }
}
