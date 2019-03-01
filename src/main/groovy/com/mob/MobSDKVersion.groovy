package com.mob

class MobSDKVersion
		implements GroovyObject
{
	public static final String NAME = "2019.0221.2318";

	MobSDKVersion()
	{
		MetaClass localMetaClass = $getStaticMetaClass();
		this.metaClass = localMetaClass;
	}
}