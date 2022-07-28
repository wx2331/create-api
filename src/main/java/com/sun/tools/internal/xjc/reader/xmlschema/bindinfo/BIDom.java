/*    */ package com.sun.tools.internal.xjc.reader.xmlschema.bindinfo;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import javax.xml.bind.annotation.XmlAttribute;
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
/*    */ @XmlRootElement(name = "dom")
/*    */ public class BIDom
/*    */   extends AbstractDeclarationImpl
/*    */ {
/*    */   @XmlAttribute
/*    */   String type;
/*    */   
/*    */   public final QName getName() {
/* 47 */     return NAME;
/*    */   }
/*    */   
/* 50 */   public static final QName NAME = new QName("http://java.sun.com/xml/ns/jaxb", "dom");
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\xmlschema\bindinfo\BIDom.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */