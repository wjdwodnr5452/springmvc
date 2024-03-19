# 로깅
- 운영 시스템에서는 System.out.println() 같은 시스템 콘솔을 사용해서 필요한 정보를 출력하지 않고, 별도의 로깅 라이브러리를 사용해서 로그를 출력한다.

##### 로깅 라이브러리
스프링 부트 라이브러리를 사용하면 스프링 부트 로깅 라이브러리가 함께 포함됨

- SLF4J
- Logback

##### 로그 사용법
```
@RestController
public class LogTestController {
   private  final Logger log = LoggerFactory.getLogger(getClass());
   @RequestMapping("/log-test")
    public String logTest(){
       String name = "Spring";

       log.trace("trace log={}", name);
       log.debug("debug log={}", name);
       log.info("info log={}", name);
       log.warn("warn log={}", name);
       log.error("error log={}", name);

       return "ok";
   }
}
```

##### log 레벨 설정
- application.properties
```
#전체 로그 레벨 설정(기본 info)
logging.level.root=info

#hello.springmvc 패키지와 그 하위 로그 레벨 설정 (로컬에서는 debug 레벨을 설정, 운영서버에는 info 레벨을 설정)
logging.level.hello.springmvc=debug
```
- 로그 출력되는 포맷 (시간, 로그 레벨, 프로세스 id, 쓰레드 명, 클래스명, 로그 메시지)
![image](https://github.com/wjdwodnr5452/springmvc/assets/90361061/641c3d42-fe9a-459a-86e8-9d7cad17dabb)

- 로그 레벨 : TRACE > DEBUG > INFO > WARN > ERROR
- 개발서버는 DEBUG, 운영서버는 INFO 출력

##### @RestController
- @Controller 는 반환값이 String 이면 뷰 이름으로 인식됨 그래서 뷰를 찾고 뷰가 랜더링 된다.
- @RestController는 반환 값으로 뷰를 찾는 것이 아니라 HTTP 메시지 바디에 바로 입력한다. 따라서 실행 결화로 ok 메시지를 받을 수 있다.

##### @Slf4j
- @Slf4j 사용하면 private  final Logger log = LoggerFactory.getLogger(getClass()); 생략 가능

##### 올바른 로그 사용법
- log.debug("debug log="+ name);
  - 로그 출력 레벨을 info로 설정해도 해당 코드에 있는 "debug log="+ name 가 실제 실행이 되어 버린다. 결과적으로 문자 더하기 연산이 발생한다.
- log.debug("debug log={}", name);
  - 로그 출력 레벨을 info로 설정하면 아무일도 발생하지 않는다. 따라서 앞과 같은 의미없는 연산이 발생하지 않는다.
 
 ##### 로그 사용시 장점
 - 쓰레드 정보, 클래스 이름 같은 부가 정보를 함께 볼수 있고, 출력 모양을 조정할 수 있음
 - 로그 레벨에 따라 개발 서버에는 모든 로그를 출력하고, 운영 서버에서는 출력하지 않고 등 로그를 상황에 맞게 조절 할 수 있다.
 - 시스템 아웃 콘솔에만 출력하는 것이 아니라, 파일이나 네트워크 등, 로그를 별도의 위치에 남길 수 있다. 특히 파일로 남길 때는 일별, 특정 용량에 따라 로그를 분할 하는것도 가능하다.
 - 성능도 일단 System.out 보다 좋다.


# 요청 매핑

##### @RestController
- @Controller 는 반환값이 String 이면 뷰 이름으로 인식됨 그래서 뷰를 찾고 뷰가 랜더링 된다.
- @RestController는 반환 값으로 뷰를 찾는 것이 아니라 HTTP 메시지 바디에 바로 입력한다. 따라서 실행 결화로 ok 메시지를 받을 수 있다.

##### @RequestMapping("/hello-basic")
- /hello-basic URL 호출이 오면 이 메서드가 실행되도록 매핑한다.
- 대부분의 속성을 배열로 제공하므로 다중 설정이 가능하다.

/hello-basic, /hello-basic/ 다른 url 이지만 스프링은 같은 요청으로 매핑한다.

##### HTTP 메서드
- @RequestMapping에 method 속성으로 HTTP 메서드를 지정하지 않으면 HTTP 메서드와 무관하게 호출한다
  예) get, post, put, delete 모두 허용

##### 메서드 지정
```
 @RequestMapping(value = "/hello-basic", method = RequestMethod.GET)
```

##### HTTP 메서드 지정 축약
```
   /**
     * 편리한 축약 애노테이션 (코드보기)
     * @GetMapping
     * @PostMapping
     * @PutMapping
     * @DeleteMapping
     * @PatchMapping
     */
    @GetMapping(value = "/mapping-get-v2")
    public String mappingGetV2() {
        log.info("mapping-get-v2");
        return "ok";
    }
```


##### PathVariable(경로 변수) 사용
```
  /**
     * PathVariable 사용
     * 변수명이 같으면 생략 가능
     * @PathVariable("userId") String userId -> @PathVariable String userId
     */
    @GetMapping("/mapping/{userId}")
    public String mappingPath(@PathVariable("userId") String data){
        log.info("mappingPath userId={}", data);
        return "ok";
    }
```
- 최근 HTTP API는 다음과 같이 리소스 경로에 식별자를 넣는 스타일을 선호
- @PathVariable 의 이름과 파라미터 이름이 같으면 생략 가능
```
  @GetMapping("/mapping/{userId}")
    public String mappingPath(@PathVariable String userId){
        log.info("mappingPath userId={}", userId);
        return "ok";
    }
```

# 요청 매핑 - API 예시

#### 회원 관리 API
- 회원 목록 조회 : GET '/users'
- 회원 등록 : POST '/users/{userId}'
- 회원 조회 : GET '/users/{userId}'
- 회원 수정 : PATCH '/users/{userId}'
- 회원 삭제 : DELETE '/users/{userId}'

# HTTP 요청 - 기본, 헤더 조회

#### MultyValueMap
- MAP과 유사한데, 하나의 키에 여러 값을 받을 수 있다.
- HTTP header, HTTP 쿼리 파라미터와 같이 하나의 키에 여러 값을 받을 때 사용
  - keyA=value1&keyA=value2

# HTTP 요청 파라미터 - @RequestParam
- @RequestParam : 파라미터 이름으로 바인딩
- @ResponseBody : View 조회를 무시하고, HTTP message body에 직접 해당 내용 입력

HTTP 파라미터 이름이 변수 이름과 같으면 @RequestParam(name="username") 생략 가능
- 생략 :  @RequestParam String username

####  requestParamDefault
- 파라미터에 값이 없는 경우 defaultValue를 사용하면 기본 값을 적용
```
 @RequestParam(required = true, defaultValue = "guest")
``


  



