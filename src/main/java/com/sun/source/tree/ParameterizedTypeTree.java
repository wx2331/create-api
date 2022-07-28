package com.sun.source.tree;

import java.util.List;
import jdk.Exported;

@Exported
public interface ParameterizedTypeTree extends Tree {
  Tree getType();
  
  List<? extends Tree> getTypeArguments();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\source\tree\ParameterizedTypeTree.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */