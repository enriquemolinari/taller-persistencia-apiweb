package unrn.main;

@SpringBootApplication
@ComponentScan(basePackages = {"unrn.*"})
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
