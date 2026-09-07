import LeadReportsDashboard from '../../lead/ReportsDashboard';
import { ReporteeViewBanner } from '../../../components/ReporteeViewBanner';

/** Super Admin Reportee Views → Team Lead Views → Reports. Reuses the Team Lead Reports page,
 *  deep-linked to its "Missing EOD" tab (TeamMissingEodReportService already resolves
 *  SUPERADMIN to org-wide data) — the EOD-by-employee tab is covered by the EOD child item. */
export default function ReporteeLeadReports() {
  return (
    <div>
      <ReporteeViewBanner label="Team Lead Views — Reports" />
      <LeadReportsDashboard initialTab="missing" />
    </div>
  );
}
