package com.leyou.provider;

import com.netflix.hystrix.exception.HystrixTimeoutException;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

@Component
public class ApiFallbackProvider implements FallbackProvider {
    @Override
    //该Provider应用的Route ID，例如：testservice，如果设置为 * ，那就对所有路由生效
    public String getRoute() {
        return "*";
    }

    @Override
    public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
        String message = "";
        if (cause instanceof HystrixTimeoutException) {
            message = "当前服务不可用，请稍后再试";
        } else {
            message = "Service exception";
        }

        return fallbackResponse(message + " - " + cause);
    }

    public ClientHttpResponse fallbackResponse(String message) {

        return new ClientHttpResponse() {
            /**
             * ClientHttpResponse 的 fallback 的状态码 返回HttpStatus
             */
            @Override
            public HttpStatus getStatusCode() throws IOException {
                return HttpStatus.OK;
            }
            /**
             * ClientHttpResponse 的 fallback 的状态码 返回 int
             */
            @Override
            public int getRawStatusCode() throws IOException {
                return getStatusCode().value();
            }

            /**
             * ClientHttpResponse 的 fallback 的状态码 返回 String
             */
            @Override
            public String getStatusText() throws IOException {
                return getStatusCode().getReasonPhrase();
            }

            @Override
            public void close() {

            }

            @Override
            public InputStream getBody() throws IOException {
                String bodyText = String.format("{\"code\": 999,\"message\": \"Service unavailable:%s\"}", message);
                return new ByteArrayInputStream(bodyText.getBytes());
            }

            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders headers = new HttpHeaders();
                //headers.setContentType(MediaType.APPLICATION_JSON);
                MediaType mediaType = new MediaType("application","json", Charset.forName("utf-8"));
                headers.setContentType(mediaType);
                return headers;
            }
        };

    }
}
