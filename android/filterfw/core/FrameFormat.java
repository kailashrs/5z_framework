package android.filterfw.core;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

public class FrameFormat
{
  public static final int BYTES_PER_SAMPLE_UNSPECIFIED = 1;
  protected static final int SIZE_UNKNOWN = -1;
  public static final int SIZE_UNSPECIFIED = 0;
  public static final int TARGET_GPU = 3;
  public static final int TARGET_NATIVE = 2;
  public static final int TARGET_RS = 5;
  public static final int TARGET_SIMPLE = 1;
  public static final int TARGET_UNSPECIFIED = 0;
  public static final int TARGET_VERTEXBUFFER = 4;
  public static final int TYPE_BIT = 1;
  public static final int TYPE_BYTE = 2;
  public static final int TYPE_DOUBLE = 6;
  public static final int TYPE_FLOAT = 5;
  public static final int TYPE_INT16 = 3;
  public static final int TYPE_INT32 = 4;
  public static final int TYPE_OBJECT = 8;
  public static final int TYPE_POINTER = 7;
  public static final int TYPE_UNSPECIFIED = 0;
  protected int mBaseType = 0;
  protected int mBytesPerSample = 1;
  protected int[] mDimensions;
  protected KeyValueMap mMetaData;
  protected Class mObjectClass;
  protected int mSize = -1;
  protected int mTarget = 0;
  
  protected FrameFormat() {}
  
  public FrameFormat(int paramInt1, int paramInt2)
  {
    mBaseType = paramInt1;
    mTarget = paramInt2;
    initDefaults();
  }
  
