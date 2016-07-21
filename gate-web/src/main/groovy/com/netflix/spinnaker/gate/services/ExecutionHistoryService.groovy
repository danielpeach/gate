/*
 * Copyright 2015 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.netflix.spinnaker.gate.services

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import com.google.common.base.Preconditions
import com.netflix.spinnaker.gate.services.commands.HystrixFactory
import com.netflix.spinnaker.gate.services.internal.OrcaService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@CompileStatic
@Component
@Slf4j
class ExecutionHistoryService {
  @Autowired
  OrcaService orcaService

  List getTasks(String app, Integer limit, String statuses) {
    Preconditions.checkNotNull(app)

    def command = HystrixFactory.newListCommand("taskExecutionHistory", "getTasksForApp") {
      orcaService.getTasks(app, limit, statuses)
    }
    return command.execute()
  }

  List getPipelines(String app, Integer limit, String statuses) {
    Preconditions.checkNotNull(app)
    def command = HystrixFactory.newListCommand("pipelineExecutionHistory", "getPipelinesForApp-$app") {
      orcaService.getPipelines(app, limit, statuses)
    }
    return command.execute()
  }
}
