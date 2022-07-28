package com.sun.tools.javac.tree;

import com.sun.tools.javac.parser.Tokens;

public interface DocCommentTable {
  boolean hasComment(JCTree paramJCTree);
  
  Tokens.Comment getComment(JCTree paramJCTree);
  
  String getCommentText(JCTree paramJCTree);
  
  DCTree.DCDocComment getCommentTree(JCTree paramJCTree);
  
  void putComment(JCTree paramJCTree, Tokens.Comment paramComment);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\tree\DocCommentTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */