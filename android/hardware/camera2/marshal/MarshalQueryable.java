package android.hardware.camera2.marshal;

import android.hardware.camera2.utils.TypeReference;

public abstract interface MarshalQueryable<T>
{
  public abstract Marshaler<T> createMarshaler(TypeReference<T> paramTypeReference, int paramInt);
  
  public abstract boolean isTypeMappingSupported(TypeReference<T> paramTypeReference, int paramInt);
}
