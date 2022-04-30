a = [1,2,3,8,9]
b=map(lambda x:x*x,a) #map函数返回一个迭代器
print(list(b))
b=[x**2 for x in a] #列表解析
print(b)

b=filter(lambda x:x%2==0,a) #filter函数返回一个迭代器
print(list(b))
b=[x for x in a if x%2==0] #列表解析
print(b)

#列表推导式比高级函数更简单