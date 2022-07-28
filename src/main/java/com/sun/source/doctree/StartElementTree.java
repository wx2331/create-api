package com.sun.source.doctree;

import java.util.List;
import javax.lang.model.element.Name;
import jdk.Exported;

@Exported
public interface StartElementTree extends DocTree {
  Name getName();
  
  List<? extends DocTree> getAttributes();
  
  boolean isSelfClosing();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\source\doctree\StartElementTree.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */