/*     */ package com.sun.istack.internal.tools;
/*     */
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.lang.reflect.Field;
/*     */ import java.net.Authenticator;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.PasswordAuthentication;
/*     */ import java.net.URL;
/*     */ import java.net.URLDecoder;
/*     */ import java.net.URLEncoder;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import java.util.regex.Pattern;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.helpers.LocatorImpl;
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
/*     */ public class DefaultAuthenticator
/*     */   extends Authenticator
/*     */ {
/*     */   private static DefaultAuthenticator instance;
/*  60 */   private static Authenticator systemAuthenticator = getCurrentAuthenticator();
/*     */   private String proxyUser;
/*     */   private String proxyPasswd;
/*  63 */   private final List<AuthInfo> authInfo = new ArrayList<>();
/*  64 */   private static int counter = 0;
/*     */
/*     */
/*     */   DefaultAuthenticator() {
/*  68 */     if (System.getProperty("http.proxyUser") != null) {
/*  69 */       this.proxyUser = System.getProperty("http.proxyUser");
/*     */     } else {
/*  71 */       this.proxyUser = System.getProperty("proxyUser");
/*     */     }
/*  73 */     if (System.getProperty("http.proxyPassword") != null) {
/*  74 */       this.proxyPasswd = System.getProperty("http.proxyPassword");
/*     */     } else {
/*  76 */       this.proxyPasswd = System.getProperty("proxyPassword");
/*     */     }
/*     */   }
/*     */
/*     */   public static synchronized DefaultAuthenticator getAuthenticator() {
/*  81 */     if (instance == null) {
/*  82 */       instance = new DefaultAuthenticator();
/*  83 */       Authenticator.setDefault(instance);
/*     */     }
/*  85 */     counter++;
/*  86 */     return instance;
/*     */   }
/*     */
/*     */   public static synchronized void reset() {
/*  90 */     counter--;
/*  91 */     if (instance != null && counter == 0) {
/*  92 */       Authenticator.setDefault(systemAuthenticator);
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   protected PasswordAuthentication getPasswordAuthentication() {
/* 100 */     if (getRequestorType() == RequestorType.PROXY && this.proxyUser != null && this.proxyPasswd != null) {
/* 101 */       return new PasswordAuthentication(this.proxyUser, this.proxyPasswd.toCharArray());
/*     */     }
/* 103 */     for (AuthInfo auth : this.authInfo) {
/* 104 */       if (auth.matchingHost(getRequestingURL())) {
/* 105 */         return new PasswordAuthentication(auth.getUser(), auth.getPassword().toCharArray());
/*     */       }
/*     */     }
/* 108 */     return null;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void setProxyAuth(String proxyAuth) {
/* 117 */     if (proxyAuth == null) {
/* 118 */       this.proxyUser = null;
/* 119 */       this.proxyPasswd = null;
/*     */     } else {
/* 121 */       int i = proxyAuth.indexOf(':');
/* 122 */       if (i < 0) {
/* 123 */         this.proxyUser = proxyAuth;
/* 124 */         this.proxyPasswd = "";
/* 125 */       } else if (i == 0) {
/* 126 */         this.proxyUser = "";
/* 127 */         this.proxyPasswd = proxyAuth.substring(1);
/*     */       } else {
/* 129 */         this.proxyUser = proxyAuth.substring(0, i);
/* 130 */         this.proxyPasswd = proxyAuth.substring(i + 1);
/*     */       }
/*     */     }
/*     */   }
/*     */
/*     */   public void setAuth(File f, Receiver l) {
/* 136 */     Receiver listener = (l == null) ? new DefaultRImpl() : l;
/* 137 */     BufferedReader in = null;
/* 138 */     FileInputStream fi = null;
/* 139 */     InputStreamReader is = null;
/*     */
/*     */     try {
/* 142 */       LocatorImpl locator = new LocatorImpl();
/* 143 */       locator.setSystemId(f.getAbsolutePath());
/*     */       try {
/* 145 */         fi = new FileInputStream(f);
/* 146 */         is = new InputStreamReader(fi, "UTF-8");
/* 147 */         in = new BufferedReader(is);
/* 148 */       } catch (UnsupportedEncodingException e) {
/* 149 */         listener.onError(e, locator);
/*     */         return;
/* 151 */       } catch (FileNotFoundException e) {
/* 152 */         listener.onError(e, locator);
/*     */         return;
/*     */       }
/*     */       try {
/* 156 */         int lineno = 1;
/* 157 */         locator.setSystemId(f.getCanonicalPath()); String text;
/* 158 */         while ((text = in.readLine()) != null) {
/* 159 */           locator.setLineNumber(lineno++);
/*     */
/* 161 */           if ("".equals(text.trim()) || text.startsWith("#")) {
/*     */             continue;
/*     */           }
/*     */           try {
/* 165 */             AuthInfo ai = parseLine(text);
/* 166 */             this.authInfo.add(ai);
/* 167 */           } catch (Exception e) {
/* 168 */             listener.onParsingError(text, locator);
/*     */           }
/*     */         }
/* 171 */       } catch (IOException e) {
/* 172 */         listener.onError(e, locator);
/* 173 */         Logger.getLogger(DefaultAuthenticator.class.getName()).log(Level.SEVERE, e.getMessage(), e);
/*     */       }
/*     */     } finally {
/*     */       try {
/* 177 */         if (in != null) {
/* 178 */           in.close();
/*     */         }
/* 180 */         if (is != null) {
/* 181 */           is.close();
/*     */         }
/* 183 */         if (fi != null) {
/* 184 */           fi.close();
/*     */         }
/* 186 */       } catch (IOException ex) {
/* 187 */         Logger.getLogger(DefaultAuthenticator.class.getName()).log(Level.SEVERE, (String)null, ex);
/*     */       }
/*     */     }
/*     */   }
/*     */
/*     */   private AuthInfo parseLine(String text) throws Exception {
/*     */     URL url;
/*     */     try {
/* 195 */       url = new URL(text);
/* 196 */     } catch (MalformedURLException mue) {
/*     */
/*     */
/*     */
/*     */
/*     */
/* 202 */       int i = text.indexOf(':', text.indexOf(':') + 1) + 1;
/* 203 */       int j = text.lastIndexOf('@');
/*     */
/*     */
/*     */
/* 207 */       String encodedUrl = text.substring(0, i) + URLEncoder.encode(text.substring(i, j), "UTF-8") + text.substring(j);
/* 208 */       url = new URL(encodedUrl);
/*     */     }
/*     */
/* 211 */     String authinfo = url.getUserInfo();
/*     */
/* 213 */     if (authinfo != null) {
/* 214 */       int i = authinfo.indexOf(':');
/*     */
/* 216 */       if (i >= 0) {
/* 217 */         String user = authinfo.substring(0, i);
/* 218 */         String password = authinfo.substring(i + 1);
/* 219 */         return new AuthInfo(new URL(url
/* 220 */               .getProtocol(), url.getHost(), url.getPort(), url.getFile()), user,
/* 221 */             URLDecoder.decode(password, "UTF-8"));
/*     */       }
/*     */     }
/* 224 */     throw new Exception();
/*     */   }
/*     */
/*     */   static Authenticator getCurrentAuthenticator() {
/* 228 */     final Field f = getTheAuthenticator();
/* 229 */     if (f == null) {
/* 230 */       return null;
/*     */     }
/*     */
/*     */     try {
/* 234 */       AccessController.doPrivileged(new PrivilegedAction<Void>()
/*     */           {
/*     */             public Void run() {
/* 237 */               f.setAccessible(true);
/* 238 */               return null;
/*     */             }
/*     */           });
/* 241 */       return (Authenticator)f.get(null);
/* 242 */     } catch (Exception ex) {
/* 243 */       return null;
/*     */     } finally {
/* 245 */       AccessController.doPrivileged(new PrivilegedAction<Void>()
/*     */           {
/*     */             public Void run() {
/* 248 */               f.setAccessible(false);
/* 249 */               return null;
/*     */             }
/*     */           });
/*     */     }
/*     */   }
/*     */
/*     */   private static Field getTheAuthenticator() {
/*     */     try {
/* 257 */       return Authenticator.class.getDeclaredField("theAuthenticator");
/* 258 */     } catch (Exception ex) {
/* 259 */       return null;
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   private static class DefaultRImpl
/*     */     implements Receiver
/*     */   {
/*     */     private DefaultRImpl() {}
/*     */
/*     */
/*     */
/*     */     public void onParsingError(String line, Locator loc) {
/* 274 */       System.err.println(getLocationString(loc) + ": " + line);
/*     */     }
/*     */
/*     */
/*     */     public void onError(Exception e, Locator loc) {
/* 279 */       System.err.println(getLocationString(loc) + ": " + e.getMessage());
/* 280 */       Logger.getLogger(DefaultAuthenticator.class.getName()).log(Level.SEVERE, e.getMessage(), e);
/*     */     }
/*     */
/*     */     private String getLocationString(Locator l) {
/* 284 */       return "[" + l.getSystemId() + "#" + l.getLineNumber() + "]";
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */   static final class AuthInfo
/*     */   {
/*     */     private final String user;
/*     */
/*     */
/*     */     private final String password;
/*     */
/*     */
/*     */     private final Pattern urlPattern;
/*     */
/*     */
/*     */     public AuthInfo(URL url, String user, String password) {
/* 302 */       String u = url.toExternalForm().replaceFirst("\\?", "\\\\?");
/* 303 */       this.urlPattern = Pattern.compile(u.replace("*", ".*"), 2);
/* 304 */       this.user = user;
/* 305 */       this.password = password;
/*     */     }
/*     */
/*     */     public String getUser() {
/* 309 */       return this.user;
/*     */     }
/*     */
/*     */     public String getPassword() {
/* 313 */       return this.password;
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public boolean matchingHost(URL requestingURL) {
/* 321 */       return this.urlPattern.matcher(requestingURL.toExternalForm()).matches();
/*     */     }
/*     */   }
/*     */
/*     */   public static interface Receiver {
/*     */     void onParsingError(String param1String, Locator param1Locator);
/*     */
/*     */     void onError(Exception param1Exception, Locator param1Locator);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\istack\internal\tools\DefaultAuthenticator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
