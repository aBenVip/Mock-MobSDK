package com.mob.products.sharesdk

import com.mob.ConfigCreator
import com.mob.DObject
import com.mob.products.ShareSDKConfig

class DevInfo extends DObject {
	static Set platforms
	static Map jarNames
	ConfigCreator config
	ShareSDKConfig sharesdk

	static {
		platforms = [
				"SinaWeibo", "TencentWeibo", "Douban", "QZone", "Renren", "KaiXin",
				"Facebook", "Twitter", "Evernote", "FourSquare", "GooglePlus",
				"Instagram", "LinkedIn", "Tumblr", "Email", "ShortMessage", "Wechat",
				"WechatMoments", "QQ", "Instapaper", "Pocket", "YouDao", "Pinterest",
				"Flickr", "Dropbox", "VKontakte", "WechatFavorite", "Yixin",
				"YixinMoments", "Mingdao", "Line", "WhatsApp", "KakaoTalk",
				"KakaoStory", "FacebookMessenger", "Alipay", "AlipayMoments",
				"Dingding", "Youtube", "Meipai", "Telegram", "Cmcc", "Reddit",
				"Telecom", "Accountkit", "Douyin"

		]

		jarNames = [:]
		def names = [
				null, null, null, null, null, null,
				null, null, null, null, null,
				null, null, null, null, null, ["Wechat", "Wechat-Core"],
				["Wechat-Moments", "Wechat-Core"], null, null, null, null, null,
				null, null, null, ["Wechat-Favorite", "Wechat-Core"], ["Yixin", "Yixin-Core"],
				["Yixin-Moments", "Yixin-Core"], null, null, null, ["KakaoTalk", "Kakao-Core"],
				["KakaoStory", "Kakao-Core"], null, ["Alipay", "Alipay-Core"], ["Alipay-Moments", "Alipay-Core"],
				null, null, null, null
		]
		for (int i = 0; i < platforms.size(); i++) {
			if (names[i] == null) {
				jarNames.put(platforms[i], [platforms[i]])
			} else {
				jarNames.put(platforms[i], names[i])
			}
		}
	}

	void setShareSDKConfig(ShareSDKConfig sharesdk){
		this.sharesdk = sharesdk
		this.config = sharesdk.config
		platforms.each { p ->
			if (!config.sharesdkInfoMap.containsKey(p)) {
				config.sharesdkInfoMap.put(p, null)
			}
		}
	}

	def methodMissing(String name, def args) {
		String plat = platforms.find {
			return name.equalsIgnoreCase(it)
		}
		if (plat != null) {
			InfoNode info = new InfoNode()
			info.closure = args[0]
			addInfo(plat, info)
			addPermission(plat)
		}
		return null
	}

	private void addInfo(String name, InfoNode info) {
		addActivity(name, info)
		Map raw = [:]
		raw.putAll(info.fields)
		if ("SinaWeibo".equals(name)) {
			raw.put("RedirectUrl", raw.remove("CallbackUri"))
		} else if ("TencentWeibo".equals(name)) {
			raw.put("RedirectUri", raw.remove("CallbackUri"))
		} else if ("Facebook".equals(name)) {
			raw.put("ConsumerKey", raw.remove("AppKey"))
			raw.put("ConsumerSecret", raw.remove("AppSecret"))
			raw.put("RedirectUrl", raw.remove("CallbackUri"))
		} else if ("Twitter".equals(name)) {
			raw.put("ConsumerKey", raw.remove("AppKey"))
			raw.put("ConsumerSecret", raw.remove("AppSecret"))
			raw.put("CallbackUrl", raw.remove("CallbackUri"))
		} else if ("Renren".equals(name)) {
			raw.put("ApiKey", raw.remove("AppKey"))
			raw.put("SecretKey", raw.remove("AppSecret"))
		} else if ("KaiXin".equals(name)) {
			raw.put("RedirectUri", raw.remove("CallbackUri"))
		} else if ("Douban".equals(name)) {
			raw.put("ApiKey", raw.remove("AppKey"))
			raw.put("Secret", raw.remove("AppSecret"))
			raw.put("RedirectUri", raw.remove("CallbackUri"))
		} else if ("YouDao".equals(name)) {
			raw.put("ConsumerKey", raw.remove("AppKey"))
			raw.put("ConsumerSecret", raw.remove("AppSecret"))
			raw.put("RedirectUri", raw.remove("CallbackUri"))
		} else if ("Evernote".equals(name)) {
			raw.put("ConsumerKey", raw.remove("AppKey"))
			raw.put("ConsumerSecret", raw.remove("AppSecret"))
		} else if ("LinkedIn".equals(name)) {
			raw.put("ApiKey", raw.remove("AppKey"))
			raw.put("SecretKey", raw.remove("AppSecret"))
			raw.put("RedirectUrl", raw.remove("CallbackUri"))
		} else if ("GooglePlus".equals(name)) {
			raw.put("ClientID", raw.remove("AppId"))
			raw.put("RedirectUrl", raw.remove("CallbackUri"))
		} else if ("FourSquare".equals(name)) {
			raw.put("ClientID", raw.remove("AppId"))
			raw.put("ClientSecret", raw.remove("AppSecret"))
			raw.put("RedirectUrl", raw.remove("CallbackUri"))
		} else if ("Pinterest".equals(name)) {
			raw.put("ClientId", raw.remove("AppId"))
		} else if ("Flickr".equals(name)) {
			raw.put("ApiKey", raw.remove("AppKey"))
			raw.put("ApiSecret", raw.remove("AppSecret"))
			raw.put("RedirectUri", raw.remove("CallbackUri"))
		} else if ("Tumblr".equals(name)) {
			raw.put("OAuthConsumerKey", raw.remove("AppKey"))
			raw.put("SecretKey", raw.remove("AppSecret"))
			raw.put("CallbackUrl", raw.remove("CallbackUri"))
		} else if ("Dropbox".equals(name)) {
			raw.put("RedirectUri", raw.remove("CallbackUri"))
		} else if ("VKontakte".equals(name)) {
			raw.put("ApplicationId", raw.remove("AppId"))
		} else if ("Instagram".equals(name)) {
			raw.put("ClientId", raw.remove("AppId"))
			raw.put("ClientSecret", raw.remove("AppSecret"))
			raw.put("RedirectUri", raw.remove("CallbackUri"))
		} else if ("Mingdao".equals(name)) {
			raw.put("RedirectUri", raw.remove("CallbackUri"))
		} else if ("Line".equals(name)) {
			raw.put("ChannelID", raw.remove("AppId"))
			raw.put("ChannelSecret", raw.remove("AppSecret"))
		} else if ("Pocket".equals(name)) {
			raw.put("ConsumerKey", raw.remove("AppKey"))
		} else if ("Instapaper".equals(name)) {
			raw.put("ConsumerKey", raw.remove("AppKey"))
			raw.put("ConsumerSecret", raw.remove("AppSecret"))
		} else if ("Youtube".equals(name)) {
			raw.put("ClientID", raw.remove("AppId"))
			raw.put("RedirectUrl", raw.remove("CallbackUri"))
		} else if ("Meipai".equals(name)) {
			raw.put("ClientID", raw.remove("AppId"))
		} else if("Reddit".equals(name)){
			raw.put("RedirectUrl", raw.remove("CallbackUri"))
		} else if("Telecom".equals(name)){
			raw.put("RedirectUrl", raw.remove("CallbackUri"))
		} else if("Accountkit".equals(name)){
			raw.put("RedirectUrl", raw.remove("CallbackUri"))
		}

		if (!raw.isEmpty()) {
			Map m = [:]
			raw.each { k, v ->
				if (k != null && v != null) {
					m.put(k, v)
				}
			}
			raw = m
		}

		// 下面平台的字段是标准的
		// Wechat、WechatMoments、WechatFavorite、QZone、QQ、Email、ShortMessage、
		// Yixin、YixinMoments、KakaoTalk、KakaoStory、WhatsApp、Bluetooth、
		// FacebookMessenger、Alipay、AlipayMoments、Dingding、Telegram
		config.sharesdkInfoMap.put(name, raw)
	}

	private void addActivity(String name, InfoNode info) {
		if ("Wechat".equals(name) || "WechatMoments".equals(name) || "WechatFavorite".equals(name)) {
			config.activitiesToAdd.add(".wxapi.WXEntryActivity")
		} else if ("Yixin".equals(name) || "YixinMoments".equals(name)) {
			config.activitiesToAdd.add(".yxapi.YXEntryActivity")
		} else if ("Alipay".equals(name) || "AlipayMoments".equals(name)) {
            config.activitiesToAdd.add(".apshare.ShareEntryActivity")
		} else if ("Dingding".equals(name)) {
			config.activitiesToAdd.add(".ddshare.DDShareActivity")
		} else if ("Dropbox".equals(name)) {
			config.intents.add("""
					<intent-filter
							xmlns:android="http://schemas.android.com/apk/res/android"
							xmlns:tools="http://schemas.android.com/tools"
							android:priority="1000">
						<data android:scheme="db-7janx53ilz11gbs" />
						<action android:name="android.intent.action.VIEW" />
						<category android:name="android.intent.category.BROWSABLE"/>
						<category android:name="android.intent.category.DEFAULT" />
					</intent-filter>
            """)
		} else if ("Line".equals(name) && info.AppId != null) {
			config.directToAdd.add("""
					<activity
						xmlns:android="http://schemas.android.com/apk/res/android"
						android:name="cn.sharesdk.line.LineHandlerActivity"
						android:configChanges="orientation|screenSize|keyboardHidden"
						android:exported="true">
						<intent-filter>
							<action android:name="android.intent.action.VIEW" />
			
							<category android:name="android.intent.category.DEFAULT" />
							<category android:name="android.intent.category.BROWSABLE" />
			
							<data android:scheme="${info.callbackscheme}" />
						</intent-filter>
					</activity>
			""")
			config.directToAdd.add("""
				   <activity-alias
						xmlns:android="http://schemas.android.com/apk/res/android"
						android:name=".lineapi.LineAuthenticationCallbackActivity"
						android:exported="true"
						android:targetActivity="cn.sharesdk.line.LineHandlerActivity" />
			""")
		} else if ("SinaWeibo".equals(name)) {
			config.intents.add("""
					<intent-filter xmlns:android="http://schemas.android.com/apk/res/android"
							xmlns:tools="http://schemas.android.com/tools">
						<action android:name="com.sina.weibo.sdk.action.ACTION_SDK_REQ_ACTIVITY" />
						<category android:name="android.intent.category.DEFAULT" />
					</intent-filter>
            """)
		} else if ("QQ".equals(name) && info.AppId != null) {
			sharesdk.hasQQ = true
			//当QQ空间配置在QQ之前，已经知道注册了QQ空间的回调，但是依然要注册QQ的回调，则此时需要移除QQ空间的回调
			if(sharesdk.hasQQ && sharesdk.hasQZone && config.directToAdd.contains(sharesdk.deleteConfigInfo)){
				config.directToAdd.remove(sharesdk.deleteConfigInfo)
			}
			config.directToAdd.add("""
				<activity
						xmlns:android="http://schemas.android.com/apk/res/android"
						xmlns:tools="http://schemas.android.com/tools"
						android:name="cn.sharesdk.tencent.qq.ReceiveActivity"
						android:launchMode="singleTask"
						android:noHistory="true">
					<intent-filter>
						<data android:scheme="tencent${info.AppId}" />
						<action android:name="android.intent.action.VIEW" />
						<category android:name="android.intent.category.DEFAULT" />
						<category android:name="android.intent.category.BROWSABLE" />
					</intent-filter>
				</activity>
			""")
		} else if ("QZone".equals(name) && info.AppId != null) {
			sharesdk.hasQZone = true
			//当QQ配置在QQ空间之前，已经知道注册了QQ的回调，则此时不需要注册QQ空间的回调
			if(!sharesdk.hasQQ){
				config.directToAdd.add("""
				<activity
						xmlns:android="http://schemas.android.com/apk/res/android"
						xmlns:tools="http://schemas.android.com/tools"
						android:name="cn.sharesdk.tencent.qzone.ReceiveActivity"
						android:launchMode="singleTask"
						android:noHistory="true">
					<intent-filter>
						<data android:scheme="tencent${info.AppId}" />
						<action android:name="android.intent.action.VIEW" />
						<category android:name="android.intent.category.DEFAULT" />
						<category android:name="android.intent.category.BROWSABLE" />
					</intent-filter>
				</activity>
			""")
				sharesdk.deleteConfigInfo = config.directToAdd.getAt(config.directToAdd.size() - 1)
			}
		} else if ("KakaoTalk".equals(name) && info.AppKey != null) {
			config.directToAdd.add("""
					<activity
							xmlns:android="http://schemas.android.com/apk/res/android"
							xmlns:tools="http://schemas.android.com/tools"
							android:theme="@android:style/Theme.Translucent.NoTitleBar"
							android:name="cn.sharesdk.kakao.talk.ReceiveActivity"
							android:exported="true">
						<intent-filter>
							<action android:name="android.intent.action.VIEW"/>
							<category android:name="android.intent.category.DEFAULT"/>
							<category android:name="android.intent.category.BROWSABLE"/>
							<data android:scheme="kakao${info.AppKey}" android:host="kakaolink" />
						</intent-filter>
					</activity>
			""")
		} else if ("FacebookMessenger".equals(name)) {
			config.directToAdd.add("""
					<activity
							xmlns:android="http://schemas.android.com/apk/res/android"
							xmlns:tools="http://schemas.android.com/tools"
							android:theme="@android:style/Theme.Translucent.NoTitleBar"
							android:name="cn.sharesdk.facebookmessenger.ReceiveActivity"
							android:exported="true">
							<intent-filter>
								<action android:name="android.intent.action.PICK"/>
								<category android:name="android.intent.category.DEFAULT" />
								<category android:name="com.facebook.orca.category.PLATFORM_THREAD_20150314" />
							</intent-filter>
					</activity>
			""")
		}
	}

	private void addPermission(String name) {
		if ("Cmcc".equals(name)) {
			config.permissions.addAll([
					"android.permission.ACCESS_COARSE_LOCATION",
					"android.permission.CHANGE_NETWORK_STATE",
					"android.permission.SEND_SMS"
			])
		}
	}

}