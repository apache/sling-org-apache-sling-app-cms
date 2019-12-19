module.exports = {
  env: {
    browser: true,
    es6: true,
  },
  extends: [
    'airbnb-base',
  ],
  globals: {
    Atomics: 'readonly',
    Handlebars: 'writable',
    rava: 'writable',
    SharedArrayBuffer: 'readonly',
    Sling: 'writable',
  },
  parserOptions: {
    ecmaVersion: 2018,
  },
  rules: {
  },
};
