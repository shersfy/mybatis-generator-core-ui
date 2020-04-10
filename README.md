# mybatis-generator-core-ui
## build
```
mvn clean package -DskipTests
```

## 修改源码
由于打jar包后，路径访问有问题，导致InputStream is = null;
org.mybatis.generator.config.xml.ParserEntityResolver

修改前：org/mybatis/generator/config/xml/

```
	public InputSource resolveEntity(String publicId, String systemId)
            throws SAXException, IOException {
        if (XmlConstants.IBATOR_CONFIG_PUBLIC_ID.equalsIgnoreCase(publicId)) {
            InputStream is = getClass().getClassLoader().getResourceAsStream(
                    "org/mybatis/generator/config/xml/ibator-config_1_0.dtd"); //$NON-NLS-1$
            InputSource ins = new InputSource(is);

            return ins;
        } else if (XmlConstants.MYBATIS_GENERATOR_CONFIG_PUBLIC_ID
                .equalsIgnoreCase(publicId)) {
            InputStream is = getClass()
                    .getClassLoader()
                    .getResourceAsStream(
                            "org/mybatis/generator/config/xml/mybatis-generator-config_1_0.dtd"); //$NON-NLS-1$
            InputSource ins = new InputSource(is);

            return ins;
        } else {
            return null;
        }
    }
```

修改后：修改文件路径，把ibator-config_1_0.dtd和mybatis-generator-config_1_0.dtd两个文件放到conf文件夹下

```
    public InputSource resolveEntity(String publicId, String systemId)
            throws SAXException, IOException {
        if (XmlConstants.IBATOR_CONFIG_PUBLIC_ID.equalsIgnoreCase(publicId)) {
            InputStream is = getClass().getClassLoader().getResourceAsStream(
                    "ibator-config_1_0.dtd"); //$NON-NLS-1$
            InputSource ins = new InputSource(is);

            return ins;
        } else if (XmlConstants.MYBATIS_GENERATOR_CONFIG_PUBLIC_ID
                .equalsIgnoreCase(publicId)) {
            InputStream is = getClass()
                    .getClassLoader()
                    .getResourceAsStream(
                            "mybatis-generator-config_1_0.dtd"); //$NON-NLS-1$
            InputSource ins = new InputSource(is);

            return ins;
        } else {
            return null;
        }
    }
```

修改messages
org.mybatis.generator.internal.util.messages
修改前：

```
private static final String BUNDLE_NAME = "org.mybatis.generator.internal.util.messages.messages"; //$NON-NLS-1$
```

修改后：拷贝messages.properties文件到conf目录下

```
private static final String BUNDLE_NAME = "messages"; //$NON-NLS-1$
```