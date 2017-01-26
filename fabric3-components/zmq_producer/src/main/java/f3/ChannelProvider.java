package f3;

import javax.xml.namespace.QName;
import java.util.Collections;
import java.util.List;

import org.fabric3.api.model.type.builder.ChannelBuilder;
import org.fabric3.api.model.type.builder.CompositeBuilder;

import org.fabric3.api.model.type.component.Composite;
import org.fabric3.api.annotation.model.Provides;

import org.fabric3.api.binding.zeromq.builder.ZeroMQBindingBuilder;
import org.fabric3.api.binding.zeromq.model.ZeroMQBinding;
 
public class ChannelProvider {

  private static final String PROVIDER_CHANNEL_ADDRESS = "192.168.99.100:5001";

  @Provides
  public static Composite createComposite() {
      QName name = new QName("urn:mycompany.com", "ChannelComposite");
      ChannelBuilder channelBuilder = ChannelBuilder.newBuilder("BuyChannel");
      CompositeBuilder compositeBuilder = CompositeBuilder.newBuilder(name);
      List<String> addresses = Collections.singletonList(PROVIDER_CHANNEL_ADDRESS);
      ZeroMQBinding binding = ZeroMQBindingBuilder.newBuilder().address(addresses).build();
      channelBuilder.binding(binding);
      compositeBuilder.channel(channelBuilder.build());
      return compositeBuilder.deployable().build();
  }

}