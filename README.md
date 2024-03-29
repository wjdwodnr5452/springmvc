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
```

# HTTP 요청 파라미터 - @ModelAttribute

##### 롬복
- @Getter, @Setter, @ToString, @EqualsAndHashCode, @RequiredArgsConstructor 를 자동으로 적용

##### @ModelAttribute - 작동 순서
- HelloData 객체 생성
- 요청 파라미터의 이름으로 HelloData 객체의 프로퍼티를 찾음 그리고 해당 프로퍼티의 setter를 호출해서 파라미터의 값을 입력 함
- 예) 파라미터 이름이 username 이면 setUsername() 메서드를 찾아서 호출하면서 값을 입력

##### 프로퍼티
- 객체에 getUsername(), setUsername() 메서드가 있으면, 이 객체는 username이라는 프로퍼티를 가지고 있다.


#  HTTP 요청 메시지 - 단순 텍스트

##### HTTP message body에 데이터를 직접 담아서 요청
- HTTP API에서 주로 사용, JSON, XML, TEXT
- 데이터 형식은 주로 JSON 사용
- POST, PUT, PATCH

요청 파라미터와 다르게, HTTP 메시지 바디를 통해 데이터가 직접 넘어오는 경우는 @RequestParam, @ModelAttribute를 사용할 수 없다. (HTML Form 형식으로 전달되는 경우는 요청 파라미터로 인정)

##### HTTP 메시지 전송과 읽는 방법
##### 1. V2
- InputStream : HTTP 요청 메시지 바디의 내용을 직접 조회
- OutputStream : HTTP 응답 메시지의 바디에 직접 결과 출력
##### 2. V3
- HttpEntity : HTTP header, body 정보를 편리하게 조회
  - 메시지 바디 정보를 직접 조회
  - 요청 파라미터를 조회하는 기능과 관계 없음 @RequestParma X, @ModelAtttribute X
- HttpEntity는 응답에도 사용 가능
  - 메시지 바디 정보 직접 반환
  - 헤더 정보 포함 가능
  - view 조회X
##### 3. V4
##### @RequestBody
- @RequestBody를 사용하면 HTTP 메시지 바디 정보를 편리하게 조회할 수 있다.

##### 요청 파라미터 vs HTTP 메시지 바디
- 요청 파라미터를 조회하는 기능 : @RequestParam, @ModelAttribute
- HTTP 메시지 바디를 직접 조회하는 기능 : @RequestBody

##### @ResponseBody
- @ResponseBody를 사용하면 응답 결과를 HTTP 메시지 바디에 직접 담아서 전달할 수 있다. 물론 이 경우에도 view를 사용하지 않는다.

# HTTP 요청 메시지 - JSON

##### @RequestBody 객체 파라미터
- @RequestBody HelloData data
- @RequestBody에 직접 만든 객체를 지정할 수 있다.

HttpEntity, @ReqyestBody를 사용하면 HTTP 메시지 컨버터가 HTTP 메시지 바디의 내용을 우리가 원하는 문자나 객체등으로 변환해준다. HTTP 메시지 컨버터는 문자 뿐만 아니라 JSON도 객체로 변환 해주는데, 헤더에 content type인 json이면 처리 해준다.

##### @RequestBody는 생략 불가능
- 스프링은 @ModelAttribute, @RequestParam 해당 생략시 다음과 규칙
  - String, int, Integer 같은 단순 타입 = @RequestParam
  - 나머지 = @ModelAttribute
 
##### @ResponseBody
- 응답 경우에도 @ResponseBody를 사용하면 해당 객체를 HTTP 메시지 바디에 직접 넣어줄 수 있다.

##### @RequestBody 요청
- JSON 요청 -> HTTP 메시지 컨버터 -> 객체

##### @ResponseBody 응답
- 객체 -> HTTP 메시지 컨버터 -> JSON  응답


# HTTP 메시지 컨버터
- 뷰 템플릿으로 HTML을 생성해서 응답하는 것이 아니라, HTTP API 처럼 JSON 데이터를 HTTP 메시지 바디에서 직접 읽거나 쓰는 경우 HTTP 메시지 컨버터를 사용하면 편리하다.

##### @ResponseBody를 사용
- HTTP의 body에 문자 내용을 직접 반환
- viewResolver 대신에 HttpMessageConverter가 동작
- 기본 문자처리 : StringHttpMessageConverter
- 기본 객체처리 : MappingJackson2HttpMessageConverter
- byte 처리 등등 기타 여러 HttpMessageConverter가 기본으로 등록되어 있음


##### HTTP 메시지 컨버터는 HTTP 요청, HTTP 응답 둘 다 사용 된다.

스프링 부트는 다양한 메시지 컨버터를 제공하는데, 대상 클래스 타입과 미디어 타입 들을 체크해서 사용여부를 결정한다. 만약 만족하지 않으면 다음 메시지 컨버터로 우선순위가 넘어간다.

- ByteArrayHttpMessageConverter : byte[] 데이터를 처리한다.
  - 클래스 타입: byte[] , 미디어타입: */* ,
  - 요청 예) @RequestBody byte[] data
  - 응답 예) @ResponseBody return byte[] 쓰기 미디어타입 application/octet-stream
- StringHttpMessageConverter : String 문자로 데이터를 처리한다.
  - 클래스 타입: String , 미디어타입: */*
  - 요청 예) @RequestBody String data
  - 응답 예) @ResponseBody return "ok" 쓰기 미디어타입 text/plain
- MappingJackson2HttpMessageConverter : application/json 
  - 클래스 타입: 객체 또는 HashMap , 미디어타입 application/json 관련
  - 요청 예) @RequestBody HelloData data
  - 응답 예) @ResponseBody return helloData 쓰기 미디어타입 application/json 관련
 
    
# 요청 매핑 핸들러 어뎁터 구조
- HTTP 메시지 컨버터는 어디 시점에서 동작을 할까? 그 부분은 @RequestMapping을 처리하는 핸들러 어댑터인 RequestMappingHandlerAdapter에 있다.

##### RequestMappingHandlerAdapter 동작 방식
- ArgumentResolver
  - HttpServletRequest, Model은 물론이고, @RequestParam, @ModelAttribute 같은 애노테이션 그리고 @RequestBody, HttpEntity 같은 HTTP 메시지를 처리하는 부분까지 매우 큰 유연함을 보여줌
  - 애노테이션 기반 컨트롤러를 처리하는 RequestMappingHandlerAdapter는 바로 ArgumentResolver를 호출해서 컨트롤러(핸들러)가 필요로 하는 다양한 파라미터의 값을 생성한다. 그리고 이렇게 파라미터의 값이 모두 준비되면 컨트롤러를 호출하면서 값을 넘겨준다.
  - ArgumentResolver의 supprtsParameter()를 호출해서 해당 파라미터를 지원하는지 체크하고 지원하면 resolveArgument()를 호출해서 실제 객체를 생성한다. 그리고 이렇게 생성된 객체가 컨트롤러 호출시 넘어가는 것이다.
    

##### HTTP 메시지 컨버터
- HTTP 메시지 컨버터를 사용하는 @RequestBody도 컨트롤러가 필요로 하는 파라미터 값에 사용된다.

1. 요청의 경우
- @RequestBody를 처리하는 ArgumentResolver가 있고, HttpEntity를 처리하는 ArgumentResolver가 있다. 이 ArgumentResolver 들이 HTTP 메시지 컨버터를 사용해서 필요한 객체를 생성하는 것

2. 응답의 경우
- @ResponseBody와 HttpEntity를 처리하는 ReturnValueHandler가 있다.

