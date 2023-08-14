package bot.proj3ct.TelegramDemoBot.repositories;

import bot.proj3ct.TelegramDemoBot.entity.DoctorServices;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorServicesRepository extends JpaRepository<DoctorServices, Long> {
}
