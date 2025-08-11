package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.example.demo.service.SchemaUpdateService;

@Component
public class SchemaUpdaterRunner implements CommandLineRunner {

    private final SchemaUpdateService service;

    public SchemaUpdaterRunner(SchemaUpdateService service) {
        this.service = service;
    }

    @Override
    public void run(String... params) {
        if (params.length < 2) {
            System.out.println("⚠ Utilisation :");
            System.out.println("  update all                -> migration tous les schémas");
            System.out.println("  update schemaName          -> migration 1 schéma");
            System.out.println("  rollbackCount all N        -> rollback N changeSets tous schémas");
            System.out.println("  rollbackCount schemaName N -> rollback N changeSets 1 schéma");
            System.out.println("  rollbackToDate all YYYY-MM-DD -> rollback jusqu'à une date");
            System.out.println("  rollbackToDate schemaName YYYY-MM-DD -> rollback jusqu'à une date");
            return;
        }

        String action = params[0];
        String target = params[1];

        switch (action) {
            case "update":
                if ("all".equalsIgnoreCase(target)) {
                    service.updateAllSchemas();
                } else {
                    service.updateSchema(target);
                }
                break;

            case "rollbackCount":
                if (params.length < 3) {
                    System.err.println("❌ rollbackCount nécessite un nombre de changeSets");
                    return;
                }
                int count = Integer.parseInt(params[2]);
                if ("all".equalsIgnoreCase(target)) {
                    service.rollbackCountAllSchemas(count);
                } else {
                    service.rollbackCount(target, count);
                }
                break;

            case "rollbackToDate":
                if (params.length < 3) {
                    System.err.println("❌ rollbackToDate nécessite une date (YYYY-MM-DD)");
                    return;
                }
                String date = params[2];
                if ("all".equalsIgnoreCase(target)) {
                    service.rollbackToDateAllSchemas(date);
                } else {
                    service.rollbackToDate(target, date);
                }
                break;

            default:
                System.err.println("❌ Action inconnue : " + action);
        }
    }
}
