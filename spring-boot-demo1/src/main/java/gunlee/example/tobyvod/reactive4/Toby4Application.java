package gunlee.example.tobyvod.reactive4;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import javax.servlet.AsyncContext;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Executors;

/**
 * DeferredResult는 thread를 만들지 않기 때문에 특정 목적에서 유용하다.
 *
 *
 */
@SpringBootApplication
@EnableAsync
@Slf4j
public class Toby4Application {

    @Configuration
    public static class FilterConfig {
        @Bean
        public FilterRegistrationBean getFilterRegistrationBean() {
            FilterRegistrationBean registrationBean = new FilterRegistrationBean(new BlankFilter());
            // registrationBean.addUrlPatterns("/*"); // 서블릿 등록 빈 처럼 패턴을 지정해 줄 수 있다.
            return registrationBean;
        }
    }

    public static class BlankFilter implements Filter {
        @Override
        public void init(javax.servlet.FilterConfig filterConfig) throws ServletException {

        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            chain.doFilter(request, response);

//            AsyncContext aCtx = request.getAsyncContext();
//            aCtx.addListener(new AsyncListener() {
//                @Override
//                public void onComplete(AsyncEvent event) throws IOException {
//                    System.out.println("[My] onComplete");
//
//                }
//
//                @Override
//                public void onTimeout(AsyncEvent event) throws IOException {
//                    System.out.println("[My] onTimeout");
//
//                }
//
//                @Override
//                public void onError(AsyncEvent event) throws IOException {
//                    System.out.println("[My] onError");
//
//                }
//
//                @Override
//                public void onStartAsync(AsyncEvent event) throws IOException {
//                    System.out.println("[My] onStartAsync");
//                }
//            });
        }

        @Override
        public void destroy() {

        }
    }

    @RestController
    public static class MyController {

        @GetMapping("/callable")
        public String callable() throws InterruptedException {
            log.info("async");
            Thread.sleep(2000);
            log.info("async-2");
            return "hello";
        }

        @GetMapping("/callableAsync")
        public Callable<String> callableAsync(HttpServletRequest request) throws InterruptedException {
            AsyncContext ctx = request.startAsync();

//            AsyncContext aCtx = request.getAsyncContext();
//            aCtx.addListener(new AsyncListener() {
//                @Override
//                public void onComplete(AsyncEvent event) throws IOException {
//                    System.out.println("[My] onComplete");
//
//                }
//
//                @Override
//                public void onTimeout(AsyncEvent event) throws IOException {
//                    System.out.println("[My] onTimeout");
//
//                }
//
//                @Override
//                public void onError(AsyncEvent event) throws IOException {
//                    System.out.println("[My] onError");
//
//                }
//
//                @Override
//                public void onStartAsync(AsyncEvent event) throws IOException {
//                    System.out.println("[My] onStartAsync");
//                }
//            });

            log.info("callable");
            return () -> {
                log.info("async");
                Thread.sleep(2000);
                log.info("async-2");
                return "hello";
            };
        }

        Queue<DeferredResult<String>> results = new ConcurrentLinkedDeque<>();

        @GetMapping("/dr")
        public DeferredResult<String> dr() throws InterruptedException {
            log.info("dr");
            DeferredResult<String> dr = new DeferredResult<>(600000L);
            results.add(dr);
            return dr;
        }

        @GetMapping("/dr/count")
        public String drcount() {
            return String.valueOf(results.size());
        }

        @GetMapping("/dr/event")
        public String drevent(String msg) {
            for (DeferredResult<String> dr : results) {
                dr.setResult("Hello " + msg);
                results.remove(dr);
            }
            return "OK";
        }

