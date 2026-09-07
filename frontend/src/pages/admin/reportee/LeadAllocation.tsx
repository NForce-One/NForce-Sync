import { useState } from 'react';
import { AllocationTab } from '../../pm/ProjectsAllocation';
import { ReporteeScopePicker } from '../../../components/ReporteeScopePicker';

/**
 * Super Admin Reportee Views → Team Lead Views → Resource Allocation.
 *
 * Team Leads don't own allocations themselves (Project Managers do — see AllocationService),
 * so there is no existing "Team Lead allocation page" to reuse as-is. Allocation is fundamentally
 * project-scoped, so this reuses the exact same AllocationTab component and business logic the
 * PM Views' Resource Allocation page uses, narrowed via the backend's `teamLeadId` filter
 * (AllocationService.listAll) to just the projects a specific Team Lead leads (Project.pm) —
 * zero duplicated allocation logic.
 *
 * Requires picking a Team Lead first (mirrors the same pattern already used by the Team Lead
 * Views → Utilization reportee page): with no natural "combine every Team Lead's allocations"
 * view, an explicit selection avoids returning an unfiltered, unscoped-looking table.
 */
export default function ReporteeLeadAllocation() {
  const [teamLeadId, setTeamLeadId] = useState<number | null>(null);

  return (
    <div>
      <ReporteeScopePicker role="MANAGER" label="Team Lead" value={teamLeadId} onChange={setTeamLeadId} />
      <div style={{ marginBottom: 20 }}>
        <h1 style={{ fontFamily: '"Space Grotesk", sans-serif', fontSize: 22, fontWeight: 700, color: 'var(--txt)', margin: '0 0 4px', letterSpacing: '-0.01em' }}>
          Resource Allocation
        </h1>
        <p style={{ fontSize: 13, color: 'var(--txt-mut)', margin: 0 }}>
          {teamLeadId == null
            ? 'Select a Team Lead above to view resource allocations across their projects.'
            : "Allocations across the selected Team Lead's projects."}
        </p>
      </div>
      {teamLeadId != null && <AllocationTab readOnly teamLeadId={teamLeadId} />}
    </div>
  );
}
