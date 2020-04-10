### 修改conf目录下jdbc配置信息
```
jdbc.url=jdbc:mysql://localhost:3306/mybatis?useUnicode=true&characterEncoding=UTF-8&useSSL=false
jdbc.username=mybatis
jdbc.password=mybatis
```

### 修改输出配置
```
output.path=
output.pkg.xml=mapping
output.pkg.model=org.shersfy.model
output.pkg.mapper=org.shersfy.model.mapper
```

## 注意事项
- 需要output.pkg.root.class指定的BaseEntity.java和conf目录下的BaseEntity.java保持一致，请勿修改