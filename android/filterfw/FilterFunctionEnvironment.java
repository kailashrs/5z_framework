package android.filterfw;

import android.filterfw.core.Filter;
import android.filterfw.core.FilterFactory;
import android.filterfw.core.FilterFunction;
import android.filterfw.core.FrameManager;

public class FilterFunctionEnvironment
  extends MffEnvironment
{
  public FilterFunctionEnvironment()
  {
    super(null);
  }
  
  public FilterFunctionEnvironment(FrameManager paramFrameManager)
  {
    super(paramFrameManager);
  }
  
  public FilterFunction createFunction(Class paramClass, Object... paramVarArgs)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("FilterFunction(");
    ((StringBuilder)localObject).append(paramClass.getSimpleName());
    ((StringBuilder)localObject).append(")");
    localObject = ((StringBuilder)localObject).toString();
    paramClass = FilterFactory.sharedFactory().createFilterByClass(paramClass, (String)localObject);
    paramClass.initWithAssignmentList(paramVarArgs);
    return new FilterFunction(getContext(), paramClass);
  }
}
