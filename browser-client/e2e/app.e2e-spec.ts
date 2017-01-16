import { BrowserClientPage } from './app.po';

describe('browser-client App', function() {
  let page: BrowserClientPage;

  beforeEach(() => {
    page = new BrowserClientPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
