package com.mob.products.sharesdk

import com.mob.AutoCorrectNode

class InfoNode extends AutoCorrectNode {
	Set getFieldNames() {
		return [
				"Id",
				"SortId",
				"AppId",       // String appId, clientID, applicationId, channelID
				"AppKey",      // String appKey, consumerKey, apiKey, oAuthConsumerKey
				"AppSecret",   // String appSecret, consumerSecret, secretKey, secret, clientSecret, apiSecret, channelSecret
				"CallbackUri", // String redirectUrl, redirectUri, callbackUrl
				"ShareByAppClient",
				"Enable",
				"BypassApproval",
				"userName",
				"path",
				"HostType",
				"WithShareTicket",
				"MiniprogramType",
				"callbackscheme"
		]
	}
}