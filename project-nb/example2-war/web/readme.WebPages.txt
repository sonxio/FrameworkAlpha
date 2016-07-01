资源按照JSF的管理放在RESOURCES目录中.
此外各个模块放在单独的目录中.

JSF TEMPLATE应放在/common中.但是有一个例外/template.xhtml,这是PERSISTANCE中从ENTITY CLASS自动生成的JSF页面模板,用于测试, 或从自动生成的页面里抄代码, 实际生产用的TEMPLATE都应放在common中.

/resources/css/jsfcrud.css 也是代码生成的仅供测试的CSS,正式使用的CSS不应放在这里.