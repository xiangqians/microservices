package org.xiangqian.microservices.common.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.xiangqian.microservices.common.util.AppUtil;
import org.xiangqian.microservices.common.util.ResourceUtil;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author xiangqian
 * @date 21:07:15 2022/03/26
 */
@Data
@NoArgsConstructor
@Schema(description = "响应信息")
public class Response<T> {

    @Schema(description = "应用程序名称")
    private String appName;

    @Schema(description = "状态码")
    private String code;

    @Schema(description = "消息")
    private String msg;

    @Schema(description = "数据")
    private T data;

    private Response(String appName, String code, String msg, T data) {
        this.appName = appName;
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> Builder<T> builder() {
        return new Builder();
    }

    public static class Builder<T> {
        private static final Map<String, Code.Description> codeCache;

        static {
            try {
                codeCache = new HashMap<>(1024, 1f);
                Set<Class<?>> classes = ResourceUtil.getClasses("org.xiangqian.microservices.**.model");
                for (Class<?> clazz : classes) {
                    if (clazz.isInterface() && Code.class.isAssignableFrom(clazz)) {
                        Field[] fields = clazz.getFields();
                        for (Field field : fields) {
                            codeCache.put(field.get(null).toString(), field.getAnnotation(Code.Description.class));
                        }
                    }
                }
            } catch (Exception e) {
                throw new Error(e);
            }
        }

        private String code;
        private String msg;
        private T data;

        private Builder() {
        }

        public Builder<T> code(String code) {
            this.code = code;
            return this;
        }

        public Builder<T> msg(String msg) {
            this.msg = msg;
            return this;
        }

        public Builder<T> data(T data) {
            this.data = data;
            return this;
        }

        public Response<T> build() {
            if (msg == null) {
                Code.Description description = codeCache.get(code);
                if (description != null) {
                    msg = description.zh();
                }
            }
            return new Response<>(AppUtil.getName(), code, msg, data);
        }
    }

}
