import { createI18n } from 'vue-i18n';

import en from './locales/en.json';
import cs from './locales/cs.json';

const messages = {
  en,
  cs,
};

const i18n = createI18n({
  locale: 'en',
  fallbackLocale: 'en',
  messages,
});

export type SupportedLocales = keyof typeof messages;

export default i18n;
