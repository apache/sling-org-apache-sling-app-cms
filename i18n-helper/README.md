# i18n Helper

Automatically translates the i18n Dictionaries using LibreTranslate and updates the dictionaries in Sling CMS.

## Dependencies

- NodeJS
- Docker
- LibreTranslate

## Use

1. Start LibreTranslate: `docker run -ti --rm -p 5000:5000 libretranslate/libretranslate`
2. Run `npm install`
3. Run `node .`
