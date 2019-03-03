package android.gesture;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

class InstanceLearner
  extends Learner
{
  private static final Comparator<Prediction> sComparator = new Comparator()
  {
    public int compare(Prediction paramAnonymousPrediction1, Prediction paramAnonymousPrediction2)
    {
      double d1 = score;
      double d2 = score;
      if (d1 > d2) {
        return -1;
      }
      if (d1 < d2) {
        return 1;
      }
      return 0;
    }
  };
  
  InstanceLearner() {}
  
  ArrayList<Prediction> classify(int paramInt1, int paramInt2, float[] paramArrayOfFloat)
  {
    ArrayList localArrayList1 = new ArrayList();
    ArrayList localArrayList2 = getInstances();
    int i = localArrayList2.size();
    TreeMap localTreeMap = new TreeMap();
    for (int j = 0; j < i; j++)
    {
      Instance localInstance = (Instance)localArrayList2.get(j);
      if (vector.length == paramArrayOfFloat.length)
      {
        double d;
        if (paramInt1 == 2) {
          d = GestureUtils.minimumCosineDistance(vector, paramArrayOfFloat, paramInt2);
        } else {
          d = GestureUtils.squaredEuclideanDistance(vector, paramArrayOfFloat);
        }
        if (d == 0.0D) {
          d = Double.MAX_VALUE;
        } else {
          d = 1.0D / d;
        }
        localObject = (Double)localTreeMap.get(label);
        if ((localObject == null) || (d > ((Double)localObject).doubleValue())) {
          localTreeMap.put(label, Double.valueOf(d));
        }
      }
    }
    Object localObject = localTreeMap.keySet().iterator();
    while (((Iterator)localObject).hasNext())
    {
      paramArrayOfFloat = (String)((Iterator)localObject).next();
      localArrayList1.add(new Prediction(paramArrayOfFloat, ((Double)localTreeMap.get(paramArrayOfFloat)).doubleValue()));
    }
    Collections.sort(localArrayList1, sComparator);
    return localArrayList1;
  }
}
