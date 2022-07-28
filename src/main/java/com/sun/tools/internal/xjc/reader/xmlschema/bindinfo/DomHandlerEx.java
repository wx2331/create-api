/*    */ package com.sun.tools.internal.xjc.reader.xmlschema.bindinfo;
/*    */
/*    */ import com.sun.xml.internal.bind.marshaller.SAX2DOMEx;
/*    */ import com.sun.xml.internal.bind.v2.util.XmlFactory;
/*    */ import javax.xml.bind.ValidationEventHandler;
/*    */ import javax.xml.bind.annotation.DomHandler;
/*    */ import javax.xml.parsers.DocumentBuilderFactory;
/*    */ import javax.xml.parsers.ParserConfigurationException;
/*    */ import javax.xml.transform.Result;
/*    */ import javax.xml.transform.Source;
/*    */ import javax.xml.transform.dom.DOMSource;
/*    */ import javax.xml.transform.sax.SAXResult;
/*    */ import org.w3c.dom.Document;
/*    */ import org.w3c.dom.Element;
/*    */ import org.xml.sax.Locator;
/*    */ import org.xml.sax.helpers.LocatorImpl;
/*    */ import org.xml.sax.helpers.XMLFilterImpl;
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
/*    */ final class DomHandlerEx
/*    */   implements DomHandler<DomHandlerEx.DomAndLocation, DomHandlerEx.ResultImpl>
/*    */ {
/*    */   public static final class DomAndLocation
/*    */   {
/*    */     public final Element element;
/*    */     public final Locator loc;
/*    */
/*    */     public DomAndLocation(Element element, Locator loc) {
/* 57 */       this.element = element;
/* 58 */       this.loc = loc;
/*    */     }
/*    */   }
/*    */
/*    */   public ResultImpl createUnmarshaller(ValidationEventHandler errorHandler) {
/* 63 */     return new ResultImpl();
/*    */   }
/*    */
/*    */   public DomAndLocation getElement(ResultImpl r) {
/* 67 */     return new DomAndLocation(((Document)r.s2d.getDOM()).getDocumentElement(), r.location);
/*    */   }
/*    */
/*    */   public Source marshal(DomAndLocation domAndLocation, ValidationEventHandler errorHandler) {
/* 71 */     return new DOMSource(domAndLocation.element);
/*    */   }
/*    */
/*    */   public static final class ResultImpl
/*    */     extends SAXResult {
/*    */     final SAX2DOMEx s2d;
/* 77 */     Locator location = null;
/*    */
/*    */     ResultImpl() {
/*    */       try {
/* 81 */         DocumentBuilderFactory factory = XmlFactory.createDocumentBuilderFactory(false);
/* 82 */         this.s2d = new SAX2DOMEx(factory);
/* 83 */       } catch (ParserConfigurationException e) {
/* 84 */         throw new AssertionError(e);
/*    */       }
/*    */
/* 87 */       XMLFilterImpl f = new XMLFilterImpl()
/*    */         {
/*    */           public void setDocumentLocator(Locator locator) {
/* 90 */             super.setDocumentLocator(locator);
/* 91 */             ResultImpl.this.location = new LocatorImpl(locator);
/*    */           }
/*    */         };
/* 94 */       f.setContentHandler(this.s2d);
/*    */
/* 96 */       setHandler(f);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\xmlschema\bindinfo\DomHandlerEx.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
