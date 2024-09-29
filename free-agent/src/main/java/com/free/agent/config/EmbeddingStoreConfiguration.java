package com.free.agent.config;

import dev.langchain4j.store.embedding.elasticsearch.ElasticsearchEmbeddingStore;
import lombok.Data;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;


/**
 * 向量存储配置
 */
@Data
@EnableConfigurationProperties(value = EmbeddingStoreConfiguration.class)
@ConfigurationProperties(prefix = "embedding-store.elasticsearch")
@Configuration
public class EmbeddingStoreConfiguration {

    /**
     * elasticsearch config
     */
    private String hostname;
    private int port;
    private String scheme;
    private String userName;
    private String password;

    private String indexName;

    private Integer dimension;

    /**
     * elasticsearch 提供的向量存储和检索
     * @return ElasticsearchEmbeddingStore
     */
    @Bean
    public ElasticsearchEmbeddingStore elasticsearchEmbeddingStore(ResourceLoader resourceLoader) throws CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        return ElasticsearchEmbeddingStore.builder()
                .indexName(indexName)
                .dimension(dimension)
                .restClient(makeHttpsRestClient(hostname, port, scheme, userName, password, resourceLoader))
                .build();
    }

    /**
     *
     * @param hostname es hostname
     * @param port es port
     * @param scheme es scheme
     * @param userName es userName
     * @param password es password
     * @param resourceLoader resourceLoader
     * @return RestClient
     */
    private RestClient makeHttpsRestClient(String hostname, int port, String scheme, String userName, String password, ResourceLoader resourceLoader) throws CertificateException, IOException
            , KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        final CredentialsProvider credentialsProvider =
                new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(userName, password));
        Resource crtResource = resourceLoader.getResource("classpath:es_crt/http_ca.crt");
        CertificateFactory factory =
                CertificateFactory.getInstance("X.509");
        Certificate trustedCa;
        try (InputStream is = Files.newInputStream(crtResource.getFile().toPath())) {
            trustedCa = factory.generateCertificate(is);
        }
        KeyStore trustStore = KeyStore.getInstance("pkcs12");
        trustStore.load(null, null);
        trustStore.setCertificateEntry("ca", trustedCa);
        SSLContextBuilder sslContextBuilder = SSLContexts.custom()
                .loadTrustMaterial(trustStore, null);
        final SSLContext sslContext = sslContextBuilder.build();
        return RestClient.builder(new HttpHost(hostname, port, scheme)).setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
            @Override
            public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                return httpClientBuilder.setSSLContext(sslContext)
                        .setDefaultCredentialsProvider(credentialsProvider);
            }
        }).build();
    }
}
