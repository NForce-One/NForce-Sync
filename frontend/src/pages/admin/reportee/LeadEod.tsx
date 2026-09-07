import LeadReportsDashboard from '../../lead/ReportsDashboard';
import { ReporteeViewBanner } from '../../../components/ReporteeViewBanner';

/** Super Admin Reportee Views → Team Lead Views → EOD. Reuses the Team Lead Reports page's
 *  "EOD by employee" tab (TeamEodByEmployeeReportService already resolves SUPERADMIN to
 *  org-wide data) — the existing per-employee/day EOD listing, not a new page. */
export default function ReporteeLeadEod() {
  return (
    <div>
      <ReporteeViewBanner label="Team Lead Views — EOD" />
      <LeadReportsDashboard initialTab="eod" />
    </div>
  );
}
