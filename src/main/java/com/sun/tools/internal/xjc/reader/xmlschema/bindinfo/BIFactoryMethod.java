/*    */ package com.sun.tools.internal.xjc.reader.xmlschema.bindinfo;
/*    */ 
/*    */ import com.sun.tools.internal.xjc.model.CPropertyInfo;
/*    */ import com.sun.tools.internal.xjc.reader.Ring;
/*    */ import com.sun.tools.internal.xjc.reader.xmlschema.BGMBuilder;
/*    */ import com.sun.xml.internal.xsom.XSComponent;
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
/*    */ 
/*    */ 
/*    */ @XmlRootElement(name = "factoryMethod")
/*    */ public class BIFactoryMethod
/*    */   extends AbstractDeclarationImpl
/*    */ {
/*    */   @XmlAttribute
/*    */   public String name;
/*    */   
/*    */   public static void handle(XSComponent source, CPropertyInfo prop) {
/* 53 */     BIInlineBinaryData inline = ((BGMBuilder)Ring.get(BGMBuilder.class)).getBindInfo(source).<BIInlineBinaryData>get(BIInlineBinaryData.class);
/* 54 */     if (inline != null) {
/* 55 */       prop.inlineBinaryData = true;
/* 56 */       inline.markAsAcknowledged();
/*    */     } 
/*    */   }
/*    */   
/*    */   public final QName getName() {
/* 61 */     return NAME;
/*    */   }
/*    */   
/* 64 */   public static final QName NAME = new QName("http://java.sun.com/xml/ns/jaxb", "factoryMethod");
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\xmlschema\bindinfo\BIFactoryMethod.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */