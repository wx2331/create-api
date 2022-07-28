/*    */ package com.sun.tools.internal.ws.processor.model.jaxb;
/*    */ 
/*    */ import com.sun.tools.internal.ws.processor.model.java.JavaStructureMember;
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JAXBElementMember
/*    */ {
/*    */   private QName _name;
/*    */   private JAXBType _type;
/*    */   private JavaStructureMember _javaStructureMember;
/*    */   private boolean _repeated;
/*    */   
/*    */   public JAXBElementMember() {}
/*    */   
/*    */   public JAXBElementMember(QName name, JAXBType type) {
/* 42 */     this(name, type, null);
/*    */   }
/*    */   
/*    */   public JAXBElementMember(QName name, JAXBType type, JavaStructureMember javaStructureMember) {
/* 46 */     this._name = name;
/* 47 */     this._type = type;
/* 48 */     this._javaStructureMember = javaStructureMember;
/*    */   }
/*    */   public QName getName() {
/* 51 */     return this._name;
/*    */   }
/*    */   public void setName(QName n) {
/* 54 */     this._name = n;
/*    */   }
/*    */   public JAXBType getType() {
/* 57 */     return this._type;
/*    */   }
/*    */   public void setType(JAXBType t) {
/* 60 */     this._type = t;
/*    */   }
/*    */   public boolean isRepeated() {
/* 63 */     return this._repeated;
/*    */   }
/*    */   public void setRepeated(boolean b) {
/* 66 */     this._repeated = b;
/*    */   }
/*    */   public JavaStructureMember getJavaStructureMember() {
/* 69 */     return this._javaStructureMember;
/*    */   }
/*    */   public void setJavaStructureMember(JavaStructureMember javaStructureMember) {
/* 72 */     this._javaStructureMember = javaStructureMember;
/*    */   }
/*    */   public boolean isInherited() {
/* 75 */     return this.isInherited;
/*    */   }
/*    */   public void setInherited(boolean b) {
/* 78 */     this.isInherited = b;
/*    */   }
/*    */   public JAXBProperty getProperty() {
/* 81 */     if (this._prop == null && this._type != null)
/* 82 */       for (JAXBProperty prop : this._type.getWrapperChildren()) {
/* 83 */         if (prop.getElementName().equals(this._name)) {
/* 84 */           setProperty(prop);
/*    */         }
/*    */       }  
/* 87 */     return this._prop;
/*    */   }
/*    */   public void setProperty(JAXBProperty prop) {
/* 90 */     this._prop = prop;
/*    */   }
/*    */   
/*    */   private boolean isInherited = false;
/*    */   private JAXBProperty _prop;
/*    */   private static final String JAXB_UNIQUE_PARRAM = "__jaxbUniqueParam_";
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\processor\model\jaxb\JAXBElementMember.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */