package ai.smartcity.repository;

import ai.smartcity.model.PatientReport;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PatientReportRepository extends JpaRepository<PatientReport, Long> {
    List<PatientReport> findByUserEmailOrderByCreatedAtDesc(String email);
    List<PatientReport> findAllByOrderByCreatedAtDesc(); // ✅ added for global alert view

}
