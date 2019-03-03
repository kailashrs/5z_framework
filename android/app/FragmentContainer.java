package android.app;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

@Deprecated
public abstract class FragmentContainer
{
  public FragmentContainer() {}
  
  public Fragment instantiate(Context paramContext, String paramString, Bundle paramBundle)
  {
    return Fragment.instantiate(paramContext, paramString, paramBundle);
  }
  
  public abstract <T extends View> T onFindViewById(int paramInt);
  
  public abstract boolean onHasView();
}
