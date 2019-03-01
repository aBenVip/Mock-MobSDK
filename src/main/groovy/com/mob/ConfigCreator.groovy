package com.mob

import groovy.xml.XmlUtil
import org.gradle.api.Project

import java.lang.reflect.Field
import java.util.zip.ZipEntry
import java.util.zip.ZipFile

class ConfigCreator {
	Project project

	void setProject(Project project) {
		this.project = project
		project.configurations.all {
			resolutionStrategy.cacheDynamicVersionsFor 60 * 10, 'seconds'
			resolutionStrategy.cacheChangingModulesFor 60 * 60, 'seconds'
		}
	}

	Project getProject() {
		return project
	}

	private Map getGlobalVariants() {
		boolean found = false
		try {
			def fld = project.rootProject.getClass().getDeclaredField("mobsdk_global_variants")
			found = fld != null
		} catch (Throwable t) {}
		if (!found) {
			project.rootProject.metaClass.mobsdk_global_variants = [:]
		}
		return project.rootProject.mobsdk_global_variants
	}

	void setAppkey(String appkey) {
		globalVariants.appkey = appkey
	}

	void setAppSecret(String appSecret) {
		globalVariants.appSecret = appSecret
	}

	void setAutoConfig(boolean autoConfig) {
		globalVariants.autoConfig = autoConfig
	}

	Set getActivitiesToAdd() {
		Set activitiesToAdd = globalVariants.activitiesToAdd
		if (activitiesToAdd == null) {
			activitiesToAdd = []
			globalVariants.activitiesToAdd = activitiesToAdd
		}
		return activitiesToAdd
	}

	Set getIntents() {
		Set intents = globalVariants.intents
		if (intents == null) {
			intents = []
			globalVariants.intents = intents
		}
		return intents
	}

	Set getDirectToAdd() {
		Set directToAdd = globalVariants.directToAdd
		if (directToAdd == null) {
			directToAdd = []
			globalVariants.directToAdd = directToAdd
		}
		return directToAdd
	}

	Map getSharesdkInfoMap() {
		Map sharesdkInfoMap = globalVariants.sharesdkInfoMap
		if (sharesdkInfoMap == null) {
			sharesdkInfoMap = [:]
			globalVariants.sharesdkInfoMap = sharesdkInfoMap
		}
		return sharesdkInfoMap
	}

	Set getPermissions() {
		Set permissions = globalVariants.permissions
		if (permissions == null) {
			permissions = []
			globalVariants.permissions = permissions
		}
		return permissions
	}

	Set getFeatures() {
		Set features = globalVariants.features
		if (features == null) {
			features = []
			globalVariants.features = features
		}
		return features
	}

	Set getCustomPermissions() {
		Set permissions = globalVariants.customPermissions

		if (permissions == null) {
			permissions = []
			globalVariants.customPermissions = permissions
		}
		return permissions
	}

	Set getExcludePermissions() {
		Set permissions = globalVariants.excludePermissions
		if (permissions == null) {
			permissions = []
			globalVariants.excludePermissions = permissions
		}
		return permissions
	}

	void setUpdateCacheDynamicVersionsFor(int seconds) {
		if (seconds < 0 || seconds > 86400) {
			seconds = 60 * 10
		}

		project.configurations.all {
			resolutionStrategy.cacheDynamicVersionsFor seconds, 'seconds'
			resolutionStrategy.cacheChangingModulesFor 60 * 60, 'seconds'
		}
	}

	String getGradlePluginVersion() {
		String version = getRealGradlePluginVersion()
		if(version != null && version.length() > 0){
			if (!Character.isDigit(version.charAt(0))){
				version = "3."
			} else{
				int startNum = Integer.valueOf(version.charAt(0).toString())
				if(startNum > 3){
					version = "3."
				}
			}
		}
		return version
	}

	String getRealGradlePluginVersion() {
		String version = globalVariants.gradlePluginVersion
		if (version == null) {
			try{
				// 反射去拿插件版本号
				Class myClass = Class.forName("com.android.build.gradle.internal.Version")
				Object obj = myClass.newInstance()
				Field field = myClass.getField("ANDROID_GRADLE_PLUGIN_VERSION")
				version = field.get(obj).toString()
			} catch (Throwable t){
				//反射拿不到就读文件
				try{
					Class clz = Class.forName("com.android.build.gradle.AppPlugin")
					URL url = clz.getProtectionDomain().getCodeSource().getLocation()
					File file = new File(url.toURI())
					ZipFile zip = new ZipFile(file)
					ZipEntry ent = zip.getEntry("META-INF/MANIFEST.MF")
					InputStream is = zip.getInputStream(ent)
					Properties prop = new Properties()
					prop.load(is)
					is.close()
					zip.close()
					version = prop.getProperty("Plugin-Version")

					if ("unspecified".equals(version)) {
						String name = file.name.toLowerCase()
						if (name.startsWith("gradle") && name.endsWith(".jar") && name.concat("-")) {
							int index = name.indexOf("-")
							version = name.substring(index + 1, name.length() - 4)
						}
					}
				} catch (Throwable tt){}
			}
			globalVariants.gradlePluginVersion = version
		}
		return version
	}

	Map getApplicationMetaData() {
		Map metaData = globalVariants.applicationMetaData
		if (metaData == null) {
			metaData = [:]
			globalVariants.applicationMetaData = metaData
		}
		return metaData
	}

	void setDisableShareSDKConfig(boolean disable) {
		globalVariants.disableShareSDKConfig = disable
	}

	void setPlaceShareSDKConfigHere(String path) {
		Map map = [:]
		map.project = project.projectDir
		map.path = path
		globalVariants.placeShareSDKConfigHere = map
	}

	boolean isDisableShareSDKConfig() {
		return "true".equalsIgnoreCase(String.valueOf(globalVariants.disableShareSDKConfig))
	}

	void setDomain(String domain) {
		globalVariants.domain = domain
	}

	void setMobLinkUri(String uri) {
		globalVariants.mobLinkUri = uri
	}

	void setMobLinkAppLinkHost(String host) {
		globalVariants.mobLinkAppLinkHost = host
	}

	void setMobLinkAppLinkAutoVerify(boolean autoVerify) {
		globalVariants.mobLinkAppLinkAutoVerify = autoVerify
	}

	// ===========================

	void create() {
		project.afterEvaluate {
			def android = project.extensions.getByName("android")
			if (globalVariants.autoConfig == null) {
				globalVariants.autoConfig = true
			}
			if (globalVariants.autoConfig) {
				if (android != null) {
					print("rexih configShareSDKXML")
					configShareSDKXML(android)

					def variants = null
					boolean appModel = false
					try {
						variants = android.applicationVariants
						appModel = true
					} catch (Throwable t) {
						try {
							variants = android.libraryVariants
						} catch (Throwable tt) {}
					}
					if (variants != null) {
						variants.all { variant ->
							variant.outputs.each { output ->
                                try {
                                    def task = getProcessManifestTask(output)
                                    if (task != null) {
                                        task.doLast {
                                            configManifest(output, appModel, variant)
                                        }
                                    }
                                } catch (Throwable t){}
							}
						}
					}
				}
			}
		}
	}

