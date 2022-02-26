#从键盘输入身高和体重，计算BIM并判断是否健康(不支持循环输入)
height = input('请输入你的身高(m)：')
weight = input('请输入你的体重(kg)：')

BIM = float(weight) / (float(height) ** 2)
print('标准BIM；18.5~23.9')
print('BIM:',round(BIM,2))

if  18.5 <= BIM <= 23.9 :
    print('健康状态：健 康')
else :
    print('健康状态：不健康')