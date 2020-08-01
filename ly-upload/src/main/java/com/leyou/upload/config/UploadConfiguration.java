package com.leyou.upload.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ConfigurationProperties( prefix = "leyou.upload" )
public class UploadConfiguration {

    private String baseUrl;

    private List<String> allowedSuffixes = new ArrayList<String>();

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public List<String> getAllowedSuffixes() {
        return allowedSuffixes;
    }

    public void setAllowedSuffixes(List<String> allowedSuffixes) {
        this.allowedSuffixes = allowedSuffixes;
    }
}