	private File findAssetsDir(def android, String targetFile) {
		def assetsDir = null
		def assetsDirs = android.sourceSets.main.assets.srcDirs
		assetsDirs.each { dir ->
			dir.list().each { name ->
				if (targetFile.equals(name)) {
					assetsDir = dir
					new File(dir, name).delete()
					return
				}
			}
		}

		if (assetsDir == null) {
			def map = globalVariants.placeShareSDKConfigHere
			def cproj = null
			def cpath = null
			if (map != null) {
				try {
					cproj = map.project
					cpath = map.path
				} catch (Throwable t) {}
			}
			if (cproj != null) {
				String assetsFolder = cpath == null ? "assets" : cpath
				assetsDir = new File(cproj, assetsFolder)
			} else {
				String assetsFolder = "intermediates${File.separator}Mob${File.separator}assets"
				assetsDir = new File(project.rootProject.buildDir, assetsFolder)
			}
			if (assetsDirs.size() > 0) {
				HashSet s = new HashSet()
				s.addAll(assetsDirs)
				s.add(assetsDir)
				android.sourceSets.main.assets.srcDirs = s
			} else {
				android.sourceSets.main.assets.srcDirs = [assetsDir]
			}
		}

		if (!assetsDir.exists()) {
			assetsDir.mkdirs()
		}
		return assetsDir
	}

	// 将配置中的devInfo添加到ShareSDK.xml中
	private void configShareSDKXML(def android) {
		boolean disable = isDisableShareSDKConfig()
		Map sharesdkInfoMap = globalVariants.sharesdkInfoMap
		if (sharesdkInfoMap != null && !sharesdkInfoMap.isEmpty()) {
			def sb = new StringBuilder()
			sharesdkInfoMap.each { platform, info ->
				if (info == null || info.isEmpty()) {
					if (!disable) {
						sb.append("<").append(platform).append(" ")
						sb.append("Enable=\"false\" />\n")
					}
				} else {
					sb.append("<").append(platform).append(" ")
					info.each {key, value ->
						sb.append(key).append("=\"").append(value).append("\"").append(" ")
					}
					sb.append("/>\n")
				}
			}

			if (sb.length() > 0) {
				StringBuilder xml = new StringBuilder()
				xml.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n")
				xml.append("<DevInfor>\n")
				xml.append(sb)
				xml.append("</DevInfor>")
				File assetsDir = findAssetsDir(android, "ShareSDK.xml")
				File xmlFile = new File(assetsDir, "ShareSDK.xml")
				xmlFile.setText(xml.toString(), "UTF-8")
			}
		}
	}

	// 修改AndroidManifest.xml
	private void configManifest(def output, boolean appModel, def variant) {
		String packageName = null
		if (appModel) {
			try {
				packageName = variant.applicationId
			} catch (Throwable t) {}
		}

		def manifestFiles = getManifestOutputFile(output, appModel)

		manifestFiles.each { manifestFile->
			if (manifestFile != null && manifestFile.exists()) {
				def parser = new XmlSlurper()
				def manifest = parser.parse(manifestFile)

				// 尝试自动加入权限列表
				def usedPermissions = []
				manifest.'uses-permission'.each { permission ->
					usedPermissions.add(permission.getProperty('@android:name').toString())
				}
				def shouldAdd = []
				Set permissions = globalVariants.permissions
				if (permissions != null) {
					Set excludePermissions = globalVariants.excludePermissions
					if (excludePermissions == null) {
						excludePermissions = []
					}
					permissions.each { permission ->
						if (!usedPermissions.contains(permission) && !excludePermissions.contains(permission)) {
							shouldAdd.add(permission)
						}
					}
				}
				def ns = 'xmlns:android="http://schemas.android.com/apk/res/android"'
				shouldAdd.each { per ->
					String lastPermission = "<uses-permission ${ns} android:name=\"${per}\" />"
					if (packageName != null && lastPermission.contains('${applicationId}')) {
						lastPermission = lastPermission.replace('${applicationId}', packageName)
					}
					def permission = parser.parseText(lastPermission)
					manifest.appendNode(permission)
				}

				// 尝试加入特性列表
				def usedFeatures = []
				manifest.'uses-feature'.each { feature ->
					usedFeatures.add(feature.getProperty('@android:name').toString())
				}
				shouldAdd = []
				Set features = globalVariants.features
				if (features != null) {
					features.each { feature ->
						if (!usedFeatures.contains(feature)) {
							shouldAdd.add(feature)
						}
					}
				}
				shouldAdd.each { fea ->
					def feature = parser.parseText("<uses-feature ${ns} android:name=\"${fea}\" />")
					manifest.appendNode(feature)
				}

				//尝试加入自定义权限（仅针对push）
				def coustomPermissions = []
				manifest.'permission'.each { permission ->
					coustomPermissions.add(permission.getProperty('@android:name').toString())
				}
				def shouldAddCoustom = []
				Set permissionsCoustom = globalVariants.customPermissions
				if (permissionsCoustom != null) {
					permissionsCoustom.each { permission ->
						if (!coustomPermissions.contains(permission)) {
							shouldAddCoustom.add(permission)
						}
					}
				}
				def nsCustom = 'xmlns:android="http://schemas.android.com/apk/res/android"'
				def level = 'android:protectionLevel="signature"'
				shouldAddCoustom.each { per ->
					String lastPermission = "<permission ${nsCustom} android:name=\"${per}\" ${level}/>"
					if (packageName != null && lastPermission.contains('${applicationId}')) {
						lastPermission = lastPermission.replace('${applicationId}', packageName)
					}
					def permission = parser.parseText(lastPermission)
					manifest.appendNode(permission)
				}

				// 尝试替换application为MobApplication
				def app = manifest.'application'[0]
				if (app == null || app.isEmpty()) {
					def appNode = """
							<application
									xmlns:android="http://schemas.android.com/apk/res/android"
									xmlns:tools="http://schemas.android.com/tools"
									tools:node="merge"
									android:name="com.mob.MobApplication"/>
							"""
					manifest.appendNode(parser.parseText(appNode))
					app = manifest.'application'[0]
				}

				def name = app.@'android:name'.toString()
				if (name == null || name.length() == 0) {
					app.@'android-name____' = "com.mob.MobApplication"
					def xml = XmlUtil.serialize(manifest)
					manifest = parser.parseText(xml.replace("android-name____", "android:name"))
					app = manifest.'application'[0]
				} else {
					String prefix = packageName == null ? '${applicationId}' : packageName
					def appNode = """
							<provider
									xmlns:android="http://schemas.android.com/apk/res/android"
									xmlns:tools="http://schemas.android.com/tools"
									android:name="com.mob.MobProvider"
									android:multiprocess="true"
									android:authorities="${prefix}.com.mob.MobProvider"
									android:exported="false"/>
							"""
					app.appendNode(parser.parseText(appNode))
				}

				// 尝试加入AppKey、AppSecret和其它metadata
				Map metaData = globalVariants.applicationMetaData
				if (metaData == null) {
					metaData = [:]
				}
				metaData.put("Mob-AppKey", globalVariants.appkey)
				metaData.put("Mob-AppSeret", globalVariants.appSecret)
				String domain = globalVariants.domain
				if (domain != null) {
					metaData.put("Domain", domain)
				}
				boolean found
				metaData.each { field, value ->
					if (value != null) {
						found = false
						app.'meta-data'.each { md ->
							def dataName = md.@'android:name'.toString()
							if (field.equals(dataName)) {
								found = true
								md.@'android:value' = value
							}
						}
						if (!found) {
							def data = """
									<meta-data 
											xmlns:android="http://schemas.android.com/apk/res/android"
											xmlns:tools="http://schemas.android.com/tools"
											tools:node="merge"
											android:name="${field}" android:value="${value}"/>
									"""
							app.appendNode(parser.parseText(data))
						}
					}
				}

				// 尝试自动加入MobUIShell
				found = false
				app.'activity'.each { act ->
					def actName = act.@'android:name'.toString()
					if ("com.mob.tools.MobUIShell".equals(actName)) {
						found = true
						return
					}
				}
				if (!found) {
					def tmp = """
							<activity
									xmlns:android="http://schemas.android.com/apk/res/android"
									xmlns:tools="http://schemas.android.com/tools"
									tools:node="merge"
									android:theme="@android:style/Theme.Translucent.NoTitleBar"
									android:name="com.mob.tools.MobUIShell"
									android:configChanges="keyboardHidden|orientation|screenSize"
									android:windowSoftInputMode="stateHidden|adjustResize"/>
							"""
					def mobUiShell = parser.parseText(tmp)
					Set intents = globalVariants.intents
					if (intents != null) {
						intents.each { i ->
							mobUiShell.appendNode(parser.parseText(i))
						}
					}
					app.appendNode(mobUiShell)
				}

				// 尝试添加QQ等平台的特殊回调Activity
				Set directToAdd = globalVariants.directToAdd
				if (directToAdd != null) {
					directToAdd.each { actToAdd ->
						if (packageName != null && (String)actToAdd.contains('${applicationId}')) {
							actToAdd = (String)actToAdd.replace('${applicationId}', packageName)
						}
                        app.appendNode(parser.parseText(actToAdd))
					}
				}

				// 添加ShareSDK的回调Activity
				Set activitiesToAdd = globalVariants.activitiesToAdd
				if (activitiesToAdd != null) {
					activitiesToAdd.each { actToAdd ->
						found = false
						app.'activity'.each { act ->
							def actName = act.@'android:name'.toString()
							if (packageName != null && actName.startsWith(packageName)) {
								actName = actName.substring(packageName.length())
							}
							if (actName.equals(actToAdd)) {
								found = true
								return
							}
						}
						if (!found) {
							def className
							if (packageName == null) {
								className = '${applicationId}' + actToAdd
							} else {
								className = packageName + actToAdd
							}

							String superClass = null
							String[] parts = actToAdd.substring(1).split("\\.")
							if ("wxapi".equals(parts[0])) {
								if ("WXPayEntryActivity".equals(parts[1])) {
									superClass = "com.mob.paysdk.PaymentActivity"
								} else {
									superClass = "cn.sharesdk.wechat.utils.WechatHandlerActivity"
								}
							} else if ("yxapi".equals(parts[0])) {
								superClass = "cn.sharesdk.yixin.utils.YixinHandlerActivity"
							} else if ("apshare".equals(parts[0])) {
								superClass = "cn.sharesdk.alipay.utils.AlipayHandlerActivity"
							} else if ("ddshare".equals(parts[0])) {
								superClass = "cn.sharesdk.dingding.utils.DingdingHandlerActivity"
							}
							def superAct = """
								<activity
										xmlns:android="http://schemas.android.com/apk/res/android"
										xmlns:tools="http://schemas.android.com/tools"
										tools:node="merge"
										android:name="${superClass}"
										android:configChanges="keyboardHidden|orientation|screenSize"
										android:exported="false" />
								"""
							app.appendNode(parser.parseText(superAct))
							def actAlias = """
								<activity-alias
										xmlns:android="http://schemas.android.com/apk/res/android"
										xmlns:tools="http://schemas.android.com/tools"
										tools:node="merge"
										android:name="${className}"
										android:exported="true"
										android:targetActivity="${superClass}" />
								"""
							app.appendNode(parser.parseText(actAlias))
						}
					}
				}

				// 注册MobLink的uri和appLinks
				boolean addUri = false
				boolean addAppLink = false
				String uri = globalVariants.mobLinkUri
				if (uri != null) {
					URI u = new URI(uri)
					String scheme = u.getScheme()
					String host = u.getHost()
					addUri = !app.'activity'.any { act ->
						return act.'intent-filter'.any { filter ->
							return filter.'data'.any { data ->
								def sk = act.@'android:scheme'.toString()
								def hst = act.@'android:host'.toString()
								return (sk.equalsIgnoreCase(scheme) && hst.equalsIgnoreCase(host))
							}
						}
					}
				}
				String host = globalVariants.mobLinkAppLinkHost
				if (host != null) {
					addAppLink = !app.'activity'.any { act ->
						return act.'intent-filter'.any { filter ->
							return filter.'data'.any { data ->
								def sk = act.@'android:scheme'.toString()
								def hst = act.@'android:host'.toString()
								return ((sk.equalsIgnoreCase("http") || sk.equalsIgnoreCase("https")) && hst.equalsIgnoreCase(host))
							}
						}
					}
				}
				if (addUri || addAppLink) {
					String uriFilter = ""
					if (addUri) {
						URI u = new URI(uri)
						uriFilter = """
							<intent-filter>
								<action android:name="android.intent.action.VIEW" />
								<category android:name="android.intent.category.DEFAULT"/>
								<category android:name="android.intent.category.BROWSABLE"/>
								<data android:scheme="${u.getScheme()}" android:host="${u.getHost()}"/>
							</intent-filter>
						"""
					}
					String linkFilter = ""
					if (addAppLink) {
						String autoVerify
						if ("true".equalsIgnoreCase(String.valueOf(globalVariants.mobLinkAppLinkAutoVerify))) {
							autoVerify = "android:autoVerify=\"true\""
						} else {
							String compileSdkVersion = project.extensions.getByName("android").compileSdkVersion
							int compileLevel = Integer.parseInt(compileSdkVersion.substring("android-".length()))
							autoVerify = compileLevel >= 23 ? "android:autoVerify=\"true\"" : ""
						}
						linkFilter = """
							<intent-filter ${autoVerify}>
								<action android:name="android.intent.action.VIEW" />
								<category android:name="android.intent.category.DEFAULT"/>
								<category android:name="android.intent.category.BROWSABLE"/>
								<data android:scheme="http" android:host="${host}"/>
								<data android:scheme="https" android:host="${host}"/>
							</intent-filter>
						"""
					}
					String mobLinkActivity = """
						<activity
							xmlns:android="http://schemas.android.com/apk/res/android"
							xmlns:tools="http://schemas.android.com/tools"
							android:name="com.mob.moblink.MobLinkActivity"
							android:launchMode="singleTask"
							android:theme="@android:style/Theme.Translucent.NoTitleBar.Fullscreen">
							${uriFilter}
							${linkFilter}
						</activity>
					"""
					app.appendNode(parser.parseText(mobLinkActivity))
				}

				// 重新输出manifest文件
				manifestFile.setText(XmlUtil.serialize(manifest), "utf-8")
			}
		}
	}

