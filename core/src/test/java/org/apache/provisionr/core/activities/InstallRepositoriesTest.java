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

package org.apache.provisionr.core.activities;

import org.apache.provisionr.api.pool.Pool;
import org.apache.provisionr.api.software.Repository;
import org.apache.provisionr.api.software.Software;
import org.apache.provisionr.core.activities.InstallRepositories;
import org.apache.provisionr.core.activities.PuppetActivity;
import static org.fest.assertions.api.Assertions.assertThat;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class InstallRepositoriesTest {

    @Test
    public void testCreatePuppetScript() throws Exception {
        Repository repository = Repository.builder()
            .name("bigtop")
            .addEntry("deb http://bigtop.s3.amazonaws.com/releases/0.5.0/ubuntu/lucid/x86_64  bigtop contrib")
            .key("-----BEGIN PGP PUBLIC KEY BLOCK-----\n" +
                "Version: GnuPG v1.4.10 (GNU/Linux)\n" +
                "\n" +
                "[....]")
            .createRepository();

        Pool pool = mock(Pool.class);
        when(pool.getSoftware()).thenReturn(Software.builder().repository(repository).createSoftware());

        PuppetActivity activity = new InstallRepositories();
        String content = activity.createPuppetScript(pool, null);

        assertThat(content).contains("apt::repository { \"bigtop\":\n" +
            "  content => \"deb http://bigtop.s3.amazonaws.com/releases/0.5.0/ubuntu/lucid/x86_64  bigtop contrib\\n\",\n" +
            "  key => \"-----BEGIN PGP PUBLIC KEY BLOCK-----\\nVersion: GnuPG v1.4.10 (GNU/Linux)\\n\\n[....]\"\n" +
            "}");
    }

}
