//package org.microservices.user.biz.controller;
//
//
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.media.Schema;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.Getter;
//import org.microservices.common.auth.annotation.AllowUnauthorizedRequest;
//import org.microservices.common.cache.support.redis.operation.RedisStringOperations;
//import org.microservices.common.core.enumeration.Enum;
//import org.microservices.common.core.resp.Response;
//import org.microservices.common.core.resp.StatusCodeImpl;
//import org.microservices.common.core.pagination.PageRequest;
//import org.springdoc.api.annotations.ParameterObject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cache.CacheManager;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * 系统角色前端控制器
// *
// * @author xiangqian
// * @date 09:54 2022/04/03
// */
//@RestController
//@RequestMapping("/role")
//@Tag(name = "Role", description = "系统用户信息")
//public class RoleController {
//
//    @Autowired
//    private CacheManager cacheManager;
//
//    @Autowired
//    private RedisStringOperations redisStringOperations;
//
//    @AllowUnauthorizedRequest
//    @GetMapping("/test")
//    public Response<Object> test() {
//        System.out.println(cacheManager.getClass() + ", " + cacheManager);
//        System.out.println("redisStringOperations" + redisStringOperations);
//
//        redisStringOperations.set("timestamp", System.currentTimeMillis());
//
//        Object timestamp = redisStringOperations.get("timestamp");
//        System.out.println("--> " + timestamp);
//
////        return response();
//        return Response.builder()
//                .statusCode(StatusCodeImpl.OK)
//                .body(timestamp)
//                .build();
//    }
//
//    //@Operation(summary = "Get user by user name", tags = { "user" })
//    //	@ApiResponses(value = {
//    //			@ApiResponse(responseCode = "200", description = "successful operation", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)), @Content(mediaType = "application/xml", schema = @Schema(implementation = User.class)) }),
//    //			@ApiResponse(responseCode = "400", description = "Invalid username supplied", content = @Content),
//    //			@ApiResponse(responseCode = "404", description = "User not found", content = @Content) })
//    //
//    //	@GetMapping(value = "/user/{username}")
//    //	default ResponseEntity<User> getUserByName(
//    //			@Parameter(description = "The name that needs to be fetched. Use user1 for testing. ", required = true) @PathVariable("username") String username) {
//    //		return getDelegate().getUserByName(username);
//    //	}
//    //
//    //	@Operation(summary = "Logs user into the system", tags = { "user" })
//    //	@ApiResponses(value = {
//    //			@ApiResponse(responseCode = "200", headers = {
//    //					@Header(name = "X-Rate-Limit", description = "calls per hour allowed by the user", schema = @Schema(type = "integer", format = "int32")),
//    //					@Header(name = "X-Expires-After", description = "date in UTC when toekn expires", schema = @Schema(type = "string", format = "date-time")) },
//    //					description = "successful operation", content = @Content(schema = @Schema(implementation = String.class))),
//    //			@ApiResponse(responseCode = "400", description = "Invalid username/password supplied", content = @Content) })
//    //	@GetMapping(value = "/user/login", produces = { "application/xml", "application/json" })
//    //	default ResponseEntity<String> loginUser(
//    //			@NotNull @Parameter(description = "The user name for login", required = false) @Valid @RequestParam(value = "username", required = false) String username,
//    //			@NotNull @Parameter(description = "The password for login in clear text", required = false) @Valid @RequestParam(value = "password", required = false) String password) {
//    //		return getDelegate().loginUser(username, password);
//    //	}
//    //
//    //	@Operation(summary = "Logs out current logged in user session", tags = { "user" })
//    //	@ApiResponses(value = { @ApiResponse(description = "successful operation") })
//    //	@GetMapping(value = "/user/logout")
//    //	default ResponseEntity<Void> logoutUser() {
//    //		return getDelegate().logoutUser();
//    //	}
//
//
//    @AllowUnauthorizedRequest
//    @GetMapping("/get")
//    @Operation(summary = "get")
////    public Response<Object> page(@ParameterObject PageRequest test, EnumTest enumTest) {
//    public Response<PageRequest> page(@ParameterObject PageRequest test, @Schema(description = "测试枚举类型") EnumTest enumTest) {
////        System.out.println("------ " + enumTest + ", " + test);
//        System.out.println("------ " + enumTest);
//
////        return response();
//        return null;
//    }
//
//    @AllowUnauthorizedRequest
//    @PostMapping("/post")
//    @Operation(summary = "post")
//    public Response<PageRequest> post(@RequestBody PageRequest test) {
//        System.out.println("---post--- " + test);
////        return response();
//        return null;
//    }
//
//    @AllowUnauthorizedRequest
//    @PostMapping("/put")
//    @Operation(summary = "put")
//    public Response<PageRequest> put(@RequestBody PageRequest test) {
//        System.out.println("---post--- " + test);
////        return response();
//        return null;
//    }
//
//    private Response<Object> response() {
//        Map<String, Object> map = new HashMap<>();
//        map.put("LocalDateTime", LocalDateTime.now());
//        map.put("LocalDate", LocalDate.now());
//        map.put("Date", new Date());
//        map.put("enumTest", EnumTest.TEST1);
//
//        return Response.builder()
//                .statusCode(StatusCodeImpl.OK)
//                .body(map)
//                .build();
//    }
//
//    @Getter
//    public static enum EnumTest implements Enum<Integer> {
//        TEST0(0, "test0"),
//        TEST1(1, "test1"),
//        TEST2(2, "test2"),
//        TEST3(3, "test3"),
//        ;
//
//        private final Integer value;
//        private final String description;
//
//        EnumTest(int value, String description) {
//            this.value = value;
//            this.description = description;
//        }
//
//    }
//
//}
//
