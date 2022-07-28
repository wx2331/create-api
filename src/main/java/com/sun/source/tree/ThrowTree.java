package com.sun.source.tree;

import jdk.Exported;

@Exported
public interface ThrowTree extends StatementTree {
  ExpressionTree getExpression();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\source\tree\ThrowTree.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */