package com.example;

import com.example.tasks.ResourceKeyBuildTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class MyPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        project.getTasks().create("resourceKeyBuild", ResourceKeyBuildTask.class);

        project.getTasks().getByName("compileJava").dependsOn("resourceKeyBuild");
    }
}