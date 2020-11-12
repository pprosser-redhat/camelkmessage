// camel-k: language=java
// camel-k: e=BROKER_URL1=my-broker-hdls-svc.amq-broker.svc.cluster.local:5672
// camel-k: property=kafka.bootstrap.address=my-cluster-kafka-bootstrap.amq-streams.svc.cluster.local:9092
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.BindToRegistry;
public class ReadMessageAndSendToKafka extends RouteBuilder {

  @BindToRegistry
    public javax.jms.ConnectionFactory connectionFactory() {

      return new org.apache.qpid.jms.JmsConnectionFactory("failover:amqp://" + java.lang.System.getenv("BROKER_URL1"));
      }

  @Override
  public void configure() throws Exception {

      // Write your routes here, for example:
      from("amqp:queue:hellomessage?exchangePattern=InOnly")
        .routeId("processAMQMessage")
        .to("log:info")
        .to("kafka:my-topic?brokers={{kafka.bootstrap.address}}");

  }
}
