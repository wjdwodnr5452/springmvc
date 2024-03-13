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

