package mk.ukimi.finiki;

import mk.ukimi.finiki.Sources.ApiData;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSink;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.PrintSinkFunction;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

public class Main {
    public static void main(String[] args) throws Exception {
        // 1. Create a stream execution environment
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // 2. Create a DataStream from the source
        DataStream<String> inputDataStream = env.addSource(new ApiData());

        // 3. Apply a map function to transform the data
        DataStream<String> transformedDataStream = inputDataStream.map(new MapFunction<String, String>() {
            @Override
            public String map(String value) throws Exception {
                // Modify the data here
                String modifiedValue = value.toUpperCase();
                return modifiedValue;
            }
        });

        // 4. Send the transformed data to a data sink (Flink dashboard)
        DataStreamSink<String> sink = transformedDataStream.addSink(createDashboardSink());
        sink.name("Dashboard Sink");

        // 5. Execute the job
        env.execute(Main.class.getName());
    }

    private static SinkFunction<String> createDashboardSink() {
        return new PrintSinkFunction<>(false);
    }
}
