/*     */ package sun.tools.serialver;
/*     */ 
/*     */ import java.applet.Applet;
/*     */ import java.awt.Button;
/*     */ import java.awt.Event;
/*     */ import java.awt.GridBagConstraints;
/*     */ import java.awt.GridBagLayout;
/*     */ import java.awt.Label;
/*     */ import java.awt.TextField;
/*     */ import java.awt.Window;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectStreamClass;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.net.URLClassLoader;
/*     */ import java.util.StringTokenizer;
/*     */ import sun.net.www.ParseUtil;
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
/*     */ public class SerialVer
/*     */   extends Applet
/*     */ {
/*     */   GridBagLayout gb;
/*     */   TextField classname_t;
/*     */   Button show_b;
/*     */   TextField serialversion_t;
/*     */   Label footer_l;
/*     */   private static final long serialVersionUID = 7666909783837760853L;
/*     */   
/*     */   public synchronized void init() {
/*  52 */     this.gb = new GridBagLayout();
/*  53 */     setLayout(this.gb);
/*     */     
/*  55 */     GridBagConstraints gridBagConstraints = new GridBagConstraints();
/*  56 */     gridBagConstraints.fill = 1;
/*     */     
/*  58 */     Label label1 = new Label(Res.getText("FullClassName"));
/*  59 */     label1.setAlignment(2);
/*  60 */     this.gb.setConstraints(label1, gridBagConstraints);
/*  61 */     add(label1);
/*     */     
/*  63 */     this.classname_t = new TextField(20);
/*  64 */     gridBagConstraints.gridwidth = -1;
/*  65 */     gridBagConstraints.weightx = 1.0D;
/*  66 */     this.gb.setConstraints(this.classname_t, gridBagConstraints);
/*  67 */     add(this.classname_t);
/*     */     
/*  69 */     this.show_b = new Button(Res.getText("Show"));
/*  70 */     gridBagConstraints.gridwidth = 0;
/*  71 */     gridBagConstraints.weightx = 0.0D;
/*  72 */     this.gb.setConstraints(this.show_b, gridBagConstraints);
/*  73 */     add(this.show_b);
/*     */     
/*  75 */     Label label2 = new Label(Res.getText("SerialVersion"));
/*  76 */     label2.setAlignment(2);
/*  77 */     gridBagConstraints.gridwidth = 1;
/*  78 */     this.gb.setConstraints(label2, gridBagConstraints);
/*  79 */     add(label2);
/*     */     
/*  81 */     this.serialversion_t = new TextField(50);
/*  82 */     this.serialversion_t.setEditable(false);
/*  83 */     gridBagConstraints.gridwidth = 0;
/*  84 */     this.gb.setConstraints(this.serialversion_t, gridBagConstraints);
/*  85 */     add(this.serialversion_t);
/*     */     
/*  87 */     this.footer_l = new Label();
/*  88 */     gridBagConstraints.gridwidth = 0;
/*  89 */     this.gb.setConstraints(this.footer_l, gridBagConstraints);
/*  90 */     add(this.footer_l);
/*     */ 
/*     */     
/*  93 */     this.classname_t.requestFocus();
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/*  98 */     this.classname_t.requestFocus();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean action(Event paramEvent, Object paramObject) {
/* 103 */     if (paramEvent.target == this.classname_t) {
/* 104 */       show((String)paramEvent.arg);
/* 105 */       return true;
/* 106 */     }  if (paramEvent.target == this.show_b) {
/* 107 */       show(this.classname_t.getText());
/* 108 */       return true;
/*     */     } 
/* 110 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean handleEvent(Event paramEvent) {
/* 116 */     return super.handleEvent(paramEvent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void show(String paramString) {
/*     */     try {
/* 125 */       this.footer_l.setText("");
/* 126 */       this.serialversion_t.setText("");
/*     */       
/* 128 */       if (paramString.equals("")) {
/*     */         return;
/*     */       }
/*     */       
/* 132 */       String str = serialSyntax(paramString);
/* 133 */       if (str != null) {
/* 134 */         this.serialversion_t.setText(str);
/*     */       } else {
/* 136 */         this.footer_l.setText(Res.getText("NotSerializable", paramString));
/*     */       } 
/* 138 */     } catch (ClassNotFoundException classNotFoundException) {
/* 139 */       this.footer_l.setText(Res.getText("ClassNotFound", paramString));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 147 */   static URLClassLoader loader = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void initializeLoader(String paramString) throws MalformedURLException, IOException {
/* 156 */     StringTokenizer stringTokenizer = new StringTokenizer(paramString, File.pathSeparator);
/* 157 */     int i = stringTokenizer.countTokens();
/* 158 */     URL[] arrayOfURL = new URL[i];
/* 159 */     for (byte b = 0; b < i; b++) {
/* 160 */       arrayOfURL[b] = ParseUtil.fileToEncodedURL(new File((new File(stringTokenizer
/* 161 */               .nextToken())).getCanonicalPath()));
/*     */     }
/* 163 */     loader = new URLClassLoader(arrayOfURL);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String serialSyntax(String paramString) throws ClassNotFoundException {
/* 171 */     String str = null;
/* 172 */     boolean bool = false;
/*     */ 
/*     */     
/* 175 */     if (paramString.indexOf('$') != -1) {
/* 176 */       str = resolveClass(paramString);
/*     */     } else {
/*     */ 
/*     */       
/*     */       try {
/*     */ 
/*     */         
/* 183 */         str = resolveClass(paramString);
/* 184 */         bool = true;
/* 185 */       } catch (ClassNotFoundException classNotFoundException) {}
/*     */ 
/*     */       
/* 188 */       if (!bool) {
/* 189 */         StringBuffer stringBuffer = new StringBuffer(paramString);
/* 190 */         String str1 = stringBuffer.toString();
/*     */         int i;
/* 192 */         while ((i = str1.lastIndexOf('.')) != -1 && !bool) {
/* 193 */           stringBuffer.setCharAt(i, '$');
/*     */           try {
/* 195 */             str1 = stringBuffer.toString();
/* 196 */             str = resolveClass(str1);
/* 197 */             bool = true;
/* 198 */           } catch (ClassNotFoundException classNotFoundException) {}
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 203 */       if (!bool) {
/* 204 */         throw new ClassNotFoundException();
/*     */       }
/*     */     } 
/* 207 */     return str;
/*     */   }
/*     */   
/*     */   static String resolveClass(String paramString) throws ClassNotFoundException {
/* 211 */     Class<?> clazz = Class.forName(paramString, false, loader);
/* 212 */     ObjectStreamClass objectStreamClass = ObjectStreamClass.lookup(clazz);
/* 213 */     if (objectStreamClass != null) {
/* 214 */       return "    private static final long serialVersionUID = " + objectStreamClass
/* 215 */         .getSerialVersionUID() + "L;";
/*     */     }
/* 217 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void showWindow(Window paramWindow) {
/* 223 */     paramWindow.show();
/*     */   }
/*     */   
/*     */   public static void main(String[] paramArrayOfString) {
/* 227 */     boolean bool = false;
/* 228 */     String str = null;
/* 229 */     byte b = 0;
/*     */     
/* 231 */     if (paramArrayOfString.length == 0) {
/* 232 */       usage();
/* 233 */       System.exit(1);
/*     */     } 
/*     */     
/* 236 */     for (b = 0; b < paramArrayOfString.length; b++) {
/* 237 */       if (paramArrayOfString[b].equals("-show")) {
/* 238 */         bool = true;
/* 239 */       } else if (paramArrayOfString[b].equals("-classpath")) {
/* 240 */         if (b + 1 == paramArrayOfString.length || paramArrayOfString[b + 1].startsWith("-")) {
/* 241 */           System.err.println(Res.getText("error.missing.classpath"));
/* 242 */           usage();
/* 243 */           System.exit(1);
/*     */         } 
/* 245 */         str = new String(paramArrayOfString[b + 1]);
/* 246 */         b++;
/* 247 */       } else if (paramArrayOfString[b].startsWith("-")) {
/* 248 */         System.err.println(Res.getText("invalid.flag", paramArrayOfString[b]));
/* 249 */         usage();
/* 250 */         System.exit(1);
/*     */       } else {
/*     */         break;
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 261 */     if (str == null) {
/* 262 */       str = System.getProperty("env.class.path");
/*     */ 
/*     */ 
/*     */       
/* 266 */       if (str == null) {
/* 267 */         str = ".";
/*     */       }
/*     */     } 
/*     */     
/*     */     try {
/* 272 */       initializeLoader(str);
/* 273 */     } catch (MalformedURLException malformedURLException) {
/* 274 */       System.err.println(Res.getText("error.parsing.classpath", str));
/* 275 */       System.exit(2);
/* 276 */     } catch (IOException iOException) {
/* 277 */       System.err.println(Res.getText("error.parsing.classpath", str));
/* 278 */       System.exit(3);
/*     */     } 
/*     */     
/* 281 */     if (!bool) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 286 */       if (b == paramArrayOfString.length) {
/* 287 */         usage();
/* 288 */         System.exit(1);
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 294 */       boolean bool1 = false;
/* 295 */       for (b = b; b < paramArrayOfString.length; b++) {
/*     */         try {
/* 297 */           String str1 = serialSyntax(paramArrayOfString[b]);
/* 298 */           if (str1 != null) {
/* 299 */             System.out.println(paramArrayOfString[b] + ":" + str1);
/*     */           } else {
/* 301 */             System.err.println(Res.getText("NotSerializable", paramArrayOfString[b]));
/*     */             
/* 303 */             bool1 = true;
/*     */           } 
/* 305 */         } catch (ClassNotFoundException classNotFoundException) {
/* 306 */           System.err.println(Res.getText("ClassNotFound", paramArrayOfString[b]));
/* 307 */           bool1 = true;
/*     */         } 
/*     */       } 
/* 310 */       if (bool1) {
/* 311 */         System.exit(1);
/*     */       }
/*     */     } else {
/* 314 */       if (b < paramArrayOfString.length) {
/* 315 */         System.err.println(Res.getText("ignoring.classes"));
/* 316 */         System.exit(1);
/*     */       } 
/* 318 */       SerialVerFrame serialVerFrame = new SerialVerFrame();
/*     */       
/* 320 */       SerialVer serialVer = new SerialVer();
/* 321 */       serialVer.init();
/*     */       
/* 323 */       serialVerFrame.add("Center", serialVer);
/* 324 */       serialVerFrame.pack();
/* 325 */       showWindow(serialVerFrame);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void usage() {
/* 334 */     System.err.println(Res.getText("usage"));
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\serialver\SerialVer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */