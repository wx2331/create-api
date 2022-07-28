package com.sun.xml.internal.rngom.ast.builder;

public interface ElementAnnotationBuilder<P extends com.sun.xml.internal.rngom.ast.om.ParsedPattern, E extends com.sun.xml.internal.rngom.ast.om.ParsedElementAnnotation, L extends com.sun.xml.internal.rngom.ast.om.Location, A extends Annotations<E, L, CL>, CL extends CommentList<L>> extends Annotations<E, L, CL> {
  void addText(String paramString, L paramL, CL paramCL) throws BuildException;
  
  E makeElementAnnotation() throws BuildException;
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\ast\builder\ElementAnnotationBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */