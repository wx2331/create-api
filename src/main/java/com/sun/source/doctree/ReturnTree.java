package com.sun.source.doctree;

import java.util.List;
import jdk.Exported;

@Exported
public interface ReturnTree extends BlockTagTree {
  List<? extends DocTree> getDescription();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\source\doctree\ReturnTree.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */