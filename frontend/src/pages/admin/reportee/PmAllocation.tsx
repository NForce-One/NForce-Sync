import ProjectsAllocation from '../../pm/ProjectsAllocation';
import { ReporteeViewBanner } from '../../../components/ReporteeViewBanner';

/** Super Admin Reportee Views → Project Manager Views → Resource Allocation. Reuses the PM's
 *  own Allocation tab (already org-wide for SUPERADMIN via GET /api/allocations), read-only. */
export default function ReporteePmAllocation() {
  return (
    <div>
      <ReporteeViewBanner label="Project Manager Views — Resource Allocation" />
      <ProjectsAllocation initialTab="allocation" readOnly />
    </div>
  );
}
