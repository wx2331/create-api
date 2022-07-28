package com.sun.source.tree;

import javax.lang.model.type.TypeKind;
import jdk.Exported;

@Exported
public interface PrimitiveTypeTree extends Tree {
  TypeKind getPrimitiveTypeKind();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\source\tree\PrimitiveTypeTree.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */