package org.xiangqian.microservices.user.api;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.*;
import org.xiangqian.microservices.common.model.Page;
import org.xiangqian.microservices.common.model.PageRequest;
import org.xiangqian.microservices.common.model.Response;
import org.xiangqian.microservices.user.model.vo.UserAddVo;
import org.xiangqian.microservices.user.model.vo.UserPageVo;
import org.xiangqian.microservices.user.model.vo.UserUpdVo;
import org.xiangqian.microservices.user.model.vo.UserVo;

/**
 * @author xiangqian
 * @date 21:53 2022/04/03
 */
@HttpExchange("/user") // 声明此类是 HTTP Interface 端点
public interface UserApi {

    // @GetExchange   : HTTP GET 请求
    // @PostExchange  : HTTP POST 请求
    // @PutExchange   : HTTP PUT 请求
    // @DeleteExchange: HTTP DELETE 请求
    // @PatchExchange : HTTP PATCH 请求

    // 传递参数格式
    // @PathVariable:将请求URL中的值替换为占位符。
    // @RequestBody:提供请求的主体。
    // @RequestParam:添加请求参数。当“content-type”设置为“application/x-www-form-urlencoded”时，请求参数会在请求体中编码。否则，它们将作为URL查询参数添加。
    // @requesttheader:添加请求头的名称和值。
    // @RequestPart:可用于添加请求部分(表单字段，资源或HttpEntity等)。
    // @CookieValue:向请求中添加cookie。

    //Additional annotations for all the HTTP methods are available:
    //1. @GetExchange for HTTP GET requests
    //2. @PostExchange for HTTP POST requests
    //3. @PutExchange for HTTP PUT requests
    //4. @PatchExchange for HTTP PATCH requests
    //5. @DelectExchange for HTTP DELETE requests

    // Method Parameters
    //In our example interface, we used @PathVariable and @RequestBody annotations for method parameters. In addition, we may use the following set of method parameters for our exchange methods:
    //1. URI: dynamically sets the URL for the request, overriding the annotation attribute
    //2. HttpMethod: dynamically sets the HTTP method for the request, overriding the annotation attribute
    //3. @RequestHeader: adds the request header names and values, the argument may be a Map or MultiValueMap
    //4. @PathVariable: replaces a value that has a placeholder in the request URL
    //5. @RequestBody: provides the body of the request either as an object to be serialized, or a reactive streams publisher such as Mono or Flux
    //6. @RequestParam: adds request parameter names and values, the argument may be a Map or MultiValueMap
    //7. @CookieValue: adds cookie names and values, the argument may be a Map or MultiValueMap
    //We should note that request parameters are encoded in the request body only for content type “application/x-www-form-urlencoded”. Otherwise, request parameters are added as URL query parameters.

    //interface BooksService {
    //
    //    @GetExchange("/books")
    //    List<Book> getBooks();
    //
    //    @DeleteExchange("/books/{id}")
    //    ResponseEntity<Void> deleteBook(@PathVariable long id);
    //}

    //@PostExchange("upload")
    //Response<Void> upload(@RequestPart("files") MultipartFile[] files);

    @GetExchange("/page")
    Response<Page<UserVo>> page(PageRequest pageRequest, UserPageVo vo);

    @GetExchange("/{id}")
    Response<UserVo> getById(@PathVariable("id") Long id);

    @PutExchange
    Response<Boolean> updById(@RequestBody UserUpdVo vo);

    @DeleteExchange("/{id}")
    Response<Boolean> delById(@PathVariable("id") Long id);

    @PostExchange
    Response<Boolean> add(@RequestBody UserAddVo vo);

}
// https://howtodoinjava.com/spring-webflux/http-declarative-http-client-httpexchange/
// https://www.springcloud.io/post/2023-02/spring-httpexchange/