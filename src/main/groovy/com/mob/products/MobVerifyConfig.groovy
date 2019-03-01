package com.mob.products

class MobVerifyConfig extends MobProductConfig {
	Set getPermission() {
		return [
				"android.permission.WRITE_SETTINGS"
		]
	}
}