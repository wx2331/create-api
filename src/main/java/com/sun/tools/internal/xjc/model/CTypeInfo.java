package com.sun.tools.internal.xjc.model;

import com.sun.codemodel.internal.JType;
import com.sun.tools.internal.xjc.model.nav.NClass;
import com.sun.tools.internal.xjc.model.nav.NType;
import com.sun.tools.internal.xjc.outline.Aspect;
import com.sun.tools.internal.xjc.outline.Outline;
import com.sun.xml.internal.bind.v2.model.core.TypeInfo;

public interface CTypeInfo extends TypeInfo<NType, NClass>, CCustomizable {
  JType toType(Outline paramOutline, Aspect paramAspect);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\model\CTypeInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */