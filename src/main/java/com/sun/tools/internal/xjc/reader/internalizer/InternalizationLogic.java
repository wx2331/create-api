package com.sun.tools.internal.xjc.reader.internalizer;

import org.w3c.dom.Element;
import org.xml.sax.helpers.XMLFilterImpl;

public interface InternalizationLogic {
  XMLFilterImpl createExternalReferenceFinder(DOMForest paramDOMForest);
  
  boolean checkIfValidTargetNode(DOMForest paramDOMForest, Element paramElement1, Element paramElement2);
  
  Element refineTarget(Element paramElement);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\internalizer\InternalizationLogic.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */