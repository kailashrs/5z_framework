package android.filterfw.io;

import android.filterfw.core.Filter;
import android.filterfw.core.FilterFactory;
import android.filterfw.core.FilterGraph;
import android.filterfw.core.KeyValueMap;
import android.filterfw.core.ProtocolException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class TextGraphReader
  extends GraphReader
{
  private KeyValueMap mBoundReferences;
  private ArrayList<Command> mCommands = new ArrayList();
  private Filter mCurrentFilter;
  private FilterGraph mCurrentGraph;
  private FilterFactory mFactory;
  private KeyValueMap mSettings;
  
  public TextGraphReader() {}
  
  private void applySettings()
    throws GraphIOException
  {
    Iterator localIterator = mSettings.keySet().iterator();
    while (localIterator.hasNext())
    {
      Object localObject1 = (String)localIterator.next();
      Object localObject2 = mSettings.get(localObject1);
      if (((String)localObject1).equals("autoBranch"))
      {
        expectSettingClass((String)localObject1, localObject2, String.class);
        if (localObject2.equals("synced"))
        {
          mCurrentGraph.setAutoBranchMode(1);
        }
        else if (localObject2.equals("unsynced"))
        {
          mCurrentGraph.setAutoBranchMode(2);
        }
        else if (localObject2.equals("off"))
        {
          mCurrentGraph.setAutoBranchMode(0);
        }
        else
        {
          localObject1 = new StringBuilder();
          ((StringBuilder)localObject1).append("Unknown autobranch setting: ");
          ((StringBuilder)localObject1).append(localObject2);
          ((StringBuilder)localObject1).append("!");
          throw new GraphIOException(((StringBuilder)localObject1).toString());
        }
      }
      else
      {
        if (!((String)localObject1).equals("discardUnconnectedOutputs")) {
          break label192;
        }
        expectSettingClass((String)localObject1, localObject2, Boolean.class);
        mCurrentGraph.setDiscardUnconnectedOutputs(((Boolean)localObject2).booleanValue());
      }
      continue;
      label192:
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("Unknown @setting '");
      ((StringBuilder)localObject2).append((String)localObject1);
      ((StringBuilder)localObject2).append("'!");
      throw new GraphIOException(((StringBuilder)localObject2).toString());
    }
  }
  
  private void bindExternal(String paramString)
    throws GraphIOException
  {
    if (mReferences.containsKey(paramString))
    {
      localObject = mReferences.get(paramString);
      mBoundReferences.put(paramString, localObject);
      return;
    }
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Unknown external variable '");
    ((StringBuilder)localObject).append(paramString);
    ((StringBuilder)localObject).append("'! You must add a reference to this external in the host program using addReference(...)!");
    throw new GraphIOException(((StringBuilder)localObject).toString());
  }
  
  private void checkReferences()
    throws GraphIOException
  {
    Object localObject = mReferences.keySet().iterator();
    while (((Iterator)localObject).hasNext())
    {
      String str = (String)((Iterator)localObject).next();
      if (!mBoundReferences.containsKey(str))
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Host program specifies reference to '");
        ((StringBuilder)localObject).append(str);
        ((StringBuilder)localObject).append("', which is not declared @external in graph file!");
        throw new GraphIOException(((StringBuilder)localObject).toString());
      }
    }
  }
  
  private void executeCommands()
    throws GraphIOException
  {
    Iterator localIterator = mCommands.iterator();
    while (localIterator.hasNext()) {
      ((Command)localIterator.next()).execute(this);
    }
  }
  
  private void expectSettingClass(String paramString, Object paramObject, Class paramClass)
    throws GraphIOException
  {
    if (paramObject.getClass() == paramClass) {
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Setting '");
    localStringBuilder.append(paramString);
    localStringBuilder.append("' must have a value of type ");
    localStringBuilder.append(paramClass.getSimpleName());
    localStringBuilder.append(", but found a value of type ");
    localStringBuilder.append(paramObject.getClass().getSimpleName());
    localStringBuilder.append("!");
    throw new GraphIOException(localStringBuilder.toString());
  }
  
  private void parseString(String paramString)
    throws GraphIOException
  {
    Object localObject1 = Pattern.compile("@[a-zA-Z]+");
    Pattern localPattern1 = Pattern.compile("\\}");
    Pattern localPattern2 = Pattern.compile("\\{");
    Pattern localPattern3 = Pattern.compile("(\\s+|//[^\\n]*\\n)+");
    Object localObject2 = Pattern.compile("[a-zA-Z\\.]+");
    Pattern localPattern4 = Pattern.compile("[a-zA-Z\\./:]+");
    Pattern localPattern5 = Pattern.compile("\\[[a-zA-Z0-9\\-_]+\\]");
    Pattern localPattern6 = Pattern.compile("=>");
    Pattern localPattern7 = Pattern.compile(";");
    Pattern localPattern8 = Pattern.compile("[a-zA-Z0-9\\-_]+");
    int i = 0;
    PatternScanner localPatternScanner = new PatternScanner(paramString, localPattern3);
    paramString = null;
    String str1 = null;
    String str2 = null;
    String str3 = null;
    String str4 = null;
    while (!localPatternScanner.atEnd())
    {
      switch (i)
      {
      default: 
        localObject3 = localObject1;
        localObject1 = paramString;
        paramString = (String)localObject2;
        localObject2 = localObject3;
        break;
      case 16: 
        localPatternScanner.eat(localPattern7, ";");
        i = 0;
        localObject3 = paramString;
        break;
      case 15: 
        localObject3 = readKeyValueAssignments(localPatternScanner, localPattern7);
        mSettings.putAll((Map)localObject3);
        break;
      case 14: 
        bindExternal(localPatternScanner.eat(localPattern8, "<external-identifier>"));
        break;
      case 13: 
        localObject3 = readKeyValueAssignments(localPatternScanner, localPattern7);
        mBoundReferences.putAll((Map)localObject3);
        i = 16;
        localObject3 = paramString;
        paramString = (String)localObject2;
        localObject2 = localObject1;
        localObject1 = localObject3;
        break;
      case 12: 
        str1 = localPatternScanner.eat(localPattern5, "[<target-port-name>]");
        str1 = str1.substring(1, str1.length() - 1);
        localObject3 = mCommands;
        ((ArrayList)localObject3).add(new ConnectCommand(str4, str3, str2, str1));
        i = 16;
        localObject3 = paramString;
        break;
      case 11: 
        str2 = localPatternScanner.eat(localPattern8, "<target-filter-name>");
        i = 12;
        break;
      case 10: 
        localPatternScanner.eat(localPattern6, "=>");
        i = 11;
        break;
      case 9: 
        str3 = localPatternScanner.eat(localPattern5, "[<source-port-name>]");
        str3 = str3.substring(1, str3.length() - 1);
        i = 10;
        break;
      case 8: 
        str4 = localPatternScanner.eat(localPattern8, "<source-filter-name>");
        i = 9;
        break;
      case 7: 
        localPatternScanner.eat(localPattern1, "}");
        i = 0;
        break;
      case 6: 
        localObject3 = readKeyValueAssignments(localPatternScanner, localPattern1);
        mCommands.add(new InitFilterCommand((KeyValueMap)localObject3));
        i = 7;
        break;
      case 5: 
        localPatternScanner.eat(localPattern2, "{");
        i = 6;
        break;
      case 4: 
        localObject3 = localPatternScanner.eat(localPattern8, "<filter-name>");
        mCommands.add(new AllocateFilterCommand(paramString, (String)localObject3));
        i = 5;
        break;
      case 3: 
        paramString = localPatternScanner.eat(localPattern8, "<class-name>");
        i = 4;
        break;
      case 2: 
        localObject3 = localPatternScanner.eat(localPattern4, "<library-name>");
        mCommands.add(new AddLibraryCommand((String)localObject3));
        i = 16;
        localObject3 = paramString;
        paramString = (String)localObject2;
        localObject2 = localObject1;
        localObject1 = localObject3;
        break;
      case 1: 
        localObject3 = paramString;
        paramString = (String)localObject2;
        localObject2 = localPatternScanner.eat(paramString, "<package-name>");
        mCommands.add(new ImportPackageCommand((String)localObject2));
        i = 16;
        localObject2 = localObject1;
        localObject1 = localObject3;
        break;
      }
      Object localObject3 = paramString;
      paramString = (String)localObject2;
      localObject2 = localObject1;
      localObject1 = localPatternScanner.eat((Pattern)localObject2, "<command>");
      if (((String)localObject1).equals("@import")) {
        i = 1;
      }
      for (;;)
      {
        localObject1 = localObject3;
        break label850;
        if (((String)localObject1).equals("@library"))
        {
          i = 2;
        }
        else if (((String)localObject1).equals("@filter"))
        {
          i = 3;
        }
        else if (((String)localObject1).equals("@connect"))
        {
          i = 8;
        }
        else if (((String)localObject1).equals("@set"))
        {
          i = 13;
        }
        else if (((String)localObject1).equals("@external"))
        {
          i = 14;
        }
        else
        {
          if (!((String)localObject1).equals("@setting")) {
            break;
          }
          i = 15;
        }
      }
      paramString = new StringBuilder();
      paramString.append("Unknown command '");
      paramString.append((String)localObject1);
      paramString.append("'!");
      throw new GraphIOException(paramString.toString());
      label850:
      localObject3 = paramString;
      paramString = (String)localObject1;
      localObject1 = localObject2;
      localObject2 = localObject3;
    }
    if ((i != 16) && (i != 0)) {
      throw new GraphIOException("Unexpected end of input!");
    }
  }
  
  private KeyValueMap readKeyValueAssignments(PatternScanner paramPatternScanner, Pattern paramPattern)
    throws GraphIOException
  {
    int i = 2;
    int j = 3;
    Pattern localPattern1 = Pattern.compile("=");
    Pattern localPattern2 = Pattern.compile(";");
    Pattern localPattern3 = Pattern.compile("[a-zA-Z]+[a-zA-Z0-9]*");
    Pattern localPattern4 = Pattern.compile("'[^']*'|\\\"[^\\\"]*\\\"");
    Pattern localPattern5 = Pattern.compile("[0-9]+");
    Pattern localPattern6 = Pattern.compile("[0-9]*\\.[0-9]+f?");
    Pattern localPattern7 = Pattern.compile("\\$[a-zA-Z]+[a-zA-Z0-9]");
    Pattern localPattern8 = Pattern.compile("true|false");
    KeyValueMap localKeyValueMap = new KeyValueMap();
    int k = 0;
    Object localObject1 = null;
    for (;;)
    {
      Object localObject2 = this;
      if (paramPatternScanner.atEnd()) {
        break;
      }
      if ((paramPattern != null) && (paramPatternScanner.peek(paramPattern))) {
        break;
      }
      switch (k)
      {
      default: 
        break;
      case 3: 
        paramPatternScanner.eat(localPattern2, ";");
        k = 0;
        break;
      case 2: 
        String str = paramPatternScanner.tryEat(localPattern4);
        if (str != null) {
          localKeyValueMap.put(localObject1, str.substring(1, str.length() - 1));
        }
        for (;;)
        {
          break;
          str = paramPatternScanner.tryEat(localPattern7);
          if (str != null)
          {
            str = str.substring(1, str.length());
            if (mBoundReferences != null) {
              localObject2 = mBoundReferences.get(str);
            } else {
              localObject2 = null;
            }
            if (localObject2 != null)
            {
              localKeyValueMap.put(localObject1, localObject2);
            }
            else
            {
              paramPatternScanner = new StringBuilder();
              paramPatternScanner.append("Unknown object reference to '");
              paramPatternScanner.append(str);
              paramPatternScanner.append("'!");
              throw new GraphIOException(paramPatternScanner.toString());
            }
          }
          else
          {
            localObject2 = paramPatternScanner.tryEat(localPattern8);
            if (localObject2 != null)
            {
              localKeyValueMap.put(localObject1, Boolean.valueOf(Boolean.parseBoolean((String)localObject2)));
            }
            else
            {
              localObject2 = paramPatternScanner.tryEat(localPattern6);
              if (localObject2 != null)
              {
                localKeyValueMap.put(localObject1, Float.valueOf(Float.parseFloat((String)localObject2)));
              }
              else
              {
                localObject2 = paramPatternScanner.tryEat(localPattern5);
                if (localObject2 == null) {
                  break label410;
                }
                localKeyValueMap.put(localObject1, Integer.valueOf(Integer.parseInt((String)localObject2)));
              }
            }
          }
        }
        k = 3;
        break;
        throw new GraphIOException(paramPatternScanner.unexpectedTokenMessage("<value>"));
      case 1: 
        label410:
        paramPatternScanner.eat(localPattern1, "=");
        k = 2;
        break;
      }
      localObject1 = paramPatternScanner.eat(localPattern3, "<identifier>");
      k = 1;
    }
    if ((k != 0) && (k != 3))
    {
      paramPattern = new StringBuilder();
      paramPattern.append("Unexpected end of assignments on line ");
      paramPattern.append(paramPatternScanner.lineNo());
      paramPattern.append("!");
      throw new GraphIOException(paramPattern.toString());
    }
    return localKeyValueMap;
  }
  
  private void reset()
  {
    mCurrentGraph = null;
    mCurrentFilter = null;
    mCommands.clear();
    mBoundReferences = new KeyValueMap();
    mSettings = new KeyValueMap();
    mFactory = new FilterFactory();
  }
  
  public FilterGraph readGraphString(String paramString)
    throws GraphIOException
  {
    FilterGraph localFilterGraph = new FilterGraph();
    reset();
    mCurrentGraph = localFilterGraph;
    parseString(paramString);
    applySettings();
    executeCommands();
    reset();
    return localFilterGraph;
  }
  
  public KeyValueMap readKeyValueAssignments(String paramString)
    throws GraphIOException
  {
    return readKeyValueAssignments(new PatternScanner(paramString, Pattern.compile("\\s+")), null);
  }
  
  private class AddLibraryCommand
    implements TextGraphReader.Command
  {
    private String mLibraryName;
    
    public AddLibraryCommand(String paramString)
    {
      mLibraryName = paramString;
    }
    
    public void execute(TextGraphReader paramTextGraphReader)
    {
      FilterFactory.addFilterLibrary(mLibraryName);
    }
  }
  
  private class AllocateFilterCommand
    implements TextGraphReader.Command
  {
    private String mClassName;
    private String mFilterName;
    
    public AllocateFilterCommand(String paramString1, String paramString2)
    {
      mClassName = paramString1;
      mFilterName = paramString2;
    }
    
    public void execute(TextGraphReader paramTextGraphReader)
      throws GraphIOException
    {
      try
      {
        Filter localFilter = mFactory.createFilterByClassName(mClassName, mFilterName);
        TextGraphReader.access$102(paramTextGraphReader, localFilter);
        return;
      }
      catch (IllegalArgumentException paramTextGraphReader)
      {
        throw new GraphIOException(paramTextGraphReader.getMessage());
      }
    }
  }
  
  private static abstract interface Command
  {
    public abstract void execute(TextGraphReader paramTextGraphReader)
      throws GraphIOException;
  }
  
  private class ConnectCommand
    implements TextGraphReader.Command
  {
    private String mSourceFilter;
    private String mSourcePort;
    private String mTargetFilter;
    private String mTargetName;
    
    public ConnectCommand(String paramString1, String paramString2, String paramString3, String paramString4)
    {
      mSourceFilter = paramString1;
      mSourcePort = paramString2;
      mTargetFilter = paramString3;
      mTargetName = paramString4;
    }
    
    public void execute(TextGraphReader paramTextGraphReader)
    {
      mCurrentGraph.connect(mSourceFilter, mSourcePort, mTargetFilter, mTargetName);
    }
  }
  
  private class ImportPackageCommand
    implements TextGraphReader.Command
  {
    private String mPackageName;
    
    public ImportPackageCommand(String paramString)
    {
      mPackageName = paramString;
    }
    
    public void execute(TextGraphReader paramTextGraphReader)
      throws GraphIOException
    {
      try
      {
        mFactory.addPackage(mPackageName);
        return;
      }
      catch (IllegalArgumentException paramTextGraphReader)
      {
        throw new GraphIOException(paramTextGraphReader.getMessage());
      }
    }
  }
  
  private class InitFilterCommand
    implements TextGraphReader.Command
  {
    private KeyValueMap mParams;
    
    public InitFilterCommand(KeyValueMap paramKeyValueMap)
    {
      mParams = paramKeyValueMap;
    }
    
    public void execute(TextGraphReader paramTextGraphReader)
      throws GraphIOException
    {
      Filter localFilter = mCurrentFilter;
      try
      {
        localFilter.initWithValueMap(mParams);
        mCurrentGraph.addFilter(mCurrentFilter);
        return;
      }
      catch (ProtocolException paramTextGraphReader)
      {
        throw new GraphIOException(paramTextGraphReader.getMessage());
      }
    }
  }
}
