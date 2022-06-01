#三位数的各项之和等于各项之积
for i in range(100,1000):
    a = i // 100
    b = i // 10 % 10
    c = i % 10
    if a+b+c == a*b*c:
        print(i)