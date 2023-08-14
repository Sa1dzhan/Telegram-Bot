package bot.proj3ct.TelegramDemoBot.repositories;

import bot.proj3ct.TelegramDemoBot.entity.Doctors;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorsRepository extends JpaRepository<Doctors, Long> {
}
