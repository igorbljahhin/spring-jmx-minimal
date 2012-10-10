import static java.lang.Integer.parseInt;

import java.io.IOException;
import java.rmi.registry.Registry;

import javax.inject.Inject;
import javax.management.JMException;
import javax.management.MBeanServer;
import javax.management.remote.JMXConnectorServer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jmx.support.ConnectorServerFactoryBean;
import org.springframework.jmx.support.MBeanServerFactoryBean;
import org.springframework.remoting.rmi.RmiRegistryFactoryBean;

@Configuration
public class JmxConfig {
  @Inject
  private Environment environment;

  @Bean(name = "jmxConnectorServer")
  public JMXConnectorServer getConnector() throws JMException, IOException {
    return getConnectorServerFactoryBean().getObject();
  }

  @Bean
  public ConnectorServerFactoryBean getConnectorServerFactoryBean() throws JMException, IOException {
    ConnectorServerFactoryBean factory = new ConnectorServerFactoryBean();
    factory.setObjectName("connector:name=rmi");
    factory.setServiceUrl("service:jmx:rmi:///jndi/rmi://localhost:" + getJmxPort() + "/jmxrmi");
    factory.setServer(getMBeanServer());
    factory.setThreaded(true);

    return factory;
  }

  protected int getJmxPort() {
    return parseInt(environment.getProperty("jmx.port", "1099"));
  }

  @Bean(name = "mbeanServer")
  public MBeanServer getMBeanServer() {
    return getMBeanServerFactoryBean().getObject();
  }

  @Bean
  public MBeanServerFactoryBean getMBeanServerFactoryBean() {
    MBeanServerFactoryBean factory = new MBeanServerFactoryBean();
    factory.setLocateExistingServerIfPossible(true);

    return factory;
  }

  @Bean(name = "registry")
  public Registry getRegistry() throws Exception {
    return getRmiRegistryFactoryBean().getObject();
  }

  @Bean
  public RmiRegistryFactoryBean getRmiRegistryFactoryBean() throws Exception {
    RmiRegistryFactoryBean factory = new RmiRegistryFactoryBean();
    factory.setPort(getJmxPort());
    factory.setAlwaysCreate(true);

    return factory;
  }
}
