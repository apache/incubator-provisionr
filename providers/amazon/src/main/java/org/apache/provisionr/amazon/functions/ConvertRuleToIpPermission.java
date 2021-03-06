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

package org.apache.provisionr.amazon.functions;

import com.amazonaws.services.ec2.model.IpPermission;
import com.google.common.base.Function;
import static com.google.common.base.Preconditions.checkNotNull;
import org.apache.provisionr.api.network.Rule;

public enum ConvertRuleToIpPermission implements Function<Rule, IpPermission> {
    FUNCTION;

    @Override
    public IpPermission apply(Rule rule) {
        checkNotNull(rule, "rule is null");

        IpPermission permission = new IpPermission()
            .withIpProtocol(rule.getProtocol().toString().toLowerCase())
            .withIpRanges(rule.getCidr());

        if (!rule.getPorts().isEmpty()) {
            permission.withFromPort(rule.getPorts().lowerEndpoint())
                .withToPort(rule.getPorts().upperEndpoint());
        } else {
            permission.withFromPort(-1).withToPort(-1);
        }
        return permission;
    }
}
