package ai.smartcity.controller;

import ai.smartcity.model.IncidentReport;
import ai.smartcity.model.PatientReport;
import ai.smartcity.repository.IncidentReportRepository;
import ai.smartcity.repository.PatientReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "http://localhost:3000") // allow frontend
public class ReportController {

    private final IncidentReportRepository incidentRepo;
    private final PatientReportRepository patientRepo;

    @Autowired
    private SimpMessagingTemplate messagingTemplate; // ✅ used for WebSocket push

    public ReportController(IncidentReportRepository incidentRepo, PatientReportRepository patientRepo) {
        this.incidentRepo = incidentRepo;
        this.patientRepo = patientRepo;
    }

    // ✅ Create Incident Report + broadcast alert
    @PostMapping("/incident")
    public ResponseEntity<IncidentReport> createIncident(Authentication auth, @RequestBody IncidentReport report) {
        String email = (String) auth.getPrincipal();
        report.setUserEmail(email);
        report.setCreatedAt(Instant.now());
        IncidentReport saved = incidentRepo.save(report);

        // 🔔 broadcast this incident to all connected WebSocket clients
        messagingTemplate.convertAndSend("/topic/alerts", saved);

        return ResponseEntity.ok(saved);
    }

    // ✅ Get My Incident Reports
    @GetMapping("/incident/my")
    public ResponseEntity<List<IncidentReport>> getMyIncidents(Authentication auth) {
        String email = (String) auth.getPrincipal();
        List<IncidentReport> reports = incidentRepo.findByUserEmailOrderByCreatedAtDesc(email);
        return ResponseEntity.ok(reports);
    }

    // ✅ Create Patient Report
    @PostMapping("/patient")
    public ResponseEntity<PatientReport> createPatient(Authentication auth, @RequestBody PatientReport report) {
        String email = (String) auth.getPrincipal();
        report.setUserEmail(email);
        report.setCreatedAt(Instant.now());
        PatientReport saved = patientRepo.save(report);
        return ResponseEntity.ok(saved);
    }

    // ✅ Get My Patient Reports
    @GetMapping("/patient/my")
    public ResponseEntity<List<PatientReport>> getMyPatients(Authentication auth) {
        String email = (String) auth.getPrincipal();
        List<PatientReport> patients = patientRepo.findByUserEmailOrderByCreatedAtDesc(email);
        return ResponseEntity.ok(patients);
    }

    // ✅ Get all incidents (city-wide)
    @GetMapping("/incident")
    public ResponseEntity<List<IncidentReport>> getAllIncidents() {
        List<IncidentReport> all = incidentRepo.findAllByOrderByCreatedAtDesc();
        return ResponseEntity.ok(all);
    }

    // ✅ Get all patients (city-wide)
    @GetMapping("/patient")
    public ResponseEntity<List<PatientReport>> getAllPatients() {
        List<PatientReport> all = patientRepo.findAllByOrderByCreatedAtDesc();
        return ResponseEntity.ok(all);
    }
}
