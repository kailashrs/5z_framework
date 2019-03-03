package android.view.textclassifier;

import android.content.Context;
import android.metrics.LogMaker;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.util.Preconditions;
import java.text.BreakIterator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.StringJoiner;

public final class SelectionSessionLogger
{
  static final String CLASSIFIER_ID = "androidtc";
  private static final boolean DEBUG_LOG_ENABLED = false;
  private static final int ENTITY_TYPE = 1254;
  private static final int EVENT_END = 1251;
  private static final int EVENT_START = 1250;
  private static final int INDEX = 1120;
  private static final String LOG_TAG = "SelectionSessionLogger";
  private static final int MODEL_NAME = 1256;
  private static final int PREV_EVENT_DELTA = 1118;
  private static final int SESSION_ID = 1119;
  private static final int SMART_END = 1253;
  private static final int SMART_START = 1252;
  private static final int START_EVENT_DELTA = 1117;
  private static final String UNKNOWN = "unknown";
  private static final int WIDGET_TYPE = 1255;
  private static final int WIDGET_VERSION = 1262;
  private static final String ZERO = "0";
  private final MetricsLogger mMetricsLogger;
  
  public SelectionSessionLogger()
  {
    mMetricsLogger = new MetricsLogger();
  }
  
  @VisibleForTesting
  public SelectionSessionLogger(MetricsLogger paramMetricsLogger)
  {
    mMetricsLogger = ((MetricsLogger)Preconditions.checkNotNull(paramMetricsLogger));
  }
  
  public static String createId(String paramString, int paramInt1, int paramInt2, Context paramContext, int paramInt3, List<Locale> paramList)
  {
    Preconditions.checkNotNull(paramString);
    Preconditions.checkNotNull(paramContext);
    Preconditions.checkNotNull(paramList);
    StringJoiner localStringJoiner = new StringJoiner(",");
    paramList = paramList.iterator();
    while (paramList.hasNext()) {
      localStringJoiner.add(((Locale)paramList.next()).toLanguageTag());
    }
    return SignatureParser.createSignature("androidtc", String.format(Locale.US, "%s_v%d", new Object[] { localStringJoiner.toString(), Integer.valueOf(paramInt3) }), Objects.hash(new Object[] { paramString, Integer.valueOf(paramInt1), Integer.valueOf(paramInt2), paramContext.getPackageName() }));
  }
  
  private static void debugLog(LogMaker paramLogMaker) {}
  
  private static int getLogSubType(SelectionEvent paramSelectionEvent)
  {
    switch (paramSelectionEvent.getInvocationMethod())
    {
    default: 
      return 0;
    case 2: 
      return 2;
    }
    return 1;
  }
  
  private static String getLogSubTypeString(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return "unknown";
    case 2: 
      return "LINK";
    }
    return "MANUAL";
  }
  
  private static int getLogType(SelectionEvent paramSelectionEvent)
  {
    int i = paramSelectionEvent.getEventType();
    switch (i)
    {
    default: 
      switch (i)
      {
      default: 
        switch (i)
        {
        default: 
          return 0;
        case 201: 
          return 1104;
        }
        return 1103;
      case 108: 
        return 1116;
      case 107: 
        return 1115;
      case 106: 
        return 1114;
      case 105: 
        return 1113;
      case 104: 
        return 1112;
      case 103: 
        return 1111;
      case 102: 
        return 1110;
      case 101: 
        return 1109;
      }
      return 1108;
    case 5: 
      return 1107;
    case 4: 
      return 1106;
    case 3: 
      return 1105;
    case 2: 
      return 1102;
    }
    return 1101;
  }
  
  private static String getLogTypeString(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return "unknown";
    case 1116: 
      return "OTHER";
    case 1115: 
      return "ABANDON";
    case 1114: 
      return "DRAG";
    case 1113: 
      return "SMART_SHARE";
    case 1112: 
      return "SHARE";
    case 1111: 
      return "CUT";
    case 1110: 
      return "PASTE";
    case 1109: 
      return "COPY";
    case 1108: 
      return "OVERTYPE";
    case 1107: 
      return "AUTO_SELECTION";
    case 1106: 
      return "SMART_SELECTION_MULTI";
    case 1105: 
      return "SMART_SELECTION_SINGLE";
    case 1104: 
      return "RESET";
    case 1103: 
      return "SELECT_ALL";
    case 1102: 
      return "SELECTION_MODIFIED";
    }
    return "SELECTION_STARTED";
  }
  
  public static BreakIterator getTokenIterator(Locale paramLocale)
  {
    return BreakIterator.getWordInstance((Locale)Preconditions.checkNotNull(paramLocale));
  }
  
  public void writeEvent(SelectionEvent paramSelectionEvent)
  {
    Preconditions.checkNotNull(paramSelectionEvent);
    LogMaker localLogMaker = new LogMaker(1100).setType(getLogType(paramSelectionEvent)).setSubtype(getLogSubType(paramSelectionEvent)).setPackageName(paramSelectionEvent.getPackageName()).addTaggedData(1117, Long.valueOf(paramSelectionEvent.getDurationSinceSessionStart())).addTaggedData(1118, Long.valueOf(paramSelectionEvent.getDurationSincePreviousEvent())).addTaggedData(1120, Integer.valueOf(paramSelectionEvent.getEventIndex())).addTaggedData(1255, paramSelectionEvent.getWidgetType()).addTaggedData(1262, paramSelectionEvent.getWidgetVersion()).addTaggedData(1256, SignatureParser.getModelName(paramSelectionEvent.getResultId())).addTaggedData(1254, paramSelectionEvent.getEntityType()).addTaggedData(1252, Integer.valueOf(paramSelectionEvent.getSmartStart())).addTaggedData(1253, Integer.valueOf(paramSelectionEvent.getSmartEnd())).addTaggedData(1250, Integer.valueOf(paramSelectionEvent.getStart())).addTaggedData(1251, Integer.valueOf(paramSelectionEvent.getEnd()));
    if (paramSelectionEvent.getSessionId() != null) {
      localLogMaker.addTaggedData(1119, paramSelectionEvent.getSessionId().flattenToString());
    }
    mMetricsLogger.write(localLogMaker);
    debugLog(localLogMaker);
  }
  
  @VisibleForTesting
  public static final class SignatureParser
  {
    public SignatureParser() {}
    
    static String createSignature(String paramString1, String paramString2, int paramInt)
    {
      return String.format(Locale.US, "%s|%s|%d", new Object[] { paramString1, paramString2, Integer.valueOf(paramInt) });
    }
    
    static String getClassifierId(String paramString)
    {
      if (paramString == null) {
        return "";
      }
      int i = paramString.indexOf("|");
      if (i >= 0) {
        return paramString.substring(0, i);
      }
      return "";
    }
    
    static int getHash(String paramString)
    {
      if (paramString == null) {
        return 0;
      }
      int i = paramString.indexOf("|", paramString.indexOf("|"));
      if (i > 0) {
        return Integer.parseInt(paramString.substring(i));
      }
      return 0;
    }
    
    static String getModelName(String paramString)
    {
      if (paramString == null) {
        return "";
      }
      int i = paramString.indexOf("|") + 1;
      int j = paramString.indexOf("|", i);
      if ((i >= 1) && (j >= i)) {
        return paramString.substring(i, j);
      }
      return "";
    }
  }
}
