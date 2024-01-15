package com.mycompany.myapp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to BE.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link tech.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {
    // jhipster-needle-application-properties-property
    // jhipster-needle-application-properties-property-getter
    // jhipster-needle-application-properties-property-class
//    private String imagesUrl;
//
//    public String getImagesUrl() {
//        return imagesUrl;
//    }
//
//    public void setImagesUrl(String imagesUrl) {
//        this.imagesUrl = imagesUrl;
//    }

    private MinIOService minioService = new MinIOService();

    public MinIOService getMinioService() {
        return minioService;
    }

    public void setMinioService(MinIOService minioService) {
        this.minioService = minioService;
    }

    public static class MinIOService {

        private String baseUrl;
        private String accessKey;
        private String secretKey;
        private int active;

        public MinIOService() {}

        public String getBaseUrl() {
            return baseUrl;
        }

        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }

        public String getAccessKey() {
            return accessKey;
        }

        public void setAccessKey(String accessKey) {
            this.accessKey = accessKey;
        }

        public String getSecretKey() {
            return secretKey;
        }

        public void setSecretKey(String secretKey) {
            this.secretKey = secretKey;
        }

        public int getActive() {
            return active;
        }

        public void setActive(int active) {
            this.active = active;
        }
    }
}
