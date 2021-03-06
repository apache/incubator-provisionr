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

package org.apache.provisionr.cloudstack;


/**
 * Building a pool of VM's on CloudStack requires more knowledge of the cloud's architecture,
 * <p/>
 * You need to know things like in what zone to build the pool, the ID's of service
 * offerings, network offerings, and more.
 * <p/>
 * All the information is passed via Provider Options. This class stores some of the option names.
 */
public final class ProviderOptions {

    private ProviderOptions() {
    }

    public static final String ZONE_ID = "zoneId";
    public static final String TEMPLATE_ID = "templateId";

    public static final String SERVICE_OFFERING_ID = "serviceOfferingId";
    public static final String NETWORK_OFFERING_ID = "networkOfferingId";
}
