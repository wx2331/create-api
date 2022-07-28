package com.sun.source.tree;

import javax.lang.model.element.Name;
import jdk.Exported;

@Exported
public interface VariableTree extends StatementTree {
  ModifiersTree getModifiers();
  
  Name getName();
  
  ExpressionTree getNameExpression();
  
  Tree getType();
  
  ExpressionTree getInitializer();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\source\tree\VariableTree.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */