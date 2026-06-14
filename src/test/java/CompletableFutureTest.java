import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author tanyaofei
 * @since 2024/8/13
 **/
public class CompletableFutureTest {

    public static void main(String[] args) throws InterruptedException {
        var monitor = CompletableFuture.delayedExecutor(5, TimeUnit.SECONDS);
        CompletableFuture.runAsync(() -> {
            System.out.println("1");
        }, monitor);


        Thread.sleep(500000);
    }
}
