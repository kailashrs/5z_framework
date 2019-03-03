package android.gesture;

import java.util.ArrayList;

abstract class Learner
{
  private final ArrayList<Instance> mInstances = new ArrayList();
  
  Learner() {}
  
  void addInstance(Instance paramInstance)
  {
    mInstances.add(paramInstance);
  }
  
  abstract ArrayList<Prediction> classify(int paramInt1, int paramInt2, float[] paramArrayOfFloat);
  
  ArrayList<Instance> getInstances()
  {
    return mInstances;
  }
  
  void removeInstance(long paramLong)
  {
    ArrayList localArrayList = mInstances;
    int i = localArrayList.size();
    for (int j = 0; j < i; j++)
    {
      Instance localInstance = (Instance)localArrayList.get(j);
      if (paramLong == id)
      {
        localArrayList.remove(localInstance);
        return;
      }
    }
  }
  
  void removeInstances(String paramString)
  {
    ArrayList localArrayList1 = new ArrayList();
    ArrayList localArrayList2 = mInstances;
    int i = localArrayList2.size();
    for (int j = 0; j < i; j++)
    {
      Instance localInstance = (Instance)localArrayList2.get(j);
      if (((label == null) && (paramString == null)) || ((label != null) && (label.equals(paramString)))) {
        localArrayList1.add(localInstance);
      }
    }
    localArrayList2.removeAll(localArrayList1);
  }
}
