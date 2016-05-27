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
package io.airlift.slice;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.VerboseMode;

import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.SECONDS)
@Fork(1)
@Warmup(iterations = 5, time = 500, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 10, time = 500, timeUnit = TimeUnit.MILLISECONDS)
public class BenchmarkSliceInputRead
{
    @Benchmark
    public int rawArrayAccess(BenchmarkData data, ByteCounter counter)
    {
        counter.add(data.getSlice().length());
        int value = 0;
        for (int i = 0; i < data.getSlice().length(); i++) {
            value ^= data.getBytes()[i];
        }
        return value;
    }

    @Benchmark
    public int sliceChecked(BenchmarkData data, ByteCounter counter)
    {
        counter.add(data.getSlice().length());
        int value = 0;
        for (int i = 0; i < data.getSlice().length(); i++) {
            value ^= data.getSlice().getByte(i);
        }
        return value;
    }

    @Benchmark
    public int sliceUnchecked(BenchmarkData data, ByteCounter counter)
    {
        counter.add(data.getSlice().length());
        int value = 0;
        for (int i = 0; i < data.getSlice().length(); i++) {
            value ^= data.getSlice().getByteUnchecked(i);
        }
        return value;
    }

    @Benchmark
    public int basicSliceInput(BenchmarkData data, ByteCounter counter)
    {
        counter.add(data.getSlice().length());
        BasicSliceInput input = data.getSlice().getInput();
        int value = 0;
        for (int i = 0; i < data.getSlice().length(); i++) {
            value ^= ((byte) input.read());
        }
        return value;
    }

    public static void main(String[] args)
            throws RunnerException
    {
        Options options = new OptionsBuilder()
                .verbosity(VerboseMode.NORMAL)
                .include(".*" + BenchmarkSliceInputRead.class.getSimpleName() + ".*")
                .build();

        new Runner(options).run();
    }
}
