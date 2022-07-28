package com.sun.xml.internal.rngom.ast.builder;

public interface Annotations<E extends com.sun.xml.internal.rngom.ast.om.ParsedElementAnnotation, L extends com.sun.xml.internal.rngom.ast.om.Location, CL extends CommentList<L>> {
  void addAttribute(String paramString1, String paramString2, String paramString3, String paramString4, L paramL) throws BuildException;
  
  void addElement(E paramE) throws BuildException;
  
  void addComment(CL paramCL) throws BuildException;
  
  void addLeadingComment(CL paramCL) throws BuildException;
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\ast\builder\Annotations.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */