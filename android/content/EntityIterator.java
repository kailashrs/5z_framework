package android.content;

import java.util.Iterator;

public abstract interface EntityIterator
  extends Iterator<Entity>
{
  public abstract void close();
  
  public abstract void reset();
}
