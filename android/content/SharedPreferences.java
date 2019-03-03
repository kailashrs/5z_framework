package android.content;

import java.util.Map;
import java.util.Set;

public abstract interface SharedPreferences
{
  public abstract boolean contains(String paramString);
  
  public abstract Editor edit();
  
  public abstract Map<String, ?> getAll();
  
  public abstract boolean getBoolean(String paramString, boolean paramBoolean);
  
  public abstract float getFloat(String paramString, float paramFloat);
  
  public abstract int getInt(String paramString, int paramInt);
  
  public abstract long getLong(String paramString, long paramLong);
  
  public abstract String getString(String paramString1, String paramString2);
  
  public abstract Set<String> getStringSet(String paramString, Set<String> paramSet);
  
  public abstract void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener paramOnSharedPreferenceChangeListener);
  
  public abstract void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener paramOnSharedPreferenceChangeListener);
  
  public static abstract interface Editor
  {
    public abstract void apply();
    
    public abstract Editor clear();
    
    public abstract boolean commit();
    
    public abstract Editor putBoolean(String paramString, boolean paramBoolean);
    
    public abstract Editor putFloat(String paramString, float paramFloat);
    
    public abstract Editor putInt(String paramString, int paramInt);
    
    public abstract Editor putLong(String paramString, long paramLong);
    
    public abstract Editor putString(String paramString1, String paramString2);
    
    public abstract Editor putStringSet(String paramString, Set<String> paramSet);
    
    public abstract Editor remove(String paramString);
  }
  
  public static abstract interface OnSharedPreferenceChangeListener
  {
    public abstract void onSharedPreferenceChanged(SharedPreferences paramSharedPreferences, String paramString);
  }
}
