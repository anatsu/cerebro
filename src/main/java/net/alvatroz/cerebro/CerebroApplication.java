package net.alvatroz.cerebro;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import lombok.extern.slf4j.Slf4j;
import net.alvatroz.cerebro.swing.CerebroUI;
import org.apache.commons.lang3.stream.Streams;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@Slf4j
public class CerebroApplication {

    public static void main(String[] args) {

        Map<String, String> mapaLoockAndFeel = new HashMap<>();
        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            log.info(" nombre: {} => {}", info.getName(), info.getClassName());
            mapaLoockAndFeel.put(info.getName(), info.getClassName());

        }

        Optional<String> lookAndFeel = Streams.of(List.of("GTK+", "Nimbus")).map(laf -> mapaLoockAndFeel.get(laf)).filter(laf -> laf != null).findAny();

        if (lookAndFeel.isPresent()) {

            try {
                UIManager.setLookAndFeel(lookAndFeel.get());

            } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                log.error("Fallo la asignación del lock and feel", e);
            }
        }

        ConfigurableApplicationContext appContext = SpringApplication.run(CerebroApplication.class, args);

        SwingUtilities.invokeLater(() -> {
            CerebroUI ventana = appContext.getBean(CerebroUI.class);
            ventana.setVisible(true);
        });
    }

}
