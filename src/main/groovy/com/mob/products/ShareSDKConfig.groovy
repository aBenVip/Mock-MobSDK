package com.mob.products

import com.mob.ConfigCreator
import com.mob.products.sharesdk.DevInfo

class ShareSDKConfig extends MobProductConfig {
	ConfigCreator config
	boolean hasQQ
	boolean hasQZone
	Object deleteConfigInfo

	void setConfigCreator(ConfigCreator config) {
		this.config = config
	}

	def methodMissing(String name, Object args) {
		if ("devInfo".equalsIgnoreCase(name) || "devInfos".equalsIgnoreCase(name)
				|| "platform".equalsIgnoreCase(name) || "platforms".equalsIgnoreCase(name)
				|| "plat".equalsIgnoreCase(name) || "plats".equalsIgnoreCase(name)
				|| "info".equalsIgnoreCase(name) || "infos".equalsIgnoreCase(name)) {
			DevInfo infos = new DevInfo()
			infos.shareSDKConfig = this
			infos.closure = args[0]
		} else if ("disableConfig".equalsIgnoreCase(name)) {
			config.disableShareSDKConfig = true
		} else if ("placeConfigHere".equalsIgnoreCase(name)) {
			String path = null
			try {
				if (args[0] instanceof String) {
					path = args[0]
				}
			} catch(Throwable t) {}
			config.setPlaceShareSDKConfigHere(path)
		}
		return super.methodMissing(name, args)
	}

	Set getPermission() {
		return [
				"android.permission.BLUETOOTH",
				"android.permission.RECEIVE_SMS"
		]
	}
}