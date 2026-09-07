import ReportsDashboard from '../../pm/ReportsDashboard';
import { ReporteeViewBanner } from '../../../components/ReporteeViewBanner';

/** Super Admin Reportee Views → Project Manager Views → Reports. Reuses the PM Reports page,
 *  deep-linked to its "Missing EOD" tab (MissingEodReportService already resolves SUPERADMIN to
 *  org-wide data) — the EOD-by-employee tab is covered separately by the EOD child item. */
export default function ReporteePmReports() {
  return (
    <div>
      <ReporteeViewBanner label="Project Manager Views — Reports" />
      <ReportsDashboard initialTab="missing" />
    </div>
  );
}
