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
package io.trino.plugin.deltalake;

import io.trino.plugin.deltalake.functions.tablechanges.TableChangesProcessorProvider;
import io.trino.plugin.deltalake.functions.tablechanges.TableChangesTableFunctionHandle;
import io.trino.spi.function.FunctionProvider;
import io.trino.spi.ptf.ConnectorTableFunctionHandle;
import io.trino.spi.ptf.TableFunctionProcessorProvider;

import javax.inject.Inject;

import static java.util.Objects.requireNonNull;

public class DeltaLakeFunctionProvider
        implements FunctionProvider
{
    private final TableChangesProcessorProvider tableChangesProcessorProvider;

    @Inject
    public DeltaLakeFunctionProvider(TableChangesProcessorProvider tableChangesProcessorProvider)
    {
        this.tableChangesProcessorProvider = requireNonNull(tableChangesProcessorProvider, "tableChangesProcessorProvider is null");
    }

    @Override
    public TableFunctionProcessorProvider getTableFunctionProcessorProvider(ConnectorTableFunctionHandle functionHandle)
    {
        if (functionHandle instanceof TableChangesTableFunctionHandle) {
            return tableChangesProcessorProvider;
        }
        throw new UnsupportedOperationException("Unsupported function: " + functionHandle);
    }
}
