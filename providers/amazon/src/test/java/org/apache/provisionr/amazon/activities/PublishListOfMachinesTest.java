/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.provisionr.amazon.activities;

import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Reservation;
import com.google.common.collect.Lists;
import java.util.List;
import org.activiti.engine.delegate.DelegateExecution;
import org.apache.provisionr.amazon.ProcessVariables;
import org.apache.provisionr.amazon.core.ProviderClientCache;
import org.apache.provisionr.api.pool.Machine;
import org.apache.provisionr.api.provider.Provider;
import org.apache.provisionr.core.CoreProcessVariables;
import org.apache.provisionr.test.ProcessVariablesCollector;
import static org.fest.assertions.api.Assertions.assertThat;
import org.junit.Test;
import org.mockito.Matchers;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PublishListOfMachinesTest {

    @Test
    public void testPublishListOfMachines() throws Exception {
        AmazonEC2 client = mock(AmazonEC2.class);

        ProviderClientCache clientCache = mock(ProviderClientCache.class);
        when(clientCache.get(Matchers.<Provider>any())).thenReturn(client);

        DelegateExecution execution = mock(DelegateExecution.class);
        List<String> instanceIds = Lists.newArrayList("i-123", "i-456");
        when(execution.getVariable(ProcessVariables.INSTANCE_IDS)).thenReturn(instanceIds);

        when(client.describeInstances(Matchers.<DescribeInstancesRequest>any()))
            .thenReturn(new DescribeInstancesResult()
                .withReservations(new Reservation().withInstances(
                    new Instance().withInstanceId("i-123").withPublicDnsName("i1.amazonaws.com")
                        .withPublicIpAddress("1.2.3.4").withPrivateDnsName("i1.internal").withPrivateIpAddress("10.1.2.3"),
                    new Instance().withInstanceId("i-456").withPublicDnsName("i2.amazonaws.com")
                        .withPublicIpAddress("5.6.7.8").withPrivateDnsName("i2.internal").withPrivateIpAddress("10.4.5.6")
                )));

        ProcessVariablesCollector collector = new ProcessVariablesCollector();
        collector.install(execution);

        AmazonActivity activity = new PublishListOfMachines(clientCache);
        activity.execute(client, null /* not used */, execution);

        @SuppressWarnings("unchecked")
        List<Machine> machines = (List<Machine>) collector.getVariable(CoreProcessVariables.MACHINES);

        assertThat(machines).hasSize(2);
        assertThat(machines.get(0).getPublicDnsName()).isEqualTo("i1.amazonaws.com");
    }
}
