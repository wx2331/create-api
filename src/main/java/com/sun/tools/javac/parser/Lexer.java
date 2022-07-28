package com.sun.tools.javac.parser;

import com.sun.tools.javac.util.Position;

public interface Lexer {
  void nextToken();
  
  Tokens.Token token();
  
  Tokens.Token token(int paramInt);
  
  Tokens.Token prevToken();
  
  Tokens.Token split();
  
  int errPos();
  
  void errPos(int paramInt);
  
  Position.LineMap getLineMap();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\parser\Lexer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */