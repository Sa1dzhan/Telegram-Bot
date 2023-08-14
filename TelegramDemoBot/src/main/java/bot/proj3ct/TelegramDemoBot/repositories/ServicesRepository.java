package bot.proj3ct.TelegramDemoBot.repositories;

import bot.proj3ct.TelegramDemoBot.entity.Services;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServicesRepository extends JpaRepository<Services, Long> {
    Services findByName(String selectedService);
}