        // HTTP streaming
        // ResponseBodyEmitter, SseEmitter, StreamingResponseBody
        // 응답을 여러번 보내는 기술
        @GetMapping("/emitter")
        public ResponseBodyEmitter emitter() throws InterruptedException {
            ResponseBodyEmitter emitter = new ResponseBodyEmitter();

            Executors.newSingleThreadExecutor().submit(() -> {
                for(int i=1; i<=50; i++) {
                    try {
                        emitter.send("<p>Stream " + i + "</p>");
                        Thread.sleep(100);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            return emitter;
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(Toby4Application.class, args);
    }

}

/**
 * Request가 두가지 종류이며 startAsync 호출에 리스너를 걸던지. 우째든지 해보자고..
 * 아님 AsyncContextImpl이나 AsyncContext를 상속한 클래스의 setStarted에서 해보던지..
 * 여기서 리스너를 등록하는 것도 방법일 듯 함..
 *
 * ► txid    = z4ubnfr1had0vb
 ► objName = /GunMac-2.local/Toby4App
 ► endtime = 20170209 19:14:54.078
 ► elapsed = 47 ms
 ► service = /callableAsync
 ► ipaddr=0.0.0.0, userid=2552704822505855920
 ► cpu=45 ms, kbytes=3971
 ► userAgent=Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36
 ► group=/**
 ------------------------------------------------------------------------------------------
 p#      #    	  TIME         T-GAP   CPU          CONTENTS
 ------------------------------------------------------------------------------------------
 [******] 19:14:54.031        0      0  start transaction
 -    [000000] 19:14:54.033        2      0  Request.requestURI [0ms] -- org.apache.coyote.Request.requestURI()Lorg/apache/tomcat/util/buf/MessageBytes;
 -    [000001] 19:14:54.033        0      0  Request.method [0ms] -- org.apache.coyote.Request.method()Lorg/apache/tomcat/util/buf/MessageBytes;
 -    [000002] 19:14:54.033        0      0  Request.queryString [0ms] -- org.apache.coyote.Request.queryString()Lorg/apache/tomcat/util/buf/MessageBytes;
 -    [000003] 19:14:54.033        0      0  Request.contentType [0ms] -- org.apache.coyote.Request.contentType()Lorg/apache/tomcat/util/buf/MessageBytes;
 -    [000004] 19:14:54.033        0      0  Request.action [0ms] -- org.apache.coyote.Request.action(Lorg/apache/coyote/ActionCode;Ljava/lang/Object;)V
 [000004] [000005] 19:14:54.033        0      0   Request.remoteAddr [0ms] -- org.apache.coyote.Request.remoteAddr()Lorg/apache/tomcat/util/buf/MessageBytes;
 -    [000006] 19:14:54.033        0      0  Request.remoteAddr [0ms] -- org.apache.coyote.Request.remoteAddr()Lorg/apache/tomcat/util/buf/MessageBytes;
 -    [000007] 19:14:54.034        1      0  Toby4Application$BlankFilter.doFilter [43ms] -- gunlee.example.tobyvod.reactive4.Toby4Application$BlankFilter.doFilter(Ljavax/servlet/ServletRequest;Ljavax/servlet/ServletResponse;Ljavax/servlet/FilterChain;)V
 [000007] [000008] 19:14:54.034        0      0   Request.isAsyncSupported [0ms] -- org.apache.catalina.connector.Request.isAsyncSupported()Z
 [000007] [000009] 19:14:54.034        0      0   Request.isAsyncSupported [0ms] -- org.apache.catalina.connector.Request.isAsyncSupported()Z
 [000007] [000010] 19:14:54.034        0      0   Request.method [0ms] -- org.apache.coyote.Request.method()Lorg/apache/tomcat/util/buf/MessageBytes;
 [000007] [000011] 19:14:54.034        0      0   Request.method [0ms] -- org.apache.coyote.Request.method()Lorg/apache/tomcat/util/buf/MessageBytes;
 [000007] [000012] 19:14:54.036        2      0   Request.method [0ms] -- org.apache.coyote.Request.method()Lorg/apache/tomcat/util/buf/MessageBytes;
 [000007] [000013] 19:14:54.036        0      0   Request.requestURI [0ms] -- org.apache.coyote.Request.requestURI()Lorg/apache/tomcat/util/buf/MessageBytes;
 [000007] [000014] 19:14:54.037        1      0   Request.requestURI [0ms] -- org.apache.coyote.Request.requestURI()Lorg/apache/tomcat/util/buf/MessageBytes;
 [000007] [000015] 19:14:54.037        0      0   Request.requestURI [0ms] -- org.apache.coyote.Request.requestURI()Lorg/apache/tomcat/util/buf/MessageBytes;
 [000007] [000016] 19:14:54.037        0      0   Request.requestURI [0ms] -- org.apache.coyote.Request.requestURI()Lorg/apache/tomcat/util/buf/MessageBytes;
 [000007] [000017] 19:14:54.038        1      0   Request.requestURI [0ms] -- org.apache.coyote.Request.requestURI()Lorg/apache/tomcat/util/buf/MessageBytes;
 [000007] [000018] 19:14:54.038        0      0   Request.requestURI [0ms] -- org.apache.coyote.Request.requestURI()Lorg/apache/tomcat/util/buf/MessageBytes;
 [000007] [000019] 19:14:54.038        0      0   Request.requestURI [0ms] -- org.apache.coyote.Request.requestURI()Lorg/apache/tomcat/util/buf/MessageBytes;
 [000007] [000020] 19:14:54.038        0      0   Request.requestURI [0ms] -- org.apache.coyote.Request.requestURI()Lorg/apache/tomcat/util/buf/MessageBytes;
 [000007] [000021] 19:14:54.038        0      0   Request.method [0ms] -- org.apache.coyote.Request.method()Lorg/apache/tomcat/util/buf/MessageBytes;
 [000007] [000022] 19:14:54.038        0      0   Request.requestURI [0ms] -- org.apache.coyote.Request.requestURI()Lorg/apache/tomcat/util/buf/MessageBytes;
 [000007] [000023] 19:14:54.038        0      0   Request.requestURI [0ms] -- org.apache.coyote.Request.requestURI()Lorg/apache/tomcat/util/buf/MessageBytes;
 [000007] [000024] 19:14:54.039        1      0   Request.requestURI [0ms] -- org.apache.coyote.Request.requestURI()Lorg/apache/tomcat/util/buf/MessageBytes;
 [000007] [000025] 19:14:54.039        0      0   Request.requestURI [0ms] -- org.apache.coyote.Request.requestURI()Lorg/apache/tomcat/util/buf/MessageBytes;
 [000007] [000026] 19:14:54.041        2      0   Request.requestURI [0ms] -- org.apache.coyote.Request.requestURI()Lorg/apache/tomcat/util/buf/MessageBytes;
 [000007] [000027] 19:14:54.041        0      0   Request.requestURI [0ms] -- org.apache.coyote.Request.requestURI()Lorg/apache/tomcat/util/buf/MessageBytes;
 [000007] [000028] 19:14:54.041        0      0   Request.requestURI [0ms] -- org.apache.coyote.Request.requestURI()Lorg/apache/tomcat/util/buf/MessageBytes;
 [000007] [000029] 19:14:54.041        0      0   Request.requestURI [0ms] -- org.apache.coyote.Request.requestURI()Lorg/apache/tomcat/util/buf/MessageBytes;
 [000007] [000030] 19:14:54.041        0      0   Request.method [0ms] -- org.apache.coyote.Request.method()Lorg/apache/tomcat/util/buf/MessageBytes;
 [000007] [000031] 19:14:54.043        2      0   Request.method [0ms] -- org.apache.coyote.Request.method()Lorg/apache/tomcat/util/buf/MessageBytes;
 [000007] [000032] 19:14:54.043        0      0   Request.method [0ms] -- org.apache.coyote.Request.method()Lorg/apache/tomcat/util/buf/MessageBytes;
 [000007] [000033] 19:14:54.057       14      0   Toby4Application$MyController.callableAsync [1ms] -- gunlee.example.tobyvod.reactive4.Toby4Application$MyController.callableAsync(Ljavax/servlet/http/HttpServletRequest;)Ljava/util/concurrent/Callable;
 [000007] [000034] 19:14:54.061        4      0   Request.isAsyncSupported [0ms] -- org.apache.catalina.connector.Request.isAsyncSupported()Z
 [000007] [000035] 19:14:54.061        0      0   Request.startAsync [14ms] -- org.apache.catalina.connector.Request.startAsync(Ljavax/servlet/ServletRequest;Ljavax/servlet/ServletResponse;)Ljavax/servlet/AsyncContext;
 [000035] [000036] 19:14:54.061        0      0    Request.isAsyncSupported [0ms] -- org.apache.catalina.connector.Request.isAsyncSupported()Z
 [000035] [000037] 19:14:54.075       14      0    Request.action [0ms] -- org.apache.coyote.Request.action(Lorg/apache/coyote/ActionCode;Ljava/lang/Object;)V
 [000035] [000038] 19:14:54.075        0      0    Request.action [0ms] -- org.apache.coyote.Request.action(Lorg/apache/coyote/ActionCode;Ljava/lang/Object;)V
 [000007] [000039] 19:14:54.075        0      0   AsyncContextImpl.addListener [1ms] -- org.apache.catalina.core.AsyncContextImpl.addListener(Ljavax/servlet/AsyncListener;)V
 [000007] [000040] 19:14:54.076        1      0   Request.isAsyncStarted [1ms] -- org.apache.catalina.connector.Request.isAsyncStarted()Z
 [000040] [000041] 19:14:54.076        0      0    AsyncContextImpl.isStarted [0ms] -- org.apache.catalina.core.AsyncContextImpl.isStarted()Z
 [000041] [000042] 19:14:54.076        0      0     Request.action [0ms] -- org.apache.coyote.Request.action(Lorg/apache/coyote/ActionCode;Ljava/lang/Object;)V
 [000040] [000043] 19:14:54.077        1      0    >>>> isStarted : true
 [000007] [000044] 19:14:54.077        0      0   Request.isAsyncStarted [0ms] -- org.apache.catalina.connector.Request.isAsyncStarted()Z
 [000044] [000045] 19:14:54.077        0      0    AsyncContextImpl.isStarted [0ms] -- org.apache.catalina.core.AsyncContextImpl.isStarted()Z
 [000045] [000046] 19:14:54.077        0      0     Request.action [0ms] -- org.apache.coyote.Request.action(Lorg/apache/coyote/ActionCode;Ljava/lang/Object;)V
 [000044] [000047] 19:14:54.077        0      0    >>>> isStarted : true
 [000007] [000048] 19:14:54.077        0      0   Request.isAsyncStarted [0ms] -- org.apache.catalina.connector.Request.isAsyncStarted()Z
 [000048] [000049] 19:14:54.077        0      0    AsyncContextImpl.isStarted [0ms] -- org.apache.catalina.core.AsyncContextImpl.isStarted()Z
 [000049] [000050] 19:14:54.077        0      0     Request.action [0ms] -- org.apache.coyote.Request.action(Lorg/apache/coyote/ActionCode;Ljava/lang/Object;)V
 [000048] [000051] 19:14:54.077        0      0    >>>> isStarted : true
 [000007] [000052] 19:14:54.077        0      0   Request.isAsyncStarted [0ms] -- org.apache.catalina.connector.Request.isAsyncStarted()Z
 [000052] [000053] 19:14:54.077        0      0    AsyncContextImpl.isStarted [0ms] -- org.apache.catalina.core.AsyncContextImpl.isStarted()Z
 [000053] [000054] 19:14:54.077        0      0     Request.action [0ms] -- org.apache.coyote.Request.action(Lorg/apache/coyote/ActionCode;Ljava/lang/Object;)V
 [000052] [000055] 19:14:54.077        0      0    >>>> isStarted : true
 [000007] [000056] 19:14:54.077        0      0   Request.requestURI [0ms] -- org.apache.coyote.Request.requestURI()Lorg/apache/tomcat/util/buf/MessageBytes;
 [000007] [000057] 19:14:54.077        0      0   Request.method [0ms] -- org.apache.coyote.Request.method()Lorg/apache/tomcat/util/buf/MessageBytes;
 [******] 19:14:54.078        1     45  end of transaction
 ------------------------------------------------------------------------------------------

 */
