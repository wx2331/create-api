package com.sun.source.tree;

import javax.lang.model.element.Name;
import jdk.Exported;

@Exported
public interface LabeledStatementTree extends StatementTree {
  Name getLabel();
  
  StatementTree getStatement();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\source\tree\LabeledStatementTree.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */