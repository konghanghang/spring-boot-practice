package com.test.es.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class MappingFieldInfo {
    private String name;
    private String type;
    private String indexType = "not_analyzed";
    private String formatType;
}
