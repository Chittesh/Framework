package com.framework.api.restServices.pojo;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.jsonschema2pojo.DefaultGenerationConfig;
import org.jsonschema2pojo.GenerationConfig;
import org.jsonschema2pojo.Jackson2Annotator;
import org.jsonschema2pojo.SchemaGenerator;
import org.jsonschema2pojo.SchemaMapper;
import org.jsonschema2pojo.SchemaStore;
import org.jsonschema2pojo.SourceType;
import org.jsonschema2pojo.rules.RuleFactory;

import com.framework.utils.TestReporter;
import com.sun.codemodel.JCodeModel;

public class JsonToPojo {
    String jsonName = null;

    public JsonToPojo(String jsonName) {
        this.jsonName = jsonName;
    }

    public void createPojoFromJson() {
        String packageName = "com.framework.api.restServices.pojo";
        File inputJson = new File(System.getProperty("user.dir") + "\\src\\main\\resources\\json" + File.separator + jsonName);
        File outputPojoDirectory = new File(System.getProperty("user.dir") + "\\test-output" + File.separator + "pojo");
        outputPojoDirectory.mkdirs();
        try {
            this.convert2JSON(inputJson.toURI().toURL(), outputPojoDirectory, packageName, inputJson.getName().replace(".json", ""));
        } catch (IOException e) {
            TestReporter.log("Encountered issue while converting to pojo: " + e.getMessage() + e.toString());
        }
    }

    public void convert2JSON(URL inputJson, File outputPojoDirectory, String packageName, String className) throws IOException {
        JCodeModel codeModel = new JCodeModel();
        URL source = inputJson;
        GenerationConfig config = new DefaultGenerationConfig() {
            @Override
            public boolean isGenerateBuilders() { // set config option by overriding method
                return true;
            }

            @Override
            public SourceType getSourceType() {
                return SourceType.JSON;
            }
        };
        SchemaMapper mapper = new SchemaMapper(new RuleFactory(config, new Jackson2Annotator(config), new SchemaStore()), new SchemaGenerator());
        mapper.generate(codeModel, className, packageName, source);
        codeModel.build(outputPojoDirectory);
    }
}