package android.webkit;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class PluginList
{
  private ArrayList<Plugin> mPlugins = new ArrayList();
  
  @Deprecated
  public PluginList() {}
  
  @Deprecated
  public void addPlugin(Plugin paramPlugin)
  {
    try
    {
      if (!mPlugins.contains(paramPlugin)) {
        mPlugins.add(paramPlugin);
      }
      return;
    }
    finally
    {
      paramPlugin = finally;
      throw paramPlugin;
    }
  }
  
  @Deprecated
  public void clear()
  {
    try
    {
      mPlugins.clear();
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  @Deprecated
  public List getList()
  {
    try
    {
      ArrayList localArrayList = mPlugins;
      return localArrayList;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  /* Error */
  @Deprecated
  public void pluginClicked(android.content.Context paramContext, int paramInt)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield 17	android/webkit/PluginList:mPlugins	Ljava/util/ArrayList;
    //   6: iload_2
    //   7: invokevirtual 41	java/util/ArrayList:get	(I)Ljava/lang/Object;
    //   10: checkcast 43	android/webkit/Plugin
    //   13: aload_1
    //   14: invokevirtual 47	android/webkit/Plugin:dispatchClickEvent	(Landroid/content/Context;)V
    //   17: goto +9 -> 26
    //   20: astore_1
    //   21: aload_0
    //   22: monitorexit
    //   23: aload_1
    //   24: athrow
    //   25: astore_1
    //   26: aload_0
    //   27: monitorexit
    //   28: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	29	0	this	PluginList
    //   0	29	1	paramContext	android.content.Context
    //   0	29	2	paramInt	int
    // Exception table:
    //   from	to	target	type
    //   2	17	20	finally
    //   2	17	25	java/lang/IndexOutOfBoundsException
  }
  
  @Deprecated
  public void removePlugin(Plugin paramPlugin)
  {
    try
    {
      int i = mPlugins.indexOf(paramPlugin);
      if (i != -1) {
        mPlugins.remove(i);
      }
      return;
    }
    finally {}
  }
}
