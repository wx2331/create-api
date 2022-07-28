package com.sun.xml.internal.rngom.binary.visitor;

import com.sun.xml.internal.rngom.binary.Pattern;
import com.sun.xml.internal.rngom.nc.NameClass;
import org.relaxng.datatype.Datatype;

public interface PatternVisitor {
  void visitEmpty();
  
  void visitNotAllowed();
  
  void visitError();
  
  void visitAfter(Pattern paramPattern1, Pattern paramPattern2);
  
  void visitGroup(Pattern paramPattern1, Pattern paramPattern2);
  
  void visitInterleave(Pattern paramPattern1, Pattern paramPattern2);
  
  void visitChoice(Pattern paramPattern1, Pattern paramPattern2);
  
  void visitOneOrMore(Pattern paramPattern);
  
  void visitElement(NameClass paramNameClass, Pattern paramPattern);
  
  void visitAttribute(NameClass paramNameClass, Pattern paramPattern);
  
  void visitData(Datatype paramDatatype);
  
  void visitDataExcept(Datatype paramDatatype, Pattern paramPattern);
  
  void visitValue(Datatype paramDatatype, Object paramObject);
  
  void visitText();
  
  void visitList(Pattern paramPattern);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\binary\visitor\PatternVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */