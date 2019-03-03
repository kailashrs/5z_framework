package android.filterfw.core;

import android.filterpacks.base.FrameBranch;
import android.filterpacks.base.NullFilter;
import android.util.Log;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;

public class FilterGraph
{
  public static final int AUTOBRANCH_OFF = 0;
  public static final int AUTOBRANCH_SYNCED = 1;
  public static final int AUTOBRANCH_UNSYNCED = 2;
  public static final int TYPECHECK_DYNAMIC = 1;
  public static final int TYPECHECK_OFF = 0;
  public static final int TYPECHECK_STRICT = 2;
  private String TAG = "FilterGraph";
  private int mAutoBranchMode = 0;
  private boolean mDiscardUnconnectedOutputs = false;
  private HashSet<Filter> mFilters = new HashSet();
  private boolean mIsReady = false;
  private boolean mLogVerbose = Log.isLoggable(TAG, 2);
  private HashMap<String, Filter> mNameMap = new HashMap();
  private HashMap<OutputPort, LinkedList<InputPort>> mPreconnections = new HashMap();
  private int mTypeCheckMode = 2;
  
  public FilterGraph() {}
  
  private void checkConnections() {}
  
  private void connectPorts()
  {
    int i = 1;
    Object localObject1 = mPreconnections.entrySet().iterator();
    while (((Iterator)localObject1).hasNext())
    {
      Object localObject2 = (Map.Entry)((Iterator)localObject1).next();
      Object localObject3 = (OutputPort)((Map.Entry)localObject2).getKey();
      localObject2 = (LinkedList)((Map.Entry)localObject2).getValue();
      if (((LinkedList)localObject2).size() == 1)
      {
        ((OutputPort)localObject3).connectTo((InputPort)((LinkedList)localObject2).get(0));
      }
      else
      {
        if (mAutoBranchMode == 0) {
          break label307;
        }
        if (mLogVerbose)
        {
          String str = TAG;
          localObject4 = new StringBuilder();
          ((StringBuilder)localObject4).append("Creating branch for ");
          ((StringBuilder)localObject4).append(localObject3);
          ((StringBuilder)localObject4).append("!");
          Log.v(str, ((StringBuilder)localObject4).toString());
        }
        if (mAutoBranchMode != 1) {
          break label297;
        }
        Object localObject4 = new StringBuilder();
        ((StringBuilder)localObject4).append("branch");
        ((StringBuilder)localObject4).append(i);
        localObject4 = new FrameBranch(((StringBuilder)localObject4).toString());
        new KeyValueMap();
        ((FrameBranch)localObject4).initWithAssignmentList(new Object[] { "outputs", Integer.valueOf(((LinkedList)localObject2).size()) });
        addFilter((Filter)localObject4);
        ((OutputPort)localObject3).connectTo(((FrameBranch)localObject4).getInputPort("in"));
        localObject3 = ((LinkedList)localObject2).iterator();
        localObject2 = ((Filter)localObject4).getOutputPorts().iterator();
        while (((Iterator)localObject2).hasNext()) {
          ((OutputPort)((Iterator)localObject2).next()).connectTo((InputPort)((Iterator)localObject3).next());
        }
        i++;
      }
      continue;
      label297:
      throw new RuntimeException("TODO: Unsynced branches not implemented yet!");
      label307:
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("Attempting to connect ");
      ((StringBuilder)localObject1).append(localObject3);
      ((StringBuilder)localObject1).append(" to multiple filter ports! Enable auto-branching to allow this.");
      throw new RuntimeException(((StringBuilder)localObject1).toString());
    }
    mPreconnections.clear();
  }
  
  private void discardUnconnectedOutputs()
  {
    LinkedList localLinkedList = new LinkedList();
    Iterator localIterator1 = mFilters.iterator();
    while (localIterator1.hasNext())
    {
      Filter localFilter = (Filter)localIterator1.next();
      int i = 0;
      Iterator localIterator2 = localFilter.getOutputPorts().iterator();
      while (localIterator2.hasNext())
      {
        localObject1 = (OutputPort)localIterator2.next();
        int j = i;
        if (!((OutputPort)localObject1).isConnected())
        {
          if (mLogVerbose)
          {
            String str = TAG;
            localObject2 = new StringBuilder();
            ((StringBuilder)localObject2).append("Autoconnecting unconnected ");
            ((StringBuilder)localObject2).append(localObject1);
            ((StringBuilder)localObject2).append(" to Null filter.");
            Log.v(str, ((StringBuilder)localObject2).toString());
          }
          Object localObject2 = new StringBuilder();
          ((StringBuilder)localObject2).append(localFilter.getName());
          ((StringBuilder)localObject2).append("ToNull");
          ((StringBuilder)localObject2).append(i);
          localObject2 = new NullFilter(((StringBuilder)localObject2).toString());
          ((NullFilter)localObject2).init();
          localLinkedList.add(localObject2);
          ((OutputPort)localObject1).connectTo(((NullFilter)localObject2).getInputPort("frame"));
          j = i + 1;
        }
        i = j;
      }
    }
    Object localObject1 = localLinkedList.iterator();
    while (((Iterator)localObject1).hasNext()) {
      addFilter((Filter)((Iterator)localObject1).next());
    }
  }
  
  private HashSet<Filter> getSourceFilters()
  {
    HashSet localHashSet = new HashSet();
    Iterator localIterator = getFilters().iterator();
    while (localIterator.hasNext())
    {
      Filter localFilter = (Filter)localIterator.next();
      if (localFilter.getNumberOfConnectedInputs() == 0)
      {
        if (mLogVerbose)
        {
          String str = TAG;
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("Found source filter: ");
          localStringBuilder.append(localFilter);
          Log.v(str, localStringBuilder.toString());
        }
        localHashSet.add(localFilter);
      }
    }
    return localHashSet;
  }
  
  private void preconnect(OutputPort paramOutputPort, InputPort paramInputPort)
  {
    LinkedList localLinkedList1 = (LinkedList)mPreconnections.get(paramOutputPort);
    LinkedList localLinkedList2 = localLinkedList1;
    if (localLinkedList1 == null)
    {
      localLinkedList2 = new LinkedList();
      mPreconnections.put(paramOutputPort, localLinkedList2);
    }
    localLinkedList2.add(paramInputPort);
  }
  
  private boolean readyForProcessing(Filter paramFilter, Set<Filter> paramSet)
  {
    if (paramSet.contains(paramFilter)) {
      return false;
    }
    paramFilter = paramFilter.getInputPorts().iterator();
    while (paramFilter.hasNext())
    {
      Filter localFilter = ((InputPort)paramFilter.next()).getSourceFilter();
      if ((localFilter != null) && (!paramSet.contains(localFilter))) {
        return false;
      }
    }
    return true;
  }
  
  private void removeFilter(Filter paramFilter)
  {
    mFilters.remove(paramFilter);
    mNameMap.remove(paramFilter.getName());
  }
  
  private void runTypeCheck()
  {
    Stack localStack = new Stack();
    HashSet localHashSet = new HashSet();
    localStack.addAll(getSourceFilters());
    while (!localStack.empty())
    {
      Filter localFilter = (Filter)localStack.pop();
      localHashSet.add(localFilter);
      updateOutputs(localFilter);
      Object localObject1;
      if (mLogVerbose)
      {
        localObject1 = TAG;
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append("Running type check on ");
        ((StringBuilder)localObject2).append(localFilter);
        ((StringBuilder)localObject2).append("...");
        Log.v((String)localObject1, ((StringBuilder)localObject2).toString());
      }
      runTypeCheckOn(localFilter);
      Object localObject2 = localFilter.getOutputPorts().iterator();
      while (((Iterator)localObject2).hasNext())
      {
        localObject1 = ((OutputPort)((Iterator)localObject2).next()).getTargetFilter();
        if ((localObject1 != null) && (readyForProcessing((Filter)localObject1, localHashSet))) {
          localStack.push(localObject1);
        }
      }
    }
    if (localHashSet.size() == getFilters().size()) {
      return;
    }
    throw new RuntimeException("Could not schedule all filters! Is your graph malformed?");
  }
  
