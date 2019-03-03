package com.android.internal.util.function.pooled;

import android.os.Message;
import android.text.TextUtils;
import android.util.Pools.SynchronizedPool;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.BitUtils;
import com.android.internal.util.function.HexConsumer;
import com.android.internal.util.function.HexFunction;
import com.android.internal.util.function.HexPredicate;
import com.android.internal.util.function.QuadConsumer;
import com.android.internal.util.function.QuadFunction;
import com.android.internal.util.function.QuadPredicate;
import com.android.internal.util.function.QuintConsumer;
import com.android.internal.util.function.QuintFunction;
import com.android.internal.util.function.QuintPredicate;
import com.android.internal.util.function.TriConsumer;
import com.android.internal.util.function.TriFunction;
import com.android.internal.util.function.TriPredicate;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

final class PooledLambdaImpl<R>
  extends OmniFunction<Object, Object, Object, Object, Object, Object, R>
{
  private static final boolean DEBUG = false;
  private static final int FLAG_ACQUIRED_FROM_MESSAGE_CALLBACKS_POOL = 128;
  private static final int FLAG_RECYCLED = 32;
  private static final int FLAG_RECYCLE_ON_USE = 64;
  private static final String LOG_TAG = "PooledLambdaImpl";
  static final int MASK_EXPOSED_AS = 16128;
  static final int MASK_FUNC_TYPE = 1032192;
  private static final int MAX_ARGS = 5;
  private static final int MAX_POOL_SIZE = 50;
  static final Pool sMessageCallbacksPool = new Pool(Message.sPoolSync);
  static final Pool sPool = new Pool(new Object());
  Object[] mArgs = null;
  long mConstValue;
  int mFlags = 0;
  Object mFunc;
  
  private PooledLambdaImpl() {}
  
  static <E extends PooledLambda> E acquire(Pool paramPool, Object paramObject1, int paramInt1, int paramInt2, int paramInt3, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7)
  {
    paramPool = acquire(paramPool);
    mFunc = paramObject1;
    paramPool.setFlags(1032192, LambdaType.encode(paramInt1, paramInt3));
    paramPool.setFlags(16128, LambdaType.encode(paramInt2, paramInt3));
    if (ArrayUtils.size(mArgs) < paramInt1) {
      mArgs = new Object[paramInt1];
    }
    setIfInBounds(mArgs, 0, paramObject2);
    setIfInBounds(mArgs, 1, paramObject3);
    setIfInBounds(mArgs, 2, paramObject4);
    setIfInBounds(mArgs, 3, paramObject5);
    setIfInBounds(mArgs, 4, paramObject6);
    setIfInBounds(mArgs, 5, paramObject7);
    return paramPool;
  }
  
  static PooledLambdaImpl acquire(Pool paramPool)
  {
    PooledLambdaImpl localPooledLambdaImpl1 = (PooledLambdaImpl)paramPool.acquire();
    PooledLambdaImpl localPooledLambdaImpl2 = localPooledLambdaImpl1;
    if (localPooledLambdaImpl1 == null) {
      localPooledLambdaImpl2 = new PooledLambdaImpl();
    }
    mFlags &= 0xFFFFFFDF;
    int i;
    if (paramPool == sMessageCallbacksPool) {
      i = 1;
    } else {
      i = 0;
    }
    localPooledLambdaImpl2.setFlags(128, i);
    return localPooledLambdaImpl2;
  }
  
  static PooledLambdaImpl acquireConstSupplier(int paramInt)
  {
    PooledLambdaImpl localPooledLambdaImpl = acquire(sPool);
    paramInt = LambdaType.encode(7, paramInt);
    localPooledLambdaImpl.setFlags(1032192, paramInt);
    localPooledLambdaImpl.setFlags(16128, paramInt);
    return localPooledLambdaImpl;
  }
  
  private void checkNotRecycled()
  {
    if (!isRecycled()) {
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Instance is recycled: ");
    localStringBuilder.append(this);
    throw new IllegalStateException(localStringBuilder.toString());
  }
  
  private String commaSeparateFirstN(Object[] paramArrayOfObject, int paramInt)
  {
    if (paramArrayOfObject == null) {
      return "";
    }
    return TextUtils.join(",", Arrays.copyOf(paramArrayOfObject, paramInt));
  }
  
  private R doInvoke()
  {
    int i = getFlags(1032192);
    int j = LambdaType.decodeArgCount(i);
    int k = LambdaType.decodeReturnType(i);
    switch (j)
    {
    default: 
      break;
    case 7: 
      switch (k)
      {
      default: 
        return mFunc;
      case 6: 
        return Double.valueOf(getAsDouble());
      case 5: 
        return Long.valueOf(getAsLong());
      }
      return Integer.valueOf(getAsInt());
    case 6: 
      switch (k)
      {
      default: 
        break;
      case 3: 
        return ((HexFunction)mFunc).apply(popArg(0), popArg(1), popArg(2), popArg(3), popArg(4), popArg(5));
      case 2: 
        return Boolean.valueOf(((HexPredicate)mFunc).test(popArg(0), popArg(1), popArg(2), popArg(3), popArg(4), popArg(5)));
      case 1: 
        ((HexConsumer)mFunc).accept(popArg(0), popArg(1), popArg(2), popArg(3), popArg(4), popArg(5));
        return null;
      }
      break;
    case 5: 
      switch (k)
      {
      default: 
        break;
      case 3: 
        return ((QuintFunction)mFunc).apply(popArg(0), popArg(1), popArg(2), popArg(3), popArg(4));
      case 2: 
        return Boolean.valueOf(((QuintPredicate)mFunc).test(popArg(0), popArg(1), popArg(2), popArg(3), popArg(4)));
      case 1: 
        ((QuintConsumer)mFunc).accept(popArg(0), popArg(1), popArg(2), popArg(3), popArg(4));
        return null;
      }
      break;
    case 4: 
      switch (k)
      {
      default: 
        break;
      case 3: 
        return ((QuadFunction)mFunc).apply(popArg(0), popArg(1), popArg(2), popArg(3));
      case 2: 
        return Boolean.valueOf(((QuadPredicate)mFunc).test(popArg(0), popArg(1), popArg(2), popArg(3)));
      case 1: 
        ((QuadConsumer)mFunc).accept(popArg(0), popArg(1), popArg(2), popArg(3));
        return null;
      }
      break;
    case 3: 
      switch (k)
      {
      default: 
        break;
      case 3: 
        return ((TriFunction)mFunc).apply(popArg(0), popArg(1), popArg(2));
      case 2: 
        return Boolean.valueOf(((TriPredicate)mFunc).test(popArg(0), popArg(1), popArg(2)));
      case 1: 
        ((TriConsumer)mFunc).accept(popArg(0), popArg(1), popArg(2));
        return null;
      }
      break;
    case 2: 
      switch (k)
      {
      default: 
        break;
      case 3: 
        return ((BiFunction)mFunc).apply(popArg(0), popArg(1));
      case 2: 
        return Boolean.valueOf(((BiPredicate)mFunc).test(popArg(0), popArg(1)));
      case 1: 
        ((BiConsumer)mFunc).accept(popArg(0), popArg(1));
        return null;
      }
      break;
    case 1: 
      switch (k)
      {
      default: 
        break;
      case 3: 
        return ((Function)mFunc).apply(popArg(0));
      case 2: 
        return Boolean.valueOf(((Predicate)mFunc).test(popArg(0)));
      case 1: 
        ((Consumer)mFunc).accept(popArg(0));
        return null;
      }
      break;
    case 0: 
      switch (k)
      {
      default: 
        break;
      case 2: 
      case 3: 
        return ((Supplier)mFunc).get();
      case 1: 
        ((Runnable)mFunc).run();
        return null;
      }
      break;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Unknown function type: ");
    localStringBuilder.append(LambdaType.toString(i));
    throw new IllegalStateException(localStringBuilder.toString());
  }
  
  private void doRecycle()
  {
    Pool localPool;
    if ((mFlags & 0x80) != 0) {
      localPool = sMessageCallbacksPool;
    } else {
      localPool = sPool;
    }
    mFunc = null;
    if (mArgs != null) {
      Arrays.fill(mArgs, null);
    }
    mFlags = 32;
    mConstValue = 0L;
    localPool.release(this);
  }
  
  private boolean fillInArg(Object paramObject)
  {
    int i = ArrayUtils.size(mArgs);
    for (int j = 0; j < i; j++) {
      if (mArgs[j] == ArgumentPlaceholder.INSTANCE)
      {
        mArgs[j] = paramObject;
        mFlags = ((int)(mFlags | BitUtils.bitAt(j)));
        return true;
      }
    }
    if ((paramObject != null) && (paramObject != ArgumentPlaceholder.INSTANCE))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("No more arguments expected for provided arg ");
      localStringBuilder.append(paramObject);
      localStringBuilder.append(" among ");
      localStringBuilder.append(Arrays.toString(mArgs));
      throw new IllegalStateException(localStringBuilder.toString());
    }
    return false;
  }
  
  private String getFuncTypeAsString()
  {
    if (!isRecycled())
    {
      if (isConstSupplier()) {
        return "supplier";
      }
      String str = LambdaType.toString(getFlags(16128));
      if (str.endsWith("Consumer")) {
        return "consumer";
      }
      if (str.endsWith("Function")) {
        return "function";
      }
      if (str.endsWith("Predicate")) {
        return "predicate";
      }
      if (str.endsWith("Supplier")) {
        return "supplier";
      }
      if (str.endsWith("Runnable")) {
        return "runnable";
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Don't know the string representation of ");
      localStringBuilder.append(str);
      throw new IllegalStateException(localStringBuilder.toString());
    }
    throw new IllegalStateException();
  }
  
  private static String hashCodeHex(Object paramObject)
  {
    return Integer.toHexString(paramObject.hashCode());
  }
  
  private boolean isConstSupplier()
  {
    boolean bool;
    if (LambdaType.decodeArgCount(getFlags(1032192)) == 7) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private boolean isInvocationArgAtIndex(int paramInt)
  {
    int i = mFlags;
    boolean bool = true;
    if ((i & 1 << paramInt) == 0) {
      bool = false;
    }
    return bool;
  }
  
  private boolean isRecycleOnUse()
  {
    boolean bool;
    if ((mFlags & 0x40) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private boolean isRecycled()
  {
    boolean bool;
    if ((mFlags & 0x20) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private static int mask(int paramInt1, int paramInt2)
  {
    return paramInt2 << Integer.numberOfTrailingZeros(paramInt1) & paramInt1;
  }
  
  private Object popArg(int paramInt)
  {
    Object localObject = mArgs[paramInt];
    if (isInvocationArgAtIndex(paramInt))
    {
      mArgs[paramInt] = ArgumentPlaceholder.INSTANCE;
      mFlags = ((int)(mFlags & BitUtils.bitAt(paramInt)));
    }
    return localObject;
  }
  
  private static void setIfInBounds(Object[] paramArrayOfObject, int paramInt, Object paramObject)
  {
    if (paramInt < ArrayUtils.size(paramArrayOfObject)) {
      paramArrayOfObject[paramInt] = paramObject;
    }
  }
  
  private static int unmask(int paramInt1, int paramInt2)
  {
    return (paramInt2 & paramInt1) / (1 << Integer.numberOfTrailingZeros(paramInt1));
  }
  
  public <V> OmniFunction<Object, Object, Object, Object, Object, Object, V> andThen(Function<? super R, ? extends V> paramFunction)
  {
    throw new UnsupportedOperationException();
  }
  
  public double getAsDouble()
  {
    return Double.longBitsToDouble(mConstValue);
  }
  
  public int getAsInt()
  {
    return (int)mConstValue;
  }
  
  public long getAsLong()
  {
    return mConstValue;
  }
  
  int getFlags(int paramInt)
  {
    return unmask(paramInt, mFlags);
  }
  
  R invoke(Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6)
  {
    checkNotRecycled();
    boolean bool = fillInArg(paramObject1);
    int i = 0;
    int j = 0;
    if ((bool) && (fillInArg(paramObject2)) && (fillInArg(paramObject3)) && (fillInArg(paramObject4)) && (fillInArg(paramObject5)) && (!fillInArg(paramObject6))) {}
    int k = LambdaType.decodeArgCount(getFlags(1032192));
    int m;
    if (k != 7)
    {
      m = 0;
      while (m < k) {
        if (mArgs[m] != ArgumentPlaceholder.INSTANCE)
        {
          m++;
        }
        else
        {
          paramObject1 = new StringBuilder();
          paramObject1.append("Missing argument #");
          paramObject1.append(m);
          paramObject1.append(" among ");
          paramObject1.append(Arrays.toString(mArgs));
          throw new IllegalStateException(paramObject1.toString());
        }
      }
    }
    try
    {
      paramObject1 = doInvoke();
      return paramObject1;
    }
    finally
    {
      if (isRecycleOnUse()) {
        doRecycle();
      }
      if (!isRecycled())
      {
        j = ArrayUtils.size(mArgs);
        for (m = i; m < j; m++) {
          popArg(m);
        }
      }
    }
  }
  
  public OmniFunction<Object, Object, Object, Object, Object, Object, R> negate()
  {
    throw new UnsupportedOperationException();
  }
  
  public void recycle()
  {
    if (!isRecycled()) {
      doRecycle();
    }
  }
  
  public OmniFunction<Object, Object, Object, Object, Object, Object, R> recycleOnUse()
  {
    mFlags |= 0x40;
    return this;
  }
  
  void setFlags(int paramInt1, int paramInt2)
  {
    mFlags &= paramInt1;
    mFlags |= mask(paramInt1, paramInt2);
  }
  
  public String toString()
  {
    if (isRecycled())
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("<recycled PooledLambda@");
      localStringBuilder.append(hashCodeHex(this));
      localStringBuilder.append(">");
      return localStringBuilder.toString();
    }
    StringBuilder localStringBuilder = new StringBuilder();
    if (isConstSupplier())
    {
      localStringBuilder.append(getFuncTypeAsString());
      localStringBuilder.append("(");
      localStringBuilder.append(doInvoke());
      localStringBuilder.append(")");
    }
    else
    {
      if ((mFunc instanceof PooledLambdaImpl))
      {
        localStringBuilder.append(mFunc);
      }
      else
      {
        localStringBuilder.append(getFuncTypeAsString());
        localStringBuilder.append("@");
        localStringBuilder.append(hashCodeHex(mFunc));
      }
      localStringBuilder.append("(");
      localStringBuilder.append(commaSeparateFirstN(mArgs, LambdaType.decodeArgCount(getFlags(1032192))));
      localStringBuilder.append(")");
    }
    return localStringBuilder.toString();
  }
  
  static class LambdaType
  {
    public static final int MASK = 63;
    public static final int MASK_ARG_COUNT = 7;
    public static final int MASK_BIT_COUNT = 6;
    public static final int MASK_RETURN_TYPE = 56;
    
    LambdaType() {}
    
    private static String argCountPrefix(int paramInt)
    {
      switch (paramInt)
      {
      default: 
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("");
        localStringBuilder.append(paramInt);
        throw new IllegalArgumentException(localStringBuilder.toString());
      case 7: 
        return "";
      case 6: 
        return "Hex";
      case 5: 
        return "Quint";
      case 4: 
        return "Quad";
      case 3: 
        return "Tri";
      case 2: 
        return "Bi";
      }
      return "";
    }
    
    static int decodeArgCount(int paramInt)
    {
      return paramInt & 0x7;
    }
    
    static int decodeReturnType(int paramInt)
    {
      return PooledLambdaImpl.unmask(56, paramInt);
    }
    
    static int encode(int paramInt1, int paramInt2)
    {
      return PooledLambdaImpl.mask(7, paramInt1) | PooledLambdaImpl.mask(56, paramInt2);
    }
    
    static String toString(int paramInt)
    {
      int i = decodeArgCount(paramInt);
      paramInt = decodeReturnType(paramInt);
      if (i == 0)
      {
        if (paramInt == 1) {
          return "Runnable";
        }
        if ((paramInt == 3) || (paramInt == 2)) {
          return "Supplier";
        }
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(argCountPrefix(i));
      localStringBuilder.append(ReturnType.lambdaSuffix(paramInt));
      return localStringBuilder.toString();
    }
    
    static class ReturnType
    {
      public static final int BOOLEAN = 2;
      public static final int DOUBLE = 6;
      public static final int INT = 4;
      public static final int LONG = 5;
      public static final int OBJECT = 3;
      public static final int VOID = 1;
      
      ReturnType() {}
      
      static String lambdaSuffix(int paramInt)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append(prefix(paramInt));
        localStringBuilder.append(suffix(paramInt));
        return localStringBuilder.toString();
      }
      
      private static String prefix(int paramInt)
      {
        switch (paramInt)
        {
        default: 
          return "";
        case 6: 
          return "Double";
        case 5: 
          return "Long";
        }
        return "Int";
      }
      
      private static String suffix(int paramInt)
      {
        switch (paramInt)
        {
        default: 
          return "Supplier";
        case 3: 
          return "Function";
        case 2: 
          return "Predicate";
        }
        return "Consumer";
      }
      
      static String toString(int paramInt)
      {
        switch (paramInt)
        {
        default: 
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("");
          localStringBuilder.append(paramInt);
          return localStringBuilder.toString();
        case 6: 
          return "DOUBLE";
        case 5: 
          return "LONG";
        case 4: 
          return "INT";
        case 3: 
          return "OBJECT";
        case 2: 
          return "BOOLEAN";
        }
        return "VOID";
      }
    }
  }
  
  static class Pool
    extends Pools.SynchronizedPool<PooledLambdaImpl>
  {
    public Pool(Object paramObject)
    {
      super(paramObject);
    }
  }
}
