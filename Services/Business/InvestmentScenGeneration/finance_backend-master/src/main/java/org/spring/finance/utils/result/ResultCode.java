package org.spring.finance.utils.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
// 枚举,封装返回的提示码和提示信息

@Getter
@AllArgsConstructor
public enum ResultCode {
    SUCCESS(20000,"success"),
    //自定义失败信息
    FAILURE(50000, "失败"),
    //通用错误码 50001~50099
    PROGRAM_INSIDE_EXCEPTION(50001, "程序内部异常"),
    REQUEST_PARAM_ERROR(50002, "请求参数错误");
    private final Integer code;
    private final String message;
}
