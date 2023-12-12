package org.xiangqian.microservices.common.util;

import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

/**
 * @author xiangqian
 * @date 20:50 2023/10/18
 */
public class RestTest {

    @Test
    public void test() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Rest rest = new Rest(new RestTemplate(), headers);
        rest.get("", null, null);
    }


}
