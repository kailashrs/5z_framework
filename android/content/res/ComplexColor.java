package android.content.res;

public abstract class ComplexColor
{
  private int mChangingConfigurations;
  
  public ComplexColor() {}
  
  public abstract boolean canApplyTheme();
  
  public int getChangingConfigurations()
  {
    return mChangingConfigurations;
  }
  
  public abstract ConstantState<ComplexColor> getConstantState();
  
  public abstract int getDefaultColor();
  
  public boolean isStateful()
  {
    return false;
  }
  
  public abstract ComplexColor obtainForTheme(Resources.Theme paramTheme);
  
  final void setBaseChangingConfigurations(int paramInt)
  {
    mChangingConfigurations = paramInt;
  }
}