  public static String baseTypeToString(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return "unknown";
    case 8: 
      return "object";
    case 7: 
      return "pointer";
    case 6: 
      return "double";
    case 5: 
      return "float";
    case 4: 
      return "int";
    case 3: 
      return "int";
    case 2: 
      return "byte";
    case 1: 
      return "bit";
    }
    return "unspecified";
  }
  
  public static int bytesPerSampleOf(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return 1;
    case 6: 
      return 8;
    case 4: 
    case 5: 
    case 7: 
      return 4;
    case 3: 
      return 2;
    }
    return 1;
  }
  
  public static String dimensionsToString(int[] paramArrayOfInt)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    if (paramArrayOfInt != null)
    {
      int i = paramArrayOfInt.length;
      for (int j = 0; j < i; j++) {
        if (paramArrayOfInt[j] == 0)
        {
          localStringBuffer.append("[]");
        }
        else
        {
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("[");
          localStringBuilder.append(String.valueOf(paramArrayOfInt[j]));
          localStringBuilder.append("]");
          localStringBuffer.append(localStringBuilder.toString());
        }
      }
    }
    return localStringBuffer.toString();
  }
  
  private void initDefaults()
  {
    mBytesPerSample = bytesPerSampleOf(mBaseType);
  }
  
  public static String metaDataToString(KeyValueMap paramKeyValueMap)
  {
    if (paramKeyValueMap == null) {
      return "";
    }
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append("{ ");
    paramKeyValueMap = paramKeyValueMap.entrySet().iterator();
    while (paramKeyValueMap.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)paramKeyValueMap.next();
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append((String)localEntry.getKey());
      localStringBuilder.append(": ");
      localStringBuilder.append(localEntry.getValue());
      localStringBuilder.append(" ");
      localStringBuffer.append(localStringBuilder.toString());
    }
    localStringBuffer.append("}");
    return localStringBuffer.toString();
  }
  
  public static int readTargetString(String paramString)
  {
    if ((!paramString.equalsIgnoreCase("CPU")) && (!paramString.equalsIgnoreCase("NATIVE")))
    {
      if (paramString.equalsIgnoreCase("GPU")) {
        return 3;
      }
      if (paramString.equalsIgnoreCase("SIMPLE")) {
        return 1;
      }
      if (paramString.equalsIgnoreCase("VERTEXBUFFER")) {
        return 4;
      }
      if (paramString.equalsIgnoreCase("UNSPECIFIED")) {
        return 0;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unknown target type '");
      localStringBuilder.append(paramString);
      localStringBuilder.append("'!");
      throw new RuntimeException(localStringBuilder.toString());
    }
    return 2;
  }
  
  public static String targetToString(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return "unknown";
    case 5: 
      return "renderscript";
    case 4: 
      return "vbo";
    case 3: 
      return "gpu";
    case 2: 
      return "native";
    case 1: 
      return "simple";
    }
    return "unspecified";
  }
  
  public static FrameFormat unspecified()
  {
    return new FrameFormat(0, 0);
  }
  
  int calcSize(int[] paramArrayOfInt)
  {
    int i = 0;
    if ((paramArrayOfInt != null) && (paramArrayOfInt.length > 0))
    {
      int j = getBytesPerSample();
      int k = paramArrayOfInt.length;
      while (i < k)
      {
        j *= paramArrayOfInt[i];
        i++;
      }
      return j;
    }
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if (!(paramObject instanceof FrameFormat)) {
      return false;
    }
    paramObject = (FrameFormat)paramObject;
    if ((mBaseType != mBaseType) || (mTarget != mTarget) || (mBytesPerSample != mBytesPerSample) || (!Arrays.equals(mDimensions, mDimensions)) || (!mMetaData.equals(mMetaData))) {
      bool = false;
    }
    return bool;
  }
  
  public int getBaseType()
  {
    return mBaseType;
  }
  
  public int getBytesPerSample()
  {
    return mBytesPerSample;
  }
  
  public int getDepth()
  {
    int i;
    if ((mDimensions != null) && (mDimensions.length >= 3)) {
      i = mDimensions[2];
    } else {
      i = -1;
    }
    return i;
  }
  
  public int getDimension(int paramInt)
  {
    return mDimensions[paramInt];
  }
  
  public int getDimensionCount()
  {
    int i;
    if (mDimensions == null) {
      i = 0;
    } else {
      i = mDimensions.length;
    }
    return i;
  }
  
  public int[] getDimensions()
  {
    return mDimensions;
  }
  
  public int getHeight()
  {
    int i;
    if ((mDimensions != null) && (mDimensions.length >= 2)) {
      i = mDimensions[1];
    } else {
      i = -1;
    }
    return i;
  }
  
  public int getLength()
  {
    int i;
    if ((mDimensions != null) && (mDimensions.length >= 1)) {
      i = mDimensions[0];
    } else {
      i = -1;
    }
    return i;
  }
  
  public Object getMetaValue(String paramString)
  {
    if (mMetaData != null) {
      paramString = mMetaData.get(paramString);
    } else {
      paramString = null;
    }
    return paramString;
  }
  
  public int getNumberOfDimensions()
  {
    int i;
    if (mDimensions != null) {
      i = mDimensions.length;
    } else {
      i = 0;
    }
    return i;
  }
  
  public Class getObjectClass()
  {
    return mObjectClass;
  }
  
  public int getSize()
  {
    if (mSize == -1) {
      mSize = calcSize(mDimensions);
    }
    return mSize;
  }
  
  public int getTarget()
  {
    return mTarget;
  }
  
  public int getValuesPerSample()
  {
    return mBytesPerSample / bytesPerSampleOf(mBaseType);
  }
  
  public int getWidth()
  {
    return getLength();
  }
  
  public boolean hasMetaKey(String paramString)
  {
    boolean bool;
    if (mMetaData != null) {
      bool = mMetaData.containsKey(paramString);
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean hasMetaKey(String paramString, Class paramClass)
  {
    if ((mMetaData != null) && (mMetaData.containsKey(paramString)))
    {
      if (paramClass.isAssignableFrom(mMetaData.get(paramString).getClass())) {
        return true;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("FrameFormat meta-key '");
      localStringBuilder.append(paramString);
      localStringBuilder.append("' is of type ");
      localStringBuilder.append(mMetaData.get(paramString).getClass());
      localStringBuilder.append(" but expected to be of type ");
      localStringBuilder.append(paramClass);
      localStringBuilder.append("!");
      throw new RuntimeException(localStringBuilder.toString());
    }
    return false;
  }
  
  public int hashCode()
  {
    return mBaseType ^ 0x1073 ^ mBytesPerSample ^ getSize();
  }
  
  public boolean isBinaryDataType()
  {
    int i = mBaseType;
    boolean bool = true;
    if ((i < 1) || (mBaseType > 6)) {
      bool = false;
    }
    return bool;
  }
  
  public boolean isCompatibleWith(FrameFormat paramFrameFormat)
  {
    if ((paramFrameFormat.getBaseType() != 0) && (getBaseType() != paramFrameFormat.getBaseType())) {
      return false;
    }
    if ((paramFrameFormat.getTarget() != 0) && (getTarget() != paramFrameFormat.getTarget())) {
      return false;
    }
    if ((paramFrameFormat.getBytesPerSample() != 1) && (getBytesPerSample() != paramFrameFormat.getBytesPerSample())) {
      return false;
    }
    if ((paramFrameFormat.getDimensionCount() > 0) && (getDimensionCount() != paramFrameFormat.getDimensionCount())) {
      return false;
    }
    for (int i = 0; i < paramFrameFormat.getDimensionCount(); i++)
    {
      int j = paramFrameFormat.getDimension(i);
      if ((j != 0) && (getDimension(i) != j)) {
        return false;
      }
    }
    if ((paramFrameFormat.getObjectClass() != null) && ((getObjectClass() == null) || (!paramFrameFormat.getObjectClass().isAssignableFrom(getObjectClass())))) {
      return false;
    }
    if (mMetaData != null)
    {
      Iterator localIterator = mMetaData.keySet().iterator();
      while (localIterator.hasNext())
      {
        String str = (String)localIterator.next();
        if ((mMetaData != null) && (mMetaData.containsKey(str)) && (mMetaData.get(str).equals(mMetaData.get(str)))) {}
        return false;
      }
    }
    return true;
  }
  
  boolean isReplaceableBy(FrameFormat paramFrameFormat)
  {
    boolean bool;
    if ((mTarget == mTarget) && (getSize() == paramFrameFormat.getSize()) && (Arrays.equals(mDimensions, mDimensions))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean mayBeCompatibleWith(FrameFormat paramFrameFormat)
  {
    if ((paramFrameFormat.getBaseType() != 0) && (getBaseType() != 0) && (getBaseType() != paramFrameFormat.getBaseType())) {
      return false;
    }
    if ((paramFrameFormat.getTarget() != 0) && (getTarget() != 0) && (getTarget() != paramFrameFormat.getTarget())) {
      return false;
    }
    if ((paramFrameFormat.getBytesPerSample() != 1) && (getBytesPerSample() != 1) && (getBytesPerSample() != paramFrameFormat.getBytesPerSample())) {
      return false;
    }
    if ((paramFrameFormat.getDimensionCount() > 0) && (getDimensionCount() > 0) && (getDimensionCount() != paramFrameFormat.getDimensionCount())) {
      return false;
    }
    for (int i = 0; i < paramFrameFormat.getDimensionCount(); i++)
    {
      int j = paramFrameFormat.getDimension(i);
      if ((j != 0) && (getDimension(i) != 0) && (getDimension(i) != j)) {
        return false;
      }
    }
    if ((paramFrameFormat.getObjectClass() != null) && (getObjectClass() != null) && (!paramFrameFormat.getObjectClass().isAssignableFrom(getObjectClass()))) {
      return false;
    }
    if ((mMetaData != null) && (mMetaData != null))
    {
      Iterator localIterator = mMetaData.keySet().iterator();
      while (localIterator.hasNext())
      {
        String str = (String)localIterator.next();
        if ((mMetaData.containsKey(str)) && (!mMetaData.get(str).equals(mMetaData.get(str)))) {
          return false;
        }
      }
    }
    return true;
  }
  
  public MutableFrameFormat mutableCopy()
  {
    MutableFrameFormat localMutableFrameFormat = new MutableFrameFormat();
    localMutableFrameFormat.setBaseType(getBaseType());
    localMutableFrameFormat.setTarget(getTarget());
    localMutableFrameFormat.setBytesPerSample(getBytesPerSample());
    localMutableFrameFormat.setDimensions(getDimensions());
    localMutableFrameFormat.setObjectClass(getObjectClass());
    KeyValueMap localKeyValueMap;
    if (mMetaData == null) {
      localKeyValueMap = null;
    } else {
      localKeyValueMap = (KeyValueMap)mMetaData.clone();
    }
    mMetaData = localKeyValueMap;
    return localMutableFrameFormat;
  }
  
  public String toString()
  {
    int i = getValuesPerSample();
    String str;
    if (i == 1) {
      str = "";
    } else {
      str = String.valueOf(i);
    }
    Object localObject1;
    if (mTarget == 0)
    {
      localObject1 = "";
    }
    else
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append(targetToString(mTarget));
      ((StringBuilder)localObject1).append(" ");
      localObject1 = ((StringBuilder)localObject1).toString();
    }
    Object localObject2;
    if (mObjectClass == null)
    {
      localObject2 = "";
    }
    else
    {
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append(" class(");
      ((StringBuilder)localObject2).append(mObjectClass.getSimpleName());
      ((StringBuilder)localObject2).append(") ");
      localObject2 = ((StringBuilder)localObject2).toString();
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append((String)localObject1);
    localStringBuilder.append(baseTypeToString(mBaseType));
    localStringBuilder.append(str);
    localStringBuilder.append(dimensionsToString(mDimensions));
    localStringBuilder.append((String)localObject2);
    localStringBuilder.append(metaDataToString(mMetaData));
    return localStringBuilder.toString();
  }
}
