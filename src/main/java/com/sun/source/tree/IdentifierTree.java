package com.sun.source.tree;

import javax.lang.model.element.Name;
import jdk.Exported;

@Exported
public interface IdentifierTree extends ExpressionTree {
  Name getName();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\source\tree\IdentifierTree.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */