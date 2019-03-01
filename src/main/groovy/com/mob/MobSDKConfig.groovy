package com.mob

import com.mob.products.*
import com.mob.products.sharesdk.DevInfo
import org.gradle.api.Action
import org.gradle.api.Project

class MobSDKConfig extends DObject {
	Project project

	void setProject(Project project) {
		hiddenMembers {
			enableWrapper = true
			config = new ConfigCreator()
			MobTools = new MobToolsConfig()
			MobCommons = new MobCommonsConfig()
			SDKWrapper = new SDKWrapperConfig()
			permissions = new PermissionConfig()

			ShareSDK = new ShareSDKConfig()
			SMSSDK = new SMSSDKConfig()
			MobVerify = new MobVerifyConfig()
			AWK = new AWKConfig()
			ShareREC = new ShareRECConfig()
			MobAPI = new MobAPIConfig()
			MobLink = new MobLinkConfig()
			BBSSDK = new BBSSDKConfig()
			UMSSDK = new UMSSDKConfig()
			CMSSDK = new CMSSDKConfig()
			AnalySDK = new AnalySDKConfig()
			PaySDK = new PaySDKConfig()
			MobPush = new MobPushConfig()
			ShopSDK = new ShopSDKConfig()
			MobIM = new MobIMConfig()
		}

		this.project = project
		config.project = project
		ShareSDK.configCreator = config
		MobPush.configCreator = config
		MobLink.configCreator = config
		BBSSDK.configCreator = config
		PaySDK.configCreator = config
		permissions.configCreator = config
		config.create()
		processMobCommons()
	}

	// ======================================

	def methodMissing(String name, def args) {
		if ("appKey".equalsIgnoreCase(name)) {
			this.appKey(args[0])
		} else if ("appSecret".equalsIgnoreCase(name)) {
			this.appSecret(args[0])
		} else if ("wrapper".equalsIgnoreCase(name)) {
			if (args == null) {
				enableWrapper = true
			} else if (args.length == 0) {
				enableWrapper = true
			} else {
				enableWrapper = "true".equalsIgnoreCase(String.valueOf(args[0]))
			}
		} else if ("autoConfig".equalsIgnoreCase(name)) {
			if (args == null) {
				config.autoConfig = true
			} else if (args.length == 0) {
				config.autoConfig = true
			} else {
				config.autoConfig = "true".equalsIgnoreCase(String.valueOf(args[0]))
			}
		} else if ("domain".equalsIgnoreCase(name) || "defaultDomain".equalsIgnoreCase(name)) {
			config.domain = args[0]
		} else if ("updateCacheDynamicVersionsFor".equalsIgnoreCase(name)) {
			try {
				int seconds = Integer.parseInt(String.valueOf(args[0]))
				config.updateCacheDynamicVersionsFor = seconds
			} catch (Throwable t) {}
		} else if ("Permissions".equalsIgnoreCase(name)) {
			permissions.closure = args[0]
		} else if ("MobTools".equalsIgnoreCase(name) || "tools".equalsIgnoreCase(name)) {
			this.MobTools.closure = args[0]
			processMobTools()
		} else if ("MobGUI".equalsIgnoreCase(name) || "gui".equalsIgnoreCase(name)) {
			if (args == null) {
				processMobTools()
				addDependencies('com.mob:MobGUI:' + MobTools.version)
			} else if (args.length == 0) {
				processMobTools()
				addDependencies('com.mob:MobGUI:' + MobTools.version)
			} else if ("true".equals(String.valueOf(args[0]).toLowerCase())) {
				processMobTools()
				addDependencies('com.mob:MobGUI:' + MobTools.version)
			}
		} else if ("MobCommons".equalsIgnoreCase(name) || "commons".equalsIgnoreCase(name) || "comm".equalsIgnoreCase(name)) {
			this.MobCommons.closure = args[0]
			processMobCommons()
		} else if ("SDKWrapper".equalsIgnoreCase(name)) {
			this.SDKWrapper.closure = args[0]
			processSDKWrapper()
		} else if ("CloudStorage".equalsIgnoreCase(name)) {
			CoreProductConfig config = new CoreProductConfig()
			config.closure = args[0]
			if (config.version == null) {
				config.version = '+'
			}
			processMobCommons()
			addDependencies('com.mob:CloudStorage:' + config.version)
		} else if ("java8".equalsIgnoreCase(name) || "java8Support".equalsIgnoreCase(name)) {
			java8Support(args[0])
		} else if ("ShareSDK".equalsIgnoreCase(name) || "share".equalsIgnoreCase(name) || "login".equalsIgnoreCase(name)) {
			processShareSDK(args[0])
		} else if ("SMSSDK".equalsIgnoreCase(name) || "sms".equalsIgnoreCase(name)) {
			processSMSSDK(args[0])
		} else if ("MobVerify".equalsIgnoreCase(name) || "verify".equalsIgnoreCase(name)) {
			processMobVerify(args[0])
		} else if ("AWK".equalsIgnoreCase(name) || "awaker".equalsIgnoreCase(name)) {
			processAWK(args[0])
		} else if ("ShareREC".equalsIgnoreCase(name) || "rec".equalsIgnoreCase(name)) {
			processShareREC(args[0])
		} else if ("MobAPI".equalsIgnoreCase(name) || "api".equalsIgnoreCase(name)) {
			processMobAPI(args[0])
		} else if ("MobLink".equalsIgnoreCase(name) || "link".equalsIgnoreCase(name)) {
			processMobLink(args[0])
		} else if ("BBSSDK".equalsIgnoreCase(name) || "bbs".equalsIgnoreCase(name)) {
			processBBSSDK(args[0])
		} else if ("UMSSDK".equalsIgnoreCase(name) || "ums".equalsIgnoreCase(name) || "user".equalsIgnoreCase(name)) {
			processUMSSDK(args[0])
		} else if ("CMSSDK".equalsIgnoreCase(name) || "cms".equalsIgnoreCase(name) || "news".equalsIgnoreCase(name)) {
			processCMSSDK(args[0])
		} else if ("AnalySDK".equalsIgnoreCase(name) || "analy".equalsIgnoreCase(name)) {
			processAnalySDK(args[0])
		} else if ("PaySDK".equalsIgnoreCase(name) || "pay".equalsIgnoreCase(name) || "MobPay".equalsIgnoreCase(name)) {
			processPaySDK(args[0])
		} else if ("MobPush".equalsIgnoreCase(name) || "push".equalsIgnoreCase(name)) {
			processMobPush(args[0])
		} else if ("ShopSDK".equalsIgnoreCase(name) || "shop".equalsIgnoreCase(name)) {
			processShopSDK(args[0])
		} else if ("MobIM".equalsIgnoreCase(name) || "im".equalsIgnoreCase(name) || "chat".equalsIgnoreCase(name)) {
			processMobIM(args[0])
		} else if ("GrowSolution".equalsIgnoreCase(name) || "grow".equalsIgnoreCase(name)) {
			MobSDKSolution solution = project.extensions.getByName("MobSDKSolution")
			solution.hmGrowSolution(args[0])
		}
		return null
	}

	void appKey(String appkey) {
		config.appkey = appkey
	}

	void appSecret(String appSecret) {
		config.appSecret = appSecret
	}

	void java8Support(boolean enable) {
		if (enable) {
			processMobTools()
			addDependencies('com.mob:Java8Suport:' + MobTools.version)
		}
	}

	protected void addDependencies(String lib) {
		String version = config.gradlePluginVersion
		if (version == null || version.startsWith("3.")) {
			project.dependencies {
				add('implementation', lib, {
					transitive = true
				})
			}
		} else {
			project.dependencies {
				add('compile', lib, {
					transitive = true
				})
			}
		}
	}

	protected def getConfigCreator() {
		return config
	}

	private void addPermission(Set permissions) {
		config.permissions.addAll(permissions)
	}

	private void processMobTools() {
		if (MobTools.version == null) {
			MobTools.version = "+"
		}
		addDependencies('com.mob:MobTools:' + MobTools.version)
		addPermission(MobTools.getPermission())
	}

	private void processMobCommons() {
		processMobTools()
		if (MobCommons.version == null) {
			MobCommons.version = "+"
		}
		addDependencies('com.mob:MobCommons:' + MobCommons.version)
		addPermission(MobCommons.getPermission())
	}

	private void processSDKWrapper() {
		processMobCommons()
		addDependencies('com.mob:MobGUI:' + MobTools.version)
		if (enableWrapper) {
			if (SDKWrapper.version == null) {
				SDKWrapper.version = "+"
			}
			addDependencies('com.mob:SDKWrapper:' + SDKWrapper.version)
			addPermission(SDKWrapper.getPermission())
		}
	}

	private void execConfig(Object arg, MobProductConfig config) {
		if (arg instanceof Action) {
			arg.execute(config)
		} else {
			config.closure = arg
		}
		processSDKWrapper()
		if (config.version == null) {
			config.version = "+"
		}
		addPermission(config.permission)
	}

	void ShareSDK(Action<ShareSDKConfig> config) {
		processShareSDK(config)
	}

	private void processShareSDK(Object arg) {
		execConfig(arg, ShareSDK)
		def v = ShareSDK.version

		addDependencies('cn.sharesdk:ShareSDK:' + v + '@aar')
		config.sharesdkInfoMap.each { platform, info ->
			if (info != null) {
				DevInfo.jarNames.get(platform).each { jar ->
					if ("FourSquare".equals(jar)) {
						jar = "Foursquare"
					} else if ("LinkedIn".equals(jar)) {
						jar = "Linkedin"
					}
					String version = config.gradlePluginVersion
					if (version == null || version.startsWith("3.")) {
						project.dependencies {
							add('implementation', "cn.sharesdk:ShareSDK-${jar}:${v}")
						}
					} else {
						project.dependencies {
							add('compile', "cn.sharesdk:ShareSDK-${jar}:${v}")
						}
					}
				}
			}
		}
		if (ShareSDK.gui) {
			addDependencies('cn.sharesdk:OneKeyShare:' + v + '@aar')
		}
	}

	void SMSSDK(Action<SMSSDKConfig> config) {
		processSMSSDK(config)
	}

	private void processSMSSDK(Object arg) {
		execConfig(arg, SMSSDK)
		addDependencies('cn.smssdk:SMSSDK:' + SMSSDK.version + '@aar')
		if (SMSSDK.gui) {
			addDependencies('cn.smssdk:ShortMessageSDKGUI:' + SMSSDK.version + '@aar')
		}
	}

	void MobVerify(Action<MobVerifyConfig> config) {
		processMobVerify(config)
	}

	private void processMobVerify(Object arg) {
		execConfig(arg, MobVerify)
		addDependencies('com.mob:MobVerify:' + MobVerify.version + '@aar')
		if (MobVerify.gui) {
			addDependencies('com.mob:MobVerifyGUI:' + MobVerify.version + '@aar')
		}
	}

	void AWK(Action<AWKConfig> config) {
		processAWK(config)
	}

	private void processAWK(Object arg) {
		execConfig(arg, MobVerify)
		if (AWK.version == null) {
			AWK.version = "+"
		}
		addDependencies('com.awstar:AWK:' + AWK.version)
	}

	void ShareREC(Action<ShareRECConfig> config) {
		processShareREC(config)
	}

	private void processShareREC(Object arg) {
		execConfig(arg, ShareREC)
		addDependencies('cn.sharerec:ShareREC-Core:' + ShareREC.version + '@aar')
		if (ShareREC.gui) {
			if (ShareREC.community) {
				addDependencies('cn.sharerec:ShareREC-VideoEditor:' + ShareREC.version + '@aar')
				addDependencies('cn.sharerec:ShareREC-VideoUploader:' + ShareREC.version + '@aar')
				addDependencies('cn.sharerec:ShareREC:' + ShareREC.version + '@aar')
			} else if (ShareREC.uploader) {
				addDependencies('cn.sharerec:ShareREC-VideoEditor:' + ShareREC.version + '@aar')
				addDependencies('cn.sharerec:ShareREC-VideoUploader:' + ShareREC.version + '@aar')
			} else if (ShareREC.editor) {
				addDependencies('cn.sharerec:ShareREC-VideoEditor:' + ShareREC.version + '@aar')
			}
		}
	}

	void MobAPI(Action<MobAPIConfig> config) {
		processMobAPI(config)
	}

	private void processMobAPI(Object arg) {
		execConfig(arg, MobAPI)
		addDependencies('com.mob:mobAPI:' + MobAPI.version + '@aar')
	}

	void MobLink(Action<MobLinkConfig> config) {
		processMobLink(config)
	}

	private void processMobLink(Object arg) {
		execConfig(arg, MobLink)
		MobLink.addHosts()
		if (MobLink.preferClassicalEdition) {
			addDependencies('com.mob:MobLink-Classical:' + MobLink.version + '@aar')
		} else {
			addDependencies('com.mob:MobLink:' + MobLink.version + '@aar')
		}
	}

	void BBSSDK(Action<BBSSDKConfig> config) {
		processBBSSDK(config)
	}

	private void processBBSSDK(Object arg) {
		execConfig(arg, BBSSDK)
		boolean add3rdLibs = false
		boolean addNewsPackage = false
		int code = 0
		String version = config.gradlePluginVersion
		addDependencies('com.mob:BBSSDK:' + BBSSDK.version + '@aar')
		if (!"+".equals(BBSSDK.version)) {
			String[] parts = BBSSDK.version.split("\\.")
			for (int i = 0; i < 3; i++) {
				code = code * 100 + Integer.parseInt(parts[i])
			}
			if (code < 20200) {
				add3rdLibs = true
			} else if (code < 20401) {
				addNewsPackage = true
			}
		}
		if (BBSSDK.gui) {
			if (add3rdLibs) {
				project.repositories.jcenter()
				if (version == null || version.startsWith("3.")) {
					project.dependencies {
						add('implementation', 'org.apache.commons:commons-csv:1.4')
					}
					project.dependencies {
						add('implementation', 'com.android.support:support-v4:23.2.0')
					}
					project.dependencies {
						add('implementation', 'jp.wasabeef:glide-transformations:2.0.2')
					}
					project.dependencies {
						add('implementation', 'com.github.bumptech.glide:glide:3.8.0')
					}
					project.dependencies {
						add('implementation', 'pl.droidsonroids.gif:android-gif-drawable:1.2.8')
					}
				} else {
					project.dependencies {
						add('compile', 'org.apache.commons:commons-csv:1.4')
					}
					project.dependencies {
						add('compile', 'com.android.support:support-v4:23.2.0')
					}
					project.dependencies {
						add('compile', 'jp.wasabeef:glide-transformations:2.0.2')
					}
					project.dependencies {
						add('compile', 'com.github.bumptech.glide:glide:3.8.0')
					}
					project.dependencies {
						add('compile', 'pl.droidsonroids.gif:android-gif-drawable:1.2.8')
					}
				}
				addDependencies('com.mob:BBSSDKGUI:' + BBSSDK.version + '@aar')
				addDependencies('com.mob:Jimu:+@aar')
				if (BBSSDK.theme != null) {
					if ("theme0".equals(BBSSDK.theme)) {
						addDependencies('com.mob:BBSSDKTHEME0:' + BBSSDK.version + '@aar')
					} else if ("theme1".equals(BBSSDK.theme)) {
						if (version == null || version.startsWith("3.")) {
							project.dependencies {
								add('implementation', 'jp.wasabeef:blurry:2.1.1')
							}
							project.dependencies {
								add('implementation', 'com.android.support:appcompat-v7:23.2.0')
							}
						} else {
							project.dependencies {
								add('compile', 'jp.wasabeef:blurry:2.1.1')
							}
							project.dependencies {
								add('compile', 'com.android.support:appcompat-v7:23.2.0')
							}
						}
						addDependencies('com.mob:BBSSDKTHEME0:' + BBSSDK.version + '@aar')
						addDependencies('com.mob:BBSSDKTHEME1:' + BBSSDK.version + '@aar')
					}
				}
			} else {
				addDependencies('com.mob:BBSSDKGUI:' + BBSSDK.version + '@aar')
				if (BBSSDK.theme == null) {
					if (addNewsPackage) {
						if ("theme0".equals(BBSSDK.newsTheme)) {
							addDependencies('com.mob:BBSSDKTHEME0NEWS:' + BBSSDK.version + '@aar')
						} else if ("theme1".equals(BBSSDK.newsTheme)) {
							addDependencies('com.mob:BBSSDKTHEME1NEWS:' + BBSSDK.version + '@aar')
						}
					}
				} else if ("theme0".equals(BBSSDK.theme)) {
					if (addNewsPackage) {
						addDependencies('com.mob:BBSSDKTHEME0NEWS:' + BBSSDK.version + '@aar')
						if ("theme1".equals(BBSSDK.newsTheme)) {
							addDependencies('com.mob:BBSSDKTHEME1NEWS:' + BBSSDK.version + '@aar')
						}
					}
					addDependencies('com.mob:BBSSDKTHEME0:' + BBSSDK.version + '@aar')
				} else if ("theme1".equals(BBSSDK.theme)) {
					if (addNewsPackage) {
						addDependencies('com.mob:BBSSDKTHEME0NEWS:' + BBSSDK.version + '@aar')
						addDependencies('com.mob:BBSSDKTHEME1NEWS:' + BBSSDK.version + '@aar')
					}
					addDependencies('com.mob:BBSSDKTHEME0:' + BBSSDK.version + '@aar')
					addDependencies('com.mob:BBSSDKTHEME1:' + BBSSDK.version + '@aar')
				}
			}
		}
	}

