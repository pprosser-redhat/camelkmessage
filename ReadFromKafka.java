// camel-k: language=java
// camel-k: property=kafka.bootstrap.address=my-cluster-kafka-bootstrap.amq-streams.svc.cluster.local:9092
import org.apache.camel.builder.RouteBuilder;

public class ReadFromKafka extends RouteBuilder {
  @Override
  public void configure() throws Exception {

      // Write your routes here, for example:
      from("kafka:my-topic?brokers={{kafka.bootstrap.address}}&groupId=myreader&autoOffsetReset=earliest")
        .routeId("KafkaListening")
        .to("log:info");

  }
}
