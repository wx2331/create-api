/*    */ package com.sun.tools.internal.xjc.reader.xmlschema.bindinfo;
/*    */ 
/*    */ import com.sun.tools.internal.xjc.model.CPropertyInfo;
/*    */ import com.sun.tools.internal.xjc.reader.Ring;
/*    */ import com.sun.tools.internal.xjc.reader.xmlschema.BGMBuilder;
/*    */ import com.sun.xml.internal.xsom.XSComponent;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ @XmlRootElement(name = "inlineBinaryData")
/*    */ public class BIInlineBinaryData
/*    */   extends AbstractDeclarationImpl
/*    */ {
/*    */   public static void handle(XSComponent source, CPropertyInfo prop) {
/* 51 */     BIInlineBinaryData inline = ((BGMBuilder)Ring.get(BGMBuilder.class)).getBindInfo(source).<BIInlineBinaryData>get(BIInlineBinaryData.class);
/* 52 */     if (inline != null) {
/* 53 */       prop.inlineBinaryData = true;
/* 54 */       inline.markAsAcknowledged();
/*    */     } 
/*    */   }
/*    */   
/*    */   public final QName getName() {
/* 59 */     return NAME;
/*    */   }
/*    */   
/* 62 */   public static final QName NAME = new QName("http://java.sun.com/xml/ns/jaxb", "inlineBinaryData");
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\xmlschema\bindinfo\BIInlineBinaryData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */