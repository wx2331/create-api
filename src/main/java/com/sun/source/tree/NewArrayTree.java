package com.sun.source.tree;

import java.util.List;
import jdk.Exported;

@Exported
public interface NewArrayTree extends ExpressionTree {
  Tree getType();
  
  List<? extends ExpressionTree> getDimensions();
  
  List<? extends ExpressionTree> getInitializers();
  
  List<? extends AnnotationTree> getAnnotations();
  
  List<? extends List<? extends AnnotationTree>> getDimAnnotations();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\source\tree\NewArrayTree.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */