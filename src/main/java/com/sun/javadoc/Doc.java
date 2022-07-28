package com.sun.javadoc;

public interface Doc extends Comparable<Object> {
  String commentText();
  
  Tag[] tags();
  
  Tag[] tags(String paramString);
  
  SeeTag[] seeTags();
  
  Tag[] inlineTags();
  
  Tag[] firstSentenceTags();
  
  String getRawCommentText();
  
  void setRawCommentText(String paramString);
  
  String name();
  
  int compareTo(Object paramObject);
  
  boolean isField();
  
  boolean isEnumConstant();
  
  boolean isConstructor();
  
  boolean isMethod();
  
  boolean isAnnotationTypeElement();
  
  boolean isInterface();
  
  boolean isException();
  
  boolean isError();
  
  boolean isEnum();
  
  boolean isAnnotationType();
  
  boolean isOrdinaryClass();
  
  boolean isClass();
  
  boolean isIncluded();
  
  SourcePosition position();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\javadoc\Doc.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */