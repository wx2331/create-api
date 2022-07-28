package sun.tools.java;

import sun.tools.tree.Node;

public interface ParserActions {
  void packageDeclaration(long paramLong, IdentifierToken paramIdentifierToken);
  
  void importClass(long paramLong, IdentifierToken paramIdentifierToken);
  
  void importPackage(long paramLong, IdentifierToken paramIdentifierToken);
  
  ClassDefinition beginClass(long paramLong, String paramString, int paramInt, IdentifierToken paramIdentifierToken1, IdentifierToken paramIdentifierToken2, IdentifierToken[] paramArrayOfIdentifierToken);
  
  void endClass(long paramLong, ClassDefinition paramClassDefinition);
  
  void defineField(long paramLong, ClassDefinition paramClassDefinition, String paramString, int paramInt, Type paramType, IdentifierToken paramIdentifierToken, IdentifierToken[] paramArrayOfIdentifierToken1, IdentifierToken[] paramArrayOfIdentifierToken2, Node paramNode);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\java\ParserActions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */