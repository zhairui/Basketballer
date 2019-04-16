package com.jerry.moduledep

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.language.assembler.plugins.internal.AssembleTaskConfig
import org.gradle.language.assembler.tasks.Assemble

class MouDepPlugin implements Plugin<Project>{

    String defaultModuleName = "app" //默认运行的app
    String compilemodule = defaultModuleName
    @Override
    void apply(Project project) {

        String module = project.path.replace(":","")

        if(project.rootProject.hasProperty("applicationModule")){
            compilemodule = project.rootProject.property("applicationModule")
        }
        boolean isRunAlone

        if(module.equals(compilemodule)){
            isRunAlone = true
        }else{
            isRunAlone = false
        }

        if(isRunAlone){
            project.apply plugin: 'com.android.application'

            project.android.sourceSets{
                main{
                    jniLibs.srcDirs =['libs']
                    manifest.srcFile 'src/main/application_manifest/AndroidManifest.xml'
                }
            }
            compileProject(project)
        }else{
            project.apply plugin:'com.android.library'
        }

    }

    void compileProject(Project project){
        String depComponent = project.properties.get("depComponent")

        if(depComponent == null || depComponent.length()==0) return

        String[] components = depComponent.split(",")

        if(components==null && components.length==0) System.out.println("请用逗号分割")
        components.each {
            project.dependencies.add("api",project.project(":"+it))
        }
    }
}