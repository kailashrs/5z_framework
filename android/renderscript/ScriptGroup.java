package android.renderscript;

import android.util.Log;
import android.util.Pair;
import dalvik.system.CloseGuard;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public final class ScriptGroup
  extends BaseObj
{
  private static final String TAG = "ScriptGroup";
  private List<Closure> mClosures;
  IO[] mInputs;
  private List<Input> mInputs2;
  private String mName;
  IO[] mOutputs;
  private Future[] mOutputs2;
  
  ScriptGroup(long paramLong, RenderScript paramRenderScript)
  {
    super(paramLong, paramRenderScript);
    guard.open("destroy");
  }
  
  ScriptGroup(RenderScript paramRenderScript, String paramString, List<Closure> paramList, List<Input> paramList1, Future[] paramArrayOfFuture)
  {
    super(0L, paramRenderScript);
    mName = paramString;
    mClosures = paramList;
    mInputs2 = paramList1;
    mOutputs2 = paramArrayOfFuture;
    paramList1 = new long[paramList.size()];
    for (int i = 0; i < paramList1.length; i++) {
      paramList1[i] = ((Closure)paramList.get(i)).getID(paramRenderScript);
    }
    setID(paramRenderScript.nScriptGroup2Create(paramString, RenderScript.getCachePath(), paramList1));
    guard.open("destroy");
  }
  
  public void destroy()
  {
    super.destroy();
    if (mClosures != null)
    {
      Iterator localIterator = mClosures.iterator();
      while (localIterator.hasNext()) {
        ((Closure)localIterator.next()).destroy();
      }
    }
  }
  
  public void execute()
  {
    mRS.nScriptGroupExecute(getID(mRS));
  }
  
  public Object[] execute(Object... paramVarArgs)
  {
    Object localObject;
    if (paramVarArgs.length < mInputs2.size())
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(toString());
      ((StringBuilder)localObject).append(" receives ");
      ((StringBuilder)localObject).append(paramVarArgs.length);
      ((StringBuilder)localObject).append(" inputs, less than expected ");
      ((StringBuilder)localObject).append(mInputs2.size());
      Log.e("ScriptGroup", ((StringBuilder)localObject).toString());
      return null;
    }
    if (paramVarArgs.length > mInputs2.size())
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(toString());
      ((StringBuilder)localObject).append(" receives ");
      ((StringBuilder)localObject).append(paramVarArgs.length);
      ((StringBuilder)localObject).append(" inputs, more than expected ");
      ((StringBuilder)localObject).append(mInputs2.size());
      Log.i("ScriptGroup", ((StringBuilder)localObject).toString());
    }
    int i = 0;
    int j = 0;
    while (j < mInputs2.size())
    {
      localObject = paramVarArgs[j];
      if ((!(localObject instanceof Future)) && (!(localObject instanceof Input)))
      {
        ((Input)mInputs2.get(j)).set(localObject);
        j++;
      }
      else
      {
        paramVarArgs = new StringBuilder();
        paramVarArgs.append(toString());
        paramVarArgs.append(": input ");
        paramVarArgs.append(j);
        paramVarArgs.append(" is a future or unbound value");
        Log.e("ScriptGroup", paramVarArgs.toString());
        return null;
      }
    }
    mRS.nScriptGroup2Execute(getID(mRS));
    Object[] arrayOfObject = new Object[mOutputs2.length];
    j = 0;
    Future[] arrayOfFuture = mOutputs2;
    int k = arrayOfFuture.length;
    while (i < k)
    {
      localObject = arrayOfFuture[i].getValue();
      paramVarArgs = (Object[])localObject;
      if ((localObject instanceof Input)) {
        paramVarArgs = ((Input)localObject).get();
      }
      arrayOfObject[j] = paramVarArgs;
      i++;
      j++;
    }
    return arrayOfObject;
  }
  
  public void setInput(Script.KernelID paramKernelID, Allocation paramAllocation)
  {
    for (int i = 0; i < mInputs.length; i++) {
      if (mInputs[i].mKID == paramKernelID)
      {
        mInputs[i].mAllocation = paramAllocation;
        mRS.nScriptGroupSetInput(getID(mRS), paramKernelID.getID(mRS), mRS.safeID(paramAllocation));
        return;
      }
    }
    throw new RSIllegalArgumentException("Script not found");
  }
  
  public void setOutput(Script.KernelID paramKernelID, Allocation paramAllocation)
  {
    for (int i = 0; i < mOutputs.length; i++) {
      if (mOutputs[i].mKID == paramKernelID)
      {
        mOutputs[i].mAllocation = paramAllocation;
        mRS.nScriptGroupSetOutput(getID(mRS), paramKernelID.getID(mRS), mRS.safeID(paramAllocation));
        return;
      }
    }
    throw new RSIllegalArgumentException("Script not found");
  }
  
  public static final class Binding
  {
    private final Script.FieldID mField;
    private final Object mValue;
    
    public Binding(Script.FieldID paramFieldID, Object paramObject)
    {
      mField = paramFieldID;
      mValue = paramObject;
    }
    
    Script.FieldID getField()
    {
      return mField;
    }
    
    Object getValue()
    {
      return mValue;
    }
  }
  
  public static final class Builder
  {
    private int mKernelCount;
    private ArrayList<ScriptGroup.ConnectLine> mLines = new ArrayList();
    private ArrayList<ScriptGroup.Node> mNodes = new ArrayList();
    private RenderScript mRS;
    
    public Builder(RenderScript paramRenderScript)
    {
      mRS = paramRenderScript;
    }
    
    private ScriptGroup.Node findNode(Script.KernelID paramKernelID)
    {
      for (int i = 0; i < mNodes.size(); i++)
      {
        ScriptGroup.Node localNode = (ScriptGroup.Node)mNodes.get(i);
        for (int j = 0; j < mKernels.size(); j++) {
          if (paramKernelID == mKernels.get(j)) {
            return localNode;
          }
        }
      }
      return null;
    }
    
    private ScriptGroup.Node findNode(Script paramScript)
    {
      for (int i = 0; i < mNodes.size(); i++) {
        if (paramScript == mNodes.get(i)).mScript) {
          return (ScriptGroup.Node)mNodes.get(i);
        }
      }
      return null;
    }
    
    private void mergeDAGs(int paramInt1, int paramInt2)
    {
      for (int i = 0; i < mNodes.size(); i++) {
        if (mNodes.get(i)).dagNumber == paramInt2) {
          mNodes.get(i)).dagNumber = paramInt1;
        }
      }
    }
    
    private void validateCycle(ScriptGroup.Node paramNode1, ScriptGroup.Node paramNode2)
    {
      for (int i = 0; i < mOutputs.size(); i++)
      {
        Object localObject = (ScriptGroup.ConnectLine)mOutputs.get(i);
        if (mToK != null)
        {
          ScriptGroup.Node localNode = findNode(mToK.mScript);
          if (!localNode.equals(paramNode2)) {
            validateCycle(localNode, paramNode2);
          } else {
            throw new RSInvalidStateException("Loops in group not allowed.");
          }
        }
        if (mToF != null)
        {
          localObject = findNode(mToF.mScript);
          if (!localObject.equals(paramNode2)) {
            validateCycle((ScriptGroup.Node)localObject, paramNode2);
          } else {
            throw new RSInvalidStateException("Loops in group not allowed.");
          }
        }
      }
    }
    
    private void validateDAG()
    {
      int i = 0;
      for (int j = 0; j < mNodes.size(); j++)
      {
        ScriptGroup.Node localNode = (ScriptGroup.Node)mNodes.get(j);
        if (mInputs.size() == 0)
        {
          if ((mOutputs.size() == 0) && (mNodes.size() > 1)) {
            throw new RSInvalidStateException("Groups cannot contain unconnected scripts");
          }
          validateDAGRecurse(localNode, j + 1);
        }
      }
      int k = mNodes.get(0)).dagNumber;
      j = i;
      while (j < mNodes.size()) {
        if (mNodes.get(j)).dagNumber == k) {
          j++;
        } else {
          throw new RSInvalidStateException("Multiple DAGs in group not allowed.");
        }
      }
    }
    
    private void validateDAGRecurse(ScriptGroup.Node paramNode, int paramInt)
    {
      if ((dagNumber != 0) && (dagNumber != paramInt))
      {
        mergeDAGs(dagNumber, paramInt);
        return;
      }
      dagNumber = paramInt;
      for (int i = 0; i < mOutputs.size(); i++)
      {
        ScriptGroup.ConnectLine localConnectLine = (ScriptGroup.ConnectLine)mOutputs.get(i);
        if (mToK != null) {
          validateDAGRecurse(findNode(mToK.mScript), paramInt);
        }
        if (mToF != null) {
          validateDAGRecurse(findNode(mToF.mScript), paramInt);
        }
      }
    }
    
    public Builder addConnection(Type paramType, Script.KernelID paramKernelID, Script.FieldID paramFieldID)
    {
      ScriptGroup.Node localNode1 = findNode(paramKernelID);
      if (localNode1 != null)
      {
        ScriptGroup.Node localNode2 = findNode(mScript);
        if (localNode2 != null)
        {
          ScriptGroup.ConnectLine localConnectLine = new ScriptGroup.ConnectLine(paramType, paramKernelID, paramFieldID);
          mLines.add(new ScriptGroup.ConnectLine(paramType, paramKernelID, paramFieldID));
          mOutputs.add(localConnectLine);
          mInputs.add(localConnectLine);
          validateCycle(localNode1, localNode1);
          return this;
        }
        throw new RSInvalidStateException("To script not found.");
      }
      throw new RSInvalidStateException("From script not found.");
    }
    
    public Builder addConnection(Type paramType, Script.KernelID paramKernelID1, Script.KernelID paramKernelID2)
    {
      ScriptGroup.Node localNode1 = findNode(paramKernelID1);
      if (localNode1 != null)
      {
        ScriptGroup.Node localNode2 = findNode(paramKernelID2);
        if (localNode2 != null)
        {
          ScriptGroup.ConnectLine localConnectLine = new ScriptGroup.ConnectLine(paramType, paramKernelID1, paramKernelID2);
          mLines.add(new ScriptGroup.ConnectLine(paramType, paramKernelID1, paramKernelID2));
          mOutputs.add(localConnectLine);
          mInputs.add(localConnectLine);
          validateCycle(localNode1, localNode1);
          return this;
        }
        throw new RSInvalidStateException("To script not found.");
      }
      throw new RSInvalidStateException("From script not found.");
    }
    
    public Builder addKernel(Script.KernelID paramKernelID)
    {
      if (mLines.size() == 0)
      {
        if (findNode(paramKernelID) != null) {
          return this;
        }
        mKernelCount += 1;
        ScriptGroup.Node localNode1 = findNode(mScript);
        ScriptGroup.Node localNode2 = localNode1;
        if (localNode1 == null)
        {
          localNode2 = new ScriptGroup.Node(mScript);
          mNodes.add(localNode2);
        }
        mKernels.add(paramKernelID);
        return this;
      }
      throw new RSInvalidStateException("Kernels may not be added once connections exist.");
    }
    
    public ScriptGroup create()
    {
      if (mNodes.size() != 0)
      {
        int i = 0;
        for (int j = 0; j < mNodes.size(); j++) {
          mNodes.get(j)).dagNumber = 0;
        }
        validateDAG();
        ArrayList localArrayList1 = new ArrayList();
        ArrayList localArrayList2 = new ArrayList();
        Object localObject1 = new long[mKernelCount];
        int k = 0;
        Object localObject2;
        Object localObject3;
        for (j = 0; j < mNodes.size(); j++)
        {
          localObject2 = (ScriptGroup.Node)mNodes.get(j);
          int m = 0;
          while (m < mKernels.size())
          {
            localObject3 = (Script.KernelID)mKernels.get(m);
            localObject1[k] = ((Script.KernelID)localObject3).getID(mRS);
            int n = 0;
            int i1 = 0;
            for (int i2 = 0; i2 < mInputs.size(); i2++) {
              if (mInputs.get(i2)).mToK == localObject3) {
                i1 = 1;
              }
            }
            for (i2 = 0; i2 < mOutputs.size(); i2++) {
              if (mOutputs.get(i2)).mFrom == localObject3) {
                n = 1;
              }
            }
            if (i1 == 0) {
              localArrayList1.add(new ScriptGroup.IO((Script.KernelID)localObject3));
            }
            if (n == 0) {
              localArrayList2.add(new ScriptGroup.IO((Script.KernelID)localObject3));
            }
            m++;
            k++;
          }
        }
        if (k == mKernelCount)
        {
          localObject2 = new long[mLines.size()];
          long[] arrayOfLong1 = new long[mLines.size()];
          long[] arrayOfLong2 = new long[mLines.size()];
          localObject3 = new long[mLines.size()];
          for (j = 0; j < mLines.size(); j++)
          {
            ScriptGroup.ConnectLine localConnectLine = (ScriptGroup.ConnectLine)mLines.get(j);
            localObject2[j] = mFrom.getID(mRS);
            if (mToK != null) {
              arrayOfLong1[j] = mToK.getID(mRS);
            }
            if (mToF != null) {
              arrayOfLong2[j] = mToF.getID(mRS);
            }
            localObject3[j] = mAllocationType.getID(mRS);
          }
          long l = mRS.nScriptGroupCreate((long[])localObject1, (long[])localObject2, arrayOfLong1, arrayOfLong2, (long[])localObject3);
          if (l != 0L)
          {
            localObject1 = new ScriptGroup(l, mRS);
            mOutputs = new ScriptGroup.IO[localArrayList2.size()];
            for (j = 0; j < localArrayList2.size(); j++) {
              mOutputs[j] = ((ScriptGroup.IO)localArrayList2.get(j));
            }
            mInputs = new ScriptGroup.IO[localArrayList1.size()];
            for (j = i; j < localArrayList1.size(); j++) {
              mInputs[j] = ((ScriptGroup.IO)localArrayList1.get(j));
            }
            return localObject1;
          }
          throw new RSRuntimeException("Object creation error, should not happen.");
        }
        throw new RSRuntimeException("Count mismatch, should not happen.");
      }
      throw new RSInvalidStateException("Empty script groups are not allowed");
    }
  }
  
  public static final class Builder2
  {
    private static final String TAG = "ScriptGroup.Builder2";
    List<ScriptGroup.Closure> mClosures;
    List<ScriptGroup.Input> mInputs;
    RenderScript mRS;
    
    public Builder2(RenderScript paramRenderScript)
    {
      mRS = paramRenderScript;
      mClosures = new ArrayList();
      mInputs = new ArrayList();
    }
    
    private ScriptGroup.Closure addInvokeInternal(Script.InvokeID paramInvokeID, Object[] paramArrayOfObject, Map<Script.FieldID, Object> paramMap)
    {
      paramInvokeID = new ScriptGroup.Closure(mRS, paramInvokeID, paramArrayOfObject, paramMap);
      mClosures.add(paramInvokeID);
      return paramInvokeID;
    }
    
    private ScriptGroup.Closure addKernelInternal(Script.KernelID paramKernelID, Type paramType, Object[] paramArrayOfObject, Map<Script.FieldID, Object> paramMap)
    {
      paramKernelID = new ScriptGroup.Closure(mRS, paramKernelID, paramType, paramArrayOfObject, paramMap);
      mClosures.add(paramKernelID);
      return paramKernelID;
    }
    
    private boolean seperateArgsAndBindings(Object[] paramArrayOfObject, ArrayList<Object> paramArrayList, Map<Script.FieldID, Object> paramMap)
    {
      int j;
      for (int i = 0;; i++)
      {
        j = i;
        if (i >= paramArrayOfObject.length) {
          break;
        }
        if ((paramArrayOfObject[i] instanceof ScriptGroup.Binding))
        {
          j = i;
          break;
        }
        paramArrayList.add(paramArrayOfObject[i]);
      }
      while (j < paramArrayOfObject.length)
      {
        if (!(paramArrayOfObject[j] instanceof ScriptGroup.Binding)) {
          return false;
        }
        paramArrayList = (ScriptGroup.Binding)paramArrayOfObject[j];
        paramMap.put(paramArrayList.getField(), paramArrayList.getValue());
        j++;
      }
      return true;
    }
    
    public ScriptGroup.Input addInput()
    {
      ScriptGroup.Input localInput = new ScriptGroup.Input();
      mInputs.add(localInput);
      return localInput;
    }
    
    public ScriptGroup.Closure addInvoke(Script.InvokeID paramInvokeID, Object... paramVarArgs)
    {
      ArrayList localArrayList = new ArrayList();
      HashMap localHashMap = new HashMap();
      if (!seperateArgsAndBindings(paramVarArgs, localArrayList, localHashMap)) {
        return null;
      }
      return addInvokeInternal(paramInvokeID, localArrayList.toArray(), localHashMap);
    }
    
    public ScriptGroup.Closure addKernel(Script.KernelID paramKernelID, Type paramType, Object... paramVarArgs)
    {
      ArrayList localArrayList = new ArrayList();
      HashMap localHashMap = new HashMap();
      if (!seperateArgsAndBindings(paramVarArgs, localArrayList, localHashMap)) {
        return null;
      }
      return addKernelInternal(paramKernelID, paramType, localArrayList.toArray(), localHashMap);
    }
    
    public ScriptGroup create(String paramString, ScriptGroup.Future... paramVarArgs)
    {
      if ((paramString != null) && (!paramString.isEmpty()) && (paramString.length() <= 100) && (paramString.equals(paramString.replaceAll("[^a-zA-Z0-9-]", "_"))))
      {
        paramString = new ScriptGroup(mRS, paramString, mClosures, mInputs, paramVarArgs);
        mClosures = new ArrayList();
        mInputs = new ArrayList();
        return paramString;
      }
      throw new RSIllegalArgumentException("invalid script group name");
    }
  }
  
  public static final class Closure
    extends BaseObj
  {
    private static final String TAG = "Closure";
    private Object[] mArgs;
    private Map<Script.FieldID, Object> mBindings;
    private FieldPacker mFP;
    private Map<Script.FieldID, ScriptGroup.Future> mGlobalFuture;
    private ScriptGroup.Future mReturnFuture;
    private Allocation mReturnValue;
    
    Closure(long paramLong, RenderScript paramRenderScript)
    {
      super(paramRenderScript);
    }
    
    Closure(RenderScript paramRenderScript, Script.InvokeID paramInvokeID, Object[] paramArrayOfObject, Map<Script.FieldID, Object> paramMap)
    {
      super(paramRenderScript);
      mFP = FieldPacker.createFromArray(paramArrayOfObject);
      mArgs = paramArrayOfObject;
      mBindings = paramMap;
      mGlobalFuture = new HashMap();
      int i = paramMap.size();
      long[] arrayOfLong1 = new long[i];
      long[] arrayOfLong2 = new long[i];
      paramArrayOfObject = new int[i];
      long[] arrayOfLong3 = new long[i];
      Object localObject1 = new long[i];
      Iterator localIterator = paramMap.entrySet().iterator();
      i = 0;
      paramMap = (Map<Script.FieldID, Object>)localObject1;
      while (localIterator.hasNext())
      {
        Object localObject2 = (Map.Entry)localIterator.next();
        localObject1 = ((Map.Entry)localObject2).getValue();
        localObject2 = (Script.FieldID)((Map.Entry)localObject2).getKey();
        arrayOfLong1[i] = ((Script.FieldID)localObject2).getID(paramRenderScript);
        retrieveValueAndDependenceInfo(paramRenderScript, i, (Script.FieldID)localObject2, localObject1, arrayOfLong2, paramArrayOfObject, arrayOfLong3, paramMap);
        i++;
      }
      setID(paramRenderScript.nInvokeClosureCreate(paramInvokeID.getID(paramRenderScript), mFP.getData(), arrayOfLong1, arrayOfLong2, paramArrayOfObject));
      guard.open("destroy");
    }
    
    Closure(RenderScript paramRenderScript, Script.KernelID paramKernelID, Type paramType, Object[] paramArrayOfObject, Map<Script.FieldID, Object> paramMap)
    {
      super(paramRenderScript);
      mArgs = paramArrayOfObject;
      mReturnValue = Allocation.createTyped(paramRenderScript, paramType);
      mBindings = paramMap;
      mGlobalFuture = new HashMap();
      int i = paramArrayOfObject.length + paramMap.size();
      paramType = new long[i];
      long[] arrayOfLong1 = new long[i];
      int[] arrayOfInt = new int[i];
      long[] arrayOfLong2 = new long[i];
      long[] arrayOfLong3 = new long[i];
      for (int j = 0; j < paramArrayOfObject.length; j++)
      {
        paramType[j] = 0L;
        retrieveValueAndDependenceInfo(paramRenderScript, j, null, paramArrayOfObject[j], arrayOfLong1, arrayOfInt, arrayOfLong2, arrayOfLong3);
      }
      paramArrayOfObject = paramMap.entrySet().iterator();
      while (paramArrayOfObject.hasNext())
      {
        Object localObject = (Map.Entry)paramArrayOfObject.next();
        paramMap = ((Map.Entry)localObject).getValue();
        localObject = (Script.FieldID)((Map.Entry)localObject).getKey();
        paramType[j] = ((Script.FieldID)localObject).getID(paramRenderScript);
        retrieveValueAndDependenceInfo(paramRenderScript, j, (Script.FieldID)localObject, paramMap, arrayOfLong1, arrayOfInt, arrayOfLong2, arrayOfLong3);
        j++;
      }
      setID(paramRenderScript.nClosureCreate(paramKernelID.getID(paramRenderScript), mReturnValue.getID(paramRenderScript), paramType, arrayOfLong1, arrayOfInt, arrayOfLong2, arrayOfLong3));
      guard.open("destroy");
    }
    
    private void retrieveValueAndDependenceInfo(RenderScript paramRenderScript, int paramInt, Script.FieldID paramFieldID, Object paramObject, long[] paramArrayOfLong1, int[] paramArrayOfInt, long[] paramArrayOfLong2, long[] paramArrayOfLong3)
    {
      if ((paramObject instanceof ScriptGroup.Future))
      {
        ScriptGroup.Future localFuture = (ScriptGroup.Future)paramObject;
        paramObject = localFuture.getValue();
        paramArrayOfLong2[paramInt] = localFuture.getClosure().getID(paramRenderScript);
        paramArrayOfLong2 = localFuture.getFieldID();
        long l;
        if (paramArrayOfLong2 != null) {
          l = paramArrayOfLong2.getID(paramRenderScript);
        } else {
          l = 0L;
        }
        paramArrayOfLong3[paramInt] = l;
      }
      else
      {
        paramArrayOfLong2[paramInt] = 0L;
        paramArrayOfLong3[paramInt] = 0L;
      }
      if ((paramObject instanceof ScriptGroup.Input))
      {
        paramRenderScript = (ScriptGroup.Input)paramObject;
        if (paramInt < mArgs.length) {
          paramRenderScript.addReference(this, paramInt);
        } else {
          paramRenderScript.addReference(this, paramFieldID);
        }
        paramArrayOfLong1[paramInt] = 0L;
        paramArrayOfInt[paramInt] = 0;
      }
      else
      {
        paramRenderScript = new ValueAndSize(paramRenderScript, paramObject);
        paramArrayOfLong1[paramInt] = value;
        paramArrayOfInt[paramInt] = size;
      }
    }
    
    public void destroy()
    {
      super.destroy();
      if (mReturnValue != null) {
        mReturnValue.destroy();
      }
    }
    
    protected void finalize()
      throws Throwable
    {
      mReturnValue = null;
      super.finalize();
    }
    
    public ScriptGroup.Future getGlobal(Script.FieldID paramFieldID)
    {
      Object localObject1 = (ScriptGroup.Future)mGlobalFuture.get(paramFieldID);
      Object localObject2 = localObject1;
      if (localObject1 == null)
      {
        localObject1 = mBindings.get(paramFieldID);
        localObject2 = localObject1;
        if ((localObject1 instanceof ScriptGroup.Future)) {
          localObject2 = ((ScriptGroup.Future)localObject1).getValue();
        }
        localObject2 = new ScriptGroup.Future(this, paramFieldID, localObject2);
        mGlobalFuture.put(paramFieldID, localObject2);
      }
      return localObject2;
    }
    
    public ScriptGroup.Future getReturn()
    {
      if (mReturnFuture == null) {
        mReturnFuture = new ScriptGroup.Future(this, null, mReturnValue);
      }
      return mReturnFuture;
    }
    
    void setArg(int paramInt, Object paramObject)
    {
      Object localObject = paramObject;
      if ((paramObject instanceof ScriptGroup.Future)) {
        localObject = ((ScriptGroup.Future)paramObject).getValue();
      }
      mArgs[paramInt] = localObject;
      paramObject = new ValueAndSize(mRS, localObject);
      mRS.nClosureSetArg(getID(mRS), paramInt, value, size);
    }
    
    void setGlobal(Script.FieldID paramFieldID, Object paramObject)
    {
      Object localObject = paramObject;
      if ((paramObject instanceof ScriptGroup.Future)) {
        localObject = ((ScriptGroup.Future)paramObject).getValue();
      }
      mBindings.put(paramFieldID, localObject);
      paramObject = new ValueAndSize(mRS, localObject);
      mRS.nClosureSetGlobal(getID(mRS), paramFieldID.getID(mRS), value, size);
    }
    
    private static final class ValueAndSize
    {
      public int size;
      public long value;
      
      public ValueAndSize(RenderScript paramRenderScript, Object paramObject)
      {
        if ((paramObject instanceof Allocation))
        {
          value = ((Allocation)paramObject).getID(paramRenderScript);
          size = -1;
        }
        else if ((paramObject instanceof Boolean))
        {
          long l;
          if (((Boolean)paramObject).booleanValue()) {
            l = 1L;
          } else {
            l = 0L;
          }
          value = l;
          size = 4;
        }
        else if ((paramObject instanceof Integer))
        {
          value = ((Integer)paramObject).longValue();
          size = 4;
        }
        else if ((paramObject instanceof Long))
        {
          value = ((Long)paramObject).longValue();
          size = 8;
        }
        else if ((paramObject instanceof Float))
        {
          value = Float.floatToRawIntBits(((Float)paramObject).floatValue());
          size = 4;
        }
        else if ((paramObject instanceof Double))
        {
          value = Double.doubleToRawLongBits(((Double)paramObject).doubleValue());
          size = 8;
        }
      }
    }
  }
  
  static class ConnectLine
  {
    Type mAllocationType;
    Script.KernelID mFrom;
    Script.FieldID mToF;
    Script.KernelID mToK;
    
    ConnectLine(Type paramType, Script.KernelID paramKernelID, Script.FieldID paramFieldID)
    {
      mFrom = paramKernelID;
      mToF = paramFieldID;
      mAllocationType = paramType;
    }
    
    ConnectLine(Type paramType, Script.KernelID paramKernelID1, Script.KernelID paramKernelID2)
    {
      mFrom = paramKernelID1;
      mToK = paramKernelID2;
      mAllocationType = paramType;
    }
  }
  
  public static final class Future
  {
    ScriptGroup.Closure mClosure;
    Script.FieldID mFieldID;
    Object mValue;
    
    Future(ScriptGroup.Closure paramClosure, Script.FieldID paramFieldID, Object paramObject)
    {
      mClosure = paramClosure;
      mFieldID = paramFieldID;
      mValue = paramObject;
    }
    
    ScriptGroup.Closure getClosure()
    {
      return mClosure;
    }
    
    Script.FieldID getFieldID()
    {
      return mFieldID;
    }
    
    Object getValue()
    {
      return mValue;
    }
  }
  
  static class IO
  {
    Allocation mAllocation;
    Script.KernelID mKID;
    
    IO(Script.KernelID paramKernelID)
    {
      mKID = paramKernelID;
    }
  }
  
  public static final class Input
  {
    List<Pair<ScriptGroup.Closure, Integer>> mArgIndex = new ArrayList();
    List<Pair<ScriptGroup.Closure, Script.FieldID>> mFieldID = new ArrayList();
    Object mValue;
    
    Input() {}
    
    void addReference(ScriptGroup.Closure paramClosure, int paramInt)
    {
      mArgIndex.add(Pair.create(paramClosure, Integer.valueOf(paramInt)));
    }
    
    void addReference(ScriptGroup.Closure paramClosure, Script.FieldID paramFieldID)
    {
      mFieldID.add(Pair.create(paramClosure, paramFieldID));
    }
    
    Object get()
    {
      return mValue;
    }
    
    void set(Object paramObject)
    {
      mValue = paramObject;
      Object localObject1 = mArgIndex.iterator();
      while (((Iterator)localObject1).hasNext())
      {
        localObject2 = (Pair)((Iterator)localObject1).next();
        ((ScriptGroup.Closure)first).setArg(((Integer)second).intValue(), paramObject);
      }
      Object localObject2 = mFieldID.iterator();
      while (((Iterator)localObject2).hasNext())
      {
        localObject1 = (Pair)((Iterator)localObject2).next();
        ((ScriptGroup.Closure)first).setGlobal((Script.FieldID)second, paramObject);
      }
    }
  }
  
  static class Node
  {
    int dagNumber;
    ArrayList<ScriptGroup.ConnectLine> mInputs = new ArrayList();
    ArrayList<Script.KernelID> mKernels = new ArrayList();
    Node mNext;
    ArrayList<ScriptGroup.ConnectLine> mOutputs = new ArrayList();
    Script mScript;
    
    Node(Script paramScript)
    {
      mScript = paramScript;
    }
  }
}
