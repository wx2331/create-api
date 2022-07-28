package com.sun.source.tree;

import jdk.Exported;

@Exported
public interface EnhancedForLoopTree extends StatementTree {
  VariableTree getVariable();
  
  ExpressionTree getExpression();
  
  StatementTree getStatement();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\source\tree\EnhancedForLoopTree.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */