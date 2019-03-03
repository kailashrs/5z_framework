package android.filterfw.core;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.FIELD})
public @interface GenerateProgramPort
{
  boolean hasDefault() default false;
  
  String name();
  
  Class type();
  
  String variableName() default "";
}
