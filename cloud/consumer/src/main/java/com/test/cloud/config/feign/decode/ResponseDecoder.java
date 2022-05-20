package com.test.cloud.config.feign.decode;

import com.google.common.base.Strings;
import com.iminling.common.json.JsonUtil;
import com.iminling.core.config.value.ResultModel;
import feign.FeignException;
import feign.Response;
import feign.Util;
import feign.codec.DecodeException;
import feign.codec.Decoder;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

/**
 * @author yslao@outlook.com
 * @since 2022/4/24
 */
public class ResponseDecoder implements Decoder {

    private Decoder delegate;

    public ResponseDecoder(Decoder delegate) {
        this.delegate = delegate;
    }

    @Override
    public Object decode(Response response, Type type) throws IOException, DecodeException, FeignException {
        Type rawType = type;
        if (type instanceof ParameterizedType) {
            rawType = ((ParameterizedType) type).getRawType();
        }
        if (ResultModel.class.isAssignableFrom((Class) rawType)) {
            return delegate.decode(response, type);
        } else {
            if (response.body() == null) {
                return null;
            }
            String body = Util.toString(response.body().asReader(StandardCharsets.UTF_8));
            if (Strings.isNullOrEmpty(body)) {
                return null;
            }
            // 将json字符串转换为对象
            ResultModel<?> resultModel = JsonUtil.str2Obj(body, ResultModel.class);
            Object data = resultModel.getData();
            if (data == null) {
                return null;
            } else if (data instanceof String) {
                return data;
            } else {
                byte[] bytes = JsonUtil.getInstant().writeValueAsBytes(data);
                feign.Response wrapResponse = feign.Response.builder()
                    .headers(response.headers())
                    .reason(response.reason())
                    .request(response.request())
                    .status(response.status())
                    .body(bytes)
                    .build();
                return delegate.decode(wrapResponse, type);
            }
        }
    }
}
