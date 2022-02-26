#从键盘上接受两个数并计算两个数的积(保留两位小数)(不支持循环输入)
number1=input('请输入第一个数：')
number2=input('请输入第二个数：')

result=float(number1) * float(number2)
print('两个数的积：%.2f' % result)