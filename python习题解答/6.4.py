#输入直角三角形斜边c，输出直角三角形直角边a，b
c=int(input("请输入一个正整数："))
flag=1
for a in range(1,c):
    for b in range(a,c):
        if a**2+b**2==c**2:
            print("a=",a,"b=",b)
            flag=0
if flag:
    print('不存在这样的直角三角形')