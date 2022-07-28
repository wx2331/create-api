/*     */ package com.sun.tools.internal.ws.processor.modeler.wsdl;
/*     */ 
/*     */ import com.sun.tools.internal.ws.processor.model.AbstractType;
/*     */ import com.sun.tools.internal.ws.processor.model.Block;
/*     */ import com.sun.tools.internal.ws.processor.model.Parameter;
/*     */ import com.sun.tools.internal.ws.processor.model.java.JavaSimpleType;
/*     */ import com.sun.tools.internal.ws.processor.model.java.JavaStructureMember;
/*     */ import com.sun.tools.internal.ws.processor.model.java.JavaStructureType;
/*     */ import com.sun.tools.internal.ws.processor.model.java.JavaType;
/*     */ import com.sun.tools.internal.ws.processor.model.jaxb.JAXBElementMember;
/*     */ import com.sun.tools.internal.ws.processor.model.jaxb.JAXBProperty;
/*     */ import com.sun.tools.internal.ws.processor.model.jaxb.JAXBStructuredType;
/*     */ import com.sun.tools.internal.ws.processor.model.jaxb.JAXBType;
/*     */ import com.sun.tools.internal.ws.processor.model.jaxb.JAXBTypeAndAnnotation;
/*     */ import com.sun.tools.internal.ws.processor.model.jaxb.RpcLitMember;
/*     */ import com.sun.tools.internal.ws.processor.model.jaxb.RpcLitStructure;
/*     */ import com.sun.tools.internal.ws.resources.ModelerMessages;
/*     */ import com.sun.tools.internal.ws.util.ClassNameInfo;
/*     */ import com.sun.tools.internal.ws.wscompile.AbortException;
/*     */ import com.sun.tools.internal.ws.wscompile.ErrorReceiverFilter;
/*     */ import com.sun.tools.internal.ws.wsdl.document.Message;
/*     */ import com.sun.tools.internal.ws.wsdl.document.MessagePart;
/*     */ import com.sun.tools.internal.xjc.api.S2JJAXBModel;
/*     */ import com.sun.tools.internal.xjc.api.TypeAndAnnotation;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ModelerUtils
/*     */ {
/*     */   public static JAXBStructuredType createJAXBStructureType(JAXBType jaxbType) {
/*  67 */     JAXBStructuredType type = new JAXBStructuredType(jaxbType);
/*  68 */     type.setName(jaxbType.getName());
/*  69 */     type.setJavaType(jaxbType.getJavaType());
/*  70 */     return type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<Parameter> createUnwrappedParameters(JAXBType jaxbType, Block block) {
/*  84 */     List<Parameter> paramList = new ArrayList<>();
/*  85 */     JAXBStructuredType type = null;
/*  86 */     if (!(jaxbType instanceof JAXBStructuredType)) {
/*  87 */       type = createJAXBStructureType(jaxbType);
/*     */     } else {
/*  89 */       type = (JAXBStructuredType)jaxbType;
/*     */     } 
/*     */     
/*  92 */     JavaStructureType jst = new JavaStructureType(jaxbType.getJavaType().getRealName(), true, type);
/*  93 */     type.setJavaType((JavaType)jst);
/*  94 */     block.setType((AbstractType)type);
/*  95 */     List memberList = jaxbType.getWrapperChildren();
/*  96 */     Iterator<JAXBProperty> props = memberList.iterator();
/*  97 */     while (props.hasNext()) {
/*  98 */       JAXBProperty prop = props.next();
/*  99 */       paramList.add(createUnwrappedParameter(prop, jaxbType, block, type, jst));
/*     */     } 
/*     */ 
/*     */     
/* 103 */     return paramList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Parameter createUnwrappedParameter(JAXBProperty prop, JAXBType jaxbType, Block block, JAXBStructuredType type, JavaStructureType jst) {
/* 115 */     QName elementName = prop.getElementName();
/* 116 */     JavaSimpleType javaSimpleType = new JavaSimpleType(prop.getType());
/* 117 */     JAXBElementMember eType = new JAXBElementMember(elementName, jaxbType);
/*     */     
/* 119 */     JavaStructureMember jsm = new JavaStructureMember(elementName.getLocalPart(), (JavaType)javaSimpleType, eType);
/* 120 */     eType.setJavaStructureMember(jsm);
/* 121 */     jst.add(jsm);
/* 122 */     eType.setProperty(prop);
/* 123 */     type.add(eType);
/*     */     
/* 125 */     JAXBType t = new JAXBType(elementName, (JavaType)javaSimpleType, jaxbType.getJaxbMapping(), jaxbType.getJaxbModel());
/* 126 */     t.setUnwrapped(true);
/* 127 */     Parameter parameter = createParameter(elementName.getLocalPart(), (AbstractType)t, block);
/* 128 */     parameter.setEmbedded(true);
/* 129 */     return parameter;
/*     */   }
/*     */   
/*     */   public static List<Parameter> createRpcLitParameters(Message message, Block block, S2JJAXBModel jaxbModel, ErrorReceiverFilter errReceiver) {
/* 133 */     RpcLitStructure rpcStruct = (RpcLitStructure)block.getType();
/*     */     
/* 135 */     List<Parameter> parameters = new ArrayList<>();
/* 136 */     for (MessagePart part : message.getParts()) {
/* 137 */       if (!isBoundToSOAPBody(part))
/*     */         continue; 
/* 139 */       QName name = part.getDescriptor();
/* 140 */       TypeAndAnnotation typeAndAnn = jaxbModel.getJavaType(name);
/* 141 */       if (typeAndAnn == null) {
/* 142 */         String msgQName = "{" + message.getDefining().getTargetNamespaceURI() + "}" + message.getName();
/* 143 */         errReceiver.error(part.getLocator(), ModelerMessages.WSDLMODELER_RPCLIT_UNKOWNSCHEMATYPE(name.toString(), part
/* 144 */               .getName(), msgQName));
/* 145 */         throw new AbortException();
/*     */       } 
/* 147 */       String type = typeAndAnn.getTypeClass().fullName();
/* 148 */       type = ClassNameInfo.getGenericClass(type);
/* 149 */       RpcLitMember param = new RpcLitMember(new QName("", part.getName()), type);
/* 150 */       JavaSimpleType javaSimpleType = new JavaSimpleType(new JAXBTypeAndAnnotation(typeAndAnn));
/* 151 */       param.setJavaType((JavaType)javaSimpleType);
/* 152 */       rpcStruct.addRpcLitMember(param);
/* 153 */       Parameter parameter = createParameter(part.getName(), (AbstractType)param, block);
/* 154 */       parameter.setEmbedded(true);
/* 155 */       parameters.add(parameter);
/*     */     } 
/* 157 */     return parameters;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Parameter createParameter(String partName, AbstractType jaxbType, Block block) {
/* 171 */     Parameter parameter = new Parameter(partName, block.getEntity());
/* 172 */     parameter.setProperty("com.sun.xml.internal.ws.processor.model.ParamMessagePartName", partName);
/*     */     
/* 174 */     parameter.setEmbedded(false);
/* 175 */     parameter.setType(jaxbType);
/* 176 */     parameter.setTypeName(jaxbType.getJavaType().getType().getName());
/* 177 */     parameter.setBlock(block);
/* 178 */     return parameter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Parameter getParameter(String paramName, List<Parameter> parameters) {
/* 189 */     if (parameters == null)
/* 190 */       return null; 
/* 191 */     for (Parameter param : parameters) {
/*     */       
/* 193 */       if (param.getName().equals(paramName)) {
/* 194 */         return param;
/*     */       }
/*     */     } 
/* 197 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isEquivalentLiteralStructures(JAXBStructuredType struct1, JAXBStructuredType struct2) {
/* 210 */     if (struct1.getElementMembersCount() != struct2.getElementMembersCount())
/* 211 */       return false; 
/* 212 */     Iterator<JAXBElementMember> members = struct1.getElementMembers();
/*     */ 
/*     */     
/* 215 */     for (int i = 0; members.hasNext(); i++) {
/* 216 */       JAXBElementMember member1 = members.next();
/* 217 */       JavaStructureMember javaMember1 = member1.getJavaStructureMember();
/*     */       
/* 219 */       JavaStructureMember javaMember2 = ((JavaStructureType)struct2.getJavaType()).getMemberByName(member1
/* 220 */           .getJavaStructureMember().getName());
/* 221 */       if (javaMember2.getConstructorPos() != i || 
/* 222 */         !javaMember1.getType().equals(javaMember2.getType())) {
/* 223 */         return false;
/*     */       }
/*     */     } 
/* 226 */     return false;
/*     */   }
/*     */   
/*     */   public static QName getRawTypeName(Parameter parameter) {
/* 230 */     String name = parameter.getName();
/*     */     
/* 232 */     if (parameter.getType() instanceof JAXBType) {
/* 233 */       JAXBType jt = (JAXBType)parameter.getType();
/* 234 */       if (jt.isUnwrappable()) {
/* 235 */         List<JAXBProperty> props = jt.getWrapperChildren();
/* 236 */         for (JAXBProperty prop : props) {
/* 237 */           if (prop.getName().equals(name)) {
/* 238 */             return prop.getRawTypeName();
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 243 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isBoundToMimeContent(MessagePart part) {
/* 251 */     if (part != null && part.getBindingExtensibilityElementKind() == 5)
/* 252 */       return true; 
/* 253 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isBoundToSOAPBody(MessagePart part) {
/* 261 */     if (part != null && part.getBindingExtensibilityElementKind() == 1)
/* 262 */       return true; 
/* 263 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isBoundToSOAPHeader(MessagePart part) {
/* 271 */     if (part != null && part.getBindingExtensibilityElementKind() == 2)
/* 272 */       return true; 
/* 273 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean isUnbound(MessagePart part) {
/* 277 */     if (part != null && part.getBindingExtensibilityElementKind() == -1)
/* 278 */       return true; 
/* 279 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\processor\modeler\wsdl\ModelerUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */