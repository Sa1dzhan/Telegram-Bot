package bot.proj3ct.TelegramDemoBot.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "doctors_services")
public class DoctorServices {
    @Id
    private Long id;
    @ManyToOne
    private Doctors doctors;

    @ManyToOne
    private Services services;

    // Getters
    public Doctors getDoctor() {
        return doctors;
    }
    public Services getService() {
        return services;
    }

    public Long getId() {
        return id;
    }
}
