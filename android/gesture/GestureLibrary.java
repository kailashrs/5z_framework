package android.gesture;

import java.util.ArrayList;
import java.util.Set;

public abstract class GestureLibrary
{
  protected final GestureStore mStore = new GestureStore();
  
  protected GestureLibrary() {}
  
  public void addGesture(String paramString, Gesture paramGesture)
  {
    mStore.addGesture(paramString, paramGesture);
  }
  
  public Set<String> getGestureEntries()
  {
    return mStore.getGestureEntries();
  }
  
  public ArrayList<Gesture> getGestures(String paramString)
  {
    return mStore.getGestures(paramString);
  }
  
  public Learner getLearner()
  {
    return mStore.getLearner();
  }
  
  public int getOrientationStyle()
  {
    return mStore.getOrientationStyle();
  }
  
  public int getSequenceType()
  {
    return mStore.getSequenceType();
  }
  
  public boolean isReadOnly()
  {
    return false;
  }
  
  public abstract boolean load();
  
  public ArrayList<Prediction> recognize(Gesture paramGesture)
  {
    return mStore.recognize(paramGesture);
  }
  
  public void removeEntry(String paramString)
  {
    mStore.removeEntry(paramString);
  }
  
  public void removeGesture(String paramString, Gesture paramGesture)
  {
    mStore.removeGesture(paramString, paramGesture);
  }
  
  public abstract boolean save();
  
  public void setOrientationStyle(int paramInt)
  {
    mStore.setOrientationStyle(paramInt);
  }
  
  public void setSequenceType(int paramInt)
  {
    mStore.setSequenceType(paramInt);
  }
}
