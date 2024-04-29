package com.example.tasks;

import org.gradle.api.DefaultTask;
import org.gradle.api.file.ConfigurableFileTree;
import org.gradle.api.tasks.TaskAction;

import java.io.*;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ResourceKeyBuildTask extends DefaultTask {


    @TaskAction
    void doAction() throws IOException {
        ConfigurableFileTree tree = getProject().fileTree("src/main/resources");
        tree.include("messages.properties");

        File file = tree.getFiles().stream().findFirst()
                .orElseThrow(() -> new RuntimeException("No messages.properties file"));

        try (BufferedReader reader = new BufferedReader (new FileReader(file))) {
            var keys = reader.lines()
                    .map(line -> Arrays.stream(line.split("=")).findFirst().orElse(null))
                    .filter(s -> s != null && !s.isBlank());

            getProject().fileTree("src/main/generated").getDir().mkdir();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/generated/Resources.java"))) {
                var template = """
                        package resources;
                        
                        import javax.annotation.processing.Generated;
                        
                        @Generated("com.example.tasks.ResourceKeyBuildTask")
                        public enum Resources {
                            %s
                            ;

                            private final String key;
                            
                            Resources(String key) {
                                this.key = key;
                            }

                            public String getKey() {
                                return key;
                            }
                        }
                        """;
                var keyDeclaration = keys
                        .map(it -> String.format("%s(\"%s\"),",
                                it.toUpperCase().replace(".", "_").trim(),
                                it.trim()))
                        .collect(Collectors.joining());

                writer.write(String.format(template, keyDeclaration));
            }
        }
    }
}
