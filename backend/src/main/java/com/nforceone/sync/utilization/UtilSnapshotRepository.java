package com.nforceone.sync.utilization;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UtilSnapshotRepository extends JpaRepository<UtilSnapshot, Long> {

    Optional<UtilSnapshot> findByEmployeeIdAndSnapshotDate(Long employeeId, LocalDate snapshotDate);

    List<UtilSnapshot> findByEmployeeIdAndSnapshotDateBetweenOrderBySnapshotDateAsc(
            Long employeeId, LocalDate from, LocalDate to);

    @Query("SELECT s FROM UtilSnapshot s WHERE s.snapshotDate = :date " +
           "AND s.employeeId IN (SELECT u.id FROM AppUser u WHERE u.manager.id = :managerId)")
    List<UtilSnapshot> findByManagerIdAndDate(@Param("managerId") Long managerId,
                                               @Param("date") LocalDate date);

    // Batch fetch — eliminates N per-member queries in getForTeam()
    @Query("SELECT s FROM UtilSnapshot s WHERE s.snapshotDate = :date AND s.employeeId IN :employeeIds")
    List<UtilSnapshot> findByEmployeeIdInAndSnapshotDate(@Param("employeeIds") List<Long> employeeIds,
                                                          @Param("date") LocalDate date);

    // Range variant — backs resolveUtilizationPctForEmployees, one query for a whole team over
    // a whole date window instead of one query per (employee, day).
    @Query("SELECT s FROM UtilSnapshot s WHERE s.snapshotDate BETWEEN :from AND :to AND s.employeeId IN :employeeIds")
    List<UtilSnapshot> findByEmployeeIdInAndSnapshotDateBetween(@Param("employeeIds") List<Long> employeeIds,
                                                                 @Param("from") LocalDate from,
                                                                 @Param("to") LocalDate to);

    // ── Super Admin Executive Dashboard aggregates ──────────────────────────────
    // These read only PERSISTED snapshot rows (written by UtilizationService.computeSnapshot on
    // EOD approval) — a day nobody ever submitted/approved an EOD for contributes no row, so it
    // is correctly excluded from these totals rather than fabricated as 0. Reuses the exact same
    // persisted values UtilizationCalculator already computed; no new formula.

    /** [SUM(approvedProductiveHours), SUM(availableHours)] — one row, nulls if no snapshot exists. */
    @Query("SELECT SUM(s.approvedProductiveHours), SUM(s.availableHours) FROM UtilSnapshot s " +
           "WHERE s.employeeId IN :employeeIds AND s.snapshotDate BETWEEN :from AND :to")
    List<Object[]> sumHoursInRange(@Param("employeeIds") List<Long> employeeIds,
                                    @Param("from") LocalDate from,
                                    @Param("to") LocalDate to);

    /** [employeeId, AVG(utilizationPct)] per employee over the range — for top/bottom ranking and thresholds. */
    @Query("SELECT s.employeeId, AVG(s.utilizationPct) FROM UtilSnapshot s " +
           "WHERE s.employeeId IN :employeeIds AND s.snapshotDate BETWEEN :from AND :to " +
           "AND s.utilizationPct IS NOT NULL GROUP BY s.employeeId")
    List<Object[]> avgUtilizationByEmployeeInRange(@Param("employeeIds") List<Long> employeeIds,
                                                    @Param("from") LocalDate from,
                                                    @Param("to") LocalDate to);

    /** [snapshotDate, AVG(utilizationPct)] per day over the range — for the org utilization trend chart. */
    @Query("SELECT s.snapshotDate, AVG(s.utilizationPct) FROM UtilSnapshot s " +
           "WHERE s.employeeId IN :employeeIds AND s.snapshotDate BETWEEN :from AND :to " +
           "AND s.utilizationPct IS NOT NULL GROUP BY s.snapshotDate ORDER BY s.snapshotDate ASC")
    List<Object[]> avgUtilizationByDateInRange(@Param("employeeIds") List<Long> employeeIds,
                                                @Param("from") LocalDate from,
                                                @Param("to") LocalDate to);
}
