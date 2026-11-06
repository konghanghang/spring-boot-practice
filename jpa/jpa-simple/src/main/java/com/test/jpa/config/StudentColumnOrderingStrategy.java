package com.test.jpa.config;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.model.relational.ColumnOrderingStrategyLegacy;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.Table;

/** Preserves the example's declaration-order DDL without overriding Hibernate internals. */
public class StudentColumnOrderingStrategy extends ColumnOrderingStrategyLegacy {

    private static final List<String> COLUMN_ORDER = List.of(
        "id", "number", "name", "sex", "email", "remark", "create_time");

    @Override
    public List<Column> orderTableColumns(Table table, Metadata metadata) {
        if (!"student".equalsIgnoreCase(table.getName())) {
            return null;
        }
        List<Column> columns = new ArrayList<>(table.getColumns());
        columns.sort(Comparator.comparingInt(column -> {
            int position = COLUMN_ORDER.indexOf(column.getName());
            return position < 0 ? COLUMN_ORDER.size() : position;
        }));
        return columns;
    }
}
