package com.sun.source.tree;

import java.util.List;
import java.util.Set;
import javax.lang.model.element.Modifier;
import jdk.Exported;

@Exported
public interface ModifiersTree extends Tree {
  Set<Modifier> getFlags();
  
  List<? extends AnnotationTree> getAnnotations();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\source\tree\ModifiersTree.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */