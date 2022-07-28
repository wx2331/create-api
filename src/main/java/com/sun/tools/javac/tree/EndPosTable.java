package com.sun.tools.javac.tree;

public interface EndPosTable {
  int getEndPos(JCTree paramJCTree);
  
  void storeEnd(JCTree paramJCTree, int paramInt);
  
  int replaceTree(JCTree paramJCTree1, JCTree paramJCTree2);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\tree\EndPosTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */