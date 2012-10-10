import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MainCli {

  public static void main(String[] args) throws InterruptedException {
    new AnnotationConfigApplicationContext(MainConfig.class, JmxConfig.class);

    while (true) {
      Thread.sleep(1000);
    }
  }

}
