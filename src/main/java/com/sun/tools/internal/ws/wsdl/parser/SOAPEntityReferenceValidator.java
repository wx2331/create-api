/*     */ package com.sun.tools.internal.ws.wsdl.parser;
/*     */ 
/*     */ import com.sun.tools.internal.ws.wsdl.document.schema.SchemaConstants;
/*     */ import com.sun.tools.internal.ws.wsdl.document.schema.SchemaKinds;
/*     */ import com.sun.tools.internal.ws.wsdl.document.soap.SOAPConstants;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.EntityReferenceValidator;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.Kind;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
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
/*     */ 
/*     */ public class SOAPEntityReferenceValidator
/*     */   implements EntityReferenceValidator
/*     */ {
/*     */   public boolean isValid(Kind kind, QName name) {
/*  51 */     if (name.getNamespaceURI().equals("http://www.w3.org/XML/1998/namespace")) {
/*  52 */       return true;
/*     */     }
/*  54 */     if (kind == SchemaKinds.XSD_TYPE)
/*  55 */       return _validTypes.contains(name); 
/*  56 */     if (kind == SchemaKinds.XSD_ELEMENT)
/*  57 */       return _validElements.contains(name); 
/*  58 */     if (kind == SchemaKinds.XSD_ATTRIBUTE) {
/*  59 */       return _validAttributes.contains(name);
/*     */     }
/*     */     
/*  62 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  72 */   private static final Set _validTypes = new HashSet(); static {
/*  73 */     _validTypes.add(SOAPConstants.QNAME_TYPE_ARRAY);
/*  74 */     _validTypes.add(SchemaConstants.QNAME_TYPE_STRING);
/*  75 */     _validTypes.add(SchemaConstants.QNAME_TYPE_NORMALIZED_STRING);
/*  76 */     _validTypes.add(SchemaConstants.QNAME_TYPE_TOKEN);
/*  77 */     _validTypes.add(SchemaConstants.QNAME_TYPE_BYTE);
/*  78 */     _validTypes.add(SchemaConstants.QNAME_TYPE_UNSIGNED_BYTE);
/*  79 */     _validTypes.add(SchemaConstants.QNAME_TYPE_BASE64_BINARY);
/*  80 */     _validTypes.add(SchemaConstants.QNAME_TYPE_HEX_BINARY);
/*  81 */     _validTypes.add(SchemaConstants.QNAME_TYPE_INTEGER);
/*  82 */     _validTypes.add(SchemaConstants.QNAME_TYPE_POSITIVE_INTEGER);
/*  83 */     _validTypes.add(SchemaConstants.QNAME_TYPE_NEGATIVE_INTEGER);
/*  84 */     _validTypes.add(SchemaConstants.QNAME_TYPE_NON_NEGATIVE_INTEGER);
/*  85 */     _validTypes.add(SchemaConstants.QNAME_TYPE_NON_POSITIVE_INTEGER);
/*  86 */     _validTypes.add(SchemaConstants.QNAME_TYPE_INT);
/*  87 */     _validTypes.add(SchemaConstants.QNAME_TYPE_UNSIGNED_INT);
/*  88 */     _validTypes.add(SchemaConstants.QNAME_TYPE_LONG);
/*  89 */     _validTypes.add(SchemaConstants.QNAME_TYPE_UNSIGNED_LONG);
/*  90 */     _validTypes.add(SchemaConstants.QNAME_TYPE_SHORT);
/*  91 */     _validTypes.add(SchemaConstants.QNAME_TYPE_UNSIGNED_SHORT);
/*  92 */     _validTypes.add(SchemaConstants.QNAME_TYPE_DECIMAL);
/*  93 */     _validTypes.add(SchemaConstants.QNAME_TYPE_FLOAT);
/*  94 */     _validTypes.add(SchemaConstants.QNAME_TYPE_DOUBLE);
/*  95 */     _validTypes.add(SchemaConstants.QNAME_TYPE_BOOLEAN);
/*  96 */     _validTypes.add(SchemaConstants.QNAME_TYPE_TIME);
/*  97 */     _validTypes.add(SchemaConstants.QNAME_TYPE_DATE_TIME);
/*  98 */     _validTypes.add(SchemaConstants.QNAME_TYPE_DURATION);
/*  99 */     _validTypes.add(SchemaConstants.QNAME_TYPE_DATE);
/* 100 */     _validTypes.add(SchemaConstants.QNAME_TYPE_G_MONTH);
/* 101 */     _validTypes.add(SchemaConstants.QNAME_TYPE_G_YEAR);
/* 102 */     _validTypes.add(SchemaConstants.QNAME_TYPE_G_YEAR_MONTH);
/* 103 */     _validTypes.add(SchemaConstants.QNAME_TYPE_G_DAY);
/* 104 */     _validTypes.add(SchemaConstants.QNAME_TYPE_G_MONTH_DAY);
/* 105 */     _validTypes.add(SchemaConstants.QNAME_TYPE_NAME);
/* 106 */     _validTypes.add(SchemaConstants.QNAME_TYPE_QNAME);
/* 107 */     _validTypes.add(SchemaConstants.QNAME_TYPE_NCNAME);
/* 108 */     _validTypes.add(SchemaConstants.QNAME_TYPE_ANY_URI);
/* 109 */     _validTypes.add(SchemaConstants.QNAME_TYPE_ID);
/* 110 */     _validTypes.add(SchemaConstants.QNAME_TYPE_IDREF);
/* 111 */     _validTypes.add(SchemaConstants.QNAME_TYPE_IDREFS);
/* 112 */     _validTypes.add(SchemaConstants.QNAME_TYPE_ENTITY);
/* 113 */     _validTypes.add(SchemaConstants.QNAME_TYPE_ENTITIES);
/* 114 */     _validTypes.add(SchemaConstants.QNAME_TYPE_NOTATION);
/* 115 */     _validTypes.add(SchemaConstants.QNAME_TYPE_NMTOKEN);
/* 116 */     _validTypes.add(SchemaConstants.QNAME_TYPE_NMTOKENS);
/* 117 */     _validTypes.add(SchemaConstants.QNAME_TYPE_URTYPE);
/* 118 */     _validTypes.add(SchemaConstants.QNAME_TYPE_SIMPLE_URTYPE);
/* 119 */     _validTypes.add(SOAPConstants.QNAME_TYPE_STRING);
/* 120 */     _validTypes.add(SOAPConstants.QNAME_TYPE_NORMALIZED_STRING);
/* 121 */     _validTypes.add(SOAPConstants.QNAME_TYPE_TOKEN);
/* 122 */     _validTypes.add(SOAPConstants.QNAME_TYPE_BYTE);
/* 123 */     _validTypes.add(SOAPConstants.QNAME_TYPE_UNSIGNED_BYTE);
/* 124 */     _validTypes.add(SOAPConstants.QNAME_TYPE_BASE64_BINARY);
/* 125 */     _validTypes.add(SOAPConstants.QNAME_TYPE_HEX_BINARY);
/* 126 */     _validTypes.add(SOAPConstants.QNAME_TYPE_INTEGER);
/* 127 */     _validTypes.add(SOAPConstants.QNAME_TYPE_POSITIVE_INTEGER);
/* 128 */     _validTypes.add(SOAPConstants.QNAME_TYPE_NEGATIVE_INTEGER);
/* 129 */     _validTypes.add(SOAPConstants.QNAME_TYPE_NON_NEGATIVE_INTEGER);
/* 130 */     _validTypes.add(SOAPConstants.QNAME_TYPE_NON_POSITIVE_INTEGER);
/* 131 */     _validTypes.add(SOAPConstants.QNAME_TYPE_INT);
/* 132 */     _validTypes.add(SOAPConstants.QNAME_TYPE_UNSIGNED_INT);
/* 133 */     _validTypes.add(SOAPConstants.QNAME_TYPE_LONG);
/* 134 */     _validTypes.add(SOAPConstants.QNAME_TYPE_UNSIGNED_LONG);
/* 135 */     _validTypes.add(SOAPConstants.QNAME_TYPE_SHORT);
/* 136 */     _validTypes.add(SOAPConstants.QNAME_TYPE_UNSIGNED_SHORT);
/* 137 */     _validTypes.add(SOAPConstants.QNAME_TYPE_DECIMAL);
/* 138 */     _validTypes.add(SOAPConstants.QNAME_TYPE_FLOAT);
/* 139 */     _validTypes.add(SOAPConstants.QNAME_TYPE_DOUBLE);
/* 140 */     _validTypes.add(SOAPConstants.QNAME_TYPE_BOOLEAN);
/* 141 */     _validTypes.add(SOAPConstants.QNAME_TYPE_TIME);
/* 142 */     _validTypes.add(SOAPConstants.QNAME_TYPE_DATE_TIME);
/* 143 */     _validTypes.add(SOAPConstants.QNAME_TYPE_DURATION);
/* 144 */     _validTypes.add(SOAPConstants.QNAME_TYPE_DATE);
/* 145 */     _validTypes.add(SOAPConstants.QNAME_TYPE_G_MONTH);
/* 146 */     _validTypes.add(SOAPConstants.QNAME_TYPE_G_YEAR);
/* 147 */     _validTypes.add(SOAPConstants.QNAME_TYPE_G_YEAR_MONTH);
/* 148 */     _validTypes.add(SOAPConstants.QNAME_TYPE_G_DAY);
/* 149 */     _validTypes.add(SOAPConstants.QNAME_TYPE_G_MONTH_DAY);
/* 150 */     _validTypes.add(SOAPConstants.QNAME_TYPE_NAME);
/* 151 */     _validTypes.add(SOAPConstants.QNAME_TYPE_QNAME);
/* 152 */     _validTypes.add(SOAPConstants.QNAME_TYPE_NCNAME);
/* 153 */     _validTypes.add(SOAPConstants.QNAME_TYPE_ANY_URI);
/* 154 */     _validTypes.add(SOAPConstants.QNAME_TYPE_ID);
/* 155 */     _validTypes.add(SOAPConstants.QNAME_TYPE_IDREF);
/* 156 */     _validTypes.add(SOAPConstants.QNAME_TYPE_IDREFS);
/* 157 */     _validTypes.add(SOAPConstants.QNAME_TYPE_ENTITY);
/* 158 */     _validTypes.add(SOAPConstants.QNAME_TYPE_ENTITIES);
/* 159 */     _validTypes.add(SOAPConstants.QNAME_TYPE_NOTATION);
/* 160 */     _validTypes.add(SOAPConstants.QNAME_TYPE_NMTOKEN);
/* 161 */     _validTypes.add(SOAPConstants.QNAME_TYPE_NMTOKENS);
/* 162 */     _validTypes.add(SOAPConstants.QNAME_TYPE_BASE64);
/*     */     
/* 164 */     _validTypes.add(SchemaConstants.QNAME_TYPE_LANGUAGE);
/*     */   }
/*     */   
/* 167 */   private static final Set _validElements = new HashSet(); static {
/* 168 */     _validElements.add(SOAPConstants.QNAME_ELEMENT_STRING);
/* 169 */     _validElements.add(SOAPConstants.QNAME_ELEMENT_NORMALIZED_STRING);
/* 170 */     _validElements.add(SOAPConstants.QNAME_ELEMENT_TOKEN);
/* 171 */     _validElements.add(SOAPConstants.QNAME_ELEMENT_BYTE);
/* 172 */     _validElements.add(SOAPConstants.QNAME_ELEMENT_UNSIGNED_BYTE);
/* 173 */     _validElements.add(SOAPConstants.QNAME_ELEMENT_BASE64_BINARY);
/* 174 */     _validElements.add(SOAPConstants.QNAME_ELEMENT_HEX_BINARY);
/* 175 */     _validElements.add(SOAPConstants.QNAME_ELEMENT_INTEGER);
/* 176 */     _validElements.add(SOAPConstants.QNAME_ELEMENT_POSITIVE_INTEGER);
/* 177 */     _validElements.add(SOAPConstants.QNAME_ELEMENT_NEGATIVE_INTEGER);
/* 178 */     _validElements.add(SOAPConstants.QNAME_ELEMENT_NON_NEGATIVE_INTEGER);
/* 179 */     _validElements.add(SOAPConstants.QNAME_ELEMENT_NON_POSITIVE_INTEGER);
/* 180 */     _validElements.add(SOAPConstants.QNAME_ELEMENT_INT);
/* 181 */     _validElements.add(SOAPConstants.QNAME_ELEMENT_UNSIGNED_INT);
/* 182 */     _validElements.add(SOAPConstants.QNAME_ELEMENT_LONG);
/* 183 */     _validElements.add(SOAPConstants.QNAME_ELEMENT_UNSIGNED_LONG);
/* 184 */     _validElements.add(SOAPConstants.QNAME_ELEMENT_SHORT);
/* 185 */     _validElements.add(SOAPConstants.QNAME_ELEMENT_UNSIGNED_SHORT);
/* 186 */     _validElements.add(SOAPConstants.QNAME_ELEMENT_DECIMAL);
/* 187 */     _validElements.add(SOAPConstants.QNAME_ELEMENT_FLOAT);
/* 188 */     _validElements.add(SOAPConstants.QNAME_ELEMENT_DOUBLE);
/* 189 */     _validElements.add(SOAPConstants.QNAME_ELEMENT_BOOLEAN);
/* 190 */     _validElements.add(SOAPConstants.QNAME_ELEMENT_TIME);
/* 191 */     _validElements.add(SOAPConstants.QNAME_ELEMENT_DATE_TIME);
/* 192 */     _validElements.add(SOAPConstants.QNAME_ELEMENT_DURATION);
/* 193 */     _validElements.add(SOAPConstants.QNAME_ELEMENT_DATE);
/* 194 */     _validElements.add(SOAPConstants.QNAME_ELEMENT_G_MONTH);
/* 195 */     _validElements.add(SOAPConstants.QNAME_ELEMENT_G_YEAR);
/* 196 */     _validElements.add(SOAPConstants.QNAME_ELEMENT_G_YEAR_MONTH);
/* 197 */     _validElements.add(SOAPConstants.QNAME_ELEMENT_G_DAY);
/* 198 */     _validElements.add(SOAPConstants.QNAME_ELEMENT_G_MONTH_DAY);
/* 199 */     _validElements.add(SOAPConstants.QNAME_ELEMENT_NAME);
/* 200 */     _validElements.add(SOAPConstants.QNAME_ELEMENT_QNAME);
/* 201 */     _validElements.add(SOAPConstants.QNAME_ELEMENT_NCNAME);
/* 202 */     _validElements.add(SOAPConstants.QNAME_ELEMENT_ANY_URI);
/* 203 */     _validElements.add(SOAPConstants.QNAME_ELEMENT_ID);
/* 204 */     _validElements.add(SOAPConstants.QNAME_ELEMENT_IDREF);
/* 205 */     _validElements.add(SOAPConstants.QNAME_ELEMENT_IDREFS);
/* 206 */     _validElements.add(SOAPConstants.QNAME_ELEMENT_ENTITY);
/* 207 */     _validElements.add(SOAPConstants.QNAME_ELEMENT_ENTITIES);
/* 208 */     _validElements.add(SOAPConstants.QNAME_ELEMENT_NOTATION);
/* 209 */     _validElements.add(SOAPConstants.QNAME_ELEMENT_NMTOKEN);
/* 210 */     _validElements.add(SOAPConstants.QNAME_ELEMENT_NMTOKENS);
/*     */   }
/* 212 */   private static final Set _validAttributes = new HashSet(); static {
/* 213 */     _validAttributes.add(SOAPConstants.QNAME_ATTR_ARRAY_TYPE);
/* 214 */     _validAttributes.add(SOAPConstants.QNAME_ATTR_OFFSET);
/* 215 */     _validAttributes.add(SOAPConstants.QNAME_ATTR_POSITION);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\parser\SOAPEntityReferenceValidator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */