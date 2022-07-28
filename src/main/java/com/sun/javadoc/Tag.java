package com.sun.javadoc;

public interface Tag {
  String name();
  
  Doc holder();
  
  String kind();
  
  String text();
  
  String toString();
  
  Tag[] inlineTags();
  
  Tag[] firstSentenceTags();
  
  SourcePosition position();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\javadoc\Tag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */