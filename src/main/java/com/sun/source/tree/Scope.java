package com.sun.source.tree;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import jdk.Exported;

@Exported
public interface Scope {
  Scope getEnclosingScope();
  
  TypeElement getEnclosingClass();
  
  ExecutableElement getEnclosingMethod();
  
  Iterable<? extends Element> getLocalElements();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\source\tree\Scope.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */