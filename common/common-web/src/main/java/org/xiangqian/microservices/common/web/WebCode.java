package org.xiangqian.microservices.common.web;

/**
 * web状态码
 *
 * @author xiangqian
 * @date 20:56 2023/10/11
 */
public interface WebCode {

    // http 1xx Informational
    String CONTINUE = "continue"; // 100
    String SWITCHING_PROTOCOLS = "switching_protocols"; // 101
    String PROCESSING = "processing"; // 102
    String EARLY_HINTS = "early_hints"; // 103
    @Deprecated
    String CHECKPOINT = "checkpoint"; // 103

    // http 2xx Successful
    String OK = "ok"; // 200
    String CREATED = "created"; // 201
    String ACCEPTED = "accepted"; // 202
    String NON_AUTHORITATIVE_INFORMATION = "non_authoritative_information"; // 203
    String NO_CONTENT = "no_content"; // 204
    String RESET_CONTENT = "reset_content"; // 205
    String PARTIAL_CONTENT = "partial_content"; // 206
    String MULTI_STATUS = "multi_status"; // 207
    String ALREADY_REPORTED = "already_reported"; // 208
    String IM_USED = "im_used"; // 226

    // http 3xx Redirection
    String MULTIPLE_CHOICES = "multiple_choices"; // 300
    String MOVED_PERMANENTLY = "moved_permanently"; // 301
    String FOUND = "found"; // 302
    @Deprecated
    String MOVED_TEMPORARILY = "moved_temporarily"; // 302
    String SEE_OTHER = "see_other"; // 303
    String NOT_MODIFIED = "not_modified"; // 304
    @Deprecated
    String USE_PROXY = "use_proxy"; // 305
    String TEMPORARY_REDIRECT = "temporary_redirect"; // 307
    String PERMANENT_REDIRECT = "permanent_redirect"; // 308

    // http 4xx ClientError
    String BAD_REQUEST = "bad_request"; // 400
    String UNAUTHORIZED = "unauthorized"; // 401
    String PAYMENT_REQUIRED = "payment_required"; // 402
    String FORBIDDEN = "forbidden"; // 403
    String NOT_FOUND = "not_found"; // 404
    String METHOD_NOT_ALLOWED = "method_not_allowed"; // 405
    String NOT_ACCEPTABLE = "not_acceptable"; // 406
    String PROXY_AUTHENTICATION_REQUIRED = "proxy_authentication_required"; // 407
    String REQUEST_TIMEOUT = "request_timeout"; // 408
    String CONFLICT = "conflict"; // 409
    String GONE = "gone"; // 410
    String LENGTH_REQUIRED = "length_required"; // 411
    String PRECONDITION_FAILED = "precondition_failed"; // 412
    String PAYLOAD_TOO_LARGE = "payload_too_large"; // 413
    @Deprecated
    String REQUEST_ENTITY_TOO_LARGE = "request_entity_too_large"; // 413
    String URI_TOO_LONG = "uri_too_long"; // 414
    @Deprecated
    String REQUEST_URI_TOO_LONG = "request_uri_too_long"; // 414
    String UNSUPPORTED_MEDIA_TYPE = "unsupported_media_type"; // 415
    String REQUESTED_RANGE_NOT_SATISFIABLE = "requested_range_not_satisfiable"; // 416
    String EXPECTATION_FAILED = "expectation_failed"; // 417
    String I_AM_A_TEAPOT = "i_am_a_teapot"; // 418
    @Deprecated
    String INSUFFICIENT_SPACE_ON_RESOURCE = "insufficient_space_on_resource"; // 419
    @Deprecated
    String METHOD_FAILURE = "method_failure"; // 420
    @Deprecated
    String DESTINATION_LOCKED = "destination_locked"; // 421
    String UNPROCESSABLE_ENTITY = "unprocessable_entity"; // 422
    String LOCKED = "locked"; // 423
    String FAILED_DEPENDENCY = "failed_dependency"; // 424
    String TOO_EARLY = "too_early"; // 425
    String UPGRADE_REQUIRED = "upgrade_required"; // 426
    String PRECONDITION_REQUIRED = "precondition_required"; // 428
    String TOO_MANY_REQUESTS = "too_many_requests"; // 429
    String REQUEST_HEADER_FIELDS_TOO_LARGE = "request_header_fields_too_large"; // 431
    String UNAVAILABLE_FOR_LEGAL_REASONS = "unavailable_for_legal_reasons"; // 451

    // http 5xx ServerError
    String INTERNAL_SERVER_ERROR = "internal_server_error"; // 500
    String NOT_IMPLEMENTED = "not_implemented"; // 501
    String BAD_GATEWAY = "bad_gateway"; // 502
    String SERVICE_UNAVAILABLE = "service_unavailable"; // 503
    String GATEWAY_TIMEOUT = "gateway_timeout"; // 504
    String HTTP_VERSION_NOT_SUPPORTED = "http_version_not_supported"; // 505
    String VARIANT_ALSO_NEGOTIATES = "variant_also_negotiates"; // 506
    String INSUFFICIENT_STORAGE = "insufficient_storage"; // 507
    String LOOP_DETECTED = "loop_detected"; // 508
    String BANDWIDTH_LIMIT_EXCEEDED = "bandwidth_limit_exceeded"; // 509
    String NOT_EXTENDED = "not_extended"; // 510
    String NETWORK_AUTHENTICATION_REQUIRED = "network_authentication_required"; // 511

}
