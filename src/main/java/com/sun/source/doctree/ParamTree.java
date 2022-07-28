package com.sun.source.doctree;

import java.util.List;
import jdk.Exported;

@Exported
public interface ParamTree extends BlockTagTree {
  boolean isTypeParameter();
  
  IdentifierTree getName();
  
  List<? extends DocTree> getDescription();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\source\doctree\ParamTree.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */