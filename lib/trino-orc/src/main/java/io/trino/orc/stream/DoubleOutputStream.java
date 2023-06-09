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
package io.trino.orc.stream;

import com.google.common.collect.ImmutableList;
import io.trino.orc.OrcOutputBuffer;
import io.trino.orc.checkpoint.DoubleStreamCheckpoint;
import io.trino.orc.metadata.CompressionKind;
import io.trino.orc.metadata.OrcColumnId;
import io.trino.orc.metadata.Stream;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkState;
import static io.airlift.slice.SizeOf.instanceSize;
import static io.trino.orc.metadata.Stream.StreamKind.DATA;
import static java.lang.Math.toIntExact;

public class DoubleOutputStream
        implements ValueOutputStream<DoubleStreamCheckpoint>
{
    private static final int INSTANCE_SIZE = instanceSize(DoubleOutputStream.class);
    private final OrcOutputBuffer buffer;
    private final List<DoubleStreamCheckpoint> checkpoints = new ArrayList<>();

    private boolean closed;

    public DoubleOutputStream(CompressionKind compression, int bufferSize)
    {
        this.buffer = new OrcOutputBuffer(compression, bufferSize);
    }

    public void writeDouble(double value)
    {
        checkState(!closed);
        buffer.writeDouble(value);
    }

    @Override
    public void close()
    {
        closed = true;
        buffer.close();
    }

    @Override
    public void recordCheckpoint()
    {
        checkState(!closed);
        checkpoints.add(new DoubleStreamCheckpoint(buffer.getCheckpoint()));
    }

    @Override
    public List<DoubleStreamCheckpoint> getCheckpoints()
    {
        checkState(closed);
        return ImmutableList.copyOf(checkpoints);
    }

    @Override
    public StreamDataOutput getStreamDataOutput(OrcColumnId columnId)
    {
        return new StreamDataOutput(buffer::writeDataTo, new Stream(columnId, DATA, toIntExact(buffer.getOutputDataSize()), false));
    }

    @Override
    public long getBufferedBytes()
    {
        return buffer.estimateOutputDataSize();
    }

    @Override
    public long getRetainedBytes()
    {
        // NOTE: we do not include checkpoints because they should be small and it would be annoying to calculate the size
        return INSTANCE_SIZE + buffer.getRetainedSize();
    }

    @Override
    public void reset()
    {
        closed = false;
        buffer.reset();
        checkpoints.clear();
    }
}
