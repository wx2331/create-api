package com.sun.source.tree;

import jdk.Exported;

@Exported
public interface InstanceOfTree extends ExpressionTree {
  ExpressionTree getExpression();
  
  Tree getType();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\source\tree\InstanceOfTree.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */