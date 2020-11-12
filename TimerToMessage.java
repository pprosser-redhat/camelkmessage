// camel-k: language=java property-file=application.properties
// camel-k: dependency=camel-amqp      
// camel-k: e=BROKER_URL1=my-broker-amqp-1-svc.amq-broker.svc.cluster.local:5672
// camel-k: e=BROKER_URL2=my-broker-amqp-0-svc.amq-broker.svc.cluster.local:5672
// camel-k: e=BROKER_URL3=my-broker-hdls-svc.amq-broker.svc.cluster.local:5672
import org.apache.camel.BindToRegistry;
import org.apache.camel.builder.RouteBuilder;

public class TimerToMessage extends RouteBuilder {

  static int counter;

  @BindToRegistry
    public javax.jms.ConnectionFactory connectionFactory() {
      
      return new org.apache.qpid.jms.JmsConnectionFactory("failover:amqp://" + java.lang.System.getenv("BROKER_URL3"));

     // return new org.apache.qpid.jms.JmsConnectionFactory("failover:amqp://" + java.lang.System.getenv("BROKER_URL1") + ",amqp://" + java.lang.System.getenv("BROKER_URL2"));
      }


  @Override
  public void configure() throws Exception {
    
      // Write your routes here, for example:
      from("timer:java?period=1000")
        .routeId("WriteAMQMessage")
        .process(exchange -> {
          counter++;
          exchange.getMessage().setHeader("counter", counter);
        })
        .setBody()
          .simple("Hello Camel K from ${routeId} ${header.counter}")
        .log("The message is ${body}")
        .to("amqp:queue:hellomessage?exchangePattern=InOnly");

  }
}
