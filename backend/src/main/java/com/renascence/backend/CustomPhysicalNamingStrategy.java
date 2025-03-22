package com.renascence.backend;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

public class CustomPhysicalNamingStrategy implements PhysicalNamingStrategy {

    @Override
    public Identifier toPhysicalCatalogName(Identifier name, JdbcEnvironment context) {
        return name; // Няма нужда от промени
    }

    @Override
    public Identifier toPhysicalSchemaName(Identifier name, JdbcEnvironment context) {
        return name; // Няма нужда от промени
    }

    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
        String tableName = name.getText(); // Вземаме името на таблицата
        String camelCaseTableName = toCamelCase(tableName); // Преобразуваме го в CamelCase
        return Identifier.toIdentifier(camelCaseTableName); // Връщаме новото име
    }

    @Override
    public Identifier toPhysicalSequenceName(Identifier name, JdbcEnvironment context) {
        return name; // Няма нужда от промени
    }

    @Override
    public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment context) {
        String columnName = name.getText(); // Вземаме името на колоната
        String camelCaseColumnName = toCamelCase(columnName); // Преобразуваме го в CamelCase
        return Identifier.toIdentifier(camelCaseColumnName); // Връщаме новото име
    }

    // Метод за преобразуване от snake_case в camelCase
    private String toCamelCase(String text) {
        StringBuilder result = new StringBuilder();
        String[] words = text.split("_"); // Разделяме на думи по символа '_'

        // Първата дума остава с малка буква
        result.append(words[0].toLowerCase());

        // Всяка следваща дума започва с голяма буква
        for (int i = 1; i < words.length; i++) {
            result.append(words[i].substring(0, 1).toUpperCase())
                    .append(words[i].substring(1).toLowerCase());
        }

        return result.toString();
    }
}
