package android.gesture;

public class Prediction
{
  public final String name;
  public double score;
  
  Prediction(String paramString, double paramDouble)
  {
    name = paramString;
    score = paramDouble;
  }
  
  public String toString()
  {
    return name;
  }
}
