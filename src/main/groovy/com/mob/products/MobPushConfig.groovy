package com.mob.products

import com.mob.ConfigCreator
import com.mob.products.mobpush.PluginsDevInfo

class MobPushConfig extends MobProductConfig {
	ConfigCreator config

	void setConfigCreator(ConfigCreator config) {
		this.config = config
	}

	void addMobService() {
		config.directToAdd.add("""
				<service
						xmlns:android="http://schemas.android.com/apk/res/android"
						xmlns:tools="http://schemas.android.com/tools"
						android:name="com.mob.pushsdk.MobService"
						android:exported="true"
						tools:node="merge"
						android:process=":mobservice">
					<intent-filter>
						<action android:name="com.mob.intent.MOB_SERVICE"/>
					</intent-filter>
				</service>
		""")

		int code  = getVersionCode()
		if(code == 0 || code >= 10600){
			config.directToAdd.add("""
				<receiver
					xmlns:android="http://schemas.android.com/apk/res/android"
					xmlns:tools="http://schemas.android.com/tools"
					android:name="com.mob.pushsdk.impl.MobPushReceiver">
					<intent-filter>
						<action android:name="android.net.conn.CONNECTIVITY_CHANGE" />
					</intent-filter>
				</receiver>
		""")
			config.directToAdd.add("""
				<service
					xmlns:android="http://schemas.android.com/apk/res/android"
					xmlns:tools="http://schemas.android.com/tools"
					android:name="com.mob.pushsdk.impl.PushJobService"
					android:permission="android.permission.BIND_JOB_SERVICE" />
		""")
		}
	}

	def methodMissing(String name, Object args) {
		if ("devInfo".equalsIgnoreCase(name) || "devInfos".equalsIgnoreCase(name)
				|| "factory".equalsIgnoreCase(name) || "factories".equalsIgnoreCase(name)
				|| "fac".equalsIgnoreCase(name) || "facs".equalsIgnoreCase(name)
				|| "info".equalsIgnoreCase(name) || "infos".equalsIgnoreCase(name)) {
			PluginsDevInfo infos = new PluginsDevInfo()
			infos.configCreator = config
			infos.sdkVersion = getVersionCode()
			infos.closure = args[0]
		}
		return super.methodMissing(name, args)
	}

	private int getVersionCode(){
		if (version == null || "+".equals(version)) {
			return 0
		} else {
			int code = 0
			String[] parts = version.split("\\.")
			for (int i = 0; i < 3; i++) {
				code = code * 100 + Integer.parseInt(parts[i])
			}
			return code
		}
	}

	Set getPermission() {
		return [
				"android.permission.VIBRATE"
		]
	}

}