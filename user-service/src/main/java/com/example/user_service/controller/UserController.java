package com.example.user_service.controller;

import com.example.user_service.dto.UserDto;
import com.example.user_service.entity.UserEntity;
import com.example.user_service.service.UserService;
import com.example.user_service.vo.Greeting;
import com.example.user_service.vo.RequestUser;
import com.example.user_service.vo.ResponseUser;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/") //
@Slf4j
public class UserController {
    // yaml 파일에 등록된 값을 가져오는 방법
    // 1. Environment 객체 선언 후
    // getProperty("가져올 데이터")
    private Environment env;
    private UserService userService;

    @Autowired
    private Greeting greeting;

    @Autowired
    public UserController(Environment env, UserService userService) {
        this.env = env;
        this.userService = userService;
    }

    @GetMapping("/health-check")
    public String status() {
        return String.format("It's Working in User Service on PORT %s (%s)"
                // server.port  local.server.port 차이
                // server.port -> 현재 할당된 포트번호를 출력하여 보여준다
                // local.server.port -> application.yml 에 등록된 server.port 를 보여준다.
                , env.getProperty("local.server.port"), env.getProperty("spring.application.name"));
    }

    @GetMapping("/welcome")
    public String welcome() {
//        return env.getProperty("greeting.message");
        return greeting.getMessage();
    }

    //TODO 강의 종료후 RequestUser 와 UserDto 통합해야함
    @PostMapping("/users")
    public ResponseEntity<ResponseUser> createUser(@RequestBody RequestUser user) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDto userDto = mapper.map(user, UserDto.class);
        log.info(userDto.toString()); // userDto 값 확인
        userService.createUser(userDto);

        ResponseUser responseUser = mapper.map(userDto, ResponseUser.class);

//        return new ResponseEntity(HttpStatus.CREATED);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }

    @GetMapping("/users")
    public ResponseEntity<List<ResponseUser>> getUsers() {
        Iterable<UserEntity> userList = userService.getUserByAll();

        List<ResponseUser> result = new ArrayList<>();
        userList.forEach(v -> {
            result.add(new ModelMapper().map(v, ResponseUser.class));
        });

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // @PathVariable("userId") -> 해당 애노테이션 사용 시 해당 컬럼에 대한 매개변수의 이름 변경을 가능하게 해준다
    @GetMapping("/users/{userId}")
    public ResponseEntity<ResponseUser> getUsers(@PathVariable("userId") String userId) {
        UserDto user = userService.getUserById(userId);

        return ResponseEntity.status(HttpStatus.OK).body(new ModelMapper().map(user, ResponseUser.class));
    }
}
