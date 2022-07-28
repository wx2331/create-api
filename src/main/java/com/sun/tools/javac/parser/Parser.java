package com.sun.tools.javac.parser;

import com.sun.tools.javac.tree.JCTree;

public interface Parser {
  JCTree.JCCompilationUnit parseCompilationUnit();
  
  JCTree.JCExpression parseExpression();
  
  JCTree.JCStatement parseStatement();
  
  JCTree.JCExpression parseType();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\parser\Parser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */