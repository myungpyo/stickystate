extra.apply {
    set("PUBLISH_GROUP_ID", "io.github.myungpyo")
    set("PUBLISH_VERSION", "1.0.0")
    set("PUBLISH_ARTIFACT_ID","stickystate-processor")
    set("PUBLISH_DESCRIPTION","StickyState Android SDK")
    set("PUBLISH_URL", "https://github.com/myungpyo/stickystate")
    set("PUBLISH_LICENSE_NAME","Apache License")
    set("PUBLISH_LICENSE_URL","https://github.com/myungpyo/stickystate/blob/main/LICENSE")
    set("PUBLISH_DEVELOPER_ID","myungpyo")
    set("PUBLISH_DEVELOPER_NAME","myungpyo")
    set("PUBLISH_DEVELOPER_EMAIL","itmansmp@gmail.com")
    set("PUBLISH_SCM_CONNECTION","scm:git:github.com/myungpyo/stickystate.git")
    set("PUBLISH_SCM_DEVELOPER_CONNECTION","scm:git:ssh://github.com/myungpyo/stickystate.git")
    set("PUBLISH_SCM_URL","https://github.com/myungpyo/stickystate/tree/main")
}

plugins {
    kotlin("jvm")
}
apply {
    from("${rootProject.projectDir}/scripts/publish-module.gradle")
}

dependencies {
    implementation("io.github.myungpyo:stickystate-core:0.0.3")
    implementation("com.google.devtools.ksp:symbol-processing-api:1.6.10-1.0.2")
}