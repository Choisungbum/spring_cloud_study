package com.example.user_service.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Data
//@AllArgsConstructor
//@NoArgsConstructor
public class Greeting {
    // yaml 파일에 등록된 값을 가져오는 방법
    // 2. 해당 데이터를 가져올 클래스를 선언 후 
    // 다음과 같이 @Value 어노테이션을 사용하여 가져온다
    @Value("${greeting.message}")
    private String message;
}
