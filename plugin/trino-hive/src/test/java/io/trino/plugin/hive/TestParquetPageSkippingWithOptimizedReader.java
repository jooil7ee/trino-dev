/*
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
package io.trino.plugin.hive;

import com.google.common.collect.ImmutableMap;
import io.trino.testing.QueryRunner;

public class TestParquetPageSkippingWithOptimizedReader
        extends AbstractTestParquetPageSkipping
{
    @Override
    protected QueryRunner createQueryRunner()
            throws Exception
    {
        return HiveQueryRunner.builder()
                .setHiveProperties(
                        ImmutableMap.of(
                                // TODO (https://github.com/trinodb/trino/issues/9359) use optimized writer
                                "parquet.optimized-writer.enabled", "false",
                                "parquet.use-column-index", "true",
                                "parquet.max-buffer-size", "1MB",
                                "parquet.optimized-reader.enabled", "true"))
                .build();
    }
}
