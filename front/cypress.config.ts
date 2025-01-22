import {defineConfig} from 'cypress'

export default defineConfig({
  videosFolder: 'cypress/videos',
  screenshotsFolder: 'cypress/screenshots',
  fixturesFolder: 'cypress/fixtures',
  video: false,
  e2e: {
    // We've imported your old cypress plugins here.
    // You may want to clean this up later by importing these.
    setupNodeEvents(on, config) {
      return require('./cypress/plugins/index.ts').default(on, config)
    },
    baseUrl: 'http://localhost:4200',
  },
  env: {
    adminEmail: 'yoga.admin@studio.com',
    adminPassword: 'test!1234',
    userEmail: 'yoga@studio.com',
    userPassword: 'test!1234',
  }
})
