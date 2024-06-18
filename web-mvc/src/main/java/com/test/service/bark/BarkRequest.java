package com.test.service.bark;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author yslao@outlook.com
 * @since 5/18/24
 */
@Data
@JsonInclude(Include.NON_NULL)
public class BarkRequest {

    private String title;

    private String body;

    private BarkLevelEnum level;

    private Integer badge;

    private Boolean autoCopy;

    private String copy;

    private String sound;

    private String icon;

    private String group;

    private Integer isArchive;

    private String url;

    @JsonProperty("device_key")
    private String deviceKey;

}
