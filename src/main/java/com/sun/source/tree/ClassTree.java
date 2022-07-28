package com.sun.source.tree;

import java.util.List;
import javax.lang.model.element.Name;
import jdk.Exported;

@Exported
public interface ClassTree extends StatementTree {
  ModifiersTree getModifiers();
  
  Name getSimpleName();
  
  List<? extends TypeParameterTree> getTypeParameters();
  
  Tree getExtendsClause();
  
  List<? extends Tree> getImplementsClause();
  
  List<? extends Tree> getMembers();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\source\tree\ClassTree.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */