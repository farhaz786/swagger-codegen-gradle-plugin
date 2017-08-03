package org.detoeuf

import io.swagger.codegen.config.CodegenConfigurator
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test

import static org.junit.Assert.assertTrue

class SwaggerCodeGenTaskTest {
    @Test
    void canAddTaskToProject() {
        Project project = ProjectBuilder.builder().build()
        def task = project.task('swagger', type: SwaggerCodeGenTask)
        assertTrue(task instanceof SwaggerCodeGenTask)
    }

    @Test
    void basicConfiguration() {

        URL url = Thread.currentThread().getContextClassLoader().getResource('petstore.yaml');
        File file = new File(url.getPath());

        Project project = ProjectBuilder.builder().build()
        def task = project.task('swagger', type: SwaggerCodeGenTask)

        def pluginExtension = new CodegenConfigurator(
                inputSpec: file.toString(),
                outputDir: 'target/generated-sources/swagger',
                lang: 'java',
                additionalProperties: [
                        'invokerPackage': 'io.swagger.petstore.client',
                        'apiPackage'    : 'io.swagger.petstore.client.api',
                        'configPackage' : 'io.swagger.petstore.client.config',
                        'modelPackage'  : 'io.swagger.petstore.client.model',
                        'library'       : 'okhttp-gson',
                        'dateLibrary'   : 'java8'
                ]
        )

        project.extensions.add('swagger', pluginExtension)
        assertTrue(task instanceof SwaggerCodeGenTask)
        task.execute()
    }

    @Test
    void onlyApisAndModels() {

        URL url = Thread.currentThread().getContextClassLoader().getResource('petstore.yaml');
        File file = new File(url.getPath());

        Project project = ProjectBuilder.builder().build()
        def task = project.task('swagger', type: SwaggerCodeGenTask)

        def pluginExtension = new CodegenConfigurator(
                inputSpec: file.toString(),
                outputDir: 'target/generated-sources/swagger',
                lang: 'java',
                additionalProperties: [
                        'invokerPackage'   : 'io.swagger.petstore.client',
                        'apiPackage'       : 'io.swagger.petstore.client.api',
                        'configPackage'    : 'io.swagger.petstore.client.config',
                        'modelPackage'     : 'io.swagger.petstore.client.model',
                        'library'          : 'okhttp-gson',
                        'serializableModel': 'true'
                ],
                systemProperties: [
                        'apis'  : '',
                        'models': ''
                ],
                importMappings: [
                        'Dog': 'io.swagger.petstore.client.model.Dog'
                ]
        )

        project.extensions.add('swagger', pluginExtension)
        assertTrue(task instanceof SwaggerCodeGenTask)
        task.execute()
    }

}
