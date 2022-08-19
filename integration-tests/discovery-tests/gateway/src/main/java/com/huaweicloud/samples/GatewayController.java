/*

 * Copyright (C) 2020-2022 Huawei Technologies Co., Ltd. All rights reserved.

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.huaweicloud.samples;

import java.time.Duration;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
public class GatewayController {
  private int circuitBreakerCounter = 0;

  private int circuitBreakerErrorCodeCounter = 0;

  @GetMapping(
      path = "/identifierRateLimiting",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<String> identifierRateLimiting() {
    return Mono.just("OK");
  }

  @GetMapping(
      path = "/testCircuitBreaker",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<String> testCircuitBreaker() {
    circuitBreakerCounter++;
    if (circuitBreakerCounter % 3 != 0) {
      return Mono.just("ok");
    }
    throw new RuntimeException("test error");
  }

  @GetMapping(
      path = "/testCircuitBreakerErrorCode",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<ResponseEntity<String>> testCircuitBreakerErrorCode() {
    circuitBreakerErrorCodeCounter++;
    if (circuitBreakerErrorCodeCounter % 3 != 0) {
      return Mono.just(ResponseEntity.status(200).body("ok"));
    }
    return Mono.just(ResponseEntity.status(503).body("fail"));
  }

  @GetMapping(
      path = "/testBulkhead",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<String> testBulkhead() {
    return Mono.delay(Duration.ofMillis(500)).then(Mono.just("ok"));
  }
}
