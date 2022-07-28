package com.sun.tools.internal.ws.api.wsdl;

import com.sun.codemodel.internal.JClass;
import java.util.Map;

public interface TWSDLOperation extends TWSDLExtensible {
  Map<String, JClass> getFaults();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\api\wsdl\TWSDLOperation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */