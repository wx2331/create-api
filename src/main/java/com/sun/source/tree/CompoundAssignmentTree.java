package com.sun.source.tree;

import jdk.Exported;

@Exported
public interface CompoundAssignmentTree extends ExpressionTree {
  ExpressionTree getVariable();
  
  ExpressionTree getExpression();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\source\tree\CompoundAssignmentTree.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */