package org.xiangqian.microservices.common.util;

import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * @author xiangqian
 * @date 20:50 2023/10/18
 */
public class RestTest {

    @Test
    public void test() {
        Rest rest = new Rest(new RestTemplate());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<String> respEntity = rest.get("https://www.baidu.com", null, headers, String.class);
        System.out.format("HttpStatusCode: %s", respEntity.getStatusCode()).println();
        System.out.format("Body: \n%s", respEntity.getBody()).println();
    }

}
