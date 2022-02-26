#输入摄氏度输出华氏度
Celsius_degree = float(input('请输入摄氏度(℃ )：'))
print('\n*************************')
print('摄氏度：{} ℃'.format(round(Celsius_degree,1))) 
Fahrenheit_degree = Celsius_degree * 1.8 + 32
print('华氏度：%.1f ℉' % Fahrenheit_degree)