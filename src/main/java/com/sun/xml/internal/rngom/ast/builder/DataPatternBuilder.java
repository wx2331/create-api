package com.sun.xml.internal.rngom.ast.builder;

import com.sun.xml.internal.rngom.parse.Context;

public interface DataPatternBuilder<P extends com.sun.xml.internal.rngom.ast.om.ParsedPattern, E extends com.sun.xml.internal.rngom.ast.om.ParsedElementAnnotation, L extends com.sun.xml.internal.rngom.ast.om.Location, A extends Annotations<E, L, CL>, CL extends CommentList<L>> {
  void addParam(String paramString1, String paramString2, Context paramContext, String paramString3, L paramL, A paramA) throws BuildException;
  
  void annotation(E paramE);
  
  P makePattern(L paramL, A paramA) throws BuildException;
  
  P makePattern(P paramP, L paramL, A paramA) throws BuildException;
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\ast\builder\DataPatternBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */