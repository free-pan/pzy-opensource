# pzy-dynamic-springi18n

## 作用

实现动态国际化资源动态读取

## 如何使用

* 实现 `org.pzy.opensource.i18n.manager.WinterI18nMessageReaderManager`, 并作为spring的Bean
* 将 `org.pzy.opensource.i18n.support.spring.WinterSessionLocaleResolver` 作为spring的bean
* 将 `org.pzy.opensource.i18n.support.spring.WinterDynamicMessageSource` 作为spring的bean
* 将`org.pzy.opensource.i18n.support.spring.WinterSessionLocaleResolver` 作为spring的bean