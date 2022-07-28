package com.sun.tools.internal.xjc.outline;

import com.sun.codemodel.internal.JClass;
import com.sun.codemodel.internal.JClassContainer;
import com.sun.codemodel.internal.JCodeModel;
import com.sun.codemodel.internal.JPackage;
import com.sun.codemodel.internal.JType;
import com.sun.tools.internal.xjc.ErrorReceiver;
import com.sun.tools.internal.xjc.model.CClassInfo;
import com.sun.tools.internal.xjc.model.CClassInfoParent;
import com.sun.tools.internal.xjc.model.CElementInfo;
import com.sun.tools.internal.xjc.model.CEnumLeafInfo;
import com.sun.tools.internal.xjc.model.CPropertyInfo;
import com.sun.tools.internal.xjc.model.CTypeRef;
import com.sun.tools.internal.xjc.model.Model;
import com.sun.tools.internal.xjc.util.CodeModelClassFactory;
import java.util.Collection;

public interface Outline {
  Model getModel();
  
  JCodeModel getCodeModel();
  
  FieldOutline getField(CPropertyInfo paramCPropertyInfo);
  
  PackageOutline getPackageContext(JPackage paramJPackage);
  
  Collection<? extends ClassOutline> getClasses();
  
  ClassOutline getClazz(CClassInfo paramCClassInfo);
  
  ElementOutline getElement(CElementInfo paramCElementInfo);
  
  EnumOutline getEnum(CEnumLeafInfo paramCEnumLeafInfo);
  
  Collection<EnumOutline> getEnums();
  
  Iterable<? extends PackageOutline> getAllPackageContexts();
  
  CodeModelClassFactory getClassFactory();
  
  ErrorReceiver getErrorReceiver();
  
  JClassContainer getContainer(CClassInfoParent paramCClassInfoParent, Aspect paramAspect);
  
  JType resolve(CTypeRef paramCTypeRef, Aspect paramAspect);
  
  JClass addRuntime(Class paramClass);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\outline\Outline.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */