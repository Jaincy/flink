/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.flink.core.fs;

import org.apache.flink.util.AbstractCloseableRegistry;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/** Tests for the {@link CloseableRegistry}. */
public class CloseableRegistryTest extends AbstractCloseableRegistryTest<Closeable, Object> {

    @Override
    protected void registerCloseable(final Closeable closeable) throws IOException {
        closeableRegistry.registerCloseable(closeable);
    }

    @Override
    protected AbstractCloseableRegistry<Closeable, Object> createRegistry() {

        return new CloseableRegistry();
    }

    @Override
    protected ProducerThread<Closeable, Object> createProducerThread(
            AbstractCloseableRegistry<Closeable, Object> registry,
            AtomicInteger unclosedCounter,
            int maxStreams) {

        return new ProducerThread<Closeable, Object>(registry, unclosedCounter, maxStreams) {
            @Override
            protected void createAndRegisterStream() throws IOException {
                TestStream testStream = new TestStream(unclosedCounter);
                registry.registerCloseable(testStream);
            }
        };
    }
}
