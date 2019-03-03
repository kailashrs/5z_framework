package android.view.animation;

public abstract class BaseInterpolator
  implements Interpolator
{
  private int mChangingConfiguration;
  
  public BaseInterpolator() {}
  
  public int getChangingConfiguration()
  {
    return mChangingConfiguration;
  }
  
  void setChangingConfiguration(int paramInt)
  {
    mChangingConfiguration = paramInt;
  }
}
