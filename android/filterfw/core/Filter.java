package android.filterfw.core;

import android.filterfw.format.ObjectFormat;
import android.filterfw.io.GraphIOException;
import android.filterfw.io.TextGraphReader;
import android.util.Log;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

public abstract class Filter
{
  static final int STATUS_ERROR = 6;
  static final int STATUS_FINISHED = 5;
  static final int STATUS_PREINIT = 0;
  static final int STATUS_PREPARED = 2;
  static final int STATUS_PROCESSING = 3;
  static final int STATUS_RELEASED = 7;
  static final int STATUS_SLEEPING = 4;
  static final int STATUS_UNPREPARED = 1;
  private static final String TAG = "Filter";
  private long mCurrentTimestamp;
  private HashSet<Frame> mFramesToRelease;
  private HashMap<String, Frame> mFramesToSet;
  private int mInputCount = -1;
  private HashMap<String, InputPort> mInputPorts;
  private boolean mIsOpen = false;
  private boolean mLogVerbose;
  private String mName;
  private int mOutputCount = -1;
  private HashMap<String, OutputPort> mOutputPorts;
  private int mSleepDelay;
  private int mStatus = 0;
  
  public Filter(String paramString)
  {
    mName = paramString;
    mFramesToRelease = new HashSet();
    mFramesToSet = new HashMap();
    mStatus = 0;
    mLogVerbose = Log.isLoggable("Filter", 2);
  }
  
  private final void addAndSetFinalPorts(KeyValueMap paramKeyValueMap)
  {
    for (Field localField : getClass().getDeclaredFields())
    {
      Object localObject = localField.getAnnotation(GenerateFinalPort.class);
      if (localObject != null)
      {
        GenerateFinalPort localGenerateFinalPort = (GenerateFinalPort)localObject;
        if (localGenerateFinalPort.name().isEmpty()) {
          localObject = localField.getName();
        } else {
          localObject = localGenerateFinalPort.name();
        }
        addFieldPort((String)localObject, localField, localGenerateFinalPort.hasDefault(), true);
        if (paramKeyValueMap.containsKey(localObject))
        {
          setImmediateInputValue((String)localObject, paramKeyValueMap.get(localObject));
          paramKeyValueMap.remove(localObject);
        }
        else if (!localGenerateFinalPort.hasDefault())
        {
          paramKeyValueMap = new StringBuilder();
          paramKeyValueMap.append("No value specified for final input port '");
          paramKeyValueMap.append((String)localObject);
          paramKeyValueMap.append("' of filter ");
          paramKeyValueMap.append(this);
          paramKeyValueMap.append("!");
          throw new RuntimeException(paramKeyValueMap.toString());
        }
      }
    }
  }
  
  private final void addAnnotatedPorts()
  {
    for (Field localField : getClass().getDeclaredFields())
    {
      Object localObject = localField.getAnnotation(GenerateFieldPort.class);
      if (localObject != null)
      {
        addFieldGenerator((GenerateFieldPort)localObject, localField);
      }
      else
      {
        localObject = localField.getAnnotation(GenerateProgramPort.class);
        if (localObject != null)
        {
          addProgramGenerator((GenerateProgramPort)localObject, localField);
        }
        else
        {
          localObject = localField.getAnnotation(GenerateProgramPorts.class);
          if (localObject != null)
          {
            localObject = ((GenerateProgramPorts)localObject).value();
            int k = localObject.length;
            for (int m = 0; m < k; m++) {
              addProgramGenerator(localObject[m], localField);
            }
          }
        }
      }
    }
  }
  
  private final void addFieldGenerator(GenerateFieldPort paramGenerateFieldPort, Field paramField)
  {
    String str;
    if (paramGenerateFieldPort.name().isEmpty()) {
      str = paramField.getName();
    } else {
      str = paramGenerateFieldPort.name();
    }
    addFieldPort(str, paramField, paramGenerateFieldPort.hasDefault(), false);
  }
  
