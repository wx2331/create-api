package com.sun.tools.internal.xjc.generator.annotation.spec;

import com.sun.codemodel.internal.JAnnotationWriter;
import javax.xml.bind.annotation.XmlElementWrapper;

public interface XmlElementWrapperWriter extends JAnnotationWriter<XmlElementWrapper> {
  XmlElementWrapperWriter name(String paramString);
  
  XmlElementWrapperWriter namespace(String paramString);
  
  XmlElementWrapperWriter required(boolean paramBoolean);
  
  XmlElementWrapperWriter nillable(boolean paramBoolean);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\generator\annotation\spec\XmlElementWrapperWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */