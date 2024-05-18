package com.free.ollama.config;

import dev.langchain4j.store.embedding.elasticsearch.ElasticsearchEmbeddingStore;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

@Slf4j
@Configuration
public class EmbeddingStoreConfiguration {


    /**
     * pgVector config
     */
    @Value("${pg.vector.host}")
    private String pgVectorHost;

    @Value("${pg.vector.port}")
    private int pgVectorPort;

    @Value("${pg.vector.database}")
    private String pgVectorDatabase;

    @Value("${pg.vector.user}")
    private String pgVectorUser;

    @Value("${pg.vector.password}")
    private String pgVectorPassword;

    @Value("${pg.vector.table}")
    private String pgVectorTable;

    //@Value("${pg.vector.dimension}")
    private Integer pgVectorDimension;

    @Value("${pg.vector.indexListSize}")
    private Integer pgVectorIndexListSize;

    /**
     * elasticsearch config
     */
    @Value("${elasticsearch.vector.server-url}")
    private String elasticsearchServerUrl;

    @Value("${elasticsearch.vector.user-name}")
    private String elasticsearchServerUserName;

    @Value("${elasticsearch.vector.pass-word}")
    private String elasticsearchServerPassword;

    @Value("${elasticsearch.vector.index-name}")
    private String elasticsearchServerIndexName;

    @Value("${elasticsearch.vector.dimension}")
    private Integer elasticsearchServerDimension;

    /**
     * pgVectorEmbeddingStore bean
     * @return
     */
    //@ConditionalOnProperty(value = "pg.vector.enable", havingValue = "true")
    //@Bean
    public PgVectorEmbeddingStore pgVectorEmbeddingStore() {
        log.info("PgVectorEmbeddingStore build start...");
        PgVectorEmbeddingStore pgVectorEmbeddingStore = PgVectorEmbeddingStore.builder()
                .host(pgVectorHost)
                .port(pgVectorPort)
                .database(pgVectorDatabase)
                .useIndex(true)
                .user(pgVectorUser)
                .password(pgVectorPassword)
                .dimension(pgVectorDimension)
                .indexListSize(pgVectorIndexListSize)
                .createTable(true)
                .table(pgVectorTable)
                .dropTableFirst(true)
                .build();
        log.info("PgVectorEmbeddingStore build success");
        return pgVectorEmbeddingStore;
    }


    /**
     * es 向量存储
     * @return
     */
    @ConditionalOnProperty(value = "elasticsearch.vector.enable", havingValue = "true")
    @Bean
    public ElasticsearchEmbeddingStore elasticsearchEmbeddingStore(ResourceLoader resourceLoader) throws CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        return ElasticsearchEmbeddingStore.builder()
                .indexName(elasticsearchServerIndexName)
                .dimension(elasticsearchServerDimension)
                .restClient(makeHttpsRestClient(elasticsearchServerUrl, elasticsearchServerUserName, elasticsearchServerPassword, resourceLoader))
                .build();
    }

    private RestClient makeHttpsRestClient(String serverUrl, String userName, String password, ResourceLoader resourceLoader) throws CertificateException, IOException
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
        return RestClient.builder(new HttpHost("localhost", 9200,"https")).setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
            @Override
            public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                return httpClientBuilder.setSSLContext(sslContext)
                        .setDefaultCredentialsProvider(credentialsProvider);
            }
        }).build();
    }

}
