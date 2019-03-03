package android.view.textclassifier;

public abstract interface TextClassificationSessionFactory
{
  public abstract TextClassifier createTextClassificationSession(TextClassificationContext paramTextClassificationContext);
}
