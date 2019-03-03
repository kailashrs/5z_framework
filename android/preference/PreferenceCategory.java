package android.preference;

import android.content.Context;
import android.util.AttributeSet;

public class PreferenceCategory
  extends PreferenceGroup
{
  private static final String TAG = "PreferenceCategory";
  
  public PreferenceCategory(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public PreferenceCategory(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 16842892);
  }
  
  public PreferenceCategory(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public PreferenceCategory(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
  }
  
  public boolean isEnabled()
  {
    return false;
  }
  
  protected boolean onPrepareAddPreference(Preference paramPreference)
  {
    if (!(paramPreference instanceof PreferenceCategory)) {
      return super.onPrepareAddPreference(paramPreference);
    }
    throw new IllegalArgumentException("Cannot add a PreferenceCategory directly to a PreferenceCategory");
  }
  
  public boolean shouldDisableDependents()
  {
    return super.isEnabled() ^ true;
  }
}
