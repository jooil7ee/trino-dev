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
package io.trino.orc.metadata.statistics;

import io.trino.orc.metadata.statistics.StatisticsHasher.Hashable;

import java.util.Objects;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkArgument;
import static io.airlift.slice.SizeOf.instanceSize;

public class TimestampStatistics
        implements RangeStatistics<Long>, Hashable
{
    // 1 byte to denote if null + 8 bytes for the value
    public static final long TIMESTAMP_VALUE_BYTES = Byte.BYTES + Long.BYTES;

    private static final int INSTANCE_SIZE = instanceSize(TimestampStatistics.class);

    private final boolean hasMinimum;
    private final boolean hasMaximum;

    private final long minimum;
    private final long maximum;

    public TimestampStatistics(Long minimum, Long maximum)
    {
        checkArgument(minimum == null || maximum == null || minimum <= maximum, "minimum is not less than or equal to maximum: %s, %s", minimum, maximum);

        this.hasMinimum = minimum != null;
        this.minimum = hasMinimum ? minimum : 0;

        this.hasMaximum = maximum != null;
        this.maximum = hasMaximum ? maximum : 0;
    }

    @Override
    public Long getMin()
    {
        return hasMinimum ? minimum : null;
    }

    @Override
    public Long getMax()
    {
        return hasMaximum ? maximum : null;
    }

    @Override
    public long getRetainedSizeInBytes()
    {
        return INSTANCE_SIZE;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TimestampStatistics that = (TimestampStatistics) o;
        return Objects.equals(getMin(), that.getMin()) &&
                Objects.equals(getMax(), that.getMax());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getMin(), getMax());
    }

    @Override
    public String toString()
    {
        return toStringHelper(this)
                .add("min", getMin())
                .add("max", getMax())
                .toString();
    }

    @Override
    public void addHash(StatisticsHasher hasher)
    {
        hasher.putOptionalLong(hasMinimum, minimum)
                .putOptionalLong(hasMaximum, maximum);
    }
}
