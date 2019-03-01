package com.mob.products

class SMSSDKConfig extends MobProductConfig {
	Set getPermission() {
		return [
				"android.permission.RECEIVE_SMS",
				"android.permission.READ_SMS",
				"android.permission.READ_CONTACTS"
		]
	}
}