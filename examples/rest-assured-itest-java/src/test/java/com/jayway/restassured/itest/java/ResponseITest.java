/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jayway.restassured.itest.java;

import com.jayway.restassured.itest.java.support.WithJetty;
import com.jayway.restassured.response.Response;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.jayway.restassured.RestAssured.*;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

public class ResponseITest extends WithJetty {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void whenNoExpectationsDefinedThenGetCanReturnBodyAsString() throws Exception {
        final String body = get("/hello").asString();
        assertEquals("{\"hello\":\"Hello Scalatra\"}", body);
    }

    @Test
    public void whenNoExpectationsDefinedThenGetCanReturnAStringAsByteArray() throws Exception {
        final byte[] expected = "{\"hello\":\"Hello Scalatra\"}".getBytes();
        final byte[] actual = get("/hello").asByteArray();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void whenExpectationsDefinedThenAsStringReturnsCanReturnTheResponseBody() throws Exception {
        final String body = expect().body(equalTo("{\"hello\":\"Hello Scalatra\"}")).get("/hello").asString();

        assertThat(body, containsString("Hello"));
    }

    @Test
    public void whenNoExpectationsDefinedThenPostCanReturnBodyAsString() throws Exception {
        final String body = with().parameters("firstName", "John", "lastName", "Doe").post("/greetXML").andReturn().body().asString();
        assertEquals("<greeting><firstName>John</firstName>\n" +
                "      <lastName>Doe</lastName>\n" +
                "    </greeting>", body);
    }

    @Test
    public void whenNoExpectationsDefinedThenPostWithBodyCanReturnBodyAsString() throws Exception {
        byte[] body = { 23, 42, 127, 123};
        final String actual = given().body(body).then().post("/binaryBody").andReturn().asString();
        assertEquals("23, 42, 127, 123", actual);
    }

    @Test
    public void whenNoExpectationsDefinedThenPutCanReturnBodyAsString() throws Exception {
        final String actual = given().cookies("username", "John", "token", "1234").then().put("/cookie").asString();
        assertEquals("username, token", actual);
    }

    @Test
    public void whenNoExpectationsDefinedThenPutWithBodyCanReturnBodyAsString() throws Exception {
        final String body = given().body("a body").when().put("/body").andReturn().body().asString();
        assertEquals("a body", body);
    }

    @Test
    public void whenNoExpectationsDefinedThenDeleteWithBodyCanReturnBodyAsString() throws Exception {
        final String actual = given().parameters("firstName", "John", "lastName", "Doe").then().delete("/greet").thenReturn().asString();
        assertEquals("{\"greeting\":\"Greetings John Doe\"}", actual);
    }

    @Test
    public void responseSupportsGettingCookies() throws Exception {
        final Response response = get("/setCookies");
        assertEquals(3, response.getCookies().size());
        assertEquals(3, response.cookies().size());
        assertEquals("value1", response.getCookie("key1"));
        assertEquals("value2", response.cookie("key2"));
    }

    @Test
    public void responseSupportsGettingHeaders() throws Exception {
        final Response response = get("/setCookies");
        assertEquals(4, response.getHeaders().size());
        assertEquals(4, response.headers().size());
        assertEquals("text/plain; charset=utf-8", response.getHeader("Content-Type"));
        assertThat(response.header("Server"), containsString("Jetty"));
    }

    @Test
    public void responseSupportsGettingStatusLine() throws Exception {
        final Response response = get("/hello");

        assertThat(response.statusLine(), equalTo("HTTP/1.1 200 OK"));
        assertThat(response.getStatusLine(), equalTo("HTTP/1.1 200 OK"));
    }

    @Test
    public void responseSupportsGettingStatusCode() throws Exception {
        final Response response = get("/hello");

        assertThat(response.statusCode(), equalTo(200));
        assertThat(response.getStatusCode(), equalTo(200));
    }
}
