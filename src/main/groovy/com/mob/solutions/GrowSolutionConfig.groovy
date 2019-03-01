package com.mob.solutions

class GrowSolutionConfig extends SolutionConfig {

	GrowSolutionConfig() {
		hiddenMembers {
			baiduMobAdID = 'bae4740c'
		}
	}

	def methodMissing(String name, def args) {
		if ("BaiduMobAdID".equalsIgnoreCase(name)) {
			baiduMobAdID = args[0]
		}
		return super.methodMissing(name, args)
	}

	void process() {
		mobSDK.ShareSDK {
			disableConfig true
			devInfo {
				SinaWeibo {}
				Wechat {}
				QQ {}
				QZone {}
				WechatMoments {}
			}
		}

		mobSDK.CMSSDK {
			gui false
		}

		mobSDK.CloudStorage {}

		mobSDK.addDependencies('com.mob:GrowSolution:+@aar')

		mobSDK.addDependencies('com.mob:BaiduMobAds:+')

		//一览视频
		mobSDK.addDependencies('com.yilan.sdk:data:1.0.1')
		mobSDK.addDependencies('com.squareup.okhttp3:okhttp:3.11.0')
		mobSDK.addDependencies('com.google.code.gson:gson:2.8.5')

		//瑞狮广告
		mobSDK.addDependencies('com.cnlive:vlion:0.3.6')
		mobSDK.addDependencies('com.android.support:appcompat-v7:26.1.0')
		mobSDK.addDependencies('com.android.support:support-v4:26.1.0')

		mobSDK.configCreator.applicationMetaData.BaiduMobAd_APP_ID = baiduMobAdID
		String appidTag = '${applicationId}'
		mobSDK.configCreator.directToAdd.add("""
				<provider
						xmlns:android="http://schemas.android.com/apk/res/android" 
						xmlns:tools="http://schemas.android.com/tools"
						tools:node="merge"
						android:name="com.baidu.mobads.openad.FileProvider"
						android:authorities="${appidTag}.bd.provider"
						android:exported="false"
						android:grantUriPermissions="true">
					<meta-data
							xmlns:android="http://schemas.android.com/apk/res/android" 
							xmlns:tools="http://schemas.android.com/tools"
							tools:node="merge"
							android:name="android.support.FILE_PROVIDER_PATHS"
							android:resource="@xml/bd_file_paths" />
				</provider>
		""")
		mobSDK.configCreator.directToAdd.add("""
				<activity
						xmlns:android="http://schemas.android.com/apk/res/android" 
						xmlns:tools="http://schemas.android.com/tools"
						tools:node="merge"
						android:name="com.baidu.mobads.AppActivity" 
						android:configChanges="screenSize|keyboard|keyboardHidden|orientation" 
						android:theme="@android:style/Theme.Translucent.NoTitleBar"/>
		""")
		mobSDK.configCreator.directToAdd.add("""
				<provider
					xmlns:android="http://schemas.android.com/apk/res/android" 
					xmlns:tools="http://schemas.android.com/tools"
					tools:node="merge"
					android:name="android.support.v4.content.FileProvider"
					android:authorities="${appidTag}.fileprovider"
					android:exported="false"
					android:grantUriPermissions="true">
					<meta-data
						android:name="android.support.FILE_PROVIDER_PATHS"
						android:resource="@xml/vlion_file_provider" />
				</provider>
		""")
		mobSDK.configCreator.directToAdd.add("""
				<activity
					xmlns:android="http://schemas.android.com/apk/res/android" 
					xmlns:tools="http://schemas.android.com/tools"
					tools:node="merge"
					android:theme="@style/Theme.AppCompat.Light.NoActionBar"
					android:name="cn.vlion.ad.view.webview.VLionWebViewActivity" />
		""")
	}
}