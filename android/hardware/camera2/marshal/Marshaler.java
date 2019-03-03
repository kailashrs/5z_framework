package android.hardware.camera2.marshal;

import android.hardware.camera2.utils.TypeReference;
import com.android.internal.util.Preconditions;
import java.nio.ByteBuffer;

public abstract class Marshaler<T>
{
  public static int NATIVE_SIZE_DYNAMIC = -1;
  protected final int mNativeType;
  protected final TypeReference<T> mTypeReference;
  
  protected Marshaler(MarshalQueryable<T> paramMarshalQueryable, TypeReference<T> paramTypeReference, int paramInt)
  {
    mTypeReference = ((TypeReference)Preconditions.checkNotNull(paramTypeReference, "typeReference must not be null"));
    mNativeType = MarshalHelpers.checkNativeType(paramInt);
    if (paramMarshalQueryable.isTypeMappingSupported(paramTypeReference, paramInt)) {
      return;
    }
    paramMarshalQueryable = new StringBuilder();
    paramMarshalQueryable.append("Unsupported type marshaling for managed type ");
    paramMarshalQueryable.append(paramTypeReference);
    paramMarshalQueryable.append(" and native type ");
    paramMarshalQueryable.append(MarshalHelpers.toStringNativeType(paramInt));
    throw new UnsupportedOperationException(paramMarshalQueryable.toString());
  }
  
  public int calculateMarshalSize(T paramT)
  {
    int i = getNativeSize();
    if (i != NATIVE_SIZE_DYNAMIC) {
      return i;
    }
    throw new AssertionError("Override this function for dynamically-sized objects");
  }
  
  public abstract int getNativeSize();
  
  public int getNativeType()
  {
    return mNativeType;
  }
  
  public TypeReference<T> getTypeReference()
  {
    return mTypeReference;
  }
  
  public abstract void marshal(T paramT, ByteBuffer paramByteBuffer);
  
  public abstract T unmarshal(ByteBuffer paramByteBuffer);
}
