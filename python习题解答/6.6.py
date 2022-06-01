#穷举法寻找一个正整数的所有约数
a=int(input("请输入一个正整数："))
for i in range(1,a+1):
    if a%i==0:
        print(i)