package com.sun.source.tree;

import java.util.List;
import jdk.Exported;

@Exported
public interface NewClassTree extends ExpressionTree {
  ExpressionTree getEnclosingExpression();
  
  List<? extends Tree> getTypeArguments();
  
  ExpressionTree getIdentifier();
  
  List<? extends ExpressionTree> getArguments();
  
  ClassTree getClassBody();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\source\tree\NewClassTree.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */