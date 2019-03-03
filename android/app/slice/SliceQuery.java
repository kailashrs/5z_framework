package android.app.slice;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class SliceQuery
{
  private static final String TAG = "SliceQuery";
  
  public SliceQuery() {}
  
  public static boolean compareTypes(SliceItem paramSliceItem, String paramString)
  {
    if ((paramString.length() == 3) && (paramString.equals("*/*"))) {
      return true;
    }
    if ((paramSliceItem.getSubType() == null) && (paramString.indexOf('/') < 0)) {
      return paramSliceItem.getFormat().equals(paramString);
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramSliceItem.getFormat());
    localStringBuilder.append("/");
    localStringBuilder.append(paramSliceItem.getSubType());
    return localStringBuilder.toString().matches(paramString.replaceAll("\\*", ".*"));
  }
  
  private static boolean contains(SliceItem paramSliceItem1, SliceItem paramSliceItem2)
  {
    if ((paramSliceItem1 != null) && (paramSliceItem2 != null)) {
      return stream(paramSliceItem1).filter(new _..Lambda.SliceQuery.fdDPNErwIni_vCQ6k_MlGGBunoE(paramSliceItem2)).findAny().isPresent();
    }
    return false;
  }
  
  public static SliceItem find(Slice paramSlice, String paramString)
  {
    return find(paramSlice, paramString, (String[])null, null);
  }
  
  public static SliceItem find(Slice paramSlice, String paramString1, String paramString2, String paramString3)
  {
    return find(paramSlice, paramString1, new String[] { paramString2 }, new String[] { paramString3 });
  }
  
  public static SliceItem find(Slice paramSlice, String paramString, String[] paramArrayOfString1, String[] paramArrayOfString2)
  {
    List localList = paramSlice.getHints();
    return find(new SliceItem(paramSlice, "slice", null, (String[])localList.toArray(new String[localList.size()])), paramString, paramArrayOfString1, paramArrayOfString2);
  }
  
  public static SliceItem find(SliceItem paramSliceItem, String paramString)
  {
    return find(paramSliceItem, paramString, (String[])null, null);
  }
  
  public static SliceItem find(SliceItem paramSliceItem, String paramString1, String paramString2, String paramString3)
  {
    return find(paramSliceItem, paramString1, new String[] { paramString2 }, new String[] { paramString3 });
  }
  
  public static SliceItem find(SliceItem paramSliceItem, String paramString, String[] paramArrayOfString1, String[] paramArrayOfString2)
  {
    return (SliceItem)stream(paramSliceItem).filter(new _..Lambda.SliceQuery.cG9kHpHpv4nbm7p3sCvlkQGlqQw(paramString, paramArrayOfString1, paramArrayOfString2)).findFirst().orElse(null);
  }
  
  public static List<SliceItem> findAll(SliceItem paramSliceItem, String paramString)
  {
    return findAll(paramSliceItem, paramString, (String[])null, null);
  }
  
  public static List<SliceItem> findAll(SliceItem paramSliceItem, String paramString1, String paramString2, String paramString3)
  {
    return findAll(paramSliceItem, paramString1, new String[] { paramString2 }, new String[] { paramString3 });
  }
  
  public static List<SliceItem> findAll(SliceItem paramSliceItem, String paramString, String[] paramArrayOfString1, String[] paramArrayOfString2)
  {
    return (List)stream(paramSliceItem).filter(new _..Lambda.SliceQuery.hLToAajdaMbaf8BUtZ8fpGK980E(paramString, paramArrayOfString1, paramArrayOfString2)).collect(Collectors.toList());
  }
  
  public static SliceItem findNotContaining(SliceItem paramSliceItem, List<SliceItem> paramList)
  {
    Object localObject = null;
    while ((localObject == null) && (paramList.size() != 0))
    {
      SliceItem localSliceItem = (SliceItem)paramList.remove(0);
      if (!contains(paramSliceItem, localSliceItem)) {
        localObject = localSliceItem;
      }
    }
    return localObject;
  }
  
  public static SliceItem getPrimaryIcon(Slice paramSlice)
  {
    paramSlice = paramSlice.getItems().iterator();
    while (paramSlice.hasNext())
    {
      SliceItem localSliceItem = (SliceItem)paramSlice.next();
      if (Objects.equals(localSliceItem.getFormat(), "image")) {
        return localSliceItem;
      }
      if (((!compareTypes(localSliceItem, "slice")) || (!localSliceItem.hasHint("list"))) && (!localSliceItem.hasHint("actions")) && (!localSliceItem.hasHint("list_item")) && (!compareTypes(localSliceItem, "action")))
      {
        localSliceItem = find(localSliceItem, "image");
        if (localSliceItem != null) {
          return localSliceItem;
        }
      }
    }
    return null;
  }
  
  public static Stream<SliceItem> stream(SliceItem paramSliceItem)
  {
    LinkedList localLinkedList = new LinkedList();
    localLinkedList.add(paramSliceItem);
    StreamSupport.stream(Spliterators.spliteratorUnknownSize(new Iterator()
    {
      public boolean hasNext()
      {
        boolean bool;
        if (size() != 0) {
          bool = true;
        } else {
          bool = false;
        }
        return bool;
      }
      
      public SliceItem next()
      {
        SliceItem localSliceItem = (SliceItem)poll();
        if ((SliceQuery.compareTypes(localSliceItem, "slice")) || (SliceQuery.compareTypes(localSliceItem, "action"))) {
          addAll(localSliceItem.getSlice().getItems());
        }
        return localSliceItem;
      }
    }, 0), false);
  }
}
