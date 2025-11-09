package ai.smartcity.repository;

import ai.smartcity.model.IncidentReport;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IncidentReportRepository extends JpaRepository<IncidentReport, Long> {
    List<IncidentReport> findByUserEmailOrderByCreatedAtDesc(String email);
    List<IncidentReport> findAllByOrderByCreatedAtDesc(); // ✅ added for global alert view

}
