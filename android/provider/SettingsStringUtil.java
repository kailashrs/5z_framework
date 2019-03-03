package android.provider;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.text.TextUtils;
import com.android.internal.util.ArrayUtils;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.function.Function;

public class SettingsStringUtil
{
  public static final String DELIMITER = ":";
  
  private SettingsStringUtil() {}
  
  public static abstract class ColonDelimitedSet<T>
    extends HashSet<T>
  {
    public ColonDelimitedSet(String paramString)
    {
      paramString = TextUtils.split(TextUtils.emptyIfNull(paramString), ":");
      int i = paramString.length;
      for (int j = 0; j < i; j++) {
        add(itemFromString(paramString[j]));
      }
    }
    
    protected abstract T itemFromString(String paramString);
    
    protected String itemToString(T paramT)
    {
      return String.valueOf(paramT);
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      Iterator localIterator = iterator();
      if (localIterator.hasNext())
      {
        localStringBuilder.append(itemToString(localIterator.next()));
        while (localIterator.hasNext())
        {
          localStringBuilder.append(":");
          localStringBuilder.append(itemToString(localIterator.next()));
        }
      }
      return localStringBuilder.toString();
    }
    
    public static class OfStrings
      extends SettingsStringUtil.ColonDelimitedSet<String>
    {
      public OfStrings(String paramString)
      {
        super();
      }
      
      public static String add(String paramString1, String paramString2)
      {
        OfStrings localOfStrings = new OfStrings(paramString1);
        if (localOfStrings.contains(paramString2)) {
          return paramString1;
        }
        localOfStrings.add(paramString2);
        return localOfStrings.toString();
      }
      
      public static String addAll(String paramString, Collection<String> paramCollection)
      {
        OfStrings localOfStrings = new OfStrings(paramString);
        if (localOfStrings.addAll(paramCollection)) {
          paramString = localOfStrings.toString();
        }
        return paramString;
      }
      
      public static boolean contains(String paramString1, String paramString2)
      {
        boolean bool;
        if (ArrayUtils.indexOf(TextUtils.split(paramString1, ":"), paramString2) != -1) {
          bool = true;
        } else {
          bool = false;
        }
        return bool;
      }
      
      public static String remove(String paramString1, String paramString2)
      {
        OfStrings localOfStrings = new OfStrings(paramString1);
        if (!localOfStrings.contains(paramString2)) {
          return paramString1;
        }
        localOfStrings.remove(paramString2);
        return localOfStrings.toString();
      }
      
      protected String itemFromString(String paramString)
      {
        return paramString;
      }
    }
  }
  
  public static class ComponentNameSet
    extends SettingsStringUtil.ColonDelimitedSet<ComponentName>
  {
    public ComponentNameSet(String paramString)
    {
      super();
    }
    
    public static String add(String paramString, ComponentName paramComponentName)
    {
      ComponentNameSet localComponentNameSet = new ComponentNameSet(paramString);
      if (localComponentNameSet.contains(paramComponentName)) {
        return paramString;
      }
      localComponentNameSet.add(paramComponentName);
      return localComponentNameSet.toString();
    }
    
    public static boolean contains(String paramString, ComponentName paramComponentName)
    {
      return SettingsStringUtil.ColonDelimitedSet.OfStrings.contains(paramString, paramComponentName.flattenToString());
    }
    
    public static String remove(String paramString, ComponentName paramComponentName)
    {
      ComponentNameSet localComponentNameSet = new ComponentNameSet(paramString);
      if (!localComponentNameSet.contains(paramComponentName)) {
        return paramString;
      }
      localComponentNameSet.remove(paramComponentName);
      return localComponentNameSet.toString();
    }
    
    protected ComponentName itemFromString(String paramString)
    {
      return ComponentName.unflattenFromString(paramString);
    }
    
    protected String itemToString(ComponentName paramComponentName)
    {
      return paramComponentName.flattenToString();
    }
  }
  
  public static class SettingStringHelper
  {
    private final ContentResolver mContentResolver;
    private final String mSettingName;
    private final int mUserId;
    
    public SettingStringHelper(ContentResolver paramContentResolver, String paramString, int paramInt)
    {
      mContentResolver = paramContentResolver;
      mUserId = paramInt;
      mSettingName = paramString;
    }
    
    public boolean modify(Function<String, String> paramFunction)
    {
      return write((String)paramFunction.apply(read()));
    }
    
    public String read()
    {
      return Settings.Secure.getStringForUser(mContentResolver, mSettingName, mUserId);
    }
    
    public boolean write(String paramString)
    {
      return Settings.Secure.putStringForUser(mContentResolver, mSettingName, paramString, mUserId);
    }
  }
}
