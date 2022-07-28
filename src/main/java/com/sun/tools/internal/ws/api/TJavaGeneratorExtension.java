package com.sun.tools.internal.ws.api;

import com.sun.codemodel.internal.JMethod;
import com.sun.tools.internal.ws.api.wsdl.TWSDLOperation;

public abstract class TJavaGeneratorExtension {
  public abstract void writeMethodAnnotations(TWSDLOperation paramTWSDLOperation, JMethod paramJMethod);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\api\TJavaGeneratorExtension.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */