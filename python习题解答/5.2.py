#函数计算组合数
def factorial(n):
    for i in range(1,n+1):
        n *= i
    return n

def combination(n,m):
    return factorial(n)//(factorial(m)*factorial(n-m))

print('注意:组合数n>=m')
x,y = int(input('请输入组合数n:')),int(input('请输入组合数m:'))
print('组合数：',combination(x,y))