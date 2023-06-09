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
package io.trino.parquet.reader;

import io.trino.parquet.PrimitiveField;
import io.trino.spi.block.BlockBuilder;
import io.trino.spi.type.Type;

import static io.trino.spi.type.DoubleType.DOUBLE;
import static io.trino.spi.type.RealType.REAL;
import static java.lang.Float.floatToRawIntBits;

public class FloatColumnReader
        extends PrimitiveColumnReader
{
    public FloatColumnReader(PrimitiveField field)
    {
        super(field);
    }

    @Override
    protected void readValue(BlockBuilder blockBuilder, Type type)
    {
        if (type == REAL) {
            type.writeLong(blockBuilder, floatToRawIntBits(valuesReader.readFloat()));
        }
        else if (type == DOUBLE) {
            type.writeDouble(blockBuilder, valuesReader.readFloat());
        }
        else {
            throw new VerifyError("Unsupported type " + type);
        }
    }
}
