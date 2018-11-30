/*     */ package com.ibm.ier.plugin.services.requestFilters;
/*     */ 
/*     */ import com.ibm.ecm.configuration.ApplicationConfig;
/*     */ import com.ibm.ecm.configuration.ConfigurationObject;
/*     */ import com.ibm.ecm.configuration.DesktopConfig;
/*     */ import com.ibm.ecm.configuration.RepositoryConfig;
/*     */ import com.ibm.ecm.configuration.exception.MissingValueException;
/*     */ import com.ibm.ier.plugin.configuration.Config;
/*     */ import com.ibm.ier.plugin.configuration.SettingsConfig;
/*     */ import com.ibm.ier.plugin.nls.MessageCode;
/*     */ import com.ibm.ier.plugin.nls.MessageResources;
/*     */ import com.ibm.ier.plugin.services.IERBaseRequestFilterService;
/*     */ import com.ibm.ier.plugin.util.SessionUtil;
/*     */ import com.ibm.jarm.api.meta.RMClassDescription;
/*     */ import com.ibm.jarm.api.meta.RMPropertyDescription;
/*     */ import com.ibm.json.java.JSONArray;
/*     */ import com.ibm.json.java.JSONArtifact;
/*     */ import com.ibm.json.java.JSONObject;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.apache.commons.configuration.ConfigurationException;
/*     */ 
/*     */ 
/*     */ public class ConfigurationFilter
/*     */   extends IERBaseRequestFilterService
/*     */ {
/*     */   private static final String APPLICATION = "application";
/*     */   private static final String ACTION = "action";
/*     */   private static final String GET = "get";
/*     */   private static final String SAVE = "save";
/*     */   private static final String DELETE = "delete";
/*     */   private static final String UPDATE = "update";
/*     */   private static final String LIST = "list";
/*     */   private static final String CONFIGURATION = "configuration";
/*     */   private static final String ID = "id";
/*     */   private static final String NAME = "name";
/*     */   private static final String JSON = "json_post";
/*     */   private static final String DELIM = ",";
/*     */   private static final String SETTINGS = "settings";
/*     */   private static final String DESKTOP = "desktop";
/*     */   private static final String REPOSITORY = "repository";
/*     */   private static final String DISPLAY_NAME = "DisplayName";
/*     */   private static final String DISPLAY_NAMES = "DisplayNames";
/*     */   private static final String LAYOUT = "ier.widget.layout.IERMainLayout";
/*     */   private static final String ADMIN_USERS = "adminUsers";
/*     */   private static final String SETTINGS_CONFIG = "SettingsConfig";
/*     */   
/*     */   public JSONObject filterServiceExecute(HttpServletRequest request, JSONArtifact jsonRequest)
/*     */     throws Exception
/*     */   {
/*  54 */     JSONObject jsonResponse = null;
/*  55 */     String application = request.getParameter("application");
/*  56 */     if ((application != null) && (application.equals("ier"))) {
/*  57 */       jsonResponse = new JSONObject();
/*  58 */       String action = request.getParameter("action");
/*  59 */       String name = request.getParameter("configuration");
/*  60 */       String id = request.getParameter("id");
/*  61 */       if ((action != null) && (name != null)) {
/*  62 */         if (action.equals("get")) {
/*  63 */           ConfigurationObject config = getConfig(name, id);
/*  64 */           if (config != null) {
/*  65 */             JSONObject jsonConfig = config.toJSON();
/*  66 */             jsonResponse.put("configuration", decorateJson(name, jsonConfig));
/*     */           }
/*  68 */         } else if (action.equals("save")) {
/*  69 */           ConfigurationObject config = getConfig(name, id);
/*  70 */           if (config != null) {
/*  71 */             JSONObject jsonConfig = JSONObject.parse(request.getParameter("json_post"));
/*  72 */             config.setValues(cleanseJson(name, jsonConfig), true);
/*  73 */             config.save();
/*  74 */             Config.removeConfigurationFromCache(config);
/*     */           }
/*  76 */         } else if (action.equals("delete")) {
/*  77 */           for (String i : id.split(",")) {
/*  78 */             ConfigurationObject config = getConfig(name, i);
/*  79 */             if (config != null) {
/*  80 */               config.delete();
/*  81 */               Config.removeConfigurationFromCache(config);
/*     */             }
/*     */           }
/*  84 */         } else if (action.equals("list")) {
/*  85 */           JSONArray jsonList = new JSONArray();
/*  86 */           for (ConfigurationObject config : listConfig(name)) {
/*  87 */             JSONObject jsonConfig = config.toJSON();
/*  88 */             jsonList.add(compactJson(name, jsonConfig));
/*     */           }
/*  90 */           jsonResponse.put("list", jsonList);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  97 */     if ((application != null) && (application.equals("navigator"))) {
/*  98 */       String action = request.getParameter("action");
/*  99 */       String name = request.getParameter("configuration");
/* 100 */       String jsonString = request.getParameter("json_post");
/* 101 */       if (jsonString != null) {
/*     */         try
/*     */         {
/* 104 */           if ((jsonString != null) && (name != null) && (name.equals("SettingsConfig")) && (action != null) && (action.equals("update")))
/*     */           {
/* 106 */             JSONObject jsonPost = JSONObject.parse(jsonString);
/* 107 */             String adminUsers = (String)jsonPost.get("adminUsers");
/* 108 */             if (adminUsers != null) {
/* 109 */               SettingsConfig config = Config.getSettingsConfig();
/* 110 */               config.setProperty("adminUsers", adminUsers);
/* 111 */               config.save();
/*     */             }
/*     */           }
/*     */         }
/*     */         catch (Exception exp) {}
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 121 */     return jsonResponse;
/*     */   }
/*     */   
/*     */   public String[] getFilteredServices() {
/* 125 */     return new String[] { "/admin/configuration" };
/*     */   }
/*     */   
/*     */   private ConfigurationObject getConfig(String name, String id) throws ConfigurationException, MissingValueException {
/* 129 */     ConfigurationObject config = null;
/* 130 */     if (name.equals("settings")) {
/* 131 */       config = Config.getSettingsConfig();
/* 132 */     } else if (name.equals("desktop")) {
/* 133 */       config = Config.getDesktopConfig(id);
/* 134 */     } else if (name.equals("repository")) {
/* 135 */       config = Config.getRepositoryConfig(id);
/*     */     }
/* 137 */     return config;
/*     */   }
/*     */   
/*     */   private Collection<ConfigurationObject> listConfig(String name) throws ConfigurationException, MissingValueException {
/* 141 */     Collection<ConfigurationObject> list = null;
/* 142 */     if (name.equals("desktop")) {
/* 143 */       list = new ArrayList();
/* 144 */       ApplicationConfig ac = Config.getApplicationConfig("navigator");
/* 145 */       for (DesktopConfig dc : ac.getDesktopList()) {
/* 146 */         if ("ier.widget.layout.IERMainLayout".equals(dc.getLayout())) {
/* 147 */           list.add(dc);
/*     */         }
/*     */       }
/* 150 */     } else if (name.equals("repository")) {
/* 151 */       Map<String, ConfigurationObject> map = new HashMap();
/* 152 */       ApplicationConfig ac = Config.getApplicationConfig("navigator");
/* 153 */       for (DesktopConfig dc : ac.getDesktopList()) {
/* 154 */         if ("ier.widget.layout.IERMainLayout".equals(dc.getLayout())) {
/* 155 */           for (RepositoryConfig rc : dc.getRepositoryList()) {
/* 156 */             map.put(rc.getObjectId(), rc);
/*     */           }
/*     */         }
/*     */       }
/* 160 */       list = map.values();
/*     */     }
/* 162 */     return list;
/*     */   }
/*     */   
/*     */   private JSONObject decorateJson(String name, JSONObject jsonConfig) {
/*     */     try {
/* 167 */       if (name.equals("repository")) {
/* 168 */         JSONObject newConfig = new JSONObject();
/* 169 */         for (Object k : jsonConfig.keySet()) {
/* 170 */           String key = (String)k;
/* 171 */           Object value = jsonConfig.get(k);
/* 172 */           String id = null;
/* 173 */           int index = key.indexOf("DisplayColumns");
/* 174 */           if (index > 0) {
/* 175 */             id = key.substring(0, index);
/*     */           } else {
/* 177 */             index = key.indexOf("DisplayProperties");
/* 178 */             if (index > 0) {
/* 179 */               id = key.substring(0, index);
/*     */             } else {
/* 181 */               index = key.indexOf("SystemProperties");
/* 182 */               if (index > 0)
/* 183 */                 id = key.substring(0, index);
/*     */             } }
/*     */           RMClassDescription cd;
/*     */           JSONObject names;
/* 187 */           if (id != null) { JSONArray jsonArray;
/*     */             JSONArray jsonArray;
/* 189 */             if ((value instanceof JSONArray)) {
/* 190 */               jsonArray = (JSONArray)value;
/* 191 */             } else { if ((!(value instanceof String)) || (((String)value).length() <= 0))
/*     */                 continue;
/* 193 */               jsonArray = new JSONArray();
/* 194 */               jsonArray.add(value);
/* 195 */               value = jsonArray;
/*     */             }
/*     */             
/*     */ 
/* 199 */             cd = SessionUtil.getClassDescription(getRepository(), id, this.servletRequest);
/* 200 */             if (cd != null) {
/* 201 */               String n = id + "DisplayName";
/* 202 */               if (newConfig.get(n) == null) {
/* 203 */                 newConfig.put(n, cd.getDisplayName());
/*     */               }
/* 205 */               n = id + "DisplayNames";
/* 206 */               names = (JSONObject)newConfig.get(n);
/* 207 */               if (names == null) {
/* 208 */                 names = new JSONObject();
/* 209 */                 newConfig.put(n, names); }
/*     */               HashMap<String, String> map;
/* 211 */               if (id.equals("RMFolder")) {
/* 212 */                 map = new HashMap();
/* 213 */                 map.put("{NAME}", MessageResources.getLocalizedMessage(MessageCode.NAME, new Object[0]));
/* 214 */                 map.put("ContainerId", MessageResources.getLocalizedMessage(MessageCode.CONTAINER_ID, new Object[0]));
/* 215 */                 for (RMPropertyDescription pd : cd.getPropertyDescriptions()) {
/* 216 */                   if (!pd.isHidden()) {
/* 217 */                     map.put(pd.getSymbolicName(), pd.getDisplayName());
/*     */                   }
/*     */                 }
/* 220 */                 for (RMPropertyDescription pd : cd.getAllDescendentPropertyDescriptions()) {
/* 221 */                   if (!pd.isHidden()) {
/* 222 */                     map.put(pd.getSymbolicName(), pd.getDisplayName());
/*     */                   }
/*     */                 }
/* 225 */                 for (Object v : jsonArray) {
/* 226 */                   if (names.get(v) == null) {
/* 227 */                     String dn = (String)map.get(v);
/* 228 */                     if (dn != null) {
/* 229 */                       names.put(v, dn);
/*     */                     }
/*     */                   }
/*     */                 }
/*     */               } else {
/* 234 */                 for (Object v : jsonArray) {
/* 235 */                   if (names.get(v) == null) {
/* 236 */                     RMPropertyDescription pd = cd.getPropertyDescription((String)v);
/* 237 */                     if (pd != null) {
/* 238 */                       names.put(v, pd.getDisplayName());
/*     */                     }
/*     */                   }
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/* 245 */           newConfig.put(key, value);
/*     */         }
/* 247 */         jsonConfig = newConfig;
/*     */       }
/*     */     } catch (Exception e) {
/* 250 */       e.printStackTrace();
/*     */     }
/* 252 */     return jsonConfig;
/*     */   }
/*     */   
/*     */   private JSONObject cleanseJson(String name, JSONObject jsonConfig) {
/*     */     try {
/* 257 */       if (name.equals("repository")) {
/* 258 */         JSONObject newConfig = new JSONObject();
/* 259 */         for (Object k : jsonConfig.keySet()) {
/* 260 */           String key = (String)k;
/* 261 */           if (key.indexOf("DisplayName") < 0) {
/* 262 */             newConfig.put(k, jsonConfig.get(k));
/*     */           }
/*     */         }
/* 265 */         jsonConfig = newConfig;
/*     */       }
/*     */     } catch (Exception e) {
/* 268 */       e.printStackTrace();
/*     */     }
/* 270 */     return jsonConfig;
/*     */   }
/*     */   
/*     */   private JSONObject compactJson(String name, JSONObject jsonConfig) {
/*     */     try {
/* 275 */       JSONObject newConfig = new JSONObject();
/* 276 */       newConfig.put("id", jsonConfig.get("id"));
/* 277 */       newConfig.put("name", jsonConfig.get("name"));
/* 278 */       jsonConfig = newConfig;
/*     */     } catch (Exception e) {
/* 280 */       e.printStackTrace();
/*     */     }
/* 282 */     return jsonConfig;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\requestFilters\ConfigurationFilter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */