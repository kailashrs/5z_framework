package android.app;

import java.util.List;

@Deprecated
public class FragmentManagerNonConfig
{
  private final List<FragmentManagerNonConfig> mChildNonConfigs;
  private final List<Fragment> mFragments;
  
  FragmentManagerNonConfig(List<Fragment> paramList, List<FragmentManagerNonConfig> paramList1)
  {
    mFragments = paramList;
    mChildNonConfigs = paramList1;
  }
  
  List<FragmentManagerNonConfig> getChildNonConfigs()
  {
    return mChildNonConfigs;
  }
  
  List<Fragment> getFragments()
  {
    return mFragments;
  }
}
