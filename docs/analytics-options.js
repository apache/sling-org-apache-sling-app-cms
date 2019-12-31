const access = require('./utils/access.js');
const request = require('request-promise-native');

/**
 * Gets the list of Analytics companies
 *
 * @param {string} apiKey the api key (clientId)
 * @param {string} imsOrgId the IMS Org Id
 * @param {string} apiToken the api Token (JWT)
 */
async function getCompanies(apiKey, imsOrgId, apiToken) {
  console.log('Getting companies for ' + apiKey);
  const response = await request.get({
    url: 'https://analytics.adobe.io/discovery/me',
    headers: {
      'X-Api-Key': apiKey,
      'Authorization': 'Bearer ' + apiToken,
    },
  });
  const companies = [];
  JSON.parse(response).imsOrgs.forEach((org) => {
    if (org.imsOrgId === imsOrgId) {
      org.companies.forEach((c) => {
        companies.push({
          value: c.globalCompanyId,
          text: c.companyName,
        });
      });
    }
  });
  return companies;
}

/**
 * Gets the list of Analytics report suites
 *
 * @param {string} apiToken the api Token (JWT)
 */
async function getReportSuites(apiToken) {
  console.log('Getting report suites...');
  const response = await request.get({
    url: 'https://api.omniture.com/admin/1.4/rest/?method=Company.GetReportSuites',
    headers: {
      'Authorization': 'Bearer ' + apiToken,
    },
  });
  const reportSuites = [];
  JSON.parse(response).report_suites.forEach((rs) => {
    reportSuites.push({
      value: rs.rsid,
      text: rs.site_title,
    });
  });
  return reportSuites;
}

/**
 * Gets / sets the organization
 *
 * @param {map} params the parameters to this method
 */
async function main(params) {
  try {
    console.log('Retrieving user access...');
    const creds = await access.validateAccess(params);
    console.log('Loaded credentials: ' + JSON.stringify(creds, false, 2));
    if (!creds.member) {
      console.log('User is not member');
      return Promise.reject(new Error({
        status: 403,
        message: 'Insufficient permissions',
      }));
    }

    const companies = await getCompanies(creds.org.apiKey, params.imsOrgId,
        creds.apiToken);
    const reportSuites = await getReportSuites(creds.apiToken);

    return {
      status: 200,
      data: {
        companies: companies,
        reportSuites: reportSuites,
      },
    };
  } catch (e) {
    console.error('Failed to retrieve analytics options: ' + e, e.stack);
    return Promise.reject(new Error({
      status: 500,
      message: 'Failed to retrieve analytics options: ' + e,
    }));
  }
}
global.main = main;
