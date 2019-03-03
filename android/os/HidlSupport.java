package android.os;

import android.annotation.SystemApi;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@SystemApi
public class HidlSupport
{
  public HidlSupport() {}
  
  @SystemApi
  public static boolean deepEquals(Object paramObject1, Object paramObject2)
  {
    boolean bool = true;
    if (paramObject1 == paramObject2) {
      return true;
    }
    if ((paramObject1 != null) && (paramObject2 != null))
    {
      Class localClass = paramObject1.getClass();
      Object localObject = paramObject2.getClass();
      if (localClass != localObject) {
        return false;
      }
      if (localClass.isArray())
      {
        localClass = localClass.getComponentType();
        if (localClass != ((Class)localObject).getComponentType()) {
          return false;
        }
        if (localClass.isPrimitive()) {
          return Objects.deepEquals(paramObject1, paramObject2);
        }
        paramObject1 = (Object[])paramObject1;
        paramObject2 = (Object[])paramObject2;
        if ((paramObject1.length != paramObject2.length) || (!IntStream.range(0, paramObject1.length).allMatch(new _..Lambda.HidlSupport.4ktYtLCfMafhYI23iSXUQOH_hxo(paramObject1, paramObject2)))) {
          bool = false;
        }
        return bool;
      }
      if ((paramObject1 instanceof List))
      {
        localObject = (List)paramObject1;
        paramObject1 = (List)paramObject2;
        if (((List)localObject).size() != paramObject1.size()) {
          return false;
        }
        paramObject2 = ((List)localObject).iterator();
        return paramObject1.stream().allMatch(new _..Lambda.HidlSupport.oV2DlGQSAfcavBj7TK20nYhwS0U(paramObject2));
      }
      throwErrorIfUnsupportedType(paramObject1);
      return paramObject1.equals(paramObject2);
    }
    return false;
  }
  
  @SystemApi
  public static int deepHashCode(Object paramObject)
  {
    if (paramObject == null) {
      return 0;
    }
    Class localClass = paramObject.getClass();
    if (localClass.isArray())
    {
      if (localClass.getComponentType().isPrimitive()) {
        return primitiveArrayHashCode(paramObject);
      }
      return Arrays.hashCode(Arrays.stream((Object[])paramObject).mapToInt(_..Lambda.HidlSupport.GHxmwrIWiKN83tl6aMQt_nV5hiw.INSTANCE).toArray());
    }
    if ((paramObject instanceof List)) {
      return Arrays.hashCode(((List)paramObject).stream().mapToInt(_..Lambda.HidlSupport.CwwfmHPEvZaybUxpLzKdwrpQRfA.INSTANCE).toArray());
    }
    throwErrorIfUnsupportedType(paramObject);
    return paramObject.hashCode();
  }
  
  @SystemApi
  public static native int getPidIfSharable();
  
  @SystemApi
  public static boolean interfacesEqual(IHwInterface paramIHwInterface, Object paramObject)
  {
    if (paramIHwInterface == paramObject) {
      return true;
    }
    if ((paramIHwInterface != null) && (paramObject != null))
    {
      if (!(paramObject instanceof IHwInterface)) {
        return false;
      }
      return Objects.equals(paramIHwInterface.asBinder(), ((IHwInterface)paramObject).asBinder());
    }
    return false;
  }
  
  private static int primitiveArrayHashCode(Object paramObject)
  {
    Class localClass = paramObject.getClass().getComponentType();
    if (localClass == Boolean.TYPE) {
      return Arrays.hashCode((boolean[])paramObject);
    }
    if (localClass == Byte.TYPE) {
      return Arrays.hashCode((byte[])paramObject);
    }
    if (localClass == Character.TYPE) {
      return Arrays.hashCode((char[])paramObject);
    }
    if (localClass == Double.TYPE) {
      return Arrays.hashCode((double[])paramObject);
    }
    if (localClass == Float.TYPE) {
      return Arrays.hashCode((float[])paramObject);
    }
    if (localClass == Integer.TYPE) {
      return Arrays.hashCode((int[])paramObject);
    }
    if (localClass == Long.TYPE) {
      return Arrays.hashCode((long[])paramObject);
    }
    if (localClass == Short.TYPE) {
      return Arrays.hashCode((short[])paramObject);
    }
    throw new UnsupportedOperationException();
  }
  
  private static void throwErrorIfUnsupportedType(Object paramObject)
  {
    if (((paramObject instanceof Collection)) && (!(paramObject instanceof List)))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Cannot check equality on collections other than lists: ");
      localStringBuilder.append(paramObject.getClass().getName());
      throw new UnsupportedOperationException(localStringBuilder.toString());
    }
    if (!(paramObject instanceof Map)) {
      return;
    }
    throw new UnsupportedOperationException("Cannot check equality on maps");
  }
  
  public static final class Mutable<E>
  {
    public E value;
    
    public Mutable()
    {
      value = null;
    }
    
    public Mutable(E paramE)
    {
      value = paramE;
    }
  }
}
