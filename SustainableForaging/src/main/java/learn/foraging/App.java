package learn.foraging;

import learn.foraging.data.ForageFileRepository;
import learn.foraging.data.ForagerFileRepository;
import learn.foraging.data.ItemFileRepository;
import learn.foraging.domain.ForageService;
import learn.foraging.domain.ForagerService;
import learn.foraging.domain.ItemService;
import learn.foraging.ui.ConsoleIO;
import learn.foraging.ui.Controller;
import learn.foraging.ui.View;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@ComponentScan
@PropertySource("classpath:data.properties")
public class App {
    public static void main(String[] args) {
        // 1. We pass the App.class, this class, as a constructor argument.
        ApplicationContext context = new AnnotationConfigApplicationContext(App.class);

        // 3. The context works the same as the XML context.
        Controller controller = context.getBean(Controller.class);
        // Run the app!
        controller.run();
    }
}