	void UMSSDK(Action<UMSSDKConfig> config) {
		processUMSSDK(config)
	}

	private void processUMSSDK(Object arg) {
		execConfig(arg, UMSSDK)
		addDependencies('com.mob:Jimu:+@aar')
		addDependencies('com.mob:UMSSDK:' + UMSSDK.version + '@aar')
		if (UMSSDK.gui) {
			addDependencies('com.mob:UMSSDKGUI:' + UMSSDK.version + '@aar')
		}
	}

	void CMSSDK(Action<CMSSDKConfig> config) {
		processCMSSDK(config)
	}

	private void processCMSSDK(Object arg) {
		execConfig(arg, CMSSDK)
		addDependencies('com.mob:Jimu:+@aar')
		addDependencies('com.mob:CMSSDK:' + CMSSDK.version + '@aar')
		if (CMSSDK.gui) {
			addDependencies('com.mob:CMSSDKGUI:' + CMSSDK.version + '@aar')
		}
	}

	void AnalySDK(Action<AnalySDKConfig> config) {
		processAnalySDK(config)
	}

	private void processAnalySDK(Object arg) {
		execConfig(arg, AnalySDK)
		addDependencies('com.mob:AnalySDK:' + AnalySDK.version + '@aar')
	}

	void PaySDK(Action<PaySDKConfig> config) {
		processPaySDK(config)
	}

	private void processPaySDK(Object arg) {
		execConfig(arg, PaySDK)
		addDependencies('com.mob:PaySDK:' + PaySDK.version + '@aar')
	}

	void MobPush(Action<MobPushConfig> config) {
		processMobPush(config)
	}

	private void processMobPush(Object arg) {
		execConfig(arg, MobPush)
		addDependencies('com.mob:MobPush:' + MobPush.version + '@aar')
		MobPush.addMobService()
	}

	void ShopSDK(Action<ShopSDKConfig> config) {
		processShopSDK(config)
	}

	private void processShopSDK(Object arg) {
		execConfig(arg, ShopSDK)
		addDependencies('com.mob:ShopSDK:' + ShopSDK.version + '@aar')
		if (ShopSDK.gui) {
			addDependencies('com.mob:ShopGUI:' + ShopSDK.version + '@aar')
		}
	}

	void MobIM(Action<MobIMConfig> config) {
		processMobIM(config)
	}

	private void processMobIM(Object arg) {
		execConfig(arg, MobIM)
		addDependencies('com.mob:MobIM:' + MobIM.version + '@aar')
	}

}