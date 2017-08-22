swagger-codegen-gradle-plugin
============================

[![Build Status](https://travis-ci.org/thebignet/swagger-codegen-gradle-plugin.svg?branch=master)](https://travis-ci.org/thebignet/swagger-codegen-gradle-plugin)

A Gradle plugin to support the [swagger](http://swagger.io) code generation project


[DEPRECATED] !!!
------------

see the [swagger-codegen-gradle-plugin-example](https://github.com/thebignet/swagger-codegen-gradle-plugin-example) for creating a Gradle task to generate code with Swagger Codegen.


Usage
============================

see the [swagger-codegen-gradle-plugin-example](https://github.com/thebignet/swagger-codegen-gradle-plugin-example), or:

Here is an example of how to use the plugin in a `build.gradle` file
```groovy
plugins {
    id 'org.detoeuf.swagger-codegen' version '1.7.4'
    id 'java'
}

apply plugin: 'org.detoeuf.swagger-codegen'

repositories {
    mavenCentral()
    jcenter()
}

swagger {
    inputSpec = 'http://petstore.swagger.io/v2/swagger.json'
    outputDir = file('build/swagger')
    lang = 'java'

    additionalProperties = [
            'invokerPackage'   : 'io.swagger.petstore.client',
            'modelPackage'     : 'io.swagger.petstore.client.model',
            'apiPackage'       : 'io.swagger.petstore.client.api',
            'dateLibrary'      : 'java8'
    ]
    importMappings = [
            'Dog': 'io.swagger.petstore.client.model.Dog'
    ]
}

sourceSets {
    swagger {
        java {
            srcDir file("${project.buildDir.path}/swagger/src/main/java")
        }
    }
}

classes.dependsOn('swagger')

ext {
    spring_boot_version = '1.5.6.RELEASE'
    jackson_version = '2.4.2'
    jersey_version = '1.18'
    jodatime_version = '2.3'
    junit_version = '4.8.1'
}

dependencies {
    swaggerCompile "org.springframework.boot:spring-boot-starter-web:$spring_boot_version"
    swaggerCompile 'io.swagger:swagger-annotations:1.5.16'
    swaggerCompile 'com.squareup.okhttp:okhttp:2.7.5'
    swaggerCompile 'com.squareup.okhttp:logging-interceptor:2.7.5'
    swaggerCompile 'com.google.code.gson:gson:2.8.1'

    compile sourceSets.swagger.output

    compile "com.sun.jersey:jersey-client:$jersey_version"
    compile "com.sun.jersey.contribs:jersey-multipart:$jersey_version"
    compile "com.fasterxml.jackson.core:jackson-core:$jackson_version"
    compile "com.fasterxml.jackson.core:jackson-annotations:$jackson_version"
    compile "com.fasterxml.jackson.core:jackson-databind:$jackson_version"
    compile "com.fasterxml.jackson.datatype:jackson-datatype-joda:2.1.5"
    compile "joda-time:joda-time:$jodatime_version"
    compile 'io.swagger:swagger-codegen:2.2.3'

    testCompile "junit:junit:$junit_version"

    runtime 'com.squareup.okhttp:okhttp:2.7.5'
    runtime 'com.squareup.okhttp:logging-interceptor:2.7.5'
    runtime 'com.google.code.gson:gson:2.8.1'
}
```

Launch with:

```
gradle swagger
```

### Configuration parameters
the `swagger {}` configuration is passed to [CodegenConfigurator.java](https://github.com/swagger-api/swagger-codegen/blob/v2.2.1/modules/swagger-codegen/src/main/java/io/swagger/codegen/config/CodegenConfigurator.java)

#### Dynamic properties
Some Swagger codegen configurations will contain extra properties that are not part of the io.swagger.codegen.CodegenConfig interface. For example there is the `sourceFolder` property that is only applicable if using jaxrs-spec as the value of the `lang` property. In order to set such properties, we should use the `dynamicProperty` method inside the `swagger {}` configuration as shown below.

```
swagger {
    lang = 'jaxrs-spec'

    addDynamicProperty 'sourceFolder', 'src/swagger/java'

    additionalProperties = [
        ...
    ]
    systemProperties = [
        ...
    ]
}
```

… to be documented …

### deprecation warning
the old behaviour had a custom plugin for this swagger config as seen below
- `inputSpec` - :check:
- `outputDir` - was: `output`
- `lang` - was: `language`
- `additionalProperties` - sets additional properties that can be referenced by the mustache templates in the format of name=value,name=value.  See [Customizing the generator](https://github.com/swagger-api/swagger-codegen/#customizing-the-generator) for list of parameters

#### new settings possible
- `systemProperties` - see [selective generation](https://github.com/swagger-api/swagger-codegen/#selective-generation)

#### no longer included
- `models` - have a look at the systemProperties section
- `apis` - [selective generation](https://github.com/swagger-api/swagger-codegen/#selective-generation) of apis.  Leave blank to generate apis only
- `supportingFiles` - [selective generation](https://github.com/swagger-api/swagger-codegen/#selective-generation) of supporting files.  Leave blank to generate supporting files only

no substituion for:
- `cleanOutputDir` - now, configured by configuring the task directly:
    ```groovy
    tasks.getByName("swagger") {
        cleanOutputDir = false
    }
    ```