    private def getProcessManifestTask(def output) {
        def result = null
        try {
            result = output.processManifestProvider.get()//gradle 3.3.0 +
        } catch (Throwable t) {}
        if (result == null) {
            try {
                result = output.processManifest
            } catch (Throwable t) {}
        }
        return result
    }

    private def getManifestOutputFile(def output, boolean appModel) {
        def manifestFiles = []
        try {
            manifestFiles.add(new File(getProcessManifestTask(output).manifestOutputDirectory, "AndroidManifest.xml"))
        } catch (Throwable t) {
            try {
				def file = getProcessManifestTask(output).manifestOutputFile
				if (file.exists()) {
					manifestFiles.add(file)
				}
            } catch (Throwable tt) {//gradle 3.3.0 +
                try {
                    manifestFiles.add(new File(getProcessManifestTask(output).manifestOutputDirectory.get().getAsFile(), "AndroidManifest.xml"))
                } catch (Throwable ttt) {}
            }
        }
        try {
            manifestFiles.add(new File(getProcessManifestTask(output).instantRunManifestOutputDirectory, "AndroidManifest.xml"))
        } catch (Throwable t) {
            try {
                manifestFiles.add(getProcessManifestTask(output).instantRunManifestOutputFile)
            } catch (Throwable tt) {}
        }
        return manifestFiles
    }

}