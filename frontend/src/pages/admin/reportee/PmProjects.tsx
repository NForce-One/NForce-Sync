import ProjectsAllocation from '../../pm/ProjectsAllocation';
import { ReporteeViewBanner } from '../../../components/ReporteeViewBanner';

/** Super Admin Reportee Views → Project Manager Views → Projects. Reuses the PM's own
 *  Projects & Allocation page (already org-wide for SUPERADMIN via GET /api/projects/all),
 *  read-only, deep-linked to its "Projects" tab. */
export default function ReporteePmProjects() {
  return (
    <div>
      <ReporteeViewBanner label="Project Manager Views — Projects" />
      <ProjectsAllocation initialTab="projects" readOnly />
    </div>
  );
}
