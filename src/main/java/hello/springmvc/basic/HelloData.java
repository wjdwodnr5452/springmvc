package hello.springmvc.basic;

import lombok.Data;

@Data // 롬복 @Data getter, setter, tostring 자동으로 적용
public class HelloData {
    private String username;
    private int age;
}
