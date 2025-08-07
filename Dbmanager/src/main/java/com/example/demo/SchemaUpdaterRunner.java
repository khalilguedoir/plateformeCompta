package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.demo.service.SchemaUpdateService;



@Component
public class SchemaUpdaterRunner implements CommandLineRunner {
	
	@Autowired
    private SchemaUpdateService schemaUpdateService;

    public SchemaUpdaterRunner(SchemaUpdateService schemaUpdateService) {
        this.schemaUpdateService = schemaUpdateService;
    }

    @Override
    public void run(String... args) {
        schemaUpdateService.applyChangelogToAllSchemas();
    }
}