  private void runTypeCheckOn(Filter paramFilter)
  {
    Object localObject1 = paramFilter.getInputPorts().iterator();
    while (((Iterator)localObject1).hasNext())
    {
      InputPort localInputPort = (InputPort)((Iterator)localObject1).next();
      if (mLogVerbose)
      {
        localObject2 = TAG;
        localObject3 = new StringBuilder();
        ((StringBuilder)localObject3).append("Type checking port ");
        ((StringBuilder)localObject3).append(localInputPort);
        Log.v((String)localObject2, ((StringBuilder)localObject3).toString());
      }
      Object localObject2 = localInputPort.getSourceFormat();
      Object localObject3 = localInputPort.getPortFormat();
      if ((localObject2 != null) && (localObject3 != null))
      {
        if (mLogVerbose)
        {
          String str = TAG;
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("Checking ");
          localStringBuilder.append(localObject2);
          localStringBuilder.append(" against ");
          localStringBuilder.append(localObject3);
          localStringBuilder.append(".");
          Log.v(str, localStringBuilder.toString());
        }
        boolean bool = true;
        switch (mTypeCheckMode)
        {
        default: 
          break;
        case 2: 
          bool = ((FrameFormat)localObject2).isCompatibleWith((FrameFormat)localObject3);
          localInputPort.setChecksType(false);
          break;
        case 1: 
          bool = ((FrameFormat)localObject2).mayBeCompatibleWith((FrameFormat)localObject3);
          localInputPort.setChecksType(true);
          break;
        case 0: 
          localInputPort.setChecksType(false);
        }
        if (!bool)
        {
          localObject1 = new StringBuilder();
          ((StringBuilder)localObject1).append("Type mismatch: Filter ");
          ((StringBuilder)localObject1).append(paramFilter);
          ((StringBuilder)localObject1).append(" expects a format of type ");
          ((StringBuilder)localObject1).append(localObject3);
          ((StringBuilder)localObject1).append(" but got a format of type ");
          ((StringBuilder)localObject1).append(localObject2);
          ((StringBuilder)localObject1).append("!");
          throw new RuntimeException(((StringBuilder)localObject1).toString());
        }
      }
    }
  }
  
  private void updateOutputs(Filter paramFilter)
  {
    Iterator localIterator = paramFilter.getOutputPorts().iterator();
    while (localIterator.hasNext())
    {
      OutputPort localOutputPort = (OutputPort)localIterator.next();
      Object localObject = localOutputPort.getBasePort();
      if (localObject != null)
      {
        localObject = ((InputPort)localObject).getSourceFormat();
        localObject = paramFilter.getOutputFormat(localOutputPort.getName(), (FrameFormat)localObject);
        if (localObject != null)
        {
          localOutputPort.setPortFormat((FrameFormat)localObject);
        }
        else
        {
          paramFilter = new StringBuilder();
          paramFilter.append("Filter did not return an output format for ");
          paramFilter.append(localOutputPort);
          paramFilter.append("!");
          throw new RuntimeException(paramFilter.toString());
        }
      }
    }
  }
  
  public boolean addFilter(Filter paramFilter)
  {
    if (!containsFilter(paramFilter))
    {
      mFilters.add(paramFilter);
      mNameMap.put(paramFilter.getName(), paramFilter);
      return true;
    }
    return false;
  }
  
  public void beginProcessing()
  {
    if (mLogVerbose) {
      Log.v(TAG, "Opening all filter connections...");
    }
    Iterator localIterator = mFilters.iterator();
    while (localIterator.hasNext()) {
      ((Filter)localIterator.next()).openOutputs();
    }
    mIsReady = true;
  }
  
  public void closeFilters(FilterContext paramFilterContext)
  {
    if (mLogVerbose) {
      Log.v(TAG, "Closing all filters...");
    }
    Iterator localIterator = mFilters.iterator();
    while (localIterator.hasNext()) {
      ((Filter)localIterator.next()).performClose(paramFilterContext);
    }
    mIsReady = false;
  }
  
  public void connect(Filter paramFilter1, String paramString1, Filter paramFilter2, String paramString2)
  {
    if ((paramFilter1 != null) && (paramFilter2 != null))
    {
      if ((containsFilter(paramFilter1)) && (containsFilter(paramFilter2)))
      {
        OutputPort localOutputPort = paramFilter1.getOutputPort(paramString1);
        InputPort localInputPort = paramFilter2.getInputPort(paramString2);
        if (localOutputPort != null)
        {
          if (localInputPort != null)
          {
            preconnect(localOutputPort, localInputPort);
            return;
          }
          paramFilter1 = new StringBuilder();
          paramFilter1.append("Unknown input port '");
          paramFilter1.append(paramString2);
          paramFilter1.append("' on Filter ");
          paramFilter1.append(paramFilter2);
          paramFilter1.append("!");
          throw new RuntimeException(paramFilter1.toString());
        }
        paramFilter2 = new StringBuilder();
        paramFilter2.append("Unknown output port '");
        paramFilter2.append(paramString1);
        paramFilter2.append("' on Filter ");
        paramFilter2.append(paramFilter1);
        paramFilter2.append("!");
        throw new RuntimeException(paramFilter2.toString());
      }
      throw new RuntimeException("Attempting to connect filter not in graph!");
    }
    throw new IllegalArgumentException("Passing null Filter in connect()!");
  }
  
  public void connect(String paramString1, String paramString2, String paramString3, String paramString4)
  {
    Filter localFilter1 = getFilter(paramString1);
    Filter localFilter2 = getFilter(paramString3);
    if (localFilter1 != null)
    {
      if (localFilter2 != null)
      {
        connect(localFilter1, paramString2, localFilter2, paramString4);
        return;
      }
      paramString1 = new StringBuilder();
      paramString1.append("Attempting to connect unknown target filter '");
      paramString1.append(paramString3);
      paramString1.append("'!");
      throw new RuntimeException(paramString1.toString());
    }
    paramString2 = new StringBuilder();
    paramString2.append("Attempting to connect unknown source filter '");
    paramString2.append(paramString1);
    paramString2.append("'!");
    throw new RuntimeException(paramString2.toString());
  }
  
  public boolean containsFilter(Filter paramFilter)
  {
    return mFilters.contains(paramFilter);
  }
  
  public void flushFrames()
  {
    Iterator localIterator = mFilters.iterator();
    while (localIterator.hasNext()) {
      ((Filter)localIterator.next()).clearOutputs();
    }
  }
  
  public Filter getFilter(String paramString)
  {
    return (Filter)mNameMap.get(paramString);
  }
  
  public Set<Filter> getFilters()
  {
    return mFilters;
  }
  
  public boolean isReady()
  {
    return mIsReady;
  }
  
  public void setAutoBranchMode(int paramInt)
  {
    mAutoBranchMode = paramInt;
  }
  
  public void setDiscardUnconnectedOutputs(boolean paramBoolean)
  {
    mDiscardUnconnectedOutputs = paramBoolean;
  }
  
  public void setTypeCheckMode(int paramInt)
  {
    mTypeCheckMode = paramInt;
  }
  
  void setupFilters()
  {
    if (mDiscardUnconnectedOutputs) {
      discardUnconnectedOutputs();
    }
    connectPorts();
    checkConnections();
    runTypeCheck();
  }
  
  public void tearDown(FilterContext paramFilterContext)
  {
    if (!mFilters.isEmpty())
    {
      flushFrames();
      Iterator localIterator = mFilters.iterator();
      while (localIterator.hasNext()) {
        ((Filter)localIterator.next()).performTearDown(paramFilterContext);
      }
      mFilters.clear();
      mNameMap.clear();
      mIsReady = false;
    }
  }
}
