/*    */ package com.sun.tools.internal.xjc.reader.xmlschema.bindinfo;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import javax.xml.bind.annotation.XmlRootElement;
/*    */ import javax.xml.namespace.QName;
/*    */ import org.xml.sax.Locator;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @XmlRootElement(name = "substitutable", namespace = "http://java.sun.com/xml/ns/jaxb/xjc")
/*    */ public final class BIXSubstitutable
/*    */   extends AbstractDeclarationImpl
/*    */ {
/*    */   public final QName getName() {
/* 44 */     return NAME;
/*    */   }
/*    */   
/* 47 */   public static final QName NAME = new QName("http://java.sun.com/xml/ns/jaxb/xjc", "substitutable");
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\xmlschema\bindinfo\BIXSubstitutable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */