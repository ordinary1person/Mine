#函数计算阶乘
def factorial(n):
    for i in range(1,n+1):
        n *= i
    return n

sum=0
for i in range(1,11):
    sum += factorial(i)
print(sum)