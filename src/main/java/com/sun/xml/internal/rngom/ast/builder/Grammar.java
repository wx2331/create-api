package com.sun.xml.internal.rngom.ast.builder;

public interface Grammar<P extends com.sun.xml.internal.rngom.ast.om.ParsedPattern, E extends com.sun.xml.internal.rngom.ast.om.ParsedElementAnnotation, L extends com.sun.xml.internal.rngom.ast.om.Location, A extends Annotations<E, L, CL>, CL extends CommentList<L>> extends GrammarSection<P, E, L, A, CL>, Scope<P, E, L, A, CL> {
  P endGrammar(L paramL, A paramA) throws BuildException;
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\ast\builder\Grammar.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */