import { describe, it, expect } from 'vitest';
import {
  validateAttachmentFile, MAX_ATTACHMENT_BYTES, MAX_ATTACHMENTS_PER_TASK, buildAttachmentViewerHtml,
  buildAttachmentLoadingHtml, buildAttachmentErrorHtml,
} from './eodAttachments';

describe('validateAttachmentFile', () => {
  it('accepts a supported type within size and count limits', () => {
    const err = validateAttachmentFile({ name: 'evidence.png', type: 'image/png', size: 1024 }, 0, 5);
    expect(err).toBeNull();
  });

  it('rejects an unsupported file type and names the supported ones', () => {
    const err = validateAttachmentFile({ name: 'malware.exe', type: 'application/x-msdownload', size: 100 }, 0, 5);
    expect(err).not.toBeNull();
    expect(err).toContain('not a supported file type');
    expect(err).toContain('PDF');
  });

  it('rejects a file over the size limit', () => {
    const err = validateAttachmentFile(
      { name: 'huge.pdf', type: 'application/pdf', size: MAX_ATTACHMENT_BYTES + 1 }, 0, 5,
    );
    expect(err).not.toBeNull();
    expect(err).toContain('exceeds');
  });

  it('accepts a file exactly at the size limit', () => {
    const err = validateAttachmentFile(
      { name: 'edge.pdf', type: 'application/pdf', size: MAX_ATTACHMENT_BYTES }, 0, 5,
    );
    expect(err).toBeNull();
  });

  it('rejects once the per-scope count cap is reached', () => {
    const err = validateAttachmentFile(
      { name: 'one-more.png', type: 'image/png', size: 100 }, MAX_ATTACHMENTS_PER_TASK, MAX_ATTACHMENTS_PER_TASK,
    );
    expect(err).not.toBeNull();
    expect(err).toContain('up to');
  });

  it('type check runs before size/count, so an unsupported type is reported even at the count cap', () => {
    const err = validateAttachmentFile(
      { name: 'bad.exe', type: 'application/x-msdownload', size: 100 }, MAX_ATTACHMENTS_PER_TASK, MAX_ATTACHMENTS_PER_TASK,
    );
    expect(err).toContain('not a supported file type');
  });
});

describe('buildAttachmentViewerHtml', () => {
  it('sets the tab title to the original filename', () => {
    const html = buildAttachmentViewerHtml('evidence.png', 'image/png', 'blob:http://x/1');
    expect(html).toContain('<title>evidence.png</title>');
  });

  it('previews an image inline via <img>', () => {
    const html = buildAttachmentViewerHtml('evidence.png', 'image/png', 'blob:http://x/1');
    expect(html).toContain('<img src="blob:http://x/1"');
  });

  it('previews a PDF inline via <embed>', () => {
    const html = buildAttachmentViewerHtml('report.pdf', 'application/pdf', 'blob:http://x/2');
    expect(html).toContain('<embed src="blob:http://x/2" type="application/pdf" />');
  });

  it('offers a named download link for a type no browser renders inline', () => {
    const html = buildAttachmentViewerHtml('budget.xlsx', 'application/vnd.ms-excel', 'blob:http://x/3');
    expect(html).toContain('<a href="blob:http://x/3" download="budget.xlsx">');
  });

  it('escapes a filename containing HTML-significant characters', () => {
    const html = buildAttachmentViewerHtml('<script>.png', 'image/png', 'blob:http://x/4');
    expect(html).not.toContain('<script>.png');
    expect(html).toContain('&lt;script&gt;.png');
  });
});

describe('buildAttachmentLoadingHtml', () => {
  it('sets the tab title to the original filename immediately, before any content has loaded', () => {
    const html = buildAttachmentLoadingHtml('evidence.png');
    expect(html).toContain('<title>evidence.png</title>');
  });

  it('escapes a filename containing HTML-significant characters', () => {
    const html = buildAttachmentLoadingHtml('<script>.png');
    expect(html).not.toContain('<script>.png');
    expect(html).toContain('&lt;script&gt;.png');
  });
});

describe('buildAttachmentErrorHtml', () => {
  it('keeps the tab title as the original filename even in the error state', () => {
    const html = buildAttachmentErrorHtml('evidence.png', 'Network error');
    expect(html).toContain('<title>evidence.png</title>');
  });

  it('shows the error message', () => {
    const html = buildAttachmentErrorHtml('evidence.png', 'Network error');
    expect(html).toContain('Network error');
  });

  it('escapes both the filename and the message', () => {
    const html = buildAttachmentErrorHtml('<b>.png', '<script>alert(1)</script>');
    expect(html).not.toContain('<b>.png');
    expect(html).not.toContain('<script>alert(1)</script>');
    expect(html).toContain('&lt;b&gt;.png');
    expect(html).toContain('&lt;script&gt;alert(1)&lt;/script&gt;');
  });
});
