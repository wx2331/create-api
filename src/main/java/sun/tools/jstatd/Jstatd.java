/*     */ package sun.tools.jstatd;
/*     */ 
/*     */ import java.net.MalformedURLException;
/*     */ import java.rmi.ConnectException;
/*     */ import java.rmi.Naming;
/*     */ import java.rmi.RMISecurityManager;
/*     */ import java.rmi.Remote;
/*     */ import java.rmi.RemoteException;
/*     */ import java.rmi.registry.LocateRegistry;
/*     */ import java.rmi.registry.Registry;
/*     */ import java.rmi.server.UnicastRemoteObject;
/*     */ import sun.jvmstat.monitor.remote.RemoteHost;
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
/*     */ public class Jstatd
/*     */ {
/*     */   private static Registry registry;
/*  46 */   private static int port = -1;
/*     */   private static boolean startRegistry = true;
/*     */   
/*     */   private static void printUsage() {
/*  50 */     System.err.println("usage: jstatd [-nr] [-p port] [-n rminame]");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static void bind(String paramString, RemoteHostImpl paramRemoteHostImpl) throws RemoteException, MalformedURLException, Exception {
/*     */     try {
/*  57 */       Naming.rebind(paramString, (Remote)paramRemoteHostImpl);
/*  58 */     } catch (ConnectException connectException) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  63 */       if (startRegistry && registry == null) {
/*  64 */         boolean bool = (port < 0) ? true : port;
/*  65 */         registry = LocateRegistry.createRegistry(bool);
/*  66 */         bind(paramString, paramRemoteHostImpl);
/*     */       } else {
/*     */         
/*  69 */         System.out.println("Could not contact registry\n" + connectException
/*  70 */             .getMessage());
/*  71 */         connectException.printStackTrace();
/*     */       } 
/*  73 */     } catch (RemoteException remoteException) {
/*  74 */       System.err.println("Could not bind " + paramString + " to RMI Registry");
/*  75 */       remoteException.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void main(String[] paramArrayOfString) {
/*  80 */     String str = null;
/*  81 */     byte b = 0;
/*     */     
/*  83 */     for (; b < paramArrayOfString.length && paramArrayOfString[b].startsWith("-"); b++) {
/*  84 */       String str1 = paramArrayOfString[b];
/*     */       
/*  86 */       if (str1.compareTo("-nr") == 0) {
/*  87 */         startRegistry = false;
/*  88 */       } else if (str1.startsWith("-p")) {
/*  89 */         if (str1.compareTo("-p") != 0) {
/*  90 */           port = Integer.parseInt(str1.substring(2));
/*     */         } else {
/*  92 */           b++;
/*  93 */           if (b >= paramArrayOfString.length) {
/*  94 */             printUsage();
/*  95 */             System.exit(1);
/*     */           } 
/*  97 */           port = Integer.parseInt(paramArrayOfString[b]);
/*     */         } 
/*  99 */       } else if (str1.startsWith("-n")) {
/* 100 */         if (str1.compareTo("-n") != 0) {
/* 101 */           str = str1.substring(2);
/*     */         } else {
/* 103 */           b++;
/* 104 */           if (b >= paramArrayOfString.length) {
/* 105 */             printUsage();
/* 106 */             System.exit(1);
/*     */           } 
/* 108 */           str = paramArrayOfString[b];
/*     */         } 
/*     */       } else {
/* 111 */         printUsage();
/* 112 */         System.exit(1);
/*     */       } 
/*     */     } 
/*     */     
/* 116 */     if (b < paramArrayOfString.length) {
/* 117 */       printUsage();
/* 118 */       System.exit(1);
/*     */     } 
/*     */     
/* 121 */     if (System.getSecurityManager() == null) {
/* 122 */       System.setSecurityManager(new RMISecurityManager());
/*     */     }
/*     */     
/* 125 */     StringBuilder stringBuilder = new StringBuilder();
/*     */     
/* 127 */     if (port >= 0) {
/* 128 */       stringBuilder.append("//:").append(port);
/*     */     }
/*     */     
/* 131 */     if (str == null) {
/* 132 */       str = "JStatRemoteHost";
/*     */     }
/*     */     
/* 135 */     stringBuilder.append("/").append(str);
/*     */ 
/*     */     
/*     */     try {
/* 139 */       System.setProperty("java.rmi.server.ignoreSubClasses", "true");
/* 140 */       RemoteHostImpl remoteHostImpl = new RemoteHostImpl();
/* 141 */       RemoteHost remoteHost = (RemoteHost)UnicastRemoteObject.exportObject((Remote)remoteHostImpl, 0);
/*     */       
/* 143 */       bind(stringBuilder.toString(), remoteHostImpl);
/* 144 */     } catch (MalformedURLException malformedURLException) {
/* 145 */       if (str != null) {
/* 146 */         System.out.println("Bad RMI server name: " + str);
/*     */       } else {
/* 148 */         System.out.println("Bad RMI URL: " + stringBuilder + " : " + malformedURLException
/* 149 */             .getMessage());
/*     */       } 
/* 151 */       System.exit(1);
/* 152 */     } catch (ConnectException connectException) {
/*     */       
/* 154 */       System.out.println("Could not contact RMI registry\n" + connectException
/* 155 */           .getMessage());
/* 156 */       System.exit(1);
/* 157 */     } catch (Exception exception) {
/* 158 */       System.out.println("Could not create remote object\n" + exception
/* 159 */           .getMessage());
/* 160 */       exception.printStackTrace();
/* 161 */       System.exit(1);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\jstatd\Jstatd.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */