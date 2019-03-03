package android.database;

public final class CharArrayBuffer
{
  public char[] data;
  public int sizeCopied;
  
  public CharArrayBuffer(int paramInt)
  {
    data = new char[paramInt];
  }
  
  public CharArrayBuffer(char[] paramArrayOfChar)
  {
    data = paramArrayOfChar;
  }
}
