package android.hardware.camera2.marshal;

import android.hardware.camera2.utils.TypeReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class MarshalRegistry
{
  private static final Object sMarshalLock = new Object();
  private static final HashMap<MarshalToken<?>, Marshaler<?>> sMarshalerMap = new HashMap();
  private static final List<MarshalQueryable<?>> sRegisteredMarshalQueryables = new ArrayList();
  
  private MarshalRegistry()
  {
    throw new AssertionError();
  }
  
  public static <T> Marshaler<T> getMarshaler(TypeReference<T> paramTypeReference, int paramInt)
  {
    synchronized (sMarshalLock)
    {
      MarshalToken localMarshalToken = new android/hardware/camera2/marshal/MarshalRegistry$MarshalToken;
      localMarshalToken.<init>(paramTypeReference, paramInt);
      Object localObject2 = (Marshaler)sMarshalerMap.get(localMarshalToken);
      Object localObject3 = localObject2;
      if (localObject2 == null) {
        if (sRegisteredMarshalQueryables.size() != 0)
        {
          Iterator localIterator = sRegisteredMarshalQueryables.iterator();
          for (;;)
          {
            localObject3 = localObject2;
            if (!localIterator.hasNext()) {
              break;
            }
            localObject3 = (MarshalQueryable)localIterator.next();
            if (((MarshalQueryable)localObject3).isTypeMappingSupported(paramTypeReference, paramInt))
            {
              localObject3 = ((MarshalQueryable)localObject3).createMarshaler(paramTypeReference, paramInt);
              break;
            }
          }
          if (localObject3 != null)
          {
            sMarshalerMap.put(localMarshalToken, localObject3);
          }
          else
          {
            localObject3 = new java/lang/UnsupportedOperationException;
            localObject2 = new java/lang/StringBuilder;
            ((StringBuilder)localObject2).<init>();
            ((StringBuilder)localObject2).append("Could not find marshaler that matches the requested combination of type reference ");
            ((StringBuilder)localObject2).append(paramTypeReference);
            ((StringBuilder)localObject2).append(" and native type ");
            ((StringBuilder)localObject2).append(MarshalHelpers.toStringNativeType(paramInt));
            ((UnsupportedOperationException)localObject3).<init>(((StringBuilder)localObject2).toString());
            throw ((Throwable)localObject3);
          }
        }
        else
        {
          paramTypeReference = new java/lang/AssertionError;
          paramTypeReference.<init>("No available query marshalers registered");
          throw paramTypeReference;
        }
      }
      return localObject3;
    }
  }
  
  public static <T> void registerMarshalQueryable(MarshalQueryable<T> paramMarshalQueryable)
  {
    synchronized (sMarshalLock)
    {
      sRegisteredMarshalQueryables.add(paramMarshalQueryable);
      return;
    }
  }
  
  private static class MarshalToken<T>
  {
    private final int hash;
    final int nativeType;
    final TypeReference<T> typeReference;
    
    public MarshalToken(TypeReference<T> paramTypeReference, int paramInt)
    {
      typeReference = paramTypeReference;
      nativeType = paramInt;
      hash = (paramTypeReference.hashCode() ^ paramInt);
    }
    
    public boolean equals(Object paramObject)
    {
      boolean bool1 = paramObject instanceof MarshalToken;
      boolean bool2 = false;
      if (bool1)
      {
        paramObject = (MarshalToken)paramObject;
        bool1 = bool2;
        if (typeReference.equals(typeReference))
        {
          bool1 = bool2;
          if (nativeType == nativeType) {
            bool1 = true;
          }
        }
        return bool1;
      }
      return false;
    }
    
    public int hashCode()
    {
      return hash;
    }
  }
}
