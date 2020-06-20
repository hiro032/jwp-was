package http;

import http.common.HttpHeader;
import http.response.ContentType;
import http.response.HttpStatus;
import http.response.ResponseMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileIoUtils;


import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;

class ResponseMessageTest {

    private static final Logger logger = LoggerFactory.getLogger(ResponseMessageTest.class);

    @DisplayName("200 상태, index.html을 바디로 하는 응답 메세지 생성하여 출력")
    @Test
    void test_sendingResponseMessage_with_200() throws IOException, URISyntaxException {
        // given
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] body = FileIoUtils.loadFileFromClasspath("./templates/index.html");
        ResponseMessage responseMessage = new ResponseMessage(new DataOutputStream(output));
        // when
        responseMessage.responseWith(HttpStatus.OK, body, ContentType.HTML);
        // then
        logger.debug("response message: {}", output.toString());
    }

    @DisplayName("302 상태, 리다이렉트url /index.html로 하는 응답 메세지 생성하여 출력")
    @Test
    void test_sendingResponseMessage_with_302() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ResponseMessage responseMessage = new ResponseMessage(new DataOutputStream(output));
        // when
        responseMessage.redirectTo("/index.html");
        // then
        logger.debug("response message: {}", output.toString());
    }

    @DisplayName("404 상태, 지정한 리소스를 찾을 수 없다는 응답 메세지 생성하여 출력")
    @Test
    void test_sendingResponseMessage_with_404() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ResponseMessage responseMessage = new ResponseMessage(new DataOutputStream(output));
        // when
        responseMessage.responseWith(HttpStatus.NOT_FOUND, "Not Found".getBytes(), ContentType.PLAIN);
        // then
        logger.debug("response message: {}", output.toString());
    }

    @DisplayName("클라이언트에 쿠키를 전송하기 위해 헤더 설정")
    @Test
    void test_setHeader() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ResponseMessage responseMessage = new ResponseMessage(new DataOutputStream(output));
        // when
        responseMessage.setHeader(HttpHeader.SET_COOKIE,"logined=true; Path=/");
        responseMessage.write(HttpStatus.OK);
        // then
        assertThat(output.toString()).isEqualTo("HTTP/1.1 200 OK\r\n" + "Set-Cookie: logined=true; Path=/\r\n" + "\r\n"
        );
    }
}