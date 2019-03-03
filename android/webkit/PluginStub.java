package android.webkit;

import android.content.Context;
import android.view.View;

public abstract interface PluginStub
{
  public abstract View getEmbeddedView(int paramInt, Context paramContext);
  
  public abstract View getFullScreenView(int paramInt, Context paramContext);
}
