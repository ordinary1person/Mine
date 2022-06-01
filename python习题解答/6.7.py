#求1000以内的‘相亲数’
def f(x):
      a=1
      b=x
      sum=0
      while a<b:
            if x%a==0:
                  sum=sum+a+b
            a+=1
            b=x/a
      if a==b and x%a==0:
            sum+=a
      return int(sum-x)
for i in range(1,1001):
      j=f(i)
      if i<j and i==f(j):
            print('1000以内的相亲数为：',i,j)