  private final void addProgramGenerator(GenerateProgramPort paramGenerateProgramPort, Field paramField)
  {
    String str1 = paramGenerateProgramPort.name();
    String str2;
    if (paramGenerateProgramPort.variableName().isEmpty()) {
      str2 = str1;
    } else {
      str2 = paramGenerateProgramPort.variableName();
    }
    addProgramPort(str1, str2, paramField, paramGenerateProgramPort.type(), paramGenerateProgramPort.hasDefault());
  }
  
  private final void closePorts()
  {
    if (mLogVerbose)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Closing all ports on ");
      ((StringBuilder)localObject).append(this);
      ((StringBuilder)localObject).append("!");
      Log.v("Filter", ((StringBuilder)localObject).toString());
    }
    Object localObject = mInputPorts.values().iterator();
    while (((Iterator)localObject).hasNext()) {
      ((InputPort)((Iterator)localObject).next()).close();
    }
    localObject = mOutputPorts.values().iterator();
    while (((Iterator)localObject).hasNext()) {
      ((OutputPort)((Iterator)localObject).next()).close();
    }
  }
  
  private final boolean filterMustClose()
  {
    Object localObject1 = mInputPorts.values().iterator();
    Object localObject2;
    while (((Iterator)localObject1).hasNext())
    {
      localObject2 = (InputPort)((Iterator)localObject1).next();
      if (((InputPort)localObject2).filterMustClose())
      {
        if (mLogVerbose)
        {
          localObject1 = new StringBuilder();
          ((StringBuilder)localObject1).append("Filter ");
          ((StringBuilder)localObject1).append(this);
          ((StringBuilder)localObject1).append(" must close due to port ");
          ((StringBuilder)localObject1).append(localObject2);
          Log.v("Filter", ((StringBuilder)localObject1).toString());
        }
        return true;
      }
    }
    localObject1 = mOutputPorts.values().iterator();
    while (((Iterator)localObject1).hasNext())
    {
      localObject2 = (OutputPort)((Iterator)localObject1).next();
      if (((OutputPort)localObject2).filterMustClose())
      {
        if (mLogVerbose)
        {
          localObject1 = new StringBuilder();
          ((StringBuilder)localObject1).append("Filter ");
          ((StringBuilder)localObject1).append(this);
          ((StringBuilder)localObject1).append(" must close due to port ");
          ((StringBuilder)localObject1).append(localObject2);
          Log.v("Filter", ((StringBuilder)localObject1).toString());
        }
        return true;
      }
    }
    return false;
  }
  
  private final void initFinalPorts(KeyValueMap paramKeyValueMap)
  {
    mInputPorts = new HashMap();
    mOutputPorts = new HashMap();
    addAndSetFinalPorts(paramKeyValueMap);
  }
  
  private final void initRemainingPorts(KeyValueMap paramKeyValueMap)
  {
    addAnnotatedPorts();
    setupPorts();
    setInitialInputValues(paramKeyValueMap);
  }
  
  private final boolean inputConditionsMet()
  {
    Object localObject = mInputPorts.values().iterator();
    while (((Iterator)localObject).hasNext())
    {
      FilterPort localFilterPort = (FilterPort)((Iterator)localObject).next();
      if (!localFilterPort.isReady())
      {
        if (mLogVerbose)
        {
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("Input condition not met: ");
          ((StringBuilder)localObject).append(localFilterPort);
          ((StringBuilder)localObject).append("!");
          Log.v("Filter", ((StringBuilder)localObject).toString());
        }
        return false;
      }
    }
    return true;
  }
  
  public static final boolean isAvailable(String paramString)
  {
    ClassLoader localClassLoader = Thread.currentThread().getContextClassLoader();
    try
    {
      paramString = localClassLoader.loadClass(paramString);
      try
      {
        paramString.asSubclass(Filter.class);
        return true;
      }
      catch (ClassCastException paramString)
      {
        return false;
      }
      return false;
    }
    catch (ClassNotFoundException paramString) {}
  }
  
  private final boolean outputConditionsMet()
  {
    Object localObject = mOutputPorts.values().iterator();
    while (((Iterator)localObject).hasNext())
    {
      FilterPort localFilterPort = (FilterPort)((Iterator)localObject).next();
      if (!localFilterPort.isReady())
      {
        if (mLogVerbose)
        {
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("Output condition not met: ");
          ((StringBuilder)localObject).append(localFilterPort);
          ((StringBuilder)localObject).append("!");
          Log.v("Filter", ((StringBuilder)localObject).toString());
        }
        return false;
      }
    }
    return true;
  }
  
  private final void releasePulledFrames(FilterContext paramFilterContext)
  {
    Iterator localIterator = mFramesToRelease.iterator();
    while (localIterator.hasNext())
    {
      Frame localFrame = (Frame)localIterator.next();
      paramFilterContext.getFrameManager().releaseFrame(localFrame);
    }
    mFramesToRelease.clear();
  }
  
  private final void setImmediateInputValue(String paramString, Object paramObject)
  {
    if (mLogVerbose)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Setting immediate value ");
      localStringBuilder.append(paramObject);
      localStringBuilder.append(" for port ");
      localStringBuilder.append(paramString);
      localStringBuilder.append("!");
      Log.v("Filter", localStringBuilder.toString());
    }
    paramString = getInputPort(paramString);
    paramString.open();
    paramString.setFrame(SimpleFrame.wrapObject(paramObject, null));
  }
  
  private final void setInitialInputValues(KeyValueMap paramKeyValueMap)
  {
    paramKeyValueMap = paramKeyValueMap.entrySet().iterator();
    while (paramKeyValueMap.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)paramKeyValueMap.next();
      setInputValue((String)localEntry.getKey(), localEntry.getValue());
    }
  }
  
  private final void transferInputFrames(FilterContext paramFilterContext)
  {
    Iterator localIterator = mInputPorts.values().iterator();
    while (localIterator.hasNext()) {
      ((InputPort)localIterator.next()).transfer(paramFilterContext);
    }
  }
  
  private final Frame wrapInputValue(String paramString, Object paramObject)
  {
    int i = 1;
    MutableFrameFormat localMutableFrameFormat = ObjectFormat.fromObject(paramObject, 1);
    if (paramObject == null)
    {
      paramString = getInputPort(paramString).getPortFormat();
      if (paramString == null) {
        paramString = null;
      } else {
        paramString = paramString.getObjectClass();
      }
      localMutableFrameFormat.setObjectClass(paramString);
    }
    if (((paramObject instanceof Number)) || ((paramObject instanceof Boolean)) || ((paramObject instanceof String)) || (!(paramObject instanceof Serializable))) {
      i = 0;
    }
    if (i != 0) {
      paramString = new SerializedFrame(localMutableFrameFormat, null);
    } else {
      paramString = new SimpleFrame(localMutableFrameFormat, null);
    }
    paramString.setObjectValue(paramObject);
    return paramString;
  }
  
  protected void addFieldPort(String paramString, Field paramField, boolean paramBoolean1, boolean paramBoolean2)
  {
    paramField.setAccessible(true);
    Object localObject;
    if (paramBoolean2) {
      localObject = new FinalPort(this, paramString, paramField, paramBoolean1);
    } else {
      localObject = new FieldPort(this, paramString, paramField, paramBoolean1);
    }
    if (mLogVerbose)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Filter ");
      localStringBuilder.append(this);
      localStringBuilder.append(" adding ");
      localStringBuilder.append(localObject);
      Log.v("Filter", localStringBuilder.toString());
    }
    ((InputPort)localObject).setPortFormat(ObjectFormat.fromClass(paramField.getType(), 1));
    mInputPorts.put(paramString, localObject);
  }
  
  protected void addInputPort(String paramString)
  {
    addMaskedInputPort(paramString, null);
  }
  
  protected void addMaskedInputPort(String paramString, FrameFormat paramFrameFormat)
  {
    StreamPort localStreamPort = new StreamPort(this, paramString);
    if (mLogVerbose)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Filter ");
      localStringBuilder.append(this);
      localStringBuilder.append(" adding ");
      localStringBuilder.append(localStreamPort);
      Log.v("Filter", localStringBuilder.toString());
    }
    mInputPorts.put(paramString, localStreamPort);
    localStreamPort.setPortFormat(paramFrameFormat);
  }
  
  protected void addOutputBasedOnInput(String paramString1, String paramString2)
  {
    OutputPort localOutputPort = new OutputPort(this, paramString1);
    if (mLogVerbose)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Filter ");
      localStringBuilder.append(this);
      localStringBuilder.append(" adding ");
      localStringBuilder.append(localOutputPort);
      Log.v("Filter", localStringBuilder.toString());
    }
    localOutputPort.setBasePort(getInputPort(paramString2));
    mOutputPorts.put(paramString1, localOutputPort);
  }
  
  protected void addOutputPort(String paramString, FrameFormat paramFrameFormat)
  {
    OutputPort localOutputPort = new OutputPort(this, paramString);
    if (mLogVerbose)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Filter ");
      localStringBuilder.append(this);
      localStringBuilder.append(" adding ");
      localStringBuilder.append(localOutputPort);
      Log.v("Filter", localStringBuilder.toString());
    }
    localOutputPort.setPortFormat(paramFrameFormat);
    mOutputPorts.put(paramString, localOutputPort);
  }
  
  protected void addProgramPort(String paramString1, String paramString2, Field paramField, Class paramClass, boolean paramBoolean)
  {
    paramField.setAccessible(true);
    paramField = new ProgramPort(this, paramString1, paramString2, paramField, paramBoolean);
    if (mLogVerbose)
    {
      paramString2 = new StringBuilder();
      paramString2.append("Filter ");
      paramString2.append(this);
      paramString2.append(" adding ");
      paramString2.append(paramField);
      Log.v("Filter", paramString2.toString());
    }
    paramField.setPortFormat(ObjectFormat.fromClass(paramClass, 1));
    mInputPorts.put(paramString1, paramField);
  }
  
  final boolean canProcess()
  {
    try
    {
      if (mLogVerbose)
      {
        StringBuilder localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("Checking if can process: ");
        localStringBuilder.append(this);
        localStringBuilder.append(" (");
        localStringBuilder.append(mStatus);
        localStringBuilder.append(").");
        Log.v("Filter", localStringBuilder.toString());
      }
      int i = mStatus;
      boolean bool1 = false;
      if (i <= 3)
      {
        boolean bool2 = bool1;
        if (inputConditionsMet())
        {
          boolean bool3 = outputConditionsMet();
          bool2 = bool1;
          if (bool3) {
            bool2 = true;
          }
        }
        return bool2;
      }
      return false;
    }
    finally {}
  }
  
  final void clearInputs()
  {
    Iterator localIterator = mInputPorts.values().iterator();
    while (localIterator.hasNext()) {
      ((InputPort)localIterator.next()).clear();
    }
  }
  
  final void clearOutputs()
  {
    Iterator localIterator = mOutputPorts.values().iterator();
    while (localIterator.hasNext()) {
      ((OutputPort)localIterator.next()).clear();
    }
  }
  
  public void close(FilterContext paramFilterContext) {}
  
  protected void closeOutputPort(String paramString)
  {
    getOutputPort(paramString).close();
  }
  
  protected void delayNextProcess(int paramInt)
  {
    mSleepDelay = paramInt;
    mStatus = 4;
  }
  
  public void fieldPortValueUpdated(String paramString, FilterContext paramFilterContext) {}
  
  public String getFilterClassName()
  {
    return getClass().getSimpleName();
  }
  
  public final FrameFormat getInputFormat(String paramString)
  {
    return getInputPort(paramString).getSourceFormat();
  }
  
  public final InputPort getInputPort(String paramString)
  {
    if (mInputPorts != null)
    {
      localObject = (InputPort)mInputPorts.get(paramString);
      if (localObject != null) {
        return localObject;
      }
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Unknown input port '");
      ((StringBuilder)localObject).append(paramString);
      ((StringBuilder)localObject).append("' on filter ");
      ((StringBuilder)localObject).append(this);
      ((StringBuilder)localObject).append("!");
      throw new IllegalArgumentException(((StringBuilder)localObject).toString());
    }
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Attempting to access input port '");
    ((StringBuilder)localObject).append(paramString);
    ((StringBuilder)localObject).append("' of ");
    ((StringBuilder)localObject).append(this);
    ((StringBuilder)localObject).append(" before Filter has been initialized!");
    throw new NullPointerException(((StringBuilder)localObject).toString());
  }
  
  final Collection<InputPort> getInputPorts()
  {
    return mInputPorts.values();
  }
  
  public final String getName()
  {
    return mName;
  }
  
  public final int getNumberOfConnectedInputs()
  {
    int i = 0;
    Iterator localIterator = mInputPorts.values().iterator();
    while (localIterator.hasNext())
    {
      int j = i;
      if (((InputPort)localIterator.next()).isConnected()) {
        j = i + 1;
      }
      i = j;
    }
    return i;
  }
  
  public final int getNumberOfConnectedOutputs()
  {
    int i = 0;
    Iterator localIterator = mOutputPorts.values().iterator();
    while (localIterator.hasNext())
    {
      int j = i;
      if (((OutputPort)localIterator.next()).isConnected()) {
        j = i + 1;
      }
      i = j;
    }
    return i;
  }
  
  public final int getNumberOfInputs()
  {
    int i;
    if (mOutputPorts == null) {
      i = 0;
    } else {
      i = mInputPorts.size();
    }
    return i;
  }
  
  public final int getNumberOfOutputs()
  {
    int i;
    if (mInputPorts == null) {
      i = 0;
    } else {
      i = mOutputPorts.size();
    }
    return i;
  }
  
  public FrameFormat getOutputFormat(String paramString, FrameFormat paramFrameFormat)
  {
    return null;
  }
  
  public final OutputPort getOutputPort(String paramString)
  {
    if (mInputPorts != null)
    {
      localObject = (OutputPort)mOutputPorts.get(paramString);
      if (localObject != null) {
        return localObject;
      }
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Unknown output port '");
      ((StringBuilder)localObject).append(paramString);
      ((StringBuilder)localObject).append("' on filter ");
      ((StringBuilder)localObject).append(this);
      ((StringBuilder)localObject).append("!");
      throw new IllegalArgumentException(((StringBuilder)localObject).toString());
    }
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Attempting to access output port '");
    ((StringBuilder)localObject).append(paramString);
    ((StringBuilder)localObject).append("' of ");
    ((StringBuilder)localObject).append(this);
    ((StringBuilder)localObject).append(" before Filter has been initialized!");
    throw new NullPointerException(((StringBuilder)localObject).toString());
  }
  
  final Collection<OutputPort> getOutputPorts()
  {
    return mOutputPorts.values();
  }
  
  public final int getSleepDelay()
  {
    return 250;
  }
  
  final int getStatus()
  {
    try
    {
      int i = mStatus;
      return i;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public final void init()
    throws ProtocolException
  {
    initWithValueMap(new KeyValueMap());
  }
  
  protected void initProgramInputs(Program paramProgram, FilterContext paramFilterContext)
  {
    if (paramProgram != null)
    {
      Iterator localIterator = mInputPorts.values().iterator();
      while (localIterator.hasNext())
      {
        InputPort localInputPort = (InputPort)localIterator.next();
        if (localInputPort.getTarget() == paramProgram) {
          localInputPort.transfer(paramFilterContext);
        }
      }
    }
  }
  
  public final void initWithAssignmentList(Object... paramVarArgs)
  {
    KeyValueMap localKeyValueMap = new KeyValueMap();
    localKeyValueMap.setKeyValues(paramVarArgs);
    initWithValueMap(localKeyValueMap);
  }
  
  public final void initWithAssignmentString(String paramString)
  {
    try
    {
      TextGraphReader localTextGraphReader = new android/filterfw/io/TextGraphReader;
      localTextGraphReader.<init>();
      initWithValueMap(localTextGraphReader.readKeyValueAssignments(paramString));
      return;
    }
    catch (GraphIOException paramString)
    {
      throw new IllegalArgumentException(paramString.getMessage());
    }
  }
  
  public final void initWithValueMap(KeyValueMap paramKeyValueMap)
  {
    initFinalPorts(paramKeyValueMap);
    initRemainingPorts(paramKeyValueMap);
    mStatus = 1;
  }
  
  public boolean isOpen()
  {
    return mIsOpen;
  }
  
  final void notifyFieldPortValueUpdated(String paramString, FilterContext paramFilterContext)
  {
    if ((mStatus == 3) || (mStatus == 2)) {
      fieldPortValueUpdated(paramString, paramFilterContext);
    }
  }
  
  public void open(FilterContext paramFilterContext) {}
  
  final void openOutputs()
  {
    if (mLogVerbose)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Opening all output ports on ");
      ((StringBuilder)localObject).append(this);
      ((StringBuilder)localObject).append("!");
      Log.v("Filter", ((StringBuilder)localObject).toString());
    }
    Object localObject = mOutputPorts.values().iterator();
    while (((Iterator)localObject).hasNext())
    {
      OutputPort localOutputPort = (OutputPort)((Iterator)localObject).next();
      if (!localOutputPort.isOpen()) {
        localOutputPort.open();
      }
    }
  }
  
  protected void parametersUpdated(Set<String> paramSet) {}
  
  final void performClose(FilterContext paramFilterContext)
  {
    try
    {
      if (mIsOpen)
      {
        if (mLogVerbose)
        {
          StringBuilder localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("Closing ");
          localStringBuilder.append(this);
          Log.v("Filter", localStringBuilder.toString());
        }
        mIsOpen = false;
        mStatus = 2;
        close(paramFilterContext);
        closePorts();
      }
      return;
    }
    finally {}
  }
  
  final void performOpen(FilterContext paramFilterContext)
  {
    try
    {
      if (!mIsOpen)
      {
        StringBuilder localStringBuilder;
        if (mStatus == 1)
        {
          if (mLogVerbose)
          {
            localStringBuilder = new java/lang/StringBuilder;
            localStringBuilder.<init>();
            localStringBuilder.append("Preparing ");
            localStringBuilder.append(this);
            Log.v("Filter", localStringBuilder.toString());
          }
          prepare(paramFilterContext);
          mStatus = 2;
        }
        if (mStatus == 2)
        {
          if (mLogVerbose)
          {
            localStringBuilder = new java/lang/StringBuilder;
            localStringBuilder.<init>();
            localStringBuilder.append("Opening ");
            localStringBuilder.append(this);
            Log.v("Filter", localStringBuilder.toString());
          }
          open(paramFilterContext);
          mStatus = 3;
        }
        if (mStatus == 3)
        {
          mIsOpen = true;
        }
        else
        {
          paramFilterContext = new java/lang/RuntimeException;
          localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("Filter ");
          localStringBuilder.append(this);
          localStringBuilder.append(" was brought into invalid state during opening (state: ");
          localStringBuilder.append(mStatus);
          localStringBuilder.append(")!");
          paramFilterContext.<init>(localStringBuilder.toString());
          throw paramFilterContext;
        }
      }
      return;
    }
    finally {}
  }
  
  final void performProcess(FilterContext paramFilterContext)
  {
    try
    {
      if (mStatus != 7)
      {
        transferInputFrames(paramFilterContext);
        if (mStatus < 3) {
          performOpen(paramFilterContext);
        }
        if (mLogVerbose)
        {
          localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("Processing ");
          localStringBuilder.append(this);
          Log.v("Filter", localStringBuilder.toString());
        }
        mCurrentTimestamp = -1L;
        process(paramFilterContext);
        releasePulledFrames(paramFilterContext);
        if (filterMustClose()) {
          performClose(paramFilterContext);
        }
        return;
      }
      paramFilterContext = new java/lang/RuntimeException;
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("Filter ");
      localStringBuilder.append(this);
      localStringBuilder.append(" is already torn down!");
      paramFilterContext.<init>(localStringBuilder.toString());
      throw paramFilterContext;
    }
    finally {}
  }
  
  final void performTearDown(FilterContext paramFilterContext)
  {
    try
    {
      performClose(paramFilterContext);
      if (mStatus != 7)
      {
        tearDown(paramFilterContext);
        mStatus = 7;
      }
      return;
    }
    finally
    {
      paramFilterContext = finally;
      throw paramFilterContext;
    }
  }
  
  protected void prepare(FilterContext paramFilterContext) {}
  
  public abstract void process(FilterContext paramFilterContext);
  
  protected final Frame pullInput(String paramString)
  {
    Frame localFrame = getInputPort(paramString).pullFrame();
    if (mCurrentTimestamp == -1L)
    {
      mCurrentTimestamp = localFrame.getTimestamp();
      if (mLogVerbose)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Default-setting current timestamp from input port ");
        localStringBuilder.append(paramString);
        localStringBuilder.append(" to ");
        localStringBuilder.append(mCurrentTimestamp);
        Log.v("Filter", localStringBuilder.toString());
      }
    }
    mFramesToRelease.add(localFrame);
    return localFrame;
  }
  
  final void pushInputFrame(String paramString, Frame paramFrame)
  {
    try
    {
      paramString = getInputPort(paramString);
      if (!paramString.isOpen()) {
        paramString.open();
      }
      paramString.pushFrame(paramFrame);
      return;
    }
    finally {}
  }
  
  final void pushInputValue(String paramString, Object paramObject)
  {
    try
    {
      pushInputFrame(paramString, wrapInputValue(paramString, paramObject));
      return;
    }
    finally
    {
      paramString = finally;
      throw paramString;
    }
  }
  
  protected final void pushOutput(String paramString, Frame paramFrame)
  {
    if (paramFrame.getTimestamp() == -2L)
    {
      if (mLogVerbose)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Default-setting output Frame timestamp on port ");
        localStringBuilder.append(paramString);
        localStringBuilder.append(" to ");
        localStringBuilder.append(mCurrentTimestamp);
        Log.v("Filter", localStringBuilder.toString());
      }
      paramFrame.setTimestamp(mCurrentTimestamp);
    }
    getOutputPort(paramString).pushFrame(paramFrame);
  }
  
  public void setInputFrame(String paramString, Frame paramFrame)
  {
    paramString = getInputPort(paramString);
    if (!paramString.isOpen()) {
      paramString.open();
    }
    paramString.setFrame(paramFrame);
  }
  
  public final void setInputValue(String paramString, Object paramObject)
  {
    setInputFrame(paramString, wrapInputValue(paramString, paramObject));
  }
  
  protected void setWaitsOnInputPort(String paramString, boolean paramBoolean)
  {
    getInputPort(paramString).setBlocking(paramBoolean);
  }
  
  protected void setWaitsOnOutputPort(String paramString, boolean paramBoolean)
  {
    getOutputPort(paramString).setBlocking(paramBoolean);
  }
  
  public abstract void setupPorts();
  
  public void tearDown(FilterContext paramFilterContext) {}
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("'");
    localStringBuilder.append(getName());
    localStringBuilder.append("' (");
    localStringBuilder.append(getFilterClassName());
    localStringBuilder.append(")");
    return localStringBuilder.toString();
  }
  
  protected void transferInputPortFrame(String paramString, FilterContext paramFilterContext)
  {
    getInputPort(paramString).transfer(paramFilterContext);
  }
  
  final void unsetStatus(int paramInt)
  {
    try
    {
      mStatus &= paramInt;
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
}
