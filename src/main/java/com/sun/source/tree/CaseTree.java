package com.sun.source.tree;

import java.util.List;
import jdk.Exported;

@Exported
public interface CaseTree extends Tree {
  ExpressionTree getExpression();
  
  List<? extends StatementTree> getStatements();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\source\tree\CaseTree.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */