package android.gesture;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

public class GestureStore
{
  private static final short FILE_FORMAT_VERSION = 1;
  public static final int ORIENTATION_INVARIANT = 1;
  public static final int ORIENTATION_SENSITIVE = 2;
  static final int ORIENTATION_SENSITIVE_4 = 4;
  static final int ORIENTATION_SENSITIVE_8 = 8;
  private static final boolean PROFILE_LOADING_SAVING = false;
  public static final int SEQUENCE_INVARIANT = 1;
  public static final int SEQUENCE_SENSITIVE = 2;
  private boolean mChanged = false;
  private Learner mClassifier = new InstanceLearner();
  private final HashMap<String, ArrayList<Gesture>> mNamedGestures = new HashMap();
  private int mOrientationStyle = 2;
  private int mSequenceType = 2;
  
  public GestureStore() {}
  
  private void readFormatV1(DataInputStream paramDataInputStream)
    throws IOException
  {
    Learner localLearner = mClassifier;
    HashMap localHashMap = mNamedGestures;
    localHashMap.clear();
    int i = paramDataInputStream.readInt();
    for (int j = 0; j < i; j++)
    {
      String str = paramDataInputStream.readUTF();
      int k = paramDataInputStream.readInt();
      ArrayList localArrayList = new ArrayList(k);
      for (int m = 0; m < k; m++)
      {
        Gesture localGesture = Gesture.deserialize(paramDataInputStream);
        localArrayList.add(localGesture);
        localLearner.addInstance(Instance.createInstance(mSequenceType, mOrientationStyle, localGesture, str));
      }
      localHashMap.put(str, localArrayList);
    }
  }
  
  public void addGesture(String paramString, Gesture paramGesture)
  {
    if ((paramString != null) && (paramString.length() != 0))
    {
      ArrayList localArrayList1 = (ArrayList)mNamedGestures.get(paramString);
      ArrayList localArrayList2 = localArrayList1;
      if (localArrayList1 == null)
      {
        localArrayList2 = new ArrayList();
        mNamedGestures.put(paramString, localArrayList2);
      }
      localArrayList2.add(paramGesture);
      mClassifier.addInstance(Instance.createInstance(mSequenceType, mOrientationStyle, paramGesture, paramString));
      mChanged = true;
      return;
    }
  }
  
  public Set<String> getGestureEntries()
  {
    return mNamedGestures.keySet();
  }
  
  public ArrayList<Gesture> getGestures(String paramString)
  {
    paramString = (ArrayList)mNamedGestures.get(paramString);
    if (paramString != null) {
      return new ArrayList(paramString);
    }
    return null;
  }
  
  Learner getLearner()
  {
    return mClassifier;
  }
  
  public int getOrientationStyle()
  {
    return mOrientationStyle;
  }
  
  public int getSequenceType()
  {
    return mSequenceType;
  }
  
  public boolean hasChanged()
  {
    return mChanged;
  }
  
  public void load(InputStream paramInputStream)
    throws IOException
  {
    load(paramInputStream, false);
  }
  
  public void load(InputStream paramInputStream, boolean paramBoolean)
    throws IOException
  {
    Object localObject1 = null;
    Object localObject2 = localObject1;
    try
    {
      DataInputStream localDataInputStream = new java/io/DataInputStream;
      localObject2 = localObject1;
      if (!(paramInputStream instanceof BufferedInputStream))
      {
        localObject2 = localObject1;
        paramInputStream = new BufferedInputStream(paramInputStream, 32768);
      }
      localObject2 = localObject1;
      localDataInputStream.<init>(paramInputStream);
      paramInputStream = localDataInputStream;
      localObject2 = paramInputStream;
      if (paramInputStream.readShort() == 1)
      {
        localObject2 = paramInputStream;
        readFormatV1(paramInputStream);
      }
      if (paramBoolean) {
        GestureUtils.closeStream(paramInputStream);
      }
      return;
    }
    finally
    {
      if (paramBoolean) {
        GestureUtils.closeStream((Closeable)localObject2);
      }
    }
  }
  
  public ArrayList<Prediction> recognize(Gesture paramGesture)
  {
    paramGesture = Instance.createInstance(mSequenceType, mOrientationStyle, paramGesture, null);
    return mClassifier.classify(mSequenceType, mOrientationStyle, vector);
  }
  
  public void removeEntry(String paramString)
  {
    mNamedGestures.remove(paramString);
    mClassifier.removeInstances(paramString);
    mChanged = true;
  }
  
  public void removeGesture(String paramString, Gesture paramGesture)
  {
    ArrayList localArrayList = (ArrayList)mNamedGestures.get(paramString);
    if (localArrayList == null) {
      return;
    }
    localArrayList.remove(paramGesture);
    if (localArrayList.isEmpty()) {
      mNamedGestures.remove(paramString);
    }
    mClassifier.removeInstance(paramGesture.getID());
    mChanged = true;
  }
  
  public void save(OutputStream paramOutputStream)
    throws IOException
  {
    save(paramOutputStream, false);
  }
  
  public void save(OutputStream paramOutputStream, boolean paramBoolean)
    throws IOException
  {
    Iterator localIterator = null;
    Object localObject1 = localIterator;
    try
    {
      Object localObject2 = mNamedGestures;
      localObject1 = localIterator;
      Object localObject3 = new java/io/DataOutputStream;
      localObject1 = localIterator;
      if (!(paramOutputStream instanceof BufferedOutputStream))
      {
        localObject1 = localIterator;
        paramOutputStream = new BufferedOutputStream(paramOutputStream, 32768);
      }
      localObject1 = localIterator;
      ((DataOutputStream)localObject3).<init>(paramOutputStream);
      paramOutputStream = (OutputStream)localObject3;
      localObject1 = paramOutputStream;
      paramOutputStream.writeShort(1);
      localObject1 = paramOutputStream;
      paramOutputStream.writeInt(((HashMap)localObject2).size());
      localObject1 = paramOutputStream;
      localIterator = ((HashMap)localObject2).entrySet().iterator();
      for (;;)
      {
        localObject1 = paramOutputStream;
        boolean bool = localIterator.hasNext();
        int i = 0;
        if (!bool) {
          break;
        }
        localObject1 = paramOutputStream;
        localObject2 = (Map.Entry)localIterator.next();
        localObject1 = paramOutputStream;
        localObject3 = (String)((Map.Entry)localObject2).getKey();
        localObject1 = paramOutputStream;
        localObject2 = (ArrayList)((Map.Entry)localObject2).getValue();
        localObject1 = paramOutputStream;
        int j = ((ArrayList)localObject2).size();
        localObject1 = paramOutputStream;
        paramOutputStream.writeUTF((String)localObject3);
        localObject1 = paramOutputStream;
        paramOutputStream.writeInt(j);
        while (i < j)
        {
          localObject1 = paramOutputStream;
          ((Gesture)((ArrayList)localObject2).get(i)).serialize(paramOutputStream);
          i++;
        }
      }
      localObject1 = paramOutputStream;
      paramOutputStream.flush();
      localObject1 = paramOutputStream;
      mChanged = false;
      if (paramBoolean) {
        GestureUtils.closeStream(paramOutputStream);
      }
      return;
    }
    finally
    {
      if (paramBoolean) {
        GestureUtils.closeStream((Closeable)localObject1);
      }
    }
  }
  
  public void setOrientationStyle(int paramInt)
  {
    mOrientationStyle = paramInt;
  }
  
  public void setSequenceType(int paramInt)
  {
    mSequenceType = paramInt;
  }
}
