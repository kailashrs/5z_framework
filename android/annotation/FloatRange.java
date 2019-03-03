package android.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target({java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.PARAMETER, java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.LOCAL_VARIABLE})
public @interface FloatRange
{
  double from() default Double.NEGATIVE_INFINITY;
  
  boolean fromInclusive() default true;
  
  double to() default Double.POSITIVE_INFINITY;
  
  boolean toInclusive() default true;
}
