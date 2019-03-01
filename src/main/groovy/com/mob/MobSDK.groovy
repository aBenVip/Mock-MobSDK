package com.mob

import org.gradle.api.Plugin
import org.gradle.api.Project

class MobSDK implements Plugin<Project> {
	void apply(Project project) {
		List urls = []
		project.rootProject.buildscript.repositories.each { repo->
			try {
				if (repo.url != null) {
					String url = String.valueOf(repo.url)
					if (url.startsWith("http://") || url.startsWith("https://") || url.startsWith("file:/")) {
						urls.add(url)
						project.repositories.maven { maven ->
							maven.url = repo.url
						}
					}
				}
			} catch (Throwable t) {}
		}

		boolean added = urls.any { url ->
			boolean usingReleaseServer = url.startsWith("http://mvn.mob.com/android")
			boolean usingDebugServer = url.startsWith("http://10.18.97.47:8080/jenkins/job/iMaven/ws/android")
			return usingReleaseServer || usingDebugServer
		}

		if (!added) {
			project.repositories.maven { maven ->
				maven.url = "http://mvn.mob.com/android"
			}
		}

		MobSDKConfig sdk = project.extensions.create("MobSDK", MobSDKConfig)
		sdk.project = project
		MobSDKSolution solution = project.extensions.create("MobSDKSolution", MobSDKSolution)
		project.extensions.add("MobSolution", solution)
		solution.mobSDK = sdk

		println("=============================")
		println("=== MobSDK ${MobSDKVersion.NAME} ===")
		println("=============================")
	}
}