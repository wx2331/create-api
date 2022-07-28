package com.sun.xml.internal.rngom.ast.builder;

import java.util.List;

public interface NameClassBuilder<N extends com.sun.xml.internal.rngom.ast.om.ParsedNameClass, E extends com.sun.xml.internal.rngom.ast.om.ParsedElementAnnotation, L extends com.sun.xml.internal.rngom.ast.om.Location, A extends Annotations<E, L, CL>, CL extends CommentList<L>> {
  N annotate(N paramN, A paramA) throws BuildException;
  
  N annotateAfter(N paramN, E paramE) throws BuildException;
  
  N commentAfter(N paramN, CL paramCL) throws BuildException;
  
  N makeChoice(List<N> paramList, L paramL, A paramA);
  
  N makeName(String paramString1, String paramString2, String paramString3, L paramL, A paramA);
  
  N makeNsName(String paramString, L paramL, A paramA);
  
  N makeNsName(String paramString, N paramN, L paramL, A paramA);
  
  N makeAnyName(L paramL, A paramA);
  
  N makeAnyName(N paramN, L paramL, A paramA);
  
  N makeErrorNameClass();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\ast\builder\NameClassBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */