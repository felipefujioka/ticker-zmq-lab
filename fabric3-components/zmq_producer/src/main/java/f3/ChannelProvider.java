package f3;

import javax.xml.namespace.QName;
import java.util.Collections;
import java.util.List;

import org.fabric3.api.model.type.builder.ChannelBuilder;
import org.fabric3.api.model.type.builder.CompositeBuilder;

import org.fabric3.api.model.type.component.Composite;
import org.fabric3.api.annotation.model.Provides;
import org.fabric3.api.model.type.component.Channel;

import org.fabric3.api.binding.zeromq.builder.ZeroMQBindingBuilder;
import org.fabric3.api.binding.zeromq.model.ZeroMQBinding;
 
public class ChannelProvider {

  private static final String PROVIDER_CHANNEL_ADDRESS = System.getenv("PRODUCER_ADDR");

  @Provides
  public static Composite createComposite() {
      QName name = new QName("urn:mycompany.com", "ChannelComposite");
      CompositeBuilder compositeBuilder = CompositeBuilder.newBuilder(name);
      compositeBuilder.channel(createZMQChannel());
      return compositeBuilder.deployable().build();
  }

  public static Channel createZMQChannel() {
      ChannelBuilder channelBuilder = ChannelBuilder.newBuilder("TickChannel");
      List<String> addresses = Collections.singletonList(PROVIDER_CHANNEL_ADDRESS);
      ZeroMQBinding binding = ZeroMQBindingBuilder.newBuilder().address(addresses).build();
      channelBuilder.binding(binding);
      return channelBuilder.build();
  }

  public static Channel createWebsocketChannel() {
      ChannelBuilder channelBuilder = ChannelBuilder.newBuilder("RTChannel");



      return channelBuilder.build();
  }

}