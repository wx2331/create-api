package com.sun.source.tree;

import java.util.List;
import javax.lang.model.element.Name;
import jdk.Exported;

@Exported
public interface MethodTree extends Tree {
  ModifiersTree getModifiers();
  
  Name getName();
  
  Tree getReturnType();
  
  List<? extends TypeParameterTree> getTypeParameters();
  
  List<? extends VariableTree> getParameters();
  
  VariableTree getReceiverParameter();
  
  List<? extends ExpressionTree> getThrows();
  
  BlockTree getBody();
  
  Tree getDefaultValue();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\source\tree\MethodTree.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */