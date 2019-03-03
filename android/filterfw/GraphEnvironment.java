package android.filterfw;

import android.content.Context;
import android.filterfw.core.AsyncRunner;
import android.filterfw.core.FilterContext;
import android.filterfw.core.FilterGraph;
import android.filterfw.core.FrameManager;
import android.filterfw.core.GraphRunner;
import android.filterfw.core.RoundRobinScheduler;
import android.filterfw.core.SyncRunner;
import android.filterfw.io.GraphIOException;
import android.filterfw.io.GraphReader;
import android.filterfw.io.TextGraphReader;
import java.util.ArrayList;

public class GraphEnvironment
  extends MffEnvironment
{
  public static final int MODE_ASYNCHRONOUS = 1;
  public static final int MODE_SYNCHRONOUS = 2;
  private GraphReader mGraphReader;
  private ArrayList<GraphHandle> mGraphs = new ArrayList();
  
  public GraphEnvironment()
  {
    super(null);
  }
  
  public GraphEnvironment(FrameManager paramFrameManager, GraphReader paramGraphReader)
  {
    super(paramFrameManager);
    mGraphReader = paramGraphReader;
  }
  
  public int addGraph(FilterGraph paramFilterGraph)
  {
    paramFilterGraph = new GraphHandle(paramFilterGraph);
    mGraphs.add(paramFilterGraph);
    return mGraphs.size() - 1;
  }
  
  public void addReferences(Object... paramVarArgs)
  {
    getGraphReader().addReferencesByKeysAndValues(paramVarArgs);
  }
  
  public FilterGraph getGraph(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt < mGraphs.size())) {
      return ((GraphHandle)mGraphs.get(paramInt)).getGraph();
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Invalid graph ID ");
    localStringBuilder.append(paramInt);
    localStringBuilder.append(" specified in runGraph()!");
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public GraphReader getGraphReader()
  {
    if (mGraphReader == null) {
      mGraphReader = new TextGraphReader();
    }
    return mGraphReader;
  }
  
  public GraphRunner getRunner(int paramInt1, int paramInt2)
  {
    switch (paramInt2)
    {
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Invalid execution mode ");
      localStringBuilder.append(paramInt2);
      localStringBuilder.append(" specified in getRunner()!");
      throw new RuntimeException(localStringBuilder.toString());
    case 2: 
      return ((GraphHandle)mGraphs.get(paramInt1)).getSyncRunner(getContext());
    }
    return ((GraphHandle)mGraphs.get(paramInt1)).getAsyncRunner(getContext());
  }
  
  public int loadGraph(Context paramContext, int paramInt)
  {
    try
    {
      paramContext = getGraphReader().readGraphResource(paramContext, paramInt);
      return addGraph(paramContext);
    }
    catch (GraphIOException localGraphIOException)
    {
      paramContext = new StringBuilder();
      paramContext.append("Could not read graph: ");
      paramContext.append(localGraphIOException.getMessage());
      throw new RuntimeException(paramContext.toString());
    }
  }
  
  private class GraphHandle
  {
    private AsyncRunner mAsyncRunner;
    private FilterGraph mGraph;
    private SyncRunner mSyncRunner;
    
    public GraphHandle(FilterGraph paramFilterGraph)
    {
      mGraph = paramFilterGraph;
    }
    
    public AsyncRunner getAsyncRunner(FilterContext paramFilterContext)
    {
      if (mAsyncRunner == null)
      {
        mAsyncRunner = new AsyncRunner(paramFilterContext, RoundRobinScheduler.class);
        mAsyncRunner.setGraph(mGraph);
      }
      return mAsyncRunner;
    }
    
    public FilterGraph getGraph()
    {
      return mGraph;
    }
    
    public GraphRunner getSyncRunner(FilterContext paramFilterContext)
    {
      if (mSyncRunner == null) {
        mSyncRunner = new SyncRunner(paramFilterContext, mGraph, RoundRobinScheduler.class);
      }
      return mSyncRunner;
    }
  }
}
