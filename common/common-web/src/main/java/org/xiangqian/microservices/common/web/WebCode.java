package org.xiangqian.microservices.common.web;

import org.xiangqian.microservices.common.model.Code;

/**
 * web状态码
 *
 * @author xiangqian
 * @date 20:56 2023/10/11
 */
public interface WebCode extends Code {

    // http 1xx Informational
    @Description(zh = "", en = "continue")
    String CONTINUE = "continue"; // 100
    @Description(zh = "", en = "switching protocols")
    String SWITCHING_PROTOCOLS = "switching_protocols"; // 101
    @Description(zh = "", en = "processing")
    String PROCESSING = "processing"; // 102
    @Description(zh = "", en = "early hints")
    String EARLY_HINTS = "early_hints"; // 103
    @Deprecated
    @Description(zh = "", en = "checkpoint")
    String CHECKPOINT = "checkpoint"; // 103

    // http 2xx Successful
    @Description(zh = "成功", en = "ok")
    String OK = "ok"; // 200
    @Description(zh = "", en = "created")
    String CREATED = "created"; // 201
    @Description(zh = "", en = "accepted")
    String ACCEPTED = "accepted"; // 202
    @Description(zh = "", en = "non authoritative information")
    String NON_AUTHORITATIVE_INFORMATION = "non_authoritative_information"; // 203
    @Description(zh = "", en = "no content")
    String NO_CONTENT = "no_content"; // 204
    @Description(zh = "", en = "reset content")
    String RESET_CONTENT = "reset_content"; // 205
    @Description(zh = "", en = "partial content")
    String PARTIAL_CONTENT = "partial_content"; // 206
    @Description(zh = "", en = "multi status")
    String MULTI_STATUS = "multi_status"; // 207
    @Description(zh = "", en = "")
    String ALREADY_REPORTED = "already_reported"; // 208
    @Description(zh = "", en = "")
    String IM_USED = "im_used"; // 226

    // http 3xx Redirection
    @Description(zh = "", en = "")
    String MULTIPLE_CHOICES = "multiple_choices"; // 300
    @Description(zh = "", en = "")
    String MOVED_PERMANENTLY = "moved_permanently"; // 301
    @Description(zh = "", en = "")
    String FOUND = "found"; // 302
    @Deprecated
    @Description(zh = "", en = "")
    String MOVED_TEMPORARILY = "moved_temporarily"; // 302
    @Description(zh = "", en = "")
    String SEE_OTHER = "see_other"; // 303
    @Description(zh = "", en = "")
    String NOT_MODIFIED = "not_modified"; // 304
    @Description(zh = "", en = "")
    @Deprecated
    String USE_PROXY = "use_proxy"; // 305
    @Description(zh = "", en = "")
    String TEMPORARY_REDIRECT = "temporary_redirect"; // 307
    @Description(zh = "", en = "")
    String PERMANENT_REDIRECT = "permanent_redirect"; // 308

    // http 4xx ClientError
    @Description(zh = "", en = "")
    String BAD_REQUEST = "bad_request"; // 400
    @Description(zh = "", en = "")
    String UNAUTHORIZED = "unauthorized"; // 401
    @Description(zh = "", en = "")
    String PAYMENT_REQUIRED = "payment_required"; // 402
    @Description(zh = "", en = "")
    String FORBIDDEN = "forbidden"; // 403
    @Description(zh = "", en = "")
    String NOT_FOUND = "not_found"; // 404
    @Description(zh = "", en = "")
    String METHOD_NOT_ALLOWED = "method_not_allowed"; // 405
    @Description(zh = "", en = "")
    String NOT_ACCEPTABLE = "not_acceptable"; // 406
    @Description(zh = "", en = "")
    String PROXY_AUTHENTICATION_REQUIRED = "proxy_authentication_required"; // 407
    @Description(zh = "", en = "")
    String REQUEST_TIMEOUT = "request_timeout"; // 408
    @Description(zh = "", en = "")
    String CONFLICT = "conflict"; // 409
    @Description(zh = "", en = "")
    String GONE = "gone"; // 410
    @Description(zh = "", en = "")
    String LENGTH_REQUIRED = "length_required"; // 411
    @Description(zh = "", en = "")
    String PRECONDITION_FAILED = "precondition_failed"; // 412
    @Description(zh = "", en = "")
    String PAYLOAD_TOO_LARGE = "payload_too_large"; // 413
    @Description(zh = "", en = "")
    @Deprecated
    String REQUEST_ENTITY_TOO_LARGE = "request_entity_too_large"; // 413
    @Description(zh = "", en = "")
    String URI_TOO_LONG = "uri_too_long"; // 414
    @Description(zh = "", en = "")
    @Deprecated
    String REQUEST_URI_TOO_LONG = "request_uri_too_long"; // 414
    @Description(zh = "", en = "")
    String UNSUPPORTED_MEDIA_TYPE = "unsupported_media_type"; // 415
    @Description(zh = "", en = "")
    String REQUESTED_RANGE_NOT_SATISFIABLE = "requested_range_not_satisfiable"; // 416
    @Description(zh = "", en = "")
    String EXPECTATION_FAILED = "expectation_failed"; // 417
    @Description(zh = "", en = "")
    String I_AM_A_TEAPOT = "i_am_a_teapot"; // 418
    @Deprecated
    @Description(zh = "", en = "")
    String INSUFFICIENT_SPACE_ON_RESOURCE = "insufficient_space_on_resource"; // 419
    @Deprecated
    @Description(zh = "", en = "")
    String METHOD_FAILURE = "method_failure"; // 420
    @Deprecated
    @Description(zh = "", en = "")
    String DESTINATION_LOCKED = "destination_locked"; // 421
    @Description(zh = "", en = "")
    String UNPROCESSABLE_ENTITY = "unprocessable_entity"; // 422
    @Description(zh = "", en = "")
    String LOCKED = "locked"; // 423
    @Description(zh = "", en = "")
    String FAILED_DEPENDENCY = "failed_dependency"; // 424
    @Description(zh = "", en = "")
    String TOO_EARLY = "too_early"; // 425
    @Description(zh = "", en = "")
    String UPGRADE_REQUIRED = "upgrade_required"; // 426
    @Description(zh = "", en = "")
    String PRECONDITION_REQUIRED = "precondition_required"; // 428
    @Description(zh = "", en = "")
    String TOO_MANY_REQUESTS = "too_many_requests"; // 429
    @Description(zh = "", en = "")
    String REQUEST_HEADER_FIELDS_TOO_LARGE = "request_header_fields_too_large"; // 431
    @Description(zh = "", en = "")
    String UNAVAILABLE_FOR_LEGAL_REASONS = "unavailable_for_legal_reasons"; // 451

    // http 5xx ServerError
    @Description(zh = "", en = "")
    String INTERNAL_SERVER_ERROR = "internal_server_error"; // 500
    @Description(zh = "", en = "")
    String NOT_IMPLEMENTED = "not_implemented"; // 501
    @Description(zh = "", en = "")
    String BAD_GATEWAY = "bad_gateway"; // 502
    @Description(zh = "", en = "")
    String SERVICE_UNAVAILABLE = "service_unavailable"; // 503
    @Description(zh = "", en = "")
    String GATEWAY_TIMEOUT = "gateway_timeout"; // 504
    @Description(zh = "", en = "")
    String HTTP_VERSION_NOT_SUPPORTED = "http_version_not_supported"; // 505
    @Description(zh = "", en = "")
    String VARIANT_ALSO_NEGOTIATES = "variant_also_negotiates"; // 506
    @Description(zh = "", en = "")
    String INSUFFICIENT_STORAGE = "insufficient_storage"; // 507
    @Description(zh = "", en = "")
    String LOOP_DETECTED = "loop_detected"; // 508
    @Description(zh = "", en = "")
    String BANDWIDTH_LIMIT_EXCEEDED = "bandwidth_limit_exceeded"; // 509
    @Description(zh = "", en = "")
    String NOT_EXTENDED = "not_extended"; // 510
    @Description(zh = "", en = "")
    String NETWORK_AUTHENTICATION_REQUIRED = "network_authentication_required"; // 511

}
