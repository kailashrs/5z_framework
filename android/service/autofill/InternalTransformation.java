package android.service.autofill;

import android.os.Parcelable;
import android.util.Log;
import android.util.Pair;
import android.view.autofill.Helper;
import android.widget.RemoteViews;
import java.util.ArrayList;

public abstract class InternalTransformation
  implements Transformation, Parcelable
{
  private static final String TAG = "InternalTransformation";
  
  public InternalTransformation() {}
  
  public static boolean batchApply(ValueFinder paramValueFinder, RemoteViews paramRemoteViews, ArrayList<Pair<Integer, InternalTransformation>> paramArrayList)
  {
    int i = paramArrayList.size();
    Object localObject;
    if (Helper.sDebug)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("getPresentation(): applying ");
      ((StringBuilder)localObject).append(i);
      ((StringBuilder)localObject).append(" transformations");
      Log.d("InternalTransformation", ((StringBuilder)localObject).toString());
    }
    int j = 0;
    while (j < i)
    {
      localObject = (Pair)paramArrayList.get(j);
      int k = ((Integer)first).intValue();
      localObject = (InternalTransformation)second;
      if (Helper.sDebug)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("#");
        localStringBuilder.append(j);
        localStringBuilder.append(": ");
        localStringBuilder.append(localObject);
        Log.d("InternalTransformation", localStringBuilder.toString());
      }
      try
      {
        ((InternalTransformation)localObject).apply(paramValueFinder, paramRemoteViews, k);
        j++;
      }
      catch (Exception paramValueFinder)
      {
        paramRemoteViews = new StringBuilder();
        paramRemoteViews.append("Could not apply transformation ");
        paramRemoteViews.append(localObject);
        paramRemoteViews.append(": ");
        paramRemoteViews.append(paramValueFinder.getClass());
        Log.e("InternalTransformation", paramRemoteViews.toString());
        return false;
      }
    }
    return true;
  }
  
  abstract void apply(ValueFinder paramValueFinder, RemoteViews paramRemoteViews, int paramInt)
    throws Exception;
}
