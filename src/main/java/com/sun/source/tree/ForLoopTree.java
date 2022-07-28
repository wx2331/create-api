package com.sun.source.tree;

import java.util.List;
import jdk.Exported;

@Exported
public interface ForLoopTree extends StatementTree {
  List<? extends StatementTree> getInitializer();
  
  ExpressionTree getCondition();
  
  List<? extends ExpressionStatementTree> getUpdate();
  
  StatementTree getStatement();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\source\tree\ForLoopTree.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */