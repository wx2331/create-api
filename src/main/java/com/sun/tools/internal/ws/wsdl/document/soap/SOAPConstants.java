/*     */ package com.sun.tools.internal.ws.wsdl.document.soap;
/*     */ 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface SOAPConstants
/*     */ {
/*     */   public static final String URI_ENVELOPE = "http://schemas.xmlsoap.org/soap/envelope/";
/*     */   public static final String NS_WSDL_SOAP = "http://schemas.xmlsoap.org/wsdl/soap/";
/*     */   public static final String NS_SOAP_ENCODING = "http://schemas.xmlsoap.org/soap/encoding/";
/*     */   public static final String URI_SOAP_TRANSPORT_HTTP = "http://schemas.xmlsoap.org/soap/http";
/*  50 */   public static final QName QNAME_ADDRESS = new QName("http://schemas.xmlsoap.org/wsdl/soap/", "address");
/*     */   
/*  52 */   public static final QName QNAME_BINDING = new QName("http://schemas.xmlsoap.org/wsdl/soap/", "binding");
/*     */   
/*  54 */   public static final QName QNAME_BODY = new QName("http://schemas.xmlsoap.org/wsdl/soap/", "body");
/*  55 */   public static final QName QNAME_FAULT = new QName("http://schemas.xmlsoap.org/wsdl/soap/", "fault");
/*  56 */   public static final QName QNAME_HEADER = new QName("http://schemas.xmlsoap.org/wsdl/soap/", "header");
/*  57 */   public static final QName QNAME_HEADERFAULT = new QName("http://schemas.xmlsoap.org/wsdl/soap/", "headerfault");
/*     */   
/*  59 */   public static final QName QNAME_OPERATION = new QName("http://schemas.xmlsoap.org/wsdl/soap/", "operation");
/*     */   
/*  61 */   public static final QName QNAME_MUSTUNDERSTAND = new QName("http://schemas.xmlsoap.org/soap/envelope/", "mustUnderstand");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  66 */   public static final QName QNAME_TYPE_ARRAY = new QName("http://schemas.xmlsoap.org/soap/encoding/", "Array");
/*     */   
/*  68 */   public static final QName QNAME_ATTR_GROUP_COMMON_ATTRIBUTES = new QName("http://schemas.xmlsoap.org/soap/encoding/", "commonAttributes");
/*     */   
/*  70 */   public static final QName QNAME_ATTR_ARRAY_TYPE = new QName("http://schemas.xmlsoap.org/soap/encoding/", "arrayType");
/*     */   
/*  72 */   public static final QName QNAME_ATTR_OFFSET = new QName("http://schemas.xmlsoap.org/soap/encoding/", "offset");
/*     */   
/*  74 */   public static final QName QNAME_ATTR_POSITION = new QName("http://schemas.xmlsoap.org/soap/encoding/", "position");
/*     */ 
/*     */   
/*  77 */   public static final QName QNAME_TYPE_BASE64 = new QName("http://schemas.xmlsoap.org/soap/encoding/", "base64");
/*     */ 
/*     */   
/*  80 */   public static final QName QNAME_ELEMENT_STRING = new QName("http://schemas.xmlsoap.org/soap/encoding/", "string");
/*     */   
/*  82 */   public static final QName QNAME_ELEMENT_NORMALIZED_STRING = new QName("http://schemas.xmlsoap.org/soap/encoding/", "normalizedString");
/*     */   
/*  84 */   public static final QName QNAME_ELEMENT_TOKEN = new QName("http://schemas.xmlsoap.org/soap/encoding/", "token");
/*     */   
/*  86 */   public static final QName QNAME_ELEMENT_BYTE = new QName("http://schemas.xmlsoap.org/soap/encoding/", "byte");
/*     */   
/*  88 */   public static final QName QNAME_ELEMENT_UNSIGNED_BYTE = new QName("http://schemas.xmlsoap.org/soap/encoding/", "unsignedByte");
/*     */   
/*  90 */   public static final QName QNAME_ELEMENT_BASE64_BINARY = new QName("http://schemas.xmlsoap.org/soap/encoding/", "base64Binary");
/*     */   
/*  92 */   public static final QName QNAME_ELEMENT_HEX_BINARY = new QName("http://schemas.xmlsoap.org/soap/encoding/", "hexBinary");
/*     */   
/*  94 */   public static final QName QNAME_ELEMENT_INTEGER = new QName("http://schemas.xmlsoap.org/soap/encoding/", "integer");
/*     */   
/*  96 */   public static final QName QNAME_ELEMENT_POSITIVE_INTEGER = new QName("http://schemas.xmlsoap.org/soap/encoding/", "positiveInteger");
/*     */   
/*  98 */   public static final QName QNAME_ELEMENT_NEGATIVE_INTEGER = new QName("http://schemas.xmlsoap.org/soap/encoding/", "negativeInteger");
/*     */   
/* 100 */   public static final QName QNAME_ELEMENT_NON_NEGATIVE_INTEGER = new QName("http://schemas.xmlsoap.org/soap/encoding/", "nonNegativeInteger");
/*     */   
/* 102 */   public static final QName QNAME_ELEMENT_NON_POSITIVE_INTEGER = new QName("http://schemas.xmlsoap.org/soap/encoding/", "nonPositiveInteger");
/*     */   
/* 104 */   public static final QName QNAME_ELEMENT_INT = new QName("http://schemas.xmlsoap.org/soap/encoding/", "int");
/*     */   
/* 106 */   public static final QName QNAME_ELEMENT_UNSIGNED_INT = new QName("http://schemas.xmlsoap.org/soap/encoding/", "unsignedInt");
/*     */   
/* 108 */   public static final QName QNAME_ELEMENT_LONG = new QName("http://schemas.xmlsoap.org/soap/encoding/", "long");
/*     */   
/* 110 */   public static final QName QNAME_ELEMENT_UNSIGNED_LONG = new QName("http://schemas.xmlsoap.org/soap/encoding/", "unsignedLong");
/*     */   
/* 112 */   public static final QName QNAME_ELEMENT_SHORT = new QName("http://schemas.xmlsoap.org/soap/encoding/", "short");
/*     */   
/* 114 */   public static final QName QNAME_ELEMENT_UNSIGNED_SHORT = new QName("http://schemas.xmlsoap.org/soap/encoding/", "unsignedShort");
/*     */   
/* 116 */   public static final QName QNAME_ELEMENT_DECIMAL = new QName("http://schemas.xmlsoap.org/soap/encoding/", "decimal");
/*     */   
/* 118 */   public static final QName QNAME_ELEMENT_FLOAT = new QName("http://schemas.xmlsoap.org/soap/encoding/", "float");
/*     */   
/* 120 */   public static final QName QNAME_ELEMENT_DOUBLE = new QName("http://schemas.xmlsoap.org/soap/encoding/", "double");
/*     */   
/* 122 */   public static final QName QNAME_ELEMENT_BOOLEAN = new QName("http://schemas.xmlsoap.org/soap/encoding/", "boolean");
/*     */   
/* 124 */   public static final QName QNAME_ELEMENT_TIME = new QName("http://schemas.xmlsoap.org/soap/encoding/", "time");
/*     */   
/* 126 */   public static final QName QNAME_ELEMENT_DATE_TIME = new QName("http://schemas.xmlsoap.org/soap/encoding/", "dateTime");
/*     */   
/* 128 */   public static final QName QNAME_ELEMENT_DURATION = new QName("http://schemas.xmlsoap.org/soap/encoding/", "duration");
/*     */   
/* 130 */   public static final QName QNAME_ELEMENT_DATE = new QName("http://schemas.xmlsoap.org/soap/encoding/", "date");
/*     */   
/* 132 */   public static final QName QNAME_ELEMENT_G_MONTH = new QName("http://schemas.xmlsoap.org/soap/encoding/", "gMonth");
/*     */   
/* 134 */   public static final QName QNAME_ELEMENT_G_YEAR = new QName("http://schemas.xmlsoap.org/soap/encoding/", "gYear");
/*     */   
/* 136 */   public static final QName QNAME_ELEMENT_G_YEAR_MONTH = new QName("http://schemas.xmlsoap.org/soap/encoding/", "gYearMonth");
/*     */   
/* 138 */   public static final QName QNAME_ELEMENT_G_DAY = new QName("http://schemas.xmlsoap.org/soap/encoding/", "gDay");
/*     */   
/* 140 */   public static final QName QNAME_ELEMENT_G_MONTH_DAY = new QName("http://schemas.xmlsoap.org/soap/encoding/", "gMonthDay");
/*     */   
/* 142 */   public static final QName QNAME_ELEMENT_NAME = new QName("http://schemas.xmlsoap.org/soap/encoding/", "Name");
/*     */   
/* 144 */   public static final QName QNAME_ELEMENT_QNAME = new QName("http://schemas.xmlsoap.org/soap/encoding/", "QName");
/*     */   
/* 146 */   public static final QName QNAME_ELEMENT_NCNAME = new QName("http://schemas.xmlsoap.org/soap/encoding/", "NCName");
/*     */   
/* 148 */   public static final QName QNAME_ELEMENT_ANY_URI = new QName("http://schemas.xmlsoap.org/soap/encoding/", "anyURI");
/*     */   
/* 150 */   public static final QName QNAME_ELEMENT_ID = new QName("http://schemas.xmlsoap.org/soap/encoding/", "ID");
/*     */   
/* 152 */   public static final QName QNAME_ELEMENT_IDREF = new QName("http://schemas.xmlsoap.org/soap/encoding/", "IDREF");
/*     */   
/* 154 */   public static final QName QNAME_ELEMENT_IDREFS = new QName("http://schemas.xmlsoap.org/soap/encoding/", "IDREFS");
/*     */   
/* 156 */   public static final QName QNAME_ELEMENT_ENTITY = new QName("http://schemas.xmlsoap.org/soap/encoding/", "ENTITY");
/*     */   
/* 158 */   public static final QName QNAME_ELEMENT_ENTITIES = new QName("http://schemas.xmlsoap.org/soap/encoding/", "ENTITIES");
/*     */   
/* 160 */   public static final QName QNAME_ELEMENT_NOTATION = new QName("http://schemas.xmlsoap.org/soap/encoding/", "NOTATION");
/*     */   
/* 162 */   public static final QName QNAME_ELEMENT_NMTOKEN = new QName("http://schemas.xmlsoap.org/soap/encoding/", "NMTOKEN");
/*     */   
/* 164 */   public static final QName QNAME_ELEMENT_NMTOKENS = new QName("http://schemas.xmlsoap.org/soap/encoding/", "NMTOKENS");
/*     */ 
/*     */   
/* 167 */   public static final QName QNAME_TYPE_STRING = new QName("http://schemas.xmlsoap.org/soap/encoding/", "string");
/*     */   
/* 169 */   public static final QName QNAME_TYPE_NORMALIZED_STRING = new QName("http://schemas.xmlsoap.org/soap/encoding/", "normalizedString");
/*     */   
/* 171 */   public static final QName QNAME_TYPE_TOKEN = new QName("http://schemas.xmlsoap.org/soap/encoding/", "token");
/*     */   
/* 173 */   public static final QName QNAME_TYPE_BYTE = new QName("http://schemas.xmlsoap.org/soap/encoding/", "byte");
/*     */   
/* 175 */   public static final QName QNAME_TYPE_UNSIGNED_BYTE = new QName("http://schemas.xmlsoap.org/soap/encoding/", "unsignedByte");
/*     */   
/* 177 */   public static final QName QNAME_TYPE_BASE64_BINARY = new QName("http://schemas.xmlsoap.org/soap/encoding/", "base64Binary");
/*     */   
/* 179 */   public static final QName QNAME_TYPE_HEX_BINARY = new QName("http://schemas.xmlsoap.org/soap/encoding/", "hexBinary");
/*     */   
/* 181 */   public static final QName QNAME_TYPE_INTEGER = new QName("http://schemas.xmlsoap.org/soap/encoding/", "integer");
/*     */   
/* 183 */   public static final QName QNAME_TYPE_POSITIVE_INTEGER = new QName("http://schemas.xmlsoap.org/soap/encoding/", "positiveInteger");
/*     */   
/* 185 */   public static final QName QNAME_TYPE_NEGATIVE_INTEGER = new QName("http://schemas.xmlsoap.org/soap/encoding/", "negativeInteger");
/*     */   
/* 187 */   public static final QName QNAME_TYPE_NON_NEGATIVE_INTEGER = new QName("http://schemas.xmlsoap.org/soap/encoding/", "nonNegativeInteger");
/*     */   
/* 189 */   public static final QName QNAME_TYPE_NON_POSITIVE_INTEGER = new QName("http://schemas.xmlsoap.org/soap/encoding/", "nonPositiveInteger");
/*     */   
/* 191 */   public static final QName QNAME_TYPE_INT = new QName("http://schemas.xmlsoap.org/soap/encoding/", "int");
/*     */   
/* 193 */   public static final QName QNAME_TYPE_UNSIGNED_INT = new QName("http://schemas.xmlsoap.org/soap/encoding/", "unsignedInt");
/*     */   
/* 195 */   public static final QName QNAME_TYPE_LONG = new QName("http://schemas.xmlsoap.org/soap/encoding/", "long");
/*     */   
/* 197 */   public static final QName QNAME_TYPE_UNSIGNED_LONG = new QName("http://schemas.xmlsoap.org/soap/encoding/", "unsignedLong");
/*     */   
/* 199 */   public static final QName QNAME_TYPE_SHORT = new QName("http://schemas.xmlsoap.org/soap/encoding/", "short");
/*     */   
/* 201 */   public static final QName QNAME_TYPE_UNSIGNED_SHORT = new QName("http://schemas.xmlsoap.org/soap/encoding/", "unsignedShort");
/*     */   
/* 203 */   public static final QName QNAME_TYPE_DECIMAL = new QName("http://schemas.xmlsoap.org/soap/encoding/", "decimal");
/*     */   
/* 205 */   public static final QName QNAME_TYPE_FLOAT = new QName("http://schemas.xmlsoap.org/soap/encoding/", "float");
/*     */   
/* 207 */   public static final QName QNAME_TYPE_DOUBLE = new QName("http://schemas.xmlsoap.org/soap/encoding/", "double");
/*     */   
/* 209 */   public static final QName QNAME_TYPE_BOOLEAN = new QName("http://schemas.xmlsoap.org/soap/encoding/", "boolean");
/*     */   
/* 211 */   public static final QName QNAME_TYPE_TIME = new QName("http://schemas.xmlsoap.org/soap/encoding/", "time");
/*     */   
/* 213 */   public static final QName QNAME_TYPE_DATE_TIME = new QName("http://schemas.xmlsoap.org/soap/encoding/", "dateTime");
/*     */   
/* 215 */   public static final QName QNAME_TYPE_DURATION = new QName("http://schemas.xmlsoap.org/soap/encoding/", "duration");
/*     */   
/* 217 */   public static final QName QNAME_TYPE_DATE = new QName("http://schemas.xmlsoap.org/soap/encoding/", "date");
/*     */   
/* 219 */   public static final QName QNAME_TYPE_G_MONTH = new QName("http://schemas.xmlsoap.org/soap/encoding/", "gMonth");
/*     */   
/* 221 */   public static final QName QNAME_TYPE_G_YEAR = new QName("http://schemas.xmlsoap.org/soap/encoding/", "gYear");
/*     */   
/* 223 */   public static final QName QNAME_TYPE_G_YEAR_MONTH = new QName("http://schemas.xmlsoap.org/soap/encoding/", "gYearMonth");
/*     */   
/* 225 */   public static final QName QNAME_TYPE_G_DAY = new QName("http://schemas.xmlsoap.org/soap/encoding/", "gDay");
/*     */   
/* 227 */   public static final QName QNAME_TYPE_G_MONTH_DAY = new QName("http://schemas.xmlsoap.org/soap/encoding/", "gMonthDay");
/*     */   
/* 229 */   public static final QName QNAME_TYPE_NAME = new QName("http://schemas.xmlsoap.org/soap/encoding/", "Name");
/*     */   
/* 231 */   public static final QName QNAME_TYPE_QNAME = new QName("http://schemas.xmlsoap.org/soap/encoding/", "QName");
/*     */   
/* 233 */   public static final QName QNAME_TYPE_NCNAME = new QName("http://schemas.xmlsoap.org/soap/encoding/", "NCName");
/*     */   
/* 235 */   public static final QName QNAME_TYPE_ANY_URI = new QName("http://schemas.xmlsoap.org/soap/encoding/", "anyURI");
/*     */   
/* 237 */   public static final QName QNAME_TYPE_ID = new QName("http://schemas.xmlsoap.org/soap/encoding/", "ID");
/* 238 */   public static final QName QNAME_TYPE_IDREF = new QName("http://schemas.xmlsoap.org/soap/encoding/", "IDREF");
/*     */   
/* 240 */   public static final QName QNAME_TYPE_IDREFS = new QName("http://schemas.xmlsoap.org/soap/encoding/", "IDREFS");
/*     */   
/* 242 */   public static final QName QNAME_TYPE_ENTITY = new QName("http://schemas.xmlsoap.org/soap/encoding/", "ENTITY");
/*     */   
/* 244 */   public static final QName QNAME_TYPE_ENTITIES = new QName("http://schemas.xmlsoap.org/soap/encoding/", "ENTITIES");
/*     */   
/* 246 */   public static final QName QNAME_TYPE_NOTATION = new QName("http://schemas.xmlsoap.org/soap/encoding/", "NOTATION");
/*     */   
/* 248 */   public static final QName QNAME_TYPE_NMTOKEN = new QName("http://schemas.xmlsoap.org/soap/encoding/", "NMTOKEN");
/*     */   
/* 250 */   public static final QName QNAME_TYPE_NMTOKENS = new QName("http://schemas.xmlsoap.org/soap/encoding/", "NMTOKENS");
/*     */   
/* 252 */   public static final QName QNAME_TYPE_LANGUAGE = new QName("http://schemas.xmlsoap.org/soap/encoding/", "LANGUAGE");
/*     */ 
/*     */ 
/*     */   
/* 256 */   public static final QName QNAME_ATTR_ID = new QName("", "id");
/* 257 */   public static final QName QNAME_ATTR_HREF = new QName("", "href");
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\document\soap\SOAPConstants.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */