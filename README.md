

#### 注解简化Redis操作

##### 名称作用

| 名称            | 作用              |
| --------------- | ----------------- |
| RedisCacheable  | 存储数据 获取数据 |
| RedisCacheEvict | 移除数据          |

##### 使用方式 RedisCacheable

- 根据prefix前缀和suffix后缀从缓存中查找并存储

```java
@RedisCacheable(prefix = USER_KEY, suffix = "#id")
@Override
public User getUser(Long id) {
	return getById(id);
}
```

- 根据prefix前缀和suffix后缀从缓存中查找并指定时间存储

```java
@RedisCacheable(prefix = USER_KEY, suffix = "#id",timeout = 10,unit = TimeUnit.MINUTES)
@Override
public User getUser(Long id) {
	return getById(id);
}
```

**使用方式 RedisCacheEvict**

- 根据prefix前缀和suffix后缀删除指定的key

```java
@RedisCacheEvict(prefix = USER_KEY, suffix = "#user.id")
@Override
public Boolean updateUser(User user) {
	return updateById(user);
}
```

