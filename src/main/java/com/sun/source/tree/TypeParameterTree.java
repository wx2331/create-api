package com.sun.source.tree;

import java.util.List;
import javax.lang.model.element.Name;
import jdk.Exported;

@Exported
public interface TypeParameterTree extends Tree {
  Name getName();
  
  List<? extends Tree> getBounds();
  
  List<? extends AnnotationTree> getAnnotations();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\source\tree\TypeParameterTree.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */